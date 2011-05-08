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

import org.ebayopensource.turmeric.eclipse.core.logging.SOALogger;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.GlobalRepositorySystem;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.TrackingEvent;
import org.ebayopensource.turmeric.eclipse.repositorysystem.ui.utils.ActionUtil;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.action.IAction;


/**
 * The Class UpdateExistingServiceVersion.
 *
 * @author yayu
 * @since 1.0.0
 */
public class UpdateExistingServiceVersion extends AbstractSubmitAssetAction {
	private static final SOALogger logger = SOALogger.getLogger();
	
	/**
	 * Instantiates a new update existing service version.
	 */
	public UpdateExistingServiceVersion() {
		super();
	}

	/* (non-Javadoc)
	 * @see org.ebayopensource.turmeric.eclipse.services.ui.actions.AbstractSubmitAssetAction#logger()
	 */
	@Override
	protected SOALogger logger() {
		return logger;
	}

	/* (non-Javadoc)
	 * @see org.ebayopensource.turmeric.eclipse.services.ui.actions.AbstractSubmitAssetAction#submitAsset(org.eclipse.jface.action.IAction, org.eclipse.core.resources.IProject)
	 */
	@Override
	public IStatus submitAsset(IAction action, IProject project)
			throws Exception {
		IStatus result = ActionUtil.updateExistingAssetVersionToSOARegistry(project);
		if (result.isOK()) {
			GlobalRepositorySystem.instanceOf().getActiveRepositorySystem()
			.trackingUsage(new TrackingEvent(
					getClass().getName(), 
					TrackingEvent.TRACKING_ACTION));
		}
		if (result.getSeverity() == IStatus.CANCEL) {
			return Status.OK_STATUS;
		}
		return result;
	}

	/* (non-Javadoc)
	 * @see org.ebayopensource.turmeric.eclipse.services.ui.actions.AbstractSubmitAssetAction#needOKDialog()
	 */
	@Override
	protected boolean needOKDialog() {
		//this action opens an editor, and do need to popup the dialog
		return false;
	}
}
