/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.resources.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Properties;
import java.util.jar.JarFile;

import javax.wsdl.Definition;
import javax.wsdl.Service;
import javax.wsdl.WSDLException;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.xerces.parsers.DOMParser;
import org.ebayopensource.turmeric.eclipse.core.logging.SOALogger;
import org.ebayopensource.turmeric.eclipse.core.resources.constants.SOAProjectConstants;
import org.ebayopensource.turmeric.eclipse.resources.model.SOAIntfMetadata;
import org.ebayopensource.turmeric.eclipse.resources.model.SOAIntfProject;
import org.ebayopensource.turmeric.eclipse.utils.io.IOUtil;
import org.ebayopensource.turmeric.eclipse.utils.io.PropertiesFileUtil;
import org.ebayopensource.turmeric.eclipse.utils.lang.StringUtil;
import org.ebayopensource.turmeric.eclipse.utils.plugin.WorkspaceUtil;
import org.ebayopensource.turmeric.eclipse.utils.wsdl.WSDLUtil;
import org.ebayopensource.turmeric.eclipse.utils.xml.XMLUtil;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.osgi.framework.Version;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Text;
import org.xml.sax.InputSource;


/**
 * The Class SOAIntfUtil.
 *
 * @author smathew
 */
public class SOAIntfUtil {
	private static final SOALogger logger = SOALogger.getLogger();
	
	/**
	 * This function returns the version of the service metadata.
	 * For pre 2.4 services, the version would be 1.0.0.
	 * For post 2.4 services, the logic would be the following:
	 * 1) For services available in the workspace,
	 * it would firstly look into the service_intf_project.properties for key sipp_version
	 * 2) For services only available in the underlying repo system as a jar,
	 * then check service_metadata.properties for key smpp_version
	 *
	 * @param assetName the asset name
	 * @param assetLocation the asset location
	 * @return the service metadata version
	 * @throws Exception the exception
	 */
	public static Version getServiceMetadataVersion(String assetName, 
			String assetLocation)
			throws Exception {
		final SOAIntfMetadata metadata = SOAIntfUtil.loadIntfMetadata(assetLocation, assetName);
		try {
			return new Version(metadata.getMetadataVersion());
		} catch (RuntimeException e) {
			logger.warning("Invalid service metadata version number->", metadata.getMetadataVersion(), 
					". Use the default version instead->", SOAProjectConstants.DEFAULT_VERSION);
		}
		return new Version(SOAProjectConstants.DEFAULT_VERSION);
	}
	
	/**
	 * Gets the service version from wsdl.
	 *
	 * @param wsdl the wsdl
	 * @param publicServiceName the public service name
	 * @return the service version from wsdl
	 */
	public static String getServiceVersionFromWsdl(Definition wsdl, 
			String publicServiceName) {
		if (wsdl == null) {
			return "";
		}
		
		for (Object obj : wsdl.getServices().values()) {
			final Service service = (Service)obj;
			if (service.getQName().getLocalPart().equals(publicServiceName)) {
				if (service.getDocumentationElement() != null) {
					final Element elem = service.getDocumentationElement();
					for (int i = 0; i < elem.getChildNodes().getLength(); i++) {
						Node node = elem.getChildNodes().item(i);
						if (node.getNodeType() == Node.ELEMENT_NODE
								&& (node.getLocalName().equals(
										ELEM_NAME_VERSION_V2_CAMEL_CASE) || node
										.getLocalName()
										.equals(ELEM_NAME_VERSION_V3_CAMEL_CASE))) {
							if (node.hasChildNodes()) {
								return node.getFirstChild().getNodeValue();
							}
						}
					}
				}
			}
		}
		
		return "";
	}

	/**
	 * Load metadata props.
	 *
	 * @param assetLocation the asset location
	 * @param serviceName the service name
	 * @return the properties
	 * @throws Exception the exception
	 */
	public static Properties loadMetadataProps(String assetLocation,
			final String serviceName) throws Exception {
		if (SOALogger.DEBUG)
			logger.entering(assetLocation, serviceName);
		Properties props = null;
		if (assetLocation != null && serviceName != null) {
			if (assetLocation.endsWith(SOAProjectConstants.JAR_EXT)) {
				final JarFile file = new JarFile(assetLocation);
				logger.info("loading service_metadata.properties from the service jar->", 
						assetLocation);
				props = loadMetadataProps(new JarFile[] { file }, serviceName);
			} else {
				IPath path = new Path(assetLocation);
				props = loadMetadataProps(path, serviceName);
			}
		}
		if (SOALogger.DEBUG)
			logger.exiting(props);
		return props;
	}

