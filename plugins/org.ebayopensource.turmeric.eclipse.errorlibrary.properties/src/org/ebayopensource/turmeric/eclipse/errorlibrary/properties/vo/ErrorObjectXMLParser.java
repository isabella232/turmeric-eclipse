/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.errorlibrary.properties.vo;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.ebayopensource.turmeric.eclipse.errorlibrary.properties.providers.PropertiesSOAConstants;
import org.ebayopensource.turmeric.eclipse.errorlibrary.properties.utils.TurmericErrorLibraryUtils;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.Namespace;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

public class ErrorObjectXMLParser {
	private static final String NAMESPACE_COMMON_CONFIG = "http://www.ebayopensource.org/turmeric/common/config";
	private static final Namespace defaultNamespace = Namespace.getNamespace(NAMESPACE_COMMON_CONFIG);
	
	public static String convertErrorBundle(SOAErrorBundleVO vo) throws IOException {
		Document doc = new Document();
		Element bundle = new Element(ErrorObjectXMLConstants.ELEM_BUNDLE);
		
		bundle.addNamespaceDeclaration(Namespace.getNamespace("tns", NAMESPACE_COMMON_CONFIG));
		bundle.addNamespaceDeclaration(Namespace.getNamespace("xs", "http://www.w3.org/2001/XMLSchema"));
		bundle.setNamespace(defaultNamespace);
		bundle.setAttribute(ErrorObjectXMLConstants.ATTR_VERSION, ""+vo.getVersion());
		bundle.setAttribute(ErrorObjectXMLConstants.ATTR_PKGNAME, vo.getPackageName());
		bundle.setAttribute(ErrorObjectXMLConstants.ATTR_DOMAIN, vo.getDomain());
		bundle.setAttribute(ErrorObjectXMLConstants.ATTR_ORG, vo.getOrganization());
		bundle.setAttribute(ErrorObjectXMLConstants.ATTR_ERROR_LIB_NAME, vo.getLibraryName());
		doc.addContent(bundle);
		Element errorList = new Element(ErrorObjectXMLConstants.ELEM_ERRORLIST, defaultNamespace);
		bundle.addContent(errorList);
		
		for (SOAErrorVO error : vo.getList().getErrors()) {
			Element elem = new Element(ErrorObjectXMLConstants.ELEM_ERROR, defaultNamespace);
			elem.setAttribute(ErrorObjectXMLConstants.ATTR_ID, ""+error.getId());
			elem.setAttribute(ErrorObjectXMLConstants.ATTR_CATEGORY, error.getCategory());
			elem.setAttribute(ErrorObjectXMLConstants.ATTR_SUBDOMAIN, error.getSubdomain());
			elem.setAttribute(ErrorObjectXMLConstants.ATTR_SEVERITY, error.getSeverity());
			elem.setAttribute(ErrorObjectXMLConstants.ATTR_ERRORGROUP, error.getErrorGroups());
			elem.setAttribute(ErrorObjectXMLConstants.ATTR_NAME, error.getName());
			errorList.addContent(elem);
		}
		XMLOutputter output = new XMLOutputter(Format.getPrettyFormat());
		return output.outputString(doc);
	}
	
	public static List<SOAErrorBundleVO> getErrorBundle(IProject project) throws JDOMException, IOException, CoreException {
		List<SOAErrorBundleVO> result = new ArrayList<SOAErrorBundleVO>();
		if (project != null) {
			for (String domainName : TurmericErrorLibraryUtils.getAllErrorDomains(project)) {
				IFile dataFile = getDomainFile(project, domainName);
				if (dataFile != null && dataFile.exists()) {
					InputStream ins = null;
					try {
						ins = dataFile.getContents();
						result.add(getErrorBundle(ins));
					} finally {
						IOUtils.closeQuietly(ins);
					}
				}
			}
		}
		return result;
	}
	
	private static IFile getDomainFile(IProject project, String domain) {
		return project.getFile(PropertiesSOAConstants.FOLDER_ERROR_DOMAIN + "/" + domain + "/" + PropertiesSOAConstants.FILE_ERROR_DATA);
	}

	public static SOAErrorBundleVO getErrorBundle(InputStream input)
			throws JDOMException, IOException {
		SOAErrorBundleVO result = new SOAErrorBundleVO();
		if (input != null) {
			SAXBuilder builder = new SAXBuilder();
			Document doc = builder.build(input);
			Element bundle = doc.getRootElement();
			bundle.setNamespace(defaultNamespace);
			result.setDomain(getAttributeAsString(bundle, ErrorObjectXMLConstants.ATTR_DOMAIN));
			result.setLibraryName(getAttributeAsString(bundle, ErrorObjectXMLConstants.ATTR_ERROR_LIB_NAME));
			result.setOrganization(getAttributeAsString(bundle, ErrorObjectXMLConstants.ATTR_ORG));
			result.setPackageName(getAttributeAsString(bundle, ErrorObjectXMLConstants.ATTR_PKGNAME));
			result.setVersion(getAttributeAsString(bundle, ErrorObjectXMLConstants.ATTR_VERSION));
			
			Element errorlist = bundle.getChild(ErrorObjectXMLConstants.ELEM_ERRORLIST, defaultNamespace);
			if (errorlist == null) {
				errorlist = new Element(ErrorObjectXMLConstants.ELEM_ERRORLIST);
			}
			SOAErrorListVO listVo = new SOAErrorListVO();
			result.setList(listVo);
			for (Iterator<?> iter = errorlist.getChildren().iterator();iter.hasNext();) {
				Element error = (Element) iter.next();
				SOAErrorVO vo = new SOAErrorVO();
				vo.setId(getAttributeAsLong(error, ErrorObjectXMLConstants.ATTR_ID));
				vo.setCategory(getAttributeAsString(error, ErrorObjectXMLConstants.ATTR_CATEGORY));
				vo.setErrorGroups(getAttributeAsString(error, ErrorObjectXMLConstants.ATTR_ERRORGROUP));
				vo.setName(getAttributeAsString(error, ErrorObjectXMLConstants.ATTR_NAME));
				vo.setSeverity(getAttributeAsString(error, ErrorObjectXMLConstants.ATTR_SEVERITY));
				vo.setSubdomain(getAttributeAsString(error, ErrorObjectXMLConstants.ATTR_SUBDOMAIN));
				listVo.addError(vo);
			}
		}
		return result;
	}
	
	private static String getAttributeAsString(Element element, String attributename) {
		return element.getAttributeValue(attributename);
	}
	
	private static Long getAttributeAsLong(Element element, String attributename) {
		String value = element.getAttributeValue(attributename);
		try {
			return Long.parseLong(value);
		} catch (NumberFormatException e) {
			return 0L;
		}
	}
}
