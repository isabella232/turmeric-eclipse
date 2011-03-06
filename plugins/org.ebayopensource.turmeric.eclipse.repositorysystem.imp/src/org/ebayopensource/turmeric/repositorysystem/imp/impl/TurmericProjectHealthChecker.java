/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.repositorysystem.imp.impl;

import org.ebayopensource.turmeric.eclipse.repositorysystem.core.IProjectHealthChecker;
import org.ebayopensource.turmeric.eclipse.repositorysystem.utils.GlobalProjectHealthChecker;
import org.ebayopensource.turmeric.eclipse.utils.plugin.EclipseMessageUtils;
import org.ebayopensource.turmeric.eclipse.utils.plugin.WorkspaceUtil;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

/**
 * @author yayu
 *
 */
public class TurmericProjectHealthChecker implements IProjectHealthChecker {

	/**
	 * 
	 */
	public TurmericProjectHealthChecker() {
		super();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.ebayopensource.turmeric.eclipse.repositorysystem.core.IProjectHealthChecker#checkProjectHealth(org.eclipse.core.resources.IProject)
	 */
	public IStatus checkProjectHealth(IProject project) throws Exception {
		for (final IResource resource : GlobalProjectHealthChecker.getSOAProjectReadableResources(project)) {
			if (WorkspaceUtil.isResourceReadable(resource) == false) {
				String message = "Resource does not exist or is not readable->" + resource.getName() 
				+ ".  It is recommended to create the missing resource, but functionality may still work with out it.";
				if (resource.exists() == true) {
					return EclipseMessageUtils.createResourceErrorStatus(resource.getLocation(),  
							message, null);
				} else {
					return EclipseMessageUtils.createStatus(message, IStatus.WARNING);
				}
			}
		}
		return Status.OK_STATUS;
	}

	/**
	 * {@inheritDoc}
	 * 
	 */
	public String getWarningMessageConsumeProjectStructureOld() {
		return "";
	}

	/**
	 * {@inheritDoc}
	 * 
	 */
	public String getWarningMessageIntfProjectStructureOld() {
		return "";
	}

}
