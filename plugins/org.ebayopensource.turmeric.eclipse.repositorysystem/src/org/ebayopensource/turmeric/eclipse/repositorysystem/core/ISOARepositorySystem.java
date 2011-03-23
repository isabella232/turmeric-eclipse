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
import org.ebayopensource.turmeric.eclipse.resources.constants.SOAProjectConstants.SupportedProjectType;
import org.ebayopensource.turmeric.eclipse.validator.core.ISOAPreValidator;
import org.ebayopensource.turmeric.eclipse.validator.core.ISOAValidator;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.IJavaProject;

/**
 * @author smathew
 * 
 *         The spec for all repo system to contribute their own locators,
 *         validators to the global repo etc. the validators are now many and
 *         could be moved out to a seperate spec. Implementors will implement
 *         this interface and contribute their class to repository system
 *         extension point
 * 
 */
public interface ISOARepositorySystem extends IAdaptable {

	/**
	 * @return the root locator service of the underlying system
	 */
	public ISOARootLocator getSOARootLocator();

	/**
	 * @return the descriptive name of the underlying system
	 */
	public String getDisplayName();

	/**
	 * @return the ID of the underlyig build system
	 */
	public String getId();

	public ISOAValidator getTargetDirectoryValidator();

	public ISOAProjectConfigurer getProjectConfigurer();

	public ISOAValidator getServiceValidator();

	public ISOAValidator getProjectWorkspaceValidator();

	public ISOAValidator getProjectFileSystemValidator();

	public ISOAValidator getWSDLValidator();

	public ISOAPreValidator getPreValidator();

	public ISOAValidator getPostValidator();

	public ISOAAssetRegistry getAssetRegistry();

	public AbstractSOAClassPathContainer getClassPathContainer(IPath path,
			IJavaProject javaProject);

	/**
	 * @return The classpath container ID for the underlying repository system
	 */
	public String getClasspathContainerID();

	public ISOACodegenTransformer getCodegenTranformer();

	public ISOALibraryDependencyDialog getLibraryDependencyDialog();

	public ITypeRegistryBridge getTypeRegistryBridge();

	/**
	 * @return the instance of ISOAHelpProvider for the underlying repository
	 *         system.
	 */
	public ISOAHelpProvider getHelpProvider();

	public ISOAConfigurationRegistry getConfigurationRegistry();

	public IErrorRegistryBridge getErorRegistryBridge();

	public List<ISOAOrganizationProvider> getOrganizationProviders();

	public ISOAOrganizationProvider getActiveOrganizationProvider();

	/**
	 * log tracking message for the underlying system.
	 * 
	 * @param event
	 */
	public void trackingUsage(TrackingEvent event);

	/**
	 * @param project
	 * @return
	 */
	public SupportedProjectType getProjectType(IProject project)
			throws CoreException;

	/**
	 * 
	 * @param projectType
	 * @return the Eclipse project nature ID for the given project type
	 */
	public String getProjectNatureId(SupportedProjectType projectType);

	/**
	 * @param project
	 * @return the Eclipse project nature ID for the given Turmeric project
	 */
	public String getTurmericProjectNatureId(IProject project)
			throws CoreException;

	/**
	 * check whether the given Eclipse project is a valid Turmeric project
	 * 
	 * @param project
	 * @return
	 */
	public boolean isValidTurmericProject(IProject project)
			throws CoreException;

	/**
	 * Need a codegen system for each organization. it will provide the
	 * destination folder for intf/impl/consumer project and be response for
	 * calling ServiceGenerator for each organization.
	 * 
	 * @return
	 */
	public ISOACodegenProvider getSOACodegenProvider();
	
	/**
	 * @return the project health checker instance for the underlying system
	 */
	public IProjectHealthChecker getProjectHealthChecker();

}
