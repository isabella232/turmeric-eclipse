/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.buildsystem.eclipse;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.ebayopensource.turmeric.eclipse.buildsystem.resources.SOAMessages;
import org.ebayopensource.turmeric.eclipse.buildsystem.utils.ActionUtil;
import org.ebayopensource.turmeric.eclipse.buildsystem.utils.BuilderUtil;
import org.ebayopensource.turmeric.eclipse.core.logging.SOALogger;
import org.ebayopensource.turmeric.eclipse.exception.resources.SOAActionExecutionFailedException;
import org.ebayopensource.turmeric.eclipse.exception.resources.SOAResourceNotAccessibleException;
import org.ebayopensource.turmeric.eclipse.repositorysystem.RepositorySystemActivator;
import org.ebayopensource.turmeric.eclipse.repositorysystem.preferences.core.PreferenceConstants;
import org.ebayopensource.turmeric.eclipse.repositorysystem.utils.GlobalProjectHealthChecker;
import org.ebayopensource.turmeric.eclipse.resources.util.MarkerUtil;
import org.ebayopensource.turmeric.eclipse.utils.lang.StringUtil;
import org.ebayopensource.turmeric.eclipse.utils.plugin.ProjectUtil;
import org.ebayopensource.turmeric.eclipse.utils.plugin.WorkspaceUtil;
import org.ebayopensource.turmeric.eclipse.utils.ui.ProjectUtils;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.preferences.DefaultScope;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.internal.core.PackageFragment;
import org.eclipse.jdt.internal.core.PackageFragmentRoot;

/**
 * The Class AbstractSOAProjectBuilder.
 *
 * @author yayu
 */
public abstract class AbstractSOAProjectBuilder extends
		IncrementalProjectBuilder {
	private static final SOALogger logger = SOALogger.getLogger();

	/**
	 * Instantiates a new abstract soa project builder.
	 */
	public AbstractSOAProjectBuilder() {
		super();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.core.resources.IncrementalProjectBuilder#build(int, java.util.Map, org.eclipse.core.runtime.IProgressMonitor)
	 */
	@Override
	protected IProject[] build(int kind, Map args, IProgressMonitor monitor)
			throws CoreException {
		final IProject project = getProject();
		long time = System.currentTimeMillis();
		/**
		 * even using meunu project->clean, the build kind is still full build.
		 */
		if (kind == CLEAN_BUILD || kind == FULL_BUILD) {
			try {
				ActionUtil.cleanProject(project, monitor);
			} catch (Exception e) {
				logger.error("Clean failed with exception:" + e);
				throw new SOAActionExecutionFailedException(e);
			} finally {
				WorkspaceUtil.refresh(monitor, project);
			}
		}
		try {
			final IResourceDelta delta = getDelta(project);
			if (shouldBuild(delta, project)) {
				MarkerUtil.cleanSOAProblemMarkers(project);
				final IStatus status = checkProjectHealth(project);
				if (status.isOK() == false) {
					MarkerUtil.createSOAProblemMarkerRecursive(status, project);
				}
				BuilderUtil.generateSourceDirectories(project, monitor);
				IProject[] toReturn= doBuild(kind, args, project, delta, monitor);
				return toReturn;
			} else {
				if (SOALogger.DEBUG) {
					logger
							.warning("Should not do build according to changed resources. Build Skipped.");
				}
			}
		} catch (Exception e) {
			logger.error(SOAMessages.SERVICE_CODEGEN_SKIPPED_MESSAGE + ":"
					+ e.getMessage());
			logger.error(e);
			MarkerUtil.createSOAProblemMarker(e, project);
		} finally {
			project.refreshLocal(IResource.DEPTH_INFINITE, monitor);
			if (SOALogger.DEBUG) {
				long duration = System.currentTimeMillis() - time;
				String msg = StringUtil.formatString(
						SOAMessages.MSG_TIME_TAKEN_FOR_BUILD_PROJECT, project
								.getName(), duration);
				logger.debug(msg);
			}
		}
		return null;
	}
	
	
	
	/**
	 * Do build.
	 *
	 * @param kind the kind
	 * @param args the args
	 * @param project the project
	 * @param delta the delta
	 * @param monitor the monitor
	 * @return the i project[]
	 * @throws Exception the exception
	 */
	protected abstract IProject[] doBuild(int kind, Map args, IProject project,
			IResourceDelta delta, IProgressMonitor monitor) throws Exception;

	/**
	 * Should build.
	 *
	 * @param delta the delta
	 * @param project the project
	 * @return true, if successful
	 * @throws Exception the exception
	 */
	protected boolean shouldBuild(IResourceDelta delta, IProject project)
			throws Exception {
		return BuilderUtil.shouldBuild(delta, project);
	}

	/**
	 * Check project health.
	 *
	 * @param project the project
	 * @return the i status
	 * @throws Exception the exception
	 */
	protected IStatus checkProjectHealth(IProject project) throws Exception {
		return GlobalProjectHealthChecker.checkProjectHealth(project);
	}

	/**
	 * Do clean.
	 *
	 * @param project the project
	 * @param monitor the monitor
	 * @throws Exception the exception
	 */
	protected abstract void doClean(IProject project, IProgressMonitor monitor)
			throws Exception;

	@Override
	protected void clean(IProgressMonitor monitor) throws CoreException {
		final IProject project = getProject();
		long time = System.currentTimeMillis();
		try {
			BuilderUtil.reOrderBuildersIfRequired(project);
			MarkerUtil.cleanSOAProblemMarkers(project);
			doClean(project, monitor);
		} catch (Exception e) {
			logger.error(e);
			MarkerUtil.createSOAProblemMarker(e, project);
		} finally {
			if (SOALogger.DEBUG) {
				long duration = System.currentTimeMillis() - time;
				String msg = StringUtil.formatString(
						SOAMessages.MSG_TIME_TAKEN_FOR_CLEAN_PROJECT, project
								.getName(), duration);
				logger.debug(msg);
			}
		}
	}

}
