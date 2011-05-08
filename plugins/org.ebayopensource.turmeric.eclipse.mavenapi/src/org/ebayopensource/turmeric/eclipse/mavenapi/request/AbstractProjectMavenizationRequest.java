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
package org.ebayopensource.turmeric.eclipse.mavenapi.request;

import org.eclipse.core.resources.IProject;

/**
 * The Class AbstractProjectMavenizationRequest.
 *
 * @author yayu
 */
public abstract class AbstractProjectMavenizationRequest {
	private IProject eclipseProject;

	/**
	 * Instantiates a new abstract project mavenization request.
	 */
	public AbstractProjectMavenizationRequest() {
		super();
	}

	/**
	 * Gets the eclipse project.
	 *
	 * @return the eclipse project
	 */
	public IProject getEclipseProject() {
		return eclipseProject;
	}

	/**
	 * Sets the eclipse project.
	 *
	 * @param eclipseProject the new eclipse project
	 */
	public void setEclipseProject(IProject eclipseProject) {
		this.eclipseProject = eclipseProject;
	}

}
