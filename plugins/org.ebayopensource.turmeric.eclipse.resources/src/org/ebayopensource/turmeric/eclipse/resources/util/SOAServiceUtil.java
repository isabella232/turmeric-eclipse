/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.resources.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.ebayopensource.turmeric.eclipse.core.logging.SOALogger;
import org.ebayopensource.turmeric.eclipse.core.resources.constants.SOAProjectConstants;
import org.ebayopensource.turmeric.eclipse.core.resources.constants.SOAProjectConstants.ServiceLayer;
import org.ebayopensource.turmeric.eclipse.core.resources.constants.SOAProjectConstants.SupportedProjectType;
import org.ebayopensource.turmeric.eclipse.resources.model.ISOAProject;
import org.ebayopensource.turmeric.eclipse.resources.model.ISOAProjectResolver;
import org.ebayopensource.turmeric.eclipse.resources.model.SOAConsumerMetadata;
import org.ebayopensource.turmeric.eclipse.resources.model.SOAImplMetadata;
import org.ebayopensource.turmeric.eclipse.resources.model.SOAIntfMetadata;
import org.ebayopensource.turmeric.eclipse.resources.model.SOAIntfProject;
import org.ebayopensource.turmeric.eclipse.resources.model.SOAProjectEclipseMetadata;
import org.ebayopensource.turmeric.eclipse.resources.model.SOAProjectResolverFactory;
import org.ebayopensource.turmeric.eclipse.soatools.configtool.ConfigTool;
import org.ebayopensource.turmeric.eclipse.utils.lang.StringUtil;
import org.ebayopensource.turmeric.eclipse.utils.plugin.WorkspaceUtil;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;


/**
 * @author smathew
 * 
 * Utility class for processing project properties The Project does not carry
 * information like the WSDL location. It will be calculated using the existing
 * project properties. The core calculation of the majority of functions inside
 * this class is based on the properties file. It handles all SOA interface,
 * implementation and consumer projects. Any new utility for these projects
 * should be added here.
 * 
 */
public class SOAServiceUtil {

	private static final SOALogger logger = SOALogger.getLogger();
	
	public static String computeAdminName(String serviceName, String domainClassifier, 
			String serviceVersion) {
		if (StringUtils.isEmpty(serviceName))
			return SOAProjectConstants.EMPTY_STRING;
		StringBuffer result = new StringBuffer();
		if (StringUtils.isNotBlank(domainClassifier)) {
			//Marketplace organization
			result.append(StringUtils.capitalize(domainClassifier));
		}
		
		result.append(serviceName);
		result.append(SOAProjectConstants.MAJOR_VERSION_PREFIX);
		result.append(SOAServiceUtil.getServiceMajorVersion(serviceVersion));
		
		return result.toString();
	}
	
	public static String generateInterfaceClassName(String adminName, 
			String servicePackageName) {
		return StringUtils.isBlank(servicePackageName) ? adminName
				: servicePackageName + SOAProjectConstants.CLASS_NAME_SEPARATOR + adminName;
	}
	
	public static String generatePackageNamePrefix(String targetNamespace) {
		if (StringUtils.isNotBlank(targetNamespace)) {
			return ConfigTool.getDefaultPackageNameFromNamespace(targetNamespace);
		}
		return "";
	}
	
	public static String generateServicePackageName(String serviceName, String packageNamePrefix) {
		if (StringUtils.isBlank(serviceName))
			return StringUtil.toString(packageNamePrefix);
		return StringUtil.toString(packageNamePrefix,
				SOAProjectConstants.CLASS_NAME_SEPARATOR, serviceName
						.toLowerCase(Locale.US));
	}
	
	public static String generateServiceImplClassName(String serviceName,String adminName, String targetNamespace) {
		return generateServiceImplPackageName(serviceName, adminName,
				generatePackageNamePrefix(targetNamespace));
	}

