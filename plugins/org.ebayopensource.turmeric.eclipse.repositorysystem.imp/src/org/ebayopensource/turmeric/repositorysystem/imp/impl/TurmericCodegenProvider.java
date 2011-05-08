/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.repositorysystem.imp.impl;

import java.util.ArrayList;

import org.ebayopensource.turmeric.eclipse.core.logging.SOALogger;
import org.ebayopensource.turmeric.eclipse.core.resources.constants.SOAProjectConstants;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.ISOACodegenProvider;
import org.ebayopensource.turmeric.eclipse.utils.classloader.SOAPluginClassLoader;
import org.ebayopensource.turmeric.tools.codegen.ServiceGenerator;
import org.osgi.framework.Bundle;

/**
 * The Class TurmericCodegenProvider.
 *
 * @author smatthew
 */
public class TurmericCodegenProvider implements ISOACodegenProvider {

	private static final SOALogger logger = SOALogger.getLogger();

	/**
	 * {@inheritDoc}
	 * 
	 */
	public boolean generateCode(String[] parameters) {
		ClassLoader classLoaderBasic = Thread.currentThread()
				.getContextClassLoader();
		if ((classLoaderBasic instanceof SOAPluginClassLoader) == false) {
			logger.error("Need to use SOAClassloader" + " in current thread!");
			return false;
		}
		SOAPluginClassLoader classLoader = (SOAPluginClassLoader) classLoaderBasic;
		ArrayList<Bundle> bundles = new ArrayList<Bundle>();
		bundles.add(org.ebayopensource.turmeric.eclipse.soatools.Activator
				.getDefault().getBundle());
		classLoader.setPluginBundles(bundles);

		try {
			ServiceGenerator serviceGenerator = new ServiceGenerator();
			serviceGenerator.startCodeGen(parameters);
		} catch (Exception e) {
			logger.error(e);
			return false;
		}
		return true;
	}

	/**
	 * {@inheritDoc}
	 * 
	 */
	public String getGenFolderForImpl() {
		return SOAProjectConstants.FOLDER_GEN_SRC_SERVICE;
	}

	/**
	 * {@inheritDoc}
	 * 
	 */
	public String getGenFolderForIntf() {
		return SOAProjectConstants.FOLDER_GEN_SRC_CLIENT;
	}

}
