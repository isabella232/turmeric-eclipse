/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.codegen.model;

import java.util.HashMap;
import java.util.Map;
import org.ebayopensource.turmeric.eclipse.repositorysystem.model.BaseCodeGenModel;

/**
 * Simple Model class for unit test class generation. Gentype used is "UnitTest"
 * 
 * @author smathew
 * 
 */
public class GenTypeUnitTest extends BaseCodeGenModel {
	private String genFolder; // -jdest Destination location for generated
								// Java source files
	private String clientName;

	/**
	 * Instantiates a new gen type unit test.
	 */
	public GenTypeUnitTest() {
		super();
		setGenType(GENTYPE_UNIT_TEST);
	}

	/* (non-Javadoc)
	 * @see org.ebayopensource.turmeric.eclipse.repositorysystem.model.BaseCodeGenModel#getCodeGenOptions()
	 */
	@Override
	public Map<String, String> getCodeGenOptions() {
		final Map<String, String> result = new HashMap<String, String>();
		result.put(PARAM_GENTYPE, getGenType());
		result.put(PARAM_NAMESPACE, getNamespace());
		result.put(PARAM_INTERFACE, getServiceInterface());
		result.put(PARAM_SERVICE_NAME, getServiceName());
		if (getServiceImplClassName() != null) {
			result.put(PARAM_SICN, getServiceImplClassName());
		}
		result.put(PARAM_SCV, getServiceVersion());
		result.put(PARAM_SRC, getSourceDirectory());
		result.put(PARAM_DEST, getDestination());
		result.put(PARAM_BIN, getOutputDirectory());
		result.put(PARAM_JDEST, getGenFolder());
		result.put(PARAM_CN, getClientName());
		return result;
	}

	/* (non-Javadoc)
	 * @see org.ebayopensource.turmeric.eclipse.repositorysystem.model.BaseCodeGenModel#getGenFolder()
	 */
	@Override
	public String getGenFolder() {
		return genFolder;
	}

	/* (non-Javadoc)
	 * @see org.ebayopensource.turmeric.eclipse.repositorysystem.model.BaseCodeGenModel#setGenFolder(java.lang.String)
	 */
	@Override
	public void setGenFolder(String genFolder) {
		this.genFolder = genFolder;
	}

	/**
	 * Gets the client name.
	 *
	 * @return the client name
	 */
	public String getClientName() {
		return clientName;
	}

	/**
	 * Sets the client name.
	 *
	 * @param clientName the new client name
	 */
	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

}
