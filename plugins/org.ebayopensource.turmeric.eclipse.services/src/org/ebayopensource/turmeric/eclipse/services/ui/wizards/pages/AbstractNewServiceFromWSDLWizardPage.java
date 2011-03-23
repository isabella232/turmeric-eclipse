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

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import javax.wsdl.Definition;
import javax.wsdl.Service;
import javax.wsdl.WSDLException;

import org.apache.commons.lang.StringUtils;
import org.ebayopensource.turmeric.eclipse.exception.validation.ValidationInterruptedException;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.GlobalRepositorySystem;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.ISOARepositorySystem;
import org.ebayopensource.turmeric.eclipse.resources.constants.SOAProjectConstants;
import org.ebayopensource.turmeric.eclipse.resources.util.SOAServiceUtil;
import org.ebayopensource.turmeric.eclipse.services.resources.SOAMessages;
import org.ebayopensource.turmeric.eclipse.soatools.configtool.ConfigTool;
import org.ebayopensource.turmeric.eclipse.ui.UIActivator;
import org.ebayopensource.turmeric.eclipse.utils.io.IOUtil;
import org.ebayopensource.turmeric.eclipse.utils.lang.StringUtil;
import org.ebayopensource.turmeric.eclipse.utils.plugin.EclipseMessageUtils;
import org.ebayopensource.turmeric.eclipse.utils.plugin.JDTUtil;
import org.ebayopensource.turmeric.eclipse.utils.ui.UIUtil;
import org.ebayopensource.turmeric.eclipse.utils.wsdl.WSDLUtil;
import org.ebayopensource.turmeric.eclipse.validator.core.ISOAValidator;
import org.ebayopensource.turmeric.eclipse.validator.utils.common.RegExConstants;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;


/**
 * @author yayu
 * 
 */
