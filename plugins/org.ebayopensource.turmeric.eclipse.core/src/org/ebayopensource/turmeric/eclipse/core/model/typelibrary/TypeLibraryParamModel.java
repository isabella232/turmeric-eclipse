/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.core.model.typelibrary;

/**
 * The Class TypeLibraryParamModel.
 *
 * @author smathew
 * UI Model for Type Library, Used from Create Type Library Wizard
 */
public class TypeLibraryParamModel {

	private String typeLibraryName;
	
	private String functionDomain;
	
	private String category;
	
	private String version;
	
	private String workspaceRoot;
	
	private String namespace;

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
	 * Gets the workspace root.
	 *
	 * @return the workspace root
	 */
	public String getWorkspaceRoot() {
		return workspaceRoot;
	}

	/**
	 * Sets the workspace root.
	 *
	 * @param workspaceRoot the new workspace root
	 */
	public void setWorkspaceRoot(String workspaceRoot) {
		this.workspaceRoot = workspaceRoot;
	}
	
}
