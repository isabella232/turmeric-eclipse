/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.ui.resources;

import org.eclipse.osgi.util.NLS;


/**
 * Standard messages class. Holds the UI and non UI (log) messages
 * 
 * @author smathew
 * @author yayu
 * 
 */
public class SOAMessages extends NLS {
	private static final String BUNDLE_NAME = "org.ebayopensource.turmeric.eclipse.ui.resources.messages"; //$NON-NLS-1$

	/** The OVERRIDE. */
	public static String OVERRIDE;

	/** The OPTIONS. */
	public static String OPTIONS;

	/** The SEARCH. */
	public static String SEARCH;

	/** The TYP e_ lib. */
	public static String TYPE_LIB;

	/** The TYP e_ li b_ wks. */
	public static String TYPE_LIB_WKS;
	
	/** The TYPES. */
	public static String TYPES;
	
	/** The TYPE s_ wks. */
	public static String TYPES_WKS;
	
	/** The SEARC h_ tip. */
	public static String SEARCH_TIP;
	
	/** The TRE e_ tip. */
	public static String TREE_TIP;
	
	/** The TYP e_ li b_ err. */
	public static String TYPE_LIB_ERR;
	
	/** The ERROR. */
	public static String ERROR;
	
	/** The NMSPAC e_ err. */
	public static String NMSPACE_ERR;

	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, SOAMessages.class);
	}
		
}
