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
package org.ebayopensource.turmeric.repositorysystem.imp.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.maven.model.Model;
import org.apache.maven.repository.metadata.ArtifactMetadata;
import org.ebayopensource.turmeric.eclipse.logging.SOALogger;
import org.ebayopensource.turmeric.eclipse.maven.core.repositorysystem.IMavenOrganizationProvider;
import org.ebayopensource.turmeric.eclipse.maven.ui.preferences.TurmericSOAConfigPrefInitializer;
import org.ebayopensource.turmeric.eclipse.registry.ExtensionPointFactory;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.ISOAOrganizationProvider;
import org.ebayopensource.turmeric.eclipse.resources.constants.SOAProjectConstants;
import org.ebayopensource.turmeric.eclipse.resources.constants.SOAProjectConstants.SOAFrameworkLibrary;
import org.ebayopensource.turmeric.eclipse.resources.constants.SOAProjectConstants.SupportedProjectType;
import org.ebayopensource.turmeric.eclipse.resources.util.SOAServiceUtil;
import org.ebayopensource.turmeric.eclipse.utils.collections.ListUtil;
import org.ebayopensource.turmeric.eclipse.utils.lang.StringUtil;
import org.ebayopensource.turmeric.repositorysystem.imp.utils.TurmericConstants;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;


/**
 * @author yayu
 *
 */
public class TurmericOrganizationProvider implements IMavenOrganizationProvider {
	private static final SOALogger logger = SOALogger.getLogger();
	
	public static final String NAME = "Turmeric";
	
	private static final Map<SupportedProjectType, String> GROUPID_MAP;
	private static final List<String> ALL_GROUPIDS;
	private static final Collection<String> MANAGED_ARTIFACTS;
	
	static {
		Map<SupportedProjectType, String> data = new ConcurrentHashMap<SupportedProjectType, String>();
		data.put(SupportedProjectType.INTERFACE, 
				TurmericConstants.SOA_INTERFACE_GROUPID);
		data.put(SupportedProjectType.IMPL, 
				TurmericConstants.SOA_IMPL_GROUPID);
		data.put(SupportedProjectType.CONSUMER, 
				TurmericConstants.SOA_CLIENT_GROUPID);
		data.put(SupportedProjectType.TYPE_LIBRARY, 
				TurmericConstants.SOA_TYPELIBRARY_GROUPID);
		data.put(SupportedProjectType.ERROR_LIBRARY, 
				TurmericConstants.SOA_ERRORLIBRARY_GROUPID);
		GROUPID_MAP = Collections.unmodifiableMap(data);
		
		ALL_GROUPIDS = ListUtil.arrayList(GROUPID_MAP.values());
		
		Collection<String> list = new ArrayList<String>();
		list.add(TurmericConstants.SOA_CLIENT);
		list.add(TurmericConstants.SOA_SERVER);
		list.add(TurmericConstants.SOA_TOOLS);
		list.add(TurmericConstants.SOA_COMMON_TL);
		list.add(TurmericConstants.SOA_COMMON_EL);
		list.add(TurmericConstants.SOA_MAVEN_PREBUILD_PLUGIN);
		list.add(TurmericConstants.BINDING_FRAMEWORK);
		MANAGED_ARTIFACTS = Collections.unmodifiableCollection(list);
	}
	
	public static final ISOAOrganizationProvider INSTANCE 
	= new TurmericOrganizationProvider();

	/**
	 * 
	 */
	public TurmericOrganizationProvider() {
		super();
	}

	/* (non-Javadoc)
	 * @see org.ebayopensource.turmeric.eclipse.repositorysystem.core.ISOAOrganizationProvider#getName()
	 */
	public String getName() {
		return NAME;
	}

	/* (non-Javadoc)
	 * @see org.ebayopensource.turmeric.eclipse.repositorysystem.core.ISOAOrganizationProvider#supportFunctionalDomain()
	 */
	public boolean supportFunctionalDomain() {
		return true;
	}

	/* (non-Javadoc)
	 * @see org.ebayopensource.turmeric.eclipse.repositorysystem.core.ISOAOrganizationProvider#supportAssetRepositoryIntegration()
	 */
	public boolean supportAssetRepositoryIntegration() {
		return true;
	}

