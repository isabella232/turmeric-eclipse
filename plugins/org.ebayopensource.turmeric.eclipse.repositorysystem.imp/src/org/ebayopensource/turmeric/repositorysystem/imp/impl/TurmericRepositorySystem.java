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

import java.util.List;

import org.ebayopensource.turmeric.eclipse.codegen.utils.SOACodegenTransformer;
import org.ebayopensource.turmeric.eclipse.core.buildsystem.AbstractSOAClassPathContainer;
import org.ebayopensource.turmeric.eclipse.core.resources.constants.SOAProjectConstants.SupportedProjectType;
import org.ebayopensource.turmeric.eclipse.maven.core.utils.SOAMavenConstants;
import org.ebayopensource.turmeric.eclipse.maven.ui.dialogs.MavenLibraryDependencyDialog;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.IErrorRegistryBridge;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.IProjectHealthChecker;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.ISOAAssetRegistry;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.ISOACodegenProvider;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.ISOACodegenTransformer;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.ISOAConfigurationRegistry;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.ISOAHelpProvider;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.ISOAOrganizationProvider;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.ISOAProjectConfigurer;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.ISOARootLocator;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.ITypeRegistryBridge;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.TrackingEvent;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.ISOALibraryDependencyDialog;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.ISOARepositorySystem;
import org.ebayopensource.turmeric.eclipse.resources.util.SOAServiceUtil;
import org.ebayopensource.turmeric.eclipse.utils.collections.ListUtil;
import org.ebayopensource.turmeric.eclipse.validator.core.AbstractSOAValidator;
import org.ebayopensource.turmeric.eclipse.validator.core.ISOAPreValidator;
import org.ebayopensource.turmeric.eclipse.validator.core.ISOAValidator;
import org.ebayopensource.turmeric.eclipse.validator.utils.common.ProjectFileSystemValidator;
import org.ebayopensource.turmeric.eclipse.validator.utils.common.ProjectWorkspaceValidator;
import org.ebayopensource.turmeric.eclipse.validator.utils.common.WSDLValidator;
import org.ebayopensource.turmeric.repositorysystem.imp.utils.TurmericConstants;
import org.ebayopensource.turmeric.repositorysystem.imp.validators.TurmericPreValidator;
import org.ebayopensource.turmeric.repositorysystem.imp.validators.TurmericServiceValidator;
import org.ebayopensource.turmeric.repositorysystem.imp.validators.TurmericTargetDirectoryValidator;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.IJavaProject;

/**
 * @author yayu
 * 
 */
public class TurmericRepositorySystem implements ISOARepositorySystem {
	private ISOAValidator targetDirValidator;
	private ISOAProjectConfigurer projectConfigurer;
	private ISOAValidator serviceValidator;
	private ISOAValidator projectFileSystemValidator;
	private ISOAValidator projectWorkspaceValidator;
	private ISOAPreValidator preValidator;
	private ISOAValidator postValidator;
	private ISOAValidator wsdlValidator;
	private ISOAAssetRegistry assetRegistry;
	private ISOACodegenTransformer codegenTransformer;
	private ITypeRegistryBridge typeRegistryBridge;
	private IErrorRegistryBridge errorRegistryBridge;
	private ISOAConfigurationRegistry configRegistry;
	private ISOACodegenProvider codegenProvider;
	private IProjectHealthChecker projectHealthChecker;

	/**
	 * 
	 */
	public TurmericRepositorySystem() {
		super();
	}


	/**
	 * {@inheritDoc}
	 * 
	 */
	public ISOARootLocator getSOARootLocator() {
		return TurmericRootLocator.INSTANCE;
	}

	/**
	 * {@inheritDoc}
	 * 
	 */
	public String getDisplayName() {
		return TurmericConstants.TURMERIC_DISPLAY_NAME;
	}

	/**
	 * {@inheritDoc}
	 * 
	 */
	public String getId() {
		return TurmericConstants.TURMERIC_ID;
	}

	/**
	 * {@inheritDoc}
	 * 
	 */
	public String getClasspathContainerID() {
		return SOAMavenConstants.MAVEN_CLASSPATH_CONTAINER_ID;
	}

	/**
	 * {@inheritDoc}
	 * 
	 */
	public ISOAProjectConfigurer getProjectConfigurer() {
		if (projectConfigurer == null)
			projectConfigurer = new TurmericProjectConfigurer();
		return projectConfigurer;
	}

	/**
	 * {@inheritDoc}
	 * 
	 */
	public ISOAAssetRegistry getAssetRegistry() {
		if (this.assetRegistry == null)
			assetRegistry = new TurmericAssetRegistry();
		return assetRegistry;
	}

	/**
	 * {@inheritDoc}
	 */
	public ISOACodegenTransformer getCodegenTranformer() {
		if (this.codegenTransformer == null)
			codegenTransformer = new SOACodegenTransformer();
		return codegenTransformer;
	}

	/**
	 * {@inheritDoc}
	 * 
	 */
	public ISOALibraryDependencyDialog getLibraryDependencyDialog() {
		return new MavenLibraryDependencyDialog();
	}

	/**
	 * {@inheritDoc}
	 */
	public ITypeRegistryBridge getTypeRegistryBridge() {
		if (this.typeRegistryBridge == null)
			typeRegistryBridge = new TurmericTypeRegistryBridge();
		return typeRegistryBridge;
	}

	/**
	 * {@inheritDoc}
	 * 
	 */
	public ISOAHelpProvider getHelpProvider() {
		return TurmericHelpProvider.INSTANCE;
	}

