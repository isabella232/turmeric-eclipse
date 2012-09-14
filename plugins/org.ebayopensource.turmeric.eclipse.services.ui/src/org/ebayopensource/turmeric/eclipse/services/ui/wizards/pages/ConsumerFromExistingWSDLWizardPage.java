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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.wsdl.Definition;
import javax.wsdl.Service;
import javax.wsdl.Types;
import javax.wsdl.WSDLException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.lang.StringUtils;
import org.ebayopensource.turmeric.eclipse.core.logging.SOALogger;
import org.ebayopensource.turmeric.eclipse.core.resources.constants.SOAProjectConstants;
import org.ebayopensource.turmeric.eclipse.exception.validation.ValidationInterruptedException;
import org.ebayopensource.turmeric.eclipse.registry.ExtensionPointFactory;
import org.ebayopensource.turmeric.eclipse.registry.intf.IClientRegistryProvider;
import org.ebayopensource.turmeric.eclipse.registry.models.ClientAssetModel;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.GlobalRepositorySystem;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.ISOAHelpProvider;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.ISOARepositorySystem;
import org.ebayopensource.turmeric.eclipse.resources.util.SOAIntfUtil;
import org.ebayopensource.turmeric.eclipse.resources.util.SOAServiceUtil;
import org.ebayopensource.turmeric.eclipse.services.ui.SOAMessages;
import org.ebayopensource.turmeric.eclipse.services.ui.wizards.ConsumerFromWSDLWizard;
import org.ebayopensource.turmeric.eclipse.ui.AbstractSOADomainWizard;
import org.ebayopensource.turmeric.eclipse.ui.wizards.pages.AbstractNewServiceFromWSDLWizardPage;
import org.ebayopensource.turmeric.eclipse.utils.lang.StringUtil;
import org.ebayopensource.turmeric.eclipse.utils.plugin.EclipseMessageUtils;
import org.ebayopensource.turmeric.eclipse.utils.ui.UIUtil;
import org.ebayopensource.turmeric.eclipse.utils.wsdl.WSDLUtil;
import org.ebayopensource.turmeric.eclipse.validator.core.ErrorMessage;
import org.ebayopensource.turmeric.eclipse.validator.core.InputObject;
import org.ebayopensource.turmeric.eclipse.validator.utils.ValidateUtil;
import org.ebayopensource.turmeric.eclipse.validator.utils.common.NameValidator;
import org.ebayopensource.turmeric.eclipse.validator.utils.common.RegExConstants;
import org.ebayopensource.turmeric.tools.codegen.external.wsdl.parser.WSDLConversionToSingleNsHelper;
import org.ebayopensource.turmeric.tools.codegen.external.wsdl.parser.util.WSDLParserUtil;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.jface.fieldassist.FieldDecoration;
import org.eclipse.jface.fieldassist.FieldDecorationRegistry;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.swt.SWT;
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
import org.eclipse.ui.forms.events.ExpansionAdapter;
import org.eclipse.ui.forms.events.ExpansionEvent;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.sun.tools.xjc.api.XJC;


/**
 * The Class ConsumerFromExistingWSDLWizardPage.
 *
 * @author yayu
 */
public class ConsumerFromExistingWSDLWizardPage extends AbstractNewServiceFromWSDLWizardPage{
	private Text serviceClientText;	
	private Text consumerID;
	private Text applicationPackage;
	private Text adminText;
	private Button retrieveConsumerIDBtn;
	private ListViewer envrionmentList;
	private List<String> environments = new ArrayList<String>();
	private String versionFromWSDL = null;
	private boolean simpleMode = false; //simple mode will not need to craete the consumer project
	private String domainName = "";
	public Map<String,String> typeLibNameAndPackage;
	
	private static final String SIMPLE_MODE_TITLE = "Simple Mode reads a pre-existing WSDL document, creates a consumerized SOA Intf Project with a ClientConfig.xml.";
	
	private static final String ADV_MODE_TITLE = "Advance Mode creates a consumerized SOA Intf Project from a pre-existing WSDL document.";

