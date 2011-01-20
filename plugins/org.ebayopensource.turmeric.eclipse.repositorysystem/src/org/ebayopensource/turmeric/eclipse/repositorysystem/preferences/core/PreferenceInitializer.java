/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.repositorysystem.preferences.core;

import org.ebayopensource.turmeric.eclipse.repositorysystem.preferences.util.PreferenceUtil;
import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;


/**
 * @author smathew
 * 
 * The preference initializer which will be called for the default 
 * first time the preference is read.
 * 
 */
public class PreferenceInitializer extends AbstractPreferenceInitializer {

	/* (non-Javadoc)
	 * @see org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer#initializeDefaultPreferences()
	 * default preference value initialization
	 */
	@Override
	public void initializeDefaultPreferences() {
		final IPreferenceStore store = PreferenceUtil.getPreferenceStore();
		store.setDefault(PreferenceConstants.PREF_REPOSITORY_SYSTEM,
				PreferenceConstants.PREF_DEFAULT_REPOSITORY_SYSTEM);
		store.setDefault(PreferenceConstants.PREF_SERVICE_LAYERS,
				PreferenceConstants.getDefaultServiceLayers());
		

	}

}
