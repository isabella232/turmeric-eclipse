/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.buildsystem;

import java.util.Collection;

import org.ebayopensource.turmeric.common.config.LibraryType;
import org.ebayopensource.turmeric.common.config.ReferredType;
import org.ebayopensource.turmeric.common.config.ReferredTypeLibraryType;
import org.ebayopensource.turmeric.common.config.TypeDependencyType;
import org.ebayopensource.turmeric.common.config.TypeLibraryDependencyType;
import org.ebayopensource.turmeric.eclipse.core.TurmericCoreActivator;
import org.ebayopensource.turmeric.eclipse.utils.plugin.WorkspaceUtil;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;

/**
 * Synchronize in-lined types in Wsdl/Xsd to type dependency xml and from type
 * dependencies.xml to project dependencies also. This can also be done
 * separately. Special cases like absence of type dependency xml are also
 * handled.
 * 
 * @author smathew
 */
public class TypeLibSynhcronizer {



	/**
	 * Updates the version entry in the type dependency file for the selected
	 * types.
	 * 
	 * The referred type is found using the type name. Then updates the version
	 * of all the types referred from this type. Only modifies the type
	 * dependencies file.
	 *
	 * @param typeName -
	 * the name of the parent type. i.e the schema file in which the
	 * selected types are referred from.
	 * @param selectedTypes -
	 * the types referred from first parameter .
	 * @param project -
	 * the container project of the first parameter xsd file.
	 * @throws Exception the exception
	 */
	public static void updateVersionEntryTypeDep(String typeName,
			Collection<LibraryType> selectedTypes, IProject project)
			throws Exception {
		IFile typeDepFile = TurmericCoreActivator.getDependencyFile(project);
		TypeLibraryDependencyType typeLibraryDependencyType = TypeDepMarshaller
				.unmarshallIt(typeDepFile);
		TypeDependencyType typeDependencyType = TypeDepMarshaller.getTypeEntry(
				typeLibraryDependencyType, typeName);
		boolean marshallReqd = false;
		// changing the version for selected types
		for (LibraryType selType : selectedTypes) {
			marshallReqd = true;
			ReferredTypeLibraryType refTypeLib = TypeDepMarshaller
					.getReferredTypeLibrary(typeDependencyType, selType
							.getLibraryInfo().getLibraryName());
			ReferredType refType = TypeDepMarshaller
					.getReferredType(refTypeLib, selType
							.getName());
			refType.setVersion(selType.getVersion());
		}
		if (marshallReqd) {
			TypeDepMarshaller
					.marshallIt(typeLibraryDependencyType, typeDepFile);
		}
		WorkspaceUtil.refresh(typeDepFile);

	}
}
