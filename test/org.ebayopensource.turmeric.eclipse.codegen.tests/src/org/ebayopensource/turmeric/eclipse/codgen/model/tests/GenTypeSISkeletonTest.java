/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.codgen.model.tests;

import static org.junit.Assert.*;

import java.util.Map;

import org.ebayopensource.turmeric.eclipse.codegen.model.GenTypeSISkeleton;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class GenTypeSISkeletonTest {

	GenTypeSISkeleton model = null;
	
	@Before
	public void setUp() throws Exception {
		model = new GenTypeSISkeleton();
	}

	@After
	public void tearDown() throws Exception {
		model = null;
	}

	@Test
	public void testGetCodeGenOptions() {
		Map<String, String> map = model.getCodeGenOptions();
		assertTrue(map.containsKey(GenTypeSISkeleton.PARAM_GENTYPE));
		assertTrue(map.containsKey(GenTypeSISkeleton.PARAM_NAMESPACE));
		assertTrue(map.containsKey(GenTypeSISkeleton.PARAM_INTERFACE));
		assertTrue(map.containsKey(GenTypeSISkeleton.PARAM_SERVICE_NAME));
		assertTrue(map.containsKey(GenTypeSISkeleton.PARAM_SCV));
		assertTrue(map.containsKey(GenTypeSISkeleton.PARAM_SRC));
		assertTrue(map.containsKey(GenTypeSISkeleton.PARAM_DEST));
		assertTrue(map.containsKey(GenTypeSISkeleton.PARAM_BIN));
		//the SICN will not be available if service impl class name is missing from ServiceConfig.xml file
		//assertTrue(map.containsKey(GenTypeSISkeleton.PARAM_SICN));
		assertTrue(map.containsKey(GenTypeSISkeleton.PARAM_GSS));
		assertFalse(map.containsKey(GenTypeSISkeleton.PARAM_OWIC));
	}

	@Test
	public void testGenTypeSISkeleton() {
		assertEquals(GenTypeSISkeleton.GENTYPE_SISKELETON, model.getGenType());
	}

	@Test
	public void testIsOverwriteImplClass() {
		model.setOverwriteImplClass(true);
		assertTrue(model.isOverwriteImplClass());
		Map<String, String> map = model.getCodeGenOptions();
		assertTrue(map.containsKey(GenTypeSISkeleton.PARAM_OWIC));
	}

	@Test
	public void testSetOverwriteImplClassDefault() {
		assertFalse(model.isOverwriteImplClass());
	}

}
