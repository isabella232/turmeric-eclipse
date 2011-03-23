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
package org.ebayopensource.turmeric.eclipse.services.ui.actions;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import org.ebayopensource.turmeric.eclipse.buildsystem.utils.ActionUtil;
import org.ebayopensource.turmeric.eclipse.logging.SOALogger;
import org.ebayopensource.turmeric.eclipse.repositorysystem.utils.GlobalProjectHealthChecker;
import org.ebayopensource.turmeric.eclipse.repositorysystem.utils.TurmericServiceUtils;
import org.ebayopensource.turmeric.eclipse.services.resources.SOAMessages;
import org.ebayopensource.turmeric.eclipse.utils.plugin.EclipseMessageUtils;
import org.ebayopensource.turmeric.eclipse.utils.ui.UIUtil;
import org.ebayopensource.turmeric.eclipse.validator.utils.ValidateUtil;
import org.ebayopensource.turmeric.eclipse.validator.utils.common.AbstractBaseAccessValidator;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;


/**
 * @author yayu
 * @since 1.0.0
 */
public abstract class AbstractSubmitAssetAction implements
		IObjectActionDelegate {
private IStructuredSelection selection;
	
	/**
	 * 
	 */
	public AbstractSubmitAssetAction() {
		super();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IObjectActionDelegate#setActivePart(org.eclipse.jface.action.IAction, org.eclipse.ui.IWorkbenchPart)
	 */
	public void setActivePart(IAction action, IWorkbenchPart targetPart) {

	}
	
	protected IStatus preValidate(IStructuredSelection selection) throws CoreException {
		if (selection == null)
			return EclipseMessageUtils.createErrorStatus(SOAMessages.ERR_EMPTY_SELECTION);
		
		final IProject project = 
			ActionUtil.preValidateAction(selection.getFirstElement(), logger());
		if (project == null) {
			return EclipseMessageUtils.createErrorStatus(SOAMessages.ERR_INVALID_PROJECT);
		}
		
		if (TurmericServiceUtils.isSOAInterfaceProject(project) == false) {
			return EclipseMessageUtils.createErrorStatus("The selected project is not a SOA interface project->" + project);
		}

		final IStatus status = new AbstractBaseAccessValidator() {

			@Override
			public List<IResource> getReadableFiles() {
				//should check the following files
				//service_intf_project.properties
				//service_metadata.properties
				//service wsdl
				try {
					return GlobalProjectHealthChecker.getSOAProjectReadableResources(project);
				} catch (Exception e) {
					logger().warning(e);
				}
				return new ArrayList<IResource>(1);
			}

			@Override
			public List<IResource> getWritableFiles() {
				//we do not need to modify anything
				final List<IResource> result = new ArrayList<IResource>();
				return result;
			}

		}.validate(project.getName());

		return status;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IActionDelegate#run(org.eclipse.jface.action.IAction)
	 */
	public void run(IAction action) {
		try {
			if (SOALogger.DEBUG)
				logger().entering(action, selection);
			
			final IStatus status = preValidate(selection);
			
			final String messages = ValidateUtil.getFormattedStatusMessagesForAction(status);
			if (messages != null) {
				UIUtil.showErrorDialog(UIUtil.getActiveShell(), "Error", 
						messages, (Throwable)null);
				return;
			}
			final IProject project = (IProject) ((IAdaptable) selection.getFirstElement())
			.getAdapter(IProject.class);
			
			final IStatus result = submitAsset(action, project);
			if (result.isOK()) {
				final String message = MessageFormat.format(
						SOAMessages.ASSET_SUMISSION_SUCCEEDED_MESSAGE, 
						new Object[]{project.getName(), result.getMessage()});
				logger().info(message);
				if (needOKDialog() == true) {
					MessageDialog.openInformation(UIUtil.getActiveShell(), SOAMessages.ASSET_SUMISSION_SUCCEEDED_TITLE,
							message);
				}
			} else {
				final String message = ValidateUtil.getFormattedStatusMessagesForAction(result);
				UIUtil.showErrorDialog(UIUtil.getActiveShell(), SOAMessages.ASSET_SUMISSION_FAILED_TITLE, 
						message, result.getException());
				logger().warning(message);
			}
		} catch (Exception e) {
			logger().error(e);
			UIUtil.showErrorDialog(e);
		} finally {
			if (SOALogger.DEBUG)
				logger().exiting();
		}
	}
	
	public abstract IStatus submitAsset(IAction action, IProject project) throws Exception;
	
	/**
	 * Indicate whether to popup a dialog when the submission succeed
	 * @return
	 */
	protected boolean needOKDialog() {
		return false;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IActionDelegate#selectionChanged(org.eclipse.jface.action.IAction, org.eclipse.jface.viewers.ISelection)
	 */
	public void selectionChanged(IAction action, ISelection selection) {
		this.selection = (IStructuredSelection)selection;
	}

	protected abstract SOALogger logger();
}
