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

import org.apache.commons.io.FileUtils;
import org.ebayopensource.turmeric.eclipse.buildsystem.core.SOAGlobalRegistryAdapter;
import org.ebayopensource.turmeric.eclipse.config.repo.SOAConfigExtensionFactory.SOAXSDTemplateSubType;
import org.ebayopensource.turmeric.eclipse.functional.test.AbstractTestCase;
import org.ebayopensource.turmeric.eclipse.functional.test.SoaTestConstants;
import org.ebayopensource.turmeric.eclipse.test.util.ZipExtractor;
import org.ebayopensource.turmeric.eclipse.test.utils.TLUtil;
import org.ebayopensource.turmeric.eclipse.test.utils.WsdlUtilTest;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.junit.Assume.*;

/**
 * @author vyaramala
 * 
 */
public class CreateTypeTest extends AbstractTestCase {
	SOAGlobalRegistryAdapter registryAdapter = null;

	static String dataDirectory = WsdlUtilTest.getPluginOSPath(
			SoaTestConstants.PLUGIN_ID,"data");
	@BeforeClass
	public static void setUp(){
		
		ZipExtractor zip = new ZipExtractor();
		zip.extract(dataDirectory+"/xsd.zip",dataDirectory +"/extractedData");
		
	}
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUpBeforeClass() throws Exception {

		TypeLibSetUp.setup();
		registryAdapter = SOAGlobalRegistryAdapter.getInstance();

		setupCreateTypeLibrary();
		setupCreateTypeLibrary1();
	}

	/*
	 * Create a type library with name = TYPELIBRARY_NAME1
	 */

	public void setupCreateTypeLibrary() throws Exception {

		assertTrue(TypeLibSetUp.TYPELIBRARY_COMMON
				+ " -- TypeLibrary Creation failed", TLUtil.createTypeLibrary(
				TypeLibSetUp.TYPELIBRARY_COMMON, "1.0.0",
				TstConstants.TEMPLATE_COMMON, TypeLibSetUp.TYPELIB_LOCATION));
		TLUtil.createType("CommonType", TypeLibSetUp.TYPELIBRARY_COMMON,
				TypeLibSetUp.TYPELIBRARY_COMMON, SOAXSDTemplateSubType.SIMPLE,
				TstConstants.TEMPLATE_SIMPLE_TYPE, TstConstants.XSD_STRING);
		TLUtil.createType("ErrorMessage", TypeLibSetUp.TYPELIBRARY_COMMON,
				TypeLibSetUp.TYPELIBRARY_COMMON, SOAXSDTemplateSubType.SIMPLE,
				TstConstants.TEMPLATE_SIMPLE_TYPE, TstConstants.XSD_STRING);
		TLUtil.createType("CustomerType", TypeLibSetUp.TYPELIBRARY_COMMON,
				TypeLibSetUp.TYPELIBRARY_COMMON, SOAXSDTemplateSubType.SIMPLE,
				TstConstants.TEMPLATE_SIMPLE_TYPE, TstConstants.XSD_STRING);
		closeEditors();
	}

	public void setupCreateTypeLibrary1() throws Exception {
		assertTrue(TypeLibSetUp.TYPELIBRARY_NAME1
				+ " -- TypeLibrary Creation failed", TLUtil.createTypeLibrary(
				TypeLibSetUp.TYPELIBRARY_NAME1, "1.0.0", "COMMON",
				TypeLibSetUp.TYPELIB_LOCATION));

		assertTrue(TypeLibSetUp.TYPELIBRARY_NAME2
				+ " -- TypeLibrary Creation failed", TLUtil.createTypeLibrary(
				TypeLibSetUp.TYPELIBRARY_NAME2, "1.0.0", "COMMON",
				TypeLibSetUp.TYPELIB_LOCATION));

	}

	@Test
	public void testCreateSimpleStringType() throws IOException {

		assertTrue("Simple Type Creation failed", TLUtil.createType(
				"CustomerType", TypeLibSetUp.TYPELIBRARY_NAME1,
				TypeLibSetUp.TYPELIBRARY_NAME1, SOAXSDTemplateSubType.SIMPLE,
				TstConstants.TEMPLATE_SIMPLE_TYPE, TstConstants.XSD_STRING));

	}

