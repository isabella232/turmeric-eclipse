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

import org.ebayopensource.turmeric.eclipse.config.repo.SOAConfigExtensionFactory.SOAXSDTemplateSubType;
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

	static String serviceName = null;

	/*
	 * Import type from existing WS
	 */
	@Test
	public void testCreateServiceFrmNewWsdl() throws IOException, CoreException {
		serviceName = ServicesUtil.getAdminName(TypeLibSetUp.SVC_NAME3);
		System.out.println(" ---Service name : " + serviceName);

		TypeLibSetUp.setupSvc(serviceName);
		try {
			

			ServiceFromBlankWsdlTest.createServiceFromBlankWsdl(
					serviceName, TypeLibSetUp.SVC_NAME3);

			WorkspaceUtil.getProject(serviceName).build(
					IncrementalProjectBuilder.FULL_BUILD,
					ProgressUtil.getDefaultMonitor(null));

			WorkspaceUtil.getProject(serviceName + "Impl").build(
					IncrementalProjectBuilder.FULL_BUILD,
					ProgressUtil.getDefaultMonitor(null));
			// Code to simulate Import action
			String typeNS = TLUtil.getTargetNamespace(TLUtil.functionDomain);
			Assert.assertTrue("Import type in WSDL failed", TLUtil
					.importAction("WSDL", serviceName, serviceName
							+ ".wsdl", "ImportType", typeNS));

			// Build Projects
			SimpleTestUtil.setAutoBuilding(true);
			WorkspaceUtil.getProject(serviceName).build(
					IncrementalProjectBuilder.FULL_BUILD,
					ProgressUtil.getDefaultMonitor(null));

			WorkspaceUtil.getProject(serviceName + "Impl").build(
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
		IProject project = WorkspaceUtil.getProject(serviceName);
		Assert.assertTrue(
				"TypeDepencies.xml didnot get generated",
				project.getFile(
						"meta-src" + File.separator + "META-INF"
								+ File.separator + serviceName
								+ File.separator + "TypeDependencies.xml")
						.exists());
		String sb = ServiceCreationFromExistingTypesTest1
				.readContentsFromIFile(SOAServiceUtil
						.getWsdlFile(serviceName));


		Assert.assertTrue(TypeLibSetUp.TYPELIBRARY_NAME1
				+ "TypeDependencies.xml does not contain type "
				+ "dependency on SOA21TestTL1", sb.indexOf("SOA21TestTL1") > -1);

		Assert.assertTrue(TypeLibSetUp.TYPELIBRARY_NAME1
				+ "TypeDependencies.xml does not contain type "
				+ "dependency ImportType", sb.indexOf("ImportType") > -1);

		

		sb = ServiceCreationFromExistingTypesTest1
				.readContentsFromIFile(project.getFile("meta-src"
						+ File.separator + "META-INF" + File.separator + "soa"
						+ File.separator + "services" + File.separator + "wsdl"
						+ File.separator + serviceName + File.separator
						+ serviceName + ".wsdl"));
		Assert.assertTrue(
				"WSDL file is not inlined with imported type - ImportType",
				sb.indexOf("ImportType") > -1);

	}

	/*
	 * Assert pom.xml has library dependency - HardwareTypeLibrary,
	 * SOA21TestTestTL2
	 */
	public void validatePOM() throws CoreException, IOException {
		IProject project = WorkspaceUtil.getProject(serviceName);

		Assert.assertTrue(
				"Project.xml doesnot contain library dependency SOA21TestTL1",
				ServiceCreationFromExistingTypesTest1
						.readContentsFromIFile(project.getFile("pom.xml"))
						.indexOf("SOA21TestTL1") > -1);

	}
}
