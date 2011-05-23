/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.errorlibrary.ui.views;

import org.ebayopensource.turmeric.eclipse.core.logging.SOALogger;
import org.ebayopensource.turmeric.eclipse.errorlibrary.ErrorLibraryActivator;
import org.ebayopensource.turmeric.eclipse.errorlibrary.views.SOAErrContentFactory;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.swt.custom.BusyIndicator;


/**
 * The Class RefreshErrRegistry.
 *
 * @author smathew
 */
public class RefreshErrRegistry extends Action {
	private StructuredViewer typeLibraryViewer;

	/**
	 * Instantiates a new refresh err registry.
	 *
	 * @param typeLibraryViewer the type library viewer
	 */
	public RefreshErrRegistry(StructuredViewer typeLibraryViewer) {
		super(TEXT, AS_PUSH_BUTTON);
		this.setToolTipText(TEXT);
		this.typeLibraryViewer = typeLibraryViewer;
		this.setImageDescriptor(ErrorLibraryActivator
				.getImageFromRegistry("icons/refresh.gif"));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void run() {
		if (typeLibraryViewer != null) {
			final Runnable runnable = new Runnable() {
				@Override
				public void run() {
					try {
						SOAErrContentFactory.invalidateProvider();
						final Object input = SOAErrContentFactory.getProvider();
						typeLibraryViewer.setInput(input);						
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
