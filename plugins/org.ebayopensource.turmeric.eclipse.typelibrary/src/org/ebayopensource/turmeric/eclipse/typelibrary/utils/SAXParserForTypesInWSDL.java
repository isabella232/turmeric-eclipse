/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.typelibrary.utils;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.ebayopensource.turmeric.eclipse.typelibrary.exception.ImportTypeException;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;


/**
 * Extract type definitions from XSD file.
 * 
 * @author mzang
 * 
 */

enum SchemaProcessStage {
	NotStart, Processing, Processed
}

public class SAXParserForTypesInWSDL extends DefaultHandler {

	private static String DEF_NODE = "definitions";
	private static String TYPE_NODE = "types";
	private static String SCHEMA_NODE = "schema";
	private static String XMLNS = "xmlns";

	private List<String> nodePath = new ArrayList<String>();

	private StringBuffer xsd = null;

	private List<StringBuffer> xsdContents = new ArrayList<StringBuffer>();

	private SchemaProcessStage status = SchemaProcessStage.NotStart;
	private StringBuffer attrInWSDL = new StringBuffer();

	public List<StringBuffer> getTypeDefsFromWSDL(String wsdlPath) throws ImportTypeException {
		InputStream stream;
		try {
			stream = new FileInputStream(wsdlPath);
			SAXParserFactory saxfac = SAXParserFactory.newInstance();
			SAXParser saxParser = saxfac.newSAXParser();
			saxParser.parse(stream, this);
			if (xsd != null) {
				xsdContents.add(xsd);
			}
		} catch (Exception e) {
			throw new ImportTypeException("Exception happens when parsing WSDL:", e);
		}
		return xsdContents;
	}

	private static String getNodeName(String qName) {
		int index = qName.indexOf(':');
		if (index > -1) {
			qName = qName.substring(index + 1);
		}
		return qName;
	}

	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		if (status == SchemaProcessStage.Processing) {
			appendXSD(new String(ch, start, length));
		}
		super.characters(ch, start, length);
	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		boolean isSchemaNode = isSchemaNode();
		nodePath.remove(nodePath.size() - 1);

		if (status == SchemaProcessStage.Processing) {
			appendXSD("</" + qName + ">");
		}

		if (isSchemaNode == true) {
			status = SchemaProcessStage.Processed;
		}
	}

	private boolean isWSDLDefNode() {
		if (nodePath.size() == 1) {
			return DEF_NODE.equals(getNodeName(nodePath.get(0)));
		}
		return false;
	}

	private boolean isSchemaNode() {
		if (nodePath.size() == 3) {
			return DEF_NODE.equals(getNodeName(nodePath.get(0)))
					&& TYPE_NODE.equals(getNodeName(nodePath.get(1)))
					&& SCHEMA_NODE.equals(getNodeName(nodePath.get(2)));
		}
		return false;
	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		nodePath.add(qName);

		boolean inDefStart = isWSDLDefNode();
		boolean inSchema = isSchemaNode();
		if (inSchema == true) {
			status = SchemaProcessStage.Processing;
			if (xsd != null) {
				xsdContents.add(xsd);
			}
			xsd = new StringBuffer(
					"<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n");
		}
		if (status == SchemaProcessStage.Processing) {
			appendXSD("<" + qName + "");
		}

		for (int index = 0; index < attributes.getLength(); index++) {
			String attrName = attributes.getQName(index);
			if (inDefStart) {
				if (attrName.toLowerCase().contains(XMLNS)) {
					attrInWSDL.append(" " + attrName + "=\""
							+ attributes.getValue(index) + "\" ");
				}
			} else if (inSchema) {
				appendXSD(" " + attrName + "=\"" + attributes.getValue(index)
						+ "\" ");
			} else if (status == SchemaProcessStage.Processing) {
				appendXSD(" " + attrName + "=\"" + attributes.getValue(index)
						+ "\" ");
			}
		}
		if (inSchema == true && attrInWSDL != null) {
			appendXSD(attrInWSDL.toString());
			appendXSD(">");
		}
		if ((inSchema == false) && status == SchemaProcessStage.Processing) {
			appendXSD(">");
		}
	}

	private void appendXSD(String content) {
		if (status == SchemaProcessStage.Processing) {
			xsd.append(content);
		}
	}
}
