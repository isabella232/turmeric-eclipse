/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.repositorysystem.core;

import java.io.File;
import java.io.InputStream;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.ebayopensource.turmeric.common.config.LibraryType;
import org.ebayopensource.turmeric.common.config.ReferredType;
import org.ebayopensource.turmeric.common.config.ReferredTypeLibraryType;
import org.ebayopensource.turmeric.common.config.TypeDependencyType;
import org.ebayopensource.turmeric.common.config.TypeLibraryDependencyType;
import org.ebayopensource.turmeric.common.config.TypeLibraryType;
import org.ebayopensource.turmeric.eclipse.resources.model.AssetInfo;
import org.ebayopensource.turmeric.eclipse.resources.model.SOABaseProject;
import org.ebayopensource.turmeric.tools.library.SOATypeRegistry;
import org.osgi.framework.Bundle;


/**
 * The Interface ITypeRegistryBridge.
 *
 * @author smathew
 */
public interface ITypeRegistryBridge {

	/**
	 * Gets the all type libraries.
	 *
	 * @return all instances of type libraries in the underlying environment including all versions
	 * @throws Exception the exception
	 */
	public Set<AssetInfo> getAllTypeLibraries() throws Exception;
	
	/**
	 * Gets the all latest type libraries.
	 *
	 * @return all instances of latest versions of type libraries in the underlying environment
	 * @throws Exception the exception
	 */
	public List<AssetInfo> getAllLatestTypeLibraries() throws Exception;

	/**
	 * Creating a new type library project.
	 *
	 * @param soaBaseProject the soa base project
	 * @return true, if successful
	 * @throws Exception the exception
	 */
	public boolean createTypeLibrary(SOABaseProject soaBaseProject) throws Exception;

	/**
	 * Deleting an existing type library project.
	 *
	 * @param typeLibName the type lib name
	 * @return true, if successful
	 * @throws Exception the exception
	 */
	public boolean deleteTypeLibrary(String typeLibName) throws Exception;

	/**
	 * Type library exists.
	 *
	 * @param typeLibName the type lib name
	 * @return true if the given type library name already exist or false otherwise
	 * @throws Exception the exception
	 */
	public boolean typeLibraryExists(String typeLibName) throws Exception;
	
	/**
	 * Gets the required libraries for type library project.
	 *
	 * @return the list of type library names that are required
	 */
	public Collection<String> getRequiredLibrariesForTypeLibraryProject();
	
	/**
	 * Gets the type dependency wsdl type name.
	 *
	 * @return the name of the WSDL type in TypeDependencies.xml
	 */
	public String getTypeDependencyWsdlTypeName();
	
	/**
	 * Gets the plugin bundles.
	 *
	 * @return the plugin bundles
	 */
	public List<Bundle> getPluginBundles();
	
	/**
	 * Gets the sOA type registry.
	 *
	 * @return the sOA type registry
	 */
	public SOATypeRegistry getSOATypeRegistry();
	
	/**
	 * Process type dep xml file.
	 *
	 * @param libraryName the library name
	 * @throws Exception the exception
	 */
	public void processTypeDepXMLFile(String libraryName) throws Exception;
	
	
	/**
	 * Creates the type library dependency type instance.
	 *
	 * @return the type library dependency type
	 */
	public TypeLibraryDependencyType createTypeLibraryDependencyTypeInstance();
	
	/**
	 * Creates the library type instance.
	 *
	 * @return the library type
	 */
	public LibraryType createLibraryTypeInstance();
	
	/**
	 * Creates the type dependency type instance.
	 *
	 * @return the type dependency type
	 */
	public TypeDependencyType createTypeDependencyTypeInstance();
	
	/**
	 * Creates the referred type instance.
	 *
	 * @return the referred type
	 */
	public ReferredType createReferredTypeInstance();
	
	/**
	 * Creates the referred type library type instance.
	 *
	 * @return the referred type library type
	 */
	public ReferredTypeLibraryType createReferredTypeLibraryTypeInstance();
	
	/**
	 * Unmarshal type library dependency type.
	 *
	 * @param inputStream the input stream
	 * @return the type library dependency type
	 */
	public TypeLibraryDependencyType unmarshalTypeLibraryDependencyType(InputStream inputStream);
	
	/**
	 * Unmarshal type information type.
	 *
	 * @param inputStream the input stream
	 * @return the type library type
	 */
	public TypeLibraryType unmarshalTypeInformationType(InputStream inputStream);
	
	/**
	 * Marshal type library dependency type.
	 *
	 * @param type the type
	 * @param xmlFile the xml file
	 */
	public void marshalTypeLibraryDependencyType(TypeLibraryDependencyType type, File xmlFile);

}
