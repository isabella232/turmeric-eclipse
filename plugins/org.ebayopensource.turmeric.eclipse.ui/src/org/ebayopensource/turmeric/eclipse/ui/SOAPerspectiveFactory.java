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
 * @author yayu
 *
 */
@SuppressWarnings("restriction")
public final class SOAPerspectiveFactory extends JavaPerspectiveFactory{
	public static final String VIEWID_SERVICES_EXPLORER = "org.ebayopensource.turmeric.eclipse.services.ui.views.ServicesExplorerView";
	public static final String VIEWID_GLOBAL_TYPE_REGISTRY = "org.ebayopensource.turmeric.eclipse.typelibrary.registryView";
	public static final String VIEWID_PROPERTY = "org.eclipse.ui.views.PropertySheet";
	public static final String VIEWID_ERROR = "org.ebayopensource.turmeric.eclipse.errorlibrary.errorRegistryView";
	
	/**
	 * 
	 */
	public SOAPerspectiveFactory() {
		super();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IPerspectiveFactory#createInitialLayout(org.eclipse.ui.IPageLayout)
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
		
		layout.addView(VIEWID_SERVICES_EXPLORER, IPageLayout.TOP, 
				(float)0.5, IPageLayout.ID_OUTLINE);
		IPlaceholderFolderLayout topFolder = layout.getFolderForView(VIEWID_SERVICES_EXPLORER);
		((IFolderLayout)topFolder).addView(VIEWID_ERROR);
		
		IPlaceholderFolderLayout folderLayout = layout.getFolderForView(IPageLayout.ID_OUTLINE);
		if (folderLayout instanceof IFolderLayout) {
			//((IFolderLayout)folderLayout).addView("org.maven.ide.eclipse.views.MavenRepositoryView");
			
		}
		
	}

}
