/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.typelibrary.buildsystem;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.bind.JAXBException;
import javax.xml.namespace.QName;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.ebayopensource.turmeric.common.config.LibraryType;
import org.ebayopensource.turmeric.common.config.ReferredType;
import org.ebayopensource.turmeric.common.config.ReferredTypeLibraryType;
import org.ebayopensource.turmeric.common.config.TypeDependencyType;
import org.ebayopensource.turmeric.common.config.TypeLibraryDependencyType;
import org.ebayopensource.turmeric.common.config.TypeLibraryType;
import org.ebayopensource.turmeric.eclipse.buildsystem.core.SOAGlobalRegistryAdapter;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.GlobalRepositorySystem;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.ITypeRegistryBridge;
import org.ebayopensource.turmeric.eclipse.typelibrary.core.SOATypeLibraryConstants;
import org.ebayopensource.turmeric.eclipse.typelibrary.resources.model.SOATypeLibraryProject;
import org.ebayopensource.turmeric.eclipse.utils.plugin.WorkspaceUtil;
import org.ebayopensource.turmeric.tools.library.SOATypeRegistry;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;

/**
 * Marshaller and Unmarshaller for type dependency xml. Additionally this
 * contains helper method for modeling the xml. Parsing, Saving, Modifying
 * helper methods are provided here
 * 
 * @author smathew
 */
public class TypeDepMarshaller {

	/**
	 * Wrapper Method. Extract the project, library name and version from the
	 * parameter model and call the
	 * 
	 * @see {@link TypeDepMarshaller#createDefaultDepXml(IProject project,
	 *      String libraryName, String version, IProgressMonitor monitor)}
	 * 
	 * method.
	 * @param typeLibraryProject -
	 *            Extracts the project name, version etc from this object
	 * @param monitor
	 * @throws CoreException
	 * @throws JAXBException
	 */
	public static void createDefaultDepXml(
			SOATypeLibraryProject typeLibraryProject, IProgressMonitor monitor)
			throws CoreException, JAXBException {

		IProject project = typeLibraryProject.getEclipseMetadata().getProject();
		String libName = typeLibraryProject.getTypeLibraryMetadata().getName();
		String version = typeLibraryProject.getTypeLibraryMetadata()
				.getVersion();
		createDefaultDepXml(project, libName, version, monitor);
	}

	/**
	 * Wrapper Method. Library name is the project name itself. Version is taken
	 * from the constants file
	 * 
	 * @see {@link TypeDepMarshaller#createDefaultDepXml(IProject project,
	 *      String libraryName, String version, IProgressMonitor monitor)}
	 * 
	 * method.
	 * @param project
	 * @param monitor
	 * @throws CoreException
	 * @throws JAXBException
	 */
	public static void createDefaultDepXml(IProject project,
			IProgressMonitor monitor) throws CoreException, JAXBException {
		createDefaultDepXml(project, project.getName(),
				SOATypeLibraryConstants.DEFAULT_TYPE_DEP_VERSION, monitor);
	}

	private static void createDefaultDepXml(IProject project,
			String libraryName, String version, IProgressMonitor monitor)
			throws CoreException, JAXBException {
		// creates the empty file
		IFile file = WorkspaceUtil.createEmptyFile(project,
				SOATypeLibraryConstants.FOLDER_META_SRC_META_INF
						+ WorkspaceUtil.PATH_SEPERATOR + project.getName()
						+ WorkspaceUtil.PATH_SEPERATOR
						+ SOATypeLibraryConstants.FILE_TYPE_DEP_XML, monitor);
		WorkspaceUtil.refresh(file);
		
		ITypeRegistryBridge birdge = GlobalRepositorySystem.instanceOf()
				.getActiveRepositorySystem().getTypeRegistryBridge();
		TypeLibraryDependencyType typeLibraryDependencyType = birdge
				.createTypeLibraryDependencyTypeInstance();

		typeLibraryDependencyType.setLibraryName(libraryName);
		typeLibraryDependencyType.setVersion(version);
		marshallIt(typeLibraryDependencyType, file);
		WorkspaceUtil.refresh(file);
	}