	/**
	 * Load intf metadata.
	 *
	 * @param assetLocation the asset location
	 * @param serviceName the service name
	 * @return the sOA intf metadata
	 * @throws Exception the exception
	 */
	public static SOAIntfMetadata loadIntfMetadata(final String assetLocation,
			final String serviceName) throws Exception {
		if (SOALogger.DEBUG)
			logger.entering(assetLocation, serviceName);
		SOAIntfMetadata result = null;
		if (assetLocation != null && serviceName != null) {
			Properties props = loadMetadataProps(assetLocation, serviceName);

			if (props != null) {
				final SOAIntfMetadata intfMetadata = getMetadataFromProperties(
						props, null);
				final Definition wsdl = getWSDLUrlFromIntfProject(serviceName,
						assetLocation);
				if (wsdl != null) {
					setInformationFromWsdl(wsdl, intfMetadata);
				} else {
					throw new IllegalArgumentException(
							"Unable to find WSDL file for service->"
									+ serviceName);
				}
				result = intfMetadata;
			}
		}
		if (SOALogger.DEBUG)
			logger.exiting(result);
		return result;
	}

	/**
	 * Load metadata props.
	 *
	 * @param zipFiles the zip files
	 * @param serviceName the service name
	 * @return the properties
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws CoreException the core exception
	 */
	public static Properties loadMetadataProps(final JarFile zipFiles[],
			String serviceName) throws IOException, CoreException {
		if (SOALogger.DEBUG)
			logger.entering(Arrays.asList(zipFiles), serviceName);
		Properties properties = null;
		for (JarFile zipFile : zipFiles) {
			try {
				serviceName = StringUtils.isEmpty(serviceName) ? zipFile
						.getName() : serviceName;
				final String jarEntryPath = SOAProjectConstants.METADATA_PROPS_LOCATION_JAR
						+ serviceName
						+ WorkspaceUtil.PATH_SEPERATOR
						+ SOAProjectConstants.PROPS_FILE_SERVICE_METADATA;
				properties = IOUtil.loadProperties(zipFile, jarEntryPath);
				// a project can have many jar files and we never
				// know which one has the props file
				if (properties != null)
					break;
			} catch (Exception e) {
				// This exception is swallowed... One jar not having it doesnt
				// mean failure
			}
		}
		if (SOALogger.DEBUG)
			logger.exiting(properties);
		return properties;
	}

	/**
	 * Load metadata props.
	 *
	 * @param project the project
	 * @param serviceName the service name
	 * @return the properties
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws CoreException the core exception
	 */
	public static Properties loadMetadataProps(final IProject project,
			final String serviceName) throws IOException, CoreException {
		if (SOALogger.DEBUG)
			logger.entering(project, serviceName);
		final IFile file = getMetadataFile(project,
				serviceName != null ? serviceName : project.getName());
		Properties result = null;
		if (file.isAccessible())
			result = WorkspaceUtil.loadProperties(file);
		
		Properties intfProps = loadIntfProjectPropFile(project);
		if (intfProps != null) {
			//values stored in the SIPP file would have higher priority
			if (result == null)
				result = intfProps;
			else {
				result.putAll(intfProps);
			}
		}
		if (SOALogger.DEBUG)
			logger.exiting(result);
		return result;
	}

	/**
	 * Save metadata props.
	 *
	 * @param oldProperties the old properties
	 * @param newProperties the new properties
	 * @param intfProject the intf project
	 * @return true, if successful
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws CoreException the core exception
	 */
	public static boolean saveMetadataProps(Properties oldProperties,
			Properties newProperties, SOAIntfProject intfProject)
			throws IOException, CoreException {
		if (SOALogger.DEBUG)
			logger.entering(oldProperties, newProperties, intfProject);
		boolean result = false;
		if (!PropertiesFileUtil.isEqual(oldProperties, newProperties)) {
			PropertiesFileUtil.writeToFile(newProperties,
					getMetadataFile(intfProject),
					SOAProjectConstants.PROPS_COMMENTS);
			result = true;
		}
		if (SOALogger.DEBUG)
			logger.exiting(result);
		return result;
	}

