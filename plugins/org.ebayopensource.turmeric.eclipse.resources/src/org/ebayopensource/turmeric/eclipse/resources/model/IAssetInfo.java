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
package org.ebayopensource.turmeric.eclipse.resources.model;

/**
 * @author yayu
 * @since 1.0.0
 */
public interface IAssetInfo {
	public static final String TYPE_PROJECT = "Project";
	public static final String TYPE_LIBRARY = "Library";
	public static final String TYPE_SERVICE_PROJECT = "Project_Service";
	public static final String TYPE_SERVICE_LIBRARY = "Library_Service";

	/**
	 * The name of the asset
	 * @return
	 */
	public String getName();
	
	/**
	 * The type of the asset
	 * @return
	 */
	public String getType();

	/**
	 * The version of the asset
	 * @return
	 */
	public String getVersion();
	
	/**
	 * An ID that could uniquely identify the asset 
	 * @return
	 */
	public String getUniqueID();

	/**
	 * A long description of the asset
	 * @return
	 */
	public String getDescription();
	
	/**
	 * A brief description of the asset
	 * @return
	 */
	public String getShortDescription();

}
