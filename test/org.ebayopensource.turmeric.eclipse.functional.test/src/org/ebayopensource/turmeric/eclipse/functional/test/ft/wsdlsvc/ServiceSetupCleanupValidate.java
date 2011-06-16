/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
/**
 * 
 */
package org.ebayopensource.turmeric.eclipse.functional.test.ft.wsdlsvc;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

import javax.wsdl.Definition;
import javax.wsdl.Service;
import javax.wsdl.WSDLException;

import org.apache.commons.io.FileUtils;
import org.ebayopensource.turmeric.eclipse.functional.test.SoaTestConstants;
import org.ebayopensource.turmeric.eclipse.test.util.ProjectArtifactValidator;
import org.ebayopensource.turmeric.eclipse.test.util.ProjectUtil;
import org.ebayopensource.turmeric.eclipse.test.utils.WsdlUtilTest;
import org.ebayopensource.turmeric.eclipse.utils.plugin.WorkspaceUtil;
import org.ebayopensource.turmeric.eclipse.utils.wsdl.WSDLUtil;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;


/**
 * @author shrao
 * 
 */
public class ServiceSetupCleanupValidate {

	static String PARENT_DIR = getParentDir();
	static boolean validateMatch = true;

	public static String getParentDir() {
		String parentDir = null;
		parentDir = org.eclipse.core.runtime.Platform.getLocation()
				.toOSString();
		System.out.println(" --- service projects parent directory = "
				+ parentDir);
		return parentDir;
	}