	/**
	 * Save metadata props.
	 *
	 * @param newProperties the new properties
	 * @param intfProject the intf project
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws CoreException the core exception
	 */
	public static void saveMetadataProps(Properties newProperties,
			SOAIntfProject intfProject) throws IOException, CoreException {
		if (SOALogger.DEBUG)
			logger.entering(newProperties, intfProject);
		PropertiesFileUtil.writeToFile(newProperties,
				getMetadataFile(intfProject),
				SOAProjectConstants.PROPS_COMMENTS);
		if (SOALogger.DEBUG)
			logger.exiting();
	}
	
	/**
	 * Save metadata props.
	 *
	 * @param newProperties the new properties
	 * @param intfProject the intf project
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws CoreException the core exception
	 */
	public static void saveMetadataProps(Properties newProperties,
			IProject intfProject) throws IOException, CoreException {
		if (SOALogger.DEBUG)
			logger.entering(newProperties, intfProject);
		PropertiesFileUtil.writeToFile(newProperties,
				getMetadataFile(intfProject, intfProject.getName()),
				SOAProjectConstants.PROPS_COMMENTS);
		if (SOALogger.DEBUG)
			logger.exiting();
	}

	/**
	 * This is only supposed to be used for modifying service layers and version.
<<<<<<< HEAD
	 *
	 * @param intfProject the intf project
	 * @param monitor the monitor
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws CoreException the core exception
=======
	 * @param intfProject
	 * @throws IOException
	 * @throws CoreException
>>>>>>> TURMERIC-1351
	 */
	public static void saveMetadataProps(SOAIntfProject intfProject, 
			IProgressMonitor monitor)
			throws IOException, CoreException {
		if (SOALogger.DEBUG)
			logger.entering(intfProject);
		
		
		final SOAIntfMetadata intfMetadata = intfProject.getMetadata();
		final IProject project = intfProject.getProject();
		final IFile oldMetadataFile = getOldMetadataFile(project, 
				intfMetadata.getServiceName());
		IFile metadataFile = null;
		Properties props = null;
		
		if (oldMetadataFile.exists() == true) {
			//pre 2.4 projects
			metadataFile = oldMetadataFile;
			props = loadMetadataProps(project, 
					intfMetadata.getServiceName());
		} else {
			metadataFile = getIntfProjectPropFile(project);
			props = loadIntfProjectPropFile(project);
		}
		props.setProperty(
				SOAProjectConstants.SERVICE_METADATA_PROPS_SERVICE_LAYER,
				intfMetadata.getServiceLayer());
		props.setProperty(
				SOAProjectConstants.SERVICE_METADATA_PROPS_SERVICE_VERSION,
				intfMetadata.getServiceVersion());
		PropertiesFileUtil.writeToFile(props,
				metadataFile,
				SOAProjectConstants.PROPS_COMMENTS);
		
		if (oldMetadataFile.exists() == false) {
			//post 2.4 projects
			project.build(IncrementalProjectBuilder.CLEAN_BUILD, monitor);
		}
		
		if (SOALogger.DEBUG)
			logger.exiting();
	}

	/**
	 * Get the IFile instance of the
<<<<<<< HEAD
	 * [PROJECT]/gen-meta-src/META-INF/soa/common/config/[SERVICE_NAME]/service_metadata.properties.
	 *
	 * @param project the project
	 * @param serviceName The interface project name or the service name
	 * @return the new metadata file
=======
	 * [PROJECT]/gen-meta-src/META-INF/soa/common/config/[SERVICE_NAME]/service_metadata.properties
	 * 
	 * @param project
	 * @param serviceName
	 *            The interface project name or the service name
	 * @return
>>>>>>> TURMERIC-1351
	 */
	public static IFile getNewMetadataFile(final IProject project,
			final String serviceName) {
		return getMetadataFile(project, 
				SOAIntfProject.GEN_META_SRC_COMMON_CONFIG,
				serviceName);
	}
	
	/**
	 * return the instance of the service_metadata.properties file. For old project
	 * structure it would call getOldMetadataFile(), otherwise it would invoke
	 * getNewMetadataFile();
	 *
	 * @param project the project
	 * @param serviceName the service name
	 * @return the metadata file
	 */
	public static IFile getMetadataFile(final IProject project, 
			final String serviceName) {
		final IFile metadataFile = getOldMetadataFile(project, serviceName);
		if (metadataFile.exists() == true)
			return metadataFile;
		else
			return getNewMetadataFile(project, serviceName);
	}
	
