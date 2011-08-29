/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.typelibrary.ui.wst;

import java.util.Iterator;

import org.apache.commons.lang.StringUtils;
import org.eclipse.xsd.XSDFactory;
import org.eclipse.xsd.XSDImport;
import org.eclipse.xsd.XSDInclude;
import org.eclipse.xsd.XSDSchema;
import org.eclipse.xsd.XSDSchemaDirective;

/**
 * The Class AddImportCommand.
 *
 * @author smathew
 */
public class AddImportCommand {

	private XSDSchema xsdSchema;
	private String schemaLocation;
	private XSDSchema resolvedSchema;

	/**
	 * Instantiates a new adds the import command.
	 *
	 * @param xsdSchema the xsd schema
	 * @param schemaLocation the schema location
	 * @param resolvedSchema the resolved schema
	 */
	public AddImportCommand(XSDSchema xsdSchema, String schemaLocation,
			XSDSchema resolvedSchema) {
		this.xsdSchema = xsdSchema;
		this.schemaLocation = schemaLocation;
		this.resolvedSchema = resolvedSchema;
	}

	/**
	 * Run.
	 */
	public void run() {
		XSDSchemaDirective xsdSchemaDirective = null;
		if (StringUtils.equals(xsdSchema.getTargetNamespace(), resolvedSchema
				.getTargetNamespace())) {
			xsdSchemaDirective = XSDFactory.eINSTANCE.createXSDInclude();
		} else {
			xsdSchemaDirective = XSDFactory.eINSTANCE.createXSDImport();
			((XSDImport) xsdSchemaDirective).setNamespace(resolvedSchema
					.getTargetNamespace());
		}
		xsdSchemaDirective.setSchemaLocation(schemaLocation);
		xsdSchemaDirective.setResolvedSchema(resolvedSchema);
		if (!isDuplicate(xsdSchemaDirective))
			xsdSchema.getContents().add(0, xsdSchemaDirective);
	}

	private boolean isDuplicate(XSDSchemaDirective xsdSchemaDirective) {
		try {
			boolean isInclude = xsdSchemaDirective instanceof XSDInclude;
			for (Iterator i = xsdSchema.getContents().iterator(); i.hasNext();) {
				Object directive = i.next();
				if (isInclude) {
					if (directive instanceof XSDInclude) {
						String newSchemaLocation = ((XSDInclude) xsdSchemaDirective)
								.getSchemaLocation();
						String oldSchemaLocation = ((XSDInclude) directive)
								.getSchemaLocation();
						if (StringUtils.equals(newSchemaLocation,
								oldSchemaLocation)) {
							return true;
						}
					}
				} else {
					if (directive instanceof XSDImport) {
						String newSchemaLocation = ((XSDImport) xsdSchemaDirective)
								.getSchemaLocation();
						String oldSchemaLocation = ((XSDImport) directive)
								.getSchemaLocation();
						String newNameSpace = ((XSDImport) xsdSchemaDirective)
								.getNamespace();
						String oldNameSpace = ((XSDImport) directive)
								.getNamespace();
						if (StringUtils.equals(newSchemaLocation,
								oldSchemaLocation)
								&& StringUtils.equals(newNameSpace,
										oldNameSpace)) {
							return true;
						}
					}
				}
			}

		} catch (Exception e) {
			// there could be some invalid imports/includes in the XSD
			// but still we would like our import logic to work
		}
		return false;

	}

}
