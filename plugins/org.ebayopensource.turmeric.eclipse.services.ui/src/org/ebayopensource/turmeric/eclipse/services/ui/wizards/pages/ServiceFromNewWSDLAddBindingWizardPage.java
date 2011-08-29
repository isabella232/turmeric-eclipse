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
package org.ebayopensource.turmeric.eclipse.services.ui.wizards.pages;


import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import org.ebayopensource.turmeric.eclipse.core.model.services.ServiceFromTemplateWsdlParamModel;
import org.ebayopensource.turmeric.eclipse.core.model.services.ServiceFromTemplateWsdlParamModel.Binding;
import org.ebayopensource.turmeric.eclipse.core.model.services.ServiceFromTemplateWsdlParamModel.TemplateWSDLModel;
import org.ebayopensource.turmeric.eclipse.core.resources.constants.SOAProjectConstants;
import org.ebayopensource.turmeric.eclipse.core.resources.constants.SOAProjectConstants.TemplateBinding;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.GlobalRepositorySystem;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.ISOAHelpProvider;
import org.ebayopensource.turmeric.eclipse.ui.SOABasePage;
import org.ebayopensource.turmeric.eclipse.ui.UIActivator;
import org.ebayopensource.turmeric.eclipse.ui.components.TableButtonPanelViewer;
import org.ebayopensource.turmeric.eclipse.ui.components.TableButtonPanelViewer.ColumnDef;
import org.ebayopensource.turmeric.eclipse.utils.plugin.EclipseMessageUtils;
import org.ebayopensource.turmeric.eclipse.utils.ui.UIUtil;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;


// TODO: Auto-generated Javadoc
/**
 * The Class ServiceFromNewWSDLAddBindingWizardPage.
 *
 * @author yayu
 */
public class ServiceFromNewWSDLAddBindingWizardPage extends SOABasePage {

	/** The binding viewer. */
	private TableButtonPanelViewer bindingViewer;
	
	/** The Constant COLUMN_TYPE. */
	public static final String COLUMN_TYPE = "type";
	
	/** The Constant BINDING_COLUMN_PROPERTIES. */
	public static final String[] BINDING_COLUMN_PROPERTIES = {COLUMN_TYPE};
	
	/** The bindings. */
	private Set<Binding> bindings = new LinkedHashSet<Binding>();

	/**
	 * Instantiates a new service from new wsdl add binding wizard page.
	 *
	 */
	public ServiceFromNewWSDLAddBindingWizardPage() {
		super("newSOAServiceProjectFromBlankWSDLAddBindingWizardPage", "Service from Template WSDL - Add Binding", 
		"Add bindings to the template WSDL");
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
		createBindingViewer(container);
	}
	
