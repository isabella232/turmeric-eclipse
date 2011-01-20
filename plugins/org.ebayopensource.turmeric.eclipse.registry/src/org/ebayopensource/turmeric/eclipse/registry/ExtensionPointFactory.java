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
package org.ebayopensource.turmeric.eclipse.registry;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.ebayopensource.turmeric.eclipse.registry.intf.IArtifactValidator;
import org.ebayopensource.turmeric.eclipse.registry.intf.IArtifactValidator2;
import org.ebayopensource.turmeric.eclipse.registry.intf.IClientRegistryProvider;
import org.ebayopensource.turmeric.eclipse.registry.intf.IRegistryProvider;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;


/**
 * @author yayu
 * @since 1.0.0
 */
public final class ExtensionPointFactory {
	private static IRegistryProvider registryProvider;
	private static Map<String, IArtifactValidator> validators;
	private static IClientRegistryProvider clientRegistryProvider;
	
	public static final String ID_REGISTRY_PROVIDER = 
		RegistryActivator.PLUGIN_ID + ".registryprovider";
	
	public static final String ID_ARTIFACT_VALIDATOR = 
		"org.ebayopensource.turmeric.eclipse.external.artifactvalidator";
	
	public static final String ID_CLIENT_REGISTRY_PROVIDER = 
		RegistryActivator.PLUGIN_ID + ".clientregistryprovider";
	
	/**
	 * 
	 */
	private ExtensionPointFactory() {
		super();
	}

	public static IRegistryProvider getSOARegistryProvider() throws CoreException{
		if (registryProvider == null) {
			final IExtensionRegistry registry = Platform.getExtensionRegistry();
			IExtensionPoint extensionPoint = registry
			.getExtensionPoint(ID_REGISTRY_PROVIDER);
			if (extensionPoint != null) {
				IExtension[] extensions = extensionPoint.getExtensions();
				Outer: for (final IExtension extension : extensions) {
					for (final IConfigurationElement element : extension
							.getConfigurationElements()) {
						registryProvider = (IRegistryProvider)
						element.createExecutableExtension("providerClass");
						break Outer;
					}
				}
			}
		}
		return registryProvider;
	}
	
	public static List<IArtifactValidator> getArtifactValidators() 
	throws CoreException {
		if (validators == null) {
			final IExtensionRegistry registry = Platform.getExtensionRegistry();
			IExtensionPoint extensionPoint = registry
			.getExtensionPoint(ID_ARTIFACT_VALIDATOR);
			validators = new LinkedHashMap<String, IArtifactValidator>();
			if (extensionPoint != null) {
				IExtension[] extensions = extensionPoint.getExtensions();
				for (final IExtension extension : extensions) {
					for (final IConfigurationElement element : extension
							.getConfigurationElements()) {
						final String name = element.getAttribute("validatorName");
						final IArtifactValidator validator = (IArtifactValidator)
						element.createExecutableExtension("validatorClass");
						validators.put(name, validator);
					}
				}
			}
		}
		return new ArrayList<IArtifactValidator>(
				validators.values());
	}
	
	/**
	 * @return true is we should run AS validation or false otherwise.
	 * @throws CoreException
	 */
	public static boolean isASForWSDLEnabled() throws CoreException {
		boolean result = false;
		for (IArtifactValidator validator : getArtifactValidators()) {
			if (validator instanceof IArtifactValidator2) {
				IArtifactValidator2 validator2 = (IArtifactValidator2) validator;
				if (validator2.isAssertionServiceEnabled() == true)
					return true;
			}
		}
		return result;
	}
	
	public static IClientRegistryProvider getSOAClientRegistryProvider() throws CoreException{
		if (clientRegistryProvider == null) {
			final IExtensionRegistry registry = Platform.getExtensionRegistry();
			IExtensionPoint extensionPoint = registry
			.getExtensionPoint(ID_CLIENT_REGISTRY_PROVIDER);
			if (extensionPoint != null) {
				IExtension[] extensions = extensionPoint.getExtensions();
				Outer: for (final IExtension extension : extensions) {
					for (final IConfigurationElement element : extension
							.getConfigurationElements()) {
						clientRegistryProvider = (IClientRegistryProvider)
						element.createExecutableExtension("providerClass");
						break Outer;
					}
				}
			}
		}
		return clientRegistryProvider;
	}
}
