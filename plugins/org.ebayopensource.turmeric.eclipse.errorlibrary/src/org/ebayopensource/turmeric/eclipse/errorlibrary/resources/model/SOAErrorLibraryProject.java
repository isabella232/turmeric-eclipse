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
package org.ebayopensource.turmeric.eclipse.errorlibrary.resources.model;

import java.util.ArrayList;
import java.util.List;

import org.ebayopensource.turmeric.eclipse.core.logging.SOALogger;
import org.ebayopensource.turmeric.eclipse.core.resources.constants.SOAProjectConstants;
import org.ebayopensource.turmeric.eclipse.core.resources.constants.SOAProjectConstants.SupportedProjectType;
import org.ebayopensource.turmeric.eclipse.errorlibrary.providers.ErrorLibraryProviderFactory;
import org.ebayopensource.turmeric.eclipse.exception.resources.SOAGetErrorLibraryProviderFailedException;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.GlobalRepositorySystem;
import org.ebayopensource.turmeric.eclipse.resources.model.AbstractSOAMetadata;
import org.ebayopensource.turmeric.eclipse.resources.model.SOABaseProject;
import org.ebayopensource.turmeric.eclipse.resources.model.SOAProjectEclipseMetadata;
import org.eclipse.jdt.core.JavaCore;


/**
 * @author yayu
 * 
 */
public class SOAErrorLibraryProject extends SOABaseProject {

	private static final String[] SOURCE_DIRECTORIES = { SOAProjectConstants.FOLDER_DOT };
	private static ErrorLibraryProviderFactory factory = ErrorLibraryProviderFactory.getInstance();

	/**
	 * 
	 */
	public SOAErrorLibraryProject() {
		super();
		final List<SOAProjectSourceDirectory> sourceDirectories = new ArrayList<SOAProjectSourceDirectory>();
		for (final String srcDir : SOURCE_DIRECTORIES) {
			sourceDirectories.add(new SOAProjectSourceDirectory(srcDir));
		}
		setSourceDirectories(sourceDirectories);
		addEclipseNature(GlobalRepositorySystem.instanceOf().getActiveRepositorySystem()
				.getProjectNatureId(SupportedProjectType.ERROR_LIBRARY));
		addEclipseNature(JavaCore.NATURE_ID);
	}

	public void setErrorLibraryMetadata(SOAErrorLibraryMetadata metadata) {
		this.setMetadata(metadata);
	}

	public SOAErrorLibraryMetadata getErrorLibraryMetadata() {
		final AbstractSOAMetadata metadata = super.getMetadata();
		return metadata instanceof SOAErrorLibraryMetadata ? (SOAErrorLibraryMetadata) metadata
				: null;
	}

	public static SOAErrorLibraryProject createSOAProjectErrorLibrary(
			SOAProjectEclipseMetadata eclipseMetadata,
			SOAErrorLibraryMetadata metadata) {
		SOAErrorLibraryProject soaErrorLibraryProject = new SOAErrorLibraryProject();
		soaErrorLibraryProject.setEclipseMetadata(eclipseMetadata);
		soaErrorLibraryProject.setErrorLibraryMetadata(metadata);
		return soaErrorLibraryProject;
	}

	@Override
	protected Class<? extends AbstractSOAMetadata> getSOAMetadataClass() {
		return SOAErrorLibraryMetadata.class;
	}

	@Override
	public List<String> getSourceSubFolders() {
		List<String> srcDirs = new ArrayList<String>();
		try {
			srcDirs = factory.getPreferredProvider().getSourceSubFolders(this);
		} catch (SOAGetErrorLibraryProviderFailedException e) {
			SOALogger.getLogger().error(e);
		}
		return srcDirs;
	}

	/**
	 * {@inheritDoc}
	 */
	public List<String> getSourceDirectoryNames() {
		List<String> srcDirs = new ArrayList<String>();
		for (SOAProjectSourceDirectory dir : getSourceDirectories()) {
			srcDirs.add(dir.getLocation());
		}

		return srcDirs;
	}

}
