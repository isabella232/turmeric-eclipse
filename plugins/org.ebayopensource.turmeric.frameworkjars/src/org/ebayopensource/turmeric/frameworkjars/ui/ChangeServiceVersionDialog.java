/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.frameworkjars.ui;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.jface.fieldassist.FieldDecoration;
import org.eclipse.jface.fieldassist.FieldDecorationRegistry;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/**
 * The Class ChangeServiceVersionDialog.
 *
 * @author yayu
 */
public class ChangeServiceVersionDialog extends TitleAreaDialog {

	/**
	 * Instantiates a new change service version dialog.
	 *
	 * @param parentShell the parent shell
	 */
	public ChangeServiceVersionDialog(Shell parentShell) {
		super(parentShell);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.dialogs.TitleAreaDialog#createDialogArea(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		
		Composite control = (Composite)super.createDialogArea(parent);
		setTitle("Change Service Version");
		setMessage("change the service version and submit to the Asset Repository.");
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
        Button btn = new Button(group, SWT.RADIO);
        btn.setText("Change Maintenance Version");
        btn.setToolTipText("Used for changes that do not affect the interface at all, such as logic bug fixes. \nThis will change your existing service in place to update \nits maintenance version. The same asset record in AR will be kept and modified to record the version change.");
		ControlDecoration controlDecoration = new ControlDecoration(btn, 
				SWT.LEFT | SWT.TOP);
		controlDecoration.setShowOnlyOnFocus(false);
		controlDecoration.setDescriptionText("Used for changes that do not affect the interface at all, such as logic bug fixes. \nThis will change your existing service in place to update \nits maintenance version. The same asset record in AR will be kept and modified to record the version change.");
		FieldDecoration fieldDecoration = FieldDecorationRegistry
		.getDefault().getFieldDecoration(
		FieldDecorationRegistry.DEC_INFORMATION);
		controlDecoration.setImage(fieldDecoration.getImage());
        //new Label(group, SWT.NONE | SWT.WRAP).setText("Used for backward incompatible changes to the interface. \nThis will take you to the new service wizard for creating a new service with major version bumped up.");
        //new Label(group, SWT.NONE);
        btn = new Button(group, SWT.RADIO);
        btn.setText("Change Minor Version");
        controlDecoration = new ControlDecoration(btn, 
				SWT.LEFT | SWT.TOP);
		controlDecoration.setShowOnlyOnFocus(false);
		controlDecoration.setDescriptionText("Used for backward compatible changes to the interface. \nThis will change your existing service in place to update its minor version. \nA new asset record will be created in AR to track the new minor version.");
		controlDecoration.setImage(fieldDecoration.getImage());
        //new Label(group, SWT.NONE | SWT.WRAP).setText("Used for backward compatible changes to the interface. \nThis will change your existing service in place to update its minor version (for). A new asset record \nwill be created in AR to track the new minor version");
        //new Label(group, SWT.NONE);
        btn = new Button(group, SWT.RADIO);
        btn.setText("Change Major Version");
        //new Label(group, SWT.NONE).setText("Change service maintenance version");
        controlDecoration = new ControlDecoration(btn, 
				SWT.LEFT | SWT.TOP);
		controlDecoration.setShowOnlyOnFocus(false);
		controlDecoration.setDescriptionText("Used for backward incompatible service changes. Treated the same as \nsubmitting a new service version - creates new projects, jars, namespace, etc. \nConsult architecture to consider all the compatibility implications.");
		controlDecoration.setImage(fieldDecoration.getImage());
        
        
        Label label = new Label(parentComposite, SWT.LEFT);
        label.setText("Existing Service Version");
        Text text = new Text(parentComposite, SWT.BORDER | SWT.READ_ONLY);
        text.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        text.setText("1.1.0");
        
        label = new Label(parentComposite, SWT.LEFT);
        label.setText("New Service Version");
        text = new Text(parentComposite, SWT.BORDER);
        text.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        text.setText("1.2.0");
		Dialog.applyDialogFont(control);
		return control;
	}
}
