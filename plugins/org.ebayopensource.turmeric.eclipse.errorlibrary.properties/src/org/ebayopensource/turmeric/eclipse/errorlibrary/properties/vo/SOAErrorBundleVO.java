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

/**
 * The Class SOAErrorBundleVO.
 */
@XmlRootElement(name = "ErrorBundle")
public class SOAErrorBundleVO {
	private String version;
	private String packageName;
	private String domain;
	private String organization;
	private String libraryName;

	private SOAErrorListVO list;

	/**
	 * Instantiates a new sOA error bundle vo.
	 */
	public SOAErrorBundleVO() {
		this.list = new SOAErrorListVO();
	}

	/**
	 * Instantiates a new sOA error bundle vo.
	 *
	 * @param packageName the package name
	 * @param domain the domain
	 * @param organization the organization
	 * @param libraryName the library name
	 * @param list the list
	 */
	public SOAErrorBundleVO(String packageName, String domain,
			String organization, String libraryName, SOAErrorListVO list) {
		super();
		this.packageName = packageName;
		this.domain = domain;
		this.organization = organization;
		this.libraryName = libraryName;
		this.list = list;
	}

	/**
	 * Gets the version.
	 *
	 * @return the version
	 */
	@XmlAttribute(name = "version")
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
	 * Gets the list.
	 *
	 * @return the list
	 */
	@XmlElement(name = "errorlist")
	public SOAErrorListVO getList() {
		return list;
	}

	/**
	 * Sets the list.
	 *
	 * @param list the new list
	 */
	public void setList(SOAErrorListVO list) {
		this.list = list;
	}

	/**
	 * Gets the package name.
	 *
	 * @return the package name
	 */
	@XmlAttribute(name = "packageName")
	public String getPackageName() {
		return packageName;
	}

	/**
	 * Sets the package name.
	 *
	 * @param packageName the new package name
	 */
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	/**
	 * Gets the domain.
	 *
	 * @return the domain
	 */
	@XmlAttribute(name = "domain")
	public String getDomain() {
		return domain;
	}

	/**
	 * Sets the domain.
	 *
	 * @param domain the new domain
	 */
	public void setDomain(String domain) {
		this.domain = domain;
	}

	/**
	 * Gets the organization.
	 *
	 * @return the organization
	 */
	@XmlAttribute(name = "organization")
	public String getOrganization() {
		return organization;
	}

	/**
	 * Sets the organization.
	 *
	 * @param organization the new organization
	 */
	public void setOrganization(String organization) {
		this.organization = organization;
	}

	/**
	 * Gets the library name.
	 *
	 * @return the library name
	 */
	@XmlAttribute(name = "error-library-name")
	public String getLibraryName() {
		return libraryName;
	}

	/**
	 * Sets the library name.
	 *
	 * @param libraryName the new library name
	 */
	public void setLibraryName(String libraryName) {
		this.libraryName = libraryName;
	}

}