	@Test
	public void testCreateStringType() throws IOException {

		assertTrue("Simple Type Creation failed", TLUtil.createType(
				"ErrorMessage", TypeLibSetUp.TYPELIBRARY_NAME1,
				TypeLibSetUp.TYPELIBRARY_NAME1, SOAXSDTemplateSubType.SIMPLE,
				TstConstants.TEMPLATE_SIMPLE_TYPE, TstConstants.XSD_STRING));

	}

	@Test
	public void testCreateSimpleIntType() throws IOException {

		assertTrue("Simple Type Creation failed", TLUtil.createType(
				"CustomerPhoneType", TypeLibSetUp.TYPELIBRARY_NAME1,
				TypeLibSetUp.TYPELIBRARY_NAME1, SOAXSDTemplateSubType.SIMPLE,
				TstConstants.TEMPLATE_SIMPLE_TYPE, TstConstants.XSD_INT));

	}

	@Test
	public void testCreateSimpleBooleanType() throws IOException {

		assertTrue("Simple Type Creation failed", TLUtil.createType(
				"CustomerValidType", TypeLibSetUp.TYPELIBRARY_NAME1,
				TypeLibSetUp.TYPELIBRARY_NAME1, SOAXSDTemplateSubType.SIMPLE,
				TstConstants.TEMPLATE_SIMPLE_TYPE, TstConstants.XSD_BOOLEAN));

	}

	@Test
	public void testCreateSimpleDateType() throws IOException {
		assertTrue("Simple Type Creation failed", TLUtil.createType(
				"CustomerDOBType", TypeLibSetUp.TYPELIBRARY_NAME1,
				TypeLibSetUp.TYPELIBRARY_NAME1, SOAXSDTemplateSubType.SIMPLE,
				TstConstants.TEMPLATE_SIMPLE_TYPE, TstConstants.XSD_DATE));

	}

	@Test
	public void testCreateSimpleDateTimeType() throws IOException {

		assertTrue("Simple Type Creation failed", TLUtil.createType(
				"CustomerDOBTimeType", TypeLibSetUp.TYPELIBRARY_NAME1,
				TypeLibSetUp.TYPELIBRARY_NAME1, SOAXSDTemplateSubType.SIMPLE,
				TstConstants.TEMPLATE_SIMPLE_TYPE, TstConstants.XSD_DATE_TIME));
	}

	@Test
	public void testCreateEnumType() throws IOException {
		String srcFile = WsdlUtilTest.getPluginOSPath(
				SoaTestConstants.PLUGIN_ID, "data/extractedData" + File.separator
						+ "xsd");
		String destFile = TypeLibSetUp.TYPELIB_LOCATION + File.separator
				+ TypeLibSetUp.TYPELIBRARY_NAME1 + File.separator + "meta-src"
				+ File.separator + "types" + File.separator
				+ TypeLibSetUp.TYPELIBRARY_NAME1;
		assertTrue("Enum Type Creation failed", TLUtil.createType(
				"EmployeeCarType", TypeLibSetUp.TYPELIBRARY_NAME1,
				TypeLibSetUp.TYPELIBRARY_NAME1, SOAXSDTemplateSubType.ENUM,
				TstConstants.TEMPLATE_TURMERIC_ENUM, TstConstants.XSD_STRING));
		srcFile = srcFile + File.separator + "EmployeeCarType.xsd";
		destFile = destFile + File.separator + "EmployeeCarType.xsd";
		FileUtils.copyFile(new File(srcFile), new File(destFile));

	}

	@Test
	public void testCreateTempTypeForImport() {
		assertTrue("Type to be used for Import action failed",
				TLUtil.createType("ImportType", TypeLibSetUp.TYPELIBRARY_NAME1,
						TypeLibSetUp.TYPELIBRARY_NAME1,
						SOAXSDTemplateSubType.SIMPLE,
						TstConstants.TEMPLATE_SIMPLE_TYPE,
						TstConstants.XSD_STRING));
	}

	@Test
	public void testCreateTypeForTL2() {
		assertTrue("Type to be used for Import action failed",
				TLUtil.createType("ZipType", TypeLibSetUp.TYPELIBRARY_NAME2,
						TypeLibSetUp.TYPELIBRARY_NAME2,
						SOAXSDTemplateSubType.ENUM,
						TstConstants.TEMPLATE_TURMERIC_ENUM,
						TstConstants.XSD_STRING));

	}

