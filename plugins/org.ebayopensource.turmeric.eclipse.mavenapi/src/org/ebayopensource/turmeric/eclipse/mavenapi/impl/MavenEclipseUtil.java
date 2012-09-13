/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.mavenapi.impl;

import static org.apache.commons.lang.StringUtils.isNotBlank;
import static org.ebayopensource.turmeric.eclipse.mavenapi.impl.MavenApiHelper.getMavenModelManager;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang.StringUtils;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.ArtifactScopeEnum;
import org.apache.maven.model.Dependency;
import org.apache.maven.model.Exclusion;
import org.apache.maven.model.Model;
import org.apache.maven.repository.metadata.ArtifactMetadata;
import org.ebayopensource.turmeric.eclipse.mavenapi.exception.MavenEclipseApiException;
import org.ebayopensource.turmeric.eclipse.mavenapi.internal.collections.ListUtil;
import org.ebayopensource.turmeric.eclipse.mavenapi.internal.util.EclipseUtil;
import org.ebayopensource.turmeric.eclipse.mavenapi.internal.util.ThrowableUtil;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.m2e.core.internal.IMavenConstants;
import org.eclipse.m2e.core.internal.index.IndexedArtifactFile;

/**
 * The Class MavenEclipseUtil.
 *
 * @author James Ervin
 */
public final class MavenEclipseUtil {
	/**
	 * Artifact meta data.
	 */
	static final String ARTIFACT_METADATA_SEPARATOR = ":";

	/**
	 * Check to see if the project has a maven nature.
	 *
	 * @param project an eclipse project
	 * @return true if the project is a maven project, false otherwise
	 * @throws CoreException the core exception
	 */
	public static boolean hasMavenNature(final IProject project)
			throws CoreException {
		if (project == null || !project.isAccessible())
			return false;
		return project.hasNature(IMavenConstants.NATURE_ID);
	}

	/**
	 * Gets the all maven projects in workspace.
	 *
	 * @return a List of projects that are Maven Projects
	 * @throws CoreException the core exception
	 */
	public static List<IProject> getAllMavenProjectsInWorkspace()
			throws CoreException {
		final List<IProject> projects = ListUtil.list();
		for (final IProject project : EclipseUtil.projects()) {
			if (project == null || !project.isAccessible()
					|| !project.hasNature(IMavenConstants.NATURE_ID))
				continue;
			projects.add(project);
		}
		return projects;
	}

	/**
	 * Read pom.
	 *
	 * @param project the project
	 * @return the model
	 * @throws CoreException the core exception
	 */
	public static Model readPOM(final IProject project) throws CoreException {
		IFile file = getPomFile(project);
		file.refreshLocal(IResource.DEPTH_ZERO, null);
		if (file.isAccessible() == false)
			return null;
		return getMavenModelManager().readMavenModel(file);
	}

	/**
	 * Gets the all project artifacts in workspace.
	 *
	 * @return the all project artifacts in workspace
	 */
	public static Map<ArtifactMetadata, IProject> getAllProjectArtifactsInWorkspace() {
		try {
			final Map<ArtifactMetadata, IProject> projects = new ConcurrentHashMap<ArtifactMetadata, IProject>();
			for (final IProject project : getAllMavenProjectsInWorkspace()) {
				if (project == null || !project.isAccessible())
					continue;
				final Model pom = readPOM(project);
				projects.put(MavenEclipseUtil.convertToArtifactMetadata(pom),
						project);
			}
			return projects;
		} catch (final CoreException e) {
			throw ThrowableUtil.adaptToRuntimeException(e);
		}
	}

	/**
	 * Gets the all project artifact ids in workspace.
	 *
	 * @return the all project artifact ids in workspace
	 */
	public static Map<String, IProject> getAllProjectArtifactIdsInWorkspace() {
		final Map<String, IProject> projects = new ConcurrentHashMap<String, IProject>();
		final Map<ArtifactMetadata, IProject> map = getAllProjectArtifactsInWorkspace();
		for (final ArtifactMetadata metadata : map.keySet()) {
			if (metadata == null)
				continue;
			projects.put(metadata.toString(), map.get(metadata));
		}
		return projects;
	}

