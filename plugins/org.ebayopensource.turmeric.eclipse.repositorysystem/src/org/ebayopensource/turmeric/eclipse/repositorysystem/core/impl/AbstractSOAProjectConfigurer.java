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
package org.ebayopensource.turmeric.eclipse.repositorysystem.core.impl;

import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.ebayopensource.turmeric.eclipse.core.resources.constants.SOAProjectConstants;
import org.ebayopensource.turmeric.eclipse.core.resources.constants.SOAProjectConstants.SupportedProjectType;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.GlobalRepositorySystem;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.ISOAProjectConfigurer;
import org.ebayopensource.turmeric.eclipse.resources.model.ISOAProject;
import org.ebayopensource.turmeric.eclipse.resources.model.SOAConsumerProject;
import org.ebayopensource.turmeric.eclipse.resources.model.SOAImplProject;
import org.ebayopensource.turmeric.eclipse.resources.model.SOAIntfMetadata;
import org.ebayopensource.turmeric.eclipse.resources.model.SOAIntfProject;
import org.ebayopensource.turmeric.eclipse.resources.util.SOAConsumerUtil;
import org.ebayopensource.turmeric.eclipse.resources.util.SOAImplUtil;
import org.ebayopensource.turmeric.eclipse.resources.util.SOAIntfUtil;
import org.ebayopensource.turmeric.eclipse.resources.util.SOAServiceUtil;
import org.ebayopensource.turmeric.eclipse.utils.plugin.ProgressUtil;
import org.ebayopensource.turmeric.eclipse.utils.plugin.WorkspaceUtil;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IProgressMonitor;
import org.osgi.framework.Version;


/**
 * @author yayu
 *
 */
public abstract class AbstractSOAProjectConfigurer implements
		ISOAProjectConfigurer {

	/**
	 * 
	 */
	public AbstractSOAProjectConfigurer() {
		super();
	}

	/* (non-Javadoc)
	 * @see org.ebayopensource.turmeric.eclipse.repositorysystem.core.ISOAProjectConfigurer#updateProject(org.ebayopensource.turmeric.eclipse.resources.model.ISOAProject, boolean)
	 */
	public boolean updateProject(ISOAProject soaProject, boolean updateClasspath, IProgressMonitor monitor)
			throws Exception {
		boolean result = false;
		if (soaProject instanceof SOAIntfProject) {
			result = saveIntfProject((SOAIntfProject) soaProject, monitor);
		} else if (soaProject instanceof SOAImplProject) {
			result = saveImplProject((SOAImplProject) soaProject, monitor);
		} else if (soaProject instanceof SOAConsumerProject) {
			result = saveConsumerProject((SOAConsumerProject) soaProject, monitor);
		}
		return result;
	}

	private boolean saveIntfProject(final SOAIntfProject intfProject, IProgressMonitor monitor)
	throws Exception {
		final SOAIntfMetadata intfMetadata = intfProject.getMetadata();
		final String newServiceVersion = intfMetadata.getServiceVersion();
		if (intfMetadata.getImlementationProjectName() != null) {
			final IProject implProject = WorkspaceUtil.getProject(intfMetadata.getImlementationProjectName());
			if (implProject != null && implProject.isAccessible()) {
				SOAImplProject imProject = (SOAImplProject)SOAServiceUtil.loadSOAProject(implProject, 
						GlobalRepositorySystem.instanceOf().getActiveRepositorySystem()
						.getProjectNatureId(SupportedProjectType.IMPL));
				ProgressUtil.progressOneStep(monitor, 5);
				if (imProject != null && newServiceVersion.equals(
						imProject.getMetadata().getImplVersion()) == false) {
					//make sure the service name is available
					imProject.getMetadata().setIntfMetadata(intfMetadata);
					SOAImplUtil.loadServiceConfig(imProject, intfMetadata.getServiceName());
					ProgressUtil.progressOneStep(monitor, 5);
					final IFile svcConfigFile = imProject.getServiceConfigFile();
					imProject.getMetadata().setImplVersion(newServiceVersion);
					// plugin should never modify the ServiceConfig.xml since soa 2.9
					// ConfigTool.saveServerConfig(imProject.getMetadata(),
					// svcConfigFile);
					svcConfigFile.refreshLocal(IResource.DEPTH_ZERO, monitor);
					ProgressUtil.progressOneStep(monitor, 10);
				}
			}
		}
		final Properties props = 
			SOAIntfUtil.loadMetadataProps(intfProject.getProject(), intfMetadata.getServiceName());
		if (props != null) {
			final String oldVersion = StringUtils.trim(props.getProperty(
					SOAProjectConstants.SERVICE_METADATA_PROPS_SERVICE_VERSION));
			if (newServiceVersion.equals(oldVersion) == false) {
				final Version newVersion = new Version(newServiceVersion);
				final String wsdlVersion = newVersion.getMajor()
						+ SOAProjectConstants.DELIMITER_DOT
						+ newVersion.getMinor()
						+ SOAProjectConstants.DELIMITER_DOT
						+ newVersion.getMicro();
				SOAIntfUtil.modifyWsdlAppInfoVersion(intfProject.getProject(),
						wsdlVersion, monitor);
				serviceVersionChanged(intfMetadata.getServiceName(),
						newServiceVersion);
			}
		}
		SOAIntfUtil.saveMetadataProps(intfProject, monitor);
		ProgressUtil.progressOneStep(monitor, 10);
		return true;
	}
	
	protected abstract void serviceVersionChanged(final String serviceName, 
			final String newServiceVersion);

	private boolean saveImplProject(final SOAImplProject implProject, IProgressMonitor monitor)
	throws Exception {
		final IFile svcConfigFile = implProject.getServiceConfigFile();
		// plugin should never modify the ServiceConfig.xml since soa 2.9
		// ConfigTool.saveServerConfig(implProject.getMetadata(),
		// svcConfigFile);
		svcConfigFile.refreshLocal(IResource.DEPTH_ZERO, monitor);
		ProgressUtil.progressOneStep(monitor);
		return true;
	}

	private boolean saveConsumerProject(final SOAConsumerProject consumerProject, IProgressMonitor monitor)
	throws Exception {
		SOAConsumerUtil.saveImplMetadataProps(consumerProject);
		ProgressUtil.progressOneStep(monitor);
		return true;
	}

	@Override
	public void postServiceVersionUpdated(SOAIntfProject soaIntfProject,
			String oldVersion, String newVersion, boolean slience,
			IProgressMonitor monitor) throws Exception {
		// do nothing by default. V3 need to do a build service.
	}

}
