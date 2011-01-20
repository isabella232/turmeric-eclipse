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
package org.ebayopensource.turmeric.eclipse.errorlibrary.ui.model;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.ebayopensource.turmeric.eclipse.errorlibrary.utils.SOAErrorLibraryConstants;
import org.ebayopensource.turmeric.eclipse.resources.ui.model.BaseServiceParamModel;


/**
 * @author yayu
 * 
 */
public class DomainParamModel extends BaseServiceParamModel {
	
	private String packageName;
	private String errorLibrary;
	private String domain;
	private String organization;
	private String locale;
	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public DomainParamModel() {
		super();
	}

	public boolean validate() {
		return true;
	}

	public String getErrorLibrary() {
		return errorLibrary;
	}

	public void setErrorLibrary(String errorLibrary) {
		this.errorLibrary = errorLibrary;
	}

	public String getOrganization() {
		return organization;
	}

	public void setOrganization(String organization) {
		this.organization = organization;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public String getLocale() {
		return locale;
	}

	public void setLocale(String locale) {
		this.locale = locale;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((domain == null) ? 0 : domain.hashCode());
		result = prime * result
				+ ((errorLibrary == null) ? 0 : errorLibrary.hashCode());
		result = prime * result + ((locale == null) ? 0 : locale.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final DomainParamModel other = (DomainParamModel) obj;
		if (domain == null) {
			if (other.domain != null)
				return false;
		} else if (!domain.equals(other.domain))
			return false;
		if (errorLibrary == null) {
			if (other.errorLibrary != null)
				return false;
		} else if (!errorLibrary.equals(other.errorLibrary))
			return false;
		if (locale == null) {
			if (other.locale != null)
				return false;
		} else if (!locale.equals(other.locale))
			return false;
		return true;
	}

	public Map<String, String> getFreeMarkerData() {
		final Map<String, String> data = new ConcurrentHashMap<String, String>();
		data.put(SOAErrorLibraryConstants.DOMAIN, domain);
		data.put(SOAErrorLibraryConstants.TYPE_LIB, errorLibrary);
		data.put(SOAErrorLibraryConstants.ORGANIZATION, organization);
		return data;
	}
}
