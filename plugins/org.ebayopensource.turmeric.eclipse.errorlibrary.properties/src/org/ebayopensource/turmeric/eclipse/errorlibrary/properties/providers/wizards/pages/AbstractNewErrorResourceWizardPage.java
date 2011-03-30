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
package org.ebayopensource.turmeric.eclipse.errorlibrary.properties.providers.wizards.pages;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.ebayopensource.turmeric.eclipse.core.resources.constants.SOAProjectConstants.SupportedProjectType;
import org.ebayopensource.turmeric.eclipse.errorlibrary.properties.eclipse.TurmericErrorLibraryProjectNature;
import org.ebayopensource.turmeric.eclipse.errorlibrary.properties.resources.SOAMessages;
import org.ebayopensource.turmeric.eclipse.logging.SOALogger;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.GlobalRepositorySystem;
import org.ebayopensource.turmeric.eclipse.resources.util.SOAServiceUtil;
import org.ebayopensource.turmeric.eclipse.ui.AbstractSOAResourceWizardPage;
import org.ebayopensource.turmeric.eclipse.ui.components.ProjectSelectionListLabelProvider;
import org.ebayopensource.turmeric.eclipse.utils.plugin.WorkspaceUtil;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.ElementListSelectionDialog;


/**
 * @author yayu
 *
 */
public abstract class AbstractNewErrorResourceWizardPage extends 
AbstractSOAResourceWizardPage {
	private IStructuredSelection selection;
	
	protected Text errorLibText;
	

	/**
	 * @param pageName
	 * @param title
	 * @param description
	 */
	public AbstractNewErrorResourceWizardPage(String pageName, String title,
			String description, IStructuredSelection selection) {
		super(pageName, title, description);
		this.selection = selection;
	}

	protected Text createErrorLibrarySelector(Composite parent) throws CoreException {
		new Label(parent, SWT.LEFT).setText(SOAMessages.TEXT_NAME_ERROR_LIBRARY);
		errorLibText = new Text(parent, SWT.BORDER);
		final GridData data = new GridData(GridData.FILL_HORIZONTAL);
		data.horizontalSpan = 2;
		errorLibText.setLayoutData(data);
		errorLibText.setEditable(false);
		
		if (selection != null) {
			if (selection.getFirstElement() instanceof IAdaptable) {
				final IProject project = (IProject) ((IAdaptable) selection.getFirstElement())
				.getAdapter(IProject.class);
				if (project != null && 
						SOAServiceUtil.hasNatures(project,  
								GlobalRepositorySystem.instanceOf().getActiveRepositorySystem()
								.getProjectNatureId(SupportedProjectType.ERROR_LIBRARY))) {
					errorLibText.setText(project.getName());
				}
			}
		}
		
		final Button selectErrorLibBtn = new Button(parent, SWT.PUSH);
		selectErrorLibBtn.setAlignment(SWT.LEFT);
		selectErrorLibBtn.setText(SOAMessages.BUTTON_NAME_BROWSE);
		final SelectionListener typeLibNameBrowseListener = new SelectionListener() {
			public void widgetDefaultSelected(final SelectionEvent e) {
				widgetSelected(e);
			}

			public void widgetSelected(final SelectionEvent e) {
				ElementListSelectionDialog selectionDialog = new ElementListSelectionDialog(
						getShell(), new ProjectSelectionListLabelProvider()) {

					@Override
					protected Control createDialogArea(Composite parent) {
						final Control control = super.createDialogArea(parent);
						/*UIUtil.getHelpSystem().setHelp(
								control, GlobalRepositorySystem.instanceOf().getActiveRepositorySystem()
								.getHelpProvider().getHelpContextID(
										ISOAHelpProvider.DIALOG_SELECT_TYPE_LIBRARY));*/
						return control;
					}
				};
				selectionDialog.setTitle(SOAMessages.DIALOG_TITLE_SELECT_ERROR_LIBRARY);

				final List<IProject> projects = new ArrayList<IProject>();
				try {
					projects.addAll(WorkspaceUtil.getProjectsByNature(
							TurmericErrorLibraryProjectNature.getNatureId()));
				} catch (CoreException e1) {
					SOALogger.getLogger().warning(e1);
				}
				
				selectionDialog.setElements(projects
						.toArray(new IProject[0]));

				selectionDialog.setBlockOnOpen(true);
				selectionDialog.setMultipleSelection(false);
				if (selectionDialog.open() == Window.OK) {
					if (selectionDialog.getResult() != null
							&& selectionDialog.getResult().length > 0) {
						String projectName = ((IProject) selectionDialog
								.getResult()[0]).getName();
						errorLibText.setText(projectName);
						errorLibraryChanged();
						dialogChanged();
					}
				}

			}
		};
		selectErrorLibBtn.addSelectionListener(typeLibNameBrowseListener);

		return errorLibText;
	}
	
	@Override
	protected boolean dialogChanged() {
		if( super.dialogChanged() == false)
			return false;
		
		if (StringUtils.isBlank(getErrorLibrary())) {
			updateStatus(errorLibText, 
					SOAMessages.ERROR_NO_ERROR_LIBRARY);
			return false;
		}
		return true;
	}

	protected abstract void errorLibraryChanged();
	
	public String getErrorLibrary() {
		return getTextValue(errorLibText);
	}
}
