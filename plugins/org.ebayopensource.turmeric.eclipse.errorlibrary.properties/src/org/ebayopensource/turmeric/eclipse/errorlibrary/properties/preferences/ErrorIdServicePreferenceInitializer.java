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
import org.ebayopensource.turmeric.eclipse.repositorysystem.RepositorySystemActivator;
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer#
	 * initializeDefaultPreferences()
	 */
	public void initializeDefaultPreferences() {
		String symbolicName = Activator.getDefault().getBundle().getSymbolicName();
		IEclipsePreferences node = new DefaultScope().getNode(symbolicName);
		IEclipsePreferences instanceNode = new InstanceScope().getNode(symbolicName);
		
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
		node.putBoolean(ErrorIdServicePreferenceConstants.USELOCALHOST, true);
		node.putBoolean(ErrorIdServicePreferenceConstants.USEREMOTEHOST, false);
		node.put(ErrorIdServicePreferenceConstants.REMOTEENDPOINTURL, "http://www.example.org/AdminASV1");
		node.put(ErrorIdServicePreferenceConstants.LOCALFILEPATH, System.getProperty("user.home"));
	}
}
