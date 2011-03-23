/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.resources.ui.model;


/**
 * 
 * 
 *
 */
public  interface IParameterElement {
	/**
	 * 
	 * @return the parameter name
	 */
	public String getName();

	/**
	 * 
	 * @param name the parameter name
	 */
	public void setName(String name);

	/**
	 * 
	 * @return an instance of LibraryType(complex type)
	 *  or String(primitive type)
	 */
	public Object getDatatype();

	/**
	 * @param datatype Must be either String or instance of LibraryType
	 */
	public void setDatatype(Object datatype);

	/**
	 * 
	 * @return minimum occurrence
	 */
	public int getMinOccurs();

	/**
	 * 
	 * @param minOccurs maximum occurrence
	 * @throws IllegalArgumentException 
	 */
	public void setMinOccurs(int minOccurs) throws IllegalArgumentException;

	/**
	 * 
	 * @return maximum occurrence
	 */
	public int getMaxOccurs();
	
	/**
	 * 
	 * @param maxOccurs minimum occurrence
	 * @throws IllegalArgumentException 
	 */

	public void setMaxOccurs(int maxOccurs) throws IllegalArgumentException;
}
