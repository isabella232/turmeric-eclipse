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

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.ebayopensource.turmeric.eclipse.core.resources.constants.SOAProjectConstants.SupportedProjectType;
import org.ebayopensource.turmeric.eclipse.maven.core.model.MavenAssetInfo;
import org.ebayopensource.turmeric.eclipse.maven.core.model.MavenProjectInfo;
import org.ebayopensource.turmeric.eclipse.maven.core.utils.MavenCoreUtils;
import org.ebayopensource.turmeric.eclipse.maven.core.utils.SOAMavenConstants;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.ISOAAssetRegistry;
import org.ebayopensource.turmeric.eclipse.repositorysystem.utils.TurmericServiceUtils;
import org.ebayopensource.turmeric.eclipse.resources.model.AssetInfo;
import org.ebayopensource.turmeric.eclipse.resources.model.IAssetInfo;
import org.ebayopensource.turmeric.eclipse.resources.model.ISOAProject;
import org.ebayopensource.turmeric.eclipse.resources.model.ProjectInfo;
import org.ebayopensource.turmeric.eclipse.resources.model.SOAConsumerMetadata;
import org.ebayopensource.turmeric.eclipse.resources.model.SOAConsumerProject;
import org.ebayopensource.turmeric.eclipse.resources.model.SOAImplMetadata;
import org.ebayopensource.turmeric.eclipse.resources.model.SOAImplProject;
import org.ebayopensource.turmeric.eclipse.resources.model.SOAIntfMetadata;
import org.ebayopensource.turmeric.eclipse.resources.model.SOAIntfProject;
import org.ebayopensource.turmeric.eclipse.resources.model.ISOAConsumerProject.SOAClientEnvironment;
import org.ebayopensource.turmeric.eclipse.resources.util.SOAConsumerUtil;
import org.ebayopensource.turmeric.eclipse.resources.util.SOAImplUtil;
import org.ebayopensource.turmeric.eclipse.resources.util.SOAIntfUtil;
import org.ebayopensource.turmeric.eclipse.resources.util.SOAServiceUtil;
import org.ebayopensource.turmeric.eclipse.utils.plugin.WorkspaceUtil;
import org.ebayopensource.turmeric.repositorysystem.imp.utils.TurmericConstants;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.GlobalRepositorySystem;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IPath;


/**
 * The Class TurmericAssetRegistry.
 *
 * @author yayu
 */
public class TurmericAssetRegistry implements ISOAAssetRegistry {

