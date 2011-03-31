/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.functional.test.ft.typelib;

import java.io.File;
import java.io.IOException;

import junit.framework.Assert;

import org.ebayopensource.turmeric.eclipse.core.resources.constants.SOAXSDTemplateSubType;
import org.ebayopensource.turmeric.eclipse.functional.test.AbstractTestCase;
import org.ebayopensource.turmeric.eclipse.functional.test.ft.wsdlsvc.ServiceFromBlankWsdlTest;
import org.ebayopensource.turmeric.eclipse.resources.util.SOAServiceUtil;
import org.ebayopensource.turmeric.eclipse.test.util.DialogMonitor;
import org.ebayopensource.turmeric.eclipse.test.utils.ServicesUtil;
import org.ebayopensource.turmeric.eclipse.test.utils.SimpleTestUtil;
import org.ebayopensource.turmeric.eclipse.test.utils.TLUtil;
import org.ebayopensource.turmeric.eclipse.utils.plugin.ProgressUtil;
import org.ebayopensource.turmeric.eclipse.utils.plugin.WorkspaceUtil;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.CoreException;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;


public class ServiceCreationFromExistingTypesTest3 extends
		AbstractTestCase {
	static DialogMonitor monitor;
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public  void setUp() throws Exception {
	
		monitor = new DialogMonitor();
		monitor.startMonitoring();
		TypeLibSetUp.setup();

		Assert.assertTrue(TypeLibSetUp.TYPELIBRARY_NAME1
				+ " -- TypeLibrary Creation failed", TLUtil.createTypeLibrary(
				TypeLibSetUp.TYPELIBRARY_NAME1, "1.0.0", "COMMON",
				TypeLibSetUp.TYPELIB_LOCATION));
		TLUtil.createType("ImportType", TypeLibSetUp.TYPELIBRARY_NAME1,
				TypeLibSetUp.TYPELIBRARY_NAME1,
				SOAXSDTemplateSubType.SIMPLE,
				TstConstants.TEMPLATE_SIMPLE_TYPE,
				TstConstants.XSD_STRING);
	}
	
	/**
	 * @throws java.lang.Exception
	 */
	@After
	public  void tearDown() throws Exception {
		super.cleanupWorkspace();
		monitor.stopMonitoring();
		monitor = null;
	}

	static String eBoxServiceName = null;

	/*
	 * Import type from existing WS
	 */
	//@Ignore("Currently failing JIRA 756")
	@Test
	public void testCreateServiceFrmNewWsdl() throws IOException, CoreException {
		eBoxServiceName = ServicesUtil.getAdminName(TypeLibSetUp.SVC_NAME3);
		System.out.println(" --- eBox Service name : " + eBoxServiceName);

		TypeLibSetUp.setupSvc(eBoxServiceName);
		try {
			/*
			 * final ServiceFromTemplateWsdlParamModel model = new
			 * ServiceFromTemplateWsdlParamModel(); final String servicePkg =
			 * "com.ebayopensource.turmeric.services";
			 * model.setTargetNamespace(SOAServiceConstants
			 * .DEFAULT_SERVICE_NAMESPACE);
			 * model.setServiceName(EBoxTypeLibSetUp.SVC_NAME3);
			 * model.setServiceInterface(servicePkg + "." +
			 * model.getServiceName());
			 * model.setWorkspaceRootDirectory(EBoxTypeLibSetUp.SVC_LOCATION);
			 * model.setServiceImpl("com.ebayopensource.turmeric.services." +
			 * EBoxTypeLibSetUp.SVC_NAME3 + "Impl");
			 * model.setServiceVersion("1.0.0");
			 * model.setImplName(EBoxTypeLibSetUp.SVC_NAME3 + "Impl");
			 * model.setWSDLSourceType
			 * (SOAProjectConstants.InterfaceWsdlSourceType.NEW);
			 * SimpleTestUtil.setAutoBuilding(true);
			 * ServiceCreator.createServiceFromBlankWSDL(model,
			 * ProgressUtil.getDefaultMonitor(null));
			 */

			ServiceFromBlankWsdlTest.createServiceFromBlankWsdl(
					eBoxServiceName, TypeLibSetUp.SVC_NAME3);

			WorkspaceUtil.getProject(eBoxServiceName).build(
					IncrementalProjectBuilder.FULL_BUILD,
					ProgressUtil.getDefaultMonitor(null));

			WorkspaceUtil.getProject(eBoxServiceName + "Impl").build(
					IncrementalProjectBuilder.FULL_BUILD,
					ProgressUtil.getDefaultMonitor(null));
			// Code to simulate Import action
			String typeNS = TLUtil.getTargetNamespace(TLUtil.functionDomain);
			Assert.assertTrue("Import type in WSDL failed", TLUtil
					.importAction("WSDL", eBoxServiceName, eBoxServiceName
							+ ".wsdl", "ImportType", typeNS));

			// Build Projects
			SimpleTestUtil.setAutoBuilding(true);
			WorkspaceUtil.getProject(eBoxServiceName).build(
					IncrementalProjectBuilder.FULL_BUILD,
					ProgressUtil.getDefaultMonitor(null));

			WorkspaceUtil.getProject(eBoxServiceName + "Impl").build(
					IncrementalProjectBuilder.FULL_BUILD,
					ProgressUtil.getDefaultMonitor(null));

			Assert.assertTrue("Service Creation with Import action passed",
					true);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Assert.assertTrue("Service Creation with Import action failed",
					false);
		}

		validateProjectArtifacts();
		validatePOM();
	}

	/*
	 * Assert ImportType and dependent types are added to the wsdl
	 */
	public void validateProjectArtifacts() throws IOException, CoreException {
		IProject project = WorkspaceUtil.getProject(eBoxServiceName);
		Assert.assertTrue(
				"TypeDepencies.xml didnot get generated",
				project.getFile(
						"meta-src" + File.separator + "META-INF"
								+ File.separator + eBoxServiceName
								+ File.separator + "TypeDependencies.xml")
						.exists());
		String sb = ServiceCreationFromExistingTypesTest1
				.readContentsFromIFile(SOAServiceUtil
						.getWsdlFile(eBoxServiceName));

		// Assert.assertTrue(EBoxTypeLibSetUp.TYPELIBRARY_NAME1 +
		// "TypeDependencies.xml does not contain type " +
		// "dependency on SoftwareTypeLibrary",
		// sb.indexOf("SoftwareTypeLibrary") > -1);

		// Assert.assertTrue(EBoxTypeLibSetUp.TYPELIBRARY_NAME1 +
		// "TypeDependencies.xml does not contain type " +
		// "dependency on SoftwareTypeLibrary", sb.indexOf("AlertType") > -1);

		Assert.assertTrue(TypeLibSetUp.TYPELIBRARY_NAME1
				+ "TypeDependencies.xml does not contain type "
				+ "dependency on SOA21TestTL1", sb.indexOf("SOA21TestTL1") > -1);

		Assert.assertTrue(TypeLibSetUp.TYPELIBRARY_NAME1
				+ "TypeDependencies.xml does not contain type "
				+ "dependency ImportType", sb.indexOf("ImportType") > -1);

		// Assert.assertTrue(EBoxTypeLibSetUp.TYPELIBRARY_NAME1 +
		// "TypeDependencies.xml does not contain type " +
		// "dependency on CustomerType", sb.indexOf("CustomerType") > -1);

		// Assert.assertTrue(EBoxTypeLibSetUp.TYPELIBRARY_NAME1 +
		// "TypeDependencies.xml does not contain type " +
		// "dependency on SOA21TestTestTL2", sb.indexOf("SOA21TestTL2") > -1);

		// Assert.assertTrue(EBoxTypeLibSetUp.TYPELIBRARY_NAME1 +
		// "TypeDependencies.xml does not contain type " +
		// "dependency on ZipType", sb.indexOf("ZipType") > -1);

		sb = ServiceCreationFromExistingTypesTest1
				.readContentsFromIFile(project.getFile("meta-src"
						+ File.separator + "META-INF" + File.separator + "soa"
						+ File.separator + "services" + File.separator + "wsdl"
						+ File.separator + eBoxServiceName + File.separator
						+ eBoxServiceName + ".wsdl"));
		Assert.assertTrue(
				"WSDL file is not inlined with imported type - ImportType",
				sb.indexOf("ImportType") > -1);

		// Assert.assertTrue("WSDL file is not inlined with imported type - CustomerType",
		// sb.indexOf("CustomerType") > -1);
		// Assert.assertTrue("WSDL file is not inlined with imported type - ZipType",
		// sb.indexOf("ZipType") > -1);
		// Assert.assertTrue("WSDL file is not inlined with imported type - AlertType",
		// sb.indexOf("AlertType") > -1);

	}

	/*
	 * Assert pom.xml has library dependency - HardwareTypeLibrary,
	 * SOA21TestTestTL2
	 */
	public void validatePOM() throws CoreException, IOException {
		IProject project = WorkspaceUtil.getProject(eBoxServiceName);
		// Assert.assertTrue("Pom.xml doesnot contain library dependency SoftwareTypeLibrary",
		// EBoxServiceCreationFromExistingTypesTest1.
		// readContentsFromIFile(project.getFile("pom.xml")).
		// indexOf("library name=\"SoftwareTypeLibrary\"") > -1);

		Assert.assertTrue(
				"Project.xml doesnot contain library dependency SOA21TestTL1",
				ServiceCreationFromExistingTypesTest1
						.readContentsFromIFile(project.getFile("pom.xml"))
						.indexOf("SOA21TestTL1") > -1);

		// Assert.assertTrue("pom.xml doesnot contain library dependency SOA21TestTL2",
		// EBoxServiceCreationFromExistingTypesTest1.
		// readContentsFromIFile(project.getFile("pom.xml")).
		// indexOf("SOA21TestTL2") > -1);
	}
}
