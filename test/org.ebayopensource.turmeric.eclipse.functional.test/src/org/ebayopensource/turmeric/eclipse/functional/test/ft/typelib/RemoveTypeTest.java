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

import static org.junit.Assume.assumeNoException;

import java.io.File;
import java.io.IOException;

import junit.framework.Assert;

import org.apache.commons.io.FileUtils;
import org.ebayopensource.turmeric.eclipse.core.resources.constants.SOAXSDTemplateSubType;
import org.ebayopensource.turmeric.eclipse.functional.test.AbstractTestCase;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.SOAGlobalRegistryAdapter;
import org.ebayopensource.turmeric.eclipse.test.util.DialogMonitor;
import org.ebayopensource.turmeric.eclipse.test.utils.TLUtil;
import org.ebayopensource.turmeric.eclipse.utils.ui.UIUtil;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;


/**
 * @author vyaramala
 * 
 */

public class RemoveTypeTest extends AbstractTestCase {

	//static DialogMonitor monitor;
	SOAGlobalRegistryAdapter registry;
	DialogMonitor monitor;
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	@Ignore
	public  void setUp() throws Exception {
	
		
		//monitor = new DialogMonitor();
		//monitor.startMonitoring();
		TypeLibSetUp.setup();
		monitor = new DialogMonitor();
		monitor.startMonitoring();
		registry = SOAGlobalRegistryAdapter.getInstance();
		try {
		Assert.assertTrue(TypeLibSetUp.TYPELIBRARY_NAME1
				+ " -- TypeLibrary Creation failed", TLUtil.createTypeLibrary(
				TypeLibSetUp.TYPELIBRARY_NAME1, "1.0.0", "COMMON",
				TypeLibSetUp.TYPELIB_LOCATION));
		} catch (NoClassDefFoundError ex) {
			assumeNoException(ex);
		}
		
		TLUtil.createType("ImportType", TypeLibSetUp.TYPELIBRARY_NAME1,
				TypeLibSetUp.TYPELIBRARY_NAME1,
				SOAXSDTemplateSubType.SIMPLE,
				TstConstants.TEMPLATE_SIMPLE_TYPE,
				TstConstants.XSD_STRING);
		TLUtil.createType("CustomerType", TypeLibSetUp.TYPELIBRARY_NAME1,
				TypeLibSetUp.TYPELIBRARY_NAME1,
				SOAXSDTemplateSubType.SIMPLE,
				TstConstants.TEMPLATE_SIMPLE_TYPE,
				TstConstants.XSD_STRING);
		closeEditors();
		TLUtil.createType("AlertType", TypeLibSetUp.TYPELIBRARY_NAME1,
				TypeLibSetUp.TYPELIBRARY_NAME1,
				SOAXSDTemplateSubType.SIMPLE,
				TstConstants.TEMPLATE_SIMPLE_TYPE,
				TstConstants.XSD_STRING);
		closeEditors();
		

		Assert.assertTrue(TypeLibSetUp.TYPELIBRARY_NAME2
				+ " -- TypeLibrary Creation failed", TLUtil.createTypeLibrary(
				TypeLibSetUp.TYPELIBRARY_NAME2, "1.0.0", "COMMON",
				TypeLibSetUp.TYPELIB_LOCATION));
		TLUtil.createType("ZipType", TypeLibSetUp.TYPELIBRARY_NAME2,
				TypeLibSetUp.TYPELIBRARY_NAME2,
				SOAXSDTemplateSubType.SIMPLE,
				TstConstants.TEMPLATE_SIMPLE_TYPE,
				TstConstants.XSD_STRING);
		closeEditors();


		
		
		String typeNS = TLUtil.getTargetNamespace(TLUtil.functionDomain);
		TLUtil.importAction("TYPELIB", TypeLibSetUp.TYPELIBRARY_NAME1,
				"ImportType.xsd", "AlertType", typeNS);

		TLUtil.importAction("TYPELIB", TypeLibSetUp.TYPELIBRARY_NAME1,
				"ImportType.xsd", "ZipType", typeNS);
		TLUtil.importAction("TYPELIB", TypeLibSetUp.TYPELIBRARY_NAME1,
				"ImportType.xsd", "CustomerType", typeNS);
		closeEditors();
	}
	
	/**
	 * @throws java.lang.Exception
	 */
	@After
	public  void tearDown() throws Exception {
		
		super.cleanupWorkspace();
		monitor.stopMonitoring();
		//monitor = null;
	}

	

