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
 * @author smathew
 * 
 */
public interface ITypeRegistryBridge {

	/**
	 * @return all instances of type libraries in the underlying environment including all versions
	 * @throws Exception
	 */
	public Set<AssetInfo> getAllTypeLibraries() throws Exception;
	
	/**
	 * @return all instances of latest versions of type libraries in the underlying environment
	 * @throws Exception
	 */
	public List<AssetInfo> getAllLatestTypeLibraries() throws Exception;

	/**
	 * Creating a new type library project
	 * @param soaBaseProject
	 * @return
	 * @throws Exception
	 */
	public boolean createTypeLibrary(SOABaseProject soaBaseProject) throws Exception;

	/**
	 * Deleting an existing type library project
	 * @param typeLibName
	 * @return
	 * @throws Exception
	 */
	public boolean deleteTypeLibrary(String typeLibName) throws Exception;

	/**
	 * @param typeLibName
	 * @return true if the given type library name already exist or false otherwise
	 * @throws Exception
	 */
	public boolean typeLibraryExists(String typeLibName) throws Exception;
	
	/**
	 * @return the list of type library names that are required
	 */
	public Collection<String> getRequiredLibrariesForTypeLibraryProject();
	
	/**
	 * @return the name of the WSDL type in TypeDependencies.xml
	 */
	public String getTypeDependencyWsdlTypeName();
	
	/**
	 * 
	 * @return
	 */
	public List<Bundle> getPluginBundles();
	
	/**
	 * 
	 * @return
	 */
	public SOATypeRegistry getSOATypeRegistry();
	
	public void processTypeDepXMLFile(String libraryName) throws Exception;
	
	
	public TypeLibraryDependencyType createTypeLibraryDependencyTypeInstance();
	
	public LibraryType createLibraryTypeInstance();
	
	public TypeDependencyType createTypeDependencyTypeInstance();
	
	public ReferredType createReferredTypeInstance();
	
	public ReferredTypeLibraryType createReferredTypeLibraryTypeInstance();
	
	public TypeLibraryDependencyType unmarshalTypeLibraryDependencyType(InputStream inputStream);
	
	public TypeLibraryType unmarshalTypeInformationType(InputStream inputStream);
	
	public void marshalTypeLibraryDependencyType(TypeLibraryDependencyType type, File xmlFile);

}
