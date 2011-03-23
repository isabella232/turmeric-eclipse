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
package org.ebayopensource.turmeric.eclipse.functional.test.ft.typelib;

import java.io.File;
import java.io.IOException;

import junit.framework.Assert;

import org.apache.commons.io.FileUtils;
import org.ebayopensource.turmeric.eclipse.buildsystem.core.SOAGlobalRegistryAdapter;
import org.ebayopensource.turmeric.eclipse.config.repo.SOAConfigExtensionFactory.SOAXSDTemplateSubType;
import org.ebayopensource.turmeric.eclipse.functional.test.AbstractTestCase;
import org.ebayopensource.turmeric.eclipse.test.util.DialogMonitor;
import org.ebayopensource.turmeric.eclipse.test.util.FunctionalTestHelper;
import org.ebayopensource.turmeric.eclipse.test.utils.TLUtil;
import org.ebayopensource.turmeric.eclipse.utils.plugin.ProgressUtil;
import org.ebayopensource.turmeric.eclipse.utils.plugin.WorkspaceUtil;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;



/**
 * @author ksathiamurthy
 *
 */
public class DeleteTypeTest extends AbstractTestCase {

	static DialogMonitor monitor;
	static String functionDomain = "Blogs";
	
	SOAGlobalRegistryAdapter registryAdapter = null;
	
	@Before
	public void setUp() throws Exception {
	
		monitor = new DialogMonitor();
		monitor.startMonitoring();
		registryAdapter = SOAGlobalRegistryAdapter.getInstance();
		createTypeLibraryAndTypes();
		FunctionalTestHelper.ensureM2EcipseBeingInited();
	}
	
	@After
	public void tearDown() throws Exception {
		super.cleanupWorkspace();
		monitor.stopMonitoring();
		monitor = null;
	} 
	
