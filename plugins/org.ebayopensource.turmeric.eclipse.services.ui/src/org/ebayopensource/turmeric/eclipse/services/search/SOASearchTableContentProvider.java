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

import java.util.Collections;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;

/**
 * The Class SOASearchTableContentProvider.
 *
 * @author yayu
 * @since 1.0.0
 */
public class SOASearchTableContentProvider implements IStructuredContentProvider{
	private TableViewer fTableViewer;
	private SOASearchResult searchResult;
	
	/**
	 * Instantiates a new sOA search table content provider.
	 */
	public SOASearchTableContentProvider() {
		super();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object[] getElements(Object inputElement) {
		if (inputElement instanceof SOASearchResult) {
			return ((SOASearchResult)inputElement).getElements();
		}
		return Collections.EMPTY_LIST.toArray();
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
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		fTableViewer = (TableViewer) viewer;
		searchResult = (SOASearchResult)newInput;
	}
	
	/**
	 * Clear.
	 */
	public void clear() {
		fTableViewer.refresh();
	}
	
	/**
	 * Elements Changed.
	 *
	 * @param updatedElements the updated elements
	 */
	public void elementsChanged(Object[] updatedElements) {
		for (int i = 0; i < updatedElements.length; i++) {
			if (searchResult.getMatchCount(updatedElements[i]) > 0) {
				if (fTableViewer.testFindItem(updatedElements[i]) != null)
					fTableViewer.refresh(updatedElements[i]);
				else
					fTableViewer.add(updatedElements[i]);
			} else {
				fTableViewer.remove(updatedElements[i]);
			}
		}
	}

}
