/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.utils.test.plugin;

import java.util.ArrayList;
import java.util.Collection;

import org.ebayopensource.turmeric.eclipse.utils.plugin.EclipseMessageUtils;
import org.ebayopensource.turmeric.eclipse.utils.test.Activator;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.core.runtime.Status;
import org.junit.Assert;
import org.junit.Test;


/**
 * @author yayu
 *
 */
public class TestEclipseMessageUtils {
	

	/**
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.utils.plugin.EclipseMessageUtils#createAssertSafeStatus(int, java.lang.String, int, java.lang.String, java.lang.Throwable)}.
	 */
	@Test
	public void testCreateAssertSafeStatus() {
		Exception e = new RuntimeException("iPhone is for girls");
		IStatus status = EclipseMessageUtils.createAssertSafeStatus(IStatus.ERROR, Activator.PLUGIN_ID, 
				100, "iPhone sucks, Android Rocks", e);
		Assert.assertNotNull(status);
		Assert.assertFalse(status.isOK());
		Assert.assertSame(e, status.getException());
	}

	/**
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.utils.plugin.EclipseMessageUtils#createEmptyOKMultiStatus(java.lang.String)}.
	 */
	@Test
	public void testCreateEmptyOKMultiStatus() {
		IStatus status = EclipseMessageUtils.createEmptyOKMultiStatus("Nikon is the best");
		Assert.assertTrue("result should be an instance of MultiStatus", status instanceof MultiStatus);
		Assert.assertTrue("result should be ok status", status.isOK());
		Assert.assertTrue(status.getChildren().length == 0);
	}

	/**
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.utils.plugin.EclipseMessageUtils#createEmptyErrorMultiStatus(java.lang.String)}.
	 */
	@Test
	public void testCreateEmptyErrorMultiStatus() {
		IStatus status = EclipseMessageUtils.createEmptyErrorMultiStatus("Nikon is the best");
		Assert.assertTrue("result should be an instance of MultiStatus", status instanceof MultiStatus);
		Assert.assertTrue(status.getChildren().length == 0);
	}

	/**
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.utils.plugin.EclipseMessageUtils#createErrorMultiStatus(java.util.Collection, java.lang.String)}.
	 */
	@Test
	public void testCreateErrorMultiStatusCollectionOfIStatusString() {
		Collection<IStatus> statuses = new ArrayList<IStatus>();
		statuses.add(EclipseMessageUtils.createErrorStatus("Android Rocks"));
		IStatus status = EclipseMessageUtils.createErrorMultiStatus(statuses, "Nikon is the best");
		Assert.assertTrue("result should be an instance of MultiStatus", status instanceof MultiStatus);
		Assert.assertFalse("result should be error status", status.isOK());
		Assert.assertTrue(status.getChildren().length > 0);
	}

	/**
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.utils.plugin.EclipseMessageUtils#createErrorMultiStatus(org.eclipse.core.runtime.IStatus[], java.lang.String)}.
	 */
	@Test
	public void testCreateErrorMultiStatusIStatusArrayString() {
		Collection<IStatus> statuses = new ArrayList<IStatus>();
		statuses.add(EclipseMessageUtils.createErrorStatus("Android Rocks"));
		IStatus status = EclipseMessageUtils.createErrorMultiStatus(statuses.toArray(
				new IStatus[0]), "Nikon is the best");
		Assert.assertTrue("result should be an instance of MultiStatus", status instanceof MultiStatus);
		Assert.assertFalse("result should be error status", status.isOK());
		Assert.assertTrue(status.getChildren().length > 0);
	}

	/**
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.utils.plugin.EclipseMessageUtils#createMultiStatus(org.eclipse.core.runtime.IStatus[], java.lang.String, java.lang.Throwable)}.
	 */
	@Test
	public void testCreateMultiStatusIStatusArrayStringThrowable() {
		Throwable t = new RuntimeException("Jackie Chan sucks");
		Collection<IStatus> statuses = new ArrayList<IStatus>();
		statuses.add(EclipseMessageUtils.createErrorStatus("Android Rocks"));
		IStatus status = EclipseMessageUtils.createMultiStatus(statuses.toArray(
				new IStatus[0]), null, t);
		Assert.assertTrue("result should be an instance of MultiStatus", status instanceof MultiStatus);
		Assert.assertFalse("result should be error status", status.isOK());
		Assert.assertTrue(status.getChildren().length > 0);
	}

	/**
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.utils.plugin.EclipseMessageUtils#createMultiStatus(java.lang.String, int, org.eclipse.core.runtime.IStatus[], java.lang.String, java.lang.Throwable)}.
	 */
	@Test
	public void testCreateMultiStatusStringIntIStatusArrayStringThrowable() {
		Throwable t = new RuntimeException("Jackie Chan sucks");
		Collection<IStatus> statuses = new ArrayList<IStatus>();
		statuses.add(EclipseMessageUtils.createErrorStatus("Android Rocks"));
		IStatus status = EclipseMessageUtils.createMultiStatus(Activator.PLUGIN_ID, IStatus.ERROR, 
				statuses.toArray(new IStatus[0]), null, t);
		Assert.assertTrue("result should be an instance of MultiStatus", status instanceof MultiStatus);
		Assert.assertFalse("result should be error status", status.isOK());
		Assert.assertTrue(status.getChildren().length > 0);
	}

