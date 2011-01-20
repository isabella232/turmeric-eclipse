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
public class SimpleAssetModel {
	public static final String ASSET_TYPE_SERVICE = "Service";
	private String assetAdminName;
	private String assetName;
	private String assetType = ASSET_TYPE_SERVICE;
	private String assetVersion;
	private String domainName;
	private String namespacePart;

	/**
	 * 
	 */
	public SimpleAssetModel() {
		super();
	}

	public SimpleAssetModel(String assetName, String assetType,
			String domainName) {
		super();
		this.assetName = assetName;
		this.assetType = assetType;
		this.domainName = domainName;
	}

	public SimpleAssetModel(String assetAdminName, String assetName,
			String assetType, String domainName) {
		super();
		this.assetAdminName = assetAdminName;
		this.assetName = assetName;
		this.assetType = assetType;
		this.domainName = domainName;
	}

	public SimpleAssetModel(String assetAdminName, String assetName,
			String assetType, String domainName, String namespacePart) {
		super();
		this.assetAdminName = assetAdminName;
		this.assetName = assetName;
		this.assetType = assetType;
		this.domainName = domainName;
		this.namespacePart = namespacePart;
	}

	/**
	 * @return Null if the underlying service is pre2.4
	 */
	public String getAssetAdminName() {
		return assetAdminName;
	}

	public void setAssetAdminName(String assetAdminName) {
		this.assetAdminName = assetAdminName;
	}

	public String getAssetName() {
		return assetName;
	}

	public void setAssetName(String assetName) {
		this.assetName = assetName;
	}

	public String getAssetType() {
		return assetType;
	}

	public void setAssetType(String assetType) {
		this.assetType = assetType;
	}

	public String getAssetVersion() {
		return assetVersion;
	}

	public void setAssetVersion(String assetVersion) {
		this.assetVersion = assetVersion;
	}

	public String getDomainName() {
		return domainName;
	}

	public void setDomainName(String domainName) {
		this.domainName = domainName;
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
		int result = 17;
		result = prime * result
				+ ((assetAdminName == null) ? 0 : assetAdminName.hashCode());
		result = prime * result
				+ ((assetName == null) ? 0 : assetName.hashCode());
		result = prime * result
				+ ((assetType == null) ? 0 : assetType.hashCode());
		result = prime * result
				+ ((assetVersion == null) ? 0 : assetVersion.hashCode());
		result = prime * result
				+ ((domainName == null) ? 0 : domainName.hashCode());
		result = prime * result
				+ ((namespacePart == null) ? 0 : namespacePart.hashCode());
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
		SimpleAssetModel other = (SimpleAssetModel) obj;
		if (assetAdminName == null) {
			if (other.assetAdminName != null)
				return false;
		} else if (!assetAdminName.equals(other.assetAdminName))
			return false;
		if (assetName == null) {
			if (other.assetName != null)
				return false;
		} else if (!assetName.equals(other.assetName))
			return false;
		if (assetType == null) {
			if (other.assetType != null)
				return false;
		} else if (!assetType.equals(other.assetType))
			return false;
		if (assetVersion == null) {
			if (other.assetVersion != null)
				return false;
		} else if (!assetVersion.equals(other.assetVersion))
			return false;
		if (domainName == null) {
			if (other.domainName != null)
				return false;
		} else if (!domainName.equals(other.domainName))
			return false;
		if (namespacePart == null) {
			if (other.namespacePart != null)
				return false;
		} else if (!namespacePart.equals(other.namespacePart))
			return false;
		return true;
	}

	public String toString() {
		StringBuffer buf = new StringBuffer();
		buf.append("Asset Name:");
		buf.append(assetName);
		if (assetAdminName != null) {
			buf.append(", Asset Admin Name:");
			buf.append(assetAdminName);
		}
		buf.append(", Asset Type: ");
		buf.append(assetType);
		if (assetVersion != null) {
			buf.append(", Asset Version:");
			buf.append(assetVersion);
		}
		if (domainName != null) {
			buf.append(", Domain Name:");
			buf.append(domainName);
		}
		return buf.toString();
	}

}
