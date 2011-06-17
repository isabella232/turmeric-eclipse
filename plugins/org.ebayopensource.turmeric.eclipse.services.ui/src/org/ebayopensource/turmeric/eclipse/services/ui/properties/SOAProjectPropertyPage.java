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
package org.ebayopensource.turmeric.eclipse.services.ui.properties;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.ebayopensource.turmeric.eclipse.core.logging.SOALogger;
import org.ebayopensource.turmeric.eclipse.core.resources.constants.SOAProjectConstants.SupportedProjectType;
import org.ebayopensource.turmeric.eclipse.exception.resources.SOAResourceModifyFailedException;
import org.ebayopensource.turmeric.eclipse.exception.validation.ValidationInterruptedException;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.GlobalRepositorySystem;
import org.ebayopensource.turmeric.eclipse.repositorysystem.ui.utils.ActionUtil;
import org.ebayopensource.turmeric.eclipse.repositorysystem.utils.TurmericServiceUtils;
import org.ebayopensource.turmeric.eclipse.resources.model.ISOAProject;
import org.ebayopensource.turmeric.eclipse.resources.model.ISOAProject.SOAProjectSourceDirectory;
import org.ebayopensource.turmeric.eclipse.resources.model.SOAConsumerMetadata;
import org.ebayopensource.turmeric.eclipse.resources.model.SOAConsumerProject;
import org.ebayopensource.turmeric.eclipse.resources.model.SOAImplProject;
import org.ebayopensource.turmeric.eclipse.resources.model.SOAIntfMetadata;
import org.ebayopensource.turmeric.eclipse.resources.model.SOAIntfProject;
import org.ebayopensource.turmeric.eclipse.resources.util.SOAIntfUtil;
import org.ebayopensource.turmeric.eclipse.resources.util.SOAServiceUtil;
import org.ebayopensource.turmeric.eclipse.services.ui.SOAMessages;
import org.ebayopensource.turmeric.eclipse.soatools.configtool.ConfigTool;
import org.ebayopensource.turmeric.eclipse.utils.plugin.ProgressUtil;
import org.ebayopensource.turmeric.eclipse.utils.plugin.WorkspaceUtil;
import org.ebayopensource.turmeric.eclipse.utils.ui.UIUtil;
import org.ebayopensource.turmeric.eclipse.validator.utils.common.ServiceVersionValidator;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbenchPropertyPage;
import org.eclipse.ui.actions.WorkspaceModifyOperation;
import org.osgi.framework.Version;


/**
 * The Class SOAProjectPropertyPage.
 *
 * @author yayu
 */
