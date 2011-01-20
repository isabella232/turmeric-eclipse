/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.ui.actions;

import org.ebayopensource.turmeric.eclipse.ui.UIActivator;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.jface.viewers.ViewerComparator;


/**
 * @author yayu
 * 
 * This is the standard Sort action. Uses the default comparator. The gif is
 * also standard sort gif.
 * 
 */
public abstract class SOASortAction extends Action {
	private boolean fSorted;

	private StructuredViewer fViewer;

	private ViewerComparator fComparator;

	public SOASortAction(StructuredViewer viewer, ViewerComparator sorter) {
		super("", IAction.AS_CHECK_BOX);
		setText(getTitle());
		setToolTipText(getToolTip());
		setImageDescriptor(UIActivator.getImageDescriptor("icons/sort.gif"));
		fViewer = viewer;
		// Set the comparator
		// If one was not specified, use the default
		if (sorter == null) {
			fComparator = new ViewerComparator();
		} else {
			fComparator = sorter;
		}
		// Determine if the viewer is already sorted
		// Note: Most likely the default comparator is null
		if (viewer.getComparator() != sorter) {
			fSorted = false;
		} else {
			fSorted = true;
		}
		// Set the status of this action depending on whether it is sorted or
		// not
		setChecked(fSorted);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.action.Action#run()
	 */
	public void run() {
		// Toggle sorting on/off
		if (fSorted) {
			// Sorting is on
			// Turn it off
			fViewer.setComparator(null);
			fSorted = false;
		} else {
			// Sorting is off
			// Turn it on
			fViewer.setComparator(fComparator);
			fSorted = true;
		}
		notifyResult(true);
	}

	/**
	 * This is the Text shown in the action
	 * 
	 * @return
	 */
	public abstract String getTitle();

	/**
	 * This is the tool tip for action
	 * 
	 * @return
	 */
	public abstract String getToolTip();

}
