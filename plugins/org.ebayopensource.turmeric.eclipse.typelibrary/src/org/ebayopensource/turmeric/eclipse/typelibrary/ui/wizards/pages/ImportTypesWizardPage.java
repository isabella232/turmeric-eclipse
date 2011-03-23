/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.typelibrary.ui.wizards.pages;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.xml.namespace.QName;

import org.apache.commons.lang.StringUtils;
import org.ebayopensource.turmeric.eclipse.buildsystem.core.SOAGlobalRegistryAdapter;
import org.ebayopensource.turmeric.eclipse.exception.validation.ValidationInterruptedException;
import org.ebayopensource.turmeric.eclipse.logging.SOALogger;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.GlobalRepositorySystem;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.ISOAHelpProvider;
import org.ebayopensource.turmeric.eclipse.typelibrary.TypeLibraryActivator;
import org.ebayopensource.turmeric.eclipse.typelibrary.builders.TypeLibraryProjectNature;
import org.ebayopensource.turmeric.eclipse.typelibrary.resources.model.SOATypeLibraryProjectResolver;
import org.ebayopensource.turmeric.eclipse.typelibrary.ui.model.ImportTypeModel;
import org.ebayopensource.turmeric.eclipse.typelibrary.ui.model.TypeParamModel;
import org.ebayopensource.turmeric.eclipse.typelibrary.utils.TypeLibraryUtil;
import org.ebayopensource.turmeric.eclipse.typelibrary.utils.XSDUtils;
import org.ebayopensource.turmeric.eclipse.ui.SOABasePage;
import org.ebayopensource.turmeric.eclipse.ui.components.ProjectSelectionListLabelProvider;
import org.ebayopensource.turmeric.eclipse.utils.plugin.WorkspaceUtil;
import org.ebayopensource.turmeric.eclipse.utils.ui.UIUtil;
import org.ebayopensource.turmeric.eclipse.validator.core.InputObject;
import org.ebayopensource.turmeric.eclipse.validator.utils.common.NameValidator;
import org.ebayopensource.turmeric.eclipse.validator.utils.common.RegExConstants;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.CheckboxCellEditor;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ViewForm;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.ui.dialogs.ElementListSelectionDialog;


public class ImportTypesWizardPage extends SOABasePage {
	private String typeFile;
	private IProject targetTLPrj;
	private TypeTable typeTable;

	private Button existProjBtn;
	private Button newProjBtn;
	private Text sourceFileTxt;
	private Text typeLibraryProjTxt;
	private Text typeNamespaceTxt;
	private Composite typeLibrarySelectCmp;
	final TypeSorter sorter = new TypeSorter();

	public ImportTypesWizardPage(String pageName, String title,
			String description, String tableTitle, IProject targetProject,
			String sourceFile) {
		super(pageName, title, description);
		this.typeFile = sourceFile;
		this.targetTLPrj = targetProject;
		typeTable = new TypeTable(tableTitle, true);
	}

	@Override
	public String getHelpContextID() {
		return GlobalRepositorySystem
				.instanceOf()
				.getActiveRepositorySystem()
				.getHelpProvider()
				.getHelpContextID(
						ISOAHelpProvider.HELPID_SCHEMA_TYPES_IMPORTEXPORT_WIZARD_MARKETPLACE_ID);
	}

	@Override
	public String getDefaultValue(Text text) {
		return "";
	}

	public void createControl(Composite parent) {
		Composite container = super.createParentControl(parent, 3);

		createSourceFileLine(container);
		createTypeLibraryGroupLine(container);
		createTypeTable(container);

		initWizardPage();
	}

	private void reloadTypes() {
		File sourceFile = new File(sourceFileTxt.getText());
		if (sourceFile.exists() == false) {
			return;
		}
		try {
			List<ImportTypeModel> allTypes = XSDUtils.getInstance()
					.extractTypeDefinitionFromFile(sourceFileTxt.getText());

			for (ImportTypeModel type : allTypes) {
				String desc = type.getDescription();
				if (desc == null) {
					desc = "";
				}
				desc = desc.trim();
				type.setDescription(desc);
			}
			typeTable.updateTableContent(allTypes);
		} catch (Exception e) {
			SOALogger.getLogger().error(e);
			typeTable.updateTableContent(new ArrayList<ImportTypeModel>());
			UIUtil.showErrorDialog("Unable to reload types", e);
		}
	}

