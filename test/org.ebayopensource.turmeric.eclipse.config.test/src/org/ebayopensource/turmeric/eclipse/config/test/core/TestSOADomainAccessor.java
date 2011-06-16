/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
/**
 * 
 */
package org.ebayopensource.turmeric.eclipse.config.test.core;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;

import org.ebayopensource.turmeric.eclipse.config.core.SOADomainAccessor;
import org.junit.Test;


/**
 * @author yayu
 *
 */
public class TestSOADomainAccessor {
	
	
	/**
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.config.core.SOADomainAccessor#getDomains(java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testGetDomains() throws Exception {
		Map<String, List<String>> domains = 
			SOADomainAccessor.getDomains(ConfigTestConstants.CONFIG_TEST_REPO_ID, 
				ConfigTestConstants.CONFIG_TEST_ORG_ID);
		Assert.assertNotNull(domains);
		Assert.assertFalse(domains.isEmpty());
	}

	/**
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.config.core.SOADomainAccessor#parseStringToDomainMap(java.lang.String)}.
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.config.core.SOADomainAccessor#paseDomainMapToString(java.util.Map)}.
	 */
	@Test
	public void testDomainMap() {
		Map<String, List<String>> domains = new LinkedHashMap<String, List<String>>();
		List<String> list = new ArrayList<String>();
		list.add("zhabei");
		list.add("pudong");
		domains.put("ShangHai", list);
		list = new ArrayList<String>();
		list.add("chaoyang");
		list.add("haidian");
		domains.put("BeiJing", list);
		String mappings = SOADomainAccessor.paseDomainMapToString(domains);
		
		Map<String, List<String>> genDomains = 
			SOADomainAccessor.parseStringToDomainMap(mappings);
		Assert.assertEquals(domains, genDomains);
		Assert.assertEquals(mappings, SOADomainAccessor.paseDomainMapToString(genDomains));
	}

	/**
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.config.core.SOADomainAccessor#isDomainRequired(java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testIsDomainRequired() throws Exception {
		Assert.assertTrue(SOADomainAccessor.isDomainRequired(ConfigTestConstants.CONFIG_TEST_REPO_ID, 
				ConfigTestConstants.CONFIG_TEST_ORG_ID));
	}

	

}
