/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.repositorysystem.resources;

import org.eclipse.osgi.util.NLS;

/**
 * Standard messages class. Holds the UI and non UI (log) messages
 * 
 * @author yayu
 * 
 */
public class SOAMessages extends NLS {

	private static final String BUNDLE_NAME = "org.ebayopensource.turmeric.eclipse.repositorysystem.resources.messages"; //$NON-NLS-1$
	
	/** The ERRO r_ a r_ ou t_ o f_ date. */
	public static String  ERROR_AR_OUT_OF_DATE;
	
	/** The WARNIN g_ a r_ no t_ available. */
	public static String  WARNING_AR_NOT_AVAILABLE;
	
	/** The ERRO r_ fai l_ t o_ submi t_ servic e_ versio n_ t o_ ar. */
	public static String ERROR_FAIL_TO_SUBMIT_SERVICE_VERSION_TO_AR;

	
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, SOAMessages.class);
	}
}
