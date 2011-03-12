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
	
	/**
	 * Returns the service name.
	 * 
	 * @return String that contains the service name.
	 */
	public String getServiceName() {
		return serviceName;
	}
	
	/**
	 * Sets the service name.
	 * 
	 * @param serviceName String that contains the service name.
	 */
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
	
	/**
	 * The target namespace for the service.
	 * 
	 * @return String the target namespace.
	 */
	public String getTargetNameSpace() {
		return targetNameSpace;
	}
	
	/**
	 * Sets the target namesapce.
	 * 
	 * @param targetNameSpace String the value to set the target namespace too.
	 */
	public void setTargetNameSpace(String targetNameSpace) {
		this.targetNameSpace = targetNameSpace;
	}
	
	/**
	 * The Service Interface Class Name.
	 *  
	 * @return String the service interface class name including the package.
	 */
	public String getServiceInterfaceClassName() {
		return serviceInterfaceClassName;
	}
	
	/**
	 * Sets the service interface classname.
	 * @param serviceInterfaceClassName String value that the interface classname should be set.
	 *   Should include package name as well.
	 */
	public void setServiceInterfaceClassName(String serviceInterfaceClassName) {
		this.serviceInterfaceClassName = serviceInterfaceClassName;
	}
	
	/**
	 * Sets the service implementation classname.
	 * 
	 * @return String return the service implementation classname with package name.
	 * 
	 */
	public String getServiceImplClassName() {
		return serviceImplClassName;
	}
	
	/**
	 * Sets the service implementation classname.
	 *  
	 * @param serviceImplClassName the full class name for the service implementation class.
	 */
	public void setServiceImplClassName(String serviceImplClassName) {
		this.serviceImplClassName = serviceImplClassName;
	}
	
	/**
	 * The service version.
	 * @return the service version.
	 */
	public String getVersion() {
		return version;
	}
	
	/**
	 * Sets the service version. 
	 * @param version the version number. usually in Major.Minor.Revision format.
	 */
	public void setVersion(String version) {
		this.version = version;
	}
	
	
	/**
	 * 
	 * @return the ProtocolProcessorName as a string
	 */
	public String getProtocolProcessorName() {
		return protocolProcessorName;
	}
	
	/**
	 * 
	 * @param protocolProcessorName the name of the protocol processor.
	 */
	public void setProtocolProcessorName(String protocolProcessorName) {
		this.protocolProcessorName = protocolProcessorName;
	}
	
	/**
	 * 
	 * @return The protocol processor version.
	 */
	public String getProtocolProcessorVersion() {
		return protocolProcessorVersion;
	}
	
	/**
	 * 
	 * @param protocolProcessorVersion the version number for the protocol processor
	 */
	public void setProtocolProcessorVersion(String protocolProcessorVersion) {
		this.protocolProcessorVersion = protocolProcessorVersion;
	}
	
	/**
	 * 
	 * @return the transport header name.
	 */
	public String getTransportHeaderName() {
		return transportHeaderName;
	}
	
	/**
	 * 
	 * @param transportHeaderName the transport header name as a string.
	 */
	public void setTransportHeaderName(String transportHeaderName) {
		this.transportHeaderName = transportHeaderName;
	}
	
	/**
	 * 
	 * @return the transport header data as a string.
	 */
	public String getTransportHeaderData() {
		return transportHeaderData;
	}
	
	/**
	 * 
	 * @param transportHeaderData the transport header data as a string.
	 * 
	 */
	public void setTransportHeaderData(String transportHeaderData) {
		this.transportHeaderData = transportHeaderData;
	}
	
	/**
	 * 
	 * @return the protocol processor classname as a string.
	 */
	public String getProtocolProcessorClassName() {
		return protocolProcessorClassName;
	}
	
	/**
	 * 
	 * @param protocolProcessorClassName the protocol processor classname as a string.
	 */
	public void setProtocolProcessorClassName(String protocolProcessorClassName) {
		this.protocolProcessorClassName = protocolProcessorClassName;
	}
}
