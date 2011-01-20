/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.mavenapi.test.internal.collections;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.TreeSet;

import org.ebayopensource.turmeric.eclipse.mavenapi.internal.collections.SetUtil;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author yayu
 *
 */
public class TestSetUtil {

	/**
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.mavenapi.internal.collections.SetUtil#treeSet(T[])}.
	 */
	@Test
	public void testTreeSetTArray() {
		Assert.assertNotNull(SetUtil.treeSet());
		Assert.assertTrue(SetUtil.treeSet().isEmpty());
		String[] data = {"3dfx", "Nvidia", "ATI"};
		Set<String> list = SetUtil.treeSet(data);
		Assert.assertEquals(TreeSet.class.getName(), list.getClass().getName());
		Set<String> set = new HashSet<String>();
		set.addAll(Arrays.asList(data));
		assertSetEquals(set, list);
	}
	
	private static <T> void assertSetEquals(Set<T> expected, Set<T> actual) {
		Assert.assertEquals(expected.size(), actual.size());
		for (T data : expected) {
			Assert.assertTrue(actual.contains(data));
		}
	}

	/**
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.mavenapi.internal.collections.SetUtil#treeSet(java.util.Collection)}.
	 */
	@Test
	public void testTreeSetCollectionOfT() {
		Set<String> collection = new HashSet<String>();
		collection.add("3dfx");
		collection.add("Nvidia");
		collection.add("ATI");
		Set<String> set = SetUtil.treeSet(collection);
		Assert.assertEquals(TreeSet.class.getName(), set.getClass().getName());
		assertSetEquals(collection, set);
	}

	/**
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.mavenapi.internal.collections.SetUtil#hashSet(T[])}.
	 */
	@Test
	public void testHashSetTArray() {
		Assert.assertNotNull(SetUtil.hashSet());
		Assert.assertTrue(SetUtil.hashSet().isEmpty());
		String[] data = {"3dfx", "Nvidia", "ATI"};
		Set<String> list = SetUtil.hashSet(data);
		Assert.assertEquals(HashSet.class.getName(), list.getClass().getName());
		Set<String> set = new HashSet<String>();
		set.addAll(Arrays.asList(data));
		assertSetEquals(set, list);
	}

	/**
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.mavenapi.internal.collections.SetUtil#hashSet(java.util.Collection)}.
	 */
	@Test
	public void testHashSetCollectionOfT() {
		Set<String> collection = new HashSet<String>();
		collection.add("3dfx");
		collection.add("Nvidia");
		collection.add("ATI");
		Set<String> set = SetUtil.hashSet(collection);
		Assert.assertEquals(HashSet.class.getName(), set.getClass().getName());
		assertSetEquals(collection, set);
	}

	/**
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.mavenapi.internal.collections.SetUtil#linkedSet(T[])}.
	 */
	@Test
	public void testLinkedSetTArray() {
		Assert.assertNotNull(SetUtil.linkedSet());
		Assert.assertTrue(SetUtil.linkedSet().isEmpty());
		String[] data = {"3dfx", "Nvidia", "ATI"};
		Set<String> list = SetUtil.linkedSet(data);
		Assert.assertEquals(LinkedHashSet.class.getName(), list.getClass().getName());
		Set<String> set = new HashSet<String>();
		set.addAll(Arrays.asList(data));
		assertSetEquals(set, list);
	}

	/**
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.mavenapi.internal.collections.SetUtil#linkedSet(java.util.Collection)}.
	 */
	@Test
	public void testLinkedSetCollectionOfT() {
		Set<String> collection = new HashSet<String>();
		collection.add("3dfx");
		collection.add("Nvidia");
		collection.add("ATI");
		Set<String> set = SetUtil.linkedSet(collection);
		Assert.assertEquals(LinkedHashSet.class.getName(), set.getClass().getName());
		assertSetEquals(collection, set);
	}

	/**
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.mavenapi.internal.collections.SetUtil#set(java.util.Collection)}.
	 */
	@Test
	public void testSetCollectionOfT() {
		Set<String> collection = new HashSet<String>();
		collection.add("3dfx");
		collection.add("Nvidia");
		collection.add("ATI");
		Set<String> set = SetUtil.set(collection);
		assertSetEquals(collection, set);
	}

	/**
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.mavenapi.internal.collections.SetUtil#set(T[])}.
	 */
	@Test
	public void testSetTArray() {
		Assert.assertNotNull(SetUtil.set());
		Assert.assertTrue(SetUtil.set().isEmpty());
		String[] data = {"3dfx", "Nvidia", "ATI"};
		Set<String> list = SetUtil.set(data);
		Set<String> set = new HashSet<String>();
		set.addAll(Arrays.asList(data));
		assertSetEquals(set, list);
	}

	/**
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.mavenapi.internal.collections.SetUtil#add(java.util.Set, T[])}.
	 */
	@Test
	public void testAdd() {
		Set<String> collection = new HashSet<String>();
		collection.add("3dfx");
		collection.add("Nvidia");
		collection.add("ATI");
		Assert.assertNotNull(SetUtil.add(collection));
		Assert.assertSame(collection, SetUtil.add(collection));
		String item = "S3";
		collection.add(item);
		Set<String> list = SetUtil.add(collection, item);
		
		assertSetEquals(collection, list);
	}

	/**
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.mavenapi.internal.collections.SetUtil#setAdd(java.util.Set, T[])}.
	 */
	@Test
	public void testSetAdd() {
		Set<String> collection = new HashSet<String>();
		collection.add("3dfx");
		collection.add("Nvidia");
		collection.add("ATI");
		Assert.assertNotNull(SetUtil.setAdd(collection));
		Assert.assertSame(collection, SetUtil.setAdd(collection));
		String item = "S3";
		collection.add(item);
		Set<String> list = SetUtil.setAdd(collection, item);
		
		assertSetEquals(collection, list);
	}

	/**
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.mavenapi.internal.collections.SetUtil#remove(java.util.Set, T[])}.
	 */
	@Test
	public void testRemove() {
		Set<String> collection = new HashSet<String>();
		collection.add("3dfx");
		collection.add("Nvidia");
		collection.add("ATI");
		String item = "S3";
		collection.add(item);
		Assert.assertNotNull(SetUtil.remove(collection));
		Assert.assertSame(collection, SetUtil.remove(collection));
		
		Set<String> list = SetUtil.remove(collection, item);
		Assert.assertFalse(list.contains(item));
	}

	/**
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.mavenapi.internal.collections.SetUtil#setRemove(java.util.Set, T[])}.
	 */
	@Test
	public void testSetRemove() {
		Set<String> collection = new HashSet<String>();
		collection.add("3dfx");
		collection.add("Nvidia");
		collection.add("ATI");
		String item = "S3";
		collection.add(item);
		Assert.assertNotNull(SetUtil.setRemove(collection));
		Assert.assertSame(collection, SetUtil.setRemove(collection));
		
		Set<String> list = SetUtil.setRemove(collection, item);
		Assert.assertFalse(list.contains(item));
	}

}
