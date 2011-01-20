/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.utils.test.lang;

import java.util.ArrayList;
import java.util.Collection;

import org.ebayopensource.turmeric.eclipse.utils.lang.StringUtil;
import org.junit.Assert;
import org.junit.Test;


/**
 * @author yayu
 *
 */
public class TestStringUtil {

	/**
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.utils.lang.StringUtil#removeFirst(java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testRemoveFirst() {
		String data = "AndroidAnd";
		Assert.assertEquals("roidAnd", StringUtil.removeFirst(data, "And"));
		Assert.assertEquals(data, StringUtil.removeFirst(data, "and"));
		Assert.assertEquals(data, StringUtil.removeFirst(data, null));
	}

	/**
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.utils.lang.StringUtil#join(java.util.Collection, java.lang.String)}.
	 */
	@Test
	public void testJoin() {
		Collection<String> data = new ArrayList<String>();
		data.add("Nikon");
		data.add("Canon");
		data.add("Pentax");
		
		Assert.assertEquals("Nikon,Canon,Pentax", StringUtil.join(data, ","));
		Assert.assertEquals("NikonCanonPentax", StringUtil.join(data, null));
	}

	/**
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.utils.lang.StringUtil#copyNonNulls(java.lang.String[])}.
	 */
	@Test
	public void testCopyNonNulls() {
		String[] data = {"Nikon", null, "Canon"};
		Assert.assertNotNull(StringUtil.copyNonNulls());
		Assert.assertArrayEquals(new String[]{"Nikon", "Canon"}, StringUtil.copyNonNulls(data));
	}

	/**
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.utils.lang.StringUtil#lineate(java.lang.String, int, java.lang.StringBuilder)}.
	 */
	@Test
	public void testLineate() {
		StringBuilder result = new StringBuilder();
		StringUtil.lineate("Hello World", 1, result);
		Assert.assertEquals("Hello" + StringUtil.EOL + "World" + StringUtil.EOL, result.toString());
	}

	/**
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.utils.lang.StringUtil#prefix(java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testPrefix() {
		String data = "Nikon";
		Assert.assertEquals(data, StringUtil.prefix(null, data));
		Assert.assertEquals(data, StringUtil.prefix("N", data));
		Assert.assertEquals("Awesome" + data, StringUtil.prefix("Awesome", data));
	}

	/**
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.utils.lang.StringUtil#postfix(java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testPostfix() {
		String data = "Nikon";
		Assert.assertEquals(data, StringUtil.postfix(data, null));
		Assert.assertEquals(data, StringUtil.postfix(data, "kon"));
		Assert.assertEquals(data + "Rocks", StringUtil.postfix(data, "Rocks"));
	}

	/**
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.utils.lang.StringUtil#bracket(java.lang.String, java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testBracket() {
		String data = "Nikon";
		Assert.assertEquals(data, StringUtil.bracket(data, null, null));
		Assert.assertEquals(data, StringUtil.bracket(null, data, "n"));
		Assert.assertEquals("Awesome" + data + "Rocks", StringUtil.bracket("Awesome", data, "Rocks"));
	}

	/**
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.utils.lang.StringUtil#toString(java.lang.Object[])}.
	 */
	@Test
	public void testToStringObjectArray() {
		Assert.assertNotNull(StringUtil.toString((Object[])null));
		Assert.assertNotNull("NikonRocks", StringUtil.toString(new Object[]{"Nikon", "Rocks"}));
		Assert.assertNotNull("Nikonnull", StringUtil.toString(new Object[]{"Nikon", null}));
	}

	/**
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.utils.lang.StringUtil#formatString(java.lang.String, java.lang.Object[])}.
	 */
	@Test
	public void testFormatString() {
		String data = "{0} is the best DSLR in the market";
		Assert.assertEquals("Nikon D700 is the best DSLR in the market", StringUtil.formatString(data, "Nikon D700"));
		Assert.assertEquals("Nikon is the best", StringUtil.formatString("Nikon is the best"));
		Assert.assertEquals("Nikon is the best", StringUtil.formatString("Nikon is the best", (Object[])null));
	}

	/**
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.utils.lang.StringUtil#broadEquals(java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testBroadEquals() {
		Assert.assertTrue(StringUtil.broadEquals("Nikon", "nikon"));
		Assert.assertTrue(StringUtil.broadEquals("Nikon ", " nikon"));
	}

}
