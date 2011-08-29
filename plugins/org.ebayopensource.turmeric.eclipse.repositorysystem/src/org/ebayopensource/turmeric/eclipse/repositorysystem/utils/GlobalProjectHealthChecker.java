/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.repositorysystem.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.ebayopensource.turmeric.eclipse.core.resources.constants.SOAProjectConstants;
import org.ebayopensource.turmeric.eclipse.core.resources.constants.SOAProjectConstants.InterfaceSourceType;
import org.ebayopensource.turmeric.eclipse.repositorysystem.RepositorySystemActivator;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.GlobalRepositorySystem;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.IProjectHealthChecker;
import org.ebayopensource.turmeric.eclipse.resources.model.ProjectInfo;
import org.ebayopensource.turmeric.eclipse.resources.util.SOAImplUtil;
import org.ebayopensource.turmeric.eclipse.resources.util.SOAIntfUtil;
import org.ebayopensource.turmeric.eclipse.resources.util.SOAServiceUtil;
import org.ebayopensource.turmeric.eclipse.utils.plugin.EclipseMessageUtils;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IStatus;


/**
 * The Class GlobalProjectHealthChecker.
 *
 * @author yayu
 */
public final class GlobalProjectHealthChecker {

	/**
	 * 
	 */
	private GlobalProjectHealthChecker() {
		super();
	}

	/**
	 * This method is used for checking the project health status for a particular SOA project.
	 * It returns an instance of IStatus  for any issues with the project.
	 * In the case of an issue with a resource, it will return an instance of IResourceStatus,
	 * which would have the path of the underlying resource.
	 *
	 * @param project the project
	 * @return An instance of <code>IStatus</code>
	 * @throws Exception If any errors occured during the checking
	 */
	public static IStatus checkProjectHealth(final IProject project)
			throws Exception {
		if (project == null)
			return EclipseMessageUtils.createErrorStatus(
					RepositorySystemActivator.PLUGIN_ID, "Project is null",
					null);

		if (project.isAccessible() == false)
			return EclipseMessageUtils.createResourceErrorStatus(
					project.getLocation(), "Project is not accessible->"
							+ project, null);

		if (TurmericServiceUtils.isSOAProject(project) == false) {
			return EclipseMessageUtils.createResourceErrorStatus(
					project.getLocation(),
					"Project is not a valid SOA project->" + project, null);
		}

		// With standard plugins now, this is more of a Warning level. The code
		// will still run with out it, but
		// it is recommended
		IProjectHealthChecker healthChecker = GlobalRepositorySystem
				.instanceOf().getActiveRepositorySystem()
				.getProjectHealthChecker();
		return healthChecker.checkProjectHealth(project);
	}

	 /**
	  * Gets the sOA project readable resources.
	  *
	  * @param project the project
	  * @return A list of resources that are supposed to be readable for the provided SOA project.
	  * It will return an empty list if not a valid SOA project.
	  * @throws Exception the exception
	  */
	public static List<IResource> getSOAProjectReadableResources(
			final IProject project) throws Exception {
		final List<IResource> resources = new ArrayList<IResource>();

		if (TurmericServiceUtils.isSOAInterfaceProject(project)) {
			// intf projects: service_metadata.properties,
			// service_intf_project.properties
			// and the WSDL file is created from WSDL
			final String serviceName = project.getName();
			resources.add(project
					.getFile(SOAProjectConstants.PROPS_FILE_SERVICE_INTERFACE));

			// only add WSDL file if the resource type is WSDL
			final Properties props = SOAIntfUtil.loadMetadataProps(project,
					serviceName);
			if (props != null
					&& InterfaceSourceType.WSDL
							.name()
							.equals(props
									.get(SOAProjectConstants.PROPS_INTF_SOURCE_TYPE)))
				resources.add(SOAServiceUtil.getWsdlFile(project, serviceName));
		} else if (TurmericServiceUtils.isSOAImplProject(project)) {
			// impl projects: ServiceConfig.xml and
			// service_impl_project.properties
			final ProjectInfo projectInfo = GlobalRepositorySystem.instanceOf()
					.getActiveRepositorySystem().getAssetRegistry()
					.getProjectInfo(project.getName());
			if (projectInfo.getInterfaceProjectName() != null)
				resources.add(SOAImplUtil.getServiceConfigFile(project,
						projectInfo.getInterfaceProjectName()));
			resources.add(project
					.getFile(SOAProjectConstants.PROPS_FILE_SERVICE_IMPL));
		} else if (TurmericServiceUtils.isSOAConsumerProject(project)) {
			// consumer projects: service_consumer_project.properties
			resources.add(project
					.getFile(SOAProjectConstants.PROPS_FILE_SERVICE_CONSUMER));
		}

		final IFile configFile = GlobalRepositorySystem.instanceOf()
				.getActiveRepositorySystem().getAssetRegistry()
				.getProjectConfigurationFile(project);
		if (configFile != null)
			resources.add(configFile);

		return resources;
	}

	/**
	 * @param project
	 * @return A list of resources that are supposed to be writable or NOT
	 *         EXISTS for the provided SOA project. It will return an empty list
	 *         if not a valid SOA project.
	 * @throws Exception
	 */
	public static List<IResource> getSOAProjectWritableResources(
			final IProject project) throws Exception {
		final List<IResource> resources = new ArrayList<IResource>();

		if (TurmericServiceUtils.isSOAInterfaceProject(project)) {
			// codegen need the dotprotobuf file writable or non-exists.
			resources.add(SOAIntfUtil.getIntfProtoBufFile(project));
		}

		return resources;
	}

}
