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
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

import org.apache.commons.lang.ObjectUtils;
import org.ebayopensource.turmeric.eclipse.utils.collections.ListUtil;
import org.ebayopensource.turmeric.eclipse.utils.collections.Transformer;
import org.junit.Assert;
import org.junit.Test;


/**
 * @author yayu
 *
 */
public class TestListUtil {

	/**
	 * Test method for {@link org.ebayopensource.turmeric.maveneclipseapi.internal.collections.ListUtil#transformed(org.ebayopensource.turmeric.maveneclipseapi.internal.collections.Transformer, java.util.List)}.
	 */
	@Test
	public void testTransformedTransformerOfTListOfQ() {
		List<String> collection = new ArrayList<String>();
		collection.add("Arc' Terryx");
		collection.add("Mountain Hardwear");
		collection.add("Black Diamond");
		final Transformer< String > transformer = new Transformer< String >()
		{
			private static final long serialVersionUID = 1631027078150099602L;
			@Override
			public String transform( final Object input )
			{
				return ObjectUtils.toString( input ).toLowerCase();
			}
		};
		List<String> list = ListUtil.transformed(transformer, collection);
		for (int i = 0; i < collection.size(); i++) {
			String expected = collection.get(i);
			String actual = list.get(i);
			Assert.assertNotSame(expected, actual);
			Assert.assertTrue(expected.equalsIgnoreCase(actual));
		}
		String item = "3dfx";
		list.add(item);
		Assert.assertTrue(list.contains(item));
		list.remove(item);
		Assert.assertFalse(list.contains(item));
		list.add(0, item);
		Assert.assertTrue(list.indexOf(item) == 0);
		Assert.assertTrue(list.lastIndexOf(item) == 0);
		list.remove(0);
		list.set(0, item);
		Assert.assertTrue(list.indexOf(item) == 0);
		
		Collection<String> data = new ArrayList<String>(2);
		data.add("Voodoo");
		data.add("Radeon");
		list.addAll(data);
		Assert.assertTrue(list.containsAll(data));
		list.removeAll(data);
		Assert.assertFalse(list.containsAll(data));
		list.addAll(0, data);
		list.retainAll(data);
	}

	/**
	 * Test method for {@link org.ebayopensource.turmeric.maveneclipseapi.internal.collections.ListUtil#array(T[])}.
	 */
	@Test
	public void testArrayTArray() {
		Assert.assertNotNull(ListUtil.array());
		Assert.assertTrue(ListUtil.array().isEmpty());
		String[] data = {"3dfx", "Nvidia", "ATI"};
		List<String> list = ListUtil.array(data);
		Assert.assertArrayEquals("data not equals", data, list.toArray(new String[0]));
	}

	/**
	 * Test method for {@link org.ebayopensource.turmeric.maveneclipseapi.internal.collections.ListUtil#array(java.util.Collection)}.
	 */
	@Test
	public void testArrayCollectionOfT() {
		Collection<String> collection = new ArrayList<String>();
		collection.add("3dfx");
		collection.add("Nvidia");
		collection.add("ATI");
		List<String> list = ListUtil.array(collection);
		Assert.assertEquals("data not equals", collection, list);
	}
	
	/**
	 * Test method for {@link org.ebayopensource.turmeric.maveneclipseapi.internal.collections.ListUtil#arrayList()}.
	 */
	@Test
	public void testArrayList() {
		Collection<String> collection = new ArrayList<String>();
		collection.add("3dfx");
		collection.add("Nvidia");
		collection.add("ATI");
		List<String> list = ListUtil.arrayList(collection);
		Assert.assertEquals("data not equals", collection, list);
		
		list = ListUtil.arrayList(collection.toArray(new String[0]));
		Assert.assertEquals("data not equals", collection, list);
	}

	/**
	 * Test method for {@link org.ebayopensource.turmeric.maveneclipseapi.internal.collections.ListUtil#linked(T[])}.
	 */
	@Test
	public void testLinkedTArray() {
		Assert.assertNotNull(ListUtil.linked());
		Assert.assertTrue(ListUtil.linked().isEmpty());
		String[] data = {"3dfx", "Nvidia", "ATI"};
		List<String> list = ListUtil.linked(data);
		Assert.assertArrayEquals("data not equals", data, list.toArray(new String[0]));
	}

	/**
	 * Test method for {@link org.ebayopensource.turmeric.maveneclipseapi.internal.collections.ListUtil#linked(java.util.Collection)}.
	 */
	@Test
	public void testLinkedCollectionOfT() {
		Collection<String> collection = new ArrayList<String>();
		collection.add("3dfx");
		collection.add("Nvidia");
		collection.add("ATI");
		List<String> list = ListUtil.linked(collection);
		Assert.assertEquals("data not equals", collection, list);
	}
	
