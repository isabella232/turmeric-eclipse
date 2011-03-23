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
package org.ebayopensource.turmeric.eclipse.registry.models;

/**
 * @author yayu
 * @since 1.0.0
 */
public class SubmitAssetModel {
	private String adminName; //since 2.4.0
	private String serviceName;
	private String interfaceProjectPath;
	private String serviceVersion;
	private String serviceLayer;
	private String serviceDomain;
	private String serviceNamespace;
	private String serviceWsdlLocation;
	private String namespacePart;

	/**
	 * 
	 */
	public SubmitAssetModel() {
		super();
	}

	/**
	 * @return Null if the underlying service is pre2.4
	 */
	public String getAdminName() {
		return adminName;
	}

	public void setAdminName(String adminName) {
		this.adminName = adminName;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getInterfaceProjectPath() {
		return interfaceProjectPath;
	}

	public void setInterfaceProjectPath(String interfaceProjectPath) {
		this.interfaceProjectPath = interfaceProjectPath;
	}

	public String getServiceVersion() {
		return serviceVersion;
	}

	public void setServiceVersion(String serviceVersion) {
		this.serviceVersion = serviceVersion;
	}

	public String getServiceLayer() {
		return serviceLayer;
	}

	public void setServiceLayer(String serviceLayer) {
		this.serviceLayer = serviceLayer;
	}

	public String getServiceDomain() {
		return serviceDomain;
	}

	public void setServiceDomain(String serviceDomain) {
		this.serviceDomain = serviceDomain;
	}

	public String getServiceNamespace() {
		return serviceNamespace;
	}

	public void setServiceNamespace(String serviceNamespace) {
		this.serviceNamespace = serviceNamespace;
	}

	public String getServiceWsdlLocation() {
		return serviceWsdlLocation;
	}

	public void setServiceWsdlLocation(String serviceWsdlLocation) {
		this.serviceWsdlLocation = serviceWsdlLocation;
	}

	public String getNamespacePart() {
		return namespacePart;
	}

	public void setNamespacePart(String namespacePart) {
		this.namespacePart = namespacePart;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((adminName == null) ? 0 : adminName.hashCode());
		result = prime
				* result
				+ ((interfaceProjectPath == null) ? 0 : interfaceProjectPath
						.hashCode());
		result = prime * result
				+ ((namespacePart == null) ? 0 : namespacePart.hashCode());
		result = prime * result
				+ ((serviceDomain == null) ? 0 : serviceDomain.hashCode());
		result = prime * result
				+ ((serviceLayer == null) ? 0 : serviceLayer.hashCode());
		result = prime * result
				+ ((serviceName == null) ? 0 : serviceName.hashCode());
		result = prime
				* result
				+ ((serviceNamespace == null) ? 0 : serviceNamespace.hashCode());
		result = prime * result
				+ ((serviceVersion == null) ? 0 : serviceVersion.hashCode());
		result = prime
				* result
				+ ((serviceWsdlLocation == null) ? 0 : serviceWsdlLocation
						.hashCode());
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
		final SubmitAssetModel other = (SubmitAssetModel) obj;
		if (adminName == null) {
			if (other.adminName != null)
				return false;
		} else if (!adminName.equals(other.adminName))
			return false;
		if (interfaceProjectPath == null) {
			if (other.interfaceProjectPath != null)
				return false;
		} else if (!interfaceProjectPath.equals(other.interfaceProjectPath))
			return false;
		if (namespacePart == null) {
			if (other.namespacePart != null)
				return false;
		} else if (!namespacePart.equals(other.namespacePart))
			return false;
		if (serviceDomain == null) {
			if (other.serviceDomain != null)
				return false;
		} else if (!serviceDomain.equals(other.serviceDomain))
			return false;
		if (serviceLayer == null) {
			if (other.serviceLayer != null)
				return false;
		} else if (!serviceLayer.equals(other.serviceLayer))
			return false;
		if (serviceName == null) {
			if (other.serviceName != null)
				return false;
		} else if (!serviceName.equals(other.serviceName))
			return false;
		if (serviceNamespace == null) {
			if (other.serviceNamespace != null)
				return false;
		} else if (!serviceNamespace.equals(other.serviceNamespace))
			return false;
		if (serviceVersion == null) {
			if (other.serviceVersion != null)
				return false;
		} else if (!serviceVersion.equals(other.serviceVersion))
			return false;
		if (serviceWsdlLocation == null) {
			if (other.serviceWsdlLocation != null)
				return false;
		} else if (!serviceWsdlLocation.equals(other.serviceWsdlLocation))
			return false;
		return true;
	}
}
