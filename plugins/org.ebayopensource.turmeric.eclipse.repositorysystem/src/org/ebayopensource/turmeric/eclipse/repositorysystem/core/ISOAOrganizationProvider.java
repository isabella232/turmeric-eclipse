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

import org.ebayopensource.turmeric.eclipse.resources.constants.SOAProjectConstants.SOAFrameworkLibrary;
import org.ebayopensource.turmeric.eclipse.resources.constants.SOAProjectConstants.SupportedProjectType;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;


/**
 * Represents the details for the underlying organization 
 * @author yayu
 * @since 1.0.0
 */
public interface ISOAOrganizationProvider {
	
	/**
	 * The name of the underlying organization
	 * @return
	 */
	public String getName();
	
	/**
	 * Whether support the functional domain
	 * @return
	 */
	public boolean supportFunctionalDomain();
	
	/**
	 * Whether the underlying organization support integration 
	 * with Asset Repository
	 * @return
	 */
	public boolean supportAssetRepositoryIntegration();
	
	/**Whether the underlying organization support integration 
	 * with Assertion Service
	 * @return 
	 */
	public boolean supportAssertionServiceIntegration() throws CoreException;
	
	/**
	 * Check whether to show the given namespace in the NamespaceToPackage viewer.
	 * @param namespace
	 * @return
	 */
	public boolean shouldShowInNamespaceToPackageViewer(String namespace);
	
	/**
	 * Generate the WSDL target namespace
	 * @param domainName
	 * @param namespacePart
	 * @param serviceVersion
	 * @return
	 * @since 1.0.0
	 */
	public String generateServiceNamespace(String domainName, 
			String namespacePart, String serviceVersion);
	
	/**
	 * @param packageName user could use null for using the default package name for the underlying organization
	 * @param adminName
	 * @return
	 * @since 1.0.0
	 */
	public String generateInterfaceClassName(String packageName, String adminName);
	
	/**
	 * parse the given targetNamesapce to get the namesapce-part out of it if 
	 * the namespace is following the Marketplace format. Otherwise return empty string.
	 * @param targetNamespace
	 * @return
	 * @since 1.0.0
	 */
	public String getNamespacePartFromTargetNamespace(String targetNamespace);
	
	/**
	 * @param domainName
	 * @param namespacePart
	 * @param version
	 * @return
	 * @since 1.0.0
	 */
	public String generateTypeLibraryTargetNamespace(
			String domainName, String namespacePart, String version);
	
	/**
	 * @param projectType
	 * @return
	 */
	public List<String> getDefaultDependencies(SupportedProjectType projectType);
	
	/**
	 * Return names of the error libraries used for runtime
	 * 
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
	 * @return the minimum required Turmeric framework version
	 */
	public String getMinimumRequiredTurmericFrameworkVersion();
	
	/**
	 * @param groupId
	 * @param artifactId
	 * @return the preferred version for the given artifact or null if do not have a preferred version
	 */
	public String getPreferredArtifactVersion(String groupId, String artifactId);
	
	/**
	 * Prevalidate the new service version with the validator of the underlying organization
	 * @param oldVersion
	 * @param newVersion
	 * @param serviceName
	 * @return
	 * @throws Exception
	 */
	public IStatus preValidateChangeServiceVersion(String oldVersion,
			String newVersion, String serviceName) throws Exception;

}
