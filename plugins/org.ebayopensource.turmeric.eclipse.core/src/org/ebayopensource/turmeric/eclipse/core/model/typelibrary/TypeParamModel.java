/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.core.model.typelibrary;

import org.ebayopensource.turmeric.eclipse.core.resources.constants.SOAXSDTemplateSubType;


/**
 * The Class TypeParamModel.
 *
 * @author smathew
 * 
 * UI Model for Type, Used from Create Type Wizard
 */
public class TypeParamModel {
	
	/** The type name. */
	protected String typeName;
	
	/** The type library name. */
	protected String typeLibraryName;
	
	/** The version. */
	protected String version;
	
	/** The namespace. */
	protected String namespace;
	
	/** The base type. */
	protected Object baseType;

	/** The template category. */
	protected SOAXSDTemplateSubType templateCategory;
	
	/** The template name. */
	protected String templateName;
	
	/** The description. */
	protected String description;

	/**
	 * Gets the template category.
	 *
	 * @return the template category
	 */
	public SOAXSDTemplateSubType getTemplateCategory() {
		return templateCategory;
	}

	/**
	 * Sets the template category.
	 *
	 * @param templateCategory the new template category
	 */
	public void setTemplateCategory(SOAXSDTemplateSubType templateCategory) {
		this.templateCategory = templateCategory;
	}

	/**
	 * Gets the type name.
	 *
	 * @return the type name
	 */
	public String getTypeName() {
		return typeName;
	}

	/**
	 * Sets the type name.
	 *
	 * @param typeName the new type name
	 */
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	/**
	 * Gets the type library name.
	 *
	 * @return the type library name
	 */
	public String getTypeLibraryName() {
		return typeLibraryName;
	}

	/**
	 * Sets the type library name.
	 *
	 * @param typeLibraryName the new type library name
	 */
	public void setTypeLibraryName(String typeLibraryName) {
		this.typeLibraryName = typeLibraryName;
	}

	/**
	 * Gets the version.
	 *
	 * @return the version
	 */
	public String getVersion() {
		return version;
	}

	/**
	 * Sets the version.
	 *
	 * @param version the new version
	 */
	public void setVersion(String version) {
		this.version = version;
	}

	/**
	 * Gets the base type.
	 *
	 * @return the base type
	 */
	public Object getBaseType() {
		return baseType;
	}

	/**
	 * Sets the base type.
	 *
	 * @param baseType the new base type
	 */
	public void setBaseType(Object baseType) {
		this.baseType = baseType;
	}

	/**
	 * Gets the namespace.
	 *
	 * @return the namespace
	 */
	public String getNamespace() {
		return namespace;
	}

	/**
	 * Sets the namespace.
	 *
	 * @param namespace the new namespace
	 */
	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}
	
	/**
	 * Gets the template name.
	 *
	 * @return the template name
	 */
	public String getTemplateName() {
		return templateName;
	}

	/**
	 * Sets the template name.
	 *
	 * @param templateName the new template name
	 */
	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}

	/**
	 * Gets the description.
	 *
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Sets the description.
	 *
	 * @param description the new description
	 */
	public void setDescription(String description) {
		this.description = description;
	}	
}
