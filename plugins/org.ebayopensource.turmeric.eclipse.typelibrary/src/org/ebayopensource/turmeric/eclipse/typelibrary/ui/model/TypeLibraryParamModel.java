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

	public String getFunctionDomain() {
		return functionDomain;
	}

	public void setFunctionDomain(String functionDomain) {
		this.functionDomain = functionDomain;
	}

	public String getNamespace() {
		return namespace;
	}

	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}

	public String getTypeLibraryName() {
		return typeLibraryName;
	}

	public void setTypeLibraryName(String typeLibraryName) {
		this.typeLibraryName = typeLibraryName;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getWorkspaceRoot() {
		return workspaceRoot;
	}

	public void setWorkspaceRoot(String workspaceRoot) {
		this.workspaceRoot = workspaceRoot;
	}
	
}
