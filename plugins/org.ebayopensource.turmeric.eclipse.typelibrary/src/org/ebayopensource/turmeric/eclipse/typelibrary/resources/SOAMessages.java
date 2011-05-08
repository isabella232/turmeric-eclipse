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

	/** The TYP e_ lib. */
	public static String TYPE_LIB;

	/** The TYP e_ li b_ wks. */
	public static String TYPE_LIB_WKS;

	/** The TYPES. */
	public static String TYPES;

	/** The TYPE s_ wks. */
	public static String TYPES_WKS;

	/** The SEARCH. */
	public static String SEARCH;

	/** The SEARC h_ tip. */
	public static String SEARCH_TIP;

	/** The TRE e_ tip. */
	public static String TREE_TIP;

	/** The INVALI d_ xs d_ url. */
	public static String INVALID_XSD_URL;

	/** The INVALI d_ xsd. */
	public static String INVALID_XSD;

	/** The FORMA t_ err. */
	public static String FORMAT_ERR;

	/** The FORMA t_ er r_ details. */
	public static String FORMAT_ERR_DETAILS;

	/** The O p_ err. */
	public static String OP_ERR;
	
	/** The O p_ er r_ details. */
	public static String OP_ERR_DETAILS;

	/** The IMPOR t_ success. */
	public static String IMPORT_SUCCESS;
	
	/** The O p_ success. */
	public static String OP_SUCCESS;
	
	/** The TYPEDE p_ err. */
	public static String TYPEDEP_ERR;
	
	/** The TYPEDE p_ er r_ enf. */
	public static String TYPEDEP_ERR_ENF;
	
	/** The DU p_ types. */
	public static String DUP_TYPES;
	
	/** The TYP e_ lin e_ fail. */
	public static String TYPE_LINE_FAIL;

	/** The IMPOR t_ err. */
	public static String IMPORT_ERR;
	
	/** The IMPOR t_ failed. */
	public static String IMPORT_FAILED;
	
	/** The BUIL d_ proj. */
	public static String BUILD_PROJ;
	
	/** The IMPOR t_ xs d_ err. */
	public static String IMPORT_XSD_ERR;
	
	/** The XS d_ er r_ details. */
	public static String XSD_ERR_DETAILS;
	
	/** The SE l_ type. */
	public static String SEL_TYPE;

	/** The N o_ updts. */
	public static String NO_UPDTS;
	
	/** The N o_ updt s_ dtl. */
	public static String NO_UPDTS_DTL;
	
	/** The NE w_ vrsn. */
	public static String NEW_VRSN;
	
	/** The UPDT s_ done. */
	public static String UPDTS_DONE;
	
	/** The UPDT s_ don e_ dtl. */
	public static String UPDTS_DONE_DTL;

	/** The ER r_ ds t_ schema. */
	public static String ERR_DST_SCHEMA;

	/** The ER r_ nmspc. */
	public static String ERR_NMSPC;
	
	/** The TYP e_ li b_ err. */
	public static String TYPE_LIB_ERR;

	/** The ERROR. */
	public static String ERROR;

	/** The ER r_ delet e_ type. */
	public static String ERR_DELETE_TYPE;

	// mzang 2010-4-20 id for property page.
	/** The ER r_ chang e_ majo r_ versio n_ no t_ allowed. */
	public static String ERR_CHANGE_MAJOR_VERSION_NOT_ALLOWED;
	
	/** The ER r_ smalle r_ versio n_ no t_ allowed. */
	public static String ERR_SMALLER_VERSION_NOT_ALLOWED;
	
	/** The ER r_ impor t_ type s_ fil e_ no t_ support. */
	public static String ERR_IMPORT_TYPES_FILE_NOT_SUPPORT;
	
	/** The ER r_ impor t_ type s_ n o_ templat e_ found. */
	public static String ERR_IMPORT_TYPES_NO_TEMPLATE_FOUND;
	
	/** The ER r_ selecte d_ typ e_ alread y_ import. */
	public static String ERR_SELECTED_TYPE_ALREADY_IMPORT;

	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, SOAMessages.class);
	}
}
