/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.repositorysystem.imp.impl;

import org.ebayopensource.turmeric.eclipse.resources.model.DefaultImplementationProjectResolver;
import org.ebayopensource.turmeric.eclipse.resources.util.SOAServiceUtil;
import org.ebayopensource.turmeric.repositorysystem.imp.utils.TurmericConstants;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;

/**
 * @author yayu
 *
 */
public class TurmericImplProjectResolver extends
		DefaultImplementationProjectResolver {

	/**
	 * 
	 */
	public TurmericImplProjectResolver() {
		super();
	}

	/* (non-Javadoc)
	 * @see org.ebayopensource.turmeric.eclipse.resources.model.DefaultImplementationProjectResolver#isConsumerProject(org.eclipse.core.resources.IProject)
	 */
	@Override
	public boolean isConsumerProject(IProject project) throws CoreException {
		return SOAServiceUtil.hasNatures(project, TurmericConstants.NATURE_ID_SOA_CONSUMER_PROJECT);
	}

}
