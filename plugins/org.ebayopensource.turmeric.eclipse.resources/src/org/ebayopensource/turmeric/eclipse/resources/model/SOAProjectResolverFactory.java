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
package org.ebayopensource.turmeric.eclipse.resources.model;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.ebayopensource.turmeric.eclipse.core.logging.SOALogger;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;


/**
 * A factory for creating SOAProjectResolver objects.
 *
 * @author yayu
 */
public final class SOAProjectResolverFactory {
	/**
	 * External Project Resolver Id.
	 */
	public static final String EXT_PROJECT_RESOLVER_ID = "org.ebayopensource.turmeric.eclipse.resources.soaProjectResolver";
	
	/**
	 * Project Nature ID.
	 */
	public static final String ID_PROJECT_NATURE = "projectNature";
	
	/**
	 * Project Resolver Class.
	 */
	public static final String ID_RESOLVER_CLASS = "projectResolverClass";
	private static final Map<String, ISOAProjectResolver<?>> SOA_PROJECT_RESOLVERS;
	
	static {
		SOA_PROJECT_RESOLVERS = Collections.unmodifiableMap(loadProjectResolvers());
	}
	
	/**
	 * 
	 */
	private SOAProjectResolverFactory() {
		super();
	}
	
	/**
	 * Gets the sOA project resolver.
	 *
	 * @param projectNature project nature
	 * @return an ISOAProjectResolver
	 */
	public static ISOAProjectResolver<?> getSOAProjectResolver(String projectNature) {
		return SOA_PROJECT_RESOLVERS.get(projectNature);
	}

	private static Map<String, ISOAProjectResolver<?>> loadProjectResolvers() {
		final Map<String, ISOAProjectResolver<?>> result = new ConcurrentHashMap<String, ISOAProjectResolver<?>>();
		final IExtensionRegistry registry = Platform.getExtensionRegistry();
		IExtensionPoint extensionPoint = registry
		.getExtensionPoint(EXT_PROJECT_RESOLVER_ID);
		if (extensionPoint != null) {
			IExtension[] extensions = extensionPoint.getExtensions();

			for (final IExtension extension : extensions) {
				for (final IConfigurationElement element : extension
						.getConfigurationElements()) {
					try {
						final String projectNature = element.getAttribute(ID_PROJECT_NATURE);
						final ISOAProjectResolver<?> resolver = (ISOAProjectResolver<?>) 
						element.createExecutableExtension(ID_RESOLVER_CLASS);
						result.put(projectNature, resolver);
					} catch (CoreException e) {
						SOALogger.getLogger().warning(e);
					}
				}
			}
		}
		return result;
	}

}
