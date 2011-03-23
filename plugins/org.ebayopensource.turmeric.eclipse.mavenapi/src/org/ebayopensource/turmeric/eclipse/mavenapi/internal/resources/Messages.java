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
 * @author yayu
 * 
 */
public final class Messages extends NLS {
	private static final String BUNDLE_NAME = "org.ebayopensource.turmeric.eclipse.mavenapi.internal.resources.messages"; //$NON-NLS-1$

	public static String CONFIGURE_COMMAND_TITLE;

	public static String ERROR_NOT_SVN_PLUGIN;
	public static String ERROR_NO_MAVEN_EMBEDDER_MANAGER;
	public static String ERROR_NO_MAVEN_EMBEDDER;

	public static String ERROR_NO_MAVEN_IMPL;

	public static String ERROR_NULL_ARTIFACT;
	public static String ERROR_NULL_SETTINGS;
	public static String ERROR_NO_ACTIVE_PROFILES;
	public static String ERROR_NO_REPOSITORIES;

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

	public static String formatString(String message, Object... args) {
		return MessageFormat.format(message, args);
	}

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
