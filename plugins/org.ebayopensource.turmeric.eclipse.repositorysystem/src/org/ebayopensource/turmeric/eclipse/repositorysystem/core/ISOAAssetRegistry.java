/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.repositorysystem.core;

import java.util.List;
import java.util.Set;

import org.ebayopensource.turmeric.eclipse.resources.model.AssetInfo;
import org.ebayopensource.turmeric.eclipse.resources.model.IAssetInfo;
import org.ebayopensource.turmeric.eclipse.resources.model.ISOAProject;
import org.ebayopensource.turmeric.eclipse.resources.model.ProjectInfo;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;


/**
 * @author smathew
 * 
 * This is the library and project regisrty. Each Repo system will have
 * different ways to calculate the available libraries in the Repo. But the
 * available projects calculation is always the same and hence the function-
 * getAllprojects is not spec'd here.
 */
public interface ISOAAssetRegistry {

	public Set<AssetInfo> getAllLibraries() throws Exception;
	
	/**
	 * @return the list of all Turmeric service instances
	 * @throws Exception
	 */
	public Set<? extends AssetInfo> getAllAvailableServices() throws Exception;
	
	/**
	 * @param projectName
	 * @return the list of dependencies for the given project
	 * @throws Exception
	 */
	public List<AssetInfo> getDependencies(String projectName) throws Exception;
	
	/**
	 * @param projectName
	 * @return the information of the given project name
	 * @throws Exception
	 */
	public ProjectInfo getProjectInfo(String projectName) throws Exception;
	
	/**
	 * @param project
	 * @return the information of the given project
	 * @throws Exception
	 */
	public ISOAProject getSOAProject(IProject project) throws Exception;
	
	/**
	 * @param project
	 * @return The project configuration file for the given project.
	 */
	public IFile getProjectConfigurationFile(IProject project);

	
	/**
	 * This should return the jar file location or even the directory location of the project.
	 * If its directory, it should return the root.
	 * @param projectName
	 * @return
	 */
	public String getAssetLocation(String projectName) throws Exception;
	
	/**
	 * @param libraryName
	 * @return the location of the given asset
	 * @throws Exception
	 */
	public String getAssetLocation(AssetInfo libraryName) throws Exception;
	
	/**
	 * Get the corresponding asset
	 * @param project
	 * @return
	 * @throws Exception
	 */
	public IAssetInfo getAsset(IProject project) throws Exception;
	
	/**
	 * Get the instance of AssetInfo for the given project dependency name
	 * @param project
	 * @param libraryName
	 * @return
	 * @throws Exception
	 */
	public AssetInfo getAssetFromProjectDependency(IProject project, String libraryName) throws Exception;

}