	private static IFile getMetadataFile(final IProject project, 
			final String root, final String serviceName) {
		return project.getFile(StringUtil.toString(
				root, WorkspaceUtil.PATH_SEPERATOR, serviceName,
				WorkspaceUtil.PATH_SEPERATOR,
				SOAProjectConstants.PROPS_FILE_SERVICE_METADATA));
	}
	
	/**
	 * Get the IFile instance of the
	 * [PROJECT]/meta-src/META-INF/soa/common/config/[SERVICE_NAME]/service_metadata.properties.
	 *
	 * @param project the project
	 * @param serviceName The interface project name or the service name
	 * @return the old metadata file
	 */
	public static IFile getOldMetadataFile(final IProject project, 
			final String serviceName) {
		return getMetadataFile(project, 
				SOAIntfProject.META_SRC_COMMON_CONFIG,
				serviceName);
	}

	/**
	 * Gets the type mappings file.
	 *
	 * @param project the project
	 * @param serviceName the service name
	 * @return the type mappings file
	 */
	public static IFile getTypeMappingsFile(final IProject project,
			final String serviceName) {
		return project.getFile(StringUtil.toString(
				SOAIntfProject.GEN_META_SRC_COMMON_CONFIG,
				WorkspaceUtil.PATH_SEPERATOR, serviceName,
				WorkspaceUtil.PATH_SEPERATOR,
				SOAProjectConstants.FILE_TYPE_MAPPINGS));
	}

	/**
	 * Gets the metadata file.
	 *
	 * @param intfProject the intf project
	 * @return the metadata file
	 */
	public static IFile getMetadataFile(SOAIntfProject intfProject) {
		IProject project = intfProject.getEclipseMetadata().getProject();
		return getMetadataFile(project, intfProject.getMetadata()
				.getServiceName());
	}

	/**
	 * Gets the intf project prop file.
	 *
	 * @param project the project
	 * @return the intf project prop file
	 */
	public static IFile getIntfProjectPropFile(final IProject project) {
		return project
				.getFile(SOAProjectConstants.PROPS_FILE_SERVICE_INTERFACE);
	}
	
	public static IFile getIntfProtoBufFile(final IProject project) {
		String protoBufPath = getIntfProtoBufFilePath(project.getName());
		return project.getFile(protoBufPath);
	}
	
	public static String getIntfProtoBufFilePath(String adminName) {
		return SOAProjectConstants.FOLDER_META_SRC + "/"
				+ SOAProjectConstants.META_PROTO_BUF + "/" + adminName + "/"
				+ adminName + ".proto";
	}

	/**
	 * Load intf project prop file.
	 *
	 * @param project the project
	 * @return the properties
	 * @throws CoreException the core exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static Properties loadIntfProjectPropFile(final IProject project)
			throws CoreException, IOException {
		final Properties properties = new Properties();
		final IFile file = getIntfProjectPropFile(project);
		if (file.isAccessible() == false) {
			logger.warning("The interface project's property file[", file
					.getLocation(), "] is not accessible");
			return properties;
		}
		InputStream io = null;
		try {
			io = file.getContents();
			properties.load(io);
		} finally {
			IOUtils.closeQuietly(io);
		}
		return properties;
	}

	/**
	 * Sets the information from wsdl.
	 *
	 * @param wsdlLocation the wsdl location
	 * @param metadata the metadata
	 * @throws WSDLException the wSDL exception
	 */
	public static void setInformationFromWsdl(final URL wsdlLocation,
			final SOAIntfMetadata metadata) throws WSDLException {
		if (wsdlLocation != null) {
			setInformationFromWsdl(wsdlLocation.toString(), metadata);
		}
	}

	/**
	 * Sets the information from wsdl.
	 *
	 * @param documentBaseURI the document base uri
	 * @param wsdlStream the wsdl stream
	 * @param metadata the metadata
	 * @throws WSDLException the wSDL exception
	 */
	public static void setInformationFromWsdl(final String documentBaseURI,
			final InputStream wsdlStream, final SOAIntfMetadata metadata)
			throws WSDLException {
		try {
			if (SOALogger.DEBUG)
				logger.entering(documentBaseURI, wsdlStream, metadata);
			if (wsdlStream != null) {
				final Definition wsdl = WSDLUtil.readWSDL(documentBaseURI,
						wsdlStream);
				setInformationFromWsdl(wsdl, metadata);
			}
		} finally {
			IOUtils.closeQuietly(wsdlStream);
			if (SOALogger.DEBUG)
				logger.exiting();
		}
	}

