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
 * @author smathew
 * 
 */
public interface IErrorRegistryBridge {

	/**
	 * @return all error libraries for the underlying system
	 * @throws Exception
	 */
	public Set<AssetInfo> getErrorLibs() throws Exception;
	
	/**
	 * get the error lib view root, create the view dir structure if required.
	 * @param project
	 * @param folders folders to be created under the error lib view root
	 * @param monitor
	 * @return
	 * @throws CoreException
	 */
	public File getErrorLibraryViewRoot(IProject project, 
			IProgressMonitor monitor) throws CoreException;
	
	/**
	 * Create specific artifacts for the underlying system
	 * @param project
	 * @param monitor
	 * @throws CoreException
	 * @throws AbstractSOAException
	 * @throws IOException
	 */
	public void createPlatformSpecificArtifacts(IProject project, IProgressMonitor monitor) 
	throws CoreException, AbstractSOAException, IOException;
	
	/**
	 * @param storeLocation
	 * @param organization
	 * @param domain
	 * @return a new Error ID for the given options
	 * @throws Exception
	 */
	public long nextErrorId(String storeLocation, String organization, 
			String domain) throws Exception;

}
