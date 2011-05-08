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

/**
 * The Class SOAErrorVO.
 *
 * @author haozhou
 */
public class SOAErrorVO {

	private long id;

	private String category;

	private String subdomain;

	private String severity;

	private String errorGroups;

	private String name;

	/**
	 * Instantiates a new sOA error vo.
	 */
	public SOAErrorVO() {
	}

	/**
	 * Instantiates a new sOA error vo.
	 *
	 * @param id the id
	 * @param category the category
	 * @param subdomain the subdomain
	 * @param severity the severity
	 * @param errorGroups the error groups
	 * @param name the name
	 */
	public SOAErrorVO(int id, String category, String subdomain,
			String severity, String errorGroups, String name) {
		super();
		this.id = id;
		this.category = category;
		this.subdomain = subdomain;
		this.severity = severity;
		this.errorGroups = errorGroups;
		this.name = name;
	}

	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	@XmlAttribute(name = "id")
	public long getId() {
		return id;
	}

	/**
	 * Sets the id.
	 *
	 * @param id the new id
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * Gets the category.
	 *
	 * @return the category
	 */
	@XmlAttribute(name = "category")
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
	 * Gets the subdomain.
	 *
	 * @return the subdomain
	 */
	@XmlAttribute(name = "subdomain")
	public String getSubdomain() {
		return subdomain;
	}

	/**
	 * Sets the subdomain.
	 *
	 * @param subdomain the new subdomain
	 */
	public void setSubdomain(String subdomain) {
		this.subdomain = subdomain;
	}

	/**
	 * Gets the severity.
	 *
	 * @return the severity
	 */
	@XmlAttribute(name = "severity")
	public String getSeverity() {
		return severity;
	}

	/**
	 * Sets the severity.
	 *
	 * @param severity the new severity
	 */
	public void setSeverity(String severity) {
		this.severity = severity;
	}

	/**
	 * Gets the error groups.
	 *
	 * @return the error groups
	 */
	@XmlAttribute(name = "errorGroups")
	public String getErrorGroups() {
		return errorGroups;
	}

	/**
	 * Sets the error groups.
	 *
	 * @param errorGroups the new error groups
	 */
	public void setErrorGroups(String errorGroups) {
		this.errorGroups = errorGroups;
	}

	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	@XmlAttribute(name = "name")
	public String getName() {
		return name;
	}

	/**
	 * Sets the name.
	 *
	 * @param name the new name
	 */
	public void setName(String name) {
		this.name = name;
	}

}
