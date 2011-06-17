/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.typelibrary.ui.wst;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.apache.commons.lang.StringUtils;
import org.apache.xerces.util.DOMUtil;
import org.ebayopensource.turmeric.common.config.LibraryType;
import org.ebayopensource.turmeric.eclipse.core.logging.SOALogger;
import org.ebayopensource.turmeric.eclipse.core.resources.constants.SOATypeLibraryConstants;
import org.ebayopensource.turmeric.eclipse.exception.core.SOABadParameterException;
import org.ebayopensource.turmeric.eclipse.typelibrary.resources.SOAMessages;
import org.ebayopensource.turmeric.eclipse.typelibrary.ui.TypeLibraryUIActivator;
import org.ebayopensource.turmeric.eclipse.typelibrary.utils.TypeLibraryUtil;
import org.ebayopensource.turmeric.eclipse.utils.lang.StringUtil;
import org.eclipse.wst.sse.core.internal.document.IDocumentLoader;
import org.eclipse.wst.sse.core.internal.encoding.CodedIO;
import org.eclipse.wst.sse.core.internal.encoding.EncodingRule;
import org.eclipse.wst.sse.core.internal.provisional.IModelLoader;
import org.eclipse.wst.sse.core.internal.provisional.IStructuredModel;
import org.eclipse.wst.sse.core.internal.provisional.text.IStructuredDocument;
import org.eclipse.wst.xml.core.internal.modelhandler.ModelHandlerForXML;
import org.eclipse.wst.xsd.ui.internal.common.util.XSDCommonUIUtils;
import org.eclipse.xsd.XSDAnnotation;
import org.eclipse.xsd.XSDSchema;
import org.eclipse.xsd.XSDTypeDefinition;
import org.eclipse.xsd.util.XSDConstants;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * The WTP Copy Utility Class. WTP has its own mechanism to copy existing xsds
 * to wsdl files. SOA reuse that functionality rather than re inventing the
 * wheel. This class if the copy functionality touch point.
 * 
 * @author smathew
 * 
 */
public class WTPCopyUtil {

	private static IStructuredModel loadUsingWTP(URL url) throws IOException {
		IStructuredDocument document = null;
		IStructuredModel model = null;
		ModelHandlerForXML xmlModelHandler = new ModelHandlerForXML();
		IDocumentLoader loader = xmlModelHandler.getDocumentLoader();
		InputStream inputStream = getMarkSupportedStream(url.openStream());

		document = (IStructuredDocument) loader.createNewStructuredDocument(
				null, inputStream, EncodingRule.FORCE_DEFAULT);
		IModelLoader xmlModelLoader = xmlModelHandler.getModelLoader();
		String path = url.getPath();
		if (path.startsWith("file:"))
			path = path.substring(5);

		IStructuredModel retModel = xmlModelLoader.createModel(document, path,
				xmlModelHandler);
		return retModel;
	}

	private static InputStream getMarkSupportedStream(InputStream inputStream) {
		if (!inputStream.markSupported()) {
			inputStream = new BufferedInputStream(inputStream,
					CodedIO.MAX_BUF_SIZE);
		}
		inputStream.mark(CodedIO.MAX_MARK_SIZE);
		return inputStream;
	}

	/**
	 * Copies the schema specified in the source URL derived from the library
	 * type to the destination XSD Schema. The name of the type inside the
	 * schema should be specified in the name parameter. Throws Bad Parameter
	 * Exception if the specified URL derived from the library type is invalid
	 * or if it is not accessible. Clients are supposed to validate the
	 * parameters or react to the exception and show it to the user
	 * meaningfully.
	 *
	 * @param dstSchema the dst schema
	 * @param srcType the src type
	 * @param typeFolding the type folding
	 * @throws SOABadParameterException the sOA bad parameter exception
	 */
	public static void copy(XSDSchema dstSchema, LibraryType srcType,
			boolean typeFolding) throws SOABadParameterException {
		String name = srcType.getName();
		URL srcURL = null;
		XSDSchema srcSchema = null;
		try {
			srcURL = TypeLibraryUtil.getXSD(srcType);
			srcSchema = TypeLibraryUtil.parseSchema(srcURL);
		} catch (Exception e) {
			SOALogger.getLogger().error(e);
			throw new SOABadParameterException(StringUtil.formatString(
					SOAMessages.INVALID_XSD_URL, srcURL));
		}

		XSDTypeDefinition srcTypeDefinition = null;
		for (XSDTypeDefinition typeDefiniton : srcSchema.getTypeDefinitions()) {
			if (StringUtils.equals(typeDefiniton.getName(), name)) {
				srcTypeDefinition = typeDefiniton;
				break;
			}
		}
		if (srcTypeDefinition == null || srcTypeDefinition.getElement() == null) {
			throw new SOABadParameterException(StringUtil.formatString(
					SOAMessages.INVALID_XSD, srcURL, name));
		}

		addAnnotation(srcTypeDefinition, srcType);
		transformQNamePrefix(dstSchema, srcSchema, typeFolding);
	}

