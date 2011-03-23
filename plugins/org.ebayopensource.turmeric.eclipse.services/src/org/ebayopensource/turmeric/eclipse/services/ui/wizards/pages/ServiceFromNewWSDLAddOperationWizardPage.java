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


import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.ebayopensource.turmeric.eclipse.logging.SOALogger;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.GlobalRepositorySystem;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.ISOAHelpProvider;
import org.ebayopensource.turmeric.eclipse.resources.constants.SOAProjectConstants;
import org.ebayopensource.turmeric.eclipse.resources.ui.model.IParameterElement;
import org.ebayopensource.turmeric.eclipse.resources.ui.model.ServiceFromTemplateWsdlParamModel;
import org.ebayopensource.turmeric.eclipse.resources.ui.model.ServiceFromTemplateWsdlParamModel.Operation;
import org.ebayopensource.turmeric.eclipse.resources.ui.model.ServiceFromTemplateWsdlParamModel.Parameter;
import org.ebayopensource.turmeric.eclipse.resources.ui.model.ServiceFromTemplateWsdlParamModel.TemplateWSDLModel;
import org.ebayopensource.turmeric.eclipse.ui.SOABasePage;
import org.ebayopensource.turmeric.eclipse.ui.SOABaseWizard;
import org.ebayopensource.turmeric.eclipse.utils.collections.ListUtil;
import org.ebayopensource.turmeric.eclipse.utils.plugin.JDTUtil;
import org.ebayopensource.turmeric.eclipse.utils.ui.UIUtil;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.viewers.AbstractTreeViewer;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.DialogCellEditor;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.IWizard;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.swt.widgets.TreeItem;


/**
 * @author yayu
 *
 */
public class ServiceFromNewWSDLAddOperationWizardPage extends SOABasePage {
	
	public static final String COLUMN_NAME = "name";
	public static final String COLUMN_INPUT_PARAM = "input_param";
	public static final String COLUMN_OUTPUT_PARAM = "output_param";
	public static final String[] OPERATION_COLUMN_PROPERTIES = 
	{COLUMN_NAME, COLUMN_INPUT_PARAM, COLUMN_OUTPUT_PARAM};
	
	private List<Operation> operations = new ArrayList<Operation>();
	
	//UI controls
	private TreeViewer operationViewer;
	private Button addOpBtn;
	private Button removeOpBtn;
	private Button upOpBtn;
	private Button downOpBtn;
	
	
	/**
	 * @param pageName
	 * @param title
	 * @param description
	 */
	public ServiceFromNewWSDLAddOperationWizardPage() {
		super("newSOAServiceProjectFromBlankWSDLAddOperationWizardPage", "Service from Template WSDL - Add Operation", 
		"Add operations to the template WSDL");
	}

	/* (non-Javadoc)
	 * @seeorg.ebayopensource.turmeric.eclipse.ui.SOABasePage#getDefaultValue(org.eclipse.swt.widgets.Text)
	 */
	@Override
	public String getDefaultValue(Text text) {
		return SOAProjectConstants.EMPTY_STRING;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets.Composite)
	 */
	public void createControl(Composite parent) {
		try {
			final Composite container = super.createParentControl(parent, 3);
			createOperationViewer(container);
			createControlButtons(container);
			dialogChanged();
		} catch (Exception e) {
			UIUtil.showErrorDialog(e);
			SOALogger.getLogger().error(e);
		}
	}
	
