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
package org.ebayopensource.turmeric.repositorysystem.imp.impl;

import java.io.File;
import java.io.IOException;
import java.util.Set;

import org.ebayopensource.turmeric.eclipse.errorlibrary.providers.ErrorLibraryProviderFactory;
import org.ebayopensource.turmeric.eclipse.errorlibrary.providers.IErrorLibraryCreator;
import org.ebayopensource.turmeric.eclipse.exception.AbstractSOAException;
import org.ebayopensource.turmeric.eclipse.maven.core.utils.MavenCoreUtils;
import org.ebayopensource.turmeric.eclipse.maven.core.utils.SOAMavenConstants;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.IErrorRegistryBridge;
import org.ebayopensource.turmeric.eclipse.resources.model.AssetInfo;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;


/**
 * @author yayu
 *
 */
public class TurmericErrorRegistryBridge implements IErrorRegistryBridge {

	/**
	 * 
	 */
	public TurmericErrorRegistryBridge() {
		super();
	}

	/* (non-Javadoc)
	 * @see org.ebayopensource.turmeric.eclipse.repositorysystem.core.IErrorRegistryBridge#getErrorLibs()
	 */
	public Set<AssetInfo> getErrorLibs() throws Exception {
		return MavenCoreUtils.getAllErrorLibraries();
	}

	/* (non-Javadoc)
	 * @see org.ebayopensource.turmeric.eclipse.repositorysystem.core.IErrorRegistryBridge#getErrorLibraryViewRoot(org.eclipse.core.resources.IProject, org.eclipse.core.runtime.IProgressMonitor)
	 */
	public File getErrorLibraryViewRoot(IProject project,
			IProgressMonitor monitor) throws CoreException {
		return project.getFolder(
				SOAMavenConstants.FOLDER_SRC_MAIN_RESOURCES).getLocation().toFile();
	}

	/* (non-Javadoc)
	 * @see org.ebayopensource.turmeric.eclipse.repositorysystem.core.IErrorRegistryBridge#createPlatformSpecificArtifacts(org.eclipse.core.resources.IProject, org.eclipse.core.runtime.IProgressMonitor)
	 */
	public void createPlatformSpecificArtifacts(IProject project,
			IProgressMonitor monitor) throws CoreException,
			AbstractSOAException, IOException {
		IErrorLibraryCreator creator = 
			ErrorLibraryProviderFactory.getPreferredProvider().getErrorLibraryCreator();
		creator.createPlatformSpecificArtifacts(project, SOAMavenConstants.FOLDER_SRC_MAIN_RESOURCES, monitor);
	}

}
