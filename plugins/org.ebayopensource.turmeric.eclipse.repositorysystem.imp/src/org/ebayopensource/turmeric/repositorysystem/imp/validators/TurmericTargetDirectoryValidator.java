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
package org.ebayopensource.turmeric.repositorysystem.imp.validators;

import org.ebayopensource.turmeric.eclipse.exception.validation.ValidationInterruptedException;
import org.ebayopensource.turmeric.eclipse.validator.core.AbstractTargetDirectoryValidator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;


/**
 * @author yayu
 *
 */
public class TurmericTargetDirectoryValidator extends
		AbstractTargetDirectoryValidator {

	/**
	 * 
	 */
	public TurmericTargetDirectoryValidator() {
		super();
	}
	
	public IStatus validate(Object object) throws ValidationInterruptedException {
		IStatus baseValidationModel = super.validate(object);

		if (baseValidationModel.isOK() == false) {
			return baseValidationModel;
		}

		if (object instanceof IPath) {
			//anyitng we want to validate?
		}
		return baseValidationModel;

	}

}
