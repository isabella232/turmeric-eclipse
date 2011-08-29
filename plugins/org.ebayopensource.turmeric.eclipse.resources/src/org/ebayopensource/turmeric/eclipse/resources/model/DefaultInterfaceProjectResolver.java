/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.resources.model;

import org.ebayopensource.turmeric.eclipse.resources.util.SOAIntfUtil;
import org.ebayopensource.turmeric.eclipse.resources.util.SOAServiceUtil;

// TODO: Auto-generated Javadoc
/**
 * The Class DefaultInterfaceProjectResolver.
 *
 * @author yayu
 */
public class DefaultInterfaceProjectResolver implements ISOAProjectResolver<SOAIntfProject> {

	/**
	 * Instantiates a new default interface project resolver.
	 */
	public DefaultInterfaceProjectResolver() {
		super();
	}

	/* 
	 * for Intf projects, do the following steps: 1) create the
	 * SOAProjectEclipseMetadata 2) read the metadata from
	 * service_intf_project.properties 3) load the metadata from
	 * service_metadata.properties
	 * (non-Javadoc)
	 * @see org.ebayopensource.turmeric.eclipse.resources.model.ISOAProjectResolver#loadProject(org.ebayopensource.turmeric.eclipse.resources.model.SOAProjectEclipseMetadata)
	 */
	@Override
	public SOAIntfProject loadProject(SOAProjectEclipseMetadata eclipseMetadata)
			throws Exception {
		
		SOAIntfMetadata intfMetadata = SOAServiceUtil.getSOAIntfMetadata(eclipseMetadata);
		SOAIntfUtil.fillMetadata(eclipseMetadata.getProject(), intfMetadata);
		return SOAIntfProject.create(intfMetadata, eclipseMetadata);
	}

}
