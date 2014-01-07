/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.services.buildsystem;

import java.util.Set;

import org.ebayopensource.turmeric.eclipse.buildsystem.core.BuildSystemConfigurer;
import org.ebayopensource.turmeric.eclipse.buildsystem.services.SOAResourceCreator;
import org.ebayopensource.turmeric.eclipse.buildsystem.utils.BuildSystemCodeGen;
import org.ebayopensource.turmeric.eclipse.buildsystem.utils.ProjectPropertiesFileUtil;
import org.ebayopensource.turmeric.eclipse.core.model.services.ServiceFromWsdlParamModel;
import org.ebayopensource.turmeric.eclipse.core.resources.constants.SOAProjectConstants.SupportedProjectType;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.GlobalRepositorySystem;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.ISOAOrganizationProvider;
import org.ebayopensource.turmeric.eclipse.resources.model.SOAImplMetadata;
import org.ebayopensource.turmeric.eclipse.resources.model.SOAImplProject;
import org.ebayopensource.turmeric.eclipse.resources.model.SOAIntfProject;
import org.ebayopensource.turmeric.eclipse.resources.model.SOAProjectEclipseMetadata;
import org.ebayopensource.turmeric.eclipse.utils.plugin.ProgressUtil;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IProgressMonitor;


/**
 * The Class ImplementationCreator.
 *
 * @author smathew
 * Creates the implementation project model
 */
public class ImplementationCreator {
	
	/**
	 * Creates the impl model from blank wsdl.
	 *
	 * @param paramModel the param model
	 * @param interfaceProject the interface project
	 * @param monitor the monitor
	 * @return the sOA impl project
	 * @throws Exception the exception
	 */
	public static SOAImplProject createImplModelFromBlankWsdl(
			ServiceFromWsdlParamModel paramModel,
			SOAIntfProject interfaceProject, IProgressMonitor monitor)
			throws Exception {
		// Creates the SOA related metadata
		SOAImplMetadata implMetadata = SOAImplMetadata.create(paramModel,
				interfaceProject.getMetadata());
		ProgressUtil.progressOneStep(monitor);
		
		SOAProjectEclipseMetadata eclipseMetadata = SOAProjectEclipseMetadata
				.create(implMetadata.getServiceImplProjectName(), paramModel
						.getWorkspaceRootDirectory());
		ProgressUtil.progressOneStep(monitor);
		
		SOAImplProject implProject = SOAImplProject.create(implMetadata,
				eclipseMetadata);
		ProgressUtil.progressOneStep(monitor);
		
		final ISOAOrganizationProvider orgProvider = GlobalRepositorySystem.instanceOf()
		.getActiveRepositorySystem().getActiveOrganizationProvider();
		Set<String> requiredLibraries = paramModel.getImplLibs();
		requiredLibraries.addAll(orgProvider.
				getDefaultDependencies(SupportedProjectType.IMPL));
		implProject.setRequiredLibraries(requiredLibraries);
		// adding the service project now
		Set<String> requiredProjects = paramModel.getImplProjects();
		requiredProjects.add(interfaceProject.getEclipseMetadata()
				.getProjectName());
		implProject.setRequiredProjects(requiredProjects);
		ProgressUtil.progressOneStep(monitor);
		return implProject;
	}

