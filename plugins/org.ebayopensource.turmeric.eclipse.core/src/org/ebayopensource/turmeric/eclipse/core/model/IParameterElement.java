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
 * The Interface IParameterElement.
 */
public  interface IParameterElement {
	
	/**
	 * Gets the name.
	 *
	 * @return the parameter name
	 */
	public String getName();

	/**
	 * Sets the name.
	 *
	 * @param name the parameter name
	 */
	public void setName(String name);

	/**
	 * Gets the datatype.
	 *
	 * @return an instance of LibraryType(complex type)
	 * or String(primitive type)
	 */
	public Object getDatatype();

	/**
	 * Sets the datatype.
	 *
	 * @param datatype Must be either String or instance of LibraryType
	 */
	public void setDatatype(Object datatype);

	/**
	 * Gets the min occurs.
	 *
	 * @return minimum occurrence
	 */
	public int getMinOccurs();

	/**
	 * Sets the min occurs.
	 *
	 * @param minOccurs maximum occurrence
	 * @throws IllegalArgumentException the illegal argument exception
	 */
	public void setMinOccurs(int minOccurs) throws IllegalArgumentException;

	/**
	 * Gets the max occurs.
	 *
	 * @return maximum occurrence
	 */
	public int getMaxOccurs();
	
	/**
	 * Sets the max occurs.
	 *
	 * @param maxOccurs minimum occurrence
	 * @throws IllegalArgumentException the illegal argument exception
	 */

	public void setMaxOccurs(int maxOccurs) throws IllegalArgumentException;
}
