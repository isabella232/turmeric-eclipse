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
import java.util.List;
import java.util.Map;

import javax.wsdl.Definition;
import javax.wsdl.Service;

import org.apache.commons.lang.StringUtils;
import org.ebayopensource.turmeric.eclipse.core.logging.SOALogger;
import org.ebayopensource.turmeric.eclipse.core.resources.constants.SOAProjectConstants;
import org.ebayopensource.turmeric.eclipse.core.resources.constants.SOAProjectConstants.SupportedProjectType;
import org.ebayopensource.turmeric.eclipse.registry.ExtensionPointFactory;
import org.ebayopensource.turmeric.eclipse.registry.intf.IClientRegistryProvider;
import org.ebayopensource.turmeric.eclipse.registry.models.ClientAssetModel;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.GlobalRepositorySystem;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.ISOAHelpProvider;
import org.ebayopensource.turmeric.eclipse.resources.model.ISOAProject;
import org.ebayopensource.turmeric.eclipse.resources.model.SOAConsumerMetadata;
import org.ebayopensource.turmeric.eclipse.resources.model.SOAConsumerProject;
import org.ebayopensource.turmeric.eclipse.resources.model.SOAImplMetadata;
import org.ebayopensource.turmeric.eclipse.resources.model.SOAImplProject;
import org.ebayopensource.turmeric.eclipse.resources.util.SOAConsumerUtil;
import org.ebayopensource.turmeric.eclipse.resources.util.SOAIntfUtil;
import org.ebayopensource.turmeric.eclipse.resources.util.SOAServiceUtil;
import org.ebayopensource.turmeric.eclipse.services.ui.SOAMessages;
import org.ebayopensource.turmeric.eclipse.ui.AbstractSOADomainWizard;
import org.ebayopensource.turmeric.eclipse.ui.wizards.pages.AbstractNewServiceFromWSDLWizardPage;
import org.ebayopensource.turmeric.eclipse.utils.lang.StringUtil;
import org.ebayopensource.turmeric.eclipse.utils.plugin.EclipseMessageUtils;
import org.ebayopensource.turmeric.eclipse.utils.ui.UIUtil;
import org.ebayopensource.turmeric.eclipse.validator.core.ErrorMessage;
import org.ebayopensource.turmeric.eclipse.validator.utils.common.RegExConstants;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.jface.fieldassist.FieldDecoration;
import org.eclipse.jface.fieldassist.FieldDecorationRegistry;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Text;


/**
 * The Class ConsumeServiceFromExistingWSDLWizardPage.
 *
 * @author mzang
 */
