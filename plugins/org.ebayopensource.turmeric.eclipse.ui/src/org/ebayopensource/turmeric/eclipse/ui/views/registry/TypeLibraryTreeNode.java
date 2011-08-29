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
package org.ebayopensource.turmeric.eclipse.ui.views.registry;

import org.ebayopensource.turmeric.common.config.TypeLibraryType;
import org.ebayopensource.turmeric.eclipse.core.resources.constants.SOAProjectConstants;

// TODO: Auto-generated Javadoc
/**
 * The Class TypeLibraryTreeNode.
 *
 * @author yayu
 */
public class TypeLibraryTreeNode extends AbstractRegistryTreeNode {

	/**
	 * Instantiates a new type library tree node.
	 */
	public TypeLibraryTreeNode() {
		super();
	}

	/**
	 * Instantiates a new type library tree node.
	 *
	 * @param parent the parent
	 * @param node the node
	 */
	public TypeLibraryTreeNode(IRegistryTreeNode parent, TypeLibraryType node) {
		super(parent, node);
	}

	/**
	 * Sets the type library type.
	 *
	 * @param node the new type library type
	 */
	public void setTypeLibraryType(TypeLibraryType node) {
		super.setNode(node);
	}

	/**
	 * Gets the type library type.
	 *
	 * @return the type library type
	 */
	public TypeLibraryType getTypeLibraryType() {
		return getNode() instanceof TypeLibraryType ? (TypeLibraryType) getNode()
				: null;
	}

	/* (non-Javadoc)
	 * @see org.ebayopensource.turmeric.eclipse.ui.views.registry.AbstractRegistryTreeNode#getLabel()
	 */
	@Override
	public String getLabel() {
		final TypeLibraryType type = getTypeLibraryType();
		return type != null ? type.getLibraryName()
				: SOAProjectConstants.EMPTY_STRING;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object parameter) {
		return parameter instanceof TypeLibraryTreeNode
				&& ((TypeLibraryTreeNode) parameter).getTypeLibraryType()
						.getLibraryName().equals(
								getTypeLibraryType().getLibraryName())
				&& ((TypeLibraryTreeNode) parameter).getTypeLibraryType()
						.getLibraryNamespace().equals(
								getTypeLibraryType().getLibraryNamespace());
	}

}
