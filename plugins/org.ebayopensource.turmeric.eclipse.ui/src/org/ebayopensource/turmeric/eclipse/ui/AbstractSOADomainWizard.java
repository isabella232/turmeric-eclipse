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

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;

import org.apache.commons.httpclient.HttpException;
import org.ebayopensource.turmeric.eclipse.core.logging.SOALogger;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.GlobalRepositorySystem;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.ISOAOrganizationProvider;
import org.ebayopensource.turmeric.eclipse.ui.util.SOADomainUtil;
import org.ebayopensource.turmeric.eclipse.utils.ui.ProjectUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


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
	protected boolean callSplitPackageService(String serviceName, Set<String> mappedPackages, boolean fromConsumer){
		try {
			JSONObject response = ProjectUtils.callSplitPackageService(serviceName, mappedPackages,true);
			StringBuilder errorMessage = new StringBuilder();
			if(response!=null)
			{Iterator k = response.keys();
			final String newLine = System.getProperty("line.separator");
			while (k.hasNext()) {
				Map<String,String> bundleVersion = new HashMap<String, String>();
				String ErrorPackageName = (String) k.next();
				JSONArray bundlesList = null;
				try {
					bundlesList = response.getJSONArray(ErrorPackageName);
					for (int i = 0; i < bundlesList.length(); i++) {
						JSONObject object = bundlesList.getJSONObject(i);
						String conflictBundleName = object.getString("bundleName");
						String conflictingVersion = object.getString("version");
						if(conflictBundleName.matches(serviceName+"\\-(\\d+\\.)?(\\d+\\.)?(\\*|\\d+)$")){							
							continue;
							//Ignoring Same Bundles with Differing versions
						}
						bundleVersion.put(conflictBundleName, conflictingVersion);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				//display errors if any, for each erroneous bundle
				if(bundleVersion.size()>0){
					errorMessage.append("Split package errors detected for package "+ErrorPackageName
							+ " with the following bundles:");
					errorMessage.append(newLine);
					for(String bundle:bundleVersion.keySet()){
						errorMessage.append("     "+ bundle+":"+bundleVersion.get(bundle));
						errorMessage.append(newLine);
					}
					errorMessage.append(newLine);
				}
			}
			if(!errorMessage.toString().isEmpty()){
				if (fromConsumer){
				errorMessage.append("For more details about resolving split package errors follow this wiki: http://short/appPackage");
				errorMessage.append(newLine);	
				errorMessage.append("\nWould you still like to continue with these packages? Click OK to continue, or Cancel, go back and provide a unique Application Package to avoid conflicts.");
				}
				else{
					errorMessage.append("Would you still like to continue with these packages? Click OK to continue, or Cancel, go back and provide a package postfix to both -");
					errorMessage.append(newLine);	
					errorMessage.append("The package column in the namespace to package mappings table and the interface package.");
				}
				logger.log(Level.WARNING, errorMessage.toString());
				MessageBox messageDialog = new MessageBox(getShell(),SWT.ICON_WARNING|SWT.OK| SWT.CANCEL);
				messageDialog.setMessage(errorMessage.toString());
				messageDialog.setText("Split Packages Detected!");
			int answer = messageDialog.open();
			if(answer == SWT.CANCEL){
				//User choses to cancel and go back to change packages
				logger.log(Level.INFO, "Discarding packages due to split package warnings");
				return false;
			}
			else{
				logger.log(Level.INFO, "Ignoring split package warnings and using the same packages");
			}
			
			}
			}
		} catch (JSONException e) {
			logger.log(Level.WARNING,e.getMessage());
		}		
		//Succesful validation
	catch (HttpException e1) {
		logger.log(Level.WARNING,e1.getMessage());
	} catch (IOException e1) {
		logger.log(Level.WARNING,e1.getMessage());
	}
	return true;
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
