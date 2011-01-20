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
package org.ebayopensource.turmeric.eclipse.repositorysystem.core;

import java.util.Collection;
import java.util.Set;

import org.ebayopensource.turmeric.eclipse.resources.model.AssetInfo;
import org.ebayopensource.turmeric.eclipse.resources.model.ISOAProject;
import org.eclipse.swt.widgets.Shell;


/**
 * @author yayu
 *
 */
public interface ISOALibraryDependencyDialog {
	
	/**
	 * Open a repository specific library dependencies dialog for choosing library from the underlying repository system.
	 * @param shell The shell to display the dialog. This is to ensure this method is invoked in a UI thread.
	 * @param soaProject the underlying SOA project
	 * @param availableLibs pass in current library dependencies
	 * @param allLibs pass in all available libraries if there is one
	 * @return The added library or null is none
	 * @throws Exception
	 */
	public Collection<AssetInfo> open(final Shell shell, final ISOAProject soaProject, 
			final Set<? extends AssetInfo> availableLibs, final Set<? extends AssetInfo> allLibs) throws Exception;
	
}
