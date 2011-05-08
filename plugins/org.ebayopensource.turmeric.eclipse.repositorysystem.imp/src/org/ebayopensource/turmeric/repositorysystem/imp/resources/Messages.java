/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.repositorysystem.imp.resources;

import org.eclipse.osgi.util.NLS;

/**
 * Standard messages class. Holds the UI and non UI (log) messages
 * 
 * @author yayu
 * @since 1.0.0
 */
public class Messages extends NLS {

	private static final String BUNDLE_NAME = "org.ebayopensource.turmeric.eclipse.repositorysystem.turmeric.resources.messages"; //$NON-NLS-1$
	
	/** The ERRO r_ servic e_ alread y_ exist. */
	public static String ERROR_SERVICE_ALREADY_EXIST;
	
	/** The ERRO r_ servic e_ validatio n_ failed. */
	public static String ERROR_SERVICE_VALIDATION_FAILED;
	
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}
}
