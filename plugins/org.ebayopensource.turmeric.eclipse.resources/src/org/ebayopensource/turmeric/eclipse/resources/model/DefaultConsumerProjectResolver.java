/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.resources.model;

import org.ebayopensource.turmeric.eclipse.resources.util.SOAConsumerUtil;
import org.ebayopensource.turmeric.eclipse.resources.util.SOAServiceUtil;

/**
 * The Class DefaultConsumerProjectResolver.
 *
 * @author yayu
 */
public class DefaultConsumerProjectResolver implements ISOAProjectResolver<SOAConsumerProject> {

	/**
	 * Instantiates a new default consumer project resolver.
	 */
	public DefaultConsumerProjectResolver() {
		super();
	}

	/* (non-Javadoc)
	 * @see org.ebayopensource.turmeric.eclipse.resources.model.ISOAProjectResolver#loadProject(org.ebayopensource.turmeric.eclipse.resources.model.SOAProjectEclipseMetadata)
	 */
	@Override
	public SOAConsumerProject loadProject(
			SOAProjectEclipseMetadata eclipseMetadata) throws Exception {
		SOAConsumerMetadata consumerMetadata = SOAServiceUtil.getSOAConsumerMetadata(eclipseMetadata);
		final SOAConsumerProject consumerProject = SOAConsumerProject
				.create(consumerMetadata, eclipseMetadata);
		SOAConsumerUtil.loadClientConfigs(consumerProject);
		return consumerProject;
	}

}
