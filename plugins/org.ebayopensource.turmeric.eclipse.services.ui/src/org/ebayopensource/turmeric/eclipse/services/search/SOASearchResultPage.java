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
package org.ebayopensource.turmeric.eclipse.services.search;

import org.ebayopensource.turmeric.eclipse.services.search.SOASearchResult.SOASearchResultService;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.search.ui.text.AbstractTextSearchViewPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;


/**
 * @author yayu
 *
 */
public class SOASearchResultPage extends AbstractTextSearchViewPage {
	private SOASearchTableContentProvider fContentProvider;
	/**
	 * 
	 */
	public SOASearchResultPage() {
		super(FLAG_LAYOUT_FLAT);
	}
	@Override
	protected void elementsChanged(Object[] objects) {
		if (fContentProvider != null)
			fContentProvider.elementsChanged(objects);
	}
	@Override
	protected void clear() {
		if (fContentProvider != null)
			fContentProvider.clear();
	}

	@Override
	protected void configureTableViewer(final TableViewer viewer) {
		viewer.setLabelProvider(new SOASearchTableLabelProvider());
		fContentProvider = new SOASearchTableContentProvider();
		viewer.setContentProvider(fContentProvider);
		final Table table = viewer.getTable();
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		//set the columns for the table and the sorter used for he table
		{
			final TableLayout layout = new TableLayout();
			layout.addColumnData(new ColumnWeightData(35, 150, true));
			layout.addColumnData(new ColumnWeightData(15, 100, true));
			layout.addColumnData(new ColumnWeightData(15, 120, true));
			table.setLayout(layout);

			viewer.setSorter(new SOASearchResultTableViewerSorter());
			
			TableColumn svcNameColumn = new TableColumn(table, SWT.LEFT);
			svcNameColumn.setText("Service Name");
			svcNameColumn.addSelectionListener
			(new TableSortSelectionAdatper(viewer, svcNameColumn, 0));
			
			TableColumn svcLayerColumn = new TableColumn(table, SWT.LEFT);
			svcLayerColumn.setText("Service Layer");
			svcLayerColumn.addSelectionListener
			(new TableSortSelectionAdatper(viewer, svcLayerColumn, 1));
			
			TableColumn svcVersionColumn = new TableColumn(table, SWT.LEFT);
			svcVersionColumn.setText("Service Version");
			svcVersionColumn.addSelectionListener
			(new TableSortSelectionAdatper(viewer, svcVersionColumn, 2));
		}
		
		//add the viewer as the selection provider
		getSite().setSelectionProvider(viewer);
		
	}

	@Override
	protected void configureTreeViewer(TreeViewer viewer) {
		// TODO Auto-generated method stub
		
	}
	
	private class SOASearchResultTableViewerSorter extends ViewerSorter {
		//0=service name, 1=service layer, 2=service version
		private int column;
		
		@Override
		public int compare(Viewer viewer, Object e1, Object e2) {
			int result = 0; 
			if (e1 instanceof SOASearchResultService 
					&& e2 instanceof SOASearchResultService) {
				SOASearchResultService svc1 = (SOASearchResultService) e1;
				SOASearchResultService svc2 = (SOASearchResultService) e2;
				switch (column) {
				case 0:
					result = svc1.getServiceName()
					.compareToIgnoreCase(svc2.getServiceName());
					break;
				case 1:
					result = svc1.getServiceLayer()
					.compareToIgnoreCase(svc2.getServiceLayer());
					break;
				case 2:
					result = svc1.getServiceVersion()
					.compareToIgnoreCase(svc2.getServiceVersion());
					break;
				}
			}
			
			// If descending order, flip the direction
			if (viewer instanceof TableViewer 
					&& ((TableViewer)viewer).getTable()
					.getSortDirection() == SWT.DOWN) {
				result = -result;
			}
			
			return result;
		}
	}
	
	private class TableSortSelectionAdatper extends SelectionAdapter {
		private TableColumn tableColumn;
		private TableViewer viewer;
		private int index;

		public TableSortSelectionAdatper(TableViewer viewer, 
				TableColumn column, int index) {
			super();
			this.tableColumn = column;
			this.viewer = viewer;
			this.index = index;
		}

		public void widgetSelected(SelectionEvent e)
		{
			if (viewer.getSorter() instanceof SOASearchResultTableViewerSorter) {
				((SOASearchResultTableViewerSorter)viewer.getSorter())
				.column = index;
			}
			//find the sort direction seen on the column while sorting
			int dir = viewer.getTable().getSortDirection();
			if (viewer.getTable().getSortColumn() == tableColumn) {
				dir = dir == SWT.UP ? SWT.DOWN : SWT.UP;
			} else {
				dir = SWT.DOWN;
			}
			viewer.getTable().setSortDirection(dir);
			viewer.getTable().setSortColumn(tableColumn);
			//refresh the table to see the change
			viewer.refresh();
		}

	}


}
