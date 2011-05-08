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
 * The Class NameValuePair.
 *
 * @author yayu
 * @since 1.0.0
 */
public class NameValuePair {
	private String domainName;
	private List<String> classifiers = new ArrayList<String>();

	/**
	 * Instantiates a new name value pair.
	 */
	public NameValuePair() {
		super();
	}
	
	/**
	 * Instantiates a new name value pair.
	 *
	 * @param domainName the domain name
	 */
	public NameValuePair(String domainName) {
		super();
		this.domainName = domainName;
	}

	/**
	 * Instantiates a new name value pair.
	 *
	 * @param domainName the domain name
	 * @param classifiers the classifiers
	 */
	public NameValuePair(String domainName, List<String> classifiers) {
		this(domainName);
		this.classifiers = classifiers;
	}

	/**
	 * Gets the domain name.
	 *
	 * @return the domain name
	 */
	public String getDomainName() {
		return domainName;
	}

	/**
	 * Sets the domain name.
	 *
	 * @param domainName the new domain name
	 */
	public void setDomainName(String domainName) {
		this.domainName = domainName;
	}

	/**
	 * Gets the classifiers.
	 *
	 * @return the classifiers
	 */
	public List<String> getClassifiers() {
		return classifiers;
	}

	/**
	 * Sets the classifiers.
	 *
	 * @param classifiers the new classifiers
	 */
	public void setClassifiers(List<String> classifiers) {
		Assert.isNotNull(classifiers);
		this.classifiers = classifiers;
	}

	/**
	 * Adds the classifier.
	 *
	 * @param classifier the classifier
	 * @return true, if successful
	 */
	public boolean addClassifier(String classifier) {
		return this.classifiers.add(classifier);
	}

}
