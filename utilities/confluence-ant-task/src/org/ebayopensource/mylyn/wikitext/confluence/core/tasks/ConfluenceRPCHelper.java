/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.mylyn.wikitext.confluence.core.tasks;

import javax.xml.rpc.ServiceException;

import com.atlassian.confluence.rpc.AuthenticationFailedException;
import com.atlassian.confluence.rpc.RemoteException;
import com.atlassian.confluence.rpc.soap.beans.RemoteAttachment;
import com.atlassian.confluence.rpc.soap.beans.RemotePage;
import com.atlassian.confluence.rpc.soap.beans.RemotePageSummary;
import com.atlassian.confluence.rpc.soap_axis.confluenceservice_v1.ConfluenceSoapServiceServiceLocator;
import com.atlassian.confluence.rpc.soap_axis.confluenceservice_v1.ConfluenceserviceV1SoapBindingStub;

public class ConfluenceRPCHelper {

	
	private ConfluenceserviceV1SoapBindingStub binding;
	

	private String sessionToken = null;

	private String USERNAME = "dcarver";

	private String PASSWORD = "cat1dog";
	
	private String spaceKey = null;
	private String pageName = null;
	
	public ConfluenceRPCHelper(String spaceKey, String pageName, String username, String password) {
		this.spaceKey = spaceKey;
		this.pageName = pageName;
		this.USERNAME = username;
		this.PASSWORD = password;
	}
	
	public void login() throws AuthenticationFailedException, RemoteException, java.rmi.RemoteException, ServiceException {
		binding = (ConfluenceserviceV1SoapBindingStub) new ConfluenceSoapServiceServiceLocator()
				.getConfluenceserviceV1();
		binding.setTimeout(60000);
		sessionToken = binding.login(USERNAME, PASSWORD);
	}
	
	public void logout() {
		try {
			binding.logout(sessionToken);
		} catch (Exception ex) {
			
		}
	}
	
	public RemotePage getTopPage() {
		try {
			return binding.getPage(sessionToken, spaceKey, pageName);
		} catch (Exception ex) {
			
		}
		return null;
	}
	
	public RemotePage getPage(String title) {
		try {
			return binding.getPage(sessionToken, spaceKey, title);
		} catch (Exception ex) {
			
		}
		return null;
	}
	
	public RemoteAttachment[] getAttachments(RemotePage page) {
		try {
			return binding.getAttachments(sessionToken, page.getId());
		} catch (Exception ex) {
			
		}
		return null;
	}
	
	public RemotePageSummary[] getChildrenPages(RemotePage page) {
		try {
			return binding.getChildren(sessionToken,page.getId());
		} catch (Exception ex) {
			
		}
		return null;
	}
	
}
