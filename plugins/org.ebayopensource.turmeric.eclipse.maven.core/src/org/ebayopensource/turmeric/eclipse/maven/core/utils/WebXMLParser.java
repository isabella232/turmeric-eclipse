/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/

package org.ebayopensource.turmeric.eclipse.maven.core.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.ebayopensource.turmeric.eclipse.config.repo.SOAConfigExtensionFactory;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.GlobalRepositorySystem;
import org.ebayopensource.turmeric.eclipse.utils.io.IOUtil;
import org.ebayopensource.turmeric.eclipse.utils.xml.FreeMarkerUtil;
import org.ebayopensource.turmeric.eclipse.utils.xml.JDOMUtil;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.jdom.Content;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.Namespace;

/**
 * The Class WebXMLParser.
 *
 * @author mzang
 */
public class WebXMLParser {

	/** The Constant SERVLET_NODE. */
	public static final String SERVLET_NODE = "servlet";

	/** The Constant SERVLET_NAME_NODE. */
	public static final String SERVLET_NAME_NODE = "servlet-name";

	/** The Constant SERVLET_MAPPING_NODE. */
	public static final String SERVLET_MAPPING_NODE = "servlet-mapping";

	/** The Constant URL_PATTERN_NODE. */
	public static final String URL_PATTERN_NODE = "url-pattern";

	// template related variables
	private static final String WEB_XML_TEMPLATE_NAME = "webxml_Template";

	private static final String ADMIN_NAME_KEY = "adminName";

	private static final String URL_PATTERN_KEY = "urlPattern";

	/**
	 * Gets the source document.
	 *
	 * @param adminName the admin name
	 * @param namespacePart the namespace part
	 * @param majorVersion the major version
	 * @return the source document
	 * @throws Exception the exception
	 */
	public static Document getSourceDocument(String adminName, IProject targetWebProject,
			 int majorVersion) throws Exception {
		String appName= RaptorAppParser.getAppNameFromRaptorApp(targetWebProject);
		
		final String org = GlobalRepositorySystem.instanceOf()
				.getActiveRepositorySystem().getActiveOrganizationProvider()
				.getName();

		String urlPattern = GlobalRepositorySystem.instanceOf()
				.getActiveRepositorySystem().getActiveOrganizationProvider()
				.getURLPattern(adminName, "", majorVersion);
		//Fix to add the url pattern to the web.xml
		if(!appName.equals(""))
		urlPattern="/"+appName+urlPattern;
		URL templateURL = SOAConfigExtensionFactory.getXMLTemplate(org,
				WEB_XML_TEMPLATE_NAME);
		
		if (templateURL == null) {
			throw new IllegalArgumentException(
					"Can not find web.xml template for organization->" + org);
		}
		
		StringWriter writer = new StringWriter();
		Map<String, String> data = new HashMap<String, String>();

		data.put(ADMIN_NAME_KEY, adminName);
		data.put(URL_PATTERN_KEY, urlPattern);

		FreeMarkerUtil.generate(data, templateURL, "webxml", writer);

		String sourceString = writer.toString();

		Document sourceDoc = JDOMUtil.readXML(IOUtils
				.toInputStream(sourceString));

		return sourceDoc;
	}

	/**
	 * Adds the servlet elements to web xml.
	 *
	 * @param sourceDoc the source doc
	 * @param target the target
	 * @param targetFile the target file
	 * @return true, if successful
	 * @throws CoreException the core exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws JDOMException the jDOM exception
	 */
	public static boolean addServletElementsToWebXML(Document sourceDoc,
			InputStream target, IFile targetFile) throws CoreException,
			IOException, JDOMException {
		try {
			Document targetDoc = addServletElements(sourceDoc,target);
			IOUtil.writeTo(JDOMUtil.convertXMLToString(targetDoc), targetFile,
					new NullProgressMonitor());

		} finally {
			IOUtils.closeQuietly(target);
		}
		return true;
	}
	