	public static String generateServiceImplPackageName(String serviceName, String adminName, String packageNamePrefix) {
		if (StringUtils.isBlank(packageNamePrefix))
			return "";
		if (StringUtils.isBlank(serviceName))
			return StringUtil.toString(packageNamePrefix,
					SOAProjectConstants.CLASS_NAME_SEPARATOR,
					SOAProjectConstants.IMPL_PROJECT_SUFFIX.toLowerCase(),
					".Service", SOAProjectConstants.IMPL_PROJECT_SUFFIX);
		return StringUtil.toString(packageNamePrefix,
				SOAProjectConstants.CLASS_NAME_SEPARATOR, serviceName
				.toLowerCase(),
				SOAProjectConstants.CLASS_NAME_SEPARATOR,
				SOAProjectConstants.IMPL_PROJECT_SUFFIX.toLowerCase(),
				SOAProjectConstants.CLASS_NAME_SEPARATOR, adminName,
				SOAProjectConstants.IMPL_PROJECT_SUFFIX);
	}

	/**
	 * Gets the WSDL from the given interface project, It scans the project and
	 * look for the WSDL in the designated location if the project is created
	 * from WSDL. Remember Now SOA supports only services from WSDL. After the
	 * above mentioned checks it calls the method
	 * 
	 * @link {@link SOAServiceUtil}{@link #getWsdlFile(IProject, String)}
	 * 
	 * @param project
	 * @return
	 */
	public static IFile getWsdlFile(SOAIntfProject project) {
		if (SOALogger.DEBUG)
			logger.entering(project);
		IFile result = null;
		SOAIntfMetadata metadata = project.getMetadata();
		SOAProjectEclipseMetadata eclipseMetadata = project
				.getEclipseMetadata();
		if (metadata.getSourceType().equals(
				SOAProjectConstants.InterfaceSourceType.WSDL)) {
			if (SOALogger.DEBUG)
				logger.debug("The source type of this intf project is WSDL->",
						project.getProject());
			result = getWsdlFile(eclipseMetadata.getProject(), metadata
					.getServiceName());
		}
		if (SOALogger.DEBUG)
			logger.exiting(result);
		return result;
	}

	/**
	 * Wrapper over the linked method. Does not check if the service was created
	 * from WSDL or java. Client to check it. First it find out the project in
	 * the work space with the given name and will invoke the linked method.
	 * 
	 * @link {@link SOAServiceUtil}{@link #getWsdlFile(IProject, String)}
	 * 
	 * @param serviceName
	 * @return
	 */
	public static IFile getWsdlFile(String serviceName) {
		if (SOALogger.DEBUG)
			logger.entering(serviceName);
		IProject project = WorkspaceUtil.getProject(serviceName);
		final IFile result = getWsdlFile(project, serviceName);
		if (SOALogger.DEBUG)
			logger.exiting(result);
		return result;
	}

	/**
	 * Gets the WSDL from the given eclipse project, It scans the project and
	 * look for the WSDL in the designated location. It just looks for the WSDL
	 * in the given eclipse project and does not have any SOA specific logic
	 * except for the location. If the project is null and is not accessible
	 * this will return null, so that way it is null safe but clients are
	 * supposed to handle the null graciously.
	 * 
	 * @param project
	 * @param serviceName
	 * @return
	 */
	public static IFile getWsdlFile(final IProject project,
			final String serviceName) {
		if (SOALogger.DEBUG)
			logger.entering(project, serviceName);
		IFile result = null;
		try {
			if (project == null || project.isAccessible() == false
					|| serviceName == null)
				return result;

			return result = project.getFile(SOAIntfProject.META_SRC_WSDL
					+ WorkspaceUtil.PATH_SEPERATOR + serviceName
					+ WorkspaceUtil.PATH_SEPERATOR + serviceName + ".wsdl");
		} finally {
			if (SOALogger.DEBUG)
				logger.exiting(result);
		}
	}

	public static SOAProjectEclipseMetadata getSOAEclipseMetadata(
			IProject project) {
		return SOAProjectEclipseMetadata.create(project.getName(), project
				.getLocation().removeLastSegments(1));
	}

	/**
	 * Loads the SOA project from the given eclipse project, first it checks the
	 * type of project that is whether it is an interface, implementation or
	 * consumer and then load the rest of the project values from the
	 * corresponding configuration file(properties file). For implementation
	 * projects, the service configuration might not be loaded properly, because
	 * the interface project name is missing. In such a case, please load the
	 * service configuration yourself.
	 * 
	 * @param project
	 * @return
	 * @throws Exception
	 */
	public static ISOAProject loadSOAProject(final IProject project, 
			String natureId)
			throws Exception {
		if (SOALogger.DEBUG)
			logger.entering(project);
		ISOAProject result = null;
		if (project == null || project.exists() == false
				|| project.isAccessible() == false)
			return result;
		project.refreshLocal(IResource.DEPTH_INFINITE, null);
		final SOAProjectEclipseMetadata eclipseMetadata = getSOAEclipseMetadata(project);
		if (SOALogger.DEBUG)
			logger.debug("Loading turmeric project->", project);
		final ISOAProjectResolver<?> resolver = SOAProjectResolverFactory.getSOAProjectResolver(
				natureId);
		if (resolver != null)
			result = resolver.loadProject(eclipseMetadata);
		else
			logger.warning("can not find project resolver for nature ID: ", natureId);
		
		if (SOALogger.DEBUG)
			logger.exiting(result);
		return result;
	}

