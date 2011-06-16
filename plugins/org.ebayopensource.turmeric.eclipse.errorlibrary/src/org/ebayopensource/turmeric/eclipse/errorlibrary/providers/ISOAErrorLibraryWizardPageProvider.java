/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.errorlibrary.providers;

import java.util.List;

import org.ebayopensource.turmeric.eclipse.core.model.BaseServiceParamModel;
import org.ebayopensource.turmeric.eclipse.ui.SOABasePage;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.viewers.IStructuredSelection;


/**
 * @author smathew
 * @since 1.0.0
 */
public interface ISOAErrorLibraryWizardPageProvider {
	
	/**
	 * prevalidate the error library creation
	 * @return
	 */
	public IStatus preValidate();

	/**
	 * @param selection
	 * @return the list of all Wizard page instances
	 */
	public List<SOABasePage> getWizardpages(IStructuredSelection selection);

	/**
	 * @return The UI model for the wizard page
	 */
	public BaseServiceParamModel performFinish() throws Exception;

}