	/* (non-Javadoc)
	 * @see org.ebayopensource.turmeric.eclipse.repositorysystem.core.ISOAOrganizationProvider#shouldShowInNamespaceToPackageViewer(java.lang.String)
	 */
	public boolean shouldShowInNamespaceToPackageViewer(String namespace) {
		return true;
	}

	public String generateInterfaceClassName(String packageName,
			String adminName) {
		if (StringUtils.isBlank(packageName))
			packageName = TurmericConstants.DEFAULT_TURMERIC_PACKAGE_NAME;
		
		final String serviceName = adminName;
		if (StringUtils.isBlank(serviceName))
			return StringUtil.toString(packageName, ".IService");
		return StringUtil.toString(packageName,
				SOAProjectConstants.CLASS_NAME_SEPARATOR, serviceName
						.toLowerCase(), ".I", StringUtils
						.capitalize(serviceName));
	}

	public String generateServiceNamespace(String domainName,
			String namespacePart, String serviceVersion) {
		final StringBuffer buf = new StringBuffer();
		buf.append(TurmericConstants.DEFAULT_SERVICE_NAMESPACE_PREFIX);
		if (StringUtils.isNotBlank(domainName)) {
			//final String classifier = getDomainClassifier();
			buf.append(SOAProjectConstants.DELIMITER_URL_SLASH);
			buf.append(namespacePart);
		}
		if (StringUtils.isNotBlank(serviceVersion)) {
			buf.append(SOAProjectConstants.DELIMITER_URL_SLASH);
			buf.append(SOAProjectConstants.MAJOR_VERSION_PREFIX.toLowerCase(Locale.US));
			buf.append(SOAServiceUtil.getServiceMajorVersion(serviceVersion));
		}
		buf.append(TurmericConstants.DEFAULT_SERVICE_NAMESPACE_SUFFIX);
		return buf.toString();
	}
	
	public String generateTypeLibraryTargetNamespace(
			String domainName, String namespacePart, String version) {
		if (StringUtils.isBlank(domainName) || StringUtils.isBlank(namespacePart)) {
			return TurmericConstants.DEFAULT_SERVICE_NAMESPACE;
		}
		final StringBuffer buf = new StringBuffer();
		buf.append(TurmericConstants.DEFAULT_SERVICE_NAMESPACE_PREFIX);
		if (StringUtils.isNotBlank(domainName)) {
			buf.append(SOAProjectConstants.DELIMITER_URL_SLASH);
			buf.append(namespacePart);
		}
		if (StringUtils.isNotBlank(version)) {
			buf.append(SOAProjectConstants.DELIMITER_URL_SLASH);
			buf.append(SOAProjectConstants.MAJOR_VERSION_PREFIX.toLowerCase(Locale.US));
			buf.append(SOAServiceUtil.getServiceMajorVersion(version));
		}
		buf.append(TurmericConstants.DEFAULT_TYPES_NAMESPACE_SUFFIX);
		return buf.toString();
	}
	

	public String getNamespacePartFromTargetNamespace(String targetNamespace) {
		if (StringUtils.isNotBlank(targetNamespace) && targetNamespace.startsWith(
				TurmericConstants.DEFAULT_SERVICE_NAMESPACE_PREFIX)) {
			//it could be following the Marketplace format
			boolean matches = Pattern.matches(
					TurmericConstants.TURMERIC_NAMESPACE_PATTERN, 
					targetNamespace);
			if (matches == true) {
				if (SOALogger.DEBUG) {
					logger.debug(
							"The target namespace is following the Turmeric recommended format->", 
							targetNamespace);
				}
				targetNamespace = StringUtils.substringAfter(targetNamespace, 
						TurmericConstants.DEFAULT_SERVICE_NAMESPACE_PREFIX);
				return StringUtils.substringBetween(targetNamespace, "/");
			} else {
				if (SOALogger.DEBUG) {
					logger.warning(
							"The target namespace is NOT following the Marketpalce format->", 
							targetNamespace);
				}
			}
			
		}
		return "";
	}

