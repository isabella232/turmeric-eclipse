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

import static org.junit.Assert.fail;

import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

import org.ebayopensource.turmeric.eclipse.config.core.SOAGlobalConfigAccessor;
import org.ebayopensource.turmeric.eclipse.config.core.SOAServiceConfiguration;
import org.junit.Assert;
import org.junit.Test;


/**
 * @author yayu
 *
 */
public class TestSOAGlobalConfigAccessor {

	/**
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.config.core.SOAGlobalConfigAccessor#getResource(org.osgi.framework.Bundle, java.lang.String)}.
	 */
	@Test
	public void testGetResource() throws Exception {
		URL resource = SOAGlobalConfigAccessor.getResource(null, 
				SOAGlobalConfigAccessor.CONF_FOLDER + "/config_test/config_test/org_conf.properties");
		checkUrlInstance(resource);
		
		resource = SOAGlobalConfigAccessor.getResource(null, 
				"hello.world");
		Assert.assertNull(resource);
	}
	
	public static void checkUrlInstance(URL resource) throws Exception {
		Assert.assertNotNull(resource);
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
		URL url = SOAGlobalConfigAccessor.getOrganizationResource(ConfigTestConstants.CONFIG_TEST_REPO_ID, 
				ConfigTestConstants.CONFIG_TEST_ORG_ID, SOAGlobalConfigAccessor.ORGANIZATION_CONFIG_FILE_NAME);
		checkUrlInstance(url);
	}

	/**
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.config.core.SOAGlobalConfigAccessor#getGlobalConfigurations()}.
	 */
	@Test
	public void testGetGlobalConfigurations() throws Exception {
		Properties props = SOAGlobalConfigAccessor.getGlobalConfigurations();
		Assert.assertNotNull(props);
		
		Assert.assertTrue(props.containsKey(SOAGlobalConfigAccessor.KEY_CATEGORIES));
		Assert.assertTrue(props.containsKey(SOAGlobalConfigAccessor.KEY_DEFAULT_COMPILER_LEVEL));
		
		
		
		
	}

	/**
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.config.core.SOAGlobalConfigAccessor#getOrganizationConfigurations(java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testGetOrganizationConfigurations() throws Exception {
		Properties props = SOAGlobalConfigAccessor.getOrganizationConfigurations(
				ConfigTestConstants.CONFIG_TEST_REPO_ID, 
				ConfigTestConstants.CONFIG_TEST_ORG_ID);
		Assert.assertTrue(props.containsKey(SOAServiceConfiguration.KEY_BASE_REQ_TYPE_NAME));
		Assert.assertTrue(props.containsKey(SOAServiceConfiguration.KEY_BASE_REQ_TYPE_NAMESPACE));
		Assert.assertTrue(props.containsKey(SOAServiceConfiguration.KEY_BASE_RESP_TYPE_NAME));
		Assert.assertTrue(props.containsKey(SOAServiceConfiguration.KEY_BASE_RESP_TYPE_NAMESPACE));
		Assert.assertTrue(props.containsKey(SOAServiceConfiguration.KEY_INCLUDED_TYPES_WSDL));
		Assert.assertTrue(props.containsKey(SOAServiceConfiguration.KEY_CLIENT_CONFIG_GROUP));
		Assert.assertTrue(props.containsKey(SOAServiceConfiguration.KEY_SERVICE_CONFIG_GROUP));
		Assert.assertTrue(props.containsKey(SOAServiceConfiguration.KEY_ENV_MAPPER_IMPL));
	}

	/**
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.config.core.SOAGlobalConfigAccessor#getDefaultCompilerLevel()}.
	 */
	@Test
	public void testGetDefaultCompilerLevel() throws Exception {
		Assert.assertNotNull(SOAGlobalConfigAccessor.getDefaultCompilerLevel());
	}

	/**
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.config.core.SOAGlobalConfigAccessor#getCategoriesForTypeLib()}.
	 */
	@Test
	public void testGetCategoriesForTypeLib() throws Exception {
		Assert.assertNotNull(SOAGlobalConfigAccessor.getCategoriesForTypeLib());
	}

	/**
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.config.core.SOAGlobalConfigAccessor#getPreferredErrorLibraryContentProvider(java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testGetPreferredErrorLibraryContentProvider() throws Exception {
		Assert.assertNotNull(SOAGlobalConfigAccessor
				.getPreferredErrorLibraryContentProvider(ConfigTestConstants.CONFIG_TEST_REPO_ID, 
						ConfigTestConstants.CONFIG_TEST_ORG_ID));
	}

	/**
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.config.core.SOAGlobalConfigAccessor#getErrorLibraryCentralLocation(java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testGetErrorLibraryCentralLocation() throws Exception {
		Assert.assertNotNull(SOAGlobalConfigAccessor
				.getErrorLibraryCentralLocation(ConfigTestConstants.CONFIG_TEST_REPO_ID, 
						ConfigTestConstants.CONFIG_TEST_ORG_ID));
	}

	/**
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.config.core.SOAGlobalConfigAccessor#getServiceConfiguration(java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testGetServiceConfiguration() throws Exception {
		SOAServiceConfiguration config = SOAGlobalConfigAccessor.getServiceConfiguration(
				ConfigTestConstants.CONFIG_TEST_REPO_ID, 
						ConfigTestConstants.CONFIG_TEST_ORG_ID);
		Assert.assertNotNull(config);
		Assert.assertNotNull(config.getBaseRequestTypeName());
		Assert.assertNotNull(config.getBaseResponseTypeName());
		Assert.assertNotNull(config.getBaseRequestTypeNameSpace());
		Assert.assertNotNull(config.getBaseResponseTypeNameSpace());
		Assert.assertNotNull(config.getTypesInWSDL());
		Assert.assertNotNull(config.getClientConfigGroup());
		Assert.assertNotNull(config.getServiceConfigGroup());
		Assert.assertNotNull(config.getEnvMapperImpl());
	}

}
