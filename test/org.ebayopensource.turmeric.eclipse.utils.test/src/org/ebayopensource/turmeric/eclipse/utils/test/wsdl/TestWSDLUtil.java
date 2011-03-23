/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.utils.test.wsdl;

import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;

import javax.wsdl.Definition;
import javax.wsdl.WSDLException;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.ebayopensource.turmeric.eclipse.utils.test.Activator;
import org.ebayopensource.turmeric.eclipse.utils.wsdl.WSDLUtil;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;


/**
 * @author yayu
 *
 */
public class TestWSDLUtil {

	/**
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.utils.wsdl.WSDLUtil#toURL(java.io.File)}.
	 * @throws MalformedURLException 
	 * @throws UnsupportedEncodingException 
	 */
	@Test
	public void testToURL() throws MalformedURLException, UnsupportedEncodingException {
		String path = System.getProperty("user.home");
		File file = new File(path);
		String expected = file.toURI().toURL().toString();
		if (path.contains("%20")) {
			path = URLDecoder.decode(path, System.getProperty("file.encoding"));
			expected = path.replaceAll("%20", " ");
		}
		Assert.assertEquals(new URL(expected), WSDLUtil.toURL(file));
	}

	/**
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.utils.wsdl.WSDLUtil#isValidURL(java.lang.String)}.
	 * @throws MalformedURLException 
	 * @throws UnsupportedEncodingException 
	 */
	@Test
	public void testIsValidURL() throws MalformedURLException, UnsupportedEncodingException {
		String path = System.getProperty("user.home");
		
		String expected = new File(path).toURI().toURL().toString();
	
		Assert.assertTrue("not a valid url", WSDLUtil.isValidURL(expected));
		Assert.assertFalse("should not be a valid url", WSDLUtil.isValidURL(path));
		Assert.assertTrue("not a valid url", WSDLUtil.isValidURL("http://www.ebay.com"));
	}

	/**
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.utils.wsdl.WSDLUtil#validateURL(java.lang.String)}.
	 * @throws MalformedURLException 
	 */
	@Test
	public void testValidateURL() throws MalformedURLException {
		String path = System.getProperty("user.home");
		
		String expected = new File(path).toURI().toURL().toString();
	
		Assert.assertTrue("not a valid url", StringUtils.isBlank(WSDLUtil.validateURL(expected)));
		Assert.assertFalse("should not be a valid url", StringUtils.isBlank(WSDLUtil.validateURL(path)));
		Assert.assertTrue("not a valid url", StringUtils.isBlank(WSDLUtil.validateURL("http://www.ebay.com")));
	}

	/**
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.utils.wsdl.WSDLUtil#readWSDL(java.lang.String)}.
	 * @throws WSDLException 
	 */
	@Test
	public void testReadWSDLString() throws WSDLException {
		URL url = TestWSDLUtil.class.getResource("Calc.wsdl");
		Assert.assertNotNull("could not find the wsdl file", url);
		Assert.assertNotNull(WSDLUtil.readWSDL(url.toExternalForm()));
	}

	/**
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.utils.wsdl.WSDLUtil#readWSDL(java.io.InputStream)}.
	 * @throws WSDLException 
	 * @throws IOException 
	 */
	@Test
	public void testReadWSDLInputStream() throws WSDLException, IOException {
		URL url = TestWSDLUtil.class.getResource("Calc.wsdl");
		Assert.assertNotNull("could not find the wsdl file", url);
		InputStream input = null;
		try {
			input = url.openStream();
			Assert.assertNotNull(WSDLUtil.readWSDL(input));
		} finally {
			IOUtils.closeQuietly(input);
		}
	}

	/**
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.utils.wsdl.WSDLUtil#readWSDLFromJarFile(java.io.File, java.lang.String)}.
	 */
	@Ignore
	@Test
	public void testReadWSDLFromJarFile() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.utils.wsdl.WSDLUtil#readWSDL(java.lang.String, java.io.InputStream)}.
	 * @throws WSDLException 
	 */
	@Ignore
	@Test
	public void testReadWSDLStringInputStream() throws WSDLException {
		URL url =  TestWSDLUtil.class.getResource("Calc.wsdl");
		Assert.assertNotNull(url);
		InputStream input = null;
		try {
			Definition wsdl = WSDLUtil.readWSDL(new File(url.getFile()).getPath(), input);
			Assert.assertNotNull(wsdl);
		} finally {
			IOUtils.closeQuietly(input);
		}
	}

