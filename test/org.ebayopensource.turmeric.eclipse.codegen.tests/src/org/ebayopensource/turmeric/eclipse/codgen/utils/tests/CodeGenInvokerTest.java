/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.codgen.utils.tests;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.apache.commons.lang.NullArgumentException;
import org.ebayopensource.turmeric.eclipse.codegen.utils.CodegenInvoker;
import org.ebayopensource.turmeric.eclipse.utils.classloader.SOAPluginClassLoader;
import org.ebayopensource.turmeric.eclipse.utils.plugin.WorkspaceUtil;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class CodeGenInvokerTest {
	IWorkspaceRoot wsRoot = WorkspaceUtil.getWorkspaceRoot(); 
	String projectName = "testProject";

	@Before
	public void setupProjects() throws Exception {
		IPath path = ResourcesPlugin.getWorkspace().getRoot().getLocation();
		IProgressMonitor monitor = new NullProgressMonitor();
		WorkspaceUtil.createProject(projectName, path, new NullProgressMonitor());
		wsRoot.refreshLocal(IResource.DEPTH_INFINITE, new NullProgressMonitor());
		assertNotNull(wsRoot.getProject(projectName));
	}
	
	@After
	public void cleanWorkspace() throws Exception {
		wsRoot.delete(true, true, new NullProgressMonitor());
		wsRoot.refreshLocal(IResource.DEPTH_INFINITE, new NullProgressMonitor());
		assertTrue(wsRoot.getProjects().length == 0);
	}
	
	@Test
	public void testInit() throws Exception {
		IProject project = wsRoot.getProject(projectName);
		CodegenInvoker invoker = CodegenInvoker.init(project);
		assertNotNull(invoker);
	}
	
	@Test
	public void testInitfail() throws Exception {
		IProject project = null;
		try {
			CodegenInvoker.init(project);
		} catch (NullArgumentException ex) {
			return;
		}
		fail("Accepted a Null value for a Project");
	}


	@Test
	public void testGetSoaPluginClassLoader() throws Exception {
		IProject project = wsRoot.getProject(projectName);
		CodegenInvoker invoker = CodegenInvoker.init(project);
		SOAPluginClassLoader classLoader = invoker.getSoaPluginClassLoader();
		assertNotNull(classLoader);
	}

}
