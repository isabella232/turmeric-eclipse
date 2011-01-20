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

import org.apache.maven.model.Model;

/**
 * This is a request for passing in the raw POM model instance
 * 
 * @author yayu
 * 
 */
public class ProjectMavenizationRequestRaw extends
		AbstractProjectMavenizationRequest {
	private Model mavenModel;

	/**
	 * 
	 */
	public ProjectMavenizationRequestRaw() {
		super();
	}

	public Model getMavenModel() {
		return mavenModel;
	}

	public void setMavenModel(Model mavenModel) {
		this.mavenModel = mavenModel;
	}

}
