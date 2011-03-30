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

import org.ebayopensource.turmeric.eclipse.core.resources.constants.SOAProjectConstants;
import org.ebayopensource.turmeric.eclipse.soatools.configtool.ConfigTool.ISOAClientConfig;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.jdom.Document;


/**
 * @author yayu
 *
 */
public interface ISOAConsumerProject extends ISOAProject {

	/**
	 * 
	 */
	public static final String META_SRC_GLOBAL_ClIENT_CONFIG = SOAProjectConstants.META_SRC_META_INF
	+ "/soa/client/config/GlobalClientConfig.xml";
	
	/**
	 * 
	 */
	public static final String FILE_ClIENT_CONFIG = "ClientConfig.xml";

	/**
	 * 
	 * @return A Map of String and SOAIntfMetadata
	 */
	public Map<String, SOAIntfMetadata> getRequiredServices();
	
	/**
	 * 
	 * @param requiredServices A Map of String and SOAIntfMetadata
	 */
	public void setRequiredServices(Map<String, SOAIntfMetadata> requiredServices);
	
	/**
	 * 
	 * @return a Map of SOAClientEnvironment and SOAClientConfig objects.
	 */
	public Map<SOAClientEnvironment, SOAClientConfig> getClientConfigs();

	/**
	 * 
	 * @param clientConfigs a Map of SOAClientEnvironment and SOAClientConfig objects.
	 */
	public void setClientConfigs(Map<SOAClientEnvironment, SOAClientConfig> clientConfigs);
	
	/**
	 * 
	 * @return a Map of client Config files.
	 * @throws CoreException 
	 */
	public Map<SOAClientEnvironment, IFile> getClientConfigFiles() throws CoreException;
	
	/**
	 * 
	 * @param clientEnv 
	 * @return an IFile that represents a clientConfigFile location.
	 * @throws CoreException 
	 * @throws IOException 
	 */
	public IFile getClientConfigFile(final SOAClientEnvironment clientEnv) throws CoreException, IOException;
	
	/**
	 * Represents a SOAClientEnviroments.  Client environments can be places like production
	 * test, qa, etc.
	 * 
	 *
	 */
	public static class SOAClientEnvironment implements Comparable<SOAClientEnvironment>{
		private String environment;
		private String serviceName;
		/**
		 * Constructor that setups a SOAClient Enviroment taking in environment name and serviceName.
		 * @param environment the name of the environement
		 * @param serviceName service name 
		 */
		public SOAClientEnvironment(String environment, String serviceName) {
			super();
			this.environment = environment;
			this.serviceName = serviceName;
		}
		
		/**
		 * 
		 * @return the environment name as a string
		 */
		public String getEnvironment() {
			return environment;
		}
		
		/**
		 * 
		 * @return the service name as a string
		 */
		public String getServiceName() {
			return serviceName;
		}
		
		/**
		 * Sets the environment name.
		 * @param environment the environment name as a string.
		 */
		public void setEnvironment(String environment) {
			this.environment = environment;
		}
		
		/**
		 *  
		 * @param serviceName the service name
		 */
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
		
		/**
		 * {@inheritDoc}
		 */
		public int compareTo(SOAClientEnvironment o) {
			if (o != null) {
				return environment.compareTo(o.environment);
			}
			return 0;
		}
		
	}

	/**
	 * Represents a SOAClientConfig file.
	 * 
	 *
	 */
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
			this.serviceName = serviceName;
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
		public void setGroup(String group) {
			this.group = group;
		}
		
		/**
		 * {@inheritDoc} 
		 * 
		 */
		public void setConsumerId(String consumerId) {
			this.consumerId = consumerId;
		}
		
		/**
		 * {@inheritDoc}
		 */
		public void setServiceInterfaceClassName(String serviceInterfaceClassName) {
			this.serviceInterfaceClassName = serviceInterfaceClassName;
		}
		
		/**
		 * {@inheritDoc}
		 */
		public void setServiceLocation(String serviceLocation) {
			this.serviceLocation = serviceLocation;
		}
		
		/**
		 * {@inheritDoc}
		 */
		public void setWsdlLocation(String wsdlLocation) {
			this.wsdlLocation = wsdlLocation;
		}
		
		/**
		 * {@inheritDoc}
		 */
		public void setServiceBinding(String serviceBinding) {
			this.serviceBinding = serviceBinding;
		}
		
		/**
		 * {@inheritDoc}
		 */
		public void setInvocationUseCase(String invocationUseCase) {
			this.invocationUseCase = invocationUseCase;
		}
		
		/**
		 * {@inheritDoc}
		 */
		public void setRequestDataBinding(String requestDataBinding) {
			this.requestDataBinding = requestDataBinding;
		}
		
		/**
		 * {@inheritDoc}
		 */
		public void setResponseDataBinding(String responseDataBinding) {
			this.responseDataBinding = responseDataBinding;
		}
		
		/**
		 * {@inheritDoc}
		 * 
		 */
		public void setMessageProtocol(String messageProtocol) {
			this.messageProtocol = messageProtocol;
		}
		
		/**
		 *
		 * @return the group name
		 */
		public String getGroup() {
			return group;
		}
		
		/**
		 *
		 * @return the service name
		 */
		public String getServiceName() {
			return serviceName;
		}
		
		/**
		 * 
		 * @return the service interface class name
		 */
		public String getServiceInterfaceClassName() {
			return serviceInterfaceClassName;
		}
		
		/**
		 * 
		 * @return the location of the service
		 */
		public String getServiceLocation() {
			return serviceLocation;
		}
		
		/**
		 * 
		 * @return the wsdl location
		 */
		public String getWsdlLocation() {
			return wsdlLocation;
		}
		
		/**
		 * 
		 * @return the target namespace
		 */
		public String getTargetNamespace() {
			return targetNamespace;
		}
		
		/**
		 * 
		 * @return the fully qualified service name
		 */
		public String getFullyQualifiedServiceName() {
			return fullyQualifiedServiceName;
		}
		
		/**
		 * 
		 * @return the service binding
		 */
		public String getServiceBinding() {
			return serviceBinding;
		}
		
		/**
		 * 
		 * @return the invocation use case
		 */
		public String getInvocationUseCase() {
			return invocationUseCase;
		}
		
		/**
		 * 
		 * @return the data binding request
		 */
		public String getRequestDataBinding() {
			return requestDataBinding;
		}
		
		/**
		 * 
		 * @return The data binding response
		 */
		public String getResponseDataBinding() {
			return responseDataBinding;
		}
		
		/**
		 * 
		 * @return the consumer id
		 */
		public String getConsumerId() {
			return consumerId;
		}
		
		/**
		 * 
		 * @return the message protocol
		 */
		public String getMessageProtocol() {
			return messageProtocol;
		}
		
		/**
		 * 
		 * @return A JDOM Document object
		 */
		public Document getDocument() {
			return document;
		}
		
		/**
		 * 
		 * @param document A JDOM Document
		 */
		public void setDocument(Document document) {
			this.document = document;
		}
		
		/**
		 * 
		 * @return the file as an IFile
		 */
		public IFile getFile() {
			return file;
		}
		
		/**
		 * 
		 * @param file set the file using an IFile
		 */
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
