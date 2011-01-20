/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.resources.model;

import org.ebayopensource.turmeric.eclipse.resources.constants.SOAProjectConstants;
import org.ebayopensource.turmeric.eclipse.resources.constants.SOAProjectConstants.InterfaceSourceType;
import org.ebayopensource.turmeric.eclipse.resources.ui.model.ServiceFromWsdlParamModel;
import org.ebayopensource.turmeric.eclipse.soatools.configtool.ConfigTool.ISOAServiceConfig;


/**
 * @author smathew
 * 
 */
public class SOAImplMetadata extends SOAConsumerMetadata implements ISOAServiceConfig{
	

	private SOAIntfMetadata intfMetadata;
	private String serviceImplProjectName;
//	private boolean includeTestJsp;
//	private boolean includeValidateInternalsServlet;
	private String implVersion;
	private String supportedVersion;
	private String group;
	private String targetNamespace;
	private String fullyQualifiedServiceName;
	private String serviceImplClassName;
	private String messageProtocol;

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
	
	// Called from SOA Propery Page, Step by step filling of data
	public static SOAImplMetadata create(String serviceImplProjectName,
			String baseConsumerSrcDir) {
		SOAImplMetadata metadata = new SOAImplMetadata();
		metadata.setServiceImplProjectName(serviceImplProjectName);
		metadata.setBaseConsumerSrcDir(baseConsumerSrcDir);
		return metadata;
	}

	/*// Called from SOA Propery Page, Step by step filling of data
	public static SOAImplMetadata create(String serviceImplProjectName,
			boolean includeTestJsp, boolean includeValidateInternalsServlet, 
			String baseConsumerSrcDir) {
		SOAImplMetadata metadata = new SOAImplMetadata();
		metadata.setServiceImplProjectName(serviceImplProjectName);
		metadata.setIncludeTestJsp(includeTestJsp);
		metadata.setIncludeValidateInternalsServlet(includeValidateInternalsServlet);
		metadata.setBaseConsumerSrcDir(baseConsumerSrcDir);
		return metadata;
	}*/

	public String getMetadataFileName() {
		return SOAProjectConstants.PROPS_FILE_SERVICE_IMPL;
	}
	
	public SOAIntfMetadata getIntfMetadata() {
		if (intfMetadata == null)
			intfMetadata = SOAIntfMetadata.create(InterfaceSourceType.WSDL);
		return intfMetadata;
	}

	public void setIntfMetadata(SOAIntfMetadata intfMetadata) {
		this.intfMetadata = intfMetadata;
	}

	public String getServiceImplProjectName() {
		return serviceImplProjectName;
	}

	public void setServiceImplProjectName(String serviceImplName) {
		this.serviceImplProjectName = serviceImplName;
	}

	/*public boolean isIncludeTestJsp() {
		return includeTestJsp;
	}

	public void setIncludeTestJsp(boolean includeTestJsp) {
		this.includeTestJsp = includeTestJsp;
	}

	public boolean isIncludeValidateInternalsServlet() {
		return includeValidateInternalsServlet;
	}

	public void setIncludeValidateInternalsServlet(
			boolean includeValidateInternalsServlet) {
		this.includeValidateInternalsServlet = includeValidateInternalsServlet;
	}*/

	public String getImplVersion() {
		return implVersion;
	}

	public void setImplVersion(String implVersion) {
		this.implVersion = implVersion;
	}
	
	public String getTargetNamespace() {
		return targetNamespace;
	}
	public String getFullyQualifiedServiceName() {
		return fullyQualifiedServiceName;
	}
	public String getSupportedVersion() {
		return supportedVersion;
	}
	public String getGroup() {
		return group;
	}
	public String getServiceName() {
		return getIntfMetadata().getServiceName();
	}
	public String getServiceInterfaceClassName() {
		return getIntfMetadata().getServiceInterface();
	}
	public String getServiceImplClassName() {
		return serviceImplClassName;
	}

	public void setSupportedVersion(String supportedVersion) {
		this.supportedVersion = supportedVersion;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public void setTargetNamespace(String targetNamespace) {
		this.targetNamespace = targetNamespace;
	}

	public void setServiceName(String serviceName) {
		getIntfMetadata().setServiceName(serviceName);
	}

	public void setFullyQualifiedServiceName(String fullyQualifiedServiceName) {
		this.fullyQualifiedServiceName = fullyQualifiedServiceName;
	}

	public void setServiceInterfaceClassName(String serviceInterfaceClassName) {
		getIntfMetadata().setServiceInterface(serviceInterfaceClassName);
	}

	public void setServiceImplClassName(String serviceImplClassName) {
		this.serviceImplClassName = serviceImplClassName;
	}

	public String getMessageProtocol() {
		return messageProtocol;
	}

	public void setMessageProtocol(String messageProtocol) {
		this.messageProtocol = messageProtocol;
	}

	
}
