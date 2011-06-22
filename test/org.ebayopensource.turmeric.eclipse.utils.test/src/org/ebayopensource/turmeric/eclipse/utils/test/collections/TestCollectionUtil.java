/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.utils.test.collections;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import junit.framework.Assert;

import org.ebayopensource.turmeric.eclipse.utils.collections.CollectionUtil;
import org.junit.Test;


/**
 * @author yayu
 *
 */
public class TestCollectionUtil {

	/**
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.utils.collections.CollectionUtil#intersection(java.util.Set, java.util.Set)}.
	 */
	@Test
	public void testIntersection() {
		Set<String> collection = new HashSet<String>();
		collection.add("3dfx");
		collection.add("Nvidia");
		collection.add("ATI");
		
		Set<String> collection2 = new HashSet<String>();
		collection2.add("3dfx");
		collection2.add("ATI");
		collection2.add("Trident");
		
		Set<String> expected = new HashSet<String>();
		expected.add("3dfx");
		expected.add("ATI");
		Set<String> data = CollectionUtil.intersection(collection, collection2);
		TestSetUtil.assertSetEquals(expected, data);
	}

	/**
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.utils.collections.CollectionUtil#twoWayDiff(java.util.Set, java.util.Set)}.
	 */
	@Test
	public void testTwoWayDiff() {
		Set<String> collection = new HashSet<String>();
		collection.add("3dfx");
		collection.add("Nvidia");
		collection.add("ATI");
		
		Set<String> collection2 = new HashSet<String>();
		collection2.add("3dfx");
		collection2.add("ATI");
		collection2.add("Trident");
		
		Set<String> expected = new HashSet<String>();
		expected.add("Nvidia");
		expected.add("Trident");
		Set<String> data = CollectionUtil.twoWayDiff(collection, collection2);
		TestSetUtil.assertSetEquals(expected, data);
	}

	/**
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.utils.collections.CollectionUtil#oneWayDiff(java.util.Set, java.util.Set)}.
	 */
	@Test
	public void testOneWayDiff() {
		Set<String> collection = new HashSet<String>();
		collection.add("3dfx");
		collection.add("Nvidia");
		collection.add("ATI");
		
		Set<String> collection2 = new HashSet<String>();
		collection2.add("3dfx");
		collection2.add("ATI");
		collection2.add("Trident");
		
		Set<String> expected = new HashSet<String>();
		expected.add("Nvidia");
		Set<String> data = CollectionUtil.oneWayDiff(collection, collection2);
		TestSetUtil.assertSetEquals(expected, data);
	}

	/**
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.utils.collections.CollectionUtil#isEmpty(java.util.Collection)}.
	 */
	@Test
	public void testIsEmpty() {
		Assert.assertTrue(CollectionUtil.isEmpty(null));
		Assert.assertTrue(CollectionUtil.isEmpty(new ArrayList<String>()));
	}

}
