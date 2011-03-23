/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.errorlibrary.properties.model;

import java.util.ArrayList;
import java.util.List;

import org.ebayopensource.turmeric.eclipse.errorlibrary.properties.resources.SOAMessages;
import org.ebayopensource.turmeric.eclipse.errorlibrary.views.ISOAErrDomain;
import org.ebayopensource.turmeric.eclipse.errorlibrary.views.ISOAError;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.PropertyDescriptor;


/**
 * @author yayu
 * @since 1.0.0
 */
public class PropertiesSOAError implements ISOAError, IPropertySource {
	private String category = null;
	private ISOAErrDomain domain = null;
	private String id = null;
	private String message = null;
	private String name = null;
	private String resolution = null;
	private String severity = null;
	private String subDomain = null;
	
//	private String[] categoryArray = new String[]{"APPLICATION", "SYSTEM", "REQUEST"};
//	private String[] severityArray = new String[]{"WARNING", "ERROR"};

	public PropertiesSOAError() {
		super();
	}

	public PropertiesSOAError(String category, ISOAErrDomain domain, String id,
			String message, String name, String resolution, String severity,
			String subDomain) {
		super();
		this.category = category;
		this.domain = domain;
		this.id = id;
		this.message = message;
		this.name = name;
		this.resolution = resolution;
		this.severity = severity;
		this.subDomain = subDomain;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public ISOAErrDomain getDomain() {
		return domain;
	}

	public void setDomain(ISOAErrDomain domain) {
		this.domain = domain;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getResolution() {
		return resolution;
	}

	public void setResolution(String resolution) {
		this.resolution = resolution;
	}

	public String getSeverity() {
		return severity;
	}

	public void setSeverity(String severity) {
		this.severity = severity;
	}

	public String getSubDomain() {
		return subDomain;
	}

	public void setSubDomain(String subDomain) {
		this.subDomain = subDomain;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((category == null) ? 0 : category.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((message == null) ? 0 : message.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result
				+ ((resolution == null) ? 0 : resolution.hashCode());
		result = prime * result
				+ ((severity == null) ? 0 : severity.hashCode());
		result = prime * result
				+ ((subDomain == null) ? 0 : subDomain.hashCode());
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
		PropertiesSOAError other = (PropertiesSOAError) obj;
		if (category == null) {
			if (other.category != null)
				return false;
		} else if (!category.equals(other.category))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (message == null) {
			if (other.message != null)
				return false;
		} else if (!message.equals(other.message))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (resolution == null) {
			if (other.resolution != null)
				return false;
		} else if (!resolution.equals(other.resolution))
			return false;
		if (severity == null) {
			if (other.severity != null)
				return false;
		} else if (!severity.equals(other.severity))
			return false;
		if (subDomain == null) {
			if (other.subDomain != null)
				return false;
		} else if (!subDomain.equals(other.subDomain))
			return false;
		return true;
	}

	public Object getEditableValue() {
		return this;
	}

	public IPropertyDescriptor[] getPropertyDescriptors() {
		List<IPropertyDescriptor> descriptors = new ArrayList<IPropertyDescriptor>();
		descriptors.add(new PropertyDescriptor("id", SOAMessages.PROP_KEY_ERROR_ID));
		descriptors.add(new PropertyDescriptor("name", SOAMessages.PROP_KEY_NAME));
		descriptors.add(new PropertyDescriptor("subdomain", SOAMessages.PROP_KEY_SUBDOMAIN));
		descriptors.add(new PropertyDescriptor("message", SOAMessages.PROP_KEY_MESSAGE));
		descriptors.add(new PropertyDescriptor("resolution", SOAMessages.PROP_KEY_RESOLUTION));
//		descriptors.add(new ComboBoxPropertyDescriptor("category", "Category",
//				categoryArray));
//		descriptors.add(new ComboBoxPropertyDescriptor("severity", "Severity",
//				severityArray));
		descriptors.add(new PropertyDescriptor("category", SOAMessages.PROP_KEY_CATEGORY));
		descriptors.add(new PropertyDescriptor("severity", SOAMessages.PROP_KEY_SEVERITY));
		return descriptors.toArray(new IPropertyDescriptor[0]);
	}

	public Object getPropertyValue(Object id) {
		if (id.equals("name")) {
			return this.name;
		} else if (id.equals("id")) {
			return this.id;
		} else if (id.equals("subdomain")) {
			return this.subDomain;
		} else if (id.equals("message")) {
			return this.message;
		} else if (id.equals("resolution")) {
			return this.resolution;
		} else if (id.equals("category")) {
//			return ArrayUtils.indexOf(categoryArray, this.category);
			return this.category != null ? this.category.toUpperCase() : "";
		} else if (id.equals("severity")) {
//			return ArrayUtils.indexOf(severityArray, this.severity);
			return this.severity != null ? this.severity.toUpperCase() : "";
		}
		return "";
	}

	public boolean isPropertySet(Object id) {
		return id.equals("name") || id.equals("id") || id.equals("subdomain")
		 || id.equals("message") || id.equals("resolution") || id.equals("category")
		  || id.equals("severity");
	}

	public void resetPropertyValue(Object id) {
		// TODO Auto-generated method stub
		
	}

	public void setPropertyValue(Object id, Object value) {
		// TODO Auto-generated method stub
		
	}

	public String getVersion() {
		return null;
	}
	

}
