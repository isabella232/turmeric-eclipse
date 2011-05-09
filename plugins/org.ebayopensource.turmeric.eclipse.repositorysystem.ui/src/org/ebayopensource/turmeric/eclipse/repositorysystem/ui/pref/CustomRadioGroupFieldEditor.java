/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.repositorysystem.ui.pref;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.ebayopensource.turmeric.eclipse.repositorysystem.RepositorySystemActivator;
import org.ebayopensource.turmeric.eclipse.repositorysystem.preferences.core.PreferenceConstants;
import org.ebayopensource.turmeric.eclipse.repositorysystem.ui.RepositorySystemUIActivator;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.preferences.IScopeContext;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.jface.preference.FieldEditor;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.preferences.ScopedPreferenceStore;

/**
 * A field editor for an enumeration type preference. The choices are presented
 * as a list of radio buttons.
 * 
 * @author yayu
 */
public class CustomRadioGroupFieldEditor extends FieldEditor {
	
	/** The Constant SUB_VALUE. */
	public static final String SUB_VALUE = "field_editor_sub_value";//$NON-NLS-1$

	private String[][] labelsAndValues;

	/**
	 * The first level key is repo ID, the second level key is org ID/Name, then
	 * the value is org display name
	 */
	private Map<String, Map<String, String>> organizations;

	private int numColumns;

	private int indent = HORIZONTAL_GAP;

	private String value;

	private String subPreferenceName;

	private String subValue;

	private Composite chooseArea;

	private Button[] radioButtons;

	private boolean useGroup;

	private Map<FieldEditor, Composite> subFieldEditors = new ConcurrentHashMap<FieldEditor, Composite>();

	private Map<String, List<Button>> subButtons = new ConcurrentHashMap<String, List<Button>>();

	/**
	 * Instantiates a new custom radio group field editor.
	 *
	 * @param name the name
	 * @param labelText the label text
	 * @param numColumns the num columns
	 * @param labelAndValues the label and values
	 * @param organizations the organizations
	 * @param parent the parent
	 */
	public CustomRadioGroupFieldEditor(String name, String labelText,
			int numColumns, String[][] labelAndValues,
			Map<String, Map<String, String>> organizations, Composite parent) {
		this(name, labelText, numColumns, labelAndValues, organizations,
				parent, false);
	}

