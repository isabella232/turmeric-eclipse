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

// TODO: Auto-generated Javadoc
/**
 * The Interface IRegistryTreeNode.
 *
 * @author yayu
 */
public interface IRegistryTreeNode {

	/**
	 * Set parent node.
	 *
	 * @param parent the new parent
	 */
	public void setParent(IRegistryTreeNode parent);
	
	/**
	 * Gets the parent.
	 *
	 * @return The parent node
	 */
	public IRegistryTreeNode getParent();
	
	/**
	 * Sets the node.
	 *
	 * @param object The object
	 */
	public void setNode(Object object);
	
	/**
	 * Gets the node.
	 *
	 * @return The underlying object
	 */
	public Object getNode();
	
	/**
	 * Gets the label.
	 *
	 * @return The label of the node
	 */
	public String getLabel();
}
