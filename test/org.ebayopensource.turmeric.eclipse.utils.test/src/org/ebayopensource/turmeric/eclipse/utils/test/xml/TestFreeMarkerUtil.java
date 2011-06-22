/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.utils.test.xml;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.io.IOUtils;
import org.ebayopensource.turmeric.eclipse.utils.xml.FreeMarkerUtil;
import org.junit.Assert;
import org.junit.Test;


import freemarker.template.TemplateException;

/**
 * @author yayu
 *
 */
public class TestFreeMarkerUtil {

	/**
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.utils.xml.FreeMarkerUtil#generate(java.util.Map, java.lang.Object, java.lang.String, java.io.OutputStream)}.
	 * @throws TemplateException 
	 * @throws IOException 
	 */
	@Test
	public void testGenerate() throws IOException, TemplateException {
		OutputStream output = null;
		InputStream input = null;
		try {
			output = new ByteArrayOutputStream();
			Map<String, String> data = new ConcurrentHashMap<String, String>();
			data.put("serviceName", "NikonLensService");
			String templateFileName = "geronimo-web.xml.ftl";
			URL templateParentFolder = TestFreeMarkerUtil.class.getResource(templateFileName);
			Assert.assertNotNull(templateParentFolder);
			
			FreeMarkerUtil.generate(data, templateParentFolder, templateFileName, output);
			System.out.println(output.toString());
			
			input = TestFreeMarkerUtil.class.getResourceAsStream("geronimo-web.xml");
			Assert.assertNotNull(templateParentFolder);
			String expected = IOUtils.toString(input);
			Assert.assertEquals(expected, output.toString());
		} finally {
			IOUtils.closeQuietly(output);
			IOUtils.closeQuietly(input);
		}
		
	}

}