	public List<String> getDefaultDependencies(SupportedProjectType projectType) {
		List<String> result = Collections.emptyList();
		if (SupportedProjectType.INTERFACE.equals(projectType)) {
			result = TurmericConstants.DEFAULT_DEPENDENCIES_INTERFACE;
		} else if (SupportedProjectType.IMPL.equals(projectType)) {
			result = TurmericConstants.DEFAULT_DEPENDENCIES_IMPL;
		} else if (SupportedProjectType.CONSUMER.equals(projectType)) {
			result = TurmericConstants.DEFAULT_DEPENDENCIES_CONSUMER;
		} else if (SupportedProjectType.TYPE_LIBRARY.equals(projectType)) {
			result = TurmericConstants.DEFAULT_DEPENDENCIES_TYPELIB;
		} else if (SupportedProjectType.ERROR_LIBRARY.equals(projectType)) {
			result = TurmericConstants.DEFAULT_DEPENDENCIES_ERRORLIB;
		}
		
		return result;
	}

	public List<String> getCommonErrorLibraries() {
		List<String> libs = new ArrayList<String>();
		libs.add(TurmericConstants.SOA_CLIENT);
		return libs;
	}

	/* (non-Javadoc)
	 * @see org.ebayopensource.turmeric.eclipse.repositorysystem.core.ISOAOrganizationProvider#getSOAFrameworkLibraryIdentifier(java.lang.String)
	 */
	public String getSOAFrameworkLibraryIdentifier(SOAFrameworkLibrary soaLibraryName) {
		if (SOAFrameworkLibrary.SOASERVER.equals(soaLibraryName)) {
			return TurmericConstants.SOA_SERVER;
		} else if (SOAFrameworkLibrary.SOATOOLS.equals(soaLibraryName)) {
			return TurmericConstants.SOA_TOOLS;
		} else if (SOAFrameworkLibrary.SOACLIENT.equals(soaLibraryName)) {
			return TurmericConstants.SOA_CLIENT;
		}
		return null;
	}

	public IStatus preValidateChangeServiceVersion(String oldVersion,
			String newVersion, String serviceName) throws Exception {
		return Status.OK_STATUS;
	}

	public ArtifactMetadata getParentPom(SupportedProjectType projectType) {
		//we dont need parent pom for Turmeric yet
		return null;
	}

	public String getProjectGroupId(SupportedProjectType projectType) {
		return GROUPID_MAP.get(projectType);
	}

	public List<String> getAllProjectTypeGroupIds() {
		return ALL_GROUPIDS;
	}

	public boolean supportAssertionServiceIntegration() throws CoreException {
		return ExtensionPointFactory.isASForWSDLEnabled();
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.ebayopensource.turmeric.eclipse.repositorysystem.core.ISOAOrganizationProvider#getMinimumRequiredTurmericFrameworkVersion()
	 */
	public String getMinimumRequiredTurmericFrameworkVersion() {
		return TurmericConstants.TURMERIC_MIN_REQUIRED_VERSION;
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.ebayopensource.turmeric.eclipse.repositorysystem.core.ISOAOrganizationProvider#getPreferredArtifactVersion(java.lang.String, java.lang.String)
	 */
	public String getPreferredArtifactVersion(String groupId, String artifactId) {
		if (MANAGED_ARTIFACTS.contains(groupId + ":" + artifactId)) {
			return TurmericSOAConfigPrefInitializer.getPreferredVersion();
		}
		return null;
	}

	public String getDisplayName() {
		return "Default";
	}

	public String getURLPattern(String serviceName, String domainName,
			int majorVersion) {
		return new StringBuilder("/").append(serviceName).toString();
	}

	public String getSOAPProtocolProcessorClassName() {
		return TurmericConstants.PROTOCOL_PROCESSOR_CLASS_NAME_VALUE;
	}

	public void postAddingServiceToWebProjects(
			List<IProject> serviceImplProjects, IProject webProject,
			Model webProjectPom, IProgressMonitor monitor) throws Exception {
		// TODO Auto-generated method stub
		
	}

}