	/**
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.utils.plugin.EclipseMessageUtils#createErrorStatus(java.lang.String, java.lang.String, java.lang.Throwable)}.
	 */
	@Test
	public void testCreateErrorStatusStringStringThrowable() {
		Throwable t = new RuntimeException("Jackie Chan sucks");
		IStatus status = EclipseMessageUtils.createErrorStatus(null, null, t);
		Assert.assertFalse("result should not be an instance of MultiStatus", status instanceof MultiStatus);
		Assert.assertFalse("result should be error status", status.isOK());
		Assert.assertSame(t, status.getException());
	}

	/**
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.utils.plugin.EclipseMessageUtils#createErrorStatus(java.lang.String, java.lang.Throwable)}.
	 */
	@Test
	public void testCreateErrorStatusStringThrowable() {
		Throwable t = new RuntimeException("Jackie Chan sucks");
		IStatus status = EclipseMessageUtils.createErrorStatus("Playstations 3 rocks", t);
		Assert.assertFalse("result should not be an instance of MultiStatus", status instanceof MultiStatus);
		Assert.assertFalse("result should be error status", status.isOK());
		Assert.assertSame(t, status.getException());
	}

	/**
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.utils.plugin.EclipseMessageUtils#createErrorStatus(java.lang.String)}.
	 */
	@Test
	public void testCreateErrorStatusString() {
		IStatus status = EclipseMessageUtils.createErrorStatus("Playstations 3 rocks");
		Assert.assertFalse("result should not be an instance of MultiStatus", status instanceof MultiStatus);
		Assert.assertFalse("result should be error status", status.isOK());
	}

	/**
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.utils.plugin.EclipseMessageUtils#createErrorStatus(java.lang.Throwable)}.
	 */
	@Test
	public void testCreateErrorStatusThrowable() {
		Throwable t = new RuntimeException("Jackie Chan sucks");
		IStatus status = EclipseMessageUtils.createErrorStatus(t);
		Assert.assertFalse("result should not be an instance of MultiStatus", status instanceof MultiStatus);
		Assert.assertFalse("result should be error status", status.isOK());
		Assert.assertSame(t, status.getException());
	}

	/**
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.utils.plugin.EclipseMessageUtils#createStatus(java.lang.String, int)}.
	 */
	@Test
	public void testCreateStatusStringInt() {
		IStatus status = EclipseMessageUtils.createStatus("Jackie Chan sucks", IStatus.ERROR);
		Assert.assertFalse("result should not be an instance of MultiStatus", status instanceof MultiStatus);
		Assert.assertFalse("result should be error status", status.isOK());
	}

	/**
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.utils.plugin.EclipseMessageUtils#createStatus(java.lang.String, java.lang.String, int, java.lang.String[])}.
	 */
	@Test
	public void testCreateStatusStringStringIntStringArray() {
		IStatus status = EclipseMessageUtils.createStatus(null, "Jackie Chan sucks", IStatus.ERROR, 
				"Jackie Chan is a shame of Chinese");
		Assert.assertFalse("result should not be an instance of MultiStatus", status instanceof MultiStatus);
		Assert.assertFalse("result should be error status", status.isOK());
	}

	/**
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.utils.plugin.EclipseMessageUtils#formatStatus(org.eclipse.core.runtime.IStatus)}.
	 */
	@Test
	public void testFormatStatus() {
		Throwable t = new RuntimeException("Jackie Chan sucks");
		Collection<IStatus> statuses = new ArrayList<IStatus>();
		statuses.add(EclipseMessageUtils.createErrorStatus("Android Rocks"));
		IStatus status = EclipseMessageUtils.createMultiStatus(Activator.PLUGIN_ID, IStatus.ERROR, 
				statuses.toArray(new IStatus[0]), null, t);
		String result = EclipseMessageUtils.formatStatus(status);
		Assert.assertNotNull(result);
		Assert.assertTrue(result.length() > 0);
	}

	/**
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.utils.plugin.EclipseMessageUtils#severityLabel(int)}.
	 */
	@Test
	public void testSeverityLabel() {
		Assert.assertNotNull(EclipseMessageUtils.severityLabel(Status.OK));
		Assert.assertNotNull(EclipseMessageUtils.severityLabel(Status.CANCEL));
		Assert.assertNotNull(EclipseMessageUtils.severityLabel(Status.WARNING));
		Assert.assertNotNull(EclipseMessageUtils.severityLabel(Status.INFO));
		Assert.assertNotNull(EclipseMessageUtils.severityLabel(Status.ERROR));
	}

}
