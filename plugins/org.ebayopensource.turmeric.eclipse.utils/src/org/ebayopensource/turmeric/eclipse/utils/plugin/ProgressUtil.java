/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.utils.plugin;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;

/**
 * The Class ProgressUtil.
 *
 * @author smathew
 * ProgressMonitor/Job related functions
 */
public class ProgressUtil {
	
	/** The Constant PROGRESS_STEP. */
	public static final int PROGRESS_STEP = 100;
	
	/**
	 * Gets the default monitor.
	 *
	 * @param monitor the monitor
	 * @return A Null safe get Monitor method If the parameter is null this will
	 * return a NullProgressMonitor to avoid the redundant code.
	 */
	public static IProgressMonitor getDefaultMonitor(IProgressMonitor monitor) {
		final IProgressMonitor retMonitor = monitor == null ? new NullProgressMonitor()
				: monitor;
		return retMonitor;
	}
	
	/**
	 * Progress one step.
	 *
	 * @param monitor the monitor
	 * @throws OperationCanceledException the operation canceled exception
	 */
	public static void progressOneStep(final IProgressMonitor monitor)
	throws OperationCanceledException{
		progressOneStep(monitor, PROGRESS_STEP);
	}
	
	/**
	 * Progress one step.
	 *
	 * @param monitor the monitor
	 * @param oneStep the one step
	 * @throws OperationCanceledException the operation canceled exception
	 */
	public static void progressOneStep(IProgressMonitor monitor, final int oneStep) 
	throws OperationCanceledException{		
		//suresh changed here
		monitor.internalWorked(oneStep);
		if (monitor.isCanceled())
			throw new OperationCanceledException();
	}
}
