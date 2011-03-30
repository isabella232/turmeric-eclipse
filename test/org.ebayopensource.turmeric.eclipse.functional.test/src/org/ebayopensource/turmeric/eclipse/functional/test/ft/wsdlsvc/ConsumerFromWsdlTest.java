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

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.ebayopensource.turmeric.eclipse.build.builder.SOAInterfaceProjectBuilder;
import org.ebayopensource.turmeric.eclipse.core.model.consumer.ConsumerFromWsdlParamModel;
import org.ebayopensource.turmeric.eclipse.core.resources.constants.SOAProjectConstants;
import org.ebayopensource.turmeric.eclipse.functional.test.AbstractTestCase;
import org.ebayopensource.turmeric.eclipse.functional.test.SoaTestConstants;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.GlobalRepositorySystem;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.ISOARepositorySystem;
import org.ebayopensource.turmeric.eclipse.services.buildsystem.ServiceCreator;
import org.ebayopensource.turmeric.eclipse.test.util.ProjectArtifactValidator;
import org.ebayopensource.turmeric.eclipse.test.util.ProjectUtil;
import org.ebayopensource.turmeric.eclipse.test.util.SimpleTestUtil;
import org.ebayopensource.turmeric.eclipse.test.util.ZipExtractor;
import org.ebayopensource.turmeric.eclipse.test.utils.ServicesUtil;
import org.ebayopensource.turmeric.eclipse.test.utils.WsdlUtilTest;
import org.ebayopensource.turmeric.eclipse.utils.plugin.ProgressUtil;
import org.ebayopensource.turmeric.eclipse.utils.plugin.WorkspaceUtil;
import org.ebayopensource.turmeric.repositorysystem.imp.impl.TurmericRepositorySystem;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.junit.Assume.*;



/**
 * @author shrao
 * 
 *         Create an eBox consumer from WSDL
 */
public class ConsumerFromWsdlTest extends AbstractTestCase {
	static String PARENT_DIR = org.eclipse.core.runtime.Platform.getLocation()
			.toOSString();
	static String WSDL_FILE = ServiceSetupCleanupValidate
			.getWsdlFilePath("CalcService.wsdl");
	static String publicServiceName = null;
	static String adminName = null;
	static boolean allMatch = true;
	final static String domainClassifier = "Blogs";
	final static String namespacePart = "blogs";
	
	static String dataDirectory = WsdlUtilTest.getPluginOSPath(
			SoaTestConstants.PLUGIN_ID,"data");
	
	
	@BeforeClass
	public static void setUp(){
		
		ZipExtractor zip = new ZipExtractor();
		zip.extract(dataDirectory+"/ConsumerFromIntfTest.zip",dataDirectory +"/extractedData");
		
	}

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public  void setUpBeforeClass() throws Exception {
	
		
		SimpleTestUtil.setAutoBuilding(false);

		ISOARepositorySystem repositorySystem = new TurmericRepositorySystem();
		GlobalRepositorySystem.instanceOf().setActiveRepositorySystem(
				repositorySystem);

		publicServiceName = ServiceSetupCleanupValidate
				.getServiceName(WSDL_FILE);
		adminName = ServicesUtil.getAdminName(publicServiceName);
		System.out.println(" --- WSDL FILE : " + WSDL_FILE);
		System.out.println(" --- Service Admin Name : " + adminName);

		ProjectUtil.cleanUpWS();
		// EBoxServiceSetupCleanupValidate.cleanupWSConsumer(eBoxServiceName);
		ServiceSetupCleanupValidate.cleanup(adminName);
	}
	
