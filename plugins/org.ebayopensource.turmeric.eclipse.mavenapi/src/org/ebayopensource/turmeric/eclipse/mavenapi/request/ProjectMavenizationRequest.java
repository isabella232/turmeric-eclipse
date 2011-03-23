/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.mavenapi.request;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

import org.apache.maven.model.Plugin;
import org.apache.maven.model.Resource;
import org.apache.maven.repository.metadata.ArtifactMetadata;
import org.eclipse.core.resources.IProject;

/**
 * 
 * @author <a href="oleg@codehaus.org">Oleg Gusakov</a>
 * 
 */
public class ProjectMavenizationRequest extends
		AbstractProjectMavenizationRequest {
	private ArtifactMetadata parent;
	private ArtifactMetadata artifact;
	private final List<Resource> resourceDirectories = new ArrayList<Resource>();
	private final List<Resource> testResourceDirectories = new ArrayList<Resource>();
	private Collection<ArtifactMetadata> dependencies;
	private String sourcePath;
	private String testSourcePath;
	private String outputPath;
	private String testOutputPath;
	private Collection<Plugin> buildPlugins = new ArrayList<Plugin>();
	private final Properties properties = new Properties();

	// -----------------------------------------------------------------------------
	public static ProjectMavenizationRequest createRequest(IProject project) {
		final ProjectMavenizationRequest pmr = new ProjectMavenizationRequest();
		pmr.setEclipseProject(project);
		return pmr;
	}

	// -----------------------------------------------------------------------------
	public ProjectMavenizationRequest() {
		super();
	}

	// -----------------------------------------------------------------------------

	public ArtifactMetadata getParent() {
		return parent;
	}

	public List<Resource> getResourceDirectories() {
		return resourceDirectories;
	}

	public boolean addResourceDirectory(Resource resDir) {
		return resourceDirectories.add(resDir);
	}

	public List<Resource> getTestResourceDirectories() {
		return testResourceDirectories;
	}

	public boolean addTestResourceDirectory(Resource resDir) {
		return testResourceDirectories.add(resDir);
	}

	public ProjectMavenizationRequest setParent(ArtifactMetadata parent) {
		this.parent = parent;
		return this;
	}

	public ArtifactMetadata getArtifact() {
		return artifact;
	}

	public ProjectMavenizationRequest setArtifact(
			ArtifactMetadata projectMetaData) {
		this.artifact = projectMetaData;
		return this;
	}

	public Collection<ArtifactMetadata> getDependencies() {
		return dependencies;
	}

	public ProjectMavenizationRequest setDependencies(
			Collection<ArtifactMetadata> dependencies) {
		this.dependencies = dependencies;
		return this;
	}

	public String getOutputPath() {
		return outputPath;
	}

	public ProjectMavenizationRequest setOutputPath(String outputPath) {
		this.outputPath = outputPath;
		return this;
	}

	public Properties getProperties() {
		return properties;
	}

	public ProjectMavenizationRequest addProperty(String key, String value) {
		this.properties.setProperty(key, value);
		return this;
	}

	public String getSourcePath() {
		return sourcePath;
	}

	public ProjectMavenizationRequest setSourcePath(String sourcePath) {
		this.sourcePath = sourcePath;
		return this;
	}

	public String getTestSourcePath() {
		return testSourcePath;
	}

	public ProjectMavenizationRequest setTestSourcePath(String testSourcePath) {
		this.testSourcePath = testSourcePath;
		return this;
	}

	public String getTestOutputPath() {
		return testOutputPath;
	}

	public void setTestOutputPath(String testOutputPath) {
		this.testOutputPath = testOutputPath;
	}

	public Collection<Plugin> getBuildPlugins() {
		return buildPlugins;
	}

	public boolean addBuildPlugin(Plugin buildPlugin) {
		return this.buildPlugins.add(buildPlugin);
	}

	// -----------------------------------------------------------------------------
	// -----------------------------------------------------------------------------
}
