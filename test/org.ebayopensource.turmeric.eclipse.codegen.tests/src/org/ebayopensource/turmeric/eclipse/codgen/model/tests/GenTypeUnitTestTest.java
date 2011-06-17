/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.codgen.model.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Map;

import org.ebayopensource.turmeric.eclipse.codegen.model.GenTypeUnitTest;
import org.ebayopensource.turmeric.eclipse.repositorysystem.model.BaseCodeGenModel;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class GenTypeUnitTestTest {

	GenTypeUnitTest model = null;

	@Before
	public void setUp() throws Exception {
		model = new GenTypeUnitTest();
	}

	@After
	public void tearDown() throws Exception {
		model = null;
	}

	@Test
	public void testGetCodeGenOptionsDefaults() {
		Map<String, String> map = model.getCodeGenOptions();
		assertTrue(map.containsKey(BaseCodeGenModel.PARAM_GENTYPE));
		assertTrue(map.containsKey(BaseCodeGenModel.PARAM_NAMESPACE));
		assertTrue(map.containsKey(BaseCodeGenModel.PARAM_INTERFACE));
		assertTrue(map.containsKey(BaseCodeGenModel.PARAM_SERVICE_NAME));
		//the SICN will not be available if service impl class name is missing from ServiceConfig.xml file
		//assertTrue(map.containsKey(GenTypeSISkeleton.PARAM_SICN));
		assertTrue(map.containsKey(BaseCodeGenModel.PARAM_SCV));
		assertTrue(map.containsKey(BaseCodeGenModel.PARAM_SRC));
		assertTrue(map.containsKey(BaseCodeGenModel.PARAM_DEST));
		assertTrue(map.containsKey(BaseCodeGenModel.PARAM_BIN));
		assertTrue(map.containsKey(BaseCodeGenModel.PARAM_JDEST));
		assertTrue(map.containsKey(BaseCodeGenModel.PARAM_CN));
	}

	@Test
	public void testGenTypeUnitTest() {
		assertEquals(BaseCodeGenModel.GENTYPE_UNIT_TEST, model.getGenType());
	}

}
