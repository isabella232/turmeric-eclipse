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
package org.ebayopensource.turmeric.eclipse.ui.wizards.pages;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.ebayopensource.turmeric.common.config.LibraryType;
import org.ebayopensource.turmeric.eclipse.core.logging.SOALogger;
import org.ebayopensource.turmeric.eclipse.core.model.IParameterElement;
import org.ebayopensource.turmeric.eclipse.core.resources.constants.SOAProjectConstants;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.GlobalRepositorySystem;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.ISOAHelpProvider;
import org.ebayopensource.turmeric.eclipse.ui.SOABasePage;
import org.ebayopensource.turmeric.eclipse.ui.components.SOAComboDialogCellEditor;
import org.ebayopensource.turmeric.eclipse.ui.components.TableButtonPanelViewer;
import org.ebayopensource.turmeric.eclipse.ui.components.TableButtonPanelViewer.ColumnDef;
import org.ebayopensource.turmeric.eclipse.utils.collections.ListUtil;
import org.ebayopensource.turmeric.eclipse.utils.plugin.EclipseMessageUtils;
import org.ebayopensource.turmeric.eclipse.utils.ui.UIUtil;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Item;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;

/**
 * The Class AbstractElementManagementWizardPage.
 *
 * @author yayu
 */
public abstract class AbstractElementManagementWizardPage extends SOABasePage {
//	public static final String NON_EDITABLE_COL_NAME = "";
	/** The Constant COLUMN_NAME. */
public static final String COLUMN_NAME = "name";
	
	/** The Constant COLUMN_TYPE. */
	public static final String COLUMN_TYPE = "type";
	
	/** The Constant COLUMN_OCCUR_MIN. */
	public static final String COLUMN_OCCUR_MIN = "min_occur";
	
	/** The Constant COLUMN_OCCUR_MAX. */
	public static final String COLUMN_OCCUR_MAX = "max_occur";
	
	/** The Constant PARAMETER_COLUMN_PROPERTIES. */
	public static final String[] PARAMETER_COLUMN_PROPERTIES = 
	{COLUMN_NAME, COLUMN_TYPE, COLUMN_OCCUR_MIN, COLUMN_OCCUR_MAX};
	
	/** The Constant OCCUR_UNBOUNDED. */
	public static final String OCCUR_UNBOUNDED = "unbounded";	
	
	/** The Constant DEFAULT_MAX_OCCURS. */
	protected static final int DEFAULT_MAX_OCCURS = -1;
	
	/** The types viewer. */
	protected TableButtonPanelViewer typesViewer;
	
	/** The elements. */
	protected List<IParameterElement> elements = new ArrayList<IParameterElement>();
	
	/** The param cell editor. */
	protected CellEditor paramCellEditor;
	private int minimumRequiredElementNum = 1;

	/**
	 * Instantiates a new abstract element management wizard page.
	 *
	 * @param pageName the page name
	 * @param title the title
	 * @param description the description
	 * @param elements the elements
	 */
	public AbstractElementManagementWizardPage(String pageName, String title,
			String description, List<? extends IParameterElement> elements) {
		this(pageName, title, description, elements, 1);
	}
	
	
	/*public AbstractElementManagementWizardPage(String pageName, String title,
			String description, List<? extends IParameterElement> elements, minimumRequiredElementNum) {
		super(pageName, title, description);
		if (elements != null) {
			this.elements.addAll(elements);
		}
	}*/

	/**
	 * Instantiates a new abstract element management wizard page.
	 *
	 * @param pageName the page name
	 * @param title the title
	 * @param description the description
	 * @param elements the elements
	 * @param minimumRequiredElementNum the minimum required element num
	 */
	public AbstractElementManagementWizardPage(String pageName,
			String title, String description, 
			List<? extends IParameterElement> elements, int minimumRequiredElementNum) {
		super(pageName, title, description);
		if (elements != null) {
			this.elements.addAll(elements);
		}
		if (minimumRequiredElementNum >= 0) {
			this.minimumRequiredElementNum = minimumRequiredElementNum;
		}
	}

	/**
	 * Gets the elements.
	 *
	 * @return the elements
	 */
	public List<IParameterElement> getElements() {
		return elements;
	}

	/* (non-Javadoc)
	 * @see org.ebayopensource.turmeric.eclipse.ui.SOABasePage#dialogChanged()
	 */
	@Override
	protected boolean dialogChanged() {
		if (super.dialogChanged() == false)
			return false;
		
		/*if (elements.size() < minimumRequiredElementNum) {
			updateStatus("Please add at least " + minimumRequiredElementNum + " element(s)");
			return false;
		}*/
		
		final Set<String> elemNames = new HashSet<String>();
		for (final IParameterElement elem : elements) {
			if (elemNames.contains(elem.getName())) {
				updateStatus(this.typesViewer.getTable(), 
						"Duplicate element names>" + elem.getName());
				return false;
			}
			elemNames.add(elem.getName());
		}
		
		return true;
	}

