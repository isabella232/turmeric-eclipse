/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.errorlibrary.properties.ui;

import org.ebayopensource.turmeric.eclipse.errorlibrary.properties.resources.SOAMessages;
import org.ebayopensource.turmeric.eclipse.errorlibrary.views.ISOAErrUIComp;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.viewers.StructuredViewer;


/**
 * The Class RefreshErrorNodeAction.
 *
 * @author yayu
 */
public class RefreshErrorNodeAction extends AbstractErrorNodeAction {

	/**
	 * Instantiates a new refresh error node action.
	 *
	 * @param viewer structured viewer
	 */
	public RefreshErrorNodeAction(StructuredViewer viewer) {
		super(SOAMessages.ACTION_TEXT_REFRESH, viewer);
	}

	/* (non-Javadoc)
	 * @see org.ebayopensource.turmeric.eclipse.errorlibrary.properties.ui.AbstractErrorNodeAction#execute(org.ebayopensource.turmeric.eclipse.errorlibrary.views.ISOAErrUIComp)
	 */
	@Override
	public IStatus execute(ISOAErrUIComp selectedErrorNode) throws Exception {
		getViewer().refresh(selectedErrorNode);
		return Status.OK_STATUS;
	}


}