	private TreeViewer createOperationViewer(Composite parent) {
		operationViewer = new TreeViewer(parent, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL 
				| SWT.FULL_SELECTION | SWT.SINGLE);
		GridData data = new GridData(GridData.FILL_BOTH);
		data.horizontalSpan = 2;
		final Tree tree = operationViewer.getTree();
		tree.setLayoutData(data);
		tree.setHeaderVisible(true);
		tree.setLinesVisible(true);
		
		{
			final TableLayout layout = new TableLayout();
			layout.addColumnData(new ColumnWeightData(26, 120, true));
			layout.addColumnData(new ColumnWeightData(37, 130, true));
			layout.addColumnData(new ColumnWeightData(37, 130, true));
			tree.setLayout(layout);
			
			TreeColumn column = new TreeColumn(tree, SWT.LEFT);
			column.setText("Operation Name");
//			column.setImage(Activator.getImageFromRegistry("operation.gif"));
			
			column = new TreeColumn(tree, SWT.LEFT);
			column.setText("Input Parameter");
//			column.setImage(Activator.getImageFromRegistry("parameter_input.gif"));
			
			column = new TreeColumn(tree, SWT.LEFT);
			column.setText("Output Parameter");
//			column.setImage(Activator.getImageFromRegistry("parameter_output.gif"));
		}
		
		operationViewer.setContentProvider(new ITreeContentProvider() {

			public Object[] getChildren(Object parentElement) {
				return getElements(parentElement);
			}

			public Object getParent(Object element) {
				return null;
			}

			public boolean hasChildren(Object element) {
				return element instanceof Collection<?>;
			}

			public Object[] getElements(Object inputElement) {
				if (inputElement instanceof Collection<?>) {
					return ((Collection<?>)inputElement).toArray();
				}
				return Collections.EMPTY_LIST.toArray();
			}

			public void dispose() {
				
			}

			public void inputChanged(Viewer viewer, Object oldInput,
					Object newInput) {
				
			}
			
		});
		
		operationViewer.setLabelProvider(new ITableLabelProvider() {

			public Image getColumnImage(Object element, int columnIndex) {
				return null;
			}

			public String getColumnText(Object element, int columnIndex) {
				if (element instanceof Operation) {
					final Operation op = (Operation) element;
					switch(columnIndex) {
					case 0:
						return op.getName();
					case 1:
						if (op.getInputParameter() != null)
							return op.getInputParameter().getName();
						break;
					case 2:
						return op.getOutputParameter().getName();
					}
				}
				
				return SOAProjectConstants.EMPTY_STRING;
			}

			public void addListener(ILabelProviderListener listener) {
			}

			public void dispose() {
			}

			public boolean isLabelProperty(Object element, String property) {
				return false;
			}

			public void removeListener(ILabelProviderListener listener) {
			}
			
		});
		
		operationViewer.setColumnProperties(OPERATION_COLUMN_PROPERTIES);
		operationViewer.setCellModifier(new ICellModifier() {

			public boolean canModify(Object element, String property) {
				return (element instanceof Operation);
			}

			public Object getValue(Object element, String property) {
				if (element instanceof Operation) {
					final Operation op = (Operation) element;
					if (COLUMN_INPUT_PARAM.equals(property)) {
						if (op.getInputParameter() != null) {
							return op.getInputParameter().getName();
						}
					}if (COLUMN_OUTPUT_PARAM.equals(property)) {
						if (op.getOutputParameter() != null) {
							return op.getOutputParameter().getName();
						}
					}
					else if (COLUMN_NAME.equals(property)) {
						return op.getName();
					}
				}
				
				return SOAProjectConstants.EMPTY_STRING;
			}

			public void modify(Object element, String property, Object value) {
				Object obj = null;
				if (element instanceof TreeItem) {
					final TreeItem item = (TreeItem)element;
					if (item.getData() instanceof Operation) {
						final Operation op = (Operation) item.getData();
						if (COLUMN_NAME.equals(property)) {
							if (op.getName().equals(value) == false) {
								op.setName(String.valueOf(value));
								if (op.getInputParameter() != null) {
									op.getInputParameter().setName(
											op.getName() + SOAProjectConstants.PARAMETER_INPUT_SUFFIX);
								}
								if (op.getOutputParameter() != null) {
									op.getOutputParameter().setName(
											op.getName() + SOAProjectConstants.PARAMETER_OUTPUT_SUFFIX);
								}
								obj = op;
							}
						}
					}
				}
				
				if (obj != null) {
					operationViewer.refresh(obj);
					dialogChanged();
				}
			}
			
		});
		operationViewer.setCellEditors(new CellEditor[]{
				new TextCellEditor(tree),
				new SOADialogCellEditor(tree), new SOADialogCellEditor(tree, false)
				});
		
		operationViewer.setInput(getInputObject());
		
		operationViewer.addSelectionChangedListener(new ISelectionChangedListener() {

			public void selectionChanged(SelectionChangedEvent event) {
				if (event.getSelection().isEmpty() == false) {
					final TemplateWSDLModel model = getSelectedModel(event.getSelection());
					resetButtons(model);
				}
			}
		});
		
		operationViewer.setAutoExpandLevel(AbstractTreeViewer.ALL_LEVELS);
		return operationViewer;
	}
	
