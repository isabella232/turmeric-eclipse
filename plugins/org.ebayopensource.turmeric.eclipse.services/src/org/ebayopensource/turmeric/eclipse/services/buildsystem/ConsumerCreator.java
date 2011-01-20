/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
/**
 * 
 */
package org.ebayopensource.turmeric.eclipse.services.buildsystem;

import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.ebayopensource.turmeric.eclipse.buildsystem.core.BuildSystemConfigurer;
import org.ebayopensource.turmeric.eclipse.buildsystem.services.SOAResourceCreator;
import org.ebayopensource.turmeric.eclipse.buildsystem.utils.BuildSystemCodeGen;
import org.ebayopensource.turmeric.eclipse.logging.SOALogger;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.GlobalRepositorySystem;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.ISOAAssetRegistry;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.ISOAOrganizationProvider;
import org.ebayopensource.turmeric.eclipse.resources.constants.SOAProjectConstants;
import org.ebayopensource.turmeric.eclipse.resources.constants.SOAProjectConstants.SOAFrameworkLibrary;
import org.ebayopensource.turmeric.eclipse.resources.constants.SOAProjectConstants.SupportedProjectType;
import org.ebayopensource.turmeric.eclipse.resources.model.ProjectInfo;
import org.ebayopensource.turmeric.eclipse.resources.model.SOAConsumerMetadata;
import org.ebayopensource.turmeric.eclipse.resources.model.SOAConsumerProject;
import org.ebayopensource.turmeric.eclipse.resources.model.SOAIntfMetadata;
import org.ebayopensource.turmeric.eclipse.resources.model.SOAIntfProject;
import org.ebayopensource.turmeric.eclipse.resources.model.SOAProjectEclipseMetadata;
import org.ebayopensource.turmeric.eclipse.resources.ui.model.ConsumerFromJavaParamModel;
import org.ebayopensource.turmeric.eclipse.resources.ui.model.ConsumerFromWsdlParamModel;
import org.ebayopensource.turmeric.eclipse.resources.util.SOAIntfUtil;
import org.ebayopensource.turmeric.eclipse.utils.collections.SetUtil;
import org.ebayopensource.turmeric.eclipse.utils.plugin.ProgressUtil;
import org.ebayopensource.turmeric.eclipse.utils.plugin.WorkspaceUtil;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IProgressMonitor;


/**
 * @author yayu
 * 
 */
public class ConsumerCreator {
	private static final SOALogger logger = SOALogger.getLogger();

	/**
	 * 
	 */
	private ConsumerCreator() {
		super();
	}

	public static SOAConsumerProject createConsumerModelFromExistingWsdl(
			ConsumerFromWsdlParamModel paramModel,
			SOAIntfProject interfaceProject, IProgressMonitor monitor)
			throws Exception {
		// Creates the SOA related metadata
		SOAConsumerMetadata metadata = SOAConsumerMetadata.create(paramModel);
		ProgressUtil.progressOneStep(monitor);
		
		// Creates the eclipse related metadata
		SOAProjectEclipseMetadata soaEclipseMetadata = SOAProjectEclipseMetadata
				.create(paramModel.getClientName(), paramModel
						.getWorkspaceRootDirectory());
		ProgressUtil.progressOneStep(monitor);
		
		SOAConsumerProject consumerProject = SOAConsumerProject.create(
				metadata, soaEclipseMetadata);
		ProgressUtil.progressOneStep(monitor);
		
		
		final ISOAOrganizationProvider orgProvider = GlobalRepositorySystem.instanceOf()
		.getActiveRepositorySystem().getActiveOrganizationProvider();
		Set<String> requiredLibraries = paramModel.getInterfaceLibs();
		requiredLibraries.addAll(orgProvider.getDefaultDependencies(SupportedProjectType.CONSUMER));
		
		consumerProject.setRequiredLibraries(requiredLibraries);
		consumerProject.setRequiredProjects(paramModel.getInterfaceProjects());
		ProgressUtil.progressOneStep(monitor);
		
		// adding the service project now
		Set<String> requiredProjects = paramModel.getImplProjects();
		requiredProjects.add(interfaceProject.getEclipseMetadata()
				.getProjectName());
		consumerProject.setRequiredProjects(requiredProjects);

		consumerProject.getRequiredServices().put(
				interfaceProject.getMetadata().getServiceName(), 
				interfaceProject.getMetadata());
		ProgressUtil.progressOneStep(monitor);
		return consumerProject;
	}

