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
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.ebayopensource.turmeric.eclipse.core.logging.SOALogger;
import org.ebayopensource.turmeric.eclipse.core.resources.constants.SOAProjectConstants;
import org.ebayopensource.turmeric.eclipse.registry.ExtensionPointFactory;
import org.ebayopensource.turmeric.eclipse.registry.exception.ClientProviderException;
import org.ebayopensource.turmeric.eclipse.registry.intf.IClientRegistryProvider;
import org.ebayopensource.turmeric.eclipse.registry.models.ClientAssetModel;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.GlobalRepositorySystem;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.ISOAHelpProvider;
import org.ebayopensource.turmeric.eclipse.repositorysystem.utils.TurmericServiceUtils;
import org.ebayopensource.turmeric.eclipse.resources.model.AssetInfo;
import org.ebayopensource.turmeric.eclipse.resources.util.SOAConsumerUtil.EnvironmentItem;
import org.ebayopensource.turmeric.eclipse.ui.components.AbstractSOAServiceListViewer;
import org.ebayopensource.turmeric.eclipse.ui.wizards.pages.AbstractNewServiceWizardPage;
import org.ebayopensource.turmeric.eclipse.utils.collections.ListUtil;
import org.ebayopensource.turmeric.eclipse.utils.plugin.JDTUtil;
import org.ebayopensource.turmeric.eclipse.utils.ui.UIUtil;
import org.ebayopensource.turmeric.eclipse.validator.core.ErrorMessage;
import org.ebayopensource.turmeric.eclipse.validator.utils.common.RegExConstants;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;


/**
 * The Class ConsumerFromJavaWizardPage.
 *
 * @author yayu
 */
public class ConsumerFromJavaWizardPage extends AbstractNewServiceWizardPage {
	private AbstractSOAServiceListViewer serviceList;
	private final IStructuredSelection selection;
	private Text consumerID;
	private Button retrieveConsumerIDBtn;
	private CCombo baseConsumerDirs;
	//whether for converting an existing Java project into a Consumer project
	private boolean convertExistingJavaProject = false;
	private static final SOALogger logger = SOALogger.getLogger();

	/**
	 * Instantiates a new consumer from java wizard page.
	 *
	 * @param selection the selection
	 */
	public ConsumerFromJavaWizardPage(IStructuredSelection selection) {
		super("newSOAClientProjectWizardPage", "New SOA Service Client Wizard",
				"This wizard creates a new SOA Service Client project.");
		this.selection = selection;
	}
	
