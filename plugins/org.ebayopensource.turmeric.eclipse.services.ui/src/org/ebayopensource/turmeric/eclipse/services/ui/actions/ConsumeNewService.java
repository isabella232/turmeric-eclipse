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
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.GlobalRepositorySystem;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.TrackingEvent;
import org.ebayopensource.turmeric.eclipse.repositorysystem.utils.GlobalProjectHealthChecker;
import org.ebayopensource.turmeric.eclipse.repositorysystem.utils.TurmericServiceUtils;
import org.ebayopensource.turmeric.eclipse.resources.model.SOAConsumerProject;
import org.ebayopensource.turmeric.eclipse.resources.util.SOAConsumerUtil;
import org.ebayopensource.turmeric.eclipse.services.ui.wizards.ConsumeNewServiceWizard;
import org.ebayopensource.turmeric.eclipse.services.ui.wizards.ConsumerFromJavaWizard;
import org.ebayopensource.turmeric.eclipse.ui.SOABaseWizard;
import org.ebayopensource.turmeric.eclipse.utils.ui.UIUtil;
import org.ebayopensource.turmeric.eclipse.validator.utils.ValidateUtil;
import org.ebayopensource.turmeric.eclipse.validator.utils.common.AbstractBaseAccessValidator;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;


/**
 * The Class ConsumeNewService.
 *
 * @author smathew
 */
public class ConsumeNewService implements IObjectActionDelegate {
	private IStructuredSelection selection;
	private static final SOALogger logger = SOALogger.getLogger();

	/**
	 * {@inheritDoc}
	 * @see org.eclipse.ui.IObjectActionDelegate#setActivePart(org.eclipse.jface.action.IAction, org.eclipse.ui.IWorkbenchPart)
	 */
	public void setActivePart(final IAction action,
			final IWorkbenchPart targetPart) {
	}

	/**
	 * {@inheritDoc}
	 *  @see org.eclipse.ui.IActionDelegate#run(org.eclipse.jface.action.IAction)
	 */
	public void run(final IAction action) {
		if (SOALogger.DEBUG)
			logger.entering(action, selection);
		try {
			if (selection == null)
				return;
			
			final IProject project = 
				ActionUtil.preValidateAction(selection.getFirstElement(), logger);
			if (project == null)
				return;
			
			if (TurmericServiceUtils.isSOAProject(project) == false) {
				//This is not a SOA project yet, must convert to a SOA Consumer project first
				if (MessageDialog.openConfirm(UIUtil.getActiveShell(), "Confirmation", 
						"The selected project is not a SOA project, " +
						"and you will be taken to the consumer creation wizard.\n\n"
						+ "Are you sure to proceed?")) {
					//The user want to convert it first
					final IStatus status = ActionUtil.
					validateJavaProjectForConvertingToConsumer(project);
					if (status.isOK() == false) {
						MessageDialog.openError(UIUtil.getActiveShell(), "Error", 
								status.getMessage());
						return;
					}
					
					final ConsumerFromJavaWizard wizard = new ConsumerFromJavaWizard(true);
					wizard.init(null, selection);
					WizardDialog dialog = new WizardDialog(UIUtil.getActiveShell(), wizard);
					dialog.open();
				}
				return;
			}

			final IStatus status = new AbstractBaseAccessValidator() {

				@Override
				public List<IResource> getReadableFiles() {
					//should check the following files
					//client config folder
					final List<IResource> result = new ArrayList<IResource>();
					
					try {
						if (TurmericServiceUtils.isSOAConsumerProject(project) == true) {
							//we should check the client config folder only if the underlying project is consumer project
							result.add(project.getFolder(SOAConsumerProject.META_SRC_ClIENT_CONFIG));
						}
						result.addAll(GlobalProjectHealthChecker.getSOAProjectReadableResources(project));
					} catch (Exception e) {
						logger.warning(e);
					}
					return result;
				}

				@Override
				public List<IResource> getWritableFiles() {
					//we should ensure that the client config folder is writable
					final List<IResource> result = new ArrayList<IResource>();
					result.add(project.getFolder(SOAConsumerProject.META_SRC_ClIENT_CONFIG));
					result.add(GlobalRepositorySystem.instanceOf().getActiveRepositorySystem()
							.getAssetRegistry().getProjectConfigurationFile(project));
					return result;
				}
			}.validate(project.getName());

			final String messages = ValidateUtil.getFormattedStatusMessagesForAction(status);
			if (messages != null) {
				UIUtil.showErrorDialog(UIUtil.getActiveShell(), "Error", 
						messages, (Throwable)null);
				return;
			}
			GlobalRepositorySystem.instanceOf().getActiveRepositorySystem()
			.trackingUsage(new TrackingEvent(
					getClass().getName(), 
					TrackingEvent.TRACKING_ACTION));
			if (SOAConsumerUtil.isOldClientConfigDirStructure(project)) {
				String message = GlobalRepositorySystem.instanceOf()
						.getActiveRepositorySystem().getProjectHealthChecker()
						.getWarningMessageConsumeProjectStructureOld();
				UIUtil.showErrorDialog(UIUtil.getActiveShell(), "Error",
						message, (Throwable) null);
				logger.warning(message, ". Proejct->", project.getName());
				return;
			}
			
			SOABaseWizard wizard = new ConsumeNewServiceWizard();
			wizard.init(PlatformUI.getWorkbench(), selection);
			WizardDialog dialog = new WizardDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), wizard);
			dialog.open();
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
	public void selectionChanged(final IAction action,
			final ISelection selection) {
		this.selection = (IStructuredSelection) selection;
	}
}