	/**
	 * Creates the binding viewer.
	 *
	 * @param parent the parent
	 */
	private void createBindingViewer(Composite parent) {
		final ColumnDef[] columns = new ColumnDef[]{
				new ColumnDef("Binding Type", 150, UIActivator.getImageFromRegistry("binding.gif"))};
		bindingViewer = new TableButtonPanelViewer(parent, 
				SWT.BORDER | SWT.SINGLE | SWT.V_SCROLL | SWT.H_SCROLL | SWT.FULL_SELECTION, 
				columns, true, false) {

					@Override
					protected void addButtonSelected(SelectionEvent event) {
						super.addButtonSelected(event);
						if (event.doit == true && bindingViewer != null) {
							final Binding binding = new Binding(SOAProjectConstants.TemplateBinding.values()[0]);
							if (bindings.add(binding)) {
								bindingViewer.refresh();
								dialogChanged();
							}
						}
					}

					@Override
					protected void removeButtonSelected(SelectionEvent event) {
						super.removeButtonSelected(event);
						final TemplateWSDLModel model = getSelectedModel(null);
						if (event.doit == true && model instanceof Binding
								&& MessageDialog.openConfirm(UIUtil.getActiveShell(), 
										"Warning", 
										"Removing a binding might cause unexpected result " +
										"in the generated WSDL, and it is strongly recommmended " +
										"to keep the default bindings.\n\nAre you sure to remove binding "
										+ ((Binding)model).getBinding().name() + "?")) {
							
							if ( bindings.remove(model)) {
								bindingViewer.refresh();
								dialogChanged();
							}
						}
					}

					@Override
					protected void viewerSelectionChanged(
							SelectionChangedEvent event) {
						super.viewerSelectionChanged(event);
					}
			
		};

		GridData data = new GridData(GridData.FILL_BOTH);
		data.horizontalSpan = 2;
		final Table table = bindingViewer.getTable();
		table.setLayoutData(data);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		
		bindingViewer.setContentProvider(new IStructuredContentProvider(){

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
		
		bindingViewer.setLabelProvider(new ITableLabelProvider() {

			@Override
			public Image getColumnImage(Object element, int columnIndex) {
				return null;
			}

			@Override
			public String getColumnText(Object element, int columnIndex) {
				if (element instanceof Binding) {
					final Binding binding = (Binding)element;
					if (columnIndex == 0 ) {
						return binding.getBinding().name();
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
		
		bindingViewer.setColumnProperties(BINDING_COLUMN_PROPERTIES);
		bindingViewer.setCellModifier(new ICellModifier() {

			@Override
			public boolean canModify(Object element, String property) {
				return true;
			}

			@Override
			public Object getValue(Object element, String property) {
				if (element instanceof Binding) {
					final Binding binding = (Binding) element;
					if (COLUMN_TYPE.equals(property)) {
						return binding.getBinding().ordinal();
					} 
				}
				return SOAProjectConstants.EMPTY_STRING;
			}

			@Override
			public void modify(Object element, String property, Object value) {
				if (element instanceof TableItem) {
					final TableItem item = (TableItem)element;
					if (item.getData() instanceof Binding) {
						Object obj = null;
						final Binding binding = (Binding) item.getData();
						if (COLUMN_TYPE.equals(property) 
								&& value instanceof Integer) {
							final Integer newValue = (Integer)value;
							if (binding.getBinding().ordinal() 
									!= newValue.intValue()) {
								binding.setBinding(TemplateBinding.values()[newValue.intValue()]);
								obj = binding;
							}
						}
						if (obj != null) {
							bindingViewer.refresh(obj);
							dialogChanged();
						}
					}
				}
			}
			
		});
		bindingViewer.setCellEditors(new CellEditor[]{
				new ComboBoxCellEditor(table, 
						SOAProjectConstants.TemplateBinding.getAllBindingNames()
						.toArray(new String[0]), SWT.READ_ONLY), 
				new TextCellEditor(table)});
		
		bindingViewer.setInput(getInputObject());
	}
	
	/* (non-Javadoc)
	 * @see org.ebayopensource.turmeric.eclipse.ui.SOABasePage#dialogChanged()
	 */
	@Override
	protected boolean dialogChanged() {
		if(super.dialogChanged() == false)
			return false;
		
		final Set<String> bindingNames = new HashSet<String>();
		for (final Binding binding : bindings) {
			final String bindingName = binding.getBinding().name();
			if (bindingNames.contains(bindingName)) {
				updateStatus(bindingViewer.getControl(), 
						"Duplicate bindings->" + bindingName);
				return false;
			}
			bindingNames.add(bindingName);
		}
		
		if (bindings.isEmpty() == true) {
			updatePageStatus(bindingViewer.getControl(), 
					EclipseMessageUtils.createStatus(
					"No binding are added to the WSDL", IStatus.WARNING));
		}
		return true;
	}

	/**
	 * Gets the bindings.
	 *
	 * @return the bindings
	 */
	public Set<Binding> getBindings() {
		return bindings;
	}
	
	/**
	 * Gets the selected model.
	 *
	 * @param sel the sel
	 * @return the selected model
	 */
	private TemplateWSDLModel getSelectedModel(ISelection sel) {
		IStructuredSelection selection = null; 
		if (sel == null && this.bindingViewer != null) {
			if (bindingViewer.getSelection().isEmpty() == false
					&& bindingViewer.getSelection() instanceof IStructuredSelection) {
				selection = (IStructuredSelection) bindingViewer.getSelection();
			}
		} else if (sel instanceof IStructuredSelection) {
			selection = (IStructuredSelection)sel;
		}

		if (selection != null && selection.getFirstElement() instanceof TemplateWSDLModel) {
			return (TemplateWSDLModel)selection.getFirstElement();
		}
		return null;
	}
	
	/**
	 * Gets the input object.
	 *
	 * @return the input object
	 */
	private Object getInputObject() {
		bindings.addAll(ServiceFromTemplateWsdlParamModel.getDefaultBindings());
		return bindings;
	}

	/* (non-Javadoc)
	 * @see org.ebayopensource.turmeric.eclipse.ui.SOABasePage#getHelpContextID()
	 */
	@Override
	public String getHelpContextID() {
		return GlobalRepositorySystem.instanceOf().getActiveRepositorySystem()
		.getHelpProvider().getHelpContextID(
				ISOAHelpProvider.PAGE_CREATE_SERVICE_FROM_TEMPLATE_WSDL);
	}

}
