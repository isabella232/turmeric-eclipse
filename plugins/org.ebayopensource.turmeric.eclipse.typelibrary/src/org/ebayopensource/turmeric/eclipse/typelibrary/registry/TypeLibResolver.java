/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.typelibrary.registry;

import org.apache.commons.lang.StringUtils;
import org.ebayopensource.turmeric.eclipse.buildsystem.core.SOAGlobalRegistryAdapter;
import org.ebayopensource.turmeric.eclipse.logging.SOALogger;
import org.ebayopensource.turmeric.eclipse.typelibrary.buildsystem.TypeDepMarshaller;
import org.ebayopensource.turmeric.eclipse.typelibrary.core.SOATypeLibraryConstants;
import org.ebayopensource.turmeric.eclipse.typelibrary.utils.TypeLibraryUtil;
import org.eclipse.core.resources.IFile;
import org.eclipse.wst.common.uriresolver.internal.provisional.URIResolverExtension;

import org.ebayopensource.turmeric.common.config.LibraryType;
import org.ebayopensource.turmeric.common.config.ReferredTypeLibraryType;
import org.ebayopensource.turmeric.common.config.TypeDependencyType;
import org.ebayopensource.turmeric.common.config.TypeLibraryDependencyType;

/**
 * @author smathew
 * 
 */
public class TypeLibResolver implements URIResolverExtension {

	public String resolve(IFile file, String baseLocation, String publicId,
			String systemId) {
		if (isEbayProtocol(systemId)) {
			try {

				String includedTypeName = TypeLibraryUtil
						.getTypeNameFromProtocolString(systemId);

				String parentProjectName = TypeLibraryUtil
						.getProjectNameFromWTPBaseLocation(baseLocation);

				String parentTypeName = TypeLibraryUtil
						.getTypeNameFromWTPBaseLocation(baseLocation);

				String includedLibraryName = TypeLibraryUtil
						.getLibraryNameFromProtocolString(systemId);
				// Empty means old style protocol
				if (StringUtils.isEmpty(includedLibraryName)) {
					TypeLibraryDependencyType typeLibraryDependencyType = TypeDepMarshaller
							.unmarshallIt(TypeLibraryUtil.getDependencyStream(
									parentProjectName, baseLocation));
					TypeDependencyType typeDependencyType = TypeDepMarshaller
							.getTypeEntry(typeLibraryDependencyType,
									parentTypeName);
					ReferredTypeLibraryType referredTypeLibraryType = TypeDepMarshaller
							.getReferredParentTypeLibrary(typeDependencyType,
									includedTypeName);

					includedLibraryName = referredTypeLibraryType.getName();
					if (StringUtils.isEmpty(includedLibraryName)) {
						LibraryType libType = null;
						try {
							libType = SOAGlobalRegistryAdapter.getInstance()
									.getGlobalRegistry().getType(
											includedTypeName);
						} catch (Exception exception) {
							// Silently ignore, because this could happen
						}

						if (libType != null && libType.getLibraryInfo() != null) {
							includedLibraryName = libType.getLibraryInfo()
									.getLibraryName();
						}
					}
				}
				return TypeLibraryUtil.getXSD(includedLibraryName,
						includedTypeName).toString();

				// if (!StringUtils.isEmpty(jarLocation)
				// && jarLocation.endsWith(SOAProjectConstants.JAR_EXT)) {
				// String xsdName = TypeLibraryUtil
				// .getXsdFileNameFromTypeName(includedTypeName);
				// URL jarURL = IOUtil.toURL(jarLocation);
				// String jarEntryPath = "";
				// if (TypeLibraryUtil.isNewTypLibrary(jarURL, libraryName)) {
				// jarEntryPath = SOATypeLibraryConstants.TYPES_LOCATION_IN_JAR
				// + WorkspaceUtil.PATH_SEPERATOR
				// + libraryName
				// + xsdName;
				// } else {
				// jarEntryPath = SOATypeLibraryConstants.TYPES_LOCATION_IN_JAR
				// + WorkspaceUtil.PATH_SEPERATOR + xsdName;
				// }
				// return IOUtil.getNonLockingURL(jarURL, jarEntryPath)
				// .toString();
				// } else {
				// IProject project = WorkspaceUtil.getProject(libraryName);
				// if (project.isAccessible()) {
				// IFile localFile = WorkspaceUtil.getProject(libraryName)
				// .getFile(
				// TypeLibraryUtil.getXsdFileLocation(
				// includedTypeName, project));
				// return localFile.getLocation().toFile().toURL()
				// .toString();
				// }
				// return null;
				// }
			} catch (Exception e) {
				SOALogger.getLogger().error(e);
			}
		}
		return null;
	}

	public boolean isEbayProtocol(String systemId) {
		if (!StringUtils.isEmpty(systemId)) {
			return systemId
					.startsWith(SOATypeLibraryConstants.EBAY_XSD_FILE_PROTOCOL);
		}
		return false;
	}

}
