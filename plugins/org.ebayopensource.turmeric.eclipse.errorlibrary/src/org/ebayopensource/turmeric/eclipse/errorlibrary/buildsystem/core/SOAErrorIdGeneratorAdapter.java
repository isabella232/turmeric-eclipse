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

import org.ebayopensource.turmeric.eclipse.core.logging.SOALogger;
import org.ebayopensource.turmeric.eclipse.exception.resources.SOAInvocationException;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.GlobalRepositorySystem;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;

/**
 * The Class SOAErrorIdGeneratorAdapter.
 *
 * @author haozhou
 */
public class SOAErrorIdGeneratorAdapter implements IRunnableWithProgress {
	private String storeLocation;
	private String organization;
	private String domain;
	private long errorId;
	
	/**
	 * Instantiates a new sOA error id generator adapter.
	 *
	 * @param storeLocation the store location
	 * @param organizationName the organization name
	 * @param domain the domain
	 */
	public SOAErrorIdGeneratorAdapter(String storeLocation,
			String organizationName, String domain) {
		this.storeLocation = storeLocation;
		this.organization = organizationName;
		this.domain = domain;
	}

	/**
	 * Run.
	 *
	 * @param monitor the monitor
	 * @throws InvocationTargetException the invocation target exception
	 * @throws InterruptedException the interrupted exception
	 * @see org.eclipse.jface.operation.IRunnableWithProgress#run(org.eclipse.core.runtime.IProgressMonitor)
	 */
	@Override
	public void run(IProgressMonitor monitor) throws InvocationTargetException,
			InterruptedException {
		try {
			this.errorId = GlobalRepositorySystem.instanceOf().getActiveRepositorySystem()
			.getErorRegistryBridge().nextErrorId(storeLocation, organization, domain);
			SOALogger.getLogger().info("Error Id generated: ", this.errorId);
		} catch (Exception e) {
			throw new SOAInvocationException(e);
		}
	}
	
	/**
	 * Gets the error id.
	 *
	 * @return the error id
	 */
	public long getErrorId() {
		return this.errorId;
	}

}
