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

// TODO: Auto-generated Javadoc
/**
 * The Class CategoryTreeNode.
 *
 * @author smathew
 * 
 * Represents the Category in the Tree Viewer
 */
public class CategoryTreeNode extends AbstractRegistryTreeNode {

	/**
	 * Instantiates a new category tree node.
	 *
	 * @param category the category
	 * @param typeTreeRoot the type tree root
	 */
	public CategoryTreeNode(String category, TypeTreeRoot typeTreeRoot) {
		super(typeTreeRoot, category);
	}

	/**
	 * Gets the category.
	 *
	 * @return the category
	 */
	public String getCategory() {
		return getNode() instanceof String ? (String) getNode() : null;
	}

	/**
	 * Gets the type tree root.
	 *
	 * @return the type tree root
	 */
	public TypeTreeRoot getTypeTreeRoot() {
		return getParent() instanceof TypeTreeRoot ? (TypeTreeRoot) getParent()
				: null;
	}

	/* (non-Javadoc)
	 * @see org.ebayopensource.turmeric.eclipse.ui.views.registry.AbstractRegistryTreeNode#getLabel()
	 */
	@Override
	public String getLabel() {
		return StringUtils.defaultString(getCategory());
	}
}
