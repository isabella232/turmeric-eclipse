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
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.PropertyDescriptor;


/**
 * @author yayu
 * @since 1.0.0
 */
public class PropertiesSOAErrorLibrary implements ISOAErrLibrary, IPropertySource {
	private final Set<ISOAErrDomain> domains = new LinkedHashSet<ISOAErrDomain>();
	private String name;
	private String version;

	/**
	 * 
	 */
	public PropertiesSOAErrorLibrary() {
		super();
	}

	public PropertiesSOAErrorLibrary(String name, String version) {
		super();
		this.name = name;
		this.version = version;
	}


	/* (non-Javadoc)
	 * @see org.ebayopensource.turmeric.eclipse.errorlibrary.views.ISOAErrLibrary#getDomains()
	 */
	public Set<ISOAErrDomain> getDomains() {
		return domains;
	}
	
	public boolean addErrorDomain(ISOAErrDomain domain) {
		return this.domains.add(domain);
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	/* (non-Javadoc)
	 * @see org.ebayopensource.turmeric.eclipse.errorlibrary.views.ISOAErrUIComp#getName()
	 */
	public String getName() {
		return name;
	}

	/* (non-Javadoc)
	 * @see org.ebayopensource.turmeric.eclipse.errorlibrary.views.ISOAErrUIComp#getVersion()
	 */
	public String getVersion() {
		return version;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		PropertiesSOAErrorLibrary other = (PropertiesSOAErrorLibrary) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (version == null) {
			if (other.version != null)
				return false;
		} else if (!version.equals(other.version))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "PropertiesSOAErrorLibrary [domains=" + domains + ", name="
				+ name + ", version=" + version + "]";
	}

	public Object getEditableValue() {
		return this;
	}

	public IPropertyDescriptor[] getPropertyDescriptors() {
		List<IPropertyDescriptor> descriptors = new ArrayList<IPropertyDescriptor>();
		descriptors.add(new PropertyDescriptor("name", SOAMessages.PROP_KEY_NAME));
		descriptors.add(new PropertyDescriptor("version", SOAMessages.PROP_KEY_VERSION));
		descriptors.add(new PropertyDescriptor("locale", SOAMessages.PROP_KEY_LOCALE));
		return descriptors.toArray(new IPropertyDescriptor[0]);
	}

	public Object getPropertyValue(Object id) {
		if (id.equals("name")) {
			return this.name;
		} else if (id.equals("version")) {
			return this.version;
		} else if (id.equals("locale")) {
			return "en";//FIXME get from local configuration
		}
		return "";
	}

	public boolean isPropertySet(Object id) {
		return id.equals("name") || id.equals("version");
	}

	public void resetPropertyValue(Object id) {
		// TODO Auto-generated method stub
		
	}

	public void setPropertyValue(Object id, Object value) {
		// TODO Auto-generated method stub
		
	}
}
