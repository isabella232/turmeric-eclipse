/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.typelibrary.ui.model;

/**
 * The Class CommonTypeProp.
 */
public class CommonTypeProp {

	private String schemaTemplate;

    private String typeName;

    private String targetNamespace;

    private String description;
    
    private String typeLibraryName;
    
    /**
     * Instantiates a new common type prop.
     */
    public CommonTypeProp(){
    	typeName = "";
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
     * Gets the target namespace.
     *
     * @return the target namespace
     */
    public String getTargetNamespace() {
        return targetNamespace;
    }

    /**
     * Sets the target namespace.
     *
     * @param namespace the new target namespace
     */
    public void setTargetNamespace(String namespace) {
        this.targetNamespace = namespace;
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

    /**
     * Gets the schema template.
     *
     * @return the schema template
     */
    public String getSchemaTemplate() {
        return schemaTemplate;
    }

    /**
     * Sets the schema template.
     *
     * @param schemaTemplate the new schema template
     */
    public void setSchemaTemplate(String schemaTemplate) {
        this.schemaTemplate = schemaTemplate;
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
     * Reset.
     */
    public void reset() {
        typeName = "";
        targetNamespace = null;
        description = null;
        schemaTemplate = null;
        typeLibraryName = null;
    }
}
