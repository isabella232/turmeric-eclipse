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
import static org.ebayopensource.turmeric.eclipse.mavenapi.impl.MavenEclipseUtil.artifactMetadata;
import static org.ebayopensource.turmeric.eclipse.mavenapi.impl.MavenEclipseUtil.getPomFile;
import static org.ebayopensource.turmeric.eclipse.mavenapi.internal.util.EclipseUtil.coreException;
import static org.eclipse.core.resources.IWorkspace.AVOID_UPDATE;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.model.Model;
import org.apache.maven.repository.RepositorySystem;
import org.apache.maven.repository.metadata.ArtifactMetadata;
import org.ebayopensource.turmeric.eclipse.mavenapi.exception.MavenEclipseApiException;
import org.ebayopensource.turmeric.eclipse.mavenapi.internal.util.EclipseUtil;
import org.ebayopensource.turmeric.eclipse.mavenapi.internal.util.MavenApiUtil;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.maven.ide.eclipse.MavenPlugin;
import org.maven.ide.eclipse.index.IIndex;
import org.maven.ide.eclipse.index.IndexManager;
import org.maven.ide.eclipse.index.IndexedArtifact;
import org.maven.ide.eclipse.index.IndexedArtifactFile;
import org.maven.ide.eclipse.internal.embedder.MavenImpl;
import org.maven.ide.eclipse.internal.index.NexusIndexManager;
import org.maven.ide.eclipse.repository.IRepository;
import org.maven.ide.eclipse.repository.IRepositoryRegistry;

/**
 * 
 * @author <a href="oleg@codehaus.org">Oleg Gusakov</a>
 * 
 */
public abstract class AbstractMavenEclipseApi extends AbstractMavenApi {
	// ---------------------------------------------------------------------
	/**
	 * {@inheritDoc}
	 */
	public Model parsePom(final IProject project)
			throws MavenEclipseApiException {
		return parsePom(getPomFile(project));
	}

