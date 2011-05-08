/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.services.ui.actions;

import java.util.ArrayList;
import java.util.List;

import org.ebayopensource.turmeric.eclipse.buildsystem.utils.ActionUtil;
import org.ebayopensource.turmeric.eclipse.core.logging.SOALogger;
import org.ebayopensource.turmeric.eclipse.core.resources.constants.SOAProjectConstants;
import org.ebayopensource.turmeric.eclipse.exception.resources.SOAActionExecutionFailedException;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.GlobalRepositorySystem;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.TrackingEvent;
import org.ebayopensource.turmeric.eclipse.repositorysystem.utils.GlobalProjectHealthChecker;
import org.ebayopensource.turmeric.eclipse.resources.model.SOAImplProject;
import org.ebayopensource.turmeric.eclipse.utils.plugin.ProgressUtil;
import org.ebayopensource.turmeric.eclipse.utils.plugin.WorkspaceUtil;
import org.ebayopensource.turmeric.eclipse.utils.ui.UIUtil;
import org.ebayopensource.turmeric.eclipse.validator.utils.ValidateUtil;
import org.ebayopensource.turmeric.eclipse.validator.utils.common.AbstractBaseAccessValidator;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.resources.WorkspaceJob;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;


/**
 * The Class GenerateGlobalClientConfig.
 *
 * @author smathew
 */
public class GenerateGlobalClientConfig implements IObjectActionDelegate {
	private IStructuredSelection selection;
	private static final SOALogger logger = SOALogger.getLogger();

	/**
	 * {@inheritDoc}
	 *  @see org.eclipse.ui.IObjectActionDelegate#setActivePart(org.eclipse.jface.action.IAction, org.eclipse.ui.IWorkbenchPart)
	 */
	public void setActivePart(final IAction action,
			final IWorkbenchPart targetPart) {
	}

	/**
	 * {@inheritDoc}
	 * @see org.eclipse.ui.IActionDelegate#run(org.eclipse.jface.action.IAction)
	 */
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
			
			final IStatus status = new AbstractBaseAccessValidator() {

				@Override
				public List<IResource> getReadableFiles() {
					//should check the following files
					try {
						return GlobalProjectHealthChecker.getSOAProjectReadableResources(project);
					} catch (Exception e) {
						logger.warning(e);
					}
					return new ArrayList<IResource>(1);
				}

				@Override
				public List<IResource> getWritableFiles() {
					//we should ensure that the client config folder is writable
					final List<IResource> result = new ArrayList<IResource>();
					result.add(project.getFile(SOAImplProject.META_SRC_ClIENT_CONFIG 
							+ WorkspaceUtil.PATH_SEPERATOR + SOAProjectConstants.FILE_GLOBAL_CLIENT_CONFIG));
					return result;
				}
			}.validate(project.getName());
			
			final String messages = ValidateUtil.getFormattedStatusMessagesForAction(status);
			if (messages != null) {
				UIUtil.showErrorDialog(UIUtil.getActiveShell(), "Error", 
						messages, (Throwable)null);
				return;
			}
			
			WorkspaceJob buildJob = new WorkspaceJob("Building Global Client Config for "
					+ project.getName()) {
				public boolean belongsTo(Object family) {
					return false;
				}

				public IStatus runInWorkspace(IProgressMonitor monitor)
						throws CoreException {
					try {
						monitor.beginTask(getName(), ProgressUtil.PROGRESS_STEP * 20);
						ActionUtil.generateGlobalClientConfig(project, monitor);
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
			buildJob.setRule(ResourcesPlugin.getWorkspace().getRuleFactory()
					.deleteRule(project));
			GlobalRepositorySystem.instanceOf().getActiveRepositorySystem()
			.trackingUsage(new TrackingEvent(
					getClass().getName(), 
					TrackingEvent.TRACKING_ACTION));
			UIUtil.runJobInUIDialog(buildJob).schedule();
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
	 *  @see org.eclipse.ui.IActionDelegate#selectionChanged(org.eclipse.jface.action.IAction, org.eclipse.jface.viewers.ISelection)
	 */
	public void selectionChanged(final IAction action,
			final ISelection selection) {
		this.selection = (IStructuredSelection) selection;
	}
}