	// -------------------------------------------------------------------------
	/**
	 * Gets the pom file.
	 *
	 * @param project the project
	 * @return the pom file
	 */
	public static IFile getPomFile(final IProject project) {
		return project.getProject().getFile(IMavenConstants.POM_FILE_NAME);
	}

	// -------------------------------------------------------------------------
	/**
	 * Artifact.
	 *
	 * @param metadata the metadata
	 * @return the artifact
	 * @throws MavenEclipseApiException the maven eclipse api exception
	 */
	public static Artifact artifact(final ArtifactMetadata metadata)
			throws MavenEclipseApiException {
		if (metadata == null)
			return null;
		if (isNotBlank(metadata.getScope()))
			return MavenApiHelper.getRepositorySystem().createArtifact(
					metadata.getGroupId(), metadata.getArtifactId(),
					metadata.getVersion(), metadata.getScope(),
					metadata.getType());
		return MavenApiHelper.getRepositorySystem()
				.createArtifactWithClassifier(metadata.getGroupId(),
						metadata.getArtifactId(), metadata.getVersion(),
						metadata.getType(), metadata.getClassifier());
	}

	/**
	 * Convert to artifact metadata.
	 *
	 * @param pom the pom
	 * @return the artifact metadata
	 */
	public static ArtifactMetadata convertToArtifactMetadata(final Model pom) {
		return new EclipseArtifactMetadata(pom.getGroupId(),
				pom.getArtifactId(), pom.getVersion(), pom.getPackaging(),
				ArtifactScopeEnum.DEFAULT_SCOPE);
	}

	/**
	 * Artifact metadata.
	 *
	 * @param library the library
	 * @return the artifact metadata
	 */
	public static ArtifactMetadata artifactMetadata(final String library) {
		final String[] coordinates = StringUtils.split(library,
				ARTIFACT_METADATA_SEPARATOR);
		final ArtifactMetadata metadata;
		if (coordinates.length == 1 || coordinates.length == 0)
			metadata = new ArtifactMetadata(library);
		else if (coordinates.length == 2)
			metadata = new EclipseArtifactMetadata(coordinates[0],
					coordinates[1], null, null, ArtifactScopeEnum.DEFAULT_SCOPE);
		else if (coordinates.length == 3)
			metadata = new EclipseArtifactMetadata(coordinates[0],
					coordinates[1], null, coordinates[2],
					ArtifactScopeEnum.DEFAULT_SCOPE);
		else if (coordinates.length == 4)
			metadata = new EclipseArtifactMetadata(coordinates[0],
					coordinates[1], coordinates[3], coordinates[2],
					ArtifactScopeEnum.DEFAULT_SCOPE);
		else
			metadata = new EclipseArtifactMetadata(coordinates[0],
					coordinates[1], coordinates[3], coordinates[2],
					ArtifactScopeEnum.valueOf(coordinates[4]));
		return metadata;
	}

	/**
	 * Artifact metadata.
	 *
	 * @param dependency the dependency
	 * @return the artifact metadata
	 */
	public static ArtifactMetadata artifactMetadata(final Dependency dependency) {
		return new EclipseArtifactMetadata(dependency.getGroupId(),
				dependency.getArtifactId(), dependency.getVersion(),
				dependency.getType(), dependency.getScope(),
				dependency.getClassifier(), null, null, true, null);
	}

	/**
	 * Artifact metadata.
	 *
	 * @param groupID the group id
	 * @param artifactID the artifact id
	 * @param version the version
	 * @param type the type
	 * @param scope the scope
	 * @param classifiler the classifiler
	 * @return the artifact metadata
	 */
	public static ArtifactMetadata artifactMetadata(final String groupID,
			final String artifactID, final String version, final String type,
			final String scope, final String classifiler) {
		return new EclipseArtifactMetadata(groupID, artifactID, version, type,
				scope, classifiler, null, null, true, null);
	}

