/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.repositorysystem.preferences.core;

import java.util.List;

import org.ebayopensource.turmeric.eclipse.soatools.configtool.ConfigTool;


/**
 * The Class PreferenceConstants.
 *
 * @author smathew
 * 
 * Normal constants file which hold the preference name and default values.
 */
public class PreferenceConstants {

	/** The Constant PREF_REPOSITORY_SYSTEM. */
	public static final String PREF_REPOSITORY_SYSTEM = "repositorySystem";
	
	/** The Constant PREF_ORGANIZATION. */
	public static final String PREF_ORGANIZATION = "organization";

	/** The Constant _PREF_DEFAULT_REPOSITORY_SYSTEM. */
	public static final String _PREF_DEFAULT_REPOSITORY_SYSTEM = "Turmeric";
	
	/** The Constant PREF_DEFAULT_ORGANIZATION. */
	public static final String PREF_DEFAULT_ORGANIZATION = 
		"Others";
	
	/** The Constant PREF_DEFAULT_ORGANIZATION_DISPLAY_NAME. */
	public static final String PREF_DEFAULT_ORGANIZATION_DISPLAY_NAME = 
		"Default";
	
//	public static final String PREF__WARNING = 
//			"Warning";
//	public static final String PREF__ERROR = 
//			"Error";
//	public static final String PREF_ERROR_LEVEL_NAME = 
//			"ErrorLevel";
	// there will be new preference field for this which accepts
	// a new service file
	/** The Constant PREF_SERVICE_LAYERS. */
	public static final String PREF_SERVICE_LAYERS = "serviceLayers";

	/**
	 * Returns the default service layer values from a codegen call. Need to
	 * find out if we can set a new file to codegen and in that case a new
	 * preference value will be added
	 * 
	 * @return String
	 */
	public static String getDefaultServiceLayers() {
		List<String> serviceLayerList = ConfigTool
				.getDefaultServiceLayersFromFile();
		StringBuffer listBuffer = new StringBuffer();
		for (String str : serviceLayerList) {
			listBuffer.append(str);
			listBuffer.append(",");
		}
		// there is an additional comma at the end which has to be stripped
		// to make it clean
		// An exception here means there is a serious problem
		return listBuffer.substring(0, listBuffer.length() - 1);
	}

}
