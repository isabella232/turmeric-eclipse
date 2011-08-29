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
package org.ebayopensource.turmeric.eclipse.ui;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.ebayopensource.turmeric.eclipse.core.logging.SOALogger;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.GlobalRepositorySystem;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.ISOAOrganizationProvider;
import org.ebayopensource.turmeric.eclipse.ui.util.SOADomainUtil;


/**
 * This is for providing the obtaining of SOA domain list from registry provider.
 * @author yayu
 * @since 1.0.0
 */
public abstract class AbstractSOADomainWizard extends SOABaseWizard {
	private static final SOALogger logger = SOALogger.getLogger();
	
	/** The domain list. */
	protected final Map<String, List<String>> domainList = new LinkedHashMap<String, List<String>>();
	
	/**
	 * Instantiates a new abstract soa domain wizard.
	 */
	public AbstractSOADomainWizard() {
		super();
	}

	/**
	 * Inits the domain list.
	 *
	 * @throws Exception the exception
	 */
	protected void initDomainList() throws Exception {
		final ISOAOrganizationProvider orgProvider = GlobalRepositorySystem.instanceOf()
		.getActiveRepositorySystem()
		.getActiveOrganizationProvider();
		if (orgProvider != null && orgProvider.supportFunctionalDomain() == false) {
			logger.warning("The current organization [", orgProvider.getName(), 
					"] does not support functional domain and namespace part, skip the creation.");
			return;
		}
		domainList.clear();
		domainList.putAll(SOADomainUtil.getRegisteredDomains());
	}

	/**
	 * Gets the domain list.
	 *
	 * @return the domain list
	 * @throws Exception the exception
	 */
	public Map<String, List<String>> getDomainList() throws Exception {
		if (domainList.isEmpty()) {
			initDomainList();
		}
		return domainList;
	}

}