	/*
	 * Remove Type - AlertType from jar
	 */
	@Test
    @Ignore
	public void testRemoveTypeAction1() throws Exception {
		System.out.println("-----------------------Test Name: testRemoveTypeAction1()---------------------");
		Assert.assertTrue("Remove Action in ImportType.xsd fails", TLUtil
				.removeAction("TYPELIB", TypeLibSetUp.TYPELIBRARY_NAME1,

				"ImportType.xsd", TypeLibSetUp.TYPELIBRARY_NAME1,
						"AlertType", TLUtil.getTargetNamespace(TLUtil.functionDomain)));

		registry.invalidateRegistry();
		registry.getGlobalRegistry();

		// validatePOM(EBoxTypeLibSetUp.TYPELIBRARY_NAME1);
		UIUtil.getActivePage().closeAllEditors(true);
	}

	/*
	 * Remove Type - ZipType from other TL (SOA21TestTL2)
	 */
	@Test
	@Ignore
	public void testRemoveTypeAction2() throws Exception {
		System.out.println("-----------------------Test Name: testRemoveTypeAction2()-----------------------");
		Assert.assertTrue("Remove Action in ImportType.xsd fails", TLUtil
				.removeAction("TYPELIB", TypeLibSetUp.TYPELIBRARY_NAME1,
						"ImportType.xsd", TypeLibSetUp.TYPELIBRARY_NAME2,
						"ZipType", TLUtil.getTargetNamespace(TLUtil.functionDomain)));
		
		registry.invalidateRegistry();
		registry.getGlobalRegistry();

		// validatePOM(EBoxTypeLibSetUp.TYPELIBRARY_NAME2);

		UIUtil.getActivePage().closeAllEditors(true);
	}

	/*
	 * Remove Type - CustomerType from same TL (SOA21TestTL1)
	 */
	@Test
	@Ignore
	public void testRemoveTypeAction3() throws Exception {
		System.out.println("-----------------------Test Name: testRemoveTypeAction1()-----------------------");
		Assert.assertTrue("Remove Action in ImportType.xsd fails", TLUtil
				.removeAction("TYPELIB", TypeLibSetUp.TYPELIBRARY_NAME1,
						"ImportType.xsd", TypeLibSetUp.TYPELIBRARY_NAME1,
						"CustomerType", TLUtil.getTargetNamespace(TLUtil.functionDomain)));
		registry.invalidateRegistry();
		registry.getGlobalRegistry();

		// validatePOM(EBoxTypeLibSetUp.TYPELIBRARY_NAME1);

		UIUtil.getActivePage().closeAllEditors(true);
	}

	public void validatePOM(String typeLibraryName) throws IOException {

		File pomXml = new File(TypeLibSetUp.TYPELIB_LOCATION
				+ File.separator + typeLibraryName + File.separator + "pom.xml");
		String fileContents = FileUtils.readFileToString(pomXml);

		// Assert.assertFalse(EBoxTypeLibSetUp.TYPELIBRARY_NAME1 +
		// ".xml does not contain type " +
		// "library dependency on SOA21TestTL1",
		// fileContents.contains("SOA21TestTL1"));

		Assert.assertFalse("pom.xml does not contain type "
				+ "library dependency on " + typeLibraryName,
				fileContents.contains(typeLibraryName));
	}


	public void projectArtifactsAfterRemoveType() throws Exception {

		String sb = "<ns2:type version=\"1.0.0\" name=\"ImportType\">"
				+ "\n"
				+ "<ns2:referredTypeLibrary version=\"1.0.0\" name=\"SoftwareTypeLibrary\">"
				+ "\n"
				+ "<ns2:referredType version=\"1.0.0\" name=\"AlertType\"/>"
				+ "\n" + "</ns2:referredTypeLibrary>" + "\n" + "</ns2:type>";

		// Verify TypeDependencies.xml
		String TDXml = TypeLibSetUp.TYPELIB_LOCATION + File.separator
				+ TypeLibSetUp.TYPELIBRARY_NAME1 + File.separator
				+ "meta-src" + File.separator + "META-INF" + File.separator
				+ TypeLibSetUp.TYPELIBRARY_NAME1 + File.separator
				+ "TypeDependencies.xml";
		File fTDXml = new File(TDXml);
		String fileContents = FileUtils.readFileToString(fTDXml);
		Assert.assertFalse(TypeLibSetUp.TYPELIBRARY_NAME1
				+ "TypeDependencies.xml does not contain type "
				+ "dependency on SoftwareTypeLibrary",
				fileContents.contains(sb));
	}
	
	@Test
	public void doNothingTest() {
		//this meant to be removed once we fix all the issues in this test, 
		//otherwise the entire CI will be blocked for at least two hours.
		System.out.println("This dummy test is to ensure CI job would not throw no test to run error");
	}

}
