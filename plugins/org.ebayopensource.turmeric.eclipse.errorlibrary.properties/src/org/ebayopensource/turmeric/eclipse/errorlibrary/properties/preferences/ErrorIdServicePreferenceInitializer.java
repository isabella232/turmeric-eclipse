/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.errorlibrary.properties.preferences;

import org.ebayopensource.turmeric.eclipse.core.logging.SOALogger;
import org.ebayopensource.turmeric.eclipse.errorlibrary.properties.Activator;
import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.core.runtime.preferences.DefaultScope;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.osgi.service.prefs.BackingStoreException;

/**
 * Class used to initialize default preference values.
 */
public class ErrorIdServicePreferenceInitializer extends
		AbstractPreferenceInitializer {


	/**
	 * {@inheritDoc}
	 */
	@Override
	public void initializeDefaultPreferences() {
		IPreferenceStore store = Activator.getDefault().getPreferenceStore();
		store.setDefault(ErrorIdServicePreferenceConstants.USELOCALHOST, true);
		store.setDefault(ErrorIdServicePreferenceConstants.USEREMOTEHOST, false);
		store.setDefault(ErrorIdServicePreferenceConstants.REMOTEENDPOINTURL,
				"http://www.example.org/AdminASV1");
		store.setDefault(ErrorIdServicePreferenceConstants.LOCALFILEPATH,
				System.getProperty("user.home"));
	}

	public static String getErrorIdServiceEndpoint() {
		return Activator.getDefault().getPreferenceStore()
				.getString(ErrorIdServicePreferenceConstants.REMOTEENDPOINTURL);
	}

	public static boolean useLocalHost() {
		return Activator.getDefault().getPreferenceStore()
				.getBoolean(ErrorIdServicePreferenceConstants.USELOCALHOST);
	}
}