	/**
	 * Transform q name prefix.
	 *
	 * @param dstSchema the dst schema
	 * @param srcSchema the src schema
	 * @param typeFolding the type folding
	 * @throws SOABadParameterException the sOA bad parameter exception
	 */
	public static void transformQNamePrefix(XSDSchema dstSchema,
			XSDSchema srcSchema, boolean typeFolding)
			throws SOABadParameterException {

		String targetNameSpace = dstSchema.getTargetNamespace();
		String targetNameSpaceprefix = "";
		Map<String, String> srcPrefixes = srcSchema
				.getQNamePrefixToNamespaceMap();
		Map<String, String> dstPrefixes = dstSchema
				.getQNamePrefixToNamespaceMap();
		Map<String, String> srcToDstPrefixMap = new TreeMap<String, String>();
		Map<String, String> additionalSrcPrefixes = new TreeMap<String, String>();

		// finding the targetNameSpacePrefix in the destination schema
		for (Entry<String, String> entry : dstPrefixes.entrySet()) {
			if (StringUtils.equals(entry.getValue(), targetNameSpace)) {
				targetNameSpaceprefix = entry.getKey();
			}
		}
		// if the destination schema does not have a name space prefix for
		// its own target name space
		if (StringUtils.isEmpty(targetNameSpaceprefix))
			targetNameSpaceprefix = StringUtils.stripEnd(TypeLibraryUIActivator
					.getPrefix(dstSchema, targetNameSpace), ":");

		for (Entry<String, String> srcEntry : srcPrefixes.entrySet()) {
			if (!XSDConstants.isSchemaForSchemaNamespace(srcEntry.getValue())) {
				if (typeFolding) {
					srcToDstPrefixMap.put(srcEntry.getKey(),
							targetNameSpaceprefix);
				} else {
					boolean foundCorrespondingPrefix = false;
					for (Entry<String, String> dstEntry : dstPrefixes
							.entrySet()) {
						if (StringUtils.equals(dstEntry.getValue(), srcEntry
								.getValue())) {
							srcToDstPrefixMap.put(srcEntry.getKey(), dstEntry
									.getKey());
							foundCorrespondingPrefix = true;
						}
					}
					// if there is no corresponding prefix add one to
					// the destination schema and replace it in the src
					// schema
					if (!foundCorrespondingPrefix) {
						String newPrefix = StringUtils
								.stripEnd(TypeLibraryUIActivator.getPrefix(dstSchema,
										srcEntry.getValue()), ":");
						// we add this to the src schema also to make sure
						// that the prefix changes does not make it invalid
						additionalSrcPrefixes.put(newPrefix, srcEntry
								.getValue());
						srcToDstPrefixMap.put(srcEntry.getKey(), newPrefix);
					}

				}
			}
		}

		srcPrefixes.putAll(additionalSrcPrefixes);
		Element srcElement = srcSchema.getTypeDefinitions().get(0).getElement();
		Element dstElement = (Element) dstSchema.getElement();
		// setting the Schema for Schema prefix for eg: xs to xsd or
		// whatever.
		srcSchema.setSchemaForSchemaQNamePrefix(dstSchema
				.getSchemaForSchemaQNamePrefix());
		updatePrefixes(srcElement, srcToDstPrefixMap);

		DOMUtil.copyInto(srcElement, dstElement);

	}

	private static void updatePrefixes(Element element,
			Map<String, String> prefixMap) {
		NamedNodeMap namedNodeMap = element.getAttributes();
		for (int i = 0; i < namedNodeMap.getLength(); i++) {
			String attrValue = namedNodeMap.item(i).getNodeValue();
			String attrName = namedNodeMap.item(i).getNodeName();
			for (String str : prefixMap.keySet()) {
				if (!StringUtils.isEmpty(attrValue)
						&& attrValue.trim().startsWith(str + ":")) {
					String newAttrValue = StringUtils.replaceOnce(attrValue
							.trim(), str + ":", prefixMap.get(str) + ":");
					element.getAttributeNode(attrName).setValue(newAttrValue);

				}
			}
		}
		NodeList children = element.getChildNodes();
		for (int i = 0; i < children.getLength(); i++) {
			Node child = children.item(i);
			if (child instanceof Element)
				updatePrefixes((Element) child, prefixMap);
		}
	}

	/**
	 * Adds the annotation.
	 *
	 * @param typeDefinition the type definition
	 * @param libraryType the library type
	 */
	public static void addAnnotation(XSDTypeDefinition typeDefinition,
			LibraryType libraryType) {

		XSDAnnotation annotation = XSDCommonUIUtils.getInputXSDAnnotation(
				typeDefinition, true);

		Element appInfoElement = null;

		if (annotation.getApplicationInformation(null).size() <= 0
				|| annotation.getApplicationInformation(null).get(0) == null) {
			appInfoElement = annotation.createApplicationInformation(null);
		} else {
			appInfoElement = annotation.getApplicationInformation().get(0);
		}
		fillAppInfo(appInfoElement, libraryType);
		annotation.getElement().appendChild(appInfoElement);

	}

	/**
	 * Fill app info.
	 *
	 * @param appInfoElement the app info element
	 * @param libraryType the library type
	 */
	public static void fillAppInfo(Element appInfoElement,
			LibraryType libraryType) {
		NodeList children = appInfoElement.getChildNodes();

		Element typeLibElement = null;
		for (int i = 0; i < children.getLength(); i++) {
			Node child = children.item(i);
			if (child instanceof Element) {
				Element childElement = (Element) child;
				if (SOATypeLibraryConstants.TAG_TYPE_LIB.equals(childElement
						.getTagName())) {
					typeLibElement = childElement;
					break;
				}
			}

		}
		//no element exists already
		if (typeLibElement == null) {
			typeLibElement = appInfoElement.getOwnerDocument().createElement(
					SOATypeLibraryConstants.TAG_TYPE_LIB);
			appInfoElement.appendChild(typeLibElement);
		}
		typeLibElement.setAttribute(SOATypeLibraryConstants.ATTR_LIB,
				libraryType.getLibraryInfo().getLibraryName());
		typeLibElement.setAttribute(SOATypeLibraryConstants.ATTR_NMSPC,
				libraryType.getLibraryInfo().getLibraryNamespace());

		
	}

}
