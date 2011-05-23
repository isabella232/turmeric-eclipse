/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.services.ui.dialogs;

import java.lang.reflect.InvocationTargetException;

import org.ebayopensource.turmeric.eclipse.core.logging.SOALogger;
import org.ebayopensource.turmeric.eclipse.exception.resources.SOAResourceModifyFailedException;
import org.ebayopensource.turmeric.eclipse.exception.validation.ValidationInterruptedException;
import org.ebayopensource.turmeric.eclipse.repositorysystem.ui.utils.ActionUtil;
import org.ebayopensource.turmeric.eclipse.resources.model.SOAIntfProject;
import org.ebayopensource.turmeric.eclipse.services.ui.SOAMessages;
import org.ebayopensource.turmeric.eclipse.services.ui.wizards.ServiceFromWSDLWizard;
import org.ebayopensource.turmeric.eclipse.utils.plugin.ProgressUtil;
import org.ebayopensource.turmeric.eclipse.utils.ui.UIUtil;
import org.ebayopensource.turmeric.eclipse.validator.utils.common.ServiceVersionValidator;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.jface.fieldassist.FieldDecoration;
import org.eclipse.jface.fieldassist.FieldDecorationRegistry;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.actions.WorkspaceModifyOperation;
import org.osgi.framework.Version;

/**
 * The Class ChangeServiceVersionDialog.
 *
 * @author yayu
 */
public class ChangeServiceVersionDialog extends TitleAreaDialog {
	
	private static SOALogger logger = SOALogger.getLogger();

	private SOAIntfProject intfProject;

	private Shell parentShell;
	
	private Text newVersionText;
	
	private Text oldVersionText;
	
	private Button maintenanceBtn;
	
	private Button minorBtn;
	
	private Button majorBtn;
	
	private Version oldVersion;
	
	private String oldVersionStr;
	
	/**
	 * Instantiates a new change service version dialog.
	 *
	 * @param parentShell the parent shell
	 * @param intfProject the intf project
	 */
	public ChangeServiceVersionDialog(Shell parentShell,
			SOAIntfProject intfProject) {
		super(parentShell);
		this.intfProject = intfProject;
		this.parentShell = parentShell;
		oldVersionStr = this.intfProject.getMetadata().getServiceVersion();
		oldVersion = new Version(oldVersionStr);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.dialogs.TitleAreaDialog#createDialogArea(org.eclipse
	 * .swt.widgets.Composite)
	 */
	@Override
	protected Control createDialogArea(Composite parent) {

		Composite control = (Composite) super.createDialogArea(parent);
		setTitle(SOAMessages.CHANGE_SERVICE_VERSION_DIALOG_TITLE);
		setMessage(SOAMessages.CHANGE_SERVICE_VERSION_DIALOG_TITLE);
		Composite parentComposite = new Composite(control, SWT.NONE);
		GridLayout layout = new GridLayout(2, false);
		layout.marginHeight = convertVerticalDLUsToPixels(IDialogConstants.VERTICAL_MARGIN);
		layout.marginWidth = convertHorizontalDLUsToPixels(IDialogConstants.HORIZONTAL_MARGIN);
		layout.verticalSpacing = convertVerticalDLUsToPixels(IDialogConstants.VERTICAL_SPACING);
		layout.horizontalSpacing = convertHorizontalDLUsToPixels(IDialogConstants.HORIZONTAL_SPACING);
		parentComposite.setLayout(layout);
		parentComposite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		parentComposite.setFont(parentComposite.getFont());

		Group group = new Group(parentComposite, SWT.SHADOW_ETCHED_IN);
		GridData data = new GridData(GridData.FILL_BOTH);
		data.horizontalSpan = 2;
		group.setLayoutData(data);
		layout = new GridLayout(1, true);
		group.setLayout(layout);
		layout.marginLeft = 3;
		maintenanceBtn = new Button(group, SWT.RADIO);
		maintenanceBtn.setText(SOAMessages.CHANGE_MAINTENANCE_VERSION_LABEL);
		ControlDecoration controlDecoration = new ControlDecoration(maintenanceBtn,
				SWT.LEFT | SWT.TOP);
		controlDecoration.setShowOnlyOnFocus(false);
		controlDecoration
				.setDescriptionText(SOAMessages.CHANGE_MAINTENANCE_VERSION_DECORATION);
		FieldDecoration fieldDecoration = FieldDecorationRegistry.getDefault()
				.getFieldDecoration(FieldDecorationRegistry.DEC_INFORMATION);
		controlDecoration.setImage(fieldDecoration.getImage());
		maintenanceBtn.addSelectionListener(new SelectionListener(){

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				widgetSelected(e);
			}

			@Override
			public void widgetSelected(SelectionEvent e) {
				maintenanceButtonSelected();
			}
			
		});