	/**
	 * Finds the type entry under the parameter type library dependency type
	 * with the specified type name and pass it back. Returns null if not found.
	 * Remember typeLibraryDependencyType is the root tag in the xml. So this
	 * API returns the first level type in the xml. to be precise the first
	 * level types refers to some other types, They are the referree(the one who
	 * refers to)
	 * 
	 * @param typeLibraryDependencyType
	 * @param typeName
	 * @return
	 */
	public static TypeDependencyType getTypeEntry(
			TypeLibraryDependencyType typeLibraryDependencyType, String typeName) {
		for (TypeDependencyType type : typeLibraryDependencyType.getType()) {
			if (StringUtils.equals(typeName, type.getName())) {
				return type;
			}
		}
		return null;
	}

	/**
	 * Adds an type entry only if it does not exist. Its No operation otherwise.
	 * Returns true only if it is modified. Remember the return factor does NOT
	 * indicate the presence of this type.
	 * 
	 * @param typeLibraryDependencyType
	 * @param libraryType
	 * @return
	 */
	public static boolean addTypeEntryIfNotExists(
			TypeLibraryDependencyType typeLibraryDependencyType,
			LibraryType libraryType) {
		if (getTypeEntry(typeLibraryDependencyType, libraryType.getName()) == null) {
			addTypeEntry(libraryType, typeLibraryDependencyType);
			return true;
		}
		return false;
	}

	/**
	 * Modifies only the version(for now) of the type entry if it exists and the
	 * existing version is not the same. Its No operation otherwise. Returns
	 * true only if it there was a modification. Remember the return factor does
	 * NOT indicate the presence of this type.
	 * 
	 * @param typeLibraryDependencyType
	 * @param libraryType
	 * @return
	 */
	public static boolean modifyTypeEntry(
			TypeLibraryDependencyType typeLibraryDependencyType,
			LibraryType libraryType) {
		TypeDependencyType typeDependencyType = getTypeEntry(
				typeLibraryDependencyType, libraryType.getName());
		if (typeDependencyType != null
				&& !StringUtils.equals(libraryType.getVersion(),
						typeDependencyType.getVersion())) {
			typeDependencyType.setVersion(libraryType.getVersion());
			return true;
		}
		return false;
	}

	/**
	 * Removes the type from the TypeLibraryDependencyType. No operation
	 * otherwise. Returns true if there was a modification.
	 * 
	 * @param typeLibraryDependencyType
	 * @param typeName
	 * 
	 * @return
	 */
	public static boolean removeTypeEntryIfExists(
			TypeLibraryDependencyType typeLibraryDependencyType, String typeName) {
		TypeDependencyType typeDepToBeRemoved = getTypeEntry(
				typeLibraryDependencyType, typeName);
		if (typeDepToBeRemoved != null) {
			typeLibraryDependencyType.getType().remove(typeDepToBeRemoved);
			return true;
		}
		return false;
	}

	/**
	 * Finds the library entry under the parameter type type dependency type
	 * with the specified library name and pass it back. Returns null if not
	 * found. Note ReferredTypeLibraryType is the type tag under the root tag
	 * the xml. So this API returns the library referred by the
	 * typeDependencyType passed.
	 * 
	 * @param typeDependencyType
	 * @param libraryName
	 * @return
	 */
	public static ReferredTypeLibraryType getReferredTypeLibrary(
			TypeDependencyType typeDependencyType, String libraryName) {
		for (ReferredTypeLibraryType refType : typeDependencyType
				.getReferredTypeLibrary()) {
			if (StringUtils.equals(libraryName, refType.getName())) {
				return refType;
			}
		}
		return null;
	}

