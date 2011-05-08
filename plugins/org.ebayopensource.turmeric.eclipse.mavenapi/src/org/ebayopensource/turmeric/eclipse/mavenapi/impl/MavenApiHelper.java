/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.mavenapi.impl;

import org.apache.maven.repository.RepositorySystem;
import org.ebayopensource.turmeric.eclipse.mavenapi.exception.MavenEclipseApiException;
import org.ebayopensource.turmeric.eclipse.mavenapi.internal.resources.Messages;
import org.maven.ide.eclipse.MavenPlugin;
import org.maven.ide.eclipse.embedder.IMaven;
import org.maven.ide.eclipse.embedder.MavenModelManager;
import org.maven.ide.eclipse.index.IndexManager;
import org.maven.ide.eclipse.internal.embedder.MavenImpl;
import org.maven.ide.eclipse.project.MavenProjectManager;

/**
 * The Class MavenApiHelper.
 *
 * @author <a href="oleg@codehaus.org">Oleg Gusakov</a>
 */
public class MavenApiHelper {

	/**
	 * Gets the maven index manager.
	 *
	 * @return the IndexManager
	 */
	public static IndexManager getMavenIndexManager() {
		return MavenPlugin.getDefault().getIndexManager();
	}

	/**
	 * Gets the maven project manager.
	 *
	 * @return the maven ProjectManager
	 */
	public static MavenProjectManager getMavenProjectManager() {
		return MavenPlugin.getDefault().getMavenProjectManager();
	}

	/**
	 * Gets the maven model manager.
	 *
	 * @return the maven model manager
	 */
	public static MavenModelManager getMavenModelManager() {
		return MavenPlugin.getDefault().getMavenModelManager();
	}

	/**
	 * Gets the maven embedder.
	 *
	 * @return the maven implementation that is being used
	 * @throws MavenEclipseApiException the maven eclipse api exception
	 */
	public static MavenImpl getMavenEmbedder() throws MavenEclipseApiException {
		IMaven embedder = MavenPlugin.getDefault().getMaven();
		if (!(embedder instanceof MavenImpl)) {
			throw new MavenEclipseApiException(Messages.ERROR_NO_MAVEN_IMPL);
		}
		return (MavenImpl) embedder;
	}
	
	/**
	 * Gets the repository system.
	 *
	 * @return the maven repository system
	 * @throws MavenEclipseApiException the maven eclipse api exception
	 */
	public static RepositorySystem getRepositorySystem() throws MavenEclipseApiException {
		try {
			return getMavenEmbedder().getPlexusContainer().lookup(RepositorySystem.class);
		} catch (Exception e) {
			throw new MavenEclipseApiException(e);
		}
	}
	

}