	/**
	 * Test method for {@link org.ebayopensource.turmeric.maveneclipseapi.internal.collections.ListUtil#linkedList()}.
	 */
	@Test
	public void testLinkedList() {
		Collection<String> collection = new ArrayList<String>();
		collection.add("3dfx");
		collection.add("Nvidia");
		collection.add("ATI");
		List<String> list = ListUtil.linkedList(collection);
		Assert.assertEquals(LinkedList.class.getName(), list.getClass().getName());
		Assert.assertEquals("data not equals", collection, list);
		
		list = ListUtil.linkedList(collection.toArray(new String[0]));
		Assert.assertEquals(LinkedList.class.getName(), list.getClass().getName());
		Assert.assertEquals("data not equals", collection, list);
	}

	/**
	 * Test method for {@link org.ebayopensource.turmeric.maveneclipseapi.internal.collections.ListUtil#newList(T[])}.
	 */
	@Test
	public void testNewListTArray() {
		Assert.assertNotNull(ListUtil.newList());
		Assert.assertTrue(ListUtil.newList().isEmpty());
		String[] data = {"3dfx", "Nvidia", "ATI"};
		List<String> list = ListUtil.newList(data);
		Assert.assertArrayEquals("data not equals", data, list.toArray(new String[0]));
	}

	/**
	 * Test method for {@link org.ebayopensource.turmeric.maveneclipseapi.internal.collections.ListUtil#newList(java.util.Collection)}.
	 */
	@Test
	public void testNewListCollectionOfT() {
		Collection<String> collection = new ArrayList<String>();
		collection.add("3dfx");
		collection.add("Nvidia");
		collection.add("ATI");
		List<String> list = ListUtil.newList(collection);
		Assert.assertNotSame("should be new list instance", collection, list);
		Assert.assertEquals("data not equals", collection, list);
	}

	/**
	 * Test method for {@link org.ebayopensource.turmeric.maveneclipseapi.internal.collections.ListUtil#list(T[])}.
	 */
	@Test
	public void testListTArray() {
		Assert.assertNotNull(ListUtil.list());
		Assert.assertTrue(ListUtil.list().isEmpty());
		String[] data = {"3dfx", "Nvidia", "ATI"};
		List<String> list = ListUtil.list(data);
		Assert.assertArrayEquals("data not equals", data, list.toArray(new String[0]));
	}
	
	/**
	 * Test method for {@link org.ebayopensource.turmeric.maveneclipseapi.internal.collections.ListUtil#toList(T[])}.
	 */
	@Test
	public void testToList() {
		Assert.assertNotNull(ListUtil.toList());
		Assert.assertTrue(ListUtil.toList().isEmpty());
		String[] data = {"3dfx", "Nvidia", "ATI"};
		List<String> list = ListUtil.toList(data);
		Assert.assertArrayEquals("data not equals", data, list.toArray(new String[0]));
	}

	/**
	 * Test method for {@link org.ebayopensource.turmeric.maveneclipseapi.internal.collections.ListUtil#list(java.util.Collection)}.
	 */
	@Test
	public void testListCollectionOfT() {
		Collection<String> collection = new ArrayList<String>();
		collection.add("3dfx");
		collection.add("Nvidia");
		collection.add("ATI");
		List<String> list = ListUtil.list(collection);
		Assert.assertEquals("data not equals", collection, list);
	}

	/**
	 * Test method for {@link org.ebayopensource.turmeric.maveneclipseapi.internal.collections.ListUtil#add(java.util.List, T[])}.
	 */
	@Test
	public void testAdd() {
		List<String> collection = new ArrayList<String>();
		collection.add("3dfx");
		collection.add("Nvidia");
		collection.add("ATI");
		List<String> list = ListUtil.add(collection);
		Assert.assertSame("data not same", collection, list);
		String item = "S3";
		list = ListUtil.add(collection, item);
		Assert.assertTrue("data not added", list.contains(item));
		item = null;
		list = ListUtil.add(collection, item);
		Assert.assertTrue("null is not added", list.contains(item));
	}
	
	/**
	 * Test method for {@link org.ebayopensource.turmeric.maveneclipseapi.internal.collections.ListUtil#changePosition()}.
	 */
	@Test
	public void testChangePosition() {
		List<String> collection = new ArrayList<String>();
		String item = "3dfx";
		collection.add(item);
		collection.add("Nvidia");
		collection.add("ATI");
		List<String> list = ListUtil.moveOnePositionDown(collection, item);
		Assert.assertTrue("moving item not succeed", list.indexOf(item) == 1);
		
		list = ListUtil.moveOnePositionUp(collection, item);
		Assert.assertTrue("moving item not succeed", list.indexOf(item) == 0);
	}

