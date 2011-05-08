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
package org.ebayopensource.turmeric.eclipse.mavenapi.internal.resources;

import java.text.MessageFormat;
import java.util.Collection;

import org.eclipse.osgi.util.NLS;

/**
 * The Class Messages.
 *
 * @author yayu
 */
public final class Messages extends NLS {
	private static final String BUNDLE_NAME = "org.ebayopensource.turmeric.eclipse.mavenapi.internal.resources.messages"; //$NON-NLS-1$

	/** The CONFIGUR e_ comman d_ title. */
	public static String CONFIGURE_COMMAND_TITLE;

	/** The ERRO r_ no t_ sv n_ plugin. */
	public static String ERROR_NOT_SVN_PLUGIN;
	
	/** The ERRO r_ n o_ mave n_ embedde r_ manager. */
	public static String ERROR_NO_MAVEN_EMBEDDER_MANAGER;
	
	/** The ERRO r_ n o_ mave n_ embedder. */
	public static String ERROR_NO_MAVEN_EMBEDDER;

	/** The ERRO r_ n o_ mave n_ impl. */
	public static String ERROR_NO_MAVEN_IMPL;

	/** The ERRO r_ nul l_ artifact. */
	public static String ERROR_NULL_ARTIFACT;
	
	/** The ERRO r_ nul l_ settings. */
	public static String ERROR_NULL_SETTINGS;
	
	/** The ERRO r_ n o_ activ e_ profiles. */
	public static String ERROR_NO_ACTIVE_PROFILES;
	
	/** The ERRO r_ n o_ repositories. */
	public static String ERROR_NO_REPOSITORIES;

	/** The MAVE n_ ap i_ erro r_ n o_ embedder. */
	public static String MAVEN_API_ERROR_NO_EMBEDDER;

	/**
	 * 
	 */
	private Messages() {
		super();
	}

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

	/**
	 * Join.
	 *
	 * @param msgList the msg list
	 * @return the string
	 */
	public static String join(Collection<String> msgList) {
		if (msgList == null)
			return null;
		if (msgList.size() < 1)
			return "";
		StringBuilder sb = new StringBuilder();
		for (String s : msgList)
			sb.append(s);

		return sb.toString();
	}
}
