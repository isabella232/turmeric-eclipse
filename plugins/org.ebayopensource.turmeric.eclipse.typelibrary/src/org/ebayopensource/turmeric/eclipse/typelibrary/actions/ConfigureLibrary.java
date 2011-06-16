/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.typelibrary.actions;

import java.util.ArrayList;
import java.util.List;

import org.ebayopensource.turmeric.eclipse.buildsystem.utils.ActionUtil;
import org.ebayopensource.turmeric.eclipse.core.resources.constants.SOAProjectConstants;
import org.ebayopensource.turmeric.eclipse.logging.SOALogger;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.GlobalRepositorySystem;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.TrackingEvent;
import org.ebayopensource.turmeric.eclipse.repositorysystem.utils.GlobalProjectHealthChecker;
import org.ebayopensource.turmeric.eclipse.utils.ui.UIUtil;
import org.ebayopensource.turmeric.eclipse.validator.utils.ValidateUtil;
import org.ebayopensource.turmeric.eclipse.validator.utils.common.AbstractBaseAccessValidator;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;


/**
 * @author mzang
 * 
 */
public class ConfigureLibrary implements IObjectActionDelegate {
    private IStructuredSelection selection;
    private static final SOALogger logger = SOALogger.getLogger();

    public void setActivePart(final IAction action,
            final IWorkbenchPart targetPart) {
    }

    public void run(final IAction action) {
        if (SOALogger.DEBUG)
            logger.entering(action, selection);
        try {
            if (selection == null)
                return;

            final IProject project = ActionUtil.preValidateAction(selection
                    .getFirstElement(), logger);
            if (project == null)
                return;

            // FIXME: Is this check needed for type library project?
            final IStatus status = new AbstractBaseAccessValidator() {

                @Override
                public List<IResource> getReadableFiles() {
                    // should check the following files
                    try {
                        return GlobalProjectHealthChecker
                                .getSOAProjectReadableResources(project);
                    } catch (Exception e) {
                        logger.warning(e);
                    }
                    return new ArrayList<IResource>(1);
                }

                @Override
                public List<IResource> getWritableFiles() {
                    // we should ensure that the project is writable
                    final List<IResource> result = new ArrayList<IResource>();
                    result.add(GlobalRepositorySystem.instanceOf()
                            .getActiveRepositorySystem().getAssetRegistry()
                            .getProjectConfigurationFile(project));
                    return result;
                }

            }.validate(project.getName());

            final String messages = ValidateUtil
                    .getFormattedStatusMessagesForAction(status);
            if (messages != null) {
                UIUtil.showErrorDialog(UIUtil.getActiveShell(), "Error",
                        messages, (Throwable) null);
                return;
            }
            final TrackingEvent event = new TrackingEvent(
					getClass().getName(), 
					TrackingEvent.TRACKING_ACTION);
			GlobalRepositorySystem.instanceOf().getActiveRepositorySystem()
			.trackingUsage(event);
            UIUtil.openPropertyPage(project,
                    SOAProjectConstants.PROP_PAGE_ID_TYPELIBRARA_PROJ);
        } catch (Exception e) {
            logger.error(e);
            UIUtil.showErrorDialog(e);
        } finally {
            if (SOALogger.DEBUG)
                logger.exiting();
        }
    }

    public void selectionChanged(final IAction action,
            final ISelection selection) {
        this.selection = (IStructuredSelection) selection;
    }
}
