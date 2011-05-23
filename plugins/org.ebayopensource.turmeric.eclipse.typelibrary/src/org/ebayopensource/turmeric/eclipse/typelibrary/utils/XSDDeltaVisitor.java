/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.typelibrary.utils;

import org.apache.commons.lang.StringUtils;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.runtime.CoreException;

/**
 * The Class XSDDeltaVisitor.
 *
 * @author smathew
 */
public class XSDDeltaVisitor implements IResourceDeltaVisitor {

	private IFile xsdFile = null;

	/**
	 * Instantiates a new xSD delta visitor.
	 */
	public XSDDeltaVisitor() {

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean visit(IResourceDelta delta) throws CoreException {
		if (StringUtils.equalsIgnoreCase(StringUtils.substringAfterLast(delta
				.getFullPath().toString(), "."), "xsd")) {
			xsdFile = (IFile) delta.getResource();
			return false;
		}
		return true;
	}

	/**
	 * Gets the xsd file.
	 *
	 * @return the xsd file
	 */
	public IFile getXsdFile() {
		return xsdFile;
	}
	

}
