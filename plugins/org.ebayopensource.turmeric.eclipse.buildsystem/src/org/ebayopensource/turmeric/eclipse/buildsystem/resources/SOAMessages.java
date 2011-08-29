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
	
	
	/** The NO t_ adaptable. */
	public static String NOT_ADAPTABLE;

	/** The INVALIDPROJECT. */
	public static String INVALIDPROJECT;

	/** The INVALI d_ imp l_ project. */
	public static String INVALID_IMPL_PROJECT;

	/** The CLEA n_ folder. */
	public static String CLEAN_FOLDER;

	/** The ER r_ mutlipl e_ src. */
	public static String ERR_MUTLIPLE_SRC;

	/** The ER r_ pro j_ root. */
	public static String ERR_PROJ_ROOT;

	/** The ER r_ lo c_ roo t_ incorrect. */
	public static String ERR_LOC_ROOT_INCORRECT;
	
	/** The WARNIN g_ missin g_ artifac t_ validator. */
	public static String WARNING_MISSING_ARTIFACT_VALIDATOR;

	/** The TARGE t_ platform. */
	public static String TARGET_PLATFORM;

	/** The IMP l_ invalid. */
	public static String IMPL_INVALID;

	/** The ERRO r_ servic e_ wsd l_ validatio n_ failed. */
	public static String ERROR_SERVICE_WSDL_VALIDATION_FAILED;
	
	/** The ERRO r_ servic e_ r s_ servic e_ failed. */
	public static String ERROR_SERVICE_RS_SERVICE_FAILED;
	
	/** The ERRO r_ servic e_ r s_ servic e_ faile d_ title. */
	public static String  ERROR_SERVICE_RS_SERVICE_FAILED_TITLE;
	
	/** The MS g_ tim e_ take n_ fo r_ buil d_ project. */
	public static String MSG_TIME_TAKEN_FOR_BUILD_PROJECT;
	
	/** The MS g_ tim e_ take n_ fo r_ clea n_ project. */
	public static String MSG_TIME_TAKEN_FOR_CLEAN_PROJECT;
	
	/** The SERVIC e_ codege n_ skippe d_ message. */
	public static String SERVICE_CODEGEN_SKIPPED_MESSAGE;
	
	/** The JAV a_ projec t_ readonly. */
	public static String JAVA_PROJECT_READONLY;
	
	/** The JAV a_ classpat h_ readonly. */
	public static String JAVA_CLASSPATH_READONLY;
	
	
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, SOAMessages.class);
	}

}
