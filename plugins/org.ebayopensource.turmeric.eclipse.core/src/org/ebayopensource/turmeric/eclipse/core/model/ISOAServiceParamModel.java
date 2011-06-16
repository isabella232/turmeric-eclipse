/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.core.model;


/**
 * @author smathew
 * 
 * The model interface. Right now we mandate that it should have a type. Later
 * this might be enhanced
 */
public interface ISOAServiceParamModel {

	/**
	 * @return validates if this model object is well formed
	 * 
	 */
	public boolean validate();

}
