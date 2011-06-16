/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.validator.utils.common;

import org.ebayopensource.turmeric.eclipse.exception.validation.ValidationInterruptedException;
import org.ebayopensource.turmeric.eclipse.utils.plugin.WorkspaceUtil;
import org.ebayopensource.turmeric.eclipse.validator.core.AbstractSOAValidator;
import org.eclipse.core.runtime.IStatus;


/**
 * @author smathew
 * 
 * Validates the workspace
 * 
 */
public class ProjectWorkspaceValidator extends AbstractSOAValidator {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ebayopensource.turmeric.eclipse.validator.core.AbstractSOAValidator#validate(java.lang.Object)
	 *      This validator is the eclipse counter part for Project Fle System
	 *      validator. It checks if a project can be created with the given name
	 *      Expected argument is project Name as a String parameter
	 * 
	 */
	@Override
	public IStatus validate(Object obj) throws ValidationInterruptedException {
		IStatus status = super.validate(obj);
		if (status.isOK()) {
			if (obj instanceof String) {
				if (WorkspaceUtil.projectExistsInWorkSpace((String) obj)) {
					status = getBasicStatusModel("Project already exists in workspace: "
							+ obj + " select another name.");
				}
			}
		}
		return status;
	}

}
