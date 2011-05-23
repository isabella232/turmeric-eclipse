/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.ui.views.registry;

import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.graphics.Image;

import org.ebayopensource.turmeric.common.config.LibraryType;

/**
 * The Class TypeLabelProvider.
 *
 * @author smathew
 * Label Provider for Right side Type Viewer.
 */
public class TypeLabelProvider implements ITableLabelProvider {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Image getColumnImage(Object element, int columnIndex) {
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getColumnText(Object element, int columnIndex) {
		if (element instanceof LibraryType) {
			LibraryType libType = (LibraryType) element;
			switch (columnIndex) {
			case 0:
				return libType.getName();
			case 1:
				return libType.getVersion();
			}
		}
		return "Not Available";
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addListener(ILabelProviderListener listener) {

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void dispose() {

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isLabelProperty(Object element, String property) {
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void removeListener(ILabelProviderListener listener) {

	}

}
