/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.ui.views.registry;

//import org.ebayopensource.turmeric.eclipse.typelibrary.actions.AddImportAction;
import org.ebayopensource.turmeric.eclipse.core.compare.LibraryTypeComparator;
import org.ebayopensource.turmeric.eclipse.core.resources.constants.SOATypeLibraryConstants;
import org.ebayopensource.turmeric.eclipse.utils.ui.UIUtil;
import org.ebayopensource.turmeric.eclipse.utils.xml.XMLUtil;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ColumnLayoutData;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.xsd.XSDSchema;
import org.eclipse.xsd.XSDTypeDefinition;

import org.ebayopensource.turmeric.common.config.LibraryType;

/**
 * This is the right side Type Viewer. Displays all types corresponding to the
 * left side library selection. For root selection it displays all types. Main
 * features exposed through this view are importing or inlining type based on
 * the selected editor, refresh the registry and creating new artifacts. An
 * additional optional feature is the reading of the XSD type. Since XSDs are
 * inside jars it will be difficult for users to view the XSD with out this
 * feature. This viewer also honors the search text as well.
 * 
 * @author smathew
 */
public class TypeViewer extends TableViewer {

	private final String columnHeaders[] = {
			SOATypeLibraryConstants.REGISTRY_VIEW_COLUMN_NAME,
			SOATypeLibraryConstants.REGISTRY_VIEW_COLUMN_VERSION };
	private ColumnLayoutData columnLayouts[] = { new ColumnWeightData(200),
			new ColumnWeightData(100), new ColumnWeightData(200) };

	private RefreshRegistryAction refreshRegistryAction = null;

	/** The lib type comparator. */
	final LibraryTypeComparator libTypeComparator = new LibraryTypeComparator();

	/**
	 * Instantiates a new type viewer.
	 *
	 * @param table the table
	 * @param refreshRegistryAction the refresh registry action
	 */
	public TypeViewer(Table table, RefreshRegistryAction refreshRegistryAction) {
		super(table);
		this.refreshRegistryAction = refreshRegistryAction;
		setContentProvider(new TypeContentProvider());
		setLabelProvider(new TypeLabelProvider());
		addDoubleClickListener(new TypeViewerDCListener());
		addFilter(new TypeFilterHonLibrary(null));
		createColumns();
	}

	private void createColumns() {
		TableLayout layout = new TableLayout();
		getTable().setLayout(layout);
		getTable().setHeaderVisible(true);
		for (int i = 0; i < columnHeaders.length; i++) {
			layout.addColumnData(columnLayouts[i]);
			TableColumn tc = new TableColumn(getTable(), SWT.NONE, i);
			tc.setResizable(columnLayouts[i].resizable);
			tc.setText(columnHeaders[i]);

			tc.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent e) {
					if (e.getSource() instanceof TableColumn) {
						TableColumn tableColumn = (TableColumn) e.getSource();
						// remove the sorter
						if (getSorter() == AbstractTypeSorterFactory
								.createSorter(tableColumn.getText())) {
							setSorter(null);
						} else {
							setSorter(AbstractTypeSorterFactory
									.createSorter(tableColumn.getText()));
						}
					}
				}

			});

		}
	}

/**	
	private void createAction() {
		MenuManager menuManager = new MenuManager();
		Menu menu = menuManager.createContextMenu(getControl());
		getControl().setMenu(menu);
		menuManager.add(new AddImportAction(this));
		menuManager.add(new Separator());
		if (refreshRegistryAction != null)
			menuManager.add(refreshRegistryAction);
	}
**/


}
