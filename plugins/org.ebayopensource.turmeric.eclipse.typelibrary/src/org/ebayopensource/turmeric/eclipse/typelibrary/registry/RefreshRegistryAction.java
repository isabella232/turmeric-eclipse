/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.typelibrary.registry;

import org.ebayopensource.turmeric.eclipse.buildsystem.core.SOAGlobalRegistryAdapter;
import org.ebayopensource.turmeric.eclipse.logging.SOALogger;
import org.ebayopensource.turmeric.eclipse.typelibrary.TypeLibraryActivator;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.swt.custom.BusyIndicator;


/**
 * @author smathew
 * 
 */
public class RefreshRegistryAction extends Action{
	private StructuredViewer typeLibraryViewer;
	private StructuredViewer typeViewer;
	private static final String TEXT = "Refresh the Type Registry";

	public RefreshRegistryAction(StructuredViewer typeLibraryViewer) {
		super(TEXT, AS_PUSH_BUTTON);
		this.setToolTipText(TEXT);
		this.typeLibraryViewer = typeLibraryViewer;
		this.setImageDescriptor(TypeLibraryActivator.getImageFromRegistry("icons/refresh.gif"));
	}
	
	public void setTypeViewer(StructuredViewer typeViewer) {
		this.typeViewer = typeViewer;
	}

	public void run() {
		if (typeLibraryViewer != null) {
			final Runnable runnable = new Runnable() {
				public void run() {
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
						throw new RuntimeException(e);
					}
				}
			};
			BusyIndicator.showWhile(null, runnable);
			typeLibraryViewer.refresh();
		}
	}
}
