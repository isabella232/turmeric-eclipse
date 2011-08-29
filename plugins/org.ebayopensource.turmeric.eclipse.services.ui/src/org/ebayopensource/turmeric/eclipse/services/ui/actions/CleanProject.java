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
package org.ebayopensource.turmeric.eclipse.services.ui.actions;

import org.ebayopensource.turmeric.eclipse.buildsystem.utils.ActionUtil;
import org.ebayopensource.turmeric.eclipse.core.logging.SOALogger;
import org.ebayopensource.turmeric.eclipse.exception.resources.SOAActionExecutionFailedException;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.GlobalRepositorySystem;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.TrackingEvent;
import org.ebayopensource.turmeric.eclipse.utils.plugin.ProgressUtil;
import org.ebayopensource.turmeric.eclipse.utils.plugin.WorkspaceUtil;
import org.ebayopensource.turmeric.eclipse.utils.ui.UIUtil;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResourceRuleFactory;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.resources.WorkspaceJob;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.ISchedulingRule;
import org.eclipse.core.runtime.jobs.MultiRule;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;


// TODO: Auto-generated Javadoc
/**
 * Removing all gen folders for the selected project.
 *
 * @author yayu
 */
public class CleanProject implements IObjectActionDelegate{
	
	/** The selection. */
	private IStructuredSelection selection;
	
	/** The Constant logger. */
	private static final SOALogger logger = SOALogger.getLogger();
	
	/** The shell. */
	private Shell shell;
	
	/**
	 * Instantiates a new clean project.
	 */
	public CleanProject() {
		super();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.ui.IObjectActionDelegate#setActivePart(org.eclipse.jface.action.IAction, org.eclipse.ui.IWorkbenchPart)
	 */
	@Override
	public void setActivePart(final IAction action,
			final IWorkbenchPart targetPart) {
		shell = targetPart.getSite().getShell();
	}

	/**
	 * {@inheritDoc}
	 * 
	 *  @see org.eclipse.ui.IActionDelegate#run(org.eclipse.jface.action.IAction)
	 */
	@Override
	public void run(final IAction action) {
		try {
			if (SOALogger.DEBUG)
				logger.entering(action, selection);
			
			if (selection == null)
				return;
			
			final IProject project = 
				ActionUtil.preValidateAction(selection.getFirstElement(), logger);
			if (project == null)
				return;
			
			if (MessageDialog.openConfirm(shell, "Confirm", 
					"Are you sure to clean all gen folders of the selected project->" + project + "?")) {
				
				
				WorkspaceJob buildJob = new WorkspaceJob("Cleaning Project for "
						+ project.getName()) {
					@Override
					public boolean belongsTo(Object family) {
						return false;
					}

					@Override
					public IStatus runInWorkspace(IProgressMonitor monitor)
							throws CoreException {
						monitor.beginTask(getName(), ProgressUtil.PROGRESS_STEP * 10);
						try {
							ActionUtil.cleanProject(project, monitor);
						} catch (Exception e) {
							logger.error(e);
							throw new SOAActionExecutionFailedException(e);
						} finally {
							monitor.done();
							WorkspaceUtil.refresh(monitor, project);
						}
						return Status.OK_STATUS;
					}
				};
				
				final IResourceRuleFactory ruleFactory = ResourcesPlugin.getWorkspace().getRuleFactory();
				final ISchedulingRule rule = MultiRule.combine(ruleFactory.refreshRule(project), 
						ruleFactory.buildRule());
				buildJob.setRule(rule);
				GlobalRepositorySystem.instanceOf().getActiveRepositorySystem()
				.trackingUsage(new TrackingEvent(
						getClass().getName(), 
						TrackingEvent.TRACKING_ACTION));
				UIUtil.runJobInUIDialog(buildJob).schedule();
			}
		} catch (Exception e) {
			logger.error(e);
			UIUtil.showErrorDialog(e);
		} finally {
			if (SOALogger.DEBUG)
				logger.exiting();
		}
	}

	/**
	 * {@inheritDoc}
	 * @see org.eclipse.ui.IActionDelegate#selectionChanged(org.eclipse.jface.action.IAction, org.eclipse.jface.viewers.ISelection)
	 */
	@Override
	public void selectionChanged(final IAction action,
			final ISelection selection) {
		this.selection = (IStructuredSelection) selection;
	}

}
