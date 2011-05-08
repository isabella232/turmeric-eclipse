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
package org.ebayopensource.turmeric.eclipse.ui.actions;

import java.lang.reflect.InvocationTargetException;

import org.ebayopensource.turmeric.eclipse.utils.plugin.EclipseMessageUtils;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.operation.IRunnableWithProgress;


/**
 * An implementation of IRunnableWithProgress which could report result.
 * @author yayu
 * @since 1.0.0
 */
public abstract class SOAStatusReportingRunnable implements IRunnableWithProgress {
	private IStatus status = Status.OK_STATUS;
	
	/**
	 * Instantiates a new sOA status reporting runnable.
	 */
	public SOAStatusReportingRunnable() {
		super();
	}

	/**
	 * {@inheritDoc}
	 */
	public final void run(IProgressMonitor monitor) throws InvocationTargetException,
			InterruptedException {
		try {
			status = execute(monitor);
		} catch (InvocationTargetException e) {
			status = EclipseMessageUtils.createErrorStatus(e);
			throw e;
		} catch (InterruptedException e) {
			status = EclipseMessageUtils.createErrorStatus(e);
			throw e;
		} catch (CoreException e) {
			status = e.getStatus();
			throw new InvocationTargetException(e);
		} catch (Exception e) {
			status = EclipseMessageUtils.createErrorStatus(e);
			throw new InvocationTargetException(e);
		}
	}
	
	/**
	 * no need to try catch.
	 *
	 * @param monitor the monitor
	 * @return the i status
	 * @throws Exception the exception
	 */
	public abstract IStatus execute(IProgressMonitor monitor) throws Exception; 
	
	
	/**
	 * Gets the status.
	 *
	 * @return the status
	 */
	public IStatus getStatus() {
		return status;
	}

}
