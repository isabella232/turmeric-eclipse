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
import static org.junit.Assert.fail;
import static org.junit.Assume.assumeNoException;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.ebayopensource.turmeric.eclipse.core.resources.constants.SOATypeLibraryConstants;
import org.ebayopensource.turmeric.eclipse.core.resources.constants.SOAXSDTemplateSubType;
import org.ebayopensource.turmeric.eclipse.test.util.DialogMonitor;
import org.ebayopensource.turmeric.eclipse.test.util.FunctionalTestHelper;
import org.ebayopensource.turmeric.eclipse.test.utils.TLUtil;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author shrao
 * 
 *         create simple types based on all available restrictions validate the
 *         generated XSDs for style guide using default style guide
 */
public class CreateTypeFromTemplateTest {
	private static final String TL_NAMESPACE = "http://www.ebayopensource.org/turmeric/services/defaulttemplatetypes";
	static DialogMonitor monitor;

	@Before
	public void setUp() throws Exception {
		monitor = new DialogMonitor();
		monitor.startMonitoring();
		FunctionalTestHelper.ensureM2EcipseBeingInited();
	}

	@After
	public void tearDown() throws Exception {
		monitor.stopMonitoring();
		monitor = null;
	}

	/*
	 * create simple-types from all available restriction types taken from
	 * SOATypeLibraryConstants.SCHEMA_DATA_TYPES
	 */
	@Test
	// @Ignore("failing")
	public void testCreateSimpleTypeFromDefaultTemplate() throws Exception {
		System.out.println("** testCreateSimpleTypeFromDefaultTemplate **");
		FunctionalTestHelper.ensureM2EcipseBeingInited();

		TypeLibSetUp.setup();
		try {
			TLUtil.createTypeLibraryInCustomNS(TypeLibSetUp.TYPELIBRARY_TEST,
					"1.0.0", "SERVICE", TypeLibSetUp.TYPELIB_LOCATION,
					TL_NAMESPACE);

			final List<String> SCHEMA_DATA_TYPES_LIST;
			SCHEMA_DATA_TYPES_LIST = Collections.unmodifiableList(Arrays
					.asList(SOATypeLibraryConstants.SCHEMA_DATA_TYPES));

			System.out.println("---" + SCHEMA_DATA_TYPES_LIST.size());
			String typeLib = TypeLibSetUp.TYPELIB_LOCATION + File.separator
					+ TypeLibSetUp.TYPELIBRARY_TEST;
			String schemaFileLocation = typeLib + File.separator + "meta-src"
					+ File.separator + "types" + File.separator
					+ TypeLibSetUp.TYPELIBRARY_TEST;
			File schemaFile = null;
			String schemaFileContent = null;

			for (Iterator<String> itr = SCHEMA_DATA_TYPES_LIST.iterator(); itr
					.hasNext();) {
				String restrictionType = itr.next().toString();
				if (restrictionType.startsWith("any")) {
					//skip the any types
					continue;
				}
				String typeName = "SimpleType" + restrictionType;
				try {
					// create the simple type
					TLUtil.createTypeInCustomNS(typeName,
							TypeLibSetUp.TYPELIBRARY_TEST,
							TypeLibSetUp.TYPELIBRARY_TEST,
							SOAXSDTemplateSubType.SIMPLE,
							TstConstants.TEMPLATE_SIMPLE_TYPE, restrictionType,
							TL_NAMESPACE);

					// Assert name, restriction & namespace
					schemaFile = new File(schemaFileLocation + File.separator
							+ typeName + ".xsd");
					assertTrue(" -- Schema def file is missing",
							schemaFile.exists());
					schemaFileContent = FileUtils.readFileToString(schemaFile);
					assertTrue(
							" -- Expected targetNamespace not found",
							schemaFileContent
									.contains("targetNamespace=\"" + TL_NAMESPACE + "\""));
					assertTrue(
							" -- Expected type name not found",
							schemaFileContent.contains("xs:simpleType name=\""
									+ typeName + "\""));
					assertTrue(" -- Expected restriction not found",
							schemaFileContent
									.contains("xs:restriction base=\"xs:"
											+ restrictionType + "\""));
					assertTrue(
							" -- Expected documentation not found",
							schemaFileContent
									.contains("xs:documentation>Automated Test</xs:documentation>"));
					assertTrue(
							" -- Expected attributeFormDefault & elementFormDefault not found",
							schemaFileContent
									.contains("attributeFormDefault=\"unqualified\" elementFormDefault=\"qualified\""));
				} catch (Exception ex) {
					fail("Exception in testCreateSimpleTypeFromDefaultTemplate(): "
							+ ex.getLocalizedMessage());
					break;
				}
			}
		} catch (NoClassDefFoundError ex) {
			assumeNoException(ex);
		}
	}

	/*
	 * create enum-types from all available restriction types are taken from
	 * SOATypeLibraryConstants.SCHEMA_DATA_TYPES
	 */
	@Test
	public void testCreateEnumTypeFromDefaultTemplate() throws IOException {
		System.out.println("** testCreateEnumTypeFromDefaultTemplate **");
		final List<String> SCHEMA_DATA_TYPES_LIST;
		SCHEMA_DATA_TYPES_LIST = Collections.unmodifiableList(Arrays
				.asList(new String[]{"string", "token", "decimal"}));
		System.out.println("---" + SCHEMA_DATA_TYPES_LIST.size());
		for (Iterator<String> itr = SCHEMA_DATA_TYPES_LIST.iterator(); itr.hasNext();) {
			String restrictionType = itr.next().toString();
			String typeName = "EnumType" + restrictionType;
			try {
				TLUtil.createTypeInCustomNS(typeName,
						TypeLibSetUp.TYPELIBRARY_TEST,
						TypeLibSetUp.TYPELIBRARY_TEST,
						SOAXSDTemplateSubType.ENUM,
						TstConstants.TEMPLATE_TURMERIC_ENUM, restrictionType,
						TL_NAMESPACE);

			} catch (Exception ex) {
				fail("Exception in testCreateEnumTypeFromDefaultTemplate(): "
						+ ex.getLocalizedMessage());
				break;
			}
		}
	}

}
