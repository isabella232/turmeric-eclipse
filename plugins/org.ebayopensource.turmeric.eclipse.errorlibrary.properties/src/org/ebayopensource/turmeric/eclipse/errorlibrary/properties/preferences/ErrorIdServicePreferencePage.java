/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.errorlibrary.properties.preferences;

import org.ebayopensource.turmeric.eclipse.errorlibrary.properties.Activator;
import org.ebayopensource.turmeric.eclipse.errorlibrary.properties.resources.SOAMessages;
import org.eclipse.jface.preference.DirectoryFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.RadioGroupFieldEditor;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

public class ErrorIdServicePreferencePage extends FieldEditorPreferencePage
		implements IWorkbenchPreferencePage {

	private StringFieldEditor asEndpointField;
	private RadioGroupFieldEditor hostSelectionField;
	private DirectoryFieldEditor localFileField;

	String[][] radioGroup = new String[][] {
			new String[] { SOAMessages.USE_LOCAL_CONF,
					ErrorIdServicePreferenceConstants.USELOCALHOST },
			new String[] { SOAMessages.USE_REMOTE_HOST,
					ErrorIdServicePreferenceConstants.USEREMOTEHOST } };

	public ErrorIdServicePreferencePage() {
		super(GRID);
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
		setDescription(SOAMessages.PREFERENCE_DESC);
	}

	/**
	 * Creates the field editors. Field editors are abstractions of the common
	 * GUI blocks needed to manipulate various types of preferences. Each field
	 * editor knows how to save and restore itself.
	 */
	public void createFieldEditors() {
		hostSelectionField = new RadioGroupFieldEditor(
				ErrorIdServicePreferenceConstants.HOSTSELECTION,
				SOAMessages.SELECT_HOST, 1, radioGroup, getFieldEditorParent(), true);
		Activator.getDefault().getPreferenceStore().addPropertyChangeListener(new IPropertyChangeListener() {
			
			public void propertyChange(PropertyChangeEvent event) {
				System.out.println(event.getProperty());
				if (ErrorIdServicePreferenceConstants.HOSTSELECTION.equals(event.getProperty())){
					//boolean enableRemoteHost = ErrorIdServicePreferenceConstants.USEREMOTEHOST.equals(Activator.getDefault().getPreferenceStore()
					//		.getString(ErrorIdServicePreferenceConstants.HOSTSELECTION));
					//FIXME set enabler dynamically according to the selected value of raido group
					//setFieldEnabled(enableRemoteHost, getFieldEditorParent());
				}
			}
		});
		addField(hostSelectionField);
		
		localFileField = new DirectoryFieldEditor(
				ErrorIdServicePreferenceConstants.LOCALFILEPATH,
				SOAMessages.LOCAL_CONF_FOLDER, getFieldEditorParent());
		addField(localFileField);

		asEndpointField = new StringFieldEditor(
				ErrorIdServicePreferenceConstants.REMOTEENDPOINTURL,
				SOAMessages.REMOTE_ENDPOINT, getFieldEditorParent());
		addField(asEndpointField);

		
	}
	
//	private void setFieldEnabled(boolean newValue, Composite parent) {
//		asEndpointField.setEnabled(newValue, parent);
//	}

	public void init(IWorkbench workbench) {
	}

}