		minorBtn = new Button(group, SWT.RADIO);
		minorBtn.setText(SOAMessages.CHANGE_MINOR_VERSION_LABEL);
		controlDecoration = new ControlDecoration(minorBtn, SWT.LEFT | SWT.TOP);
		controlDecoration.setShowOnlyOnFocus(false);
		controlDecoration
				.setDescriptionText(SOAMessages.CHANGE_MINOR_VERSION_DECORATION);
		controlDecoration.setImage(fieldDecoration.getImage());
		minorBtn.addSelectionListener(new SelectionListener(){

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				widgetSelected(e);
			}

			@Override
			public void widgetSelected(SelectionEvent e) {
				minorButtonSelected();
			}
			
		});

		majorBtn = new Button(group, SWT.RADIO);
		majorBtn.setText(SOAMessages.CHANGE_MAJOR_VERSION_LABEL);
		controlDecoration = new ControlDecoration(majorBtn, SWT.LEFT | SWT.TOP);
		controlDecoration.setShowOnlyOnFocus(false);
		controlDecoration
				.setDescriptionText(SOAMessages.CHANGE_MAJOR_VERSION_DECORATION);
		controlDecoration.setImage(fieldDecoration.getImage());
		majorBtn.addSelectionListener(new SelectionListener(){

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				widgetSelected(e);
			}

			@Override
			public void widgetSelected(SelectionEvent e) {
				majorButtonSelected();
			}
			
		});

		Label label = new Label(parentComposite, SWT.LEFT);
		label.setText(SOAMessages.EXISTING_SERVICE_VERSION);
		oldVersionText = new Text(parentComposite, SWT.BORDER | SWT.READ_ONLY);
		oldVersionText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		oldVersionText.setText(oldVersionStr);

		label = new Label(parentComposite, SWT.LEFT);
		label.setText(SOAMessages.NEW_SERVICE_VERSION);
		newVersionText = new Text(parentComposite, SWT.BORDER);
		newVersionText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		newVersionText.setText("1.2.0");
		newVersionText.addModifyListener(new ModifyListener(){

			@Override
			public void modifyText(ModifyEvent e) {
				validateNewVersionText();
			}
			
		});
		newVersionText.addKeyListener(new KeyListener(){

			@Override
			public void keyPressed(KeyEvent e) {
			}

			@Override
			public void keyReleased(KeyEvent e) {
				// only when newVersionText is editable, remember this string.
				// it means this is a manual input.
				if (newVersionText.getEditable() == true) {
					String newVersionStr = newVersionText.getText();
					newVersionText.setData(newVersionStr);
				}
			}
			
		});
		newVersionText.setData(oldVersionStr);
		Dialog.applyDialogFont(control);
		return control;
	}
	
	private void validateNewVersionText(){
		this.setErrorMessage(null);
		
		if(majorBtn.getSelection() == true){
			return;
		}
		
		String newVersionStr = newVersionText.getText();
		
		// validate new version format first.
		try {
			IStatus status = ServiceVersionValidator.getInstance().validate(
					newVersionStr);
			if (status.isOK() == false) {
				this.setErrorMessage("Error: " + status.getMessage());
				return;
			}
		} catch (ValidationInterruptedException e) {
			this.setErrorMessage("Error: " + e.toString());
			return;
		}
		
		
		Version newVersion = new Version(newVersionStr);
		
		if(newVersion.compareTo(oldVersion) < 0){
			this.setErrorMessage(SOAMessages.NEW_VERSION_SMALLER_ERROR_MESSAGE);
			return;
		}
		
		// boolean maintenanceChanged = newVersion.getMicro() != oldVersion.getMicro();
		boolean minorChanged = newVersion.getMinor() != oldVersion.getMinor();
		boolean majorChanged = newVersion.getMajor() != oldVersion.getMajor();

		if(minorBtn.getSelection() == true){
			if(majorChanged == true){
				this.setErrorMessage(SOAMessages.SHOULD_ONLY_CHANGE_MAINTENANCE_VERSION_OR_MINIOR_VERSION_ERROR_MESSAGE);
			}
		}else if(maintenanceBtn.getSelection() == true){
			if(majorChanged == true || minorChanged == true){
				this.setErrorMessage(SOAMessages.SHOULD_ONLY_CHANGE_MAINTENANCE_VERSION_ERROR_MESSAGE);
			}
		}
	}

	private void maintenanceButtonSelected() {
		newVersionText.setEditable(true);
		newVersionText.setText((String)newVersionText.getData());
		validateNewVersionText();
	}
	
	private void minorButtonSelected() {
		newVersionText.setEditable(true);
		newVersionText.setText((String)newVersionText.getData());
		validateNewVersionText();
	}

	private void majorButtonSelected() {
		newVersionText.setEditable(false);
		newVersionText.setText(SOAMessages.NEW_MAJOR_SERVICE_VERSION_NOTIFICATION);
		validateNewVersionText();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.dialogs.Dialog#okPressed()
	 */
	@Override
	protected void okPressed() {
		final String newVersionStr = newVersionText.getText();
		boolean changeMajor = majorBtn.getSelection();
		super.okPressed();
		if(changeMajor == true){
			ServiceFromWSDLWizard svcFromWsld = new ServiceFromWSDLWizard();
			WizardDialog dialog = new WizardDialog(parentShell, svcFromWsld);
			dialog.open();
		}else{
			changeServiceVersionOperations(oldVersionStr, newVersionStr);
		}
	}
	
	private boolean changeServiceVersionOperations(final String oldVersionStr,
			final String newVersionStr) {
		final WorkspaceModifyOperation operation = new WorkspaceModifyOperation() {

			@Override
			protected void execute(IProgressMonitor monitor)
					throws CoreException, InvocationTargetException,
					InterruptedException {
				String projectName = intfProject.getProjectName();
				monitor.beginTask("Updating project->" + projectName,
						ProgressUtil.PROGRESS_STEP * 10);
				try {
					// change local metadata (wsdl, properties), sync AR
					// version.
					ActionUtil
							.updateInterfaceProjectVersion(
									intfProject,
									oldVersionStr, newVersionStr, false, monitor);

				} catch (Exception e) {
					logger.error(e);
					throw new SOAResourceModifyFailedException(
							"Failed to modify project dependencies for project->"
									+ projectName, e);
				} finally {
					monitor.done();
				}
			}
		};

		try {
			new ProgressMonitorDialog(parentShell).run(false, true, operation);
			return true;
		} catch (Exception e) {
			logger.error(e);
			UIUtil.showErrorDialog(e);
			return false;
		}
	}
	
}
