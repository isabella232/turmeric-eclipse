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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.xml.namespace.QName;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.ebayopensource.turmeric.eclipse.config.repo.SOAConfigExtensionFactory.SOAConfigTemplate;
import org.ebayopensource.turmeric.eclipse.core.TurmericCoreActivator;
import org.ebayopensource.turmeric.eclipse.core.logging.SOALogger;
import org.ebayopensource.turmeric.eclipse.core.model.TemplateModel;
import org.ebayopensource.turmeric.eclipse.core.resources.constants.SOAProjectConstants;
import org.ebayopensource.turmeric.eclipse.core.resources.constants.SOAXSDTemplateSubType;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.ISOAHelpProvider;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.SOAGlobalRegistryAdapter;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.GlobalRepositorySystem;
import org.ebayopensource.turmeric.eclipse.core.resources.constants.SOATypeLibraryConstants;
import org.ebayopensource.turmeric.eclipse.ui.AbstractSOAResourceWizardPage;
import org.ebayopensource.turmeric.eclipse.ui.UIActivator;
import org.ebayopensource.turmeric.eclipse.ui.UIConstants;
import org.ebayopensource.turmeric.eclipse.ui.components.ProjectSelectionListLabelProvider;
import org.ebayopensource.turmeric.eclipse.utils.plugin.WorkspaceUtil;
import org.ebayopensource.turmeric.eclipse.utils.ui.UIUtil;
import org.ebayopensource.turmeric.tools.library.TypeLibraryConstants;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.ElementListSelectionDialog;
import org.eclipse.wst.xsd.ui.internal.common.util.XSDCommonUIUtils;
import org.eclipse.xsd.XSDAnnotation;
import org.eclipse.xsd.XSDSchema;
import org.eclipse.xsd.XSDTypeDefinition;

import org.ebayopensource.turmeric.common.config.TypeLibraryType;
import org.w3c.dom.Element;

/**
 * The Class AbstractNewTypeWizardPage.
 *
 * @author yayu
 */
