/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.repositorysystem.core;

import org.ebayopensource.turmeric.eclipse.repositorysystem.model.BaseCodeGenModel;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IProgressMonitor;


/**
 * The Interface ISOACodegenTransformer.
 *
 * @author smathew
 */
public interface ISOACodegenTransformer {

	/**
	 * Transform model.
	 *
	 * @param project the project
	 * @param monitor the monitor
	 * @return the base code gen model
	 * @throws Exception the exception
	 */
	public BaseCodeGenModel transformModel(IProject project, IProgressMonitor monitor) throws Exception;
}