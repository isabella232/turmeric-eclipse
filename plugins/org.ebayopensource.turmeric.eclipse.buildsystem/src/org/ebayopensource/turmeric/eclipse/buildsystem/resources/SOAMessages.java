/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.buildsystem.resources;

import org.eclipse.osgi.util.NLS;

/**
 * Standard messages class. Holds the UI and non UI (log) messages
 * 
 * @author smathew
 * @author yayu
 */
public class SOAMessages extends NLS{
	
	private static final String BUNDLE_NAME = "org.ebayopensource.turmeric.eclipse.buildsystem.resources.messages"; //$NON-NLS-1$
	
	
	public static String NOT_ADAPTABLE;

	public static String INVALIDPROJECT;

	public static String INVALID_IMPL_PROJECT;

	public static String CLEAN_FOLDER;

	public static String ERR_MUTLIPLE_SRC;

	public static String ERR_PROJ_ROOT;

	public static String ERR_LOC_ROOT_INCORRECT;

	public static String TARGET_PLATFORM;

	public static String IMPL_INVALID;
	
	public static String WARNING_MISSING_ARTIFACT_VALIDATOR;
	
	public static String ERROR_SERVICE_WSDL_VALIDATION_FAILED;
	
	public static String ERROR_SERVICE_RS_SERVICE_FAILED;
	
	public static String  ERROR_SERVICE_RS_SERVICE_FAILED_TITLE;
	
	public static String  ERROR_AR_OUT_OF_DATE;
	
	public static String  WARNING_AR_NOT_AVAILABLE;
	
	public static String MSG_TIME_TAKEN_FOR_BUILD_PROJECT;
	public static String MSG_TIME_TAKEN_FOR_CLEAN_PROJECT;
	
	public static String ERROR_FAIL_TO_SUBMIT_SERVICE_VERSION_TO_AR;
	
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, SOAMessages.class);
	}

}
