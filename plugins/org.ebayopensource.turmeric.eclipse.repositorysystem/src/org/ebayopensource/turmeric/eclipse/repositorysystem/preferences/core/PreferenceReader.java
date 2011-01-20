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
import org.ebayopensource.turmeric.eclipse.repositorysystem.preferences.util.PreferenceUtil;


/**
 * @author smathew
 * 
 * Wrapper Reader class to read SOA Repo Preferences
 */
public class PreferenceReader {

	public static List<String> getServiceLayer() {
		String serviceLayer = PreferenceUtil.getPreferenceStore().getString(
				PreferenceConstants.PREF_SERVICE_LAYERS);
		String layers[] = StringUtils.split(serviceLayer, ",");
		return Arrays.asList(layers);
	}

	public static String getCurrentRepositorySystemId() {
		return PreferenceUtil.getPreferenceStore().getString(
				PreferenceConstants.PREF_REPOSITORY_SYSTEM);
	}
	
	public static String getCurrentOrganizationId() {
		String orgId = PreferenceUtil.getPreferenceStore().getString(
				PreferenceConstants.PREF_ORGANIZATION);
		if (StringUtils.isBlank(orgId)) {
			orgId = PreferenceConstants.PREF_DEFAULT_ORGANIZATION;
			PreferenceUtil.getPreferenceStore().setDefault(
					PreferenceConstants.PREF_ORGANIZATION, 
					orgId);
		}
		return orgId;
	}
}
