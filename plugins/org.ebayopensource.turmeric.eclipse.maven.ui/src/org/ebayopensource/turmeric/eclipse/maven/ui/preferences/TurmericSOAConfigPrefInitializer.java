/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.maven.ui.preferences;

import org.ebayopensource.turmeric.eclipse.maven.ui.Activator;
import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

/**
 * The Class TurmericSOAConfigPrefInitializer.
 *
 * @author yayu
 */
public class TurmericSOAConfigPrefInitializer extends
		AbstractPreferenceInitializer {
	
	/** The Constant PREF_KEY_OVERWRITE_PREFERRED_VERSOIN. */
	public static final String PREF_KEY_OVERWRITE_PREFERRED_VERSOIN = "overwrite_turmeric_preferred_version";
	
	/** The Constant PREF_KEY_MINIMUM_REQUIRED_VERSOIN. */
	public static final String PREF_KEY_MINIMUM_REQUIRED_VERSOIN = "min_turmeric_required_version";
	
	/** The Constant PREF_KEY_TURMERIC_PREFERRED_VERSOIN. */
	public static final String PREF_KEY_TURMERIC_PREFERRED_VERSOIN = "turmeric_preferred_version";

	/**
	 * Instantiates a new turmeric soa config pref initializer.
	 */
	public TurmericSOAConfigPrefInitializer() {
		super();
	}

	@Override
	public void initializeDefaultPreferences() {
		final IPreferenceStore store = getPreferenceStore();
		store.setDefault(PREF_KEY_OVERWRITE_PREFERRED_VERSOIN,
				Boolean.FALSE.toString());
		store.setDefault(PREF_KEY_TURMERIC_PREFERRED_VERSOIN,
				"");
	}
	
	/**
	 * Gets the preference store.
	 *
	 * @return the preference store
	 */
	public static IPreferenceStore getPreferenceStore() {
		return Activator.getDefault().getPreferenceStore();
	}
	
	/**
	 * Gets the preferred version.
	 *
	 * @return the preferred runtime version to use
	 */
	public static String getPreferredVersion() {
		if (getPreferenceStore().getBoolean(PREF_KEY_OVERWRITE_PREFERRED_VERSOIN) == true) {
			return getPreferenceStore().getString(PREF_KEY_TURMERIC_PREFERRED_VERSOIN);
		}
		return null;
	}

}
