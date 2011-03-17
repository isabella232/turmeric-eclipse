/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.functional.test.ft.typelib;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.ebayopensource.turmeric.eclipse.buildsystem.core.SOAGlobalRegistryAdapter;
import org.ebayopensource.turmeric.eclipse.config.repo.SOAConfigExtensionFactory.SOAXSDTemplateSubType;
import org.ebayopensource.turmeric.eclipse.functional.test.AbstractTestCase;
import org.ebayopensource.turmeric.eclipse.functional.test.SoaTestConstants;
import org.ebayopensource.turmeric.eclipse.test.util.DialogMonitor;
import org.ebayopensource.turmeric.eclipse.test.util.ZipExtractor;
import org.ebayopensource.turmeric.eclipse.test.utils.TLUtil;
import org.ebayopensource.turmeric.eclipse.test.utils.WsdlUtilTest;
import org.ebayopensource.turmeric.tools.library.SOATypeRegistry;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import static org.junit.Assume.*;



public class ImportTypeTest extends AbstractTestCase {

	static DialogMonitor monitor;
	SOAGlobalRegistryAdapter registryAdapter;
	SOATypeRegistry typeRegistry;
	
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
		
		
		monitor = new DialogMonitor();
		monitor.startMonitoring();
		try {
			registryAdapter = SOAGlobalRegistryAdapter.getInstance();
			typeRegistry = registryAdapter.getGlobalRegistry();
			TypeLibSetUp.setup();
		} catch (Exception ex) {
			assumeNoException(ex);
		}
		assertTrue(TypeLibSetUp.TYPELIBRARY_NAME1
				+ " -- TypeLibrary Creation failed", TLUtil.createTypeLibrary(
				TypeLibSetUp.TYPELIBRARY_NAME1, "1.0.0", "COMMON",
				TypeLibSetUp.TYPELIB_LOCATION));
		assertTrue(TypeLibSetUp.TYPELIBRARY_NAME2
				+ " -- TypeLibrary Creation failed", TLUtil.createTypeLibrary(
				TypeLibSetUp.TYPELIBRARY_NAME2, "1.0.0", "COMMON",
				TypeLibSetUp.TYPELIB_LOCATION));
		TLUtil.createType("CustomerType", TypeLibSetUp.TYPELIBRARY_NAME1,
				TypeLibSetUp.TYPELIBRARY_NAME1,
				SOAXSDTemplateSubType.SIMPLE,
				TstConstants.TEMPLATE_SIMPLE_TYPE,
				TstConstants.XSD_STRING);
		TLUtil.createType("AlertType", TypeLibSetUp.TYPELIBRARY_NAME1,
				TypeLibSetUp.TYPELIBRARY_NAME1,
				SOAXSDTemplateSubType.SIMPLE,
				TstConstants.TEMPLATE_SIMPLE_TYPE,
				TstConstants.XSD_STRING);
		TLUtil.createType("ZipType", TypeLibSetUp.TYPELIBRARY_NAME2,
				TypeLibSetUp.TYPELIBRARY_NAME2,
				SOAXSDTemplateSubType.SIMPLE,
				TstConstants.TEMPLATE_SIMPLE_TYPE,
				TstConstants.XSD_STRING);
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

	/*
	 * Import from SOACommonTypeLibrary - ErrorMessage
	 */
	//@Ignore("JIRA 756")
	@Test
	public void testImportAction1() throws Exception {
		
		
		Thread.sleep(20000);	
		registryAdapter.invalidateRegistry();
		registryAdapter.getGlobalRegistry();
			assertTrue("Import Action in ImportType.xsd fails", TLUtil
				.importAction("TYPELIB", TypeLibSetUp.TYPELIBRARY_NAME1,
						"ImportType.xsd", "ErrorMessage", 
						"http://www.ebayopensource.org/turmeric/common/v1/types"));
	}

	/*
	 * Import from SOA21TestTL1 - CustomerType
	 */
	//@Ignore("JIRA 756")
	@Test
	public void testImportAction2() throws Exception {
		Thread.sleep(20000);
		registryAdapter.invalidateRegistry();
		registryAdapter.getGlobalRegistry();
		assertTrue("Import Action in ImportType.xsd fails", TLUtil
				.importAction("TYPELIB", TypeLibSetUp.TYPELIBRARY_NAME1,
						"ImportType.xsd", "CustomerType", 
						TLUtil.getTargetNamespace(TLUtil.functionDomain)));
	}

	/*
	 * Import from SOA21TestTL1 - AlertType
	 */
	//@Ignore("JIRA 756")
	@Test
	public void testImportAction3() throws Exception {
		Thread.sleep(20000);	
		registryAdapter.invalidateRegistry();
		registryAdapter.getGlobalRegistry();
		assertTrue("Import Action in ImportType.xsd fails", TLUtil
				.importAction("TYPELIB", TypeLibSetUp.TYPELIBRARY_NAME1,
						"ImportType.xsd", "AlertType", 
						TLUtil.getTargetNamespace(TLUtil.functionDomain)));
	}

