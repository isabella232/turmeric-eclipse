/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.repositorysystem.core;

/**
 * Now we have two codegen systems: Opensource codgen and eBay codegen. It is
 * needed to provide organization level codegen
 * 
 * @author mzang
 * 
 */
public interface ISOACodegenProvider {

	/**
	 * Call codegen of specified system.
	 *
	 * @param parameters coegen parameters
	 * @return true if success
	 * @throws Exception the exception
	 */
	public boolean generateCode(String[] parameters) throws Exception;

	/**
	 * codegen folder. It is different for different codegen system. Opensource
	 * codegen starts with src.
	 *
	 * @return the gen folder for intf
	 */
	public String getGenFolderForIntf();

	/**
	 * codegen folder. It is different for different codegen system. Opensource
	 * codegen starts with src.
	 *
	 * @return the gen folder for impl
	 */
	public String getGenFolderForImpl();

}
