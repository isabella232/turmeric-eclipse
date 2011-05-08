/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.typelibrary;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.xml.namespace.QName;

import org.apache.commons.lang.StringUtils;
import org.ebayopensource.turmeric.common.config.LibraryType;
import org.ebayopensource.turmeric.eclipse.core.resources.constants.SOATypeLibraryConstants;
import org.ebayopensource.turmeric.eclipse.exception.core.SOABadParameterException;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.SOAGlobalRegistryAdapter;
import org.ebayopensource.turmeric.eclipse.typelibrary.resources.SOAMessages;
import org.ebayopensource.turmeric.eclipse.typelibrary.utils.TypeLibraryUtil;
import org.ebayopensource.turmeric.eclipse.utils.lang.StringUtil;
import org.ebayopensource.turmeric.tools.library.SOATypeRegistry;
import org.eclipse.core.runtime.Plugin;
import org.eclipse.wst.wsdl.Definition;
import org.eclipse.wst.wsdl.Types;
import org.eclipse.xsd.XSDAnnotation;
import org.eclipse.xsd.XSDImport;
import org.eclipse.xsd.XSDInclude;
import org.eclipse.xsd.XSDSchema;
import org.eclipse.xsd.XSDSchemaDirective;
import org.eclipse.xsd.XSDTypeDefinition;
import org.osgi.framework.BundleContext;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


/**
 * The activator class controls the plug-in life cycle.
 *
 * @author smathew
 */
public class TypeLibraryActivator extends Plugin {

	// The plug-in ID
	/** The Constant PLUGIN_ID. */
	public static final String PLUGIN_ID = "org.ebayopensource.turmeric.eclipse.typelibrary";
	
	/** The Constant ICON_PATH. */
	public static final String ICON_PATH = "icons/";

	// The shared instance
	private static TypeLibraryActivator plugin;

	/**
	 * The constructor.
	 */
	public TypeLibraryActivator() {

	}

	/**
	 * {@inheritDoc}
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
	}

	/**
	 * {@inheritDoc}
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);

	}

	/**
	 * Returns the shared instance.
	 *
	 * @return the shared instance
	 */
	public static TypeLibraryActivator getDefault() {
		return plugin;
	}

	
	/**
	 * Returns the types defined in the given WSDL definition that are part of
	 * the global registry. Basically the API scan the WSDL and finds all the
	 * types and then query the Global registry with the type name and will add
	 * it to the returned map only if the registry has the type. In short it
	 * returns all the type library types in the WSDL definition.
	 *
	 * @param definition the definition
	 * @return the type library types
	 * @throws Exception the exception
	 */
	public static Map<LibraryType, XSDTypeDefinition> getTypeLibraryTypes(
			Definition definition) throws Exception {
		Map<LibraryType, XSDTypeDefinition> allSchemas = new ConcurrentHashMap<LibraryType, XSDTypeDefinition>();
		Types types = ((Types) definition.getTypes());
		// Do we have a schema already?
		for (Object objSchema : types.getSchemas()) {
			XSDSchema xsdSchema = (XSDSchema) objSchema;
			for (Object xsdSchemaContent : xsdSchema.getContents()) {
				if (xsdSchemaContent instanceof XSDTypeDefinition) {
					XSDTypeDefinition xsdTypeDefinition = (XSDTypeDefinition) xsdSchemaContent;
					// Annotations will take precedence over the XSD target
					// namespace
					String nameSpace = getNameSpace(xsdTypeDefinition);
					SOATypeRegistry typeRegistry = SOAGlobalRegistryAdapter.getInstance().getGlobalRegistry(); 
					LibraryType libraryType = typeRegistry.getType(
									new QName(nameSpace, xsdTypeDefinition
											.getName()));
					if (libraryType != null) {
						allSchemas.put(libraryType, xsdTypeDefinition);
					}
				}
			}
		}
		return allSchemas;
	}	
	
	/**
	 * Gets the name space.
	 *
	 * @param xsdTypeDefinition the xsd type definition
	 * @return the name space
	 * @throws SOABadParameterException the sOA bad parameter exception
	 */
	public static String getNameSpace(XSDTypeDefinition xsdTypeDefinition)
			throws SOABadParameterException {
		for (XSDAnnotation annotation : xsdTypeDefinition.getAnnotations()) {
			for (Element element : annotation.getApplicationInformation()) {
				NodeList children = element.getChildNodes();
				for (int i = 0; i < children.getLength(); i++) {
					Node node = children.item(i);
					if (node instanceof Element) {
						Element childElement = (Element) node;
						if (StringUtils.equals(childElement.getNodeName(),
								SOATypeLibraryConstants.TAG_TYPE_LIB)) {
							if (StringUtils
									.isEmpty(childElement
											.getAttribute(SOATypeLibraryConstants.ATTR_NMSPC))) {
								throw new SOABadParameterException(
										StringUtil.formatString(
												SOAMessages.ERR_NMSPC,
												xsdTypeDefinition.getName()));
							}
							return childElement
									.getAttribute(SOATypeLibraryConstants.ATTR_NMSPC);
						}
					}

				}
			}
		}
		return xsdTypeDefinition.getTargetNamespace();
	}
	
	/**
	 * Scans the schema and returns all the type imports pertaining to SOA
	 * protocol. Remember this will not return the normal XSD imports.ie the
	 * imports without the typelib: protocol. The registry is being queried for
	 * the type name before adding it to the returned map.
	 *
	 * @param xsdSchema the xsd schema
	 * @return the all type lib imports
	 * @throws Exception the exception
	 */
	public static Map<LibraryType, XSDSchemaDirective> getAllTypeLibImports(
			XSDSchema xsdSchema) throws Exception {
		Map<LibraryType, XSDSchemaDirective> typeImportsList = new ConcurrentHashMap<LibraryType, XSDSchemaDirective>();
		for (Object xsdContent : xsdSchema.getContents()) {
			if (xsdContent instanceof XSDSchemaDirective) {
				XSDSchemaDirective xsdSchemaDirective = (XSDSchemaDirective) xsdContent;
				String xsdSchemaLocation = xsdSchemaDirective
						.getSchemaLocation();
				String typeName = TypeLibraryUtil
						.getTypeNameFromProtocolString(xsdSchemaLocation);
				String importedTypeNameSpace = "";
				if (xsdSchemaDirective instanceof XSDInclude) {
					if (xsdSchemaDirective.getResolvedSchema() != null) {
						importedTypeNameSpace = ((XSDInclude)xsdSchemaDirective).getResolvedSchema().getTargetNamespace();
					} else {
						importedTypeNameSpace = xsdSchema.getTargetNamespace();
					}
				} else if (xsdSchemaDirective instanceof XSDImport) {
					importedTypeNameSpace = ((XSDImport) xsdSchemaDirective)
							.getNamespace();
				}
				if (!StringUtils.isEmpty(typeName)) {
					SOATypeRegistry typeRegistry = SOAGlobalRegistryAdapter.getInstance().getGlobalRegistry();
					LibraryType libType = typeRegistry.getType(new QName(importedTypeNameSpace,
									typeName));
					if (libType != null) {
						typeImportsList.put(libType,
								xsdSchemaDirective);
					}
				}
			}
		}
		return typeImportsList;
	}

	
}
