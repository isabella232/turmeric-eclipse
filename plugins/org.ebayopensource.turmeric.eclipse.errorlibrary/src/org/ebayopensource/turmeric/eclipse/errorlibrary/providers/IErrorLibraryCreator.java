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

import java.io.IOException;

import org.ebayopensource.turmeric.eclipse.errorlibrary.ui.model.ErrorLibraryParamModel;
import org.ebayopensource.turmeric.eclipse.exception.resources.projects.SOAErrorLibraryCreationFailedException;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;


/**
 * @author yayu
 *
 */
public interface IErrorLibraryCreator {
	
	public void preCreation(ErrorLibraryParamModel model) 
	throws SOAErrorLibraryCreationFailedException;
	
	public void postCreation(IProject project, ErrorLibraryParamModel model,
			IProgressMonitor monitor) throws SOAErrorLibraryCreationFailedException;
	
	public void createPlatformSpecificArtifacts(IProject project, String srcFolder, 
			IProgressMonitor monitor) throws CoreException, IOException;

}
