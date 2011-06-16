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
import org.ebayopensource.turmeric.eclipse.logging.SOALogger;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.GlobalRepositorySystem;
import org.ebayopensource.turmeric.eclipse.resources.model.ProjectInfo;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

/**
 * @author smathew
 * 
 *         The content provider for services explorer view
 * @see ITreeContentProvider
 */
public class ServicesContentProvider implements ITreeContentProvider {
	Object[] services = null;

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

	public Object getParent(Object element) {
		return null;
	}

	public boolean hasChildren(Object element) {
		return element instanceof ProjectInfo;
	}

	public Object[] getElements(Object inputElement) {
		try {
			services = GlobalRepositorySystem.instanceOf()
					.getActiveRepositorySystem().getAssetRegistry()
					.getAllAvailableServices().toArray();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return services;
	}

	public void dispose() {
	}

	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
	}

}
