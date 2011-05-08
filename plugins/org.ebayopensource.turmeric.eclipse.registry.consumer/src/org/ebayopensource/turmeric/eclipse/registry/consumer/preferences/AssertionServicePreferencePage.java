/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.registry.consumer.preferences;

import org.ebayopensource.turmeric.eclipse.registry.consumer.Activator;
import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

/**
 * This class represents a preference page that
 * is contributed to the Preferences dialog. By 
 * subclassing <samp>FieldEditorPreferencePage</samp>, we
 * can use the field support built into JFace that allows
 * us to create a page that is small and knows how to 
 * save, restore and apply itself.
 * <p>
 * This page is used to modify preferences only. They
 * are stored in the preference store that belongs to
 * the main plug-in class. That way, preferences can
 * be accessed directly via the preference store.
 * 
 * @author yayu
 * @since 1.0.0
 */

public class AssertionServicePreferencePage
	extends FieldEditorPreferencePage
	implements IWorkbenchPreferencePage {
	private BooleanFieldEditor enableASField;
	private BooleanFieldEditor overwriteASField;
	private StringFieldEditor asEndpointField;

	/**
	 * Instantiates a new assertion service preference page.
	 */
	public AssertionServicePreferencePage() {
		super(GRID);
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
		setDescription("a preference page for Assertion Service");
	}
	
	/**
	 * Creates the field editors. Field editors are abstractions of
	 * the common GUI blocks needed to manipulate various types
	 * of preferences. Each field editor knows how to save and
	 * restore itself.
	 */
	public void createFieldEditors() {
		final Composite fieldParent = getFieldEditorParent();
		enableASField = new BooleanFieldEditor(
				AssertionServicePreferenceConstants.ENABLE_ASSERTION_SERVICE,
				"&Enable Assertion Service Integration",
				fieldParent);
		addField(enableASField);
		overwriteASField = new BooleanFieldEditor(
				AssertionServicePreferenceConstants.OVERWRITE_AS_ENDPOINT,
				"&Overwrite Assertion Service Properties",
				fieldParent) {

					@Override
					protected void valueChanged(boolean oldValue,
							boolean newValue) {
						setFieldEnabled(newValue, fieldParent);
						if (newValue == false) {
							//user want to use the default value
							asEndpointField.loadDefault();
						}
						super.valueChanged(oldValue, newValue);
					}
			
		};
		addField(overwriteASField);
		boolean overwrite = Activator.getDefault().getPreferenceStore().getBoolean(
				AssertionServicePreferenceConstants.OVERWRITE_AS_ENDPOINT);
		
		asEndpointField = new StringFieldEditor(AssertionServicePreferenceConstants.URL_AS_ENDPOINT, 
				"&Endpoint:", 
				fieldParent);
		addField(asEndpointField);
		
		setFieldEnabled(overwrite, fieldParent);
	}
	
	private void setFieldEnabled(boolean enabled, Composite fieldParent) {
		asEndpointField.setEnabled(enabled, fieldParent);
	}

	/**
	 * {@inheritDoc}
	 */
	public void init(IWorkbench workbench) {
	}
	
}