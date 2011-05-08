/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.typelibrary.resources.model;

import org.ebayopensource.turmeric.eclipse.core.model.typelibrary.TypeLibraryParamModel;

/**
 * The Class SOATypeLibraryMetadata.
 *
 * @author smathew
 * 
 * SOA Type Lib related metadata
 */
public class SOATypeLibraryMetadata {

	private String name;
	private String functionDomain;
	private String version;
	private String category;

	/**
	 * Creates the.
	 *
	 * @param name the name
	 * @param category the category
	 * @param version the version
	 * @return the sOA type library metadata
	 */
	public static SOATypeLibraryMetadata create(String name, String category,
			String version) {
		SOATypeLibraryMetadata soaTypeLibraryMetadata = new SOATypeLibraryMetadata();
		soaTypeLibraryMetadata.setName(name);
		soaTypeLibraryMetadata.setVersion(version);
		soaTypeLibraryMetadata.setCategory(category);
		return soaTypeLibraryMetadata;
	}

	/**
	 * Creates the.
	 *
	 * @param model the model
	 * @return the sOA type library metadata
	 */
	public static SOATypeLibraryMetadata create(TypeLibraryParamModel model) {
		SOATypeLibraryMetadata soaTypeLibraryMetadata = new SOATypeLibraryMetadata();
		soaTypeLibraryMetadata.setName(model.getTypeLibraryName());
		soaTypeLibraryMetadata.setFunctionDomain(model.getFunctionDomain());
		soaTypeLibraryMetadata.setVersion(model.getVersion());
		soaTypeLibraryMetadata.setCategory(model.getCategory());
		return soaTypeLibraryMetadata;
	}

	private SOATypeLibraryMetadata() {
		super();
	}

	/**
	 * Gets the function domain.
	 *
	 * @return the function domain
	 */
	public String getFunctionDomain() {
		return functionDomain;
	}

	/**
	 * Sets the function domain.
	 *
	 * @param functionDomain the new function domain
	 */
	public void setFunctionDomain(String functionDomain) {
		this.functionDomain = functionDomain;
	}

	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name.
	 *
	 * @param name the new name
	 */
	public void setName(String name) {
		this.name = name;
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
	 * Gets the category.
	 *
	 * @return the category
	 */
	public String getCategory() {
		return category;
	}

	/**
	 * Sets the category.
	 *
	 * @param category the new category
	 */
	public void setCategory(String category) {
		this.category = category;
	}

}
