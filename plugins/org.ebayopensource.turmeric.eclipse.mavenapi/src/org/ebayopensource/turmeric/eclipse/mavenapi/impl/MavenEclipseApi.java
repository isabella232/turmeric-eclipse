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
import static org.ebayopensource.turmeric.eclipse.mavenapi.impl.MavenApiHelper.getMavenProjectManager;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.WildcardQuery;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.model.Build;
import org.apache.maven.model.Model;
import org.apache.maven.model.Parent;
import org.apache.maven.model.Plugin;
import org.apache.maven.model.io.xpp3.MavenXpp3Writer;
import org.apache.maven.project.MavenProject;
import org.apache.maven.repository.metadata.ArtifactMetadata;
import org.ebayopensource.turmeric.eclipse.mavenapi.exception.MavenEclipseApiException;
import org.ebayopensource.turmeric.eclipse.mavenapi.request.AbstractProjectMavenizationRequest;
import org.ebayopensource.turmeric.eclipse.mavenapi.request.ProjectMavenizationRequest;
import org.ebayopensource.turmeric.eclipse.mavenapi.request.ProjectMavenizationRequestRaw;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.jdt.core.JavaCore;
import org.maven.ide.eclipse.MavenPlugin;
import org.maven.ide.eclipse.core.IMavenConstants;
import org.maven.ide.eclipse.index.IndexedArtifact;
import org.maven.ide.eclipse.internal.index.NexusIndexManager;
import org.maven.ide.eclipse.internal.repository.RepositoryInfo;
import org.maven.ide.eclipse.project.IMavenProjectFacade;
import org.maven.ide.eclipse.project.IProjectConfigurationManager;
import org.maven.ide.eclipse.project.MavenProjectInfo;
import org.maven.ide.eclipse.project.ProjectImportConfiguration;
import org.maven.ide.eclipse.project.ResolverConfiguration;
import org.maven.ide.eclipse.repository.IRepositoryRegistry;
import org.sonatype.nexus.index.ArtifactInfo;

/**
 * The Class MavenEclipseApi.
 *
 * @author <a href="oleg@codehaus.org">Oleg Gusakov</a>
 */
public class MavenEclipseApi extends AbstractMavenEclipseApi {

	private Model generateModel(final ProjectMavenizationRequest req,
			IProgressMonitor monitor) {
		final Model model = new Model();
		model.setModelVersion("4.0.0");

		final ArtifactMetadata artifact = req.getArtifact();
		if ((monitor instanceof NullProgressMonitor) == false) {
			monitor = new SubProgressMonitor(monitor, 50);
			monitor.setTaskName("Mavenizing project->"
					+ req.getEclipseProject());
		}
		if (req.getParent() != null) {
			final ArtifactMetadata md = req.getParent();
			final Parent parent = new Parent();
			parent.setGroupId(md.getGroupId());
			parent.setArtifactId(md.getArtifactId());
			parent.setVersion(md.getVersion());
			model.setParent(parent);
		}
		monitor.worked(5);

		model.setGroupId(artifact.getGroupId());
		model.setArtifactId(artifact.getArtifactId());
		model.setVersion(artifact.getVersion());
		model.setName(req.getEclipseProject().getName());
		//RIDE changes
		if(isNotBlank(req.getPackaging())){
			model.setPackaging(req.getPackaging());
		}
		//overriding
		
		if (isNotBlank(model.getPackaging())&&isNotBlank(artifact.getType())
				&& !StringUtils.equalsIgnoreCase(artifact.getType().trim(),
						"jar"))
			model.setPackaging(artifact.getType());

		final Properties props = req.getProperties();
		if (props != null && props.size() > 0)
			model.setProperties(props);
		monitor.worked(5);

		// build is first set into model here
		if (req.getSourcePath() != null) {
			final String sp = req.getSourcePath();
			final Build build = model.getBuild() != null ? model.getBuild()
					: new Build();
			build.setSourceDirectory(sp);
			model.setBuild(build);
		}

		if (req.getTestSourcePath() != null) {
			final Build build = model.getBuild() != null ? model.getBuild()
					: new Build();
			build.setTestSourceDirectory(req.getTestSourcePath());
			model.setBuild(build);
		}

		if (req.getTestOutputPath() != null) {
			final Build build = model.getBuild() != null ? model.getBuild()
					: new Build();
			build.setTestOutputDirectory(req.getTestOutputPath());
			model.setBuild(build);
		}

		if (req.getResourceDirectories() != null
				&& req.getResourceDirectories().isEmpty() == false) {
			final Build build = model.getBuild() != null ? model.getBuild()
					: new Build();
			build.setResources(req.getResourceDirectories());
			model.setBuild(build);
		}
		monitor.worked(5);

		if (req.getTestResourceDirectories() != null
				&& req.getTestResourceDirectories().isEmpty() == false) {
			final Build build = model.getBuild() != null ? model.getBuild()
					: new Build();
			build.setTestResources(req.getTestResourceDirectories());
			model.setBuild(build);
		}
		monitor.worked(5);

		if (req.getBuildPlugins() != null
				&& req.getBuildPlugins().isEmpty() == false) {
			final Build build = model.getBuild() != null ? model.getBuild()
					: new Build();
			for (Plugin buildPlugin : req.getBuildPlugins()) {
				build.addPlugin(buildPlugin);
			}
		}

		if (req.getOutputPath() != null) {
			final Build build = model.getBuild() != null ? model.getBuild()
					: new Build();
			build.setOutputDirectory(req.getOutputPath());
		}

		if (req.getDependencies() != null
				&& req.getDependencies().isEmpty() == false) {
			for (final ArtifactMetadata am : req.getDependencies()) {
				model.addDependency(MavenEclipseUtil.dependency(am));
			}
		}
		monitor.worked(5);
		return model;
	}

