/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.services.ui.views;

import org.apache.commons.lang.StringUtils;
import org.ebayopensource.turmeric.eclipse.ui.UIConstants;


/**
 * @author smathew
 * 
 */
public class ServicesViewLayerModel extends ServicesViewBaseModel {
	private String layer;

	public ServicesViewLayerModel(String layer) {
		super();
		this.layer = layer;
	}

	public String getLayer() {
		return layer;
	}

	public void setLayer(String layer) {
		this.layer = layer;
	}

	public static String getImageName() {
		return UIConstants.IMAGE_LABEL;
	}

	@Override
	public String toString() {
		String[] formattedArray = { UIConstants.SERVICES_VIEW_LAYER,
				UIConstants.SERVICES_VIEW_COLON, " ", layer };
		return StringUtils.join(formattedArray);
	}
}
