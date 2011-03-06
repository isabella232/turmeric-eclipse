/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.build.builder;

import org.ebayopensource.turmeric.eclipse.build.SOAFrameworkBuilderActivator;
import org.ebayopensource.turmeric.eclipse.core.buildsystem.AbstractSOANature;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.GlobalRepositorySystem;
import org.eclipse.core.resources.IProjectNature;


/**
 * All SOA Implementation Projects will have this nature. The corresponding
 * builder is SOAImplProjectBuilder.
 * 
 * @see SOAImplProjectBuilder
 * @see IProjectNature
 * @author smathew
 */
public class SOAImplProjectNature extends AbstractSOANature {
	/**
	 * The Nature ID for the SOAImplProjectNature.
	 * 
	 */
	public static final String NATURE_ID = SOAFrameworkBuilderActivator.PLUGIN_ID
			+ ".SOAImplProjectNature";

	@Override
	public String getBuilderName() {
		return SOAImplProjectBuilder.BUILDER_ID;
	}
	
	@Override
	public void configure() throws org.eclipse.core.runtime.CoreException {
		if (GlobalRepositorySystem.instanceOf().getActiveRepositorySystem()
				.getProjectConfigurer().configureProjectNature(getProject(), this)) {
			super.configure();
		}
	}
}
