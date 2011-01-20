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

import org.ebayopensource.turmeric.eclipse.codegen.model.GenTypeConsumer;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;


public class GenTypeConsumerTest {

	GenTypeConsumer model = null;
	
	@Before
	public void setUp() throws Exception {
		model = new GenTypeConsumer();
	}

	@After
	public void tearDown() throws Exception {
		model = null;
	}

	@Test
	public void testGetCodeGenOptionsGenFolderNull() {
		Map<String, String> map = model.getCodeGenOptions();
		assertFalse(map.containsKey(GenTypeConsumer.PARAM_JDEST));
	}

	@Test
	public void testGetCodeGenOptionsGenFolder() {
		model.setGenFolder("someFolder");
		Map<String, String> map = model.getCodeGenOptions();
		assertTrue(map.containsKey(GenTypeConsumer.PARAM_JDEST));
	}
	
	@Test
	public void testGetCodeGenOptionsServiceLocationNull() {
		Map<String, String> map = model.getCodeGenOptions();
		assertFalse(map.containsKey(GenTypeConsumer.PARAM_SL));
	}
	
	@Test
	public void testGetCodeGenOptionsServiceLocation() {
		model.setServiceLocation("someServiceLocation");
		Map<String, String> map = model.getCodeGenOptions();
		assertTrue(map.containsKey(GenTypeConsumer.PARAM_SL));
	}
	
	@Test
	public void testGetCodeGenOptionsDefaultEnvironmentNameNull() {
		Map<String, String> map = model.getCodeGenOptions();
		assertFalse(map.containsKey(GenTypeConsumer.PARAM_ENVIRONMENT));
	}
	
	@Test
	public void testGetCodeGenOptionsEnvironmentName() {
		model.setDefaultEnvironmentName("someEnvironment");
		Map<String, String> map = model.getCodeGenOptions();
		assertTrue(map.containsKey(GenTypeConsumer.PARAM_ENVIRONMENT));
	}
	
	@Test
	public void testGetCodeGenOptionsEnvMapperNull() {
		Map<String, String> map = model.getCodeGenOptions();
		assertFalse(map.containsKey(GenTypeConsumer.PARAM_ENV_MAPPER));
	}
	
	@Test
	public void testGetCodeGenOptionsEnvironmentMapper() {
		model.setEnvMapper("someEnvironmentMapper");
		Map<String, String> map = model.getCodeGenOptions();
		assertTrue(map.containsKey(GenTypeConsumer.PARAM_ENV_MAPPER));
	}
	
	
	@Ignore
	public void testIterator() {
		//TODO: refactor this method to get rid of the of the nasty inner class to make it testable.
	}
	
}
