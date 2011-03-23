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
package org.ebayopensource.turmeric.eclipse.errorlibrary.buildsystem;

import org.ebayopensource.turmeric.eclipse.buildsystem.services.SOAResourceCreator;
import org.ebayopensource.turmeric.eclipse.errorlibrary.buildsystem.core.ErrorLibraryBuildSystemConfigurer;
import org.ebayopensource.turmeric.eclipse.errorlibrary.providers.ErrorLibraryProviderFactory;
import org.ebayopensource.turmeric.eclipse.errorlibrary.providers.IErrorLibraryCreator;
import org.ebayopensource.turmeric.eclipse.errorlibrary.resources.model.SOAErrorLibraryMetadata;
import org.ebayopensource.turmeric.eclipse.errorlibrary.resources.model.SOAErrorLibraryProject;
import org.ebayopensource.turmeric.eclipse.errorlibrary.ui.model.ErrorLibraryParamModel;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.GlobalRepositorySystem;
import org.ebayopensource.turmeric.eclipse.resources.constants.SOAProjectConstants.SupportedProjectType;
import org.ebayopensource.turmeric.eclipse.resources.model.SOAProjectEclipseMetadata;
import org.ebayopensource.turmeric.eclipse.resources.model.SOABaseProject.ProjectLinkedResource;
import org.ebayopensource.turmeric.eclipse.resources.ui.model.AbstractSOAProjetParamModel.UIModelProjectLinkedResource;
import org.ebayopensource.turmeric.eclipse.utils.collections.SetUtil;
import org.ebayopensource.turmeric.eclipse.utils.plugin.ProgressUtil;
import org.ebayopensource.turmeric.eclipse.utils.plugin.WorkspaceUtil;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IProgressMonitor;



/**
 * @author yayu
 *
 */
public class ErrorLibraryCreator {

	/**
	 * 
	 */
	private ErrorLibraryCreator() {
		super();
	}
	
	public static void createErrorLibrary(ErrorLibraryParamModel model,
			IProgressMonitor monitor) throws Exception {
		// Eclipse Metadata
		SOAProjectEclipseMetadata eclipseMetadata = SOAProjectEclipseMetadata
				.create(model.getProjectName(), model.getWorkspaceRootDirectory());
		
		// Creates the SOA related metadata
		SOAErrorLibraryMetadata metadata = SOAErrorLibraryMetadata.create(model);
		ProgressUtil.progressOneStep(monitor);

		// Creating SOA Type Library Project
		SOAErrorLibraryProject errorLibraryProject = SOAErrorLibraryProject
				.createSOAProjectErrorLibrary(eclipseMetadata, metadata);
		
		GlobalRepositorySystem.instanceOf().getActiveRepositorySystem()
		.getProjectConfigurer().addProjectLinkedResources(errorLibraryProject);
		for (UIModelProjectLinkedResource lRes : model.getLinkedResources()) {
			errorLibraryProject.addProjectLinkedResource(
					new ProjectLinkedResource(lRes.getName()
							, lRes.getType(), lRes.getLocation()));
		}
		ProgressUtil.progressOneStep(monitor);
		
//		errorLibraryProject.setRequiredProjects(SetUtil.linkedSet(
//				SOAProjectConstants.REQUIRED_PROJECTS_ERROR_LIB_PROJECT));
		errorLibraryProject.setRequiredLibraries(SetUtil.linkedSet(
				GlobalRepositorySystem.instanceOf().getActiveRepositorySystem()
				.getActiveOrganizationProvider().getDefaultDependencies(SupportedProjectType.ERROR_LIBRARY)));

		// Project Creation starts here
		IProject project = SOAResourceCreator.createProject(
				eclipseMetadata, monitor);
		// Folder Creation
		SOAResourceCreator.createFolders(project, errorLibraryProject, monitor);
		ProgressUtil.progressOneStep(monitor);

		// call BuildSystem
		GlobalRepositorySystem.instanceOf().getActiveRepositorySystem()
				.getProjectConfigurer().initializeErrorLibProject(
						errorLibraryProject, model.getVersion(), monitor);
		ProgressUtil.progressOneStep(monitor);

		// Configure
		ErrorLibraryBuildSystemConfigurer.configure(errorLibraryProject, monitor);
		ProgressUtil.progressOneStep(monitor);
		
		IErrorLibraryCreator creator = 
			ErrorLibraryProviderFactory.getPreferredProvider().getErrorLibraryCreator();
		
		creator.postCreation(errorLibraryProject.getProject(), model, monitor);
		
		ErrorLibraryBuildSystemConfigurer.reorderClasspath(
				errorLibraryProject.getProject(), monitor);
		WorkspaceUtil.refresh(project);
	}

	
	

}
