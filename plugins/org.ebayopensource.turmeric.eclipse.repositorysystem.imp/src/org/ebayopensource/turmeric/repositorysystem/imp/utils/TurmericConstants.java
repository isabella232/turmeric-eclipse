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
package org.ebayopensource.turmeric.repositorysystem.imp.utils;

import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.ebayopensource.turmeric.eclipse.core.resources.constants.SOAProjectConstants.SupportedProjectType;
import org.ebayopensource.turmeric.eclipse.maven.core.utils.SOAMavenConstants;
import org.ebayopensource.turmeric.eclipse.utils.collections.ListUtil;


/**
 * The Class TurmericConstants.
 *
 * @author yayu
 * @since 1.0.0
 */
public final class TurmericConstants {

	/**
	 * 
	 */
	private TurmericConstants() {
		super();
	}
	
	/** The Constant TURMERIC_DISPLAY_NAME. */
	public static final String TURMERIC_DISPLAY_NAME = "Turmeric System";
	
	/** The Constant TURMERIC_ID. */
	public static final String TURMERIC_ID = "Turmeric";
	
	/** The Constant DEFAULT_TURMERIC_PACKAGE_NAME. */
	public static final String DEFAULT_TURMERIC_PACKAGE_NAME = "org.ebayopensource.turmeric.services";
	
	/** The Constant DEFAULT_SERVICE_NAMESPACE_PREFIX. */
	public static final String DEFAULT_SERVICE_NAMESPACE_PREFIX = "http://www.ebayopensource.org/turmeric";
	
	/** The Constant DEFAULT_SERVICE_NAMESPACE_SUFFIX. */
	public static final String DEFAULT_SERVICE_NAMESPACE_SUFFIX = "/services";
	
	/** The Constant DEFAULT_TYPES_NAMESPACE_SUFFIX. */
	public static final String DEFAULT_TYPES_NAMESPACE_SUFFIX = "/types";
	
	/** The Constant DEFAULT_SERVICE_NAMESPACE. */
	public static final String DEFAULT_SERVICE_NAMESPACE = DEFAULT_SERVICE_NAMESPACE_PREFIX +
	DEFAULT_SERVICE_NAMESPACE_SUFFIX;
	
	/** The Constant TURMERIC_NAMESPACE_PATTERN. */
	public static final String TURMERIC_NAMESPACE_PATTERN = DEFAULT_SERVICE_NAMESPACE_PREFIX
	+ "/[a-z]+/v[1-9]+" + DEFAULT_SERVICE_NAMESPACE_SUFFIX;

	/** The Constant TYPE_NAME_CONST_FOR_ALL_WSDL. */
	public static final String TYPE_NAME_CONST_FOR_ALL_WSDL = "TurmericWSDLType";
	
	/** The Constant TYPE_INFORMATION_NAMESPACE. */
	public static final String TYPE_INFORMATION_NAMESPACE="http://www.ebayopensource.org/turmeric/services";
	
	/** The Constant SOA_NAME_SPACE_INTIALS. */
	public static final String SOA_NAME_SPACE_INTIALS = "http://www.ebayopensource.org/turmeric";
	
	/** The Constant TURMERIC_SVC_CONFIG_FOLDER_NAME. */
	public static final String TURMERIC_SVC_CONFIG_FOLDER_NAME = TURMERIC_ID.toLowerCase(Locale.US);
	
	/** The Constant SOA_INTERFACE_GROUPID. */
	public static final String SOA_INTERFACE_GROUPID = "org.ebayopensource.turmeric.intf";
	
	/** The Constant SOA_IMPL_GROUPID. */
	public static final String SOA_IMPL_GROUPID = "org.ebayopensource.turmeric.impl";
	
	/** The Constant SOA_CLIENT_GROUPID. */
	public static final String SOA_CLIENT_GROUPID = "org.ebayopensource.turmeric.client";
	
	/** The Constant SOA_TYPELIBRARY_GROUPID. */
	public static final String SOA_TYPELIBRARY_GROUPID = "org.ebayopensource.turmeric.typelib"; 
	
	/** The Constant SOA_ERRORLIBRARY_GROUPID. */
	public static final String SOA_ERRORLIBRARY_GROUPID = "org.ebayopensource.turmeric.errorlib";
	
