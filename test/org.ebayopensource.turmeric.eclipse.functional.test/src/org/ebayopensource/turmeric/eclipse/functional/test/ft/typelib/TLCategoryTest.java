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
import junit.framework.Assert;

import org.ebayopensource.turmeric.common.config.TypeLibraryType;
import org.ebayopensource.turmeric.eclipse.functional.test.AbstractTestCase;
import org.ebayopensource.turmeric.eclipse.test.util.DialogMonitor;
import org.ebayopensource.turmeric.eclipse.test.util.FunctionalTestHelper;
import org.ebayopensource.turmeric.eclipse.test.utils.TLUtil;
import org.ebayopensource.turmeric.eclipse.ui.monitor.typelib.SOAGlobalRegistryAdapter;
import org.ebayopensource.turmeric.tools.library.SOATypeRegistry;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author ksathiamurthy
 * 
 */
public class TLCategoryTest extends AbstractTestCase {
	static DialogMonitor monitor;

	@Before
	public void setUp() throws Exception {
		monitor = new DialogMonitor();
		monitor.startMonitoring();
		FunctionalTestHelper.ensureM2EcipseBeingInited();

		TypeLibSetUp.setup();
		try {
			Assert.assertTrue(TypeLibSetUp.TYPELIBRARY_COMMON
					+ " -- TypeLibrary Creation failed", TLUtil
					.createTypeLibrary(TypeLibSetUp.TYPELIBRARY_COMMON,
							"1.0.0", "COMMON", TypeLibSetUp.TYPELIB_LOCATION));
			Assert.assertTrue(TypeLibSetUp.TYPELIBRARY_DOMAIN
					+ " -- TypeLibrary Creation failed", TLUtil
					.createTypeLibrary(TypeLibSetUp.TYPELIBRARY_DOMAIN,
							"1.1.0", "DOMAIN", TypeLibSetUp.TYPELIB_LOCATION));
			Assert.assertTrue(TypeLibSetUp.TYPELIBRARY_SERVICE
					+ " -- TypeLibrary Creation failed", TLUtil
					.createTypeLibrary(TypeLibSetUp.TYPELIBRARY_SERVICE,
							"1.1.1", "SERVICE", TypeLibSetUp.TYPELIB_LOCATION));
		} catch (NoClassDefFoundError ex) {
			assumeNoException(ex);
		}
	}

	@After
	public void tearDown() throws Exception {
		super.cleanupWorkspace();
		monitor.stopMonitoring();
		monitor = null;
	}

	// to test whether type library is created under correct(common) category
	// with correct version.
	@Test
	public void testCategoryforCommonTL() throws Exception {
		SOATypeRegistry typeRegistry = SOAGlobalRegistryAdapter.getInstance()
				.getGlobalRegistry();
		TypeLibraryType tlType = typeRegistry
				.getTypeLibrary(TypeLibSetUp.TYPELIBRARY_COMMON);
		Assert.assertTrue(
				"Invalid name for " + TypeLibSetUp.TYPELIBRARY_COMMON,
				tlType.getLibraryName().contains(
						TypeLibSetUp.TYPELIBRARY_COMMON));
		System.out.println("********************"
				+ TypeLibSetUp.TYPELIBRARY_COMMON
				+ "  created with right name!!********************");
		Assert.assertTrue("Invalid category for "
				+ TypeLibSetUp.TYPELIBRARY_COMMON, tlType.getCategory()
				.contains("COMMON"));
		System.out.println("********************"
				+ TypeLibSetUp.TYPELIBRARY_COMMON
				+ "  created under correct Category!!********************");
		Assert.assertTrue("Invalid version for "
				+ TypeLibSetUp.TYPELIBRARY_COMMON, tlType.getVersion()
				.contains("1.0.0"));
		System.out.println("********************"
				+ TypeLibSetUp.TYPELIBRARY_COMMON
				+ "  created with correct Version!!********************");
	}

	// to test whether type library is created under correct(domain) category
	// with correct version.
	@Test
	public void testCategoryforDomainTL() throws Exception {
		SOATypeRegistry typeRegistry = SOAGlobalRegistryAdapter.getInstance()
				.getGlobalRegistry();
		TypeLibraryType tlType = typeRegistry
				.getTypeLibrary(TypeLibSetUp.TYPELIBRARY_DOMAIN);
		Assert.assertTrue(
				"Invalid name for" + TypeLibSetUp.TYPELIBRARY_DOMAIN,
				tlType.getLibraryName().contains(
						TypeLibSetUp.TYPELIBRARY_DOMAIN));
		System.out.println("********************"
				+ TypeLibSetUp.TYPELIBRARY_DOMAIN
				+ "  created with right name!!********************");
		Assert.assertTrue("Invalid category for "
				+ TypeLibSetUp.TYPELIBRARY_DOMAIN, tlType.getCategory()
				.contains("DOMAIN"));
		System.out.println("********************"
				+ TypeLibSetUp.TYPELIBRARY_DOMAIN
				+ "  created under correct Category!!********************");
		Assert.assertTrue("Invalid version for "
				+ TypeLibSetUp.TYPELIBRARY_DOMAIN, tlType.getVersion()
				.contains("1.1.0"));
		System.out.println("********************"
				+ TypeLibSetUp.TYPELIBRARY_DOMAIN
				+ "  created with correct Version!!********************");
	}

	// to test whether type library is created under correct(service) category
	// with correct version.
	@Test
	public void testCategoryforServiceTL() throws Exception {
		SOATypeRegistry typeRegistry = SOAGlobalRegistryAdapter.getInstance()
				.getGlobalRegistry();
		TypeLibraryType tlType = typeRegistry
				.getTypeLibrary(TypeLibSetUp.TYPELIBRARY_SERVICE);
		Assert.assertTrue(
				"Invalid name for" + TypeLibSetUp.TYPELIBRARY_SERVICE,
				tlType.getLibraryName().contains(
						TypeLibSetUp.TYPELIBRARY_SERVICE));
		System.out.println("********************"
				+ TypeLibSetUp.TYPELIBRARY_SERVICE
				+ "  created with right name!!********************");
		Assert.assertTrue("Invalid category for "
				+ TypeLibSetUp.TYPELIBRARY_SERVICE, tlType.getCategory()
				.contains("SERVICE"));
		System.out.println("********************"
				+ TypeLibSetUp.TYPELIBRARY_SERVICE
				+ "  created under correct Category!!********************");
		Assert.assertTrue("Invalid version for "
				+ TypeLibSetUp.TYPELIBRARY_SERVICE, tlType.getVersion()
				.contains("1.1.1"));
		System.out.println("********************"
				+ TypeLibSetUp.TYPELIBRARY_SERVICE
				+ "  created with correct Version!!********************");
	}
}