	public static void cleanupWSConsumer(String serviceName) {

		System.out.println(" ---Service cleanupWSConsumer()");
		try {
			if (WorkspaceUtil.getProject(serviceName).exists())
				ProjectUtil.cleanUpProjects(WorkspaceUtil
						.getProject(serviceName));
			if (WorkspaceUtil.getProject(serviceName + "Consumer").exists())
				ProjectUtil.cleanUpProjects(WorkspaceUtil
						.getProject(serviceName + "Consumer"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println(" --- Exception in cleanupWSConsumer() : "
					+ e.getMessage());
			e.printStackTrace();
		}
	}

	public static void cleanupWSService(String serviceName) {

		System.out.println(" --- eBox Service cleanupWSService()");
		try {
			if (WorkspaceUtil.getProject(serviceName).exists())
				ProjectUtil.cleanUpProjects(WorkspaceUtil
						.getProject(serviceName));
			if (WorkspaceUtil.getProject(serviceName + "Impl").exists())
				ProjectUtil.cleanUpProjects(WorkspaceUtil
						.getProject(serviceName + "Impl"));

		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println(" -- Exception in cleanupWSService() : "
					+ e.getMessage());
			e.printStackTrace();
		}
	}

	public static void cleanup(String serviceName)
			throws InterruptedException {

		String dirPath = null;

		dirPath = PARENT_DIR + File.separator + serviceName;
		
		try{
		FileUtils.deleteDirectory(new File(dirPath));

		dirPath = PARENT_DIR + File.separator + serviceName + "Impl";
		FileUtils.deleteDirectory(new File(dirPath));

		dirPath = PARENT_DIR + File.separator + serviceName + "Consumer";
		FileUtils.deleteDirectory(new File(dirPath));
		}catch(IOException e){
			
		}

		/*
		 * ProcessBuilder procBuilder = null; Process pid = null;
		 * 
		 * System.out.println(" --- Delete Svc Artifacts ");
		 * 
		 * //Delete Intf Proj folder procBuilder = new ProcessBuilder("cmd",
		 * "/c", "rmdir", "/S/Q", PARENT_DIR + "\\"+ serviceName); pid =
		 * procBuilder.start(); pid.waitFor();
		 * 
		 * //Delete Impl Proj folder procBuilder = new ProcessBuilder("cmd",
		 * "/c", "rmdir", "/S/Q", PARENT_DIR + "\\" + serviceName + "Impl"); pid
		 * = procBuilder.start(); pid.waitFor();
		 * 
		 * //Delete Consumer Proj folder procBuilder = new ProcessBuilder("cmd",
		 * "/c", "rmdir", "/S/Q", PARENT_DIR + "\\" + serviceName + "Consumer");
		 * pid = procBuilder.start(); pid.waitFor();
		 */
	}

	public static void cleanupConsumer(String serviceName) throws IOException,
			InterruptedException {

		String dirPath = null;

		dirPath = PARENT_DIR + File.separator + serviceName + "Consumer";

		FileUtils.deleteDirectory(new File(dirPath));
		/*
		 * ProcessBuilder procBuilder = null; Process pid = null;
		 * 
		 * System.out.println(" --- Delete SvcConsumer Artifacts ");
		 * 
		 * //Delete Consumer Proj folder procBuilder = new ProcessBuilder("cmd",
		 * "/c", "rmdir", "/S/Q", PARENT_DIR + "\\" + serviceName + "Client");
		 * pid = procBuilder.start(); pid.waitFor();
		 */
	}

	public static boolean validateConsumerArtifacts(IProject consumerPrj,
			String goldCopyFolder) {
		ProjectArtifactValidator pav = new ProjectArtifactValidator("target",
				".svn");
		pav.setGoldCopyRootDir(goldCopyFolder);
		try {
			consumerPrj.accept(pav);
			if (pav.isMatches() == false) {
				validateMatch = false;
				System.out.println("Goldcopy validation failed for "
						+ consumerPrj.getName());
			}
		} catch (CoreException e) {
			validateMatch = false;
			System.out.println("Exception in validateConsumerArtifacts() for "
					+ consumerPrj.getName());
			e.printStackTrace();
		}
		return validateMatch;
	}

	public static boolean validateIntfArtifacts(IProject intfPrj,
			String goldCopyFolder) {
		ProjectArtifactValidator pav = new ProjectArtifactValidator("target",
				".svn");
		pav.setGoldCopyRootDir(goldCopyFolder);
		try {
			intfPrj.accept(pav);
			if (pav.isMatches() == false) {
				validateMatch = false;
				System.out.println("Goldcopy validation failed for "
						+ intfPrj.getName());
			}
		} catch (CoreException e) {
			validateMatch = false;
			System.out.println("Exception in validateIntfArtifacts() for "
					+ intfPrj.getName());
			e.printStackTrace();
		}
		return validateMatch;
	}

	public static boolean validateImplArtifacts(IProject implPrj,
			String goldCopyFolder) {
		ProjectArtifactValidator pav = new ProjectArtifactValidator("target",
				".svn");
		pav.setGoldCopyRootDir(goldCopyFolder);
		try {
			implPrj.accept(pav);
			if (pav.isMatches() == false) {
				validateMatch = false;
				System.out.println("Goldcopy validation failed for "
						+ implPrj.getName());
			}
		} catch (CoreException e) {
			validateMatch = false;
			System.out.println("Exception in validateImplArtifacts() for "
					+ implPrj.getName());
			e.printStackTrace();
		}
		return validateMatch;
	}

	public static String getServiceName(String wsdlFilePath) {
		String serviceName = null;
		try {
			final Definition definition = WSDLUtil.readWSDL(wsdlFilePath);
			final Collection<?> services = definition.getServices().values();
			if (services.size() > 0) { // we believe that the wsdl should
				// contain only one service
				final Service service = (Service) services.toArray()[0];
				// serviceName =
				// StringUtils.capitalize(service.getQName().getLocalPart());
				serviceName = service.getQName().getLocalPart();
			}

		} catch (final WSDLException wsdlE) {
			wsdlE.printStackTrace();
		}
		return serviceName;
	}

	public static String getWsdlFilePath(String wsdlFileName) {
		String wsdlFilePath = null;
		wsdlFilePath = WsdlUtilTest.getPluginOSPath(
				SoaTestConstants.PLUGIN_ID, "data/extractedData")
				+ WorkspaceUtil.PATH_SEPERATOR + wsdlFileName;
		return wsdlFilePath;
	}

	public void initializeConsumer() {

	}

	public void initializeInterface() {

	}

	public void initializeImpl() {

	}

}
