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
package org.ebayopensource.turmeric.eclipse.ui;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.ebayopensource.turmeric.eclipse.core.logging.SOALogger;
import org.ebayopensource.turmeric.eclipse.exception.validation.ValidationInterruptedException;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.GlobalRepositorySystem;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.ISOAOrganizationProvider;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.ISOARepositorySystem;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.ISOARootLocator;
import org.ebayopensource.turmeric.eclipse.utils.plugin.WorkspaceUtil;
import org.ebayopensource.turmeric.eclipse.utils.ui.UIUtil;
import org.ebayopensource.turmeric.eclipse.validator.core.ErrorMessage;
import org.ebayopensource.turmeric.eclipse.validator.core.ISOAValidator;
import org.ebayopensource.turmeric.eclipse.validator.core.InputObject;
import org.ebayopensource.turmeric.eclipse.validator.utils.common.NameValidator;
import org.ebayopensource.turmeric.eclipse.validator.utils.common.RegExConstants;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;


/**
 * @author yayu
 * 
 */
public abstract class AbstractSOAProjectWizardPage extends
AbstractSOAResourceWizardPage {

	private Text workspaceRootText;
	private Button workspaceRootBrowseButton;
	private Button workspaceRootOverrideButton;
	protected CCombo serviceDomainList;
	protected CCombo domainClassifierList;
	
	private ModifyListener domainListModifyListener;
	private ModifyListener domainClassifierModifyListener;
	private static final SOALogger logger = SOALogger.getLogger();

	/**
	 * @param pageName
	 */
	public AbstractSOAProjectWizardPage(String pageName) {
		super(pageName);
	}

	@Override
	public void setVisible(boolean visible) {
		if (visible == true) {
			populateDomainList();
		}
		super.setVisible(visible);
	}
	
	private void populateDomainList() {
		if (domainListModifyListener == null 
				|| domainClassifierModifyListener == null
				|| serviceDomainList == null
				|| domainClassifierList == null
				|| serviceDomainList.getItemCount() > 0) {
			return;
		}
		try {
			Map<String, List<String>> domainList = Collections.emptyMap();
			if (getWizard() instanceof AbstractSOADomainWizard) {
				domainList = ((AbstractSOADomainWizard)getWizard()).getDomainList();
			}
			
			if (domainList.isEmpty() == false) {
				serviceDomainList.removeModifyListener(domainListModifyListener);
				serviceDomainList.setItems(domainList.keySet().toArray(new String[0]));
				domainClassifierList.removeModifyListener(domainClassifierModifyListener);
				populiateClassifierList();
				domainClassifierList.addModifyListener(domainClassifierModifyListener);
				serviceDomainList.addModifyListener(domainListModifyListener);
				serviceDomainList.select(0);
				serviceDomainList.removeModifyListener(domainListModifyListener);
			}
		} catch (Exception e) {
			logger.warning(e);
		}
	}

	/**
	 * @param pageName
	 * @param title
	 * @param description
	 */
	public AbstractSOAProjectWizardPage(String pageName, String title,
			String description) {
		super(pageName, title, description);
	}
	
	

	protected Composite addWorkspaceRootChooser(final Composite parentComposite) {
		final ISOARootLocator locator = GlobalRepositorySystem.instanceOf()
				.getActiveRepositorySystem().getSOARootLocator();
		final String rootLocation;/*
									 * = locator != null ? locator
									 * .getDefaultProjectLocation() :
									 * DEFAULT_TEXT_VALUE;
									 */
		final boolean shouldOverrideProjectRootDirectory = locator
				.shouldOverrideProjectRootDirectory();
		boolean saveLocation = false; // whether to save the location to pref
										// store
		if (shouldOverrideProjectRootDirectory) {
			final String storedRoot = getWorkspaceRoot();
			if (StringUtils.isNotBlank(storedRoot)) {
				// try the stored parent dir first
				rootLocation = storedRoot;
			} else {
				// the parent dir is not stored, use the default location
				// instead
				rootLocation = locator != null ? locator
						.getDefaultProjectLocation() : DEFAULT_TEXT_VALUE;
				saveLocation = true;
			}
		} else {
			// not overriding the root loc, use the default project loc instaed
			rootLocation = locator != null ? locator
					.getDefaultProjectLocation() : DEFAULT_TEXT_VALUE;
			saveLocation = true;
		}

		if (saveLocation == true) {
			saveWorkspaceRoot(rootLocation);
		}
		// workspace root
		new Label(parentComposite, SWT.LEFT).setText("&Parent Directory:");
		workspaceRootText = new Text(parentComposite, SWT.BORDER);
		workspaceRootText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		workspaceRootText.addModifyListener(modifyListener);
		workspaceRootText.setEditable(shouldOverrideProjectRootDirectory);
		workspaceRootText.setEnabled(shouldOverrideProjectRootDirectory);
		workspaceRootText.setText(rootLocation);
		UIUtil.decorateControl(this, workspaceRootText, 
				"either browse to or enter the destination of the new project");
		workspaceRootBrowseButton = new Button(parentComposite, SWT.PUSH);
		workspaceRootBrowseButton.setAlignment(SWT.RIGHT);
		workspaceRootBrowseButton.setText("&Browse...");
		workspaceRootBrowseButton.setSelection(false);
		workspaceRootBrowseButton
				.setEnabled(shouldOverrideProjectRootDirectory);
		final SelectionListener workspaceBrowseListener = new SelectionListener() {
			public void widgetDefaultSelected(final SelectionEvent e) {
				widgetSelected(e);
			}

			public void widgetSelected(final SelectionEvent e) {
				final String dirName = UIUtil.directoryDialog(
						"Select Target Directory for Project:", rootLocation);
				if (StringUtils.isBlank(dirName))
					return;
				workspaceRootText.setText(dirName);
				dialogChanged();
			}
		};
		workspaceRootBrowseButton.addSelectionListener(workspaceBrowseListener);

		// workspace root override button
		final SelectionListener overrideWorkspaceListener = new SelectionListener() {
			public void widgetDefaultSelected(final SelectionEvent e) {
				widgetSelected(e);
			}

			public void widgetSelected(final SelectionEvent e) {
				if (workspaceRootOverrideButton.getSelection() == false) {
					workspaceRootText.setEditable(false);
					workspaceRootText.setEnabled(false);
					workspaceRootText.setText(locator
							.getDefaultProjectLocation());
					workspaceRootBrowseButton.setEnabled(false);
				} else {
					workspaceRootText.setEditable(true);
					workspaceRootText.setEnabled(true);
					workspaceRootBrowseButton.setEnabled(true);
					if (StringUtils.isBlank(workspaceRootText.getText()))
						workspaceRootText.setText(getWorkspaceRoot());
				}
				dialogChanged();
			}
		};
		workspaceRootOverrideButton = createOverrideButton(parentComposite,
				workspaceRootText, overrideWorkspaceListener);
		workspaceRootOverrideButton
				.setSelection(shouldOverrideProjectRootDirectory);
		return parentComposite;
	}

	protected Text getWorkspaceRootText() {
		return workspaceRootText;
	}
	
	protected CCombo addServiceDomainList(final Composite composite) throws Exception{
		return addServiceDomainList(composite, true);
	}
	
	protected CCombo addServiceDomainList(final Composite composite, 
			boolean enabled) throws Exception{
		ISOAOrganizationProvider provider = 
			GlobalRepositorySystem.instanceOf().getActiveRepositorySystem().getActiveOrganizationProvider();
		if (provider != null && provider.supportFunctionalDomain() == false) {
			return null;
		}
		this.serviceDomainList = 
			super.createCCombo(composite, "Functional Do&main", 
					true, new String[0], 
					"The service functional domain");
		// we still want it look like modifiable although it is ready only.
		serviceDomainList.setBackground(UIUtil.display()
				.getSystemColor(SWT.COLOR_LIST_BACKGROUND));
		this.serviceDomainList.setEnabled(enabled);
		domainListModifyListener = new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				populiateClassifierList();
				if (modifyListener != null)
					modifyListener.modifyText(e);
				dialogChanged();
			}
		};
		//serviceDomainList.addModifyListener(domainListModifyListener);
		serviceDomainList.addListener(SWT.DefaultSelection, new Listener() {
			public void handleEvent(Event e) {
				domainListModifyListener.modifyText(new ModifyEvent(e));
			}
		});
		serviceDomainList.addFocusListener(new FocusAdapter() {

			@Override
			public void focusLost(FocusEvent e) {
				Event event = new Event();
				event.display = e.display;
				event.widget = e.widget;
				event.time = e.time;
				event.data = e.data;
				domainListModifyListener.modifyText(new ModifyEvent(event));
			}
			
		});
		serviceDomainList.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		domainClassifierList = new CCombo(composite, 
				SWT.BORDER | SWT.DROP_DOWN | SWT.READ_ONLY);
		domainClassifierList.setEnabled(enabled);
		domainClassifierList.setBackground(UIUtil.display()
					.getSystemColor(SWT.COLOR_LIST_BACKGROUND));
		domainClassifierList.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		UIUtil.decorateControl(this, domainClassifierList, 
				"the namespace part of the selected functional domain");
		domainClassifierModifyListener = new ModifyListener() {

			public void modifyText(ModifyEvent e) {
				domainClassifierChanged();
			}
			
		};
		domainClassifierList.addModifyListener(domainClassifierModifyListener);
		new Label(composite, SWT.NONE);
		return serviceDomainList;
	}
	
	protected void domainClassifierChanged() {
	}
	
	private void populiateClassifierList() {
		List<String> classifiers = null;
		final String domainName = getServiceDomain();
		try {
			Map<String, List<String>> domainList = Collections.emptyMap();
			if (getWizard() instanceof AbstractSOADomainWizard) {
				domainList = ((AbstractSOADomainWizard)getWizard()).getDomainList();
			}
			classifiers = domainList.get(domainName);
		} catch (Exception exp) {
			logger.warning(exp);
		}
		if (classifiers == null || classifiers.isEmpty() == true) {
			//user entered domain name
			classifiers = new ArrayList<String>(5);
			String classifier = StringUtils
			.deleteWhitespace(domainName.toLowerCase(Locale.US));
			logger.warning("Could not find corresponding classifier for the provided domain name->'",
					domainName, 
					"' using the converted domain name instead->", classifier);
			classifiers.add(classifier);
		}
		domainClassifierList.setItems(classifiers.toArray(new String[0]));
		if (classifiers.isEmpty() == false) {
			domainClassifierList.select(0);
		}
	}

	@Override
	protected boolean dialogChanged() {
		if (super.dialogChanged() == false)
			return false;

		final ISOARepositorySystem activeRepositorySystem = GlobalRepositorySystem
				.instanceOf().getActiveRepositorySystem();
		ISOAValidator validator = null;
		IStatus validationModel = null;

		final boolean isOverrideProjectRootDirectory = isOverrideProjectRootDirectory();
		if (isOverrideProjectRootDirectory) {
			if (StringUtils.isBlank(getProjectRootDirectory())
					|| !new File(getProjectRootDirectory()).exists()) {
				updateStatus("Project Root Directory must exist.", this.workspaceRootText);
				return false;
			}
			validator = activeRepositorySystem.getTargetDirectoryValidator();
			try {
				validationModel = validator.validate(new Path(
						getProjectRootDirectory()));
			} catch (ValidationInterruptedException e) {
				processException(e);
			}
			if (checkValidationResult(validationModel, this.workspaceRootText) == false)
				return false;
		}

		final String parentFolderName = isOverrideProjectRootDirectory ? getProjectRootDirectory()
				: activeRepositorySystem.getSOARootLocator()
						.getDefaultProjectLocation();
		final List<ProjectNameControl> projectNames = getProjectNames();
		if (projectNames.isEmpty() == false) {
			for (ProjectNameControl prjControl : projectNames) {
				validationModel = validateProject(activeRepositorySystem,
						parentFolderName, prjControl.getProjectName());
				if (checkValidationResult(validationModel, 
						prjControl.getControls().toArray(new Control[0])) == false)
					return false;
			}
		}
		
		if (this.serviceDomainList != null && this.serviceDomainList.isEnabled()) {
			try {
				String serviceDomain = getServiceDomain();
				if (StringUtils.isBlank(serviceDomain) == true) {
					if (activeRepositorySystem.getActiveOrganizationProvider()
							.supportFunctionalDomain() == true) {
						updateStatus("Service domain must not be empty", 
								this.serviceDomainList);
						return false;
					}
				} else {
					final InputObject inputObject = new InputObject(serviceDomain,
							RegExConstants.DOMAIN_NAME_EXP,
							ErrorMessage.DOMAIN_NAME_ERRORMSG);

					validationModel = NameValidator.getInstance().validate(
							inputObject);
					if (checkValidationResult(validationModel, 
							this.serviceDomainList) == false)
						return false;
					if (serviceDomain.endsWith(" ")) {
						updateStatus("Domain name can not ends with white spaces.", 
								this.serviceDomainList);
						return false;
					}
				}
			} catch (Exception e) {
				updateStatus(e.toString());
				return false;
			}
		}

		return true;
	}

	protected IStatus validateProject(
			ISOARepositorySystem activeRepositorySystem,
			final String parentFolderName, String... projectNames) {
		IStatus validationModel = null;
		if (activeRepositorySystem == null)
			activeRepositorySystem = GlobalRepositorySystem.instanceOf()
					.getActiveRepositorySystem();
		for (String projectName : projectNames) {
			if (StringUtils.isBlank(projectName))
				continue;
			
			String projectFullPath = WorkspaceUtil.getProjectDirPath(
					parentFolderName, projectName);

			ISOAValidator validator = activeRepositorySystem
					.getProjectFileSystemValidator();
			try {
				validationModel = validator.validate(projectFullPath);
			} catch (ValidationInterruptedException e1) {
				processException(e1);
			}
			if (validationModel.getSeverity() == IStatus.ERROR) {
				return validationModel;
			}

			validator = activeRepositorySystem.getProjectWorkspaceValidator();
			try {
				validationModel = validator.validate(projectName);
			} catch (ValidationInterruptedException e) {
				processException(e);
			}
			if (validationModel.getSeverity() == IStatus.ERROR) {
				return validationModel;
			}

			validationModel = ResourcesPlugin.getWorkspace().validateName(
					projectName, IResource.PROJECT);
			if (validationModel.getSeverity() == IStatus.ERROR)
				return validationModel;
		}
		return Status.OK_STATUS;
	}

	public boolean isOverrideProjectRootDirectory() {
		if (workspaceRootOverrideButton == null
				|| !workspaceRootOverrideButton.isEnabled())
			return false;
		return workspaceRootOverrideButton.getSelection();
	}

	public String getProjectRootDirectory() {
		return getTextValue(workspaceRootText);
	}

	/**
	 * Subclass should use LinkedHashMap for overriding this method
	 * in order to keep the order of the names.
	 * @return A list of project names for creation 
	 * as well as the corresponding controls
	 */
	public List<ProjectNameControl> getProjectNames() {
		final List<ProjectNameControl> result = new ArrayList<ProjectNameControl>(5);
		result.add(new ProjectNameControl(getResourceName(), getResourceNameText()));
		return result;
	}
	
	public String getServiceDomain() {
		return getTextValue(this.serviceDomainList);
	}
	
	public String getDomainClassifier() {
		return getTextValue(this.domainClassifierList);
	}

	@Override
	public String getDefaultValue(Text text) {
		if (text == null)
			return DEFAULT_TEXT_VALUE;
		if (text == this.workspaceRootText) {
			final ISOARootLocator locator = GlobalRepositorySystem.instanceOf()
					.getActiveRepositorySystem().getSOARootLocator();
			return locator != null ? locator.getRoot().toString()
					: DEFAULT_TEXT_VALUE;
		}
		return super.getDefaultValue(text);
	}
	
	public static class ProjectNameControl {
		private String projectName;
		private List<Control> controls = new ArrayList<Control>(3);
		public ProjectNameControl(String projectName, Control... controls) {
			this(projectName, controls != null ? Arrays.asList(controls) : null);
		}
		public ProjectNameControl(String projectName, List<Control> controls) {
			super();
			this.projectName = projectName;
			if (controls != null) {
				this.controls = controls;
			}
		}
		
		public String getProjectName() {
			return projectName;
		}
		public List<Control> getControls() {
			return controls;
		}
	}
}
