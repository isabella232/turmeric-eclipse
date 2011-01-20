/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.typelibrary.registry;

import org.apache.commons.lang.StringUtils;

/**
 * @author smathew
 * 
 * Represents the Category in the Tree Viewer
 */
public class CategoryTreeNode extends AbstractRegistryTreeNode {

	public CategoryTreeNode(String category, TypeTreeRoot typeTreeRoot) {
		super(typeTreeRoot, category);
	}

	public String getCategory() {
		return getNode() instanceof String ? (String) getNode() : null;
	}

	public TypeTreeRoot getTypeTreeRoot() {
		return getParent() instanceof TypeTreeRoot ? (TypeTreeRoot) getParent()
				: null;
	}

	@Override
	public String getLabel() {
		return StringUtils.defaultString(getCategory());
	}
}
