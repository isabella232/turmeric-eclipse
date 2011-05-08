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
package org.ebayopensource.turmeric.eclipse.errorlibrary.providers;

import org.ebayopensource.turmeric.eclipse.errorlibrary.ui.model.ErrorParamModel;
import org.ebayopensource.turmeric.eclipse.exception.resources.projects.SOAErrorCreationFailedException;
import org.eclipse.core.runtime.IProgressMonitor;


/**
 * The Interface IErrorTypeCreator.
 *
 * @author yayu
 * @since 1.0.0
 */
public interface IErrorTypeCreator {
	
	/**
	 * Any works before the creation.
	 *
	 * @param model the model
	 * @throws SOAErrorCreationFailedException the sOA error creation failed exception
	 */
	public void preCreation(ErrorParamModel model)
			throws SOAErrorCreationFailedException;

	/**
	 * Any necessary works after the creation.
	 *
	 * @param model the model
	 * @param monitor the monitor
	 * @throws SOAErrorCreationFailedException the sOA error creation failed exception
	 */
	public void postCreation(ErrorParamModel model, IProgressMonitor monitor)
			throws SOAErrorCreationFailedException;
}
