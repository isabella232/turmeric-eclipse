/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.utils.test.ui;

import org.ebayopensource.turmeric.eclipse.utils.ui.UIUtil;
import org.eclipse.core.runtime.IStatus;
import org.junit.Assert;
import org.junit.Test;


/**
 * @author yayu
 *
 */
public class TestUIUtil {

	/**
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.utils.ui.UIUtil#progressService()}.
	 */
	@Test
	public void testProgressService() {
		Assert.assertNotNull(UIUtil.progressService());
	}

	/**
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.utils.ui.UIUtil#getWorkbench()}.
	 */
	@Test
	public void testGetWorkbench() {
		Assert.assertNotNull(UIUtil.getWorkbench());
	}

	/**
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.utils.ui.UIUtil#getHelpSystem()}.
	 */
	@Test
	public void testGetHelpSystem() {
		Assert.assertNotNull(UIUtil.getHelpSystem());
	}

	/**
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.utils.ui.UIUtil#display()}.
	 */
	@Test
	public void testDisplay() {
		Assert.assertNotNull(UIUtil.display());
	}

	/**
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.utils.ui.UIUtil#getActiveShell()}.
	 */
	@Test
	public void testGetActiveShell() {
		Assert.assertNotNull(UIUtil.getActiveShell());
	}

	/**
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.utils.ui.UIUtil#getActiveWorkBenchWindow()}.
	 */
	@Test
	public void testGetActiveWorkBenchWindow() {
		Assert.assertNotNull(UIUtil.getActiveWorkBenchWindow());
	}

	/**
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.utils.ui.UIUtil#getDetailedExceptionStackTrace(java.lang.Throwable)}.
	 */
	@Test
	public void testGetDetailedExceptionStackTrace() {
		Throwable t = new RuntimeException("Hello World");
		IStatus status = UIUtil.getDetailedExceptionStackTrace(t);
		Assert.assertNotNull(status);
	}

	/**
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.utils.ui.UIUtil#getActivePage()}.
	 */
	@Test
	public void testGetActivePage() {
		Assert.assertNotNull(UIUtil.getActivePage());
	}

	/**
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.utils.ui.UIUtil#getStatusLineManager()}.
	 */
	@Test
	public void testGetStatusLineManager() {
		Assert.assertNotNull(UIUtil.getStatusLineManager());
	}

}
