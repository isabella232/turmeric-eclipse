/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.services.ui.wizards.pages;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.wsdl.Definition;
import javax.wsdl.WSDLException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.lang.StringUtils;
import org.ebayopensource.turmeric.eclipse.core.logging.SOALogger;
import org.ebayopensource.turmeric.eclipse.core.resources.constants.SOAProjectConstants;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.GlobalRepositorySystem;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.ISOAHelpProvider;
import org.ebayopensource.turmeric.eclipse.resources.util.SOAIntfUtil;
import org.ebayopensource.turmeric.eclipse.resources.util.SOAServiceUtil;
import org.ebayopensource.turmeric.eclipse.services.ui.SOAMessages;
import org.ebayopensource.turmeric.eclipse.ui.AbstractSOADomainWizard;
import org.ebayopensource.turmeric.eclipse.ui.wizards.pages.AbstractNewServiceFromWSDLWizardPage;
import org.ebayopensource.turmeric.eclipse.utils.lang.StringUtil;
import org.ebayopensource.turmeric.eclipse.utils.plugin.EclipseMessageUtils;
import org.ebayopensource.turmeric.eclipse.utils.ui.UIUtil;
import org.ebayopensource.turmeric.eclipse.utils.wsdl.WSDLUtil;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.swt.widgets.Composite;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.sun.tools.xjc.api.XJC;


/**
 * The Class ServiceFromExistingWSDLWizardPage.
 *
 * @author yayu
 */
