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

import org.ebayopensource.turmeric.eclipse.codegen.model.GenTypeServiceFromWSDLImpl;
import org.ebayopensource.turmeric.eclipse.codegen.model.GenTypeServiceFromWSDLIntf;
import org.ebayopensource.turmeric.eclipse.repositorysystem.model.BaseCodeGenModel;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class GenTypeServiceFromWSDLIntfTest {

	GenTypeServiceFromWSDLIntf model = null;
	@Before
	public void setUp() throws Exception {
		model = new GenTypeServiceFromWSDLIntf();
	}

	@After
	public void tearDown() throws Exception {
		model = null;
	}

	@Test
	public void testGetCodeGenOptions() {
		assertNotNull(model.getCodeGenOptions());
		assertFalse(model.getCodeGenOptions().isEmpty());
	}

	@Test
	public void testGenTypeServiceFromWSDLIntf() {
		assertEquals(BaseCodeGenModel.GENTYPE_SERVICE_FROM_WSDL_INTF, model.getGenType());
	}

}
