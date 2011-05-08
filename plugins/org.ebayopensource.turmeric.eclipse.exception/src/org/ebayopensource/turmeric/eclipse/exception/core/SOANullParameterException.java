/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.exception.core;

/**
 * Thrown when the parameter to an API is null.
 *
 * @author smathew
 */
public class SOANullParameterException extends SOABadParameterException {


	private static final long serialVersionUID = 1L;

	/**
	 * The order of the parameter If its the first parameter its 1, second
	 * parameter 2 etc
	 */
	private int paramOrder = 0;

	/**
	 * Instantiates a new sOA null parameter exception.
	 *
	 * @param paramOrder The order of the parameter If its the first parameter its 1,
	 * second parameter 2 etc
	 */
	public SOANullParameterException(int paramOrder) {
		this.paramOrder = paramOrder;
	}

	/**
	 * Gets the param order.
	 *
	 * @return the param order
	 */
	public int getParamOrder() {
		return paramOrder;
	}

	/**
	 * Sets the param order.
	 *
	 * @param paramOrder the new param order
	 */
	public void setParamOrder(int paramOrder) {
		this.paramOrder = paramOrder;
	}

}
