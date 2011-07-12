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
import org.ebayopensource.turmeric.eclipse.core.model.services.ServiceFromWsdlParamModel;
import org.ebayopensource.turmeric.eclipse.core.resources.constants.SOAProjectConstants;
import org.ebayopensource.turmeric.eclipse.resources.ui.model.ConsumerFromJavaParamModel;
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
	private boolean isZeroConfig;
	private String metadataVersion = SOAProjectConstants.DEFAULT_VERSION;
	private Map<String, String> namespaceToPackageMappings = new ConcurrentHashMap<String, String>();
	
	private String serviceNonXSDProtocols;

	/**
	 * This is called from the UI processors. Since we have the user entered
	 * information at the creation flow the UI object is passed and is converted
	 * to this intermediate object for the rest of the processing logic.
	 *
	 * @param paramModel the param model
	 * @return This has been made a static factory to enable object pooling and
	 * controlled creation.
	 * @throws WSDLException the wSDL exception
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
		metadata.setMetadataVersion(SOAProjectConstants.PROPS_INTERFACE_PROPERTY_VERSION);
		metadata.setNamespaceToPackageMappings(paramModel
				.getNamespaceToPacakgeMappings());
		metadata.setServiceNamespacePart(paramModel.getNamespacePart());
		metadata.setServiceDomainName(paramModel.getServiceDomain());
		metadata.setServiceNonXSDProtocols(paramModel.getServiceProtocols());
		metadata.setZeroConfig(true);
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
	 * @param intfSource the interface source
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

	/**
	 * Called from Consumer.
	 * 
	 * @param paramModel the Consumer from Java Parameter Model.
	 * @return creates a Map with servicen anem and SOAIntfMetadata
	 */
	public static Map<String, SOAIntfMetadata> create(
			ConsumerFromJavaParamModel paramModel) {
		Map<String, SOAIntfMetadata> metadatas = new TreeMap<String, SOAIntfMetadata>();
		for (String serviceName : paramModel.getServiceNames()) {
			SOAIntfMetadata metadata = new SOAIntfMetadata();
			metadata.setServiceName(serviceName);
			metadatas.put(serviceName, metadata);
			metadata.setZeroConfig(true);
		}
		
		return metadatas;
	}

	
	private SOAIntfMetadata() {

	}

	public boolean isZeroConfig() {
		return isZeroConfig;
	}

	public void setZeroConfig(boolean isZeroConfig) {
		this.isZeroConfig = isZeroConfig;
	}

	/**
	 * Gets the metadata version.
	 * @return the meta data version
	 */
	public String getMetadataVersion() {
		return metadataVersion;
	}

	/**
	 * Sets the metadata version.
	 *
	 * @param metadataVersion the meta data version in major.minor format.
	 */
	public void setMetadataVersion(String metadataVersion) {
		if (StringUtils.isNotBlank(metadataVersion))
			this.metadataVersion = metadataVersion;
	}

	/**
	 * Gets the public service name.
	 *
	 * @return the public service name
	 */
	public String getPublicServiceName() {
		return publicServiceName;
	}

	/**
	 * Sets the public service name.
	 *
	 * @param publicServiceName the public service name
	 */
	public void setPublicServiceName(String publicServiceName) {
		this.publicServiceName = publicServiceName;
	}

	/**
	 * Gets the service domain name.
	 *
	 * @return the service domain name
	 */
	public String getServiceDomainName() {
		return serviceDomainName;
	}

	/**
	 * Sets the service domain name.
	 *
	 * @param serviceDomainName the service domain name
	 */
	public void setServiceDomainName(String serviceDomainName) {
		this.serviceDomainName = serviceDomainName;
	}

	/**
	 * Gets the type namespace.
	 *
	 * @return the type namespace
	 */
	public String getTypeNamespace() {
		return typeNamespace;
	}

	/**
	 * Sets the type namespace.
	 *
	 * @param typeNamespace the type namespace
	 */
	public void setTypeNamespace(String typeNamespace) {
		this.typeNamespace = typeNamespace;
	}

	/**
	 * Gets the type folding.
	 *
	 * @return Is type folding enabled?
	 */
	public boolean getTypeFolding() {
		return typeFolding;
	}

	/**
	 * Sets the type folding.
	 *
	 * @param typeFolding Should type folding be enabled.
	 */
	public void setTypeFolding(boolean typeFolding) {
		this.typeFolding = typeFolding;
	}

	/**
	 * Gets the imlementation project name.
	 *
	 * @return implementation project name
	 */
	public String getImlementationProjectName() {
		return imlementationProjectName;
	}

	/**
	 * Sets the imlementation project name.
	 *
	 * @param imlementationProjectName implementation project name
	 */
	public void setImlementationProjectName(String imlementationProjectName) {
		this.imlementationProjectName = imlementationProjectName;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getMetadataFileName() {
		return SOAProjectConstants.PROPS_FILE_SERVICE_INTERFACE;
	}

	/**
	 * Gets the service name.
	 *
	 * @return the service name
	 */
	public String getServiceName() {
		return serviceName;
	}

	/**
	 * Sets the service name.
	 *
	 * @param serviceName the service name
	 */
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	/**
	 * Gets the service namespace part.
	 *
	 * @return the service namespace part
	 */
	public String getServiceNamespacePart() {
		return serviceNamespacePart;
	}

	/**
	 * Sets the service namespace part.
	 *
	 * @param serviceNamespacePart the service namespace part
	 */
	public void setServiceNamespacePart(String serviceNamespacePart) {
		this.serviceNamespacePart = serviceNamespacePart;
	}

	/**
	 * Gets the service version.
	 *
	 * @return the service version
	 */
	public String getServiceVersion() {
		return serviceVersion;
	}

	/**
	 * Sets the service version.
	 *
	 * @param serviceVersion the service version
	 */
	public void setServiceVersion(String serviceVersion) {
		this.serviceVersion = serviceVersion;
	}

	/**
	 * Gets the service interface.
	 *
	 * @return the service interface
	 */
	public String getServiceInterface() {
		return serviceInterface;
	}

	/**
	 * Sets the service interface.
	 *
	 * @param serviceInterface the service interface
	 */
	public void setServiceInterface(String serviceInterface) {
		this.serviceInterface = serviceInterface;
	}

	/**
	 * Gets the service layer.
	 *
	 * @return the service layer
	 */
	public String getServiceLayer() {
		return serviceLayer;
	}

	/**
	 * Sets the service layer.
	 *
	 * @param serviceLayer the service layer
	 */
	public void setServiceLayer(String serviceLayer) {
		this.serviceLayer = serviceLayer;
	}

	/**
	 * Gets the source type.
	 *
	 * @return the interface source type
	 */
	public SOAProjectConstants.InterfaceSourceType getSourceType() {
		return sourceType;
	}

	/**
	 * Sets the source type.
	 *
	 * @param sourceType the source type
	 */
	public void setSourceType(SOAProjectConstants.InterfaceSourceType sourceType) {
		this.sourceType = sourceType;
	}

	/**
	 * Gets the service location.
	 *
	 * @return service location
	 */
	public String getServiceLocation() {
		return serviceLocation;
	}

	/**
	 * Sets the service location.
	 *
	 * @param serviceLocation the service location
	 */
	public void setServiceLocation(String serviceLocation) {
		this.serviceLocation = serviceLocation;
	}

	/**
	 * Gets the original wsdl url.
	 *
	 * @return the location of the original WSDL
	 */
	public URL getOriginalWSDLUrl() {
		return originalWSDLUrl;
	}

	/**
	 * Sets the original wsdl url.
	 *
	 * @param originalWSDLUrl the location of the original wsdl.
	 */
	public void setOriginalWSDLUrl(URL originalWSDLUrl) {
		this.originalWSDLUrl = originalWSDLUrl;
	}

	/**
	 * Gets the wsdl source type.
	 *
	 * @return the wsdl source type
	 */
	public SOAProjectConstants.InterfaceWsdlSourceType getWsdlSourceType() {
		return wsdlSourceType;
	}

	/**
	 * Sets the wsdl source type.
	 *
	 * @param wsdlSourceType the wsdl source type
	 */
	public void setWsdlSourceType(
			SOAProjectConstants.InterfaceWsdlSourceType wsdlSourceType) {
		this.wsdlSourceType = wsdlSourceType;
	}

	/**
	 * Gets the target namespace.
	 *
	 * @return the target namespace
	 */
	public String getTargetNamespace() {
		return targetNamespace;
	}

	/**
	 * Sets the target namespace.
	 *
	 * @param targetNamespace the target namespace
	 */
	public void setTargetNamespace(String targetNamespace) {
		this.targetNamespace = targetNamespace;
	}

	/**
	 * Gets the namespace to package mappings.
	 *
	 * @return A map of namespaces to package mappings
	 */
	public Map<String, String> getNamespaceToPackageMappings() {
		return namespaceToPackageMappings;
	}

	/**
	 * Sets the namespace to package mappings.
	 *
	 * @param namespaceToPackageMappings a Map of namespaces to package mappings
	 */
	public void setNamespaceToPackageMappings(
			Map<String, String> namespaceToPackageMappings) {
		this.namespaceToPackageMappings = namespaceToPackageMappings;
	}

	public String getServiceNonXSDProtocols() {
		return serviceNonXSDProtocols;
	}

	public void setServiceNonXSDProtocols(String serviceNonXSDProtocol) {
		this.serviceNonXSDProtocols = serviceNonXSDProtocol;
	}
}
