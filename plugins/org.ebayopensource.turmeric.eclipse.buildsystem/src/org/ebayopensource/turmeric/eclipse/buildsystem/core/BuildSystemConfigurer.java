/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.buildsystem.core;

import java.io.IOException;

import org.ebayopensource.turmeric.eclipse.buildsystem.utils.BuildSystemUtil;
import org.ebayopensource.turmeric.eclipse.config.core.SOAGlobalConfigAccessor;
import org.ebayopensource.turmeric.eclipse.core.resources.constants.SOAProjectConstants;
import org.ebayopensource.turmeric.eclipse.core.resources.constants.SOAProjectConstants.SupportedProjectType;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.GlobalRepositorySystem;
import org.ebayopensource.turmeric.eclipse.resources.model.SOAConsumerProject;
import org.ebayopensource.turmeric.eclipse.resources.model.SOAImplProject;
import org.ebayopensource.turmeric.eclipse.resources.model.SOAIntfProject;
import org.ebayopensource.turmeric.eclipse.utils.plugin.JDTUtil;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;


/**
 * Responsible for adding SOA Support for any natural project. SOA Support needs
 * Java Support to be added and so we add both Java support and SOA Support in
 * one shot. Adding support in a broad sense means adding the builders and
 * natures, and class path containers. Next most important functionality is to
 * perform build system initializations. The APIS in this class are very
 * straight forward that we don't want to create documentation for each API.
 * 
 * @author smathew
 */
public class BuildSystemConfigurer {

	/**
	 * Configure the interface project with both Java support and Turmeric support.
	 *
	 * @param intfProject SOAIntfProject
	 * @param monitor IProgressMonitor
	 * @throws CoreException the core exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static void configure(SOAIntfProject intfProject,
			IProgressMonitor monitor) throws CoreException, IOException {
		// add java support
		JDTUtil.addJavaSupport(intfProject.getEclipseMetadata().getProject(),
				intfProject.getSourceDirectoryNames(), 
				SOAGlobalConfigAccessor.getDefaultCompilerLevel(), 
				SOAProjectConstants.FOLDER_OUTPUT_DIR, monitor);
		// add SOA support
		
		BuildSystemUtil.addSOASupport(intfProject, GlobalRepositorySystem.instanceOf()
				.getActiveRepositorySystem().getProjectNatureId(SupportedProjectType.INTERFACE), monitor);
	}

	/**
	 * Configure the implementation project with Java and Turmeric support.
	 *
	 * @param implProject SOAImplProject
	 * @param monitor IProgressMonitor
	 * @throws Exception the exception
	 */
	public static void configure(SOAImplProject implProject,
			IProgressMonitor monitor) throws Exception {
		// add java support
		JDTUtil.addJavaSupport(implProject.getEclipseMetadata().getProject(),
				implProject.getSourceDirectoryNames(),
				SOAGlobalConfigAccessor.getDefaultCompilerLevel(), 
				SOAProjectConstants.FOLDER_OUTPUT_DIR, monitor);
		// add SOA support
		BuildSystemUtil.addSOASupport(implProject, GlobalRepositorySystem.instanceOf()
				.getActiveRepositorySystem().getProjectNatureId(SupportedProjectType.IMPL), monitor);
	}

	/**
	 * Configure a consumer project for both Java and Turmeric support.
	 *
	 * @param consumerProject SOAConsumerProject
	 * @param monitor IProgressMonitor
	 * @throws CoreException the core exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static void configure(SOAConsumerProject consumerProject,
			IProgressMonitor monitor) throws CoreException, IOException {
		// add java support
		JDTUtil.addJavaSupport(consumerProject.getEclipseMetadata()
				.getProject(), consumerProject.getSourceDirectoryNames(),
				SOAGlobalConfigAccessor.getDefaultCompilerLevel(), 
				SOAProjectConstants.FOLDER_OUTPUT_DIR, monitor);
		// add SOA support
		BuildSystemUtil.addSOASupport(consumerProject, GlobalRepositorySystem.instanceOf()
				.getActiveRepositorySystem().getProjectNatureId(SupportedProjectType.CONSUMER), monitor);
	}

	/**
	 * Perform repository specific tasks.
	 *
	 * @param intfProject the intf project
	 * @param implProject the impl project
	 * @param monitor the monitor
	 * @throws Exception the exception
	 */
	public static void performRepositorySpecificTasks(
			SOAIntfProject intfProject, SOAImplProject implProject,
			IProgressMonitor monitor) throws Exception {
		GlobalRepositorySystem.instanceOf().getActiveRepositorySystem()
				.getProjectConfigurer().initializeProject(intfProject,
						implProject, monitor);
	}

	/**
	 * Perform repository specific tasks.
	 *
	 * @param intfProject the intf project
	 * @param monitor the monitor
	 * @throws Exception the exception
	 */
	public static void performRepositorySpecificTasks(
			SOAIntfProject intfProject, IProgressMonitor monitor)
			throws Exception {
		GlobalRepositorySystem.instanceOf().getActiveRepositorySystem()
				.getProjectConfigurer().initializeProject(intfProject, monitor);
	}

	/**
	 * Perform repository specific tasks.
	 *
	 * @param implProject the impl project
	 * @param monitor the monitor
	 * @throws Exception the exception
	 */
	public static void performRepositorySpecificTasks(
			SOAImplProject implProject, IProgressMonitor monitor)
			throws Exception {
		GlobalRepositorySystem.instanceOf().getActiveRepositorySystem()
				.getProjectConfigurer().initializeProject(implProject, monitor);
	}

	/**
	 * Perform repository specific tasks.
	 *
	 * @param consumerProject the consumer project
	 * @param serviceVersion the service version
	 * @param convertingJavaProject the converting java project
	 * @param monitor the monitor
	 * @throws Exception the exception
	 */
	public static void performRepositorySpecificTasks(
			SOAConsumerProject consumerProject, String serviceVersion,
			boolean convertingJavaProject, 
			IProgressMonitor monitor) throws Exception {
		GlobalRepositorySystem.instanceOf().getActiveRepositorySystem()
				.getProjectConfigurer().initializeProject(consumerProject,
						serviceVersion, convertingJavaProject, monitor);
	}

}
