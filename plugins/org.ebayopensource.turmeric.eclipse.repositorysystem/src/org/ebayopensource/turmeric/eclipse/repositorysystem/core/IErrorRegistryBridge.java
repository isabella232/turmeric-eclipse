/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.repositorysystem.core;

import java.io.File;
import java.io.IOException;
import java.util.Set;

import org.ebayopensource.turmeric.eclipse.exception.AbstractSOAException;
import org.ebayopensource.turmeric.eclipse.resources.model.AssetInfo;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;


/**
 * The Interface IErrorRegistryBridge.
 *
 * @author smathew
 */
public interface IErrorRegistryBridge {

	/**
	 * Gets the error libs.
	 *
	 * @return all error libraries for the underlying system
	 * @throws Exception the exception
	 */
	public Set<AssetInfo> getErrorLibs() throws Exception;
	
	/**
	 * get the error lib view root, create the view dir structure if required.
	 *
	 * @param project the project
	 * @param monitor the monitor
	 * @return the error library view root
	 * @throws CoreException the core exception
	 */
	public File getErrorLibraryViewRoot(IProject project, 
			IProgressMonitor monitor) throws CoreException;
	
	/**
	 * Create specific artifacts for the underlying system.
	 *
	 * @param project the project
	 * @param monitor the monitor
	 * @throws CoreException the core exception
	 * @throws AbstractSOAException the abstract soa exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public void createPlatformSpecificArtifacts(IProject project, IProgressMonitor monitor) 
	throws CoreException, AbstractSOAException, IOException;
	
	/**
	 * Next error id.
	 *
	 * @param storeLocation the store location
	 * @param organization the organization
	 * @param domain the domain
	 * @return a new Error ID for the given options
	 * @throws Exception the exception
	 */
	public long nextErrorId(String storeLocation, String organization, 
			String domain) throws Exception;

}
