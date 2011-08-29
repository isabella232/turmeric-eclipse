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


// TODO: Auto-generated Javadoc
/**
 * The Class SubmitServiceMantenanceVersion.
 *
 * @author yayu
 * @since 1.0.0
 */
public class SubmitServiceMantenanceVersion extends AbstractSubmitAssetAction {
	
	/** The Constant logger. */
	private static final SOALogger logger = SOALogger.getLogger();
	
	/**
	 * Instantiates a new submit service mantenance version.
	 */
	public SubmitServiceMantenanceVersion() {
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
		IStatus result = ActionUtil.submitServiceMaintenanceVersion(project, null);
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

}
