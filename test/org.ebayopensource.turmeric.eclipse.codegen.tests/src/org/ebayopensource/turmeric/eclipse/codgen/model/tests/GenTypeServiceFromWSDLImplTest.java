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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Map;

import org.ebayopensource.turmeric.eclipse.codegen.model.GenTypeServiceFromWSDLImpl;
import org.ebayopensource.turmeric.eclipse.repositorysystem.model.BaseCodeGenModel;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class GenTypeServiceFromWSDLImplTest {

	GenTypeServiceFromWSDLImpl model = null;
	@Before
	public void setUp() throws Exception {
		model = new GenTypeServiceFromWSDLImpl();
	}

	@After
	public void tearDown() throws Exception {
		model = null;
	}

	@Test
	public void testGetCodeGenOptions() {
		Map<String, String> map = model.getCodeGenOptions();
		assertTrue("Missing GenType Key.", map.containsKey(BaseCodeGenModel.PARAM_GENTYPE));
		assertTrue(map.containsKey(BaseCodeGenModel.PARAM_NAMESPACE));
		assertTrue(map.containsKey(BaseCodeGenModel.PARAM_PR));
		assertTrue(map.containsKey(BaseCodeGenModel.PARAM_INTERFACE));
		assertTrue(map.containsKey(BaseCodeGenModel.PARAM_SERVICE_NAME));
		assertTrue(map.containsKey(BaseCodeGenModel.PARAM_SCV));
		assertTrue(map.containsKey(BaseCodeGenModel.PARAM_SRC));
		assertTrue(map.containsKey(BaseCodeGenModel.PARAM_DEST));
		assertTrue(map.containsKey(BaseCodeGenModel.PARAM_BIN));
		//the SICN will not be available if service impl class name is missing from ServiceConfig.xml file
		//assertTrue(map.containsKey(GenTypeSISkeleton.PARAM_SICN));
		assertTrue(map.containsKey(BaseCodeGenModel.PARAM_CN));
		assertTrue(map.containsKey(BaseCodeGenModel.PARAM_GT));
		
	}
	
	@Test
	public void testGetCodeGenOptionsAdminNull() throws Exception {
		Map<String, String> map = model.getCodeGenOptions();
		assertFalse(map.containsKey(BaseCodeGenModel.PARAM_ADMIN_NAME));
		assertFalse(map.containsKey(BaseCodeGenModel.PARAM_ENVIRONMENT));
	}

	@Test
	public void testGetCodeGenOptionsAdmin() throws Exception {
		model.setAdminName("AdminName");
		model.setServiceName("ServiceName");
		Map<String, String> map = model.getCodeGenOptions();
		assertTrue(map.containsKey(BaseCodeGenModel.PARAM_ADMIN_NAME));
		assertTrue(map.containsKey(BaseCodeGenModel.PARAM_ENVIRONMENT));
	}
	
	@Test
	public void testGetCodeGenOptionsAdminEqServiceName() throws Exception {
		model.setAdminName("AdminName");
		model.setServiceName("AdminName");
		Map<String, String> map = model.getCodeGenOptions();
		assertTrue(map.containsKey(BaseCodeGenModel.PARAM_ADMIN_NAME));
		assertFalse(map.containsKey(BaseCodeGenModel.PARAM_ENVIRONMENT));
	}
	
	@Test
	public void testGetCodeGenOptionsServiceConfigGroupBlank() throws Exception {
		Map<String, String> map = model.getCodeGenOptions();
		assertFalse(map.containsKey(BaseCodeGenModel.PARAM_SCGN));
	}
	
	@Test
	public void testGetCodeGenOptionsServiceConfigGroup() throws Exception {
		model.setServiceConfigGroup("ServiceConfigGroup");
		Map<String, String> map = model.getCodeGenOptions();
		assertTrue(map.containsKey(BaseCodeGenModel.PARAM_SCGN));
	}
	
	@Test
	public void testGenTypeServiceFromWSDLImpl() {
		assertEquals(BaseCodeGenModel.GENTYPE_SERVICE_FROM_WSDL_IMPL, model.getGenType());
	}

}