public abstract class AbstractNewTypeWizardPage extends
		AbstractSOAResourceWizardPage {
	private Text namespaceText;
	private CCombo templateCombo;
	private Text typeLibraryNameText;
	private Button typeLibNameBrowseBtn;
	private String typeLibName;
	private Text parentTypeText;
	private static final SOALogger logger = SOALogger.getLogger();

	/** The container. */
	protected Composite container;
	
	/** The doc text. */
	protected Text docText;
	
	/** The base type comp. */
	protected CCombo baseTypeComp;
	private String currentTemplate;

	/**
	 * Instantiates a new abstract new type wizard page.
	 *
	 * @param pageName the page name
	 * @param title the title
	 * @param description the description
	 * @param typeLibName the type lib name
	 */
	public AbstractNewTypeWizardPage(String pageName, String title,
			String description, String typeLibName) {
		this(pageName, title, description);
		this.typeLibName = typeLibName;
	}

	/**
	 * Instantiates a new abstract new type wizard page.
	 *
	 * @param pageName the page name
	 * @param title the title
	 * @param description the description
	 */
	public AbstractNewTypeWizardPage(String pageName, String title,
			String description) {
		super(pageName, title, description);
	}

	/* (non-Javadoc)
	 * @see org.ebayopensource.turmeric.eclipse.ui.AbstractSOAResourceWizardPage#getDefaultResourceName()
	 */
	@Override
	public String getDefaultResourceName() {
		return SOATypeLibraryConstants.DEFAULT_TYPE_NAME;
	}

	/**
	 * Creates the template combo.
	 *
	 * @param container the container
	 * @return the c combo
	 */
	protected CCombo createTemplateCombo(Composite container) {
		final Map<String, URL> templateTypes = getTemplateTypes();
		templateCombo = super.createCCombo(container, "&Template:", false,
				templateTypes.keySet().toArray(new String[0]), 
				"the template of the new schema type");
		templateCombo.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				try {
					loadDocumentationFromTemplate();
				} catch (Exception exp) {
					logger.error(exp);
					UIUtil.showErrorDialog(exp);
				}
			}
		});
		return templateCombo;
	}

	/**
	 * Load documentation from template.
	 *
	 * @throws Exception the exception
	 */
	protected void loadDocumentationFromTemplate() throws Exception {
		if (docText != null && templateCombo != null) {
			final String templateFileName = templateCombo.getText();
			if (StringUtils.equals(currentTemplate, templateFileName) == false) {
				currentTemplate = templateFileName;
				final Map<String, URL> templateTypes = getTemplateTypes();
				if (StringUtils.isNotBlank(templateFileName)) {
					final URL file = templateTypes.get(templateFileName);
					if (file != null) {
						InputStream input = null;
						try {
							input = file.openStream();
							final TemplateModel model = processTemplateModel(input);
							docText.setText(model.getDocumentation());
						} finally {
							IOUtils.closeQuietly(input);
						}
					}
				}
			}
		}
	}
	
	private TemplateModel processTemplateModel(InputStream inputStream)
			throws CoreException, IOException {
		TemplateModel templateModel = new TemplateModel();
		XSDSchema schema = TurmericCoreActivator.parseSchema(inputStream);
		if (schema.getTypeDefinitions() != null
				&& schema.getTypeDefinitions().size() > 0
				&& schema.getTypeDefinitions().get(0) != null
				&& schema.getTypeDefinitions().get(0) instanceof XSDTypeDefinition) {
			XSDAnnotation xsdAnnotation = XSDCommonUIUtils
					.getInputXSDAnnotation(schema
							.getTypeDefinitions().get(0), false);
			if (xsdAnnotation != null) {
				List documentationList = xsdAnnotation.getUserInformation();
				if (documentationList.size() > 0) {
					Element documentationElement = (Element) documentationList
							.get(0);
					templateModel.setDocumentation(documentationElement
							.getTextContent());
				}
			}
		}
		return templateModel;

	}	

	/**
	 * Creates the control.
	 *
	 * @param parent the parent
	 * @param validateNow the validate now
	 */
	public void createControl(Composite parent, boolean validateNow) {
		try {
			container = super.createParentControl(parent, 4);
			Text typeNameText = super.createResourceNameControl(container,
					"Type &Name:", modifyListener, true, 
					"the name of the schema type");
			typeNameText.setFocus();
			super.createResourceVersionControl(container, "&Version:",
					modifyListener, "the version of the new schema type");
			createTemplateCombo(container);
			addNamespace(container);
			addTypeLibraryName(container);
			setControl(container);
			if (validateNow)
				dialogChanged();
		} catch (Exception e) {
			SOALogger.getLogger().error(e);
			throw new RuntimeException(e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void createControl(Composite parent) {
		createControl(parent, true);
	}

	/**
	 * Adds the type library name.
	 *
	 * @param parentComposite the parent composite
	 * @return the text
	 */
	protected Text addTypeLibraryName(Composite parentComposite) {
		new Label(parentComposite, SWT.LEFT).setText("&Type Library:");
		typeLibraryNameText = new Text(parentComposite, SWT.BORDER);
		GridData gData = new GridData(GridData.FILL_HORIZONTAL);
		gData.horizontalSpan = 2;
		typeLibraryNameText.setLayoutData(gData);
		typeLibraryNameText.addModifyListener(modifyListener);
		typeLibraryNameText.setEditable(false);
		typeLibraryNameText.setText(typeLibName);
		UIUtil.decorateControl(this, typeLibraryNameText, 
				"the name of the target type library project for the new schema type");

		// Browse Button
		typeLibNameBrowseBtn = new Button(parentComposite, SWT.PUSH);
		typeLibNameBrowseBtn.setAlignment(SWT.LEFT);
		typeLibNameBrowseBtn.setLayoutData(new GridData(SWT.FILL, SWT.FILL,
				false, false, 1, 1));
		typeLibNameBrowseBtn.setText("&Select...");
		typeLibNameBrowseBtn.setSelection(false);
		final SelectionListener typeLibNameBrowseListener = new SelectionListener() {
			@Override
			public void widgetDefaultSelected(final SelectionEvent e) {
				widgetSelected(e);
			}

			@Override
			public void widgetSelected(final SelectionEvent e) {
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
					.getProjectsByNature(
							UIConstants.TYPELIB_NATURE_ID);
					if (typeLibraryNameText != null && StringUtils.isNotBlank(typeLibraryNameText.getText())
							&& WorkspaceUtil.getProject(
							typeLibraryNameText.getText()).isAccessible()) {
						projects.remove(WorkspaceUtil.getProject(typeLibraryNameText.getText()));
					}
					selectionDialog.setElements(
							projects.toArray(new IProject[0]));
					
				} catch (CoreException e1) {
					SOALogger.getLogger().error(e1);
					return;
				}
				selectionDialog.setBlockOnOpen(true);
				selectionDialog.setMultipleSelection(false);
				if (selectionDialog.open() == Window.OK) {
					if (selectionDialog.getResult() != null
							&& selectionDialog.getResult().length > 0) {
						String projectName = ((IProject) selectionDialog
								.getResult()[0]).getName();
						typeLibraryNameText.setText(projectName);
						dialogChanged();
					}
				}

			}
		};
		typeLibNameBrowseBtn.addSelectionListener(typeLibNameBrowseListener);
		return typeLibraryNameText;
	}

	/* (non-Javadoc)
	 * @see org.ebayopensource.turmeric.eclipse.ui.AbstractSOAResourceWizardPage#dialogChanged()
	 */
	@Override
	protected boolean dialogChanged() {
		boolean result = super.dialogChanged();
		if (result == false) {
			return result;
		}
		// The listener attached to the user selected type library triggers this
		// method.
		// Set the library namespace derived from the user selected type library
		if (typeLibraryNameText != null
				&& !"".equals(typeLibraryNameText.getText()))
			setNamespaceField(typeLibraryNameText.getText());
		if (StringUtils.isBlank(templateCombo.getText())) {
			updateStatus(this.templateCombo, 
					"Template Type cannot be empty. Check if templates are present in config plugin");
			return false;
		}
		final String fileName = getResourceName();
		IStatus validationModel = ResourcesPlugin.getWorkspace().validateName(
				fileName, IResource.FILE);
		if (checkValidationResult(getResourceNameText(), validationModel) == false)
			return false;

		try {
			if (StringUtils.isEmpty(typeLibraryNameText.getText())) {
				updateStatus(typeLibraryNameText, "Select a type library.");
				return false;
			}
			IProject project = WorkspaceUtil.getProject(typeLibraryNameText
					.getText());
			if (project.getFile(
					TurmericCoreActivator.getXsdFileLocation(fileName, project))
					.exists()
					|| SOAGlobalRegistryAdapter.getInstance().getGlobalRegistry().getType(
							new QName(
									UIActivator
											.getNameSpace(typeLibraryNameText
													.getText()), fileName)) != null) {
				updateStatus(super.getResourceNameText(), 
						"Type with the same name already exists in the specified namespace.");
				return false;
			}
			for (final IResource resource : TurmericCoreActivator.getTypeLibProjectReadableResources(WorkspaceUtil
							.getProject(typeLibraryNameText.getText()))) {
				if (WorkspaceUtil.isResourceReadable(resource) == false) {
					updateStatus(super.getResourceNameText(), 
							resource.getName()
							+ " does not exist or is not accessible.");
					return false;
				}
			}
			for (final IResource resource : TurmericCoreActivator.getTypeLibProjectWritableResources(WorkspaceUtil
							.getProject(typeLibraryNameText.getText()))) {
				if (WorkspaceUtil.isResourceModifiable(resource) == false) {
					updateStatus(super.getResourceNameText(), 
							resource.getName()
							+ " does not exist or is not modifiable.");
					return false;
				}
			}
			if (SOAGlobalRegistryAdapter.getInstance().getGlobalRegistry().getTypeLibrary(
					typeLibraryNameText.getText()) == null) {
				updateStatus("The Type registry seems to be out of sync. Please open the GlobalRegistry view, Window-->Show View-->SOA Plugin-->Global Registry and click the refresh button and try again.");
				return false;
			}
		} catch (Exception exception) {
			SOALogger.getLogger().warning("Validation Failure!", exception);
			// Validation failure is Okay :).
		}

		return result;
	}

	/**
	 * Gets the namespace value.
	 *
	 * @return the namespace value
	 */
	public String getNamespaceValue() {
		return getTextValue(namespaceText);
	}

	private void setNamespaceField(String typeLibName) {
		TypeLibraryType typeLibraryType = null;
		try {
			typeLibraryType = SOAGlobalRegistryAdapter.getInstance().getGlobalRegistry()
					.getTypeLibrary(typeLibName);
		} catch (Exception e) {
			logger.error(e);
			UIUtil.showErrorDialog(e);
		}
		if (typeLibraryType != null)
			namespaceText.setText(typeLibraryType.getLibraryNamespace());
	}

	/**
	 * Gets the type library name.
	 *
	 * @return the type library name
	 */
	public String getTypeLibraryName() {
		return getTextValue(typeLibraryNameText);
	}

	/**
	 * Gets the parent type.
	 *
	 * @return the parent type
	 */
	public String getParentType() {
		return getTextValue(parentTypeText);
	}

	/**
	 * Gets the template types.
	 *
	 * @return the template types
	 */
	protected abstract Map<String, URL> getTemplateTypes();

	/**
	 * Gets the name value.
	 *
	 * @return the name value
	 */
	public String getNameValue() {
		return getResourceName();
	}

	/**
	 * Gets the version value.
	 *
	 * @return the version value
	 */
	public String getVersionValue() {
		return getResourceVersion();
	}

	/**
	 * Gets the template value.
	 *
	 * @return the template value
	 */
	public String getTemplateValue() {
		return getTextValue(this.templateCombo);
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
	 * Adds the namespace.
	 *
	 * @param composite the composite
	 * @return the text
	 */
	public Text addNamespace(Composite composite) {
		return namespaceText = createLabelTextField(composite, "&Namespace:",
				TypeLibraryConstants.TYPE_INFORMATION_NAMESPACE,
				modifyListener, true, false, "the namespace of the new schema type");
	}

	/**
	 * Creates the type combo.
	 *
	 * @param parent the parent
	 * @param typeLabel the type label
	 */
	protected void createTypeCombo(Composite parent, String typeLabel) {
		baseTypeComp = createCCombo(parent, typeLabel, false,
				SOATypeLibraryConstants.SCHEMA_DATA_TYPES, 
				"select an existing type for the new schema type to be based on");
		baseTypeComp.select(0);
		baseTypeComp.setBackground(UIUtil.display().getSystemColor(
				SWT.COLOR_WHITE));
	}

	/**
	 * Creates the documentation text.
	 *
	 * @param parent the parent
	 * @throws Exception the exception
	 */
	protected void createDocumentationText(Composite parent) throws Exception {
		docText = createLabelTextField(parent, "&Documentation:",
				SOAProjectConstants.EMPTY_STRING, modifyListener, true, true,
				SWT.BORDER | SWT.MULTI | SWT.WRAP, 
				"the description of the new schema type");
		docText
				.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2,
						2));
		if (templateCombo != null && templateCombo.getItemCount() > 0) {
			loadDocumentationFromTemplate();
		}
	}

	/**
	 * Gets the doc text.
	 *
	 * @return the doc text
	 */
	public String getDocText() {
		return StringUtils.replace(docText.getText(), docText
				.getLineDelimiter(), "");
	}

	/**
	 * Gets the raw base type.
	 *
	 * @return the raw base type
	 */
	public Object getRawBaseType() {
		return getTextValue(this.baseTypeComp);
	}

	/**
	 * Truncate xsd extension.
	 *
	 * @param templateTypeValues the template type values
	 * @return the map
	 */
	protected Map<String, File> truncateXSDExtension(
			Map<String, File> templateTypeValues) {
		if (templateTypeValues == null)
			return null;
		final Map<String, File> truncatedTemplateTypeValues = new LinkedHashMap<String, File>();
		for (String fileName : templateTypeValues.keySet()) {
			if (StringUtils.isNotBlank(fileName)) {
				final File file = templateTypeValues.get(fileName);
			if (fileName.endsWith(SOATypeLibraryConstants.DOT_XSD)) {
				fileName = fileName.replace(SOATypeLibraryConstants.DOT_XSD,
						SOAProjectConstants.EMPTY_STRING);
			}
			truncatedTemplateTypeValues.put(fileName, file);
			}
		}
		return truncatedTemplateTypeValues;
	}

	/**
	 * Gets the template type names.
	 *
	 * @param subType the sub type
	 * @return the template type names
	 */
	protected Map<String, URL> getTemplateTypeNames(
			SOAXSDTemplateSubType subType) {
		final List<SOAConfigTemplate> templateTypes = UIActivator
				.getFiles(subType);
		final Map<String, URL> templateTypeNames = new LinkedHashMap<String, URL>();
		for (SOAConfigTemplate template : templateTypes)
			templateTypeNames.put(template.getName(), template.getUrl());
		return templateTypeNames;
	}
}
