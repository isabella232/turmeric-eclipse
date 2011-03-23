/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.errorlibrary.views;

/**
 * @author smathew
 * 
 * Represents an item that can be displayed in the view. View typically has the
 * display name, version and an image Right now we mandate only the display name
 * 
 */
public interface ISOAErrUIComp {

	/**
	 * This string would be shown in the UI as the label. Implementors are
	 * suggested not return null, rather return "", if this item does not carry
	 * a name
	 * 
	 * @return the display string
	 */
	String getName();

	/**
	 * This string would be shown in the UI as the version. Implementors are can
	 * return null,if this item does not have a version
	 * 
	 * @return the version
	 */
	String getVersion();

}
