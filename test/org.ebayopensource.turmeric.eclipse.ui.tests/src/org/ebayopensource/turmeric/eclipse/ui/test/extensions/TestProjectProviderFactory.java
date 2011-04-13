/*******************************************************************************
 * Copyright (c) 2006-2011 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.ui.test.extensions;

import java.util.Collection;
import java.util.List;

import org.ebayopensource.turmeric.eclipse.ui.extensions.ITurmericProvider;
import org.ebayopensource.turmeric.eclipse.ui.extensions.ITurmericWizard;
import org.ebayopensource.turmeric.eclipse.ui.extensions.ITurmericWizardPage;
import org.ebayopensource.turmeric.eclipse.ui.extensions.ProjectProviderFactory;
import org.eclipse.jface.wizard.IWizardPage;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TestProjectProviderFactory {

	Collection<ITurmericProvider> providers = null;
	
	@Before
	public void setUp() {
		ProjectProviderFactory factory = new ProjectProviderFactory();
		providers = factory.createProviders();
	}
	
	@After
	public void tearDown() {
		providers = null;
	}
	
	@Test
	public void testCreateProviders() {
		assertNotNull(providers);
		assertFalse(providers.isEmpty());
	}

	@Test
	public void testProviderHasWizards() {
		ITurmericProvider provider = providers.iterator().next();
		assertTrue(provider.hasWizards());
	}
	
	@Test
	public void testProviderHasServiceWizards() {
		ITurmericProvider provider = providers.iterator().next();
		assertTrue(provider.hasServiceWizard());
	}
	
	@Test
	public void testFindServiceWizard() {
		ITurmericProvider provider = providers.iterator().next();
		ITurmericWizard wizard = provider.findWizard("Service");
		assertTrue("Wizard is not of type Service", wizard.isType("Service"));
	}
	
	@Test
	public void testWizardHasPages() {
		ITurmericProvider provider = providers.iterator().next();
		ITurmericWizard wizard = provider.findWizard("Service");
		assertTrue("No pages defined for wizard.", wizard.hasPages());
	}
	
	@Test 
	public void testWizardPageList() {
		ITurmericProvider provider = providers.iterator().next();
		ITurmericWizard wizard = provider.findWizard("Service");
		List<ITurmericWizardPage> pages = wizard.allPages();
		assertFalse(pages.isEmpty());
	}
	
	@Test 
	public void testWizardPageValid() {
		ITurmericProvider provider = providers.iterator().next();
		ITurmericWizard wizard = provider.findWizard("Service");
		List<ITurmericWizardPage> pages = wizard.allPages();
		for(ITurmericWizardPage page : pages) {
			if (!(page.createWizardPage() instanceof IWizardPage)) {
				fail("Page that was created was not a IWizardPage");
			}
		}
	}
	
	
}
