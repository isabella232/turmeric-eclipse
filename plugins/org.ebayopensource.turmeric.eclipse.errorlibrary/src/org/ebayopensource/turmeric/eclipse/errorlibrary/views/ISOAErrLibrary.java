/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.errorlibrary.views;

import java.util.Set;

/**
 * @author smathew
 * 
 * Represents an error library object. Typically this class would be implemented
 * by different implementing framewroks like V4, properties based impl etc
 */
public interface ISOAErrLibrary extends ISOAErrUIComp {

	Set<ISOAErrDomain> getDomains();
	
}