	/**
	 * Returns a model of the pom.
	 * 
	 * @param file the pom file to read
	 * @return the Maven Model
	 * @throws MavenEclipseApiException 
	 */
	public Model parsePom(final IFile file) throws MavenEclipseApiException {
		try {
			return getMavenModelManager().readMavenModel(file);
		} catch (final CoreException ex) {
			throw new MavenEclipseApiException(ex);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public Model parsePom(final InputStream ins)
			throws MavenEclipseApiException {
		try {
			return getMavenModelManager().readMavenModel(ins);
		} catch (final CoreException ex) {
			throw new MavenEclipseApiException(ex);
		} finally {
			IOUtils.closeQuietly(ins);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void writePom(final IFile file, final Model model)
			throws MavenEclipseApiException {
		final IWorkspaceRunnable runnable = new IWorkspaceRunnable() {
			public void run(final IProgressMonitor monitor)
					throws CoreException {
				FileOutputStream writer = null;
				try {
					writer = new FileOutputStream(file.getLocation().toFile());
					writePom(writer, model);
				} catch (final Exception e) {
					throw coreException(e);
				} finally {
					IOUtils.closeQuietly(writer);
				}
			}
			
		};
		try {
			EclipseUtil.workspace().run(runnable, file, AVOID_UPDATE, new NullProgressMonitor());
			EclipseUtil.refresh(file);
		} catch (final CoreException e) {
			throw new MavenEclipseApiException(e);
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void writePom(final OutputStream output, final Model model)
	throws MavenEclipseApiException {
		try {
			MavenPlugin.getDefault().getMaven().writeModel(model, output);
		} catch (final Exception e) {
			throw new MavenEclipseApiException(e);
		} finally {
			IOUtils.closeQuietly(output);
		}
	}

	/**
	 * return local repository.
	 * @return the local Repository Artifact
	 * @throws MavenEclipseApiException 
	 */
	public ArtifactRepository localRepository() throws MavenEclipseApiException {
		try {
			return _getEmbedder().getLocalRepository();
		} catch (CoreException e) {
			throw new MavenEclipseApiException(e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public Artifact resolveArtifact(final Artifact artifact)
			throws MavenEclipseApiException {
		if (artifact == null || artifact.isResolved())
			return artifact;

		// _getEmbedder().resolve( artifact, _getKnownRepositories(
		// _getEmbedder(),
		// artifact.getType()), localRepository() );
		MavenApiUtil.resolveArtifact(MavenApiHelper.getRepositorySystem(), artifact,
				localRepository(),
				_getKnownRepositories(_getEmbedder(), artifact.getType()));

		return artifact;
	}

	/**
	 * helper function used in almost all the APIs
	 */
	private volatile MavenImpl embedder = null;

	/**
	 * The maven implementation that is being used.
	 * 
	 * @return the maven implementation
	 * @throws MavenEclipseApiException 
	 */
	protected MavenImpl _getEmbedder() throws MavenEclipseApiException {
		if (embedder == null)
			embedder = MavenApiHelper.getMavenEmbedder();
		return embedder;
	}


	/**
	 * The maven index manager.
	 * @return see above
	 * @throws MavenEclipseApiException 
	 */
	protected IndexManager _getIndexManager() throws MavenEclipseApiException {
		return MavenApiHelper.getMavenIndexManager();
	}

	/**
	 * Create an artifact given the metadata.  This populates it into the repository.
	 * 
	 * @param metadata the artifact metadata to use.
	 * @return the created artifact.
	 * @throws MavenEclipseApiException 
	 */
	public Artifact createArtifact(final ArtifactMetadata metadata)
			throws MavenEclipseApiException {
		RepositorySystem res = MavenApiHelper.getRepositorySystem();
		if (metadata.getClassifier() == null)
			return res.createArtifact(
					metadata.getGroupId(), metadata.getArtifactId(),
					metadata.getVersion(), metadata.getScope(),
					metadata.getType());
		return res.createArtifactWithClassifier(
				metadata.getGroupId(), metadata.getArtifactId(),
				metadata.getVersion(), metadata.getType(),
				metadata.getClassifier());
	}

	/**
	 * A collection of artifacts.
	 * @param mdColl the artifact metadata collection.
	 * @return see above
	 * @throws MavenEclipseApiException 
	 */
	protected Collection<Artifact> _toArtifactCollection(
			final Collection<ArtifactMetadata> mdColl)
			throws MavenEclipseApiException {
		final List<Artifact> artifacts = new ArrayList<Artifact>();
		if (mdColl == null || mdColl.isEmpty())
			return artifacts;
		for (final ArtifactMetadata metadata : mdColl)
			artifacts.add(createArtifact(metadata));
		return artifacts;
	}

	/**
	 * 
	 * @param findings a Map of String and IndexedArtifact
	 * @return a collection ofArtifactMetaData that was found.
	 */
	protected Collection<ArtifactMetadata> _returnFindings(
			final Map<String, IndexedArtifact> findings) {
		final List<ArtifactMetadata> list = new ArrayList<ArtifactMetadata>();
		if (findings == null || findings.isEmpty())
			return list;
		for (final IndexedArtifact indexed : findings.values()) {
			for (final Object obj : indexed.getFiles()) {
				final IndexedArtifactFile file = (IndexedArtifactFile) obj;
				String packaging = isNotBlank(file.type)
						&& !StringUtils.equalsIgnoreCase(file.type, "null") ? file.type
						: null;
				if (packaging == null) {
					// could not find the valid packaging type
					if ("workspace".equalsIgnoreCase(file.repository)
							|| "workspace://".equalsIgnoreCase(file.repository)) {
						// artifacts inside the workspace, thus the file name
						// would not have a proper file extension
						packaging = "jar";
					} else {
						packaging = new Path(file.fname).getFileExtension();
					}
				}
				
				list.add(artifactMetadata(file, packaging));
			}
		}
		return list;
	}

	/**
	 * 
	 * @param indexManager the IndexManager
	 * @param groupRegEx a regular expression for filtering the group.
	 * @return a collection of ArtifactMetaData that was found.
	 * @throws MavenEclipseApiException 
	 */
	protected Collection<ArtifactMetadata> _findGroup(
			final IndexManager indexManager, final String groupRegEx)
			throws MavenEclipseApiException {
		try {
			final Map<String, IndexedArtifact> results = indexManager.search(
					groupRegEx, IIndex.SEARCH_GROUP);
			return _returnFindings(results);
		} catch (final CoreException e) {
			throw new MavenEclipseApiException(e);
		}
	}

	/**
	 * 
	 * @param indexManager the IndexManager
	 * @param query a query string to the repository
	 * @return a collectin of ArtifactMetadata for items matching the query parameters
	 * @throws MavenEclipseApiException 
	 */
	protected Collection<ArtifactMetadata> _findArtifact(
			final IndexManager indexManager, final String query)
			throws MavenEclipseApiException {
		try {
			final Map<String, IndexedArtifact> results = indexManager.search(
					query, IIndex.SEARCH_ARTIFACT);
			return _returnFindings(results);
		} catch (final CoreException e) {
			throw new MavenEclipseApiException(e);
		}
	}

	/**
	 * Update the maven repository indexes.
	 * 
	 * @throws MavenEclipseApiException 
	 */
	public void refreshAllIndices() throws MavenEclipseApiException {
		for (IRepository repo : MavenPlugin.getDefault()
				.getRepositoryRegistry()
				.getRepositories(IRepositoryRegistry.SCOPE_UNKNOWN)) {
			((NexusIndexManager) _getIndexManager()).scheduleIndexUpdate(repo,
					true);
		}
	}
}