	/**
	 * Creates the impl project from blank wsdl.
	 *
	 * @param implProject the impl project
	 * @param intfProject the intf project
	 * @param monitor the monitor
	 * @throws Exception the exception
	 */
	public static void createImplProjectFromBlankWsdl(
			SOAImplProject implProject, SOAIntfProject intfProject,ServiceFromWsdlParamModel paramModel,
			IProgressMonitor monitor) throws Exception {
		IProject project = SOAResourceCreator.createProject(implProject
				.getEclipseMetadata(), monitor);
		ProgressUtil.progressOneStep(monitor);
		
		SOAResourceCreator.createFolders(project, implProject, monitor);
		ProgressUtil.progressOneStep(monitor);
		
		SOAResourceCreator.createPropertiesFile(implProject);
		ProgressUtil.progressOneStep(monitor);
		ProjectPropertiesFileUtil.createPrefsFile(implProject.getProject(), monitor);
		BuildSystemConfigurer.performRepositorySpecificTasks(implProject,paramModel.getRaptorPlatformVersion(),
				paramModel.getReuse(),paramModel.getWebProjectName(),
				 monitor);
		ProgressUtil.progressOneStep(monitor);
		
		BuildSystemConfigurer.configure(implProject, monitor);
		ProgressUtil.progressOneStep(monitor);
		
		BuildSystemCodeGen.generateServiceConfigXml(implProject);
		ProgressUtil.progressOneStep(monitor);
	}
	
	/**
	 * Creates the impl model from existing wsdl.
	 *
	 * @param paramModel the param model
	 * @param interfaceProject the interface project
	 * @param monitor the monitor
	 * @return the sOA impl project
	 * @throws Exception the exception
	 */
	public static SOAImplProject createImplModelFromExistingWsdl(
			ServiceFromWsdlParamModel paramModel,
			SOAIntfProject interfaceProject, IProgressMonitor monitor)
			throws Exception {
		// Creates the SOA related metadata
		SOAImplMetadata implMetadata = SOAImplMetadata.create(paramModel,
				interfaceProject.getMetadata());
		SOAProjectEclipseMetadata eclipseMetadata = SOAProjectEclipseMetadata
				.create(implMetadata.getServiceImplProjectName(), paramModel
						.getWorkspaceRootDirectory());
		SOAImplProject implProject = SOAImplProject.create(implMetadata,
				eclipseMetadata);

		final ISOAOrganizationProvider orgProvider = GlobalRepositorySystem.instanceOf()
		.getActiveRepositorySystem().getActiveOrganizationProvider();
		Set<String> requiredLibraries = paramModel.getImplLibs();
		requiredLibraries.addAll(orgProvider.
				getDefaultDependencies(SupportedProjectType.IMPL));
		
		implProject.setRequiredLibraries(requiredLibraries);
		// adding the service project now
		Set<String> requiredProjects = paramModel.getImplProjects();
		requiredProjects.add(interfaceProject.getEclipseMetadata()
				.getProjectName());
		implProject.setRequiredProjects(requiredProjects);
		return implProject;
	}

	/**
	 * Creates the impl project from existing wsdl.
	 *
	 * @param implProject the impl project
	 * @param intfProject the intf project
	 * @param monitor the monitor
	 * @throws Exception the exception
	 */
	public static void createImplProjectFromExistingWsdl(
			SOAImplProject implProject, SOAIntfProject intfProject, ServiceFromWsdlParamModel paramModel,
			IProgressMonitor monitor) throws Exception {
		IProject project = SOAResourceCreator.createProject(implProject
				.getEclipseMetadata(), monitor);
		ProgressUtil.progressOneStep(monitor);
		
		SOAResourceCreator.createFolders(project, implProject, monitor);
		ProgressUtil.progressOneStep(monitor);
		
		SOAResourceCreator.createPropertiesFile(implProject);
		ProgressUtil.progressOneStep(monitor);
		ProjectPropertiesFileUtil.createPrefsFile(implProject.getProject(), monitor);
		
		BuildSystemConfigurer.performRepositorySpecificTasks(implProject,paramModel.getRaptorPlatformVersion(),
				paramModel.getReuse(),paramModel.getWebProjectName(),
				 monitor);
		ProgressUtil.progressOneStep(monitor);
		
		BuildSystemConfigurer.configure(implProject, monitor);
		ProgressUtil.progressOneStep(monitor);
		
		BuildSystemCodeGen.generateServiceConfigXml(implProject);
		ProgressUtil.progressOneStep(monitor);
	}

}
