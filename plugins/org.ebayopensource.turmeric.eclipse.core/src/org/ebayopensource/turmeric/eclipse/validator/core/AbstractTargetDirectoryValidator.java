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
package org.ebayopensource.turmeric.eclipse.validator.core;

import org.ebayopensource.turmeric.eclipse.exception.validation.ValidationInterruptedException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;


/**
 * The Class AbstractTargetDirectoryValidator.
 *
 * @author yayu
 */
public abstract class AbstractTargetDirectoryValidator extends
		AbstractSOAValidator {

	/**
	 * Instantiates a new abstract target directory validator.
	 */
	public AbstractTargetDirectoryValidator() {
		super();
	}
	
	
	/**
	 * Validate.
	 *
	 * @param object the object
	 * @return the i status
	 * @throws ValidationInterruptedException the validation interrupted exception
	 */
	@Override
	public IStatus validate(Object object) throws ValidationInterruptedException {
		IStatus status = super.validate(object);
		
		if (status.isOK() == false) {
			return status;
		}
		if (object instanceof IPath) {
			IPath path = (IPath) object;
			if (!path.isValidPath(path.toString())) {
				status = getBasicStatusModel(path
						+ " is not a valid path.");
			}
			else if (!path.toFile().isDirectory()) {
				status = getBasicStatusModel(path
						+ " is not a directory.");
			}
			else if (!path.toFile().exists()) {
				status = getBasicStatusModel(path
						+ " does not exist.");
			}
		}
		return status;
	}

}
