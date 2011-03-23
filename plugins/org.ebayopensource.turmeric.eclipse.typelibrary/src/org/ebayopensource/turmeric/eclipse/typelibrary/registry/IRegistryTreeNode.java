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

/**
 * @author yayu
 *
 */
public interface IRegistryTreeNode {

	/**
	 * Set parent node
	 * @param parent
	 */
	public void setParent(IRegistryTreeNode parent);
	
	/**
	 * @return The parent node
	 */
	public IRegistryTreeNode getParent();
	
	/**
	 * @param object The object
	 */
	public void setNode(Object object);
	
	/**
	 * @return The underlying object
	 */
	public Object getNode();
	
	/**
	 * @return The label of the node
	 */
	public String getLabel();
}
