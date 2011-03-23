/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.test.utils;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;

import javax.wsdl.WSDLException;

import junit.framework.Assert;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.ebayopensource.turmeric.eclipse.functional.test.AbstractTestCase;
import org.ebayopensource.turmeric.eclipse.functional.test.SoaTestConstants;
import org.ebayopensource.turmeric.eclipse.utils.wsdl.WSDLUtil;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.osgi.framework.Bundle;


public class WsdlUtilTest extends AbstractTestCase {

	private static File outputFile = null;

	private static File inputFile = null;
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public  void setUpBeforeClass() throws Exception {
	
		
		String testDataLocation = getPluginOSPath(SoaTestConstants.PLUGIN_ID,
				SoaTestConstants.TEST_DATA)
				+ File.pathSeparator
				+ AbstractTestCase.class.getName();
		outputFile = new File(testDataLocation, "output");
		FileUtils.forceMkdir(outputFile);
		inputFile = new File(testDataLocation, "input");
		Assert.assertNotNull(inputFile);
		//TODO temporarilly disable the following asssertion
		//Assert.assertTrue(inputFile.exists());
	}
	
	/**
	 * @throws java.lang.Exception
	 */
	@After
	public  void tearDownAfterClass() throws Exception {
	
		// clean the output folder
		FileUtils.cleanDirectory(outputFile);
	}

	public static String getPluginOSPath(String pluginId, String subDirPath) {
		URL platformContextURL = getPluginOSURL(pluginId, subDirPath);
		String fullPath = (new File(platformContextURL.getPath()))
				.getAbsolutePath();
		IPath osPath = new Path(fullPath);
		return osPath.toString();
	}

	public static URL getPluginOSURL(String pluginId, String subDirPath) {
		Bundle bundle = Platform.getBundle(pluginId);
		if (bundle == null) {
			return null;
		}

		URL installLocation = bundle.getEntry("/");
		URL local = null;
		URL platformContextURL = null;
			try {
				local = FileLocator.toFileURL(installLocation);
				platformContextURL = subDirPath == null ? local : new URL(local,
						subDirPath.toString());
			} catch (MalformedURLException ex) {
				return null;
			} catch (IOException e) {
				return null;
			}

		return platformContextURL;
	}

	@Ignore("Currently Failing")
	@Test
	public void testWriteWSDLStringString() {

		// testPerFile("C:\\testWsdlUtil\\input\\Calc.wsdl");/*with no imports
		// at all*/
		// testPerFile("C:\\testWsdlUtil\\input\\PayPalSvc.wsdl");/*has schema
		// imports recursively*/
		// testPerFile("C:\\testWsdlUtil\\input\\calcSchemaInclude\\CalculatorService.wsdl");/*test
		// schema include*/
		// testPerFile("C:\\testWsdlUtil\\input\\calcSchemaInclude2\\CalculatorService.wsdl");/*test
		// schema include*/
		testPerFile("https://www.paypal.com/wsdl/PayPalSvc.wsdl");/*
																 * test fetch
																 * through http
																 * connection
																 */

		// test every wsdl file resides input directory
		Collection wsdlfiles = FileUtils.listFiles(inputFile,
				new String[] { "wsdl" }, true);
		for (Object object : wsdlfiles) {
			File file = (File) object;
			testPerFile(file.getAbsolutePath());
		}

	}

	private void testPerFile(String filePath) {
		String destFolderPath = outputFile.getAbsolutePath() + File.separator
				+ FilenameUtils.getBaseName(filePath);

		if (new File(destFolderPath).exists()) {
			destFolderPath = destFolderPath + System.currentTimeMillis();
		}

		String destFilePath = destFolderPath + File.separator
				+ FilenameUtils.getName(filePath);

		try {

			WSDLUtil.writeWSDL(filePath, destFilePath);

		} catch (Exception e1) {
			Assert.fail(e1.getMessage());
		}

		try {
			WSDLUtil.readWSDL(destFilePath);
		} catch (WSDLException e) {
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}

	}

}
