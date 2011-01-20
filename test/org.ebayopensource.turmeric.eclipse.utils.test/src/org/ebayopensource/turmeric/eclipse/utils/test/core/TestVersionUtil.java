/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.utils.test.core;


import org.ebayopensource.turmeric.eclipse.utils.core.VersionUtil;
import org.junit.Assert;
import org.junit.Test;


/**
 * @author yayu
 *
 */
public class TestVersionUtil {

	/**
	 * Test method for {@link org.ebayopensource.turmeric.eclipse.utils.core.VersionUtil#compare(java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testCompare() {
		Assert.assertTrue("version should be same", VersionUtil.compare("1.0", "1.0.0") == 0);
		Assert.assertTrue("version should be smaller", VersionUtil.compare("1.0", "2.0") < 0);
		Assert.assertTrue("version should be greater", VersionUtil.compare("1.0", "0.99.9") > 0);
		Assert.assertTrue("version should be smaller", VersionUtil.compare("1.0", "2.0b") < 0);
		Assert.assertTrue("version should be greater", VersionUtil.compare("1.0", "0.99.9a") > 0);
	}

}
