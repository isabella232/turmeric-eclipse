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
 * The Class SimpleAssetModel.
 *
 * @author yayu
 * @since 1.0.0
 */
public class SimpleAssetModel {
	
	/** The Constant ASSET_TYPE_SERVICE. */
	public static final String ASSET_TYPE_SERVICE = "Service";
	private String assetAdminName;
	private String assetName;
	private String assetType = ASSET_TYPE_SERVICE;
	private String assetVersion;
	private String domainName;
	private String namespacePart;

	/**
	 * Instantiates a new simple asset model.
	 */
	public SimpleAssetModel() {
		super();
	}

	/**
	 * Instantiates a new simple asset model.
	 *
	 * @param assetName the asset name
	 * @param assetType the asset type
	 * @param domainName the domain name
	 */
	public SimpleAssetModel(String assetName, String assetType,
			String domainName) {
		super();
		this.assetName = assetName;
		this.assetType = assetType;
		this.domainName = domainName;
	}

	/**
	 * Instantiates a new simple asset model.
	 *
	 * @param assetAdminName the asset admin name
	 * @param assetName the asset name
	 * @param assetType the asset type
	 * @param domainName the domain name
	 */
	public SimpleAssetModel(String assetAdminName, String assetName,
			String assetType, String domainName) {
		super();
		this.assetAdminName = assetAdminName;
		this.assetName = assetName;
		this.assetType = assetType;
		this.domainName = domainName;
	}

	/**
	 * Instantiates a new simple asset model.
	 *
	 * @param assetAdminName the asset admin name
	 * @param assetName the asset name
	 * @param assetType the asset type
	 * @param domainName the domain name
	 * @param namespacePart the namespace part
	 */
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
	 * Gets the asset admin name.
	 *
	 * @return Null if the underlying service is pre2.4
	 */
	public String getAssetAdminName() {
		return assetAdminName;
	}

	/**
	 * Sets the asset admin name.
	 *
	 * @param assetAdminName the new asset admin name
	 */
	public void setAssetAdminName(String assetAdminName) {
		this.assetAdminName = assetAdminName;
	}

	/**
	 * Gets the asset name.
	 *
	 * @return the asset name
	 */
	public String getAssetName() {
		return assetName;
	}

	/**
	 * Sets the asset name.
	 *
	 * @param assetName the new asset name
	 */
	public void setAssetName(String assetName) {
		this.assetName = assetName;
	}

	/**
	 * Gets the asset type.
	 *
	 * @return the asset type
	 */
	public String getAssetType() {
		return assetType;
	}

	/**
	 * Sets the asset type.
	 *
	 * @param assetType the new asset type
	 */
	public void setAssetType(String assetType) {
		this.assetType = assetType;
	}

	/**
	 * Gets the asset version.
	 *
	 * @return the asset version
	 */
	public String getAssetVersion() {
		return assetVersion;
	}

	/**
	 * Sets the asset version.
	 *
	 * @param assetVersion the new asset version
	 */
	public void setAssetVersion(String assetVersion) {
		this.assetVersion = assetVersion;
	}

	/**
	 * Gets the domain name.
	 *
	 * @return the domain name
	 */
	public String getDomainName() {
		return domainName;
	}

	/**
	 * Sets the domain name.
	 *
	 * @param domainName the new domain name
	 */
	public void setDomainName(String domainName) {
		this.domainName = domainName;
	}

	/**
	 * Gets the namespace part.
	 *
	 * @return the namespace part
	 */
	public String getNamespacePart() {
		return namespacePart;
	}

	/**
	 * Sets the namespace part.
	 *
	 * @param namespacePart the new namespace part
	 */
	public void setNamespacePart(String namespacePart) {
		this.namespacePart = namespacePart;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
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

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
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

	/**
	 * {@inheritDoc}
	 */
	@Override
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
