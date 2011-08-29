/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.ui.views.registry;

import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.viewers.ViewerFilter;

// TODO: Auto-generated Javadoc
/**
 * The listener interface for receiving typeLibrarySelectionChanged events.
 * The class that is interested in processing a typeLibrarySelectionChanged
 * event implements this interface, and the object created
 * with that class is registered with a component using the
 * component's <code>addTypeLibrarySelectionChangedListener</code> method. When
 * the typeLibrarySelectionChanged event occurs, that object's appropriate
 * method is invoked.
 *
 * @author smathew
 */
public class TypeLibrarySelectionChangedListener implements
		ISelectionChangedListener {
	
	/** The filter. */
	private ViewerFilter filter = null;

	/** The type viewer. */
	private TableViewer typeViewer;

	/**
	 * Instantiates a new type library selection changed listener.
	 *
	 * @param typeViewer the type viewer
	 */
	public TypeLibrarySelectionChangedListener(TableViewer typeViewer) {
		this.typeViewer = typeViewer;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void selectionChanged(SelectionChangedEvent event) {
		if (event != null && event.getSelection() instanceof TreeSelection) {
			TreeSelection selection = (TreeSelection) event.getSelection();
			
			if (selection.getFirstElement() instanceof IRegistryTreeNode) {
				final IRegistryTreeNode node = (IRegistryTreeNode) selection.getFirstElement();
				
				if (typeViewer != null) {
					if (filter == null) {
						//remove the existing filter
						for (ViewerFilter filter : typeViewer.getFilters()) {
							if (filter instanceof TypeFilterHonLibrary) {
								// there is one already. remove it first
								typeViewer.removeFilter(filter);
							}
						}
					} else {
						typeViewer.removeFilter(this.filter);
					}
					this.filter = new TypeFilterHonLibrary(node);
					typeViewer.addFilter(this.filter);
				}
			}
		}

	}

}
