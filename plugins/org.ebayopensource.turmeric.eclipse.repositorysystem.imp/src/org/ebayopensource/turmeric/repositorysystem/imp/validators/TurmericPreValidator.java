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
import org.ebayopensource.turmeric.eclipse.validator.core.AbstractSOAValidator;
import org.ebayopensource.turmeric.eclipse.validator.core.ISOAPreValidator;
import org.eclipse.core.runtime.IStatus;


/**
 * @author yayu
 *
 */
public class TurmericPreValidator extends AbstractSOAValidator implements
		ISOAPreValidator {

	/**
	 * 
	 */
	public TurmericPreValidator() {
		super();
	}

	/* (non-Javadoc)
	 * @see org.ebayopensource.turmeric.eclipse.validator.core.ISOAPreValidator#validateProjectCreation(java.lang.Object)
	 */
	public IStatus validateProjectCreation(Object obj)
			throws ValidationInterruptedException {
		return super.validate(obj);
	}

}
