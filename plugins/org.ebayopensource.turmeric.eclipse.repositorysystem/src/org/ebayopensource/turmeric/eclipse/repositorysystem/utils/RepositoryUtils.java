/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.repositorysystem.utils;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.lang.StringUtils;
import org.ebayopensource.turmeric.eclipse.core.resources.constants.SOAProjectConstants;
import org.ebayopensource.turmeric.eclipse.resources.model.AssetInfo;
import org.ebayopensource.turmeric.eclipse.resources.model.IAssetInfo;
import org.ebayopensource.turmeric.eclipse.resources.model.ProjectInfo;
import org.ebayopensource.turmeric.eclipse.resources.util.SOAIntfUtil;
import org.ebayopensource.turmeric.eclipse.utils.plugin.WorkspaceUtil;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;


// TODO: Auto-generated Javadoc
/**
 * The Class RepositoryUtils.
 *
 * @author smathew
 * 
 * Util class for Repo
 */
public class RepositoryUtils {

	/**
	 * returns the projects which has the nature in the workspace as an
	 * AssetInfo Collection. If the natures is empty , this api returns all
	 * projects in workspace
	 *
	 * @param natureIds the nature ids
	 * @return Set
	 * @throws CoreException the core exception
	 * @see AssetInfo
	 */
	public static Set<AssetInfo> getProjectInfoProjectsFromWorkSpace(
			String... natureIds) throws CoreException {
		Set<AssetInfo> projectSet = new TreeSet<AssetInfo>();
		List<IProject> projects = WorkspaceUtil.getProjectsByNature(natureIds);
		if (natureIds.length == 0) {
			projects = Arrays.asList(WorkspaceUtil.getAllProjectsInWorkSpace());
		}
		for (IProject project : projects) {
			if (project.isOpen()) {
				AssetInfo assetInfo = new AssetInfo(project.getName(),
						IAssetInfo.TYPE_PROJECT);
				projectSet.add(assetInfo);
			}
		}
		return projectSet;
	}

	/**
	 * Gets the interface projects from work space.
	 *
	 * @return the interface projects from work space
	 * @throws Exception the exception
	 */
	public static Set<ProjectInfo> getInterfaceProjectsFromWorkSpace()
			throws Exception {
		Set<ProjectInfo> projectSet = new TreeSet<ProjectInfo>();
		IProject[] projects = WorkspaceUtil.getAllProjectsInWorkSpace();
		for (IProject project : projects) {
			if (project.isAccessible()) {
				if (TurmericServiceUtils.isSOAInterfaceProject(project)) {
					final Properties props = SOAIntfUtil.loadMetadataProps(
							project, project.getName());
					if (props != null) {
						final String adminName = props.containsKey(SOAProjectConstants.PROP_KEY_ADMIN_NAME) ?
								props.getProperty(SOAProjectConstants.PROP_KEY_ADMIN_NAME) : 
									props.getProperty(SOAProjectConstants.PROP_KEY_SERVICE_NAME);
						final ProjectInfo info = new ProjectInfo(
								StringUtils.trim(adminName),
								StringUtils.trim(props.getProperty(SOAProjectConstants.PROP_KEY_SERVICE_VERSION)),
								project.getLocation().toString(),
								StringUtils.trim(props.getProperty(SOAProjectConstants.PROP_KEY_SERVICE_LAYER)),
								IAssetInfo.TYPE_SERVICE_LIBRARY);

						projectSet.add(info);
					}
				}
			}
		}
		return projectSet;
	}

}
