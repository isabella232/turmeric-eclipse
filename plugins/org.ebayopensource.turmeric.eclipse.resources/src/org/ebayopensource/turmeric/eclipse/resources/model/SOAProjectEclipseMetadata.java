/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.resources.model;

import org.ebayopensource.turmeric.eclipse.utils.plugin.WorkspaceUtil;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;


// TODO: Auto-generated Javadoc
/**
 * The Class SOAProjectEclipseMetadata.
 *
 * @author smathew
 * Eclipse related information eg: Project
 */
public class SOAProjectEclipseMetadata {

	/** The proj name. */
	private String projName;

	/** The workspace location. */
	private IPath workspaceLocation;
	
	/** The project. */
	private IProject project;

	/**
	 * Creates an instance of a SOAProjectEclipseMetaData.
	 * @param name the project name
	 * @param workSpace the workspace location
	 * @return an instance of a SOAProjectEclipseMetaData
	 */
	public static SOAProjectEclipseMetadata create(String name, String workSpace) {
		return create(name, new Path(workSpace));
	}
	
	
	/**
	 * Creates an instance of the SOAProjectEclipseMetadata.
	 * @param name the project name
	 * @param workSpace the workspace location
	 * @return an instance of a SOAProjectEclipseMetadata
	 */
	public static SOAProjectEclipseMetadata create(String name, IPath workSpace) {
		SOAProjectEclipseMetadata metadata = new SOAProjectEclipseMetadata();
		metadata.setProjectName(name);
		metadata.setWorkspaceLocation(workSpace);
		return metadata;
	}

	/**
	 * Instantiates a new sOA project eclipse metadata.
	 */
	private SOAProjectEclipseMetadata() {
	}

	/**
	 * Gets the project name.
	 *
	 * @return the project name
	 */
	public String getProjectName() {
		return projName;
	}

	/**
	 * Sets the project name.
	 *
	 * @param name the project name
	 */
	public void setProjectName(String name) {
		this.projName = name;
	}

	/**
	 * Gets the workspace location.
	 *
	 * @return the workspace location as an IPath
	 */
	public IPath getWorkspaceLocation() {
		return workspaceLocation;
	}

	/**
	 * Sets the workspace location.
	 *
	 * @param projectLocation a project location
	 */
	public void setWorkspaceLocation(IPath projectLocation) {
		this.workspaceLocation = projectLocation;
	}

	/**
	 * Gets the project.
	 *
	 * @return an IProject
	 */
	public IProject getProject() {
		if (project == null)
			this.project = WorkspaceUtil.getProject(projName);
		return this.project;
	}

}
