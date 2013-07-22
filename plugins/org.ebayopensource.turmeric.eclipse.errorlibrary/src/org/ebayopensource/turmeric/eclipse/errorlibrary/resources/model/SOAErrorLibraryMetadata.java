/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
/**
 * 
 */
package org.ebayopensource.turmeric.eclipse.errorlibrary.resources.model;

import org.ebayopensource.turmeric.eclipse.errorlibrary.ui.model.ErrorLibraryParamModel;
import org.ebayopensource.turmeric.eclipse.resources.model.AbstractSOAMetadata;


/**
 * The Class SOAErrorLibraryMetadata.
 *
 * @author yayu
 * @since 1.0.0
 */
public class SOAErrorLibraryMetadata extends AbstractSOAMetadata {
	private String errorLibraryName;
	private String locale;
	private String superVersion;
	
	/**
	 * Instantiates a new sOA error library metadata.
	 */
	public SOAErrorLibraryMetadata() {
		super();
	}
	
	/**
	 * Creates the.
	 *
	 * @param model the model
	 * @return the sOA error library metadata
	 */
	public static SOAErrorLibraryMetadata create(ErrorLibraryParamModel model) {
		SOAErrorLibraryMetadata soaErrorLibraryMetadata = new SOAErrorLibraryMetadata();
		soaErrorLibraryMetadata.setErrorLibraryName(model.getProjectName());
		soaErrorLibraryMetadata.setLocale(model.getLocale());
		return soaErrorLibraryMetadata;
	}

	/* (non-Javadoc)
	 * @see org.ebayopensource.turmeric.eclipse.resources.model.AbstractSOAMetadata#getMetadataFileName()
	 */
	@Override
	public String getMetadataFileName() {
		return null;
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
	 * Gets the locale.
	 *
	 * @return the locale
	 */
	public String getLocale() {
		return locale;
	}

	/**
	 * Sets the locale.
	 *
	 * @param locale the new locale
	 */
	public void setLocale(String locale) {
		this.locale = locale;
	}

	@Override
	public String getSuperVersion() {
		// TODO Auto-generated method stub
		return superVersion;
	}

	public void setSuperVersion(String version) {
		// TODO Auto-generated method stub
		superVersion=version;
	}

}
