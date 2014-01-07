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
import org.ebayopensource.turmeric.eclipse.buildsystem.utils.ProjectPropertiesFileUtil;
import org.ebayopensource.turmeric.eclipse.core.model.services.ServiceFromTemplateWsdlParamModel;
import org.ebayopensource.turmeric.eclipse.core.model.services.ServiceFromWsdlParamModel;
import org.ebayopensource.turmeric.eclipse.core.resources.constants.SOAProjectConstants.SupportedProjectType;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.GlobalRepositorySystem;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.ISOAOrganizationProvider;
import org.ebayopensource.turmeric.eclipse.resources.model.SOAImplProject;
import org.ebayopensource.turmeric.eclipse.resources.model.SOAIntfMetadata;
import org.ebayopensource.turmeric.eclipse.resources.model.SOAIntfProject;
import org.ebayopensource.turmeric.eclipse.resources.model.SOAProjectEclipseMetadata;
import org.ebayopensource.turmeric.eclipse.resources.util.SOAIntfUtil;
import org.ebayopensource.turmeric.eclipse.resources.util.SOAServiceUtil;
import org.ebayopensource.turmeric.eclipse.template.wsdl.processors.WSDLTemplateProcessor;
import org.ebayopensource.turmeric.eclipse.utils.plugin.ProgressUtil;
import org.ebayopensource.turmeric.eclipse.utils.plugin.WorkspaceUtil;
import org.ebayopensource.turmeric.eclipse.utils.wsdl.WSDLUtil;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IProgressMonitor;


/**
 * The Class InterfaceCreator.
 *
 * @author smathew Creates the interface project model
 */
public class InterfaceCreator {
	
	/**
	 * Creates the intf model from blank wsdl.
	 *
	 * @param paramModel the param model
	 * @param monitor the monitor
	 * @return the sOA intf project
	 * @throws Exception the exception
	 */
	public static SOAIntfProject createIntfModelFromBlankWsdl(
			ServiceFromTemplateWsdlParamModel paramModel,
			IProgressMonitor monitor) throws Exception {
		// Creates the SOA related metadata
		SOAIntfMetadata metadata = SOAIntfMetadata.create(paramModel);
		ProgressUtil.progressOneStep(monitor);
		
		// Creates the eclipse related metadata
		SOAProjectEclipseMetadata soaEclipseMetadata = SOAProjectEclipseMetadata
				.create(paramModel.getServiceName(), paramModel
						.getWorkspaceRootDirectory());
		SOAIntfProject intfProject = SOAIntfProject.create(metadata,
				soaEclipseMetadata);
		ProgressUtil.progressOneStep(monitor);
		
		final ISOAOrganizationProvider orgProvider = GlobalRepositorySystem.instanceOf()
		.getActiveRepositorySystem().getActiveOrganizationProvider();
		Set<String> requiredLibraries = paramModel.getInterfaceLibs();
		requiredLibraries.addAll(orgProvider.
				getDefaultDependencies(SupportedProjectType.INTERFACE));
		
		intfProject.setRequiredLibraries(requiredLibraries);
		intfProject.setRequiredProjects(paramModel.getInterfaceProjects());
		return intfProject;
	}

	/**
	 * Creates the intf project from blank wsdl.
	 *
	 * @param intfProject the intf project
	 * @param implProject the impl project
	 * @param nameSpace the name space
	 * @param wsdlTemplateProcessor the wsdl template processor
	 * @param monitor the monitor
	 * @throws Exception the exception
	 */
	public static void createIntfProjectFromBlankWsdl(
			SOAIntfProject intfProject, SOAImplProject implProject,
			ServiceFromTemplateWsdlParamModel paramModel, WSDLTemplateProcessor wsdlTemplateProcessor,
			IProgressMonitor monitor) throws Exception {
		IProject project = SOAResourceCreator.createProject(intfProject
				.getEclipseMetadata(), monitor);
		ProgressUtil.progressOneStep(monitor);
		
		SOAResourceCreator.createFolders(project, intfProject, monitor);
		final IFile wsdlTargetFile = SOAServiceUtil.getWsdlFile(intfProject);
		wsdlTemplateProcessor.setDestinationFile(wsdlTargetFile);
		wsdlTemplateProcessor.process(monitor);
		ProgressUtil.progressOneStep(monitor);
		
		intfProject.getMetadata().setOriginalWSDLUrl(
				wsdlTargetFile.getLocationURI().toURL());
		SOAIntfUtil.setInformationFromWsdl(intfProject.getMetadata()
				.getOriginalWSDLUrl(), intfProject.getMetadata());
		ProgressUtil.progressOneStep(monitor);
		
		SOAResourceCreator.createPropertiesFile(intfProject, monitor);
		ProgressUtil.progressOneStep(monitor);
		ProjectPropertiesFileUtil.createPrefsFile(intfProject.getProject(), monitor);
		BuildSystemConfigurer.performRepositorySpecificTasks(intfProject,
				implProject,paramModel.getRaptorPlatformVersion(),
				paramModel.getReuse(),paramModel.getWebProjectName(), monitor);
		ProgressUtil.progressOneStep(monitor);
		
		BuildSystemConfigurer.configure(intfProject, monitor);
		ProgressUtil.progressOneStep(monitor);
		
		wsdlTemplateProcessor.finalize();
		ProgressUtil.progressOneStep(monitor);
		
		//No need to do this anymore
		/*BuildSystemCodeGen.generateServiceMetadataProperties(intfProject,
				nameSpace);
		ProgressUtil.progressOneStep(monitor);*/
	}

