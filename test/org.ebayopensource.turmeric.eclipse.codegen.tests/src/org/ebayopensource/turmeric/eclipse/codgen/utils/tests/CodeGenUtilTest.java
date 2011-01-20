/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.codgen.utils.tests;

import static org.junit.Assert.assertEquals;

import org.ebayopensource.turmeric.eclipse.codegen.utils.CodeGenUtil;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.junit.Test;


public class CodeGenUtilTest {

	@Test
	public void testGetGIPFromInterface() {
		String GIP = "org.ebayopensource";
		String serviceInterface = "org.ebayopensource.service";
		assertEquals("Wrong GIP found", GIP,CodeGenUtil.getGIPFromInterface(serviceInterface));
	}

	@Test
	public void testGetGINFromInterface() {
		String GIN = "service";
		String serviceInterface = "org.ebayopensource.service";
		assertEquals("Wrong GIN found", GIN, CodeGenUtil.getGINFromInterface(serviceInterface));
	}

	@Test
	public void testGetJavaHome() {
		String javaHome = System.getProperty("java.home");
		assertEquals("Java home did not match", javaHome, CodeGenUtil.getJavaHome());
	}

	@Test
	public void testGetJdkHome() {
		String jdkHome = System.getProperty("java.home");
		IPath path = new Path(jdkHome);
		if (path.lastSegment().equals("jre"))
			path = path.removeLastSegments(1);
		assertEquals("JDK home did not match", path, new Path(CodeGenUtil.getJdkHome()));
	}

}
