/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.soatools.configtool;

/**
 * The Class InvokeUnit.
 *
 * @author  yualiu
 */
public class InvokeUnit {
	private String[] pathSplit;
	private String attr;
	private boolean addIfNotExist;

	/**
	 * Gets the attr.
	 *
	 * @return the attr
	 */
	public String getAttr() {
		return attr;
	}

	/**
	 * Sets the attr.
	 *
	 * @param attr the attr to set
	 * @return the invoke unit
	 */
	public InvokeUnit setAttr(String attr) {
		this.attr = attr;
		return this;
	}

	/**
	 * Checks if is adds the if not exist.
	 *
	 * @return the addIfNotExist
	 */
	public boolean isAddIfNotExist() {
		return addIfNotExist;
	}

	/**
	 * Sets the add if not exist.
	 *
	 * @param addIfNotExist the addIfNotExist to set
	 * @return the invoke unit
	 */
	public InvokeUnit setAddIfNotExist(boolean addIfNotExist) {
		this.addIfNotExist = addIfNotExist;
		return this;
	}

	/**
	 * Gets the path split.
	 *
	 * @return the pathSplit
	 */
	public String[] getPathSplit() {
		return pathSplit;
	}

	/**
	 * Sets the path split.
	 *
	 * @param split the split
	 * @return the invoke unit
	 */
	public InvokeUnit setPathSplit(String ... split) {
		pathSplit = split;
		return this;
	}

	/**
	 * Gets the x path.
	 *
	 * @return the x path
	 */
	public String getXPath() {
		StringBuilder buf = new StringBuilder();
		for(String str : pathSplit){
			buf.append("/").append(str);
		}
		if(null != attr){
			buf.append("@").append(attr);
		}
		return buf.toString();
	}

	/**
	 * Gets the leaf name.
	 *
	 * @return the leaf name
	 */
	public String getLeafName() {		
		return pathSplit[pathSplit.length - 1];
	}

}