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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Map;

import org.ebayopensource.turmeric.eclipse.repositorysystem.model.BaseCodeGenModel;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class BaseCodeGenModelTest {

	BaseCodeGenModel model = null;
	
	@Before
	public void setupBaseCodeGenModel() throws Exception {
		model = new BaseCodeGenModel();
	}
	
	@After
	public void tearDownBaseCodeGenModel() throws Exception {
		model = null;
	}
	
	@Test
	public void testBaseCodeGenModel() {
		assertNotNull(model);
	}


	@Test
	public void testSetGenTypeUnkownFail() {
		try {
			model.setGenType("Nermal");
		}
		catch (IllegalArgumentException ex) {
			return;
		}
		fail("Allowed an illegal gentype");
	}
	@Test
	public void testSetGenTypeAll() throws Exception {
		model.setGenType(BaseCodeGenModel.GENTYPE_ALL);
		assertEquals(BaseCodeGenModel.GENTYPE_ALL, model.getGenType());
	}

	@Test
	public void testSetGenTypeClient() throws Exception {
		model.setGenType(BaseCodeGenModel.GENTYPE_CLIENT);
		assertEquals(BaseCodeGenModel.GENTYPE_CLIENT, model.getGenType());
	}
	
	@Test
	public void testSetGenTypeConsumer() throws Exception {
		model.setGenType(BaseCodeGenModel.GENTYPE_CONSUMER);
		assertEquals(BaseCodeGenModel.GENTYPE_CONSUMER, model.getGenType());
	}
	
	@Test
	public void testSetGenTypeClientNoConfig() throws Exception {
		model.setGenType(BaseCodeGenModel.GENTYPE_CLIENT_NO_CONFIG);
		assertEquals(BaseCodeGenModel.GENTYPE_CLIENT_NO_CONFIG, model.getGenType());
	}
	
	@Test
	public void testSetGenTypeServer() throws Exception {
		model.setGenType(BaseCodeGenModel.GENTYPE_SERVER);
		assertEquals(BaseCodeGenModel.GENTYPE_SERVER, model.getGenType());
	}
	
	@Test
	public void testSetGenTypeServerNoConfig() throws Exception {
		model.setGenType(BaseCodeGenModel.GENTYPE_SERVER_NO_CONFIG);
		assertEquals(BaseCodeGenModel.GENTYPE_SERVER_NO_CONFIG, model.getGenType());
	}
	
	@Test
	public void testSetGenTypeProxy() throws Exception {
		model.setGenType(BaseCodeGenModel.GENTYPE_PROXY);
		assertEquals(BaseCodeGenModel.GENTYPE_PROXY, model.getGenType());
	}
	
	@Test
	public void testSetGenTypeDispatcher() throws Exception {
		model.setGenType(BaseCodeGenModel.GENTYPE_DISPATCHER);
		assertEquals(BaseCodeGenModel.GENTYPE_DISPATCHER, model.getGenType());
	}
	
	@Test
	public void testSetGenTypeConfigAll() throws Exception {
		model.setGenType(BaseCodeGenModel.GENTYPE_CONFIG_ALL);
		assertEquals(BaseCodeGenModel.GENTYPE_CONFIG_ALL, model.getGenType());
	}
	
	@Test
	public void testSetGenTypeClientConfig() throws Exception {
		model.setGenType(BaseCodeGenModel.GENTYPE_CLIENT_CONFIG);
		assertEquals(BaseCodeGenModel.GENTYPE_CLIENT_CONFIG, model.getGenType());
	}

	@Test
	public void testSetGenTypeServerConfig() throws Exception {
		model.setGenType(BaseCodeGenModel.GENTYPE_SERVER_CONFIG);
		assertEquals(BaseCodeGenModel.GENTYPE_SERVER_CONFIG, model.getGenType());
	}
	
	@Test
	public void testSetGenTypeGlobalServerConfig() throws Exception {
		model.setGenType(BaseCodeGenModel.GENTYPE_GLOBAL_SERVER_CONFIG);
		assertEquals(BaseCodeGenModel.GENTYPE_GLOBAL_SERVER_CONFIG, model.getGenType());
	}
	
	@Test
	public void testSetGenTypeGlobalClientConfig() throws Exception {
		model.setGenType(BaseCodeGenModel.GENTYPE_GLOBAL_CLIENT_CONFIG);
		assertEquals(BaseCodeGenModel.GENTYPE_GLOBAL_CLIENT_CONFIG, model.getGenType());
	}
	
	@Test
	public void testSetGenTypeWSDL() throws Exception {
		model.setGenType(BaseCodeGenModel.GENTYPE_WSDL);
		assertEquals(BaseCodeGenModel.GENTYPE_WSDL, model.getGenType());
	}
	
	@Test
	public void testSetGenTypeInterface() throws Exception {
		model.setGenType(BaseCodeGenModel.GENTYPE_INTERFACE);
		assertEquals(BaseCodeGenModel.GENTYPE_INTERFACE, model.getGenType());
	}
	
	@Test
	public void testSetGenTypeSchema() throws Exception {
		model.setGenType(BaseCodeGenModel.GENTYPE_SCHEMA);
		assertEquals(BaseCodeGenModel.GENTYPE_SCHEMA, model.getGenType());
	}
	
	@Test
	public void testSetGenTypeSiskelton() throws Exception {
		model.setGenType(BaseCodeGenModel.GENTYPE_SISKELETON);
		assertEquals(BaseCodeGenModel.GENTYPE_SISKELETON, model.getGenType());
	}
	
	@Test
	public void testSetGenTypeTypeMappings() throws Exception {
		model.setGenType(BaseCodeGenModel.GENTYPE_TYPE_MAPPINGS);
		assertEquals(BaseCodeGenModel.GENTYPE_TYPE_MAPPINGS, model.getGenType());
	}
	
	@Test
	public void testSetGenTypeWebXML() throws Exception {
		model.setGenType(BaseCodeGenModel.GENTYPE_WEB_XML);
		assertEquals(BaseCodeGenModel.GENTYPE_WEB_XML, model.getGenType());
	}

	@Test
	public void testSetGenTypeUnitTest() throws Exception {
		model.setGenType(BaseCodeGenModel.GENTYPE_UNIT_TEST);
		assertEquals(BaseCodeGenModel.GENTYPE_UNIT_TEST, model.getGenType());
	}
	
	@Test
	public void testSetGenTypeTestClient() throws Exception {
		model.setGenType(BaseCodeGenModel.GENTYPE_TEST_CLIENT);
		assertEquals(BaseCodeGenModel.GENTYPE_TEST_CLIENT, model.getGenType());
	}
	
	@Test
	public void testSetGenTypeServiceOpProps() throws Exception {
		model.setGenType(BaseCodeGenModel.GENTYPE_SERVICE_OP_PROPS);
		assertEquals(BaseCodeGenModel.GENTYPE_SERVICE_OP_PROPS, model.getGenType());
	}
	
	@Test
	public void testSetGenTypeSecurityPolicyConfig() throws Exception {
		model.setGenType(BaseCodeGenModel.GENTYPE_SECURITY_POLICY_CONFIG);
		assertEquals(BaseCodeGenModel.GENTYPE_SECURITY_POLICY_CONFIG, model.getGenType());
	}
	
	@Test
	public void testSetGenTypeServiceMetaDataProps() throws Exception {
		model.setGenType(BaseCodeGenModel.GENTYPE_SERVICE_METADATA_PROPS);
		assertEquals(BaseCodeGenModel.GENTYPE_SERVICE_METADATA_PROPS, model.getGenType());
	}
	
	@Test
	public void testSetGenTypeServiceIntfProjectProps() throws Exception {
		model.setGenType(BaseCodeGenModel.GENTYPE_SERVICE_INTF_PROJECT_PROPS);
		assertEquals(BaseCodeGenModel.GENTYPE_SERVICE_INTF_PROJECT_PROPS, model.getGenType());
	}
	
	@Test
	public void testSetGenTypeServiceFromWSDLIntf() throws Exception {
		model.setGenType(BaseCodeGenModel.GENTYPE_SERVICE_FROM_WSDL_INTF);
		assertEquals(BaseCodeGenModel.GENTYPE_SERVICE_FROM_WSDL_INTF, model.getGenType());
	}
	
	@Test
	public void testSetGenTypeAddType() throws Exception {
		model.setGenType(BaseCodeGenModel.GENTYPE_ADDTYPE);
		assertEquals(BaseCodeGenModel.GENTYPE_ADDTYPE, model.getGenType());
	}
	
	@Test
	public void testSetGenTypeCreateTypeLibrary() throws Exception {
		model.setGenType(BaseCodeGenModel.GENTYPE_CREATETYPELIBRARY);
		assertEquals(BaseCodeGenModel.GENTYPE_CREATETYPELIBRARY, model.getGenType());
	}
	
	@Test
	public void testSetGenTypeCleanBuildTypeLibrary() throws Exception {
		model.setGenType(BaseCodeGenModel.GENTYPE_CLEANBUILDTYPELIBRARY);
		assertEquals(BaseCodeGenModel.GENTYPE_CLEANBUILDTYPELIBRARY, model.getGenType());
	}
	
	@Test
	public void testSetGenTypeIncrBuildTypeLibrary() throws Exception {
		model.setGenType(BaseCodeGenModel.GENTYPE_INCRBUILDTYPELIBRARY);
		assertEquals(BaseCodeGenModel.GENTYPE_INCRBUILDTYPELIBRARY, model.getGenType());
	}
	
	@Test
	public void testSetGenTypeDeleteType() throws Exception {
		model.setGenType(BaseCodeGenModel.GENTYPE_DELETETYPE);
		assertEquals(BaseCodeGenModel.GENTYPE_DELETETYPE, model.getGenType());
	}
	
	@Test
	public void testSetGenTypeCommadLineAll() throws Exception {
		model.setGenType(BaseCodeGenModel.GENTYPE_COMMAND_LINE_ALL);
		assertEquals(BaseCodeGenModel.GENTYPE_COMMAND_LINE_ALL, model.getGenType());
	}
	
	@Test
	public void testSetGenErrorLibAll() throws Exception {
		model.setGenType(BaseCodeGenModel.GENTYPE_ERROR_LIB_ALL);
		assertEquals(BaseCodeGenModel.GENTYPE_ERROR_LIB_ALL, model.getGenType());
	}
	

	@Test
	public void testGetCodeGenOptions() {
		model.setAdminName("Nermal");
		Map<String, String> map = model.getCodeGenOptions();
		assertTrue("Unexpected number of entries", map.size() == 2);
		assertTrue(map.containsKey(BaseCodeGenModel.PARAM_ADMIN_NAME));
		assertTrue(map.containsValue("Nermal"));
	}
	



}
