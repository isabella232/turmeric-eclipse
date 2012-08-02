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

import org.ebayopensource.turmeric.eclipse.resources.model.AssetInfo;
import org.ebayopensource.turmeric.eclipse.resources.model.ISOAProject;
import org.ebayopensource.turmeric.eclipse.resources.model.SOABaseProject;
import org.ebayopensource.turmeric.eclipse.resources.model.SOAConsumerProject;
import org.ebayopensource.turmeric.eclipse.resources.model.SOAImplProject;
import org.ebayopensource.turmeric.eclipse.resources.model.SOAIntfProject;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectNature;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.IJavaProject;


/**
 * The Interface ISOAProjectConfigurer.
 *
 * @author smathew
 * 
 * This is a confusing interface. Keeping it here since it was there already.
 * Might have to revisit and see if its common for all repos. And if not,
 * probably this will go out to the respective repo.
 */
public interface ISOAProjectConfigurer {

	/**
	 * Make appropriate projet nature changes for the underlying repo system.
	 *
	 * @param project the project
	 * @param projectNature the project nature
	 * @return whether need to actuall do the configuration
	 * @throws CoreException the core exception
	 */
	public boolean configureProjectNature(IProject project, 
			IProjectNature projectNature) throws CoreException;

	/**
	 * This method will return the path relative to the Repository Root of the
	 * given resource's parent if it is located in a subfolder of the Repository
	 * Root. This method returns null if the location is outside the Repository
	 * Root.
	 *
	 * @param projectLocation the project location
	 * @return the repository path
	 */
	public IPath getRepositoryPath(final IPath projectLocation);

	/**
	 * Initialize project.
	 *
	 * @param intfProject the intf project
	 * @param implProject the impl project
	 * @param monitor the monitor
	 * @throws Exception the exception
	 */
	public void initializeProject(SOAIntfProject intfProject,SOAImplProject implProject, IProgressMonitor monitor) throws Exception;
	
	/**
	 * Initialize project.
	 *
	 * @param intfProject the intf project
	 * @param monitor the monitor
	 * @throws Exception the exception
	 */
	public void initializeProject(SOAIntfProject intfProject, IProgressMonitor monitor) throws Exception;
	
	/**
	 * Initialize project.
	 *
	 * @param implProject the impl project
	 * @param monitor the monitor
	 * @throws Exception the exception
	 */
	public void initializeProject(SOAImplProject implProject, IProgressMonitor monitor) throws Exception;
	
	/**
	 * Initialize project.
	 *
	 * @param consumerProject the consumer project
	 * @param serviceVersion The version of the consuming service
	 * @param convertingJavaProject whether it is converting Java project,
	 * and thus no need to create platform specific metadata if already exist
	 * @param monitor the monitor
	 * @throws Exception the exception
	 */
	public void initializeProject(SOAConsumerProject consumerProject, String serviceVersion, 
			boolean convertingJavaProject, IProgressMonitor monitor) throws Exception;
	
	/**
	 * Initialize type lib project.
	 *
	 * @param typeLibProject the type lib project
	 * @param version the version
	 * @param monitor the monitor
	 * @throws Exception the exception
	 */
	public void initializeTypeLibProject(SOABaseProject typeLibProject, String version, IProgressMonitor monitor) throws Exception;
	
	/**
	 * Initialize the Error Library project.
	 *
	 * @param errorLibProject the error lib project
	 * @param version the version
	 * @param monitor the monitor
	 * @throws Exception the exception
	 */
	public void initializeErrorLibProject(SOABaseProject errorLibProject, String version, IProgressMonitor monitor) throws Exception;
	
	/**
	 * this is equivalent to updateProject(soaProject, true);.
	 *
	 * @param soaProject the soa project
	 * @param monitor the monitor
	 * @return true, if successful
	 * @throws Exception the exception
	 */
	public boolean updateProject(ISOAProject soaProject, IProgressMonitor monitor) throws Exception;
	
	/**
	 * Update project.
	 *
	 * @param soaProject the soa project
	 * @param updateClasspath the update classpath
	 * @param monitor the monitor
	 * @return true, if successful
	 * @throws Exception the exception
	 */
	public boolean updateProject(ISOAProject soaProject, boolean updateClasspath, IProgressMonitor monitor) throws Exception;
	
	/**
	 * Adds the dependency.
	 *
	 * @param projectName the project name
	 * @param dependentProjectName the dependent project name
	 * @param type - One of the Type in AssetInfo TYPE_PROJECT etc
	 * @param addRemove the add remove
	 * @param monitor the monitor
	 * @return true, if successful
	 * @throws Exception the exception
	 */
	public boolean addDependency(String projectName, String dependentProjectName, String type,boolean addRemove, IProgressMonitor monitor) throws Exception;
	
	/**
	 * Adds the dependencies.
	 *
	 * @param projectName the project name
	 * @param dependentLibraries the dependent libraries
	 * @param monitor the monitor
	 * @return true, if successful
	 * @throws Exception the exception
	 */
	public boolean addDependencies(String projectName, List<AssetInfo> dependentLibraries, IProgressMonitor monitor) throws Exception;
	
	/**
	 * Removes the dependencies.
	 *
	 * @param projectName the project name
	 * @param dependentLibraries the dependent libraries
	 * @param monitor the monitor
	 * @return true, if successful
	 * @throws Exception the exception
	 */
	public boolean removeDependencies(String projectName, List<AssetInfo> dependentLibraries, IProgressMonitor monitor) throws Exception;
	
	/**
	 * For adding a type library dependency.
	 *
	 * @param projectName the project name
	 * @param dependentProjectName the dependent project name
	 * @param type - One of the Type in AssetInfo TYPE_PROJECT etc
	 * @param addRemove the add remove
	 * @param monitor the monitor
	 * @return true, if successful
	 * @throws Exception the exception
	 */
	public boolean addTypeLibraryDependency(String projectName, String dependentProjectName, String type,boolean addRemove, IProgressMonitor monitor) throws Exception;
	
	/**
	 * add the linked resources to the given SOA project.
	 *
	 * @param project the project
	 */
	public void addProjectLinkedResources(SOABaseProject project);
	
	/**
	 * Add build system specific classpath container to the given project.
	 *
	 * @param javaProject the java project
	 * @param monitor the monitor
	 * @throws CoreException the core exception
	 */
	public void addBuildSystemClasspathContainer(IJavaProject javaProject,
			IProgressMonitor monitor) throws CoreException;

	/**
	 * this is used to do something after project property version is changed: do a
	 * v3 build if it is v3 mode. Notify users before doing it. Library Catalog
	 * version need to be synchronized before doing a v3 build. It is
	 * cancelable.
	 *
	 * @param soaIntfProject the interface project that need to be updated
	 * @param oldVersion service old version
	 * @param newVersion service new version.
	 * @param silence the silence
	 * @param monitor the progress monitor
	 * @throws Exception the exception
	 */
	public void postServiceVersionUpdated(SOAIntfProject soaIntfProject,
			String oldVersion, String newVersion, boolean silence, IProgressMonitor monitor)
			throws Exception;

	public String getClientGroupName(IProject project);
	
}
