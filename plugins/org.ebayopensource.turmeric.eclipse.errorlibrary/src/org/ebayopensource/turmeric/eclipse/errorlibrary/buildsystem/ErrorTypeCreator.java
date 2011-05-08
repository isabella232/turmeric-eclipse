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
package org.ebayopensource.turmeric.eclipse.errorlibrary.buildsystem;

import org.ebayopensource.turmeric.eclipse.errorlibrary.providers.ErrorLibraryProviderFactory;
import org.ebayopensource.turmeric.eclipse.errorlibrary.ui.model.ErrorParamModel;
import org.eclipse.core.runtime.IProgressMonitor;


/**
 * The Class ErrorTypeCreator.
 *
 * @author yayu
 */
public final class ErrorTypeCreator {

	private static ErrorLibraryProviderFactory factory = ErrorLibraryProviderFactory.getInstance();
	
	/**
	 * Creates the error type.
	 *
	 * @param model the model
	 * @param monitor the monitor
	 * @throws Exception the exception
	 */
	public static void createErrorType(ErrorParamModel model,
			IProgressMonitor monitor) throws Exception {
		factory.getPreferredProvider().getErrorTypeCreator()
		.postCreation(model, monitor);
	}
}
