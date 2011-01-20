/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.utils.test.io;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;

import org.ebayopensource.turmeric.eclipse.utils.io.IOUtil;
import org.junit.Ignore;
import org.junit.Test;


/**
 * @author yayu
 *
 */
public class TestIOUtil {

	/**
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.utils.io.IOUtil#newFile(java.io.File, java.lang.String[])}.
	 * @throws IOException 
	 */
	@Test
	public void testNewFile() throws IOException {
		String tmpdir = System.getProperty("java.io.tmpdir");
		assertNotNull("java.io.tmpdir is not set", tmpdir);
		File tmpDir = new File(tmpdir);
		String[] segments = {"hello", "world"};
		File result = IOUtil.newFile(tmpDir, segments);
		assertNotNull(result);
		assertEquals(new File(tmpDir, "hello/world"), result);
	}

	/**
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.utils.io.IOUtil#readableDir(java.io.File)}.
	 */
	@Test
	public void testReadableDir() {
		String tmpdir = System.getProperty("java.io.tmpdir");
		assertNotNull("java.io.tmpdir is not set", tmpdir);
		File tmpDir = new File(tmpdir);
		assertTrue("Folder should be readable", IOUtil.readableDir(tmpDir));
		assertFalse("Folder should not be readable", 
				IOUtil.readableDir(new File("Android/Tools")));
	}

	/**
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.utils.io.IOUtil#validateURL(java.lang.String)}.
	 * @throws MalformedURLException 
	 */
	@Test
	public void testValidateURL() throws MalformedURLException {
		String tmpdir = System.getProperty("user.home");
		assertTrue("should be valid URL", IOUtil.validateURL(new File(tmpdir).toURI().toURL().toString()));
		assertTrue("should be a valid URL", IOUtil.validateURL("http://www.ebay.com"));
		assertFalse("should not be a valid URL", IOUtil.validateURL("android://www.ebay.com"));
		assertFalse("should not be a valid URL", IOUtil.validateURL(new File(tmpdir).toString()));
	}

	/**
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.utils.io.IOUtil#getTempDirectory()}.
	 */
	@Test
	public void testGetTempDirectory() {
		assertEquals(new File(System.getProperty("java.io.tmpdir")), IOUtil.getTempDirectory());
	}

	/**
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.utils.io.IOUtil#toURL(java.lang.String)}.
	 * @throws MalformedURLException 
	 * @throws UnsupportedEncodingException 
	 */
	@Test
	public void testToURL() throws MalformedURLException, UnsupportedEncodingException {
		String path = "http://www.ebay.com";
		assertEquals(new URL(path), IOUtil.toURL(path));
		
		path = System.getProperty("user.home");
		String expected = new File(path).toURI().toURL().toString();
		
		if (path.contains("%20") || expected.contains("%20")) {
			path = URLDecoder.decode(path, System.getProperty("file.encoding"));
			expected = expected.replaceAll("%20", " ");
		}
		assertEquals(new URL(expected), IOUtil.toURL(path));
	}

	/**
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.utils.io.IOUtil#listFile(java.io.File, boolean)}.
	 */
	@Test
	public void testListFile() {
		File userHomeDir = new File(System.getProperty("user.home"));
		for (File dir : IOUtil.listFile(userHomeDir, true)) {
			assertTrue("it must be a directory", dir.isDirectory());
		}
		for (File dir : IOUtil.listFile(userHomeDir, false)) {
			assertTrue("it must be a file", dir.isFile());
		}
	}

}
