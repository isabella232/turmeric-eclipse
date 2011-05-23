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

import org.ebayopensource.turmeric.eclipse.core.resources.constants.SOAProjectConstants;

/**
 * The Class AbstractRegistryTreeNode.
 *
 * @author yayu
 */
public abstract class AbstractRegistryTreeNode implements IRegistryTreeNode {

	private Object node;
	
	private IRegistryTreeNode parent;
	
	/**
	 * Instantiates a new abstract registry tree node.
	 */
	public AbstractRegistryTreeNode() {
		super();
	}
	
	/**
	 * Instantiates a new abstract registry tree node.
	 *
	 * @param parent the parent
	 * @param node the node
	 */
	public AbstractRegistryTreeNode(IRegistryTreeNode parent, 
			Object node) {
		this();
		this.parent = parent;
		this.node = node;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object getNode() {
		return node;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IRegistryTreeNode getParent() {
		return parent;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setNode(Object object) {
		this.node = object;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setParent(IRegistryTreeNode parent) {
		this.parent = parent;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getLabel() {
		return getNode() != null ? getNode().toString() 
				: SOAProjectConstants.EMPTY_STRING;
	}

}