public class ServiceFromExistingWSDLWizardPage extends
		AbstractNewServiceFromWSDLWizardPage {
	private static final SOALogger logger = SOALogger.getLogger();
	public Map<String,String> typeLibNameAndPackage;
	private String versionFromWSDL = null;

	/**
	 * Instantiates a new service from existing wsdl wizard page.
	 */
	public ServiceFromExistingWSDLWizardPage() {
		super("newSOAServiceProjectFromWSDLWizardPage",
				"New SOA Service From Existing WSDL Wizard",
				"This wizard creates a new SOA Service from a pre-existing WSDL document.");
	}

	/* (non-Javadoc)
	 * @see org.ebayopensource.turmeric.eclipse.ui.AbstractSOAProjectWizardPage#setVisible(boolean)
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
	public void createControl(final Composite parent) {
		try {
			final Composite container = createParentControl(parent, 4);
			addWSDL(container);
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
			addAdminName(container);
			addTargetNamespace(container, null, false);
			addTypeNamespace(container);
			addServicePackage(container);
			addServiceImpl(container);
			addServiceLayer(container);
			createServiceImplTypeCombo(container);
			addTypeFolding(container);
			addWSDLPackageToNamespace(container);
		} catch (Exception e) {
			logger.error(e);
			UIUtil.showErrorDialog(e);
		}
	}
	
	/* (non-Javadoc)
	 * @see org.ebayopensource.turmeric.eclipse.ui.wizards.pages.AbstractNewServiceFromWSDLWizardPage#targetNamespaceModified(java.lang.String)
	 */
	@Override
	protected void targetNamespaceModified(String newNamespace) {
		super.targetNamespaceModified(newNamespace);
		if (this.serviceDomainList == null || this.domainClassifierList == null)
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
				|| StringUtils.isBlank(namespacePart)) {
			// could not get the namespace-part
			this.serviceDomainList.select(-1);
			this.serviceDomainList.clearSelection();
			this.domainClassifierList.select(-1);
			this.domainClassifierList.clearSelection();
		}
	}

	/* (non-Javadoc)
	 * @see org.ebayopensource.turmeric.eclipse.ui.SOABasePage#getHelpContextID()
	 */
	@Override
	public String getHelpContextID() {
		return GlobalRepositorySystem
				.instanceOf()
				.getActiveRepositorySystem()
				.getHelpProvider()
				.getHelpContextID(
						ISOAHelpProvider.PAGE_CREATE_SERVICE_FROM_EXISTING_WSDL);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean dialogChanged(boolean validateWsdl) {
		final boolean result = super.dialogChanged(validateWsdl);
		if (result == false && isPageComplete() == false)
			return result;

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
				updateStatus(StringUtil.formatString(
						SOAMessages.ERR_WRONG_NAMESPACEPART,
						getDomainClassifier(), this.wsdl.getTargetNamespace()),
						super.domainClassifierList, super.adminNameText,
						super.resourceVersionText, super.getResourceNameText());
				return false;
			}
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

		if ((versionFromWSDL != null)
				&& versionFromWSDL.equals(getResourceVersion()) == false) {
			String errorMsg = StringUtil.formatString(
					SOAMessages.DIFFERENT_SERVICE_VERSION_WITH_WSDL, getResourceVersion(),
					versionFromWSDL);
			updateStatus(super.resourceVersionText, errorMsg);
			return false;
		}

		if (StringUtils.isNotEmpty(getResourceName())
				&& Character.isLowerCase(getResourceName().charAt(0))) {
			updatePageStatus(getResourceNameText(), EclipseMessageUtils
					.createStatus(SOAMessages.SVCNAME_ERR, IStatus.WARNING));
			return true;
		}
		return true;
	}
	@Override
	protected boolean getDefaultEnabledNSValue(){
		Collection<String> allTNS=WSDLUtil.getAllTargetNamespaces(wsdl);
		if(allTNS.size()>1) return false;
		return true;
	}
	/* (non-Javadoc)
	 * @see org.ebayopensource.turmeric.eclipse.ui.wizards.pages.AbstractNewServiceWizardPage#getDefaultTypeNamespace()
	 */
	@Override
	public String getDefaultTypeNamespace() {
		return getTargetNamespace();
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

	/* (non-Javadoc)
	 * @see org.ebayopensource.turmeric.eclipse.ui.wizards.pages.AbstractNewServiceFromWSDLWizardPage#wsdlChanged(javax.wsdl.Definition)
	 */
		
  public boolean customCanFlipToNextPage() {
 		
    if((super.canFlipToNextPage())&&(typeLibNameAndPackage!=null))
 	 	
       if (typeLibNameAndPackage.keySet().size()>0){
          return true;
        }
    return false;
  }
	
	@Override
	public void wsdlChanged(Definition wsdl) {
		boolean nsChanged = false;
		if (wsdl != null) {
			try {
				final String targetNamespace = WSDLUtil
						.getTargetNamespace(wsdl);
				if (StringUtils.isNotBlank(targetNamespace)) {
					setTypeNamespace(targetNamespace);
					setTargetNamespace(targetNamespace);
					nsChanged = true;
				}

			} catch (WSDLException e) {
				SOALogger.getLogger().warning(e);
				updateStatus(e.getLocalizedMessage());
			}
			if (domainClassifierList == null) {
				// Non-MP
				String nsPart = StringUtils
						.capitalize(getOrganizationProvider()
								.getNamespacePartFromTargetNamespace(
										getTargetNamespace()));
				if (StringUtils.isNotBlank(nsPart)) {
					getResourceNameText().setText(nsPart + getResourceName());
				} else {
					getResourceNameText()
							.setText(
									getPublicServiceName()
											+ SOAProjectConstants.MAJOR_VERSION_PREFIX
											+ SOAServiceUtil
													.getServiceMajorVersion(getServiceVersion()));
				}
			}
			String version = SOAIntfUtil.getServiceVersionFromWsdl(wsdl,
					getPublicServiceName()).trim();
			resourceVersionText.setEditable(true);
			versionFromWSDL = null;
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
		}
		setTypeFolding(getDefaultEnabledNSValue());
		if (nsChanged == false) {
			setTargetNamespace("");
			setTypeNamespace("");
		}
		try {
			typeLibNameAndPackage=ServiceFromExistingWSDLWizardPage.getAllTypeLibraryNames(wsdl);
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

	}
	public boolean canFlipTOTL(){
		 if((typeLibNameAndPackage!=null))
		        if (typeLibNameAndPackage.keySet().size()>0){
		          return true;
		        }
		 return false;
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
			//add check for annotation tag
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
		//Removing default dependencies
		typeLibraryPackageSet.remove("MarketPlaceServiceCommonTypeLibrary");
		typeLibraryPackageSet.remove("SOACommonTypeLibrary");
		}
		catch(Exception e){
			logger.error(e);
		}
		return typeLibraryPackageSet;
	}

}
