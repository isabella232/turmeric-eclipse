/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.validator.core;

import org.ebayopensource.turmeric.eclipse.exception.validation.ValidationInterruptedException;
import org.ebayopensource.turmeric.eclipse.utils.plugin.EclipseMessageUtils;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;



/**
 * The Class AbstractSOAValidator.
 *
 * @author smathew
 * Abstract class to make sure that nobody instantiate and use it as such
 * Contains common validation and a convenience method to create
 * a  Base Status Model object.
 * There could be validators which doesnt extend from this clas.
 */
public abstract class AbstractSOAValidator implements ISOAValidator{
	
	/**
	 * validates Null
	 * Implementation classes should be aware that the
	 * method is null safe and will return a non empty model
	 * if fed with null.
	 * 
	 * (non-Javadoc)
	 *
	 * @param obj the obj
	 * @return the i status
	 * @throws ValidationInterruptedException the validation interrupted exception
	 * @see org.ebayopensource.turmeric.eclipse.validator.core.ISOAValidator#validate(java.lang.Object)
	 */
	public IStatus validate(Object obj) throws ValidationInterruptedException{
		if (obj == null) {
			return getBasicStatusModel("The validation object specified is null.");
		}
		
		return Status.OK_STATUS;
	}
	
	/**
	 * Gets the basic status model.
	 *
	 * @param message the message
	 * @return the basic status model
	 */
	protected IStatus getBasicStatusModel(String message) {
		return getBasicStatusModel(message, IStatus.ERROR);
	}
	
	/**
	 * Gets the basic status model.
	 *
	 * @param message the message
	 * @param severity the severity
	 * @return the basic status model
	 */
	protected IStatus getBasicStatusModel(String message, int severity) {
		if (severity == IStatus.ERROR)
			return EclipseMessageUtils.createErrorStatus(message);
		else
			return EclipseMessageUtils.createStatus(message, severity);
	}
}
