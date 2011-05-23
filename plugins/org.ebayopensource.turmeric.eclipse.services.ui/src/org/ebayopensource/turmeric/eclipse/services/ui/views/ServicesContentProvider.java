/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.services.ui.views;

import org.ebayopensource.turmeric.eclipse.buildsystem.utils.PropertiesUtil;
import org.ebayopensource.turmeric.eclipse.core.logging.SOALogger;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.GlobalRepositorySystem;
import org.ebayopensource.turmeric.eclipse.resources.model.ProjectInfo;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

/**
 * The Class ServicesContentProvider.
 *
 * @author smathew
 * 
 * The content provider for services explorer view
 * @see ITreeContentProvider
 */
public class ServicesContentProvider implements ITreeContentProvider {
	
	/** The services. */
	Object[] services = null;

	/**
	 * {@inheritDoc}
	 *  @see org.eclipse.jface.viewers.ITreeContentProvider#getChildren(java.lang.Object)
	 */
	@Override
	public Object[] getChildren(Object parentElement) {
		if (parentElement instanceof ProjectInfo) {
			ProjectInfo projectInfo = (ProjectInfo) parentElement;
			Object obj[] = new Object[3];
			obj[0] = new ServicesViewLayerModel(projectInfo.getServiceLayer());
			try {
				obj[1] = new ServicesViewInterfaceModel(
						PropertiesUtil
								.getInterfaceClassNameForService(projectInfo
										.getName()));
			} catch (Exception e) {
				SOALogger.getLogger().error(e);
			}
			obj[2] = new ServicesViewServiceModel(projectInfo.getName(),
					projectInfo.getVersion());
			return obj;
		}
		return null;
	}

	/**
	 * {@inheritDoc}
	 *  @see org.eclipse.jface.viewers.ITreeContentProvider#getParent(java.lang.Object)
	 */
	@Override
	public Object getParent(Object element) {
		return null;
	}

	/**
	 * {@inheritDoc}
	 * 
	 *  @see org.eclipse.jface.viewers.ITreeContentProvider#hasChildren(java.lang.Object)
	 */
	@Override
	public boolean hasChildren(Object element) {
		return element instanceof ProjectInfo;
	}

	/**
	 * {@inheritDoc}
	 * 
	 *  @see org.eclipse.jface.viewers.ITreeContentProvider#getElements(java.lang.Object)
	 */
	@Override
	public Object[] getElements(Object inputElement) {
		try {
			services = GlobalRepositorySystem.instanceOf()
					.getActiveRepositorySystem().getAssetRegistry()
					.getAllAvailableServices().toArray();
		} catch (Exception e) {
			SOALogger.getLogger().error(e);
		}
		return services;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.IContentProvider#dispose()
	 */
	@Override
	public void dispose() {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
	}

}
