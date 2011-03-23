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
 * @author yayu
 * @since 1.0.0
 */
public final class SOAMavenConstants {
	public static final String MAVEN_CLASSPATH_CONTAINER_ID = "org.maven.ide.eclipse.MAVEN2_CLASSPATH_CONTAINER";

	public static final String LIBRARY_JUNIT = "junit:junit:jar:4.4:test";
	public static final String TURMERIC_CODEGEN_TOOLS_GROUPID = "org.ebayopensource.turmeric.codegen";
	public static final String SOA_FRAMEWORK_GROUPID = "org.ebayopensource.turmeric.runtime";
	

	public static final String SOA_INTERFACE_GROUPID_PROPERTYNAME = "serviceGroupID";

	public static final String MAVEN_PACKAGING_JAR = "jar";
	public static final String MAVEN_PACKAGING_WAR = "war";
	public static final String MAVEN_PACKAGING_POM = "pom";
	
	public static final String MAVEN_PROJECT_CONFIG_FILE = "pom.xml";

	public static final String POM_PROP_KEY_SERVICE_NAME = "serviceName";
	public static final String POM_PROP_KEY_REQUIRED_SERVICES = "requiredServices";
	public static final String POM_PROP_KEY_SERVICE_GROUP_ID = "serviceGroupID";
	public static final String POM_PROP_KEY_IMPL_PROJECT_NAME = "implProjectName";
	public static final String POM_PROP_KEY_SINGLE_POM = "singlePom";

	public static final String FOLDER_SRC_MAIN_JAVA = "src/main/java";
	public static final String FOLDER_SRC_MAIN_RESOURCES = "src/main/resources";
	public static final String FOLDER_SRC_TEST_JAVA = "src/test/java";
	public static final String FOLDER_SRC_TEST_RESOURCES = "src/test/resources";
	public static final String FOLDER_TARGET_CLASSES = "target/classes";
	public static final String FOLDER_TARGET_TESTCLASSES = "target/test-classes";
	
	public static enum ProjectType {
		INTERFACE, IMPLEMENTATION, CONSUMER, TYPELIBRARY, ERRORLIBRARY
	}
	
	public static final String TESTCLASSES_EXCLUDE_PATTERNS = "**";

	/**
	 * 
	 */
	private SOAMavenConstants() {
		super();
	}
	
	

}
