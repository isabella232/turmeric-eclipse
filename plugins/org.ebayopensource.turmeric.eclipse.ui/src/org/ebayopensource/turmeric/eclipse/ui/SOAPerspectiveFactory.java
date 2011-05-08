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
package org.ebayopensource.turmeric.eclipse.ui;

import org.eclipse.jdt.internal.ui.JavaPerspectiveFactory;
import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPlaceholderFolderLayout;
import org.eclipse.ui.internal.PageLayout;

/**
 * A factory for creating SOAPerspective objects.
 *
 * @author yayu
 */
@SuppressWarnings("restriction")
public final class SOAPerspectiveFactory extends JavaPerspectiveFactory{
	
	/** The Constant VIEWID_GLOBAL_TYPE_REGISTRY. */
	public static final String VIEWID_GLOBAL_TYPE_REGISTRY = "org.ebayopensource.turmeric.eclipse.typelibrary.registryView";
	
	/** The Constant VIEWID_PROPERTY. */
	public static final String VIEWID_PROPERTY = "org.eclipse.ui.views.PropertySheet";
	
	/**
	 * Instantiates a new sOA perspective factory.
	 */
	public SOAPerspectiveFactory() {
		super();
	}

	/**
	 * {@inheritDoc}
	 */
	public void createInitialLayout(IPageLayout layout) {
		super.createInitialLayout(layout);
		if (layout instanceof PageLayout) {			
			IPlaceholderFolderLayout folderLayout = layout.getFolderForView(IPageLayout.ID_PROBLEM_VIEW);
			if (folderLayout instanceof IFolderLayout) {
				final IFolderLayout bottomLayout = (IFolderLayout)folderLayout;
				bottomLayout.addView(VIEWID_GLOBAL_TYPE_REGISTRY);
				bottomLayout.addView("org.maven.ide.eclipse.views.MavenRepositoryView");
				bottomLayout.addView(VIEWID_PROPERTY);
			}
		}
	}

}
