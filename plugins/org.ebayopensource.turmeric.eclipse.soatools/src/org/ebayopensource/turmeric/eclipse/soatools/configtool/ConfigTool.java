/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.soatools.configtool;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.ebayopensource.turmeric.eclipse.logging.SOALogger;
import org.ebayopensource.turmeric.tools.codegen.CodeGenInfoFinder;
import org.ebayopensource.turmeric.tools.codegen.ConfigHelper;
import org.ebayopensource.turmeric.tools.codegen.exception.BadInputValueException;
import org.ebayopensource.turmeric.tools.codegen.exception.CodeGenFailedException;
import org.ebayopensource.turmeric.tools.codegen.external.WSDLUtil;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;

import org.ebayopensource.turmeric.common.config.ClientConfig;
import org.ebayopensource.turmeric.common.config.ClientConfigList;
import org.ebayopensource.turmeric.common.config.ClientGroupConfig;
import org.ebayopensource.turmeric.common.config.ServiceConfig;

public class ConfigTool {
	private static SOALogger s_logger = null;

	public static SOALogger getLogger() {
		if (s_logger == null) {
			s_logger = SOALogger.getLogger();
		}
		return s_logger;
	}
	
	public static void modifyServiceConfigNamespace(final String newNamespace, 
			final URL fileLocation) throws Exception {
		final ClassLoader loader = Thread.currentThread()
		.getContextClassLoader();
		InputStream input = null;
		OutputStream out = null;
		try {
			Thread.currentThread().setContextClassLoader(
					ConfigTool.class.getClassLoader());
			if (fileLocation != null) {
				input = fileLocation.openStream();
				final ServiceConfig svcConfig = ConfigHelper.parseServiceConfig(input);
				IOUtils.closeQuietly(input);
				input = null;
				String fullServiceName = svcConfig.getServiceName();
				
				if (StringUtils.isNotBlank(fullServiceName)) {
					final String[] names = parseFullyQualifiedServiceName(fullServiceName);
					if (names != null) {
						fullServiceName = "{" + newNamespace + "}" + names[1];
					}
					svcConfig.setServiceName(fullServiceName);
					final String configXml = ConfigHelper.serviceConfigToXml(svcConfig);
					out = new FileOutputStream(fileLocation.getFile());
					IOUtils.write(configXml, out);
				}
			}
		} finally {
			IOUtils.closeQuietly(input);
			IOUtils.closeQuietly(out);
			Thread.currentThread().setContextClassLoader(loader);
		}
	}
	
	public static void saveServerConfig(final ISOAServiceConfig serviceConfig, 
			final URL fileLocation) throws Exception {
		final ClassLoader loader = Thread.currentThread()
		.getContextClassLoader();
		InputStream input = null;
		OutputStream out = null;
		try {
			Thread.currentThread().setContextClassLoader(
					ConfigTool.class.getClassLoader());
			if (fileLocation != null) {
				input = fileLocation.openStream();
				final ServiceConfig svcConfig = ConfigHelper.parseServiceConfig(input);
				IOUtils.closeQuietly(input);
				input = null;
				//we are no longer modifying the current version, and the version would be maintained 
				//in the service_metadata.properties
				//svcConfig.setCurrentVersion(serviceConfig.getCurrentVersion());
				svcConfig.setServiceImplClassName(serviceConfig.getServiceImplClassName());
				final String configXml = ConfigHelper.serviceConfigToXml(svcConfig);
				out = new FileOutputStream(fileLocation.getFile());
				IOUtils.write(configXml, out);
			}
			
		} finally {
			IOUtils.closeQuietly(input);
			IOUtils.closeQuietly(out);
			Thread.currentThread().setContextClassLoader(loader);
		}
	}
	
