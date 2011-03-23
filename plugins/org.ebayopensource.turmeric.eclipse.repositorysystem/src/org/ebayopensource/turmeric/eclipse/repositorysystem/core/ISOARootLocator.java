/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.repositorysystem.core;

import org.eclipse.core.runtime.IPath;

/**
 * @author smathew
 * 
 * This interface is for the repos to implement and decide whether the project
 * root directory should be overriden. Used by Creator wizards lie
 * ServiceFromNewWSDLWizardPage
 * 
 * 
 */
public interface ISOARootLocator {

	/**
	 * @return path to the root. This is the repo root.
	 */
	public IPath getRoot();

	/**
	 * This method was written for V3 Build projects, where by default we want
	 * to override the project root directory. For all other build systems, this
	 * returns false.
	 */
	public boolean shouldOverrideProjectRootDirectory();

	/**
	 * This method returns the default value for the project directory location
	 * in the wizards. eg:AbstractSOAProjectWizardPage
	 */
	public String getDefaultProjectLocation();
}