	// @Ignore("currently failing")
	@Test
	public void createComplexType3() throws IOException {
		String srcFile = WsdlUtilTest.getPluginOSPath(
				SoaTestConstants.PLUGIN_ID, "data/extractedData" + File.separator
						+ "xsd");
		String destFile = TypeLibSetUp.TYPELIB_LOCATION + File.separator
				+ TypeLibSetUp.TYPELIBRARY_NAME1 + File.separator + "meta-src"
				+ File.separator + "types" + File.separator
				+ TypeLibSetUp.TYPELIBRARY_NAME1;
		assertTrue("Complex Type Creation failed", TLUtil.createType("Address",
				TypeLibSetUp.TYPELIBRARY_NAME1, TypeLibSetUp.TYPELIBRARY_NAME1,
				SOAXSDTemplateSubType.COMPLEX,
				TstConstants.TEMPLATE_TURMERIC_COMPLEX, null));

		// Copy .xsd from test-data to project and build
		srcFile = srcFile + File.separator + "Address_2.xsd";
		destFile = destFile + File.separator + "Address.xsd";
		FileUtils.copyFile(new File(srcFile), new File(destFile));
	}

	/*
	 * Create Duplicate Type - within same TL
	 */
	@Test
	public void testCreateDuplicateType1() throws Exception {
		registryAdapter.invalidateRegistry();
		registryAdapter.getGlobalRegistry();
		TLUtil.createType("CustomerType", TypeLibSetUp.TYPELIBRARY_NAME1,
				TypeLibSetUp.TYPELIBRARY_NAME1, SOAXSDTemplateSubType.SIMPLE,
				TstConstants.TEMPLATE_SIMPLE_TYPE, TstConstants.XSD_STRING);

		assertFalse("Duplicate Type Creation did not fail", TLUtil.createType(
				"CustomerType", TypeLibSetUp.TYPELIBRARY_NAME1,
				TypeLibSetUp.TYPELIBRARY_NAME1, SOAXSDTemplateSubType.SIMPLE,
				TstConstants.TEMPLATE_SIMPLE_TYPE, TstConstants.XSD_STRING));
	}

	/*
	 * Create Duplicate Type - within TL in ws
	 */
	@Test
	public void testCreateDuplicateType2() throws Exception {

		registryAdapter.invalidateRegistry();
		registryAdapter.getGlobalRegistry();
		TLUtil.createType("ImportType", TypeLibSetUp.TYPELIBRARY_NAME1,
				TypeLibSetUp.TYPELIBRARY_NAME1, SOAXSDTemplateSubType.ENUM,
				TstConstants.TEMPLATE_TURMERIC_ENUM, TstConstants.XSD_STRING);

		assertFalse("Duplicate Type Creation did not fail", TLUtil.createType(
				"ImportType", TypeLibSetUp.TYPELIBRARY_NAME1,
				TypeLibSetUp.TYPELIBRARY_NAME1, SOAXSDTemplateSubType.SIMPLE,
				TstConstants.TEMPLATE_TURMERIC_ENUM, TstConstants.XSD_STRING)); // TypeCategory.ENUM

	}

	/*
	 * Create Duplicate Type - in jar
	 */
	@Test
	public void testCreateDuplicateType3() throws Exception {
		registryAdapter.invalidateRegistry();
		registryAdapter.getGlobalRegistry();
		TLUtil.createType("cacheType", TypeLibSetUp.TYPELIBRARY_NAME1,
				TypeLibSetUp.TYPELIBRARY_NAME1, SOAXSDTemplateSubType.COMPLEX,
				TstConstants.TEMPLATE_TURMERIC_COMPLEX, TstConstants.XSD_STRING);
		assertFalse("Duplicate Type Creation didnot fail",

				TLUtil.createType("cacheType", TypeLibSetUp.TYPELIBRARY_NAME1,
						TypeLibSetUp.TYPELIBRARY_NAME1,
						SOAXSDTemplateSubType.COMPLEX,
						TstConstants.TEMPLATE_TURMERIC_COMPLEX,
						TstConstants.XSD_STRING));
	}
	
	@AfterClass
	public static void deInit(){
		
		ensureClean(dataDirectory +"/extractedData");
	}

}
