/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.codegen.utils;

import java.io.File;

import org.apache.commons.lang.StringUtils;

/**
 * Standard Utility class for Codegen. Contains common string based processing.
 * This is mainly used in the model transformer.
 * 
 * @author smathew
 * 
 */
public class CodeGenUtil {

	/**
	 * Extracts the GIP value from the service interface name. Acts as a domain
	 * wrapper over string utility class
	 * 
	 * @param serviceInterface
	 * @return
	 */
	public static String getGIPFromInterface(String serviceInterface) {
		return StringUtils.substringBeforeLast(serviceInterface, ".");
	}

	/**
	 * Extracts the GIN value from the service interface name. Acts as a domain
	 * wrapper over string utility class
	 * 
	 * @param serviceInterface
	 * @return
	 */
	public static String getGINFromInterface(String serviceInterface) {
		return StringUtils.substringAfterLast(serviceInterface, ".");
	}
	
	public static String getJavaHome() {
		return System.getProperty("java.home");
	}
	
	public static String getJdkHome() {
		final String javaHome = getJavaHome();
		
		if (StringUtils.isNotBlank(javaHome)) {
			final File dir = new File(javaHome);
			if (dir.exists() && dir.getName().equals("jre")) {
				return dir.getParent();
			} else {
				return javaHome;
			}
		}
		return null;
	}
}
