/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.codgen.model.tests;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Map;

import org.ebayopensource.turmeric.eclipse.codegen.model.GenTypeErrorLibAll;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class GenTypeErrorLibAllTest {

	GenTypeErrorLibAll model = null;
	
	@Before
	public void setUp() throws Exception {
		model = new GenTypeErrorLibAll();
	}

	@After
	public void tearDown() throws Exception {
		model = null;
	}

	@Test
	public void testGetCodeGenOptionsDomainsEmpty() {
		Map<String, String> map = model.getCodeGenOptions();
		assertFalse(map.containsKey(GenTypeErrorLibAll.PARAM_DOMAIN));
	}

	@Test
	public void testGetCodeGenOptionsDomain() {
		model.addDomain("org.ebayopensource");
		Map<String, String> map = model.getCodeGenOptions();
		assertTrue(map.containsKey(GenTypeErrorLibAll.PARAM_DOMAIN));
	}
	
	@Test
	public void testGetCodeGenOptionsErrorLibraryNameBlank() {
		Map<String, String> map = model.getCodeGenOptions();
		assertFalse(map.containsKey(GenTypeErrorLibAll.PARAM_ERROR_LIBRARY_NAME));
	}
	
	@Test
	public void testGetCodeGenOptionsError() {
		model.setErrorLibraryName("Error");
		Map<String, String> map = model.getCodeGenOptions();
		assertTrue(map.containsKey(GenTypeErrorLibAll.PARAM_ERROR_LIBRARY_NAME));
	}
}
