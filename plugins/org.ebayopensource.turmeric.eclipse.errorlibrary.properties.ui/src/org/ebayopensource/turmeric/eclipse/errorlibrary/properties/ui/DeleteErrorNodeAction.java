/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.errorlibrary.properties.ui;

import org.apache.commons.io.FileUtils;
import org.ebayopensource.turmeric.eclipse.errorlibrary.properties.registry.TurmericErrorRegistry;
import org.ebayopensource.turmeric.eclipse.errorlibrary.properties.resources.SOAMessages;
import org.ebayopensource.turmeric.eclipse.errorlibrary.properties.utils.TurmericErrorLibraryUtils;
import org.ebayopensource.turmeric.eclipse.errorlibrary.views.ISOAErrDomain;
import org.ebayopensource.turmeric.eclipse.errorlibrary.views.ISOAErrLibrary;
import org.ebayopensource.turmeric.eclipse.errorlibrary.views.ISOAErrUIComp;
import org.ebayopensource.turmeric.eclipse.errorlibrary.views.ISOAError;
import org.ebayopensource.turmeric.eclipse.utils.lang.StringUtil;
import org.ebayopensource.turmeric.eclipse.utils.plugin.EclipseMessageUtils;
import org.ebayopensource.turmeric.eclipse.utils.plugin.WorkspaceUtil;
import org.ebayopensource.turmeric.eclipse.utils.ui.UIUtil;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.StructuredViewer;


/**
 * @author yayu
 *
 */
public class DeleteErrorNodeAction extends AbstractErrorNodeAction {


	/**
	 * 
	 * @param viewer a structured viewer
	 */
	public DeleteErrorNodeAction(StructuredViewer viewer) {
		super(SOAMessages.ACTION_TEXT_DELETE, viewer);
	}
	
	@Override
	protected IStatus preValidation() {
		IStatus status = super.preValidation();
		if(status.isOK() == false) {
			return status;
		}
		final ISOAErrUIComp selectedErrorNode = getSelectedErrorNode();
		if (selectedErrorNode instanceof ISOAErrLibrary) {
			return EclipseMessageUtils.createErrorStatus(
					SOAMessages.ERROR_NOSUPPORT_ERRORLIB);
		}
		if (selectedErrorNode instanceof ISOAErrDomain) {
			return validateErrorDomain((ISOAErrDomain)selectedErrorNode);
		} else if (selectedErrorNode instanceof ISOAError){
			final ISOAError error = (ISOAError) selectedErrorNode;
			if (error.getDomain() == null) {
				return EclipseMessageUtils.createErrorStatus(
						StringUtil.formatString(SOAMessages.ERROR_MISSING_ERRORDOMAIN, 
								error.getName()));
			}
			IStatus stat = validateErrorDomain(error.getDomain());
			if (stat.isOK() == false) {
				return stat;
			}
		}
		
		return Status.OK_STATUS;
	}
	
	private IStatus validateErrorDomain(ISOAErrDomain domain) {
		if (domain.getLibrary() == null) {
			return EclipseMessageUtils.createErrorStatus(
					StringUtil.formatString(SOAMessages.ERROR_NO_PARENT_ERRORLIB, 
							domain.getName()));
		}
		IProject project = WorkspaceUtil.getProject(domain.getLibrary().getName());
		if (project.isAccessible() == false) {
			return EclipseMessageUtils.createErrorStatus(
					StringUtil.formatString(SOAMessages.ERROR_NO_ERRORLIB_IN_WORKSPACE, 
							domain.getName(), domain.getLibrary().getName()));
		}
		IFolder domainFolder = TurmericErrorLibraryUtils.getErrorDomainFolder(
				project, domain.getName());
		if (domainFolder.exists() == false) {
			return EclipseMessageUtils.createErrorStatus(
					StringUtil.formatString(SOAMessages.ERROR_NO_ERRORDOMAIN_FOLDER, 
							domainFolder.getLocation()));
		}
		return Status.OK_STATUS;
	}



	@Override
	public IStatus execute(ISOAErrUIComp selectedErrorNode) throws Exception {
		if (MessageDialog.openConfirm(UIUtil.getActiveShell(), "Confirmation", 
				"Are you sure you want to delete the selected node->" + selectedErrorNode.getName() + "?")
				== false) {
			return Status.CANCEL_STATUS;
		}
		if (selectedErrorNode instanceof ISOAErrDomain) {
			final ISOAErrDomain domain = (ISOAErrDomain) selectedErrorNode;
			final IProject project = WorkspaceUtil.getProject(
					domain.getLibrary().getName());
			IFolder domainFolder = TurmericErrorLibraryUtils.getErrorDomainFolder(
					project, domain.getName());
			FileUtils.deleteDirectory(domainFolder.getLocation().toFile());
			WorkspaceUtil.refresh(domainFolder.getParent());
			TurmericErrorRegistry.removeErrorDomain(domain);
			//update library property file
			TurmericErrorLibraryUtils.removeDomainFromProps(project, domain.getName());
		} else if (selectedErrorNode instanceof ISOAError){
			final ISOAError error = (ISOAError) selectedErrorNode;
			final ISOAErrDomain domain = error.getDomain();
			final IProject project = WorkspaceUtil.getProject(
					domain.getLibrary().getName());
			IFolder domainFolder = TurmericErrorLibraryUtils.getErrorDomainFolder(
					project, domain.getName());
			TurmericErrorLibraryUtils.removeErrorFromXmlData(domainFolder, error);
			TurmericErrorLibraryUtils.removeErrorFromPropsFile(domainFolder, error);
			WorkspaceUtil.refresh(domainFolder.getParent());
			TurmericErrorRegistry.removeError(domain.getName(), error);
		}
		return Status.OK_STATUS;
	}

}
