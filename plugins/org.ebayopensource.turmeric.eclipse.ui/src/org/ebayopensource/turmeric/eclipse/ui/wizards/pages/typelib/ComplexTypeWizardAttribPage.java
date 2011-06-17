/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.ui.wizards.pages.typelib;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.ebayopensource.turmeric.common.config.LibraryType;
import org.ebayopensource.turmeric.eclipse.core.model.IParameterElement;
import org.ebayopensource.turmeric.eclipse.core.resources.constants.SOAProjectConstants;
import org.ebayopensource.turmeric.eclipse.core.resources.constants.SOATypeLibraryConstants;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.GlobalRepositorySystem;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.ISOAHelpProvider;
import org.ebayopensource.turmeric.eclipse.ui.SOABasePage;
import org.ebayopensource.turmeric.eclipse.ui.components.TableButtonPanelViewer;
import org.ebayopensource.turmeric.eclipse.ui.components.TableButtonPanelViewer.ColumnDef;
import org.ebayopensource.turmeric.eclipse.ui.wizards.pages.AbstractElementManagementWizardPage;
import org.ebayopensource.turmeric.eclipse.utils.collections.ListUtil;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.ISelectionChangedListener;
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
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;

/**
 * Attribute Page for complex type wizard.
 * 
 * @author ramurthy
 * 
 */

public class ComplexTypeWizardAttribPage extends SOABasePage {

	private static final String ATTRIBUTE_NAME = "Name";
	private static final String ATTRIBUTE_TYPE = "Type";
	private static final String ATTRIBUTE_DESC = "Description";

	private static final String[] colProperties = new String[] {
			ATTRIBUTE_NAME, ATTRIBUTE_TYPE, ATTRIBUTE_DESC };
	private static final List<String> SCHEMA_DATA_TYPES_LIST;

	private List<AttribTableModel> attribHolder = new LinkedList<AttribTableModel>();

	private CellEditor[] editors;

	private static final ColumnDef[] colDef = {
			// removed icons because it is causing blur
			new ColumnDef(ATTRIBUTE_NAME, 80, null), // UIActivator.getImageFromRegistry("attribute.gif")
			new ColumnDef(ATTRIBUTE_TYPE, 80, null), // UIActivator.getImageFromRegistry("schemaType.gif")
			new ColumnDef(ATTRIBUTE_DESC, 150, null) }; // UIActivator.getImageFromRegistry("description.gif")

	private TableButtonPanelViewer attribViewer;
	private Table table;

	static {
		SCHEMA_DATA_TYPES_LIST = Collections.unmodifiableList(Arrays
				.asList(SOATypeLibraryConstants.SCHEMA_DATA_TYPES));
	}

