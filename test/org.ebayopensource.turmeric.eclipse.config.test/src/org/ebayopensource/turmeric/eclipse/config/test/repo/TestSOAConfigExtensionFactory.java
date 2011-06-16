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
package org.ebayopensource.turmeric.eclipse.config.test.repo;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.Map;

import org.ebayopensource.turmeric.eclipse.config.repo.SOAConfigExtensionFactory;
import org.ebayopensource.turmeric.eclipse.config.repo.SOAConfigExtensionFactory.SOAConfigTemplate;
import org.ebayopensource.turmeric.eclipse.config.repo.SOAConfigExtensionFactory.SOAXSDTemplateSubType;
import org.ebayopensource.turmeric.eclipse.config.test.core.ConfigTestConstants;
import org.ebayopensource.turmeric.eclipse.config.test.core.TestSOAGlobalConfigAccessor;
import org.junit.Assert;
import org.junit.Test;


/**
 * @author yayu
 *
 */
public class TestSOAConfigExtensionFactory {

	/**
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.config.repo.SOAConfigExtensionFactory#getWSDLTemplate(java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testGetWSDLTemplate() throws Exception {
		URL url = SOAConfigExtensionFactory.getWSDLTemplate( 
				ConfigTestConstants.CONFIG_TEST_ORG_ID, 
				"Test_NoOperationTemplate");
		TestSOAGlobalConfigAccessor.checkUrlInstance(url);
		
		url = SOAConfigExtensionFactory.getWSDLTemplate( 
				ConfigTestConstants.CONFIG_TEST_ORG_ID, 
				"hello");
		Assert.assertNull(url);
	}

	/**
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.config.repo.SOAConfigExtensionFactory#getXSDTemplates(java.lang.String)}.
	 */
	@Test
	public void testGetXSDTemplates() throws Exception {
		Map<SOAXSDTemplateSubType, List<SOAConfigTemplate>> data = 
			SOAConfigExtensionFactory.getXSDTemplates(ConfigTestConstants.CONFIG_TEST_ORG_ID);
		Assert.assertNotNull(data);
		for (SOAXSDTemplateSubType subType : SOAXSDTemplateSubType.values()) {
			List<SOAConfigTemplate> list = data.get(subType);
			Assert.assertNotNull(list);
			Assert.assertFalse(list.isEmpty());
		}
		
		data = 
			SOAConfigExtensionFactory.getXSDTemplates("hello");
		Assert.assertNotNull(data);
		Assert.assertTrue(data.isEmpty());
	}

	/**
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.config.repo.SOAConfigExtensionFactory#getWSDLTemplates(java.lang.String)}.
	 */
	@Test
	public void testGetWSDLTemplates() throws Exception {
		List<SOAConfigTemplate> templates = 
			SOAConfigExtensionFactory.getWSDLTemplates(ConfigTestConstants.CONFIG_TEST_ORG_ID);
		Assert.assertNotNull(templates);
		
		templates = 
			SOAConfigExtensionFactory.getWSDLTemplates("hello");
		Assert.assertNotNull(templates);
		Assert.assertTrue(templates.isEmpty());
	}
	
	/**
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.config.repo.SOAConfigExtensionFactory.SOAConfigTemplate}.
	 */
	@Test
	public void testSOAConfigTemplate() throws Exception {
		String name = "test";
		String organization = ConfigTestConstants.CONFIG_TEST_ORG_ID;
		String relativePath = "path";
		String tempdir = System.getProperty("java.io.tmpdir");
		URL url = new File(tempdir).toURI().toURL();
		SOAConfigTemplate template = new SOAConfigTemplate(name, organization, relativePath, url);
		Assert.assertEquals(name, template.getName());
		Assert.assertEquals(organization, template.getOrganization());
		Assert.assertEquals(relativePath, template.getRelativePath());
		Assert.assertEquals(url, template.getUrl());
		
		template = new SOAConfigTemplate(name, organization, relativePath, url, SOAXSDTemplateSubType.COMPLEX);
		Assert.assertEquals(name, template.getName());
		Assert.assertEquals(organization, template.getOrganization());
		Assert.assertEquals(relativePath, template.getRelativePath());
		Assert.assertEquals(url, template.getUrl());
		Assert.assertEquals(SOAXSDTemplateSubType.COMPLEX, template.getSubType());
		
		template = new SOAConfigTemplate(null, null, null, null);
		template.setName(name);
		template.setOrganization(organization);
		template.setRelativePath(relativePath);
		template.setSubType(SOAXSDTemplateSubType.COMPLEX);
		template.setUrl(url);
		Assert.assertEquals(name, template.getName());
		Assert.assertEquals(organization, template.getOrganization());
		Assert.assertEquals(relativePath, template.getRelativePath());
		Assert.assertEquals(url, template.getUrl());
		Assert.assertEquals(SOAXSDTemplateSubType.COMPLEX, template.getSubType());
	}

}
