/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.utils.test.io;

import static org.junit.Assert.fail;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.io.IOUtils;
import org.ebayopensource.turmeric.eclipse.utils.io.PropertiesFileUtil;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;


/**
 * @author yayu
 *
 */
public class TestPropertiesFileUtil {

	/**
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.utils.io.PropertiesFileUtil#isEqual(java.util.Properties, java.util.Properties)}.
	 */
	@Test
	public void testIsEqual() {
		Map<String, String> data = new ConcurrentHashMap<String, String>();
		data.put("nikon", "D3x");
		data.put("canon", "1D Mark III");
		data.put("pentax", "K7");
		
		Properties props1 = new Properties();
		props1.putAll(data);
		
		Properties props2 = new Properties();
		props2.putAll(new ConcurrentHashMap<String, String>(data));
		Assert.assertTrue(PropertiesFileUtil.isEqual(props1, props2));
		
		props2.put("sony", "a900");
		Assert.assertFalse(PropertiesFileUtil.isEqual(props1, props2));
		
		props2.remove("sony");
		props2.put("canon", "sucks");
		System.out.println(props1);
		System.out.println(props2);
		Assert.assertFalse(PropertiesFileUtil.isEqual(props1, props2));
		
	}

	/**
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.utils.io.PropertiesFileUtil#getPropertyValueByKey(java.io.InputStream, java.lang.String)}.
	 * @throws IOException 
	 */
	@Test
	public void testGetPropertyValueByKey() throws IOException {
		File tempFile = null;
		InputStream input = null;
		OutputStream output = null;
		try {
			tempFile = File.createTempFile("nikon", "d80");
			output = new FileOutputStream(tempFile);
			Properties props = new Properties();
			props.put("Nikon", "d80,d90,d700,d3s,d3x");
			props.store(output , "Comments");
			IOUtils.closeQuietly(output);
			
			input = new FileInputStream(tempFile);
			String value = PropertiesFileUtil.getPropertyValueByKey(input, "Nikon");
			Assert.assertNotNull(value);
			Assert.assertEquals("d80,d90,d700,d3s,d3x", value);
		} finally {
			IOUtils.closeQuietly(output);
			IOUtils.closeQuietly(input);
			tempFile.delete();
		}
		
	}

	/**
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.utils.io.PropertiesFileUtil#removePropertyByKey(java.io.InputStream, java.io.OutputStream, java.lang.String[])}.
	 * @throws IOException 
	 */
	@Test
	public void testRemovePropertyByKey() throws IOException {
		File tempFile = null;
		InputStream input = null;
		OutputStream output = null;
		try {
			tempFile = File.createTempFile("nikon", "d80");
			output = new FileOutputStream(tempFile);
			Properties props = new Properties();
			props.put("Nikon", "d80,d90,d700,d3s,d3x");
			props.put("Canon", "5D Mark II");
			props.store(output , "Comments");
			IOUtils.closeQuietly(output);
			
			input = new FileInputStream(tempFile);
			String value = PropertiesFileUtil.getPropertyValueByKey(input, "Nikon");
			Assert.assertNotNull(value);
			Assert.assertEquals("d80,d90,d700,d3s,d3x", value);
			
			output = new ByteArrayOutputStream();
			PropertiesFileUtil.removePropertyByKey(input, output, new String[]{"Nikon"});
			
			//TODO more tests
			System.out.println("byte output: " + output.toString());
		} finally {
			IOUtils.closeQuietly(output);
			IOUtils.closeQuietly(input);
			tempFile.delete();
		}
	}

}
