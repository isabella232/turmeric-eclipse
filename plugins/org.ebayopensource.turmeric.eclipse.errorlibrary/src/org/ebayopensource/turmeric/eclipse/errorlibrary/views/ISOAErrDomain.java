/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.errorlibrary.views;

import java.util.Set;

/**
 * The Interface ISOAErrDomain.
 *
 * @author smathew
 * 
 * Represents an error domain object. Typically this class would be implemented
 * by different implementing frameworks like V4, properties based implementation
 * etc
 */
public interface ISOAErrDomain extends ISOAErrUIComp{

	/**
	 * returns all the errors inside a domain.
	 *
	 * @return the errors
	 */
	Set<ISOAError> getErrors();

	/**
	 * return the organization property for this domain.
	 *
	 * @return the organization
	 */
	String getOrganization();
	
	/**
	 * return the parent library to which this domain belong to.
	 *
	 * @return the library
	 */
	ISOAErrLibrary getLibrary();
	
	/**
	 * Adds the error.
	 *
	 * @param error the error
	 */
	void addError(ISOAError error);
	

}