	private void resetButtons(TemplateWSDLModel model) {
		final boolean isOperation = model instanceof Operation;
		removeOpBtn.setEnabled(isOperation);
		upOpBtn.setEnabled(false);
		downOpBtn.setEnabled(false);
		final int index = operations.indexOf(model);
		if( index >= 0) {
			upOpBtn.setEnabled(index > 0);
			downOpBtn.setEnabled(index + 1 < operations.size());
		}
		
	}
	
	private TemplateWSDLModel getSelectedModel(ISelection sel) {
		IStructuredSelection selection = null; 
		if (sel == null && this.operationViewer != null) {
			if (operationViewer.getSelection().isEmpty() == false
					&& operationViewer.getSelection() instanceof IStructuredSelection) {
				selection = (IStructuredSelection) operationViewer.getSelection();
				
			}
		} else if (sel instanceof IStructuredSelection) {
			selection = (IStructuredSelection)sel;
		}

		if (selection != null && selection.getFirstElement() instanceof TemplateWSDLModel) {
			return (TemplateWSDLModel)selection.getFirstElement();
		}
		return null;
	}
	
	@Override
	public String getHelpContextID() {
		return GlobalRepositorySystem.instanceOf().getActiveRepositorySystem()
		.getHelpProvider().getHelpContextID(
				ISOAHelpProvider.PAGE_CREATE_SERVICE_FROM_TEMPLATE_WSDL);
	}
	
	@Override
	protected boolean dialogChanged() {
		if(super.dialogChanged() == false)
			return false;
		if (getOperations().isEmpty()) {
			updateStatus(operationViewer.getControl(), 
					"At least one operation must be added");
			return false;
		}
		final Set<String> opNames = new HashSet<String>();
		for (final Operation op : getOperations()) {
			final String opName = op.getName();
			if (StringUtils.isBlank(String.valueOf(opName).trim())) {
				updateStatus(operationViewer.getControl(), 
						"Operation name must not be empty");
				return false;
			}
			else if (opNames.contains(opName) == true) {
				updateStatus(operationViewer.getControl(), 
						"Duplicate operation name: " + opName);
				return false;
			}
			
			final IStatus status = JDTUtil.validateMethodName(opName);
			if (status.isOK() == false) {
				updateStatus(operationViewer.getControl(), 
						"'" + opName + "' is not a valid operation name");
				return false;
			}
			opNames.add(opName);
		}
		return true;
	}
	
