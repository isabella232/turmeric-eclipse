/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.errorlibrary.utils;

/**
 * @author smathew
 * 
 * Standard Constants file for error lib
 * 
 */
public class SOAErrorLibraryConstants {

	public static final String ERR_PROP_NAME = "name";
	public static final String ERR_PROP_NID = "nid";
	public static final String ERR_PROP_CATEGORY = "category";
	public static final String ERR_PROP_SUB_DOMAIN = "subdomain";
	public static final String ERR_PROP_SEVERITY = "severity";
	public static final String ERR_PROP_GRP = "errorgroups";
	public static final String ERR_PROP_MESSAGE = "message";
	public static final String ERR_PROP_RESOLUTION = "resolution";
	public static final String ORGANIZATION = "organization";
	public static final String DOMAIN = "domain";
	public static final String TYPE_LIB = "typelib";
	public static final String DOMAIN_PROP_VERSION = "errorVersion";

	public static final String ID = "id";

	public static final String ERR_VIEW_COLUMN_NAME = "Name";
	public static final String ERR_VIEW_COLUMN_CATEGORY = "Category";
	public static final String ERR_VIEW_COLUMN_SUBDOMAIN = "SubDomain";
	public static final String ERR_VIEW_COLUMN_SEVERITY = "Severity";
	public static final String ERR_VIEW_COLUMN_MESSAGE = "Message";
	public static final String ERR_VIEW_COLUMN_RESOLUTION = "Resolution";

	

	public static final String DEFAULT_ORGANIZATION = "Marketplace";
	public static final String[] ERROR_CATEGORIES = { "APPLICATION", "SYSTEM",
			"REQUEST" };
	public static final String[] ERROR_SEVERITY = { "ERROR", "WARNING" };

	public static final String DEfAULT_CATEGORY = "category";
	public static final String DEfAULT_NAME = "application_error";
	public static final String DEFAULT_SEVERITY = "WARNING";
	public static final String DEFAULT_MSG = "New message";
	public static final String DEFAULT_SUBDOMAIN = "Buying";

	public static String errorLibraryCentralLocation;
	
}