	/**
	 * Instantiates a new turmeric asset registry.
	 */
	public TurmericAssetRegistry() {
		super();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.ebayopensource.turmeric.eclipse.repositorysystem.core.ISOAAssetRegistry#getAllLibraries()
	 */
	public Set<? extends AssetInfo> getAllAvailableServices() throws Exception {
		final Set<AssetInfo> result = new TreeSet<AssetInfo>();
		
		for (final AssetInfo assetInfo : MavenCoreUtils.getAllServicesInLocalRepository()) {
			result.add(assetInfo);
		}
		return result;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.ebayopensource.turmeric.eclipse.repositorysystem.core.ISOAAssetRegistry#getAllLibraries()
	 */
	public Set<AssetInfo> getAllLibraries() throws Exception {
		return new HashSet<AssetInfo>();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.ebayopensource.turmeric.eclipse.repositorysystem.core.ISOAAssetRegistry#getDependencies(java.lang.String)
	 */
	public List<AssetInfo> getDependencies(String projectName) throws Exception {
		return MavenCoreUtils.getDependencies(projectName);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.ebayopensource.turmeric.eclipse.repositorysystem.core.ISOAAssetRegistry#getProjectInfo(java.lang.String)
	 */
	public ProjectInfo getProjectInfo(String projectName) throws Exception {
		return MavenCoreUtils.getProjectInfo(projectName);
	}

	/**
	 * {@inheritDoc}
	 * 
	 */
	public IAssetInfo getAsset(IProject project) throws Exception {
		return MavenCoreUtils.getProjectInfo(project);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.ebayopensource.turmeric.eclipse.repositorysystem.core.ISOAAssetRegistry#getSOAProject(org.eclipse.core.resources.IProject)
	 */
	public ISOAProject getSOAProject(IProject project) throws Exception {
		WorkspaceUtil.refresh(project);
		SupportedProjectType projectType = GlobalRepositorySystem.instanceOf().getActiveRepositorySystem()
		.getProjectType(project);
		final ISOAProject soaProject = SOAServiceUtil.loadSOAProject(project, 
				TurmericConstants.PROJECT_NATUREIDS_MAP.get(projectType));
		final MavenProjectInfo projectInfo = MavenCoreUtils.getProjectInfo(project);
		
		if (projectInfo != null) {
			soaProject.setRequiredLibraries(projectInfo.getRequiredLibraries());
			soaProject.setRequiredProjects(projectInfo.getRequiredProjects());
			if (soaProject instanceof SOAIntfProject) {
				final SOAIntfMetadata intfMetadata = (SOAIntfMetadata) soaProject
						.getMetadata();
				intfMetadata.setImlementationProjectName(projectInfo
						.getImplementationProjectName());
			} else if (soaProject instanceof SOAImplProject) {
				final SOAImplMetadata implMetadata = (SOAImplMetadata) soaProject
						.getMetadata();
				SOAImplUtil.loadServiceConfig((SOAImplProject) soaProject,
						projectInfo.getInterfaceProjectName());
				final String intfProjectLocation = MavenCoreUtils.getAssetLocation(projectInfo.getInterfaceProjectName());
				final SOAIntfMetadata intfMetadata = SOAIntfUtil.loadIntfMetadata(intfProjectLocation, projectInfo.getInterfaceProjectName());
				implMetadata.setIntfMetadata(intfMetadata);
				implMetadata.setImplVersion(projectInfo.getVersion());
				intfMetadata.setImlementationProjectName(project.getName());
			}
			
			if (TurmericServiceUtils.isSOAConsumerProject(project)) {
				final SOAConsumerProject consumerProject = (SOAConsumerProject) soaProject;
				final Map<String, SOAIntfMetadata> requiredServices = consumerProject
						.getRequiredServices();

				final Set<String> requiredSvcs = new HashSet<String>();
				for (SOAClientEnvironment env : consumerProject.getClientConfigFiles().keySet()) {
					requiredSvcs.add(env.getServiceName());
				}
				requiredSvcs.addAll(projectInfo.getRequiredServices());
				
				for (final String requiredServiceName : requiredSvcs) {
					if (requiredServices.containsKey(requiredServiceName) == false) {
						// dependent service is not in the current workspace,
						// need to load from repository system.
						final String intfProjectLocation = MavenCoreUtils.getAssetLocation(requiredServiceName);
						final SOAIntfMetadata intfMetadata = SOAIntfUtil.loadIntfMetadata(intfProjectLocation, requiredServiceName);
						final MavenProjectInfo requiredService = MavenCoreUtils.getProjectInfo(requiredServiceName);
						intfMetadata.setImlementationProjectName(requiredService.getImplementationProjectName());
						soaProject.getRequiredLibraries().addAll(
								requiredService.getRequiredLibraries());
						soaProject.getRequiredProjects().addAll(
								requiredService.getRequiredProjects());
						requiredServices.put(requiredServiceName, intfMetadata);
					}
				}
				
				final IFile consumerPropsFile = SOAConsumerUtil.getConsumerPropertiesFile(project);
				if (consumerPropsFile.exists() == true) {
					SOAConsumerMetadata metadata = SOAServiceUtil.getSOAConsumerMetadata(project);
					consumerProject.getMetadata().setClientName(metadata.getClientName());
					consumerProject.getMetadata().setConsumerId(metadata.getConsumerId());
				}
			}
		}
		return soaProject;
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 */
	public IFile getProjectConfigurationFile(IProject project) {
		if (project == null || project.isAccessible() == false)
			return null;
		return project.getFile(SOAMavenConstants.MAVEN_PROJECT_CONFIG_FILE);
	}

	/**
	 * {@inheritDoc}
	 * 
	 */
	public String getAssetLocation(String projectName) throws Exception {
		return MavenCoreUtils.getAssetLocation(projectName);
	}

	/**
	 * {@inheritDoc}
	 * 
	 */
	public String getAssetLocation(AssetInfo library) throws Exception {
		if (library instanceof MavenAssetInfo) {
			final MavenAssetInfo assetInfo = (MavenAssetInfo)library;
			final IPath result = MavenCoreUtils.getArtifactJarLocation(assetInfo.getGroupID(), 
					assetInfo.getName(), assetInfo.getVersion());
			return result != null ? result.toString() : null;
		} else {
			return MavenCoreUtils.getAssetLocation(library.getName());
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 */
	public AssetInfo getAssetFromProjectDependency(IProject project,
			String libraryName) throws Exception {
		for (AssetInfo info : MavenCoreUtils.getDependencies(project.getName())) {
			if (info.getName().equals(libraryName)) {
				return info;
			}
		}
		return null;
	}

}