	private static final SOALogger logger = SOALogger.getLogger();

	/**
	 * Instantiates a new consumer from existing wsdl wizard page.
	 *
	 * @param selection the selection
	 */
	public ConsumerFromExistingWSDLWizardPage(
			final IStructuredSelection selection) {
		super("newConsumerFromWSDLWizardPage",
				"New Consumer From Existing WSDL Wizard",
				ADV_MODE_TITLE);
	}

	/**
	 * Instantiates a new consumer from existing wsdl wizard page.
	 */
	public ConsumerFromExistingWSDLWizardPage() {
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
			Text wsdlText = addWSDL(container);
			
			addWorkspaceRootChooser(container);
			addServiceName(container, false);
			
			this.adminText = addAdminName(container, false);
			
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
			
			createServiceClient(container, false);			
			
			
				//advanced section
				ExpansionAdapter listener  = new ExpansionAdapter() {
					public void expansionStateChanged(ExpansionEvent e) {
						boolean status = e.getState();
						if(status == true){
							setDescription(SIMPLE_MODE_TITLE);
						}else {
							setDescription(ADV_MODE_TITLE);
						}
					}
				};
				Composite advancedPanel = super.createAdvancedSettingsPanel(container, listener);
				if (serviceDomainList != null && domainClassifierList != null) {
					this.serviceDomainList.select(-1);
					this.serviceDomainList.clearSelection();
					this.domainClassifierList.select(-1);
					this.domainClassifierList.clearSelection();
				}
				createApplicationPackageIDText(advancedPanel);
				addTargetNamespace(advancedPanel, "", false);
				addServicePackage(advancedPanel);
				createConsumerIDText(advancedPanel);
				createEnvironmentList(advancedPanel, true);
				addWSDLPackageToNamespace(advancedPanel);
				
				addTypeFolding(advancedPanel).setVisible(true);				
				super.setTypeFolding(true);
			
			
			adminText.addModifyListener(new ModifyListener(){

				@Override
				public void modifyText(ModifyEvent e) {
					if (serviceClientText.getEditable() == false) {
						serviceClientText
								.setText(ConsumerFromExistingWSDLWizardPage.this
										.getDefaultValue(serviceClientText));
					}
				}
				
			});
			wsdlText.setFocus();
		} catch (Exception e) {
			logger.error(e);
			UIUtil.showErrorDialog(e);
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
		this.consumerID = super.createLabelTextField(parent, "Consumer &ID:",
				"", modifyListener, false, true,
				"the consumer ID of the new service consumer");
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

		// TODO get consumer ID if possible
		return this.consumerID;
	}

	/**
	 * Creates the application package id text.
	 *
	 * @param parent the parent
	 * @return the text
	 * @throws CoreException the core exception
	 */
	protected Text createApplicationPackageIDText(Composite parent) throws CoreException {
		this.applicationPackage = super.createLabelTextField(parent, "Application &Package:",
				"", null, false, true,
				"The unique application prefix of the consumer which determines the Interface package and the namespace to package mappings, using which pojos are generated by the soa framework.");
				
			
			applicationPackage.addModifyListener(new ModifyListener() {
				@Override
				public void modifyText(ModifyEvent e) {
					//What to do if the text is modified
					changeGenPackages();
					dialogChanged();
				}
			});
		 
			
			super.createEmptyLabel(parent, 1);

		// TODO get consumer ID if possible
		return this.applicationPackage;
	}
	void changeGenPackages(){
if(this.applicationPackage==null)
	return;
		if(!StringUtils.isBlank(this.applicationPackage.getText()))
		{String postfix="";
		if(!getClientName().isEmpty())
			postfix = "."+getClientName().toLowerCase();
			String uniqueAppServicePackageName=applicationPackage.getText()+postfix;
			super.setServicePackage(uniqueAppServicePackageName);
			super.setNamespaceToPackageMappings(null, uniqueAppServicePackageName);
		}else{
			//generate old namespace things
			super.setServicePackage(getDefaultServicePackageName());
			super.setNamespaceToPackageMappings(null, getDefaultServicePackageName());
		}
	}

