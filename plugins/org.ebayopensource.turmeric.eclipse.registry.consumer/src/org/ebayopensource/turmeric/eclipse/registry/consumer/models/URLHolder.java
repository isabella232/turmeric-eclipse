/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.registry.consumer.models;

/**
 * URL Holder.
 *
 * @author ramurthy
 */

public class URLHolder {

	private static URLHolder fURLHolder = null;
	private String fRepoServiceURL = null;
	private String fRepoMetadataServiceURL = null;
	private String fAssertionsServiceURL = null;
	private static boolean fValid;
	
	/**
	 * Gets the uRL holder instance.
	 *
	 * @return the uRL holder instance
	 */
	public static synchronized URLHolder getURLHolderInstance() {
		if (fURLHolder == null && !fValid) {
			fURLHolder = new URLHolder();
			fValid = true;
		}
		return fURLHolder;
	}
	
	/**
	 * Sets the repo service url.
	 *
	 * @param repoServiceURL the new repo service url
	 */
	public void setRepoServiceURL(String repoServiceURL) {
		fRepoServiceURL = repoServiceURL;
	}
	
	/**
	 * Sets the repo metadata service url.
	 *
	 * @param repoMetadataServiceURL the new repo metadata service url
	 */
	public void setRepoMetadataServiceURL(String repoMetadataServiceURL) {
		fRepoMetadataServiceURL = repoMetadataServiceURL;
	}

	/**
	 * Gets the repo service url.
	 *
	 * @return the repo service url
	 */
	public String getRepoServiceURL() {
		return fRepoServiceURL;
	}

	/**
	 * Gets the repo metadata service url.
	 *
	 * @return the repo metadata service url
	 */
	public String getRepoMetadataServiceURL() {
		return fRepoMetadataServiceURL;
	}
	
	/**
	 * Invalidate url holder.
	 */
	public void invalidateURLHolder() {
		if (fValid) {
			fURLHolder = null;
			fValid = false;
		}
	}

	/**
	 * Gets the assertions service url.
	 *
	 * @return the assertions service url
	 */
	public String getAssertionsServiceURL() {
		return fAssertionsServiceURL;
	}

	/**
	 * Sets the assertions service url.
	 *
	 * @param assertionsServiceURL the new assertions service url
	 */
	public void setAssertionsServiceURL(String assertionsServiceURL) {
		fAssertionsServiceURL = assertionsServiceURL;
	}
	
}
