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
package org.ebayopensource.turmeric.eclipse.ui.components;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;
import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

/**
 * <p>
 * This cell editor combines both comboBox and Dialog. It would open a Dialog if
 * user select the last item: "...".
 * </p>
 * 
 * <p>
 * This class would automatically append an item "..." in the end, so no need to
 * add it yourself.
 * </p>
 * 
 * @author yayu
 * @see org.eclipse.jface.viewers.ComboBoxCellEditor
 * @since 1.0.0
 */
public abstract class SOAComboDialogCellEditor extends CellEditor {

    public static final String DIALOG_BUTTON_LABEL = "Browse Types...";
    /**
     * The list of items to present in the combo box.
     */
    private String[] items;

    /**
     * The value;
     */
    Object selectedValue;

    /**
     * The custom combo box control.
     */
    CCombo comboBox;

    /**
     * Default ComboBoxCellEditor style
     */
    private static final int defaultStyle = SWT.READ_ONLY;

    public SOAComboDialogCellEditor() {
        super();
        setStyle(defaultStyle);
    }

    /**
     * @param parent
     * @param items
     */
    public SOAComboDialogCellEditor(Composite parent, String[] items) {
        this(parent, items, defaultStyle);
    }

    /**
     * @param parent
     * @param items
     * @param style
     */
    public SOAComboDialogCellEditor(Composite parent, String[] items, int style) {
        super(parent, style);
        setItems(items);
    }

    protected List<String> getItemList() {
        return Arrays.asList(getItems());
    }

    /**
     * Returns the list of choices for the combo box
     * 
     * @return the list of choices for the combo box
     */
    public String[] getItems() {
        return items;
    }

    /**
     * Sets the list of choices for the combo box
     * 
     * @param items
     *            the list of choices for the combo box
     */
    public void setItems(String[] items) {
        Assert.isNotNull(items);
        this.items = (String[]) ArrayUtils.add(items, DIALOG_BUTTON_LABEL);
        fillcomboBox();
    }

    /*
     * (non-Javadoc) Method declared on CellEditor.
     */
    protected Control createControl(Composite parent) {

        comboBox = new CCombo(parent, getStyle());
        comboBox.setFont(parent.getFont());
        fillcomboBox();

        comboBox.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                keyReleaseOccured(e);
            }
        });

        comboBox.addSelectionListener(new SelectionAdapter() {
            /**
             * in order to avoid twice pop up, using "lose focus" to trigger
             * "postEditing". if users selected "browse type..." item, then
             * force focus on parent (must be table because this is a table cell
             * editor). Then combo will lose focus and the focus lost event will
             * be triggered.
             */
            public void widgetDefaultSelected(SelectionEvent event) {
                Object newValue = comboBox.getText();
                Composite parent = comboBox.getParent();
                if (DIALOG_BUTTON_LABEL.equals(newValue)
                        && ((parent == null) == false)
                        && (parent.isDisposed() == false)) {
                    parent.forceFocus();
                }
            }

            public void widgetSelected(SelectionEvent event) {
                Object newValue = comboBox.getText();
                Composite parent = comboBox.getParent();
                if (DIALOG_BUTTON_LABEL.equals(newValue)
                        && ((parent == null) == false)
                        && (parent.isDisposed() == false)) {
                    parent.forceFocus();
                }
            }
        });

        comboBox.addTraverseListener(new TraverseListener() {
            public void keyTraversed(TraverseEvent e) {
                if (e.detail == SWT.TRAVERSE_ESCAPE
                        || e.detail == SWT.TRAVERSE_RETURN) {
                    e.doit = false;
                }
            }
        });

        comboBox.addFocusListener(new FocusAdapter() {
            public void focusLost(FocusEvent e) {
                SOAComboDialogCellEditor.this.focusLost();
            }
        });
        return comboBox;
    }

    /**
     * return selected value
     */
    protected Object doGetValue() {
        return selectedValue;
    }

    protected void doSetFocus() {
        comboBox.setFocus();
    }

    /**
     * if combobox contains no item, set its default width to 60, otherwise, set
     * its mini width to ten characters.
     */
    public LayoutData getLayoutData() {
        LayoutData data = super.getLayoutData();
        data.minimumWidth = 60;
        if ((comboBox != null) && (comboBox.isDisposed() == false)) {
            GC gc = new GC(comboBox);
            data.minimumWidth = (gc.getFontMetrics().getAverageCharWidth() * 10) + 10;
            gc.dispose();
        }
        return data;
    }

    /**
     * 
     */
    protected void doSetValue(Object value) {
        Assert.isTrue((comboBox != null) && (value != null));
        int selection = ArrayUtils.indexOf(getItems(), value);
        if (selection < 0) {
            // data from the dialog and not present in the combo
            comboBox.setText(value.toString());
        } else {
            // data from drop down list
            comboBox.select(selection);
        }
    }

    /**
     * Updates the list of choices for the combo box for the current control.
     */
    private void fillcomboBox() {
        if (comboBox != null && items != null) {
            comboBox.removeAll();
            for (int i = 0; i < items.length; i++) {
                comboBox.add(items[i], i);
            }

            setValueValid(true);
            if (items.length > 0) {
                selectedValue = items[0];
                comboBox.setVisibleItemCount(Math.min(items.length, 10));
            }
        }
    }

    /**
     * Applies the currently selected value and deactivates the cell editor
     */
    protected void postEditing() {

        Object newValue = comboBox.getText();
        if (DIALOG_BUTTON_LABEL.equals(newValue)) {
            // the last item
            newValue = openDialogBox(comboBox);
        }
        if (newValue == null) {
            comboBox.setText(getValueText(selectedValue));
            return;
        } else {
            comboBox.setText(newValue.toString());
        }

        markDirty();
        boolean isValid = isCorrect(newValue);
        setValueValid(isValid);

        if (isValid) {
            selectedValue = newValue;
        } else {
            setErrorMessage(MessageFormat.format(getErrorMessage(),
                    new Object[] { selectedValue }));
        }

        fireApplyEditorValue();
        deactivate();
    }

    /**
     * The child class can override this method in the case that the toString()
     * method of the value's class is not implemented properly.
     * 
     * @param value
     * @return
     */
    protected String getValueText(Object value) {
        return String.valueOf(value);
    }

    protected void focusLost() {
        if (isActivated()) {
            postEditing();
        }
    }

    protected void keyReleaseOccured(KeyEvent keyEvent) {
        if (keyEvent.character == '\u001b') { // Escape character
            fireCancelEditor();
        } else if (keyEvent.character == '\t') { // tab key
            postEditing();
        }
    }

    /**
     * pop up a dialog when users selected Browse Types...
     * 
     * @param cellEditorWindow
     * @return
     */
    protected abstract Object openDialogBox(Control cellEditorWindow);
}
