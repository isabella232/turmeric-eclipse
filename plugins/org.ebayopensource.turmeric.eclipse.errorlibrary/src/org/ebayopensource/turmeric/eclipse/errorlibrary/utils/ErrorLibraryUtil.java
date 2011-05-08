/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.errorlibrary.utils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import org.ebayopensource.turmeric.eclipse.config.core.SOAGlobalConfigAccessor;
import org.ebayopensource.turmeric.eclipse.core.logging.SOALogger;
import org.ebayopensource.turmeric.eclipse.errorlibrary.providers.ErrorLibraryProviderFactory;
import org.ebayopensource.turmeric.eclipse.errorlibrary.providers.IErrorLibraryProvider;
import org.ebayopensource.turmeric.eclipse.errorlibrary.views.ISOAErrDomain;
import org.ebayopensource.turmeric.eclipse.errorlibrary.views.ISOAErrLibrary;
import org.ebayopensource.turmeric.eclipse.errorlibrary.views.ISOAErrRegistry;
import org.ebayopensource.turmeric.eclipse.errorlibrary.views.ISOAError;
import org.ebayopensource.turmeric.eclipse.exception.resources.SOAFileNotWritableException;
import org.ebayopensource.turmeric.eclipse.exception.resources.SOAGetErrorLibraryProviderFailedException;
import org.ebayopensource.turmeric.eclipse.exception.resources.projects.SOAOperationNotAvailableException;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.GlobalRepositorySystem;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;


/**
 * The Class ErrorLibraryUtil.
 *
 * @author smathew Standard Utility Class for Error Library. Contains mainly
 * helper methods for the registry.
 */
public class ErrorLibraryUtil {

	/**
	 * Lists all the errors in this registry. Regardless of the library it
	 * belongs to. Not null safe.
	 *
	 * @param errorRegistry the error registry
	 * @return the all errors
	 */
	public static List<ISOAError> getAllErrors(ISOAErrRegistry errorRegistry) {
		ArrayList<ISOAError> errors = new ArrayList<ISOAError>();
		for (ISOAErrLibrary errLibrary : errorRegistry.getLibraries()) {
			for (ISOAErrDomain domain : errLibrary.getDomains()) {
				errors.addAll(domain.getErrors());
			}
		}
		return errors;
	}

	/**
	 * Lists all the errors in a library. Not null safe.
	 *
	 * @param errorLibrary the error library
	 * @return the all errors
	 */
	public static List<ISOAError> getAllErrors(ISOAErrLibrary errorLibrary) {
		ArrayList<ISOAError> errors = new ArrayList<ISOAError>();
		for (ISOAErrDomain domain : errorLibrary.getDomains()) {
			errors.addAll(domain.getErrors());
		}
		return errors;
	}

	/**
	 * Returns the code snippet corresponding to the type of project being
	 * passed. Once the run time adds a new constructor for key, we might need
	 * to change it to accomodate it. Not null safe
	 *
	 * @param project the project
	 * @param error the error
	 * @return the import source
	 * @throws SOAOperationNotAvailableException the sOA operation not available exception
	 * @throws CoreException the core exception
	 */
	public static String getImportSource(IProject project, ISOAError error)
			throws SOAOperationNotAvailableException, CoreException {
		IErrorLibraryProvider provider = null;
		try {
			ErrorLibraryProviderFactory factory = ErrorLibraryProviderFactory.getInstance();
			provider = factory.getPreferredProvider();
		} catch (SOAGetErrorLibraryProviderFailedException e) {
			throw new SOAOperationNotAvailableException(e.getMessage());
		}
		if (provider != null) {
			return provider.getImportErrorSource(project, error);
		}

		return "";
	}
	
	/**
	 * Gets the error library central location.
	 *
	 * @return the error library central location
	 * @throws SOAFileNotWritableException the sOA file not writable exception
	 */
	public static String getErrorLibraryCentralLocation() throws SOAFileNotWritableException {
		if (SOAErrorLibraryConstants.errorLibraryCentralLocation == null) {
			try {
				String buildSystem = GlobalRepositorySystem.instanceOf().getActiveRepositorySystem().getId();
				String organization = GlobalRepositorySystem.instanceOf().getActiveRepositorySystem()
				.getActiveOrganizationProvider().getName();
				final String loc = SOAGlobalConfigAccessor.getErrorLibraryCentralLocation(buildSystem, 
						organization);
				if (loc.startsWith("http")) {
					//validate http server
					URLConnection connection = new URL(loc).openConnection();
					connection.getDate();
				} else { // indicates it's file based
					final File file = new File(loc);
					if (file.exists() && file.canWrite()) {
						SOALogger.getLogger().info(
								"Error Library central location initialized->"
								+ file);
					} else {
						throw new SOAFileNotWritableException(
								"The specified error library central location is either not exist or not writable->"
								+ file, file);
					}
				}
				SOAErrorLibraryConstants.errorLibraryCentralLocation = loc;
			} catch (IOException e) {
				throw new SOAFileNotWritableException(e);
			}
		}
		return SOAErrorLibraryConstants.errorLibraryCentralLocation;
	}
}
