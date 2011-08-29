/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.typelibrary.ui;

/**
 * Extract type definitions from XSD file.
 * 
 * @author mzang
 * 
 */
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.ebayopensource.turmeric.eclipse.typelibrary.ui.model.CommonTypeProp;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

// TODO: Auto-generated Javadoc
/**
 * 
 * 
 *
 */
enum ParseStatus {
	/**
	 * Proccesing Type.
	 */
	ProcessingType, ProcessingCommon, ProcessingIgnore
}

/**
 * The Class XSDTypeParser.
 */
public class XSDTypeParser extends DefaultHandler {

	/** The Constant typeNameMarker. */
	public static final String typeNameMarker = "TypeName";

	/** The Constant namespaceMarker. */
	public static final String namespaceMarker = "Namespace";

	/** The Constant documentMarker. */
	public static final String documentMarker = "Document";

	/** The current. */
	private CommonTypeProp current = null;

	// contains xml header and import.
	/** The common header. */
	private StringBuffer commonHeader = new StringBuffer(
			"<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n");

	/** The curr type content. */
	private StringBuffer currTypeContent = new StringBuffer();

	/** The all types. */
	private List<CommonTypeProp> allTypes = new ArrayList<CommonTypeProp>();

	/** The Constant COMPLEX_TYPE. */
	private static final String COMPLEX_TYPE = "complexType";

	/** The Constant SIMPLE_TYPE. */
	private static final String SIMPLE_TYPE = "simpleType";

	/** The Constant IMPORT. */
	private static final String IMPORT = "import";

	/** The Constant SCHEMA_NODE_NAME. */
	private static final String SCHEMA_NODE_NAME = "schema";

	/** The Constant ANNOTATION_NODE_NAME. */
	private static final String ANNOTATION_NODE_NAME = "annotation";

	/** The Constant DOCUMENT_NODE_NAME. */
	private static final String DOCUMENT_NODE_NAME = "documentation";

	/** The Constant NAME_ATTR. */
	private static final String NAME_ATTR = "name";

	/** The Constant TARGET_NAMESPACE_ATTR. */
	private static final String TARGET_NAMESPACE_ATTR = "targetNamespace";

	/** The LEVE l2_ node. */
	private static Set<String> LEVEL2_NODE = new HashSet<String>();

	static {
		LEVEL2_NODE.add(COMPLEX_TYPE);
		LEVEL2_NODE.add(SIMPLE_TYPE);
		LEVEL2_NODE.add(IMPORT);

	}

	/** The status. */
	ParseStatus status = ParseStatus.ProcessingCommon;

	/** The target namespace. */
	private String targetNamespace = null;

	/** The node path. */
	private List<String> nodePath = new ArrayList<String>();

	/** The document index. */
	private int documentIndex = -1;

	/** The prefix. */
	private String prefix = "xs";

	/** The schema end. */
	private String schemaEnd = null;

