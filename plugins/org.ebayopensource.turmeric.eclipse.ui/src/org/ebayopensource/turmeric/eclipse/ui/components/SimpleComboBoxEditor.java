/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.ui.components;

import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.preference.FieldEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

/**
 * The Class SimpleComboBoxEditor.
 *
 * @author smathew
 */
public class SimpleComboBoxEditor extends FieldEditor {

    /**
     * component to display
     */
    private Combo itemList;

    /**
     * the value of selected item
     */
    private String selectedValue;

    /**
     * display name and the value for the display name.
     */
    private String[][] nameValuePairs;

    /**
     * Instantiates a new simple combo box editor.
     *
     * @param name the name
     * @param labelText the label text
     * @param entryNamesAndValues the entry names and values
     * @param parent the parent
     */
    public SimpleComboBoxEditor(String name, String labelText,
            String[][] entryNamesAndValues, Composite parent) {
        init(name, labelText);
        Assert.isTrue(isNameValuePairCorrect(entryNamesAndValues));
        nameValuePairs = entryNamesAndValues;
        createControl(parent);
    }

    /**
     * check if the given array is a correct name-value array.
     * 
     * @param table
     * @return true if the array format is correct.
     */
    private boolean isNameValuePairCorrect(String[][] table) {
        if (table == null) {
            return false;
        }
        for (int i = 0; i < table.length; i++) {
            String[] array = table[i];
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
	protected void adjustForNumColumns(int numColumns) {
        if (numColumns > 1) {
            Control control = getLabelControl();
            int left = numColumns;
            if (control != null) {
                ((GridData) control.getLayoutData()).horizontalSpan = 1;
                left = left - 1;
            }
            ((GridData) itemList.getLayoutData()).horizontalSpan = left;
        } else {
            Control control = getLabelControl();
            if (control != null) {
                ((GridData) control.getLayoutData()).horizontalSpan = 1;
            }
            ((GridData) itemList.getLayoutData()).horizontalSpan = 1;
        }
    }

	/**
	 * {@inheritDoc}
	 */
    @Override
	protected void doFillIntoGrid(Composite parent, int numColumns) {
        int comboC = 1;
        if (numColumns > 1) {
            comboC = numColumns - 1;
        }
        Control control = getLabelControl(parent);
        GridData gd = new GridData();
        gd.horizontalSpan = 1;
        control.setLayoutData(gd);
        control = getComboBoxControl(parent);
        gd = new GridData();
        gd.horizontalSpan = comboC;
        gd.horizontalAlignment = GridData.FILL;
        control.setLayoutData(gd);
        control.setFont(parent.getFont());
    }

	/**
	 * {@inheritDoc}
	 */
    @Override
	protected void doLoad() {
        selectItemOfValue(getPreferenceStore().getString(getPreferenceName()));
    }

	/**
	 * {@inheritDoc}
	 */
    @Override
	protected void doLoadDefault() {
        selectItemOfValue(getPreferenceStore().getDefaultString(
                getPreferenceName()));
    }

	/**
	 * {@inheritDoc}
	 */
    @Override
	protected void doStore() {
        if (selectedValue == null) {
            getPreferenceStore().setToDefault(getPreferenceName());
            return;
        }
        getPreferenceStore().setValue(getPreferenceName(), selectedValue);
    }

	/**
	 * {@inheritDoc}
	 */
    @Override
	public int getNumberOfControls() {
        return 2;
    }

    /**
     * Gets the combo box control.
     *
     * @param parent the parent
     * @return the combo box control
     */
    public Combo getComboBoxControl(Composite parent) {
        if (itemList == null) {
            itemList = new Combo(parent, SWT.READ_ONLY);
            itemList.setFont(parent.getFont());
            for (int i = 0; i < nameValuePairs.length; i++) {
                itemList.add(nameValuePairs[i][0], i);
            }

            itemList.addSelectionListener(new SelectionAdapter() {
                @Override
				public void widgetSelected(SelectionEvent evt) {
                    String oldValue = selectedValue;
                    String name = itemList.getText();
                    selectedValue = getSelectedItemValue(name);
                    setPresentsDefaultValue(false);
                    fireValueChanged(VALUE, oldValue, selectedValue);
                }
            });
        }
        return itemList;
    }
    
	public void updateProtocolList(String selection,
			String[][] newNameValuePairs) {
		if (itemList == null) {
			return;
		}
		itemList.removeAll();
		boolean has = false;
		for (int i = 0; i < newNameValuePairs.length; i++) {
			itemList.add(newNameValuePairs[i][0], i);
			if (newNameValuePairs[i][0].equalsIgnoreCase(selection) == true) {
				has = true;
			}
		}
		nameValuePairs = newNameValuePairs;
		if (has == true) {
			itemList.setText(selection);
		} else {
			itemList.setText(newNameValuePairs[0][0]);
		}
	}

    private String getSelectedItemValue(String selected) {
        for (int i = 0; i < nameValuePairs.length; i++) {
            String[] entry = nameValuePairs[i];
            if (selected.equals(entry[0])) {
                return entry[1];
            }
        }
        return nameValuePairs[0][0];
    }

    /**
     * select the item whose value is the given parameter
     * 
     * @param value
     */
    private void selectItemOfValue(String value) {
        selectedValue = value;
        for (int i = 0; i < nameValuePairs.length; i++) {
            if (value.equals(nameValuePairs[i][1])) {
                itemList.setText(nameValuePairs[i][0]);
                return;
            }
        }
        if (nameValuePairs.length > 0) {
            selectedValue = nameValuePairs[0][1];
            itemList.setText(nameValuePairs[0][0]);
        }
    }
}
