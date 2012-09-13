/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.buildsystem.utils;

import org.ebayopensource.turmeric.eclipse.repositorysystem.core.GlobalRepositorySystem;
import org.ebayopensource.turmeric.eclipse.resources.model.SOABaseProject;
import org.ebayopensource.turmeric.eclipse.utils.plugin.ProjectUtil;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.ClasspathContainerInitializer;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;


/**
 * Configures the project with necessary add on supports like JDT, SOA. Support
 * includes adding builders natures and the containers if required.
 * 
 * @author smathew
 * 
 * 
 */
public class BuildSystemUtil {
	//private static final SOALogger logger = SOALogger.getLogger();

	/**
	 * Adds all natures and containers that SOA needs to perform its code
	 * generation and management for a SOA project. As per JDT API specification we have to set
	 * the raw class path back to effect the new container addition.
	 *
	 * @param soaProject the soa project
	 * @param natureId the nature id
	 * @param monitor the monitor
	 * @throws CoreException the core exception
	 */
	public static void addSOASupport(SOABaseProject soaProject, String natureId,
			IProgressMonitor monitor) throws CoreException {
		// The last nature to contribute will have its builder at the start.
		// We need Service Natured builder at the start and then
		// the service builder
		
		ProjectUtil.addNature(soaProject.getProject(),
				monitor, 
				natureId);

		final IJavaProject javaProject = (IJavaProject) soaProject.getProject()
				.getNature(JavaCore.NATURE_ID);
		appendSOAClassPath(javaProject, monitor);
	}	

	/**
	 * Updates the SOA Container. Rather than update a better name for this API
	 * would be re-initialize. This basically fetches the associated container
	 * initializer and as the initializer to reinitialize the container, which
	 * in turn calls the container call back methods.
	 *
	 * @param project the project
	 * @throws CoreException the core exception
	 */
	public static void updateSOAClasspathContainer(final IProject project)
			throws CoreException {
		final IJavaProject javaProject = (IJavaProject) project
				.getNature(JavaCore.NATURE_ID);
		final String containerId = GlobalRepositorySystem.instanceOf()
		.getActiveRepositorySystem().getClasspathContainerID();
		final IPath containerPath = new Path(containerId);
		final ClasspathContainerInitializer containerInitializer = JavaCore
				.getClasspathContainerInitializer(containerId);
		containerInitializer.initialize(containerPath, javaProject);
	}

	/**
	 * Adds all natures and containers that SOA needs to perform its code
	 * generation and management for a consumer project. This is tricky method,
	 * this assumes that the project already has SOA Nature and it also assumes
	 * that the SOA container is also present. The reason why its safe to assume
	 * this is that this API will be called only on an existing SOA Project.
	 *
	 * @param implProject the impl project
	 * @param natureId the nature id
	 * @param monitor the monitor
	 * @throws CoreException the core exception
	 */
	public static void addSOAConsumerSupport(final IProject implProject,
			String natureId, IProgressMonitor monitor) throws CoreException {
		// The last nature to contribute will have its builder at the start.
		// We need Service Natured builder at the start and then
		// the service builder
		ProjectUtil.addNature(implProject, monitor, 
				natureId);
	}

	/**
	 * Append soa class path.
	 *
	 * @param javaProject the java project
	 * @param monitor the monitor
	 * @throws CoreException the core exception
	 */
	public static void appendSOAClassPath(IJavaProject javaProject,
			IProgressMonitor monitor) throws CoreException {
		GlobalRepositorySystem.instanceOf().getActiveRepositorySystem()
		.getProjectConfigurer().addBuildSystemClasspathContainer(javaProject, monitor);
	}
	
}
