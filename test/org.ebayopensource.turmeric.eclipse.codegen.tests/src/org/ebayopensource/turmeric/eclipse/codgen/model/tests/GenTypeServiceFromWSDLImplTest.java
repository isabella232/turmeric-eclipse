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

import org.ebayopensource.turmeric.eclipse.codegen.model.GenTypeServiceFromWSDLImpl;
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
		assertTrue("Missing GenType Key.", map.containsKey(GenTypeServiceFromWSDLImpl.PARAM_GENTYPE));
		assertTrue(map.containsKey(GenTypeServiceFromWSDLImpl.PARAM_NAMESPACE));
		assertTrue(map.containsKey(GenTypeServiceFromWSDLImpl.PARAM_PR));
		assertTrue(map.containsKey(GenTypeServiceFromWSDLImpl.PARAM_INTERFACE));
		assertTrue(map.containsKey(GenTypeServiceFromWSDLImpl.PARAM_SERVICE_NAME));
		assertTrue(map.containsKey(GenTypeServiceFromWSDLImpl.PARAM_SCV));
		assertTrue(map.containsKey(GenTypeServiceFromWSDLImpl.PARAM_SRC));
		assertTrue(map.containsKey(GenTypeServiceFromWSDLImpl.PARAM_DEST));
		assertTrue(map.containsKey(GenTypeServiceFromWSDLImpl.PARAM_BIN));
		//the SICN will not be available if service impl class name is missing from ServiceConfig.xml file
		//assertTrue(map.containsKey(GenTypeSISkeleton.PARAM_SICN));
		assertTrue(map.containsKey(GenTypeServiceFromWSDLImpl.PARAM_CN));
		assertTrue(map.containsKey(GenTypeServiceFromWSDLImpl.PARAM_GT));
		
	}
	
	@Test
	public void testGetCodeGenOptionsAdminNull() throws Exception {
		Map<String, String> map = model.getCodeGenOptions();
		assertFalse(map.containsKey(GenTypeServiceFromWSDLImpl.PARAM_ADMIN_NAME));
		assertFalse(map.containsKey(GenTypeServiceFromWSDLImpl.PARAM_ENVIRONMENT));
	}

	@Test
	public void testGetCodeGenOptionsAdmin() throws Exception {
		model.setAdminName("AdminName");
		model.setServiceName("ServiceName");
		Map<String, String> map = model.getCodeGenOptions();
		assertTrue(map.containsKey(GenTypeServiceFromWSDLImpl.PARAM_ADMIN_NAME));
		assertTrue(map.containsKey(GenTypeServiceFromWSDLImpl.PARAM_ENVIRONMENT));
	}
	
	@Test
	public void testGetCodeGenOptionsAdminEqServiceName() throws Exception {
		model.setAdminName("AdminName");
		model.setServiceName("AdminName");
		Map<String, String> map = model.getCodeGenOptions();
		assertTrue(map.containsKey(GenTypeServiceFromWSDLImpl.PARAM_ADMIN_NAME));
		assertFalse(map.containsKey(GenTypeServiceFromWSDLImpl.PARAM_ENVIRONMENT));
	}
	
	@Test
	public void testGetCodeGenOptionsServiceConfigGroupBlank() throws Exception {
		Map<String, String> map = model.getCodeGenOptions();
		assertFalse(map.containsKey(GenTypeServiceFromWSDLImpl.PARAM_SCGN));
	}
	
	@Test
	public void testGetCodeGenOptionsServiceConfigGroup() throws Exception {
		model.setServiceConfigGroup("ServiceConfigGroup");
		Map<String, String> map = model.getCodeGenOptions();
		assertTrue(map.containsKey(GenTypeServiceFromWSDLImpl.PARAM_SCGN));
	}
	
	@Test
	public void testGenTypeServiceFromWSDLImpl() {
		assertEquals(GenTypeServiceFromWSDLImpl.GENTYPE_SERVICE_FROM_WSDL_IMPL, model.getGenType());
	}

}