	/**
	 * Creates the intf model from existing wsdl.
	 *
	 * @param paramModel the param model
	 * @param monitor the monitor
	 * @return the sOA intf project
	 * @throws Exception the exception
	 */
	public static SOAIntfProject createIntfModelFromExistingWsdl(
			ServiceFromWsdlParamModel paramModel, IProgressMonitor monitor)
			throws Exception {
		// Creates the SOA related metadata
		SOAIntfMetadata metadata = SOAIntfMetadata.create(paramModel);
		ProgressUtil.progressOneStep(monitor);

		// Creates the eclipse related metadata
		SOAProjectEclipseMetadata soaEclipseMetadata = SOAProjectEclipseMetadata
				.create(paramModel.getServiceName(), paramModel
						.getWorkspaceRootDirectory());
		ProgressUtil.progressOneStep(monitor);

		SOAIntfProject intfProject = SOAIntfProject.create(metadata,
				soaEclipseMetadata);
		ProgressUtil.progressOneStep(monitor);
		 
		final ISOAOrganizationProvider orgProvider = GlobalRepositorySystem.instanceOf()
		.getActiveRepositorySystem().getActiveOrganizationProvider();
		Set<String> requiredLibraries = paramModel.getInterfaceLibs();
		requiredLibraries.addAll(orgProvider.
				getDefaultDependencies(SupportedProjectType.INTERFACE));
		intfProject.setRequiredLibraries(requiredLibraries);
		intfProject.setRequiredProjects(paramModel.getInterfaceProjects());
		return intfProject;
	}

	/**
	 * Creates the intf project from existing wsdl.
	 *
	 * @param intfProject the intf project
	 * @param implProject the impl project
	 * @param nameSpace the name space
	 * @param monitor the monitor
	 * @throws Exception the exception
	 */
	public static void createIntfProjectFromExistingWsdl(
			SOAIntfProject intfProject, SOAImplProject implProject,
			ServiceFromWsdlParamModel paramModel, IProgressMonitor monitor) throws Exception {
		IProject project = SOAResourceCreator.createProject(intfProject
				.getEclipseMetadata(), monitor);
		ProgressUtil.progressOneStep(monitor);
		
		SOAResourceCreator.createFolders(project, intfProject, monitor);
		final IFile wsdlTarget = SOAServiceUtil.getWsdlFile(intfProject);
		WSDLUtil.writeWSDL(intfProject.getMetadata().getOriginalWSDLUrl()
				.toString(), wsdlTarget.getLocation().toString());
		WorkspaceUtil.refresh(wsdlTarget, monitor);
		ProgressUtil.progressOneStep(monitor);
		
		SOAResourceCreator.createPropertiesFile(intfProject, monitor);
		ProgressUtil.progressOneStep(monitor);
		ProjectPropertiesFileUtil.createPrefsFile(intfProject.getProject(), monitor);
		
		BuildSystemConfigurer.performRepositorySpecificTasks(intfProject,
				implProject,paramModel.getRaptorPlatformVersion(),paramModel.getReuse(),paramModel.getWebProjectName(), monitor);
		ProgressUtil.progressOneStep(monitor);
		
		BuildSystemConfigurer.configure(intfProject, monitor);
		ProgressUtil.progressOneStep(monitor);
		
		//No need to do this anymore
		/*BuildSystemCodeGen.generateServiceMetadataProperties(intfProject,
				nameSpace);
		ProgressUtil.progressOneStep(monitor);*/
	}

	/**
	 * Creates the intf project from existing wsdl.
	 *
	 * @param intfProject the intf project
	 * @param monitor the monitor
	 * @throws Exception the exception
	 */
	public static void createIntfProjectFromExistingWsdl(
			SOAIntfProject intfProject, IProgressMonitor monitor)
			throws Exception {
		IProject project = SOAResourceCreator.createProject(intfProject
				.getEclipseMetadata(), monitor);
		ProgressUtil.progressOneStep(monitor);
		SOAResourceCreator.createFolders(project, intfProject, monitor);
		ProgressUtil.progressOneStep(monitor);
		final IFile wsdlTarget = SOAServiceUtil.getWsdlFile(intfProject);
		WSDLUtil.writeWSDL(intfProject.getMetadata().getOriginalWSDLUrl()
				.toString(), wsdlTarget.getLocation().toString());
		WorkspaceUtil.refresh(wsdlTarget, monitor);
		ProgressUtil.progressOneStep(monitor);
		SOAResourceCreator.createPropertiesFile(intfProject, monitor);
		ProgressUtil.progressOneStep(monitor);
		ProjectPropertiesFileUtil.createPrefsFile(intfProject.getProject(), monitor);
		
		
		BuildSystemConfigurer.performRepositorySpecificTasks(intfProject,
				monitor);
		ProgressUtil.progressOneStep(monitor);
		//ProjectPropertiesFileUtil.createPrefsFile(project, monitor);
		BuildSystemConfigurer.configure(intfProject, monitor);
		ProgressUtil.progressOneStep(monitor);

		//No need to do this anymore
		/*BuildSystemCodeGen.generateServiceMetadataProperties(intfProject,
				intfProject.getMetadata().getTargetNamespace());
		ProgressUtil.progressOneStep(monitor);*/
	}

}
