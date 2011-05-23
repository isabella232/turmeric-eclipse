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
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang.StringUtils;
import org.ebayopensource.turmeric.eclipse.core.logging.SOALogger;
import org.ebayopensource.turmeric.eclipse.core.resources.constants.SOAProjectConstants;
import org.ebayopensource.turmeric.eclipse.exception.validation.ValidationInterruptedException;
import org.ebayopensource.turmeric.eclipse.registry.ExtensionPointFactory;
import org.ebayopensource.turmeric.eclipse.registry.exception.ClientProviderException;
import org.ebayopensource.turmeric.eclipse.registry.intf.IClientRegistryProvider;
import org.ebayopensource.turmeric.eclipse.registry.models.ClientAssetModel;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.GlobalRepositorySystem;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.ISOAHelpProvider;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.ISOARepositorySystem;
import org.ebayopensource.turmeric.eclipse.repositorysystem.utils.TurmericServiceUtils;
import org.ebayopensource.turmeric.eclipse.resources.model.AssetInfo;
import org.ebayopensource.turmeric.eclipse.resources.util.SOAConsumerUtil;
import org.ebayopensource.turmeric.eclipse.resources.util.SOAConsumerUtil.EnvironmentItem;
import org.ebayopensource.turmeric.eclipse.ui.SOABasePage;
import org.ebayopensource.turmeric.eclipse.ui.components.AbstractSOAServiceListViewer;
import org.ebayopensource.turmeric.eclipse.utils.collections.SetUtil;
import org.ebayopensource.turmeric.eclipse.utils.plugin.WorkspaceUtil;
import org.ebayopensource.turmeric.eclipse.utils.ui.UIUtil;
import org.ebayopensource.turmeric.eclipse.validator.core.ErrorMessage;
import org.ebayopensource.turmeric.eclipse.validator.core.InputObject;
import org.ebayopensource.turmeric.eclipse.validator.utils.common.NameValidator;
import org.ebayopensource.turmeric.eclipse.validator.utils.common.RegExConstants;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;


/**
 * The Class ConsumeNewServiceWizardPage.
 *
 * @author yayu
 */
public class ConsumeNewServiceWizardPage extends SOABasePage {
	private AbstractSOAServiceListViewer serviceList;
    private Set<AssetInfo> addedServices = new LinkedHashSet<AssetInfo>();
    private Map<String, EnvironmentItem> addedEnvironments = new ConcurrentHashMap<String, EnvironmentItem>();
    private Set<AssetInfo> existingServices = new LinkedHashSet<AssetInfo>();
    private List<String> existingEnvironments = new ArrayList<String>();
    private IProject project;
    
    private Text clientName;
    private Text consumerId;
    private String scppVersion;
    private Button retrieveConsumerIDBtn;
    private static final SOALogger logger = SOALogger.getLogger();
    
