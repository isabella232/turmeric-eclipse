/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.maveneclipseapi.test.request;

import org.apache.maven.model.Plugin;
import org.apache.maven.model.Resource;
import org.ebayopensource.turmeric.eclipse.mavenapi.request.ProjectMavenizationRequest;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author yayu
 *
 */
public class TestProjectMavenizationRequest {

	/**
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.mavenapi.request.ProjectMavenizationRequest#createRequest(org.eclipse.core.resources.IProject)}.
	 */
	@Test
	public void testCreateRequest() {
		IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject("AndroidRocks");
		ProjectMavenizationRequest req = ProjectMavenizationRequest.createRequest(project);
		Assert.assertSame(project, req.getEclipseProject());
		
		req.setEclipseProject(project);
		Assert.assertSame(project, req.getEclipseProject());
		String outputPath = "build/classes";
		req.setOutputPath(outputPath);
		Assert.assertEquals(outputPath, req.getOutputPath());
		req.addProperty("key", "value");
		Assert.assertEquals("value", req.getProperties().get("key"));
		
		Plugin plugin = new Plugin();
		plugin.setArtifactId("hello");
		req.addBuildPlugin(plugin);
		Assert.assertSame(plugin, req.getBuildPlugins().iterator().next());
		
		Resource resDir = new Resource();
		resDir.setDirectory("android");
		req.addResourceDirectory(resDir);
		Assert.assertSame(resDir, req.getResourceDirectories().get(0));
		
		resDir = new Resource();
		resDir.setDirectory("test");
		req.addTestResourceDirectory(resDir);
		Assert.assertSame(resDir, req.getTestResourceDirectories().get(0));
	}


}
