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
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.GlobalRepositorySystem;
import org.ebayopensource.turmeric.eclipse.resources.model.ISOAProject;
import org.ebayopensource.turmeric.eclipse.resources.model.SOAIntfProject;
import org.ebayopensource.turmeric.eclipse.services.ui.dialogs.ChangeServiceVersionDialog;
import org.ebayopensource.turmeric.eclipse.utils.ui.UIUtil;
import org.eclipse.core.resources.IProject;
import org.eclipse.jface.action.IAction;
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
public class ChangeServiceVersion implements IObjectActionDelegate {
	
	/** The selection. */
	private IStructuredSelection selection;
	
	/** The Constant logger. */
	private static final SOALogger logger = SOALogger.getLogger();
	
	/** The shell. */
	private Shell shell;

	/**
	 * Instantiates a new change service version.
	 */
	public ChangeServiceVersion() {
		super();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setActivePart(final IAction action,
			final IWorkbenchPart targetPart) {
		shell = targetPart.getSite().getShell();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
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
			ISOAProject soaProject = GlobalRepositorySystem.instanceOf()
					.getActiveRepositorySystem().getAssetRegistry()
					.getSOAProject(project);
			if (soaProject instanceof SOAIntfProject) {
				ChangeServiceVersionDialog chagneVersionDialog = new ChangeServiceVersionDialog(
						shell, (SOAIntfProject) soaProject);
				chagneVersionDialog.open();
			} else {
				logger.warning("Selected project is not "
						+ "a service interface project.");
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
	 */
	@Override
	public void selectionChanged(final IAction action,
			final ISelection selection) {
		this.selection = (IStructuredSelection) selection;
	}

}
