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
import org.ebayopensource.turmeric.eclipse.resources.model.ProjectInfo;
import org.ebayopensource.turmeric.eclipse.utils.lang.StringUtil;


/**
 * @author yayu
 * @since 1.0.0
 */
public class MavenProjectInfo extends ProjectInfo implements IMavenArtifact {
	private String groupID;
	//this field is used for the impl project, which is the group id of the interface projects.
	private String serviceGroupID;
	
	public MavenProjectInfo(String groupID, String name, String version, 
			String serviceLayer, String type) {
		super(name, version, "", serviceLayer, type);
		this.groupID = groupID;
	}

	public MavenProjectInfo(String groupID, String name, String version, 
			String serviceLayer) {
		super(name, version, "", serviceLayer);
		this.groupID = groupID;
	}

	public MavenProjectInfo(String groupID, String name, String version) {
		super(name, version, "", "", AssetInfo.TYPE_PROJECT);
		this.groupID = groupID;
	}
	
	public String getServiceGroupID() {
		return serviceGroupID;
	}

	public void setServiceGroupID(String serviceGropuID) {
		this.serviceGroupID = serviceGropuID;
	}

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
		return getName();
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

	public static MavenProjectInfo translateToEBoxProjectInfo(final String groupID, final ProjectInfo projectInfo) {
		if (projectInfo != null) {
			return new MavenProjectInfo(groupID, projectInfo.getName(), projectInfo.getVersion(),  
					projectInfo.getServiceLayer(), projectInfo.getType());
		}
		return null;
	}
}
