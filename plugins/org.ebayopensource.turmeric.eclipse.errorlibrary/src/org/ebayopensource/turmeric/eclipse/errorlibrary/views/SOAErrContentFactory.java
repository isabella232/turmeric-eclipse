/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.errorlibrary.views;

import java.util.LinkedHashMap;
import java.util.Map;

import org.ebayopensource.turmeric.eclipse.config.core.SOAGlobalConfigAccessor;
import org.ebayopensource.turmeric.eclipse.core.logging.SOALogger;
import org.ebayopensource.turmeric.eclipse.errorlibrary.ErrorLibraryActivator;
import org.ebayopensource.turmeric.eclipse.errorlibrary.resources.SOAMessages;
import org.ebayopensource.turmeric.eclipse.exception.resources.SOAGetErrorLibraryProviderFailedException;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.GlobalRepositorySystem;
import org.ebayopensource.turmeric.eclipse.utils.lang.StringUtil;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;


/**
 * @author smathew
 * 
 */
public class SOAErrContentFactory {
	private static ISOAErrRegistry soaErrRegistry;
	private static final SOALogger logger = SOALogger.getLogger();
	public static final String PROP_KEY_PROVIDER_ID = "providerID";
	public static final String PROP_KEY_MODEL_PROVIDER = "errViewModelProvider";

	private SOAErrContentFactory() {

	}

	private static ISOAErrRegistry getPreferredProvider()
			throws SOAGetErrorLibraryProviderFailedException {
		if (soaErrRegistry != null)
			return soaErrRegistry;

		try {
			final IExtensionRegistry registry = Platform.getExtensionRegistry();
			final IExtensionPoint extensionPoint = registry
					.getExtensionPoint(ErrorLibraryActivator.PLUGIN_ID
							+ ".soaErrorLibModelProvider");
			String buildSystem = GlobalRepositorySystem.instanceOf().getActiveRepositorySystem().getId();
			String organization = GlobalRepositorySystem.instanceOf().getActiveRepositorySystem()
			.getActiveOrganizationProvider().getName();
			final String preferredProviderID = SOAGlobalConfigAccessor
					.getPreferredErrorLibraryContentProvider(buildSystem, organization);
			logger.info(SOAMessages.PREF_CONTENT_PROVIDER, preferredProviderID);

			if (extensionPoint != null) {
				IExtension[] extensions = extensionPoint.getExtensions();
				final Map<String, IConfigurationElement> providers = new LinkedHashMap<String, IConfigurationElement>();
				Outer: for (final IExtension extension : extensions) {
					for (final IConfigurationElement element : extension
							.getConfigurationElements()) {
						final String providerID = element
								.getAttribute(PROP_KEY_PROVIDER_ID);
						if (providerID.equals(preferredProviderID)) {
							// found the preferred provider
							soaErrRegistry = (ISOAErrRegistry) element
									.createExecutableExtension(PROP_KEY_MODEL_PROVIDER);
							break Outer;
						}
						providers.put(providerID, element);
					}
				}

				if (providers.isEmpty() == false && soaErrRegistry == null) {
					// could NOT find the preferred provider
					final String providerName = providers.keySet().iterator()
							.next();
					logger.warning(StringUtil.formatString(
							SOAMessages.PREF_CONTENT_PROVIDER_NOT_FOUND,
							providerName));
					soaErrRegistry = (ISOAErrRegistry) providers.get(
							providerName).createExecutableExtension(
							PROP_KEY_MODEL_PROVIDER);
				}
				if (soaErrRegistry == null) {
					// no error providers found. bad bad. throw an exception
					throw new SOAGetErrorLibraryProviderFailedException(
							SOAMessages.CONTENT_PROVIDER_NOT_FOUND);

				}

			}
		} catch (Exception e) {
			throw new SOAGetErrorLibraryProviderFailedException(
					SOAMessages.CONTENT_PROVIDER_NOT_FOUND, e);
		}
		return soaErrRegistry;
	}

	public static ISOAErrRegistry getProvider()
			throws SOAGetErrorLibraryProviderFailedException {
		return getPreferredProvider();
	}

	/**
	 * This should invalidate the current registry object. Implementors should
	 * expect a call to getLibraries after this call.
	 * 
	 * @return
	 * @throws Exception 
	 */
	public static void invalidateProvider() throws Exception {
		//soaErrRegistry = null;
		if (soaErrRegistry != null) {
			soaErrRegistry.refreshRegistry();
		}
	}
}
