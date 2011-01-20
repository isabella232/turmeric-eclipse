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
 * @author smathew
 * @since 1.0.0
 */
public interface IErrorLibraryProvider {

	/**
	 * @return The ID of the error library content provider
	 */
	public String getProviderID();

	/**
	 * @return an instance of the new error library wizard page provider
	 */
	public ISOAErrorLibraryWizardPageProvider getErrorLibraryWizardPageProvider();
	
	/**
	 * @return an instance of the new error wizard page provider
	 */
	public ISOAErrorLibraryWizardPageProvider getErrorWizardPageProvider();
	
	/**
	 * @return an instance of the new error domain wizard page provider
	 */
	public ISOAErrorLibraryWizardPageProvider getErrorDomainWizardPageProvider();
	
	/**
	 * @return an instance of the error registry view provider
	 */
	public IErrorRegistryViewProvider getErrorRegistryViewProvider();
	
	/**
	 * @return an instance of error library creator
	 */
	public IErrorLibraryCreator getErrorLibraryCreator();
	
	/**
	 * @return an instance of the error domain creator
	 */
	public IErrorDomainCreator getErrorDomainCreator();
	
	/**
	 * @return an instance of the error type creator
	 */
	public IErrorTypeCreator getErrorTypeCreator();
	
	/**
	 * @return a collection of sub directories
	 */
	public List<String> getSourceSubFolders(ISOAProject project);
	
	/**
	 * @param source code generated for feature 'Import SOA Error'
	 * @return
	 */
	public String getImportErrorSource(IProject project, ISOAError error) 
	throws SOAOperationNotAvailableException, CoreException;
	
	public String getErrorLibraryCentralLocation();
}
