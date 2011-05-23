/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.ui.wizards.pages.typelib;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.ebayopensource.turmeric.eclipse.core.resources.constants.SOAProjectConstants;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.ISOAHelpProvider;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.GlobalRepositorySystem;
import org.ebayopensource.turmeric.eclipse.ui.SOABasePage;
import org.ebayopensource.turmeric.eclipse.ui.components.TableButtonPanelViewer;
import org.ebayopensource.turmeric.eclipse.ui.components.TableButtonPanelViewer.ColumnDef;
import org.ebayopensource.turmeric.eclipse.utils.collections.ListUtil;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Item;
import org.eclipse.swt.widgets.Text;


/**
 * Details Page for enum type wizard.
 *
 * @author ramurthy
 */

public class EnumTypeWizardDetailsPage extends SOABasePage {
	
	private static final String COL_ENUM_VALUE = "Value";
	private static final String COL_ENUM_DESC = "Description";
	private static final String[] colProperties = new String[] {
			SOAProjectConstants.EMPTY_STRING, COL_ENUM_VALUE, COL_ENUM_DESC };
	private List<EnumTableModel> enumHolder = new LinkedList<EnumTableModel>();
	private TableButtonPanelViewer enumViewer;
	private CellEditor[] editors;

	private static final ColumnDef[] colDef = {
			new ColumnDef(SOAProjectConstants.EMPTY_STRING, 20),
			// removing the icons because it is causing blur
			new ColumnDef(COL_ENUM_VALUE, 80, null),// UIActivator.getImageFromRegistry("enum.gif")
			new ColumnDef(COL_ENUM_DESC, 200, null) // UIActivator.getImageFromRegistry("description.gif")
	};