	/**
	 * Sets the information from wsdl.
	 *
	 * @param wsdlLocation the wsdl location
	 * @param metadata the metadata
	 * @throws WSDLException the wSDL exception
	 */
	public static void setInformationFromWsdl(final String wsdlLocation,
			final SOAIntfMetadata metadata) throws WSDLException {
		if (SOALogger.DEBUG)
			logger.entering(wsdlLocation, metadata);
		if (StringUtils.isNotBlank(wsdlLocation)) {
			final Definition wsdl = WSDLUtil.readWSDL(wsdlLocation);
			setInformationFromWsdl(wsdl, metadata);
		}
		if (SOALogger.DEBUG)
			logger.exiting();
	}

	/**
	 * Sets the information from wsdl.
	 *
	 * @param wsdl the wsdl
	 * @param metadata the metadata
	 * @throws WSDLException the wSDL exception
	 */
	public static void setInformationFromWsdl(final Definition wsdl,
			final SOAIntfMetadata metadata) throws WSDLException {
		if (SOALogger.DEBUG)
			logger.entering(wsdl, metadata);
		if (wsdl != null && metadata != null) {
			if (metadata.getPublicServiceName() == null)
				metadata.setPublicServiceName(WSDLUtil.getServiceNameFromWSDL(wsdl));
			if (metadata.getTargetNamespace() == null)
				metadata.setTargetNamespace(WSDLUtil.getTargetNamespace(wsdl));
			metadata.setServiceLocation(WSDLUtil
					.getServiceLocationFromWSDL(wsdl));
		}
		if (SOALogger.DEBUG)
			logger.exiting();
	}

	/**
	 * load the metadata from
	 * [PROJECT]/meta-src/META-INF/soa/common/config/[SERVICE_NAME]/service_metadata.properties.
	 *
	 * @param project the project
	 * @param metadata the metadata
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws CoreException the core exception
	 * @throws WSDLException the wSDL exception
	 */
	public static void fillMetadata(final IProject project,
			final SOAIntfMetadata metadata) throws IOException, CoreException,
			WSDLException {
		if (SOALogger.DEBUG)
			logger.entering(project, metadata);
		final Properties properties = loadMetadataProps(project, metadata
				.getServiceName());
		if (properties != null) {
			getMetadataFromProperties(properties, metadata);
			if (SOAProjectConstants.InterfaceSourceType.WSDL.toString().equals(metadata
					.getSourceType().toString())) {
				final IFile wsdlFile = SOAServiceUtil.getWsdlFile(project,
						project.getName());
				if (wsdlFile != null)
					setInformationFromWsdl(wsdlFile.getLocation().toString(),
							wsdlFile.getContents(), metadata);
			}
		}
		if (SOALogger.DEBUG)
			logger.exiting();
	}

