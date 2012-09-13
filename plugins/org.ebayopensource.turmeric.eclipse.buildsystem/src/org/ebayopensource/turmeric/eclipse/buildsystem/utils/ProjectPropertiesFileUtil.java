/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.buildsystem.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Properties;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.ebayopensource.turmeric.eclipse.core.logging.SOALogger;
import org.ebayopensource.turmeric.eclipse.core.resources.constants.SOAProjectConstants;
import org.ebayopensource.turmeric.eclipse.core.resources.constants.SOAProjectConstants.ServiceImplType;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.GlobalRepositorySystem;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.ISOAAssetRegistry;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.ISOAConfigurationRegistry;
import org.ebayopensource.turmeric.eclipse.resources.model.SOAConsumerMetadata;
import org.ebayopensource.turmeric.eclipse.resources.model.SOAConsumerProject;
import org.ebayopensource.turmeric.eclipse.resources.model.SOAImplMetadata;
import org.ebayopensource.turmeric.eclipse.resources.model.SOAImplProject;
import org.ebayopensource.turmeric.eclipse.resources.model.SOAIntfMetadata;
import org.ebayopensource.turmeric.eclipse.resources.model.SOAIntfProject;
import org.ebayopensource.turmeric.eclipse.resources.util.SOAConsumerUtil;
import org.ebayopensource.turmeric.eclipse.resources.util.SOAIntfUtil;
import org.ebayopensource.turmeric.eclipse.utils.plugin.WorkspaceUtil;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.osgi.framework.Version;


/**
 * The SOA plugin specific project properties files are created here. Includes
 * interface project properties file, consumer properties file and
 * implementation properties file. Some of these files might be empty for now,
 * But we still maintain this for future purposes. These properties file has
 * information that plugin needs to call codegen or identify the project
 * details. Most of the information here are not used by codegen directly.
 * 
 * @author smathew
 * 
 * 
 */
public class ProjectPropertiesFileUtil {

