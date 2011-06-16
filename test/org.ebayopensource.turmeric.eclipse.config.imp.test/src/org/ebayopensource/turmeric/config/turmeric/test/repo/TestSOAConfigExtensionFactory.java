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
package org.ebayopensource.turmeric.config.turmeric.test.repo;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.util.List;
import java.util.Map;

import org.ebayopensource.turmeric.config.turmeric.test.core.TestSOADomainAccessor;
import org.ebayopensource.turmeric.config.turmeric.test.core.TestSOAGlobalConfigAccessor;
import org.ebayopensource.turmeric.eclipse.config.repo.SOAConfigExtensionFactory;
import org.ebayopensource.turmeric.eclipse.config.repo.SOAConfigExtensionFactory.SOAConfigTemplate;
import org.ebayopensource.turmeric.eclipse.config.repo.SOAConfigExtensionFactory.SOAXSDTemplateSubType;
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
	public void testGetWSDLTemplates() throws Exception {
		List<SOAConfigTemplate> templates = 
			SOAConfigExtensionFactory.getWSDLTemplates(TestSOADomainAccessor.TURMERIC_ID);
		assertNotNull(templates);
		assertFalse(templates.isEmpty());
		for (SOAConfigTemplate template : templates) {
			assertNotNull(template.getUrl());
			TestSOAGlobalConfigAccessor.checkUrlInstance(template.getUrl());
		}
	}

	/**
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.config.repo.SOAConfigExtensionFactory#getXSDTemplates(java.lang.String)}.
	 */
	@Test
	public void testGetXSDTemplates() throws Exception {
		Map<SOAXSDTemplateSubType, List<SOAConfigTemplate>> data = 
			SOAConfigExtensionFactory.getXSDTemplates(TestSOADomainAccessor.TURMERIC_ID);
		assertNotNull(data);
		assertFalse(data.isEmpty());
		for (SOAXSDTemplateSubType subType : SOAXSDTemplateSubType.values()) {
			List<SOAConfigTemplate> list = data.get(subType);
			assertNotNull(list);
			assertFalse(list.isEmpty());
			for (SOAConfigTemplate template : list) {
				assertNotNull(template.getUrl());
				TestSOAGlobalConfigAccessor.checkUrlInstance(template.getUrl());
			}
		}
	}

}
