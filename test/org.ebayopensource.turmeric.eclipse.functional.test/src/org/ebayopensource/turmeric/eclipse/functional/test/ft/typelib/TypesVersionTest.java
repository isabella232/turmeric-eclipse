/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.functional.test.ft.typelib;

import static org.junit.Assume.assumeNoException;

import java.io.File;
import java.io.IOException;

import junit.framework.Assert;

import org.apache.commons.io.FileUtils;
import org.ebayopensource.turmeric.eclipse.config.repo.SOAConfigExtensionFactory.SOAXSDTemplateSubType;
import org.ebayopensource.turmeric.eclipse.functional.test.AbstractTestCase;
import org.ebayopensource.turmeric.eclipse.functional.test.SoaTestConstants;
import org.ebayopensource.turmeric.eclipse.test.util.ZipExtractor;
import org.ebayopensource.turmeric.eclipse.test.utils.TLUtil;
import org.ebayopensource.turmeric.eclipse.test.utils.WsdlUtilTest;
import org.ebayopensource.turmeric.eclipse.utils.plugin.ProgressUtil;
import org.ebayopensource.turmeric.eclipse.utils.plugin.WorkspaceUtil;
import org.ebayopensource.turmeric.eclipse.utils.ui.UIUtil;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.CoreException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;


public class TypesVersionTest extends AbstractTestCase {
	
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
		