	/**
	 * Creates the interface project properties file. The name of the file is
	 * "service_intf_project.properties". This file has information like source
	 * type of the project, package to name space mapping etc.
	 *
	 * @param soaIntfProject the soa intf project
	 * @param monitor the monitor
	 * @return the i file
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws CoreException the core exception
	 */
	public static void createPrefsFile(IProject project, IProgressMonitor monitor){
		
		IFolder parentfolder = project.getFolder(".settings");
		try {if(!parentfolder.exists())
			parentfolder.create(true, true, null);
		} catch (CoreException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		IFile file = parentfolder.getFile(SOAProjectConstants.PREFS_FILE);
		
		InputStream source =null;
		try{
		  String contents = SOAProjectConstants.RIDE_PREFS_KEY_BUNDLE+
				  "="+SOAProjectConstants.RIDE_PREFS_VALUE_BUNDLE;
		  if(!file.exists()){
					  source= new ByteArrayInputStream(contents.getBytes());
					  file.create(source, false, null);						
		}
		 
		  }catch (Exception e){//Catch exception if any
			  SOALogger.getLogger().error(e.getMessage());
		  }	finally{
			  IOUtils.closeQuietly(source);
		  }
	}
	public static IFile createPropsFile(SOAIntfProject soaIntfProject, 
			IProgressMonitor monitor)
			throws IOException, CoreException {
		IFile file = soaIntfProject.getEclipseMetadata().getProject().getFile(
				SOAProjectConstants.PROPS_FILE_SERVICE_INTERFACE);
		OutputStream output = null;
		try {
			output = new ByteArrayOutputStream();
			final Properties properties = new Properties();
			final SOAIntfMetadata metadata = soaIntfProject.getMetadata();
			addServiceMetadataProperties(properties, metadata);
			if (metadata.getServiceNonXSDProtocols() == null) {
				boolean protoBufCreated = ProjectProtoBufFileUtil
						.createServiceProtoBufFile(soaIntfProject,
								metadata.getServiceLocation());
				if (protoBufCreated == true) {
					properties.setProperty(
							SOAProjectConstants.PROP_KEY_NON_XSD_FORMATS,
							SOAProjectConstants.SVC_PROTOCOL_BUF);
				}
			}
			if (SOAProjectConstants.InterfaceSourceType.WSDL.toString().equals(metadata
					.getSourceType().toString())
					|| SOAProjectConstants.InterfaceWsdlSourceType.EXISTIING
							.equals(metadata.getWsdlSourceType())) {
				properties.setProperty(
						SOAProjectConstants.PROPS_INTF_SOURCE_TYPE,
						SOAProjectConstants.PROPS_INTF_SOURCE_TYPE_WSDL);
				if (metadata.getNamespaceToPackageMappings().isEmpty() == false) {
					final Collection<String> data = new ArrayList<String>();
					for (final String namespace : metadata
							.getNamespaceToPackageMappings().keySet()) {
						final String pakcage = metadata
								.getNamespaceToPackageMappings().get(namespace);
						data.add(namespace + SOAProjectConstants.DELIMITER_PIPE
								+ pakcage);
					}
					final String ns2pkg = StringUtils.join(data,
							SOAProjectConstants.DELIMITER_COMMA);
					properties.setProperty(
							SOAProjectConstants.PROPS_KEY_NAMESPACE_TO_PACKAGE,
							ns2pkg);
				}
			} else {
				properties.setProperty(
						SOAProjectConstants.PROPS_INTF_SOURCE_TYPE,
						SOAProjectConstants.PROPS_INTF_SOURCE_TYPE_JAVA);
			}
			if (!metadata.getTypeFolding()
					&& StringUtils.isNotBlank(metadata.getTypeNamespace())) {
				properties.setProperty(
						SOAProjectConstants.PROPS_KEY_TYPE_NAMESPACE, metadata
								.getTypeNamespace());
			}
			if (StringUtils.isNotBlank(metadata.getServiceDomainName())) {
				properties.setProperty(SOAProjectConstants.PROPS_SERVICE_DOMAIN_NAME, 
						metadata.getServiceDomainName());
			}
			
			if (StringUtils.isNotBlank(metadata.getServiceNamespacePart())) {
				properties.setProperty(SOAProjectConstants.PROPS_SERVICE_NAMESPACE_PART, 
						metadata.getServiceNamespacePart());
			}
			
			properties.setProperty(SOAProjectConstants.PROPS_KEY_TYPE_FOLDING,
					Boolean.toString(metadata.getTypeFolding()));
			properties.setProperty(SOAProjectConstants.PROPS_SUPPORT_ZERO_CONFIG, 
					Boolean.TRUE.toString());
			
			ISOAConfigurationRegistry configReg = 
				GlobalRepositorySystem.instanceOf().getActiveRepositorySystem()
				.getConfigurationRegistry();
			if (configReg != null && StringUtils.isNotBlank(configReg.getEnvironmentMapperImpl())) {
				properties.setProperty(SOAProjectConstants.PROPS_ENV_MAPPER, 
						configReg.getEnvironmentMapperImpl());
			}
			
			properties.setProperty(SOAProjectConstants.PROPS_KEY_SIPP_VERSION, 
					SOAProjectConstants.PROPS_DEFAULT_SIPP_VERSION);
			properties.setProperty(SOAProjectConstants.PROPS_KEY_RAPTOR_CONSUMER_GENERATION,
					SOAProjectConstants.PROPS_DEFAULT_RAPTOR_CONSUMER_GENERATION);	
			properties.setProperty(SOAProjectConstants.PROPS_OBJECT_FACT_PACK_INFO_DEL, 
					SOAProjectConstants.PROPS_DEFAULT_OBJECT_FACT_PACK_INFO_DEL);
			//short package name for shared consumer
			final String intfPkgName = StringUtils.substringBeforeLast(
					metadata.getServiceInterface(), SOAProjectConstants.DELIMITER_DOT);
			properties.setProperty(SOAProjectConstants.PROPS_KEY_SHORT_PATH_FOR_SHARED_CONSUMER, 
					intfPkgName + SOAProjectConstants.DELIMITER_DOT + SOAProjectConstants.GEN);

			properties.store(output, SOAProjectConstants.PROPS_COMMENTS);
			
			WorkspaceUtil.writeToFile(output.toString(), file, monitor);
		} finally {
			IOUtils.closeQuietly(output);
		}
		return file;
	}
	
	/**
	 * 
	 * adding service metadata properties
	 * @param props
	 * @param intfProject
	 */
	private static void addServiceMetadataProperties(final Properties props, 
			final SOAIntfMetadata metadata) {
		props.setProperty(SOAProjectConstants.PROP_KEY_SERVICE_INTERFACE_CLASS_NAME, 
				metadata.getServiceInterface());
		props.setProperty(SOAProjectConstants.PROP_KEY_SERVICE_LAYER, 
				metadata.getServiceLayer());
		if (metadata.getOriginalWSDLUrl() != null) {
			props.setProperty(SOAProjectConstants.PROP_KEY_ORIGINAL_WSDL_URI, 
					metadata.getOriginalWSDLUrl().toString());
		}
		props.setProperty(SOAProjectConstants.PROP_KEY_SERVICE_VERSION, 
				metadata.getServiceVersion());
		props.setProperty(SOAProjectConstants.PROP_KEY_ADMIN_NAME, 
				metadata.getServiceName());
		String protocols = metadata.getServiceNonXSDProtocols();
		if (protocols == null) {
			protocols = "";
		}
		props.setProperty(SOAProjectConstants.PROP_KEY_NON_XSD_FORMATS, protocols);
	}

	/**
	 * Creates the implementation project properties file. The name of the file
	 * is "service_impl_project.properties". This file has information about the
	 * base consumer source directory if there is one.
	 *
	 * @param soaImplProject the soa impl project
	 * @return the i file
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws CoreException the core exception
	 */
	public static IFile createPropsFile(SOAImplProject soaImplProject)
			throws IOException, CoreException {
		IFile file = soaImplProject.getEclipseMetadata().getProject().getFile(
				SOAProjectConstants.PROPS_FILE_SERVICE_IMPL);
		OutputStream output = null;

		try {
			output = new ByteArrayOutputStream();
			Properties properties = new Properties();
			SOAImplMetadata metadata = soaImplProject.getMetadata();
			properties.setProperty(SOAProjectConstants.PROPS_KEY_SIMP_VERSION, 
					SOAProjectConstants.PROPS_DEFAULT_SIMP_VERSION);


			boolean useServiceFactory = ServiceImplType.SERVICE_IMPL_FACTORY
					.equals(metadata.getServiceImplType());

			properties.setProperty(
					SOAProjectConstants.PROPS_KEY_USE_EXTERNAL_SERVICE_FACTORY,
					useServiceFactory ? Boolean.TRUE.toString() : Boolean.FALSE
							.toString());

			if (useServiceFactory == true) {
				String implClassName = soaImplProject.getMetadata().getServiceImplClassName();
				properties
						.setProperty(
								SOAProjectConstants.PROPS_KEY_SERVICE_FACTORY_CLASS_NAME,
								implClassName
										+SOAProjectConstants.PROPS_DEFAULT_VALUE_SERVICE_FACTORY_CLASS_NAME_POSTFIX);
			}

			properties.store(output, SOAProjectConstants.PROPS_COMMENTS);
			WorkspaceUtil.writeToFile(output.toString(), file, null);
		} finally {
			IOUtils.closeQuietly(output);
		}
		return file;
	}

	/**
	 * Creates the consumer project properties file. The name of the file is
	 * "service_consumer_project.properties". This file has information about
	 * the base consumer source directory if there is one.
	 *
	 * @param soaConsumerProject the soa consumer project
	 * @param monitor the monitor
	 * @return the i file
	 * @throws Exception the exception
	 */
	public static IFile createPropsFile(SOAConsumerProject soaConsumerProject, 
			IProgressMonitor monitor)
			throws Exception {
		IFile file = soaConsumerProject.getEclipseMetadata().getProject()
				.getFile(SOAProjectConstants.PROPS_FILE_SERVICE_CONSUMER);
		OutputStream output = null;
		try {
			final SOAConsumerMetadata metadata = soaConsumerProject.getMetadata();
			output = new ByteArrayOutputStream();
			Properties properties = new Properties();
			properties.setProperty(SOAProjectConstants.PROPS_KEY_CLIENT_NAME, metadata.getClientName());
			String consumerID = metadata.getConsumerId() != null ? metadata.getConsumerId() : "";
			properties.setProperty(SOAProjectConstants.PROPS_KEY_CONSUMER_ID, consumerID);
			ISOAConfigurationRegistry configReg = 
				GlobalRepositorySystem.instanceOf().getActiveRepositorySystem()
				.getConfigurationRegistry();
			if (configReg != null && StringUtils.isNotBlank(configReg.getEnvironmentMapperImpl())) {
				properties.setProperty(SOAProjectConstants.PROPS_ENV_MAPPER, 
						configReg.getEnvironmentMapperImpl());
			}
			properties.setProperty(SOAProjectConstants.PROPS_KEY_SCPP_VERSION, 
					SOAProjectConstants.PROPS_DEFAULT_SCPP_VERSION);
			
			final Collection<String> services = new ArrayList<String>();
			final ISOAAssetRegistry assetRegistry = GlobalRepositorySystem.instanceOf()
			.getActiveRepositorySystem().getAssetRegistry();
			
			for (String serviceName : metadata.getServiceNames()) {
				final String assetLocation = assetRegistry.getAssetLocation(serviceName);
				if (StringUtils.isNotBlank(assetLocation)) {
					final Version version = SOAIntfUtil.getServiceMetadataVersion(serviceName, assetLocation);
					if (version.compareTo(SOAProjectConstants.DEFAULT_PROPERTY_VERSION) >= 0) {
						//the project is post 2.4
						services.add(serviceName);
					}
				} else {
					SOALogger.getLogger().warning(
							"Could not find the service in the underlying system, so generate the base cosumer:", 
							serviceName);
					//services.add(serviceName);
				}
			}
			properties.setProperty(SOAProjectConstants.PROPS_NOT_GENERATE_BASE_CONSUMER, 
					StringUtils.join(services, SOAProjectConstants.DELIMITER_COMMA));
			properties.setProperty(SOAProjectConstants.PROPS_SUPPORT_ZERO_CONFIG, Boolean.toString(metadata.isZeroConfig()));
			
			properties.store(output, SOAProjectConstants.PROPS_COMMENTS);
			WorkspaceUtil.writeToFile(output.toString(), file, monitor);
		} finally {
			IOUtils.closeQuietly(output);
		}
		return file;
	}
	
	public static IFile createPropsFileForImplProjects(IProject implProject, 
			String clientName, String consumerId, 
			IProgressMonitor monitor)
	throws IOException, CoreException {
		IFile file = SOAConsumerUtil.getConsumerPropertiesFile(implProject);

		Properties properties = new Properties();
		if (file.isAccessible() == true) {
			properties = SOAConsumerUtil.loadConsumerProperties(implProject);
		}
		properties.setProperty(SOAProjectConstants.PROPS_KEY_CLIENT_NAME, clientName);
		String consumerID = consumerId != null ? consumerId : "";
		properties.setProperty(SOAProjectConstants.PROPS_KEY_CONSUMER_ID, consumerID);

		properties.setProperty(SOAProjectConstants.PROPS_KEY_SCPP_VERSION, 
				SOAProjectConstants.PROPS_DEFAULT_SCPP_VERSION);
		if (properties.containsKey(SOAProjectConstants.PROPS_ENV_MAPPER) == false) {
			ISOAConfigurationRegistry configReg = 
				GlobalRepositorySystem.instanceOf().getActiveRepositorySystem()
				.getConfigurationRegistry();
			if (configReg != null && StringUtils.isNotBlank(configReg.getEnvironmentMapperImpl())) {
				properties.setProperty(SOAProjectConstants.PROPS_ENV_MAPPER, 
						configReg.getEnvironmentMapperImpl());
			}
		}
		
		// set the default value of PROPS_SUPPORT_ZERO_CONFIG to false. if this
		// is modify client name or client id, then no change.
		if (properties
				.containsKey(SOAProjectConstants.PROPS_SUPPORT_ZERO_CONFIG) == false
				&& StringUtils
						.isEmpty(properties
								.getProperty(SOAProjectConstants.PROPS_SUPPORT_ZERO_CONFIG)) == true) {
			properties.setProperty(
					SOAProjectConstants.PROPS_SUPPORT_ZERO_CONFIG,
					Boolean.FALSE.toString());
		}

		SOAConsumerUtil.savePropsFileForConsumer(implProject, properties, monitor);

		return file;
	}
	
	

}
