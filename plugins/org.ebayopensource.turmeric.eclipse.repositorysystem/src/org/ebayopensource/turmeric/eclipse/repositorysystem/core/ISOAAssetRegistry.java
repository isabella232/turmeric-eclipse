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
 * The Interface ISOAAssetRegistry.
 *
 * @author smathew
 * 
 * This is the library and project regisrty. Each Repo system will have
 * different ways to calculate the available libraries in the Repo. But the
 * available projects calculation is always the same and hence the function-
 * getAllprojects is not spec'd here.
 */
public interface ISOAAssetRegistry {

	/**
	 * Gets the all libraries.
	 *
	 * @return the all libraries
	 * @throws Exception the exception
	 */
	public Set<AssetInfo> getAllLibraries() throws Exception;
	
	/**
	 * Gets the all available services.
	 *
	 * @return the list of all Turmeric service instances
	 * @throws Exception the exception
	 */
	public Set<? extends AssetInfo> getAllAvailableServices() throws Exception;
	
	/**
	 * Gets the dependencies.
	 *
	 * @param projectName the project name
	 * @return the list of dependencies for the given project
	 * @throws Exception the exception
	 */
	public List<AssetInfo> getDependencies(String projectName) throws Exception;
	
	/**
	 * Gets the project info.
	 *
	 * @param projectName the project name
	 * @return the information of the given project name
	 * @throws Exception the exception
	 */
	public ProjectInfo getProjectInfo(String projectName) throws Exception;
	
	/**
	 * Gets the sOA project.
	 *
	 * @param project the project
	 * @return the information of the given project
	 * @throws Exception the exception
	 */
	public ISOAProject getSOAProject(IProject project) throws Exception;
	
	/**
	 * Gets the project configuration file.
	 *
	 * @param project the project
	 * @return The project configuration file for the given project.
	 */
	public IFile getProjectConfigurationFile(IProject project);

	
	/**
	 * This should return the jar file location or even the directory location of the project.
	 * If its directory, it should return the root.
	 *
	 * @param projectName the project name
	 * @return the asset location
	 * @throws Exception the exception
	 */
	public String getAssetLocation(String projectName) throws Exception;
	
	/**
	 * Gets the asset location.
	 *
	 * @param libraryName the library name
	 * @return the location of the given asset
	 * @throws Exception the exception
	 */
	public String getAssetLocation(AssetInfo libraryName) throws Exception;
	
	/**
	 * Get the corresponding asset.
	 *
	 * @param project the project
	 * @return the asset
	 * @throws Exception the exception
	 */
	public IAssetInfo getAsset(IProject project) throws Exception;
	
	/**
	 * Get the instance of AssetInfo for the given project dependency name.
	 *
	 * @param project the project
	 * @param libraryName the library name
	 * @return the asset from project dependency
	 * @throws Exception the exception
	 */
	public AssetInfo getAssetFromProjectDependency(IProject project, String libraryName) throws Exception;

}
