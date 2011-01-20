/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.resources.constants;


/**
 * @author smathew
 *  Service related constants
 */
public final class SOAServiceConstants {
	
    public static final String SOA_SERVER = "soa-server";
    public static final String SOA_CLIENT = "soa-client";
    public static final String SOA_TOOLS = "codegen-tools";
    public static final String[] SOA_FRAMEWORK_JARS = { SOA_CLIENT, SOA_SERVER, SOA_TOOLS };
    public static final String JUNIT = "junit";
    
    public static final String[] DEFAULT_LIBRARIES_INTF = {SOA_CLIENT,SOA_TOOLS};

}
