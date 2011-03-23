/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.errorlibrary.properties.vo;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "ErrorBundle")
public class SOAErrorBundleVO {
	private String version;
	private String packageName;
	private String domain;
	private String organization;
	private String libraryName;

	private SOAErrorListVO list;

	public SOAErrorBundleVO() {
		this.list = new SOAErrorListVO();
	}

	public SOAErrorBundleVO(String packageName, String domain,
			String organization, String libraryName, SOAErrorListVO list) {
		super();
		this.packageName = packageName;
		this.domain = domain;
		this.organization = organization;
		this.libraryName = libraryName;
		this.list = list;
	}

	@XmlAttribute(name = "version")
	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	@XmlElement(name = "errorlist")
	public SOAErrorListVO getList() {
		return list;
	}

	public void setList(SOAErrorListVO list) {
		this.list = list;
	}

	@XmlAttribute(name = "packageName")
	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	@XmlAttribute(name = "domain")
	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	@XmlAttribute(name = "organization")
	public String getOrganization() {
		return organization;
	}

	public void setOrganization(String organization) {
		this.organization = organization;
	}

	@XmlAttribute(name = "error-library-name")
	public String getLibraryName() {
		return libraryName;
	}

	public void setLibraryName(String libraryName) {
		this.libraryName = libraryName;
	}

}
