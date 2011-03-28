/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.services.ui.views;

import org.ebayopensource.turmeric.eclipse.resources.model.ProjectInfo;
import org.ebayopensource.turmeric.eclipse.ui.UIActivator;
import org.ebayopensource.turmeric.eclipse.ui.UIConstants;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.swt.graphics.Image;


/**
 * @author smathew The Label provider for services explorer view
 * @see ILabelProvider
 */
public class ServicesLabelProvider implements ILabelProvider {
	private static String DEFAULT_IMAGE = UIConstants.IMAGE_ECLIPSE;

	public Image getImage(Object element) {
		String imageName = DEFAULT_IMAGE;
		if (element instanceof ServicesViewInterfaceModel) {
			imageName = ServicesViewInterfaceModel.getImageName();
		} else if (element instanceof ServicesViewLayerModel) {
			imageName = ServicesViewLayerModel.getImageName();
		} else if (element instanceof ServicesViewServiceModel) {
			imageName = ServicesViewServiceModel.getImageName();
		}

		return UIActivator.getImageFromRegistry(imageName);
	}

	public String getText(Object element) {
		if (element instanceof ProjectInfo)
			return ((ProjectInfo) element).getName();
		else if (element instanceof ServicesViewBaseModel)
			return element.toString();
		return "";
	}

	public void addListener(ILabelProviderListener listener) {
	}

	public void dispose() {
	}

	public boolean isLabelProperty(Object element, String property) {
		return false;
	}

	public void removeListener(ILabelProviderListener listener) {
	}

	public static Image getIcon(final String name) {
		final Image image = UIActivator.getImageFromRegistry(name);
		if (image != null)
			return image;
		UIActivator.getDefault().getImageRegistry().put(name,
				UIActivator.getImageDescriptor(UIActivator.ICON_PATH + name));
		return UIActivator.getDefault().getImageRegistry().get(name);
	}

}
