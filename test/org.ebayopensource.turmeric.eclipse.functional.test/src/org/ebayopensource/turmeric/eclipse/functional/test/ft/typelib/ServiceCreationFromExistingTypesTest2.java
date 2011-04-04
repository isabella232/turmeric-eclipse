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

import javax.xml.namespace.QName;

import junit.framework.Assert;

import org.ebayopensource.turmeric.eclipse.core.resources.constants.SOAXSDTemplateSubType;
import org.ebayopensource.turmeric.eclipse.functional.test.AbstractTestCase;
import org.ebayopensource.turmeric.eclipse.functional.test.ft.wsdlsvc.ServiceFromBlankWsdlTest;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.SOAGlobalRegistryAdapter;
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

import org.ebayopensource.turmeric.common.config.LibraryType;


public class ServiceCreationFromExistingTypesTest2 extends
		AbstractTestCase {
	static DialogMonitor monitor;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public  void setUp() throws Exception {
	
		monitor = new DialogMonitor();
		monitor.startMonitoring();
	}

	/**
	 * @throws CoreException 
	 * @throws java.lang.Exception
	 */
	@After
	public  void tearDown() throws CoreException{
		super.cleanupWorkspace();
		monitor.stopMonitoring();
		monitor = null;
	}
	
	/*
	 * Import type from existing WS
	 */
	//@Ignore("failig due to 756 JIRA")
	@Test
	public void testCreateServiceFrmNewWsdl() throws IOException, CoreException {
		String eBoxServiceName = ServicesUtil.getAdminName(TypeLibSetUp.SVC_NAME2);
		System.out.println(" --- eBox Service name : " + eBoxServiceName);
		TypeLibSetUp.setupSvc(eBoxServiceName);

		try {
			/*
			 * final ServiceFromTemplateWsdlParamModel model = new
			 * ServiceFromTemplateWsdlParamModel(); final String servicePkg =
			 * "com.ebayopensource.turmeric.services"; model
			 * .setTargetNamespace(SOAServiceConstants
			 * .DEFAULT_SERVICE_NAMESPACE);
			 * model.setServiceName(EBoxTypeLibSetUp.SVC_NAME2); model
			 * .setServiceInterface(servicePkg + "." + model.getServiceName());
			 * model.setWorkspaceRootDirectory(EBoxTypeLibSetUp.SVC_LOCATION);
			 * model.setServiceImpl("com.ebayopensource.turmeric.services." +
			 * EBoxTypeLibSetUp.SVC_NAME2 + "Impl");
			 * model.setServiceVersion("1.0.0");
			 * model.setImplName(EBoxTypeLibSetUp.SVC_NAME2 + "Impl"); model
			 * .setWSDLSourceType
			 * (SOAProjectConstants.InterfaceWsdlSourceType.NEW);
			 * SimpleTestUtil.setAutoBuilding(true);
			 * ServiceCreator.createServiceFromBlankWSDL(model, ProgressUtil
			 * .getDefaultMonitor(null));
			 * 
			 * WorkspaceUtil.getProject(EBoxTypeLibSetUp.SVC_NAME2).build(
			 * IncrementalProjectBuilder.FULL_BUILD,
			 * ProgressUtil.getDefaultMonitor(null));
			 * 
			 * WorkspaceUtil.getProject(EBoxTypeLibSetUp.SVC_NAME2 + "Impl")
			 * .build(IncrementalProjectBuilder.FULL_BUILD,
			 * ProgressUtil.getDefaultMonitor(null));
			 */
	
			//TypeLibSetUp.setup();

			Assert.assertTrue(TypeLibSetUp.TYPELIBRARY_NAME2
					+ " -- TypeLibrary Creation failed", TLUtil
					.createTypeLibrary(TypeLibSetUp.TYPELIBRARY_NAME2,
							"1.0.0", "COMMON",
							TypeLibSetUp.TYPELIB_LOCATION));
			TLUtil.createType("EmployerType",
					TypeLibSetUp.TYPELIBRARY_NAME2,
					TypeLibSetUp.TYPELIBRARY_NAME2,
					SOAXSDTemplateSubType.SIMPLE,
					TstConstants.TEMPLATE_SIMPLE_TYPE,
					TstConstants.XSD_STRING);
			ServiceFromBlankWsdlTest.createServiceFromBlankWsdl(
					eBoxServiceName, TypeLibSetUp.SVC_NAME2);
			// Code to simulate Import action
			String typeNS = TLUtil.getTargetNamespace(TLUtil.functionDomain);
			
			WorkspaceUtil.refresh();
			SOAGlobalRegistryAdapter.getInstance().getGlobalRegistry();
			SOAGlobalRegistryAdapter.getInstance().invalidateRegistry();
		
			
			
			LibraryType type = SOAGlobalRegistryAdapter.getInstance().getGlobalRegistry()
			.getType(new QName("http://www.ebayopensource.org/turmeric/blogs/v1/types","EmployerType"));
			
			Assert.assertTrue("Import type in WSDL failed", TLUtil
					.importAction("WSDL", eBoxServiceName,
							TypeLibSetUp.SVC_NAME2 + ".wsdl",
							"EmployerType", typeNS));

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

		validateProjectArtifacts(eBoxServiceName);

		validateBuildArtifacts(eBoxServiceName);
	}

	/*
	 * Assert EmployerType and dependent types are added to the wsdl
	 */
	public void validateProjectArtifacts(String eBoxServiceName) throws IOException, CoreException {
		IProject project = WorkspaceUtil.getProject(eBoxServiceName);
		Assert.assertTrue(
				"TypeDepencies.xml didnot get generated",
				project.getFile(
						"meta-src" + File.separator + "META-INF"
								+ File.separator + eBoxServiceName
								+ File.separator + "TypeDependencies.xml")
						.exists());
		String sb = ServiceCreationFromExistingTypesTest1
				.readContentsFromIFile(project.getFile("meta-src"
						+ File.separator + "META-INF" + File.separator + "soa"
						+ File.separator + "services" + File.separator + "wsdl"
						+ File.separator + eBoxServiceName + File.separator
						+ eBoxServiceName + ".wsdl"));
		Assert.assertTrue(
				"WSDL file is not inlined with imported type - EmployerType",
				sb.indexOf("EmployerType") > -1);

	}

	/*
	 * Assert pom.xml has library dependency - HardwareTypeLibrary
	 */
	public void validateBuildArtifacts(String eBoxServiceName) throws CoreException, IOException {
		IProject project = WorkspaceUtil.getProject(eBoxServiceName);
		Assert.assertTrue(
				"Project.xml doesnot contain library dependency SOA21TestTL2",
				ServiceCreationFromExistingTypesTest1
						.readContentsFromIFile(project.getFile("pom.xml"))
						.indexOf("SOA21TestTL2") > -1);
	}

}
