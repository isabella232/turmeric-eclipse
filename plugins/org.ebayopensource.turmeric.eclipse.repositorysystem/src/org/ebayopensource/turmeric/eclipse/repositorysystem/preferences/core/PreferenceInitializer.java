/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.repositorysystem.preferences.core;

import org.ebayopensource.turmeric.eclipse.core.logging.SOALogger;
import org.ebayopensource.turmeric.eclipse.repositorysystem.RepositorySystemActivator;
import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.core.runtime.preferences.DefaultScope;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.osgi.service.prefs.BackingStoreException;


/**
 * The preference initializer which will be called for the default 
 * first time the preference is read.
 * 
 * @author dcarver
 * @since 1.0 Upgraded to use IEclipsePreferences.
 * 
 */
public class PreferenceInitializer extends AbstractPreferenceInitializer {

	/* (non-Javadoc)
	 * @see org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer#initializeDefaultPreferences()
	 * default preference value initialization
	 */
	@Override
	public void initializeDefaultPreferences() {
		IEclipsePreferences node = new DefaultScope().getNode(RepositorySystemActivator.getDefault().getBundle().getSymbolicName());
		IEclipsePreferences instanceNode = new InstanceScope().getNode(RepositorySystemActivator.getDefault().getBundle().getSymbolicName());
		
		setNodeValues(node);
		
		try {
			if (instanceNode.keys() != null && instanceNode.keys().length == 0) {
				setNodeValues(instanceNode);
				instanceNode.flush();
			}
		} catch (BackingStoreException e) {
			SOALogger.getLogger().warning(e);
		}
	
	}

	private void setNodeValues(IEclipsePreferences node) {
		node.put(PreferenceConstants.PREF_REPOSITORY_SYSTEM,
				PreferenceConstants._PREF_DEFAULT_REPOSITORY_SYSTEM);
		node.put(PreferenceConstants.PREF_SERVICE_LAYERS,
				PreferenceConstants.getDefaultServiceLayers());
		node.put(PreferenceConstants.PREF_ERROR_LEVEL_NAME, 
				PreferenceConstants.PREF__WARNING);
	}
	

}
