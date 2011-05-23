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
package org.ebayopensource.turmeric.eclipse.services.search;

import org.ebayopensource.turmeric.eclipse.services.search.SOASearchResult.SOASearchResultService;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.graphics.Image;


/**
 * The Class SOASearchTableLabelProvider.
 *
 * @author yayu
 * @since 1.0.0
 */
public class SOASearchTableLabelProvider implements ITableLabelProvider {

	/**
	 * Instantiates a new sOA search table label provider.
	 */
	public SOASearchTableLabelProvider() {
		super();
	}

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
		if (element instanceof SOASearchResultService) {
			final SOASearchResultService service = (SOASearchResultService) element;
			switch (columnIndex) {
			case 0:
				return service.getServiceName();
			case 1:
				return service.getServiceLayer();
			case 2:
				return service.getServiceVersion();
			}
		}
		return "";
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
