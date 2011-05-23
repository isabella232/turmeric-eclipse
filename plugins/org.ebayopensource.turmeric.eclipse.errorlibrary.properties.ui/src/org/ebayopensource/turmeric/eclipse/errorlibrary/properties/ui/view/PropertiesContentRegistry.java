/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.errorlibrary.properties.ui.view;

import java.util.Collection;
import java.util.Collections;

import org.ebayopensource.turmeric.eclipse.core.logging.SOALogger;
import org.ebayopensource.turmeric.eclipse.errorlibrary.properties.registry.TurmericErrorRegistry;
import org.ebayopensource.turmeric.eclipse.errorlibrary.views.ISOAErrLibrary;
import org.ebayopensource.turmeric.eclipse.errorlibrary.views.ISOAErrRegistry;


/**
 * The Class PropertiesContentRegistry.
 *
 * @author yayu
 */
public class PropertiesContentRegistry implements ISOAErrRegistry {
	private static final SOALogger logger = SOALogger.getLogger();
	

	/**
	 * Instantiates a new properties content registry.
	 */
	public PropertiesContentRegistry() {
		super();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Collection<ISOAErrLibrary> getLibraries() {
		try {
			return TurmericErrorRegistry.getErrorLibraries();
		} catch (Exception e) {
			logger.error(e);
		}
		return Collections.emptySet();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean importError() {
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void refreshRegistry() throws Exception {
		TurmericErrorRegistry.refresh();
		
	}

}
