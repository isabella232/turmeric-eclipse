/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.utils.xml;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IPath;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.DOMBuilder;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

/**
 * Standard JDOM Utility Class. JDOM has the standard encoding and gets the
 * special ASCIIout by itself. So in SOA string conversions are supposed to take
 * the JDOM route to save. This class mainly contains the conversion logic from
 * standard w3 elements to JDOM ones. Also it has some file based and string
 * based conversions. JDOM for some strange reasons does not follow standard w3
 * class hierarchy.
 * 
 * @author smathew
 * 
 */
public class JDOMUtil {

	/**
	 * Saves the parameter document to the given file. Client should handle the
	 * exception. Not null safe.
	 *
	 * @param document the document
	 * @param file the file
	 * @throws Exception the exception
	 */
	public static void outputDocument(final Document document, final IFile file)
			throws Exception {
		final XMLOutputter outputter = new XMLOutputter(Format
				.getPrettyFormat());
		final String fileContents = outputter.outputString(document);
		if (!file.exists())
			file.create(new ByteArrayInputStream(fileContents.getBytes()),
					true, null);
		else
			file.setContents(new ByteArrayInputStream(fileContents.getBytes()),
					true, true, null);

	}

	/**
	 * Wrapper over the linked method read xml. Convenience method to avoid type
	 * casting from the client side.
	 *
	 * @param file the file
	 * @return the element
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws JDOMException the jDOM exception
	 * @see {@link JDOMUtil#readXML(File)}
	 */
	public static Element readXMLFile(final File file) throws IOException,
			JDOMException {
		return readXML(file).getRootElement();
	}

	/**
	 * Wrapper over the linked method read xml. This API is for eclipse specific
	 * clients.
	 *
	 * @param path the path
	 * @return the element
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws JDOMException the jDOM exception
	 * @see {@link JDOMUtil#readXML(File)}
	 */
	public static Element readXMLFile(final IPath path) throws IOException,
			JDOMException {
		return readXMLFile(path.toFile());
	}

	/**
	 * The real :) file read method used by the above wrappers. Uses the usual
	 * SAX builder
	 *
	 * @param file the file
	 * @return the document
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws JDOMException the jDOM exception
	 */
	public static Document readXML(final File file) throws IOException,
			JDOMException {
		try {
			final String contents = FileUtils.readFileToString(file);
			final Document document = new SAXBuilder().build(IOUtils
					.toInputStream(contents));
			return document;
		} catch (Exception e) {
			throw new IOException("Error occured while parsing file->" + file.getCanonicalPath(), e);
		}
	}
	
	/**
	 * Read xml.
	 *
	 * @param ins the ins
	 * @return the document
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws JDOMException the jDOM exception
	 */
	public static Document readXML(final InputStream ins) throws IOException, 
	JDOMException {
		try {
			final Document document = new SAXBuilder().build(ins);
			return document;
		} catch (Exception e) {
			throw new IOException("Error occured while parsing input stream->" + ins, e);
		}
	}

	/**
	 * Converts a JDOM document to String. Note: JDOM document is not a w3c
	 * document.
	 *
	 * @param document the document
	 * @return the string
	 */
	public static String convertXMLToString(final Document document) {
		final XMLOutputter outputter = new XMLOutputter(Format
				.getPrettyFormat());
		return outputter.outputString(document);
	}

	/**
	 * Converts a JDOM element to String. Note: JDOM element is not a w3c
	 * element.
	 *
	 * @param element the element
	 * @return the string
	 */
	public static String convertXMLToString(final Element element) {
		final XMLOutputter outputter = new XMLOutputter(Format
				.getPrettyFormat());
		return outputter.outputString(element);
	}

	/**
	 * Converts a w3c document to JDOM one. Uses the DOM builder.
	 *
	 * @param domDoc the dom doc
	 * @return the org.jdom. document
	 */
	public static org.jdom.Document convertToJDom(org.w3c.dom.Document domDoc) {
		DOMBuilder builder = new DOMBuilder();
		org.jdom.Document jdomDoc = builder.build(domDoc);
		return jdomDoc;
	}
}
