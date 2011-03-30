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
package org.ebayopensource.turmeric.eclipse.buildsystem.utils;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.ebayopensource.turmeric.eclipse.core.resources.constants.SOAProjectConstants;
import org.ebayopensource.turmeric.eclipse.core.resources.constants.SOAProjectConstants.Binding;
import org.ebayopensource.turmeric.eclipse.core.resources.constants.SOAProjectConstants.SOAFrameworkLibrary;
import org.ebayopensource.turmeric.eclipse.logging.SOALogger;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.GlobalRepositorySystem;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.ISOAAssetRegistry;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.ISOAOrganizationProvider;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.ISOAProjectConfigurer;
import org.ebayopensource.turmeric.eclipse.resources.model.AssetInfo;
import org.ebayopensource.turmeric.eclipse.resources.model.ISOAProject;
import org.ebayopensource.turmeric.eclipse.resources.model.ProjectInfo;
import org.ebayopensource.turmeric.eclipse.resources.model.ISOAConsumerProject.SOAClientConfig;
import org.ebayopensource.turmeric.eclipse.resources.util.SOAClientConfigUtil;
import org.ebayopensource.turmeric.eclipse.resources.util.SOAConsumerUtil;
import org.ebayopensource.turmeric.eclipse.resources.util.SOAIntfUtil;
import org.ebayopensource.turmeric.eclipse.utils.plugin.EclipseMessageUtils;
import org.ebayopensource.turmeric.eclipse.utils.plugin.ProgressUtil;
import org.ebayopensource.turmeric.eclipse.utils.plugin.WorkspaceUtil;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;


/**
 * @author yayu
 * 
 */
public final class PropertiesUtil {
	private static final SOALogger logger = SOALogger.getLogger();

	/**
	 * 
	 */
	private PropertiesUtil() {
		super();
	}

	public static void modifyProjectProperties(final ISOAProject soaProject,
			IProgressMonitor monitor) throws Exception {
		if (SOALogger.DEBUG)
			logger.entering(soaProject, monitor);
		ProgressUtil.progressOneStep(monitor);
		ISOAProjectConfigurer configurer = GlobalRepositorySystem.instanceOf()
				.getActiveRepositorySystem().getProjectConfigurer();
		ProgressUtil.progressOneStep(monitor);

		configurer.updateProject(soaProject, false, monitor);
		ProgressUtil.progressOneStep(monitor);
		if (SOALogger.DEBUG)
			logger.exiting();
	}

	public static void modifyProjectDependencies(final String projectName,
			final Set<String> addedProjects, final Set<String> removedProjects,
			IProgressMonitor monitor) throws Exception {
		if (SOALogger.DEBUG)
			logger.entering(projectName, addedProjects, removedProjects,
					monitor);
		ProgressUtil.progressOneStep(monitor);
		ISOAProjectConfigurer configurer = GlobalRepositorySystem.instanceOf()
				.getActiveRepositorySystem().getProjectConfigurer();
		boolean userChangedSomething = false;
		for (String str : addedProjects) {
			configurer.addDependency(projectName, str, AssetInfo.TYPE_PROJECT,
					true, monitor);
			userChangedSomething = true;
		}
		ProgressUtil.progressOneStep(monitor);
		for (String str : removedProjects) {
			configurer.addDependency(projectName, str, AssetInfo.TYPE_PROJECT,
					false, monitor);
			userChangedSomething = true;
		}
		ProgressUtil.progressOneStep(monitor);
		if (userChangedSomething) {
			BuildSystemUtil.updateSOAClasspathContainer(WorkspaceUtil
					.getProject(projectName));
			WorkspaceUtil.refresh(WorkspaceUtil.getProject(projectName));
		}
		if (SOALogger.DEBUG)
			logger.exiting();
	}

	public static void modifyLibraryDependencies(final String projectName,
			final Set<String> addedLibraries,
			final Set<String> removedLibraries, IProgressMonitor monitor)
			throws Exception {
		if (SOALogger.DEBUG)
			logger.entering(projectName, addedLibraries, removedLibraries,
					monitor);
		try {
			ProgressUtil.progressOneStep(monitor);
			ISOAProjectConfigurer configurer = GlobalRepositorySystem
					.instanceOf().getActiveRepositorySystem()
					.getProjectConfigurer();
			boolean userChangedSomething = false;
			for (String str : addedLibraries) {
				configurer.addDependency(projectName, str,
						AssetInfo.TYPE_LIBRARY, true, monitor);
				userChangedSomething = true;
			}
			ProgressUtil.progressOneStep(monitor);
			for (String str : removedLibraries) {
				configurer.addDependency(projectName, str,
						AssetInfo.TYPE_LIBRARY, false, monitor);
				userChangedSomething = true;
			}
			ProgressUtil.progressOneStep(monitor);
			if (userChangedSomething) {
				BuildSystemUtil.updateSOAClasspathContainer(WorkspaceUtil
						.getProject(projectName));
				WorkspaceUtil.refresh(WorkspaceUtil.getProject(projectName));
			}
		} finally {
			if (SOALogger.DEBUG)
				logger.exiting();
		}
	}