	// Property Page
    /** The Constant PROP_PAGE_ID_SOA_PROJ. */
	public static final String PROP_PAGE_ID_SOA_PROJ = "org.ebayopensource.turmeric.eclipse.services.ui.properties.soaProjectPropertyPage";
    // mzang 2010-4-20 id for property page.
    /** The Constant PROP_PAGE_ID_TYPELIBRARA_PROJ. */
    public static final String PROP_PAGE_ID_TYPELIBRARA_PROJ = "org.ebayopensource.turmeric.eclipse.services.ui.properties.typelibraryProjectPropertyPage";
	
	/** The Constant TURMERIC_FRAMEWORK_GROUPID. */
	public static final String TURMERIC_FRAMEWORK_GROUPID = SOAMavenConstants.SOA_FRAMEWORK_GROUPID;
	
	/** The Constant SOA_CLIENT. */
	public static final String SOA_CLIENT = TURMERIC_FRAMEWORK_GROUPID + ":soa-client";
	
	/** The Constant SOA_TOOLS. */
	public static final String SOA_TOOLS = SOAMavenConstants.TURMERIC_CODEGEN_TOOLS_GROUPID + ":codegen-tools";
    
    /** The Constant SOA_SERVER. */
    public static final String SOA_SERVER = TURMERIC_FRAMEWORK_GROUPID + ":soa-server";
    
    /** The Constant BINDING_FRAMEWORK. */
    public static final String BINDING_FRAMEWORK = TURMERIC_FRAMEWORK_GROUPID + "binding-framework";
    
    /** The Constant SOA_COMMON_TL. */
    public static final String SOA_COMMON_TL = SOA_TYPELIBRARY_GROUPID + ":common-type-library";
    
    /** The Constant SOA_COMMON_EL. */
    public static final String SOA_COMMON_EL = SOA_ERRORLIBRARY_GROUPID + ":runtime-error-library";
    
    /** The Constant SOA_MAVEN_PREBUILD_PLUGIN. */
    public static final String SOA_MAVEN_PREBUILD_PLUGIN = "org.ebayopensource.turmeric.maven:turmeric-maven-plugin";
    
    /** The Constant TAG_AUTOUPDATE_VERSION. */
    public static final String TAG_AUTOUPDATE_VERSION = "${autoupdate.version}";
    
    /** The Constant TURMERIC_DEVELOPMENT_VERSION. */
    public static final String TURMERIC_DEVELOPMENT_VERSION = "1.0.1.0-SNAPSHOT";
    
    /** The Constant TURMERIC_MIN_REQUIRED_VERSION. */
    public static final String TURMERIC_MIN_REQUIRED_VERSION = "0.9.0";
	
	//public static final String[] ERROR_LIB_DEPENDENCIES_TURMERIC = { "EboxDsf"};
	/** The Constant DEFAULT_DEPENDENCIES_INTERFACE. */
	public static final List<String> DEFAULT_DEPENDENCIES_INTERFACE;
	
	/** The Constant DEFAULT_DEPENDENCIES_IMPL. */
	public static final List<String> DEFAULT_DEPENDENCIES_IMPL;
	
	/** The Constant DEFAULT_DEPENDENCIES_CONSUMER. */
	public static final List<String> DEFAULT_DEPENDENCIES_CONSUMER;
	
	/** The Constant DEFAULT_DEPENDENCIES_TYPELIB. */
	public static final List<String> DEFAULT_DEPENDENCIES_TYPELIB;
	
	/** The Constant DEFAULT_DEPENDENCIES_ERRORLIB. */
	public static final List<String> DEFAULT_DEPENDENCIES_ERRORLIB;
	private static final String JAXB_XJC = "com.sun.xml.bind:jaxb-xjc:jar:2.1.2";
	//private static final String[] LIB_JAXB2 = {//"com.sun.xml.bind:jaxb1-impl:2.1.13",};
	
	/** The Constant PROTOCOL_PROCESSOR_CLASS_NAME_VALUE. */
	public static final String PROTOCOL_PROCESSOR_CLASS_NAME_VALUE = "org.ebayopensource.turmeric.runtime.sif.impl.protocolprocessor.soap.ClientSOAPProtocolProcessor";
	
