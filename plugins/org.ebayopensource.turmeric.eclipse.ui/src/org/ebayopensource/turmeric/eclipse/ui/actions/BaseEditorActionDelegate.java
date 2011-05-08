/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.ui.actions;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IEditorActionDelegate;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.actions.ActionDelegate;

/**
 * Serves as the base classes for action, intending to use the Editors Context.
 * Most of the editor action needs the selected file and the selected project.
 * Since this is the commonality, this class abstracts the common functions.
 * Clients can overwrite it if required. Typically clients don't need to
 * overwrite it.
 * 
 * @author smathew
 * 
 */
public abstract class BaseEditorActionDelegate extends ActionDelegate implements
		IEditorActionDelegate {
	
	/** The editor part. */
	protected IEditorPart editorPart = null;

	/**
	 * {@inheritDoc}
	 */
	public void selectionChanged(IAction action, ISelection selection) {
	}

	/**
	 * {@inheritDoc}
	 */
	public void setActiveEditor(IAction action, IEditorPart targetEditor) {
		editorPart = targetEditor;
	}

	/**
	 * Usual getter. Most of the actions needs this selected file to operate on.
	 * 
	 * @return IFile - selected file
	 */
	protected IFile getSelectedFile() {
		IFile xsdFile = null;
		if (editorPart != null
				&& editorPart.getEditorInput() instanceof IFileEditorInput) {
			IFileEditorInput editorInput = (IFileEditorInput) editorPart
					.getEditorInput();
			xsdFile = editorInput.getFile();

		}
		return xsdFile;
	}

	/**
	 * Usual getter. Most of the actions needs this selected project to operate
	 * on. Most of them needs this project needs minimum to minimum refresh the
	 * project.
	 *
	 * @return the selected project
	 */
	protected IProject getSelectedProject() {
		IProject project = null;
		if (getSelectedFile() != null)
			project = getSelectedFile().getProject();
		return project;
	}
}