	/**
	 * Loads SOAIntfMedata from service_intf_project.properties. Pure Wrapper
	 * over the linked method.
	 * 
	 * @see {@link SOAServiceUtil}{@link #getSOAIntfMetadata(SOAProjectEclipseMetadata, SOAIntfMetadata)}
	 * 
	 * @param metadata
	 * @return
	 * @throws IOException
	 * @throws CoreException
	 */
	public static SOAIntfMetadata getSOAIntfMetadata(
			SOAProjectEclipseMetadata metadata) throws IOException,
			CoreException {
		return getSOAIntfMetadata(metadata, null);
	}

	/**
	 * Loads the SOA interface project properties from the given meta data
	 * object. It can tolerate a null SOAIntfMetadata but meta data is not
	 * supposed to be null. Not null safe, Clients should handle the exceptions.
	 * It does not fail with an invalid properties file meaning an absence of a
	 * property will not cause this function to fail, It just makes the
	 * corresponding property empty.
	 * 
	 * @param metadata
	 * @param intfMetadata
	 * @return
	 * @throws IOException
	 * @throws CoreException
	 */
	public static SOAIntfMetadata getSOAIntfMetadata(
			SOAProjectEclipseMetadata metadata, SOAIntfMetadata intfMetadata)
	throws IOException, CoreException {
		if (SOALogger.DEBUG)
			logger.entering(metadata, intfMetadata);

		if (intfMetadata == null) {
			intfMetadata = SOAIntfMetadata
			.create(SOAProjectConstants.InterfaceSourceType.JAVA);
		}

		final IFile file = SOAIntfUtil.getIntfProjectPropFile(metadata
				.getProject());
		if (file.isAccessible() == false) {
			logger
			.warning("The interface project's property file[", file
					.getLocation(),
			"] is not accessible, returning an empty instance of SOAIntfMetadata.");
			return SOAIntfMetadata
			.create(SOAProjectConstants.InterfaceSourceType.JAVA);
		}
		final Properties properties = new Properties();
		InputStream io = null;
		try {
			io = file.getContents();
			properties.load(io);
			if (SOAProjectConstants.PROPS_INTF_SOURCE_TYPE_WSDL
					.equals(StringUtils.trim(properties
							.getProperty(SOAProjectConstants.PROPS_INTF_SOURCE_TYPE)))) {
				intfMetadata
				.setSourceType(SOAProjectConstants.InterfaceSourceType.WSDL);
			}
			final String typeNamespace = StringUtils.trim(properties
					.getProperty(SOAProjectConstants.PROPS_KEY_TYPE_NAMESPACE));
			if (StringUtils.isNotBlank(typeNamespace)) {
				intfMetadata.setTypeNamespace(typeNamespace);
			}
			
			if(properties.containsKey(SOAProjectConstants.PROP_KEY_SERVICE_VERSION)) {
				intfMetadata.setServiceVersion(StringUtils.trim(properties.getProperty(
						SOAProjectConstants.PROP_KEY_SERVICE_VERSION)));
			}
			
			if(properties.containsKey(SOAProjectConstants.PROP_KEY_SERVICE_INTERFACE_CLASS_NAME)) {
				intfMetadata.setServiceInterface(StringUtils.trim(properties.getProperty(
						SOAProjectConstants.PROP_KEY_SERVICE_INTERFACE_CLASS_NAME)));
			}
			
			if(properties.containsKey(SOAProjectConstants.PROP_KEY_SERVICE_LAYER)) {
				intfMetadata.setServiceLayer(StringUtils.trim(properties.getProperty(
						SOAProjectConstants.PROP_KEY_SERVICE_LAYER)));
			}

			final String strTypeFolding = StringUtils.trim(properties
					.getProperty(SOAProjectConstants.PROPS_KEY_TYPE_FOLDING));

			intfMetadata.setTypeFolding(Boolean.valueOf(strTypeFolding));

			final String domainName = StringUtils.trim(properties
					.getProperty(SOAProjectConstants.PROPS_SERVICE_DOMAIN_NAME));
			if (StringUtils.isNotBlank(domainName)) {
				intfMetadata.setServiceDomainName(domainName);
			}

			final String nsPart = StringUtils.trim(properties
					.getProperty(SOAProjectConstants.PROPS_SERVICE_NAMESPACE_PART));

			if (StringUtils.isNotBlank(nsPart)) {
				intfMetadata.setServiceNamespacePart(nsPart);
			}
		} finally {
			IOUtils.closeQuietly(io);
		}
		if (SOALogger.DEBUG)
			logger.exiting(intfMetadata);
		return intfMetadata;
	}