	/**
	 * Instantiates a new enum type wizard details page.
	 */
	public EnumTypeWizardDetailsPage() {
		super("enumTypeWizardDetailsPage", "Add Details",
				"Add Enum Values and Descriptions");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void createControl(Composite parent) {
		final Composite container = super.createParentControl(parent, 2);
		createViewer(container);
		initializeCellEditors();
		enumViewer.setCellEditors(editors);
		enumViewer.setCellModifier(new EnumTableCellModifier(enumViewer));
		dialogChanged();
	}

	/**
	 * Creates the viewer.
	 *
	 * @param container the container
	 */
	public void createViewer(Composite container) {
		int style = SWT.BORDER | SWT.SINGLE | SWT.V_SCROLL | SWT.H_SCROLL
				| SWT.FULL_SELECTION;
		enumViewer = new TableButtonPanelViewer(container, style, colDef, true) {

			@Override
			protected void addButtonSelected(SelectionEvent event) {
				super.addButtonSelected(event);
				EnumTableModel model = new EnumTableModel();
				model.setEnumValue(COL_ENUM_VALUE);
				model.setEnumDesc(COL_ENUM_DESC);
				if (enumHolder.add(model)) {
					enumViewer.getRemoveButton().setEnabled(true);
					enumViewer.refresh();
					dialogChanged();
				}
			}

			@Override
			protected void downButtonSelected(SelectionEvent event) {
				super.downButtonSelected(event);
				final Object obj = getSelectedObject();
				if (obj instanceof EnumTableModel) {
					ListUtil.moveOnePositionDown(enumHolder,
							(EnumTableModel) obj);
					enumViewer.refresh();
				}
			}

			@Override
			protected void removeButtonSelected(SelectionEvent event) {
				super.removeButtonSelected(event);
				final Object obj = getSelectedObject();
				if (obj instanceof EnumTableModel && enumHolder.remove(obj)) {
					enumViewer.getRemoveButton().setEnabled(false);
					enumViewer.refresh();
					dialogChanged();
				}
			}

			@Override
			protected void upButtonSelected(SelectionEvent event) {
				super.upButtonSelected(event);
				final Object obj = getSelectedObject();
				if (obj instanceof EnumTableModel) {
					ListUtil
							.moveOnePositionUp(enumHolder, (EnumTableModel) obj);
					enumViewer.refresh();
				}
			}

			@Override
			protected void viewerSelectionChanged(SelectionChangedEvent event) {
				super.viewerSelectionChanged(event);
				dialogChanged();
			}
		};
		enumViewer.setContentProvider(new ArrayContentProvider());
		enumViewer.setLabelProvider(new EnumTableLabelProvider());
		enumViewer.setColumnProperties(colProperties);
		enumViewer.setInput(enumHolder);
		enumViewer.getTable().setLayoutData(
				new GridData(SWT.FILL, SWT.FILL, true, true));
	}

	/**
	 * Initialize cell editors.
	 */
	public void initializeCellEditors() {
		editors = new CellEditor[colDef.length];
		int i = 0;
		while (i < editors.length) {
			editors[i] = new TextCellEditor(enumViewer.getTable());
			i++;
		}
	}

	/**
	 * Gets the enum table model.
	 *
	 * @return the enum table model
	 */
	public EnumTableModel[] getEnumTableModel() {
		EnumTableModel[] enumTableModel = new EnumTableModel[enumHolder.size()];
		enumTableModel = enumHolder.toArray(enumTableModel);
		return enumTableModel;
	}

	/* (non-Javadoc)
	 * @see org.ebayopensource.turmeric.eclipse.ui.SOABasePage#getHelpContextID()
	 */
	@Override
	public String getHelpContextID() {
		return GlobalRepositorySystem.instanceOf().getActiveRepositorySystem()
				.getHelpProvider().getHelpContextID(
						ISOAHelpProvider.PAGE_CREATE_SCHEMA_TYPE);
	}

	/**
	 * Label Provider for Enum Table
	 * 
	 */
	private static class EnumTableLabelProvider extends LabelProvider implements
			ITableLabelProvider {

		@Override
		public Image getColumnImage(Object element, int columnIndex) {
			return null;
		}

		@Override
		public String getColumnText(Object element, int columnIndex) {
			EnumTableModel enumTableModel = null;
			if (element instanceof EnumTableModel)
				enumTableModel = (EnumTableModel) element;
			switch (columnIndex) {
			case 1:
				return enumTableModel.getEnumValue();

			case 2:
				return enumTableModel.getEnumDesc();

			default:
				return SOAProjectConstants.EMPTY_STRING;
			}
		}
	}

	/**
	 * Enum Table UI Model
	 * 
	 */
	private class EnumTableCellModifier implements ICellModifier {

		private TableButtonPanelViewer viewer;

		public EnumTableCellModifier(TableButtonPanelViewer viewer) {
			this.viewer = viewer;
		}

		@Override
		public boolean canModify(Object element, String property) {
			int colIndex = viewer.getColumnNames().indexOf(property);
			if (colIndex == 0)
				return false;
			return true;
		}

		@Override
		public Object getValue(Object element, String property) {
			EnumTableModel model = (EnumTableModel) element;
			int colIndex = viewer.getColumnNames().indexOf(property);
			switch (colIndex) {
			case 1:
				return model.getEnumValue();
			case 2:
				return model.getEnumDesc();
			default:
				return null;
			}
		}

		@Override
		public void modify(Object element, String property, Object value) {
			if (element instanceof Item) {
				element = ((Item) element).getData();
			}
			EnumTableModel model = (EnumTableModel) element;
			int colIndex = viewer.getColumnNames().indexOf(property);
			switch (colIndex) {
			case 1:
				model.setEnumValue((String) value);
				break;
			case 2:
				model.setEnumDesc((String) value);
				break;
			default:
			}
			if (model != null) {
				viewer.refresh();
				dialogChanged();
			}
		}
	}

	/* (non-Javadoc)
	 * @see org.ebayopensource.turmeric.eclipse.ui.SOABasePage#dialogChanged()
	 */
	@Override
	protected boolean dialogChanged() {
		updateStatus(null);

		final Set<String> enumValues = new HashSet<String>();
		for (final EnumTableModel enumModel : enumHolder) {
			final String enumValue = enumModel.getEnumValue();
			/*
			 * if (!StringUtils.isAlphanumeric(enumValue)) {
			 * updateStatus("Element name should be alphanumeric"); return
			 * false; }
			 */
			if (enumValues.contains(enumValue)) {
				updateStatus(enumViewer.getTable(), "Duplicate enum value - "
						+ enumValue);
				return false;
			}/*
			 * else { updateStatus(null); }
			 */
			enumValues.add(enumValue);
		}
		return true;
	}

	/**
	 * Enum Table Model.
	 */
	public static class EnumTableModel {

		private String enumValue;
		private String enumDesc;

		/**
		 * Gets the enum desc.
		 *
		 * @return the enum desc
		 */
		public String getEnumDesc() {
			return enumDesc;
		}

		/**
		 * Sets the enum desc.
		 *
		 * @param enumDesc the new enum desc
		 */
		public void setEnumDesc(String enumDesc) {
			this.enumDesc = enumDesc;
		}

		/**
		 * Gets the enum value.
		 *
		 * @return the enum value
		 */
		public String getEnumValue() {
			return enumValue;
		}

		/**
		 * Sets the enum value.
		 *
		 * @param enumValue the new enum value
		 */
		public void setEnumValue(String enumValue) {
			this.enumValue = enumValue;
		}
	}

	/* (non-Javadoc)
	 * @see org.ebayopensource.turmeric.eclipse.ui.SOABasePage#getDefaultValue(org.eclipse.swt.widgets.Text)
	 */
	@Override
	public String getDefaultValue(Text text) {
		return SOAProjectConstants.EMPTY_STRING;
	}
}
