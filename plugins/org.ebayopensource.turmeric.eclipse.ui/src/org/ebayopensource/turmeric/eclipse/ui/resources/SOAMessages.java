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

	public static String OVERRIDE;

	public static String OPTIONS;

	public static String SEARCH;

	public static String TYPE_LIB;

	public static String TYPE_LIB_WKS;
	
	public static String TYPES;
	
	public static String TYPES_WKS;
	
	public static String SEARCH_TIP;
	
	public static String TREE_TIP;
	
	public static String TYPE_LIB_ERR;
	
	public static String ERROR;
	
	public static String NMSPACE_ERR;

	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, SOAMessages.class);
	}
		
}
