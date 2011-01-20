/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.ui.components;

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.ebayopensource.turmeric.eclipse.resources.model.AssetInfo;
import org.ebayopensource.turmeric.eclipse.utils.collections.SetUtil;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.CheckboxCellEditor;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;


/**
 * @author smathew
 *
 */
public class DependenciesViewer extends CheckboxTableViewer {
	private String serviceName = "";
	private final Map<AssetInfo, Boolean> selected = new TreeMap<AssetInfo, Boolean>();

	public DependenciesViewer(final Composite parent) {
		this(parent, true);
	}

	public DependenciesViewer(final Composite parent, final boolean multiSelect) {
		this(new Table(parent, (multiSelect ? SWT.MULTI : SWT.SINGLE) | SWT.H_SCROLL
				| SWT.V_SCROLL | SWT.BORDER | SWT.CHECK | SWT.FULL_SELECTION));
	}

	/**
	 * create a table viewer with MultiSelect support
	 * @param table
	 */
	public DependenciesViewer(Table table) {
		super(table);
		getTable().setLinesVisible(true);
		getTable().setHeaderVisible(true);
		final TableColumn checkBoxColumn = new TableColumn(getTable(),
				SWT.CENTER, 0);
		checkBoxColumn.setText("!");
		checkBoxColumn.setWidth(20);
		final TableColumn serviceColumn = new TableColumn(getTable(), SWT.LEFT,
				1);
		serviceColumn.setText("Service");
		serviceColumn.setWidth(270);
		final TableColumn versionColumn = new TableColumn(getTable(), SWT.LEFT,
				2);
		versionColumn.setText("Version");
		versionColumn.setWidth(60);
		final TableColumn layerColumn = new TableColumn(getTable(), SWT.LEFT, 3);
		layerColumn.setText("Service Layer");
		layerColumn.setWidth(120);
		final CellEditor[] editors = new CellEditor[] {
				new CheckboxCellEditor(getTable()),
				new TextCellEditor(getTable(), SWT.READ_ONLY),
				new TextCellEditor(getTable(), SWT.READ_ONLY),
				new TextCellEditor(getTable(), SWT.READ_ONLY) };
		setCellEditors(editors);
		setColumnProperties(new String[] { "selected", "serviceName",
				"serviceVersion", "serviceLayer" });
	}

	public String getServiceName() {
		return serviceName;
	}

	public DependenciesViewer setServiceName(final String serviceName) {
		this.serviceName = serviceName;
		return this;
	}

	@Override
	public void handleSelect(SelectionEvent event) {
		super.handleSelect(event);
		if (event.detail == SWT.CHECK) {
			final TableItem tableItem = (TableItem) event.item;
            final AssetInfo info = (AssetInfo)tableItem.getData();
            boolean isChecked = tableItem.getChecked();
            if (isMultiSelect() == false) {
    			selected.clear();
    			if (isChecked == true) {
    				this.setCheckedElements(new Object[]{info});
    			}
    		}
            selected.put(info, isChecked);
        } 
	}

	public Set<AssetInfo> getSelected() {
		final Set<AssetInfo> selection = SetUtil.linkedSet();
		for (final AssetInfo service : selected.keySet()) {
			if (selected.get(service) == true)
				selection.add(service);
		}
		return selection;
	}

	public boolean isSelected(final AssetInfo serviceInfo) {
		if (!selected.containsKey(serviceInfo))
			return false;
		return selected.get(serviceInfo);
	}

	private boolean isMultiSelect() {
		return (getControl().getStyle() & SWT.MULTI) != 0;
	}
}
