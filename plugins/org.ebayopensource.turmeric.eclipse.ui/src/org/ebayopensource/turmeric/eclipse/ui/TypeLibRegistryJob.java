/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.ui;

import org.ebayopensource.turmeric.eclipse.core.logging.SOALogger;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.SOAGlobalRegistryAdapter;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;

/**
 * A job that runs at startup to initialize the type lib registry.
 * 
 * This is needed for both the Type Library project and the Service Projects in legacy mode.
 * 
 * The Type Library View needs this information as well.
 *  
 * @author dcarver
 *
 */
public class TypeLibRegistryJob extends Job {

	/**
	 * Constructor.
	 * 
	 * @param name job name
	 */
	public TypeLibRegistryJob(String name) {
		super(name);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.core.runtime.jobs.Job#run(org.eclipse.core.runtime.IProgressMonitor)
	 */
	@Override
	protected IStatus run(IProgressMonitor monitor) {
		monitor.beginTask("Refreshing Registry", 100);
		SOAGlobalRegistryAdapter registryAdapter = SOAGlobalRegistryAdapter.getInstance();
		registryAdapter.invalidateRegistry();
		monitor.worked(50);
		
		if (monitor.isCanceled()) {
			return cancelJob(monitor);
		}
		
		refreshRegistry(monitor, registryAdapter);
		
		if (monitor.isCanceled()) {
			return cancelJob(monitor);
		}
		
		return Status.OK_STATUS;
	}
	
	/**
	 * Refreshes the registry after it has been invalidated.
	 * 
	 * @param monitor the progress monitor
	 * @param registryAdapter an instance of the registry adapter
	 * 
	 */
	protected void refreshRegistry(IProgressMonitor monitor,
			SOAGlobalRegistryAdapter registryAdapter) {
		try {
			registryAdapter.getGlobalRegistry();
			monitor.worked(30);
		} catch (Exception e) {
			e.printStackTrace();
			SOALogger.getLogger().error(e);
		}
	}
	
	/**
	 * Cancels a running job.
	 * @param monitor the progress monitor
	 * @return the cancel status of the job.
	 */
	protected IStatus cancelJob(IProgressMonitor monitor) {
		monitor.done();
		return Status.CANCEL_STATUS;
	}	

}