	/**
	 * Adds the servlet elements.
	 *
	 * @param sourceDoc the source doc
	 * @param target the target
	 * @return the document
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws JDOMException the jDOM exception
	 */
	public static Document addServletElements(Document sourceDoc,
			InputStream target) throws IOException, JDOMException{
		Document targetDoc = null;
		try {
			targetDoc = JDOMUtil.readXML(target);
			IOUtils.closeQuietly(target);

			Element sourceRoot = sourceDoc.getRootElement();
			Element targetRoot = targetDoc.getRootElement();

			addContentToTargetDocument(sourceRoot, targetRoot, SERVLET_NODE);

			addContentToTargetDocument(sourceRoot, targetRoot,
					SERVLET_MAPPING_NODE);

		} finally {
			IOUtils.closeQuietly(target);
		}
		return targetDoc;
	}

	private static boolean addContentToTargetDocument(Element sourceRoot,
			Element targetRoot, String nodeName) {
		Namespace sourceNamespace = sourceRoot.getNamespace();

		Element sourceEle = sourceRoot.getChild(nodeName, sourceNamespace);
		if (sourceEle == null) {
			return false;
		}

		Namespace targetNamespace = targetRoot.getNamespace();

		int index = getNextNodeIndex(targetRoot, nodeName);
		if (index == -1) {
			targetRoot.addContent(copyElement(sourceEle, targetNamespace));
		} else {
			targetRoot.addContent(index + 1, copyElement(sourceEle,
					targetNamespace));
		}

		return true;
	}

	@SuppressWarnings("unchecked")
	private static int getNextNodeIndex(Element root, String nodeName) {
		List<Object> child = root.getChildren(SERVLET_NODE);
		int index = -1;
		if (child == null) {
			return index;
		}
		for (Object objEle : child) {
			if (objEle instanceof Element == false) {
				continue;
			}
			int currIdx = root.indexOf((Content) objEle);
			if (index < currIdx) {
				index = currIdx;
			}
		}
		return index;
	}

	private static Element copyElement(Element source, Namespace namespace) {
		Element current = new Element(source.getName());
		current.setText(source.getText());
		current.setNamespace(namespace);
		for (Object elementObj : source.getChildren()) {
			if (elementObj instanceof Element) {
				Element child = copyElement((Element) elementObj, namespace);
				current.addContent(child);
			}
		}
		return current;
	}

	private static String getNodeValue(Element root, String... path) {
		Element current = root;
		Namespace namespace = root.getNamespace();
		for (String nodeName : path) {
			Element child = current.getChild(nodeName, namespace);
			if (child == null) {
				return null;
			}
			current = child;
		}
		return current.getValue();
	}

	@SuppressWarnings("unused")
	private static void setNodeValue(Element root, String value, String... path) {
		Element current = root;
		Namespace namespace = root.getNamespace();
		for (String nodeName : path) {
			Element child = current.getChild(nodeName, namespace);
			if (child == null) {
				return;
			}
			current = child;
		}
		current.setText(value);
	}

	/**
	 * Gets the servlet names.
	 *
	 * @param source the source
	 * @return the servlet names
	 */
	@SuppressWarnings("unchecked")
	public static List<String> getServletNames(InputStream source) {
		List<String> servletNames = new ArrayList<String>();
		try {
			Document document = JDOMUtil.readXML(source);
			source.close();
			Element root = document.getRootElement();
			Namespace namespace = root.getNamespace();
			List<Object> servlets = root.getChildren(SERVLET_NODE, namespace);
			if (servlets == null) {
				return servletNames;
			}
			for (Object servletObj : servlets) {
				if (servletObj instanceof Element == false) {
					continue;
				}
				Element servlet = (Element) servletObj;
				String servletName = getNodeValue(servlet, SERVLET_NAME_NODE);
				if (servletName == null) {
					continue;
				}
				servletNames.add(servletName);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JDOMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			// IOUtils.closeQuietly(source);
		}
		return servletNames;
	}

}
