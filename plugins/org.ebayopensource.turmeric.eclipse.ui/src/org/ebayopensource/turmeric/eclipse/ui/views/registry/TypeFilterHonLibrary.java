/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.ui.views.registry;

import org.apache.commons.lang.StringUtils;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;

import org.ebayopensource.turmeric.common.config.LibraryType;

/**
 * The Class TypeFilterHonLibrary.
 *
 * @author smathew
 */
public class TypeFilterHonLibrary extends ViewerFilter {
	
	//the currently selected node
	private IRegistryTreeNode node;

	
	/**
	 * Instantiates a new type filter hon library.
	 *
	 * @param node the node
	 */
	public TypeFilterHonLibrary(IRegistryTreeNode node) {
		this.node = node;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ViewerFilter#select(org.eclipse.jface.viewers.Viewer, java.lang.Object, java.lang.Object)
	 */
	@Override
	public boolean select(Viewer viewer, Object parentElement, Object element) {
		if (node == null || node instanceof TypeTreeRoot)
			//the root is selected
			return true;
		if (element instanceof LibraryType) {
			LibraryType type = (LibraryType) element;
			if (node instanceof CategoryTreeNode) {
				final CategoryTreeNode category = (CategoryTreeNode) node;
				return StringUtils.equals(type.getLibraryInfo().getCategory()
						, category.getLabel());
			} else if (node instanceof TypeLibraryTreeNode) {
				final TypeLibraryTreeNode typeLib = (TypeLibraryTreeNode) node;
				return StringUtils
				.equals(type.getLibraryInfo().getLibraryName(),
						typeLib.getLabel());
			}
		}
		return false;
	}

	/**
	 * Gets the node.
	 *
	 * @return the node
	 */
	public IRegistryTreeNode getNode() {
		return node;
	}

	/**
	 * Sets the node.
	 *
	 * @param node the new node
	 */
	public void setNode(IRegistryTreeNode node) {
		this.node = node;
	}

}
