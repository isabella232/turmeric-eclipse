/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.repositorysystem.core;

import java.util.List;

import org.ebayopensource.turmeric.eclipse.core.buildsystem.AbstractSOAClassPathContainer;
import org.ebayopensource.turmeric.eclipse.core.resources.constants.SOAProjectConstants.SupportedProjectType;
import org.ebayopensource.turmeric.eclipse.validator.core.ISOAPreValidator;
import org.ebayopensource.turmeric.eclipse.validator.core.ISOAValidator;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.IJavaProject;

/**
 * The Interface ISOARepositorySystem.
 *
 * @author smathew
 * 
 * The spec for all repo system to contribute their own locators,
 * validators to the global repo etc. the validators are now many and
 * could be moved out to a seperate spec. Implementors will implement
 * this interface and contribute their class to repository system
 * extension point
 */
public interface ISOARepositorySystem extends IAdaptable {

	/**
	 * Gets the sOA root locator.
	 *
	 * @return the root locator service of the underlying system
	 */
	public ISOARootLocator getSOARootLocator();

	/**
	 * Gets the display name.
	 *
	 * @return the descriptive name of the underlying system
	 */
	public String getDisplayName();

	/**
	 * Gets the id.
	 *
	 * @return the ID of the underlyig build system
	 */
	public String getId();

	/**
	 * Gets the target directory validator.
	 *
	 * @return the target directory validator
	 */
	public ISOAValidator getTargetDirectoryValidator();

	/**
	 * Gets the project configurer.
	 *
	 * @return the project configurer
	 */
	public ISOAProjectConfigurer getProjectConfigurer();

	/**
	 * Gets the service validator.
	 *
	 * @return the service validator
	 */
	public ISOAValidator getServiceValidator();

	/**
	 * Gets the project workspace validator.
	 *
	 * @return the project workspace validator
	 */
	public ISOAValidator getProjectWorkspaceValidator();

	/**
	 * Gets the project file system validator.
	 *
	 * @return the project file system validator
	 */
	public ISOAValidator getProjectFileSystemValidator();

	/**
	 * Gets the wSDL validator.
	 *
	 * @return the wSDL validator
	 */
	public ISOAValidator getWSDLValidator();

	/**
	 * Gets the pre validator.
	 *
	 * @return the pre validator
	 */
	public ISOAPreValidator getPreValidator();

	/**
	 * Gets the post validator.
	 *
	 * @return the post validator
	 */
	public ISOAValidator getPostValidator();

	/**
	 * Gets the asset registry.
	 *
	 * @return the asset registry
	 */
	public ISOAAssetRegistry getAssetRegistry();

	/**
	 * Gets the class path container.
	 *
	 * @param path the path
	 * @param javaProject the java project
	 * @return the class path container
	 */
	public AbstractSOAClassPathContainer getClassPathContainer(IPath path,
			IJavaProject javaProject);

	/**
	 * Gets the classpath container id.
	 *
	 * @return The classpath container ID for the underlying repository system
	 */
	public String getClasspathContainerID();

	/**
	 * Gets the codegen tranformer.
	 *
	 * @return the codegen tranformer
	 */
	public ISOACodegenTransformer getCodegenTranformer();

	/**
	 * Gets the library dependency dialog.
	 *
	 * @return the library dependency dialog
	 */
	public ISOALibraryDependencyDialog getLibraryDependencyDialog();

	/**
	 * Gets the type registry bridge.
	 *
	 * @return the type registry bridge
	 */
	public ITypeRegistryBridge getTypeRegistryBridge();

	/**
	 * Gets the help provider.
	 *
	 * @return the instance of ISOAHelpProvider for the underlying repository
	 * system.
	 */
	public ISOAHelpProvider getHelpProvider();

	/**
	 * Gets the configuration registry.
	 *
	 * @return the configuration registry
	 */
	public ISOAConfigurationRegistry getConfigurationRegistry();

	/**
	 * Gets the eror registry bridge.
	 *
	 * @return the eror registry bridge
	 */
	public IErrorRegistryBridge getErorRegistryBridge();

	/**
	 * Gets the organization providers.
	 *
	 * @return the organization providers
	 */
	public List<ISOAOrganizationProvider> getOrganizationProviders();

	/**
	 * Gets the active organization provider.
	 *
	 * @return the active organization provider
	 */
	public ISOAOrganizationProvider getActiveOrganizationProvider();

	/**
	 * log tracking message for the underlying system.
	 *
	 * @param event the event
	 */
	public void trackingUsage(TrackingEvent event);

	/**
	 * Gets the project type.
	 *
	 * @param project the project
	 * @return the project type
	 * @throws CoreException the core exception
	 */
	public SupportedProjectType getProjectType(IProject project)
			throws CoreException;

	/**
	 * Gets the project nature id.
	 *
	 * @param projectType the project type
	 * @return the Eclipse project nature ID for the given project type
	 */
	public String getProjectNatureId(SupportedProjectType projectType);

	/**
	 * Gets the turmeric project nature id.
	 *
	 * @param project the project
	 * @return the Eclipse project nature ID for the given Turmeric project
	 * @throws CoreException the core exception
	 */
	public String getTurmericProjectNatureId(IProject project)
			throws CoreException;

	/**
	 * check whether the given Eclipse project is a valid Turmeric project.
	 *
	 * @param project the project
	 * @return true, if is valid turmeric project
	 * @throws CoreException the core exception
	 */
	public boolean isValidTurmericProject(IProject project)
			throws CoreException;

	/**
	 * Need a codegen system for each organization. it will provide the
	 * destination folder for intf/impl/consumer project and be response for
	 * calling ServiceGenerator for each organization.
	 *
	 * @return the sOA codegen provider
	 */
	public ISOACodegenProvider getSOACodegenProvider();
	
	/**
	 * Gets the project health checker.
	 *
	 * @return the project health checker instance for the underlying system
	 */
	public IProjectHealthChecker getProjectHealthChecker();

}
