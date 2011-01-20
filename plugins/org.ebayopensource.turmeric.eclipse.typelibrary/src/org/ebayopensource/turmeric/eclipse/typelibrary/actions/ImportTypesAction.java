/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.typelibrary.actions;

import org.ebayopensource.turmeric.eclipse.logging.SOALogger;
import org.ebayopensource.turmeric.eclipse.typelibrary.ui.wizards.ImportTypesWizard;
import org.ebayopensource.turmeric.eclipse.utils.ui.UIUtil;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.IEditorActionDelegate;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.progress.UIJob;


/**
 * Action for import types from wsdl or schema file.
 * 
 * @author mzang
 * 
 */
public class ImportTypesAction implements IObjectActionDelegate,
		IEditorActionDelegate {
	private static final SOALogger logger = SOALogger.getLogger();

	private int mode = ImportTypesWizard.EXPORT_MODE;

	private IFile wsdlFile = null;

	private IProject targetTypeLibrary = null;

	public void setActivePart(IAction action, IWorkbenchPart targetPart) {

	}

	public void run(IAction action) {

		if (SOALogger.DEBUG)
			logger.entering(action);
		try {

			final String wsdlPath = wsdlFile == null ? null : wsdlFile
					.getLocation().toString();
			final UIJob job = new UIJob("Import types") {

				@Override
				public IStatus runInUIThread(IProgressMonitor monitor) {
					new WizardDialog(UIUtil.getActiveShell(),
							new ImportTypesWizard(targetTypeLibrary, wsdlPath,
									mode)).open();
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

	public void selectionChanged(IAction action, ISelection selection) {
		if (selection == null
				|| (selection instanceof IStructuredSelection) == false) {
			return;
		}
		Object selectionObj = ((IStructuredSelection) selection)
				.getFirstElement();
		
		if (selectionObj == null) {
			return;
		}

		mode = ImportTypesWizard.EXPORT_MODE;
		if ((selectionObj instanceof IProject) == true) {
			targetTypeLibrary = (IProject) selectionObj;
			mode = ImportTypesWizard.IMPORT_MODE;
		} else if ((selectionObj instanceof IFile) == true) {
			wsdlFile = (IFile) selectionObj;
			mode = ImportTypesWizard.EXPORT_MODE;
		}
	}

	public void setActiveEditor(IAction action, IEditorPart targetEditor) {
		if (targetEditor == null) {
			return;
		}
		IEditorInput input = targetEditor.getEditorInput();
		if (input instanceof FileEditorInput) {
			final FileEditorInput fileInput = (FileEditorInput) input;
			wsdlFile = fileInput.getFile();
			mode = ImportTypesWizard.EXPORT_MODE;
		}

	}

}
