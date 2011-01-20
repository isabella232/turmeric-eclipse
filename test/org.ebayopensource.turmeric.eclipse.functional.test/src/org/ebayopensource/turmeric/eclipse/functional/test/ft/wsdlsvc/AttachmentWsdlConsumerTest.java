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

import static org.junit.Assert.*;
import static org.junit.Assume.*;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.ebayopensource.turmeric.eclipse.functional.test.AbstractTestCase;
import org.ebayopensource.turmeric.eclipse.resources.constants.SOAProjectConstants;
import org.ebayopensource.turmeric.eclipse.resources.ui.model.ConsumerFromWsdlParamModel;
import org.ebayopensource.turmeric.eclipse.services.buildsystem.ServiceCreator;
import org.ebayopensource.turmeric.eclipse.test.util.FunctionalTestHelper;
import org.ebayopensource.turmeric.eclipse.test.util.ProjectArtifactValidator;
import org.ebayopensource.turmeric.eclipse.test.util.SimpleTestUtil;
import org.ebayopensource.turmeric.eclipse.test.utils.ServicesUtil;
import org.ebayopensource.turmeric.eclipse.utils.plugin.ProgressUtil;
import org.ebayopensource.turmeric.eclipse.utils.plugin.WorkspaceUtil;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.CoreException;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

/**
 * @author shrao
 * 
 *         use case: create consumer from CS API WSDL validate consumer project
 *         artifacts
 */
public class AttachmentWsdlConsumerTest extends AbstractTestCase {

	public static String PARENT_DIR = ServiceSetupCleanupValidate
			.getParentDir();
	static String WSDL_FILE = ServiceSetupCleanupValidate
			.getWsdlFilePath("CSUpdateMACActivityAddAttachments.wsdl");
	static String publicServiceName = null;
	static String adminName = null;
	static IProject consProject = null;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUpBeforeClass() throws Exception {

		SimpleTestUtil.setAutoBuilding(false);

		publicServiceName = ServiceSetupCleanupValidate
				.getServiceName(WSDL_FILE);

		adminName = ServicesUtil.getAdminName(publicServiceName);

		System.out.println("--- WSDL file : " + WSDL_FILE);
		System.out.println("--- Service Admin Name : " + adminName);

		ServiceSetupCleanupValidate.cleanupWSConsumer(adminName);
		ServiceSetupCleanupValidate.cleanup(adminName);
		// turn ON Build-Automatically for services
		SimpleTestUtil.setAutoBuilding(true);

		FunctionalTestHelper.ensureM2EcipseBeingInited();
	}

	public void createConsumerFromWsdl(URL wsdlURL) throws Exception {

		String publicService = ServicesUtil.getServiceName(wsdlURL.toString());
		String serviceName = ServicesUtil.getAdminName(publicService);
		String targetNS = ServicesUtil.getTargetNamespaceFromWsdl(wsdlURL
				.toString());
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
		model.setWSDLSourceType(SOAProjectConstants.InterfaceWsdlSourceType.EXISTIING);
		model.setClientName(serviceName + "Consumer");
		model.setOriginalWsdlUrl(wsdlURL);
		model.setPublicServiceName(publicService);
		model.setConsumerId("consumer_Id");
		model.setEnvironments(environment);
		// model.setServiceDomain(domainClassifier);
		// model.setNamespacePart(nsPart);
		SimpleTestUtil.setAutoBuilding(false);

		try {
			ServiceCreator.createConsumerFromExistingWSDL(model,
					ProgressUtil.getDefaultMonitor(null));
		} catch (Exception e) {
			System.out.println("--- Exception in createConsumerFromWsdl() - "
					+ e.getMessage());
			e.printStackTrace();
			fail("Exception in createConsumerFromWsdl() - (consumer creation failed) "
					+ e.getLocalizedMessage());
		}

		try {
			WorkspaceUtil.getProject(model.getServiceName()).build(
					IncrementalProjectBuilder.FULL_BUILD,
					ProgressUtil.getDefaultMonitor(null));
		} catch (CoreException e) {
			System.out.println("--- Exception in createConsumerFromWsdl() - "
					+ e.getMessage());
			e.printStackTrace();
			fail("Exception in createConsumerFromWsdl() - (build on Intf failed) "
					+ e.getLocalizedMessage());
		}

		try {
			WorkspaceUtil.getProject(model.getClientName()).build(
					IncrementalProjectBuilder.FULL_BUILD,
					ProgressUtil.getDefaultMonitor(null));
		} catch (CoreException e) {
			System.out.println("--- Exception in createConsumerFromWsdl() - "
					+ e.getMessage());
			e.printStackTrace();
			fail("Exception in createConsumerFromWsdl() - (build on consumer failed) "
					+ e.getLocalizedMessage());
		}

	}

	@Test
	public void testAttachmentServiceConsumerCreate() throws Exception {

		try {
			createConsumerFromWsdl(new File(WSDL_FILE).toURI().toURL());

			boolean intfMatch = ServiceSetupCleanupValidate
					.validateIntfArtifacts(WorkspaceUtil.getProject(adminName),
							adminName);
			boolean consumerMatch = ServiceSetupCleanupValidate
					.validateConsumerArtifacts(
							WorkspaceUtil.getProject(adminName + "Consumer"),
							adminName + "Consumer");
			String failMessages = ProjectArtifactValidator
					.getErroredFileMessage().toString();

			ServiceSetupCleanupValidate.validateMatch = true;
			ProjectArtifactValidator.getErroredFileMessage().setLength(0);
			System.out.println(failMessages);

			assertTrue("Test failed for" + failMessages, intfMatch
					&& consumerMatch);
		} catch (NoClassDefFoundError ex) {
			assumeNoException(ex);
		}

	}
}
