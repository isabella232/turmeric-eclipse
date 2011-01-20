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
package org.ebayopensource.turmeric.eclipse.registry.intf;

/**
 * @author yayu
 * @since 1.0.0
 */
public interface IArtifactValidator2 {
	
	/**
	 * @return the version of the underlying IArtifactValidator instance
	 */
	public String getVersion();
	
	/**
	 * @return whether the assertion service is enabled or not
	 */
	public boolean isAssertionServiceEnabled();

}
