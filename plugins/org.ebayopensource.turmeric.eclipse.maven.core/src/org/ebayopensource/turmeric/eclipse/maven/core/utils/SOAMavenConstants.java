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
package org.ebayopensource.turmeric.eclipse.maven.core.utils;



/**
 * The Class SOAMavenConstants.
 *
 * @author yayu
 * @since 1.0.0
 */
public final class SOAMavenConstants {
	
	/** The Constant MAVEN_CLASSPATH_CONTAINER_ID. */
	public static final String MAVEN_CLASSPATH_CONTAINER_ID = "org.eclipse.m2e.MAVEN2_CLASSPATH_CONTAINER";

	/** The Constant LIBRARY_JUNIT. */
	public static final String LIBRARY_JUNIT = "junit:junit:jar:4.4:test";
	
	/** The Constant TURMERIC_CODEGEN_TOOLS_GROUPID. */
	public static final String TURMERIC_CODEGEN_TOOLS_GROUPID = "org.ebayopensource.turmeric.codegen";
	
	/** The Constant SOA_FRAMEWORK_GROUPID. */
	public static final String SOA_FRAMEWORK_GROUPID = "org.ebayopensource.turmeric.runtime";
	

	/** The Constant SOA_INTERFACE_GROUPID_PROPERTYNAME. */
	public static final String SOA_INTERFACE_GROUPID_PROPERTYNAME = "serviceGroupID";

	/** The Constant MAVEN_PACKAGING_JAR. */
	public static final String MAVEN_PACKAGING_JAR = "jar";
	
	/** The Constant MAVEN_PACKAGING_WAR. */
	public static final String MAVEN_PACKAGING_WAR = "war";
	
	/** The Constant MAVEN_PACKAGING_POM. */
	public static final String MAVEN_PACKAGING_POM = "pom";
	
	/** The Constant MAVEN_PROJECT_CONFIG_FILE. */
	public static final String MAVEN_PROJECT_CONFIG_FILE = "pom.xml";

	/** The Constant POM_PROP_KEY_SERVICE_NAME. */
	public static final String POM_PROP_KEY_SERVICE_NAME = "serviceName";
	
	/** The Constant POM_PROP_KEY_REQUIRED_SERVICES. */
	public static final String POM_PROP_KEY_REQUIRED_SERVICES = "requiredServices";
	
	/** The Constant POM_PROP_KEY_SERVICE_GROUP_ID. */
	public static final String POM_PROP_KEY_SERVICE_GROUP_ID = "serviceGroupID";
	
	/** The Constant POM_PROP_KEY_IMPL_PROJECT_NAME. */
	public static final String POM_PROP_KEY_IMPL_PROJECT_NAME = "implProjectName";
	
	/** The Constant POM_PROP_KEY_SINGLE_POM. */
	public static final String POM_PROP_KEY_SINGLE_POM = "singlePom";

	/** The Constant FOLDER_SRC_MAIN_JAVA. */
	public static final String FOLDER_SRC_MAIN_JAVA = "src/main/java";
	
	/** The Constant FOLDER_SRC_MAIN_RESOURCES. */
	public static final String FOLDER_SRC_MAIN_RESOURCES = "src/main/resources";
	
	/** The Constant FOLDER_SRC_TEST_JAVA. */
	public static final String FOLDER_SRC_TEST_JAVA = "src/test/java";
	
	/** The Constant FOLDER_SRC_TEST_RESOURCES. */
	public static final String FOLDER_SRC_TEST_RESOURCES = "src/test/resources";
	
	/** The Constant FOLDER_TARGET_CLASSES. */
	public static final String FOLDER_TARGET_CLASSES = "target/classes";
	
	/** The Constant FOLDER_TARGET_TESTCLASSES. */
	public static final String FOLDER_TARGET_TESTCLASSES = "target/test-classes";
	
	/**
	 * The Enum ProjectType.
	 */
	public static enum ProjectType {
		
		/** The INTERFACE. */
		INTERFACE, 
 /** The IMPLEMENTATION. */
 IMPLEMENTATION, 
 /** The CONSUMER. */
 CONSUMER, 
 /** The TYPELIBRARY. */
 TYPELIBRARY, 
 /** The ERRORLIBRARY. */
 ERRORLIBRARY
	}
	
	/** The Constant TESTCLASSES_EXCLUDE_PATTERNS. */
	public static final String TESTCLASSES_EXCLUDE_PATTERNS = "**";

	/**
	 * 
	 */
	private SOAMavenConstants() {
		super();
	}
	
	

}
