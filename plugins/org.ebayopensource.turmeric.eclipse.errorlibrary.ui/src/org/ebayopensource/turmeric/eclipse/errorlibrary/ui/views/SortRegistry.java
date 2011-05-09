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
package org.ebayopensource.turmeric.eclipse.errorlibrary.ui.views;

import org.ebayopensource.turmeric.eclipse.errorlibrary.ui.resources.SOAMessages;
import org.ebayopensource.turmeric.eclipse.ui.actions.SOASortAction;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.jface.viewers.ViewerComparator;


/**
 * The Class SortRegistry.
 *
 * @author yayu
 */
public class SortRegistry extends SOASortAction {

	/**
	 * Instantiates a new sort registry.
	 *
	 * @param viewer the viewer
	 * @param sorter the sorter
	 */
	public SortRegistry(StructuredViewer viewer, ViewerComparator sorter) {
		super(viewer, sorter);
	}

	/* (non-Javadoc)
	 * @see org.ebayopensource.turmeric.eclipse.ui.actions.SOASortAction#getTitle()
	 */
	@Override
	public String getTitle() {
		return SOAMessages.UI_SORT_ERROR;
	}

	/* (non-Javadoc)
	 * @see org.ebayopensource.turmeric.eclipse.ui.actions.SOASortAction#getToolTip()
	 */
	@Override
	public String getToolTip() {
		return SOAMessages.UI_SORT_ERROR;
	}
}