	/**
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.utils.wsdl.WSDLUtil#writeWSDL(java.lang.String, java.lang.String)}.
	 */
	@Ignore
	@Test
	public void testWriteWSDLStringString() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.utils.wsdl.WSDLUtil#writeWSDL(javax.wsdl.Definition, java.lang.String)}.
	 */
	@Ignore
	@Test
	public void testWriteWSDLDefinitionString() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.utils.wsdl.WSDLUtil#getServiceNameFromWSDL(javax.wsdl.Definition)}.
	 * @throws IOException 
	 * @throws WSDLException 
	 */
	@Test
	public void testGetServiceNameFromWSDL() throws IOException, WSDLException {
		URL url = TestWSDLUtil.class.getResource("Calc.wsdl");
		Assert.assertNotNull("could not find the wsdl file", url);
		InputStream input = null;
		try {
			input = url.openStream();
			Definition wsdl = WSDLUtil.readWSDL(input);
			Assert.assertNotNull(wsdl);
			Assert.assertEquals("CalculatorService", WSDLUtil.getServiceNameFromWSDL(wsdl));
		} finally {
			IOUtils.closeQuietly(input);
		}
	}

	/**
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.utils.wsdl.WSDLUtil#getServiceLocationFromWSDL(javax.wsdl.Definition)}.
	 * @throws IOException 
	 * @throws WSDLException 
	 */
	@Test
	public void testGetServiceLocationFromWSDL() throws IOException, WSDLException {
		URL url = TestWSDLUtil.class.getResource("Calc.wsdl");
		Assert.assertNotNull("could not find the wsdl file", url);
		InputStream input = null;
		try {
			input = url.openStream();
			Definition wsdl = WSDLUtil.readWSDL(input);
			Assert.assertNotNull(wsdl);
			Assert.assertEquals("http://localhost:8080/calculator", WSDLUtil.getServiceLocationFromWSDL(wsdl));
		} finally {
			IOUtils.closeQuietly(input);
		}
	}

	/**
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.utils.wsdl.WSDLUtil#getTargetNamespace(java.lang.String)}.
	 */
	@Ignore
	@Test
	public void testGetTargetNamespaceString() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.utils.wsdl.WSDLUtil#getTargetNamespace(javax.wsdl.Definition)}.
	 * @throws IOException 
	 * @throws WSDLException 
	 */
	@Test
	public void testGetTargetNamespaceDefinition() throws IOException, WSDLException {
		URL url = TestWSDLUtil.class.getResource("Calc.wsdl");
		Assert.assertNotNull("could not find the wsdl file", url);
		InputStream input = null;
		try {
			input = url.openStream();
			Definition wsdl = WSDLUtil.readWSDL(input);
			Assert.assertNotNull(wsdl);
			Assert.assertEquals("http://localhost:8080/calculator", WSDLUtil.getServiceLocationFromWSDL(wsdl));
		} finally {
			IOUtils.closeQuietly(input);
		}
	}

	/**
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.utils.wsdl.WSDLUtil#getTargetNamespace(java.lang.String, java.io.InputStream)}.
	 * @throws WSDLException 
	 */
	@Ignore
	@Test
	public void testGetTargetNamespaceStringInputStream() throws WSDLException {
		URL url = WSDLUtil.getPluginOSURL(Activator.PLUGIN_ID, "wsdl/Calc.wsdl");
		Assert.assertNotNull(url);
		InputStream input = null;
		try {
			String namespace = WSDLUtil.getTargetNamespace(new File(url.getFile()).getParentFile().getPath(), input);
			Assert.assertNotNull(namespace);
		} finally {
			IOUtils.closeQuietly(input);
		}
		
	}

	/**
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.utils.wsdl.WSDLUtil#getPluginOSPath(java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testGetPluginOSPath() {
		String path = WSDLUtil.getPluginOSPath(Activator.PLUGIN_ID, "wsdl/Calc.wsdl");
		Assert.assertNotNull(path);
	}

	/**
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.utils.wsdl.WSDLUtil#getPluginOSURL(java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testGetPluginOSURL() {
		URL url = WSDLUtil.getPluginOSURL(Activator.PLUGIN_ID, "wsdl/Calc.wsdl");
		Assert.assertNotNull(url);
	}

	/**
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.utils.wsdl.WSDLUtil#getAllTargetNamespaces(javax.wsdl.Definition)}.
	 * @throws IOException 
	 * @throws WSDLException 
	 */
	@Test
	public void testGetAllTargetNamespaces() throws IOException, WSDLException {
		URL url = TestWSDLUtil.class.getResource("Calc.wsdl");
		Assert.assertNotNull("could not find the wsdl file", url);
		InputStream input = null;
		try {
			input = url.openStream();
			Definition wsdl = WSDLUtil.readWSDL(input);
			Assert.assertNotNull(wsdl);
			Assert.assertFalse(WSDLUtil.getAllTargetNamespaces(wsdl).isEmpty());
		} finally {
			IOUtils.closeQuietly(input);
		}
	}

}
