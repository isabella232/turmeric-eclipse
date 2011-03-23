/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/

package org.ebayopensource.turmeric.eclipse.maven.core.utils.tests;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.ebayopensource.turmeric.eclipse.maven.core.utils.WebXMLParser;
import org.ebayopensource.turmeric.eclipse.utils.xml.JDOMUtil;
import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.Namespace;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * 
 * @author mzang@ebay.com
 *
 */
public class TestWebXMLParser {

	public static final String SOURCE_WEBXML_FILE = "web_source.xml";

	public static final String TARGET_WEBXML_FILE = "web_target.xml";

	public static final String[] EXPECTED_SERVLET_NAMES = new String[] {
			"Servlet1", "Servlet2" };

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Test method for
	 * {@link org.ebayopensource.turmeric.eclipse.maven.core.utils.WebXMLParser#getServletNames(java.io.InputStream)}
	 * .
	 */
	@Test
	public void testGetServletNames() throws IOException {

		InputStream webxmlStream = TestWebXMLParser.class
				.getResourceAsStream("web_target.xml");

		List<String> servletNames = WebXMLParser.getServletNames(webxmlStream);

		Assert.assertArrayEquals("Servlet Names not match with expected!",
				EXPECTED_SERVLET_NAMES, servletNames.toArray(new String[0]));
	}

	/**
	 * Test method for and
	 * {@link org.ebayopensource.turmeric.eclipse.maven.core.utils.WebXMLParser#addServletElements(org.jdom.Document, java.io.InputStream)}
	 * 
	 * @throws Exception
	 */
	@Test
	public void testGetSourceDocument() throws Exception {

		InputStream webxmlStreamSource = TestWebXMLParser.class
				.getResourceAsStream("web_source.xml");

		InputStream webxmlStreamTarget = TestWebXMLParser.class
				.getResourceAsStream("web_target.xml");

		Document sourceDoc = JDOMUtil.readXML(webxmlStreamSource);

		Document targetDoc = WebXMLParser.addServletElements(sourceDoc,
				webxmlStreamTarget);

		Element targetRoot = targetDoc.getRootElement();

		// Load web_target_compare.xml for compare
		InputStream webxmlStreamTargetCompare = TestWebXMLParser.class
				.getResourceAsStream("web_target_compare.xml");

		Document targetDocCompare = JDOMUtil.readXML(webxmlStreamTargetCompare);

		Element compareRoot = targetDocCompare.getRootElement();

		Assert.assertTrue(compareJDOMElement(targetRoot, compareRoot));
	}

	/**
	 * need a two direction compare to make sure they are all the same.
	 * 
	 * @param source
	 * @param expected
	 * @return
	 */
	private boolean compareJDOMElement(Element source, Element expected) {
		// source and expected are null
		if (source == null && expected == null) {
			return true;
		}

		if (source == null && expected != null) {
			return false;
		}

		if (source != null && expected == null) {
			return false;
		}

		if (expected.getNamespace().equals(source.getNamespace()) == false) {
			return false;
		}

		List<Object> expectedAttrs = expected.getAttributes();

		for (Object expectedArrObj : expectedAttrs) {
			Attribute expectedAttr = (Attribute) expectedArrObj;
			String expectedValue = expectedAttr.getValue();
			String arrtname = expectedAttr.getName();
			String sourceValue = source.getAttributeValue(arrtname);
			
			if(expectedValue.equals(sourceValue) == false){
				return false;
			}
		}

		Namespace sourceNamespce = source.getNamespace();
		// find out if all element in expected are also exists in source
		List<Object> childrenExpected = expected.getChildren();
		if (childrenExpected == null) {
			// expected child is null, return true;
			return true;
		}
		for (Object objEle : childrenExpected) {
			if (objEle instanceof Element == false) {
				continue;
			}
			Element childExpected = (Element) objEle;
			String expectedName = childExpected.getName();
			Element childSource = source.getChild(expectedName, sourceNamespce);

			if (childSource == null) {
				return false;
			}
			
			compareJDOMElement(childSource, childExpected);
		}
		return true;
	}

}
