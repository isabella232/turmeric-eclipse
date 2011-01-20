/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.mavenapi.test.internal.resources;

import java.util.ArrayList;
import java.util.Collection;

import org.ebayopensource.turmeric.eclipse.mavenapi.internal.resources.Messages;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author yayu
 *
 */
public class TestMessages {

	/**
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.mavenapi.internal.resources.Messages#formatString(java.lang.String, java.lang.Object[])}.
	 */
	@Test
	public void testFormatString() {
		String message = "Hello {0}";
		String args = "Android";
		String actual = Messages.formatString(message, args);
		Assert.assertEquals("Hello Android", actual);
	}

	/**
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.mavenapi.internal.resources.Messages#join(java.util.Collection)}.
	 */
	@Test
	public void testJoin() {
		Collection<String> collection = new ArrayList<String>();
		collection.add("3dfx");
		collection.add("Nvidia");
		collection.add("ATI");
		Assert.assertEquals("3dfxNvidiaATI", Messages.join(collection));
	}

}
