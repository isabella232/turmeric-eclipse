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
package org.ebayopensource.turmeric.eclipse.mavenapi.intf;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collection;
import java.util.List;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.model.Model;
import org.apache.maven.project.MavenProject;
import org.apache.maven.repository.metadata.ArtifactMetadata;
import org.ebayopensource.turmeric.eclipse.mavenapi.exception.MavenEclipseApiException;
import org.ebayopensource.turmeric.eclipse.mavenapi.request.AbstractProjectMavenizationRequest;
import org.ebayopensource.turmeric.eclipse.mavenapi.request.ProjectMavenizationRequest;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IProgressMonitor;

/**
 * The Interface IMavenEclipseApi.
 *
 * @author yayu
 */
public interface IMavenEclipseApi {

	/**
	 * Parse the given file to Maven pom Model instance.
	 *
	 * @param file the file
	 * @return the model
	 * @throws MavenEclipseApiException the maven eclipse api exception
	 */
	public Model parsePom(File file) throws MavenEclipseApiException;

	/**
	 * Parse the given instance of InputStream to Maven pom Model instance.
	 *
	 * @param ins the ins
	 * @return the model
	 * @throws MavenEclipseApiException the maven eclipse api exception
	 */
	public Model parsePom(InputStream ins) throws MavenEclipseApiException;

	/**
	 * Parse the pom.xml of the given Eclipse project to Maven pom Model
	 * instance.
	 *
	 * @param project the project
	 * @return the model
	 * @throws MavenEclipseApiException the maven eclipse api exception
	 */
	public Model parsePom(IProject project) throws MavenEclipseApiException;

	/**
	 * Persist the given Maven pom model to the target destination.
	 *
	 * @param targetFile the target destination
	 * @param model the model
	 * @throws MavenEclipseApiException the maven eclipse api exception
	 */
	public void writePom(IFile targetFile, Model model)
			throws MavenEclipseApiException;
	
	/**
	 * Persist the given Maven pom model to the given output stream.
	 *
	 * @param output the output
	 * @param model the model
	 * @throws MavenEclipseApiException the maven eclipse api exception
	 */
	public void writePom(OutputStream output, Model model)
	throws MavenEclipseApiException;

	/**
	 * Convert an existing Eclipse project to a Maven project.
	 *
	 * @param req the req
	 * @param monitor the monitor
	 * @return the i project
	 * @throws MavenEclipseApiException the maven eclipse api exception
	 */
	public IProject mavenizeProject(ProjectMavenizationRequest req,
			IProgressMonitor monitor) throws MavenEclipseApiException;

	/**
	 * Convert an existing Eclipse project to a Maven project.
	 *
	 * @param req the req
	 * @param monitor the monitor
	 * @return the i project
	 * @throws MavenEclipseApiException the maven eclipse api exception
	 */
	public IProject mavenizeProject(AbstractProjectMavenizationRequest req,
			IProgressMonitor monitor) throws MavenEclipseApiException;

	/**
	 * Converting an instance of ArtifactMetadata to an instance of Artifact.
	 *
	 * @param md the md
	 * @return the artifact
	 * @throws MavenEclipseApiException the maven eclipse api exception
	 */
	public Artifact resolveArtifact(ArtifactMetadata md)
			throws MavenEclipseApiException;

	/**
	 * Resolving an instance of Artifact to an instance of MavenProject.
	 *
	 * @param artifact the artifact
	 * @return the maven project
	 * @throws MavenEclipseApiException the maven eclipse api exception
	 */
	public MavenProject resolveArtifactAsProject(Artifact artifact)
			throws MavenEclipseApiException;

	/**
	 * Resolve artifact as classpath.
	 *
	 * @param md the md
	 * @return the list
	 * @throws MavenEclipseApiException the maven eclipse api exception
	 */
	public List<Artifact> resolveArtifactAsClasspath(ArtifactMetadata md)
			throws MavenEclipseApiException;

	/**
	 * Resolving the given artifact, and download the data if necessary.
	 *
	 * @param artifact the artifact
	 * @return the artifact
	 * @throws MavenEclipseApiException the maven eclipse api exception
	 */
	public Artifact resolveArtifact(Artifact artifact)
			throws MavenEclipseApiException;

	/**
	 * Find group.
	 *
	 * @param groupRegEx the group reg ex
	 * @return the collection
	 * @throws MavenEclipseApiException the maven eclipse api exception
	 */
	public Collection<Artifact> findGroup(String groupRegEx)
			throws MavenEclipseApiException;

	/**
	 * Find artifact.
	 *
	 * @param query the query
	 * @return the collection
	 * @throws MavenEclipseApiException the maven eclipse api exception
	 */
	public Collection<Artifact> findArtifact(String query)
			throws MavenEclipseApiException;

	/**
	 * Find artifact by name and group.
	 *
	 * @param name the name
	 * @param group the group
	 * @return the collection
	 * @throws MavenEclipseApiException the maven eclipse api exception
	 */
	public Collection<Artifact> findArtifactByNameAndGroup(String name,
			String group) throws MavenEclipseApiException;

}
