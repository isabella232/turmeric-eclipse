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
	
	/**
	 * Constructor.
	 * 
	 * @param groupID the maven group id
	 * @param name the project name
	 * @param version maven version
	 * @param serviceLayer the servicelay 
	 * @param type the type
	 */
	public MavenProjectInfo(String groupID, String name, String version, 
			String serviceLayer, String type) {
		super(name, version, "", serviceLayer, type);
		this.groupID = groupID;
	}

	
	/**
	 * Constructor.
	 * 
	 * @param groupID the group id
	 * @param name the artifact name
	 * @param version the version
	 * @param serviceLayer the service layer.
	 */
	public MavenProjectInfo(String groupID, String name, String version, 
			String serviceLayer) {
		super(name, version, "", serviceLayer);
		this.groupID = groupID;
	}

	/**
	 * Constructor.
	 * @param groupID the maven group id
	 * @param name the artifact name
	 * @param version the version
	 */
	public MavenProjectInfo(String groupID, String name, String version) {
		super(name, version, "", "", AssetInfo.TYPE_PROJECT);
		this.groupID = groupID;
	}
	
	/**
	 * 
	 * @return the service group id
	 */
	public String getServiceGroupID() {
		return serviceGroupID;
	}

	/**
	 * 
	 * @param serviceGropuID the service group id
	 */
	public void setServiceGroupID(String serviceGropuID) {
		this.serviceGroupID = serviceGropuID;
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

	/**
	 * 
	 * @param groupID the group id
	 * @param projectInfo the ProjectInfo
	 * @return the maven project info
	 */
	public static MavenProjectInfo translateToEBoxProjectInfo(final String groupID, final ProjectInfo projectInfo) {
		if (projectInfo != null) {
			return new MavenProjectInfo(groupID, projectInfo.getName(), projectInfo.getVersion(),  
					projectInfo.getServiceLayer(), projectInfo.getType());
		}
		return null;
	}
}