	private void updateProjectAndNamepsace() {
		if (targetTLPrj == null) {
			typeNamespaceTxt.setText("");
			return;
		}
		typeLibraryProjTxt.setText(targetTLPrj.getName());
		try {
			typeNamespaceTxt.setText(SOATypeLibraryProjectResolver
					.loadTypeLibraryModel(targetTLPrj).getNamespace());
		} catch (Exception e) {
			SOALogger.getLogger().error(e);
			typeNamespaceTxt.setText("Unable to read type library namespace.");
		}
		this.validateAll();
	}

	private void createSourceFileLine(Composite parent) {
		new Label(parent, SWT.LEFT).setText("Source File:");
		sourceFileTxt = new Text(parent, SWT.BORDER);
		GridData gData = new GridData(GridData.FILL_HORIZONTAL);
		sourceFileTxt.setLayoutData(gData);
		sourceFileTxt.setEditable(true);
		UIUtil.decorateControl(this, sourceFileTxt,
				"Source file that is used to extract type library from.");
		Button browseButton = new Button(parent, SWT.PUSH);
		browseButton.setText("&Browse...");
		browseButton.setSelection(false);
		browseButton.addSelectionListener(new SelectionListener() {

			public void widgetDefaultSelected(SelectionEvent e) {
				browseSourceFile();
			}

			public void widgetSelected(SelectionEvent e) {
				browseSourceFile();
			}

		});
	}

	private void createTypeLibraryGroupLine(Composite parent) {
		GridData optionGD = new GridData(GridData.FILL_HORIZONTAL);
		optionGD.horizontalSpan = 3;
		existProjBtn = new Button(parent, SWT.RADIO | SWT.LEFT);
		existProjBtn.setLayoutData(optionGD);
		existProjBtn.setText("Existing Type Library Project");
		existProjBtn.addSelectionListener(new SelectionListener() {

			public void widgetDefaultSelected(SelectionEvent e) {
				updateTypeLibrarySelectionStatus();
			}

			public void widgetSelected(SelectionEvent e) {
				if (existProjBtn.getSelection() == true) {
					updateTypeLibrarySelectionStatus();
				}
			}

		});

		existProjBtn.setSelection(true);

		optionGD = new GridData(GridData.FILL_HORIZONTAL);
		optionGD.horizontalSpan = 3;
		typeLibrarySelectCmp = createTypeLibrarySelection(parent);
		typeLibrarySelectCmp.setLayoutData(optionGD);

		optionGD = new GridData(GridData.FILL_HORIZONTAL);
		optionGD.horizontalSpan = 3;
		newProjBtn = new Button(parent, SWT.RADIO);
		newProjBtn.setLayoutData(optionGD);
		newProjBtn.setText("Create New Target Library");
		newProjBtn.addSelectionListener(new SelectionListener() {

			public void widgetDefaultSelected(SelectionEvent e) {
				updateTypeLibrarySelectionStatus();
			}

			public void widgetSelected(SelectionEvent e) {
				if (existProjBtn.getSelection() == false) {
					updateTypeLibrarySelectionStatus();
				}
			}

		});

	}

	public boolean needNewTypeLibraryCreation() {
		return newProjBtn.getSelection() == true;
	}

	public boolean useExistProject() {
		return existProjBtn.getSelection() == true;
	}

	private void updateTypeLibrarySelectionStatus() {
		enableCompositeChildrens(this.existProjBtn.getSelection());
		validateAll();
	}

