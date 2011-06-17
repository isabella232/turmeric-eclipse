/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.utils.test.xml;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactoryConfigurationError;

import org.apache.commons.io.IOUtils;
import org.custommonkey.xmlunit.XMLAssert;
import org.ebayopensource.turmeric.eclipse.utils.xml.XMLUtil;
import org.junit.Test;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;


/**
 * @author yayu
 *
 */
public class TestXMLUtil {
	private static final String XML_DATA;
	
	static {
		StringBuffer buf = new StringBuffer();
		String lineSeparator = System.getProperty("line.separator");
		buf.append("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>");
		buf.append(lineSeparator);
		buf.append("<project xmlns=\"http://maven.apache.org/POM/4.0.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"");
		buf.append(" xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd\">");
		buf.append("   <modelVersion>4.0.0</modelVersion>");
		buf.append("   <groupId>org.ebayopensource.turmeric</groupId>");
		buf.append("   <artifactId>org.ebayopensource.turmeric.soa.utils.test</artifactId>");
		buf.append("   <packaging>eclipse-test-plugin</packaging>");
		buf.append("   <version>1.0.0-SNAPSHOT</version>");
		buf.append("   <name>Turmeric: SOA Utils Plugin Tests</name>");
		buf.append(lineSeparator);
		buf.append("</project>");
		buf.append(lineSeparator);
		XML_DATA = buf.toString();
	}

	/**
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.utils.xml.XMLUtil#convertXMLToString(org.w3c.dom.Node)}.
	 * @throws ParserConfigurationException 
	 * @throws IOException 
	 * @throws SAXException 
	 * @throws TransformerException 
	 * @throws TransformerFactoryConfigurationError 
	 */
	@Test
	public void testConvertXMLToString() throws SAXException, IOException, ParserConfigurationException, TransformerFactoryConfigurationError, TransformerException {
		InputStream input = null;
		try {
			input = new ByteArrayInputStream(XML_DATA.getBytes());
			Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(input);
			String result = XMLUtil.convertXMLToString(doc);
			XMLAssert.assertXMLEqual("Comparing gold copy with generated xml",
					XML_DATA, result);
			//Assert.assertEquals(XML_DATA, result);
		} finally {
			IOUtils.closeQuietly(input);
		}
	}

	/**
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.utils.xml.XMLUtil#writeXML(org.w3c.dom.Node, java.io.Writer)}.
	 * @throws ParserConfigurationException 
	 * @throws IOException 
	 * @throws SAXException 
	 * @throws TransformerException 
	 * @throws TransformerFactoryConfigurationError 
	 */
	@Test
	public void testWriteXML() throws SAXException, IOException, ParserConfigurationException, TransformerFactoryConfigurationError, TransformerException {
		StringWriter writer = null;
		InputStream input = null;
		try {
			writer = new StringWriter();
			input = new ByteArrayInputStream(XML_DATA.getBytes());
			Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(input);
			XMLUtil.writeXML(doc, writer);
			String result = writer.toString();
			XMLAssert.assertXMLEqual("Comparing gold copy with generated xml",
					XML_DATA, result);
			//Assert.assertEquals(XML_DATA, result);
		} finally {
			IOUtils.closeQuietly(input);
			IOUtils.closeQuietly(writer);
		}
	}

}
