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

import org.ebayopensource.turmeric.eclipse.codegen.model.GenTypeServiceMetadataProps;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class GenTypeServiceMetadataPropsTest {

	GenTypeServiceMetadataProps model = null;
	@Before
	public void setUp() throws Exception {
		model = new GenTypeServiceMetadataProps();
	}

	@After
	public void tearDown() throws Exception {
		model = null;
	}

	@Test
	public void testGetCodeGenOptions() {
		Map<String, String> map = model.getCodeGenOptions();
		assertTrue(map.containsKey(GenTypeServiceMetadataProps.PARAM_GENTYPE));
		assertTrue(map.containsKey(GenTypeServiceMetadataProps.PARAM_INTERFACE));
	}
	
	@Test
	public void testGetCodeGenOptionsNulls() throws Exception {
		Map<String, String> map = model.getCodeGenOptions();
		assertFalse(map.containsKey(GenTypeServiceMetadataProps.PARAM_ADMIN_NAME));
		assertFalse(map.containsKey(GenTypeServiceMetadataProps.PARAM_NAMESPACE));
		assertFalse(map.containsKey(GenTypeServiceMetadataProps.PARAM_SERVICE_LAYER_FILE));
		assertFalse(map.containsKey(GenTypeServiceMetadataProps.PARAM_PR));
		assertFalse(map.containsKey(GenTypeServiceMetadataProps.PARAM_SCV));
		assertFalse(map.containsKey(GenTypeServiceMetadataProps.PARAM_SICN));
		assertFalse(map.containsKey(GenTypeServiceMetadataProps.PARAM_SLAYER));
		assertFalse(map.containsKey(GenTypeServiceMetadataProps.PARAM_WSDL));
		assertFalse(map.containsKey(GenTypeServiceMetadataProps.PARAM_GIP));
		assertFalse(map.containsKey(GenTypeServiceMetadataProps.PARAM_SLAYER));
	}

	@Test
	public void testGetCodeGenOptionsSetters() throws Exception {
		model.setAdminName("AdminName");
		model.setNamespace("http://www.example.com");
		model.setServiceLayerFile("somefile.txt");
		model.setProjectRoot("/home/someplace");
		model.setServiceVersion("1.0");
		model.setServiceImplClassName("ServiceImpl");
		model.setServiceLayer("BUSINESS");
		model.setOriginalWsdlUrl("http://www.example.com/WSDL.wsdl");
		model.setServiceInterface("org.ebayopensouce.package.ServiceInterface");
		Map<String, String> map = model.getCodeGenOptions();
		assertTrue(map.containsKey(GenTypeServiceMetadataProps.PARAM_ADMIN_NAME));
		assertTrue(map.containsKey(GenTypeServiceMetadataProps.PARAM_NAMESPACE));
		assertTrue(map.containsKey(GenTypeServiceMetadataProps.PARAM_SERVICE_LAYER_FILE));
		assertTrue(map.containsKey(GenTypeServiceMetadataProps.PARAM_PR));
		assertTrue(map.containsKey(GenTypeServiceMetadataProps.PARAM_SCV));
//		assertTrue(map.containsKey(GenTypeServiceMetadataProps.PARAM_SICN));
		assertTrue(map.containsKey(GenTypeServiceMetadataProps.PARAM_SLAYER));
		assertTrue(map.containsKey(GenTypeServiceMetadataProps.PARAM_WSDL));
		assertTrue(map.containsKey(GenTypeServiceMetadataProps.PARAM_GIP));
		assertTrue(map.containsKey(GenTypeServiceMetadataProps.PARAM_GIN));
	}
	
	@Test
	public void testGenTypeServiceMetadataProps() {
		assertEquals(GenTypeServiceMetadataProps.GENTYPE_SERVICE_METADATA_PROPS, model.getGenType());
	}

}