	/**
	 * Gets the metadata from properties.
	 *
	 * @param properties the properties
	 * @param metadata the metadata
	 * @return the metadata from properties
	 * @throws MalformedURLException the malformed url exception
	 */
	public static SOAIntfMetadata getMetadataFromProperties(
			final Properties properties, SOAIntfMetadata metadata)
			throws MalformedURLException {
		if (SOALogger.DEBUG)
			logger.entering(properties, metadata);
		if (metadata == null) {
			metadata = SOAIntfMetadata
					.create(SOAProjectConstants.InterfaceSourceType.WSDL);
		}
		String adminName = properties.containsKey(SOAProjectConstants.PROP_KEY_ADMIN_NAME) ?
				properties.getProperty(SOAProjectConstants.PROP_KEY_ADMIN_NAME) : 
					properties.getProperty(SOAProjectConstants.PROP_KEY_SERVICE_NAME);
		adminName = StringUtils.trim(adminName);
		metadata.setServiceName(adminName);
		metadata.setPublicServiceName(StringUtils.trim(properties
				.getProperty(SOAProjectConstants.PROP_KEY_SERVICE_NAME)));
		if (StringUtils.isBlank(metadata.getServiceInterface())) {
			metadata.setServiceInterface(StringUtils.trim(properties
					.getProperty(SOAProjectConstants.SERVICE_METADATA_PROPS_SERVICE_INTERFACE_CLASS_NAME)));
		}
		if (StringUtils.isBlank(metadata.getServiceLayer())) {
			metadata.setServiceLayer(StringUtils.trim(properties
					.getProperty(SOAProjectConstants.SERVICE_METADATA_PROPS_SERVICE_LAYER)));
		}
		if (StringUtils.isBlank(metadata.getServiceVersion())) {
			metadata.setServiceVersion(StringUtils.trim(properties
					.getProperty(SOAProjectConstants.SERVICE_METADATA_PROPS_SERVICE_VERSION)));
		}
		
		if (properties.containsKey(SOAProjectConstants.PROPS_KEY_SIPP_VERSION)) {
			metadata.setMetadataVersion(StringUtils.trim(properties.getProperty(
					SOAProjectConstants.PROPS_KEY_SIPP_VERSION)));
		} else if (properties.containsKey(SOAProjectConstants.PROPS_KEY_SMP_VERSION)) {
			metadata.setMetadataVersion(StringUtils.trim(properties.getProperty(
					SOAProjectConstants.PROPS_KEY_SMP_VERSION)));
		}
		
		if (properties.containsKey(SOAProjectConstants.PROPS_SERVICE_NAMESPACE_PART)) {
			metadata.setServiceNamespacePart(StringUtils.trim(properties.getProperty(
					SOAProjectConstants.PROPS_SERVICE_NAMESPACE_PART)));
		}
		
		if (properties.containsKey(SOAProjectConstants.PROPS_SERVICE_DOMAIN_NAME)) {
			metadata.setServiceDomainName(StringUtils.trim(properties.getProperty(
					SOAProjectConstants.PROPS_SERVICE_DOMAIN_NAME)));
		}
		
		if (properties.containsKey(SOAProjectConstants.PROP_KEY_NON_XSD_FORMATS)) {
			metadata.setServiceNonXSDProtocols(StringUtils.trim(properties
					.getProperty(SOAProjectConstants.PROP_KEY_NON_XSD_FORMATS)));
		}
		
		if (properties.containsKey(SOAProjectConstants.PROPS_SUPPORT_ZERO_CONFIG)) {
			metadata.setZeroConfig(Boolean.valueOf(StringUtils.trim(properties
					.getProperty(SOAProjectConstants.PROPS_SUPPORT_ZERO_CONFIG))));
		}
		
		final String wsdlUrl = StringUtils.trim(properties
				.getProperty(SOAProjectConstants.PROP_KEY_ORIGINAL_WSDL_URI));
		if (WSDLUtil.isValidURL(wsdlUrl)) {
			metadata.setOriginalWSDLUrl(new URL(wsdlUrl));
		} else {
			metadata
					.setSourceType(SOAProjectConstants.InterfaceSourceType.JAVA);
		}
		if (SOALogger.DEBUG)
			logger.exiting(metadata);

		return metadata;
	}

