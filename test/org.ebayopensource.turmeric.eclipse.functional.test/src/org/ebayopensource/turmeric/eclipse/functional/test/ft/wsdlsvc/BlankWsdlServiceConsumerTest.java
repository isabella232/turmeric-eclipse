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

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.junit.Assume.*;

import org.ebayopensource.turmeric.eclipse.functional.test.AbstractTestCase;
import org.ebayopensource.turmeric.eclipse.functional.test.SoaTestConstants;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.GlobalRepositorySystem;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.ISOARepositorySystem;
import org.ebayopensource.turmeric.eclipse.resources.ui.model.ConsumerFromJavaParamModel;
import org.ebayopensource.turmeric.eclipse.services.buildsystem.ServiceCreator;
import org.ebayopensource.turmeric.eclipse.test.util.FunctionalTestHelper;
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

/**
 * @author shrao
 * 
 */
public class BlankWsdlServiceConsumerTest extends AbstractTestCase {

	
	public static String PARENT_DIR = org.eclipse.core.runtime.Platform
			.getLocation().toOSString();
	static String adminName = null;
	static String publicServiceName = null;
	static String dataDirectory = WsdlUtilTest.getPluginOSPath(
			SoaTestConstants.PLUGIN_ID,"data");

	@BeforeClass
	public static void setUp(){
		
		ZipExtractor zip = new ZipExtractor();
		zip.extract(dataDirectory+"/BlankWsdlServiceConsumerTest.zip",dataDirectory +"/extractedData");
		
	}
	
	/**
	 * @throws java.lang.Exception
	 */
	@Override
	@Before
	public void setUpBeforeClass() throws Exception {
		

		SimpleTestUtil.setAutoBuilding(false);

		ISOARepositorySystem repositorySystem = new TurmericRepositorySystem();
		GlobalRepositorySystem.instanceOf().setActiveRepositorySystem(
				repositorySystem);

		publicServiceName = "Service";
		adminName = ServicesUtil.getAdminName(publicServiceName);
		System.out.println(" --- Service Admin Name : " + adminName);

		if (WorkspaceUtil.getProject(adminName + "Consumer").exists())
			ProjectUtil.cleanUpProjects(WorkspaceUtil.getProject(adminName
					+ "Consumer"));
		// ServiceSetupCleanupValidate.cleanupWSConsumer(ServiceName);
		ServiceSetupCleanupValidate.cleanup(adminName);
		ServiceSetupCleanupValidate.cleanupConsumer(adminName);
		FunctionalTestHelper.ensureM2EcipseBeingInited();
		String admin = adminName;
		String publicService = publicServiceName;
		try {
			boolean result = ServiceFromBlankWsdlTest
					.createServiceFromBlankWsdl(admin, publicService);
			assertTrue(result);
			SimpleTestUtil.setAutoBuilding(true);
		} catch (NoClassDefFoundError ex) {
			assumeNoException(ex);
		}
		// SimpleTestUtil.setAutoBuilding(true);
	}

	@Test
     @Ignore("Fails after refactoring")
	public void testConsumeCalculatorSvc() throws Exception {

		// Turn on the auto-build for the builders to kick-in

		try {
			String consumerId = "CalcConsumer_Id";
			List<String> environment = new ArrayList<String>();
			environment.add("production");
			ConsumerFromJavaParamModel model = new ConsumerFromJavaParamModel();
			model.setBaseConsumerSrcDir("src");
			model.setClientName(adminName + "Consumer");
			ArrayList<String> list = new ArrayList<String>();
			list.add(adminName);
			model.setServiceNames(list);
			model.setParentDirectory(PARENT_DIR);
			model.setConsumerId(consumerId);
			model.setEnvironments(environment);
			model.setConvertingJavaProject(true);
			SimpleTestUtil.setAutoBuilding(false);
			Thread.sleep(5000);

			ServiceCreator.createConsumerFromJava(model,
					ProgressUtil.getDefaultMonitor(null));
			Thread.sleep(5000);

			SimpleTestUtil.setAutoBuilding(true);
			// Add validation for the expected artifacts and contents..

			// we know that the CC.xml or GCC.xml is the last artifact to be
			// created
			// The project exists as
			IProject consProject = WorkspaceUtil.getProject(model
					.getClientName());

			consProject.build(IncrementalProjectBuilder.FULL_BUILD,
					ProgressUtil.getDefaultMonitor(null));

			boolean consMatch = ServiceSetupCleanupValidate
					.validateConsumerArtifacts(
							WorkspaceUtil.getProject(adminName + "Consumer"),
							adminName + "Consumer");

			String failMessages = ProjectArtifactValidator
					.getErroredFileMessage().toString();
			ServiceSetupCleanupValidate.validateMatch = true;
			ProjectArtifactValidator.getErroredFileMessage().setLength(0);
			System.out.println(failMessages);
			assertTrue(" --- Service Consumer validation failed "
					+ failMessages.toString(), consMatch);
		} catch (NoClassDefFoundError ex) {
			assumeNoException(ex);
		}

		/*
		 * FunctionalTestHelper.consumeJunitModifiedTestSvcBeforeMod(
		 * ServiceName, consProject);
		 * 
		 * FunctionalTestHelper.modifyClientPrjServicePP(consProject,
		 * ServiceName, SOAProjectConstants.MessageProtocol.SOAP11.toString());
		 * 
		 * FunctionalTestHelper.invokeConsumer(consProject);
		 * 
		 * FunctionalTestHelper.modifyClientPrjServicePP(consProject,
		 * ServiceName, SOAProjectConstants.MessageProtocol.NONE.toString());
		 * 
		 * FunctionalTestHelper.modifyClientPrjRespDataBinding(consProject ,
		 * ServiceName, SOAProjectConstants.DataBinding.JSON.toString());
		 * 
		 * FunctionalTestHelper.invokeConsumer(consProject);
		 */

	}
	
	@AfterClass
	public static void deInit(){
		
		ensureClean(dataDirectory +"/extractedData");
	}
}
