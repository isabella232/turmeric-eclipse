/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.resources.model;

import org.apache.commons.lang.StringUtils;
import org.ebayopensource.turmeric.eclipse.logging.SOALogger;
import org.ebayopensource.turmeric.eclipse.resources.util.SOAConsumerUtil;
import org.ebayopensource.turmeric.eclipse.resources.util.SOAImplUtil;
import org.ebayopensource.turmeric.eclipse.resources.util.SOAServiceUtil;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;

/**
 * @author yayu
 *
 */
public abstract class DefaultImplementationProjectResolver implements
		ISOAProjectResolver<SOAImplProject> {
	private static final SOALogger logger = SOALogger.getLogger();

	/**
	 * 
	 */
	public DefaultImplementationProjectResolver() {
		super();
	}

	/* 
	 * for impl projects, do the followings: 1) create the
	 * SOAProjectEclipseMetadata 2) read metadata from
	 * service_impl_project.properties 3) load the metadata from
	 * ServiceConfig.xml 4) load the corresponding intf project metadata
	 * 
	 * (non-Javadoc)
	 * @see org.ebayopensource.turmeric.eclipse.resources.model.ISOAProjectResolver#loadProject(org.ebayopensource.turmeric.eclipse.resources.model.SOAProjectEclipseMetadata)
	 */
	@Override
	public SOAImplProject loadProject(SOAProjectEclipseMetadata eclipseMetadata)
			throws Exception {
		SOAImplMetadata implMetadata = SOAServiceUtil.getSOAImplMetadata(eclipseMetadata);
		final SOAImplProject implProject = SOAImplProject.create(
				implMetadata, eclipseMetadata);
		// we might not know the service name right now, should read it
		// later
		if (StringUtils.isNotBlank(implMetadata.getIntfMetadata()
				.getServiceName()))
			SOAImplUtil.loadServiceConfig(implProject, implMetadata
					.getIntfMetadata().getServiceName());
		else {
			if (SOALogger.DEBUG)
				logger.debug("The corresponding service name is empty, service config could not be loaded.");
		}
		SOAImplUtil.fillMetadata(eclipseMetadata, implProject);
		if (isConsumerProject(eclipseMetadata.getProject())) {
			SOAConsumerUtil.loadClientConfigs(implProject);
		}
		return implProject;
	}
	
	/**
	 * @param project
	 * @return true if the given project is also a consumer project
	 */
	public abstract boolean isConsumerProject(IProject project) throws CoreException;

}