	private void mavenizeProject(Model model, IProject proj,
			IProgressMonitor monitor) throws MavenEclipseApiException {
		final MavenXpp3Writer mw = new MavenXpp3Writer();

		final IFile pom = proj.getFile(IMavenConstants.POM_FILE_NAME);

		try {
			final StringWriter sw = new StringWriter();
			mw.write(sw, model);
			final ByteArrayInputStream bais = new ByteArrayInputStream(sw
					.toString().getBytes());

			if (pom.exists())
				pom.setContents(bais, true, true, null);
			else
				pom.create(bais, true, null);

			final MavenPlugin m3e = MavenPlugin.getDefault();
			if (m3e == null)
				throw new MavenEclipseApiException("Cannot obtain MavenPlugin");

			/*
			 * final BuildPathManager bpm = m3e.getBuildpathManager(); if( bpm
			 * == null ) throw new MavenEclipseApiException(
			 * "Cannot obtain BuildPathManager from MavenPlugin" );
			 */
			monitor.worked(5);

			final MavenProjectInfo info = new MavenProjectInfo(proj.getName(),
					pom.getLocation().toFile(), model, null);
			final ProjectImportConfiguration importConfiguration = new ProjectImportConfiguration();

			// will get SWTException if a normal progress monitor
			// is passed to M2Eclipse API (0.10.0),
			// which terminates the artifact downloading. See
			// https://issues.sonatype.org/browse/MNGECLIPSE-2251. To work
			// around, use NullProgressMonitor instead.
			monitor = new NullProgressMonitor();

			final IProjectConfigurationManager configManager = MavenPlugin
					.getDefault().getProjectConfigurationManager();
			final Collection<MavenProjectInfo> projects = new ArrayList<MavenProjectInfo>(
					1);
			projects.add(info);
			// configManager.getRule();
			// configManager.createSimpleProject(proj, pom.getLocation(), model,
			// null, importConfiguration, monitor);
			configManager
					.importProjects(projects, importConfiguration, monitor);

			final ResolverConfiguration rc = new ResolverConfiguration();
			if (proj.hasNature(JavaCore.NATURE_ID) == false
					|| proj.hasNature(IMavenConstants.NATURE_ID) == false)
				;
			{// during the import project process, the JDT/Maven natures will
				// not be added if the project already exists
				configManager.enableMavenNature(proj, rc, monitor);
			}

			// this function consumes too much memory
			// configManager.updateProjectConfiguration(proj, rc, "", monitor);

			pom.refreshLocal(IResource.DEPTH_INFINITE, monitor);
		} catch (final Exception e) {
			throw new MavenEclipseApiException(e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IProject mavenizeProject(final ProjectMavenizationRequest req,
			IProgressMonitor monitor) throws MavenEclipseApiException {
		Model model = generateModel(req, monitor);
		mavenizeProject(model, req.getEclipseProject(), monitor);
		return req.getEclipseProject();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Artifact resolveArtifact(final ArtifactMetadata metadata)
			throws MavenEclipseApiException {
		if (metadata == null)
			return null;
		return super.resolveArtifact(_getEmbedder(), 
				MavenApiHelper.getRepositorySystem(), metadata);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Collection<Artifact> findGroup(final String groupRegEx)
			throws MavenEclipseApiException {
		return _toArtifactCollection(_findGroup(_getIndexManager(), groupRegEx));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Collection<Artifact> findArtifact(final String query)
			throws MavenEclipseApiException {
		return _toArtifactCollection(_findArtifact(_getIndexManager(), query));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Artifact> resolveArtifactAsClasspath(
			final ArtifactMetadata metadata) throws MavenEclipseApiException {
		return super.resolveArtifactAsClasspath(_getEmbedder(), metadata);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public MavenProject resolveArtifactAsProject(final Artifact artifact)
			throws MavenEclipseApiException {
		if (artifact == null)
			return null;
		final IMavenProjectFacade facade = getMavenProjectManager()
				.getMavenProject(artifact.getGroupId(),
						artifact.getArtifactId(), artifact.getVersion());

		try {
			if (facade != null)
				return facade.getMavenProject(new NullProgressMonitor());
			resolveArtifact(artifact);
			final File pomFile = MavenEclipseUtil.getArtifactPOMFile(artifact);
			if (pomFile.exists() == false) {
				final ArtifactMetadata pomArtifact = MavenEclipseUtil
						.artifactMetadata(artifact);
				pomArtifact.setType(PACKAGING_TYPE_POM);
				resolveArtifact(pomArtifact);
			}

			return _getEmbedder().readProject(pomFile,
					new NullProgressMonitor());
		} catch (CoreException e) {
			throw new MavenEclipseApiException(e);
		}
	}

	/**
	 * according to name,to search artifact. It supports wildcard.
	 *
	 * @param name the name, may include wild cards.
	 * @return a Collection of Artifacts that matched the query.
	 * @throws MavenEclipseApiException the maven eclipse api exception
	 */
	public Collection<Artifact> findArtifactByName(String name)
			throws MavenEclipseApiException {
		return findArtifactByNameAndGroup(name, "");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Collection<Artifact> findArtifactByNameAndGroup(String name,
			String group) throws MavenEclipseApiException {
		return findArtifactByNameAndGroupAndRepositoryUrl(name, group, "");
	}

	/**
	 * according to artifact name,group and repository url ,to search artifact.
	 * It supports wildcard.
	 *
	 * @param name the artifact name, can include wildcards
	 * @param group the group id, can include wildcards
	 * @param repositoryUrl a repository url location
	 * @return a collection of artifacts that matched the query
	 * @throws MavenEclipseApiException the maven eclipse api exception
	 */
	public Collection<Artifact> findArtifactByNameAndGroupAndRepositoryUrl(
			String name, String group, String repositoryUrl)
			throws MavenEclipseApiException {
		Collection<ArtifactMetadata> ams = null;
		try {
			BooleanQuery bq = new BooleanQuery();
			BooleanQuery.setMaxClauseCount(Integer.MAX_VALUE);
			if (StringUtils.isNotBlank(name)) {
				bq.add(new WildcardQuery(new Term(ArtifactInfo.ARTIFACT_ID,
						name.toLowerCase())), Occur.MUST);
			} else {
				bq.add(new WildcardQuery(
						new Term(ArtifactInfo.ARTIFACT_ID, "*")), Occur.MUST);
			}
			if (StringUtils.isNotBlank(group)) {
				bq.add(new WildcardQuery(new Term(ArtifactInfo.GROUP_ID, group
						.toLowerCase())), Occur.MUST);
			} else {
				bq.add(new WildcardQuery(new Term(ArtifactInfo.GROUP_ID, "*")),
						Occur.MUST);
			}
			
			RepositoryInfo repository = new RepositoryInfo(null, repositoryUrl,
					IRepositoryRegistry.SCOPE_UNKNOWN, null);
			final Map<String, IndexedArtifact> results = ((NexusIndexManager) _getIndexManager())
					.search(repository, bq);
			ams = _returnFindings(results);
		} catch (final CoreException e) {
			throw new MavenEclipseApiException(e);
		}
		return _toArtifactCollection(ams);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IProject mavenizeProject(AbstractProjectMavenizationRequest req,
			IProgressMonitor monitor) throws MavenEclipseApiException {
		if (req instanceof ProjectMavenizationRequest) {
			return mavenizeProject((ProjectMavenizationRequest) req, monitor);
		} else if (req instanceof ProjectMavenizationRequestRaw) {
			final ProjectMavenizationRequestRaw reqRaw = (ProjectMavenizationRequestRaw) req;
			mavenizeProject(reqRaw.getMavenModel(), reqRaw.getEclipseProject(),
					monitor);
			return reqRaw.getEclipseProject();
		} else {
			throw new IllegalArgumentException("illegal argument->" + req);
		}
	}

}
