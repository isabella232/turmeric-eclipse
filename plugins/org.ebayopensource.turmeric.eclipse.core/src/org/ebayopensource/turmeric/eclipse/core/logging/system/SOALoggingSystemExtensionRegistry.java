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
package org.ebayopensource.turmeric.eclipse.core.logging.system;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;


/**
 * The Class SOALoggingSystemExtensionRegistry.
 *
 * @author yayu
 */
public final class SOALoggingSystemExtensionRegistry {
	
	/** The Constant LOGGING_SYSTEM_ID_PROVIDER_EXTENSION_POINT_ID. */
	public static final String LOGGING_SYSTEM_ID_PROVIDER_EXTENSION_POINT_ID = "org.ebayopensource.turmeric.eclipse.loggingSystem";
	
	/** The Constant ELEM_SOA_LOGGING_SYSTEM. */
	public static final String ELEM_SOA_LOGGING_SYSTEM = "SOALoggingSystem";
	
	/** The Constant ATTR_LOGGING_SYSTEM_ID_PROVIDER. */
	public static final String ATTR_LOGGING_SYSTEM_ID_PROVIDER = "loggingSystemIDProvider";
	private static SOALoggingSystemExtensionRegistry instance;
	private static Map<String, ISOALoggingSystemProvider> providerMaps;
	
	
	/**
	 * Gets the single instance of SOALoggingSystemExtensionRegistry.
	 *
	 * @return single instance of SOALoggingSystemExtensionRegistry
	 */
	public static SOALoggingSystemExtensionRegistry getInstance() {
		if (instance == null) {
			instance = new SOALoggingSystemExtensionRegistry();
		}
		return instance;
	}
	
	/**
	 * 
	 */
	private SOALoggingSystemExtensionRegistry() {
		super();
	}
	
	/**
	 * Gets the logging system id provider.
	 *
	 * @param buildSystemID the build system id
	 * @return the logging system id provider
	 * @throws CoreException the core exception
	 */
	public ISOALoggingSystemProvider getLoggingSystemIDProvider(String buildSystemID) throws CoreException{
		if (providerMaps == null)
			init();
		return providerMaps.get(buildSystemID);
	}
	
	private void init() throws CoreException{
		IConfigurationElement[] elements =
			Platform.getExtensionRegistry().getConfigurationElementsFor(
					LOGGING_SYSTEM_ID_PROVIDER_EXTENSION_POINT_ID);
		
		if (elements != null) {
			providerMaps = new HashMap<String, ISOALoggingSystemProvider>(elements.length);
			for (IConfigurationElement element: elements) {
				//we only use the first registered logging system ID provider
				final String loggingSystemIDProviderClassName = element.getAttribute(ATTR_LOGGING_SYSTEM_ID_PROVIDER);
				if (loggingSystemIDProviderClassName == null || loggingSystemIDProviderClassName.length() == 0) {
					System.err.println( "Invalid logging system ID provider class name: " + String.valueOf(loggingSystemIDProviderClassName));
				}
				ISOALoggingSystemProvider idProvider = 
					(ISOALoggingSystemProvider)element
					                                      .createExecutableExtension(ATTR_LOGGING_SYSTEM_ID_PROVIDER);
				providerMaps.put(idProvider.getSystemID(), idProvider);
			}
		}
	}

}
