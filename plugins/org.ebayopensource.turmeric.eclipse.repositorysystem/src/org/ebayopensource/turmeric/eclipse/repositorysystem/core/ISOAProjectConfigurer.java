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
 * @author smathew
 * 
 * This is a confusing interface. Keeping it here since it was there already.
 * Might have to revisit and see if its common for all repos. And if not,
 * probably this will go out to the respective repo.
 */
public interface ISOAProjectConfigurer {

	/**
	 * Make appropriate projet nature changes for the underlying repo system.
	 * @param project
	 * @param projectNature
	 * @return whether need to actuall do the configuration
	 * @throws CoreException
	 */
	public boolean configureProjectNature(IProject project, 
			IProjectNature projectNature) throws CoreException;

	/**
	 * This method will return the path relative to the Repository Root of the
	 * given resource's parent if it is located in a subfolder of the Repository
	 * Root. This method returns null if the location is outside the Repository
	 * Root.
	 */
	public IPath getRepositoryPath(final IPath projectLocation);

	/**
	 * @param intfProject
	 * @param implProject
	 * @param monitor
	 * @throws Exception
	 */
	public void initializeProject(SOAIntfProject intfProject,SOAImplProject implProject, IProgressMonitor monitor) throws Exception;
	
	/**
	 * @param intfProject
	 * @param monitor
	 * @throws Exception
	 */
	public void initializeProject(SOAIntfProject intfProject, IProgressMonitor monitor) throws Exception;
	
	/**
	 * @param implProject
	 * @param monitor
	 * @throws Exception
	 */
	public void initializeProject(SOAImplProject implProject, IProgressMonitor monitor) throws Exception;
	
	/**
	 * @param consumerProject
	 * @param serviceVersion The version of the consuming service
	 * @param convertingJavaProject whether it is converting Java project, 
	 * and thus no need to create platform specific metadata if already exist
	 * @param monitor
	 * @throws Exception
	 */
	public void initializeProject(SOAConsumerProject consumerProject, String serviceVersion, 
			boolean convertingJavaProject, IProgressMonitor monitor) throws Exception;
	
	/**
	 * @param typeLibProject
	 * @param version
	 * @param monitor
	 * @throws Exception
	 */
	public void initializeTypeLibProject(SOABaseProject typeLibProject, String version, IProgressMonitor monitor) throws Exception;
	
	/**
	 * Initialize the Error Library project
	 * @param errorLibProject
	 * @param version
	 * @param monitor
	 * @throws Exception
	 */
	public void initializeErrorLibProject(SOABaseProject errorLibProject, String version, IProgressMonitor monitor) throws Exception;
	
	/**
	 * this is equivalent to updateProject(soaProject, true);
	 * @param soaProject
	 * @return
	 * @throws Exception
	 */
	public boolean updateProject(ISOAProject soaProject, IProgressMonitor monitor) throws Exception;
	
	/**
	 * @param soaProject
	 * @param updateClasspath
	 * @param monitor
	 * @return
	 * @throws Exception
	 */
	public boolean updateProject(ISOAProject soaProject, boolean updateClasspath, IProgressMonitor monitor) throws Exception;
	
	/**
	 * @param projectName
	 * @param dependentProjectName
	 * @param type - One of the Type in AssetInfo TYPE_PROJECT etc
	 * @param add = true, remove = false
	 * @return
	 * @throws Exception
	 */
	public boolean addDependency(String projectName, String dependentProjectName, String type,boolean addRemove, IProgressMonitor monitor) throws Exception;
	
	/**
	 * @param projectName
	 * @param dependentLibraries
	 * @param monitor
	 * @return
	 * @throws Exception
	 */
	public boolean addDependencies(String projectName, List<AssetInfo> dependentLibraries, IProgressMonitor monitor) throws Exception;
	
	public boolean removeDependencies(String projectName, List<AssetInfo> dependentLibraries, IProgressMonitor monitor) throws Exception;
	
	/**
	 * For adding a type library dependency
	 * @param projectName
	 * @param dependentProjectName
	 * @param type - One of the Type in AssetInfo TYPE_PROJECT etc
	 * @param add = true, remove = false
	 * @return
	 * @throws Exception
	 */
	public boolean addTypeLibraryDependency(String projectName, String dependentProjectName, String type,boolean addRemove, IProgressMonitor monitor) throws Exception;
	
	/**
	 * add the linked resources to the given SOA project
	 * @param project
	 */
	public void addProjectLinkedResources(SOABaseProject project);
	
	/**
	 * Add build system specific classpath container to the given project
	 * @param javaProject
	 * @param monitor
	 */
	public void addBuildSystemClasspathContainer(IJavaProject javaProject,
			IProgressMonitor monitor) throws CoreException;

	/**
	 * this is used to do something after project property version is changed: do a
	 * v3 build if it is v3 mode. Notify users before doing it. Library Catalog
	 * version need to be synchronized before doing a v3 build. It is
	 * cancelable.
	 * 
	 * @param soaIntfProject
	 *            the interface project that need to be updated
	 * @param oldVersion
	 *            service old version
	 * @param newVersion
	 *            service new version.
	 * @param forceBuild
	 *            just for the repos that need build after version change.
	 * @param monitor
	 *            the progress monitor
	 */
	public void postServiceVersionUpdated(SOAIntfProject soaIntfProject,
			String oldVersion, String newVersion, boolean silence, IProgressMonitor monitor)
			throws Exception;
	
}
