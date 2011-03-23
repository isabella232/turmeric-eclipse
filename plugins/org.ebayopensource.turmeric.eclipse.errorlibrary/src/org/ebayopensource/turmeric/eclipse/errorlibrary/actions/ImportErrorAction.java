/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.errorlibrary.actions;

import java.util.List;

import org.ebayopensource.turmeric.eclipse.buildsystem.utils.BuildSystemUtil;
import org.ebayopensource.turmeric.eclipse.core.exception.SOAExceptionHandler;
import org.ebayopensource.turmeric.eclipse.errorlibrary.resources.SOAMessages;
import org.ebayopensource.turmeric.eclipse.errorlibrary.ui.components.ErrorSelector;
import org.ebayopensource.turmeric.eclipse.errorlibrary.utils.ErrorLibraryUtil;
import org.ebayopensource.turmeric.eclipse.errorlibrary.views.ISOAError;
import org.ebayopensource.turmeric.eclipse.errorlibrary.views.SOAErrContentFactory;
import org.ebayopensource.turmeric.eclipse.exception.resources.SOAGetErrorLibraryProviderFailedException;
import org.ebayopensource.turmeric.eclipse.exception.resources.projects.SOAOperationNotAvailableException;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.GlobalRepositorySystem;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.ISOAProjectConfigurer;
import org.ebayopensource.turmeric.eclipse.resources.model.AssetInfo;
import org.ebayopensource.turmeric.eclipse.ui.actions.BaseEditorActionDelegate;
import org.ebayopensource.turmeric.eclipse.utils.plugin.ProgressUtil;
import org.ebayopensource.turmeric.eclipse.utils.ui.UIUtil;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.text.ITextOperationTarget;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchPartSite;


/**
 * This action will be shown in the jdt editor. Imports the selected error and
 * perform the following steps
 * 
 * 1) Add a code snippet to the editor. 2) Updates the dependency xml. 3)
 * Updates the dependency(library). 4) Add Provider APP to service config xml is
 * required.
 * 
 * 
 * @author smathew
 */
public class ImportErrorAction extends BaseEditorActionDelegate {

	public ImportErrorAction() {
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.actions.ActionDelegate#run(org.eclipse.jface.action.IAction)
	 */
	public void run(IAction action) {
		try {
			ErrorSelector errorSelector = new ErrorSelector(UIUtil
					.getActiveShell(), "Select Error", ErrorLibraryUtil
					.getAllErrors(SOAErrContentFactory.getProvider()).toArray(
							new ISOAError[0]));
			errorSelector.setMultipleSelection(true);
			if (errorSelector.open() == Window.OK) {

				for (ISOAError error : errorSelector.getSelectedErrors()) {
					// add the source
					String code = ErrorLibraryUtil
							.getImportSource(getSelectedProject(),error);
					ITextOperationTarget textOperationTarget = (ITextOperationTarget) editorPart
							.getAdapter(ITextOperationTarget.class);
					if (textOperationTarget
							.canDoOperation(ITextOperationTarget.PASTE)) {
						copyCode(textOperationTarget, code);
					} else {
						MessageDialog.openWarning(UIUtil.getActiveShell(),
								SOAMessages.PASTE_OP_NOT_AVAILABLE,
								SOAMessages.PASTE_OP_NOT_AVAILABLE_RES + code);
					}
					// add the dependency
					ISOAProjectConfigurer projectConfigurer = GlobalRepositorySystem
							.instanceOf().getActiveRepositorySystem()
							.getProjectConfigurer();
					
					try {
						List<String> commonLibraries = GlobalRepositorySystem
								.instanceOf().getActiveRepositorySystem()
								.getActiveOrganizationProvider()
								.getCommonErrorLibraries();
						for (String libName : commonLibraries) {
							projectConfigurer.addDependency(
									getSelectedProject().getName(), libName,
									AssetInfo.TYPE_LIBRARY, true,
									ProgressUtil.getDefaultMonitor(null));
						}
						projectConfigurer.addDependency(getSelectedProject()
								.getName(), error.getDomain().getLibrary()
								.getName(), AssetInfo.TYPE_LIBRARY, true,
								ProgressUtil.getDefaultMonitor(null));
					} catch (Exception e) {
						SOAExceptionHandler.silentHandleException(e);
						UIUtil.showErrorDialog(
								SOAMessages.DEP_MANIFEST_READ_ONLY, e);
					}
				}
				BuildSystemUtil
						.updateSOAClasspathContainer(getSelectedProject());

			}

		} catch (SOAGetErrorLibraryProviderFailedException e) {
			SOAExceptionHandler.handleException(e);
		} catch (SOAOperationNotAvailableException e) {
			SOAExceptionHandler.handleException(e);
		} catch (CoreException e) {
			SOAExceptionHandler.handleException(e);
		}
	}

	private void copyCode(ITextOperationTarget textOperationTarget, String code)
			throws SOAOperationNotAvailableException, CoreException {
		Clipboard clipboard = new Clipboard(getDisplay());
		TextTransfer textTransfer[] = new TextTransfer[1];
		textTransfer[0] = TextTransfer.getInstance();
		String data[] = new String[1];
		data[0] = code;
		clipboard.setContents(data, textTransfer);
		textOperationTarget.doOperation(ITextOperationTarget.PASTE);
	}

	private Shell getShell() {
		if (editorPart != null) {
			IWorkbenchPartSite site = editorPart.getSite();
			Shell shell = site.getShell();
			if (shell != null && !shell.isDisposed()) {
				return shell;
			}
		}
		return null;
	}

	private Display getDisplay() {
		Shell shell = getShell();
		if (shell != null) {
			return shell.getDisplay();
		}
		return null;
	}
}
