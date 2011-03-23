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
package org.ebayopensource.turmeric.eclipse.typelibrary.core.wst;

import org.ebayopensource.turmeric.eclipse.core.command.BaseEditorActionDelegate;
import org.ebayopensource.turmeric.eclipse.exception.validation.ValidationInterruptedException;
import org.ebayopensource.turmeric.eclipse.typelibrary.actions.ActionUtil;
import org.ebayopensource.turmeric.eclipse.utils.ui.UIUtil;
import org.ebayopensource.turmeric.eclipse.validator.utils.ValidateUtil;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.ui.IFileEditorInput;


/**
 * @author yayu
 *
 */
public abstract class AbastractTypeLibraryAtion extends
		BaseEditorActionDelegate {

	/**
	 * 
	 */
	public AbastractTypeLibraryAtion() {
		super();
	}
	
	protected boolean doValidation() throws ValidationInterruptedException, CoreException {
		return doValidation(true);
	}
	
	protected boolean doValidation(boolean validateProjectConfigFile) 
	throws ValidationInterruptedException, CoreException {
		if (editorPart != null && editorPart.getEditorInput() instanceof IFileEditorInput) {
			final IFile editorFile = ((IFileEditorInput) editorPart
					.getEditorInput()).getFile();
			final IProject project = editorFile.getProject();
			IFile additionalFile = getSelectedFile();
			
			final IStatus status = ActionUtil
					.validateTypeDependencyAndProjectConfigFile(project, validateProjectConfigFile, 
							additionalFile);

			final String messages = ValidateUtil
					.getFormattedStatusMessagesForAction(status);
			if (messages != null) {
				UIUtil.showErrorDialog(UIUtil.getActiveShell(),
						"Error", messages, (Throwable) null);
				return false;
			}
		}
		return true;
	}

}
