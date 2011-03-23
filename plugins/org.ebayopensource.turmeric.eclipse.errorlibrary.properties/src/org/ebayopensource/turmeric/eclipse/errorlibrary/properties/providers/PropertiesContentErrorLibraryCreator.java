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
package org.ebayopensource.turmeric.eclipse.errorlibrary.properties.providers;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;

import org.apache.commons.io.IOUtils;
import org.ebayopensource.turmeric.eclipse.errorlibrary.properties.model.PropertiesSOAErrorLibrary;
import org.ebayopensource.turmeric.eclipse.errorlibrary.properties.registry.TurmericErrorRegistry;
import org.ebayopensource.turmeric.eclipse.errorlibrary.properties.utils.TurmericErrorLibraryUtils;
import org.ebayopensource.turmeric.eclipse.errorlibrary.providers.IErrorLibraryCreator;
import org.ebayopensource.turmeric.eclipse.errorlibrary.ui.model.ErrorLibraryParamModel;
import org.ebayopensource.turmeric.eclipse.exception.resources.projects.SOAErrorLibraryCreationFailedException;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.GlobalRepositorySystem;
import org.ebayopensource.turmeric.eclipse.resources.constants.SOAProjectConstants;
import org.ebayopensource.turmeric.eclipse.utils.plugin.WorkspaceUtil;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;


/**
 * @author haozhou
 *
 */
public class PropertiesContentErrorLibraryCreator implements
		IErrorLibraryCreator {

	/**
	 * @see org.ebayopensource.turmeric.eclipse.errorlibrary.providers.IErrorLibraryCreator#preCreation(org.ebayopensource.turmeric.eclipse.errorlibrary.ui.model.ErrorLibraryParamModel)
	 */
	public void preCreation(ErrorLibraryParamModel model)
			throws SOAErrorLibraryCreationFailedException {
		// TODO Auto-generated method stub

	}

	/**
	 * @see org.ebayopensource.turmeric.eclipse.errorlibrary.providers.IErrorLibraryCreator#postCreation(org.eclipse.core.resources.IProject, org.ebayopensource.turmeric.eclipse.errorlibrary.ui.model.ErrorLibraryParamModel, org.eclipse.core.runtime.IProgressMonitor)
	 */
	public void postCreation(IProject project, ErrorLibraryParamModel model,
			IProgressMonitor monitor)
			throws SOAErrorLibraryCreationFailedException {
		try {
			GlobalRepositorySystem.instanceOf().getActiveRepositorySystem()
					.getErorRegistryBridge()
					.createPlatformSpecificArtifacts(project, monitor);
			createPropsFile(project, monitor);
			PropertiesSOAErrorLibrary library = new PropertiesSOAErrorLibrary();
			library.setName(model.getProjectName());
			library.setVersion(model.getVersion());
			TurmericErrorRegistry.addErrorLibrary(library);
		} catch (Exception e) {
			throw new SOAErrorLibraryCreationFailedException(e);
		}
	}

	/**
	 * @see org.ebayopensource.turmeric.eclipse.errorlibrary.providers.IErrorLibraryCreator#createPlatformSpecificArtifacts(org.eclipse.core.resources.IProject, java.lang.String, org.eclipse.core.runtime.IProgressMonitor)
	 */
	public void createPlatformSpecificArtifacts(IProject project,
			String srcFolder, IProgressMonitor monitor) throws CoreException,
			IOException {
		
	}
	
	private IFile createPropsFile(IProject project, IProgressMonitor monitor) throws IOException, CoreException {
		IFile file = TurmericErrorLibraryUtils.getDomainListPropsFile(project);
		OutputStream output = null;
		try {
			output = new ByteArrayOutputStream();
			final Properties properties = new Properties();
			properties.setProperty(
					PropertiesSOAConstants.PROPS_LIST_OF_DOMAINS, "");
			properties.store(output, SOAProjectConstants.PROPS_COMMENTS);
			WorkspaceUtil.writeToFile(output.toString(), file, monitor);
		} finally {
			IOUtils.closeQuietly(output);
		}
		return file;
	}

}
