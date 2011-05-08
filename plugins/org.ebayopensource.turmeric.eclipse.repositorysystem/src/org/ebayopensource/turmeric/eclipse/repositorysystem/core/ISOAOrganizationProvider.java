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
package org.ebayopensource.turmeric.eclipse.repositorysystem.core;

import java.util.List;

import org.ebayopensource.turmeric.eclipse.core.resources.constants.SOAProjectConstants.SOAFrameworkLibrary;
import org.ebayopensource.turmeric.eclipse.core.resources.constants.SOAProjectConstants.SupportedProjectType;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;


/**
 * Represents the details for the underlying organization.
 *
 * @author yayu
 * @since 1.0.0
 */
public interface ISOAOrganizationProvider {
	
	/**
	 * The name of the underlying organization.
	 *
	 * @return the name
	 */
	public String getName();
	
	/**
	 * Gets the display name.
	 *
	 * @return the descriptive name of the underlying organization
	 */
	public String getDisplayName();
	
	/**
	 * Whether support the functional domain.
	 *
	 * @return true, if successful
	 */
	public boolean supportFunctionalDomain();
	
	/**
	 * Whether the underlying organization support integration
	 * with Asset Repository.
	 *
	 * @return true, if successful
	 */
	public boolean supportAssetRepositoryIntegration();
	
	/**
	 * Whether the underlying organization support integration
	 * with Assertion Service.
	 *
	 * @return true, if successful
	 * @throws CoreException the core exception
	 */
	public boolean supportAssertionServiceIntegration() throws CoreException;
	
	/**
	 * Check whether to show the given namespace in the NamespaceToPackage viewer.
	 *
	 * @param namespace the namespace
	 * @return true, if successful
	 */
	public boolean shouldShowInNamespaceToPackageViewer(String namespace);
	
	/**
	 * Generate the WSDL target namespace.
	 *
	 * @param domainName the domain name
	 * @param namespacePart the namespace part
	 * @param serviceVersion the service version
	 * @return the string
	 * @since 1.0.0
	 */
	public String generateServiceNamespace(String domainName, 
			String namespacePart, String serviceVersion);
	
	/**
	 * Generate interface class name.
	 *
	 * @param packageName user could use null for using the default package name for the underlying organization
	 * @param adminName the admin name
	 * @return the string
	 * @since 1.0.0
	 */
	public String generateInterfaceClassName(String packageName, String adminName);
	
	/**
	 * parse the given targetNamesapce to get the namesapce-part out of it if
	 * the namespace is following the Marketplace format. Otherwise return empty string.
	 *
	 * @param targetNamespace the target namespace
	 * @return the namespace part from target namespace
	 * @since 1.0.0
	 */
	public String getNamespacePartFromTargetNamespace(String targetNamespace);
	
	/**
	 * Generate type library target namespace.
	 *
	 * @param domainName the domain name
	 * @param namespacePart the namespace part
	 * @param version the version
	 * @return the string
	 * @since 1.0.0
	 */
	public String generateTypeLibraryTargetNamespace(
			String domainName, String namespacePart, String version);
	
	/**
	 * Gets the default dependencies.
	 *
	 * @param projectType the project type
	 * @return the default dependencies
	 */
	public List<String> getDefaultDependencies(SupportedProjectType projectType);
	
	/**
	 * Return names of the error libraries used for runtime.
	 *
	 * @return the common error libraries
	 * @returnt
	 */
	public List<String> getCommonErrorLibraries();
	
	/**
	 * return the build system specific identifier for the given soa library name.
	 * @param soaLibraryName The expected value would be SOATools, SOAClient and SOAServer
	 * @return either identifier or null if could not be found
	 */
	public String getSOAFrameworkLibraryIdentifier(SOAFrameworkLibrary soaLibraryName);
	
	/**
	 * Gets the minimum required turmeric framework version.
	 *
	 * @return the minimum required Turmeric framework version
	 */
	public String getMinimumRequiredTurmericFrameworkVersion();
	
	/**
	 * Gets the preferred artifact version.
	 *
	 * @param groupId the group id
	 * @param artifactId the artifact id
	 * @return the preferred version for the given artifact or null if do not have a preferred version
	 */
	public String getPreferredArtifactVersion(String groupId, String artifactId);
	
	/**
	 * Prevalidate the new service version with the validator of the underlying organization.
	 *
	 * @param oldVersion the old version
	 * @param newVersion the new version
	 * @param serviceName the service name
	 * @return the i status
	 * @throws Exception the exception
	 */
	public IStatus preValidateChangeServiceVersion(String oldVersion,
			String newVersion, String serviceName) throws Exception;
	
	/**
	 * return the build system specified URL pattern.
	 * a leading slash "/" should be added as a prefix of returned URL
	 *
	 * @param serviceName admin name of the service
	 * @param namespacePart the namespace part
	 * @param majorVersion the major version
	 * @return the uRL pattern
	 */
	public String getURLPattern(String serviceName, String namespacePart,
			int majorVersion);

	/**
	 * The protocol processor class name for ClientConfig.xml.
	 * 
	 * @return protocol processor class name
	 */
	public String getSOAPProtocolProcessorClassName();

}