	/**
	 * Instantiates a new custom radio group field editor.
	 *
	 * @param name the name
	 * @param labelText the label text
	 * @param numColumns the num columns
	 * @param labelAndValues the label and values
	 * @param organizations the organizations
	 * @param parent the parent
	 * @param useGroup the use group
	 */
	public CustomRadioGroupFieldEditor(String name, String labelText,
			int numColumns, String[][] labelAndValues,
			Map<String, Map<String, String>> organizations, Composite parent,
			boolean useGroup) {

		setPreferenceStore(RepositorySystemUIActivator.getDefault().getPreferenceStore());
		init(name, labelText);
		Assert.isTrue(validateKeyValuePair(labelAndValues));
		Assert.isNotNull(organizations);
		this.subPreferenceName = PreferenceConstants.PREF_ORGANIZATION;
		this.labelsAndValues = labelAndValues;
		this.organizations = organizations;
		this.numColumns = numColumns;
		this.useGroup = useGroup;
		createControl(parent);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void doLoad() {
		String value = getPreferenceStore().getString(getPreferenceName());
		updateValue(value);
		setSubButtonsEnabled(value);
		updateSubValue(value,
				getPreferenceStore().getString(getSubPreferenceName()));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void doLoadDefault() {
		String value = getPreferenceStore().getDefaultString(
				getPreferenceName());
		updateValue(value);
		updateSubValue(value,
				getPreferenceStore().getDefaultString(getSubPreferenceName()));
	}

	/**
	 * Gets the sub preference name.
	 *
	 * @return the sub preference name
	 */
	public String getSubPreferenceName() {
		return subPreferenceName;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void doStore() {
		if (value == null) {
			getPreferenceStore().setToDefault(getPreferenceName());
			return;
		}

		getPreferenceStore().setValue(getPreferenceName(), value);

		if (subValue == null) {
			getPreferenceStore().setToDefault(getSubPreferenceName());
			return;
		}

		getPreferenceStore().setValue(getSubPreferenceName(), subValue);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getNumberOfControls() {
		return 1;
	}

	private void createChooseArea(Composite parent) {
		Font font = parent.getFont();

		if (useGroup) {
			Group group = new Group(parent, SWT.NONE);
			group.setFont(font);
			String text = getLabelText();
			if (text != null) {
				group.setText(text);
			}
			chooseArea = group;
			GridLayout layout = new GridLayout();
			layout.horizontalSpacing = HORIZONTAL_GAP;
			layout.numColumns = numColumns;
			chooseArea.setLayout(layout);
		} else {
			chooseArea = new Composite(parent, SWT.NONE);
			GridLayout layout = new GridLayout();
			layout.marginWidth = 0;
			layout.marginHeight = 0;
			layout.horizontalSpacing = HORIZONTAL_GAP;
			layout.numColumns = numColumns;
			chooseArea.setLayout(layout);
			chooseArea.setFont(font);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	protected void adjustForNumColumns(int numColumns) {
		Control control = getLabelControl();
		if (control != null) {
			((GridData) control.getLayoutData()).horizontalSpan = numColumns;
		}
		((GridData) chooseArea.getLayoutData()).horizontalSpan = numColumns;
	}

	private boolean validateKeyValuePair(String[][] keyValuePair) {
		if (keyValuePair == null) {
			return false;
		}
		for (int i = 0; i < keyValuePair.length; i++) {
			String[] array = keyValuePair[i];
			if (array == null || array.length != 2) {
				return false;
			}
		}
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void doFillIntoGrid(Composite parent, int numColumns) {
		if (useGroup == true) {
			Control control = getRadioBoxControl(parent);
			GridData data = new GridData(GridData.FILL_HORIZONTAL);
			control.setLayoutData(data);
		} else {
			Control control = getLabelControl(parent);
			GridData data = new GridData();
			data.horizontalSpan = numColumns;
			control.setLayoutData(data);
			control = getRadioBoxControl(parent);
			data = new GridData();
			data.horizontalSpan = numColumns;
			data.horizontalIndent = indent;
			control.setLayoutData(data);
		}

	}

	/**
	 * Gets the radio box control.
	 *
	 * @param parent the parent
	 * @return the radio box control
	 */
	public Composite getRadioBoxControl(Composite parent) {
		if (chooseArea != null) {
			checkParent(chooseArea, parent);
			return chooseArea;
		}
		createChooseArea(parent);
		Font font = parent.getFont();
		radioButtons = new Button[labelsAndValues.length];
		for (int i = 0; i < labelsAndValues.length; i++) {
			final Button radio = new Button(chooseArea, SWT.RADIO | SWT.LEFT);
			radioButtons[i] = radio;
			String[] labelAndValue = labelsAndValues[i];
			radio.setText(labelAndValue[0]);
			radio.setData(labelAndValue[1]);
			radio.setFont(font);
			radio.setEnabled(organizations.size() > 1);
			radio.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent event) {
					CustomRadioGroupFieldEditor.this.clearErrorMessage();
					String oldValue = value;
					value = (String) event.widget.getData();
					setPresentsDefaultValue(false);
					setSubButtonsEnabled(value);
					fireValueChanged(VALUE, oldValue, value);
				}
			});

			final Composite composite = new Composite(chooseArea, SWT.NONE);
			final GridLayout layout = new GridLayout(1, true);
			composite.setLayout(layout);
			List<Button> buttons = new ArrayList<Button>();
			subButtons.put(labelAndValue[1], buttons);
			for (String orgId : organizations.get(labelAndValue[1]).keySet()) {
				final Button radio1 = new Button(composite, SWT.RADIO
						| SWT.LEFT);
				GridData data = new GridData();
				data.horizontalIndent = 30;
				radio1.setLayoutData(data);
				radio1.setText(organizations.get(labelAndValue[1]).get(orgId));
				radio1.setData(orgId);
				radio1.setFont(font);
				radio1.setEnabled(organizations.get(labelAndValue[1]).size() > 1);
				radio1.addSelectionListener(new SelectionAdapter() {
					@Override
					public void widgetSelected(SelectionEvent event) {
						CustomRadioGroupFieldEditor.this.clearErrorMessage();
						String oldValue = subValue;
						subValue = (String) event.widget.getData();
						setPresentsDefaultValue(false);
						fireValueChanged(SUB_VALUE, oldValue, subValue);
					}
				});
				buttons.add(radio1);
			}

			final Composite panel = new Composite(chooseArea, SWT.NONE);
			GridData data = new GridData(GridData.FILL_HORIZONTAL);
			data.horizontalSpan = numColumns;
			panel.setLayoutData(data);
			panel.setLayout(new GridLayout(2, false));
			final FieldEditor fieldEditor = createChoiceSubFieldEditor(panel,
					radio);
			if (fieldEditor != null) {
				Label label = fieldEditor.getLabelControl(panel);
				data = new GridData();
				data.horizontalIndent = 15;
				label.setLayoutData(data);
				subFieldEditors.put(fieldEditor, panel);
				fieldEditor.setEnabled(
						radio.getText().equals(
								getPreferenceStore().getString(
										getPreferenceName())), panel);
				radio.addSelectionListener(new SelectionAdapter() {

					@Override
					public void widgetDefaultSelected(SelectionEvent e) {
						widgetSelected(e);
					}

					@Override
					public void widgetSelected(SelectionEvent e) {
						setSubFieldEditorEnabled(false);
						fieldEditor.setEnabled(radio.getSelection(), panel);
						fieldEditor.setFocus();
					}

				});
			}
		}
		chooseArea.addDisposeListener(new DisposeListener() {
			public void widgetDisposed(DisposeEvent event) {
				chooseArea = null;
				radioButtons = null;
			}
		});

		return chooseArea;
	}

	private void setSubButtonsEnabled(String id) {
		for (String key : subButtons.keySet()) {
			for (Button btn : subButtons.get(key)) {
				btn.setEnabled(id.equals(key));
				btn.setSelection(false);
			}
		}
		updateSubValue(id, "");
	}

	/**
	 * Creates the choice sub field editor.
	 *
	 * @param parent the parent
	 * @param radioBtn the radio btn
	 * @return the field editor
	 */
	protected FieldEditor createChoiceSubFieldEditor(final Composite parent,
			final Button radioBtn) {

		return null;
	}

	private void setSubFieldEditorEnabled(boolean enabled) {
		for (FieldEditor fieldEditor : this.subFieldEditors.keySet()) {
			fieldEditor.setEnabled(enabled,
					this.subFieldEditors.get(fieldEditor));
		}
	}

	/**
	 * Sets the indent used for the first column of the radion button matrix.
	 * 
	 * @param indent
	 *            the indent (in pixels)
	 */
	public void setIndent(int indent) {
		if (indent < 0) {
			this.indent = 0;
		} else {
			this.indent = indent;
		}
	}

	private void updateValue(String newValue) {
		this.value = newValue;
		if (radioButtons == null) {
			return;
		}

		if (this.value != null) {
			boolean found = false;
			for (int i = 0; i < radioButtons.length; i++) {
				Button radio = radioButtons[i];
				boolean selection = false;
				if (((String) radio.getData()).equals(this.value)) {
					selection = true;
					found = true;
				}
				radio.setSelection(selection);
			}
			if (found) {
				return;
			}
		}

		// if valie is not found, the first radio button will be selected.
		if (radioButtons.length > 0) {
			radioButtons[0].setSelection(true);
			this.value = (String) radioButtons[0].getData();
		}
		return;
	}

	private void updateSubValue(String firstValue, String selectedValue) {
		this.subValue = selectedValue;
		if (subButtons == null) {
			return;
		}

		if (this.subValue != null) {
			boolean found = false;
			for (Button radio : subButtons.get(this.value)) {
				boolean selection = false;
				if (((String) radio.getData()).equals(this.subValue)) {
					selection = true;
					found = true;
				}
				radio.setSelection(selection);
				radio.setEnabled(this.organizations.get(this.value).size() > 1);
			}
			if (found) {
				return;
			}
		}

		// if value is not found, the first radio button will be selected.
		if (subButtons.get(firstValue).isEmpty() == false) {
			subButtons.get(firstValue).get(0).setSelection(true);
			this.subValue = (String) subButtons.get(firstValue).get(0)
					.getData();
		}
		return;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setEnabled(boolean enabled, Composite parent) {
		if (useGroup == false) {
			super.setEnabled(enabled, parent);
		}
		for (int i = 0; i < radioButtons.length; i++) {
			radioButtons[i].setEnabled(enabled);
		}

	}
}
