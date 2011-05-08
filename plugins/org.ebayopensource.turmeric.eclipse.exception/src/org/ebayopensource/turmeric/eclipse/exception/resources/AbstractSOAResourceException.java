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
 * The Class AbstractSOAResourceException.
 *
 * @author yayu
 */
public abstract class AbstractSOAResourceException extends AbstractSOAException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1955701516596970946L;
	private IResource resource;

	/**
	 * Instantiates a new abstract soa resource exception.
	 *
	 * @param message the message
	 * @param resource the resource
	 */
	public AbstractSOAResourceException(String message, IResource resource) {
		super(message);
		this.resource = resource;
	}

	/**
	 * Instantiates a new abstract soa resource exception.
	 *
	 * @param resource the resource
	 * @param cause the cause
	 */
	public AbstractSOAResourceException(IResource resource, Throwable cause) {
		super(cause);
		this.resource = resource;
	}

	/**
	 * Instantiates a new abstract soa resource exception.
	 *
	 * @param message the message
	 * @param resource the resource
	 * @param cause the cause
	 */
	public AbstractSOAResourceException(String message, IResource resource, Throwable cause) {
		super(message, cause);
		this.resource = resource;
	}

	/* (non-Javadoc)
	 * @see org.ebayopensource.turmeric.eclipse.exception.AbstractSOAException#getMessage()
	 */
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
	
	/**
	 * Gets the resource.
	 *
	 * @return the resource
	 */
	public IResource getResource() {
		return this.resource;
	}

}