	public static IStatus validate(String serviceName, String implProjectName,
			String newServiceLocation, String newServiceBinding)
			throws Exception {
		return validate(serviceName, implProjectName, newServiceLocation,
				StringUtils.equals(Binding.LOCAL.name(), newServiceBinding));
	}

	private static IStatus validate(String serviceName, String implProjectName,
			String newServiceLocation, boolean isLocalBindingSelected)
			throws Exception {

		if (StringUtils.isEmpty(newServiceLocation)) {
			return EclipseMessageUtils.createErrorStatus(
					"Service locaton can not be empty", null);
		}
		return validateBinding(serviceName, implProjectName,
				isLocalBindingSelected);
	}

	private static IStatus validateBinding(String serviceName,
			String implProjectName, boolean isLocalBindingSelected)
			throws Exception {
		if (isLocalBindingSelected) {
			if (StringUtils.isBlank(implProjectName)) {
				return EclipseMessageUtils
						.createErrorStatus(
								"Can not set the service binding of the selected service to LOCAL, because the implementation project [" +
								implProjectName + "] is missing",
								null);
			}
			final IProject implProject = WorkspaceUtil
					.getProject(implProjectName);
			if (implProject != null && implProject.isAccessible()) {
				return Status.OK_STATUS;
			} else {
				return EclipseMessageUtils
						.createErrorStatus(
								"Can not set the service binding of the selected service to LOCAL, because the correponding implementation project is not accessible in the current worksapce-> "
										+ implProjectName, null);
			}
		}
		return Status.OK_STATUS;
	}

	private static boolean isChangeRequired(final SOAClientConfig clientConfig,
			final String newServiceLocation, final String newServiceBinding,
			final String newMessageProtocol,
			final String newRequestDataBinding,
			final String newResponseDataBinding) {
		return !StringUtils.equalsIgnoreCase(newServiceBinding, clientConfig
				.getServiceBinding())
				|| !StringUtils.equalsIgnoreCase(newServiceLocation,
						clientConfig.getServiceLocation())
				|| !StringUtils.equalsIgnoreCase(newRequestDataBinding,
						clientConfig.getRequestDataBinding())
				|| !StringUtils.equalsIgnoreCase(newResponseDataBinding,
						clientConfig.getResponseDataBinding())
				|| !StringUtils.equalsIgnoreCase(newMessageProtocol,
						clientConfig.getMessageProtocol());

	}

	/**
	 * The caller should call the validate method before calling this function.
	 * 
	 * @param consumerProject
	 * @param serviceName
	 *            The name of the consuming service
	 * @param implProjectName
	 *            The name of the corresponding impl project name of the
	 *            service, This is an optional param, and the caller can pass
	 *            null for it
	 * @param newServiceLocation
	 * @param newServiceBinding
	 * @param newMessageProtocol
	 * @param newRequestDataBinding
	 * @param newResponseDataBinding
	 * @param requiredServices
	 *            All required services, this is used to check whether still
	 *            have Local binding service
	 * @param monitor
	 * @throws Exception
	 */
	public static void modifyServiceDependencies(
			final IProject consumerProject, final String envName, final String serviceName,
			final String implProjectName, final String newServiceLocation,
			final String newServiceBinding, final String newMessageProtocol,
			final String newRequestDataBinding,
			final String newResponseDataBinding,
			final String[] requiredServices, IProgressMonitor monitor)
			throws Exception {
		if (SOALogger.DEBUG)
			logger.entering(consumerProject, serviceName, newServiceLocation,
					newServiceBinding, newMessageProtocol,
					newRequestDataBinding, newResponseDataBinding, Arrays
							.asList(requiredServices), monitor);
		try {
			final SOAClientConfig config = SOAConsumerUtil.loadClientConfig(
					consumerProject, envName, serviceName);
			if (config != null) {
				final boolean isLocalBindingSelected = StringUtils.equals(
						Binding.LOCAL.name(), newServiceBinding);
				if (isChangeRequired(config, newServiceLocation,
						newServiceBinding, newMessageProtocol,
						newRequestDataBinding, newResponseDataBinding)) {
					ProgressUtil.progressOneStep(monitor);
					config.setServiceLocation(newServiceLocation);
					config.setServiceBinding(newServiceBinding);
					config.setMessageProtocol(newMessageProtocol);
					config.setRequestDataBinding(newRequestDataBinding);
					config.setResponseDataBinding(newResponseDataBinding);
					ProgressUtil.progressOneStep(monitor);
					String protocalProcessorClassName = GlobalRepositorySystem
					.instanceOf().getActiveRepositorySystem()
					.getActiveOrganizationProvider()
					.getSOAPProtocolProcessorClassName();
					SOAClientConfigUtil.save(config, protocalProcessorClassName);
					ProgressUtil.progressOneStep(monitor);
					performProjectDependencyChanges(consumerProject,
							envName, serviceName, implProjectName,
							isLocalBindingSelected, requiredServices, monitor);
					ProgressUtil.progressOneStep(monitor);
				}
			}
		} finally {
			if (SOALogger.DEBUG)
				logger.exiting();
		}
	}