	/**
	 * Finds the type entry under the parameter type
	 * ReferredTypeLibraryType(library) with the specified type name and pass it
	 * back. Returns null if not found. Note ReferredTypeLibraryType is the
	 * library tag under the type tag the xml. So this API returns the type
	 * referred by referredTypeLibraryType among different types. One type might
	 * depend on many types from the same type library. This API returns the
	 * type with the specified name
	 * 
	 * @param referredTypeLibraryType
	 * @param typeName
	 * @return
	 */
	public static ReferredType getReferredType(
			ReferredTypeLibraryType referredTypeLibraryType, String typeName) {
		for (ReferredType referredType : referredTypeLibraryType
				.getReferredType()) {
			if (StringUtils.equals(typeName, referredType.getName())) {
				return referredType;
			}
		}
		return null;
	}

	/**
	 * Name says it all. Unmarshalls the file to a model java object.
	 * Additionally it refresh the file to make sure that it is in sync
	 * 
	 * @param file
	 * @return
	 * @throws JAXBException
	 * @throws CoreException
	 */
	public static TypeLibraryDependencyType unmarshallIt(IFile file)
			throws JAXBException, CoreException {
		WorkspaceUtil.refresh(file);
		InputStream inputStream = file.getContents();
		try {
			return unmarshallIt(inputStream);
		} finally {
			IOUtils.closeQuietly(inputStream);
		}

	}

	/**
	 * Name says it all. Unmarshalls the file to a model java object.
	 * Additionally it refresh the file to make sure that it is in sync
	 * 
	 * @param file
	 * @return
	 * @throws JAXBException
	 * @throws CoreException
	 */
	public static TypeLibraryDependencyType unmarshallIt(InputStream inputStream)
			throws JAXBException, CoreException {
		try {
			ITypeRegistryBridge birdge = GlobalRepositorySystem.instanceOf()
					.getActiveRepositorySystem().getTypeRegistryBridge();
			return birdge.unmarshalTypeLibraryDependencyType(inputStream);
		} finally {
			IOUtils.closeQuietly(inputStream);
		}
	}

	/**
	 * Marshalls the java model object back to a file. Write operation.
	 * Additionally does a refresh
	 * 
	 * @param typeLibraryDependencyType
	 * @param file
	 * @throws JAXBException
	 * @throws CoreException
	 */
	public static void marshallIt(
			TypeLibraryDependencyType typeLibraryDependencyType, IFile file)
			throws JAXBException, CoreException {

		ITypeRegistryBridge birdge = GlobalRepositorySystem.instanceOf()
				.getActiveRepositorySystem().getTypeRegistryBridge();
		birdge.marshalTypeLibraryDependencyType(typeLibraryDependencyType,  file.getLocation().toFile());
		WorkspaceUtil.refresh(file);
	}

	/**
	 * Finds all the types under the specified given TypeDependency Type. All
	 * means the types under all referred type libraries under the type
	 * dependency type.
	 * 
	 * @param typeDependencyType
	 * @return
	 * @throws Exception
	 */
	public static Set<QName> getAllReferredtypes(
			TypeDependencyType typeDependencyType) throws Exception {
		Set<QName> allReferredTypes = new HashSet<QName>();
		SOAGlobalRegistryAdapter registryAdapter = SOAGlobalRegistryAdapter.getInstance();
		SOATypeRegistry typeRegistry = registryAdapter.getGlobalRegistry();
		for (ReferredTypeLibraryType referredTypeLibraryType : typeDependencyType
				.getReferredTypeLibrary()) {
			TypeLibraryType typeLib = typeRegistry.getTypeLibrary(referredTypeLibraryType.getName());
			if (typeLib == null) {
				throw new RuntimeException("Could not find the referred type library '"
						+ referredTypeLibraryType.getName() 
						+ "' fromt the types registry. Please do a refresh in the Types Explorer view.");
			}
			String nameSpace = typeLib.getLibraryNamespace();
			for (ReferredType referredType : referredTypeLibraryType
					.getReferredType()) {
				allReferredTypes.add(new QName(nameSpace, referredType
						.getName()));
			}
		}
		return allReferredTypes;
	}

