/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.resources.model;

import org.ebayopensource.turmeric.eclipse.soatools.configtool.ConfigTool.ISOAServiceConfig;
import org.ebayopensource.turmeric.eclipse.core.model.services.ServiceFromWsdlParamModel;
import org.ebayopensource.turmeric.eclipse.core.resources.constants.SOAProjectConstants;
import org.ebayopensource.turmeric.eclipse.core.resources.constants.SOAProjectConstants.InterfaceSourceType;


/**
 * The Class SOAImplMetadata.
 *
 * @author smathew
 */
public class SOAImplMetadata extends SOAConsumerMetadata implements ISOAServiceConfig{
	

	private SOAIntfMetadata intfMetadata;
	private String serviceImplProjectName;
	private String implVersion;
	private String supportedVersion;
	private String group;
	private String targetNamespace;
	private String fullyQualifiedServiceName;
	private String serviceImplClassName;
	private String messageProtocol;

	/**
	 * Creates the.
	 *
	 * @param paramModel parameter model from Wsdl
	 * @param intfData interface data
	 * @return an instance of SOAImplMetadata
	 */
	public static SOAImplMetadata create(ServiceFromWsdlParamModel paramModel,
			SOAIntfMetadata intfData) {
		SOAImplMetadata metadata = new SOAImplMetadata();
		metadata.setServiceImplClassName(paramModel.getServiceImpl());
		metadata.setIntfMetadata(intfData);
		metadata.setImplVersion(intfData.getServiceVersion());
		metadata.setServiceImplProjectName(intfData.getServiceName()
				+ SOAProjectConstants.IMPL_PROJECT_SUFFIX);
		metadata.setBaseConsumerSrcDir(paramModel.getBaseConsumerSrcDir());
		/*metadata.setIncludeTestJsp(paramModel.isIncludeTestJSP());
		metadata.setIncludeValidateInternalsServlet(paramModel
				.isIncludeValidateInternals());*/
		return metadata;
	}
	
	/**
	 * Called from SOA Propery Page, Step by step filling of data.
	 * 
	 * @param serviceImplProjectName implementation project name
	 * @param baseConsumerSrcDir the base consumer source directory
	 * @return an instace of SOAImplMetadata
	 */
	public static SOAImplMetadata create(String serviceImplProjectName,
			String baseConsumerSrcDir) {
		SOAImplMetadata metadata = new SOAImplMetadata();
		metadata.setServiceImplProjectName(serviceImplProjectName);
		metadata.setBaseConsumerSrcDir(baseConsumerSrcDir);
		return metadata;
	}

	/**
	 * {@inheritDoc}
	 */
	public String getMetadataFileName() {
		return SOAProjectConstants.PROPS_FILE_SERVICE_IMPL;
	}
	
	/**
	 * Gets the intf metadata.
	 *
	 * @return returns and instance of SOAIntfMetadata
	 */
	public SOAIntfMetadata getIntfMetadata() {
		if (intfMetadata == null)
			intfMetadata = SOAIntfMetadata.create(InterfaceSourceType.WSDL);
		return intfMetadata;
	}

	/**
	 * Sets the intf metadata.
	 *
	 * @param intfMetadata the interface metadata
	 */
	public void setIntfMetadata(SOAIntfMetadata intfMetadata) {
		this.intfMetadata = intfMetadata;
	}

	/**
	 * Gets the service impl project name.
	 *
	 * @return the service implementation project name
	 */
	public String getServiceImplProjectName() {
		return serviceImplProjectName;
	}

	/**
	 * Sets the service impl project name.
	 *
	 * @param serviceImplName the name of the service implementation project
	 */
	public void setServiceImplProjectName(String serviceImplName) {
		this.serviceImplProjectName = serviceImplName;
	}


	/**
	 * Gets the impl version.
	 *
	 * @return the implementation version
	 */
	public String getImplVersion() {
		return implVersion;
	}

	/**
	 * Sets the impl version.
	 *
	 * @param implVersion the implementation version
	 */
	public void setImplVersion(String implVersion) {
		this.implVersion = implVersion;
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
	 * Gets the fully qualified service name.
	 *
	 * @return the fully qualifies service name
	 */
	public String getFullyQualifiedServiceName() {
		return fullyQualifiedServiceName;
	}
	
	/**
	 * Gets the supported version.
	 *
	 * @return the supported version
	 */
	public String getSupportedVersion() {
		return supportedVersion;
	}
	
	/**
	 * Gets the group.
	 *
	 * @return the group
	 */
	public String getGroup() {
		return group;
	}
	
	/**
	 * Gets the service name.
	 *
	 * @return the service name
	 */
	public String getServiceName() {
		return getIntfMetadata().getServiceName();
	}
	
	/**
	 * Gets the service interface class name.
	 *
	 * @return the service interface class name
	 */
	public String getServiceInterfaceClassName() {
		return getIntfMetadata().getServiceInterface();
	}
	
	/**
	 * {@inheritDoc}
	 */
	public String getServiceImplClassName() {
		return serviceImplClassName;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setSupportedVersion(String supportedVersion) {
		this.supportedVersion = supportedVersion;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setGroup(String group) {
		this.group = group;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setTargetNamespace(String targetNamespace) {
		this.targetNamespace = targetNamespace;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setServiceName(String serviceName) {
		getIntfMetadata().setServiceName(serviceName);
	}

	/**
	 * {@inheritDoc}
	 */
	public void setFullyQualifiedServiceName(String fullyQualifiedServiceName) {
		this.fullyQualifiedServiceName = fullyQualifiedServiceName;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setServiceInterfaceClassName(String serviceInterfaceClassName) {
		getIntfMetadata().setServiceInterface(serviceInterfaceClassName);
	}

	/**
	 * {@inheritDoc}
	 */
	public void setServiceImplClassName(String serviceImplClassName) {
		this.serviceImplClassName = serviceImplClassName;
	}

	/**
	 * {@inheritDoc}
	 */
	public String getMessageProtocol() {
		return messageProtocol;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setMessageProtocol(String messageProtocol) {
		this.messageProtocol = messageProtocol;
	}

	
}
