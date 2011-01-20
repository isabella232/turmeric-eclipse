/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/

package org.ebayopensource.turmeric.eclipse.maven.core.repositorysystem;

import java.util.List;

import org.apache.maven.repository.metadata.ArtifactMetadata;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.ISOAOrganizationProvider;
import org.ebayopensource.turmeric.eclipse.resources.constants.SOAProjectConstants.SupportedProjectType;

/**
 * @author yayu
 *
 */
public interface IMavenOrganizationProvider extends ISOAOrganizationProvider {
	
	/**
	 * 
	 * @param projectType
	 * @return the parent POM instance of null if no parent POM
	 */
	public ArtifactMetadata getParentPom(SupportedProjectType projectType);
	
	/**
	 * Get the Maven group ID for the given project type
	 * @param projectType
	 * @return
	 */
	public String getProjectGroupId(SupportedProjectType projectType);
	
	/**
	 * Get Maven group IDs for all supported project type
	 * @return
	 */
	public List<String> getAllProjectTypeGroupIds();

}
