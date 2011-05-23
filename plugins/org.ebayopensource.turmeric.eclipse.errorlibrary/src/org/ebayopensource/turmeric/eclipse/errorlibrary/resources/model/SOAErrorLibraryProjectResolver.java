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

import org.ebayopensource.turmeric.eclipse.errorlibrary.ui.model.ErrorLibraryParamModel;
import org.ebayopensource.turmeric.eclipse.resources.model.ISOAProjectResolver;
import org.ebayopensource.turmeric.eclipse.resources.model.SOAProjectEclipseMetadata;
import org.eclipse.core.resources.IProject;


/**
 * The Class SOAErrorLibraryProjectResolver.
 *
 * @author yayu
 */
public class SOAErrorLibraryProjectResolver implements ISOAProjectResolver<SOAErrorLibraryProject> {

	/**
	 * Instantiates a new sOA error library project resolver.
	 */
	public SOAErrorLibraryProjectResolver() {
		super();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public SOAErrorLibraryProject loadProject(SOAProjectEclipseMetadata eclipseMetadata)
			throws Exception {
		final SOAErrorLibraryMetadata metadata = SOAErrorLibraryMetadata.create(
				loadErrorLibModel(eclipseMetadata.getProject()));
		final SOAErrorLibraryProject errorLibProject = SOAErrorLibraryProject
		.createSOAProjectErrorLibrary(eclipseMetadata, metadata);
		
		return errorLibProject;
	}

	/**
	 * Load error lib model.
	 *
	 * @param project the project
	 * @return the error library param model
	 */
	public static ErrorLibraryParamModel loadErrorLibModel(IProject project) {
		final ErrorLibraryParamModel model = new ErrorLibraryParamModel();
		model.setProjectName(project.getName());
		
		return model;
	}
}
