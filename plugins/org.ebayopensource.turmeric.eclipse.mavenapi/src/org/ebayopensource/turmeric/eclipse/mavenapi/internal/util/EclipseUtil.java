/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.mavenapi.internal.util;

/**
 * 
 * @author James Ervin</a>
 *
 */
import static java.lang.System.getProperty;
import static org.apache.commons.lang.StringUtils.defaultString;
import static org.ebayopensource.turmeric.eclipse.mavenapi.MavenApiPlugin.PLUGIN_ID;
import static org.osgi.framework.Version.parseVersion;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.osgi.framework.Version;

/**
 * The Class EclipseUtil.
 */
public class EclipseUtil {
	
	/**
	 * Workspace.
	 *
	 * @return the i workspace
	 */
	public static IWorkspace workspace() {
		return ResourcesPlugin.getWorkspace();
	}

	/**
	 * Workspace root.
	 *
	 * @return the i workspace root
	 */
	public static IWorkspaceRoot workspaceRoot() {
		return workspace().getRoot();
	}

	/**
	 * Projects.
	 *
	 * @return the i project[]
	 */
	public static IProject[] projects() {
		return workspaceRoot().getProjects();
	}

	/**
	 * Gets the project.
	 *
	 * @param projectName the project name
	 * @return the project
	 */
	public static IProject getProject(final String projectName) {
		return workspaceRoot().getProject(projectName);
	}

	/**
	 * Project.
	 *
	 * @param projectName the project name
	 * @return the i project
	 */
	public static IProject project(final String projectName) {
		return getProject(projectName);
	}

	/**
	 * Gets the eclipse version.
	 *
	 * @return the eclipse version
	 */
	public static Version getEclipseVersion() {
		return parseVersion(getProperty("osgi.framework.version"));
	}

	/**
	 * Core exception.
	 *
	 * @param t the t
	 * @return the core exception
	 */
	public static CoreException coreException(final Throwable t) {
		if (t instanceof CoreException)
			return (CoreException) t;
		return new CoreException(new Status(IStatus.ERROR, PLUGIN_ID, -1,
				defaultString(t.getMessage()), t));
	}

	/**
	 * Refresh.
	 *
	 * @param resources the resources
	 */
	public static void refresh(final IResource... resources) {
		for (final IResource resource : resources)
			refresh(resource, null);
	}

	/**
	 * Refresh.
	 *
	 * @param monitor the monitor
	 * @param resources the resources
	 */
	public static void refresh(final IProgressMonitor monitor,
			final IResource... resources) {
		for (final IResource resource : resources)
			refresh(resource, monitor);
	}

	/**
	 * Refresh.
	 *
	 * @param resource the resource
	 * @param progressMonitor the progress monitor
	 */
	public static void refresh(final IResource resource,
			final IProgressMonitor progressMonitor) {
		if (resource == null)
			return;
		final IProgressMonitor monitor = progressMonitor != null ? progressMonitor
				: new NullProgressMonitor();
		try {
			resource.refreshLocal(IResource.DEPTH_INFINITE, monitor);
		} catch (final CoreException e) {
			throw new RuntimeException(e);
		}
	}
}
