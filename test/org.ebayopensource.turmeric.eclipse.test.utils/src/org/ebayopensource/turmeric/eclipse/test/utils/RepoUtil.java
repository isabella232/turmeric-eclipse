/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.test.utils;

import org.ebayopensource.turmeric.eclipse.repositorysystem.core.GlobalRepositorySystem;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.ISOARepositorySystem;

public class RepoUtil {
	public static String V3_ROOT = null;
	public static String deliverable = "SOAPlaygroundDeliverable";

	public static void setActiveRepo(String id) {

		GlobalRepositorySystem.instanceOf().setActiveRepositorySystem(
				GlobalRepositorySystem.instanceOf().getRepositorySystem(id));

	}

	public static ISOARepositorySystem getActiveRepo() {
		return GlobalRepositorySystem.instanceOf().getActiveRepositorySystem();
	}
	public static String getV3ViewRoot() {
		V3_ROOT = RepoUtil.getActiveRepo().getSOARootLocator().getRoot().toString();
		return V3_ROOT;
	}
}
