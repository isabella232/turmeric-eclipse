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
package org.ebayopensource.turmeric.repositorysystem.imp.impl;

import org.ebayopensource.turmeric.eclipse.repositorysystem.core.ISOARootLocator;
import org.ebayopensource.turmeric.eclipse.utils.plugin.WorkspaceUtil;
import org.eclipse.core.runtime.IPath;


/**
 * @author yayu
 *
 */
public class TurmericRootLocator implements ISOARootLocator {
	public static final ISOARootLocator INSTANCE = new TurmericRootLocator();

	/**
	 * 
	 */
	public TurmericRootLocator() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * {@inheritDoc}
	 */
	public IPath getRoot() {
		return WorkspaceUtil.getWorkspaceRoot().getLocation();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.ebayopensource.turmeric.eclipse.repositorysystem.core.ISOARootLocator#shouldOverrideProjectRootDirectory()
	 */
	public boolean shouldOverrideProjectRootDirectory() {
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	public String getDefaultProjectLocation() {
		if(getRoot()==null)
			return "";
		return getRoot().toString();
	}

}
