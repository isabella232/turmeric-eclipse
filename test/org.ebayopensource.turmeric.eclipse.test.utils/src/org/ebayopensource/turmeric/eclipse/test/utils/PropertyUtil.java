/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.test.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * The Class PropertyUtil.
 */
public class PropertyUtil {

	/**
	 * Load properties file.
	 *
	 * @param fileName the file name
	 * @return the properties
	 */
	public static Properties loadPropertiesFile(String fileName) {
		Properties props = new Properties();
		FileInputStream is = null;
		try {
			is = new FileInputStream(fileName);
			props.load(is);
		} catch (IOException e) {
			return null;
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					// We don't care about the exception
				}
			}
		}
		return props;
	}
}