public abstract class AbstractNewServiceFromWSDLWizardPage extends
		AbstractNewServiceWizardPage {
	private Text wsdlURLText;
	private Button importWSDLFileButton;
	protected TableViewer ns2pkgViewer;
	private boolean hasWSDLError;
	private Button overrideNamespaceBtn;
	private Text namespaceText;
	protected Text publicServiceNameText;
	protected Definition wsdl;

	/**
	 * @param pageName
	 */
	public AbstractNewServiceFromWSDLWizardPage(String pageName, String title,
			String description) {
		super(pageName, title, description);
	}

	@Override
	protected boolean dialogChanged() {
		return dialogChanged(false);
	}
	
	protected boolean hasWsdlError() {
		return hasWSDLError;
	}

	protected boolean dialogChanged(boolean validateWsdl) {
		if (wsdlURLText != null) {
			String wsdlUrl = getWSDLURL();
			if (StringUtils.isBlank(wsdlUrl)) {
				updateStatus(wsdlURLText, "WSDL URL must be specified.");
				hasWSDLError = false;
				return false;
			}
			if (IOUtil.validateURL(wsdlUrl) == false) {
				updateStatus(wsdlURLText, "Invalid WSDL Url.");
				hasWSDLError = false;
				return false;
			}
		}
		boolean result = super.dialogChanged();
		if (result == false)
			return result;
		
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
		
		if (this.namespaceText != null
				&& StringUtils.isBlank(getTargetNamespace())) {
			updateStatus(namespaceText, SOAMessages.NMSPACE_ERR);
			return false;
		}

		if ((hasWSDLError == true || validateWsdl == true)
				&& wsdlURLText != null) {
			hasWSDLError = false;
			final ISOARepositorySystem activeRepositorySystem = GlobalRepositorySystem
					.instanceOf().getActiveRepositorySystem();

			ISOAValidator validator = activeRepositorySystem.getWSDLValidator();
			IStatus validationModel = null;
			try {
				String wsdlurltext = getWSDLURL();
				Object wsdl = null;
				if (wsdlurltext.startsWith("http:") || wsdlurltext.startsWith("https")) {
					try {
						wsdl = new URL(wsdlurltext);
					} catch (MalformedURLException e) {
						wsdl = new Path(wsdlurltext);
					}
				} else {
					wsdl = wsdlurltext;
				}
				validationModel = validator.validate(wsdl);
			} catch (ValidationInterruptedException e) {
				processException(e);
			}
			if (validationModel.isOK() == false) {
				updatePageStatus(wsdlURLText, validationModel);
				hasWSDLError = true;
				return false;
			}
		}
		
		updateStatus(null);
		return true;
	}

	protected Text addWSDL(final Composite composite) {
		final Label label = new Label(composite, SWT.NULL);
		label.setText("&WSDL: ");
		wsdlURLText = new Text(composite, SWT.BORDER | SWT.SINGLE);
		wsdlURLText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false, 1, 1));
		final FocusListener focusListener = new FocusAdapter() {
			@Override
			public void focusLost(final FocusEvent e) {
				final Runnable runnable = new Runnable() {
					public void run() {
						if (StringUtils.isNotBlank(getWSDLURL())
								&& setServiceNameFromWSDL(getWSDLURL())) {
							dialogChanged(true);
						} else {
							dialogChanged(false);
						}
					}
				};
				BusyIndicator.showWhile(null, runnable);
			}
		};
		wsdlURLText.addFocusListener(focusListener);
		wsdlURLText.setText(SOAProjectConstants.EMPTY_STRING);
		UIUtil.decorateControl(this, wsdlURLText, 
				"either browse to or enter the service WSDL location");
		wsdlURLText.setEditable(true);
		importWSDLFileButton = new Button(composite, SWT.PUSH);
		importWSDLFileButton.setAlignment(SWT.RIGHT);
		importWSDLFileButton.setText("B&rowse...");
		final SelectionListener selectionListener = new SelectionListener() {
			public void widgetDefaultSelected(final SelectionEvent e) {
				widgetSelected(e);
			}

			public void widgetSelected(final SelectionEvent e) {
				final String fileName = UIUtil.fileDialog(
						"Select WSDL Interface File", "*.wsdl");
				if (StringUtils.isBlank(fileName))
					return;
				final Runnable runnable = new Runnable() {
					public void run() {
						if (setServiceNameFromWSDL(String.valueOf(WSDLUtil
								.toURL(new File(fileName)))) == true) {
							dialogChanged(true);
						}
					}
				};
				BusyIndicator.showWhile(null, runnable);
			}
		};
		importWSDLFileButton.addSelectionListener(selectionListener);
		importWSDLFileButton.setLayoutData(new GridData(SWT.BEGINNING,
				SWT.CENTER, false, false, 2, 1));
		return wsdlURLText;
	}
	
	protected Composite addTargetNamespace(final Composite parent, 
			final String defaultNamespace, final boolean allowOverride) {
		final String namespace = StringUtils.isNotBlank(defaultNamespace) 
		? defaultNamespace : "";
		
		final ModifyListener nsModifyListener = new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				targetNamespaceModified(getTargetNamespace());
				dialogChanged();
			}
		};
		
		namespaceText = super.createLabelTextField(parent,
				"&Target Namespace:",
				namespace, nsModifyListener, 
				!allowOverride, false, "the target namespace of the service");
		if (allowOverride == true) {
			overrideNamespaceBtn = createOverrideButton(parent,
					namespaceText, null);
		}

		return parent;
	}
	
	protected void targetNamespaceModified(String newNamespace) {
		if (StringUtils.isNotBlank(newNamespace)) {
			final String defaultPkgName = ConfigTool
					.getDefaultPackageNameFromNamespace(newNamespace);
			if (StringUtils.isNotBlank(defaultPkgName)) {
				if (overrideServicePackageButton != null 
						&& overrideServicePackageButton.getSelection() == false) {
					setServicePackage(getDefaultServicePackageName(defaultPkgName));
				}
				if (overrideServiceImplementationButton != null
						&& overrideServiceImplementationButton.getSelection() == false) {
					setServiceImplementation(getDefaultServiceImplName(defaultPkgName));
				}
			}
		}
	}
	
	public String populateServiceNamespace() {
		return getOrganizationProvider()
		.generateServiceNamespace(getServiceDomain(), getDomainClassifier(), getServiceVersion());
	}

	protected void addWSDLPackageToNamespace(final Composite parent) {
		ns2pkgViewer = new TableViewer(parent, SWT.SINGLE | SWT.H_SCROLL
				| SWT.V_SCROLL | SWT.BORDER | SWT.FULL_SELECTION);
		final Table table = ns2pkgViewer.getTable();
		table.setLinesVisible(true);
		table.setHeaderVisible(true);

		final GridData gridData = new GridData(GridData.FILL_BOTH);
		gridData.horizontalSpan = ((GridLayout) parent.getLayout()).numColumns;
		table.setLayoutData(gridData);

		final TableLayout layout = new TableLayout();
		layout.addColumnData(new ColumnWeightData(50, 75, true));
		layout.addColumnData(new ColumnWeightData(50, 75, true));
		table.setLayout(layout);

		{// columns
			TableColumn column = new TableColumn(table, SWT.LEFT);
			column.setText("Namespace");
			column.setImage(UIActivator.getImageFromRegistry("element.gif"));

			column = new TableColumn(table, SWT.LEFT);
			column.setText("Package Name");
			column.setImage(UIActivator.getImageFromRegistry("package.gif"));
		}

		ns2pkgViewer.setContentProvider(new IStructuredContentProvider() {

			public Object[] getElements(Object inputElement) {
				final Set<NamespaceToPackageModel> result = new LinkedHashSet<NamespaceToPackageModel>();
				if (inputElement instanceof Definition) {
					Definition wsdl = (Definition) inputElement;
					for (final String namespace : WSDLUtil
							.getAllTargetNamespaces(wsdl)) {
						//we do not need the default namespace to be displayed
						if (getOrganizationProvider().shouldShowInNamespaceToPackageViewer(namespace)) {
							final String defaultPkgName = ConfigTool
							.getTypePackageNameFromNamespace(namespace, WSDLUtil.getServiceNameFromWSDL(wsdl));
							result.add(new NamespaceToPackageModel(namespace,
									defaultPkgName));
						}
					}
				}
				return result.toArray();
			}

			public void dispose() {

			}

			public void inputChanged(Viewer viewer, Object oldInput,
					Object newInput) {

			}
		});

		ns2pkgViewer.setLabelProvider(new ITableLabelProvider() {

			public Image getColumnImage(Object element, int columnIndex) {
				return null;
			}

			public String getColumnText(Object element, int columnIndex) {
				if ((element instanceof NamespaceToPackageModel) == false) {
					return SOAProjectConstants.EMPTY_STRING;
				}
				switch (columnIndex) {
				case 0: {
					return ((NamespaceToPackageModel) element).namespace;
				}
				case 1:
					return ((NamespaceToPackageModel) element).pacakgeName;
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

		ns2pkgViewer.setColumnProperties(new String[] { PROP_NAME_NAMESPACE,
				PROP_NAME_PACKAGE });
		ns2pkgViewer.setCellEditors(new CellEditor[] {
				new TextCellEditor(table), new TextCellEditor(table) });
		ns2pkgViewer.setCellModifier(new ICellModifier() {

			public boolean canModify(Object element, String property) {
				return PROP_NAME_PACKAGE.equals(property);
			}

			public Object getValue(Object element, String property) {
				if ((element instanceof NamespaceToPackageModel) == false) {
					return SOAProjectConstants.EMPTY_STRING;
				}
				if (PROP_NAME_NAMESPACE.equals(property)) {
					return ((NamespaceToPackageModel) element).namespace;
				} else if (PROP_NAME_PACKAGE.equals(property)) {
					return ((NamespaceToPackageModel) element).pacakgeName;
				}
				return SOAProjectConstants.EMPTY_STRING;
			}

			public void modify(Object element, String property, Object value) {
				if ((element instanceof TableItem) == false
						|| PROP_NAME_PACKAGE.equals(property) == false)
					return;
				final NamespaceToPackageModel model = (NamespaceToPackageModel) ((TableItem) element)
						.getData();
				final String newValue = String.valueOf(value).trim();
				if (model.pacakgeName.equals(newValue) == false) {
					final IStatus status = validatePackageName(newValue);
					((TableItem) element).setText(1, newValue);
					model.pacakgeName = newValue;
					updatePageStatus(ns2pkgViewer.getTable(), status);
					if (status.isOK()) {
						((TableItem) element).setImage(1, null);
						dialogChanged(true);
					} else {
						((TableItem) element).setImage(1, UIActivator
								.getImageFromRegistry("error.gif"));
					}
				}
			}
		});
	}

	private IStatus validatePackageName(final String newPkgValue) {
		if (StringUtils.isBlank(newPkgValue)) {
			return EclipseMessageUtils
					.createErrorStatus("Package name can not be either null or empty string");
		}
		if (getNamespaceToPackageMappings().containsValue(newPkgValue)) {
			return EclipseMessageUtils
					.createErrorStatus("Package name must be unique->"
							+ newPkgValue);
		}
		return JDTUtil.validatePacakgeName(newPkgValue);
	}

	public Map<String, String> getNamespaceToPackageMappings() {
		final Map<String, String> result = new LinkedHashMap<String, String>();
		for (final TableItem item : ns2pkgViewer.getTable().getItems()) {
			NamespaceToPackageModel model = (NamespaceToPackageModel) item
					.getData();
			result.put(model.namespace.trim(), model.pacakgeName.trim());
		}
		return result;
	}

	private static class NamespaceToPackageModel {
		private String namespace;
		private String pacakgeName = SOAProjectConstants.EMPTY_STRING;

		public NamespaceToPackageModel(String namespace, String pacakgeName) {
			super();
			this.namespace = namespace;
			this.pacakgeName = pacakgeName;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result
					+ ((namespace == null) ? 0 : namespace.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			final NamespaceToPackageModel other = (NamespaceToPackageModel) obj;
			if (namespace == null) {
				if (other.namespace != null)
					return false;
			} else if (!namespace.equals(other.namespace))
				return false;
			return true;
		}
	}

	private static final String PROP_NAME_NAMESPACE = "namespace";
	private static final String PROP_NAME_PACKAGE = "package";

	private boolean setServiceNameFromWSDL(final String wsdlURL) {
		final Definition oldWsdl = this.wsdl;
		try {
			final Definition definition = WSDLUtil.readWSDL(wsdlURL);
			this.wsdl = definition;
			final Collection<?> services = definition.getServices().values();
			if (services.size() > 0) { // we believe that the wsdl should
				// contain only one service
				final Service service = (Service) services.toArray()[0];
				setServiceName(service.getQName().getLocalPart());
			} else
				setServiceName(SOAProjectConstants.EMPTY_STRING);
			if (!StringUtils.equals(wsdlURL, getWSDLURL()))
				wsdlURLText.setText(wsdlURL);
			this.ns2pkgViewer.setInput(definition);
			wsdlChanged(definition);
		} catch (final WSDLException wsdlE) {
			this.wsdl = oldWsdl;
			getContainer().showPage(this);
			resetServiceName();
			updateStatus(this.wsdlURLText, StringUtil.toString(
					"Problem acquiring WSDL from URL: ", wsdlURL, ". ", wsdlE
							.getLocalizedMessage()));
			wsdlURLText.setText(wsdlURL);
			return false;
		}
		return true;
	}
	
	protected Definition getCurrentWSDLDefinition() {
		return this.wsdl;
	}
	
	@Override
	protected void domainClassifierChanged() {
		final Text adminText = getResourceNameText();
		if (adminText != null && overrideAdminNameButton != null
				&& overrideAdminNameButton.getSelection() == false) {
			getResourceNameText().setText(computeServiceName());
		}
		
		if (namespaceText != null && overrideNamespaceBtn != null
				&& overrideNamespaceBtn.getSelection() == false) {
			namespaceText.setText(populateServiceNamespace());
		}
		if (typeNamespaceText != null && 
				overrideTypeNSButton.getSelection() == false) {
			typeNamespaceText.setText(getTargetNamespace());
		}
		
		dialogChanged();
	}
	
	@Override
	protected void setServiceName(String serviceName) {
		if (serviceName != null) {
			if (publicServiceNameText != null) {
				publicServiceNameText.setText(serviceName);
				super.setServiceName(computeServiceName());
			} else {
				super.setServiceName(serviceName);
			}
		}
	}

	@Override
	protected Text addServiceVersion(Composite composite) {
		final Text text = super.addServiceVersion(composite);
		text.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				serviceVersionChanged(text.getText());
			}
			
		});
		return text;
	}
	
	protected void serviceVersionChanged(String newServiceVersion) {
		if (publicServiceNameText != null) {
			//we only set the service name if we use public service name text field
			if ((getResourceNameText() == adminNameText
					&& overrideAdminNameButton != null 
					&& overrideAdminNameButton.getSelection() == true) == false) {
				//admin name is available and user choose NOT to override
				AbstractNewServiceFromWSDLWizardPage
				.super.setServiceName(computeServiceName());
			}
		}
		if (namespaceText!= null && overrideNamespaceBtn != null
				&& overrideNamespaceBtn.getSelection() == false) {
			namespaceText.setText(populateServiceNamespace());
			if (typeNamespaceText != null && overrideTypeNSButton != null
					&& overrideTypeNSButton.getSelection() == false) {
				typeNamespaceText.setText(namespaceText.getText());
			}
		}
	}

	protected Text addServiceName(final Composite composite, boolean editable) {
		final ModifyListener svcNameListener = new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				if (publicServiceNameText != null) {
					if (servicePackageText != null && overrideServicePackageButton != null) {
						if (overrideServicePackageButton.getSelection() == false) {
							String pkgName = getDefaultServicePackageName();
							if (pkgName != null && pkgName.startsWith(".") == false) {
								servicePackageText.setText(pkgName);
							}
						}
					}
					
					if (serviceImplementationText != null && overrideServiceImplementationButton != null) {
						if (!overrideServiceImplementationButton.getSelection())
							serviceImplementationText
									.setText(getDefaultServiceImplName());
					}
					
					/*final ControlDecoration dec = 
						getErrorDecorations().get(serviceImplementationText);
					if ((serviceImplementationText.isEnabled()
							&& serviceImplementationText.getEditable()
							&& serviceImplementationText.isFocusControl())
							|| (dec != null && StringUtils.isNotBlank(dec.getDescriptionText())))
						dialogChanged();*/
				}
				if (getResourceNameText() == adminNameText && 
						overrideAdminNameButton != null 
						&& overrideAdminNameButton.getSelection() == true)
					//adminname exist and has been overriden
					return;
				AbstractNewServiceFromWSDLWizardPage
				.super.setServiceName(computeServiceName());
			}
		};
		publicServiceNameText = createLabelTextField(composite, "&Service Name:",
				"", svcNameListener, "the name of the new service in the WSDL file");
		publicServiceNameText.setEditable(editable);
		publicServiceNameText.addModifyListener(modifyListener);
		return publicServiceNameText;
	}
	
	
	public String getPublicServiceName() {
		String publicSvcName = getTextValue(publicServiceNameText);
		return publicSvcName;
	}
	
	public String computeServiceName() {
		final String serviceName = getPublicServiceName();
		if (StringUtils.isEmpty(serviceName))
			return DEFAULT_TEXT_VALUE;
		
		String domainClassifier = getDomainClassifier();
		if (StringUtils.isBlank(domainClassifier)) {
			domainClassifier = getOrganizationProvider().getNamespacePartFromTargetNamespace(getTargetNamespace());
		}
		
		return SOAServiceUtil.computeAdminName(serviceName, domainClassifier, getServiceVersion());
	}
	
	@Override
	public String getDefaultResourceName() {
		final String defaultName = computeServiceName();
		if (StringUtils.isNotBlank(defaultName))
			return defaultName;
		else
			return super.getDefaultResourceName();
	}
	
	@Override
	protected String getDefaultServicePackageNamePrefix() {
		return SOAServiceUtil.generatePackageNamePrefix(getTargetNamespace());
	}
	
	@Override
	protected String getDefaultIMplPackageNamePrefix() {
		return SOAServiceUtil.generatePackageNamePrefix(getTargetNamespace());
	}
	
	public String getTargetNamespace() {
		return getTextValue(namespaceText);
	}
	
	public Text getTargetNamespaceText() {
		return namespaceText;
	}
	
	public void setTargetNamespace(String namespace) {
		if (StringUtils.isNotBlank(namespace) && this.namespaceText != null)
			this.namespaceText.setText(namespace);
	}

	public String getWSDLURL() {
		if (wsdlURLText == null || StringUtils.isBlank(wsdlURLText.getText()))
			return "";
		return getTextValue(wsdlURLText);
	}
	
	@Override
	public String getDefaultValue(Text text) {
		if (text == this.namespaceText) {
			return populateServiceNamespace();
		} else {
			return super.getDefaultValue(text);
		}
	}

	public abstract void wsdlChanged(final Definition wsdl);
}
