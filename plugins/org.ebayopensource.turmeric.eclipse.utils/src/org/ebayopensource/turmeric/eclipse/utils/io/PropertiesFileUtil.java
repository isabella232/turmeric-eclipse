/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.utils.io;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.ebayopensource.turmeric.eclipse.utils.plugin.WorkspaceUtil;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;


/**
 * @author smathew
 * 
 */
public class PropertiesFileUtil {
	
	
	/**
	 * @param firstProperties
	 * @param secondProperties
	 * @return assumes that the key/value are all strings can be enhanced later
	 *         if required
	 */
	public static boolean isEqual(Properties firstProperties,
			Properties secondProperties) {
		if (firstProperties.size() == secondProperties.size()) {
			for (Object key1 : firstProperties.keySet()) {
				if (key1 instanceof String) {
					String strKey1 = (String) key1;
					String value1 = String.valueOf(firstProperties.getProperty(strKey1));
					String value2 = String.valueOf(secondProperties.getProperty(strKey1));
					if (StringUtils.equals(value1, value2) == false) {
						return false;
					}
				} else {
					return false;
				}
			}
			return true;
		}
		return false;
	}

	public static void writeToFile(Properties properties, IFile file,
			String comments) throws IOException, CoreException {
		OutputStream output = null;
		try {
			output = new ByteArrayOutputStream();
			properties.store(output, comments);
			WorkspaceUtil.writeToFile(output.toString(), file, null);
		} finally {
			IOUtils.closeQuietly(output);
		}
	}
	
	@SuppressWarnings("unchecked")
	private static void accessProperty(InputStream input, OutputStream output, String[] keys, PropertyOperation operation, boolean autoclose) throws IOException {
		List<String> lines = new ArrayList<String>();
		List<String> result = new ArrayList<String>();
		try {
			lines = IOUtils.readLines(input);
		} finally {
			if (autoclose) {
				IOUtils.closeQuietly(input);
			}
		}
		try {
			for (String line : lines) {
				int offset = line.indexOf("=");
				if (line.trim().length() > 0 && offset > -1) {
					String originalKey = line.substring(0, offset).trim();
					String originalValue = line.substring(++offset);
					int index = ArrayUtils.indexOf(keys, originalKey);
					if (index > -1) {
						String outputLine = operation.process(line, originalKey, originalValue);
						if (outputLine != null) {
							result.add(outputLine);
						}
					} else {
						result.add(line);
					}
				} else {
					result.add(line);
				}
			}
			if (output != null) {
				IOUtils.writeLines(result, null, output);
			}
		} finally {
			if (autoclose) {
				IOUtils.closeQuietly(output);
			}
		}
	}
	
	public static String getPropertyValueByKey(InputStream input, String key) throws IOException {
		final StringBuffer buffer = new StringBuffer();
		accessProperty(input, null, new String[]{key}, new PropertyOperation() {
			
			@Override
			public String process(String input, String key, String value) {
				buffer.append(value);
				return value;
			}
		}, false);
		return buffer.length() > 0 ? buffer.toString() : null;
	}
	
	public static void removePropertyByKey(InputStream input, OutputStream output, String[] keys) throws IOException {
		accessProperty(input, output, keys, new PropertyOperation() {
			
			@Override
			public String process(String input, String key, String value) {
				return null;//null means remove this line
			}
		}, true);
	}
	
	public static void updatePropertyByKey(InputStream input, OutputStream output, String targetKey, final String targetValue) throws IOException {
		accessProperty(input, output, new String[]{targetKey}, new PropertyOperation() {
			
			@Override
			public String process(String input, String key, String value) {
				return key + "=" + targetValue;
			}
		}, true);
	}
	
	@SuppressWarnings("unchecked")
	public static void addProperty(InputStream input, OutputStream output, Map<String, String> properties) throws IOException {
		List<String> sources = new ArrayList<String>();
		try {
			sources = IOUtils.readLines(input);
		} finally {
			IOUtils.closeQuietly(input);
		}
		try {
			for (String key : properties.keySet()) {
				String value = properties.get(key);
				sources.add(key + "=" + value);
			}
			IOUtils.writeLines(sources, null, output);
		} finally {
			IOUtils.closeQuietly(output);
		}
	}
}
