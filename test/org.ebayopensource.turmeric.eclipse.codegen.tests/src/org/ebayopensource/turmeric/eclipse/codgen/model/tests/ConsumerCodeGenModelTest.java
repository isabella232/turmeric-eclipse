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

import org.ebayopensource.turmeric.eclipse.codegen.model.ConsumerCodeGenModel;
import org.ebayopensource.turmeric.eclipse.repositorysystem.model.BaseCodeGenModel;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class ConsumerCodeGenModelTest {

	ConsumerCodeGenModel model = null;
	
	@Before
	public void setUp() {
		model = new ConsumerCodeGenModel();
	}
	
	@After
	public void tearDown() {
		model = null;
	}	
	
	@Test
	public void testGetCodeGenOptionsNullClientName() throws Exception {
		Map<String, String> map = model.getCodeGenOptions();
		assertFalse(map.containsKey(BaseCodeGenModel.PARAM_CN));
	}
	
	@Test
	public void testGetCodeGenOptionsClientName() throws Exception {
		model.setClientName("Nermal");
		Map<String, String> map = model.getCodeGenOptions();
		assertTrue(map.containsKey(BaseCodeGenModel.PARAM_CN));
	}
	
	@Test
	public void testGetCodeGenOtionsConsumerIdNull() throws Exception {
		Map<String, String> map = model.getCodeGenOptions();
		assertFalse(map.containsKey(BaseCodeGenModel.PARAM_CONSUMER_ID));
	}

	@Test
	public void testGetCodeGenOptionsConsumerID() throws Exception {
		model.setConsumerId("http://www.example.com/");
		Map<String, String> map = model.getCodeGenOptions();
		assertTrue(map.containsKey(BaseCodeGenModel.PARAM_CONSUMER_ID));
	}
	
	@Test
	public void testGetCodeGenOptionsBlankConfigGroup() {
		Map<String, String> map = model.getCodeGenOptions();
		assertFalse(map.containsKey(BaseCodeGenModel.PARAM_CCGN));
	}
	
	@Test
	public void testGetCodeGenOptionsConfigGroup() {
		model.setClientConfigGroup("ClientConfig");
		Map<String, String> map = model.getCodeGenOptions();
		assertTrue(map.containsKey(BaseCodeGenModel.PARAM_CCGN));
	}
	
}
