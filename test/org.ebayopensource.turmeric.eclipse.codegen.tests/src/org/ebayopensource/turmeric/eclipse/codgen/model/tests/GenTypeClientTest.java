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

import org.ebayopensource.turmeric.eclipse.codegen.model.GenTypeClient;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class GenTypeClientTest {
	
	GenTypeClient model = null;
	
	@Before
	public void setUp() {
		model = new GenTypeClient();
	}
	
	
	@After
	public void tearDown() {
		model = null;
	}

	@Test
	public void testGetCodeGenOptionsGenFolderNull() {
		Map<String, String> map = model.getCodeGenOptions();
		assertFalse(map.containsKey(GenTypeClient.PARAM_JDEST));
	}

	@Test
	public void testGetCodeGenOptionsGenFolder() {
		model.setGenFolder("someFolder");
		Map<String, String> map = model.getCodeGenOptions();
		assertTrue(map.containsKey(GenTypeClient.PARAM_JDEST));
	}
	
	@Test
	public void testGetCodeGenOptionsInterfacePackageNameNull() {
		Map<String, String> map = model.getCodeGenOptions();
		assertFalse(map.containsKey(GenTypeClient.PARAM_GIP));
	}

	@Test
	public void testGetCodeGenOptionsInterfacePackageName() {
		model.setGenInterfacePacakgeName("com.exmample");
		Map<String, String> map = model.getCodeGenOptions();
		assertTrue(map.containsKey(GenTypeClient.PARAM_GIP));
	}
	
	@Test
	public void testGetCodeGenOptionsInterfaceClassNameNull() {
		Map<String, String> map = model.getCodeGenOptions();
		assertFalse(map.containsKey(GenTypeClient.PARAM_GIN));
	}
	
	@Test
	public void testGetCodeGenOptionsInterfaceClassName() {
		model.setGenInterfaceClassName("InterfaceClassName");
		Map<String, String> map = model.getCodeGenOptions();
		assertTrue(map.containsKey(GenTypeClient.PARAM_GIN));
	}
	
	@Test
	public void testGetCodeGenOptionsWSDLFalse() {
		model.setGenerateFromWsdl(false);
		Map<String, String> map = model.getCodeGenOptions();
		assertFalse(map.containsKey(GenTypeClient.PARAM_WSDL));
	}
	
	@Test
	public void testGetCodeGenOptionWSDL() {
		model.setGenerateFromWsdl(true);
		Map<String, String> map = model.getCodeGenOptions();
		assertTrue(map.containsKey(GenTypeClient.PARAM_WSDL));
	}
	
	
}