	public static void modifyClientConfigNamespace(final String newNamespace, 
			final URL fileLocation) throws Exception {
		final ClassLoader loader = Thread.currentThread()
		.getContextClassLoader();
		InputStream input = null;
		OutputStream out = null;
		try {
			Thread.currentThread().setContextClassLoader(
					ConfigTool.class.getClassLoader());
			if (fileLocation != null) {
				input = fileLocation.openStream();
				final ClientConfigList clientConfigList = ConfigHelper.parseClientConfig(input);
				IOUtils.closeQuietly(input);
				input = null;
				if (clientConfigList.getClientConfig().size() > 0) {
					final ClientConfig clientConfig = clientConfigList.getClientConfig().get(0);
					String fullServiceName = clientConfig.getServiceName();
						
					if (StringUtils.isNotBlank(fullServiceName)) {
						final String[] names = parseFullyQualifiedServiceName(fullServiceName);
						if (names != null) {
							fullServiceName = "{" + newNamespace + "}" + names[1];
						}
						clientConfig.setServiceName(fullServiceName);
						final String configXml = ConfigHelper.clientConfigToXml(clientConfigList);
						out = new FileOutputStream(fileLocation.getFile());
						IOUtils.write(configXml, out);
					}
				}
			}
		} finally {
			IOUtils.closeQuietly(input);
			IOUtils.closeQuietly(out);
			Thread.currentThread().setContextClassLoader(loader);
		}
	}
	
	public static ISOAClientConfig parseClientConfig(final InputStream input, final ISOAClientConfig clientConfig)
			throws Exception {
		final ClassLoader loader = Thread.currentThread()
				.getContextClassLoader();
		try {
			Thread.currentThread().setContextClassLoader(
					ConfigTool.class.getClassLoader());
			List<ISOAClientConfig> result = new ArrayList<ISOAClientConfig>();
			for (final ClientConfig config : ConfigHelper.parseClientConfig(input).getClientConfig()) {
				clientConfig.setGroup(config.getGroup());
				clientConfig.setServiceInterfaceClassName(config.getServiceInterfaceClassName());
				clientConfig.setServiceLocation(config.getServiceLocation());
				clientConfig.setFullyQualifiedServiceName(config.getServiceName());
				if (StringUtils.isNotBlank(config.getServiceName())) {
					final String[] names = parseFullyQualifiedServiceName(config.getServiceName());
					if (names != null) {
						clientConfig.setTargetNamespace(names[0]);
						clientConfig.setServiceName(names[1]);
					}
				}
				
				clientConfig.setWsdlLocation(config.getWsdlLocation());
				
				if (config.getClientInstanceConfig() != null) {
					ClientGroupConfig instanceConfig = config.getClientInstanceConfig();
					if (instanceConfig.getInvocationOptions() != null) {
						clientConfig.setServiceBinding(instanceConfig.getInvocationOptions().getPreferredTransport().getName());
						clientConfig.setInvocationUseCase(instanceConfig.getInvocationOptions().getInvocationUseCase());
						clientConfig.setRequestDataBinding(instanceConfig.getInvocationOptions().getRequestDataBinding());
						clientConfig.setResponseDataBinding(instanceConfig.getInvocationOptions().getResponseDataBinding());
					}
					if (instanceConfig.getProtocolProcessor() != null && instanceConfig.getProtocolProcessor().size() > 0) {
						clientConfig.setMessageProtocol(instanceConfig.getProtocolProcessor().get(0).getName());
					}
				}
				result.add(clientConfig);
			}
			return result.get(0);
		} finally {
			IOUtils.closeQuietly(input);
			Thread.currentThread().setContextClassLoader(loader);
		}
	}

	public static ISOAServiceConfig parseServiceConfig(final InputStream input, final ISOAServiceConfig serviceConfig)
			throws Exception {
		final ClassLoader loader = Thread.currentThread()
				.getContextClassLoader();
		try {
			Thread.currentThread().setContextClassLoader(
					ConfigTool.class.getClassLoader());
			ServiceConfig config = ConfigHelper.parseServiceConfig(input);
			serviceConfig.setFullyQualifiedServiceName(config.getServiceName());
			if (StringUtils.isNotBlank(config.getServiceName())) {
				String[] names = parseFullyQualifiedServiceName(config.getServiceName());
				if (names != null) {
					serviceConfig.setTargetNamespace(names[0]);
					serviceConfig.setServiceName(names[1]);
				}
			}
			serviceConfig.setServiceInterfaceClassName(StringUtils.trim(config.getServiceInterfaceClassName()));
			serviceConfig.setServiceImplClassName(StringUtils.trim(config.getServiceImplClassName()));
			return serviceConfig;
		} finally {
			Thread.currentThread().setContextClassLoader(loader);
			IOUtils.closeQuietly(input);
		}
	}

