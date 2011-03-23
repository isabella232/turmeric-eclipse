/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.typelibrary.actions;

import org.ebayopensource.turmeric.eclipse.buildsystem.utils.ActionUtil;
import org.ebayopensource.turmeric.eclipse.logging.SOALogger;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.GlobalRepositorySystem;
import org.ebayopensource.turmeric.eclipse.resources.constants.SOAProjectConstants.SupportedProjectType;
import org.ebayopensource.turmeric.eclipse.resources.util.MarkerUtil;
import org.ebayopensource.turmeric.eclipse.typelibrary.builders.TypeLibraryBuilderUtils;
import org.ebayopensource.turmeric.eclipse.typelibrary.builders.TypeLibraryProjectNature;
import org.ebayopensource.turmeric.eclipse.typelibrary.buildsystem.TypeLibSynhcronizer;
import org.ebayopensource.turmeric.eclipse.utils.plugin.ProgressUtil;
import org.ebayopensource.turmeric.eclipse.utils.plugin.WorkspaceUtil;
import org.ebayopensource.turmeric.eclipse.utils.ui.UIUtil;
import org.ebayopensource.turmeric.eclipse.validator.utils.ValidateUtil;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;


/**
 * @author smathew
 * 
 * Sync Dep Action in the context menu.
 * 
 * This is a very important action used in type lib world.
 * 
 * This action performs the following steps. 1) validates the project before
 * anything 2) syncronize Wsdl and Dep Xml if its intf project 3) syncronize All
 * XSDs and Dep Xml if its typelib project 4) rerfesh the workspace
 */
public class SyncUpDependencyAction implements IObjectActionDelegate {
	private IStructuredSelection selection;
	private static final SOALogger logger = SOALogger.getLogger();

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IObjectActionDelegate#setActivePart(org.eclipse.jface.action.IAction,
	 *      org.eclipse.ui.IWorkbenchPart)
	 */
	public void setActivePart(final IAction action,
			final IWorkbenchPart targetPart) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IActionDelegate#run(org.eclipse.jface.action.IAction)
	 */
	public void run(final IAction action) {
		if (SOALogger.DEBUG)
			logger.entering(action, selection);
		try {
			if (selection == null)
				return;
			final IProject project = ActionUtil.preValidateAction(selection
					.getFirstElement(), logger);
			if (validate(project)) {
				executeTask(project);
				MessageDialog
						.openInformation(UIUtil.getActiveShell(),
								"The operation was performed successfully.",
								"The dependency information for this project has been updated successfully.");
			}
		} catch (Exception e) {
			logger.error(e);
			UIUtil
					.openChoiceDialog(
							"The selected operation cannot be performed.",
							"This might be due to one of the following reasons, \r 1) Type Library registry is not synchronized, Please go to Types Explorer view and select refresh. ",
							MessageDialog.INFORMATION);
		} finally {
			if (SOALogger.DEBUG)
				logger.exiting();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IActionDelegate#selectionChanged(org.eclipse.jface.action.IAction,
	 *      org.eclipse.jface.viewers.ISelection)
	 */
	public void selectionChanged(final IAction action,
			final ISelection selection) {
		this.selection = (IStructuredSelection) selection;
	}

	private boolean validate(IProject project) throws Exception {
		String intfNatureId = GlobalRepositorySystem.instanceOf()
		.getActiveRepositorySystem().getProjectNatureId(SupportedProjectType.INTERFACE);
		if (project == null
				|| !(project.hasNature(GlobalRepositorySystem.instanceOf()
						.getActiveRepositorySystem().getProjectNatureId(SupportedProjectType.TYPE_LIBRARY)) || project
						.hasNature(intfNatureId))) {
			UIUtil.showErrorDialog(null,
							"This action is not available for this project.",
							"Action not available",
							"Please check if this project is either a TypeLibrary or SOA Service project.");
			return false;
		}

		if (project.hasNature(TypeLibraryProjectNature.getTypeLibraryNatureId())) {
			IStatus status = TypeLibraryBuilderUtils
					.checkProjectHealth(project);
			if (status.isOK() == false) {
				UIUtil.showErrorDialog(null, "This project has some issues.",
						"Action could not be performed",
						"Please check the problems view and fix the issues.");
				MarkerUtil.createSOAProblemMarker(status, project);
				return false;
			}
		}
		
		if (project.hasNature(intfNatureId)) {
			final IStatus status = TypeLibraryBuilderUtils
					.checkProjectHealthForIntfProject(project);
			if (status.isOK() == false) {
				MarkerUtil.createSOAProblemMarker(status, project);
				return false;
			}
		}
		final IStatus status = org.ebayopensource.turmeric.eclipse.typelibrary.actions.ActionUtil
				.validateTypeDependencyAndProjectConfigFile(project);

		final String messages = ValidateUtil
				.getFormattedStatusMessagesForAction(status);
		if (messages != null) {
			UIUtil.showErrorDialog(UIUtil.getActiveShell(), "Error", messages,
					(Throwable) null);
			return false;
		}
		return true;
	}

	private void executeTask(final IProject project) throws CoreException,
			Exception {

		WorkspaceUtil.refresh(project);
		if (project.hasNature(TypeLibraryProjectNature.getTypeLibraryNatureId())) {
			TypeLibSynhcronizer.syncronizeAllXSDsandDepXml(project);
		} else if (project.hasNature(GlobalRepositorySystem.instanceOf()
				.getActiveRepositorySystem().getProjectNatureId(SupportedProjectType.INTERFACE))) {
			TypeLibSynhcronizer.syncronizeWsdlandDepXml(project);
		}
		TypeLibSynhcronizer.synchronizeTypeDepandProjectDep(project, 
				ProgressUtil.getDefaultMonitor(null));
		WorkspaceUtil.refresh(project);
	}

}
