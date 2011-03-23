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
 * All SOA interface Projects will have this nature. The corresponding builder
 * is SOAInterfaceProjectBuilder.
 * 
 * @see SOAInterfaceProjectBuilder
 * @see IProjectNature
 * @author smathew
 */
public class SOAInterfaceProjectNature extends AbstractSOANature {
	/**
	 * The Nature ID for the SOAInterfaceProjectNature.
	 */
	public static final String NATURE_ID = SOAFrameworkBuilderActivator.PLUGIN_ID
			+ ".SOAInterfaceProjectNature";

	@Override
	public void configure() throws org.eclipse.core.runtime.CoreException {
		if (GlobalRepositorySystem.instanceOf().getActiveRepositorySystem()
				.getProjectConfigurer().configureProjectNature(getProject(), this)) {
			super.configure();
		}
	}
	
	@Override
	public String getBuilderName() {
		return SOAInterfaceProjectBuilder.BUILDER_ID;
	}

}
