/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.test.util;

import org.apache.commons.lang.StringUtils;
import org.ebayopensource.turmeric.eclipse.exception.resources.SOAActionExecutionFailedException;
import org.ebayopensource.turmeric.eclipse.utils.plugin.ProgressUtil;
import org.ebayopensource.turmeric.eclipse.utils.plugin.WorkspaceUtil;
import org.ebayopensource.turmeric.eclipse.utils.ui.UIUtil;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.resources.WorkspaceJob;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.jdt.core.JavaCore;



/**
 * @author smathew
 * 
 *         Test helper for Proect related activities
 * 
 */
public class ProjectUtil {

	public static IProject createProject(String name, String location)
			throws CoreException {
		// testing utility
		return WorkspaceUtil.createProject(name, new Path(location),
				new NullProgressMonitor());

	}

	public static IWorkspace getWorkspace() {
		return WorkspaceUtil.getWorkspace();
	}

	public static void createProject(String name) throws CoreException {
		WorkspaceUtil.createProject(name, WorkspaceUtil.getWorkspaceRoot()
				.getLocation(), null);
	}

	public static void deleteProject(String name) throws CoreException {
		WorkspaceUtil.deleteProject(name);
	}

	public static IProject getProject(String name) throws CoreException {
		return WorkspaceUtil.getWorkspaceRoot().getProject(name);
	}

	public static boolean isJavaNatureSet(String projectName)
			throws CoreException {
		IProject project = getProject(projectName);
		if (project.exists()) {
			IProjectDescription description = project.getDescription();
			for (String addedNature : description.getNatureIds()) {
				if (StringUtils.equals(addedNature, JavaCore.NATURE_ID)) {
					return true;
				}
			}

		}
		return false;
	}

	public static void cleanUpWS() throws CoreException, InterruptedException {
		cleanUpProjects(WorkspaceUtil.getAllProjectsInWorkSpace());
	}

	public static void cleanUpProjects(IProject... projects)
			throws CoreException, InterruptedException {

		final IResource resource = ResourcesPlugin.getWorkspace().getRoot();
		WorkspaceJob buildJob = null;
		for (IProject project : projects) {
			// WorkspaceUtil.delete(project, null);

			final IProject curPrj = project;
			buildJob = new WorkspaceJob("Deleting project " + project.getName()) {
				@Override
				public boolean belongsTo(Object family) {
					return false;
				}

				@Override
				public IStatus runInWorkspace(IProgressMonitor monitor)
						throws CoreException {
					try {
						monitor.beginTask(getName(),
								ProgressUtil.PROGRESS_STEP * 20);
						curPrj.close(monitor);

					} catch (Exception e) {
						e.printStackTrace();
						throw new SOAActionExecutionFailedException(e);
					} finally {
						monitor.done();

					}
					return Status.OK_STATUS;
				}
			};

			buildJob.setRule(ResourcesPlugin.getWorkspace().getRuleFactory()
					.deleteRule(ResourcesPlugin.getWorkspace().getRoot()));
			UIUtil.runJobInUIDialog(buildJob).schedule();
			buildJob.join();

			buildJob = new WorkspaceJob("Deleting project " + curPrj.getName()) {
				@Override
				public boolean belongsTo(Object family) {
					return false;
				}

				@Override
				public IStatus runInWorkspace(IProgressMonitor monitor)
						throws CoreException {
					try {
						monitor.beginTask(getName(),
								ProgressUtil.PROGRESS_STEP * 20);
						curPrj.refreshLocal(IResource.DEPTH_INFINITE, null);
						WorkspaceUtil.deleteProject(curPrj.getName());

					} catch (Exception e) {
						e.printStackTrace();
						throw new SOAActionExecutionFailedException(e);
					} finally {
						monitor.done();

					}
					return Status.OK_STATUS;
				}
			};

			buildJob.setRule(ResourcesPlugin.getWorkspace().getRuleFactory()
					.deleteRule(curPrj));
			UIUtil.runJobInUIDialog(buildJob).schedule();
			buildJob.join();

			buildJob = new WorkspaceJob("Deleting project " + curPrj.getName()) {
				@Override
				public boolean belongsTo(Object family) {
					return false;
				}

				@Override
				public IStatus runInWorkspace(IProgressMonitor monitor)
						throws CoreException {
					try {
						monitor.beginTask(getName(),
								ProgressUtil.PROGRESS_STEP * 20);

						resource.refreshLocal(IResource.DEPTH_INFINITE,
								ProgressUtil.getDefaultMonitor(null));

					} catch (Exception e) {

						throw new SOAActionExecutionFailedException(e);
					} finally {
						monitor.done();

					}
					return Status.OK_STATUS;
				}
			};
			buildJob.setRule(ResourcesPlugin.getWorkspace().getRuleFactory()
					.deleteRule(resource));
			UIUtil.runJobInUIDialog(buildJob).schedule();
			buildJob.join();

		}
	}

}