	/**
	 * Instantiates a new consumer from java wizard page.
	 *
	 * @param selection the selection
	 * @param convertExistingJavaProject the convert existing java project
	 */
	public ConsumerFromJavaWizardPage(IStructuredSelection selection,
			boolean convertExistingJavaProject) {
		this(selection);
		this.convertExistingJavaProject = convertExistingJavaProject;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void createControl(Composite parent) {
		try {
			final Composite container = super.createParentControl(parent, 4);
			addWorkspaceRootChooser(container);
			Text serviceClientText = super.createResourceNameControl(
					container, "&Client Name:", modifyListener, true, 
					"the client project name");
			createConsumerIDText(container);
			createBaseConsumerSource(container);
			createServiceList(container);
			dialogChanged();
			serviceClientText.setFocus();
		} catch (Exception e) {
			SOALogger.getLogger().error(e);
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Creates the consumer id text.
	 *
	 * @param parent the parent
	 * @return the text
	 * @throws CoreException the core exception
	 */
	protected Text createConsumerIDText(Composite parent) throws CoreException {
		this.consumerID = super.createLabelTextField(parent, "Consumer &ID:", "", modifyListener, 
				false, true, 
				"the consumer ID of the new service consumer");
		final IClientRegistryProvider clientRegProvider 
		= ExtensionPointFactory.getSOAClientRegistryProvider();
		if (clientRegProvider != null) {
			//The retrieve button should only be created if AR plugin is available
			retrieveConsumerIDBtn = new Button(parent, SWT.PUSH);
			retrieveConsumerIDBtn.setText("Retrie&ve");
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
								consumerID.setText(conID);
								/*if (StringUtils.isNotBlank(conID))
									retrieveConsumerIDBtn.setEnabled(false);*/
							}
						} catch (ClientProviderException e1) {
							SOALogger.getLogger().error(e1);
							UIUtil.showErrorDialog(e1);
						}
					}
				}
			});
			
			final Text text = getResourceNameText();
			
			text.addModifyListener(new ModifyListener() {
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
		
		return this.consumerID;
	}
	
	private void setRetrieveConsumerIDEnabled() throws CoreException {
		if (retrieveConsumerIDBtn != null) {
			retrieveConsumerIDBtn.setEnabled(
					ExtensionPointFactory.getSOAClientRegistryProvider() != null && 
					StringUtils.isNotBlank(getResourceName()));
		}
	}
	

	/**
	 * Creates the service list.
	 *
	 * @param parent the parent
	 * @throws Exception the exception
	 */
	protected void createServiceList(final Composite parent)
	throws Exception {
		serviceList = new AbstractSOAServiceListViewer(null, 
				ISOAHelpProvider.WINDOW_SELECT_SERVICE)
		{
			@Override
			protected void enviromentAdded(String environmentName,
					EnvironmentItem environmentForCopy) {
				dialogChanged();
			}

			@Override
			protected void environmentRemoved(EnvironmentItem environment) {
				dialogChanged();
			}
			@Override
			protected void serviceAdded(Collection<AssetInfo> services) {
				if (StringUtils.isBlank(getClientName()) && services.size() == 1) {
					//no client project name yet, and this is the first added service
					final AssetInfo assetInfo = services.iterator().next();
					getResourceNameText().setText(assetInfo.getName() 
							+ SOAProjectConstants.CLIENT_PROJECT_SUFFIX);
				}
				dialogChanged();
			}

			@Override
			protected void serviceRemoved(Object removedService) {
				dialogChanged();
			}
			
		};
		
		
		final IProject selectedProject = getSelectedProject();
		List<EnvironmentItem> items = new ArrayList<EnvironmentItem>();
		EnvironmentItem item = new EnvironmentItem(SOAProjectConstants.DEFAULT_CLIENT_CONFIG_ENVIRONMENT);
		items.add(item);
		final AssetInfo selectedServiceInfo = getSelectedServiceInfo(selectedProject);
		if (selectedServiceInfo != null) {
			getResourceNameText().setText(selectedServiceInfo.getName()
					+ SOAProjectConstants.CLIENT_PROJECT_SUFFIX);
			item.addServiceData(selectedServiceInfo);
		} else if (selectedProject != null && convertExistingJavaProject == true
				&& selectedProject.isAccessible() &&
				TurmericServiceUtils.isSOAProject(selectedProject) == false) {
			//not a SOA project yet, convert it first
			final Text clientProjectName = getResourceNameText();
			clientProjectName.setText(selectedProject.getName());
			clientProjectName.setEditable(false);
		}
		serviceList.createControl(parent, items);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Text createBaseConsumerSource(Composite parent) {
		//final Text text = super.createBaseConsumerSource(parent);
		final IProject project = this.getSelectedProject();
		try {
			if (project != null && convertExistingJavaProject == true 
					&& project.isAccessible() 
					&& TurmericServiceUtils.isSOAProject(project) == false) {
				final List<IPath> dirs = JDTUtil.getSourceDirectories(project);
				final List<String> items = new ArrayList<String>(dirs.size());
				for (IPath path : dirs) {
					items.add(project.getFullPath().isPrefixOf(path) ? 
							path.removeFirstSegments(1).toString() : path.toString());
				}
				this.baseConsumerDirs = super.createCCombo(parent, "&BaseConsumer Source Dir:", 
						false, items.toArray(new String[0]), "the source directory of the base consumer class");
				if (items.contains(SOAProjectConstants.FOLDER_SRC)) {
					this.baseConsumerDirs.setText(SOAProjectConstants.FOLDER_SRC);
				} 
				this.baseConsumerDirs.addSelectionListener(new SelectionAdapter() {

					@Override
					public void widgetSelected(SelectionEvent e) {
						dialogChanged();
					}
				});
			} else {
				return super.createBaseConsumerSource(parent);
			}
		} catch (CoreException e) {
			SOALogger.getLogger().error(e);
			throw new RuntimeException(e);
		}
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getBaseConsumerSrcDir() {
		if (this.baseConsumerDirs != null) {
			return getTextValue(this.baseConsumerDirs);
		}
		return super.getBaseConsumerSrcDir();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Control getBaseConsumerSrcControl() {
		if (this.baseConsumerDirs != null) {
			return this.baseConsumerDirs;
		}
		return super.getBaseConsumerSrcControl();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected boolean supportServiceValidation() {
		//this is only for creating consumer, no need to check service existence.
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected boolean dialogChanged() {
		boolean result = super.dialogChanged();
		if (result == false)
			return result;
		if (this.serviceList != null) {
			if (this.serviceList.isEnvironmentListEmpty()) {
				updateStatus(this.serviceList.getServiceList().getTree(), 
						"At least one environment must be added.");
				return false;
			}
			
			if (this.serviceList.isServiceListEmpty()) {
				updateStatus(this.serviceList.getServiceList().getTree(), 
						"At least one service must be added.");
				return false;
			}
		}
		
		final String clientName = getClientName();
		
		if (StringUtils.isBlank(clientName)) {
			updateStatus(super.getResourceNameText(), 
					"Client name must not be empty");
			return false;
		}
		
		if (validateName(super.getResourceNameText(), 
				clientName,
				RegExConstants.PROJECT_NAME_EXP,
				ErrorMessage.CLIENT_NAME_ERRORMSG) == false) {
			return false;
		}
		
		if (StringUtils
				.equals(StringUtils.capitalize(getClientName()),
						getClientName()) == false) {
			updateStatus(super.getResourceNameText(), "Client name must be capitalized.");
			return false;
		}
		
		updateStatus(null);
		return true;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<ProjectNameControl> getProjectNames() {
		if (convertExistingJavaProject == true) {
			//we do not check the project existence if this is converting 
			//an existing Java project into a SOA consumer project.
			return ListUtil.array();
		}
		return super.getProjectNames();
	}

	private IProject getSelectedProject() {
		if (selection == null)
			return null;
		if (!(selection.getFirstElement() instanceof IAdaptable))
			return null;
		final IAdaptable adaptable = (IAdaptable) selection.getFirstElement();
		final IProject project = (IProject) adaptable
				.getAdapter(IProject.class);
		return project;
	}

	/* (non-Javadoc)
	 * @see org.ebayopensource.turmeric.eclipse.ui.wizards.pages.AbstractNewServiceWizardPage#getDefaultResourceName()
	 */
	@Override
	public String getDefaultResourceName() {
		return "";
	}
	
	/**
	 * Gets the consumer id.
	 *
	 * @return the consumer id
	 */
	public String getConsumerID() {
		return super.getTextValue(this.consumerID);
	}
	
	/**
	 * Gets the client name.
	 *
	 * @return the client name
	 */
	public String getClientName() {
		return getResourceName();
	}

	private static AssetInfo getSelectedServiceInfo(final IProject project) 
	throws Exception {
		if (project != null && project.isAccessible() 
				&& TurmericServiceUtils.isSOAInterfaceProject(project)) {
			return GlobalRepositorySystem.instanceOf()
			.getActiveRepositorySystem().getAssetRegistry().getProjectInfo(project.getName());
		}

		/*
		 * final ISOABaseProject baseProject = SOACore.getSOAProject(
		 * project.getName() ); if( baseProject == null ) return "";
		 */
		return null;// baseProject.getProperty(
		// ISOAInterfaceProject.SERVICE_NAME );
	}

	/**
	 * Gets the service list.
	 *
	 * @return the service list
	 */
	public List<String> getServiceList() {
		final List<String> result = ListUtil.list();
		if (this.serviceList != null) {
			final List<EnvironmentItem> items = this.serviceList.getServiceInfoList();
			if (items.size() > 0) {
				result.addAll(items.get(0).getServices());
			}
		}
		return result;
	}
	
	/**
	 * Gets the environments.
	 *
	 * @return the environments
	 */
	public List<String> getEnvironments() {
		final List<String> result = ListUtil.list();
		if (this.serviceList != null) {
			final List<EnvironmentItem> items = this.serviceList.getServiceInfoList();
			for (EnvironmentItem item : items) {
				result.add(item.getName());
			}
		}
		return result;
	}
	
	/**
	 * Gets the help id.
	 *
	 * @return the help id
	 */
	public int getHelpID() {
		final int helpID = convertExistingJavaProject ? 
				ISOAHelpProvider.PAGE_CONSUME_NEW_SERVICE 
				: ISOAHelpProvider.PAGE_CREATE_CONSUMER;
		return helpID;
	}

	/* (non-Javadoc)
	 * @see org.ebayopensource.turmeric.eclipse.ui.SOABasePage#getHelpContextID()
	 */
	@Override
	public String getHelpContextID() {
		
		return GlobalRepositorySystem.instanceOf().getActiveRepositorySystem()
		.getHelpProvider().getHelpContextID(getHelpID());
	}
	
}
