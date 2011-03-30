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

import org.apache.maven.model.Model;
import org.apache.maven.repository.metadata.ArtifactMetadata;
import org.ebayopensource.turmeric.eclipse.core.resources.constants.SOAProjectConstants.SupportedProjectType;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.ISOAOrganizationProvider;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IProgressMonitor;

/**
 * @author yayu
 *
 */
public interface IMavenOrganizationProvider extends ISOAOrganizationProvider {
	
	/**
	 * 
	 * @param projectType the project type
	 * @return the parent POM instance of null if no parent POM
	 */
	public ArtifactMetadata getParentPom(SupportedProjectType projectType);
	
	/**
	 * Get the Maven group ID for the given project type.
	 * 
	 * @param projectType the supported project type
	 * @return the project group id
	 */
	public String getProjectGroupId(SupportedProjectType projectType);
	
	/**
	 * Get Maven group IDs for all supported project type.
	 * 
	 * @return a list of maven group ids
	 */
	public List<String> getAllProjectTypeGroupIds();
	
	/**
	 * post action after adding the list of impl projects to the target web project.
	 * @param serviceImplProjects
	 * @param webProject
	 * @param monitor
	 * @throws Exception
	 */
	public void postAddingServiceToWebProjects(
			List<IProject> serviceImplProjects, IProject webProject,
			Model webProjectPom, IProgressMonitor monitor) throws Exception;

}