	/**
	 * Finds the library to which this type name belongs to. Basically it scans
	 * the whole type libraries and the types inside it and once it finds a
	 * matching name, it does not return the type but the library that contains
	 * the type
	 * 
	 * TODO:if (StringUtils.equalsIgnoreCase(typeName, referredType .getName()))
	 * has to be changed to honor the name space also. QName is the right option
	 * here
	 * 
	 * @param typeDependencyType
	 * @param typeName
	 * @return
	 */
	public static ReferredTypeLibraryType getReferredParentTypeLibrary(
			TypeDependencyType typeDependencyType, String typeName) {
		for (ReferredTypeLibraryType referredTypeLibraryType : typeDependencyType
				.getReferredTypeLibrary()) {
			for (ReferredType referredType : referredTypeLibraryType
					.getReferredType()) {
				if (StringUtils.equalsIgnoreCase(typeName, referredType
						.getName())) {
					return referredTypeLibraryType;
				}
			}
		}
		return null;
	}

	/**
	 * Removes all the referred type libraries from this type dependency type.
	 * Initially we thought we name it removeAllReferredTypeLibraries. But
	 * actually even if its removing the libraries, the ultimate effect is that
	 * all the types under it is also removed. So that why we have kept this
	 * name.
	 * 
	 * @param typeDependencyType
	 */
	public static void removeAllReferredTypes(
			TypeDependencyType typeDependencyType) {
		List<ReferredTypeLibraryType> allreferredTypeLibs = new ArrayList<ReferredTypeLibraryType>(
				typeDependencyType.getReferredTypeLibrary());
		typeDependencyType.getReferredTypeLibrary().removeAll(
				allreferredTypeLibs);
	}

	/**
	 * Adds all the new referred types to the TypeDependency Type. This API
	 * takes care of missing referredTypeLibrary also, Meaning this will add
	 * missing referred libraries also. The old refTypes is used for finding out
	 * the version. Basically we don't want to change the version here, we would
	 * like to keep the old version and thats why we need the old referred types
	 * 
	 * @param typeDependencyType
	 * @param newRefTypes
	 * @param oldRefTypes
	 */
	public static void addAllReferredTypes(
			TypeDependencyType typeDependencyType,
			Set<LibraryType> newRefTypes, Set<ReferredType> oldRefTypes) {
		for (LibraryType libraryType : newRefTypes) {
			addReferredTypeLibraryIfNotExists(libraryType, typeDependencyType);
			ReferredTypeLibraryType referredTypeLibraryType = getReferredTypeLibrary(
					typeDependencyType, libraryType.getLibraryInfo()
							.getLibraryName());
			addReferredTypeUnderTypeLibrary(libraryType,
					referredTypeLibraryType, oldRefTypes);
		}

	}

	/**
	 * Gets all referred type libraries in this type dependency xml. Basically
	 * it scans the xml finds all the types and go inside each type and find all
	 * the referred type library and returns it,
	 * 
	 * @param typeLibraryDependencyType
	 * @return
	 */
	public static Set<String> getAllReferredTypeLibraries(
			TypeLibraryDependencyType typeLibraryDependencyType) {
		Set<String> allReferredTypeLibraries = new HashSet<String>();

		for (TypeDependencyType type : typeLibraryDependencyType.getType()) {
			for (ReferredTypeLibraryType referredTypeLibraryType : type
					.getReferredTypeLibrary()) {
				allReferredTypeLibraries.add(referredTypeLibraryType.getName());
			}
		}
		return allReferredTypeLibraries;
	}

