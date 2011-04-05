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
import org.ebayopensource.turmeric.eclipse.core.model.IParameterElement;
import org.ebayopensource.turmeric.eclipse.core.resources.constants.SOAProjectConstants;
import org.ebayopensource.turmeric.eclipse.core.resources.constants.SOATypeLibraryConstants;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.ISOAHelpProvider;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.GlobalRepositorySystem;
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

import org.ebayopensource.turmeric.common.config.LibraryType;

/**
 * Attribute Page for complex type wizard
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

	public ComplexTypeWizardAttribPage() {
		super("complexTypeWizardAttribPage", "Add Details",
				"Add Attribute Name, Type and Description");
	}

	public void createControl(Composite parent) {
		final Composite container = super.createParentControl(parent, 2);
		createViewer(container);
		initializeCellEditors();
		attribViewer.setCellEditors(editors);
		attribViewer.setCellModifier(new AttribTableCellModifier(attribViewer));
		attribViewer
				.addSelectionChangedListener(new ISelectionChangedListener() {
					public void selectionChanged(SelectionChangedEvent event) {
						dialogChanged();
					}
				});

		dialogChanged();
	}

	public void createViewer(Composite container) {
		int style = SWT.BORDER | SWT.SINGLE | SWT.V_SCROLL | SWT.H_SCROLL
				| SWT.FULL_SELECTION;
		attribViewer = new TableButtonPanelViewer(container, style, colDef,
				true) {

			@Override
			protected void addButtonSelected(SelectionEvent event) {
				super.addButtonSelected(event);
				AttribTableModel model = new AttribTableModel();
				model.setAttribName(ATTRIBUTE_NAME);
				model
						.setAttribType(SOATypeLibraryConstants.SCHEMA_DATA_TYPES[0]);
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
					ListUtil.moveOnePositionUp(attribHolder,
							(AttribTableModel) obj);
					attribViewer.refresh();
				}
			}

			@Override
			protected void viewerSelectionChanged(SelectionChangedEvent event) {
				super.viewerSelectionChanged(event);
				dialogChanged();
			}
		};
		attribViewer.setContentProvider(new ArrayContentProvider());
		attribViewer.setLabelProvider(new AttribTableLabelProvider());
		attribViewer.setColumnProperties(colProperties);
		attribViewer.setInput(attribHolder);
		attribViewer.getTable().setLayoutData(
				new GridData(SWT.FILL, SWT.FILL, true, true));
	}

	public void initializeCellEditors() {
		table = attribViewer.getTable();
		editors = new CellEditor[colDef.length];
		editors[0] = new TextCellEditor(table);
		editors[1] = new ComboBoxCellEditor(table,
				SOATypeLibraryConstants.SCHEMA_DATA_TYPES);
		/*
		 * new SOAComboDialogCellEditor(table,
		 * SOATypeLibraryConstants.SCHEMA_DATA_TYPES) {
		 * 
		 * @Override protected Object openDialogBox(Control cellEditorWindow) {
		 * try { List<LibraryType> libTypes =
		 * SOAGlobalRegistryAdapter.getGlobalRegistry() .getAllTypes(); if
		 * (libTypes != null) { TypeSelector typeSelector = new
		 * TypeSelector(UIUtil.getActiveShell(), "Select Type",
		 * libTypes.toArray(new LibraryType[0]), "New Project");
		 * typeSelector.setMultipleSelection(false);
		 * 
		 * if (typeSelector.open() == Window.OK) return
		 * typeSelector.getFirstResult(); } } catch (Exception e) {
		 * SOALogger.getLogger().error(e); }
		 * 
		 * return null; } };
		 */
		editors[2] = new TextCellEditor(table);
	}

	public AttribTableModel[] getAttribTableModel() {
		AttribTableModel[] attribTableModel = new AttribTableModel[attribHolder
				.size()];
		attribTableModel = (AttribTableModel[]) attribHolder
				.toArray(attribTableModel);
		return attribTableModel;
	}

	/**
	 * Label Provider for Attrib Table
	 * 
	 * @author ramurthy
	 * 
	 */
	private class AttribTableLabelProvider extends LabelProvider implements
			ITableLabelProvider {

		public Image getColumnImage(Object element, int columnIndex) {
			return null;
		}

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

		public boolean canModify(Object element, String property) {
			int colIndex = viewer.getColumnNames().indexOf(property);
			if (colIndex < 0)
				return false;
			return true;
		}

		public Object getValue(Object element, String property) {
			AttribTableModel model = (AttribTableModel) element;
			int colIndex = viewer.getColumnNames().indexOf(property);
			switch (colIndex) {
			case 0:
				return (Object) model.getAttribName();
			case 1:
				final String dataType = AbstractElementManagementWizardPage
						.getDataTypeOfParameterElement(model);
				return SCHEMA_DATA_TYPES_LIST.indexOf(dataType);
			case 2:
				return (Object) model.getAttribDesc();
			default:
				return null;
			}
		}

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

	@Override
	protected boolean dialogChanged() {
		/*
		 * TODO this page is optional if (attribHolder.isEmpty()) {
		 * updateStatus("At least one attribute must be added"); return false; }
		 * else {
		 */
		updateStatus(null);
		// }

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

	@Override
	public String getHelpContextID() {
		return GlobalRepositorySystem.instanceOf().getActiveRepositorySystem()
				.getHelpProvider().getHelpContextID(
						ISOAHelpProvider.PAGE_CREATE_SCHEMA_TYPE);
	}

	/**
	 * Attribute Table Model
	 * 
	 */

	public static class AttribTableModel implements IParameterElement {

		private String attribName;
		private Object attribType;
		private String attribDesc;

		public String getAttribDesc() {
			return attribDesc;
		}

		public void setAttribDesc(String attribDesc) {
			this.attribDesc = attribDesc;
		}

		public String getAttribName() {
			return attribName;
		}

		public void setAttribName(String attribName) {
			this.attribName = attribName;
		}

		public String getAttribType() {
			if (attribType instanceof LibraryType) {
				return ((LibraryType) attribType).getName();
			} else if (attribType != null) {
				return attribType.toString();
			}
			return null;
		}

		public void setAttribType(Object attribType) {
			this.attribType = attribType;
		}

		public Object getDatatype() {
			return getAttribType();
		}

		public Object getRawDataType() {
			return attribType;
		}

		public int getMaxOccurs() {
			throw new UnsupportedOperationException();
		}

		public int getMinOccurs() {
			throw new UnsupportedOperationException();
		}

		public String getName() {
			return getAttribName();
		}

		public void setDatatype(Object datatype) {
			setAttribType(datatype);
		}

		public void setMaxOccurs(int maxOccurs) throws IllegalArgumentException {
			throw new UnsupportedOperationException();
		}

		public void setMinOccurs(int minOccurs) throws IllegalArgumentException {
			throw new UnsupportedOperationException();
		}

		public void setName(String name) {
			setAttribName(name);
		}
	}

	@Override
	public String getDefaultValue(Text text) {
		return SOAProjectConstants.EMPTY_STRING;
	}
}
