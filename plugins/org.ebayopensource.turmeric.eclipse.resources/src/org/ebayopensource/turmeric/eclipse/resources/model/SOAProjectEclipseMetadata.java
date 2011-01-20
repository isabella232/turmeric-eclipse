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


/**
 * @author smathew
 *  Eclipse related information eg: Project
 */
public class SOAProjectEclipseMetadata {

	private String projName;

	private IPath workspaceLocation;
	
	private IProject project;

	public static SOAProjectEclipseMetadata create(String name, String workSpace) {
		return create(name, new Path(workSpace));
	}
	
	public static SOAProjectEclipseMetadata create(String name, IPath workSpace) {
		SOAProjectEclipseMetadata metadata = new SOAProjectEclipseMetadata();
		metadata.setProjectName(name);
		metadata.setWorkspaceLocation(workSpace);
		return metadata;
	}

	private SOAProjectEclipseMetadata() {
	}

	public String getProjectName() {
		return projName;
	}

	public void setProjectName(String name) {
		this.projName = name;
	}

	public IPath getWorkspaceLocation() {
		return workspaceLocation;
	}

	public void setWorkspaceLocation(IPath projectLocation) {
		this.workspaceLocation = projectLocation;
	}

	public IProject getProject() {
		if (project == null)
			this.project = WorkspaceUtil.getProject(projName);
		return this.project;
	}

}
