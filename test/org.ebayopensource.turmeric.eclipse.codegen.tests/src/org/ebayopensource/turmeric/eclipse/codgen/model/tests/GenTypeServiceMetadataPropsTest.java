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
import org.ebayopensource.turmeric.eclipse.repositorysystem.model.BaseCodeGenModel;
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
		assertTrue(map.containsKey(BaseCodeGenModel.PARAM_GENTYPE));
		assertTrue(map.containsKey(BaseCodeGenModel.PARAM_INTERFACE));
	}
	
	@Test
	public void testGetCodeGenOptionsNulls() throws Exception {
		Map<String, String> map = model.getCodeGenOptions();
		assertFalse(map.containsKey(BaseCodeGenModel.PARAM_ADMIN_NAME));
		assertFalse(map.containsKey(BaseCodeGenModel.PARAM_NAMESPACE));
		assertFalse(map.containsKey(BaseCodeGenModel.PARAM_SERVICE_LAYER_FILE));
		assertFalse(map.containsKey(BaseCodeGenModel.PARAM_PR));
		assertFalse(map.containsKey(BaseCodeGenModel.PARAM_SCV));
		assertFalse(map.containsKey(BaseCodeGenModel.PARAM_SICN));
		assertFalse(map.containsKey(BaseCodeGenModel.PARAM_SLAYER));
		assertFalse(map.containsKey(BaseCodeGenModel.PARAM_WSDL));
		assertFalse(map.containsKey(BaseCodeGenModel.PARAM_GIP));
		assertFalse(map.containsKey(BaseCodeGenModel.PARAM_SLAYER));
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
		assertTrue(map.containsKey(BaseCodeGenModel.PARAM_ADMIN_NAME));
		assertTrue(map.containsKey(BaseCodeGenModel.PARAM_NAMESPACE));
		assertTrue(map.containsKey(BaseCodeGenModel.PARAM_SERVICE_LAYER_FILE));
		assertTrue(map.containsKey(BaseCodeGenModel.PARAM_PR));
		assertTrue(map.containsKey(BaseCodeGenModel.PARAM_SCV));
//		assertTrue(map.containsKey(GenTypeServiceMetadataProps.PARAM_SICN));
		assertTrue(map.containsKey(BaseCodeGenModel.PARAM_SLAYER));
		assertTrue(map.containsKey(BaseCodeGenModel.PARAM_WSDL));
		assertTrue(map.containsKey(BaseCodeGenModel.PARAM_GIP));
		assertTrue(map.containsKey(BaseCodeGenModel.PARAM_GIN));
	}
	
	@Test
	public void testGenTypeServiceMetadataProps() {
		assertEquals(BaseCodeGenModel.GENTYPE_SERVICE_METADATA_PROPS, model.getGenType());
	}

}
