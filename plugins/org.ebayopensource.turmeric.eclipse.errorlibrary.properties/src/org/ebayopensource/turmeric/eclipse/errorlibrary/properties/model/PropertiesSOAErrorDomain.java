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
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.ebayopensource.turmeric.eclipse.errorlibrary.properties.resources.SOAMessages;
import org.ebayopensource.turmeric.eclipse.errorlibrary.views.ISOAErrDomain;
import org.ebayopensource.turmeric.eclipse.errorlibrary.views.ISOAErrLibrary;
import org.ebayopensource.turmeric.eclipse.errorlibrary.views.ISOAError;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.PropertyDescriptor;


/**
 * @author yayu
 * @since 1.0.0
 */
public class PropertiesSOAErrorDomain implements ISOAErrDomain, IPropertySource {
	private final Set<ISOAError> errors = new LinkedHashSet<ISOAError>();
	private ISOAErrLibrary library = null;
	private String organization = null;
	private String name = null;
	private String version = null;
	private String packageName = null;
	
	/**
	 * 
	 */
	public PropertiesSOAErrorDomain() {
		super();
	}

	public PropertiesSOAErrorDomain(ISOAErrLibrary library,
			String organization, String name, String version) {
		super();
		this.library = library;
		if (this.library instanceof PropertiesSOAErrorLibrary) {
			((PropertiesSOAErrorLibrary)this.library).addErrorDomain(this);
		}
		this.organization = organization;
		this.name = name;
		this.version = version;
	}

	public ISOAErrLibrary getLibrary() {
		return library;
	}

	public void setLibrary(ISOAErrLibrary library) {
		this.library = library;
	}

	public String getOrganization() {
		return organization;
	}

	public void setOrganization(String organization) {
		this.organization = organization;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public Set<ISOAError> getErrors() {
		return errors;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((errors == null) ? 0 : errors.hashCode());
		result = prime * result + ((library == null) ? 0 : library.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result
				+ ((organization == null) ? 0 : organization.hashCode());
		result = prime * result
				+ ((packageName == null) ? 0 : packageName.hashCode());
		result = prime * result + ((version == null) ? 0 : version.hashCode());
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
		PropertiesSOAErrorDomain other = (PropertiesSOAErrorDomain) obj;
		if (errors == null) {
			if (other.errors != null)
				return false;
		} else if (!errors.equals(other.errors))
			return false;
		if (library == null) {
			if (other.library != null)
				return false;
		} else if (!library.equals(other.library))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (organization == null) {
			if (other.organization != null)
				return false;
		} else if (!organization.equals(other.organization))
			return false;
		if (packageName == null) {
			if (other.packageName != null)
				return false;
		} else if (!packageName.equals(other.packageName))
			return false;
		if (version == null) {
			if (other.version != null)
				return false;
		} else if (!version.equals(other.version))
			return false;
		return true;
	}

	public Object getEditableValue() {
		return this;
	}

	public IPropertyDescriptor[] getPropertyDescriptors() {
		List<IPropertyDescriptor> descriptors = new ArrayList<IPropertyDescriptor>();
		IPropertyDescriptor descriptor = new PropertyDescriptor("name", "Name");
		
		descriptors.add(descriptor);
		descriptors.add(new PropertyDescriptor("version", SOAMessages.PROP_KEY_VERSION));
		descriptors.add(new PropertyDescriptor("organization", SOAMessages.PROP_KEY_ORG));
		descriptors.add(new PropertyDescriptor("package", SOAMessages.PROP_KEY_PACKAGENAME));
		return descriptors.toArray(new IPropertyDescriptor[0]);
	}

	public Object getPropertyValue(Object id) {
		if (id.equals("name")) {
			return this.name;
		} else if (id.equals("version")) {
			return this.version;
		} else if (id.equals("organization")) {
			return this.organization;
		} else if (id.equals("package")) {
			return this.packageName;
		}
		return "";
	}

	public boolean isPropertySet(Object id) {
		return id.equals("name") || id.equals("version") || id.equals("organization");
	}

	public void resetPropertyValue(Object id) {
		// TODO Auto-generated method stub
		
	}

	public void setPropertyValue(Object id, Object value) {
		// TODO Auto-generated method stub
		
	}

	public void addError(ISOAError error) {
		errors.add(error);
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}


}
