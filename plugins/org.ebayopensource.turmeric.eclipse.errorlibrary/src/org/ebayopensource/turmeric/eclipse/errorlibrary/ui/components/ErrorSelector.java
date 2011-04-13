/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.errorlibrary.ui.components;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.ebayopensource.turmeric.eclipse.core.resources.constants.SOAProjectConstants;
import org.ebayopensource.turmeric.eclipse.errorlibrary.resources.SOAMessages;
import org.ebayopensource.turmeric.eclipse.errorlibrary.views.ISOAError;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.GlobalRepositorySystem;
import org.ebayopensource.turmeric.eclipse.repositorysystem.utils.TurmericServiceUtils;
import org.ebayopensource.turmeric.eclipse.utils.lang.StringUtil;
import org.ebayopensource.turmeric.eclipse.utils.plugin.WorkspaceUtil;
import org.ebayopensource.turmeric.eclipse.utils.ui.UIUtil;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.TwoPaneElementSelector;


/**
 * @author smathew
 * 
 * The selection dialog for adding errors
 */
public class ErrorSelector extends TwoPaneElementSelector {

	@Override
	protected boolean validateCurrentSelection() {
		IStatus status = new Status(IStatus.OK, PlatformUI.PLUGIN_ID,
				IStatus.OK, "", //$NON-NLS-1$
				null);
		if (super.validateCurrentSelection()) {

			for (ISOAError error : getCurrentSelection()) {
				String errorLibrary = error.getDomain().getLibrary().getName();
				IProject errorLibProject = WorkspaceUtil
						.getProject(errorLibrary);
				// first check if there is a workspace project with the same
				// name
				// and with soa error lib nature
				if (errorLibProject != null && errorLibProject.isAccessible()
						&& isErrorLibrary(errorLibProject)) {
					// this is fine, there is an error lib project with the same
					// name in workspace
				} else {
					// finding out if the jar is exists in repo.
					String location = "";
					try {
						location = GlobalRepositorySystem.instanceOf()
								.getActiveRepositorySystem().getAssetRegistry()
								.getAssetLocation(errorLibrary);
						if (StringUtils.isEmpty(location)
								|| !StringUtils.equalsIgnoreCase(
										SOAProjectConstants.FILE_EXTENSION_JAR,
										StringUtils.substringAfterLast(
												location, "."))) {
							status = new Status(IStatus.ERROR,
									PlatformUI.PLUGIN_ID, IStatus.ERROR,
									StringUtil.formatString(SOAMessages.LIBRARY_NOT_BUILT_ERR_MSG
											, errorLibrary), null);

							updateStatus(status);
						}
					} catch (Exception e) {
						status = new Status(IStatus.ERROR,
								PlatformUI.PLUGIN_ID, IStatus.ERROR,
								SOAMessages.LIBRARY_NOT_BUILT_ERR_MSG
										+ errorLibrary, null);
						updateStatus(status);

					}

				}
			}
		}
		return status.isOK();
	}

	private ArrayList<ISOAError> selectedErrors = new ArrayList<ISOAError>();

	@Override
	protected void computeResult() {
		super.computeResult();
		selectedErrors = new ArrayList<ISOAError>();
		if (fFilteredList != null) {
			if (fFilteredList.getSelectionIndex() >= 0) {
				if (fFilteredList.getSelection() != null) {
					for (Object obj : fFilteredList.getSelection()) {
						if (obj instanceof ISOAError)
							selectedErrors.add((ISOAError) obj);
					}
				}
			}
		}

	}

	private List<ISOAError> getCurrentSelection() {
		List<ISOAError> retErrors = new ArrayList<ISOAError>();
		if (fFilteredList != null) {
			if (fFilteredList.getSelectionIndex() >= 0) {
				if (fFilteredList.getSelection() != null) {
					for (Object obj : fFilteredList.getSelection()) {
						if (obj instanceof ISOAError)
							retErrors.add((ISOAError) obj);
					}
				}
			}
		}
		return retErrors;
	}

	public ErrorSelector(Shell parent, String title, ISOAError errors[]) {
		super(parent, new ErrorSelectorElementRenderer(),
				new ErrorSelectorQualifiedRenderer());
		setTitle(title);
		try {
			setElements(errors);
		} catch (Exception e) {
			UIUtil.showErrorDialog(parent, e.getMessage(), SOAMessages.DIALOG_TITLE_ERROR,
					SOAMessages.BAD_ERR_REGISTRY);
		}
	}

	public ArrayList<ISOAError> getSelectedErrors() {
		if (selectedErrors == null) {
			selectedErrors = new ArrayList<ISOAError>();
		}
		return selectedErrors;
	}

	public void setSelectedTypes(ArrayList<ISOAError> selectedErrors) {
		this.selectedErrors = selectedErrors;
	}

	private static boolean isErrorLibrary(IProject project) {
		try {
			return TurmericServiceUtils.isSOAErrorLibraryProject(project);
		} catch (CoreException e) {
			// this means that the nature was not accessible, In that case also
			// this is not a valid error lib project
			return false;
		}
	}
}
