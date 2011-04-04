/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.functional.test.ft.typelib;

import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;

import org.ebayopensource.turmeric.eclipse.functional.test.AbstractTestCase;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.SOAGlobalRegistryAdapter;
import org.ebayopensource.turmeric.eclipse.test.util.DialogMonitor;
import org.ebayopensource.turmeric.eclipse.test.util.FunctionalTestHelper;
import org.ebayopensource.turmeric.eclipse.test.utils.TLUtil;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.junit.Assume.*;

import org.ebayopensource.turmeric.common.config.TypeInformationType;
import org.ebayopensource.turmeric.common.config.TypeLibraryType;

public class SOAGlobalRegistryAdapterTest extends AbstractTestCase {
	static DialogMonitor monitor;

	@Before
	public void setUp() throws Exception {
		Thread.sleep(75000);
		monitor = new DialogMonitor();
		monitor.startMonitoring();
		FunctionalTestHelper.ensureM2EcipseBeingInited();

	}

	@After
	public void tearDown() throws Exception {
		super.cleanupWorkspace();
		monitor.stopMonitoring();
		monitor = null;
	}

	@Test
	public void testGetTypeLibrary() throws Exception {
		String tlName = "common-type-library";
		boolean check = false;
		SOAGlobalRegistryAdapter.getInstance().populateRegistry(
				tlName);
		SOAGlobalRegistryAdapter.getInstance().invalidateRegistry();
		TypeLibraryType tlType = SOAGlobalRegistryAdapter.getInstance()
				.getGlobalRegistry().getTypeLibrary(tlName);
		assertNotNull("Can not find the type library " + tlName, tlType);
		assertTrue("Invalid name for " + tlName,
				"common-type-library".equals(tlType.getLibraryName()));
		assertTrue("Invalid category for " + tlName, tlType.getCategory()
				.contains("COMMON"));
		assertTrue(
				"Invalid namespace for " + tlName,
				tlType.getLibraryNamespace()
						.contains(
								"http://www.ebayopensource.org/turmeric/common/v1/types"));
		assertTrue("Invalid version for " + tlName, tlType.getVersion()
				.contains("1.0.0"));
		tlType.getType().get(6).getXmlTypeName().contains("ErrorMessage");
		Iterator<TypeInformationType> it = tlType.getType().iterator();
		while (it.hasNext()) {
			if (it.next().getXmlTypeName().equals("ErrorMessage")) {
				check = true;
			}
		}

		assertTrue(tlName + " does not contain error message", check);
	}

	@Test
	public void testpopulateRegistry() throws Exception {
		TypeLibSetUp.setup();
		TLUtil.createTypeLibrary(TypeLibSetUp.TYPELIBRARY_TEST, "1.0.0",
				"COMMON", TypeLibSetUp.TYPELIB_LOCATION);
		SOAGlobalRegistryAdapter.getInstance().populateRegistry(
				TypeLibSetUp.TYPELIBRARY_TEST);
		SOAGlobalRegistryAdapter.getInstance().invalidateRegistry();
		assertTrue("Type Library does not exists in GTR",
				SOAGlobalRegistryAdapter.getInstance().getGlobalRegistry()
						.getTypeLibrary(TypeLibSetUp.TYPELIBRARY_TEST)
						.getLibraryName().equals(TypeLibSetUp.TYPELIBRARY_TEST));

	}

}
