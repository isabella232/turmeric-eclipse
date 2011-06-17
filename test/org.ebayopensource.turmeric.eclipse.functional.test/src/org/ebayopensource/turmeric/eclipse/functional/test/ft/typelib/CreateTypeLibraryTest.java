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

import static org.junit.Assert.assertTrue;
import static org.junit.Assume.assumeNoException;
import static org.junit.Assume.assumeTrue;

import org.ebayopensource.turmeric.eclipse.core.resources.constants.SOAXSDTemplateSubType;
import org.ebayopensource.turmeric.eclipse.functional.test.AbstractTestCase;
import org.ebayopensource.turmeric.eclipse.test.utils.TLUtil;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

/**
 * @author vyaramala
 * 
 */
public class CreateTypeLibraryTest extends AbstractTestCase {

	/**
	 * @throws java.lang.Exception
	 */
	@Override
	@Before
	public void setUpBeforeClass() throws Exception {

		createTypeLibrary1();
		createTypeLibrary2();
	}

	/*
	 * Create a type library with name = TYPELIBRARY_NAME1
	 */
	private static void createTypeLibrary1() throws Exception {
		TypeLibSetUp.setup();
		try {
			assertTrue(TypeLibSetUp.TYPELIBRARY_NAME1
					+ " -- TypeLibrary Creation failed",
					TLUtil.createTypeLibrary(TypeLibSetUp.TYPELIBRARY_NAME1,
							"1.0.0", TstConstants.TEMPLATE_COMMON,
							TypeLibSetUp.TYPELIB_LOCATION));
			TLUtil.createType("ABCD", TypeLibSetUp.TYPELIBRARY_NAME1,
					TypeLibSetUp.TYPELIBRARY_NAME1,
					SOAXSDTemplateSubType.SIMPLE,
					TstConstants.TEMPLATE_SIMPLE_TYPE, TstConstants.XSD_STRING);
		} catch (NoClassDefFoundError ex) {
			assumeNoException(ex);
		}
	}

	/*
	 * Project specific artifact generation 1. gen-meta-src - META-INF
	 * ->TestTypeLibrary ->TypeInformation.xml 2. Sun-jaxb.episode 3. gen-src 4.
	 * meta-src 5. META-INF ->TestTypeLibrary -> TypeDependencies.xml 6. Types
	 */
	/*
	 * public void testValidateProjectArtifactsTypeLibrary1() throws Exception {
	 * IProject project =
	 * WorkspaceUtil.getProject(EBoxTypeLibSetUp.TYPELIBRARY_NAME1);
	 * TLArtifactValidator visitor = new TLArtifactValidator();
	 * 
	 * visitor.setGoldCopyRootDir(EBoxTypeLibSetUp.TYPELIBRARY_NAME1 +
	 * "InitialeBox"); project.accept(visitor);
	 * 
	 * Assert.assertTrue("Gold Copy match failed", visitor.isMatches());
	 * 
	 * }
	 */

	/*
	 * Create a type library with name = TYPELIBRARY_NAME2 To be used for Import
	 * functionality
	 */
	private static void createTypeLibrary2() throws Exception {
		
		assertTrue(TypeLibSetUp.TYPELIBRARY_NAME2
				+ " -- TypeLibrary creation failed", TLUtil.createTypeLibrary(
				TypeLibSetUp.TYPELIBRARY_NAME2, "2.0.1", "DOMAIN",
				TypeLibSetUp.TYPELIB_LOCATION));
		TLUtil.createType("CustomerType", TypeLibSetUp.TYPELIBRARY_NAME2,
				TypeLibSetUp.TYPELIBRARY_NAME2, SOAXSDTemplateSubType.SIMPLE,
				TstConstants.TEMPLATE_SIMPLE_TYPE, TstConstants.XSD_STRING);
	}

	// /*
	// * Validate GTR if the type libraries show up
	// */
	// public void testValidateGTR() throws Exception {
	// SOAGlobalRegistryAdapter.invalidateRegistry();
	// SOATypeRegistry typeRegistry =
	// SOAGlobalRegistryAdapter.getGlobalRegistry();
	//
	// // List<LibraryType> types = typeRegistry.getAllTypes();
	// Set<String> types = typeRegistry.getAllTypeLibrariesNames();
	// TypeLibraryType library =
	// typeRegistry.getTypeLibrary(EBoxTypeLibSetUp.TYPELIBRARY_NAME2);
	// LibraryType type = typeRegistry.getType("CustomerType",
	// EBoxTypeLibSetUp.TYPELIBRARY_NAME2);
	// System.out.println("type library ==== " + library + "type == "+ type);
	// //
	// Assert.assertTrue("Global View Registry does not contain MarketPlaceServiceCommonTypeLibrary",
	// types.contains("MarketPlaceServiceCommonTypeLibrary"));
	// Assert.assertTrue("Global View Registry does not contain SOACommonTypeLibrary",
	// types.contains("SOACommonTypeLibrary"));
	// Assert.assertTrue("Global View Registry doesnot contain " +
	// EBoxTypeLibSetUp.TYPELIBRARY_NAME1,
	// types.contains(EBoxTypeLibSetUp.TYPELIBRARY_NAME1));
	// Assert.assertTrue("Global View Registry doesnot contain " +
	// EBoxTypeLibSetUp.TYPELIBRARY_NAME2,
	// types.contains(EBoxTypeLibSetUp.TYPELIBRARY_NAME2));
	// }

	/*
	 * Same TL name with WS
	 */

	@Test
	@Ignore
	public void testCreateDuplicateCreationTypeLibrary1() {
		boolean createTypeLib = TLUtil.createTypeLibrary(
				TypeLibSetUp.TYPELIBRARY_NAME1, "1.0.0",
				TstConstants.TEMPLATE_COMMON, TypeLibSetUp.TYPELIB_LOCATION);

		// assumeTrue("Test did not fail for TypeLibrary with duplicate name",
		// !createTypeLib);
		assumeTrue(!createTypeLib);
	}

	/*
	 * Same TL name with jar
	 */
	@Test
	@Ignore
	public void testCreateDuplicateCreationTypeLibrary2() {

		// assertFalse(
		// "Test did not fail for TypeLibrary with duplicate name", TLUtil
		// .createTypeLibrary(TypeLibSetUp.TYPELIBRARY_NAME2,
		// "2.0.1", "DOMAIN",
		// TypeLibSetUp.TYPELIB_LOCATION));
		assumeTrue(!TLUtil.createTypeLibrary(TypeLibSetUp.TYPELIBRARY_NAME2,
				"2.0.1", "DOMAIN", TypeLibSetUp.TYPELIB_LOCATION));

	}

	@Test
	public void testInvalidVersion() {
		assumeTrue(!TLUtil.createTypeLibrary("TestTypeLibrary", "1.0.x",
				TstConstants.TEMPLATE_COMMON, TypeLibSetUp.TYPELIB_LOCATION));
		assumeTrue(!TLUtil.createTypeLibrary("TestTypeLibrary", "1.x.0",
				TstConstants.TEMPLATE_COMMON, TypeLibSetUp.TYPELIB_LOCATION));
		assumeTrue(!TLUtil.createTypeLibrary("TestTypeLibrary", "x.0.0",
				TstConstants.TEMPLATE_COMMON, TypeLibSetUp.TYPELIB_LOCATION));
		assumeTrue(!TLUtil.createTypeLibrary("TestTypeLibrary", "x.0.0",
				TstConstants.TEMPLATE_COMMON, TypeLibSetUp.TYPELIB_LOCATION));
	}

}
