/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.typelibrary.ui.model;

public class CommonTypeProp {

	private String schemaTemplate;

    private String typeName;

    private String targetNamespace;

    private String description;
    
    private String typeLibraryName;
    
    public CommonTypeProp(){
    	typeName = "";
    }

	public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getTargetNamespace() {
        return targetNamespace;
    }

    public void setTargetNamespace(String namespace) {
        this.targetNamespace = namespace;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSchemaTemplate() {
        return schemaTemplate;
    }

    public void setSchemaTemplate(String schemaTemplate) {
        this.schemaTemplate = schemaTemplate;
    }
    
    public String getTypeLibraryName() {
		return typeLibraryName;
	}

	public void setTypeLibraryName(String typeLibraryName) {
		this.typeLibraryName = typeLibraryName;
	}

    public void reset() {
        typeName = "";
        targetNamespace = null;
        description = null;
        schemaTemplate = null;
        typeLibraryName = null;
    }
}