	/* (non-Javadoc)
	 * @see org.ebayopensource.turmeric.eclipse.ui.SOABasePage#getDefaultValue(org.eclipse.swt.widgets.Text)
	 */
	@Override
	public String getDefaultValue(Text text) {
		return SOAProjectConstants.EMPTY_STRING;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void createControl(Composite parent) {
		final Composite container = super.createParentControl(parent, 3);
		createTypesViewer(container);
	}
	
	private TableViewer createTypesViewer(Composite parent) {
		final List<ColumnDef> columns = new ArrayList<ColumnDef>(4);
		{
			columns.add(new ColumnDef("Name", 80));
			columns.add(new ColumnDef("Type", 95));
			columns.add(new ColumnDef("Min Occurs", 70));
			columns.add(new ColumnDef("Max Occurs", 70));
		}
		
		typesViewer = new TableButtonPanelViewer(parent, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL 
				| SWT.FULL_SELECTION | SWT.SINGLE, columns.toArray(new ColumnDef[0]), true, true) {

			@Override
			protected void addButtonSelected(SelectionEvent event) {
				super.addButtonSelected(event);
				elements.add(createNewElement("element", "string"));
				addPressed();
				typesViewer.refresh();
				AbstractElementManagementWizardPage.this.dialogChanged();
			}

			@Override
			protected void downButtonSelected(SelectionEvent event) {
				super.downButtonSelected(event);
				final Object obj = getSelectedObject();
				if (obj instanceof IParameterElement) {
					ListUtil.moveOnePositionDown(elements, (IParameterElement)obj);
					typesViewer.refresh();
				}
			}

			@Override
			protected void removeButtonSelected(SelectionEvent event) {
				super.removeButtonSelected(event);
				final Object obj = getSelectedObject();
				if (obj instanceof IParameterElement) {
					elements.remove(obj);
					removePrssed(obj);
					typesViewer.refresh();
					AbstractElementManagementWizardPage.this.dialogChanged();
				}
			}

			@Override
			protected void upButtonSelected(SelectionEvent event) {
				super.upButtonSelected(event);
				final Object obj = getSelectedObject();
				if (obj instanceof IParameterElement) {
					ListUtil.moveOnePositionUp(elements, (IParameterElement)obj);
					typesViewer.refresh();
				}
			}

			@Override
			protected void viewerSelectionChanged(
					SelectionChangedEvent event) {
				super.viewerSelectionChanged(event);
				dialogChanged();
			}
		};
		
		
		GridData data = new GridData(GridData.FILL_BOTH);
		data.horizontalSpan = 2;
		final Table table = typesViewer.getTable();
		table.setLayoutData(data);
		
		typesViewer.setContentProvider(new IStructuredContentProvider() {
			@Override
			public Object[] getElements(Object inputElement) {
				if (inputElement instanceof Collection<?>) {
					return ((Collection<?>)inputElement).toArray();
				}
				return Collections.EMPTY_LIST.toArray();
			}

			@Override
			public void dispose() {
				
			}

			@Override
			public void inputChanged(Viewer viewer, Object oldInput,
					Object newInput) {
			}
		});
		
		typesViewer.setLabelProvider(new ITableLabelProvider() {
			@Override
			public Image getColumnImage(Object element, int columnIndex) {
				return null;
			}

			@Override
			public String getColumnText(Object element, int columnIndex) {
				
				if (element instanceof IParameterElement) {
					final IParameterElement param = (IParameterElement) element;
					switch(columnIndex) {
					case 0:
						return param.getName();
					case 1:
						return getDataTypeOfParameterElement(param);
					case 2:
						return String.valueOf(param.getMinOccurs());
					case 3:
						if (param.getMaxOccurs() < 0)
							return OCCUR_UNBOUNDED;
						return String.valueOf(param.getMaxOccurs());
					}
				}
				
				return SOAProjectConstants.EMPTY_STRING;
			}

			@Override
			public void addListener(ILabelProviderListener listener) {
			}

			@Override
			public void dispose() {
			}

			@Override
			public boolean isLabelProperty(Object element, String property) {
				return false;
			}

			@Override
			public void removeListener(ILabelProviderListener listener) {
			}
			
		});
		typesViewer.setColumnProperties(PARAMETER_COLUMN_PROPERTIES);		
		typesViewer.setCellModifier(new ICellModifier() {

			@Override
			public boolean canModify(Object element, String property) {


				return true;
			}

			@Override
			public Object getValue(Object element, String property) {
				if (element instanceof IParameterElement) {
					final IParameterElement param = (IParameterElement) element;
					if (COLUMN_NAME.equals(property)) {
						return param.getName();
					} if (COLUMN_TYPE.equals(property)) {
						return getDataTypeOfParameterElement(param);
					} else if (COLUMN_OCCUR_MIN.equals(property)) {
						return String.valueOf(param.getMinOccurs());
					} else if (COLUMN_OCCUR_MAX.equals(property)) {
						if (param.getMaxOccurs() < 0)
							return OCCUR_UNBOUNDED;
						return String.valueOf(param.getMaxOccurs());
					}
				}
				
				return SOAProjectConstants.EMPTY_STRING;
			}

			@Override
			public void modify(Object element, String property, Object value) {
				Object obj = null;
				if (element instanceof Item) {
					element = ((Item) element).getData();
				}
				try {
					if (element instanceof IParameterElement) {
						final IParameterElement param = (IParameterElement) element;
						if (COLUMN_NAME.equals(property)) {
							if (param.getName().equals(value) == false) {
								param.setName(String.valueOf(value));
								obj = param;
							}
						} else if (COLUMN_TYPE.equals(property)) {
							//modify datatype
							if (modifyDataTypeOfParameterElement(param, value))
								obj = param;
						} else if (COLUMN_OCCUR_MIN.equals(property)) {
							int newValue = Integer.parseInt(String.valueOf(value));
							if (param.getMinOccurs() != newValue) {
								param.setMinOccurs(newValue);
								obj = param;
							}
						} else if (COLUMN_OCCUR_MAX.equals(property)) {
							if (!String.valueOf(value).equalsIgnoreCase(OCCUR_UNBOUNDED)) {
								int newValue = Integer.parseInt(String.valueOf(value));
								if (param.getMaxOccurs() != newValue) {
									param.setMaxOccurs(newValue);
									obj = param;
								}
							} else {
								param.setMaxOccurs(DEFAULT_MAX_OCCURS);
								obj = param;
							}
						}
					}
				} catch (RuntimeException e) {
					updatePageStatus(typesViewer.getTable(), 
							EclipseMessageUtils.createErrorStatus(e));
					return;
				}
				if (obj != null) {
					typesViewer.refresh(obj);
					dialogChanged();
				}
			}			
		});
		paramCellEditor = new SOAComboDialogCellEditor(table, 
				SOAProjectConstants.DEFAULT_DATA_TYPES) {
			@Override
			protected Object openDialogBox(Control cellEditorWindow) {
				List<LibraryType> libTypes = null;
				try {
					libTypes = getSelectedLibraryTypes();
				} catch (Exception e) {
					SOALogger.getLogger().error(e);
					
				}
				if (libTypes != null) {
					return openTypeSelectorDialog(UIUtil.getActiveShell(), 
							libTypes.toArray(new LibraryType[0]));
				}
				return null;
			}

			@Override
			protected String getValueText(Object value) {
				if (value instanceof LibraryType)
					return ((LibraryType)value).getName();
				return super.getValueText(value);
			}
		};
		typesViewer.setCellEditors(new CellEditor[]{ 
				new TextCellEditor(table), paramCellEditor,
				 new TextCellEditor(table), new TextCellEditor(table)});
		
		typesViewer.setInput(this.elements);
		
		return typesViewer;
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
	 * Gets the data type of parameter element.
	 *
	 * @param elem the elem
	 * @return the data type of parameter element
	 */
	public static String getDataTypeOfParameterElement(
			IParameterElement elem) {
		if (elem != null) {
			if (elem.getDatatype() instanceof LibraryType) {
				return ((LibraryType)elem.getDatatype()).getName();
			} else if (elem.getDatatype() != null){
				return elem.getDatatype().toString();
			}
		}
		return SOAProjectConstants.EMPTY_STRING;
	}
	
	/**
	 * Modify data type of parameter element.
	 *
	 * @param elem the elem
	 * @param value the value
	 * @return true, if successful
	 */
	public static boolean modifyDataTypeOfParameterElement(IParameterElement elem, 
			Object value) {
		if (value.equals(elem.getDatatype()) == false){
			elem.setDatatype(value);
			return true;
		}
		return false;
	}
	
	/**
	 * Gets the selected library types.
	 *
	 * @return the selected library types
	 * @throws Exception the exception
	 */
	protected abstract List<LibraryType> getSelectedLibraryTypes() throws Exception;
	
	/**
	 * Open type selector dialog.
	 *
	 * @param shell the shell
	 * @param libTypes the lib types
	 * @return the object
	 */
	protected abstract Object openTypeSelectorDialog(Shell shell, LibraryType[] libTypes);
	
	/**
	 * Creates the new element.
	 *
	 * @param name the name
	 * @param type the type
	 * @return the i parameter element
	 */
	protected abstract IParameterElement createNewElement(String name, String type);
	
	/**
	 * Adds the pressed.
	 */
	protected abstract void addPressed();
	
	/**
	 * Removes the prssed.
	 *
	 * @param object the object
	 */
	protected abstract void removePrssed(Object object);
}