	/**
	 * Loads meta data from service_impl_project.properties Pure Wrapper over
	 * the linked method.
	 * 
	 * @see {@link SOAServiceUtil}{@link #getSOAImplMetadata(IProject))}
	 * 
	 * 
	 * @param metadata
	 * @return
	 * @throws IOException
	 * @throws CoreException
	 */
	public static SOAImplMetadata getSOAImplMetadata(
			SOAProjectEclipseMetadata metadata) throws IOException,
			CoreException {
		if (SOALogger.DEBUG)
			logger.entering(metadata);
		final SOAImplMetadata implMetadata = getSOAImplMetadata(metadata
				.getProject());
		if (SOALogger.DEBUG)
			logger.exiting(implMetadata);
		return implMetadata;
	}

	/**
	 * Loads the SOA implementation project properties from the given IProject
	 * object. It can tolerate a null SOAImplMetadata but meta data is not
	 * supposed to be null. Not null safe, Clients should handle the exceptions.
	 * It does not fail with an invalid properties file meaning an absence of a
	 * property will not cause this function to fail, It just makes the
	 * corresponding property empty.
	 * 
	 * @param project
	 * @return
	 * @throws IOException
	 * @throws CoreException
	 */
	public static SOAImplMetadata getSOAImplMetadata(final IProject project)
			throws IOException, CoreException {
		if (SOALogger.DEBUG)
			logger.entering(project);
		final IFile file = project
				.getFile(SOAProjectConstants.PROPS_FILE_SERVICE_IMPL);
		if (file.isAccessible() == false) {
			logger
					.warning("The implementation project's property file[",
							file.getLocation(),
							"] is not accessible, returning an empty instance of SOAImplMetadata.");
			return SOAImplMetadata.create(project.getName(),
					SOAProjectConstants.DEFAULT_BASE_CONSUMER_SOURCE_DIRECTORY);
		}
		Properties properties = new Properties();
		InputStream io = null;
		SOAImplMetadata implMetadata = null;
		try {
			io = file.getContents();
			properties.load(io);
			/*
			 * final boolean includeTestJSP = Boolean.valueOf(properties
			 * .getProperty(SOAProjectConstants.PROPS_IMPL_INCLUDE_TEST_JSP,
			 * Boolean.FALSE.toString())); final boolean includeVIPage = Boolean
			 * .valueOf(properties.getProperty(SOAProjectConstants.PROPS_IMPL_INLCUDE_VALIDATE_INTERNAL_SERVLET,
			 * Boolean.FALSE.toString()));
			 */
			final String baseConsumerDir = StringUtils.trim(properties
					.getProperty(SOAProjectConstants.PROPS_IMPL_BASE_CONSUMER_SRC_DIR));
			implMetadata = SOAImplMetadata.create(project.getName(),
					baseConsumerDir);
			/*
			 * implMetadata = SOAImplMetadata.create(metadata .getProjectName(),
			 * includeTestJSP, includeVIPage, baseConsumerDir);
			 */
		} finally {
			IOUtils.closeQuietly(io);
		}
		if (SOALogger.DEBUG)
			logger.exiting(implMetadata);
		return implMetadata;
	}

