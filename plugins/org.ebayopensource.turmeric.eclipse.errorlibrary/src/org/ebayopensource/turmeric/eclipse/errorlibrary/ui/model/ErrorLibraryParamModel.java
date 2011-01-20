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
package org.ebayopensource.turmeric.eclipse.errorlibrary.ui.model;

import org.ebayopensource.turmeric.eclipse.errorlibrary.resources.SOAMessages;
import org.ebayopensource.turmeric.eclipse.resources.ui.model.AbstractSOAProjetParamModel;


/**
 * @author yayu
 * 
 */
public class ErrorLibraryParamModel extends AbstractSOAProjetParamModel {

	private boolean overrideWorkspaceRoot;
	private String workspaceRootDirectory;
	private String contentRepoRoot;
	private String locale;
	private String version;

	/**
	 * 
	 */
	public ErrorLibraryParamModel() {
		super();
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getContentRepoRoot() {
		return contentRepoRoot;
	}

	public void setContentRepoRoot(String contentRepoRoot) {
		this.contentRepoRoot = contentRepoRoot;
	}

	public boolean isOverrideWorkspaceRoot() {
		return overrideWorkspaceRoot;
	}

	public void setOverrideWorkspaceRoot(boolean overrideWorkspaceRoot) {
		this.overrideWorkspaceRoot = overrideWorkspaceRoot;
	}

	public String getWorkspaceRootDirectory() {
		return workspaceRootDirectory;
	}

	public void setWorkspaceRootDirectory(String workspaceRootDirectory) {
		this.workspaceRootDirectory = workspaceRootDirectory;
	}

	public String getLocale() {
		return locale;
	}

	public void setLocale(String locale) {
		this.locale = locale;
	}
	
	@Override
	public String toString() {
		final StringBuffer buf = new StringBuffer();
		buf.append(SOAMessages.UI_ERRORLIB);
		buf.append(getProjectName());
		return buf.toString();
	}
}
