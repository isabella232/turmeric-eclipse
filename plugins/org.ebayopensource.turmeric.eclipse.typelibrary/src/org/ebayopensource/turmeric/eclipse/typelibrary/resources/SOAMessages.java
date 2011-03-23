/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.typelibrary.resources;

import org.eclipse.osgi.util.NLS;

/**
 * The standard messages class. All UI and log messages are stored here.
 * 
 * @author smathew
 * @author yayu
 * 
 */
public class SOAMessages extends NLS {
	private static final String BUNDLE_NAME = "org.ebayopensource.turmeric.eclipse.typelibrary.resources.messages"; //$NON-NLS-1$

	public static String TYPE_LIB;

	public static String TYPE_LIB_WKS;

	public static String TYPES;

	public static String TYPES_WKS;

	public static String SEARCH;

	public static String SEARCH_TIP;

	public static String TREE_TIP;

	public static String INVALID_XSD_URL;

	public static String INVALID_XSD;

	public static String FORMAT_ERR;

	public static String FORMAT_ERR_DETAILS;

	public static String OP_ERR;
	public static String OP_ERR_DETAILS;

	public static String IMPORT_SUCCESS;
	public static String OP_SUCCESS;
	public static String TYPEDEP_ERR;
	public static String TYPEDEP_ERR_ENF;
	public static String DUP_TYPES;
	public static String TYPE_LINE_FAIL;

	public static String IMPORT_ERR;
	public static String IMPORT_FAILED;
	public static String BUILD_PROJ;
	public static String IMPORT_XSD_ERR;
	public static String XSD_ERR_DETAILS;
	public static String SEL_TYPE;

	public static String NO_UPDTS;
	public static String NO_UPDTS_DTL;
	public static String NEW_VRSN;
	public static String UPDTS_DONE;
	public static String UPDTS_DONE_DTL;

	public static String ERR_DST_SCHEMA;

	public static String ERR_NMSPC;
	public static String TYPE_LIB_ERR;

	public static String ERROR;

	public static String ERR_DELETE_TYPE;

	// mzang 2010-4-20 id for property page.
	public static String ERR_CHANGE_MAJOR_VERSION_NOT_ALLOWED;
	public static String ERR_SMALLER_VERSION_NOT_ALLOWED;
	public static String ERR_IMPORT_TYPES_FILE_NOT_SUPPORT;
	public static String ERR_IMPORT_TYPES_NO_TEMPLATE_FOUND;
	public static String ERR_SELECTED_TYPE_ALREADY_IMPORT;

	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, SOAMessages.class);
	}
}
