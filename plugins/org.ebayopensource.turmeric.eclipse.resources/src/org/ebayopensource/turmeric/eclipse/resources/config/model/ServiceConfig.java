/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.resources.config.model;

/**
 * @author smathew
 *
 */
public class ServiceConfig {
	
	private String serviceName;
	private String targetNameSpace;
	private String serviceInterfaceClassName;
	private String serviceImplClassName;
	private String version;
	private String protocolProcessorName;
	private String protocolProcessorVersion;
	private String transportHeaderName;
	private String transportHeaderData;
	private String protocolProcessorClassName;
	
	public String getServiceName() {
		return serviceName;
	}
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
	public String getTargetNameSpace() {
		return targetNameSpace;
	}
	public void setTargetNameSpace(String targetNameSpace) {
		this.targetNameSpace = targetNameSpace;
	}
	public String getServiceInterfaceClassName() {
		return serviceInterfaceClassName;
	}
	public void setServiceInterfaceClassName(String serviceInterfaceClassName) {
		this.serviceInterfaceClassName = serviceInterfaceClassName;
	}
	public String getServiceImplClassName() {
		return serviceImplClassName;
	}
	public void setServiceImplClassName(String serviceImplClassName) {
		this.serviceImplClassName = serviceImplClassName;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getProtocolProcessorName() {
		return protocolProcessorName;
	}
	public void setProtocolProcessorName(String protocolProcessorName) {
		this.protocolProcessorName = protocolProcessorName;
	}
	public String getProtocolProcessorVersion() {
		return protocolProcessorVersion;
	}
	public void setProtocolProcessorVersion(String protocolProcessorVersion) {
		this.protocolProcessorVersion = protocolProcessorVersion;
	}
	public String getTransportHeaderName() {
		return transportHeaderName;
	}
	public void setTransportHeaderName(String transportHeaderName) {
		this.transportHeaderName = transportHeaderName;
	}
	public String getTransportHeaderData() {
		return transportHeaderData;
	}
	public void setTransportHeaderData(String transportHeaderData) {
		this.transportHeaderData = transportHeaderData;
	}
	public String getProtocolProcessorClassName() {
		return protocolProcessorClassName;
	}
	public void setProtocolProcessorClassName(String protocolProcessorClassName) {
		this.protocolProcessorClassName = protocolProcessorClassName;
	}
	

}
