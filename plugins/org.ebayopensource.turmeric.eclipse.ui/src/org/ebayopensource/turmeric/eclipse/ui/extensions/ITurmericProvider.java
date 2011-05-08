/*******************************************************************************
 * Copyright (c) 2006-2011 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.ui.extensions;

/**
 * The Interface ITurmericProvider.
 */
public interface ITurmericProvider {
	
	/**
	 * Provider name.
	 *
	 * @return the string
	 */
	public String providerName();
	
	/**
	 * Checks for wizards.
	 *
	 * @return true, if successful
	 */
	public boolean hasWizards();
	
	/**
	 * Checks for service wizard.
	 *
	 * @return true, if successful
	 */
	public boolean hasServiceWizard();
	
	/**
	 * Checks for type lib wizard.
	 *
	 * @return true, if successful
	 */
	public boolean hasTypeLibWizard();
	
	/**
	 * Checks for error lib wizard.
	 *
	 * @return true, if successful
	 */
	public boolean hasErrorLibWizard();
	
	/**
	 * Checks for consumer wsdl wizard.
	 *
	 * @return true, if successful
	 */
	public boolean hasConsumerWSDLWizard();
	
	/**
	 * Find wizard.
	 *
	 * @param type the type
	 * @return the i turmeric wizard
	 */
	public ITurmericWizard findWizard(String type);
	

}
