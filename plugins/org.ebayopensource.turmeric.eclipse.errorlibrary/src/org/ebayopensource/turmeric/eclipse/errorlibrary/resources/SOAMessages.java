/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.errorlibrary.resources;

import org.eclipse.osgi.util.NLS;

/**
 * Holds all the messages. Similar classes could be found in similar packages
 * 
 * @author smathew
 * @author yayu
 */
public class SOAMessages extends NLS {
	private static final String BUNDLE_NAME = "org.ebayopensource.turmeric.eclipse.errorlibrary.resources.messages"; //$NON-NLS-1$
	

	public static String PASTE_OP_NOT_AVAILABLE;
	public static String PASTE_OP_NOT_AVAILABLE_RES;
	public static String DEP_MANIFEST_READ_ONLY;
	public static String PREF_CONTENT_PROVIDER;
	public static String PREF_CONTENT_PROVIDER_NOT_FOUND;

	public static String CONTENT_PROVIDER_NOT_FOUND;
	public static String LIBRARY_NOT_BUILT_ERR_MSG;
	public static String BAD_ERR_REGISTRY;
	public static String DIALOG_TITLE_ERROR;
	public static String CREATING_DOMAIN;
	public static String CREATE_DOMAIN_ERR;
	public static String NEW_ERR_DOMAIN;
	public static String CREATING_ERRLIB;
	public static String CREATE_ERRLIB_ERR;
	public static String NEW_ERR_LIB;
	public static String CREATING_ERR;
	public static String CREATE_ERR_ERR;
	public static String NEW_ERR;
	public static String NOT_AVAILABLE;
	public static String ERROR_LIBRARIES;
	public static String ERRORS;
	public static String DRAG_EERROR;
	public static String DRAG_FAILED;

	public static String UI_ALL_ERR_LIBS;
	public static String UI_ERRORLIB;
	public static String UI_SEARCH_ERROR;
	public static String UI_FILTER_ERROR;
	public static String UI_SORT_ERROR;

	public static String SEARCH_TIP;

	public static String TREE_TIP;
	
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, SOAMessages.class);
	}

}
