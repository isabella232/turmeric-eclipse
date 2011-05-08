/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.errorlibrary.views;

import java.util.Collection;

/**
 * The Interface ISOAErrRegistry.
 *
 * @author smathew
 * 
 * Interface that has to be implemented by participant implementation frameworks
 * Implementing classes can be contributed via an extension point to this core
 * error library plugin
 */
public interface ISOAErrRegistry {

	/**
	 * Returns all the libraries in the view/repository This has to be a unique
	 * set.
	 *
	 * @return the libraries
	 */
	public Collection<ISOAErrLibrary> getLibraries();
	
	/**
	 * Refresh the entire registry.
	 *
	 * @throws Exception the exception
	 */
	public void refreshRegistry() throws Exception; 

	/**
	 * Perform all the after import tasks here. Import is importing/using an
	 * error in the impl or client
	 * 
	 * @return false if this operation fails. true otherwise
	 */
	public boolean importError();
}
