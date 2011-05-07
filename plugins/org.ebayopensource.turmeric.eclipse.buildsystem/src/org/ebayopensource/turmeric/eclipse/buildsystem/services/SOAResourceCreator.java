/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.buildsystem.services;

import java.io.IOException;

import org.ebayopensource.turmeric.eclipse.buildsystem.utils.ProjectPropertiesFileUtil;
import org.ebayopensource.turmeric.eclipse.resources.model.SOABaseProject;
import org.ebayopensource.turmeric.eclipse.resources.model.SOAConsumerProject;
import org.ebayopensource.turmeric.eclipse.resources.model.SOAImplProject;
import org.ebayopensource.turmeric.eclipse.resources.model.SOAIntfProject;
import org.ebayopensource.turmeric.eclipse.resources.model.SOAProjectEclipseMetadata;
import org.ebayopensource.turmeric.eclipse.utils.plugin.WorkspaceUtil;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;


/**
 * Creates the underlying resources. like project,property files and
 * directories. There is a clear line between the artifacts generated and owned
 * by plugin and those owned by codegen component. This class creates the
 * artifacts owned and managed by plugin.
 * 
 * 
 * @author smathew
 */
public class SOAResourceCreator {

	/**
	 * Creates the project from the extracted information from the metadata
	 * passed. This is not a null safe method. Clients are expected to handle
	 * the expected.
	 *
	 * @param metadata -
	 * All SOA project models will have this metadata and for the
	 * same reason this utility is used across all projects
	 * @param monitor the monitor
	 * @return the i project
	 * @throws CoreException the core exception
	 */
	public static IProject createProject(SOAProjectEclipseMetadata metadata,
			IProgressMonitor monitor) throws CoreException {
		IProject project = WorkspaceUtil.createProject(metadata
				.getProjectName(), metadata.getWorkspaceLocation(), monitor);

		monitor.worked(2);
		return project;
	}

	/**
	 * Simple API to create the folders and sub folders for a given project.
	 *
	 * @param project -
	 * The project in which the folders are created.
	 * @param soaBaseProject -
	 * The model object holding the folder paths to be created.
	 * @param monitor the monitor
	 * @throws CoreException the core exception
	 */
	public static void createFolders(IProject project,
			SOABaseProject soaBaseProject, IProgressMonitor monitor)
			throws CoreException {
		// Source Directories
		WorkspaceUtil.createFolders(project, soaBaseProject
				.getSourceDirectoryNames(), monitor);
		// Internal Directories
		WorkspaceUtil.createFolders(project, soaBaseProject
				.getSourceSubFolders(), monitor);
		// Output Directories
		WorkspaceUtil.createFolders(project, SOABaseProject
				.getOutputDirectory(), monitor);
	}

	/**
	 * Creates the properties file for an interface project. This file is
	 * created, modified and maintained by plugin
	 *
	 * @param intfProject the intf project
	 * @param monitor the monitor
	 * @return the i file
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws CoreException the core exception
	 */
	public static IFile createPropertiesFile(SOAIntfProject intfProject, 
			IProgressMonitor monitor)
			throws IOException, CoreException {
		return ProjectPropertiesFileUtil.createPropsFile(intfProject, monitor);
	}

	/**
	 * Creates the properties file for an implementation project. This file is
	 * created, modified and maintained by plugin
	 *
	 * @param implProject the impl project
	 * @return the i file
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws CoreException the core exception
	 */
	public static IFile createPropertiesFile(SOAImplProject implProject)
			throws IOException, CoreException {
		return ProjectPropertiesFileUtil.createPropsFile(implProject);
	}

	/**
	 * Creates the properties file for a consumer project. This file is created,
	 * modified and maintained by plugin
	 *
	 * @param consumerProject the consumer project
	 * @param monitor the monitor
	 * @return the i file
	 * @throws Exception the exception
	 */
	public static IFile createPropertiesFile(SOAConsumerProject consumerProject, 
			IProgressMonitor monitor)
			throws Exception {
		return ProjectPropertiesFileUtil.createPropsFile(consumerProject, monitor);
	}
	
	/**
	 * Creates the consumer properties file for impl projects.
	 *
	 * @param project the project
	 * @param clientName the client name
	 * @param consumerId the consumer id
	 * @param monitor the monitor
	 * @return the i file
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws CoreException the core exception
	 */
	public static IFile createConsumerPropertiesFileForImplProjects(IProject project,
			String clientName, String consumerId, IProgressMonitor monitor) 
	throws IOException, CoreException {
		return ProjectPropertiesFileUtil.createPropsFileForImplProjects(project, 
				clientName, consumerId, monitor);
	}
}
