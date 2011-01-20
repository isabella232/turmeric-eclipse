/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.resources.model;

import java.net.URL;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

import javax.wsdl.WSDLException;

import org.apache.commons.lang.StringUtils;
import org.ebayopensource.turmeric.eclipse.resources.constants.SOAProjectConstants;
import org.ebayopensource.turmeric.eclipse.resources.ui.model.ConsumerFromJavaParamModel;
import org.ebayopensource.turmeric.eclipse.resources.ui.model.ServiceFromWsdlParamModel;
import org.ebayopensource.turmeric.eclipse.resources.util.SOAIntfUtil;


/**
 * Holds the meta data information corresponding to an interface project.
 * Interface meta data is computed from the UI model and also from the project
 * properties file and is used as a place holder for SOA domain specific data
 * corresponding to an interface project. In the new project creation it is
 * created from the UI model and for existing projects it is created from the
 * project resource like the properties files and configuration xml file.
 * 
 * @author smathew
 */
public class SOAIntfMetadata extends AbstractSOAMetadata {

	private String publicServiceName; //this now means the serviceName
	private String serviceName; //this now means the adminName
	private String serviceVersion;
	private String serviceInterface;
	private String serviceLayer;
	private String serviceDomainName;
	private String serviceNamespacePart;
	private String imlementationProjectName;
	private SOAProjectConstants.InterfaceSourceType sourceType;
	private SOAProjectConstants.InterfaceWsdlSourceType wsdlSourceType;
	private URL originalWSDLUrl;
	private String targetNamespace;
	private String typeNamespace;
	private boolean typeFolding;
	private String serviceLocation;
	private String metadataVersion = SOAProjectConstants.DEFAULT_VERSION;
	private Map<String, String> namespaceToPackageMappings = new ConcurrentHashMap<String, String>();

	/**
	 * This is called from the UI processors. Since we have the user entered
	 * information at the creation flow the UI object is passed and is converted
	 * to this intermediate object for the rest of the processing logic.
	 * 
	 * @param serviceName
	 * @param serviceVersion
	 * @param serviceInterface
	 * @param serviceLayer
	 * @param wsdlURL
	 * @return This has been made a static factory to enable object pooling and
	 *         controlled creation.
	 * @throws WSDLException
	 */
	public static SOAIntfMetadata create(ServiceFromWsdlParamModel paramModel)
			throws WSDLException {
		SOAIntfMetadata metadata = new SOAIntfMetadata();
		metadata.setPublicServiceName(paramModel.getPublicServiceName());
		metadata.setServiceName(paramModel.getServiceName());
		metadata.setTypeNamespace(paramModel.getTypeNamespace());
		metadata.setTypeFolding(paramModel.getTypeFolding());
		metadata.setServiceVersion(paramModel.getServiceVersion());
		metadata.setServiceInterface(paramModel.getServiceInterface());
		metadata.setServiceLayer(paramModel.getServiceLayer());
		metadata.setSourceType(SOAProjectConstants.InterfaceSourceType.WSDL);
		metadata.setWsdlSourceType(paramModel.getWSDLSourceType());
		metadata.setOriginalWSDLUrl(paramModel.getOriginalWsdlUrl());
		metadata.setMetadataVersion(SOAProjectConstants.PROPS_DEFAULT_PROPERTY_VERSION);
		metadata.setNamespaceToPackageMappings(paramModel
				.getNamespaceToPacakgeMappings());
		metadata.setServiceNamespacePart(paramModel.getNamespacePart());
		metadata.setServiceDomainName(paramModel.getServiceDomain());
		SOAIntfUtil.setInformationFromWsdl(paramModel.getOriginalWsdlUrl(),
				metadata);
		return metadata;
	}

	/**
	 * This is called from the builder as the first step and later the other
	 * properties are filled in by passing this object to other processing
	 * classes which reads the configuration files and finally creates the SOA
	 * specific domain data also.
	 * 
	 * @param intfSource
	 * @return This method would be called from builder because it does not have
	 *         the user parameters in hand. This would be a step by step
	 *         creation process
	 */
	public static SOAIntfMetadata create(
			SOAProjectConstants.InterfaceSourceType intfSource) {
		SOAIntfMetadata metadata = new SOAIntfMetadata();
		metadata.setSourceType(intfSource);
		return metadata;
	}

