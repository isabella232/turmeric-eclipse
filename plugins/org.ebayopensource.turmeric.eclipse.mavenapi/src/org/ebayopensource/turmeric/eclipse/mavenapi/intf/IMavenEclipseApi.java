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
 * @author yayu
 * 
 */
public interface IMavenEclipseApi {

	/**
	 * Parse the given file to Maven pom Model instance
	 * 
	 * @param file
	 * @return
	 * @throws MavenEclipseApiException
	 */
	public Model parsePom(File file) throws MavenEclipseApiException;

	/**
	 * Parse the given instance of InputStream to Maven pom Model instance
	 * 
	 * @param ins
	 * @return
	 * @throws MavenEclipseApiException
	 */
	public Model parsePom(InputStream ins) throws MavenEclipseApiException;

	/**
	 * Parse the pom.xml of the given Eclipse project to Maven pom Model
	 * instance
	 * 
	 * @param project
	 * @return
	 * @throws MavenEclipseApiException
	 */
	public Model parsePom(IProject project) throws MavenEclipseApiException;

	/**
	 * Persist the given Maven pom model to the target destination.
	 * 
	 * @param targetFile
	 *            the target destination
	 * @param model
	 * @throws MavenEclipseApiException
	 */
	public void writePom(IFile targetFile, Model model)
			throws MavenEclipseApiException;
	
	/**
	 * Persist the given Maven pom model to the given output stream
	 * @param output
	 * @param model
	 * @throws MavenEclipseApiException
	 */
	public void writePom(OutputStream output, Model model)
	throws MavenEclipseApiException;

	/**
	 * Convert an existing Eclipse project to a Maven project.
	 * 
	 * @param req
	 * @param monitor
	 * @return
	 * @throws MavenEclipseApiException
	 */
	public IProject mavenizeProject(ProjectMavenizationRequest req,
			IProgressMonitor monitor) throws MavenEclipseApiException;

	/**
	 * Convert an existing Eclipse project to a Maven project.
	 * 
	 * @param req
	 * @param monitor
	 * @return
	 * @throws MavenEclipseApiException
	 */
	public IProject mavenizeProject(AbstractProjectMavenizationRequest req,
			IProgressMonitor monitor) throws MavenEclipseApiException;

	/**
	 * Converting an instance of ArtifactMetadata to an instance of Artifact
	 * 
	 * @param md
	 * @return
	 * @throws MavenEclipseApiException
	 */
	public Artifact resolveArtifact(ArtifactMetadata md)
			throws MavenEclipseApiException;

	/**
	 * Resolving an instance of Artifact to an instance of MavenProject
	 * 
	 * @param artifact
	 * @return
	 * @throws MavenEclipseApiException
	 */
	public MavenProject resolveArtifactAsProject(Artifact artifact)
			throws MavenEclipseApiException;

	public List<Artifact> resolveArtifactAsClasspath(ArtifactMetadata md)
			throws MavenEclipseApiException;

	/**
	 * Resolving the given artifact, and download the data if necessary
	 * 
	 * @param artifact
	 * @return
	 * @throws MavenEclipseApiException
	 */
	public Artifact resolveArtifact(Artifact artifact)
			throws MavenEclipseApiException;

	public Collection<Artifact> findGroup(String groupRegEx)
			throws MavenEclipseApiException;

	public Collection<Artifact> findArtifact(String query)
			throws MavenEclipseApiException;

	public Collection<Artifact> findArtifactByNameAndGroup(String name,
			String group) throws MavenEclipseApiException;

}
