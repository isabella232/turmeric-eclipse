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

import java.util.ArrayList;
import java.util.List;

import org.ebayopensource.turmeric.eclipse.buildsystem.utils.ActionUtil;
import org.ebayopensource.turmeric.eclipse.core.logging.SOALogger;
import org.ebayopensource.turmeric.eclipse.core.resources.constants.SOAProjectConstants;
import org.ebayopensource.turmeric.eclipse.exception.resources.SOAActionExecutionFailedException;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.GlobalRepositorySystem;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.TrackingEvent;
import org.ebayopensource.turmeric.eclipse.repositorysystem.utils.GlobalProjectHealthChecker;
import org.ebayopensource.turmeric.eclipse.resources.model.ProjectInfo;
import org.ebayopensource.turmeric.eclipse.resources.model.SOAImplMetadata;
import org.ebayopensource.turmeric.eclipse.resources.util.SOAImplUtil;
import org.ebayopensource.turmeric.eclipse.utils.io.PropertiesFileUtil;
import org.ebayopensource.turmeric.eclipse.utils.plugin.JDTUtil;
import org.ebayopensource.turmeric.eclipse.utils.plugin.ProgressUtil;
import org.ebayopensource.turmeric.eclipse.utils.plugin.WorkspaceUtil;
import org.ebayopensource.turmeric.eclipse.utils.ui.UIUtil;
import org.ebayopensource.turmeric.eclipse.validator.utils.ValidateUtil;
import org.ebayopensource.turmeric.eclipse.validator.utils.common.AbstractBaseAccessValidator;
import org.eclipse.core.resources.IFile;
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
 * The Class ReGenerateServiceImplClass.
 *
 * @author yayu
 */
public class ReGenerateServiceImplClass implements IObjectActionDelegate {
	private IStructuredSelection selection;
	private static final SOALogger logger = SOALogger.getLogger();
	
	/**
	 * Instantiates a new re generate service impl class.
	 */
	public ReGenerateServiceImplClass() {
		super();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.ui.IObjectActionDelegate#setActivePart(org.eclipse.jface.action.IAction, org.eclipse.ui.IWorkbenchPart)
	 */
	public void setActivePart(final IAction action,
			final IWorkbenchPart targetPart) {
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.ui.IActionDelegate#run(org.eclipse.jface.action.IAction)
	 */
	public void run(final IAction action) {
		try {
			if (SOALogger.DEBUG)
				logger.entering(action, selection);

			if (selection == null)
				return;

			final IProject project = ActionUtil.preValidateAction(selection
					.getFirstElement(), logger);
			if (project == null)
				return;
			
			if (!UIUtil
					.openChoiceDialog(
							"Overwriting file",
							"This will overwrite the implementation class files, Do you really want to continue?",
							IStatus.WARNING)) {
				return;
			}
			

			final IStatus status = new AbstractBaseAccessValidator() {

				@Override
				public List<IResource> getReadableFiles() {
					// should check the following files
					try {
						return GlobalProjectHealthChecker
								.getSOAProjectReadableResources(project);
					} catch (Exception e) {
						logger.warning(e);
					}
					return new ArrayList<IResource>(1);
				}

				@Override
				public List<IResource> getWritableFiles() {
					// we should ensure that the client config folder is
					// writable
					final List<IResource> result = new ArrayList<IResource>();
					result.add(project);
					try {
						final ProjectInfo projectInfo = GlobalRepositorySystem.instanceOf()
						.getActiveRepositorySystem().getAssetRegistry().getProjectInfo(project.getName());
						if (projectInfo != null && projectInfo.getInterfaceProjectName() != null) {
							final SOAImplMetadata metadata = 
								SOAImplUtil.loadServiceConfig(project, projectInfo.getInterfaceProjectName());
							if (metadata != null) {
								final IResource classResource = project.getFolder(SOAProjectConstants.FOLDER_SRC)
								.getFile(JDTUtil.convertClassNameToFilePath(metadata.getServiceImplClassName()));
								result.add(classResource);
							}
						}
					} catch (Exception e) {
						throw new RuntimeException(e);
					}
					return result;
				}
			}.validate(project.getName());

			final String messages = ValidateUtil
					.getFormattedStatusMessagesForAction(status);
			if (messages != null) {
				UIUtil.showErrorDialog(UIUtil.getActiveShell(), "Error",
						messages, (Throwable) null);
				return;
			}

			WorkspaceJob buildJob = new WorkspaceJob(
					"Re-generating Service Impl class for " + project.getName()) {
				public boolean belongsTo(Object family) {
					return false;
				}

				public IStatus runInWorkspace(IProgressMonitor monitor)
						throws CoreException {
					try {
						monitor.beginTask(getName(),
								ProgressUtil.PROGRESS_STEP * 10);
						ActionUtil
								.generateServiceImplSkeleton(project, true, monitor);
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

		if (this.selection.size() > 1) {
			return;
		}

		action.setEnabled(true);
		final IProject project = ActionUtil.preValidateAction(this.selection
				.getFirstElement(), logger);
		if (project != null) {
			IFile svcImplProperties = SOAImplUtil
					.getServiceImplPropertiesFile(project);
			if (svcImplProperties.isAccessible() == true) {
				try {
					String useExternalFac = PropertiesFileUtil
							.getPropertyValueByKey(
									svcImplProperties.getContents(),
									SOAProjectConstants.PROPS_KEY_USE_EXTERNAL_SERVICE_FACTORY);
					action.setEnabled(Boolean.valueOf(useExternalFac) == false);

				} catch (Exception e) {
					logger.warning(e);
				}
			}
		}
	}

}
