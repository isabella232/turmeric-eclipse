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
package org.ebayopensource.turmeric.eclipse.typelibrary.ui.wizards.pages;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.QName;

import org.apache.commons.lang.StringUtils;
import org.ebayopensource.turmeric.eclipse.logging.SOALogger;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.GlobalRepositorySystem;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.ISOAHelpProvider;
import org.ebayopensource.turmeric.eclipse.typelibrary.core.SOATypeLibraryConstants;
import org.ebayopensource.turmeric.eclipse.typelibrary.utils.importtypes.TypeModel;
import org.ebayopensource.turmeric.eclipse.ui.AbstractSOAProjectWizardPage;
import org.ebayopensource.turmeric.eclipse.ui.UIActivator;
import org.ebayopensource.turmeric.eclipse.ui.monitor.typelib.SOAGlobalRegistryAdapter;
import org.ebayopensource.turmeric.eclipse.utils.plugin.EclipseMessageUtils;
import org.ebayopensource.turmeric.eclipse.utils.ui.UIUtil;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;


/**
 * @author yayu
 * 
 */
public class NewTypeLibraryWizardPage extends AbstractSOAProjectWizardPage {
	private CCombo categoryCombo;
	private Text namespaceText;
	private Button overrideNamespaceBtn;
	private List<TypeModel> types;

	public void setImportedTypes(List<TypeModel> types) {
		this.types = types;
		this.dialogChanged();
	}

	/**
	 * @param pageName
	 */
	public NewTypeLibraryWizardPage() {
		super("newTypeLibraryWizardPage", "New Type Library",
				"Create a new type library project");
	}

	public void createControl(Composite parent) {
		try {
			final Composite container = super.createParentControl(parent, 4);
			addWorkspaceRootChooser(container);
			final CCombo domainCombo = addServiceDomainList(container);
			UIUtil.decorateControl(this, domainCombo,
					"The type library functional domain");
			Text typeLibraryNameText = super.createResourceNameControl(
					container, "Type Library &Name:", modifyListener, true,
					"the name of the new type library");
			typeLibraryNameText.setFocus();

			final ModifyListener mListener = new ModifyListener() {

				public void modifyText(ModifyEvent e) {
					if (domainClassifierList != null)
						domainClassifierChanged();
					modifyListener.modifyText(e);
				}
			};
			super.createResourceVersionControl(container, "&Version:",
					mListener, "the version of the new type library");
			categoryCombo = super.createCCombo(container, "&Category:", false,
					getAllCategories().toArray(new String[0]),
					"the category of the new type library");
			addNamespace(container);
			setControl(container);
			dialogChanged();
		} catch (Exception e) {
			SOALogger.getLogger().error(e);
			throw new RuntimeException(e);
		}
	}

	protected List<String> getAllCategories() {
		List<String> strList = new ArrayList<String>();
		for (final String category : UIActivator.getCategories())
			strList.add(category);
		return strList;
	}

	@Override
	public String getDefaultResourceName() {
		return SOATypeLibraryConstants.DEFAULT_TYPE_LIB_NAME;
	}

	@Override
	protected void domainClassifierChanged() {
		if (namespaceText != null && overrideNamespaceBtn != null
				&& overrideNamespaceBtn.getSelection() == false) {
			this.namespaceText.setText(populateTypeLibraryTargetNamespace());
		}
	}

	private String populateTypeLibraryTargetNamespace() {
		String namespacePart = domainClassifierList == null ? null
				: getDomainClassifier();
		return getOrganizationProvider().generateTypeLibraryTargetNamespace(
				getServiceDomain(), namespacePart, getVersionValue());
	}