	/**
	 * Gets all referred types in this type dependency xml. Basically it scans
	 * the xml, finds all the types and go inside each type and find all the
	 * referred type library and goes inside each type library and finds all
	 * referred types and returns it,
	 * 
	 * @param typeLibraryDependencyType
	 * @return
	 * @throws Exception
	 */
	public static Set<QName> getAllReferredTypes(
			TypeLibraryDependencyType typeLibraryDependencyType)
			throws Exception {
		Set<QName> allReferredTypes = new HashSet<QName>();
		SOAGlobalRegistryAdapter registryAdapter = SOAGlobalRegistryAdapter.getInstance();
		SOATypeRegistry typeRegistry = registryAdapter.getGlobalRegistry();

		for (TypeDependencyType type : typeLibraryDependencyType.getType()) {
			for (ReferredTypeLibraryType referredTypeLibraryType : type
					.getReferredTypeLibrary()) {
				for (ReferredType referredType : referredTypeLibraryType
						.getReferredType()) {
					allReferredTypes.add(new QName(typeRegistry.getTypeLibrary(
									referredTypeLibraryType.getName())
							.getLibraryNamespace(), referredType.getName()));
				}
			}
		}
		return allReferredTypes;
	}

	/**
	 * Gets all referred types in this type dependency xml. Basically it scans
	 * the xml, finds all the types and go inside each type and find all the
	 * referred type library and goes inside each type library and finds all
	 * referred types and returns it,
	 * 
	 * @param typeLibraryDependencyType
	 * @return
	 */
	public static Set<ReferredType> getALLReferredTypes(
			TypeLibraryDependencyType typeLibraryDependencyType) {
		Set<ReferredType> allReferredTypes = new HashSet<ReferredType>();

		for (TypeDependencyType type : typeLibraryDependencyType.getType()) {
			for (ReferredTypeLibraryType referredTypeLibraryType : type
					.getReferredTypeLibrary()) {
				for (ReferredType referredType : referredTypeLibraryType
						.getReferredType()) {
					allReferredTypes.add(referredType);
				}
			}
		}
		return allReferredTypes;
	}

	private static void addTypeEntry(LibraryType libraryType,
			TypeLibraryDependencyType typeLibraryDependencyType) {
		TypeDependencyType typeDependencyType = GlobalRepositorySystem.instanceOf().getActiveRepositorySystem()
		.getTypeRegistryBridge().createTypeDependencyTypeInstance();
		typeDependencyType.setName(libraryType.getName());
		typeDependencyType.setVersion(libraryType.getVersion());
		typeLibraryDependencyType.getType().add(typeDependencyType);
	}

	private static void addReferredTypeLibrary(LibraryType addedType,
			TypeDependencyType typeDependencyType) {
		ReferredTypeLibraryType referredTypeLibraryType = GlobalRepositorySystem.instanceOf().getActiveRepositorySystem()
		.getTypeRegistryBridge().createReferredTypeLibraryTypeInstance();
		referredTypeLibraryType.setName(addedType.getLibraryInfo()
				.getLibraryName());
		referredTypeLibraryType.setVersion(addedType.getLibraryInfo()
				.getVersion());
		typeDependencyType.getReferredTypeLibrary()
				.add(referredTypeLibraryType);
	}

	private static boolean addReferredTypeLibraryIfNotExists(
			LibraryType addedType, TypeDependencyType typeDependencyType) {
		if (getReferredTypeLibrary(typeDependencyType, addedType
				.getLibraryInfo().getLibraryName()) == null) {
			addReferredTypeLibrary(addedType, typeDependencyType);
			return true;
		}
		return false;
	}

	private static void addReferredTypeUnderTypeLibrary(LibraryType addedType,
			ReferredTypeLibraryType referredTypeLibraryType,
			Set<ReferredType> oldReferredTypes) {
		ReferredType refType = GlobalRepositorySystem.instanceOf().getActiveRepositorySystem()
		.getTypeRegistryBridge().createReferredTypeInstance();
		refType.setName(addedType.getName());
		String version = addedType.getVersion();
		// TODO:QName is not honored here
		for (ReferredType referredType : oldReferredTypes) {
			if (StringUtils.equals(addedType.getName(), referredType.getName())) {
				version = referredType.getVersion();
				break;
			}
		}
		refType.setVersion(version);
		referredTypeLibraryType.getReferredType().add(refType);
	}
}
