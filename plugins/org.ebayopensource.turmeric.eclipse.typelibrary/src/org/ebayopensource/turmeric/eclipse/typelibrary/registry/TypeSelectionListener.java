/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.typelibrary.registry;

import org.ebayopensource.turmeric.eclipse.buildsystem.core.SOAGlobalRegistryAdapter;
import org.ebayopensource.turmeric.eclipse.core.exception.SOAExceptionHandler;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;

import org.ebayopensource.turmeric.common.config.LibraryType;

/**
 * Selects the library back on type selection. Stores an instance of the type
 * library viewer to select the corresponding type library when a type is
 * selected. The selected type is extracted from the selection event as a call
 * back, but the type library selection is a little greedy, We do it on the
 * instance that we have.
 * 
 * @see ISelectionChangedListener
 * @author smathew
 * 
 */
public class TypeSelectionListener implements ISelectionChangedListener {

	private TreeViewer treeViewer = null;

	/**
	 * Store the tree viewer that is the library viewer as an instance variable
	 * for future selection changes from type viewer to type library viewer.
	 * 
	 * @param treeViewer
	 */
	public TypeSelectionListener(TreeViewer treeViewer) {
		this.treeViewer = treeViewer;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ISelectionChangedListener#selectionChanged(org.eclipse.jface.viewers.SelectionChangedEvent)
	 */
	public void selectionChanged(SelectionChangedEvent event) {		
		IStructuredSelection selection = (IStructuredSelection) event
				.getSelection();

		if (selection.getFirstElement() instanceof LibraryType) {
			LibraryType libraryType = (LibraryType) selection.getFirstElement();
			String typeLibraryName = libraryType.getLibraryInfo()
					.getLibraryName();
			TypeLibraryTreeNode libraryNode = new TypeLibraryTreeNode();
			try {
				libraryNode.setTypeLibraryType(SOAGlobalRegistryAdapter.getInstance()
						.getGlobalRegistry().getTypeLibrary(typeLibraryName));
			} catch (Exception e) {
				SOAExceptionHandler.silentHandleException(e);
			}
			StructuredSelection structuredSelection = new StructuredSelection(
					libraryNode);
			treeViewer.setSelection(structuredSelection,true);			
		}
	}	
}