	/**
	 * This function creates the SOA related meta data for the given project
	 * from service_consumer_project.properties. Pure Wrapper over the linked
	 * method.
	 * 
	 * @see {@link SOAServiceUtil}{@link #getSOAConsumerMetadata(IProject))}
	 * 
	 * @param metadata
	 * @return
	 * @throws IOException
	 * @throws CoreException
	 *             Suresh changed this
	 */
	public static SOAConsumerMetadata getSOAConsumerMetadata(
			SOAProjectEclipseMetadata metadata) throws IOException,
			CoreException {
		if (SOALogger.DEBUG)
			logger.entering(metadata);
		SOAConsumerMetadata consumerMetadata = getSOAConsumerMetadata(metadata
				.getProject());
		if (SOALogger.DEBUG)
			logger.exiting(consumerMetadata);
		return consumerMetadata;
	}

	/**
	 * Loads the SOA consumer project properties from the given IProject object.
	 * It can tolerate a null SOAConsumerMetadata but meta data is not supposed
	 * to be null. Not null safe, Clients should handle the exceptions. It does
	 * not fail with an invalid properties file meaning an absence of a property
	 * will not cause this function to fail, It just makes the corresponding
	 * property empty.
	 * 
	 * @param project
	 * @return
	 * @throws IOException
	 * @throws CoreException
	 */
	public static SOAConsumerMetadata getSOAConsumerMetadata(
			final IProject project) throws IOException, CoreException {
		if (SOALogger.DEBUG)
			logger.entering(project);
		final IFile file = project
				.getFile(SOAProjectConstants.PROPS_FILE_SERVICE_CONSUMER);
		Properties properties = new Properties();
		InputStream input = null;
		try {
			input = file.getContents();
			properties.load(input);
		} finally {
			IOUtils.closeQuietly(input);
		}

		final String baseConsumerDir = StringUtils.trim(properties
				.getProperty(SOAProjectConstants.PROPS_IMPL_BASE_CONSUMER_SRC_DIR));
		SOAConsumerMetadata consumerMetadata = SOAConsumerMetadata.create(
				project.getName(), baseConsumerDir);
		final String envMapper = StringUtils.trim(properties.getProperty(
				SOAProjectConstants.PROPS_ENV_MAPPER));
		if (StringUtils.isNotBlank(envMapper)) {
			consumerMetadata.setEnvMapper(envMapper);
		}
		final String clientName = properties.getProperty(
				SOAProjectConstants.PROPS_KEY_CLIENT_NAME, project.getName());
		final String consumerId = properties.getProperty(
				SOAProjectConstants.PROPS_KEY_CONSUMER_ID, null);
		consumerMetadata.setClientName(clientName);
		consumerMetadata.setConsumerId(consumerId);
		
		if (SOALogger.DEBUG)
			logger.exiting(consumerMetadata);
		return consumerMetadata;
	}

	

