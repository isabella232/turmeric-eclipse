/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.doc.confluence;

import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.net.MalformedURLException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.xml.rpc.ServiceException;

import com.atlassian.confluence.rpc.AuthenticationFailedException;
import com.atlassian.confluence.rpc.RemoteException;
import com.atlassian.confluence.rpc.soap.beans.RemoteAttachment;
import com.atlassian.confluence.rpc.soap.beans.RemotePage;
import com.atlassian.confluence.rpc.soap.beans.RemotePageSummary;
import com.atlassian.confluence.rpc.soap_axis.confluenceservice_v1.ConfluenceSoapServiceServiceLocator;
import com.atlassian.confluence.rpc.soap_axis.confluenceservice_v1.ConfluenceserviceV1SoapBindingStub;

public class GetWikiContent {

	private static String sessionToken = null;
	private static ConfluenceserviceV1SoapBindingStub binding;
	private static final String outputDir = "/home/dcarver/temp/wikitext";

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		login();

		try {
			downloadPages("TEP", "UserGuide");
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		logout();

	}

	public static void login() {
		try {
			binding = (ConfluenceserviceV1SoapBindingStub) new ConfluenceSoapServiceServiceLocator()
					.getConfluenceserviceV1();
			// Time out after a minute
			binding.setTimeout(60000);
			sessionToken = binding.login("dcarver", "cat1dog");
		} catch (AuthenticationFailedException e) {
			System.out.println("Unknown username and password.");
			System.exit(1);
		} catch (RemoteException e) {
			e.printStackTrace();
			System.exit(1);
		} catch (java.rmi.RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(1);
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(1);
		}
	}

	private static void downloadPages(String spaceKey, String pageName)
			throws Exception {
		RemotePage page = binding.getPage(sessionToken, spaceKey, pageName);
		StringBuffer buffer = new StringBuffer(page.getContent());

		String filename = page.getTitle() + ".confluence";
		File wikiPage = new File(outputDir + File.separator + filename);
		FileWriter fwStream = new FileWriter(wikiPage);
		BufferedWriter write = new BufferedWriter(fwStream);
		write.write(buffer.toString());
		write.close();

		downloadImages(page.getId());

		RemotePageSummary[] rmSum = binding.getChildren(sessionToken,
				page.getId());
		if (rmSum != null) {
			for (RemotePageSummary rpSum : rmSum) {
				downloadPages(spaceKey, rpSum.getTitle());
			}
		}
	}

	private static void downloadImages(long pageId) {
		try {
			RemoteAttachment[] attachments = binding.getAttachments(sessionToken, pageId);
			if (attachments == null) {
				return;
			}
			
			if (attachments.length == 0) {
				return;
			}
			
			for (RemoteAttachment attachment : attachments) {
				try {
					URL attachurl = new URL(attachment.getUrl());
					BufferedImage image = ImageIO.read(attachurl);
					File imageOut = new File(outputDir + File.separator + "images" + File.separator + attachment.getFileName());
					String formatType = "jpg";
					if (attachment.getFileName().contains(".jpg")) {
						formatType = "jpg";
					} else if (attachment.getFileName().contains(".png")) {
						formatType = "png";
					} else if (attachment.getFileName().contains(".gif")) {
						formatType = "gif";
					}
					ImageIO.write(image, formatType, imageOut);
					
				} catch (MalformedURLException e) {
					e.printStackTrace();
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private static void logout() {
		try {
			binding.logout(sessionToken);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}
