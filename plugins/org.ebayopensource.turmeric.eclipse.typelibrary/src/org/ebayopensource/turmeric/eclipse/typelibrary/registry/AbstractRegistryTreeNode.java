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
package org.ebayopensource.turmeric.eclipse.typelibrary.registry;

import org.ebayopensource.turmeric.eclipse.resources.constants.SOAProjectConstants;

/**
 * @author yayu
 *
 */
public abstract class AbstractRegistryTreeNode implements IRegistryTreeNode {

	private Object node;
	
	private IRegistryTreeNode parent;
	
	/**
	 * 
	 */
	public AbstractRegistryTreeNode() {
		super();
	}
	
	public AbstractRegistryTreeNode(IRegistryTreeNode parent, 
			Object node) {
		this();
		this.parent = parent;
		this.node = node;
	}

	/* (non-Javadoc)
	 * @see org.ebayopensource.turmeric.eclipse.typelibrary.registry.IRegistryTreeNode#getNode()
	 */
	public Object getNode() {
		return node;
	}

	/* (non-Javadoc)
	 * @see org.ebayopensource.turmeric.eclipse.typelibrary.registry.IRegistryTreeNode#getParent()
	 */
	public IRegistryTreeNode getParent() {
		return parent;
	}

	/* (non-Javadoc)
	 * @see org.ebayopensource.turmeric.eclipse.typelibrary.registry.IRegistryTreeNode#setNode(java.lang.Object)
	 */
	public void setNode(Object object) {
		this.node = object;
	}

	/* (non-Javadoc)
	 * @see org.ebayopensource.turmeric.eclipse.typelibrary.registry.IRegistryTreeNode#setParent(org.ebayopensource.turmeric.eclipse.typelibrary.registry.IRegistryTreeNode)
	 */
	public void setParent(IRegistryTreeNode parent) {
		this.parent = parent;
	}
	
	/* (non-Javadoc)
	 * @see org.ebayopensource.turmeric.eclipse.typelibrary.registry.IRegistryTreeNode#getLabel()
	 */
	public String getLabel() {
		return getNode() != null ? getNode().toString() 
				: SOAProjectConstants.EMPTY_STRING;
	}

}