	/**
	 * Returns true if the given project has one of the natures passed. Not null
	 * safe. If the project is not accessible, the API will fail and clients are
	 * supposed to handle it.
	 * 
	 * @param project
	 * @param natureIDs
	 * @return
	 * @throws CoreException
	 */
	public static boolean hasNatures(final IProject project,
			final String... natureIDs) throws CoreException {
		for (String natureId : natureIDs) {
			if (project.hasNature(natureId)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Checks if the given string is a valid service layer string, Now two
	 * checks are performed, empty and non numeric.
	 * 
	 * @param layer
	 * @return
	 */
	public static boolean isValidServiceLayer(final String layer) {
		return StringUtils.isNotBlank(layer)
				&& StringUtils.isNumeric(layer) == false;
	}

	/**
	 * It first use the string based non SOA specific basic validation and it it
	 * passes then it will do the domain specific validation using the linked
	 * method for both the layers passed.
	 * 
	 * @see SOAServiceUtil#validateServiceLayer(ServiceLayer, ServiceLayer)
	 * 
	 * @param layer
	 * @param dependencyLayer
	 * @return
	 */
	public static boolean validateServiceLayer(final String layer,
			final String dependencyLayer) {
		if (isValidServiceLayer(layer) == false
				|| isValidServiceLayer(dependencyLayer) == false)
			return false;
		return validateServiceLayer(ServiceLayer.value(layer), ServiceLayer
				.value(dependencyLayer));
	}

	/**
	 * Does the SOA specific service layer validation specified in the SOA ERD.
	 * The API is null safe and returns false if any of the layer passed is
	 * null.
	 * 
	 * @param layer
	 * @param dependencyLayer
	 * @return
	 */
	public static boolean validateServiceLayer(final ServiceLayer layer,
			final ServiceLayer dependencyLayer) {
		if (SOALogger.DEBUG)
			logger.entering(layer, dependencyLayer);
		boolean result = false;
		try {
			if (layer == null || dependencyLayer == null)
				return result;
			// This is eBay Marketplaces service layering policy for SOA
			// Framework
			// 1.8+ as per ERD
			// The relationship is greater than or equal (ordinal value) unless
			// its
			// the Business layer.
			if (ServiceLayer.BUSINESS.ordinal() == layer.ordinal()
					&& ServiceLayer.BUSINESS.ordinal() == dependencyLayer
							.ordinal())
				return result;
			return result = layer.ordinal() >= dependencyLayer.ordinal();
		} finally {
			if (SOALogger.DEBUG)
				logger.exiting(result);
		}
	}

	/**
	 * Gets the consumer folder for a given eclipse project. This API works for
	 * both implementation projects and consumer projects. The meta data object
	 * will have the directory name and it makes an eclipse API call on the
	 * project.
	 * 
	 * @param project
	 * @return
	 * @throws IOException
	 * @throws CoreException
	 */
	public static IFolder getBaseConsumerFolder(final IProject project, 
			SupportedProjectType projectType)
			throws IOException, CoreException {
		if (SupportedProjectType.IMPL.equals(projectType)) {
			final SOAImplMetadata metadata = getSOAImplMetadata(project);
			if (metadata != null
					&& StringUtils.isNotBlank(metadata.getBaseConsumerSrcDir())) {
				return project.getFolder(metadata.getBaseConsumerSrcDir());
			}
		} else if (SupportedProjectType.CONSUMER.equals(projectType)) {
			final SOAConsumerMetadata metadata = getSOAConsumerMetadata(project);
			if (metadata != null
					&& StringUtils.isNotBlank(metadata.getBaseConsumerSrcDir())) {
				return project.getFolder(metadata.getBaseConsumerSrcDir());
			}
		}
		return project.getFolder(SOAProjectConstants.FOLDER_SRC);
	}
	
	public static List<String> getInvokeableServiceLayer(String layerName){
		List<String> layers = new ArrayList<String>();
		ServiceLayer layer = ServiceLayer.value(layerName);
		switch (layer) {
		case COMMON:
			layers.add(ServiceLayer.COMMON.name());
			break;
		case INTERMEDIATE:
			layers.add(ServiceLayer.COMMON.name());
			layers.add(ServiceLayer.INTERMEDIATE.name());
			break;
		case BUSINESS:
			layers.add(ServiceLayer.COMMON.name());
			layers.add(ServiceLayer.INTERMEDIATE.name());
			break;
		}
		return layers;
	}
	
	/**
	 * Get the major version of the given service version
	 * @param version
	 * @return
	 */
	public static String getServiceMajorVersion(String version) {
		if (StringUtils.isBlank(version))
			return "";
		return StringUtils.substringBefore(version, ".");
	}

	/*
	 * public static String getTargetNamespace(final ISOAProject soaProject)
	 * throws WSDLException { String nameSpace =
	 * SOAProjectConstants.DEFAULT_SERVICE_NAMESPACE; if (soaProject instanceof
	 * SOAIntfProject) { final SOAIntfMetadata intfMetadata =
	 * ((SOAIntfProject)soaProject).getMetadata(); if
	 * (InterfaceSourceType.WSDL.equals(intfMetadata.getWsdlSourceType()) &&
	 * intfMetadata.getWsdl() != null) { return
	 * intfMetadata.getWsdl().getTargetNamespace(); } } else if (soaProject
	 * instanceof SOAImplProject) { final SOAImplMetadata implMetadata =
	 * ((SOAImplProject)soaProject).getMetadata(); if
	 * (StringUtils.isNotBlank(implMetadata.getTargetNamespace())) { //the
	 * targetNamespace already been read from the service config file return
	 * implMetadata.getTargetNamespace(); } final SOAIntfMetadata intfMetadata =
	 * ((SOAImplProject)soaProject).getMetadata().getIntfMetadata(); if
	 * (InterfaceSourceType.WSDL.equals(intfMetadata.getWsdlSourceType()) &&
	 * intfMetadata.getWsdl() != null) { return
	 * intfMetadata.getWsdl().getTargetNamespace(); } } else if (soaProject
	 * instanceof SOAConsumerProject) { } return nameSpace; }
	 */

}
