/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.typelibrary.ui.model;

import org.ebayopensource.turmeric.eclipse.config.repo.SOAConfigExtensionFactory.SOAXSDTemplateSubType;

/**
 * @author smathew
 *	
 *  UI Model for Type, Used from Create Type Wizard
 */
public class TypeParamModel {
	
	protected String typeName;
	
	protected String typeLibraryName;
	
	protected String version;
	
	protected String namespace;
	
	protected Object baseType;

	protected SOAXSDTemplateSubType templateCategory;
	
	protected String templateName;
	
	protected String description;

	public SOAXSDTemplateSubType getTemplateCategory() {
		return templateCategory;
	}

	public void setTemplateCategory(SOAXSDTemplateSubType templateCategory) {
		this.templateCategory = templateCategory;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public String getTypeLibraryName() {
		return typeLibraryName;
	}

	public void setTypeLibraryName(String typeLibraryName) {
		this.typeLibraryName = typeLibraryName;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public Object getBaseType() {
		return baseType;
	}

	public void setBaseType(Object baseType) {
		this.baseType = baseType;
	}

	public String getNamespace() {
		return namespace;
	}

	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}
	
	public String getTemplateName() {
		return templateName;
	}

	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}	
}
