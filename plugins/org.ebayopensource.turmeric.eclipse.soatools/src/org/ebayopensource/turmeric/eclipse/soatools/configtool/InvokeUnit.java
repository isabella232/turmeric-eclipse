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
 * @author  yualiu
 */
public class InvokeUnit {
	private String[] pathSplit;
	private String attr;
	private boolean addIfNotExist;

	/**
	 * @return the attr
	 */
	public String getAttr() {
		return attr;
	}

	/**
	 * @param attr the attr to set
	 */
	public InvokeUnit setAttr(String attr) {
		this.attr = attr;
		return this;
	}

	/**
	 * @return the addIfNotExist
	 */
	public boolean isAddIfNotExist() {
		return addIfNotExist;
	}

	/**
	 * @param addIfNotExist the addIfNotExist to set
	 */
	public InvokeUnit setAddIfNotExist(boolean addIfNotExist) {
		this.addIfNotExist = addIfNotExist;
		return this;
	}

	/**
	 * @return the pathSplit
	 */
	public String[] getPathSplit() {
		return pathSplit;
	}

	/**
	 * @param split
	 * @return
	 */
	public InvokeUnit setPathSplit(String ... split) {
		pathSplit = split;
		return this;
	}

	/**
	 * @return
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
	 * @return
	 */
	public String getLeafName() {		
		return pathSplit[pathSplit.length - 1];
	}

}