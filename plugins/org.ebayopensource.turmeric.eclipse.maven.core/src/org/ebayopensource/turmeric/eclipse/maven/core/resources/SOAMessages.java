/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.maven.core.resources;

import org.eclipse.osgi.util.NLS;

/**
 * Standard messages class. Holds the UI and non UI (log) messages
 * 
 * @author yayu
 * 
 */
public class SOAMessages extends NLS {

	private static final String BUNDLE_NAME = "org.ebayopensource.turmeric.eclipse.maven.core.resources.messages"; //$NON-NLS-1$
	
	
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, SOAMessages.class);
	}
}
