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

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.ebayopensource.turmeric.eclipse.buildsystem.utils.ActionUtil;
import org.ebayopensource.turmeric.eclipse.logging.SOALogger;
import org.ebayopensource.turmeric.eclipse.registry.intf.IValidationStatus;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.GlobalRepositorySystem;
import org.ebayopensource.turmeric.eclipse.resources.util.SOAServiceUtil;
import org.ebayopensource.turmeric.eclipse.services.resources.SOAMessages;
import org.ebayopensource.turmeric.eclipse.utils.plugin.ProgressUtil;
import org.ebayopensource.turmeric.eclipse.utils.ui.UIUtil;
import org.ebayopensource.turmeric.eclipse.validator.utils.ValidateUtil;
import org.ebayopensource.turmeric.eclipse.validator.utils.common.AbstractBaseAccessValidator;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IEditorActionDelegate;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.progress.IProgressService;


/**
 * @author yayu
 * @since 1.0.0
 */
public class ValidateServiceWSDL implements IObjectActionDelegate, IEditorActionDelegate {

	private IStructuredSelection selection;
	private static final SOALogger logger = SOALogger.getLogger();
	private IStatus regStatus;
	private IEditorPart targetEditor;

	public void setActivePart(final IAction action,
			final IWorkbenchPart targetPart) {
	}
	
	public void setActiveEditor(IAction action, IEditorPart targetEditor) {
		this.targetEditor = targetEditor;
	}
	
	/**
	 * 
	 */
	public ValidateServiceWSDL() {
		super();
	}
	
	private void validateSOAProject(final IProject project) throws InvocationTargetException, InterruptedException {
		if (project == null)
			return;

		final IStatus validationResult = new AbstractBaseAccessValidator() {

			@Override
			public List<IResource> getReadableFiles() {
				//should check the service WSDL file
				try {
					final List<IResource> res = new ArrayList<IResource>();
					res.add(SOAServiceUtil.getWsdlFile(project, project.getName()));
					return res;
				} catch (Exception e) {
					logger.warning(e);
				}
				return new ArrayList<IResource>(1);
			}

			@Override
			public List<IResource> getWritableFiles() {
				return Collections.emptyList();
			}

		}.validate(project.getName());

		final String messages = ValidateUtil.getFormattedStatusMessagesForAction(validationResult);
		if (messages != null) {
			UIUtil.showErrorDialog(UIUtil.getActiveShell(), "Error", 
					messages, (Throwable)null);
			return;
		}
		
		
	}

	public void run(final IAction action) {
		if (SOALogger.DEBUG)
			logger.entering(action, selection);
		try {
			if (selection == null)
				return;
			

			final IFile wsdlFile;
			boolean typeFolding = false;

			if (this.targetEditor == null) {
				Object sel = selection.getFirstElement();
				if (sel instanceof IProject) {
					// invoking via the intf project context menu
					final IProject project = ActionUtil.preValidateAction(selection
							.getFirstElement(), logger);
					validateSOAProject(project);
					typeFolding = SOAServiceUtil.getSOAIntfMetadata(
							SOAServiceUtil.getSOAEclipseMetadata(project))
							.getTypeFolding();
					wsdlFile = SOAServiceUtil.getWsdlFile(project, project
							.getName());
				} else if (sel instanceof IFile) {
					// invoking via the wsdl context menu
					wsdlFile = (IFile) sel;
					typeFolding = SOAServiceUtil.getSOAIntfMetadata(
							SOAServiceUtil.getSOAEclipseMetadata(wsdlFile
									.getProject())).getTypeFolding();
				} else {
					return;
				}
			} else {
				// invoking within the WTP WSDL editor
				final IEditorInput input = this.targetEditor.getEditorInput();
				if (input instanceof FileEditorInput) {
					final FileEditorInput fileInput = (FileEditorInput) input;
					wsdlFile = fileInput.getFile();
					final IProject project = wsdlFile.getProject();
					typeFolding = SOAServiceUtil.getSOAIntfMetadata(
							SOAServiceUtil.getSOAEclipseMetadata(project))
							.getTypeFolding();
				} else {
					return;
				}
			}
			
			final IWorkbench wb = UIUtil.getWorkbench();
			final IProgressService ps = wb.getProgressService();
			ps.run(false, false, new IRunnableWithProgress() {

				public void run(IProgressMonitor monitor)
						throws InvocationTargetException, InterruptedException {
					try {
						monitor.beginTask("Validating service WSDL",
								ProgressUtil.PROGRESS_STEP * 20);
						regStatus = ActionUtil.validateServiceWSDL(wsdlFile,
								wsdlFile.getLocationURI().toURL(), GlobalRepositorySystem.instanceOf()
								.getActiveRepositorySystem().getActiveOrganizationProvider()
								.supportAssertionServiceIntegration(),
								monitor);
					} catch (Exception e) {
						throw new InvocationTargetException(e);
					} finally {
						monitor.done();
					}
					
				}
				
			});
			
			if (regStatus != null && regStatus.isOK() == false) {
				int errorCount = 0, warningCount = 0, mayCount = 0;
				final StringBuffer msg = new StringBuffer();
				if (typeFolding == false) {
					msg
							.append(SOAMessages.WARNING_TYPE_FOLDING_DISABLED_VALIDATION
									+ "\r\n\r\n");
				}
				msg.append(SOAMessages.ISSUES_DIALOG_OPENING);

				if (regStatus.isMultiStatus() == true) {

					for (IStatus status : regStatus.getChildren()) {
						switch (status.getSeverity()) {
						case IStatus.WARNING:
							if (status.getCode() == IValidationStatus.CODE_MAY)
								mayCount++;
							else
								warningCount++;
							break;
						case IStatus.ERROR:
							errorCount++;
							break;
						}
					}
				}

				if (errorCount > 0) {
					msg.append("\n");
					msg.append(SOAMessages.ISSUES_DIALOG_MUST_FIX);
					msg.append(errorCount);
				}
				if (warningCount > 0) {
					msg.append("\n");
					msg.append(SOAMessages.ISSUES_DIALOG_SHOULD_FIX);
					msg.append(warningCount);
				}
				if (mayCount > 0) {
					msg.append("\n");
					msg.append(SOAMessages.ISSUES_DIALOG_MAY_FIX);
					msg.append(mayCount);
				}

				if (regStatus.getException() != null
						|| regStatus.isMultiStatus() == true) {
					msg.append("\n\n");
					msg.append(SOAMessages.ISSUES_DIALOG_COPY_TO_CLIPBOARD);
				}
				String title = regStatus.getSeverity() == IStatus.WARNING ? "Problems Occured"
						: "Validation Failed";
				logger.warning(msg);
				logger.warning(regStatus);
				UIUtil.showErrorDialog(UIUtil.getActiveShell(), title, msg
						.toString(), regStatus, true, true, false, false);
				if (SOALogger.DEBUG)
					logger.exiting(false);
			} else if (regStatus != null && regStatus.isOK() == true) {
				// validation is OK
				MessageDialog
						.openInformation(UIUtil.getActiveShell(),
								"Validation Result",
								"Congratulations! It appears no issues found during validation.");
			}
		} catch (Exception e) {
			logger.error(e);
			UIUtil.showErrorDialog(e);
		} finally {
			if (SOALogger.DEBUG)
				logger.exiting();
		}
	}

	public void selectionChanged(final IAction action,
			final ISelection selection) {
		this.selection = (IStructuredSelection) selection;
	}
}
