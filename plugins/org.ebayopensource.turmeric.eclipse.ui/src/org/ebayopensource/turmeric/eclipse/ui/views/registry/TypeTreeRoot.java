/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.ui.views.registry;

import org.ebayopensource.turmeric.tools.library.SOATypeRegistry;

/**
 * The Class TypeTreeRoot.
 *
 * @author smathew
 */
public class TypeTreeRoot extends AbstractRegistryTreeNode{

	/**
	 * Instantiates a new type tree root.
	 *
	 * @param typeRegistry the type registry
	 */
	public TypeTreeRoot(SOATypeRegistry typeRegistry){
		super((IRegistryTreeNode)null, typeRegistry);
	}
	
	/**
	 * Gets the type registry.
	 *
	 * @return the type registry
	 */
	public SOATypeRegistry getTypeRegistry() {
		return getNode() instanceof SOATypeRegistry 
		? (SOATypeRegistry)getNode() : null;
	}
	
	/**
	 * Sets the type registry.
	 *
	 * @param typeRegistry the new type registry
	 */
	public void setTypeRegistry(SOATypeRegistry typeRegistry) {
		setNode(typeRegistry);
	}
	
	/* (non-Javadoc)
	 * @see org.ebayopensource.turmeric.eclipse.ui.views.registry.AbstractRegistryTreeNode#getLabel()
	 */
	@Override
	public String getLabel() {
		return "All Type Libraries";
	}
}
