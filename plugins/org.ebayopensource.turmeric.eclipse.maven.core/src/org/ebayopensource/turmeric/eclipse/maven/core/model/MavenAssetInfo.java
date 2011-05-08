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
package org.ebayopensource.turmeric.eclipse.maven.core.model;

import org.ebayopensource.turmeric.eclipse.core.resources.constants.SOAProjectConstants;
import org.ebayopensource.turmeric.eclipse.maven.core.utils.MavenCoreUtils;
import org.ebayopensource.turmeric.eclipse.resources.model.AssetInfo;
import org.ebayopensource.turmeric.eclipse.utils.lang.StringUtil;


/**
 * The Class MavenAssetInfo.
 *
 * @author yayu
 * @since 1.0.0
 */
public class MavenAssetInfo extends AssetInfo implements IMavenArtifact{
	private String groupID;
	
	/**
	 * Instantiates a new maven asset info.
	 *
	 * @param groupID the maven group id
	 * @param name the asset name
	 * @param version the version number
	 * @param dir the directory it is located
	 * @param serviceLayer the service layer
	 * @param type the asset type
	 */
	public MavenAssetInfo(String groupID, String name, String version, String dir,
			String serviceLayer, String type) {
		super(name, version, dir, serviceLayer, type);
		this.groupID = groupID;
	}
	

	/**
	 * {@inheritDoc}
	 */
	public String getGroupID() {
		return groupID;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setGroupID(String groupID) {
		this.groupID = groupID;
	}
	
	/* (non-Javadoc)
	 * @see org.ebayopensource.turmeric.eclipse.resources.model.AssetInfo#getShortDescription()
	 */
	@Override
	public String getShortDescription() {
		return StringUtil.toString(groupID, 
				SOAProjectConstants.DELIMITER_SEMICOLON, getName());
	}

	/* (non-Javadoc)
	 * @see org.ebayopensource.turmeric.eclipse.resources.model.AssetInfo#getDescription()
	 */
	@Override
	public String getDescription() {
		return MavenCoreUtils.translateLibraryName(groupID, getName(), getVersion());
	}

	/* (non-Javadoc)
	 * @see org.ebayopensource.turmeric.eclipse.resources.model.AssetInfo#toString()
	 */
	@Override
	public String toString() {
		return getDescription();
	}
	
	/* (non-Javadoc)
	 * @see org.ebayopensource.turmeric.eclipse.resources.model.AssetInfo#getUniqueID()
	 */
	@Override
	public String getUniqueID() {
		return getDescription();
	}

	/* (non-Javadoc)
	 * @see org.ebayopensource.turmeric.eclipse.resources.model.AssetInfo#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ ((groupID == null) ? 0 : groupID.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see org.ebayopensource.turmeric.eclipse.resources.model.AssetInfo#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (obj instanceof IMavenArtifact) {
			final IMavenArtifact other = (IMavenArtifact) obj;
			if (groupID == null) {
				if (other.getGroupID() != null)
					return false;
			} else if (!groupID.equals(other.getGroupID()))
				return false;
		} else {
			return false;
		}
		return true;
	}
}
