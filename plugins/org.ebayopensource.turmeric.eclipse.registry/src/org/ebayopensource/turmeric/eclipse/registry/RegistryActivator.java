/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.registry;

import java.util.logging.Logger;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;

public class RegistryActivator implements BundleActivator {
	// The plug-in ID
	public static final String PLUGIN_ID = "org.ebayopensource.turmeric.eclipse.registry";

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		StringBuffer buf = new StringBuffer();
    	buf.append("SOAPlugin.start - ");
    	
        if (context.getBundle() != null)
        {
        	Bundle bundle = context.getBundle();
        	buf.append(bundle.getSymbolicName());
        	Object versionID = bundle.getHeaders().get(Constants.BUNDLE_VERSION);
        	if (versionID != null)
        	{
        		buf.append(" ");
        		buf.append(versionID.toString());
        	}
        	
        }
        
    	Logger.getLogger(getClass().getName()).info(buf.toString());
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
	}

}
