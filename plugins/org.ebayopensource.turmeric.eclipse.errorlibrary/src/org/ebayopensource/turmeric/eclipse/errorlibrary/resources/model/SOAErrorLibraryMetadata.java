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
 * @author yayu
 * @since 1.0.0
 *
 */
public class SOAErrorLibraryMetadata extends AbstractSOAMetadata {
	private String errorLibraryName;
	private String locale;
	
	/**
	 * 
	 */
	public SOAErrorLibraryMetadata() {
		super();
	}
	
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
		// TODO Auto-generated method stub
		return null;
	}

	public String getErrorLibraryName() {
		return errorLibraryName;
	}

	public void setErrorLibraryName(String errorLibraryName) {
		this.errorLibraryName = errorLibraryName;
	}

	public String getLocale() {
		return locale;
	}

	public void setLocale(String locale) {
		this.locale = locale;
	}

}