	/**
	 * Artifact metadata.
	 *
	 * @param groupID the group id
	 * @param artifactID the artifact id
	 * @param version the version
	 * @param packaging the packaging
	 * @return the artifact metadata
	 */
	public static ArtifactMetadata artifactMetadata(final String groupID,
			final String artifactID, final String version,
			final String packaging) {
		return new EclipseArtifactMetadata(groupID, artifactID, version,
				packaging);
	}

	/**
	 * Artifact metadata.
	 *
	 * @param artifact the artifact
	 * @return the artifact metadata
	 */
	public static ArtifactMetadata artifactMetadata(final Artifact artifact) {
		return new EclipseArtifactMetadata(artifact.getGroupId(),
				artifact.getArtifactId(), artifact.getVersion(),
				artifact.getType(), artifact.getScope(),
				artifact.getClassifier(), null, null, true, null);
	}

	/**
	 * Artifact metadata.
	 *
	 * @param model the model
	 * @return the artifact metadata
	 */
	public static ArtifactMetadata artifactMetadata(final Model model) {
		if (model == null)
			return null;
		return artifactMetadata(model.getId());
	}

	/**
	 * Artifact metadata.
	 *
	 * @param file the file
	 * @param packaging the packaging
	 * @return the artifact metadata
	 */
	public static ArtifactMetadata artifactMetadata(
			final IndexedArtifactFile file, final String packaging) {
		return new EclipseArtifactMetadata(file.group, file.artifact,
				file.version, packaging, ArtifactScopeEnum.DEFAULT_SCOPE);
	}

	/**
	 * Dependency.
	 *
	 * @param metadata the metadata
	 * @return the dependency
	 */
	public static Dependency dependency(final ArtifactMetadata metadata) {
		final Dependency dependency = new Dependency();
		dependency.setGroupId(metadata.getGroupId());
		dependency.setArtifactId(metadata.getArtifactId());
		dependency.setVersion(metadata.getVersion());
		dependency.setClassifier(metadata.getClassifier());
		dependency.setType(metadata.getType());
//		if (metadata.getScope() != null
//				&& ArtifactScopeEnum.DEFAULT_SCOPE.getScope().equals(
//						metadata.getScope()) == false)
		//Exclusion exclude = new Exclusion("org.apache", "axis2", null, null) ;
		if(dependency.getArtifactId().equals("soaMerged")&&(dependency.getGroupId().equals("com.ebay.soa"))){
		Exclusion exclusion = new Exclusion();
		exclusion.setArtifactId("axis2");
		exclusion.setGroupId("org.apache");
		dependency.addExclusion(exclusion);
		}
		if(dependency.getArtifactId().equals("MiscServiceMerged")&&(dependency.getGroupId().equals("com.ebay.services"))){
			Exclusion exclusion = new Exclusion();
			exclusion.setArtifactId("xjc");
			exclusion.setGroupId("com.ebay.thirdparty.jaxb");
			dependency.addExclusion(exclusion);
			}
	
		dependency.setScope(metadata.getScope());
		return dependency;
	}

	/**
	 * Dependency.
	 *
	 * @param model the model
	 * @return the dependency
	 */
	public static Dependency dependency(final Model model) {
		if (model == null)
			return null;
		return dependency(artifactMetadata(model));
	}

	/**
	 * Gets the artifact pom file.
	 *
	 * @param artifact the artifact
	 * @return the artifact pom file
	 */
	public static File getArtifactPOMFile(final Artifact artifact) {
		if (artifact == null)
			return null;
		final StringBuffer loc = new StringBuffer();
		loc.append(artifact.getArtifactId());
		loc.append("-");
		loc.append(artifact.getVersion());
		loc.append(".pom");
		return new File(artifact.getFile().getParentFile(), loc.toString());
	}
}
