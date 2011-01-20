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

import org.ebayopensource.turmeric.eclipse.maven.core.utils.MavenCoreUtils;
import org.ebayopensource.turmeric.eclipse.resources.constants.SOAProjectConstants;
import org.ebayopensource.turmeric.eclipse.resources.model.AssetInfo;
import org.ebayopensource.turmeric.eclipse.utils.lang.StringUtil;


/**
 * @author yayu
 * @since 1.0.0
 */
public class MavenAssetInfo extends AssetInfo implements IMavenArtifact{
	private String groupID;
	public MavenAssetInfo(String groupID, String name, String version, String dir,
			String serviceLayer, String type) {
		super(name, version, dir, serviceLayer, type);
		this.groupID = groupID;
	}
	
	/*public EBoxAssetInfo(String name, String version, String dir,
			String serviceLayer, String type) {
		super(name, version, dir, serviceLayer, type);
	}

	public EBoxAssetInfo(String name, String version, String dir,
			String type) {
		super(name, version, dir, type);
	}

	public EBoxAssetInfo(String name, String version, String dir) {
		super(name, version, "");
	}

	public EBoxAssetInfo(String name, String type) {
		super(name, type);
	}*/

	public String getGroupID() {
		return groupID;
	}

	public void setGroupID(String groupID) {
		this.groupID = groupID;
	}
	
	@Override
	public String getShortDescription() {
		return StringUtil.toString(groupID, 
				SOAProjectConstants.DELIMITER_SEMICOLON, getName());
	}

	@Override
	public String getDescription() {
		return MavenCoreUtils.translateLibraryName(groupID, getName(), getVersion());
	}

	@Override
	public String toString() {
		return getDescription();
	}
	
	@Override
	public String getUniqueID() {
		return getDescription();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ ((groupID == null) ? 0 : groupID.hashCode());
		return result;
	}

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
