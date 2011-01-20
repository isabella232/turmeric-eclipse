/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
/**
 * 
 */
package org.ebayopensource.turmeric.eclipse.utils.xml;

import java.io.StringWriter;
import java.io.Writer;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.io.IOUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

/**
 * @author yayu
 *
 */
public final class XMLUtil {

	/**
	 * 
	 */
	private XMLUtil() {
		super();
	}
	
	public static String convertXMLToString(final Node node) 
	throws TransformerFactoryConfigurationError, TransformerException {
		if (node == null)
			return "";
		
		final Transformer t = TransformerFactory.newInstance().newTransformer();
		StringWriter sw = null;
		try {
			sw = new StringWriter();
			t.setOutputProperty(OutputKeys.INDENT,"yes");
			t.setOutputProperty("{http://xml.apache.org/xslt}indent-amount","4");
			if ((node instanceof Document) == false) {
				t.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
			}
			t.transform(new DOMSource(node), new StreamResult(sw));
		} finally {
			IOUtils.closeQuietly(sw);
		}
		
		return sw.toString();
	}
	
	public static void writeXML(final Node node, 
			final Writer writer) 
	throws TransformerFactoryConfigurationError, TransformerException {
		if (node == null || writer == null)
			return ;
		
		final Transformer t = TransformerFactory.newInstance().newTransformer();
		try {
			t.setOutputProperty(OutputKeys.INDENT,"yes");
			t.setOutputProperty("{http://xml.apache.org/xslt}indent-amount","4");
			if ((node instanceof Document) == false) {
				t.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
			}
			t.transform(new DOMSource(node), new StreamResult(writer));
		} finally {
			IOUtils.closeQuietly(writer);
		}
		
	}
}