	public static String clientConfigToXml(final ClientConfigList clientCfgList)
			throws Exception {
		final ClassLoader loader = Thread.currentThread()
				.getContextClassLoader();
		try {
			Thread.currentThread().setContextClassLoader(
					ConfigTool.class.getClassLoader());
			return ConfigHelper.clientConfigToXml(clientCfgList);
		} finally {
			Thread.currentThread().setContextClassLoader(loader);
		}
	}

	public static String serviceConfigToXml(final ServiceConfig config)
			throws Exception {
		final ClassLoader loader = Thread.currentThread()
				.getContextClassLoader();
		try {
			Thread.currentThread().setContextClassLoader(
					ConfigTool.class.getClassLoader());
			return ConfigHelper.serviceConfigToXml(config);
		} finally {
			Thread.currentThread().setContextClassLoader(loader);
		}
	}

	public static IPath getServiceMetaDataPath(final String serviceName) throws BadInputValueException {
			return new Path(CodeGenInfoFinder.getPathforNonModifiableArtifact(
					serviceName, "SERVICE_METADATA"));
	}

	public static IPath getWSDLPath(final String serviceName) throws BadInputValueException {
		return new Path(CodeGenInfoFinder.getPathforNonModifiableArtifact(
				serviceName, "WSDL"));
	}

	public static List<String> getDefaultServiceLayersFromFile() {
		try {
			return CodeGenInfoFinder.getServiceLayersFromDefaultFile();
		} catch (CodeGenFailedException e) {
			throw new RuntimeException(e);
		}
	}
	
	public static String getDefaultPackageNameFromNamespace(final String namespace) {
		return WSDLUtil.getPackageFromNamespace(namespace);
	}
	
	public static interface ISOAClientConfig {
		
		public void setTargetNamespace(String targetNamespace);
		public void setServiceName(String serviceName);
		public void setFullyQualifiedServiceName(String fullyQualifiedServiceName);
		public void setGroup(String group);
		public void setServiceInterfaceClassName(String serviceInterfaceClassName);
		public void setServiceLocation(String serviceLocation);
		public void setWsdlLocation(String wsdlLocation);
		public void setServiceBinding(String serviceBinding);
		public void setInvocationUseCase(String invocationUseCase);
		public void setRequestDataBinding(String requestDataBinding);
		public void setResponseDataBinding(String responseDataBinding);
		public void setConsumerId(String consumerId);
		public void setMessageProtocol(String messageProtocol);
		
	}
	
	public static interface ISOAServiceConfig {
		//service config file will no longer have version
		/*public String getCurrentVersion();
		public void setCurrentVersion(String currentVersion);*/

		public void setSupportedVersion(String supportedVersion);

		public void setGroup(String group);

		public void setTargetNamespace(String targetNamespace);

		public void setServiceName(String serviceName);

		public void setFullyQualifiedServiceName(String fullyQualifiedServiceName);
		
		public void setServiceInterfaceClassName(String serviceInterfaceClassName);

		public String getServiceImplClassName();
		public void setServiceImplClassName(String serviceImplClassName);
		
		public String getMessageProtocol();
		public void setMessageProtocol(String messageProtocol);
	}

	public static void main(String[] args) {
		System.out.println(getDefaultServiceLayersFromFile());
	}
	
	/**
	 * @param serviceName
	 * @return The first value is the namespace and the second is the service name
	 */
	public static String[] parseFullyQualifiedServiceName(final String serviceName) {
		if (StringUtils.isBlank(serviceName))
			throw new IllegalArgumentException("Service name must not be empty->" + serviceName);
		String[] result = new String[2];
		result[0] = StringUtils.substringBetween(serviceName, "{", "}");
		result[1] = StringUtils.substringAfter(serviceName, "}");
		return result;
	}
}
