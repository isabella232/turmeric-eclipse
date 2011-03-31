/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.typelibrary.resolvers;

import org.apache.commons.lang.StringUtils;
import org.ebayopensource.turmeric.eclipse.core.logging.SOALogger;
import org.ebayopensource.turmeric.eclipse.core.resources.constants.SOATypeLibraryConstants;
import org.ebayopensource.turmeric.eclipse.typelibrary.buildsystem.TypeDepMarshaller;
import org.ebayopensource.turmeric.eclipse.typelibrary.utils.TypeLibraryUtil;
import org.ebayopensource.turmeric.eclipse.ui.monitor.typelib.SOAGlobalRegistryAdapter;
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

			} catch (Exception e) {
				SOALogger.getLogger().error(e);
			}
		}
		return null;
	}

	public boolean isEbayProtocol(String systemId) {
		if (!StringUtils.isEmpty(systemId)) {
			return systemId
					.startsWith(SOATypeLibraryConstants.TURMERIC_XSD_FILE_PROTOCOL);
		}
		return false;
	}

}
