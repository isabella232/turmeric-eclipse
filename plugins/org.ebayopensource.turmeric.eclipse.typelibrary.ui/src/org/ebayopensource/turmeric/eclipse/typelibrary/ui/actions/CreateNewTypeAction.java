/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.typelibrary.ui.actions;

import org.ebayopensource.turmeric.eclipse.buildsystem.utils.ActionUtil;
import org.ebayopensource.turmeric.eclipse.core.logging.SOALogger;
import org.ebayopensource.turmeric.eclipse.typelibrary.ui.wizards.TypeSelectionWizard;
import org.ebayopensource.turmeric.eclipse.utils.ui.UIUtil;
import org.ebayopensource.turmeric.eclipse.validator.utils.ValidateUtil;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.progress.UIJob;


/**
 * The Class CreateNewTypeAction.
 *
 * @author smathew
 * 
 * "Create Type" action in the context menu of the viewer
 * 
 * Not used now TODO: Delete this class if the PM does not need this menu
 * anymore
 * Putting a key work "Stale_Class" for easy search later
 */
public class CreateNewTypeAction implements IObjectActionDelegate {
	private IStructuredSelection selection;
	private static final SOALogger logger = SOALogger.getLogger();

	/**
	 * {@inheritDoc}
	 */
	public void setActivePart(final IAction action,
			final IWorkbenchPart targetPart) {
	}

	/**
	 * {@inheritDoc}
	 */
	public void run(final IAction action) {
		if (SOALogger.DEBUG)
			logger.entering(action, selection);
		try {
			if (selection == null)
				return;

			final IProject project = ActionUtil.preValidateAction(selection
					.getFirstElement(), logger);
			if (project == null)
				return;
			final IStatus status = org.ebayopensource.turmeric.eclipse.typelibrary.ui.actions.ActionUtil
					.validateTypeDependencyAndProjectConfigFile(project);

			final String messages = ValidateUtil
					.getFormattedStatusMessagesForAction(status);
			if (messages != null) {
				UIUtil.showErrorDialog(UIUtil.getActiveShell(), "Error",
						messages, (Throwable) null);
				return;
			}
			final UIJob job = new UIJob("") {
				@Override
				public IStatus runInUIThread(final IProgressMonitor monitor) {
					new WizardDialog(UIUtil.getActiveShell(),
							new TypeSelectionWizard()).open();
					return Status.OK_STATUS;
				}

			};
			job.setSystem(true);
			job.schedule();

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
	 */
	public void selectionChanged(final IAction action,
			final ISelection selection) {
		this.selection = (IStructuredSelection) selection;
	}
}