	private void createControlButtons(Composite parent) {
		final Composite container = new Composite(parent, SWT.NONE);
		container.setLayoutData(new GridData(GridData.FILL_VERTICAL));
		container.setLayout(new GridLayout(1, true));
		final GridData data = new GridData(GridData.FILL_HORIZONTAL);
		data.widthHint = 100;
		
		{
			addOpBtn = new Button(container, SWT.PUSH);
			addOpBtn.setText("Add &Operation");
			addOpBtn.setLayoutData(data);
			addOpBtn.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					operations.add(ServiceFromTemplateWsdlParamModel.createOperation("newOperation"));
					operationViewer.refresh();
					resetButtons(null);
					dialogChanged();
				}
			});

			removeOpBtn = new Button(container, SWT.PUSH);
			removeOpBtn.setText("&Remove Operation");
			removeOpBtn.setLayoutData(data);
			removeOpBtn.setEnabled(false);
			removeOpBtn.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					final TemplateWSDLModel model = getSelectedModel(null);
					if (model instanceof Operation && operations.remove(model)) {
						operationViewer.refresh();
						resetButtons(null);
						dialogChanged();
					}
				}
			});
			
			new Label(container, SWT.LEFT);
			
			upOpBtn = new Button(container, SWT.PUSH);
			upOpBtn.setText("&Up");
			upOpBtn.setLayoutData(data);
			upOpBtn.setEnabled(false);
			upOpBtn.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					final TemplateWSDLModel model = getSelectedModel(null);
					if (model instanceof Operation) {
						ListUtil.moveOnePositionUp(operations, (Operation)model);
						operationViewer.refresh();
						resetButtons(model);
					}
				}
			});
			
			downOpBtn = new Button(container, SWT.PUSH);
			downOpBtn.setText("&Down");
			downOpBtn.setLayoutData(data);
			downOpBtn.setEnabled(false);
			downOpBtn.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					final TemplateWSDLModel model = getSelectedModel(null);
					if (model instanceof Operation) {
						ListUtil.moveOnePositionDown(operations, (Operation)model);
						operationViewer.refresh();
						resetButtons(model);
					}
				}
			});
		}
	}
	
	public List<Operation> getOperations() {
		return operations;
	}
	
	private Object getInputObject() {
		final Operation op = ServiceFromTemplateWsdlParamModel.createOperation("getVersion");
		op.getOutputParameter().getElements().clear();
		operations.add(op);
		return operations;
	}
	
	private class SOADialogCellEditor extends DialogCellEditor{
		private boolean input;
		
		public SOADialogCellEditor(Composite composite) {
			this(composite, true);
		}

		public SOADialogCellEditor(Composite composite, boolean input) {
			super(composite);
			this.input = input;
		}

		@Override
		protected Object openDialogBox(Control cellEditorWindow) {
			final TemplateWSDLModel model = getSelectedModel(null);
			if (model instanceof Operation) {
				final Operation op = (Operation) model;
				final IWizard soaWizard = new SOABaseWizard() {
					private DataTypeManagementWizardPage datatypePage;
					@Override
					public IWizardPage[] getContentPages() {
						final List<? extends IParameterElement> elements;
						final String paramName;
						if (input) {
							if (op.getInputParameter() != null) {
								paramName = op.getInputParameter().getName();
								elements = op.getInputParameter().getElements();
							} else {
								paramName = op.getName() + SOAProjectConstants.PARAMETER_INPUT_SUFFIX;
								elements = null;
							}
						} else {
							paramName = op.getOutputParameter().getName();
							elements = op.getOutputParameter().getElements();
						}
						int minimumElems = input ? 0 : 1;
						datatypePage = new DataTypeManagementWizardPage(
								"Add Elements to " + paramName, 
								elements, minimumElems);
						final List<IWizardPage> pages = new ArrayList<IWizardPage>();
						pages.add(datatypePage);
						return pages.toArray(new IWizardPage[0]);
					}

					@Override
					public boolean performFinish() {
						if (datatypePage.getElements() != null) {
							if (input) {
								//has param elements
								if (op.getInputParameter() == null) {
									op.setInputParameter(new Parameter(
											op.getName() + SOAProjectConstants.PARAMETER_INPUT_SUFFIX));
								}
								op.getInputParameter().setElements(datatypePage.getElements());
							} else {
								op.getOutputParameter().setElements(datatypePage.getElements());
							}
						}
						return true;
					}
					
					public void setShellAtCenter(Shell activeShell) {
						Rectangle rect = activeShell.getDisplay().getPrimaryMonitor().getBounds();
						int width = 450, height = 400;
						// Calculate the left and top
						int left = (rect.width - width)/2;
						int top = (rect.height - height)/2;

						// Set shell bounds,
				    	activeShell.setBounds(left, top, width, height);
					}

				};
				final WizardDialog dialog = new WizardDialog(UIUtil.getActiveShell(), soaWizard);
				
				if (dialog.open() == Window.OK) {
					operationViewer.refresh(op);
				}
			}
			return "";
		}
	}
}
