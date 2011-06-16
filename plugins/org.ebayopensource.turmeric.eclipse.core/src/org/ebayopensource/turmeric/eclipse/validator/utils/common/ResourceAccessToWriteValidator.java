/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.validator.utils.common;

import java.io.File;

import org.ebayopensource.turmeric.eclipse.exception.validation.ValidationInterruptedException;
import org.ebayopensource.turmeric.eclipse.utils.plugin.WorkspaceUtil;
import org.ebayopensource.turmeric.eclipse.validator.core.AbstractSOAValidator;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IStatus;


/**
 * @author smathew
 * 
 * Checks whether the resource can be accessed for a write. 1)Resource should be
 * accessible. 2)Writable 3)If this does not exist, then parent folder should be
 * writable
 */
public class ResourceAccessToWriteValidator extends AbstractSOAValidator {

	/**
	 * @param obj
	 * @return
	 */
	@Override
	public IStatus validate(Object obj) throws ValidationInterruptedException {
		IStatus status = super.validate(obj);
		if (status.isOK()) {
			// First checking if its an eclipse Resource
			if (obj instanceof IResource) {
				IResource resource = (IResource) obj;
				if (WorkspaceUtil.isResourceWritable(resource)) {

				}

			} else if (obj instanceof File) {

			}
			// Second checking if its a Java IO Resource

		}
		return status;

	}

}
