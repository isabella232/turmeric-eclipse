/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.utils.test.collections;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

import org.ebayopensource.turmeric.eclipse.utils.collections.MapUtil;
import org.junit.Assert;
import org.junit.Test;


/**
 * @author yayu
 *
 */
public class TestMapUtil {

	/**
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.utils.collections.MapUtil#toArray(java.util.Map)}.
	 */
	@Test
	public void testToArrayMapOfKV() {
		Map<String, String> data = new LinkedHashMap<String, String>();
		data.put("flickr", "http://www.flickr.com");
		data.put("twitter", "http://twitter.com");
		data.put("bbc", "http://www.bbc.co.uk");
		Object[] array = MapUtil.toArray(data);
		System.out.println(Arrays.asList(array));
		Assert.assertEquals("Data is not expected", "flickr", array[0]);
		Assert.assertEquals("Data is not expected", "http://www.bbc.co.uk", array[array.length - 1]);
	}

	/**
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.utils.collections.MapUtil#toArray(java.util.Map, boolean)}.
	 */
	@Test
	public void testToArrayMapOfKVBoolean() {
		Map<String, String> data = new LinkedHashMap<String, String>();
		data.put("flickr", "http://www.flickr.com");
		data.put("twitter", "http://twitter.com");
		data.put("bbc", "http://www.bbc.co.uk");
		Object[] array = MapUtil.toArray(data, false);
		System.out.println(Arrays.asList(array));
		Assert.assertEquals("Data is not expected", "flickr", array[0]);
		Assert.assertEquals("Data is not expected", "http://www.bbc.co.uk", array[array.length - 1]);
		
		data.put(null, "data");
		array = MapUtil.toArray(data, true);
		System.out.println(Arrays.asList(array));
		Assert.assertEquals("Data is not expected", "flickr", array[0]);
		Assert.assertEquals("Data is not expected", "data", array[array.length - 1]);
	}

	/**
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.utils.collections.MapUtil#toArray(java.util.Map, T[])}.
	 */
	@Test
	public void testToArrayMapOfKVTArray() {
		Map<String, String> data = new LinkedHashMap<String, String>();
		data.put("flickr", "http://www.flickr.com");
		data.put("twitter", "http://twitter.com");
		data.put("bbc", "http://www.bbc.co.uk");
		Object[] array = MapUtil.toArray(data, new String[0]);
		System.out.println(Arrays.asList(array));
		Assert.assertEquals("Data is not expected", "flickr", array[0]);
		Assert.assertEquals("Data is not expected", "http://www.bbc.co.uk", array[array.length - 1]);
		
	}

	/**
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.utils.collections.MapUtil#toArray(java.util.Map, T[], boolean)}.
	 */
	@Test
	public void testToArrayMapOfKVTArrayBoolean() {
		Map<String, String> data = new LinkedHashMap<String, String>();
		data.put("flickr", "http://www.flickr.com");
		data.put("twitter", "http://twitter.com");
		data.put("bbc", "http://www.bbc.co.uk");
		data.put(null, "null");
		Object[] array = MapUtil.toArray(data, new String[0], true);
		System.out.println(Arrays.asList(array));
		Assert.assertEquals("Data is not expected", "flickr", array[0]);
		Assert.assertEquals("Data is not expected", "http://www.bbc.co.uk", array[array.length - 2]);
		Assert.assertEquals("Data is not expected", "null", array[array.length - 1]);
	}

}