	/**
	 * Creates the environment list.
	 *
	 * @param parent the parent
	 * @return the list viewer
	 */
	protected ListViewer createEnvironmentList(Composite parent, boolean simpleMode) {
		this.simpleMode = simpleMode;
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
		//if (simpleMode == false) {
		//Removed on ride, since users need to be able to use ZCC only if they want to.
			environments.add(SOAProjectConstants.DEFAULT_CLIENT_CONFIG_ENVIRONMENT);
		//}
		envrionmentList.setInput(environments);
		
		Composite btnComposite = new Composite(group, SWT.NONE);
		btnComposite.setLayout(new GridLayout(1, true));
		btnComposite.setLayoutData(new GridData(GridData.VERTICAL_ALIGN_FILL));

		final Button addBtn = new Button(btnComposite, SWT.PUSH);
		addBtn.setText("Add...");
		addBtn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				final IInputValidator validator = new IInputValidator() {

					@Override
					public String isValid(String newText) {
						if (StringUtils.isBlank(newText)) {
							return "Error: Environment name must not be empty";
						}
						if (environments.contains(newText)) {
							return "Error: Environment name already exist->"
									+ newText;
						}
						final InputObject inputObject = new InputObject(
								newText, RegExConstants.PROJECT_NAME_EXP,
								ErrorMessage.CLIENT_NAME_ERRORMSG);

						try {
							IStatus validationModel = NameValidator
									.getInstance().validate(inputObject);
							if (validationModel != null
									&& validationModel.getSeverity() == IStatus.ERROR) {
								return ValidateUtil
										.getBasicFormattedUIErrorMessage(validationModel);
							}
						} catch (ValidationInterruptedException e) {
							processException(e);
						}
						return null;
					}

				};
				InputDialog dialog = new InputDialog(UIUtil.getActiveShell(),
						"New Environment",
						"Please enter the new environment name", "", validator);
				if (dialog.open() == Window.OK) {
					environments.add(dialog.getValue());
					envrionmentList.refresh();
					dialogChanged();
				}
			}
		});

		final Button removeBtn = new Button(btnComposite, SWT.PUSH);
		removeBtn.setText("Remove");
		removeBtn.setEnabled(false);
		removeBtn.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				Object obj = ((IStructuredSelection) envrionmentList
						.getSelection()).getFirstElement();
				environments.remove(obj);
				envrionmentList.refresh();
				dialogChanged();
			}
		});
		envrionmentList.addSelectionChangedListener(new ISelectionChangedListener() {

			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				removeBtn.setEnabled(envrionmentList.getSelection()
						.isEmpty() == false);
			}
		}
		);
		
		UIUtil.setEqualWidthHintForButtons(addBtn, removeBtn);
		return envrionmentList;
	}

	/**
	 * Creates the service client.
	 *
	 * @param parent the parent
	 * @param editable the editable
	 * @return the text
	 */
	protected Text createServiceClient(final Composite parent,
			final boolean editable) {
		final ModifyListener nsModifyListener = new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				changeGenPackages();
				dialogChanged();
			}
		};
		serviceClientText = super.createLabelTextField(parent, "&Client Name:",
				"", nsModifyListener, false, editable,
				"the client name of consumer");

		Button overrideSvcClient = createOverrideButton(parent,
				serviceClientText, null);
		overrideSvcClient.setSelection(editable);

		return serviceClientText;
	}
	
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getDefaultValue(Text text) {
		if (text == serviceClientText) {
			String adminName = getAdminName();
			if (StringUtils.isEmpty(adminName) == true) {
				return "";
			} else {
				return adminName + SOAProjectConstants.CLIENT_PROJECT_SUFFIX;
			}
		}
		//Adding service package test to this class since default value depends on app package field
		if (text == this.servicePackageText){
			if((StringUtils.isEmpty(getApplicationPackage()))||(getClientName().isEmpty()))
			return getDefaultServicePackageName();
			else return getApplicationPackage()+"."+getClientName().toLowerCase();
		}
		return super.getDefaultValue(text);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected boolean dialogChanged(boolean wsdlChanged) {
		if (super.dialogChanged(wsdlChanged) == false && isPageComplete() == false){
			return false;
		}
		return dialogChanged();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected boolean dialogChanged() {
		
		if (super.dialogChanged(false) == false && isPageComplete() == false){
			return false;
		}
		
		
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
		
		// Client name is consumer project name and admin name is the service
		// interface project name. They should not be the same.
		if (this.serviceClientText != null && adminNameText != null) {
			String clientName = serviceClientText.getText();
			String adminName = adminNameText.getText();
			if (StringUtils.equalsIgnoreCase(clientName, adminName) == true) {
				updateStatus(
						"Admin Name and Client Name should not be the same.",
						serviceClientText, adminNameText);
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
			//Adding check to validate client name post project xml split, so that project catalog uniqueness is maintained
			final ISOARepositorySystem activeRepositorySystem = 
				GlobalRepositorySystem
				.instanceOf().getActiveRepositorySystem();
			try {
				IStatus validationModel = activeRepositorySystem.getServiceValidator()
				.validate(getClientName());
				if (checkValidationResult(serviceClientText, 
						validationModel) == false){
					updateStatus(this.serviceClientText,
					"Project already exists in the workspace or file system with this name -->"+getClientName());
					return false;
				}
					
			} catch (ValidationInterruptedException e) {
				// TODO Auto-generated catch block
				processException(e);
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

		if (envrionmentList != null) {
			if (environments.isEmpty() && this.simpleMode == false) {
				updateStatus(envrionmentList.getControl(),
						"At least one environment must be added");
				return false;
			}
		}
//Til this only
		// String version = resourceVersionText.getText();
		// if (StringUtils.isNotBlank(version) &&
		// (isV2Format == true)) {
		// // version from WSDL is X.Y, only maintenance version should be
		// changed.
		// Version newVer = new Version(version);
		// Version oldVer = new Version(versionFromWSDL);
		// if ((newVer.getMajor() != oldVer.getMajor())
		// || (newVer.getMinor() != oldVer.getMinor())) {
		// updateStatus(super.resourceVersionText,
		// "Service version from WSDL is: "+versionFromWSDL +
		// ". You can not change the major and minor version. The version must start with->"
		// + versionFromWSDL);
		// return false;
		// }
		// }

		/*
		 * 1) If service version in WSDL follows V3 format, like 1.2.3, service
		 * version text will not be editable. 2) If service version in WSDL
		 * doesn?t follow V3 format, like 1.2, 1.2,3, 1, 1.a, 1.2.a, then
		 * service version text is editable. BUT even user specified a correct
		 * V3 version, there will be an error marker on the service version text
		 * says Specified service version [1.2.3] does not match service version
		 * in WSDL [1.2]. Please modify service version in source WSDL and
		 * follow format {major.minor.maintance}?. It means the WSDL file used
		 * in wizard must contain a correct V3 format service version.
		 * Otherwise, the wizard couldn?t continue.
		 */
//after this only
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

		if (StringUtils.isNotEmpty(getResourceName())
				&& Character.isLowerCase(getResourceName().charAt(0))) {
			updatePageStatus(getResourceNameText(), EclipseMessageUtils
					.createStatus(SOAMessages.SVCNAME_ERR, Status.WARNING));
			return true;
		}

		return true;
	}
public static Map<String,String> getAllTypeLibraryNames(Definition wsdl) throws ParserConfigurationException, SAXException, IOException{
		
		Map<String,String> typeLibraryPackageSet = new HashMap<String,String>();
		try{
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder;

		builder = factory.newDocumentBuilder();
		Document m_Document = builder.parse(wsdl.getDocumentBaseURI());
		
		NodeList list = m_Document.getElementsByTagName("typeLibrarySource");
		for(int i =0;i<list.getLength();i++){
			Node n = list.item(i);
			
			if(((n.getParentNode().getNodeName().endsWith(":appinfo")) 
					||(n.getParentNode().getNodeName().equalsIgnoreCase("appinfo")))
				&&(n.hasAttributes()) ){
				Node typeLibrary = n.getAttributes().getNamedItem("library");
				Node nameSpace = n.getAttributes().getNamedItem("namespace");
				if(typeLibrary!=null&&nameSpace!=null){
					//Calling the ns to package mapper from XJC to maintain consistency
					
					String packageName = XJC.getDefaultPackageName(nameSpace.getNodeValue());
					//lets call the service here if we do not know which bundles it belongs to already.
					//Then lets populate some datasructure with thegroup id, art id, tl name, version.
					//lets return that data structure here.
					typeLibraryPackageSet.put(typeLibrary.getNodeValue(), packageName);

				}
			}
		}
//		if (wsdl.getTypes() != null) {
//			
//			for (Object obj : wsdl.getTypes().getExtensibilityElements()) {
//				if (obj instanceof Schema) {
//				Schema xs = (Schema)obj;
//				NodeList list =xs.getElement().getChildNodes();
//				for(int i =0;i<list.getLength();i++){
//					Node a = list.item(i);
//					System.out.println(a.getNodeName());
//					
//				}
//				NamedNodeMap nodemapr=list.item(0).getAttributes();
//				System.out.println(nodemapr.getLength());
//				}
//			}
//		}}
		}
		catch(Exception e){
			logger.error(e);
		}
		return typeLibraryPackageSet;
	}
	
	/**
	 * {@inheritDoc}
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
			serviceClientText.setText(getAdminName()
					+ SOAProjectConstants.CLIENT_PROJECT_SUFFIX);
			setTypeFolding(getDefaultEnabledNSValue());
			typeFoldingButton.setEnabled(false);
			
			
			try {
				typeLibNameAndPackage=ConsumerFromExistingWSDLWizardPage.getAllTypeLibraryNames(wsdl);
				// for each entry in r,
				// If no conflicting bundle exists,
					//Display a mesg asking user to move tl to library and then add it as a dep and rebuild library.
				// If conflicting bundle exists,
					//Display a mesg asking user to add it as a possible dependency.
			} catch (ParserConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SAXException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
				
			
			
		} else {
			serviceClientText.setText(DEFAULT_TEXT_VALUE);
		}
	}

	/**
	 * {@inheritDoc}
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

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getServiceVersion() {
		String result = super.getServiceVersion();
		if (StringUtils.isBlank(result) && StringUtils.isNotBlank(versionFromWSDL)) {
			return versionFromWSDL;
		}
		return result;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void resetServiceName() {
		super.resetServiceName();
		if (serviceClientText != null)
			serviceClientText.setText(DEFAULT_TEXT_VALUE);
	}
	@Override
	protected boolean getDefaultEnabledNSValue(){
		Collection<String> allTNS=WSDLUtil.getAllTargetNamespaces(wsdl);
		if(allTNS.size()>1) return false;
		return true;
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
	 * Gets the application packge
	 *
	 * @return the application packge
	 */
	public String getApplicationPackage() {
		return super.getTextValue(this.applicationPackage);
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
	 * {@inheritDoc}
	 */
	@Override
	public List<ProjectNameControl> getProjectNames() {
		final List<ProjectNameControl> result = super.getProjectNames();
		if (this.serviceClientText != null) {
			result.add(new ProjectNameControl(getClientName(),
					this.serviceClientText));
		}
		return result;
	}
	
	/**
	 * Gets the environments.
	 *
	 * @return the environments
	 */
	public List<String> getEnvironments() {
		return environments;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getDefaultResourceName() {
		final String defaultName = computeServiceName();
		if (StringUtils.isNotBlank(defaultName))
			return defaultName;
		else
			return "";
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getServiceDomain() {
		if (serviceDomainList == null) {
			return this.domainName;
		}
		return super.getServiceDomain();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getHelpContextID() {
		return GlobalRepositorySystem.instanceOf().getActiveRepositorySystem()
				.getHelpProvider().getHelpContextID(
						ISOAHelpProvider.PAGE_CREATE_CONSUMER_FROM_WSDL);
	}
	
	
}
