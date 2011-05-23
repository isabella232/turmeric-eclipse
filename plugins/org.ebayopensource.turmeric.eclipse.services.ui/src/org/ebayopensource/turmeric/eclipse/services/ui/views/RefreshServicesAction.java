/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.services.ui.views;

import org.ebayopensource.turmeric.eclipse.ui.UIActivator;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.StructuredViewer;


/**
 * The Class RefreshServicesAction.
 *
 * @author smathew
 * 
 * Refresh Action for Services Explorer. Uses the build in refresh support from
 * the viewer.
 */
public class RefreshServicesAction extends Action {

	private StructuredViewer structuredViewer;
	
	/** The Constant ID. */
	public static final String ID = "org.ebayopensource.turmeric.eclipse.services.ui.refreshaction";

	/**
	 * Instantiates a new refresh services action.
	 *
	 * @param structuredViewer the structured viewer
	 */
	public RefreshServicesAction(StructuredViewer structuredViewer) {
		this.structuredViewer = structuredViewer;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.action.Action#getText()
	 */
	@Override
	public String getText() {
		return "Refresh";
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.action.Action#getToolTipText()
	 */
	@Override
	public String getToolTipText() {
		return "Refresh the registry";
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.action.Action#run()
	 */
	@Override
	public void run() {
		if (structuredViewer != null) {
			structuredViewer.refresh();
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.action.Action#getImageDescriptor()
	 */
	@Override
	public ImageDescriptor getImageDescriptor() {
		
		return UIActivator.getImageDescriptor("icons/refresh.gif");
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.action.Action#getId()
	 */
	@Override
	public String getId() {
		return ID;
	}
}