	/**
	 * Instantiates a new consume new service wizard page.
	 *
	 * @param project the project
	 */
	public ConsumeNewServiceWizardPage(final IProject project) {
		super("consumeNewServiceWizardPage");
		setTitle("Set Up Consumer Configurations");
		setDescription("Set up configurations for environments and services.");
		this.project = project;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void createControl(Composite parent) {
		try {
			Composite composite = new Composite(parent, SWT.NONE);
			composite.setLayoutData(new GridData(GridData.FILL_BOTH));
			composite.setLayout(new GridLayout(4, false));
			
			if (project != null) {
				final IFile propsFile = SOAConsumerUtil.getConsumerPropertiesFile(project);
				//the client name is default to be the project name
				String clientName = TurmericServiceUtils.isSOAImplProject(project) ? "" : this.project.getName();
				String consumerId = "";
				if (propsFile.exists() == true) {
					final Properties props = SOAConsumerUtil.loadConsumerProperties(project);
					clientName = StringUtils.trim(props.getProperty(
							SOAProjectConstants.PROPS_KEY_CLIENT_NAME, clientName));
					consumerId = StringUtils.trim(props.getProperty(
							SOAProjectConstants.PROPS_KEY_CONSUMER_ID, ""));
					scppVersion = StringUtils.trim(props.getProperty(
							SOAProjectConstants.PROPS_KEY_SCPP_VERSION, ""));
				} else {
					logger.warning(
							"The underlying projects do not have service_consumer_project.properties file->", 
							project.getName());
				}
				
				final Text clientNameText = createClientNameText(composite, clientName);
				if (TurmericServiceUtils.isSOAImplProject(project) 
						&& TurmericServiceUtils.isSOAConsumerProject(project) == false
						&& StringUtils.isEmpty(clientName)) {
					clientNameText.setText(StringUtils.substringBefore(project.getName(), 
							SOAProjectConstants.IMPL_PROJECT_SUFFIX) + SOAProjectConstants.CLIENT_PROJECT_SUFFIX);
				} else {
					clientNameText.setText(clientName);
				}
				createConsumerIDText(composite, consumerId);
				createServiceList(composite);
			}
			setControl(composite);
			dialogChanged();
		} catch (Exception e) {
			SOALogger.getLogger().error(e);
			UIUtil.showErrorDialog(e);
		}
		UIUtil.getHelpSystem().setHelp(
				parent, getHelpContextID());
	}
	
	/**
	 * Creates the client name text.
	 *
	 * @param parent the parent
	 * @param defaultValue the default value
	 * @return the text
	 */
	protected Text createClientNameText(Composite parent, String defaultValue) {
		this.clientName = super.createLabelTextField(parent, "Client &Name:", 
				defaultValue, modifyListener, 
				"the client name of the consumer");
		this.clientName.setEditable(StringUtils.isBlank(defaultValue));
		return clientName;
	}
	
	/**
	 * Creates the consumer id text.
	 *
	 * @param parent the parent
	 * @param defaultValue the default value
	 * @return the text
	 * @throws CoreException the core exception
	 */
	protected Text createConsumerIDText(Composite parent, String defaultValue) throws CoreException {
		this.consumerId = super.createLabelTextField(parent, "Consumer &ID:", 
				defaultValue, modifyListener, false, false,
				"the consumer ID of the consumer");
		boolean editable = false;
		//this field is only editable if 
		//1) the underlying project is an impl project and has not consumed any services yet
		//2) the underlying project is consumer and created with scpp version.
		if (StringUtils.isBlank(defaultValue)) {
			//no consumer id yet
			if (StringUtils.isNotBlank(scppVersion)) {
				//a new project
				editable = true;
			} else if (TurmericServiceUtils.isSOAImplProject(project)
					&& TurmericServiceUtils.isSOAConsumerProject(project) == false) {
				editable = true;
			}
		}
		this.consumerId.setEditable(editable);
		final IClientRegistryProvider clientRegProvider 
		= ExtensionPointFactory.getSOAClientRegistryProvider();
		
		if (clientRegProvider != null && editable == true) {
			this.retrieveConsumerIDBtn = new Button(parent, SWT.PUSH);
			this.retrieveConsumerIDBtn.setText("Retrie&ve");
			setRetrieveConsumerIDEnabled();
			retrieveConsumerIDBtn.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					if (clientRegProvider != null) {
						try {
							ClientAssetModel clientModel = 
								clientRegProvider.getClientAsset(getClientName());
							if (clientModel != null) {
								String conID = StringUtils.isBlank(clientModel.getConsumerId()) 
								? "" : clientModel.getConsumerId();
								consumerId.setText(conID);
							}
						} catch (ClientProviderException e1) {
							SOALogger.getLogger().error(e1);
							UIUtil.showErrorDialog(e1);
						}
					}
				}
			});
			
