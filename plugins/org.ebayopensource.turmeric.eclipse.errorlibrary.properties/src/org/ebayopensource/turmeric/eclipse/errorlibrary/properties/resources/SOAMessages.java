/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.errorlibrary.properties.resources;

import org.eclipse.osgi.util.NLS;

/**
 * Standard messages class. Holds the UI and non UI (log) messages
 * 
 * @author yayu
 * 
 */
public class SOAMessages extends NLS {

	private static final String BUNDLE_NAME = "org.ebayopensource.turmeric.eclipse.errorlibrary.properties.resources.messages"; //$NON-NLS-1$
	
	//UI texts
	public static String TEXT_NAME_ERROR_LIBRARY;
	public static String TEXT_NAME_PROJECT_NAME_PREFIX ;
	public static String TOOLTIP_NAME_PROJECT_NAME_PREFIX;
	public static String TEXT_NAME_PROJECT_NAME;
	public static String TOOLTIP_NAME_PROJECT_NAME;
	public static String TEXT_NAME_CONTENT_REPO_ROOT;
	public static String TOOLTIP_NAME_CONTENT_REPO_ROOT;
	public static String TEXT_NAME_LOCALE;
	public static String TOOLTIP_NAME_LOCALE;
	public static String TEXT_NAME_VERSION;
	public static String TOOLTIP_NAME_VERSION_ERRORLIB;
	public static String BUTTON_NAME_BROWSE;
	public static String DIALOG_TITLE_SELECT_ERROR_LIBRARY;
	public static String TEXT_NAME_DOMAIN_NAME;
	public static String PACKAGE_NAME_DOMAIN_NAME;
	public static String TOOLTIP_NAME_DOMAIN_NAME;
	public static String TOOLTIP_NAME_PACKAGE_NAME;
	public static String TEXT_NAME_DOMAIN;
	public static String TOOLTIP_NAME_DOMAIN;
	public static String TEXT_NAME_SUBDOMAIN;
	public static String TOOLTIP_NAME_SUBDOMAIN;
	public static String TEXT_NAME_ORGANIZATION;
	public static String TOOLTIP_NAME_ORGANIZATION;
	public static String GROUP_TITLE_CONTENT_STRUCTURE;
	public static String TEXT_NAME_NAME;
	public static String TOOLTIP_NAME_NAME;
	public static String TEXT_NAME_MESSAGE;
	public static String TOOLTIP_NAME_MESSAGE;
	public static String TEXT_NAME_RESOLUTION;
	public static String TOOLTIP_NAME_RESOLUTION;
	public static String TEXT_NAME_CATEGORY;
	public static String TOOLTIP_NAME_CATEGORY;
	public static String TEXT_NAME_SEVERITY;
	public static String TOOLTIP_NAME_SEVERITY;
	
	public static String ACTION_TEXT_DELETE;
	public static String ACTION_TEXT_REFRESH;
	public static String TITLE_VALIDATION_FALIED;
	public static String TITLE_EXECUTION_FALIED;
	
	//Validation Errors
	public static String ERROR_NO_ERROR_LIBRARY;
	public static String ERROR_EMPTY_DOMAIN_NAME;
	public static String ERROR_EMPTY_DOMAIN_PACKAGE;
	public static String ERROR_DUPLICATE_DOMAIN_NAME;
	public static String ERROR_DUPLICATE_ERROR_TYPE;
	public static String ERROR_EMPTY_ERROR_MESSAGE;
	public static String ERROR_NO_DOMAIN;
	public static String ERROR_NOSUPPORT_ERRORLIB;
	public static String ERROR_NO_PARENT_ERRORLIB;
	public static String ERROR_NO_ERRORLIB_IN_WORKSPACE;
	public static String ERROR_NO_ERRORDOMAIN_FOLDER;
	public static String ERROR_MISSING_ERRORDOMAIN;
	public static String ERROR_MISSING_DOMAIN_ORGANIZATION;
	
	//LOGGING
	public static String LOG_CONTENT_ALREADY_EXIST;
	public static String LOG_CONTENT_REGISTRY_VIEW_NOT_AVAILABLE;
	public static String LOG_CONTENT_CONTRACT_ALREADY_EXIST;
	public static String LOG_CONTENT_FOLDER_ALREADY_EXIST;
	
	public static String EXCEPTION_INVALID_4CB_FILE;
	
	//Property View
	public static String PROP_KEY_NAME;
	public static String PROP_KEY_LOCALE;
	public static String PROP_KEY_VERSION;
	public static String PROP_KEY_ORG;
	public static String PROP_KEY_PACKAGENAME;
	public static String PROP_KEY_ERROR_ID;
	public static String PROP_KEY_SUBDOMAIN;
	public static String PROP_KEY_MESSAGE;
	public static String PROP_KEY_RESOLUTION;
	public static String PROP_KEY_CATEGORY;
	public static String PROP_KEY_SEVERITY;
	
	//Preference page
	public static String PREFERENCE_DESC;
	public static String USE_LOCAL_CONF;
	public static String USE_REMOTE_HOST;
	public static String SELECT_HOST;
	public static String LOCAL_CONF_FOLDER;
	public static String REMOTE_ENDPOINT;
	
	
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, SOAMessages.class);
	}
}
