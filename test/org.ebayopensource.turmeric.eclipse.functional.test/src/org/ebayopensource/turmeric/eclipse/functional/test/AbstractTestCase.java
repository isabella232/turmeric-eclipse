/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.functional.test;

import org.ebayopensource.turmeric.eclipse.utils.plugin.WorkspaceUtil;
import org.ebayopensource.turmeric.eclipse.utils.ui.UIUtil;
import org.eclipse.core.internal.resources.ResourceException;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IWorkbenchPage;
import org.junit.After;
import org.junit.Before;
import static org.junit.Assume.*;


public abstract class AbstractTestCase {

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUpBeforeClass() throws Exception {
		cleanupWorkspace();
	}
	
	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDownAfterClass() throws Exception {
		cleanupWorkspace();
	}
	
	public void assumeFalse(boolean value) {
		assumeTrue(!value);
	}

	protected  void cleanupWorkspace() throws CoreException {
		closeEditors();
		
		IWorkspaceRoot root = WorkspaceUtil.getWorkspaceRoot();
		try {
			root.refreshLocal(IResource.DEPTH_INFINITE, new NullProgressMonitor());
			root.delete(true, true, new NullProgressMonitor());
			
		} catch (ResourceException ex) {
			//TODO: Fix EBoxTypeLib.setup() to use friendlier deletion.
			//If we get the exception we ignore it, seems to only handle on Windows.
			//This is due to the deletion of files in the EBoxTypeLib.Setup() method
			//This should eventually be addressed.
		}
		root.refreshLocal(IResource.DEPTH_INFINITE, new NullProgressMonitor());
	}

	protected  void closeEditors() {
		IWorkbenchPage page = UIUtil.getActivePage();
		IEditorReference[] reference = page.getEditorReferences();
		if (reference != null && reference.length > 0) {
			for(IEditorReference eref : reference) {
				IEditorPart editor = eref.getEditor(false);
				if (editor != null) {
					page.closeEditor(editor, false);
				}
			}
		}
	}

}