			this.clientName.addModifyListener(new ModifyListener() {
				@Override
				public void modifyText(ModifyEvent e) {
					try {
						setRetrieveConsumerIDEnabled();
					} catch (CoreException e1) {
						logger.warning(e1);
					}
				}
			});
		} else {
			//AR plugin is not available
			super.createEmptyLabel(parent, 1);
		}
		return consumerId;
	}
	
	private void setRetrieveConsumerIDEnabled() throws CoreException {
		if (retrieveConsumerIDBtn != null) {
			retrieveConsumerIDBtn.setEnabled(
					ExtensionPointFactory.getSOAClientRegistryProvider() != null && 
					StringUtils.isNotBlank(getClientName()));
		}
	}
	
	/**
	 * Creates the service list.
	 *
	 * @param parent the parent
	 * @return the composite
	 * @throws Exception the exception
	 */
	protected Composite createServiceList(Composite parent) throws Exception {
		final Map<String, AssetInfo> dependencies = new ConcurrentHashMap<String, AssetInfo>();
		for (final AssetInfo info : GlobalRepositorySystem.instanceOf()
				.getActiveRepositorySystem()
				.getAssetRegistry().getDependencies(project.getName())) {
			dependencies.put(info.getName(), info);
		}
		
		final List<EnvironmentItem> items = 
			SOAConsumerUtil.getClientConfigStructure(project);
		
		for (EnvironmentItem item : items) {
			for (String serviceName : item.getServices()) {
				AssetInfo asset = dependencies.get(serviceName);
				if (asset == null) {
					logger.warning("Could not service->", serviceName, " in the project dependency list->", project);
				} else {
					item.addServiceData(asset);
					existingServices.add(asset);
				}
			}
			existingEnvironments.add(item.getName());
		}
		
		this.serviceList = new AbstractSOAServiceListViewer(project, 
				ISOAHelpProvider.WINDOW_SELECT_SERVICE) {

			@Override
			protected void serviceAdded(Collection<AssetInfo> services) {
				addedServices.addAll(services);
				dialogChanged();
			}

			@Override
			protected void serviceRemoved(Object removedService) {
				addedServices.remove(removedService);
				dialogChanged();
			}

			@Override
			protected void enviromentAdded(String environmentName,
					EnvironmentItem environmentForCopy) {
				addedEnvironments.put(environmentName, environmentForCopy);
				dialogChanged();
			}

			@Override
			protected void environmentRemoved(EnvironmentItem environment) {
				addedEnvironments.remove(environment.getName());
				dialogChanged();
			}
			
		};
		if (items.isEmpty()) {
			//an impl project that does not have any environment yet
			items.add(new EnvironmentItem(SOAProjectConstants.DEFAULT_CLIENT_CONFIG_ENVIRONMENT));
		}
		final Composite container = this.serviceList.createControl(parent, items);
		return container;
	}
	
	/**
	 * Key is the description of the AssetInfo instance.
	 *
	 * @return the added services info
	 */
	public Set<AssetInfo> getAddedServicesInfo() {
		return addedServices;
	}
	
	/**
	 * Gets the services.
	 *
	 * @return the services
	 */
	public Set<AssetInfo> getServices() {
		final Set<AssetInfo> services = new LinkedHashSet<AssetInfo>();
		if (this.serviceList != null) {
			final List<EnvironmentItem> items = this.serviceList.getServiceInfoList();
			if (items.size() > 0) {
				for (Object service : items.get(0).getServiceData().values()) {
					if (service instanceof AssetInfo)
						services.add((AssetInfo)service);
				}
			}
		}
		
		return services;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected boolean dialogChanged() {
		super.dialogChanged();
		boolean pageComplete = false;
		try {
			if (project == null || serviceList == null)
				return true;
			final List<EnvironmentItem> infoList = serviceList.getServiceInfoList();
			
			if (TurmericServiceUtils.isSOAImplProject(project) == true) {
				final String clientName = getClientName();
				if (StringUtils.isBlank(clientName) && 
						(infoList.isEmpty() == false || 
								infoList.get(0).getServices().isEmpty() == false)) {
					//client name is empty, but user has added environments and services
					updateStatus(this.clientName, "Client name must not be empty");
					return false;
				}
				
				if (StringUtils.isNotBlank(clientName)) {
					if (clientName.equalsIgnoreCase(project.getName())) {
						updateStatus(this.clientName, "Client name should not be same as the implementation project name");
						return false;
					}
					IProject clientProject = WorkspaceUtil.getProject(clientName);
					if(clientProject.exists() && TurmericServiceUtils.isSOAConsumerProject(clientProject)) {
						updateStatus(this.clientName, 
								"Client name should not be same as the name of an existing consumer project->" + clientName);
						return false;
					}
				}
				
				try {
					final InputObject inputObject = new InputObject(clientName,
							RegExConstants.PROJECT_NAME_EXP,
							ErrorMessage.PROJECT_NAME_ERRORMSG + 
							" The name [" + clientName + "] is not valid against the pattern \"" 
							+ RegExConstants.PROJECT_NAME_EXP + "\"");
					IStatus validationModel = NameValidator.getInstance().validate(
							inputObject);
					if (checkValidationResult(this.clientName, 
							validationModel) == false)
						return false;
					
					final ISOARepositorySystem activeRepositorySystem = 
						GlobalRepositorySystem
						.instanceOf().getActiveRepositorySystem();
					validationModel = activeRepositorySystem.getServiceValidator()
					.validate(clientName);
					if (checkValidationResult(this.clientName, 
							validationModel) == false)
						return false;
					
					
				} catch (ValidationInterruptedException e) {
					processException(e);
				}
				
				if (infoList.isEmpty() == false && 
						infoList.get(0).getServices().isEmpty() == true) {
					updateStatus(serviceList.getServiceList().getTree(), 
							"At least one service must be added");
					return false;
				}
				if (TurmericServiceUtils.isSOAConsumerProject(project) == false) {
					//the underlying impl projects have not consumed any services yet
					if (infoList.isEmpty() == true) {
						updateStatus(serviceList.getServiceList().getTree(), 
								"At least one environment must be added");
						return false;
					}
				}
				pageComplete = true;
			} else if (TurmericServiceUtils.isSOAConsumerProject(project)) {
				//has consumer nature
				if (TurmericServiceUtils.isSOAImplProject(project) == false) {
					//not an impl project
					if (infoList.isEmpty() == true) {
						updateStatus(serviceList.getServiceList().getTree(), 
								"At least one environment must be added");
						return false;
					}
					if (infoList.get(0).getServices().isEmpty() == true) {
						updateStatus(serviceList.getServiceList().getTree(), 
								"At least one service must be added");
						return false;
					}
				}
				pageComplete = true;
			}
		} catch (CoreException e) {
			SOALogger.getLogger().error(e);
			UIUtil.showErrorDialog(e);
			this.setPageComplete(false);
			return false;
		}
		
		this.setPageComplete(pageComplete);
		return true;
	}
	
	/**
	 * Gets the client name.
	 *
	 * @return the client name
	 */
	public String getClientName() {
		return this.clientName.getText();
	}
	
	/**
	 * Gets the consumer id.
	 *
	 * @return the consumer id
	 */
	public String getConsumerId() {
		return this.consumerId.getText();
	}
	
	/**
	 * Checks if is consumer id editable.
	 *
	 * @return true, if is consumer id editable
	 */
	public boolean isConsumerIdEditable() {
		return this.consumerId.getEditable();
	}
	
	/*
	 * Non destructive calculation
	 */
	/**
	 * Gets the removed services.
	 *
	 * @return the removed services
	 */
	public Set<AssetInfo> getRemovedServices() {
		Set<AssetInfo> existingLibrariesSet = SetUtil.linkedSet(existingServices);
		
		// now this has the removed libraries, could be empty
		existingLibrariesSet.removeAll(getServices());
		return existingLibrariesSet;
	}

	/**
	 * Gets the added services.
	 *
	 * @return the added services
	 */
	public Set<AssetInfo> getAddedServices() {
		Set<AssetInfo> newLibrariesSet = getServices();
		newLibrariesSet.removeAll(existingServices);
		// now this has the added new libraries, could be empty
		return newLibrariesSet;
	}
	
	/**
	 * Gets the environments.
	 *
	 * @return the environments
	 */
	public Set<String> getEnvironments() {
		final Set<String> environments = new LinkedHashSet<String>();
		if (this.serviceList != null) {
			for(EnvironmentItem item : this.serviceList.getServiceInfoList()) {
				environments.add(item.getName());
			}
		}
		return environments;
	}
	
	/**
	 * Gets the added environments.
	 *
	 * @return the added environments
	 */
	public Map<String, EnvironmentItem> getAddedEnvironments() {
		return addedEnvironments;
	}
	
	/**
	 * Gets the removed environments.
	 *
	 * @return the removed environments
	 */
	public Set<String> getRemovedEnvironments() {
		Set<String> envs = SetUtil.hashSet(existingEnvironments);
		envs.removeAll(getEnvironments());
		return envs;
	}

	/* (non-Javadoc)
	 * @see org.ebayopensource.turmeric.eclipse.ui.SOABasePage#getDefaultValue(org.eclipse.swt.widgets.Text)
	 */
	@Override
	public String getDefaultValue(Text text) {
		return "";
	}
	
	/* (non-Javadoc)
	 * @see org.ebayopensource.turmeric.eclipse.ui.SOABasePage#getHelpContextID()
	 */
	@Override
	public String getHelpContextID() {
		return GlobalRepositorySystem.instanceOf().getActiveRepositorySystem()
		.getHelpProvider().getHelpContextID(
				ISOAHelpProvider.PAGE_ADD_REMOVE_REQUIRED_SERVICES);
	}
}