	/**
	 * load the service_metadata.properties from the correct location:
	 * 1) if file exists in meta-src folder, then it is using old dir structure
	 * 2) if file exists in gen-meta-src folder, then it is using the new dir structure and it is already been generated
	 * 3) if file does not exist in the previous two locations, then it should be using new dir but file not generated yet.
	 * Read it from service_interface_project.properties file.
	 *
	 * @param projectDir the project dir
	 * @param serviceName the service name
	 * @return the properties
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static Properties loadMetadataProps(final IPath projectDir,
			final String serviceName) throws IOException {
		if (SOALogger.DEBUG)
			logger.entering(projectDir, serviceName);
		final String pathSuffix = serviceName + WorkspaceUtil.PATH_SEPERATOR
		+ SOAProjectConstants.PROPS_FILE_SERVICE_METADATA;
		File metadataFile = null;
		
		final IPath root = (IPath)projectDir.clone();
		final IPath serviceMetadataPath = root.append(
				SOAIntfProject.META_SRC_COMMON_CONFIG).append(pathSuffix);
		final IPath serviceGenMetadataPath = root.append(
				SOAIntfProject.GEN_META_SRC_COMMON_CONFIG).append(pathSuffix);
		
		if (serviceMetadataPath.makeAbsolute().toFile().exists()) {
			//the smp file exists in the meta-src folder
			metadataFile = serviceMetadataPath.makeAbsolute().toFile();
			logger.info("Loading service metadata from the old dir structure->", metadataFile);
		} else if (serviceGenMetadataPath.makeAbsolute().toFile().exists()){
			//the smp file already been generated
			metadataFile = serviceGenMetadataPath.makeAbsolute().toFile();
			logger.info("Loading service metadata from the new dir structure->", metadataFile);
		} else {
			//the smp file not generated yet
			metadataFile = projectDir.append(SOAProjectConstants.PROPS_FILE_SERVICE_INTERFACE)
			.makeAbsolute().toFile();
			logger.info("Loading service metadata from the service_intf_project.propperties->", metadataFile);
		}
		Properties result = null;
		if (metadataFile.exists() && metadataFile.canRead()) {
			result = new Properties();
			InputStream in = null;
			try {
				//TODO decode the white spaces: %20
				in = new FileInputStream(metadataFile);
				result.load(in);
			} finally {
				IOUtils.closeQuietly(in);
			}
		}
		
		if (SOALogger.DEBUG)
			logger.exiting(result);
		return result;
	}

	/**
	 * Gets the wSDL url from intf project.
	 *
	 * @param serviceName the service name
	 * @param requiredServiceProjectPath the required service project path
	 * @return the wSDL url from intf project
	 * @throws Exception the exception
	 */
	public static Definition getWSDLUrlFromIntfProject(
			final String serviceName, String requiredServiceProjectPath)
			throws Exception {
		final IProject project = WorkspaceUtil.getProject(serviceName);
		return getWSDLUrlFromProject(project, requiredServiceProjectPath,
				serviceName);
	}
	
	private static final String ELEM_NAME_SERVICE = "service";
	private static final String ELEM_NAME_DOCUMENTATION = "documentation";
	private static final String ELEM_NAME_VERSION_V2_CAMEL_CASE = "Version";
	private static final String ELEM_NAME_VERSION_V3_CAMEL_CASE = "version";
	
	/**
	 * The format should be the following
	 * &lt;wsdl:service name="CreativeService">
	 * &lt;wsdl:documentation>
	 *    &lt;version>1.0&lt;/version>
	 * &lt;/wsdl:documentation>
	 * ...
	 *
	 * @param project the project
	 * @param newVersion the new version
	 * @param monitor the monitor
	 * @throws Exception the exception
	 */
	public static void modifyWsdlAppInfoVersion(final IProject project, 
			final String newVersion, IProgressMonitor monitor)
	throws Exception {
		monitor.setTaskName("Modifying service WSDL...");
		final String serviceName = project.getName();
		final IFile wsdlFile = SOAServiceUtil.getWsdlFile(project,
				serviceName);
		InputStream ins = null;
		Definition wsdl = null;
		try {
			ins = wsdlFile.getContents();
			wsdl = WSDLUtil.readWSDL(ins);
		} finally {
			IOUtils.closeQuietly(ins);
		}
		monitor.worked(10);
		if (wsdl == null)
			return;
		
		DOMParser domParser = new DOMParser();
		Document doc = null;
		try {
			ins = wsdlFile.getContents();
			domParser.parse(new InputSource(ins));
			doc = domParser.getDocument();
		} finally {
			IOUtils.closeQuietly(ins);
		}
		monitor.worked(10);
		if (doc == null)
			return;
		
		Node wsdlNode = doc.getFirstChild();
		Node serviceNode = null;
		for (int i = wsdlNode.getChildNodes().getLength() - 1; i >= 0 ; i--) {
			Node node = wsdlNode.getChildNodes().item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE && 
					ELEM_NAME_SERVICE.equals(node.getLocalName())) {
				serviceNode = node;
				break;
			}
		}
		monitor.worked(10);
		if (serviceNode == null)
			return;
		
		Node documentationNode = null;
		for (int i = 0; i < serviceNode.getChildNodes().getLength(); i++) {
			Node node = serviceNode.getChildNodes().item(i);
			if (ELEM_NAME_DOCUMENTATION.equals(node.getLocalName())) {
				documentationNode = node;
			}
		}
		monitor.worked(10);
		
		boolean needUpdateWsdl = false;
		
