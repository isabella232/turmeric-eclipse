/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.validator.utils.common;

import java.util.ArrayList;
import java.util.List;

import org.ebayopensource.turmeric.eclipse.utils.plugin.EclipseMessageUtils;
import org.ebayopensource.turmeric.eclipse.utils.plugin.WorkspaceUtil;
import org.ebayopensource.turmeric.eclipse.validator.core.AbstractSOAValidator;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;


/**
 * @author smathew This is the base class for most of the generate action
 *         validators
 */
public abstract class AbstractBaseAccessValidator extends AbstractSOAValidator {

	/**
	 * @return
	 */
	public abstract List<IResource> getWritableFiles();

	/**
	 * @return Client should return the appropriate files that are to be
	 *         validated for readability
	 */
	public abstract List<IResource> getReadableFiles();

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ebayopensource.turmeric.eclipse.validator.core.AbstractSOAValidator#validate(java.lang.Object)
	 *      Goes through the readable and writable Files and returns with the
	 *      error model if there is a discrepancy parameter doesnt matter, feel
	 *      free to pass null. because the "to be" validated files are already
	 *      decided by the extended classes
	 * 
	 */
	@Override
	public IStatus validate(Object stringFileName) {
		final List<IStatus> statues = new ArrayList<IStatus>();
		for (IResource resource : getReadableFiles()) {
			if (!WorkspaceUtil.isResourceReadable(resource)) {
				statues.add(getBasicStatusModel(resource.getLocation()
						+ " is not readable."));
			}
		}
		for (IResource resource : getWritableFiles()) {
			if (!WorkspaceUtil.isResourceWritable(resource)) {
				statues.add(getBasicStatusModel(resource.getLocation()
						+ " is not writable."));
			}
		}
		return statues.isEmpty() ? Status.OK_STATUS : 
			EclipseMessageUtils.createErrorMultiStatus(statues, 
					"Base Access Validation failed!");
	}

}