	/**
	 * {@inheritDoc}
	 * 
	 */
	public ISOAConfigurationRegistry getConfigurationRegistry() {
		final String orgId = getActiveOrganizationProvider().getName();
		if (configRegistry == null
				|| configRegistry.getOrganizationName().equals(orgId) == false) {
			// either not init yet or org has been changed
			configRegistry = new TurmericConfigurationRegistry(orgId);
		}
		return configRegistry;
	}

	/**
	 * {@inheritDoc}
	 */
	public IErrorRegistryBridge getErorRegistryBridge() {
		if (errorRegistryBridge == null)
			errorRegistryBridge = new TurmericErrorRegistryBridge();
		return errorRegistryBridge;
	}

	/**
	 * {@inheritDoc}
	 */
	public List<ISOAOrganizationProvider> getOrganizationProviders() {
		return ListUtil.list(TurmericOrganizationProvider.INSTANCE);
	}

	/**
	 * {@inheritDoc}
	 */
	public ISOAOrganizationProvider getActiveOrganizationProvider() {
		return TurmericOrganizationProvider.INSTANCE;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * <p>Currently this doesn't track any events.</p>
	 */
	public void trackingUsage(TrackingEvent event) {
		// we don't track anything for now

	}

	/**
	 * {@inheritDoc}
	 * 
	 */
	public ISOAValidator getTargetDirectoryValidator() {
		if (targetDirValidator == null)
			targetDirValidator = new TurmericTargetDirectoryValidator();
		return targetDirValidator;
	}

	/**
	 * {@inheritDoc}
	 */
	public ISOAValidator getServiceValidator() {
		if (serviceValidator == null)
			serviceValidator = new TurmericServiceValidator();
		return serviceValidator;
	}

	/**
	 * {@inheritDoc}
	 * 
	 */
	public ISOAValidator getProjectWorkspaceValidator() {
		if (projectWorkspaceValidator == null)
			projectWorkspaceValidator = new ProjectWorkspaceValidator();
		return projectWorkspaceValidator;
	}

	/**
	 * {@inheritDoc}
	 * 
	 */
	public ISOAValidator getProjectFileSystemValidator() {
		if (projectFileSystemValidator == null)
			projectFileSystemValidator = new ProjectFileSystemValidator();
		return projectFileSystemValidator;
	}

	/**
	 * {@inheritDoc}
	 * 
	 */
	public ISOAValidator getWSDLValidator() {
		if (wsdlValidator == null)
			wsdlValidator = new WSDLValidator();
		return wsdlValidator;
	}

	/**
	 * {@inheritDoc}
	 */
	public ISOAPreValidator getPreValidator() {
		if (preValidator == null)
			preValidator = new TurmericPreValidator();
		return preValidator;
	}

	/**
	 * {@inheritDoc}
	 */
	public ISOAValidator getPostValidator() {
		if (postValidator == null)
			postValidator = new AbstractSOAValidator() {
			};
		return postValidator;
	}

	/**
	 * {@inheritDoc}
	 */
	public AbstractSOAClassPathContainer getClassPathContainer(IPath path,
			IJavaProject javaProject) {
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	public Object getAdapter(Class arg0) {
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	public String getProjectNatureId(SupportedProjectType projectType) {
		return TurmericConstants.PROJECT_NATUREIDS_MAP.get(projectType);
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isValidTurmericProject(IProject project)
			throws CoreException {
		return SOAServiceUtil.hasNatures(project,
				TurmericConstants.PROJECT_NATUREIDS_MAP.values().toArray(
						new String[0]));
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @seeorg.ebayopensource.turmeric.eclipse.repositorysystem.core.
	 * ISOARepositorySystem#getProjectType(org.eclipse.core.resources.IProject)
	 */
	public SupportedProjectType getProjectType(IProject project)
			throws CoreException {
		if (SOAServiceUtil.hasNatures(project,
				TurmericConstants.NATURE_ID_SOA_INTF_PROJECT)) {
			return SupportedProjectType.INTERFACE;
		} else if (SOAServiceUtil.hasNatures(project,
				TurmericConstants.NATURE_ID_SOA_IMPL_PROJECT)) {
			return SupportedProjectType.IMPL;
		} else if (SOAServiceUtil.hasNatures(project,
				TurmericConstants.NATURE_ID_SOA_CONSUMER_PROJECT)) {
			return SupportedProjectType.CONSUMER;
		} else if (SOAServiceUtil.hasNatures(project,
				TurmericConstants.NATURE_ID_SOA_TYPELIB_PROJECT)) {
			return SupportedProjectType.TYPE_LIBRARY;
		} else if (SOAServiceUtil.hasNatures(project,
				TurmericConstants.NATURE_ID_TURMERIC_ERRORLIB_PROJECT)) {
			return SupportedProjectType.ERROR_LIBRARY;
		}
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	public String getTurmericProjectNatureId(IProject project)
			throws CoreException {
		return getProjectNatureId(getProjectType(project));
	}

	/**
	 * {@inheritDoc}
	 */
	public ISOACodegenProvider getSOACodegenProvider() {
		if (codegenProvider == null) {
			codegenProvider = new TurmericCodegenProvider();
		}
		return codegenProvider;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.ebayopensource.turmeric.eclipse.repositorysystem.ui.ISOARepositorySystem#getProjectHealthChecker()
	 */
	public IProjectHealthChecker getProjectHealthChecker() {
		if (this.projectHealthChecker == null) {
			this.projectHealthChecker = new TurmericProjectHealthChecker();
		}
		return this.projectHealthChecker;
	}
}
