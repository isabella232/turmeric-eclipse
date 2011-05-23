/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.registry.consumer.models;

import java.util.Comparator;

/**
 * AssertionsSeverityModelComparator compares ArtifactValidationResult on the basis of 
 * the severity of the error set in the instance of the class.
 * <p>
 * The ArtifactValidationResult object having the severity MUST is placed
 * before the object having the severity SHOULD and then MAY.
 * </p>
 * @author asagarwal
 */

public class AssertionsSeverityModelComparator implements Comparator<ArtifactValidationResult> {

	/**
	 * Compare the two objects to determine
	 * the relative ordering.
	 *
	 * @param o1 the o1
	 * @param o2 the o2
	 * @return the int
	 */
	@Override
	public int compare(ArtifactValidationResult o1, ArtifactValidationResult o2) {
		return o1.getAssertionsSeverityModel().ordinal() - o2.getAssertionsSeverityModel().ordinal();
	}
	
}
