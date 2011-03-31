/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.typelibrary.resources.model;

import java.util.ArrayList;
import java.util.List;

import org.ebayopensource.turmeric.eclipse.core.resources.constants.SOATypeLibraryConstants;
import org.ebayopensource.turmeric.eclipse.resources.model.SOABaseProject;
import org.ebayopensource.turmeric.eclipse.resources.model.SOAProjectEclipseMetadata;
import org.ebayopensource.turmeric.eclipse.utils.collections.ListUtil;
import org.ebayopensource.turmeric.eclipse.utils.plugin.WorkspaceUtil;


/**
 * @author smathew
 * 
 * Type Lib Project Model containing both eclipse and type lib metadata
 */
public class SOATypeLibraryProject extends SOABaseProject {

	SOATypeLibraryMetadata metadata;

	public static final String[] SOURCE_DIRECTORIES = {
			SOATypeLibraryConstants.FOLDER_GEN_META_SRC,
			SOATypeLibraryConstants.FOLDER_GEN_SRC,
			SOATypeLibraryConstants.FOLDER_META_SRC, };

	@Override
	public List<String> getSourceSubFolders() {
		List<String> subFolders = new ArrayList<String>();
		subFolders.add(SOATypeLibraryConstants.FOLDER_META_SRC_META_INF
				+ WorkspaceUtil.PATH_SEPERATOR
				+ getTypeLibraryMetadata().getName());
		subFolders.add(SOATypeLibraryConstants.FOLDER_META_SRC_TYPES
				+ WorkspaceUtil.PATH_SEPERATOR
				+ getTypeLibraryMetadata().getName());
		//subFolders.add(SOATypeLibraryConstants.FOLDER_META_SRC_TYPES);
		return subFolders;
	}

	public List<String> getSourceDirectoryNames() {
		return ListUtil.arrayList(SOURCE_DIRECTORIES);
	}

	public static SOATypeLibraryProject createSOAProjectTypeLibrary(
			SOAProjectEclipseMetadata eclipseMetadata,
			SOATypeLibraryMetadata metadata) {
		SOATypeLibraryProject soaTypeLibraryProject = new SOATypeLibraryProject();
		soaTypeLibraryProject.setEclipseMetadata(eclipseMetadata);
		soaTypeLibraryProject.setTypeLibraryMetadata(metadata);
		return soaTypeLibraryProject;
	}

	public SOATypeLibraryMetadata getTypeLibraryMetadata() {
		return metadata;
	}

	public void setTypeLibraryMetadata(SOATypeLibraryMetadata metadata) {
		this.metadata = metadata;
	}
}