	/**
	 * Instantiates a new xSD type parser.
	 *
	 * @param xsdStream the xsd stream
	 */
	public XSDTypeParser(InputStream xsdStream) {
		try {
			SAXParserFactory saxfac = SAXParserFactory.newInstance();
			SAXParser saxParser = saxfac.newSAXParser();
			saxParser.parse(xsdStream, this);

			String commonHeaderStr = commonHeader.toString();
			for (CommonTypeProp type : allTypes) {
				type.setSchemaTemplate(commonHeaderStr
						+ type.getSchemaTemplate() + schemaEnd);
			}

		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Checks if is type node.
	 *
	 * @param nodeName the node name
	 * @return true, if is type node
	 */
	private static boolean isTypeNode(String nodeName) {
		nodeName = getNodeName(nodeName);
		return SIMPLE_TYPE.equalsIgnoreCase(nodeName)
				|| COMPLEX_TYPE.equalsIgnoreCase(nodeName);
	}

	/**
	 * Checks if is schema node.
	 *
	 * @param nodeName the node name
	 * @return true, if is schema node
	 */
	private static boolean isSchemaNode(String nodeName) {
		return SCHEMA_NODE_NAME.equalsIgnoreCase(getNodeName(nodeName));
	}

	/**
	 * Should handle on level2.
	 *
	 * @param qName the q name
	 * @return true, if successful
	 */
	private static boolean shouldHandleOnLevel2(String qName) {
		String nodeName = getNodeName(qName);
		return LEVEL2_NODE.contains(nodeName);
	}

	/**
	 * Gets the node name.
	 *
	 * @param qName the q name
	 * @return the node name
	 */
	private static String getNodeName(String qName) {
		int index = qName.indexOf(':');
		if (index > -1) {
			qName = qName.substring(index + 1);
		}
		return qName;
	}

	/**
	 * Gets the prefix.
	 *
	 * @param qName the q name
	 * @return the prefix
	 */
	private static String getPrefix(String qName) {
		int index = qName.indexOf(':');
		if (index > -1) {
			return qName.substring(0, index);
		}
		return "xs";
	}

	/**
	 * Checks if is type document.
	 *
	 * @return true, if is type document
	 */
	private boolean isTypeDocument() {
		if (nodePath.size() == 4 && isSchemaNode(nodePath.get(0))
				&& isTypeNode(nodePath.get(1))) {
			return ANNOTATION_NODE_NAME.equalsIgnoreCase(getNodeName(nodePath
					.get(2)))
					&& DOCUMENT_NODE_NAME.equalsIgnoreCase(getNodeName(nodePath
							.get(3)));
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see org.xml.sax.helpers.DefaultHandler#characters(char[], int, int)
	 */
	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		if (status == ParseStatus.ProcessingIgnore) {
			return;
		}

		String content = new String(ch, start, length);
		if (isTypeDocument() == true) {
			current.setDescription(content);
			appendXSD("${" + documentMarker + "}");
		} else {
			appendXSD(content);
		}
	}

	/* (non-Javadoc)
	 * @see org.xml.sax.helpers.DefaultHandler#endElement(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		nodePath.remove(nodePath.size() - 1);
		if (status == ParseStatus.ProcessingIgnore) {
			return;
		}

		if (isTypeDocument() == true) {
			current.setDescription("");
			appendXSD("${" + documentMarker + "}");
		}
		if (isSchemaNode(qName) == true) {
			schemaEnd = "\r\n</" + qName + ">";
		} else {
			appendXSD("</" + qName + ">");
		}

		if ((status == ParseStatus.ProcessingType)
				&& (isTypeNode(qName) == true)) {
			if (current != null) {
				status = ParseStatus.ProcessingCommon;
				current.setTargetNamespace(targetNamespace);
				if (current.getDescription() == null) {
					current.setDescription("");
					currTypeContent.insert(documentIndex, getDummyDocument());
				}
				current.setSchemaTemplate(currTypeContent.toString());
				currTypeContent.delete(0, currTypeContent.length());
				allTypes.add(current);
			}
			documentIndex = -1;
		}
	}

	/**
	 * Gets the dummy document.
	 *
	 * @return the dummy document
	 */
	private String getDummyDocument() {
		return "\r\n<" + prefix + ":annotation>" + "\r\n<" + prefix
				+ ":documentation>" + "\r\n${" + documentMarker + "}"
				+ "\r\n</" + prefix + ":documentation>" + "\r\n </" + prefix
				+ ":annotation>";

	}

	/* (non-Javadoc)
	 * @see org.xml.sax.helpers.DefaultHandler#startElement(java.lang.String, java.lang.String, java.lang.String, org.xml.sax.Attributes)
	 */
	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		nodePath.add(qName);

		if (nodePath.size() == 2 && (shouldHandleOnLevel2(qName) == false)) {
			status = ParseStatus.ProcessingIgnore;
			return;
		}

		boolean inTypeNode = false;
		boolean inSchemaNode = false;
		if ((status != ParseStatus.ProcessingType)
				&& (isTypeNode(qName) == true)) {
			status = ParseStatus.ProcessingType;
			inTypeNode = true;
			current = new CommonTypeProp();
		}

		inSchemaNode = isSchemaNode(qName);

		if (inSchemaNode) {
			prefix = getPrefix(qName);
		}

		appendXSD("<" + qName + "");
		for (int index = 0; index < attributes.getLength(); index++) {

			String attrName = attributes.getQName(index);
			String value = attributes.getValue(index);

			if (inTypeNode == true && NAME_ATTR.equalsIgnoreCase(attrName)) {
				// replace type name with type name marker
				current.setTypeName(value);
				appendXSD(" " + attrName + "=\"${" + typeNameMarker + "}\" ");
			} else if (inSchemaNode == true
					&& TARGET_NAMESPACE_ATTR.equalsIgnoreCase(attrName)) {
				targetNamespace = value;
				appendXSD(" " + attrName + "=\"${" + namespaceMarker + "}\" ");
			} else {
				appendXSD(" " + attrName + "=\"" + value + "\" ");
			}
		}
		appendXSD(">");

		if (inTypeNode == true) {
			documentIndex = currTypeContent.length();
		}

	}

	/**
	 * Append xsd.
	 *
	 * @param content the content
	 */
	private void appendXSD(String content) {
		if (status == ParseStatus.ProcessingType) {
			currTypeContent.append(content);
		} else if (status == ParseStatus.ProcessingCommon) {
			commonHeader.append(content);
		}
	}

	/**
	 * Gets the all types.
	 *
	 * @return the all types
	 */
	public List<CommonTypeProp> getAllTypes() {
		return allTypes;
	}

	/**
	 * Sets the all types.
	 *
	 * @param allTypes the new all types
	 */
	public void setAllTypes(List<CommonTypeProp> allTypes) {
		this.allTypes = allTypes;
	}

}
