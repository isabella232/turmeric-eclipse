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
package org.ebayopensource.turmeric.eclipse.maven.ui.dialogs;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.apache.maven.model.Dependency;
import org.apache.maven.project.MavenProject;
import org.ebayopensource.turmeric.eclipse.maven.core.utils.MavenCoreUtils;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.ISOALibraryDependencyDialog;
import org.ebayopensource.turmeric.eclipse.resources.model.AssetInfo;
import org.ebayopensource.turmeric.eclipse.resources.model.ISOAProject;
import org.eclipse.jface.window.Window;
import org.eclipse.m2e.core.internal.index.IndexedArtifactFile;
import org.eclipse.m2e.core.ui.internal.dialogs.MavenRepositorySearchDialog;
import org.eclipse.swt.widgets.Shell;

/**
 * The Class MavenLibraryDependencyDialog.
 *
 * @author yayu
 */
public class MavenLibraryDependencyDialog implements
		ISOALibraryDependencyDialog {

	/**
	 * Instantiates a new maven library dependency dialog.
	 */
	public MavenLibraryDependencyDialog() {
		super();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Collection<AssetInfo> open(Shell shell, ISOAProject soaProject,
			Set<? extends AssetInfo> availableLibs,
			Set<? extends AssetInfo> allLibs) throws Exception {
		final Collection<AssetInfo> result = new HashSet<AssetInfo>();
		
		final MavenProject mavenProject = soaProject != null ? MavenCoreUtils.getMavenProject( soaProject.getProject() ) : null;
        final MavenRepositorySearchDialog dialog = MavenRepositorySearchDialog.createSearchDependencyDialog(shell, "Select Library to add:", mavenProject, soaProject.getProject(), false);
        
        if( dialog.open() == Window.OK ) {
        	final Object obj = dialog.getFirstResult();
        	if (obj instanceof IndexedArtifactFile) {
        		final IndexedArtifactFile artifactFile = ( IndexedArtifactFile )obj;
        		final Dependency dependency = artifactFile.getDependency();
        		result.add(MavenCoreUtils.translateToAssetInfo(dependency));
        	}
        }
            
		return result;
	}

}
