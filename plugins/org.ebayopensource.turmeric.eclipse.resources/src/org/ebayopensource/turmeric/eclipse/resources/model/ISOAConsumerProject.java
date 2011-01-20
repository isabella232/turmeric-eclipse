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
package org.ebayopensource.turmeric.eclipse.resources.model;

import java.io.IOException;
import java.util.Map;

import org.ebayopensource.turmeric.eclipse.resources.constants.SOAProjectConstants;
import org.ebayopensource.turmeric.eclipse.soatools.configtool.ConfigTool.ISOAClientConfig;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.jdom.Document;


/**
 * @author yayu
 *
 */
public interface ISOAConsumerProject extends ISOAProject {

	public static final String META_SRC_GLOBAL_ClIENT_CONFIG = SOAProjectConstants.META_SRC_META_INF
	+ "/soa/client/config/GlobalClientConfig.xml";
	public static final String FILE_ClIENT_CONFIG = "ClientConfig.xml";

	public Map<String, SOAIntfMetadata> getRequiredServices();
	
	public void setRequiredServices(Map<String, SOAIntfMetadata> requiredServices);
	
	public Map<SOAClientEnvironment, SOAClientConfig> getClientConfigs();

	public void setClientConfigs(Map<SOAClientEnvironment, SOAClientConfig> clientConfigs);
	
	public Map<SOAClientEnvironment, IFile> getClientConfigFiles() throws CoreException;
	
	public IFile getClientConfigFile(final SOAClientEnvironment clientEnv) throws CoreException, IOException;
	
	public static class SOAClientEnvironment implements Comparable<SOAClientEnvironment>{
		private String environment;
		private String serviceName;
		public SOAClientEnvironment(String environment, String serviceName) {
			super();
			this.environment = environment;
			this.serviceName = serviceName;
		}
		public String getEnvironment() {
			return environment;
		}
		public String getServiceName() {
			return serviceName;
		}
		public void setEnvironment(String environment) {
			this.environment = environment;
		}
		public void setServiceName(String serviceName) {
			this.serviceName = serviceName;
		}
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result
					+ ((environment == null) ? 0 : environment.hashCode());
			result = prime * result
					+ ((serviceName == null) ? 0 : serviceName.hashCode());
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
			final SOAClientEnvironment other = (SOAClientEnvironment) obj;
			if (environment == null) {
				if (other.environment != null)
					return false;
			} else if (!environment.equals(other.environment))
				return false;
			if (serviceName == null) {
				if (other.serviceName != null)
					return false;
			} else if (!serviceName.equals(other.serviceName))
				return false;
			return true;
		}
		public int compareTo(SOAClientEnvironment o) {
			if (o != null) {
				return environment.compareTo(o.environment);
			}
			return 0;
		}
		
	}

	public static class SOAClientConfig implements ISOAClientConfig{
		private String targetNamespace;
		private String serviceName;
		private String fullyQualifiedServiceName;
		private String group;
		private String serviceInterfaceClassName;
		private String serviceLocation;
		private String wsdlLocation;
		private String serviceBinding;
		private String consumerId;
		private String invocationUseCase;
		private String requestDataBinding;
		private String responseDataBinding;
		private String messageProtocol;
		private Document document;
		private IFile file;
		public void setTargetNamespace(String targetNamespace) {
			this.targetNamespace = targetNamespace;
		}
		public void setServiceName(String serviceName) {
			this.serviceName = serviceName;
		}
		public void setFullyQualifiedServiceName(String fullyQualifiedServiceName) {
			this.fullyQualifiedServiceName = fullyQualifiedServiceName;
		}
		public void setGroup(String group) {
			this.group = group;
		}
		public void setConsumerId(String consumerId) {
			this.consumerId = consumerId;
		}
		public void setServiceInterfaceClassName(String serviceInterfaceClassName) {
			this.serviceInterfaceClassName = serviceInterfaceClassName;
		}
		public void setServiceLocation(String serviceLocation) {
			this.serviceLocation = serviceLocation;
		}
		public void setWsdlLocation(String wsdlLocation) {
			this.wsdlLocation = wsdlLocation;
		}
		public void setServiceBinding(String serviceBinding) {
			this.serviceBinding = serviceBinding;
		}
		public void setInvocationUseCase(String invocationUseCase) {
			this.invocationUseCase = invocationUseCase;
		}
		public void setRequestDataBinding(String requestDataBinding) {
			this.requestDataBinding = requestDataBinding;
		}
		public void setResponseDataBinding(String responseDataBinding) {
			this.responseDataBinding = responseDataBinding;
		}
		public void setMessageProtocol(String messageProtocol) {
			this.messageProtocol = messageProtocol;
		}
		public String getGroup() {
			return group;
		}
		public String getServiceName() {
			return serviceName;
		}
		public String getServiceInterfaceClassName() {
			return serviceInterfaceClassName;
		}
		public String getServiceLocation() {
			return serviceLocation;
		}
		public String getWsdlLocation() {
			return wsdlLocation;
		}
		public String getTargetNamespace() {
			return targetNamespace;
		}
		public String getFullyQualifiedServiceName() {
			return fullyQualifiedServiceName;
		}
		public String getServiceBinding() {
			return serviceBinding;
		}
		public String getInvocationUseCase() {
			return invocationUseCase;
		}
		public String getRequestDataBinding() {
			return requestDataBinding;
		}
		public String getResponseDataBinding() {
			return responseDataBinding;
		}
		public String getConsumerId() {
			return consumerId;
		}
		public String getMessageProtocol() {
			return messageProtocol;
		}
		public Document getDocument() {
			return document;
		}
		public void setDocument(Document document) {
			this.document = document;
		}
		public IFile getFile() {
			return file;
		}
		public void setFile(IFile file) {
			this.file = file;
		}
		@Override
		public String toString() {
			if (getFile() != null) {
				return getFile().getLocation().toString();
			}
			return super.toString();
		}
	}
}
