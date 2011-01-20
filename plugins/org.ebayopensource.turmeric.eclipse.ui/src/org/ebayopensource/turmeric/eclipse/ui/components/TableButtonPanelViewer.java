/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.ui.components;

import java.util.Arrays;
import java.util.List;

import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TableColumn;

/**
 * Custom widget - Table Viewer with button panel 
 * @author ramurthy
 *
 */

public class TableButtonPanelViewer extends TableViewer {
	
	private ColumnDef[] columns	;
	private Button addButton;
	private Button removeButton;
	private Button upButton;
	private Button downButton;
		
	public TableButtonPanelViewer(Composite parent, int style, ColumnDef[] columnDef) {
		super(parent, style | SWT.SINGLE);	
		this.columns = columnDef;
		getTable().setHeaderVisible(true);
		getTable().setLinesVisible(true);
		setColumns();
	}
	
	public TableButtonPanelViewer(Composite parent, int style, ColumnDef[] columnDef, 
			boolean createAddRemoveButtons) {
		this(parent, style, columnDef, createAddRemoveButtons, createAddRemoveButtons);
	}
	
	public TableButtonPanelViewer(Composite parent, int style, ColumnDef[] columnDef, 
			boolean createAddRemoveButtons, boolean createUpDownButtons) {
		this(parent, style, columnDef);
		if (createAddRemoveButtons)
			createButtons(parent, createUpDownButtons);
	}
	
	private void setColumns() {
		if (columns != null) {
			for (int i = 0; i < columns.length; i++) {
				TableColumn tableColumn = new TableColumn(getTable(), SWT.NONE);
				tableColumn.setText(columns[i].getTitle());
				tableColumn.setWidth(columns[i].getWidth());
				tableColumn.setData(columns[i]);
				tableColumn.setImage(columns[i].getImage());
			}
		}
		addSelectionChangedListener(new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent event) {
				viewerSelectionChanged(event);
				resetButtons();
			}
		});
	}
	
	protected void viewerSelectionChanged(SelectionChangedEvent event) {
		
	}
	
	public Button getAddButton() {
		return addButton;
	}
	
	public Button getRemoveButton() {
		return removeButton;
	}
	
	public Button getUpButton() {
		return upButton;
	}
	
	public Button getDownButton() {
		return downButton;
	}
	
	private void createButtons(Composite parent, boolean createUpDownButtons) {
		Composite buttonComposite = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout(1, false);		
		buttonComposite.setLayout(layout);
		GridData gd = new GridData(SWT.BEGINNING, SWT.BEGINNING, false, false);
		buttonComposite.setLayoutData(gd);
		
		gd = new GridData();
		gd.widthHint = 80;
		addButton = new Button(buttonComposite, SWT.PUSH | SWT.CENTER);
		addButton.setText("&Add");
		addButton.setLayoutData(gd);
		addButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				addButtonSelected(e);
				resetButtons();
			}
		});
				
		removeButton = new Button(buttonComposite, SWT.PUSH | SWT.CENTER);
		removeButton.setText("&Remove");
		removeButton.setLayoutData(gd);
		removeButton.setEnabled(false);
		removeButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				removeButtonSelected(e);
				resetButtons();
			}
		});
		
		if (createUpDownButtons) {
			//we also need the Up/Down buttons
			new Label(buttonComposite, SWT.LEFT);
			upButton = new Button(buttonComposite, SWT.PUSH | SWT.CENTER);
			upButton.setText("&Up");
			upButton.setLayoutData(gd);
			upButton.setEnabled(false);
			upButton.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					upButtonSelected(e);
					resetButtons();
				}
			});
			
			downButton = new Button(buttonComposite, SWT.PUSH | SWT.CENTER);
			downButton.setText("&Down");
			downButton.setLayoutData(gd);
			downButton.setEnabled(false);
			downButton.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					downButtonSelected(e);
					resetButtons();
				}
			});
		}
	}
	
	protected void addButtonSelected(SelectionEvent event) {
		
	}
	
	protected void removeButtonSelected(SelectionEvent event) {
		
	}
	
	protected void upButtonSelected(SelectionEvent event) {

	}

	protected void downButtonSelected(SelectionEvent event) {

	}
	
	private void resetButtons() {
		if (upButton != null && downButton != null) {
			upButton.setEnabled(false);
			downButton.setEnabled(false);
		}
		final Object obj = getSelectedObject();
		removeButton.setEnabled(obj != null);
		if (obj != null) {
			removeButton.setEnabled(true);
			if (upButton != null && downButton != null) {
				final int index = getTable().getSelectionIndex();
				final int size = getTable().getItemCount();
				if (index > 0)
					upButton.setEnabled(true);
				if (index + 1 < size)
					downButton.setEnabled(true);
			}
		}
	}
	
	public void setAddButtonsEnabled(boolean enabled) {
		addButton.setEnabled(enabled);
	}
	
	public Object getSelectedObject() {
		if (getSelection().isEmpty() == false 
				&& getSelection() instanceof IStructuredSelection) {
			return ((IStructuredSelection)getSelection()).getFirstElement();
		}
		return null;
	}
	
	public List<String> getColumnNames() {
		String[] columnNames = new String[columns.length];		
		for (int i = 0; i < columns.length; i++) {
			columnNames[i] = columns[i].getTitle();
		}		
		return Arrays.asList(columnNames);
	}
	
	/**
	 * Column Definition
	 * @author ramurthy
	 *
	 */

	public static class ColumnDef {
		
		private String title;	
		private int width;
		private Image image;
		
		public ColumnDef(String title, int width) {
			this.title = title;
			this.width = width;
		}
		
		public ColumnDef(String title, int width, Image image) {
			this(title, width);
			this.image = image;
		}

		public String getTitle() {
			return title;
		}

		public int getWidth() {
			return width;
		}
		
		public Image getImage() {
			return image;
		}
	}
}
