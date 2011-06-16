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
package org.ebayopensource.turmeric.config.turmeric.test.core;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Map;

import org.ebayopensource.turmeric.eclipse.config.core.SOADomainAccessor;
import org.junit.Test;


/**
 * @author yayu
 *
 */
public class TestSOADomainAccessor {
	public static final String TURMERIC_ID = "Turmeric";
	
	/**
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.config.core.SOADomainAccessor#getDomains(java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testGetDomains() throws Exception {
		Map<String, List<String>> domains = 
			SOADomainAccessor.getDomains(TURMERIC_ID, 
					TURMERIC_ID);
		assertNotNull(domains);
		assertFalse(domains.isEmpty());
	}


	/**
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.config.core.SOADomainAccessor#isDomainRequired(java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testIsDomainRequired() throws Exception {
		assertTrue(SOADomainAccessor.isDomainRequired(TURMERIC_ID, 
				TURMERIC_ID));
	}

	

}