	/*public static SOAIntfMetadata create(final String serviceName,
			final String serviceVersion, final String serviceLayer) {
		final SOAIntfMetadata metadata = new SOAIntfMetadata();
		metadata.setServiceName(serviceName);
		metadata.setServiceVersion(serviceVersion);
		metadata.setServiceLayer(serviceLayer);
		return metadata;
	}*/

	/**
	 * Called from Consumer
	 */
	public static Map<String, SOAIntfMetadata> create(
			ConsumerFromJavaParamModel paramModel) {
		Map<String, SOAIntfMetadata> metadatas = new TreeMap<String, SOAIntfMetadata>();
		for (String serviceName : paramModel.getServiceNames()) {
			SOAIntfMetadata metadata = new SOAIntfMetadata();
			metadata.setServiceName(serviceName);
			metadatas.put(serviceName, metadata);
		}
		return metadatas;
	}

	private SOAIntfMetadata() {

	}

	public String getMetadataVersion() {
		return metadataVersion;
	}

	public void setMetadataVersion(String metadataVersion) {
		if (StringUtils.isNotBlank(metadataVersion))
			this.metadataVersion = metadataVersion;
	}

	public String getPublicServiceName() {
		return publicServiceName;
	}

	public void setPublicServiceName(String publicServiceName) {
		this.publicServiceName = publicServiceName;
	}

	public String getServiceDomainName() {
		return serviceDomainName;
	}

	public void setServiceDomainName(String serviceDomainName) {
		this.serviceDomainName = serviceDomainName;
	}

	public String getTypeNamespace() {
		return typeNamespace;
	}

	public void setTypeNamespace(String typeNamespace) {
		this.typeNamespace = typeNamespace;
	}

	public boolean getTypeFolding() {
		return typeFolding;
	}

	public void setTypeFolding(boolean typeFolding) {
		this.typeFolding = typeFolding;
	}

	public String getImlementationProjectName() {
		return imlementationProjectName;
	}

	public void setImlementationProjectName(String imlementationProjectName) {
		this.imlementationProjectName = imlementationProjectName;
	}

	public String getMetadataFileName() {
		return SOAProjectConstants.PROPS_FILE_SERVICE_INTERFACE;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getServiceNamespacePart() {
		return serviceNamespacePart;
	}

	public void setServiceNamespacePart(String serviceNamespacePart) {
		this.serviceNamespacePart = serviceNamespacePart;
	}

	public String getServiceVersion() {
		return serviceVersion;
	}

	public void setServiceVersion(String serviceVersion) {
		this.serviceVersion = serviceVersion;
	}

	public String getServiceInterface() {
		return serviceInterface;
	}

	public void setServiceInterface(String serviceInterface) {
		this.serviceInterface = serviceInterface;
	}

	public String getServiceLayer() {
		return serviceLayer;
	}

	public void setServiceLayer(String serviceLayer) {
		this.serviceLayer = serviceLayer;
	}

	public SOAProjectConstants.InterfaceSourceType getSourceType() {
		return sourceType;
	}

	public void setSourceType(SOAProjectConstants.InterfaceSourceType sourceType) {
		this.sourceType = sourceType;
	}

	public String getServiceLocation() {
		return serviceLocation;
	}

	public void setServiceLocation(String serviceLocation) {
		this.serviceLocation = serviceLocation;
	}

	public URL getOriginalWSDLUrl() {
		return originalWSDLUrl;
	}

	public void setOriginalWSDLUrl(URL originalWSDLUrl) {
		this.originalWSDLUrl = originalWSDLUrl;
	}

	public SOAProjectConstants.InterfaceWsdlSourceType getWsdlSourceType() {
		return wsdlSourceType;
	}

	public void setWsdlSourceType(
			SOAProjectConstants.InterfaceWsdlSourceType wsdlSourceType) {
		this.wsdlSourceType = wsdlSourceType;
	}

	public String getTargetNamespace() {
		return targetNamespace;
	}

	public void setTargetNamespace(String targetNamespace) {
		this.targetNamespace = targetNamespace;
	}

	public Map<String, String> getNamespaceToPackageMappings() {
		return namespaceToPackageMappings;
	}

	public void setNamespaceToPackageMappings(
			Map<String, String> namespaceToPackageMappings) {
		this.namespaceToPackageMappings = namespaceToPackageMappings;
	}
}
