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
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.namespace.QName;

import org.apache.commons.lang.StringUtils;
import org.ebayopensource.turmeric.eclipse.core.resources.constants.SOAProjectConstants;
import org.ebayopensource.turmeric.eclipse.exception.validation.ValidationInterruptedException;
import org.ebayopensource.turmeric.eclipse.logging.SOALogger;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.GlobalRepositorySystem;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.ISOAHelpProvider;
import org.ebayopensource.turmeric.eclipse.typelibrary.TypeLibraryActivator;
import org.ebayopensource.turmeric.eclipse.typelibrary.builders.TypeLibraryProjectNature;
import org.ebayopensource.turmeric.eclipse.typelibrary.resources.model.SOATypeLibraryProjectResolver;
import org.ebayopensource.turmeric.eclipse.typelibrary.utils.TypeLibraryUtil;
import org.ebayopensource.turmeric.eclipse.typelibrary.utils.importtypes.ImportTypesFromWSDLParser;
import org.ebayopensource.turmeric.eclipse.typelibrary.utils.importtypes.ImportTypesFromXSDParser;
import org.ebayopensource.turmeric.eclipse.typelibrary.utils.importtypes.TypeModel;
import org.ebayopensource.turmeric.eclipse.ui.SOABasePage;
import org.ebayopensource.turmeric.eclipse.ui.components.ProjectSelectionListLabelProvider;
import org.ebayopensource.turmeric.eclipse.ui.dialogs.DetailMessageDialog;
import org.ebayopensource.turmeric.eclipse.ui.monitor.typelib.SOAGlobalRegistryAdapter;
import org.ebayopensource.turmeric.eclipse.utils.plugin.WorkspaceUtil;
import org.ebayopensource.turmeric.eclipse.utils.ui.UIUtil;
import org.ebayopensource.turmeric.eclipse.validator.core.InputObject;
import org.ebayopensource.turmeric.eclipse.validator.utils.common.NameValidator;
import org.ebayopensource.turmeric.eclipse.validator.utils.common.RegExConstants;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.resource.ImageDescriptor;
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
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
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
	private Action viewTypeContentAction;

	private String globalErrMsg = null;

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

			Collection<TypeModel> types = loadTypesFromFile();

			if (types == null) {
				return;
			}

			typeTable.updateTableContent(types);

			updateTypeLibNameAndTypeNamespace();
		} catch (Exception e) {
			SOALogger.getLogger().error(e);
			typeTable.updateTableContent(new ArrayList<TypeModel>());
			UIUtil.showErrorDialog("Unable to reload types", e);
		} catch (Throwable ex) {
			ex.printStackTrace();
		}
	}

	public Collection<TypeModel> loadTypesFromFile() {
		Collection<TypeModel> types = null;
		try {
			String path = sourceFileTxt.getText();
			String sourcePath = path.toLowerCase();
			ReloadTypesRunnable reload = new ReloadTypesRunnable(sourcePath);
			new ProgressMonitorDialog(this.getShell())
					.run(false, false, reload);
			types = reload.getTypes();
			int refferedTLTypeCount = 0;
			if (reload.getReferedTypes() != null) {
				refferedTLTypeCount = reload.getReferedTypes().size();
			}
			if (types == null || types.size() == 0) {
				StringBuilder msg = new StringBuilder();
				msg.append("No Type found in file \"" + path + "\".");
				if (refferedTLTypeCount > 0) {
					msg.append(" Found " + refferedTLTypeCount
							+ " types that refer "
							+ "to existing types in type registry.");
				}
				MessageDialog.openWarning(this.getShell(), "No Type to Import",
						msg.toString());
			}
		} catch (Exception e) {
			MessageDialog.openError(this.getShell(), "Exception", e
					.getMessage());
		}
		return types;
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
		updateTypeLibNameAndTypeNamespace();
		this.validateAll();
	}

	private void createSourceFileLine(Composite parent) {

		new Label(parent, SWT.LEFT).setText("Source File:");
		sourceFileTxt = new Text(parent, SWT.BORDER);
		GridData gDatda = new GridData(GridData.FILL_HORIZONTAL);
		sourceFileTxt.setLayoutData(gDatda);
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
				// try {
				// CutXSDFromWSDL.main(null);
				// } catch (SAXException e1) {
				// // TODO Auto-generated catch block
				// e1.printStackTrace();
				// } catch (IOException e1) {
				// // TODO Auto-generated catch block
				// e1.printStackTrace();
				// } catch (ParserConfigurationException e1) {
				// // TODO Auto-generated catch block
				// e1.printStackTrace();
				// }
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
		updateTypeLibNameAndTypeNamespace();
		validateAll();
	}

	private void updateTypeLibNameAndTypeNamespace() {
		String typeLibName = typeLibraryProjTxt.getText();
		String namespace = typeNamespaceTxt.getText();
		if (existProjBtn.getSelection() == false) {
			typeLibName = "NewTypeLibrary";
			namespace = "http://www.ebay.com/importtypes/temp";
		}
		typeTable.updateTypeLibAndNamespace(typeLibName, namespace);
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
		viewTypeContentAction = new Action() {
			public void run() {
				List<TypeModel> selection = typeTable.getUserSelection();
				if (selection.size() == 0) {
					return;
				}
				typeTable.showTypeContent(selection);
			}
		};
		viewTypeContentAction.setText("View Type Content");
		viewTypeContentAction.setToolTipText("View Type Content");
		viewTypeContentAction
				.setImageDescriptor(TOOLBAR_SCHEMA_CONTENT_SELECTED_DES);

		MenuManager menuMgr = new MenuManager("#PopupMenu");
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(new IMenuListener() {

			@Override
			public void menuAboutToShow(IMenuManager manager) {
				manager.add(viewTypeContentAction);
			}
		});
		Menu menu = menuMgr.createContextMenu(typeTable.getTable());
		typeTable.getTable().setMenu(menu);
		// getSite().registerContextMenu(menuMgr, viewer);
	}

	private void browseSourceFile() {
		File sourceFile = new File(sourceFileTxt.getText());
		String fileName = UIUtil.fileDialog("Select Source File",
				"WSDL or XSD file", sourceFile, "*.xsd; *.wsdl");
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
					.getProjectsByNature(TypeLibraryProjectNature
							.getTypeLibraryNatureId());
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

		globalErrMsg = typeTable.validateSelectedTypes();
		if (globalErrMsg == null) {
			super.updateStatus(null, globalErrMsg);
		} else {
			super.updateStatus(typeTable.getTable(), globalErrMsg);
		}

	}

	public String getSelectedTypeLibraryName() {
		return typeLibraryProjTxt.getText();
	}

	public String getNamespace() {
		return typeNamespaceTxt.getText();
	}

	public List<TypeModel> getSelectedTypeModels() {
		List<TypeModel> models = getSelectedTypeImportModels();
		List<TypeModel> typeModels = new ArrayList<TypeModel>();
		for (TypeModel model : models) {
			if (model.isSelected() == true) {
				typeModels.add(model);
			}
		}
		return typeModels;
	}

	public List<TypeModel> getSelectedTypeImportModels() {
		return typeTable.getSelectedType();
	}

	private static final Image CHECKED = TypeLibraryActivator
			.getImageFromRegistry("icons/select.gif").createImage();
	private static final Image UNCHECKED = TypeLibraryActivator
			.getImageFromRegistry("icons/unselect.gif").createImage();
	private static final Image ERROR = TypeLibraryActivator
			.getImageFromRegistry("icons/error.gif").createImage();
	private static final Image WARNING = TypeLibraryActivator
			.getImageFromRegistry("icons/warning.gif").createImage();

	private static final Image TOOLBAR_SELECT_ALL = TypeLibraryActivator
			.getImageFromRegistry("icons/select_all.gif").createImage();
	private static final Image TOOLBAR_DISSELECT_ALL = TypeLibraryActivator
			.getImageFromRegistry("icons/unselect_all.gif").createImage();
	private static final Image TOOLBAR_SELECT_ALL_CORRECT = TypeLibraryActivator
			.getImageFromRegistry("icons/select_correct.gif").createImage();

	private static final Image TOOLBAR_ERROR_REPORT = TypeLibraryActivator
			.getImageFromRegistry("icons/error_report.gif").createImage();
	private static final ImageDescriptor TOOLBAR_SCHEMA_CONTENT_SELECTED_DES = TypeLibraryActivator
			.getImageFromRegistry("icons/schema_content_selected.gif");
	private static final Image TOOLBAR_SCHEMA_CONTENT_SELECTED = TOOLBAR_SCHEMA_CONTENT_SELECTED_DES
			.createImage();
	private static final Image TOOLBAR_SCHEMA_CONTENT_IMPORT = TypeLibraryActivator
			.getImageFromRegistry("icons/schema_content_import.gif")
			.createImage();

	private static final Image IMPORT_COL = TypeLibraryActivator
			.getImageFromRegistry("icons/import_col.gif").createImage();
	private static final Image TYPENAME_COL = TypeLibraryActivator
			.getImageFromRegistry("icons/typename_col.gif").createImage();
	private static final Image NAMESPACE_COL = TypeLibraryActivator
			.getImageFromRegistry("icons/namespace_col.gif").createImage();
	private static final Image DOCUMENTATION_COL = TypeLibraryActivator
			.getImageFromRegistry("icons/documentation_col.gif").createImage();

	class TypeTable {
		private List<TypeModel> typeList = new ArrayList<TypeModel>();

		private Table typeTable;
		private TableViewer typeTableView;
		private TableColumn nsTableCol;

		private String[] titles = { "", "Type Name", "Original Namespace",
				"Description" };

		private Image[] colImage = { IMPORT_COL, TYPENAME_COL, NAMESPACE_COL,
				DOCUMENTATION_COL };

		private String groupTitle;
		private boolean showNS;

		public TypeTable(String groupTitle, boolean showNS) {
			this.groupTitle = groupTitle;
			this.showNS = showNS;
		}

		public List<TypeModel> getSelectedType() {
			List<TypeModel> selectedTypeList = new ArrayList<TypeModel>();
			for (TypeModel type : typeList) {
				if (type.isSelected() == true) {
					selectedTypeList.add(type);
				}
			}
			return selectedTypeList;
		}

		private List<TypeModel> getUserSelection() {
			List<TypeModel> selections = new ArrayList<TypeModel>();
			for (TableItem item : typeTable.getSelection()) {
				Object obj = item.getData();
				if (obj instanceof TypeModel) {
					selections.add((TypeModel) obj);
				}
			}
			return selections;
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
			selectAll.setImage(TOOLBAR_SELECT_ALL);
			selectAll.setToolTipText("Choose all types");
			selectAll.addSelectionListener(new SelectionListener() {

				public void widgetDefaultSelected(SelectionEvent e) {
					selectAll(true);
				}

				public void widgetSelected(SelectionEvent e) {
					selectAll(true);
				}

			});

			ToolItem diselectAll = new ToolItem(toolBar, SWT.PUSH);
			diselectAll.setImage(TOOLBAR_DISSELECT_ALL);
			diselectAll.setToolTipText("Un-choose all types");
			diselectAll.addSelectionListener(new SelectionListener() {

				public void widgetDefaultSelected(SelectionEvent e) {
					selectAll(false);
				}

				public void widgetSelected(SelectionEvent e) {
					selectAll(false);
				}

			});

			new ToolItem(toolBar, SWT.SEPARATOR);

			ToolItem selectCorrect = new ToolItem(toolBar, SWT.PUSH);
			selectCorrect.setImage(TOOLBAR_SELECT_ALL_CORRECT);
			selectCorrect
					.setToolTipText("Choose all types"
							+ " that have no error or warning. Notice that this operation "
							+ "might cause dependency error "
							+ "if a dependency type has error or warnings.");
			selectCorrect.addSelectionListener(new SelectionListener() {

				public void widgetDefaultSelected(SelectionEvent e) {
					selectAllCorrect();
				}

				public void widgetSelected(SelectionEvent e) {
					selectAllCorrect();
				}

			});

			new ToolItem(toolBar, SWT.SEPARATOR);

			ToolItem errorReport = new ToolItem(toolBar, SWT.PUSH);
			errorReport.setImage(TOOLBAR_ERROR_REPORT);
			errorReport.setToolTipText("View error report");
			errorReport.addSelectionListener(new SelectionListener() {

				public void widgetDefaultSelected(SelectionEvent e) {
					showErrprReport();
				}

				public void widgetSelected(SelectionEvent e) {
					showErrprReport();
				}

			});

			new ToolItem(toolBar, SWT.SEPARATOR);

			ToolItem schemaContentImport = new ToolItem(toolBar, SWT.PUSH);
			schemaContentImport.setImage(TOOLBAR_SCHEMA_CONTENT_IMPORT);
			schemaContentImport.setToolTipText("View type schema content "
					+ "that chosen to be imported. "
					+ "Please check the first column to choose a type.");
			schemaContentImport.addSelectionListener(new SelectionListener() {

				public void widgetDefaultSelected(SelectionEvent e) {
					widgetSelected(e);
				}

				public void widgetSelected(SelectionEvent e) {
					List<TypeModel> selection = getSelectedType();
					if (selection.size() == 0) {
						MessageDialog.openWarning(ImportTypesWizardPage.this
								.getContainer().getShell(), "No Type chosen",
								"No Type chosen in the table. Please check "
										+ "the first column to choose a type.");
						return;
					}
					showTypeContent(selection);
				}

			});

			ToolItem schemaContentSelected = new ToolItem(toolBar, SWT.PUSH);
			schemaContentSelected.setImage(TOOLBAR_SCHEMA_CONTENT_SELECTED);
			schemaContentSelected.setToolTipText("View type schema content "
					+ "that selected in table. " + "Please Use Ctrl or Shift "
					+ "to select multi types in the table.");
			schemaContentSelected.addSelectionListener(new SelectionListener() {

				public void widgetDefaultSelected(SelectionEvent e) {
					widgetSelected(e);
				}

				public void widgetSelected(SelectionEvent e) {
					List<TypeModel> selection = getUserSelection();
					if (selection.size() == 0) {
						MessageDialog.openWarning(ImportTypesWizardPage.this
								.getContainer().getShell(), "No Type Selected",
								"No Type selected in the table. Please Use Ctrl "
										+ "or Shift to select multi "
										+ "types in the table.");
						return;
					}
					showTypeContent(selection);
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
				column.setImage(colImage[i]);
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
			typeTable.getColumn(0).setWidth(50);
			nsTableCol = typeTable.getColumn(2);
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
			typeTable.addListener(SWT.MouseDoubleClick, new Listener() {
				public void handleEvent(Event event) {
					// find clicked on which item
					Point pt = new Point(event.x, event.y);
					TableItem item = typeTable.getItem(pt);
					if (item == null
							|| (item.getData() instanceof TypeModel == false))
						return;
					// find the column
					List<TypeModel> doubleClickType = new ArrayList<TypeModel>();
					doubleClickType.add((TypeModel) item.getData());
					showTypeContent(doubleClickType);
				}
			});
			return tableComposite;
		}

		private void showTypeContent(List<TypeModel> types) {
			GetTypeContentRunnable contentGen = new GetTypeContentRunnable(
					types);

			try {
				if (types.size() < 7) {
					contentGen.run(new NullProgressMonitor());
				} else {
					new ProgressMonitorDialog(ImportTypesWizardPage.this
							.getShell()).run(false, false, contentGen);
				}
			} catch (Exception e) {
				UIUtil.showErrorDialog("Error ", e);
			}
			DetailMessageDialog dialog = new DetailMessageDialog(
					ImportTypesWizardPage.this.getContainer().getShell(),
					"Type Content Report", contentGen.getTypesContent(), false);
			dialog.open();
		}

		class GetTypeContentRunnable implements IRunnableWithProgress {
			private List<TypeModel> types;

			private String content;

			public GetTypeContentRunnable(List<TypeModel> types) {
				this.types = types;
			}

			/**
			 * this must be done in current thread to make sure the return
			 * values is correct.
			 * 
			 * @return
			 * 
			 * @return
			 */
			public String getTypesContent() {
				return content;
			}

			@Override
			public void run(IProgressMonitor monitor)
					throws InvocationTargetException, InterruptedException {
				try {
					monitor.beginTask("Generating types content...",
							IProgressMonitor.UNKNOWN);
					StringBuilder typeContents = new StringBuilder(
							"Here is the type content:\r\n\r\n");
					for (TypeModel model : types) {
						typeContents.append("Type Name: " + model.getTypeName()
								+ "\r\n");
						proceeTypeErrorReport(typeContents, model);
						typeContents.append("Type Content:\r\n");
						String xsdContent = model.getTypeContent().toString();
						xsdContent = xsdContent.replace("\r\n", "\n");
						xsdContent = xsdContent.replace("\n", "\r\n");
						typeContents.append(xsdContent);
					}
					// for better display
					content = typeContents.toString();
				} catch (Exception e) {
					throw new InvocationTargetException(e);
				} finally {
					monitor.done();
				}
			}
		}

		private void showErrprReport() {
			List<TypeModel> selection = getSelectedType();
			List<TypeModel> allTypes = typeList;
			StringBuilder sel = new StringBuilder(
					"Errors and Warnings in chosen types:\r\n");
			StringBuilder unSel = new StringBuilder(
					"Errors and Warnings in un-chosen types:\r\n");

			boolean selErr = false;
			boolean unselErr = false;

			for (TypeModel model : selection) {
				selErr |= proceeTypeErrorReport(sel, model);
			}

			for (TypeModel model : allTypes) {
				if (selection.contains(model)) {
					continue;
				}
				unselErr |= proceeTypeErrorReport(unSel, model);
			}

			StringBuilder all = new StringBuilder();
			if (globalErrMsg != null) {
				all.append("Global Error Message:\r\n");
				all.append("\t" + globalErrMsg + "\r\n\r\n");
			}
			if (selErr == true) {
				all.append(sel);
			}
			if (unselErr == true) {
				all.append(unSel);
			}

			if (globalErrMsg == null && selErr == false && (unselErr == false)) {
				all.append("Congratulations! No error or warning found.");
			}

			DetailMessageDialog dialog = new DetailMessageDialog(
					ImportTypesWizardPage.this.getContainer().getShell(),
					"Error Report", all.toString(), false);
			dialog.open();
		}

		protected boolean proceeTypeErrorReport(StringBuilder content,
				TypeModel model) {
			boolean hasError = model.hasError();
			boolean hasWarning = model.hasWarning();
			if (hasError == true || hasWarning == true) {
				content.append("Type " + model.getTypeName() + ":\r\n");
			}
			if (model.hasError() == true) {
				content.append("Errors:\r\n");
				for (String str : model.getErrors()) {
					content.append("\t" + str + "\r\n");
				}
			}
			if (model.hasWarning() == true) {
				content.append("Warnings:\r\n");
				for (String str : model.getWarnings()) {
					content.append("\t" + str + "\r\n");
				}
			}
			return hasWarning || hasError;
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

		public void updateTableContent(Collection<TypeModel> types) {
			typeList.clear();
			typeList.addAll(types);
			validateAll();
			this.refreshTypeTable(true);
		}

		public List<TypeModel> getTypes() {
			return typeList;
		}

		private void selectAll(boolean select) {
			for (TypeModel type : typeList) {
				type.setSelected(select);
			}
			validateAll();
			// model changes, call refresh to update view
			refreshTypeTable(false);
		}

		private void selectAllCorrect() {
			for (TypeModel type : typeList) {
				type.setSelected((type.hasError() == false)
						&& (type.hasWarning() == false));
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

				if ((element instanceof TypeModel) == false) {
					return null;
				}
				TypeModel type = (TypeModel) element;

				switch (columnIndex) {
				case 0:
					return type.isSelected() ? CHECKED : UNCHECKED;
				case 1:
					if (type.isSelected() == false) {
						return null;
					}
					if (type.hasError() == true
							|| type.isHasImportError() == true) {
						return ERROR;
					}
					if (type.hasWarning() == true) {
						return WARNING;
					}
				default:
					return null;
				}
			}

			public String getColumnText(Object element, int columnIndex) {
				if (element == null) {
					return "";
				}
				if ((element instanceof TypeModel) == false) {
					return "";
				}
				TypeModel type = (TypeModel) element;
				switch (columnIndex) {
				case 0:
					return "";
				case 1:
					return type.getTypeName();
				case 2:
					return type.getNamespace();
				case 3:
					String desc = type.getDocumentation();
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
						|| ((element instanceof TypeModel) == false)) {
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
						|| ((element instanceof TypeModel) == false)) {
					return null;
				}
				TypeModel type = (TypeModel) element;
				if (titles[0].equals(property)) {
					return type.isSelected();
				} else if (titles[1].equals(property)) {
					return type.getTypeName();
				} else if (titles[2].equals(property)) {
					return type.getNamespace();
				} else if (titles[3].equals(property)) {
					return type.getDocumentation();
				}

				return null;
			}

			private void selectAllType(TypeModel model) {
				List<TypeModel> unSelectedDep = new ArrayList<TypeModel>();
				List<TypeModel> unSelectedDepTemp = new ArrayList<TypeModel>();
				// current type is the root.
				unSelectedDep.add(model);
				while (unSelectedDep.size() > 0) {

					// find dep of all type. if the type dep is already
					// selected, skip it.
					for (TypeModel dep : unSelectedDep) {
						// select type
						dep.setSelected(true);
						if (dep.hasInternalDependencies()) {
							for (TypeModel depType : dep
									.getInternalDependencies()) {
								if (depType.isSelected() == false) {
									unSelectedDepTemp.add(depType);
								}
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
				TypeModel type = (TypeModel) ((TableItem) element).getData();
				if (titles[0].equals(property)) {
					boolean selection = Boolean.valueOf(value.toString());
					if (selection == true) {
						selectAllType(type);
					} else {
						type.setSelected(false);
					}
				} else if (titles[1].equals(property)) {
					type.setTypeName(value.toString());
				} else if (titles[2].equals(property)) {
					// type.type = value.toString();
				} else if (titles[3].equals(property)) {
					String descripion = value.toString();
					descripion = StringUtils.remove(descripion, '\r');
					type.setDocumentation(descripion);
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

		private void updateTypeLibAndNamespace(String typeLibName,
				String namespace) {
			for (TypeModel type : typeList) {
				type.setTypeLibName(typeLibName);
				type.setNamespace(namespace);
			}
		}

		public String validateSelectedTypes() {

			// clean all errors
			for (TypeModel type : typeList) {
				type.setHasImportError(false);
			}

			// find if any unsupported type is selected
			StringBuffer unsupportedType = new StringBuffer();
			for (TypeModel type : typeList) {
				if (type.isSelected() == false) {
					continue;
				}
				if (type.hasError() == true) {
					// unsupportedType.append(type.getUnSupportedReason());
					if (unsupportedType.length() == 0) {
						unsupportedType.append(type.getTypeName());
					} else {
						unsupportedType.append(", " + type.getTypeName());
					}
					type.setHasImportError(true);
					type.setImportError("This type is not supported for now.");
				}
			}

			if (unsupportedType.length() > 0) {
				return unsupportedType.insert(
						0,
						"The follow chosen type has errors. "
								+ "Please view detail error report "
								+ "using error report button on toolbar: ")
						.toString();
			}

			// find if at least one type is selected. Validate type name
			int typeCount = 0;
			StringBuffer nameValidation = new StringBuffer();
			StringBuffer depValidation = new StringBuffer();

			for (TypeModel type : typeList) {
				if (type.isSelected() == false) {
					continue;
				}
				typeCount++;
				String typeName = type.getTypeName();
				String errorMsg = "The name [" + typeName
						+ "] is not valid against the pattern \""
						+ RegExConstants.PROJECT_NAME_EXP + "\"";
				IStatus validationModel = validateName(typeName, errorMsg);
				if (validationModel.isOK() == false) {
					nameValidation.append(typeName + ", ");
					type.setHasImportError(true);
					type.setImportError(errorMsg);
				}
				if (type.hasInternalDependencies() == true) {
					for (TypeModel depModel : type.getInternalDependencies()) {
						if (depModel.isSelected() == false) {
							type.setHasImportError(true);
							String depErrorMsg = typeName + " depends on "
									+ depModel.getTypeName() + ". ";
							depValidation.append(depErrorMsg);
							type.setImportError(depErrorMsg);
						}
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
				return "Please choose at least one type to continue.";
			}

			boolean hasEmptyTypeName = false;

			// find duplicate names from existing type library project. find
			// empty type names
			StringBuffer dupNamesInTypeRepo = new StringBuffer();
			if (useExistProject() == true) {
				IProject project = targetTLPrj;
				for (TypeModel type : typeList) {
					if (type.isSelected() == false) {
						continue;
					}

					String typeName = type.getTypeName();
					if (StringUtils.isBlank(typeName) == true) {
						hasEmptyTypeName = true;
						continue;
					}

					boolean hasXSDFile = project.getFile(
							TypeLibraryUtil.getXsdFileLocation(typeName,
									project)).exists();
					boolean hasType = true;

					try {
						hasType = SOAGlobalRegistryAdapter.getInstance()
								.getGlobalRegistry().getType(
										new QName(TypeLibraryUtil
												.getNameSpace(targetTLPrj
														.getName()), typeName)) != null;
					} catch (Exception e) {
						e.printStackTrace();
					}
					if (hasXSDFile == true || hasType == true) {
						dupNamesInTypeRepo.append(type.getTypeName() + ", ");
						type.setHasImportError(true);
						type.setImportError("Type Name \"" + type.getTypeName()
								+ "\" is duplicated with "
								+ "existing type in type library..");
					}
				}
			}

			// find duplicate names from type list
			StringBuffer dupNamesInTypeTable = new StringBuffer();
			Map<String, TypeModel> types = new HashMap<String, TypeModel>();
			for (TypeModel type : typeList) {
				if (type.isSelected() == false) {
					continue;
				}
				if (types.containsKey(type.getTypeName())) {
					TypeModel dupNameType = types.get(type.getTypeName());
					dupNameType.setHasImportError(true);
					type.setHasImportError(true);
					dupNameType.setImportError("Type Name is duplicated.");
					type.setImportError("Type Name is duplicated.");
					dupNamesInTypeTable.append(type.getTypeName() + ", ");
				} else {
					types.put(type.getTypeName(), type);
				}
			}
			refreshTypeTable(false);

			StringBuffer dupErrMsg = new StringBuffer();
			if (dupNamesInTypeRepo.length() > 0) {
				dupErrMsg.append("chosen types duplicated "
						+ "with types in the Type Repository ["
						+ dupNamesInTypeRepo.substring(0, dupNamesInTypeRepo
								.length() - 2) + "]. ");
			}

			if (dupNamesInTypeTable.length() > 0) {
				dupErrMsg.append("Duplicated type chosen ["
						+ dupNamesInTypeTable.substring(0, dupNamesInTypeTable
								.length() - 2) + "].");
			}

			if (dupErrMsg.length() > 0) {
				return dupErrMsg.toString();
			}

			if (hasEmptyTypeName == true) {
				return "No Type chosen";
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
			TypeModel p1 = (TypeModel) e1;
			TypeModel p2 = (TypeModel) e2;
			int rc = 0;
			switch (columnIndex) {

			case 0:
				rc = p1.isSelected() == p2.isSelected() ? 0
						: (p1.isSelected() ? 1 : -1);
				break;
			case 1:
				rc = p1.getTypeName().toUpperCase().compareTo(
						p2.getTypeName().toUpperCase());
				break;
			case 2:
				rc = p1.getNamespace().toUpperCase().compareTo(
						p2.getNamespace().toUpperCase());
				break;
			case 3:
				rc = p1.getDocumentation().toUpperCase().compareTo(
						p2.getDocumentation().toUpperCase());
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

class ReloadTypesRunnable implements IRunnableWithProgress {
	private String path;
	private Collection<TypeModel> types;
	private Map<String, TypeModel> referedTypes;

	public ReloadTypesRunnable(String path) {
		this.path = path;
	}

	/**
	 * this must be done in current thread to make sure the return values is
	 * correct.
	 * 
	 * @return
	 */
	public Collection<TypeModel> getTypes() {
		return types;
	}

	public Map<String, TypeModel> getReferedTypes() {
		return referedTypes;
	}

	@Override
	public void run(IProgressMonitor monitor) throws InvocationTargetException,
			InterruptedException {
		try {
			monitor.beginTask("Extracting schema types from file...",
					IProgressMonitor.UNKNOWN);
			if (path.endsWith(SOAProjectConstants.XSD)) {
				ImportTypesFromXSDParser schmaHandler = new ImportTypesFromXSDParser();
				schmaHandler.cutXSD(path);
				types = schmaHandler.getTypeModels();
				referedTypes = schmaHandler.getReferedTLTypes();
			} else if (path.endsWith(SOAProjectConstants.WSDL)) {
				ImportTypesFromWSDLParser wsdlHandler = new ImportTypesFromWSDLParser();
				wsdlHandler.cutWSDL(path.toString());
				types = wsdlHandler.getTypeModels();
				referedTypes = wsdlHandler.getReferedTLTypes();
			}
		} catch (Exception e) {
			throw new InvocationTargetException(e);
		} finally {
			monitor.done();
		}
	}
}