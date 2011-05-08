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
 * The Interface IErrorLibraryCreator.
 *
 * @author yayu
 */
public interface IErrorLibraryCreator {
	
	/**
	 * Pre creation.
	 *
	 * @param model the model
	 * @throws SOAErrorLibraryCreationFailedException the sOA error library creation failed exception
	 */
	public void preCreation(ErrorLibraryParamModel model) 
	throws SOAErrorLibraryCreationFailedException;
	
	/**
	 * Post creation.
	 *
	 * @param project the project
	 * @param model the model
	 * @param monitor the monitor
	 * @throws SOAErrorLibraryCreationFailedException the sOA error library creation failed exception
	 */
	public void postCreation(IProject project, ErrorLibraryParamModel model,
			IProgressMonitor monitor) throws SOAErrorLibraryCreationFailedException;
	
	/**
	 * Creates the platform specific artifacts.
	 *
	 * @param project the project
	 * @param srcFolder the src folder
	 * @param monitor the monitor
	 * @throws CoreException the core exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public void createPlatformSpecificArtifacts(IProject project, String srcFolder, 
			IProgressMonitor monitor) throws CoreException, IOException;

}