	public static void createConsumerProjectFromExistingWsdl(
			SOAConsumerProject consumerProject, SOAIntfProject intfProject,
			IProgressMonitor monitor) throws Exception {
		IProject project = SOAResourceCreator.createProject(consumerProject
				.getEclipseMetadata(), monitor);
		ProgressUtil.progressOneStep(monitor);
		
		SOAResourceCreator.createFolders(project, consumerProject, monitor);
		ProgressUtil.progressOneStep(monitor);
		
		SOAResourceCreator.createPropertiesFile(consumerProject, monitor);
		ProgressUtil.progressOneStep(monitor);
		
		BuildSystemConfigurer.performRepositorySpecificTasks(consumerProject, 
				intfProject.getMetadata().getServiceVersion(), false, 
				monitor);
		ProgressUtil.progressOneStep(monitor);
		
		BuildSystemConfigurer.configure(consumerProject, monitor);
		ProgressUtil.progressOneStep(monitor);
		
		//we only generate the client config now, because the interface class is not ready yet
		//the client project builder will be responsible for generating the base consumer
		BuildSystemCodeGen.generateClientConfigXml(consumerProject, monitor);
		ProgressUtil.progressOneStep(monitor);
	}


	public static SOAConsumerProject createConsumerModelFromJava(
			ConsumerFromJavaParamModel uiModel, IProgressMonitor monitor) 
	throws Exception{
		// Creates the SOA related metadata
		SOAConsumerMetadata metadata = SOAConsumerMetadata.create(uiModel);
		ProgressUtil.progressOneStep(monitor);
		
		// Creates the eclipse related metadata
		SOAProjectEclipseMetadata soaEclipseMetadata = SOAProjectEclipseMetadata
				.create(uiModel.getClientName(), uiModel.getParentDirectory());
		ProgressUtil.progressOneStep(monitor);
		
		SOAConsumerProject consumerProject = SOAConsumerProject.create(
				metadata, soaEclipseMetadata);
		ProgressUtil.progressOneStep(monitor);
		
		final ISOAOrganizationProvider orgProvider = GlobalRepositorySystem.instanceOf()
		.getActiveRepositorySystem().getActiveOrganizationProvider();
		Set<String> requiredLibraries = SetUtil.treeSet(orgProvider.
				getDefaultDependencies(SupportedProjectType.CONSUMER));
		
		consumerProject.setRequiredLibraries(requiredLibraries);
		ProgressUtil.progressOneStep(monitor);
		
		final ISOAAssetRegistry assetRegistry = GlobalRepositorySystem.instanceOf().getActiveRepositorySystem()
		.getAssetRegistry();
		// adding the service project now
		for (String serviceName : uiModel.getServiceNames()) {
			final String assetLocation = assetRegistry.getAssetLocation(serviceName);
			if (assetLocation != null) {
				final SOAIntfMetadata intfMetadata = SOAIntfUtil.loadIntfMetadata(assetLocation, serviceName);
				if (intfMetadata != null) {
					final String serviceLocation = intfMetadata.getServiceLocation();
					consumerProject.getRequiredServices().put(serviceName, intfMetadata);
					
					//we need to get all required libs for the intf service
					final ProjectInfo intfProjectInfo = assetRegistry.getProjectInfo(serviceName);
					consumerProject.getRequiredLibraries().addAll(intfProjectInfo.getRequiredLibraries());
					consumerProject.getRequiredProjects().addAll(intfProjectInfo.getRequiredProjects());

					if (serviceLocation != null && serviceLocation
							.startsWith(SOAProjectConstants.PROTOCOL_HTTP) == false) {
						//the service location not starts with http. so the service binding will be Local
						final String implProjectName = intfProjectInfo.getImplementationProjectName();
						if (StringUtils.isNotBlank(implProjectName)) {
							intfMetadata.setImlementationProjectName(implProjectName);
							if (WorkspaceUtil.getProject(implProjectName).isAccessible())
								consumerProject.getRequiredProjects().add(implProjectName);
							else
								logger.warning("The implementation project could not be found in the current workspace->"
										, implProjectName);
						}
						consumerProject.getRequiredLibraries().add(
								orgProvider.getSOAFrameworkLibraryIdentifier(SOAFrameworkLibrary.SOASERVER));
					}
				}
			}
			
			ProgressUtil.progressOneStep(monitor);
		}
		
		return consumerProject;
	}

	public static void createConsumerProjectFromJava(
			SOAConsumerProject consumerProject, boolean convertJavaProject, 
			IProgressMonitor monitor)
			throws Exception {
		final IProject project = SOAResourceCreator.createProject(consumerProject
				.getEclipseMetadata(), monitor);
		ProgressUtil.progressOneStep(monitor);
		SOAResourceCreator.createFolders(project, consumerProject, monitor);
		ProgressUtil.progressOneStep(monitor);
		SOAResourceCreator.createPropertiesFile(consumerProject, monitor);
		ProgressUtil.progressOneStep(monitor);
		BuildSystemConfigurer.performRepositorySpecificTasks(consumerProject, 
				SOAProjectConstants.DEFAULT_SERVICE_VERSION, convertJavaProject, 
				monitor);
		ProgressUtil.progressOneStep(monitor);
		BuildSystemConfigurer.configure(consumerProject, monitor);
		ProgressUtil.progressOneStep(monitor);
		BuildSystemCodeGen.generateArtifactsForConsumerProject(consumerProject, monitor);
	}
}