	/*@Override
	public void setUp() throws Exception {

		super.setUp();
		SimpleTestUtil.setAutoBuilding(false);

		ISOARepositorySystem repositorySystem = new TurmericRepositorySystem();
		GlobalRepositorySystem.instanceOf().setActiveRepositorySystem(
				repositorySystem);

		eBoxServiceName = EBoxServiceSetupCleanupValidate
				.getServiceName(WSDL_FILE);
		eBoxServiceName = ServicesUtil.getAdminName(eBoxServiceName);
		System.out.println(" --- WSDL FILE : " + WSDL_FILE);
		System.out.println(" --- eBox Service name : " + eBoxServiceName);

		ProjectUtil.cleanUpWS();
		// EBoxServiceSetupCleanupValidate.cleanupWSConsumer(eBoxServiceName);
		EBoxServiceSetupCleanupValidate.cleanup(eBoxServiceName);

	}*/
	
	
	@Test
	@Ignore("failing")
	public void testCreateEBoxConsumerFromWsdl() throws Exception {

		Boolean b = false;
		
		try {
		b = createConsumerFromWsdl((new File(WSDL_FILE)).toURI()
				.toURL(),adminName);
		} catch (NoClassDefFoundError ex) {
			assumeNoException(ex);
		}
		
		assertTrue(adminName
				+ "createEBoxConsumerFromWsdl() failed", b);

		boolean intfMatch = ServiceSetupCleanupValidate
				.validateIntfArtifacts(
						WorkspaceUtil.getProject(adminName),
						adminName);
		boolean consumerMatch = ServiceSetupCleanupValidate
				.validateConsumerArtifacts(
						WorkspaceUtil.getProject(adminName + "Consumer"),
						adminName + "Consumer");
		
		
		StringBuffer failMessages = ProjectArtifactValidator.getErroredFileMessage();
		
		System.out.println(failMessages.toString());
		assertTrue(" --- Service artifacts validation failed for" + failMessages.toString(), intfMatch
				&& consumerMatch);
		ProjectArtifactValidator.getErroredFileMessage().setLength(0);
	}

	public static boolean createConsumerFromWsdl(URL wsdlUrl,String adminNam) {
		try {
			String publicService = ServicesUtil.getServiceName(wsdlUrl
					.getFile());
			//String domainClassifier = SoaTestConstants.DOMAIN_CLASSIFIER;
			String serviceName = ServicesUtil.getAdminName(publicService);
			// String nsPart = domainClassifier.toLowerCase().trim();
			String targetNS = ServicesUtil.getTargetNamespaceFromWsdl(wsdlUrl
					.getFile());
			String serviceInterface = ServicesUtil.getInterfacePackage(
					publicService, targetNS);
			String serviceImpl = serviceInterface
					+ SOAProjectConstants.CLASS_NAME_SEPARATOR
					+ SOAProjectConstants.IMPL_PROJECT_SUFFIX.toLowerCase()
					+ SOAProjectConstants.CLASS_NAME_SEPARATOR + serviceName
					+ "Impl";
			List<String> environment = new ArrayList<String>();
			environment.add("production");
			ConsumerFromWsdlParamModel model = new ConsumerFromWsdlParamModel();
			model.setServiceName(serviceName);
			model.setServiceInterface(serviceInterface);
			model.setWorkspaceRootDirectory(PARENT_DIR);
			model.setServiceImpl(serviceImpl);
			model.setBaseConsumerSrcDir("src");
			model.setServiceVersion("1.0.0");
			model.setServiceLayer("COMMON");
			//added
			model.setTypeFolding(true);
			model.setWSDLSourceType(SOAProjectConstants.InterfaceWsdlSourceType.EXISTIING);
			model.setClientName(serviceName + "Consumer");
			model.setOriginalWsdlUrl((new File(wsdlUrl.getFile())).toURI()
					.toURL());
			model.setPublicServiceName(publicService);
			model.setConsumerId("CalcConsumer_Id");
			model.setEnvironments(environment);
			model.setServiceDomain(domainClassifier);
			model.setNamespacePart(namespacePart);
			SimpleTestUtil.setAutoBuilding(false);
			ServiceCreator.createConsumerFromExistingWSDL(model,
					ProgressUtil.getDefaultMonitor(null));
			SimpleTestUtil.setAutoBuilding(true);
			
			IProject intfProject = WorkspaceUtil.getProject(adminNam);

			/*intfProject.build(IncrementalProjectBuilder.FULL_BUILD,
					ProgressUtil.getDefaultMonitor(null));
			
			IProject consProject = WorkspaceUtil.getProject(model.getClientName());

			consProject.build(IncrementalProjectBuilder.FULL_BUILD,
					ProgressUtil.getDefaultMonitor(null));*/
			
			
			return true;
		} catch (Exception e) {
			System.out
					.println("--- Exception in createEBoxConsumerFromWsdl() - "
							+ e.getMessage());
			fail("Exception in createEBoxConsumerFromWsdl() - "
					+ e.getLocalizedMessage());
		}
		return false;
	}
	
	@AfterClass
	public static void deInit(){
		
		ensureClean(dataDirectory +"/extractedData");
	}
}
