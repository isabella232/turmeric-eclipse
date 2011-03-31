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
 * @author smathew
 * 
 * SOA Type Lib related metadata
 */
public class SOATypeLibraryMetadata {

	private String name;
	private String functionDomain;
	private String version;
	private String category;

	public static SOATypeLibraryMetadata create(String name, String category,
			String version) {
		SOATypeLibraryMetadata soaTypeLibraryMetadata = new SOATypeLibraryMetadata();
		soaTypeLibraryMetadata.setName(name);
		soaTypeLibraryMetadata.setVersion(version);
		soaTypeLibraryMetadata.setCategory(category);
		return soaTypeLibraryMetadata;
	}

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

	public String getFunctionDomain() {
		return functionDomain;
	}

	public void setFunctionDomain(String functionDomain) {
		this.functionDomain = functionDomain;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

}
