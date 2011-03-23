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
package org.ebayopensource.turmeric.eclipse.typelibrary.resources.model;

import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.ebayopensource.turmeric.eclipse.logging.SOALogger;
import org.ebayopensource.turmeric.eclipse.resources.constants.SOAProjectConstants;
import org.ebayopensource.turmeric.eclipse.resources.model.ISOAProjectResolver;
import org.ebayopensource.turmeric.eclipse.resources.model.SOAProjectEclipseMetadata;
import org.ebayopensource.turmeric.eclipse.typelibrary.ui.model.TypeLibraryParamModel;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;


/**
 * @author yayu
 *
 */
public class SOATypeLibraryProjectResolver implements ISOAProjectResolver<SOATypeLibraryProject> {

	/**
	 * 
	 */
	public SOATypeLibraryProjectResolver() {
		super();
	}

	public SOATypeLibraryProject loadProject(
			SOAProjectEclipseMetadata eclipseMetadata) throws Exception {
		final SOATypeLibraryMetadata metadata = SOATypeLibraryMetadata.create(
				loadTypeLibraryModel(eclipseMetadata.getProject()));
		final SOATypeLibraryProject typeLibProject = SOATypeLibraryProject
		.createSOAProjectTypeLibrary(eclipseMetadata, metadata);
		
		return typeLibProject;
	}
	
	public static TypeLibraryParamModel loadTypeLibraryModel(IProject project) throws Exception {
		TypeLibraryParamModel model = new TypeLibraryParamModel();
		final IFile propFile = project.getFile(SOAProjectConstants.PROPS_FILE_TYPE_LIBRARY);
		final Properties props = new Properties();
		InputStream in = null;
		try {
			in = propFile.getContents();
			props.load(in);
			String category = StringUtils.trim(props.getProperty("TYPE_LIBRARY_CATEGORY"));
			String version = StringUtils.trim(props.getProperty("TYPE_LIBRARY_VERSION"));
			String namespace = StringUtils.trim(props.getProperty("TYPE_LIBRARY_NAMESPACE"));
			String name = StringUtils.trim(props.getProperty("TYPE_LIBRARY_NAME"));
			model.setCategory(category);
			model.setNamespace(namespace);
			model.setTypeLibraryName(name);
			model.setVersion(version);
			SOALogger.getLogger().info("Loaded typelibrary informatoin: ", 
					"Name=", name, ", Category=", category, ", Namespace=", namespace, ", version=", version);
		} finally {
			IOUtils.closeQuietly(in);
		}
		return model;
	}

}
