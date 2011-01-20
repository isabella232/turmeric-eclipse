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
 * @author haozhou
 * 
 */
public class SOAErrorVO {

	private long id;

	private String category;

	private String subdomain;

	private String severity;

	private String errorGroups;

	private String name;

	public SOAErrorVO() {
	}

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

	@XmlAttribute(name = "id")
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@XmlAttribute(name = "category")
	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	@XmlAttribute(name = "subdomain")
	public String getSubdomain() {
		return subdomain;
	}

	public void setSubdomain(String subdomain) {
		this.subdomain = subdomain;
	}

	@XmlAttribute(name = "severity")
	public String getSeverity() {
		return severity;
	}

	public void setSeverity(String severity) {
		this.severity = severity;
	}

	@XmlAttribute(name = "errorGroups")
	public String getErrorGroups() {
		return errorGroups;
	}

	public void setErrorGroups(String errorGroups) {
		this.errorGroups = errorGroups;
	}

	@XmlAttribute(name = "name")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
