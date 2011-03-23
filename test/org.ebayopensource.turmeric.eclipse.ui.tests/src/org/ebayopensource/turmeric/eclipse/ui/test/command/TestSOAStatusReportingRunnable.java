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
package org.ebayopensource.turmeric.eclipse.ui.test.command;

import java.lang.reflect.InvocationTargetException;

import junit.framework.Assert;

import org.ebayopensource.turmeric.eclipse.ui.actions.SOAStatusReportingRunnable;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.junit.Test;


/**
 * @author yayu
 *
 */
public class TestSOAStatusReportingRunnable {

	/**
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.ui.actions.SOAStatusReportingRunnable#run(org.eclipse.core.runtime.IProgressMonitor)}.
	 */
	@Test
	public void testRun() throws Exception {
		IProgressMonitor monitor = new NullProgressMonitor();
		SOAStatusReportingRunnable runnable = new SOAStatusReportingRunnable() {

			@Override
			public IStatus execute(IProgressMonitor monitor) throws Exception {
				return Status.OK_STATUS;
			}
		};
		runnable.run(monitor);
		Assert.assertEquals(runnable.getStatus(), Status.OK_STATUS);
		
		runnable = new SOAStatusReportingRunnable() {

			@Override
			public IStatus execute(IProgressMonitor monitor) throws Exception {
				throw new RuntimeException();
			}
		};
		
		try {
			runnable.run(monitor);
		} catch (InvocationTargetException e) {
			Assert.assertFalse(runnable.getStatus().isOK());
		}
		
		runnable = new SOAStatusReportingRunnable() {

			@Override
			public IStatus execute(IProgressMonitor monitor) throws Exception {
				throw new InvocationTargetException(new RuntimeException());
			}
		};
		
		try {
			runnable.run(monitor);
		} catch (InvocationTargetException e) {
			Assert.assertFalse(runnable.getStatus().isOK());
		}
		
		runnable = new SOAStatusReportingRunnable() {

			@Override
			public IStatus execute(IProgressMonitor monitor) throws Exception {
				throw new InterruptedException();
			}
		};
		
		try {
			runnable.run(monitor);
		} catch (InterruptedException e) {
			Assert.assertFalse(runnable.getStatus().isOK());
		}
		
		runnable = new SOAStatusReportingRunnable() {

			@Override
			public IStatus execute(IProgressMonitor monitor) throws Exception {
				throw new CoreException(new Status(IStatus.ERROR, "org.ebayopensource.turmeric", "hello"));
			}
		};
		try {
			runnable.run(monitor);
		} catch (InvocationTargetException e) {
			Assert.assertFalse(runnable.getStatus().isOK());
		}
	}

	/**
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.ui.actions.SOAStatusReportingRunnable#getStatus()}.
	 */
	@Test
	public void testGetStatus() throws Exception {
		SOAStatusReportingRunnable runnable = new SOAStatusReportingRunnable() {

			@Override
			public IStatus execute(IProgressMonitor monitor) throws Exception {
				return Status.OK_STATUS;
			}
		};
		runnable.run(new NullProgressMonitor());
		Assert.assertEquals(runnable.getStatus(), Status.OK_STATUS);
	}

}
