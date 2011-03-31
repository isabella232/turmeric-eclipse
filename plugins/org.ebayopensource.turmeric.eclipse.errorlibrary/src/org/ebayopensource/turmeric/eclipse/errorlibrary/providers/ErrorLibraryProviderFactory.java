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
package org.ebayopensource.turmeric.eclipse.errorlibrary.providers;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.ebayopensource.turmeric.eclipse.core.logging.SOALogger;
import org.ebayopensource.turmeric.eclipse.errorlibrary.ErrorLibraryActivator;
import org.ebayopensource.turmeric.eclipse.errorlibrary.resources.SOAMessages;
import org.ebayopensource.turmeric.eclipse.exception.resources.SOAGetErrorLibraryProviderFailedException;
import org.ebayopensource.turmeric.eclipse.utils.collections.ListUtil;
import org.ebayopensource.turmeric.eclipse.utils.ui.UIUtil;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.dialogs.ListDialog;


/**
 * @author yayu
 * @since 1.0.0
 */
public final class ErrorLibraryProviderFactory {

	private static String preferredProviderID = null;
	private static Map<String, IErrorLibraryProvider> errorProviders = 
		new LinkedHashMap<String, IErrorLibraryProvider>();
	private static final SOALogger logger = SOALogger.getLogger();

	public static final String PROP_KEY_PROVIDER_ID = "providerID";
	public static final String PROP_KEY_PROVIDER_IMPLEMENTATION = "providerImplementation";

	/**
	 * 
	 */
	private ErrorLibraryProviderFactory() {
		super();
	}
	
	public static List<IErrorLibraryProvider> getErrorLibraryProviders() 
	throws SOAGetErrorLibraryProviderFailedException{
		if (errorProviders.isEmpty() == true) {
			try {
				final IExtensionRegistry registry = Platform.getExtensionRegistry();
				final IExtensionPoint extensionPoint = registry
						.getExtensionPoint(ErrorLibraryActivator.PLUGIN_ID
								+ ".soaErrorLibProvider");

				if (extensionPoint != null) {
					IExtension[] extensions = extensionPoint.getExtensions();
					final Map<String, IConfigurationElement> providers = new LinkedHashMap<String, IConfigurationElement>();
					for (final IExtension extension : extensions) {
						for (final IConfigurationElement element : extension
								.getConfigurationElements()) {
							final String providerID = element
									.getAttribute(PROP_KEY_PROVIDER_ID);
							
							providers.put(providerID, element);
							errorProviders.put(providerID, (IErrorLibraryProvider)
									element.createExecutableExtension(PROP_KEY_PROVIDER_IMPLEMENTATION));
						}
					}
				}
			} catch (Exception e) {
				throw new SOAGetErrorLibraryProviderFailedException(
						SOAMessages.CONTENT_PROVIDER_NOT_FOUND, e);
			}
		}
		return ListUtil.arrayList(errorProviders.values());
	}
	
	public static void setPreferredErrorLibraryProviderId(String providerId) {
		preferredProviderID = providerId;
	}

	/**
	 * @return The preferred error library content provider
	 */
	public static IErrorLibraryProvider getPreferredProvider()
			throws SOAGetErrorLibraryProviderFailedException {
		IErrorLibraryProvider preferredProvider = null;
		if (preferredProviderID == null) {
			try {
				final List<IErrorLibraryProvider> providers = getErrorLibraryProviders();
				if (providers.size() == 1) {
					preferredProviderID = providers.get(0).getProviderID();
					logger.info(SOAMessages.PREF_CONTENT_PROVIDER, preferredProviderID);
				} else {
					IStructuredContentProvider contentProvider = new IStructuredContentProvider() {

						public Object[] getElements(Object inputElement) {
							try {
								return getErrorLibraryProviders().toArray();
							} catch (SOAGetErrorLibraryProviderFailedException e) {
								return Collections.EMPTY_LIST.toArray();
							}
						}

						public void dispose() {
						}

						public void inputChanged(Viewer viewer, Object oldInput,
								Object newInput) {
						}

					};
					ILabelProvider labelProvider = new ILabelProvider() {

						public Image getImage(Object element) {
							return null;
						}

						public String getText(Object element) {
							if (element instanceof IErrorLibraryProvider) {
								return ((IErrorLibraryProvider)element).getProviderID();
							}
							return "";
						}

						public void addListener(ILabelProviderListener listener) {

						}

						public void dispose() {

						}

						public boolean isLabelProperty(Object element,
								String property) {
							return false;
						}

						public void removeListener(ILabelProviderListener listener) {

						}

					};
					ListDialog dialog = new ListDialog(
							UIUtil.getActiveShell());
					dialog.setContentProvider(contentProvider);
					dialog.setLabelProvider(labelProvider);
					dialog.setInput(new Object());
					dialog.setMessage("Please select a preferred error library provider to use, " +
					"\nthe selected provider will be used as the default provider.");

					if (dialog.open() == Window.OK) {
						Object[] objects = dialog.getResult();
						if (objects != null && objects.length > 0) {
							preferredProviderID = ((IErrorLibraryProvider)objects[0]).getProviderID();
							logger.info(SOAMessages.PREF_CONTENT_PROVIDER, preferredProviderID);
						}

					}
				}
			} catch (Exception e) {
				throw new SOAGetErrorLibraryProviderFailedException(
						SOAMessages.CONTENT_PROVIDER_NOT_FOUND, e);
			}
		}
		
		if (errorProviders.isEmpty() == true) {
			getErrorLibraryProviders();
		}
		preferredProvider = errorProviders.get(preferredProviderID);
		
		if (preferredProvider == null) {
			// no error providers found. bad bad. throw an exception
			throw new SOAGetErrorLibraryProviderFailedException(
					SOAMessages.CONTENT_PROVIDER_NOT_FOUND);

		}
		
		return preferredProvider;
	}

}
