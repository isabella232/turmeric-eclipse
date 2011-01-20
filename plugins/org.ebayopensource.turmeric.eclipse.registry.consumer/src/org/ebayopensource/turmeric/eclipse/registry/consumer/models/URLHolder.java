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
 * URL Holder
 * @author ramurthy
 */

public class URLHolder {

	private static URLHolder fURLHolder = null;
	private String fRepoServiceURL = null;
	private String fRepoMetadataServiceURL = null;
	private String fAssertionsServiceURL = null;
	private static boolean fValid;
	
	public static synchronized URLHolder getURLHolderInstance() {
		if (fURLHolder == null && !fValid) {
			fURLHolder = new URLHolder();
			fValid = true;
		}
		return fURLHolder;
	}
	
	public void setRepoServiceURL(String repoServiceURL) {
		fRepoServiceURL = repoServiceURL;
	}
	
	public void setRepoMetadataServiceURL(String repoMetadataServiceURL) {
		fRepoMetadataServiceURL = repoMetadataServiceURL;
	}

	public String getRepoServiceURL() {
		return fRepoServiceURL;
	}

	public String getRepoMetadataServiceURL() {
		return fRepoMetadataServiceURL;
	}
	
	public void invalidateURLHolder() {
		if (fValid) {
			fURLHolder = null;
			fValid = false;
		}
	}

	public String getAssertionsServiceURL() {
		return fAssertionsServiceURL;
	}

	public void setAssertionsServiceURL(String assertionsServiceURL) {
		fAssertionsServiceURL = assertionsServiceURL;
	}
	
}
