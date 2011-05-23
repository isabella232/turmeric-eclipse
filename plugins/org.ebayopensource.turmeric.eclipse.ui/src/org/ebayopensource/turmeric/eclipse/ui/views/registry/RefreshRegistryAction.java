/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.ui.views.registry;

import org.ebayopensource.turmeric.eclipse.core.logging.SOALogger;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.SOAGlobalRegistryAdapter;
import org.ebayopensource.turmeric.eclipse.ui.UIActivator;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.StructuredViewer;


/**
 * The Class RefreshRegistryAction.
 *
 * @author smathew
 * @deprecated Use the new RegistryRefreshHandler
 */
@Deprecated
public class RefreshRegistryAction extends Action{
	private StructuredViewer typeLibraryViewer;
	private StructuredViewer typeViewer;
	private static final String TEXT = "Refresh the Type Registry";

	/**
	 * Instantiates a new refresh registry action.
	 *
	 * @param typeLibraryViewer the type library viewer
	 */
	public RefreshRegistryAction(StructuredViewer typeLibraryViewer) {
		super(TEXT, AS_PUSH_BUTTON);
		this.setToolTipText(TEXT);
		this.typeLibraryViewer = typeLibraryViewer;
		this.setImageDescriptor(UIActivator.getImageDescriptor("icons/refresh.gif"));
	}
	
	/**
	 * Sets the type viewer.
	 *
	 * @param typeViewer the new type viewer
	 */
	public void setTypeViewer(StructuredViewer typeViewer) {
		this.typeViewer = typeViewer;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void run() {
		if (typeLibraryViewer != null) {
			Job job = new Job("Refresh SOA Typelib Registry") {
				@Override
				protected IStatus run(IProgressMonitor monitor) {
					monitor.beginTask("Invalidating registry", 10);
					SOAGlobalRegistryAdapter.getInstance().invalidateRegistry();
					try {
						final Object input = SOAGlobalRegistryAdapter.getInstance()
						.getGlobalRegistry();
						typeLibraryViewer.setInput(input);
						if (typeViewer != null) {
							typeViewer.setInput(input);
						}
					} catch (Exception e) {
						SOALogger.getLogger().error(e);
						return Status.CANCEL_STATUS;
					}
					typeLibraryViewer.refresh(true);
					return Status.OK_STATUS;
				}
			};
			job.setUser(true);
			job.schedule();
		}
	}
}
