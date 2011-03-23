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

import java.util.List;

import org.ebayopensource.turmeric.eclipse.registry.exception.ProviderException;
import org.ebayopensource.turmeric.eclipse.registry.models.NameValuePair;
import org.ebayopensource.turmeric.eclipse.registry.models.SimpleAssetModel;
import org.ebayopensource.turmeric.eclipse.registry.models.SubmitAssetModel;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;


/**
 * @author yayu
 * @since 1.0.0
 */
public interface IRegistryProvider {
	
	/**
	 * check whether the given asset already exist in the repository.
	 * @param assetModel the assert model for checking existence
	 * @param monitor
	 * @return 
	 * @throws ProviderException
	 */
	public IStatus assetNameExists(SimpleAssetModel assetModel, 
			IProgressMonitor monitor) throws ProviderException;

	/**
	 * Retrieving asset domain namespace values from repository
	 * @return the list of domain names from Asset Repository, or null if none
	 * @throws ProviderException
	 */
	public List<NameValuePair> getDomainNamespaceValues() throws ProviderException;

	/**
	 * Submit a new asset to the repository for governance
	 * @param assetModel the assert model for submission
	 * @return
	 * @throws ProviderException
	 */
	IStatus submitNewAssetForGovernance(SubmitAssetModel assetModel) throws ProviderException;
	
	/**
	 * @param assetModel the assert model for submission
	 * @return
	 * @throws ProviderException
	 */
	IStatus submitNewVersionForGovernance(SubmitAssetModel assetModel) throws ProviderException;
	
	/**
	 * @param assetModel the assert model for submission
	 * @return
	 * @throws ProviderException
	 */
	IStatus updateExistingVersionForGovernance(SubmitAssetModel assetModel) throws ProviderException;

	/**
	 * @param assetModel the assert model for submission
	 * @return
	 * @throws ProviderException
	 */
	IStatus submitNewMaintenanceVersion(SubmitAssetModel assetModel) throws ProviderException;

}