		if (documentationNode != null) {
			//we found the documentation node
			Node verNode = null;
			for (int i = 0; i < documentationNode.getChildNodes().getLength(); i++) {
				Node node = documentationNode.getChildNodes().item(i);
				if (ELEM_NAME_VERSION_V2_CAMEL_CASE.equals(node.getLocalName())
						|| ELEM_NAME_VERSION_V3_CAMEL_CASE.equals(node
								.getLocalName())) {
					verNode = node;
					break;
				}
			}

			if (verNode == null) {
				// add version node to document node if there is no version
				// node.
				Element v3Version = doc.createElement("version");
				Text versionText = doc.createTextNode(newVersion);
				v3Version.appendChild(versionText);
				documentationNode.appendChild(v3Version);
				needUpdateWsdl = true;
			} else {
				if (ELEM_NAME_VERSION_V2_CAMEL_CASE.equals(verNode
						.getLocalName())) {
					// if current version node is V2 format, replace it with V3
					// format
					Element v3Version = doc.createElement("version");
					Text versionText = doc.createTextNode(newVersion);
					v3Version.appendChild(versionText);
					documentationNode.replaceChild(v3Version, verNode);
					needUpdateWsdl = true;
				} else {
					// current version format is V3, update version value.
					for (int i = 0; i < verNode.getChildNodes().getLength(); i++) {
						Node node = verNode.getChildNodes().item(i);
						if (node.getNodeType() == Node.TEXT_NODE
								&& newVersion.equals(node.getNodeValue()) == false) {
							logger
									.warning(
											"Version defined in WSDL's service section->",
											node.getNodeValue(),
											" is older than the new version->",
											newVersion);
							node.setNodeValue(newVersion);
							needUpdateWsdl = true;
							break;
						}
					}
				}
			}
		}
		
		monitor.worked(10);
		if (needUpdateWsdl == true) {
			FileWriter writer = null;
			try {
				writer = new FileWriter(wsdlFile.getLocation().toFile());
				XMLUtil.writeXML(wsdlNode, writer);
			} finally {
				IOUtils.closeQuietly(writer);
				wsdlFile.refreshLocal(IResource.DEPTH_ONE, monitor);
			}
		} else {
			logger.info("WSDL already have the correct version '", newVersion, 
					"', skip the modification for WSDL->", wsdlFile.getLocation());
		}
		monitor.worked(10);
	}

	private static Definition getWSDLUrlFromProject(final IProject project,
			String requiredServiceProjectPath, final String serviceName)
			throws Exception {
		if (SOALogger.DEBUG)
			logger.entering(project, requiredServiceProjectPath, serviceName);
		Definition result = null;
		// load the wsdl directly from the project if available
		if (project != null && project.isAccessible()) {
			final IFile wsdlFile = SOAServiceUtil.getWsdlFile(project,
					serviceName);
			if (wsdlFile != null && wsdlFile.exists()) {
				if (SOALogger.DEBUG)
					logger.debug("Directly loading WSDL from the project->",
							wsdlFile.getLocation());
				result = WSDLUtil.readWSDL(wsdlFile.getLocation().toString());
			}
		}

		// read from the local system
		if (result == null
				&& StringUtils.isNotBlank(requiredServiceProjectPath)) {
			final IPath intfProjectPath = new Path(requiredServiceProjectPath);
			if (SOAProjectConstants.FILE_EXTENSION_JAR
					.equalsIgnoreCase(intfProjectPath.getFileExtension())) {
				final File file = intfProjectPath.toFile();
				final String jarEntryLoc = StringUtil.toString(
						SOAProjectConstants.META_INF_WSDL,
						WorkspaceUtil.PATH_SEPERATOR, serviceName,
						WorkspaceUtil.PATH_SEPERATOR, serviceName,
						SOAProjectConstants.WSDL_EXT);
				if (SOALogger.DEBUG)
					logger.debug("Reading WSDL from ", file, "!", jarEntryLoc);
				result = WSDLUtil.readWSDLFromJarFile(file, jarEntryLoc);
			} else {
				final IPath wsdlPath = intfProjectPath.append(
						SOAIntfProject.META_SRC_WSDL).append(serviceName)
						.append(serviceName).addFileExtension("wsdl");
				if (wsdlPath.toFile().exists()) {
					if (SOALogger.DEBUG)
						logger.debug("Loading WSDL from ", wsdlPath);
					result = WSDLUtil.readWSDL(wsdlPath.toString());
				}
			}
		}

		if (SOALogger.DEBUG)
			logger.exiting(result);
		return result;
	}
}
