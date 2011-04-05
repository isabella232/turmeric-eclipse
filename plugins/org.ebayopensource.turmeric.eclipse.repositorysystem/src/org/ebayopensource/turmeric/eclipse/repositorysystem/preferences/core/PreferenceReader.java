/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.repositorysystem.preferences.core;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.ebayopensource.turmeric.eclipse.repositorysystem.RepositorySystemActivator;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;

/**
 * @author smathew
 * 
 * Wrapper Reader class to read SOA Repo Preferences
 */
public class PreferenceReader {

	public static List<String> getServiceLayer() {
		IEclipsePreferences prefs = RepositorySystemActivator.getDefault().getPreferences();
		String serviceLayer = prefs.get(PreferenceConstants.PREF_SERVICE_LAYERS,  PreferenceConstants.getDefaultServiceLayers());
		String layers[] = StringUtils.split(serviceLayer, ",");
		return Arrays.asList(layers);
	}

	public static String getCurrentRepositorySystemId() {
		IEclipsePreferences prefs = RepositorySystemActivator.getDefault().getPreferences();
		return prefs.get(PreferenceConstants.PREF_REPOSITORY_SYSTEM, PreferenceConstants._PREF_DEFAULT_REPOSITORY_SYSTEM);
	}
	
	public static String getCurrentOrganizationId(String defaultOrgId) {
		IEclipsePreferences prefs = RepositorySystemActivator.getDefault().getPreferences();
		
		String orgId = prefs.get(PreferenceConstants.PREF_ORGANIZATION, "");
		if (orgId.equals("")) {
			orgId = defaultOrgId;
			prefs.put(PreferenceConstants.PREF_ORGANIZATION, orgId);
		}
		return orgId;
	}
}