	public void createTypeLibraryAndTypes() throws Exception {
		
		TypeLibSetUp.setup();
		Assert.assertTrue(TypeLibSetUp.TYPELIBRARY_NAME1
				+ " -- TypeLibrary Creation failed", TLUtil.createTypeLibrary(
						TypeLibSetUp.TYPELIBRARY_NAME1, "1.0.0", "COMMON",
				TypeLibSetUp.TYPELIB_LOCATION));
//		Clean and do a build
		WorkspaceUtil.getProject(TypeLibSetUp.TYPELIBRARY_NAME1).build(IncrementalProjectBuilder.CLEAN_BUILD, 
				ProgressUtil.getDefaultMonitor(null));
		
		WorkspaceUtil.getProject(TypeLibSetUp.TYPELIBRARY_NAME1).build(IncrementalProjectBuilder.FULL_BUILD, 
				ProgressUtil.getDefaultMonitor(null));
		Assert.assertTrue("Simple Type Creation failed", 
				TLUtil.createType("CustomerType", TypeLibSetUp.TYPELIBRARY_NAME1, 
						TypeLibSetUp.TYPELIBRARY_NAME1, SOAXSDTemplateSubType.SIMPLE,TstConstants.TEMPLATE_SIMPLE_TYPE, "string"));
//		Clean and do a build
		WorkspaceUtil.getProject(TypeLibSetUp.TYPELIBRARY_NAME1).build(IncrementalProjectBuilder.CLEAN_BUILD, 
				ProgressUtil.getDefaultMonitor(null));
		
		WorkspaceUtil.getProject(TypeLibSetUp.TYPELIBRARY_NAME1).build(IncrementalProjectBuilder.FULL_BUILD, 
				ProgressUtil.getDefaultMonitor(null));
		/*TLUtil.createType("CustomerType", TypeLibSetUp.TYPELIBRARY_NAME1,
				TypeLibSetUp.TYPELIBRARY_NAME1,
				SOAXSDTemplateSubType.SIMPLE,
				SoaTestConstants.TEMPLATE_SIMPLE_TYPE,
				SoaTestConstants.XSD_STRING); */
		
		//UIUtil.getActivePage().closeAllEditors(true);

		/*Assert.assertTrue(TypeLibSetUp.TYPELIBRARY_NAME2
				+ " -- TypeLibrary Creation failed", TLUtil.createTypeLibrary(
				TypeLibSetUp.TYPELIBRARY_NAME2, "1.0.0", "COMMON",
				TypeLibSetUp.TYPELIB_LOCATION));
		TLUtil.createType("ZipType", TypeLibSetUp.TYPELIBRARY_NAME2,
				TypeLibSetUp.TYPELIBRARY_NAME2,
				SOAXSDTemplateSubType.SIMPLE,
				SoaTestConstants.TEMPLATE_SIMPLE_TYPE,
				SoaTestConstants.XSD_STRING);
		final Display display = Display.getCurrent();
		System.out.println(display.getActiveShell());

		String typeNS = TLUtil.getTargetNamespace(TLUtil.functionDomain);
		TLUtil.importAction("TYPELIB", TypeLibSetUp.TYPELIBRARY_NAME1,
				"ImportType.xsd", "AlertType", typeNS);

		TLUtil.importAction("TYPELIB", TypeLibSetUp.TYPELIBRARY_NAME1,
				"ImportType.xsd", "ZipType", typeNS);
		TLUtil.importAction("TYPELIB", TypeLibSetUp.TYPELIBRARY_NAME1,
				"ImportType.xsd", "CustomerType", typeNS);

		UIUtil.getActivePage().closeAllEditors(true); */
	}
	
	
	/*
	 * Delete Type CustomerType.xsd
	 * The xsd has no imports or includes
	 * The xsd is not used by other xsd
	 */
	@Test
	@Ignore("failing")
	public void testDeleteTypeAction1() throws Exception {
	
		
				TLUtil.deleteAction(TypeLibSetUp.TYPELIBRARY_NAME1, 
						"CustomerType.xsd",TLUtil.getTargetNamespace(functionDomain));
		registryAdapter.invalidateRegistry();
		registryAdapter.getGlobalRegistry();
		//UIUtil.getActivePage().closeAllEditors(true);
		
//		Clean and do a build
		WorkspaceUtil.getProject(TypeLibSetUp.TYPELIBRARY_NAME1).build(IncrementalProjectBuilder.CLEAN_BUILD, 
				ProgressUtil.getDefaultMonitor(null));
		
		WorkspaceUtil.getProject(TypeLibSetUp.TYPELIBRARY_NAME1).build(IncrementalProjectBuilder.FULL_BUILD, 
				ProgressUtil.getDefaultMonitor(null));
		
		WorkspaceUtil.refresh();
		
		projectArtifactsAfterDeleteType();
	}
	
	
	public void projectArtifactsAfterDeleteType() throws IOException, InterruptedException {
		
//		Verify TI.xml
		String file = TypeLibSetUp.TYPELIB_LOCATION + File.separator + 
		TypeLibSetUp.TYPELIBRARY_NAME1 + File.separator + "gen-meta-src" + File.separator +
			"META-INF" + File.separator + TypeLibSetUp.TYPELIBRARY_NAME1 + File.separator + 
			"TypeInformation.xml";
		File f = new File(file);
		String fileContents = FileUtils.readFileToString(f);
		Assert.assertFalse("TypeInformation.xml should not contain Customer Type ", 
				fileContents.contains("CustomerType"));
		
//		Verify TypeDependencies.xml
		String TDXml = TypeLibSetUp.TYPELIB_LOCATION + File.separator + 
		TypeLibSetUp.TYPELIBRARY_NAME1 + File.separator + "meta-src" + File.separator +
		"META-INF" + File.separator + TypeLibSetUp.TYPELIBRARY_NAME1 + File.separator + 
		"TypeDependencies.xml";
		File fTDXml = new File(TDXml);
		String fileContents1 = FileUtils.readFileToString(fTDXml);
		Assert.assertFalse(TypeLibSetUp.TYPELIBRARY_NAME1 + "TypeDependencies.xml does not contain type " + 
				"dependency on SoftwareTypeLibrary", fileContents1.contains("CustomerType"));
		
//		Verify .episode deleted
		file = TypeLibSetUp.TYPELIB_LOCATION + File.separator + 
		TypeLibSetUp.TYPELIBRARY_NAME1 + File.separator + "gen-meta-src" + File.separator +
			"META-INF" + File.separator + TypeLibSetUp.TYPELIBRARY_NAME1 + File.separator + 
			"CustomerType.episode";
		f = new File(file);
		//Assert.assertFalse("CustomerType.episode is not deleted", f.exists());
		
//		Verify .java deleted
		file = TypeLibSetUp.TYPELIB_LOCATION + File.separator + 
		TypeLibSetUp.TYPELIBRARY_NAME1 + File.separator + "gen-meta-src" + File.separator +
		"META-INF" + File.separator + TypeLibSetUp.TYPELIBRARY_NAME1 + File.separator + 
		"CustomerType.java";
		f = new File(file);
		//Assert.assertFalse("CustomerType.java is not deleted", f.exists());
	}
}
