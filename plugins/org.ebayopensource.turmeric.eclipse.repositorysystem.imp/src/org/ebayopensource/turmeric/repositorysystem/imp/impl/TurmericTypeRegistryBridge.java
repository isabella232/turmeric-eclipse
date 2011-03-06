/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
/**
 * 
 */
package org.ebayopensource.turmeric.repositorysystem.imp.impl;

import java.util.List;

import org.ebayopensource.turmeric.eclipse.maven.core.repositorysystem.AbstractMavenTypeRegistryBridge;
import org.ebayopensource.turmeric.eclipse.soatools.Activator;
import org.ebayopensource.turmeric.eclipse.utils.collections.ListUtil;
import org.ebayopensource.turmeric.repositorysystem.imp.utils.TurmericConstants;
import org.ebayopensource.turmeric.tools.library.SOAGlobalRegistryFactory;
import org.ebayopensource.turmeric.tools.library.SOATypeRegistry;
import org.osgi.framework.Bundle;

/**
 * @author yayu
 *
 */
public class TurmericTypeRegistryBridge extends AbstractMavenTypeRegistryBridge {

	/**
	 * 
	 */
	public TurmericTypeRegistryBridge() {
		super();
	}
	
	/**
	 * {@inheritDoc}
	 */
	public String getTypeDependencyWsdlTypeName() {
		return TurmericConstants.TYPE_NAME_CONST_FOR_ALL_WSDL;
	}

	/**
	 * {@inheritDoc}
	 */
	public List<Bundle> getPluginBundles() {
		return ListUtil.arrayList(
				Activator.getDefault().getBundle());
	}

	/**
	 * {@inheritDoc}
	 */
	public SOATypeRegistry getSOATypeRegistry() {
		return SOAGlobalRegistryFactory.getSOATypeRegistryInstance();
	}


}
