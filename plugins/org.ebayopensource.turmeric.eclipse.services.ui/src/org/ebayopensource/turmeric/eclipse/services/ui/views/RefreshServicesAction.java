/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.services.ui.views;

import org.ebayopensource.turmeric.eclipse.services.ui.Activator;
import org.ebayopensource.turmeric.eclipse.typelibrary.TypeLibraryActivator;
import org.ebayopensource.turmeric.eclipse.ui.UIActivator;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.StructuredViewer;


/**
 * @author smathew
 * 
 * Refresh Action for Services Explorer. Uses the build in refresh support from
 * the viewer.
 */
public class RefreshServicesAction extends Action {

	private StructuredViewer structuredViewer;
	public static final String ID = "org.ebayopensource.turmeric.eclipse.services.ui.refreshaction";

	public RefreshServicesAction(StructuredViewer structuredViewer) {
		this.structuredViewer = structuredViewer;
	}

	@Override
	public String getText() {
		return "Refresh";
	}

	@Override
	public String getToolTipText() {
		return "Refresh the registry";
	}

	@Override
	public void run() {
		if (structuredViewer != null) {
			structuredViewer.refresh();
		}
	}

	@Override
	public ImageDescriptor getImageDescriptor() {
		
		return UIActivator.getImageDescriptor("icons/refresh.gif");
	}

	@Override
	public String getId() {
		return ID;
	}
}
