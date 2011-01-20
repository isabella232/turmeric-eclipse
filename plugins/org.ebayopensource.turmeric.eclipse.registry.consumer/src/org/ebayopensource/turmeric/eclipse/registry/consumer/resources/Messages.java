/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
/**
 * 
 */
package org.ebayopensource.turmeric.eclipse.registry.consumer.resources;

import java.text.MessageFormat;

import org.eclipse.osgi.util.NLS;

/**
 * @author yayu
 *
 */
public final class Messages extends NLS {
	private static final String BUNDLE_NAME = Messages.class.getPackage().getName() + ".messages"; //$NON-NLS-1$
	
	/**
	 * 
	 */
	private Messages() {
		super();
	}
	
	public static String METHOD_START;
	public static String METHOD_END;
	
	public static String VALIDATION_IN_PROGRESS;
	public static String ERROR_INVALID_ARTIFACT_CONTENTS_OR_TYPE;
	public static String ERROR_VALIDATION_COULDNT_RUN;
	
	public static String VALIDATION_RESULT_MAY;
	public static String VALIDATION_RESULT_MUST;
	public static String VALIDATION_RESULT_SHOULD;
	
	public static String VALIDATION_RESULT_ERROR;
	public static String VALIDATION_RESULT_WARNING;
	
	public static String VALIDATION_RESULT_METHOD_END;
	
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}
	
	public static String formatString(String message, Object... args) {
		return MessageFormat.format(message, args);
	}
	

}