	/**
	 * @param consumerProject
	 * @param serviceName
	 * @param implProjectName
	 * @param isLocalBindingSelected
	 * @param requiredServices
	 * @param monitor
	 * @throws Exception
	 */
	private static void performProjectDependencyChanges(
			final IProject consumerProject, String envName, final String serviceName,
			String implProjectName, final boolean isLocalBindingSelected,
			final String[] requiredServices, IProgressMonitor monitor)
			throws Exception {
		if (SOALogger.DEBUG)
			logger.entering(consumerProject, serviceName, implProjectName,
					isLocalBindingSelected, Arrays.asList(requiredServices),
					monitor);

		String consumerProjectName = consumerProject.getName();

		final ISOAProjectConfigurer projectConfigurer = GlobalRepositorySystem
				.instanceOf().getActiveRepositorySystem()
				.getProjectConfigurer();

		if (implProjectName == null) {
			logger
					.warning("The passed in implementation project name is null, "
							+ "reading from the repository system");
			final ProjectInfo intfProjectInfo = GlobalRepositorySystem
					.instanceOf().getActiveRepositorySystem()
					.getAssetRegistry().getProjectInfo(serviceName);
			implProjectName = intfProjectInfo.getImplementationProjectName();
		}
		// we have already checked in the UI whether the impl project is
		// available
		// when selected local or remote, So if its null means no change in
		// binding and if its local
		// there will always be an impl project
		if (implProjectName != null) {
			projectConfigurer.addDependency(consumerProjectName,
					implProjectName, AssetInfo.TYPE_PROJECT,
					isLocalBindingSelected, monitor);
		}
		ProgressUtil.progressOneStep(monitor);

		final ISOAOrganizationProvider orgProvider = GlobalRepositorySystem.instanceOf()
		.getActiveRepositorySystem().getActiveOrganizationProvider();
		
		if (isLocalBindingSelected) {
			projectConfigurer.addDependency(consumerProjectName,
					orgProvider.getSOAFrameworkLibraryIdentifier(SOAFrameworkLibrary.SOASERVER), AssetInfo.TYPE_LIBRARY,
					isLocalBindingSelected, monitor);
			ProgressUtil.progressOneStep(monitor);
		} else {
			boolean stillHasLocalBinding = false;
			final List<String> envList = SOAConsumerUtil.getClientEnvironmentList(
					consumerProject, monitor);
			Loop: for (String env : envList) {
				for (final String requiredServiceName : requiredServices) {
					if (serviceName.equals(requiredServiceName) == false
							|| envName.equals(env) == false ) {
						// another dependent service
						final SOAClientConfig config = SOAConsumerUtil
						.loadClientConfig(consumerProject,
								env, requiredServiceName);
						if (config != null) {
							if (Binding.LOCAL.name().equals(
									config.getServiceBinding())) {
								stillHasLocalBinding = true;
								break Loop;
							}
						}
					}
				}
			}
			ProgressUtil.progressOneStep(monitor);

			if (stillHasLocalBinding == false) {
				projectConfigurer.addDependency(consumerProjectName,
						orgProvider.getSOAFrameworkLibraryIdentifier(SOAFrameworkLibrary.SOASERVER), 
						AssetInfo.TYPE_LIBRARY,
						false, monitor);
				ProgressUtil.progressOneStep(monitor);
			}
		}
		IProject project = WorkspaceUtil.getProject(consumerProjectName);
		WorkspaceUtil.refresh(project);
		ProgressUtil.progressOneStep(monitor);

		BuildSystemUtil.updateSOAClasspathContainer(project);
		ProgressUtil.progressOneStep(monitor);
		if (SOALogger.DEBUG)
			logger.exiting();
	}

	public static String getInterfaceClassNameForService(
			final String serviceName) throws Exception {
		return getValueFromServiceProps(
				serviceName,
				SOAProjectConstants.SERVICE_METADATA_PROPS_SERVICE_INTERFACE_CLASS_NAME);
	}

	/*
	 * public static String getServiceLayerForService(final String serviceName)
	 * throws Exception { return getValueFromServiceProps(serviceName,
	 * SOAProjectConstants.SERVICE_METADATA_PROPS_SERVICE_LAYER); }
	 */

	private static String getValueFromServiceProps(final String serviceName,
			final String key) throws Exception {
		final ISOAAssetRegistry registry = GlobalRepositorySystem.instanceOf()
				.getActiveRepositorySystem().getAssetRegistry();
		final String assetLocation = registry.getAssetLocation(serviceName);
		final Properties props = SOAIntfUtil.loadMetadataProps(assetLocation,
				serviceName);
		if (props != null) {
			return StringUtils.trim(props.getProperty(key));
		}
		return null;
	}
}
