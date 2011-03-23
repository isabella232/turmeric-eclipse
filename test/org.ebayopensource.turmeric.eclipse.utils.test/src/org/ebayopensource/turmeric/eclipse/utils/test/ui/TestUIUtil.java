/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.utils.test.ui;

import static org.junit.Assert.fail;

import org.ebayopensource.turmeric.eclipse.utils.ui.UIUtil;
import org.eclipse.core.runtime.IStatus;
import org.junit.Assert;
import org.junit.Ignore;
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
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.utils.ui.UIUtil#fileDialog(java.lang.String, java.lang.String[])}.
	 */
	@Ignore("Can not test dialog for now")
	@Test
	public void testFileDialog() {
		fail("Not yet implemented");
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
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.utils.ui.UIUtil#modalFileDialog(java.lang.String, java.lang.String[])}.
	 */
	@Ignore("Can not test dialog for now")
	@Test
	public void testModalFileDialog() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.utils.ui.UIUtil#directoryDialog(java.lang.String, java.lang.String)}.
	 */
	@Ignore("Can not test dialog for now")
	@Test
	public void testDirectoryDialog() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.utils.ui.UIUtil#runJobInUIDialog(org.eclipse.core.runtime.jobs.Job)}.
	 */
	@Ignore("Can not test dialog for now")
	@Test
	public void testRunJobInUIDialog() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.utils.ui.UIUtil#openPropertyPage(org.eclipse.core.runtime.IAdaptable, java.lang.String)}.
	 */
	@Ignore("Can not test dialog for now")
	@Test
	public void testOpenPropertyPage() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.utils.ui.UIUtil#openChoiceDialog(java.lang.String, java.lang.String, int, java.lang.String, java.lang.String)}.
	 */
	@Ignore("Can not test dialog for now")
	@Test
	public void testOpenChoiceDialogStringStringIntStringString() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.utils.ui.UIUtil#openChoiceDialog(java.lang.String, java.lang.String, int)}.
	 */
	@Ignore("Can not test dialog for now")
	@Test
	public void testOpenChoiceDialogStringStringInt() {
		fail("Not yet implemented");
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
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.utils.ui.UIUtil#showErrorDialog(org.eclipse.swt.widgets.Shell, java.lang.String, java.lang.String, java.lang.Throwable)}.
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.utils.ui.UIUtil#showErrorDialog(java.lang.String, java.lang.Throwable)}.
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.utils.ui.UIUtil#showErrorDialog(java.lang.Throwable)}.
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.utils.ui.UIUtil#showErrorDialog(org.eclipse.swt.widgets.Shell, java.lang.String, java.lang.String)}.
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.utils.ui.UIUtil#showErrorDialog(org.eclipse.swt.widgets.Shell, java.lang.String, org.eclipse.core.runtime.IStatus)}.
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.utils.ui.UIUtil#showErrorDialog(org.eclipse.swt.widgets.Shell, java.lang.String, java.lang.String, org.eclipse.core.runtime.IStatus, boolean, boolean, boolean, boolean)}.
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.utils.ui.UIUtil#showErrorDialog(org.eclipse.swt.widgets.Shell, java.lang.String, java.lang.String, org.eclipse.core.runtime.IStatus)}.
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.utils.ui.UIUtil#showErrorDialog(org.eclipse.swt.widgets.Shell, java.lang.String, java.lang.String, java.lang.String)}.
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.utils.ui.UIUtil#showErrorDialogInNewThread(org.eclipse.swt.widgets.Shell, java.lang.String, java.lang.String)}.
	 * 
	 */
	@Ignore("Can not test dialog for now")
	@Test
	public void testShowErrorDialog() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.utils.ui.UIUtil#getButtonWidthHint(org.eclipse.swt.widgets.Button)}.
	 */
	@Ignore
	@Test
	public void testGetButtonWidthHint() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.utils.ui.UIUtil#setButtonWidthHint(org.eclipse.swt.widgets.Button)}.
	 */
	@Ignore
	@Test
	public void testSetButtonWidthHint() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.utils.ui.UIUtil#setEqualWidthHintForButtons(org.eclipse.swt.widgets.Button[])}.
	 */
	@Ignore
	@Test
	public void testSetEqualWidthHintForButtons() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.utils.ui.UIUtil#syncExec(java.lang.String, org.ebayopensource.turmeric.eclipse.utils.ui.UIUtil.IRunnable)}.
	 */
	@Ignore
	@Test
	public void testSyncExec() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.utils.ui.UIUtil#belongsTo(java.lang.Object)}.
	 */
	@Ignore
	@Test
	public void testBelongsTo() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.utils.ui.UIUtil#getActivePage()}.
	 */
	@Test
	public void testGetActivePage() {
		Assert.assertNotNull(UIUtil.getActivePage());
	}

	/**
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.utils.ui.UIUtil#getActiveEditor()}.
	 */
	@Ignore("Currently nothing opened")
	@Test
	public void testGetActiveEditor() {
		Assert.assertNotNull(UIUtil.getActiveEditor());
	}

	/**
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.utils.ui.UIUtil#getActiveEditorsProject()}.
	 */
	@Ignore("Currently nothing opened")
	@Test
	public void testGetActiveEditorsProject() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.utils.ui.UIUtil#decorateControl(org.ebayopensource.turmeric.eclipse.utils.ui.UIUtil.ISOAControlDecorator, org.eclipse.swt.widgets.Control, java.lang.String)}.
	 */
	@Ignore("Currently no UI testing")
	@Test
	public void testDecorateControl() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.utils.ui.UIUtil#getStatusLineManager()}.
	 */
	@Test
	public void testGetStatusLineManager() {
		Assert.assertNotNull(UIUtil.getStatusLineManager());
	}

}
