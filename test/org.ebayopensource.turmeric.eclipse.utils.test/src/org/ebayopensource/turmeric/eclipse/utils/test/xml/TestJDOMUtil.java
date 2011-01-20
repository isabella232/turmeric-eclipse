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
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.io.IOUtils;
import org.custommonkey.xmlunit.XMLAssert;
import org.ebayopensource.turmeric.eclipse.utils.collections.ListUtil;
import org.ebayopensource.turmeric.eclipse.utils.plugin.WorkspaceUtil;
import org.ebayopensource.turmeric.eclipse.utils.xml.JDOMUtil;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.JavaCore;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.xml.sax.SAXException;


/**
 * @author yayu
 *
 */
public class TestJDOMUtil {
	private static final String XML_DATA;
	private static final String XML_DATA_WITH_HEADER;
	
	static {
		StringBuffer buf = new StringBuffer();
		String lineSeparator = System.getProperty("line.separator");
		
		buf.append("<project xmlns=\"http://maven.apache.org/POM/4.0.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"");
		buf.append(" xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd\">");
		buf.append(lineSeparator);
		buf.append("  <modelVersion>4.0.0</modelVersion>");
		buf.append(lineSeparator);
		buf.append("  <groupId>org.ebayopensource.turmeric</groupId>");
		buf.append(lineSeparator);
		buf.append("  <artifactId>org.ebayopensource.turmeric.soa.utils.test</artifactId>");
		buf.append(lineSeparator);
		buf.append("  <packaging>eclipse-test-plugin</packaging>");
		buf.append(lineSeparator);
		buf.append("  <version>1.0.0-SNAPSHOT</version>");
		buf.append(lineSeparator);
		buf.append("  <name>Turmeric: SOA Utils Plugin Tests</name>");
		buf.append(lineSeparator);
		buf.append("</project>");
		XML_DATA = buf.toString();
		XML_DATA_WITH_HEADER = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" 
			+ lineSeparator + buf.toString();
	}
	
	private static IProject project;

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		IPath workspaceRoot = ResourcesPlugin.getWorkspace().getRoot().getLocation();
		IProgressMonitor monitor = new NullProgressMonitor();
		project = WorkspaceUtil.createProject(TestJDOMUtil.class.getSimpleName() + "Project", 
				workspaceRoot, monitor);
		final IProjectDescription description = project.getDescription();
		final List<String> natureIDs = ListUtil.array(description
				.getNatureIds());
		natureIDs.add(JavaCore.NATURE_ID);
		description.setNatureIds(natureIDs.toArray(new String[0]));
		project.setDescription(description, monitor);
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		project.delete(true, new NullProgressMonitor());
		project = null;
	}

	/**
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.utils.xml.JDOMUtil#readXMLFile(java.io.File)}.
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.utils.xml.JDOMUtil#readXMLFile(org.eclipse.core.runtime.IPath)}.
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.utils.xml.JDOMUtil#outputDocument(org.jdom.Document, org.eclipse.core.resources.IFile)}.
	 * @throws Exception 
	 */
	@Test
	public void testOutputDocument() throws Exception {
		InputStream input = null;
		try {
			input = new ByteArrayInputStream(XML_DATA_WITH_HEADER.getBytes());
			Document document = JDOMUtil.readXML(input);
			IFile file = project.getFile("d7000.nikon");
			JDOMUtil.outputDocument(document, file);
			
			Element elem = JDOMUtil.readXMLFile(file.getLocation());
			String result = JDOMUtil.convertXMLToString(elem);
			XMLAssert.assertXMLEqual("Comparing gold copy with generated xml",
					XML_DATA, result);
			//Assert.assertEquals(XML_DATA, result);
			
			elem = JDOMUtil.readXMLFile(file.getLocation().toFile());
			result = JDOMUtil.convertXMLToString(elem);
			XMLAssert.assertXMLEqual("Comparing gold copy with generated xml",
					XML_DATA, result);
			//Assert.assertEquals(XML_DATA, result);
		} finally {
			IOUtils.closeQuietly(input);
		}
	}

	/**
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.utils.xml.JDOMUtil#readXML(java.io.InputStream)}.
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.utils.xml.JDOMUtil#convertXMLToString(org.jdom.Document)}.
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.utils.xml.JDOMUtil#convertXMLToString(org.jdom.Element)}.
	 * @throws JDOMException 
	 * @throws IOException 
	 * @throws SAXException 
	 */
	@Test
	public void testReadXMLInputStream() throws IOException, JDOMException, SAXException {
		InputStream input = null;
		try {
			input = new ByteArrayInputStream(XML_DATA_WITH_HEADER.getBytes());
			Document document = JDOMUtil.readXML(input);
			String result = JDOMUtil.convertXMLToString(document);
			XMLAssert.assertXMLEqual("Comparing gold copy with generated xml",
					XML_DATA_WITH_HEADER, result);
			//Assert.assertEquals(XML_DATA_WITH_HEADER, result);
			
			result = JDOMUtil.convertXMLToString(document.getRootElement());
			XMLAssert.assertXMLEqual("Comparing gold copy with generated xml",
					XML_DATA, result);
			//Assert.assertEquals(XML_DATA, result);
		} finally {
			IOUtils.closeQuietly(input);
		}
	}

	/**
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.utils.xml.JDOMUtil#convertToJDom(org.w3c.dom.Document)}.
	 * @throws ParserConfigurationException 
	 * @throws IOException 
	 * @throws SAXException 
	 */
	@Test
	public void testConvertToJDom() throws SAXException, IOException, ParserConfigurationException {
		InputStream input = null;
		try {
			input = new ByteArrayInputStream(XML_DATA_WITH_HEADER.getBytes());
			org.w3c.dom.Document doc = DocumentBuilderFactory.newInstance()
			.newDocumentBuilder().parse(input);
			Document document = JDOMUtil.convertToJDom(doc);
			String result = JDOMUtil.convertXMLToString(document);
			XMLAssert.assertXMLEqual("Comparing gold copy with generated xml",
					XML_DATA_WITH_HEADER, result);
		} finally {
			IOUtils.closeQuietly(input);
		}
	}

}

