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
package org.ebayopensource.turmeric.eclipse.typelibrary.registry;

import org.ebayopensource.turmeric.eclipse.ui.actions.SOASortAction;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.jface.viewers.ViewerComparator;


/**
 * @author yayu
 * 
 */
public class SortRegistryAction extends SOASortAction {

	public SortRegistryAction(StructuredViewer viewer, ViewerComparator sorter) {
		super(viewer, sorter);
	}

	@Override
	public String getTitle() {
		return "Sort Types";
	}

	@Override
	public String getToolTip() {
		return "Sort Types";
	}

}
