/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.repositorysystem.core;

import org.eclipse.jface.wizard.IWizardPage;

import org.ebayopensource.turmeric.eclipse.core.model.ISOAServiceParamModel;

/**
 * Build System should implement this page if it needs to perform any additional
 * configuration. Remember this is required only if it needs additional inputs
 * from the user, otherwise please use the @see {@link ISOAProjectConfigurer}
 * interface and use it instead of this.
 * 
 * @author smathew
 * 
 */
public interface ISOAAdditionalBuildConfigPage extends IWizardPage {

	/**
	 * This is required for this wizard page so that this can complete its
	 * action without depending on the wizard page's finish. We want this
	 * special config page to do it independently so that the core model and the
	 * wizards can stay as it is without having build system specific
	 * properties. Lets say for example one build system wants to add each of
	 * the created project to an index file and it needs some additional input
	 * from the user. This page should take the input from the user and store
	 * the user inputs in the same instance and perform the finish when called.
	 * This user input will not be added to the core model. Even though Core
	 * model is not aware of the special input this page can always be aware of
	 * the core model or some of its properties through this formal paramater.
	 *
	 * @param soaParamModel the soa param model
	 */
	void performFinish(ISOAServiceParamModel soaParamModel);

//	/**
//	 * The name says everything. Yes This will be called by the core to see if
//	 * the wizard controller carrying this page can be finished.
//	 * 
//	 * @return
//	 */
//	boolean canFinish();

}
