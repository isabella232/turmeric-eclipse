/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.errorlibrary.buildsystem.core;

import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;

import org.ebayopensource.turmeric.eclipse.exception.resources.SOAInvocationException;
import org.ebayopensource.turmeric.eclipse.logging.SOALogger;
import org.ebayopensource.turmeric.eclipse.resources.constants.SOAProjectConstants;
import org.ebayopensource.turmeric.tools.errorlibrary.ErrorIdGenerator;
import org.ebayopensource.turmeric.tools.errorlibrary.ErrorIdGeneratorFactory;
import org.ebayopensource.turmeric.tools.errorlibrary.ErrorIdServiceFactory;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;

/**
 * @author haozhou
 *
 */
public class SOAErrorIdGeneratorAdapter implements IRunnableWithProgress {
	private String storeLocation;
	private String organization;
	private String domain;
	private long errorId;
	
	public SOAErrorIdGeneratorAdapter(String storeLocation,
			String organizationName, String domain) {
		this.storeLocation = storeLocation;
		this.organization = organizationName;
		this.domain = domain;
	}

	/**
	 * @see org.eclipse.jface.operation.IRunnableWithProgress#run(org.eclipse.core.runtime.IProgressMonitor)
	 */
	@Override
	public void run(IProgressMonitor monitor) throws InvocationTargetException,
			InterruptedException {
		ErrorIdGenerator generator = null;
		if (storeLocation.startsWith(SOAProjectConstants.PROTOCOL_HTTP)) {
			generator = ErrorIdServiceFactory.getInstance()
					.getErrorIdGenerator(storeLocation, this.organization);
		} else {
			generator = ErrorIdGeneratorFactory.getErrorIdGenerator(
					storeLocation, this.organization);
		}
		try {
			this.errorId = generator.getNextId(domain);
			SOALogger.getLogger().log(Level.INFO, "Error Id generated: " + this.errorId);
		} catch (Exception e) {
			throw new SOAInvocationException(e);
		}
	}
	
	public long getErrorId() {
		return this.errorId;
	}

}
