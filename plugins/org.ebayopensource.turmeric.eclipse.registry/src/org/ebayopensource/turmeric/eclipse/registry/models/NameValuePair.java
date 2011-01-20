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
package org.ebayopensource.turmeric.eclipse.registry.models;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.Assert;

/**
 * @author yayu
 * @since 1.0.0
 */
public class NameValuePair {
	private String domainName;
	private List<String> classifiers = new ArrayList<String>();

	/**
	 * 
	 */
	public NameValuePair() {
		super();
	}
	
	public NameValuePair(String domainName) {
		super();
		this.domainName = domainName;
	}

	public NameValuePair(String domainName, List<String> classifiers) {
		this(domainName);
		this.classifiers = classifiers;
	}

	public String getDomainName() {
		return domainName;
	}

	public void setDomainName(String domainName) {
		this.domainName = domainName;
	}

	public List<String> getClassifiers() {
		return classifiers;
	}

	public void setClassifiers(List<String> classifiers) {
		Assert.isNotNull(classifiers);
		this.classifiers = classifiers;
	}

	public boolean addClassifier(String classifier) {
		return this.classifiers.add(classifier);
	}

}
