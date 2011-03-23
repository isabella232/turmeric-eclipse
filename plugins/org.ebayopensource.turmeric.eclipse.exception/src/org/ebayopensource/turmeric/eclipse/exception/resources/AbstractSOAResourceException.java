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
package org.ebayopensource.turmeric.eclipse.exception.resources;

import org.ebayopensource.turmeric.eclipse.exception.AbstractSOAException;
import org.eclipse.core.resources.IResource;


/**
 * @author yayu
 *
 */
public abstract class AbstractSOAResourceException extends AbstractSOAException {
	private IResource resource;

	/**
	 * @param message
	 */
	public AbstractSOAResourceException(String message, IResource resource) {
		super(message);
		this.resource = resource;
	}

	/**
	 * @param cause
	 */
	public AbstractSOAResourceException(IResource resource, Throwable cause) {
		super(cause);
		this.resource = resource;
	}

	/**
	 * @param message
	 * @param cause
	 */
	public AbstractSOAResourceException(String message, IResource resource, Throwable cause) {
		super(message, cause);
		this.resource = resource;
	}

	@Override
	public String getMessage() {
		final StringBuffer buf = new StringBuffer();
		buf.append(super.getMessage());
		if (this.resource != null) {
			buf.append("\nAffected Resource: ");
			buf.append(this.resource.getLocation());
		}
		
		return buf.toString();
	}
	
	public IResource getResource() {
		return this.resource;
	}

}
