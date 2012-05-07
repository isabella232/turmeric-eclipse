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
 * The Class ProjectMavenizationRequest.
 *
 * @author <a href="oleg@codehaus.org">Oleg Gusakov</a>
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
	private String packaging;

	// -----------------------------------------------------------------------------
	/**
	 * Creates the request.
	 *
	 * @param project the project
	 * @return the project mavenization request
	 */
	public static ProjectMavenizationRequest createRequest(IProject project) {
		final ProjectMavenizationRequest pmr = new ProjectMavenizationRequest();
		pmr.setEclipseProject(project);
		return pmr;
	}

	// -----------------------------------------------------------------------------
	/**
	 * Instantiates a new project mavenization request.
	 */
	public ProjectMavenizationRequest() {
		super();
	}

	// -----------------------------------------------------------------------------

	/**
	 * Gets the parent.
	 *
	 * @return the parent
	 */
	public ArtifactMetadata getParent() {
		return parent;
	}

	/**
	 * Gets the resource directories.
	 *
	 * @return the resource directories
	 */
	public List<Resource> getResourceDirectories() {
		return resourceDirectories;
	}

	/**
	 * Adds the resource directory.
	 *
	 * @param resDir the res dir
	 * @return true, if successful
	 */
	public boolean addResourceDirectory(Resource resDir) {
		return resourceDirectories.add(resDir);
	}

	/**
	 * Gets the test resource directories.
	 *
	 * @return the test resource directories
	 */
	public List<Resource> getTestResourceDirectories() {
		return testResourceDirectories;
	}

	/**
	 * Adds the test resource directory.
	 *
	 * @param resDir the res dir
	 * @return true, if successful
	 */
	public boolean addTestResourceDirectory(Resource resDir) {
		return testResourceDirectories.add(resDir);
	}

	/**
	 * Sets the parent.
	 *
	 * @param parent the parent
	 * @return the project mavenization request
	 */
	public ProjectMavenizationRequest setParent(ArtifactMetadata parent) {
		this.parent = parent;
		return this;
	}

	/**
	 * Gets the artifact.
	 *
	 * @return the artifact
	 */
	public ArtifactMetadata getArtifact() {
		return artifact;
	}

	/**
	 * Sets the artifact.
	 *
	 * @param projectMetaData the project meta data
	 * @return the project mavenization request
	 */
	public ProjectMavenizationRequest setArtifact(
			ArtifactMetadata projectMetaData) {
		this.artifact = projectMetaData;
		return this;
	}

	/**
	 * Gets the dependencies.
	 *
	 * @return the dependencies
	 */
	public Collection<ArtifactMetadata> getDependencies() {
		return dependencies;
	}

	/**
	 * Sets the dependencies.
	 *
	 * @param dependencies the dependencies
	 * @return the project mavenization request
	 */
	public ProjectMavenizationRequest setDependencies(
			Collection<ArtifactMetadata> dependencies) {
		this.dependencies = dependencies;
		return this;
	}

	/**
	 * Gets the output path.
	 *
	 * @return the output path
	 */
	public String getOutputPath() {
		return outputPath;
	}

	/**
	 * Sets the output path.
	 *
	 * @param outputPath the output path
	 * @return the project mavenization request
	 */
	public ProjectMavenizationRequest setOutputPath(String outputPath) {
		this.outputPath = outputPath;
		return this;
	}

	/**
	 * Gets the properties.
	 *
	 * @return the properties
	 */
	public Properties getProperties() {
		return properties;
	}

	/**
	 * Adds the property.
	 *
	 * @param key the key
	 * @param value the value
	 * @return the project mavenization request
	 */
	public ProjectMavenizationRequest addProperty(String key, String value) {
		this.properties.setProperty(key, value);
		return this;
	}

	/**
	 * Gets the source path.
	 *
	 * @return the source path
	 */
	public String getSourcePath() {
		return sourcePath;
	}

	/**
	 * Sets the source path.
	 *
	 * @param sourcePath the source path
	 * @return the project mavenization request
	 */
	public ProjectMavenizationRequest setSourcePath(String sourcePath) {
		this.sourcePath = sourcePath;
		return this;
	}

	/**
	 * Gets the test source path.
	 *
	 * @return the test source path
	 */
	public String getTestSourcePath() {
		return testSourcePath;
	}

	/**
	 * Sets the test source path.
	 *
	 * @param testSourcePath the test source path
	 * @return the project mavenization request
	 */
	public ProjectMavenizationRequest setTestSourcePath(String testSourcePath) {
		this.testSourcePath = testSourcePath;
		return this;
	}

	/**
	 * Gets the test output path.
	 *
	 * @return the test output path
	 */
	public String getTestOutputPath() {
		return testOutputPath;
	}

	/**
	 * Sets the test output path.
	 *
	 * @param testOutputPath the new test output path
	 */
	public void setTestOutputPath(String testOutputPath) {
		this.testOutputPath = testOutputPath;
	}

	/**
	 * Gets the builds the plugins.
	 *
	 * @return the builds the plugins
	 */
	public Collection<Plugin> getBuildPlugins() {
		return buildPlugins;
	}

	/**
	 * Adds the build plugin.
	 *
	 * @param buildPlugin the build plugin
	 * @return true, if successful
	 */
	public boolean addBuildPlugin(Plugin buildPlugin) {
		return this.buildPlugins.add(buildPlugin);
	}

	public void setPackaging(String packaging) {
		
		this.packaging = packaging;
	}
	public String getPackaging()
	{
		return packaging;
	}

	// -----------------------------------------------------------------------------
	// -----------------------------------------------------------------------------
}
