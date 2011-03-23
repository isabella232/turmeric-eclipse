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

import org.ebayopensource.turmeric.eclipse.errorlibrary.ui.model.DomainParamModel;
import org.ebayopensource.turmeric.eclipse.exception.resources.SOAErrorDomainCreationFailedException;
import org.eclipse.core.runtime.IProgressMonitor;


/**
 * @author yayu
 *
 */
public interface IErrorDomainCreator {
	public void preCreation(DomainParamModel model) 
	throws SOAErrorDomainCreationFailedException;
	
	public void postCreation(DomainParamModel model,
			IProgressMonitor monitor) throws SOAErrorDomainCreationFailedException;
}
