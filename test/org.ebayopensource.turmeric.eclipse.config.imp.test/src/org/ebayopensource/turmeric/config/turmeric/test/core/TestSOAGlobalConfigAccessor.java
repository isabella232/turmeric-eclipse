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


import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

import static org.junit.Assert.*;

import org.ebayopensource.turmeric.eclipse.config.core.SOAGlobalConfigAccessor;
import org.ebayopensource.turmeric.eclipse.config.core.SOAServiceConfiguration;
import org.junit.Test;


/**
 * @author yayu
 *
 */
public class TestSOAGlobalConfigAccessor {
	
	public static void checkUrlInstance(URL resource) throws Exception {
		assertNotNull(resource);
		InputStream ins = null;
		try {
			ins = resource.openStream();
		} catch (Exception e) {
			fail(e.toString());
		} finally {
			if (ins != null) {
				ins.close();
			}
		}
	}

	/**
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.config.core.SOAGlobalConfigAccessor#getOrganizationResource(java.lang.String, java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testGetOrganizationResource() throws Exception {
		URL url = SOAGlobalConfigAccessor.getOrganizationResource(TestSOADomainAccessor.TURMERIC_ID, 
				TestSOADomainAccessor.TURMERIC_ID, SOAGlobalConfigAccessor.ORGANIZATION_CONFIG_FILE_NAME);
		checkUrlInstance(url);
	}

	/**
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.config.core.SOAGlobalConfigAccessor#getOrganizationConfigurations(java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testGetOrganizationConfigurations() throws Exception {
		Properties props = SOAGlobalConfigAccessor.getOrganizationConfigurations(
				TestSOADomainAccessor.TURMERIC_ID, 
				TestSOADomainAccessor.TURMERIC_ID);
		assertTrue(props.containsKey(SOAServiceConfiguration.KEY_BASE_REQ_TYPE_NAME));
		assertTrue(props.containsKey(SOAServiceConfiguration.KEY_BASE_REQ_TYPE_NAMESPACE));
		assertTrue(props.containsKey(SOAServiceConfiguration.KEY_BASE_RESP_TYPE_NAME));
		assertTrue(props.containsKey(SOAServiceConfiguration.KEY_BASE_RESP_TYPE_NAMESPACE));
		assertTrue(props.containsKey(SOAServiceConfiguration.KEY_INCLUDED_TYPES_WSDL));
		assertTrue(props.containsKey(SOAServiceConfiguration.KEY_CLIENT_CONFIG_GROUP));
		assertTrue(props.containsKey(SOAServiceConfiguration.KEY_SERVICE_CONFIG_GROUP));
		assertTrue(props.containsKey(SOAServiceConfiguration.KEY_ENV_MAPPER_IMPL));
	}

	/**
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.config.core.SOAGlobalConfigAccessor#getPreferredErrorLibraryContentProvider(java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testGetPreferredErrorLibraryContentProvider() throws Exception {
		String providerId = SOAGlobalConfigAccessor
		.getPreferredErrorLibraryContentProvider(TestSOADomainAccessor.TURMERIC_ID, 
				TestSOADomainAccessor.TURMERIC_ID);
		assertNotNull(providerId);
		assertEquals("PropertiesContentErrorLibraryProvider", providerId);
	}

	/**
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.config.core.SOAGlobalConfigAccessor#getErrorLibraryCentralLocation(java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testGetErrorLibraryCentralLocation() throws Exception {
		assertNotNull(SOAGlobalConfigAccessor
				.getErrorLibraryCentralLocation(TestSOADomainAccessor.TURMERIC_ID, 
						TestSOADomainAccessor.TURMERIC_ID));
	}

	/**
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.config.core.SOAGlobalConfigAccessor#getServiceConfiguration(java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testGetServiceConfiguration() throws Exception {
		SOAServiceConfiguration config = SOAGlobalConfigAccessor.getServiceConfiguration(
				TestSOADomainAccessor.TURMERIC_ID, 
				TestSOADomainAccessor.TURMERIC_ID);
		assertNotNull(config);
		assertNotNull(config.getBaseRequestTypeName());
		assertNotNull(config.getBaseResponseTypeName());
		assertNotNull(config.getBaseRequestTypeNameSpace());
		assertNotNull(config.getBaseResponseTypeNameSpace());
		assertNotNull(config.getTypesInWSDL());
		assertNotNull(config.getClientConfigGroup());
		assertNotNull(config.getServiceConfigGroup());
		assertNotNull(config.getEnvMapperImpl());
	}

}
