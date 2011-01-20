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

import org.ebayopensource.turmeric.eclipse.registry.exception.ClientProviderException;
import org.ebayopensource.turmeric.eclipse.registry.models.ClientAssetModel;
import org.eclipse.core.runtime.IStatus;


/**
 * @author yayu
 *
 */
public interface IClientRegistryProvider {
	
	/**
	 * Retrieve the registered client asset detail from the Asset Registry or null if not registered yet.
	 * @param clientName
	 * @return
	 * @throws Exception
	 */
	public ClientAssetModel getClientAsset(String clientName) throws ClientProviderException;
	
	
	/**
	 * Submit a SOA client to the Asset Registry
	 * @param clientModel
	 * @return
	 * @throws ClientProviderException
	 */
	public IStatus submitNewClientAsset(ClientAssetModel clientModel) throws ClientProviderException;

}