		try {

			Assert.assertTrue(TypeLibSetUp.TYPELIBRARY_NAME1
					+ " -- TypeLibrary Creation failed", TLUtil.createTypeLibrary(
							TypeLibSetUp.TYPELIBRARY_NAME1, "1.0.0", "COMMON",
							TypeLibSetUp.TYPELIB_LOCATION));

		} catch (NoClassDefFoundError ex) {
			assumeNoException(ex);
		} 
		
	}
	
	@Test

	public void testVersionComplex() throws Exception {
		
		

		TLUtil.createType("EmployerType", TypeLibSetUp.TYPELIBRARY_NAME1,
				TypeLibSetUp.TYPELIBRARY_NAME1,
				SOAXSDTemplateSubType.COMPLEX,
				TstConstants.TEMPLATE_TURMERIC_COMPLEX,
				TstConstants.XSD_STRING);
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
		File f  = new File(TypeLibSetUp.TYPELIB_LOCATION + File.separatorChar
				+ "SOA21TestTL1" + File.separatorChar + "gen-meta-src"
				+ File.separatorChar + "META-INF" + File.separatorChar
				+ "SOA21TestTL1" + File.separatorChar + "TypeInformation.xml");

		IProject tlProject = WorkspaceUtil.getProject(TypeLibSetUp.TYPELIBRARY_NAME1);
		WorkspaceUtil.refresh(tlProject);
		// Clean & Full Build
		tlProject.build(
				IncrementalProjectBuilder.CLEAN_BUILD,
				ProgressUtil.getDefaultMonitor(null));

		tlProject.build(
				IncrementalProjectBuilder.FULL_BUILD,
				ProgressUtil.getDefaultMonitor(null));

		// Verify TI.xml
		String s = FileUtils.readFileToString(f);
		Assert.assertTrue(
				"TypeInformation.xml is not updated with version 2.0.0 for Address.xsd",
				s.contains("version=\"2.0.0\""));

	}

	@Test

	public void testVersionSimple() throws IOException, CoreException, InterruptedException {
		

		TLUtil.createType("CustomerType", TypeLibSetUp.TYPELIBRARY_NAME1,
				TypeLibSetUp.TYPELIBRARY_NAME1,
				SOAXSDTemplateSubType.SIMPLE,
				TstConstants.TEMPLATE_SIMPLE_TYPE,
				TstConstants.XSD_STRING);

		UIUtil.getActivePage().closeAllEditors(true);
		String srcFile = WsdlUtilTest.getPluginOSPath(SoaTestConstants.PLUGIN_ID,
				"data/extractedData" + File.separator + "xsd");
		String destFile = TypeLibSetUp.TYPELIB_LOCATION + File.separator
				+ TypeLibSetUp.TYPELIBRARY_NAME1 + File.separator
				+ "meta-src" + File.separator + "types" + File.separator
				+ TypeLibSetUp.TYPELIBRARY_NAME1;
		// Copy the xsd with changed version
		srcFile = srcFile + File.separator + "CustomerType_2.xsd";
		destFile = destFile + File.separator + "CustomerType.xsd";
		FileUtils.copyFile(new File(srcFile), new File(destFile));
		// File f = new File(destFile);
		IProject tlProject = WorkspaceUtil.getProject(TypeLibSetUp.TYPELIBRARY_NAME1);
		WorkspaceUtil.refresh(tlProject);
		// Clean & Full Build
		tlProject.build(
				IncrementalProjectBuilder.CLEAN_BUILD,
				ProgressUtil.getDefaultMonitor(null));

		tlProject.build(
				IncrementalProjectBuilder.FULL_BUILD,
				ProgressUtil.getDefaultMonitor(null));

		// Verify TI.xml

		File f = new File(TypeLibSetUp.TYPELIB_LOCATION
				+ File.separatorChar + "SOA21TestTL1" + File.separatorChar
				+ "gen-meta-src" + File.separatorChar + "META-INF"
				+ File.separatorChar + "SOA21TestTL1" + File.separatorChar
				+ "TypeInformation.xml");
		String s = FileUtils.readFileToString(f);
		Assert.assertTrue(
				"TypeInformation.xml is not updated with version 1.0.2 for CustomerType.xsd",
				s.contains("version=\"1.0.2\""));
	}

	@Test

	public void testVersionEnum() {
		

		TLUtil.createType("EmployeeCarType",
				TypeLibSetUp.TYPELIBRARY_NAME1,
				TypeLibSetUp.TYPELIBRARY_NAME1, SOAXSDTemplateSubType.ENUM,
				TstConstants.TEMPLATE_TURMERIC_ENUM,
				TstConstants.XSD_STRING);
		UIUtil.getActivePage().closeAllEditors(true);
		String srcFile = WsdlUtilTest.getPluginOSPath(SoaTestConstants.PLUGIN_ID,
				"data/extractedData" + File.separator + "xsd");
		String destFile = TypeLibSetUp.TYPELIB_LOCATION + File.separator
				+ TypeLibSetUp.TYPELIBRARY_NAME1 + File.separator
				+ "meta-src" + File.separator + "types" + File.separator
				+ TypeLibSetUp.TYPELIBRARY_NAME1;
		// Copy the xsd with changed version
		srcFile = srcFile + File.separator + "EmployeeCarType_2.xsd";
		destFile = destFile + File.separator + "EmployeeCarType.xsd";

		try {
			FileUtils.copyFile(new File(srcFile), new File(destFile));
			File f = new File(destFile);
			String s = FileUtils.readFileToString(f);
			IProject tlProject = WorkspaceUtil.getProject(TypeLibSetUp.TYPELIBRARY_NAME1);
			WorkspaceUtil.refresh(tlProject);
			// Clean & Full Build
			tlProject.build(
					IncrementalProjectBuilder.CLEAN_BUILD,
					ProgressUtil.getDefaultMonitor(null));
			tlProject.build(
					IncrementalProjectBuilder.FULL_BUILD,
					ProgressUtil.getDefaultMonitor(null));
			f = new File(TypeLibSetUp.TYPELIB_LOCATION + File.separatorChar
					+ "SOA21TestTL1" + File.separatorChar + "gen-meta-src"
					+ File.separatorChar + "META-INF" + File.separatorChar
					+ "SOA21TestTL1" + File.separatorChar + "TypeInformation.xml");
			s = FileUtils.readFileToString(f);
			Assert.assertTrue(
					"TypeInformation.xml is not updated with version 1.1.2 for CustomerType.xsd",
					s.contains("version=\"1.1.2\""));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		

		
	}

	
	
	@After
	public void deInit() throws CoreException, InterruptedException{
		
		super.cleanupWorkspace();
		Thread.sleep(15000);
	}
	
	@AfterClass
	public static void deInitAfter(){
		
		ensureClean(dataDirectory +"/extractedData");
	}

}
