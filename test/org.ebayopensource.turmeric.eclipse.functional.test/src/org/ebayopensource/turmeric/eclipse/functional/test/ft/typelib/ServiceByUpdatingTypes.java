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
import java.util.ArrayList;
import java.util.Map;

import junit.framework.Assert;

import org.apache.commons.io.FileUtils;
import org.ebayopensource.turmeric.common.config.LibraryType;
import org.ebayopensource.turmeric.common.config.TypeLibraryType;
import org.ebayopensource.turmeric.eclipse.core.resources.constants.SOAXSDTemplateSubType;
import org.ebayopensource.turmeric.eclipse.functional.test.AbstractTestCase;
import org.ebayopensource.turmeric.eclipse.functional.test.SoaTestConstants;
import org.ebayopensource.turmeric.eclipse.functional.test.ft.wsdlsvc.ServiceFromBlankWsdlTest;
import org.ebayopensource.turmeric.eclipse.functional.test.ft.wsdlsvc.ServiceSetupCleanupValidate;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.SOAGlobalRegistryAdapter;
import org.ebayopensource.turmeric.eclipse.resources.util.SOAServiceUtil;
import org.ebayopensource.turmeric.eclipse.test.util.DialogMonitor;
import org.ebayopensource.turmeric.eclipse.test.util.ZipExtractor;
import org.ebayopensource.turmeric.eclipse.test.utils.ProjectUtil;
import org.ebayopensource.turmeric.eclipse.test.utils.ServicesUtil;
import org.ebayopensource.turmeric.eclipse.test.utils.TLUtil;
import org.ebayopensource.turmeric.eclipse.test.utils.WsdlUtilTest;
import org.ebayopensource.turmeric.eclipse.typelibrary.TypeLibraryActivator;
import org.ebayopensource.turmeric.eclipse.typelibrary.ui.TypeLibraryUtil;
import org.ebayopensource.turmeric.eclipse.typelibrary.ui.wst.UpdateTypeVersion;
import org.ebayopensource.turmeric.eclipse.utils.plugin.ProgressUtil;
import org.ebayopensource.turmeric.eclipse.utils.plugin.WorkspaceUtil;
import org.ebayopensource.turmeric.eclipse.utils.ui.UIUtil;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.ide.IDE;
import org.eclipse.wst.wsdl.Definition;
import org.eclipse.xsd.XSDTypeDefinition;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class ServiceByUpdatingTypes extends AbstractTestCase {
	private static final String SVC_NAME = TypeLibSetUp.SVC_NAME2;

	private static final String SVC_NAME_ADMIN = TypeLibSetUp.SVC_NAME2;

	static DialogMonitor monitor;
	
	static String dataDirectory = WsdlUtilTest.getPluginOSPath(
			SoaTestConstants.PLUGIN_ID,"data");
	@BeforeClass
	public static void setUpBefore(){
		
		ZipExtractor zip = new ZipExtractor();
		zip.extract(dataDirectory+"/xsd.zip",dataDirectory +"/extractedData");
		
	}
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public  void setUp() throws Exception {
		
		
		// EBoxFunctionalTestHelper.ensureM2EcipseBeingInited();
		monitor = new DialogMonitor();
		monitor.startMonitoring();
		createService();
		createTypeLibrary();
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public  void tearDown() throws Exception {
		super.cleanupWorkspace();
		ServiceSetupCleanupValidate.cleanup(ServicesUtil
				.getAdminName(SVC_NAME));
	
		monitor.stopMonitoring();
		monitor = null;
	}

	public static void createService() throws Exception {
		System.out.println(" --- eBox Service name : " + SVC_NAME);
		TypeLibSetUp.setup();
		ServiceSetupCleanupValidate.cleanup(SVC_NAME);

		boolean b = ServiceFromBlankWsdlTest.createServiceFromBlankWsdl(
				SVC_NAME_ADMIN, SVC_NAME);
		Assert.assertTrue("Service creation fails --- ", b);

	}

	public static void createTypeLibrary() throws Exception {

		Assert.assertTrue(TypeLibSetUp.TYPELIBRARY_NAME1

		+ " -- TypeLibrary Creation failed", TLUtil.createTypeLibrary(
				TypeLibSetUp.TYPELIBRARY_NAME1, "1.0.0", "COMMON",
				TypeLibSetUp.TYPELIB_LOCATION));
		TLUtil.createType("EmployerType", TypeLibSetUp.TYPELIBRARY_NAME1,
				TypeLibSetUp.TYPELIBRARY_NAME1,
				SOAXSDTemplateSubType.SIMPLE,
				TstConstants.TEMPLATE_SIMPLE_TYPE,
				TstConstants.XSD_STRING);

	}

	/*
	 * Update the version of customerType
	 */
	@Test
	public void testUpdateVersion() throws Exception {
		String srcFile = WsdlUtilTest.getPluginOSPath(SoaTestConstants.PLUGIN_ID,
				"data/extractedData" + File.separator + "xsd");
		String destFile = TypeLibSetUp.TYPELIB_LOCATION + File.separator
				+ TypeLibSetUp.TYPELIBRARY_NAME1 + File.separator
				+ "meta-src" + File.separator + "types" + File.separator
				+ TypeLibSetUp.TYPELIBRARY_NAME1;
		// Copy the xsd with changed version
		srcFile = srcFile + File.separator + "EmployerType_2.xsd";
		destFile = destFile + File.separator + "EmployerType.xsd";
		FileUtils.copyFile(new File(srcFile), new File(destFile));
		File f = new File(srcFile);

		// Clean & Full Build
		WorkspaceUtil.getProject(TypeLibSetUp.TYPELIBRARY_NAME1).build(
				IncrementalProjectBuilder.CLEAN_BUILD,
				ProgressUtil.getDefaultMonitor(null));

		WorkspaceUtil.getProject(TypeLibSetUp.TYPELIBRARY_NAME1).build(
				IncrementalProjectBuilder.FULL_BUILD,
				ProgressUtil.getDefaultMonitor(null));

		// Verify TI.xml
		String s = FileUtils.readFileToString(f);
		Assert.assertTrue(
				"TypeInformation.xml is not updated with version 2.0.0 for EmployerType.xsd",
				s.contains("version=\"2.0.0\""));

		validatePOM(TypeLibSetUp.TYPELIBRARY_NAME1);
	}

	//@Ignore()
	@Test
	public void testWsdl() throws IOException, CoreException {
		UpdateTypeVersion update = new UpdateTypeVersion();
		try {
			// Update the wsdl
			IProject project = ProjectUtil.getProject(SVC_NAME_ADMIN);
			IFile wsdlFile = SOAServiceUtil.getWsdlFile(SVC_NAME_ADMIN);
			/*
			 * IFile wsdlFile = project
			 * .getFile("\\meta-src\\META-INF\\soa\\services\\wsdl\\" +
			 * EBoxTypeLibSetUp.SVC_NAME2 + "\\" + EBoxTypeLibSetUp.SVC_NAME2 +
			 * ".wsdl");
			 */
			IDE.openEditor(UIUtil.getActiveWorkBenchWindow().getActivePage(),
					wsdlFile);

			IEditorPart editorPart = UIUtil.getActiveEditor();
			TypeLibraryType typeLibInfo = SOAGlobalRegistryAdapter.getInstance()
					.getGlobalRegistry().getTypeLibrary(
							TypeLibSetUp.TYPELIBRARY_NAME1);
			LibraryType type = TypeLibraryUtil.getLibraryType("EmployerType",
					"1.0.0", typeLibInfo);
			// LibraryType type = SOAGlobalRegistryAdapter.getGlobalRegistry()
			// .getType("EmployerType");
			ArrayList<LibraryType> selectedTypes = new ArrayList<LibraryType>();

			selectedTypes.add(type);
			System.out.println("SelectedType[0] -- "
					+ selectedTypes.get(0).getName());
			Object adaptedObject = TypeLibraryUtil
					.getAdapterClassFromWTPEditors(editorPart);

			Definition definition = (Definition) adaptedObject;
			Map<LibraryType, XSDTypeDefinition> importedTypesMap = TypeLibraryActivator
					.getTypeLibraryTypes(definition);

			update.modifyWSDL(selectedTypes, definition, importedTypesMap,
					project);

			editorPart.doSave(ProgressUtil.getDefaultMonitor(null));

			WorkspaceUtil.getProject(SVC_NAME).build(
					IncrementalProjectBuilder.FULL_BUILD,
					ProgressUtil.getDefaultMonitor(null));

			WorkspaceUtil.getProject(SVC_NAME + "Impl").build(
					IncrementalProjectBuilder.FULL_BUILD,
					ProgressUtil.getDefaultMonitor(null));
			Assert.assertTrue("WSDL updation with changed version failed", true);

		} catch (Exception e) {
			e.printStackTrace();
			Assert.assertTrue("WSDL updation with changed version failed",
					false);
		}

		validateProjectArtifacts(SVC_NAME_ADMIN);

	}

	/*
	 * Assert Td.xml is generated with right dependency Assert EmployerType is
	 * added to the wsdl
	 */
	public void validateProjectArtifacts(String ServiceName)
			throws IOException, CoreException {
		IProject project = WorkspaceUtil.getProject(ServiceName);
		Assert.assertTrue(
				"TypeDepencies.xml didnot get generated",
				project.getFile(
						"meta-src" + File.separator + "META-INF"
								+ File.separator + ServiceName + File.separator
								+ "TypeDependencies.xml").exists());
		String sb = ServiceCreationFromExistingTypesTest1
				.readContentsFromIFile(SOAServiceUtil.getWsdlFile(ServiceName));

		Assert.assertTrue(
				"WSDL file is not inlined with imported type - EmployerType",
				sb.contains("EmployerType"));
		Assert.assertTrue("WSDL file is not inlined with updated type - age",
				sb.contains("age"));
		// Assert.assertTrue(
		// "WSDL file is not inlined with updated type - designation",
		// sb.contains("designation"));

		// Assert.assertTrue("WSDL file is not inlined with updated type - name",
		// sb.contains("name"));

	}

	/*
	 * Assert pom.xml has library dependency - SOA21TestTL1
	 */
	public void validatePOM(String typeLibName) throws CoreException,
			IOException {
		File pomXml = new File(TypeLibSetUp.SVC_LOCATION + File.separator
				+ typeLibName + File.separator + "pom.xml");
		String fileContents = FileUtils.readFileToString(pomXml);

		Assert.assertTrue("pom.xml doesnot contain library dependency "
				+ typeLibName, fileContents.contains(typeLibName));
	}
	

	@AfterClass
	public static void deInit(){
		
		ensureClean(dataDirectory +"/extractedData");
	}
	
}
