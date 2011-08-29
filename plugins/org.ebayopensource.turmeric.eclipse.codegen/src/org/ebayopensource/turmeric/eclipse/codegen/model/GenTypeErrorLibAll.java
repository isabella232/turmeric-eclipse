/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.codegen.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.ebayopensource.turmeric.eclipse.core.resources.constants.SOAProjectConstants;
import org.ebayopensource.turmeric.eclipse.repositorysystem.model.BaseCodeGenModel;


// TODO: Auto-generated Javadoc
/**
 * The Class GenTypeErrorLibAll.
 *
 * @author yayu
 */
public class GenTypeErrorLibAll extends BaseCodeGenModel {
	
	/** The error library name. */
	private String errorLibraryName;
	
	/** The domains. */
	private final Collection<String> domains = new ArrayList<String>();

	/**
	 * Instantiates a new gen type error lib all.
	 */
	public GenTypeErrorLibAll() {
		super();
		this.setGenType(GENTYPE_ERROR_LIB_ALL);
	}

	/**
	 * Instantiates a new gen type error lib all.
	 *
	 * @param projectRoot the project root
	 * @param destination the destination
	 */
	public GenTypeErrorLibAll(String projectRoot, 
			String destination) {
		this();
		this.setProjectRoot(projectRoot);
		this.setDestination(destination);
	}

	/**
	 * Gets the error library name.
	 *
	 * @return the error library name
	 */
	public String getErrorLibraryName() {
		return errorLibraryName;
	}

	/**
	 * Sets the error library name.
	 *
	 * @param errorLibraryName the new error library name
	 */
	public void setErrorLibraryName(String errorLibraryName) {
		this.errorLibraryName = errorLibraryName;
	}

	/**
	 * Gets the domains.
	 *
	 * @return the domains
	 */
	public Collection<String> getDomains() {
		return domains;
	}

	/**
	 * Adds the domain.
	 *
	 * @param domain the domain
	 * @return true, if successful
	 * @see java.util.Collection#add(java.lang.Object)
	 */
	public boolean addDomain(String domain) {
		return domains.add(domain);
	}
	
	/**
	 * Adds the domains.
	 *
	 * @param domains the domains
	 * @return true, if successful
	 */
	public boolean addDomains(Collection<String> domains) {
		return this.domains.addAll(domains);
	}

	/* (non-Javadoc)
	 * @see org.ebayopensource.turmeric.eclipse.codegen.model.BaseCodeGenModel#getCodeGenOptions()
	 */
	@Override
	public Map<String, String> getCodeGenOptions() {
		final Map<String, String> result = super.getCodeGenOptions();
		if (domains.isEmpty() == false) {
			final String data = StringUtils.join(domains, 
					SOAProjectConstants.DELIMITER_COMMA);
			result.put(PARAM_DOMAIN, data);
		}
		if (StringUtils.isNotBlank(errorLibraryName)) {
			result.put(PARAM_ERROR_LIBRARY_NAME, errorLibraryName);
		}
		return result;
	}

}
