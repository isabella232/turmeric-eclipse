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
 * The Class Messages.
 *
 * @author yayu
 */
public final class Messages extends NLS {
	private static final String BUNDLE_NAME = Messages.class.getPackage().getName() + ".messages"; //$NON-NLS-1$
	
	/**
	 * 
	 */
	private Messages() {
		super();
	}
	
	/** The METHO d_ start. */
	public static String METHOD_START;
	
	/** The METHO d_ end. */
	public static String METHOD_END;
	
	/** The VALIDATIO n_ i n_ progress. */
	public static String VALIDATION_IN_PROGRESS;
	
	/** The ERRO r_ invali d_ artifac t_ content s_ o r_ type. */
	public static String ERROR_INVALID_ARTIFACT_CONTENTS_OR_TYPE;
	
	/** The ERRO r_ validatio n_ couldn t_ run. */
	public static String ERROR_VALIDATION_COULDNT_RUN;
	
	/** The VALIDATIO n_ resul t_ may. */
	public static String VALIDATION_RESULT_MAY;
	
	/** The VALIDATIO n_ resul t_ must. */
	public static String VALIDATION_RESULT_MUST;
	
	/** The VALIDATIO n_ resul t_ should. */
	public static String VALIDATION_RESULT_SHOULD;
	
	/** The VALIDATIO n_ resul t_ error. */
	public static String VALIDATION_RESULT_ERROR;
	
	/** The VALIDATIO n_ resul t_ warning. */
	public static String VALIDATION_RESULT_WARNING;
	
	/** The VALIDATIO n_ resul t_ metho d_ end. */
	public static String VALIDATION_RESULT_METHOD_END;
	
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}
	
	/**
	 * Format string.
	 *
	 * @param message the message
	 * @param args the args
	 * @return the string
	 */
	public static String formatString(String message, Object... args) {
		return MessageFormat.format(message, args);
	}
	

}
