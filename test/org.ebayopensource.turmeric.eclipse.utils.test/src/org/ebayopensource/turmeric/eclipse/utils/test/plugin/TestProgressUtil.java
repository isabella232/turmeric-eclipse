/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.utils.test.plugin;

import org.ebayopensource.turmeric.eclipse.utils.plugin.ProgressUtil;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.junit.Assert;
import org.junit.Test;


/**
 * @author yayu
 *
 */
public class TestProgressUtil {

	/**
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.utils.plugin.ProgressUtil#getDefaultMonitor(org.eclipse.core.runtime.IProgressMonitor)}.
	 */
	@Test
	public void testGetDefaultMonitor() {
		Assert.assertNotNull(ProgressUtil.getDefaultMonitor(null));
		
		IProgressMonitor monitor = new NullProgressMonitor();
		Assert.assertSame(monitor, ProgressUtil.getDefaultMonitor(monitor));
	}

	/**
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.utils.plugin.ProgressUtil#progressOneStep(org.eclipse.core.runtime.IProgressMonitor)}.
	 */
	@Test
	public void testProgressOneStepIProgressMonitor() {
		TestProgressMonitor monitor = new TestProgressMonitor();
		ProgressUtil.progressOneStep(monitor);
		Assert.assertTrue(monitor.getProgress() == ProgressUtil.PROGRESS_STEP);
	}

	/**
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.utils.plugin.ProgressUtil#progressOneStep(org.eclipse.core.runtime.IProgressMonitor, int)}.
	 */
	@Test
	public void testProgressOneStepIProgressMonitorInt() {
		TestProgressMonitor monitor = new TestProgressMonitor();
		ProgressUtil.progressOneStep(monitor, 10);
		Assert.assertTrue(monitor.getProgress() == 10);
		
	}
	
	private static class TestProgressMonitor implements IProgressMonitor {
		private int progress = 0;
		
		public void beginTask(String arg0, int arg1) {
			
		}

		public void done() {
			
		}

		public void internalWorked(double arg0) {
			this.progress += arg0;
			
		}

		public boolean isCanceled() {
			return false;
		}

		public void setCanceled(boolean arg0) {
			
		}

		public void setTaskName(String arg0) {
			
		}

		public void subTask(String arg0) {
			
		}

		public void worked(int arg0) {
			this.progress += arg0;
			
		}
		
		public int getProgress() {
			return this.progress;
		}
		
	}

}
