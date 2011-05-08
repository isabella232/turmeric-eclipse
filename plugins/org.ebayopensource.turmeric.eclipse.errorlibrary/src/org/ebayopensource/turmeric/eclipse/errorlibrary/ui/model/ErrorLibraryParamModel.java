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

import org.ebayopensource.turmeric.eclipse.core.model.AbstractSOAProjetParamModel;
import org.ebayopensource.turmeric.eclipse.errorlibrary.resources.SOAMessages;


/**
 * The Class ErrorLibraryParamModel.
 *
 * @author yayu
 */
public class ErrorLibraryParamModel extends AbstractSOAProjetParamModel {

	private boolean overrideWorkspaceRoot;
	private String workspaceRootDirectory;
	private String contentRepoRoot;
	private String locale;
	private String version;

	/**
	 * Instantiates a new error library param model.
	 */
	public ErrorLibraryParamModel() {
		super();
	}

	/**
	 * Gets the version.
	 *
	 * @return the version
	 */
	public String getVersion() {
		return version;
	}

	/**
	 * Sets the version.
	 *
	 * @param version the new version
	 */
	public void setVersion(String version) {
		this.version = version;
	}

	/**
	 * Gets the content repo root.
	 *
	 * @return the content repo root
	 */
	public String getContentRepoRoot() {
		return contentRepoRoot;
	}

	/**
	 * Sets the content repo root.
	 *
	 * @param contentRepoRoot the new content repo root
	 */
	public void setContentRepoRoot(String contentRepoRoot) {
		this.contentRepoRoot = contentRepoRoot;
	}

	/**
	 * Checks if is override workspace root.
	 *
	 * @return true, if is override workspace root
	 */
	public boolean isOverrideWorkspaceRoot() {
		return overrideWorkspaceRoot;
	}

	/**
	 * Sets the override workspace root.
	 *
	 * @param overrideWorkspaceRoot the new override workspace root
	 */
	public void setOverrideWorkspaceRoot(boolean overrideWorkspaceRoot) {
		this.overrideWorkspaceRoot = overrideWorkspaceRoot;
	}

	/**
	 * Gets the workspace root directory.
	 *
	 * @return the workspace root directory
	 */
	public String getWorkspaceRootDirectory() {
		return workspaceRootDirectory;
	}

	/**
	 * Sets the workspace root directory.
	 *
	 * @param workspaceRootDirectory the new workspace root directory
	 */
	public void setWorkspaceRootDirectory(String workspaceRootDirectory) {
		this.workspaceRootDirectory = workspaceRootDirectory;
	}

	/**
	 * Gets the locale.
	 *
	 * @return the locale
	 */
	public String getLocale() {
		return locale;
	}

	/**
	 * Sets the locale.
	 *
	 * @param locale the new locale
	 */
	public void setLocale(String locale) {
		this.locale = locale;
	}
	
	/* (non-Javadoc)
	 * @see org.ebayopensource.turmeric.eclipse.core.model.AbstractSOAProjetParamModel#toString()
	 */
	@Override
	public String toString() {
		final StringBuffer buf = new StringBuffer();
		buf.append(SOAMessages.UI_ERRORLIB);
		buf.append(getProjectName());
		return buf.toString();
	}
}