public class SOAProjectPropertyPage extends PreferencePage implements
		IWorkbenchPropertyPage {
	private ISOAProject soaProject;


	private static final SOALogger logger = SOALogger.getLogger();

	private Combo serviceLayer;
	private Text serviceVersion;
	private Text implVersion;
	private Text baseConsumerDir;
	
	private String oldVersion;
	
	

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.preference.PreferencePage#createContents(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	protected Control createContents(Composite parent) {
		noDefaultAndApplyButton();
		// create top panel
		Composite result = new Composite(parent, SWT.NONE);
		try {
			GridLayout layout = new GridLayout();
			layout.marginHeight = convertVerticalDLUsToPixels(IDialogConstants.VERTICAL_MARGIN);
			layout.marginWidth = 0;
			layout.verticalSpacing = convertVerticalDLUsToPixels(10);
			layout.horizontalSpacing = convertHorizontalDLUsToPixels(IDialogConstants.HORIZONTAL_SPACING);
			result.setLayout(layout);
			result.setLayoutData(new GridData(GridData.FILL_BOTH));

			createGroups(result);

			Dialog.applyDialogFont(result);
		} catch (Exception e) {
			logger.error(e);
			UIUtil.showErrorDialog(e);
		}
		return result;
	}

	/**
	 * Create Groups.
	 * @param parent
	 * @throws Exception
	 */
	private void createGroups(Composite parent) throws Exception {

		if (soaProject instanceof SOAIntfProject) {
			createIntfGroups(parent);
		} else if (soaProject instanceof SOAImplProject) {
			createImplGroups(parent);
		} else if (soaProject instanceof SOAConsumerProject) {
			createConsumerGroups(parent);
		}

	}

	private void createIntfGroups(Composite parent) throws Exception {
		Group group = createServicePropertyGroup(parent);
		boolean newSvc = SOAIntfUtil.getOldMetadataFile(soaProject.getProject(), 
				soaProject.getProjectName()).exists() == false;
		
		final String svcName = newSvc ?
		getIntfMetadata().getPublicServiceName() : 
		getIntfMetadata().getServiceName();

		if (newSvc == true) {
			addAdminName(group);
		}
		addServiceName(group, svcName);
		addServiceVersion(group, true);
		
		addServiceLayerCombo(group, true);
		addServiceIntfClassName(group);

		this.validateAll();
	}

	private void createConsumerGroups(Composite parent) {
		Group group = createConsumerPropertyGroup(parent);
		addClientName(group);
		addConsumerId(group);
		addBaseConsumerSrcDir(group);// consumer
	}

	private void createImplGroups(Composite parent) throws Exception {
		Group serviceGroup = createServicePropertyGroup(parent);		
		final String adminName = getIntfMetadata().getServiceName();
		final String svcName = getIntfMetadata().getPublicServiceName();

		if (StringUtils.isNotBlank(adminName)) {
			addAdminName(serviceGroup);
		}
		addServiceName(serviceGroup, svcName);
		addServiceVersion(serviceGroup, false);
		addServiceLayerCombo(serviceGroup, false);
		addServiceIntfClassName(serviceGroup);

		// create second group
		Group group = createImplPropertyGroup(parent);
		addImplVersion(group);
		addServiceImplClassName(group);
		
		if (this.soaProject.getProject()
				.hasNature(GlobalRepositorySystem.instanceOf().getActiveRepositorySystem()
						.getProjectNatureId(SupportedProjectType.CONSUMER))) {
			//is a consumer project also
			Group consumerGroup = createConsumerPropertyGroup(parent);
			addClientName(consumerGroup);
			addConsumerId(consumerGroup);
			addBaseConsumerSrcDir(consumerGroup);
		}

	}

	/**
	 * @param parent
	 * @return
	 */
	private Group createServicePropertyGroup(Composite parent) {
		Group servicePropertyGroup = new Group(parent, SWT.NONE);
		servicePropertyGroup.setLayout(new GridLayout(2, false));
		servicePropertyGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL,
				true, false));
		servicePropertyGroup.setText("Service Properties");
		return servicePropertyGroup;
	}

	/**
	 * @param parent
	 * @return
	 */
	private Group createImplPropertyGroup(Composite parent) {
		Group implPropertyGroup = new Group(parent, SWT.NONE);
		implPropertyGroup.setLayout(new GridLayout(2, false));
		implPropertyGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
				false));
		implPropertyGroup.setText("Implementation Properties");
		return implPropertyGroup;
	}
	
	private Group createConsumerPropertyGroup(Composite parent) {
		Group consumerPropertyGroup = new Group(parent, SWT.NONE);
		consumerPropertyGroup.setLayout(new GridLayout(2, false));
		consumerPropertyGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
				false));
		consumerPropertyGroup.setText("Consumer Properties");
		return consumerPropertyGroup;
	}


	/**
	 * {@inheritDoc}
	 * @see org.eclipse.jface.preference.PreferencePage#createControl(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public void createControl(Composite parent) {
		super.createControl(parent);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.ui.IWorkbenchPropertyPage#getElement()
	 */
	@Override
	public IAdaptable getElement() {
		return null;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.preference.PreferencePage#performOk()
	 */
	@Override
	public boolean performOk() {
		try {
			if (soaProject instanceof SOAIntfProject) {
				final SOAIntfMetadata intfMetadata = ((SOAIntfProject) soaProject)
						.getMetadata();
				final String oldVersion = intfMetadata.getServiceVersion();
				String oldServiceLayer = intfMetadata.getServiceLayer();
				final String newVersion = serviceVersion.getText();
				String newLayer = serviceLayer.getText();
				if (StringUtils.equalsIgnoreCase(oldVersion, newVersion) == true
						&& StringUtils.equalsIgnoreCase(oldServiceLayer,
								newLayer) == true) {
					return true;
				}
				logger.info("Modify service " + soaProject.getProjectName()
						+ ": new version is " + newVersion + ". New layer is: "
						+ newLayer);
				intfMetadata.setServiceVersion(serviceVersion.getText());
				intfMetadata.setServiceLayer(serviceLayer.getText());
				
				final WorkspaceModifyOperation operation = new WorkspaceModifyOperation() {

					@Override
					protected void execute(IProgressMonitor monitor)
							throws CoreException, InvocationTargetException,
							InterruptedException {
						monitor.beginTask("Updating project->"
								+ soaProject.getProjectName(),
								ProgressUtil.PROGRESS_STEP * 10);
						try {
							if (StringUtils.equals(oldVersion,newVersion) == true) {
								//only service layer changed
								SOAIntfUtil.saveMetadataProps((SOAIntfProject) soaProject, monitor);
							} else {
								// change local metadata (wsdl, properties), sync AR version.
								ActionUtil
								.updateInterfaceProjectVersion(
										(SOAIntfProject) soaProject,
										oldVersion, newVersion, false, monitor);
							}
						} catch (Exception e) {
							logger.error(e);
							throw new SOAResourceModifyFailedException(
									"Failed to modify project properties for project->"
											+ soaProject.getProjectName(), e);
						} finally {
							monitor.done();
						}
					}
				};

				try {
					new ProgressMonitorDialog(getControl().getShell()).run(false,
							true, operation);
					return true;
				} catch (Exception e) {
					logger.error(e);
					UIUtil.showErrorDialog(e);
					return false;
				}
				
			} else if (soaProject instanceof SOAImplProject) {
				// skip the modification for impl
				return true;
			} else if (soaProject instanceof SOAConsumerProject) {
				// we do not support changing properties for consumer
				return true;
			}else {
				return false;
			}
		} catch (Exception e) {
			logger.error(e);
			UIUtil.showErrorDialog(
					"Error Occurred While Saving Project Properties", e);
			return false;
		}
	}

	/**
	 * 
	 * create a text and a label.
	 * 
	 * @param composite
	 * @param label
	 * @param isEditable
	 * @param defaultValue
	 */
	private Text createLabeledText(Composite composite, String label,
			boolean isEditable, String defaultValue) {
		new Label(composite, SWT.LEFT).setText(label);
		Text text = new Text(composite, SWT.BORDER);
		text.setEditable(isEditable);
		text.setText(StringUtils.defaultString(defaultValue));
		text.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL
				| GridData.FILL_HORIZONTAL));
		return text;
	}

	private void addServiceIntfClassName(Composite group) {
		createLabeledText(group, "Interface Class:", false, getIntfMetadata()
				.getServiceInterface());

	}

	private void addServiceImplClassName(Group group) {
		createLabeledText(group, "Impl Class:", false,
				((SOAImplProject) getSoaProject()).getMetadata()
						.getServiceImplClassName());
	}

	private void addImplVersion(Group group) {
		String defaultValue = ((SOAImplProject) getSoaProject()).getMetadata()
				.getImplVersion();
		final Text text = createLabeledText(group, "Impl Version:", false,
				defaultValue);
		this.implVersion = text;
		text.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				try {
					validateAll();
				} catch (ValidationInterruptedException e1) {
					processException(e1);
				}

			}
		});

	}

	private void addServiceVersion(Composite group, final boolean editable) {

		oldVersion = getIntfMetadata().getServiceVersion();
		this.serviceVersion = createLabeledText(group, "Service Version:",
				editable, oldVersion);
		this.serviceVersion.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				try {
					validateAll();
				} catch (ValidationInterruptedException e1) {
					processException(e1);
				}

			}
		});

	}
	
	private void addAdminName(Composite group) {
		createLabeledText(group, "Admin Name:", false, getIntfMetadata()
				.getServiceName());
	}

	private void addServiceName(Composite group, final String svcName) {
		createLabeledText(group, "Service Name:", false, svcName);
	}
	
	private void addClientName(Composite comp) {
		createLabeledText(comp, "Client Name:", false,
				getConsumerMetadata().getClientName());
	}
	
	private void addConsumerId(Composite comp) {
		final String conId = getConsumerMetadata().getConsumerId() != null 
		? getConsumerMetadata().getConsumerId() : "";
		createLabeledText(comp, "Consumer ID:", false,
				conId);
	}

	private boolean isValidLayer(final String svcLayer,
			final List<String> requiredSvcLayers) {
		for (final String requiredSvcLayer : requiredSvcLayers) {
			if (SOAServiceUtil.validateServiceLayer(svcLayer, requiredSvcLayer) == false)
				return false;
		}
		return true;
	}

	private void addServiceLayerCombo(Composite group, final boolean enabled)
			throws Exception {
		new Label(group, SWT.LEFT).setText("Service Layer:");
		final List<String> layers = new ArrayList<String>();
		final SOAIntfMetadata intfMetadata = getIntfMetadata();
		final String implProjectName = intfMetadata
				.getImlementationProjectName();
		if (enabled == true && StringUtils.isNotBlank(implProjectName)) {
			// this is an intf project, and we should only show the valid layers
			final IProject project = WorkspaceUtil.getProject(implProjectName);
			if (project != null && project.isAccessible()) {
				final ISOAProject soaProject = GlobalRepositorySystem
						.instanceOf().getActiveRepositorySystem()
						.getAssetRegistry().getSOAProject(project);
				if (soaProject instanceof SOAImplProject) {
					final SOAImplProject implProject = (SOAImplProject) soaProject;
					final List<String> requiredSvcLayers = new ArrayList<String>();
					for (final SOAIntfMetadata metadata : implProject
							.getRequiredServices().values()) {
						requiredSvcLayers.add(metadata.getServiceLayer());
					}
					for (final String layer : ConfigTool
							.getDefaultServiceLayersFromFile()) {
						if (isValidLayer(layer, requiredSvcLayers))
							// we only add those layers that are compatible with
							// the impl's required services
							layers.add(layer);
					}
				}
			}
		} else {
			layers.addAll(ConfigTool.getDefaultServiceLayersFromFile());
		}
		
		if (layers.isEmpty() == true) {
			layers.add(intfMetadata.getServiceLayer());
		}

		Combo combo = new Combo(group, SWT.READ_ONLY | SWT.BORDER);

		this.serviceLayer = combo;
		// Service Layer:
		if (layers != null)
			this.serviceLayer.setItems(layers.toArray(new String[0]));

		if (layers.size() > 0) {
			if (layers.contains(intfMetadata.getServiceLayer()))
				serviceLayer.setText(intfMetadata.getServiceLayer());
			else
				serviceLayer.setText(layers.get(0));
		}
		// the service layer can only be changed if there are more than one
		// layers available for selection
		serviceLayer.setEnabled(enabled && layers.size() > 1
				&& StringUtils.isNotBlank(serviceLayer.getText()));
	}

	private void addBaseConsumerSrcDir(Composite group) {
		final List<SOAProjectSourceDirectory> srcDirs = soaProject
				.getSourceDirectories();
		Collections.sort(srcDirs);
		baseConsumerDir = createLabeledText(group, "Consumer Src Dir:", false,
				getConsumerMetadata().getBaseConsumerSrcDir());
	}

	private SOAIntfMetadata getIntfMetadata() {
		if (soaProject instanceof SOAIntfProject) {
			return ((SOAIntfProject) soaProject).getMetadata();
		} else if (soaProject instanceof SOAImplProject) {
			return ((SOAImplProject) soaProject).getMetadata()
					.getIntfMetadata();
		}
		return null;
	}

	private SOAConsumerMetadata getConsumerMetadata() {
		if (soaProject instanceof SOAImplProject) {
			return ((SOAImplProject) soaProject).getMetadata();
		} else if (soaProject instanceof SOAConsumerProject) {
			return ((SOAConsumerProject) soaProject).getMetadata();
		}
		return null;
	}

	private void validateAll() throws ValidationInterruptedException {

		boolean result = true;
		if (soaProject instanceof SOAIntfProject) {
			result = validateIntfProjectFields();
		} else if (soaProject instanceof SOAImplProject) {
			result = validateImplProjectFields();
		} else if (soaProject instanceof SOAConsumerProject) {
			result = validateConsumerProjectFields();
		}
		if (result == false) {
			setValid(false);
			return;
		}

		setValid(true);
		setErrorMessage(null);
	}

	private boolean validateIntfProjectFields()
			throws ValidationInterruptedException {
		if (serviceVersion != null) {
			final IStatus validationModel = ServiceVersionValidator
					.getInstance().validate(serviceVersion.getText());

			if (validationModel.isOK() == false) {
				setErrorMessage(validationModel.getMessage());
				return false;
			}
			
			if (soaProject instanceof SOAIntfProject) {
				final SOAIntfProject intfProject = (SOAIntfProject) soaProject;
				final Version newVer = new Version(serviceVersion.getText());
				final Version existingVer = new Version(intfProject.getMetadata().getServiceVersion());
				if (newVer.getMajor() != existingVer.getMajor()) {
					setErrorMessage(SOAMessages.ERR_CHANGE_MAJOR_VERSION_NOT_ALLOWED);
					return false;
				}
				IStatus status = 
					ServiceVersionValidator.getInstance().validate(newVer.toString());
				if (status.isOK() == false) {
					setErrorMessage(status.getMessage());
					return false;
				}
			}
			
		}
		return true;
	}

	private boolean validateImplProjectFields()
			throws ValidationInterruptedException {
		if (implVersion != null) {
			final IStatus validationModel = ServiceVersionValidator
					.getInstance().validate(implVersion.getText());

			if (validationModel.isOK() == false) {
				setErrorMessage(validationModel.getMessage());
				return false;
			}
		}
		return true;
	}

	/**
	 * Validate consumer project fields.
	 *
	 * @return true, if successful
	 */
	public boolean validateConsumerProjectFields() {
		if (StringUtils.isNotBlank(baseConsumerDir.getText())) {
			setErrorMessage("Base comsumer source dir could be null");
			return false;
		}
		return true;
	}

	/**
	 * {@inheritDoc}
	 * @see org.eclipse.ui.IWorkbenchPropertyPage#setElement(org.eclipse.core.runtime.IAdaptable)
	 */
	@Override
	public void setElement(IAdaptable element) {
		try {
			if (element.getAdapter(IProject.class) instanceof IProject) {
				IProject project = (IProject) element
						.getAdapter(IProject.class);

				if (TurmericServiceUtils.isSOAProject(project)) {
					soaProject = GlobalRepositorySystem.instanceOf()
							.getActiveRepositorySystem().getAssetRegistry()
							.getSOAProject(project);
				}
			}
		} catch (Exception e) {
			logger.error(e);
		}
	}

	/**
	 * Gets the soa project.
	 *
	 * @return the soa project
	 */
	public ISOAProject getSoaProject() {
		return soaProject;
	}

	private void processException(Exception exception) {
		if (exception != null) {
			UIUtil.showErrorDialog(exception);
			SOALogger.getLogger().error(exception);
		}
	}

}
