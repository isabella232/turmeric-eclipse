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
 * The Interface ISOAConsumerProject.
 *
 * @author yayu
 */
public interface ISOAConsumerProject extends ISOAProject {

	/** The Constant META_SRC_GLOBAL_ClIENT_CONFIG. */
	public static final String META_SRC_GLOBAL_ClIENT_CONFIG = SOAProjectConstants.META_SRC_META_INF
	+ "/soa/client/config/GlobalClientConfig.xml";
	
	/** The Constant FILE_ClIENT_CONFIG. */
	public static final String FILE_ClIENT_CONFIG = "ClientConfig.xml";

	/**
	 * Gets the required services.
	 *
	 * @return A Map of String and SOAIntfMetadata
	 */
	public Map<String, SOAIntfMetadata> getRequiredServices();
	
	/**
	 * Sets the required services.
	 *
	 * @param requiredServices A Map of String and SOAIntfMetadata
	 */
	public void setRequiredServices(Map<String, SOAIntfMetadata> requiredServices);
	
	/**
	 * Gets the client configs.
	 *
	 * @return a Map of SOAClientEnvironment and SOAClientConfig objects.
	 */
	public Map<SOAClientEnvironment, SOAClientConfig> getClientConfigs();

	/**
	 * Sets the client configs.
	 *
	 * @param clientConfigs a Map of SOAClientEnvironment and SOAClientConfig objects.
	 */
	public void setClientConfigs(Map<SOAClientEnvironment, SOAClientConfig> clientConfigs);
	
	/**
	 * Gets the client config files.
	 *
	 * @return a Map of client Config files.
	 * @throws CoreException the core exception
	 */
	public Map<SOAClientEnvironment, IFile> getClientConfigFiles() throws CoreException;
	
	/**
	 * Gets the client config file.
	 *
	 * @param clientEnv the client env
	 * @return an IFile that represents a clientConfigFile location.
	 * @throws CoreException the core exception
	 * @throws IOException Signals that an I/O exception has occurred.
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
		 * Gets the environment.
		 *
		 * @return the environment name as a string
		 */
		public String getEnvironment() {
			return environment;
		}
		
		/**
		 * Gets the service name.
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
		 * Sets the service name.
		 *
		 * @param serviceName the service name
		 */
		public void setServiceName(String serviceName) {
			this.serviceName = serviceName;
		}
		
		/* (non-Javadoc)
		 * @see java.lang.Object#hashCode()
		 */
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
		
		/* (non-Javadoc)
		 * @see java.lang.Object#equals(java.lang.Object)
		 */
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
		@Override
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
		@Override
		public void setTargetNamespace(String targetNamespace) {
			this.targetNamespace = targetNamespace;
		}
		
		/**
		 * {@inheritDoc} 
		 */
		@Override
		public void setServiceName(String serviceName) {
			this.serviceName = serviceName;
		}
		
		/**
		 * {@inheritDoc} 
		 */
		@Override
		public void setFullyQualifiedServiceName(String fullyQualifiedServiceName) {
			this.fullyQualifiedServiceName = fullyQualifiedServiceName;
		}
		
		/**
		 * {@inheritDoc} 
		 */
		@Override
		public void setGroup(String group) {
			this.group = group;
		}
		
		/**
		 * {@inheritDoc} 
		 * 
		 */
		@Override
		public void setConsumerId(String consumerId) {
			this.consumerId = consumerId;
		}
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public void setServiceInterfaceClassName(String serviceInterfaceClassName) {
			this.serviceInterfaceClassName = serviceInterfaceClassName;
		}
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public void setServiceLocation(String serviceLocation) {
			this.serviceLocation = serviceLocation;
		}
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public void setWsdlLocation(String wsdlLocation) {
			this.wsdlLocation = wsdlLocation;
		}
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public void setServiceBinding(String serviceBinding) {
			this.serviceBinding = serviceBinding;
		}
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public void setInvocationUseCase(String invocationUseCase) {
			this.invocationUseCase = invocationUseCase;
		}
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public void setRequestDataBinding(String requestDataBinding) {
			this.requestDataBinding = requestDataBinding;
		}
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public void setResponseDataBinding(String responseDataBinding) {
			this.responseDataBinding = responseDataBinding;
		}
		
		/**
		 * {@inheritDoc}
		 * 
		 */
		@Override
		public void setMessageProtocol(String messageProtocol) {
			this.messageProtocol = messageProtocol;
		}
		
		/**
		 * Gets the group.
		 *
		 * @return the group name
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
			return serviceName;
		}
		
		/**
		 * Gets the service interface class name.
		 *
		 * @return the service interface class name
		 */
		public String getServiceInterfaceClassName() {
			return serviceInterfaceClassName;
		}
		
		/**
		 * Gets the service location.
		 *
		 * @return the location of the service
		 */
		public String getServiceLocation() {
			return serviceLocation;
		}
		
		/**
		 * Gets the wsdl location.
		 *
		 * @return the wsdl location
		 */
		public String getWsdlLocation() {
			return wsdlLocation;
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
		 * @return the fully qualified service name
		 */
		public String getFullyQualifiedServiceName() {
			return fullyQualifiedServiceName;
		}
		
		/**
		 * Gets the service binding.
		 *
		 * @return the service binding
		 */
		public String getServiceBinding() {
			return serviceBinding;
		}
		
		/**
		 * Gets the invocation use case.
		 *
		 * @return the invocation use case
		 */
		public String getInvocationUseCase() {
			return invocationUseCase;
		}
		
		/**
		 * Gets the request data binding.
		 *
		 * @return the data binding request
		 */
		public String getRequestDataBinding() {
			return requestDataBinding;
		}
		
		/**
		 * Gets the response data binding.
		 *
		 * @return The data binding response
		 */
		public String getResponseDataBinding() {
			return responseDataBinding;
		}
		
		/**
		 * Gets the consumer id.
		 *
		 * @return the consumer id
		 */
		public String getConsumerId() {
			return consumerId;
		}
		
		/**
		 * Gets the message protocol.
		 *
		 * @return the message protocol
		 */
		public String getMessageProtocol() {
			return messageProtocol;
		}
		
		/**
		 * Gets the document.
		 *
		 * @return A JDOM Document object
		 */
		public Document getDocument() {
			return document;
		}
		
		/**
		 * Sets the document.
		 *
		 * @param document A JDOM Document
		 */
		public void setDocument(Document document) {
			this.document = document;
		}
		
		/**
		 * Gets the file.
		 *
		 * @return the file as an IFile
		 */
		public IFile getFile() {
			return file;
		}
		
		/**
		 * Sets the file.
		 *
		 * @param file set the file using an IFile
		 */
		public void setFile(IFile file) {
			this.file = file;
		}
		
		/* (non-Javadoc)
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			if (getFile() != null) {
				return getFile().getLocation().toString();
			}
			return super.toString();
		}
	}
}