public class ConsumeServiceFromExistingWSDLWizardPage extends
		AbstractNewServiceFromWSDLWizardPage {
	private Text serviceClientText;
	private Text consumerID;
	private Text adminText;
	private Button retrieveConsumerIDBtn;
	private ListViewer envrionmentList;
	private static final SOALogger logger = SOALogger.getLogger();

	private String versionFromWSDL = null;

	private ISOAProject soaPrj;

	/** The client prop editable. */
	boolean clientPropEditable = false;

	private String consumerIDStr = "";
	private String clientNameStr = "";
	private List<String> environments = new ArrayList<String>();
	private String serviceLayerStr = "";
	private String domainName = "";
	
	private static final String ADV_MODE_TITLE = "This wizard creates a SOA Intf Project from a pre-existing WSDL document and add it to selected consumer.";

	/**
	 * Instantiates a new consume service from existing wsdl wizard page.
	 *
	 * @param selection the selection
	 * @throws Exception the exception
	 */
	public ConsumeServiceFromExistingWSDLWizardPage(
			final IStructuredSelection selection) throws Exception {
		super("ConsumeServiceFromWSDLWizardPage",
				"Consume Service From Existing WSDL Wizard", ADV_MODE_TITLE);
		IProject project = (IProject) selection.getFirstElement();
		soaPrj = GlobalRepositorySystem.instanceOf()
				.getActiveRepositorySystem().getAssetRegistry()
				.getSOAProject(project);
		String consumerNatureId = GlobalRepositorySystem.instanceOf()
				.getActiveRepositorySystem()
				.getProjectNatureId(SupportedProjectType.CONSUMER);
		if ((soaPrj instanceof SOAImplProject == true)
				&& (project.hasNature(consumerNatureId) == false)) {
			// if this project is just a impl project
			SOAImplMetadata metadata = ((SOAImplProject) soaPrj).getMetadata();
			consumerIDStr = "";
			clientNameStr = StringUtils.substringBefore(project.getName(),
					SOAProjectConstants.IMPL_PROJECT_SUFFIX)
					+ SOAProjectConstants.CLIENT_PROJECT_SUFFIX;
			environments
					.add(SOAProjectConstants.DEFAULT_CLIENT_CONFIG_ENVIRONMENT);
			clientPropEditable = true;
			serviceLayerStr = metadata.getIntfMetadata().getServiceLayer();
		} else if ((soaPrj instanceof SOAConsumerProject)
				&& (project.hasNature(consumerNatureId) == true)) {
			SOAConsumerMetadata metadata = ((SOAConsumerProject) soaPrj)
					.getMetadata();
			consumerIDStr = metadata.getConsumerId();
			clientNameStr = metadata.getClientName();
			try {
				environments = SOAConsumerUtil.getClientEnvironmentList(
						soaPrj.getProject(), null);
			} catch (CoreException e) {
				logger.error("Unable to load environments.", e);
				UIUtil.showErrorDialog("Unable to load environments.", e);
			}
			clientPropEditable = false;
		}
	}

	/**
	 * Gets the sOA project.
	 *
	 * @return the sOA project
	 */
	public ISOAProject getSOAProject() {
		return soaPrj;
	}

	/**
	 * Instantiates a new consume service from existing wsdl wizard page.
	 *
	 * @throws Exception the exception
	 */
	public ConsumeServiceFromExistingWSDLWizardPage() throws Exception {
		this(null);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setVisible(boolean visible) {
		super.setVisible(visible);
		if (visible == true) {
			dialogChanged(true);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void createControl(Composite parent) {
		try {
			final Composite container = super.createParentControl(parent, 4);
			addWSDL(container);
			
			addWorkspaceRootChooser(container);
			addServiceName(container, false);
			this.adminText = addAdminName(container, false);

			{
				//advanced section
				Composite advancedPanel = super.createAdvancedSettingsPanel(container, null);
				if (serviceDomainList != null && domainClassifierList != null) {
					this.serviceDomainList.select(-1);
					this.serviceDomainList.clearSelection();
					this.domainClassifierList.select(-1);
					this.domainClassifierList.clearSelection();
				}
				{
					if (this.resourceNameControlDecoration != null) {
						// we do not want to show both WARNING and INFORMATION icons
						resourceNameControlDecoration.hide();
					}
					ControlDecoration controlDecoration = new ControlDecoration(
							adminText, SWT.LEFT | SWT.TOP);
					controlDecoration.setShowOnlyOnFocus(false);
					controlDecoration
							.setDescriptionText(SOAMessages.WARNING_ADMIN_NAME_MANUAL_OVERRIDE);
					FieldDecoration fieldDecoration = FieldDecorationRegistry
							.getDefault().getFieldDecoration(
									FieldDecorationRegistry.DEC_WARNING);
					controlDecoration.setImage(fieldDecoration.getImage());
				}
				// intf related
				addTargetNamespace(advancedPanel, "", false);
				addServicePackage(advancedPanel);
				// consumer related.
				createServiceClient(advancedPanel, clientPropEditable);
				createConsumerIDText(advancedPanel).setEditable(clientPropEditable);
				createEnvironmentList(advancedPanel);
				addWSDLPackageToNamespace(advancedPanel);
				addTypeFolding(advancedPanel).setVisible(false);
				super.setTypeFolding(false);
				
				if (clientPropEditable == true) {
					adminText.addModifyListener(new ModifyListener() {

						@Override
						public void modifyText(ModifyEvent e) {
							if (serviceClientText.getEditable() == false) {
								serviceClientText
										.setText(getDefaultValue(serviceClientText));
							}
						}

					});
				}
			}
			/*final Composite container = super.createParentControl(parent, 4);
			// service related
			Text wsdlText = addWSDL(container);
			wsdlText.setFocus();
			addWorkspaceRootChooser(container);
			addServiceDomainList(container, true);
			if (serviceDomainList != null && domainClassifierList != null) {
				this.serviceDomainList.select(-1);
				this.serviceDomainList.clearSelection();
				this.domainClassifierList.select(-1);
				this.domainClassifierList.clearSelection();
			}
			addServiceVersion(container);
			addServiceName(container, false);
			this.adminText = addAdminName(container, false);
			{
				if (this.resourceNameControlDecoration != null) {
					// we do not want to show both WARNING and INFORMATION icons
					resourceNameControlDecoration.hide();
				}
				ControlDecoration controlDecoration = new ControlDecoration(
						adminText, SWT.LEFT | SWT.TOP);
				controlDecoration.setShowOnlyOnFocus(false);
				controlDecoration
						.setDescriptionText(SOAMessages.WARNING_ADMIN_NAME_MANUAL_OVERRIDE);
				FieldDecoration fieldDecoration = FieldDecorationRegistry
						.getDefault().getFieldDecoration(
								FieldDecorationRegistry.DEC_WARNING);
				controlDecoration.setImage(fieldDecoration.getImage());
			}
			addTargetNamespace(container, null, false);
			addServicePackage(container);
			CCombo layer = addServiceLayer(container);
			if (clientPropEditable == true) {
				reformatServiceLayer(layer);
			}

			// consumer related.
			createServiceClient(container, clientPropEditable);
			createConsumerIDText(container).setEditable(clientPropEditable);

			createEnvironmentList(container);
			addWSDLPackageToNamespace(container);

			addTypeFolding(container);
			super.setTypeFolding(false);
			
			if (clientPropEditable == true) {
				adminText.addModifyListener(new ModifyListener() {

					@Override
					public void modifyText(ModifyEvent e) {
						if (serviceClientText.getEditable() == false) {
							serviceClientText
									.setText(getDefaultValue(serviceClientText));
						}
					}

				});
			}*/
		} catch (Exception e) {
			logger.error(e);
			UIUtil.showErrorDialog(e);
		}
	}
	
	/**
	 * Reformat service layer.
	 *
	 * @param serviceLayer the service layer
	 */
	protected void reformatServiceLayer(CCombo serviceLayer) {
		serviceLayer.removeAll();
		for (final String layer : SOAServiceUtil
				.getInvokeableServiceLayer(serviceLayerStr)) {
			serviceLayer.add(layer.toString());
		}
		serviceLayer.select(0);
	}

	/**
	 * Creates the consumer id text.
	 *
	 * @param parent the parent
	 * @return the text
	 * @throws CoreException the core exception
	 */
	protected Text createConsumerIDText(Composite parent) throws CoreException {
		this.consumerID = super.createLabelTextField(parent, "Consumer &ID:",
				"", modifyListener, false, true,
				"the consumer ID of the new service consumer");
		consumerID.setText(consumerIDStr);
		consumerID.setEditable(false);
		final IClientRegistryProvider clientRegProvider = ExtensionPointFactory
				.getSOAClientRegistryProvider();
		if (clientRegProvider != null) {
			// The retrieve button should only be created if AR plugin is
			// available
			retrieveConsumerIDBtn = new Button(parent, SWT.PUSH);
			retrieveConsumerIDBtn.setText("Retrie&ve");
			retrieveConsumerIDBtn.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					try {
						ClientAssetModel clientModel = clientRegProvider
								.getClientAsset(getClientName());
						if (clientModel != null) {
							String conID = StringUtils.isBlank(clientModel
									.getConsumerId()) ? "" : clientModel
									.getConsumerId();
							consumerID.setText(conID);
							/*
							 * if (StringUtils.isNotBlank(conID))
							 * retrieveConsumerIDBtn.setEnabled(false);
							 */
						}
					} catch (Exception e1) {
						SOALogger.getLogger().error(e1);
						UIUtil.showErrorDialog(e1);
					}
				}
			});

			final Text text = getResourceNameText();
			text.addModifyListener(new ModifyListener() {

				@Override
				public void modifyText(ModifyEvent e) {
					retrieveConsumerIDBtn.setEnabled(StringUtils
							.isNotBlank(getResourceName()));

				}
			});
		} else {
			// AR plugin is not available
			super.createEmptyLabel(parent, 1);
		}
		return this.consumerID;
	}

	/**
	 * Creates the environment list.
	 *
	 * @param parent the parent
	 * @return the list viewer
	 */
	protected ListViewer createEnvironmentList(Composite parent) {

		Group group = new Group(parent, SWT.SHADOW_ETCHED_IN);
		group.setText("Environments");
		GridData data = new GridData(GridData.FILL_HORIZONTAL);
		data.horizontalSpan = 4;
		group.setLayoutData(data);
		group.setLayout(new GridLayout(2, false));
		envrionmentList = new ListViewer(group, SWT.SINGLE | SWT.BORDER);
		envrionmentList.getList().setLayoutData(
				new GridData(GridData.FILL_BOTH));
		envrionmentList.setContentProvider(new IStructuredContentProvider() {

			@Override
			public Object[] getElements(Object inputElement) {
				return environments.toArray();
			}

			@Override
			public void dispose() {

			}

			@Override
			public void inputChanged(Viewer viewer, Object oldInput,
					Object newInput) {

			}

		});
		envrionmentList.setLabelProvider(new ILabelProvider() {

			@Override
			public Image getImage(Object element) {
				return null;
			}

			@Override
			public String getText(Object element) {
				return String.valueOf(element);
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

		envrionmentList.setInput(environments);

		Composite btnComposite = new Composite(group, SWT.NONE);
		btnComposite.setLayout(new GridLayout(1, true));
		btnComposite.setLayoutData(new GridData(GridData.VERTICAL_ALIGN_FILL));

		final Button addBtn = new Button(btnComposite, SWT.PUSH);
		addBtn.setText("Add...");
		addBtn.setEnabled(false);
		
		final Button removeBtn = new Button(btnComposite, SWT.PUSH);
		removeBtn.setText("Remove");
		removeBtn.setEnabled(false);

		UIUtil.setEqualWidthHintForButtons(addBtn, removeBtn);
		envrionmentList.getControl().setEnabled(false);
		return envrionmentList;
	}

	/**
	 * Creates the service client.
	 *
	 * @param parent the parent
	 * @param clientPropEditable the client prop editable
	 * @return the text
	 */
	protected Text createServiceClient(final Composite parent,
			final boolean clientPropEditable) {
		final ModifyListener nsModifyListener = new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				dialogChanged();
			}
		};
		serviceClientText = super.createLabelTextField(parent, "&Client Name:",
				"", nsModifyListener, clientPropEditable == false, false,
				"the client name of consumer");

		if (clientPropEditable == true) {
			Button overrideSvcClient = createOverrideButton(parent,
					serviceClientText, null);
			overrideSvcClient.setSelection(false);
		}
		serviceClientText.setText(clientNameStr);
		return serviceClientText;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getDefaultValue(Text text) {
		if (text == serviceClientText) {
			if (clientPropEditable == false) {
				return clientNameStr;
			} else if (StringUtils.isEmpty(getAdminName()) == false) {
				return getAdminName()
						+ SOAProjectConstants.CLIENT_PROJECT_SUFFIX;
			} else if (StringUtils.isEmpty(getAdminName()) == true) {
				return "";
			}
		}
		return super.getDefaultValue(text);
	}

	/* (non-Javadoc)
	 * @see org.ebayopensource.turmeric.eclipse.ui.wizards.pages.AbstractNewServiceFromWSDLWizardPage#dialogChanged()
	 */
	@Override
	protected boolean dialogChanged() {

		if (super.dialogChanged() == false && isPageComplete() == false)
			return false;
		
		if (publicServiceNameText != null
				&& (validateName(
						publicServiceNameText,
						getPublicServiceName(),
						RegExConstants.SERVICE_NAME_EXP,
						"The service name ["
								+ getPublicServiceName()
								+ "] in WSDL file is not valid against the pattern \""
								+ RegExConstants.SERVICE_NAME_EXP
								+ "\". Please correct service name in WSDL and run this wizard again.") == false)) {
			return false;
		}


		/*
		 * 1) If service version in WSDL follows V3 format, like 1.2.3, service
		 * version text will not be editable. 2) If service version in WSDL
		 * doesn�t follow V3 format, like 1.2, 1.2,3, 1, 1.a, 1.2.a, then
		 * service version text is editable. BUT even user specified a correct
		 * V3 version, there will be an error marker on the service version text
		 * says Specified service version [1.2.3] does not match service version
		 * in WSDL [1.2]. Please modify service version in source WSDL and
		 * follow format {major.minor.maintenance}�. It means the WSDL file used
		 * in wizard must contain a correct V3 format service version.
		 * Otherwise, the wizard couldn�t continue.
		 */

		if ((versionFromWSDL != null && resourceVersionText != null)
				&& versionFromWSDL.equals(getResourceVersion()) == false) {
			String errorMsg = StringUtil.formatString(
					SOAMessages.DIFFERENT_SERVICE_VERSION_WITH_WSDL, getResourceVersion(),
					versionFromWSDL);
			updateStatus(super.resourceVersionText, errorMsg);
			return false;
		}

		if (domainClassifierList != null
				&& StringUtils.isNotBlank(getDomainClassifier())
				&& this.wsdl != null) {
			String namespacePart = getOrganizationProvider()
					.getNamespacePartFromTargetNamespace(
							this.wsdl.getTargetNamespace());
			if (StringUtils.isNotBlank(namespacePart)
					&& namespacePart.equals(getDomainClassifier()) == false) {
				// user has selected a namespace part that not match the ns-part
				// from the wsdl file
				updateStatus(super.domainClassifierList, StringUtil
						.formatString(SOAMessages.ERR_WRONG_NAMESPACEPART,
								getDomainClassifier(), this.wsdl
										.getTargetNamespace()));
				return false;
			}
		}
		
		if (this.serviceClientText != null) {
			if (StringUtils.isBlank(getClientName())) {
				updateStatus(this.serviceClientText,
						"Client name must be specified");
				return false;
			}
			if (StringUtils.equals(StringUtils.capitalize(getClientName()),
					getClientName()) == false) {
				updateStatus(this.serviceClientText,
						"Client name must be capitalized.");
				return false;
			}
			if (validateName(this.serviceClientText, getClientName(),
					RegExConstants.PROJECT_NAME_EXP,
					ErrorMessage.PROJECT_NAME_ERRORMSG + " The name ["
							+ getClientName()
							+ "] is not valid against the pattern \""
							+ RegExConstants.PROJECT_NAME_EXP + "\"") == false) {
				return false;
			}
		}

		if (StringUtils.isNotEmpty(getResourceName())
				&& Character.isLowerCase(getResourceName().charAt(0))) {
			updatePageStatus(getResourceNameText(), EclipseMessageUtils
					.createStatus(SOAMessages.SVCNAME_ERR, IStatus.WARNING));
			return true;
		}

		return true;
	}

	/* (non-Javadoc)
	 * @see org.ebayopensource.turmeric.eclipse.ui.wizards.pages.AbstractNewServiceFromWSDLWizardPage#wsdlChanged(javax.wsdl.Definition)
	 */
	@Override
	public void wsdlChanged(final Definition wsdl) {
		final Collection<?> services = wsdl.getServices().values();
		if (services.size() > 0) {
			// only process the first service
			final Service service = (Service) services.toArray()[0];
			if (services.size() > 1) {
				logger
						.warning("Found multiple service, but only the first service will be processed->"
								+ service.getQName());
			}
			final String targetNs = wsdl.getTargetNamespace();
			targetNamespaceModified(targetNs);
			setTargetNamespace(targetNs);
			if (domainClassifierList == null) {
				// Non-MP
				String nsPart = StringUtils
						.capitalize(getOrganizationProvider()
								.getNamespacePartFromTargetNamespace(targetNs));
				this.domainName = nsPart;
				if (StringUtils.isNotBlank(nsPart)) {
					this.adminText.setText(nsPart + getAdminName());
				} else {
					this.adminText
							.setText(getPublicServiceName()
									+ SOAProjectConstants.MAJOR_VERSION_PREFIX
									+ SOAServiceUtil
											.getServiceMajorVersion(getServiceVersion()));
				}
			}
			String version = SOAIntfUtil.getServiceVersionFromWsdl(wsdl,
					getPublicServiceName()).trim();
			if (resourceVersionText != null) {
				resourceVersionText.setEditable(true);
				if (StringUtils.isNotBlank(version)) {
					versionFromWSDL = version;
					// has version
					int versionPart = StringUtils.countMatches(version,
							SOAProjectConstants.DELIMITER_DOT);
					// add "dot number" to version. It will be changed to X.Y.Z
					if (versionPart == 2) {
						// is new version format, set version text read-only.
						resourceVersionText.setEditable(false);
					} else {
						// is v2format
						while (versionPart < 2) {
							version += SOAProjectConstants.DELIMITER_DOT + "0";
							versionPart++;
						}
					}
					resourceVersionText.setText(version);
				} else {
					// don't have version, use default version.
					resourceVersionText
					.setText(SOAProjectConstants.DEFAULT_SERVICE_VERSION);
				}
			} else {
				versionFromWSDL = version;
				serviceVersionChanged(version);
			}
		}
	}

	/* (non-Javadoc)
	 * @see org.ebayopensource.turmeric.eclipse.ui.wizards.pages.AbstractNewServiceFromWSDLWizardPage#targetNamespaceModified(java.lang.String)
	 */
	@Override
	protected void targetNamespaceModified(String newNamespace) {
		super.targetNamespaceModified(newNamespace);
		if (this.serviceDomainList == null || this.domainClassifierList == null
				|| StringUtils.isBlank(newNamespace))
			return;

		String namespacePart = getOrganizationProvider()
				.getNamespacePartFromTargetNamespace(newNamespace);

		if (StringUtils.isNotBlank(namespacePart)) {
			String domainName = StringUtils.capitalize(namespacePart);
			Map<String, List<String>> domainList = Collections.emptyMap();
			if (getWizard() instanceof AbstractSOADomainWizard) {
				try {
					domainList = ((AbstractSOADomainWizard) getWizard())
							.getDomainList();
				} catch (Exception e) {
					logger.warning(e);
				}
			}

			for (String key : domainList.keySet()) {
				final List<String> values = domainList.get(key);
				if (values != null && values.contains(namespacePart)) {
					domainName = key;
					break;
				}
			}
			this.serviceDomainList.setText(domainName);
			this.domainClassifierList.setText(namespacePart);
		} else if (StringUtils.isBlank(getServiceDomain())
				|| StringUtils.isBlank(getDomainClassifier())) {
			// could not get the namespace-part
			this.serviceDomainList.select(-1);
			this.serviceDomainList.clearSelection();
			this.domainClassifierList.clearSelection();
		}
	}

	/* (non-Javadoc)
	 * @see org.ebayopensource.turmeric.eclipse.ui.wizards.pages.AbstractNewServiceWizardPage#resetServiceName()
	 */
	@Override
	public void resetServiceName() {
		super.resetServiceName();
		if (serviceClientText != null)
			serviceClientText.setText(clientNameStr);
	}

	/**
	 * Gets the client name.
	 *
	 * @return the client name
	 */
	public String getClientName() {
		return super.getTextValue(this.serviceClientText);
	}

	/**
	 * Gets the consumer id.
	 *
	 * @return the consumer id
	 */
	public String getConsumerId() {
		return super.getTextValue(this.consumerID);
	}

	/**
	 * Gets the environments.
	 *
	 * @return the environments
	 */
	public List<String> getEnvironments() {
		return environments;
	}
	
	@Override
	public String getServiceVersion() {
		String result = super.getServiceVersion();
		if (StringUtils.isBlank(result) && StringUtils.isNotBlank(versionFromWSDL)) {
			return versionFromWSDL;
		}
		return result;
	}

	/* (non-Javadoc)
	 * @see org.ebayopensource.turmeric.eclipse.ui.wizards.pages.AbstractNewServiceFromWSDLWizardPage#getDefaultResourceName()
	 */
	@Override
	public String getDefaultResourceName() {
		final String defaultName = computeServiceName();
		if (StringUtils.isNotBlank(defaultName))
			return defaultName;
		else
			return "";
	}
	
	@Override
	public String getServiceDomain() {
		if (serviceDomainList == null) {
			return this.domainName;
		}
		return super.getServiceDomain();
	}

	/* (non-Javadoc)
	 * @see org.ebayopensource.turmeric.eclipse.ui.SOABasePage#getHelpContextID()
	 */
	@Override
	public String getHelpContextID() {
		return GlobalRepositorySystem.instanceOf().getActiveRepositorySystem()
				.getHelpProvider().getHelpContextID(
						ISOAHelpProvider.HELPID_CONSUME_SERVICE_FROM_WSDL);
	}

}