	/**
	 * Test method for {@link org.ebayopensource.turmeric.maveneclipseapi.internal.collections.ListUtil#addIgnoreNull(java.util.List, T[])}.
	 */
	@Test
	public void testAddIgnoreNull() {
		List<String> collection = new ArrayList<String>();
		collection.add("3dfx");
		collection.add("Nvidia");
		collection.add("ATI");
		List<String> list = ListUtil.addIgnoreNull(collection);
		Assert.assertSame("data not same", collection, list);
		String item = "S3";
		list = ListUtil.addIgnoreNull(collection, item);
		Assert.assertTrue("data not added", list.contains(item));
		item = null;
		list = ListUtil.addIgnoreNull(collection, item);
		Assert.assertFalse("null should not be added", list.contains(item));
	}

	/**
	 * Test method for {@link org.ebayopensource.turmeric.maveneclipseapi.internal.collections.ListUtil#listAdd(java.util.List, T[])}.
	 */
	@Test
	public void testListAdd() {
		List<String> collection = new ArrayList<String>();
		collection.add("3dfx");
		collection.add("Nvidia");
		collection.add("ATI");
		List<String> list = ListUtil.listAdd(collection);
		Assert.assertSame("data not same", collection, list);
		String item = "S3";
		list = ListUtil.listAdd(collection, item);
		Assert.assertTrue("data not added", list.contains(item));
		item = null;
		list = ListUtil.listAdd(collection, item);
		Assert.assertTrue("null is not added", list.contains(item));
	}

	/**
	 * Test method for {@link org.ebayopensource.turmeric.maveneclipseapi.internal.collections.ListUtil#remove(java.util.List, T[])}.
	 */
	@Test
	public void testRemove() {
		List<String> collection = new ArrayList<String>();
		collection.add("3dfx");
		collection.add("Nvidia");
		collection.add("ATI");
		String item = "S3";
		collection.add(item);
		List<String> list = ListUtil.remove(collection);
		Assert.assertSame("data not same", collection, list);
		list = ListUtil.remove(collection, item);
		Assert.assertFalse("data not removed", list.contains(item));
	}

	/**
	 * Test method for {@link org.ebayopensource.turmeric.maveneclipseapi.internal.collections.ListUtil#listRemove(java.util.List, T[])}.
	 */
	@Test
	public void testListRemove() {
		List<String> collection = new ArrayList<String>();
		collection.add("3dfx");
		collection.add("Nvidia");
		collection.add("ATI");
		String item = "S3";
		collection.add(item);
		List<String> list = ListUtil.listRemove(collection);
		Assert.assertSame("data not same", collection, list);
		list = ListUtil.listRemove(collection, item);
		Assert.assertFalse("data not removed", list.contains(item));
	}

	/**
	 * Test method for {@link org.ebayopensource.turmeric.maveneclipseapi.internal.collections.ListUtil#caseless(E[])}.
	 */
	@Test
	public void testCaselessEArray() {
		String[] data = {"S3", "Nvidia", "ATI"};
		List<String> list = ListUtil.caseless(data);
		//list = ListUtil.array(list);
		for (int i = 0; i < data.length; i++) {
			String expected = data[i];
			String actual = list.get(i);
			Assert.assertNotSame(expected, actual);
			Assert.assertTrue(expected.equalsIgnoreCase(actual));
		}
	}

	/**
	 * Test method for {@link org.ebayopensource.turmeric.maveneclipseapi.internal.collections.ListUtil#caseless(java.util.List)}.
	 */
	@Test
	public void testCaselessListOfQ() {
		List<String> collection = new ArrayList<String>();
		collection.add("S3");
		collection.add("Nvidia");
		collection.add("ATI");
		List<String> list = ListUtil.caseless(collection);
		for (int i = 0; i < collection.size(); i++) {
			String expected = collection.get(i);
			String actual = list.get(i);
			Assert.assertNotSame(expected, actual);
			Assert.assertTrue(expected.equalsIgnoreCase(actual));
		}
	}

	/**
	 * Test method for {@link org.ebayopensource.turmeric.maveneclipseapi.internal.collections.ListUtil#vector(T[])}.
	 */
	@Test
	public void testVectorTArray() {
		String[] data = {"3dfx", "Nvidia", "ATI"};
		List<String> list = ListUtil.vector(data);
		Assert.assertEquals(Vector.class.getName(), list.getClass().getName());
		Assert.assertArrayEquals("data not equals", data, list.toArray(new String[0]));
		
	}

	/**
	 * Test method for {@link org.ebayopensource.turmeric.maveneclipseapi.internal.collections.ListUtil#vector(java.util.Collection)}.
	 */
	@Test
	public void testVectorCollectionOfT() {
		List<String> collection = new ArrayList<String>();
		collection.add("3dfx");
		collection.add("Nvidia");
		collection.add("ATI");
		List<String> list = ListUtil.vector(collection);
		Assert.assertEquals(Vector.class.getName(), list.getClass().getName());
		Assert.assertEquals("data not same", collection, list);
	}

}