	/*
	 * Import from SOA21TestTL2 - ZipType
	 */
	//@Ignore("JIRA 756")
	@Test
	public void testImportAction4() throws Exception {
		Thread.sleep(20000);	
		registryAdapter.invalidateRegistry();
		registryAdapter.getGlobalRegistry();
		assertTrue("Import Action in ImportType.xsd fails", TLUtil
				.importAction("TYPELIB", TypeLibSetUp.TYPELIBRARY_NAME1,
						"ImportType.xsd", "ZipType", 
						TLUtil.getTargetNamespace(TLUtil.functionDomain)));
	}

	/*
	 * public void testImportfromGTR() throws Exception {
	 * SOAGlobalRegistryAdapter.invalidateRegistry();
	 * SOAGlobalRegistryAdapter.getGlobalRegistry();
	 * Assert.assertTrue("Import Action in ImportType.xsd fails",
	 * TLUtil.importAction("TYPELIB", EBoxTypeLibSetUp.TYPELIBRARY_COMMON,
	 * "ImportType.xsd", "CustomerType")); }
	 * 
	 * /* Import from DomainTypeLibrary - DomainType
	 */
	/*
	 * public void testImportfromDiffTL() throws Exception {
	 * SOAGlobalRegistryAdapter.invalidateRegistry();
	 * SOAGlobalRegistryAdapter.getGlobalRegistry();
	 * Assert.assertTrue("Import Action in ImportType.xsd fails",
	 * TLUtil.importAction("TYPELIB", EBoxTypeLibSetUp.TYPELIBRARY_COMMON,
	 * "CommonType.xsd", "DomainType" )); }
	 */

	public void validatePOM() throws IOException {

		// if eBox Verify pom.xml
		File pomXml = new File(TypeLibSetUp.TYPELIB_LOCATION
				+ File.separator + TypeLibSetUp.TYPELIBRARY_NAME1
				+ File.separator + "pom.xml");
		String fileContents = FileUtils.readFileToString(pomXml);

		assertTrue(TypeLibSetUp.TYPELIBRARY_NAME1
				+ ".xml does not contain type "
				+ "library dependency on SOACommonTypeLibrary",
				fileContents.contains("SOACommonTypeLibrary"));
		assertTrue("pom.xml does not contain type "
				+ "library dependency on SOA21TestTL1",
				fileContents.contains("SOA21TestTL1"));
		assertTrue("pom.xml does not contain type "
				+ "library dependency on SOA21TestTL1",
				fileContents.contains("SOA21TestTL2"));
	}

	/*
	 * Verify TypeDependencies.xml
	 */
	public void validateProjectArtifactsAfterImport() throws IOException {

		// Verify TypeDependencies.xml
		String TDXml = TypeLibSetUp.TYPELIB_LOCATION + File.separator
				+ TypeLibSetUp.TYPELIBRARY_NAME1 + File.separator
				+ "meta-src" + File.separator + "META-INF" + File.separator
				+ TypeLibSetUp.TYPELIBRARY_NAME1 + File.separator
				+ "TypeDependencies.xml";
		File fTDXml = new File(TDXml);
		String fileContents = FileUtils.readFileToString(fTDXml);
		assertTrue(TypeLibSetUp.TYPELIBRARY_NAME1
				+ "TypeDependencies.xml does not contain type "
				+ "dependency on HardwareTypeLibrary",
				fileContents.contains("SOACommonTypeLibrary"));

		assertTrue(TypeLibSetUp.TYPELIBRARY_NAME1
				+ "TypeDependencies.xml does not contain type "
				+ "dependency on HardwareTypeLibrary",
				fileContents.contains("AlertType"));

		assertTrue(TypeLibSetUp.TYPELIBRARY_NAME1
				+ "TypeDependencies.xml does not contain type "
				+ "dependency on SOACommonTypeLibrary",
				fileContents.contains("ErrorMessage"));

		assertTrue(TypeLibSetUp.TYPELIBRARY_NAME1
				+ "TypeDependencies.xml does not contain type "
				+ "dependency on SOA21TestTestTL1",
				fileContents.contains("SOA21TestTL1"));

		assertTrue(TypeLibSetUp.TYPELIBRARY_NAME1
				+ "TypeDependencies.xml does not contain type "
				+ "dependency on CustomerType",
				fileContents.contains("CustomerType"));

		assertTrue(TypeLibSetUp.TYPELIBRARY_NAME1
				+ "TypeDependencies.xml does not contain type "
				+ "dependency on SOA21TestTestTL2",
				fileContents.contains("SOA21TestTL2"));

		assertTrue(TypeLibSetUp.TYPELIBRARY_NAME1
				+ "TypeDependencies.xml does not contain type "
				+ "dependency on ZipType", fileContents.contains("ZipType"));
	}
	
	
	@AfterClass
	public static void deInit(){
		
		ensureClean(dataDirectory +"/extractedData");
	}

}
