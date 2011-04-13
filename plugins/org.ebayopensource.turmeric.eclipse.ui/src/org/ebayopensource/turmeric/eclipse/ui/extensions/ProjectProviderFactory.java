/*******************************************************************************
 * Copyright (c) 2006-2011 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/

package org.ebayopensource.turmeric.eclipse.ui.extensions;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;


public class ProjectProviderFactory {
	
	private static final String PROVIDER_NAME = "provider";
	private static final String PROJECT_WIZARD_PROVIDER_ID = "org.ebayopensource.turmeric.eclipse.ui.projectWizardProvider";
	private IExtensionPoint extensionPoint;

	public ProjectProviderFactory() {
		IExtensionRegistry registry = Platform.getExtensionRegistry();
		extensionPoint  = registry.getExtensionPoint(PROJECT_WIZARD_PROVIDER_ID);

	}
		
	public Collection<ITurmericProvider>createProviders() {
		IExtension[] extensions = extensionPoint.getExtensions();
		ArrayList<ITurmericProvider> providers = new ArrayList<ITurmericProvider>();
		for(IExtension ext : extensions) {
			IConfigurationElement[] elems = ext.getConfigurationElements();
			for (IConfigurationElement elm : elems) {
				if (PROVIDER_NAME.equals(elm.getName())) {
					ITurmericProvider provider = new TurmericProvider(elm);
					providers.add(provider);
				}
			}
		}
		return providers;
	}
}