	// Nature IDs
    /** The Constant NATURE_ID_SOA_INTF_PROJECT. */
	public static final String NATURE_ID_SOA_INTF_PROJECT = "org.ebayopensource.turmeric.eclipse.build.SOAInterfaceProjectNature";
    
    /** The Constant NATURE_ID_SOA_CONSUMER_PROJECT. */
    public static final String NATURE_ID_SOA_CONSUMER_PROJECT = "org.ebayopensource.turmeric.eclipse.build.SOAClientProjectNature";
    
    /** The Constant NATURE_ID_SOA_IMPL_PROJECT. */
    public static final String NATURE_ID_SOA_IMPL_PROJECT = "org.ebayopensource.turmeric.eclipse.build.SOAImplProjectNature";
    
    /** The Constant NATURE_ID_SOA_TYPELIB_PROJECT. */
    public static final String NATURE_ID_SOA_TYPELIB_PROJECT = "org.ebayopensource.turmeric.eclipse.typelibrary.TypeLibraryProjectNature";
    
    /** The Constant NATURE_ID_TURMERIC_ERRORLIB_PROJECT. */
    public static final String NATURE_ID_TURMERIC_ERRORLIB_PROJECT = "org.ebayopensource.turmeric.eclipse.errorlibrary.properties.ErrorLibraryProjectNature";
	
	/** The Constant PROJECT_NATUREIDS_MAP. */
	public static final Map<SupportedProjectType, String> PROJECT_NATUREIDS_MAP;
	
	/** The Constant PROJECT_TYPES_MAP. */
	public static final Map<String, SupportedProjectType> PROJECT_TYPES_MAP;
	
	static {
		
		DEFAULT_DEPENDENCIES_INTERFACE = Collections.unmodifiableList(
				ListUtil.arrayList(SOA_CLIENT));
		DEFAULT_DEPENDENCIES_IMPL = Collections.unmodifiableList(
				ListUtil.arrayList(SOAMavenConstants.LIBRARY_JUNIT,
			            SOA_SERVER, SOA_CLIENT));
		DEFAULT_DEPENDENCIES_CONSUMER = Collections.unmodifiableList(
				ListUtil.arrayList(SOA_CLIENT));
		DEFAULT_DEPENDENCIES_TYPELIB = Collections.unmodifiableList(
				ListUtil.arrayList(JAXB_XJC));
		
		DEFAULT_DEPENDENCIES_ERRORLIB = Collections.unmodifiableList(
				ListUtil.arrayList(SOA_COMMON_TL));
		
		Map<SupportedProjectType, String> data = new ConcurrentHashMap<SupportedProjectType, String>();
		data.put(SupportedProjectType.INTERFACE, NATURE_ID_SOA_INTF_PROJECT);
		data.put(SupportedProjectType.IMPL, NATURE_ID_SOA_IMPL_PROJECT);
		data.put(SupportedProjectType.CONSUMER, NATURE_ID_SOA_CONSUMER_PROJECT);
		data.put(SupportedProjectType.TYPE_LIBRARY, NATURE_ID_SOA_TYPELIB_PROJECT);
		data.put(SupportedProjectType.ERROR_LIBRARY, NATURE_ID_TURMERIC_ERRORLIB_PROJECT);
		PROJECT_NATUREIDS_MAP = Collections.unmodifiableMap(data);
		
		Map<String, SupportedProjectType> map = new ConcurrentHashMap<String, SupportedProjectType>();
		map.put(NATURE_ID_SOA_INTF_PROJECT, SupportedProjectType.INTERFACE);
		map.put(NATURE_ID_SOA_IMPL_PROJECT, SupportedProjectType.IMPL);
		map.put(NATURE_ID_SOA_CONSUMER_PROJECT, SupportedProjectType.CONSUMER);
		map.put(NATURE_ID_SOA_TYPELIB_PROJECT, SupportedProjectType.TYPE_LIBRARY);
		map.put(NATURE_ID_TURMERIC_ERRORLIB_PROJECT, SupportedProjectType.ERROR_LIBRARY);
		PROJECT_TYPES_MAP = Collections.unmodifiableMap(map);
	}

}