	@Override
	protected boolean dialogChanged() {

		boolean result = super.dialogChanged();
		if (result) {
			// IStatus validationModel = null;
			try {
				if (SOAGlobalRegistryAdapter.getInstance().getGlobalRegistry()
						.getTypeLibrary(getResourceName()) != null) {
					updateStatus(super.getResourceNameText(),
							"Type library with the same name already exists in the registry.");
					return false;
				}
			} catch (Exception e) {
				SOALogger.getLogger().error(e);
			}
			if (namespaceText != null) {
				try {
					new URI(namespaceText.getText());
				} catch (URISyntaxException e) {
					SOALogger.getLogger().error(e);
					updateStatus(namespaceText, "Invalid Namespace.");
					return false;
				}
			}
			if (StringUtils.isEmpty(getResourceName()) == false) {
				try {
					if (GlobalRepositorySystem.instanceOf()
							.getActiveRepositorySystem()
							.getTypeRegistryBridge().typeLibraryExists(
									getResourceName())) {
						updateStatus(super.getResourceNameText(),
								"A project with the same name already exists. Please choose a different name.");
						return false;
					}
				} catch (Exception e) {
					SOALogger.getLogger().error(e);
					updateStatus("There are some problems in activating the registry. Please open the global registry view and do a manual refresh.");
					return false;
				}
			}

			if (validateNamespaceAndTypesDuplicated() == false) {
				return false;
			}

			String tlName = this.getResourceName();
			if (StringUtils.isEmpty(tlName) == false
					&& Character.isUpperCase(tlName.charAt(0)) == false) {
				IStatus status = EclipseMessageUtils
						.createStatus(
								"By convention Type Library name should start with uppercase.",
								IStatus.WARNING);
				updatePageStatus(getResourceNameText(), status);
				return true;
			}

			// if(Character.isUpperCase((getResourceName().charAt(0))) ==
			// false){
			// updateStatus(super.getResourceNameText(),
			// "Type library name should start with upper case character");
			// return true;
			// }

		}
		return result;
	}

	private boolean validateNamespaceAndTypesDuplicated() {
		if (types == null) {
			return true;
		}
		StringBuffer dupNames = new StringBuffer();
		for (TypeModel type : types) {

			boolean hasType = true;
			try {
				hasType = SOAGlobalRegistryAdapter.getInstance().getGlobalRegistry().getType(
						new QName(namespaceText.getText(), type.getTypeName())) != null;
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (hasType) {
				dupNames.append(type.getTypeName() + ", ");
			}
		}
		if (dupNames.length() > 0) {
			String errorMsg = "Chosen types duplicated with types in the Type Repository [ "
					+ dupNames.substring(0, dupNames.length() - 2)
					+ "]. Please return to previous page and un-select these types or "
					+ "change Type Library namespace.";
			if (overrideNamespaceBtn.getSelection() == true) {
				updateStatus(errorMsg, namespaceText);
			} else {
				updateStatus(errorMsg, namespaceText, domainClassifierList,
						serviceDomainList);
			}
		}
		return false;
	}

	public String getCategoryValue() {
		return getTextValue(this.categoryCombo);
	}

	public String getNameValue() {
		return getResourceName();
	}

	public String getVersionValue() {
		return getResourceVersion();
	}

	@Override
	public String getHelpContextID() {
		return GlobalRepositorySystem.instanceOf().getActiveRepositorySystem()
				.getHelpProvider().getHelpContextID(
						ISOAHelpProvider.PAGE_CREATE_TYPE_LIBRARY);
	}

	public String getNamespaceValue() {
		return getTextValue(namespaceText);
	}

	protected Text addNamespace(Composite parent) {
		final String namespace = populateTypeLibraryTargetNamespace();

		final ModifyListener nsModifyListener = new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				// targetNamespaceModified(getNamespaceValue());
				dialogChanged();
			}
		};

		namespaceText = super.createLabelTextField(parent, "&Namespace:",
				namespace, nsModifyListener, false, false,
				"the target namespace of the service");
		overrideNamespaceBtn = createOverrideButton(parent, namespaceText, null);
		overrideNamespaceBtn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (overrideNamespaceBtn.getSelection() == false) {
					namespaceText.setText(populateTypeLibraryTargetNamespace());
				}
				dialogChanged();
			}
		});
		return namespaceText;
		/*
		 * return namespaceText = createLabelTextField(parent, "&Namespace:",
		 * TypeLibraryConstants.TYPE_INFORMATION_NAMESPACE, modifyListener,
		 * "the namespace of the new type library");
		 */
	}
}
