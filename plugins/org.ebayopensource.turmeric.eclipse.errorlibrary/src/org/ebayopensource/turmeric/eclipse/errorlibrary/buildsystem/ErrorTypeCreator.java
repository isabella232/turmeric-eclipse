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
 * @author yayu
 *
 */
public final class ErrorTypeCreator {

	/**
	 * 
	 */
	private ErrorTypeCreator() {
		super();
	}

	public static void createErrorType(ErrorParamModel model,
			IProgressMonitor monitor) throws Exception {
		ErrorLibraryProviderFactory.getPreferredProvider().getErrorTypeCreator()
		.postCreation(model, monitor);
	}
}