	private Composite createTypeLibrarySelection(Composite parent) {
		Composite tlSelect = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout(3, false);
		layout.verticalSpacing = 8;
		layout.marginLeft = 5;
		tlSelect.setLayout(layout);

		new Label(tlSelect, SWT.LEFT).setText("Type Library Project:");
		GridData valueGD = new GridData(GridData.FILL_HORIZONTAL);
		valueGD.horizontalSpan = 1;
		typeLibraryProjTxt = new Text(tlSelect, SWT.BORDER);
		typeLibraryProjTxt.setLayoutData(valueGD);
		typeLibraryProjTxt.setEditable(false);
		UIUtil.decorateControl(this, typeLibraryProjTxt,
				"Target type library project that will accept new types.");

		Button browseTypeLibraryBtn = new Button(tlSelect, SWT.PUSH);
		browseTypeLibraryBtn.setText("&Select...");
		browseTypeLibraryBtn.addSelectionListener(new SelectionListener() {

			public void widgetDefaultSelected(SelectionEvent e) {
				browseTargetTypeLibraryProject();
			}

			public void widgetSelected(SelectionEvent e) {
				browseTargetTypeLibraryProject();
			}

		});
		browseTypeLibraryBtn.setSelection(false);

		valueGD = new GridData(GridData.FILL_HORIZONTAL);
		valueGD.widthHint = 1;
		new Label(tlSelect, SWT.LEFT).setText("Namespace:");
		typeNamespaceTxt = new Text(tlSelect, SWT.BORDER);
		typeNamespaceTxt.setLayoutData(valueGD);
		typeNamespaceTxt.setEditable(false);
		typeNamespaceTxt.setText("");
		UIUtil.decorateControl(this, typeNamespaceTxt,
				"Target type library namespace.");
		return tlSelect;
	}

