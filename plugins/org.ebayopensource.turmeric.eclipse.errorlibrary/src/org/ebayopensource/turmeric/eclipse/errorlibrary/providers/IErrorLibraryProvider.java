/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.errorlibrary.providers;

import java.util.List;

import org.ebayopensource.turmeric.eclipse.errorlibrary.views.ISOAError;
import org.ebayopensource.turmeric.eclipse.exception.resources.projects.SOAOperationNotAvailableException;
import org.ebayopensource.turmeric.eclipse.resources.model.ISOAProject;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;


/**
 * The Interface IErrorLibraryProvider.
 *
 * @author smathew
 * @since 1.0.0
 */
public interface IErrorLibraryProvider {

	/**
	 * Gets the provider id.
	 *
	 * @return The ID of the error library content provider
	 */
	public String getProviderID();

	/**
	 * Gets the error library wizard page provider.
	 *
	 * @return an instance of the new error library wizard page provider
	 */
	public ISOAErrorLibraryWizardPageProvider getErrorLibraryWizardPageProvider();
	
	/**
	 * Gets the error wizard page provider.
	 *
	 * @return an instance of the new error wizard page provider
	 */
	public ISOAErrorLibraryWizardPageProvider getErrorWizardPageProvider();
	
	/**
	 * Gets the error domain wizard page provider.
	 *
	 * @return an instance of the new error domain wizard page provider
	 */
	public ISOAErrorLibraryWizardPageProvider getErrorDomainWizardPageProvider();
	
	/**
	 * Gets the error registry view provider.
	 *
	 * @return an instance of the error registry view provider
	 */
	public IErrorRegistryViewProvider getErrorRegistryViewProvider();
	
	/**
	 * Gets the error library creator.
	 *
	 * @return an instance of error library creator
	 */
	public IErrorLibraryCreator getErrorLibraryCreator();
	
	/**
	 * Gets the error domain creator.
	 *
	 * @return an instance of the error domain creator
	 */
	public IErrorDomainCreator getErrorDomainCreator();
	
	/**
	 * Gets the error type creator.
	 *
	 * @return an instance of the error type creator
	 */
	public IErrorTypeCreator getErrorTypeCreator();
	
	/**
	 * Gets the source sub folders.
	 *
	 * @param project the project
	 * @return a collection of sub directories
	 */
	public List<String> getSourceSubFolders(ISOAProject project);
	
	/**
	 * Gets the import error source.
	 *
	 * @param project the project
	 * @param error the error
	 * @return the import error source
	 * @throws SOAOperationNotAvailableException the sOA operation not available exception
	 * @throws CoreException the core exception
	 */
	public String getImportErrorSource(IProject project, ISOAError error) 
	throws SOAOperationNotAvailableException, CoreException;
}