	/**
	 * Instantiates a new complex type wizard attrib page.
	 */
	public ComplexTypeWizardAttribPage() {
		super("complexTypeWizardAttribPage", "Add Details",
				"Add Attribute Name, Type and Description");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void createControl(Composite parent) {
		final Composite container = super.createParentControl(parent, 2);
		createViewer(container);
		initializeCellEditors();
		attribViewer.setCellEditors(editors);
		attribViewer.setCellModifier(new AttribTableCellModifier(attribViewer));
		attribViewer
				.addSelectionChangedListener(new ISelectionChangedListener() {
					@Override
					public void selectionChanged(SelectionChangedEvent event) {
						dialogChanged();
					}
				});

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

		attribViewer = new ComplexTypeTableButtonPanelViewer(container, style, colDef,
				true);
		attribViewer.setContentProvider(new ArrayContentProvider());
		attribViewer.setLabelProvider(new AttribTableLabelProvider());
		attribViewer.setColumnProperties(colProperties);
		attribViewer.setInput(attribHolder);
		attribViewer.getTable().setLayoutData(
				new GridData(SWT.FILL, SWT.FILL, true, true));
	}

	/**
	 * Initialize cell editors.
	 */
	public void initializeCellEditors() {
		table = attribViewer.getTable();
		editors = new CellEditor[colDef.length];
		editors[0] = new TextCellEditor(table);
		editors[1] = new ComboBoxCellEditor(table,
				SOATypeLibraryConstants.SCHEMA_DATA_TYPES);
		editors[2] = new TextCellEditor(table);
	}

	/**
	 * Gets the attrib table model.
	 *
	 * @return the attrib table model
	 */
	public AttribTableModel[] getAttribTableModel() {
		AttribTableModel[] attribTableModel = new AttribTableModel[attribHolder
				.size()];
		attribTableModel = attribHolder
				.toArray(attribTableModel);
		return attribTableModel;
	}

	/**
	 * Label Provider for Attrib Table.
	 * 
	 * @author ramurthy
	 * 
	 */
	private static class AttribTableLabelProvider extends LabelProvider implements
			ITableLabelProvider {

		@Override
		public Image getColumnImage(Object element, int columnIndex) {
			return null;
		}

		@Override
		public String getColumnText(Object element, int columnIndex) {
			AttribTableModel attribTableModel = null;
			if (element instanceof AttribTableModel)
				attribTableModel = (AttribTableModel) element;
			switch (columnIndex) {
			case 0:
				return attribTableModel.getAttribName();
			case 1:
				return AbstractElementManagementWizardPage
						.getDataTypeOfParameterElement(attribTableModel);
			case 2:
				return attribTableModel.getAttribDesc();
			default:
				return SOAProjectConstants.EMPTY_STRING;
			}
		}
	}

	/**
	 * Attrib Table UI Model
	 * 
	 * @author ramurthy
	 * 
	 */
	private class AttribTableCellModifier implements ICellModifier {

		private TableButtonPanelViewer viewer;

		public AttribTableCellModifier(TableButtonPanelViewer viewer) {
			this.viewer = viewer;
		}

		@Override
		public boolean canModify(Object element, String property) {
			int colIndex = viewer.getColumnNames().indexOf(property);
			if (colIndex < 0)
				return false;
			return true;
		}

		@Override
		public Object getValue(Object element, String property) {
			AttribTableModel model = (AttribTableModel) element;
			int colIndex = viewer.getColumnNames().indexOf(property);
			switch (colIndex) {
			case 0:
				return model.getAttribName();
			case 1:
				final String dataType = AbstractElementManagementWizardPage
						.getDataTypeOfParameterElement(model);
				return SCHEMA_DATA_TYPES_LIST.indexOf(dataType);
			case 2:
				return model.getAttribDesc();
			default:
				return null;
			}
		}

		@Override
		public void modify(Object element, String property, Object value) {
			if (element instanceof Item) {
				element = ((Item) element).getData();
			}
			AttribTableModel model = (AttribTableModel) element;
			int colIndex = viewer.getColumnNames().indexOf(property);
			switch (colIndex) {
			case 0:
				model.setAttribName((String) value);
				break;
			case 1:
				if (value instanceof Integer) {
					AbstractElementManagementWizardPage
							.modifyDataTypeOfParameterElement(model,
									SCHEMA_DATA_TYPES_LIST.get((Integer) value));
				}
				break;
			case 2:
				model.setAttribDesc((String) value);
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

		final Set<String> attribNames = new HashSet<String>();
		for (final AttribTableModel attribModel : attribHolder) {
			final String attribName = attribModel.getAttribName();
			if (!StringUtils.isAlphanumeric(attribName)) {
				updateStatus(this.attribViewer.getTable(),
						"Attribute name should be alphanumeric");
				return false;
			}
			if (attribNames.contains(attribName)) {
				updateStatus(this.attribViewer.getTable(),
						"Duplicate attrib name - " + attribName);
				return false;
			} else {
				updateStatus(null);
			}
			attribNames.add(attribName);
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see org.ebayopensource.turmeric.eclipse.ui.SOABasePage#getHelpContextID()
	 */
	@Override
	public String getHelpContextID() {
		return GlobalRepositorySystem.instanceOf().getActiveRepositorySystem()
				.getHelpProvider()
				.getHelpContextID(ISOAHelpProvider.PAGE_CREATE_SCHEMA_TYPE);
	}

	/**
	 * Attribute Table Model.
	 * 
	 */

	public static class AttribTableModel implements IParameterElement {

		private String attribName;
		private Object attribType;
		private String attribDesc;

		/**
		 * Gets the attrib desc.
		 *
		 * @return the attrib desc
		 */
		public String getAttribDesc() {
			return attribDesc;
		}

		/**
		 * Sets the attrib desc.
		 *
		 * @param attribDesc the new attrib desc
		 */
		public void setAttribDesc(String attribDesc) {
			this.attribDesc = attribDesc;
		}

		/**
		 * Gets the attrib name.
		 *
		 * @return the attrib name
		 */
		public String getAttribName() {
			return attribName;
		}

		/**
		 * Sets the attrib name.
		 *
		 * @param attribName the new attrib name
		 */
		public void setAttribName(String attribName) {
			this.attribName = attribName;
		}

		/**
		 * Gets the attrib type.
		 *
		 * @return the attrib type
		 */
		public String getAttribType() {
			if (attribType instanceof LibraryType) {
				return ((LibraryType) attribType).getName();
			} else if (attribType != null) {
				return attribType.toString();
			}
			return null;
		}

		/**
		 * Sets the attrib type.
		 *
		 * @param attribType the new attrib type
		 */
		public void setAttribType(Object attribType) {
			this.attribType = attribType;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Object getDatatype() {
			return getAttribType();
		}

		/**
		 * Gets the raw data type.
		 *
		 * @return the raw data type
		 */
		public Object getRawDataType() {
			return attribType;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public int getMaxOccurs() {
			throw new UnsupportedOperationException();
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public int getMinOccurs() {
			throw new UnsupportedOperationException();
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public String getName() {
			return getAttribName();
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void setDatatype(Object datatype) {
			setAttribType(datatype);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void setMaxOccurs(int maxOccurs) throws IllegalArgumentException {
			throw new UnsupportedOperationException();
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void setMinOccurs(int minOccurs) throws IllegalArgumentException {
			throw new UnsupportedOperationException();
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void setName(String name) {
			setAttribName(name);
		}
	}

	/* (non-Javadoc)
	 * @see org.ebayopensource.turmeric.eclipse.ui.SOABasePage#getDefaultValue(org.eclipse.swt.widgets.Text)
	 */
	@Override
	public String getDefaultValue(Text text) {
		return SOAProjectConstants.EMPTY_STRING;
	}

	private class ComplexTypeTableButtonPanelViewer extends
			TableButtonPanelViewer {

		public ComplexTypeTableButtonPanelViewer(Composite parent, int style,
				ColumnDef[] columnDef, boolean createAddRemoveButtons) {
			super(parent, style, columnDef, createAddRemoveButtons);
		}

		@Override
		protected void addButtonSelected(SelectionEvent event) {
			super.addButtonSelected(event);
			AttribTableModel model = new AttribTableModel();
			model.setAttribName(ATTRIBUTE_NAME);
			model.setAttribType(SOATypeLibraryConstants.SCHEMA_DATA_TYPES[0]);
			model.setAttribDesc(ATTRIBUTE_DESC);
			if (attribHolder.add(model)) {
				attribViewer.getRemoveButton().setEnabled(true);
				attribViewer.refresh();
				dialogChanged();
			}
		}

		@Override
		protected void downButtonSelected(SelectionEvent event) {
			super.downButtonSelected(event);
			final Object obj = getSelectedObject();
			if (obj instanceof AttribTableModel) {
				ListUtil.moveOnePositionDown(attribHolder,
						(AttribTableModel) obj);
				attribViewer.refresh();
			}
		}

		@Override
		protected void removeButtonSelected(SelectionEvent event) {
			super.removeButtonSelected(event);
			final Object obj = getSelectedObject();
			if (obj instanceof AttribTableModel && attribHolder.remove(obj)) {
				attribViewer.getRemoveButton().setEnabled(false);
				attribViewer.refresh();
				dialogChanged();
			}
		}

		@Override
		protected void upButtonSelected(SelectionEvent event) {
			super.upButtonSelected(event);
			final Object obj = getSelectedObject();
			if (obj instanceof AttribTableModel) {
				ListUtil.moveOnePositionUp(attribHolder, (AttribTableModel) obj);
				attribViewer.refresh();
			}
		}

		@Override
		protected void viewerSelectionChanged(SelectionChangedEvent event) {
			super.viewerSelectionChanged(event);
			dialogChanged();
		}
	}
}
