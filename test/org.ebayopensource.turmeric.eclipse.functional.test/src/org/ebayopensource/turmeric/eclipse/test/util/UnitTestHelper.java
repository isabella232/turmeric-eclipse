/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.test.util;

import java.lang.reflect.Method;

import org.eclipse.core.internal.resources.Workspace;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

public class UnitTestHelper {
	public static void runParentJob(Job job) {
		Class superClass = job.getClass().getSuperclass();
		runJob(superClass, job);
	}

	public static void runJob(Job job) {
		Class jobClass = job.getClass();
		runJob(jobClass, job);
	}

	private static void runJob(Class jobClass, Job job) {
		try {
			jobClass.getName();
			Method scheduleMethod = jobClass.getDeclaredMethod("run",
					IProgressMonitor.class);
			scheduleMethod.setAccessible(true);

			scheduleMethod.invoke(job, new Object[] { null });

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void runEventQueue(int count, int sleepInterval) {

		for (int counter = 0; counter < count; counter++) {
			runEventQueue();
			sleep(sleepInterval);
		}
	}

	/**
	 * Runs the event queue on the current display until it is empty.
	 */
	public static void runEventQueue() {
		IWorkbenchWindow window = getActiveWorkbenchWindow();
		if (window != null)
			runEventQueue(window.getShell());
	}

	public static void runEventQueue(IWorkbenchPart part) {
		if (part == null) {
			return;
		}
		runEventQueue(part.getSite().getShell());
	}

	public static void runEventQueue(Shell shell) {
		runEventQueue(shell.getDisplay());
	}

	public static void runEventQueue(Display display) {
		while (display.readAndDispatch()) {
			// Do nothing
		}

	}

	public static void removeAllProjectFromWorkspace() {

		Workspace m_workspace = (Workspace) ResourcesPlugin.getWorkspace();
		IProject[] projects = m_workspace.getRoot().getProjects();

		if (projects != null) {
			try {

				for (IProject project : projects) {
					project.close(null);
					project.delete(true, null);

				}

			} catch (CoreException e) {
				// Who cares...
			}

		}
	}

	public static void sleep(int intervalTime) {
		try {
			Thread.sleep(intervalTime);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public static Display getActiveDisplay() {
		IWorkbenchWindow window = getActiveWorkbenchWindow();
		return window != null ? window.getShell().getDisplay() : null;
	}

	public static IWorkbenchWindow getActiveWorkbenchWindow() {
		return PlatformUI.getWorkbench().getActiveWorkbenchWindow();
	}

}
