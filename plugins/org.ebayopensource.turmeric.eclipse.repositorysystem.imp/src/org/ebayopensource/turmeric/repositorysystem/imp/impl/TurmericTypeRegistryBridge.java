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

import java.io.File;
import java.io.InputStream;
import java.util.List;

import javax.xml.bind.JAXB;

import org.ebayopensource.turmeric.common.config.LibraryType;
import org.ebayopensource.turmeric.common.config.ReferredType;
import org.ebayopensource.turmeric.common.config.ReferredTypeLibraryType;
import org.ebayopensource.turmeric.common.config.TypeDependencyType;
import org.ebayopensource.turmeric.common.config.TypeLibraryDependencyType;
import org.ebayopensource.turmeric.common.config.TypeLibraryType;
import org.ebayopensource.turmeric.eclipse.maven.core.repositorysystem.AbstractMavenTypeRegistryBridge;
import org.ebayopensource.turmeric.eclipse.soatools.Activator;
import org.ebayopensource.turmeric.eclipse.utils.collections.ListUtil;
import org.ebayopensource.turmeric.repositorysystem.imp.utils.TurmericConstants;
import org.ebayopensource.turmeric.tools.library.SOAGlobalRegistryFactory;
import org.ebayopensource.turmeric.tools.library.SOATypeRegistry;
import org.ebayopensource.turmeric.tools.library.TypeDependencyParser;
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

	public void processTypeDepXMLFile(String libraryName) throws Exception {
		TypeDependencyParser.getInstance().processTypeDepXMLFile(libraryName);
	}

	public LibraryType createLibraryTypeInstance() {
		return new LibraryType();
	}

	public TypeDependencyType createTypeDependencyTypeInstance() {
		return new TypeDependencyType();
	}

	public ReferredType createReferredTypeInstance() {
		return new ReferredType();
	}

	public ReferredTypeLibraryType createReferredTypeLibraryTypeInstance() {
		return new ReferredTypeLibraryType();
	}

	public TypeLibraryDependencyType unmarshalTypeLibraryDependencyType(
			InputStream inputStream) {
		return JAXB.unmarshal(inputStream, TypeLibraryDependencyType.class);
	}

	public TypeLibraryType unmarshalTypeInformationType(InputStream inputStream) {
		return JAXB.unmarshal(inputStream, TypeLibraryType.class);
	}

	public void marshalTypeLibraryDependencyType(
			TypeLibraryDependencyType type, File xmlFile) {
		JAXB.marshal(type, xmlFile);
	}

	public TypeLibraryDependencyType createTypeLibraryDependencyTypeInstance() {
		return new TypeLibraryDependencyType();
	}

}