	private void createTypeTable(Composite parent) {
		Composite tableComposite = typeTable.createTypeTable(parent);
		tableComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
				true, 3, 1));
	}

	private void browseSourceFile() {
		File sourceFile = new File(sourceFileTxt.getText());
		String fileName = UIUtil.fileDialog("Select Source File", "WSDL or XSD file", sourceFile,
				"*.xsd; *.wsdl");
		if (fileName != null) {
			File newSourceFile = new File(fileName);
			sourceFileTxt.setText(fileName);
			if (newSourceFile.isFile() == true) {
				reloadTypes();
			} else {
				UIUtil.showErrorDialog(this.getContainer().getShell(),
						"Invalidated File",
						"Please select a validated WSDL / XSD file.");
			}
		}
	}

	private void browseTargetTypeLibraryProject() {

		ElementListSelectionDialog selectionDialog = new ElementListSelectionDialog(
				getShell(), new ProjectSelectionListLabelProvider()) {

			@Override
			protected Control createDialogArea(Composite parent) {
				final Control control = super.createDialogArea(parent);
				UIUtil
						.getHelpSystem()
						.setHelp(
								control,
								GlobalRepositorySystem
										.instanceOf()
										.getActiveRepositorySystem()
										.getHelpProvider()
										.getHelpContextID(
												ISOAHelpProvider.DIALOG_SELECT_TYPE_LIBRARY));
				return control;
			}
		};
		selectionDialog.setTitle("Select the Type Library Project");
		try {
			final List<IProject> projects = WorkspaceUtil
					.getProjectsByNature(TypeLibraryProjectNature.getTypeLibraryNatureId());
			if (typeLibraryProjTxt != null
					&& StringUtils.isNotBlank(typeLibraryProjTxt.getText())
					&& WorkspaceUtil.getProject(typeLibraryProjTxt.getText())
							.isAccessible()) {
				projects.remove(WorkspaceUtil.getProject(typeLibraryProjTxt
						.getText()));
			}
			selectionDialog.setElements(projects.toArray(new IProject[0]));

		} catch (CoreException e1) {
			SOALogger.getLogger().error(e1);
			return;
		}
		selectionDialog.setBlockOnOpen(true);
		selectionDialog.setMultipleSelection(false);
		if (selectionDialog.open() == Window.OK) {
			if (selectionDialog.getResult() != null
					&& selectionDialog.getResult().length > 0) {
				targetTLPrj = ((IProject) selectionDialog.getResult()[0]);
				dialogChanged();
			}
		}
		updateProjectAndNamepsace();
	}

	public void sourceFileUpdated() {
		this.validateAll();
	}

	public void targetTypeLibraryUpdated() {
		this.validateAll();
	}

	private void initWizardPage() {
		if (typeFile != null) {
			sourceFileTxt.setText(typeFile);
		}

		updateProjectAndNamepsace();

		reloadTypes();

	}

	private void enableCompositeChildrens(boolean enable) {
		Control[] controls = typeLibrarySelectCmp.getChildren();
		if (controls == null) {
			return;
		}
		for (Control control : controls) {
			if (control != null) {
				control.setEnabled(enable);
			}
		}
	}

	private void validateAll() {
		// Check source file
		String filepath = sourceFileTxt.getText();
		File file = new File(filepath);
		if (file.isFile() == false) {
			String errMsg = "Please select a validated WSDL/XSD file";
			super.updateStatus(sourceFileTxt, errMsg);
			return;
		}
		// check tl project name if use existing tl project.
		String projectName = typeLibraryProjTxt.getText();
		if ((existProjBtn.getSelection() == true)
				&& projectName.isEmpty() == true) {
			String errMsg = "Please select a validated type library";
			super.updateStatus(typeLibraryProjTxt, errMsg);
			return;
		}

		String errMsg = typeTable.validateSelectedTypes();
		if (errMsg == null) {
			super.updateStatus(null, errMsg);
		} else {
			super.updateStatus(typeTable.getTable(), errMsg);
		}

	}

	public String getSelectedTypeLibraryName() {
		return typeLibraryProjTxt.getText();
	}

	public String getNamespace() {
		return typeNamespaceTxt.getText();
	}
	
	public List<TypeParamModel> getSelectedTypeModels() {
		List<ImportTypeModel> models = getSelectedTypeImportModels();
		List<TypeParamModel> typeModels = new ArrayList<TypeParamModel>();
		for (ImportTypeModel model : models) {
			if (model.isSelected() == true) {
				typeModels.add(model.getTypeModel());
			}
		}
		return typeModels;
	}

	public List<ImportTypeModel> getSelectedTypeImportModels() {
		return typeTable.getSelectedType();
	}

	private static final Image CHECKED = TypeLibraryActivator
			.getImageFromRegistry("icons/select.gif").createImage();
	private static final Image UNCHECKED = TypeLibraryActivator
			.getImageFromRegistry("icons/unselect.gif").createImage();
	private static final Image ERROR = TypeLibraryActivator
			.getImageFromRegistry("icons/error.gif").createImage();

	class TypeTable {
		private List<ImportTypeModel> typeList = new ArrayList<ImportTypeModel>();

		private Table typeTable;
		private TableViewer typeTableView;
		private TableColumn nsTableCol;

		private String[] titles = { "Include", "Type Name",
				"Original Namespace", "Description" };

		private String groupTitle;
		private boolean showNS;

		public TypeTable(String groupTitle, boolean showNS) {
			this.groupTitle = groupTitle;
			this.showNS = showNS;
		}

		public List<ImportTypeModel> getSelectedType() {
			List<ImportTypeModel> selectedTypeList = new ArrayList<ImportTypeModel>();
			for (ImportTypeModel type : typeList) {
				if (type.isSelected() == true) {
					selectedTypeList.add(type);
				}
			}
			return selectedTypeList;
		}

		public Composite createTypeTable(Composite parent) {
			Composite tableComposite = new Composite(parent, SWT.NONE);
			tableComposite.setLayout(new GridLayout(1, false));
			Label tableIntro = new Label(tableComposite, SWT.LEFT);
			tableIntro.setText("    ");
			tableIntro.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
					false, 1, 1));

			Group tableGroup = new Group(tableComposite, SWT.NONE);
			tableGroup.setText(groupTitle);
			tableGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
					true, 1, 1));
			FillLayout layout = new FillLayout();
			layout.marginWidth = 2;
			layout.marginHeight = 5;
			tableGroup.setLayout(layout);

			ViewForm viewForm = new ViewForm(tableGroup, SWT.NONE);

			ToolBar toolBar = new ToolBar(viewForm, SWT.FLAT);
			ToolItem selectAll = new ToolItem(toolBar, SWT.PUSH);
			selectAll.setText("Select All");
			selectAll.addSelectionListener(new SelectionListener() {

				public void widgetDefaultSelected(SelectionEvent e) {
					selectAll(true);
				}

				public void widgetSelected(SelectionEvent e) {
					selectAll(true);
				}

			});

			ToolItem diselectAll = new ToolItem(toolBar, SWT.PUSH);
			diselectAll.setText("Deselect All");
			diselectAll.addSelectionListener(new SelectionListener() {

				public void widgetDefaultSelected(SelectionEvent e) {
					selectAll(false);
				}

				public void widgetSelected(SelectionEvent e) {
					selectAll(false);
				}

			});

			typeTableView = new TableViewer(viewForm, SWT.MULTI
					| SWT.FULL_SELECTION);

			typeTable = typeTableView.getTable();
			typeTable.setLinesVisible(true);
			typeTable.setHeaderVisible(true);

			viewForm.setContent(typeTable);
			viewForm.setTopLeft(toolBar);

			for (int i = 0; i < titles.length; i++) {
				final int index = i;
				final TableColumn column = new TableColumn(typeTable, SWT.NONE);
				column.setText(titles[i]);
				column.addSelectionListener(new SelectionAdapter() {
					@Override
					public void widgetSelected(SelectionEvent e) {
						typeTableView.setSorter(sorter);
						int dir = typeTableView.getTable().getSortDirection();
						if (typeTableView.getTable().getSortColumn() == column) {
							dir = (dir == SWT.UP) ? SWT.DOWN : SWT.UP;
						} else {
							dir = SWT.DOWN;
						}
						typeTable.setSortDirection(dir);
						typeTable.setSortColumn(column);
						sorter.setSortParameters(index, dir);
						refreshTypeTable(false);
					}
				});
				column.setWidth(150);
			}
			typeTable.pack();
			nsTableCol = typeTable.getColumn(2);
			nsTableCol.setWidth(0);
			nsTableCol.setResizable(false);

			typeTableView.setContentProvider(new TypeContentProvider());
			typeTableView.setLabelProvider(new TypeLabelProvider());
			typeTableView.setInput(typeList);
			typeTableView.setColumnProperties(titles);
			typeTableView.setCellModifier(new TypeModifier());
			CellEditor[] cellEditors = new CellEditor[4];
			cellEditors[0] = new CheckboxCellEditor(typeTable);
			cellEditors[1] = new TextCellEditor(typeTable);
			cellEditors[2] = new TextCellEditor(typeTable);
			cellEditors[3] = new TextCellEditor(typeTable);
			typeTableView.setCellEditors(cellEditors);

			return tableComposite;
		}

		private void refreshTypeTable(boolean repackColumns) {
			Object data = null;
			TableItem[] sel = typeTable.getSelection();
			if (sel != null && sel.length > 0) {
				data = sel[0].getData();
			}
			typeTableView.refresh();
			int itemLen = typeTable.getItemCount();
			if (data != null) {
				for (int i = 0; i < itemLen; i++) {
					TableItem item = typeTable.getItem(i);
					if (data == item.getData()) {
						typeTable.setSelection(item);
						break;
					}
				}
			}
			if (repackColumns == false) {
				return;
			}
			for (TableColumn column : typeTable.getColumns()) {
				if (showNS == false && column == nsTableCol) {
					continue;
				} else {
					column.pack();
				}
			}
		}

		public Table getTable() {
			return typeTable;
		}

		public void updateTableContent(List<ImportTypeModel> types) {
			typeList.clear();
			typeList.addAll(types);
			validateAll();
			this.refreshTypeTable(true);
		}

		public List<ImportTypeModel> getTypes() {
			return typeList;
		}

		private void selectAll(boolean select) {
			for (ImportTypeModel type : typeList) {
				type.setSelected(select);
			}
			validateAll();
			// model changes, call refresh to update view
			refreshTypeTable(false);
		}

		class TypeContentProvider implements IStructuredContentProvider {

			public Object[] getElements(Object inputElement) {
				return typeList.toArray();
			}

			public void dispose() {

			}

			public void inputChanged(Viewer viewer, Object oldInput,
					Object newInput) {

			}

		}

		class TypeLabelProvider extends LabelProvider implements
				ITableLabelProvider {

			public Image getColumnImage(Object element, int columnIndex) {

				if ((element instanceof ImportTypeModel) == false) {
					return null;
				}
				ImportTypeModel type = (ImportTypeModel) element;

				switch (columnIndex) {
				case 0:
					return type.isSelected() ? CHECKED : UNCHECKED;
				case 1:
					return type.isSelected() && type.isError() ? ERROR : null;
				default:
					return null;
				}
			}

			public String getColumnText(Object element, int columnIndex) {
				if (element == null) {
					return "";
				}
				if ((element instanceof ImportTypeModel) == false) {
					return "";
				}
				ImportTypeModel type = (ImportTypeModel) element;
				switch (columnIndex) {
				case 0:
					return "";
				case 1:
					return type.getName();
				case 2:
					return type.getNamespace();
				case 3:
					String desc = type.getDescription();
					desc = StringUtils.replace(desc, "\r\n", " ");
					desc = StringUtils.replace(desc, "\n", " ");
					return desc;
				}
				return "";
			}
		}

		class TypeModifier implements ICellModifier {

			public boolean canModify(Object element, String property) {
				if (property == null
						|| ((element instanceof ImportTypeModel) == false)) {
					return false;
				}
				// namespace is not editble
				if (titles[2].equals(property)) {
					return false;
				}
				return true;
			}

			public Object getValue(Object element, String property) {
				if (property == null
						|| ((element instanceof ImportTypeModel) == false)) {
					return null;
				}
				ImportTypeModel type = (ImportTypeModel) element;
				if (titles[0].equals(property)) {
					return type.isSelected();
				} else if (titles[1].equals(property)) {
					return type.getName();
				} else if (titles[2].equals(property)) {
					return type.getTypeModel();
				} else if (titles[3].equals(property)) {
					return type.getDescription();
				}

				return null;
			}
			
			private void selectAllType(ImportTypeModel model){
				List<ImportTypeModel> unSelectedDep = new ArrayList<ImportTypeModel>();
				List<ImportTypeModel> unSelectedDepTemp = new ArrayList<ImportTypeModel>();
				// current type is the root.
				unSelectedDep.add(model);
				while(unSelectedDep.size() > 0){
					// select type
					for(ImportTypeModel dep : unSelectedDep){
						dep.setSelected(true);
					}
					// find dep of all type. if the type dep is already selected, skip it.
					for(ImportTypeModel dep : unSelectedDep){
						for(ImportTypeModel depType : dep.getDependencies().values()){
							if(depType.isSelected() == false){
								unSelectedDepTemp.add(depType);
							}
						}
					}
					unSelectedDep.clear();
					unSelectedDep.addAll(unSelectedDepTemp);
					unSelectedDepTemp.clear();
				}
			}

			public void modify(Object element, String property, Object value) {
				if (property == null || element == null) {
					return;
				}
				ImportTypeModel type = (ImportTypeModel) ((TableItem) element)
						.getData();
				if (titles[0].equals(property)) {
					boolean selection = Boolean.valueOf(value.toString());
					if (selection == true) {
						selectAllType(type);
					} else {
						type.setSelected(false);
					}
				} else if (titles[1].equals(property)) {
					type.setName(value.toString());
				} else if (titles[2].equals(property)) {
					// type.type = value.toString();
				} else if (titles[3].equals(property)) {
					String descripion = value.toString();
					descripion = StringUtils.remove(descripion, '\r');
					type.setDescription(descripion);
				}
				validateAll();
				// do a refresh so that if type name is duplicated, it will
				// becomes red.
				refreshTypeTable(false);
			}
		}

		protected IStatus validateName(String typeName, String errorMessage) {
			final InputObject inputObject = new InputObject(typeName,
					RegExConstants.PROJECT_NAME_EXP, errorMessage);
			try {
				IStatus validationModel = NameValidator.getInstance().validate(
						inputObject);
				return validationModel;
			} catch (ValidationInterruptedException e) {
				processException(e);
			}
			return Status.OK_STATUS;
		}

		public String validateSelectedTypes() {

			// clean all errors
			for (ImportTypeModel type : typeList) {
				type.setError(false);
			}

			// find if any unsupported type is selected
			StringBuffer unsupportedType = new StringBuffer();
			for (ImportTypeModel type : typeList) {
				if (type.isSelected() == false) {
					continue;
				}
				if (type.isUnSupported() == true) {
					unsupportedType.append(type.getUnSupportedReason());
					type.setError(true);
				}
			}

			if (unsupportedType.length() > 0) {
				return unsupportedType.toString();
			}

			// find if at least one type is selected. Validate type name
			int typeCount = 0;
			StringBuffer nameValidation = new StringBuffer();
			StringBuffer depValidation = new StringBuffer();

			for (ImportTypeModel type : typeList) {
				if (type.isSelected() == false) {
					continue;
				}
				typeCount++;

				IStatus validationModel = validateName(type.getName(),
						"The name [" + type.getName()
								+ "] is not valid against the pattern \""
								+ RegExConstants.PROJECT_NAME_EXP + "\"");
				if (validationModel.isOK() == false) {
					nameValidation.append(type.getName() + ", ");
					type.setError(true);
				}

				for (ImportTypeModel depModel : type.getDependencies().values()) {
					if (depModel.isSelected() == false) {
						depModel.setError(true);
						depValidation.append(type.getName() + " depends on "
								+ depModel.getName() + ". ");
					}
				}
			}

			if (nameValidation.length() > 0) {
				String error = "The name ["
						+ nameValidation.substring(0,
								nameValidation.length() - 2)
						+ "] is not valid against the pattern \""
						+ RegExConstants.PROJECT_NAME_EXP + "\"";
				return error;
			}

			if (depValidation.length() > 0) {
				String error = "Type Dependency validation failed: "
						+ depValidation;
				return error;
			}

			if (typeCount == 0) {
				return "Please select at least one type to continue.";
			}

			boolean hasEmptyTypeName = false;

			// find duplicate names from existing type library project. find
			// empty type names
			StringBuffer dupNamesInTypeRepo = new StringBuffer();
			if (useExistProject() == true) {
				IProject project = targetTLPrj;
				for (ImportTypeModel type : typeList) {
					if (type.isSelected() == false) {
						continue;
					}

					String typeName = type.getName();
					if (StringUtils.isBlank(typeName) == true) {
						hasEmptyTypeName = true;
						continue;
					}

					boolean hasXSDFile = project.getFile(
							TypeLibraryUtil.getXsdFileLocation(typeName,
									project)).exists();
					boolean hasType = true;

					try {
						hasType = SOAGlobalRegistryAdapter.getInstance().getGlobalRegistry()
								.getType(
										new QName(TypeLibraryUtil
												.getNameSpace(targetTLPrj
														.getName()), typeName)) != null;
					} catch (Exception e) {
						e.printStackTrace();
					}
					if (hasXSDFile == true || hasType == true) {
						dupNamesInTypeRepo.append(type.getName() + ", ");
						type.setError(true);
					}
				}
			}

			// find duplicate names from type list
			StringBuffer dupNamesInTypeTable = new StringBuffer();
			Map<String, ImportTypeModel> types = new ConcurrentHashMap<String, ImportTypeModel>();
			for (ImportTypeModel type : typeList) {
				if (type.isSelected() == false) {
					continue;
				}
				if (types.containsKey(type.getName())) {
					types.get(type.getName()).setError(true);
					type.setError(true);
					dupNamesInTypeTable.append(type.getName() + ", ");
				} else {
					types.put(type.getName(), type);
				}
			}
			refreshTypeTable(false);

			StringBuffer dupErrMsg = new StringBuffer();
			if (dupNamesInTypeRepo.length() > 0) {
				dupErrMsg
						.append("Selected types duplicated with types in the Type Repository ["
								+ dupNamesInTypeRepo.substring(0,
										dupNamesInTypeRepo.length() - 2)
								+ "]. ");
			}

			if (dupNamesInTypeTable.length() > 0) {
				dupErrMsg.append("Duplicated type selected ["
						+ dupNamesInTypeTable.substring(0, dupNamesInTypeTable
								.length() - 2) + "].");
			}

			if (dupErrMsg.length() > 0) {
				return dupErrMsg.toString();
			}

			if (hasEmptyTypeName == true) {
				return "No Type Selected";
			}

			return null;
		}

	}

	class TypeSorter extends ViewerSorter {

		private int columnIndex;
		private int direction = SWT.UP;

		public TypeSorter() {
			this.columnIndex = 0;
			direction = SWT.UP;
		}

		public void setSortParameters(int column, int direction) {
			columnIndex = column;
			this.direction = direction;
		}

		@Override
		public int compare(Viewer viewer, Object e1, Object e2) {
			ImportTypeModel p1 = (ImportTypeModel) e1;
			ImportTypeModel p2 = (ImportTypeModel) e2;
			int rc = 0;
			switch (columnIndex) {

			case 0:
				rc = p1.isSelected() == p2.isSelected() ? 0
						: (p1.isSelected() ? 1 : -1);
				break;
			case 1:
				rc = p1.getName().toUpperCase().compareTo(
						p2.getName().toUpperCase());
				break;
			case 2:
				rc = p1.getNamespace().toUpperCase().compareTo(
						p2.getNamespace().toUpperCase());
				break;
			case 3:
				rc = p1.getDescription().toUpperCase().compareTo(
						p2.getDescription().toUpperCase());
				break;
			default:
				rc = 0;
			}
			// If descending order, flip the direction
			if (direction == SWT.DOWN) {
				rc = -rc;
			}
			return rc;
		}

	}

}
