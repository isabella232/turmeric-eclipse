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

import org.ebayopensource.turmeric.eclipse.resources.constants.SOAProjectConstants;

import org.ebayopensource.turmeric.common.config.TypeLibraryType;

/**
 * @author yayu
 * 
 */
public class TypeLibraryTreeNode extends AbstractRegistryTreeNode {

	public TypeLibraryTreeNode() {
		super();
	}

	/**
	 * @param parent
	 * @param node
	 */
	public TypeLibraryTreeNode(IRegistryTreeNode parent, TypeLibraryType node) {
		super(parent, node);
	}

	public void setTypeLibraryType(TypeLibraryType node) {
		super.setNode(node);
	}

	public TypeLibraryType getTypeLibraryType() {
		return getNode() instanceof TypeLibraryType ? (TypeLibraryType) getNode()
				: null;
	}

	@Override
	public String getLabel() {
		final TypeLibraryType type = getTypeLibraryType();
		return type != null ? type.getLibraryName()
				: SOAProjectConstants.EMPTY_STRING;
	}

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
