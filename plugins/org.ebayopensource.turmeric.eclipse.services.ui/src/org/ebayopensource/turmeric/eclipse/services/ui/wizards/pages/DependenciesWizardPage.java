/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.services.ui.wizards.pages;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;

import org.ebayopensource.turmeric.eclipse.core.logging.SOALogger;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.GlobalRepositorySystem;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.ISOAHelpProvider;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.SOAGlobalRegistryAdapter;
import org.ebayopensource.turmeric.eclipse.repositorysystem.utils.RepositoryUtils;
import org.ebayopensource.turmeric.eclipse.resources.model.AssetInfo;
import org.ebayopensource.turmeric.eclipse.resources.model.ProjectInfo;
import org.ebayopensource.turmeric.eclipse.ui.components.DependencyListEditor;
import org.ebayopensource.turmeric.eclipse.ui.components.DependencyListEditor.IDependencyLazyLoader;
import org.ebayopensource.turmeric.eclipse.utils.ui.UIUtil;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;


/**
 * The Class DependenciesWizardPage.
 *
 * @author smathew
 * 
 * The project and library dependency page This page is used for both project
 * and library dependency
 * 
 * Modified for raptor to show type library dependencies and suggestions
 */
public class DependenciesWizardPage extends WizardPage implements IWizardPage {
	private DependencyListEditor projectsEditor;
	private Text guideLines;
	private Composite composite;
	private DependencyListEditor libraryEditor;
	

	/**
	 * Instantiates a new dependencies wizard page.
	 *
	 * @param projectType the project type
	 */
	public DependenciesWizardPage(final String projectType) {
		super("newSOAServiceProjectDependenciesWizardPage");
		setTitle("Type Library Dependencies");
		setDescription("This wizard page adds type library dependencies to the new consumer project, "+
						"to avoid regeneration of types in the consumer project. Below, you will find"+
				"our suggestions for bundles that may contain type libraries referred by your wsdl.");
	}
	@Override
	public void setVisible(boolean visible) {
		super.setVisible(visible);
		if(visible){
		ConsumerFromExistingWSDLWizardPage page = (ConsumerFromExistingWSDLWizardPage) this.getWizard().getPreviousPage(this);
		StringBuilder guidlinesText = new StringBuilder();
		final String newLine = System.getProperty("line.separator");
		guidlinesText.append(newLine);
		for (String typeLibrary: page.typeLibNameAndPackage.keySet()){
			String packageName =page.typeLibNameAndPackage.get(typeLibrary);
			//call the service if any new type librraries are present.
		
			
			guidlinesText.append("Library " + typeLibrary +" is found at Package "+packageName);
			guidlinesText.append(newLine);
			guidlinesText.append(newLine);
		}
		guideLines.setText(guidlinesText.toString());
		//updateRaptorProperties(getTypeLibArtifactsToAdd(page.typeLibNameAndPackage));	
		//reset types explorer, try
		SOAGlobalRegistryAdapter.getInstance().invalidateRegistry();
		try {
			SOAGlobalRegistryAdapter.getInstance().getGlobalRegistry();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
				
		
		}
		
	}
	private void getTypeLibArtifactsToAdd(Map<String,String> typeLibNameAndPackage){
		//preparing the inputs to split package service
		Set<String> allKnownTls=null;
		try {
			allKnownTls = SOAGlobalRegistryAdapter.getInstance().getGlobalRegistry().getAllTypeLibrariesNames();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Set<String> packageList = new HashSet<String>();
		StringBuilder toBeAppended=new StringBuilder();
		for (String typeLibrary: typeLibNameAndPackage.keySet()){
			if(!allKnownTls.contains(typeLibrary)){
				//unKnown, so need to call split package service on this
				packageList.add(typeLibNameAndPackage.get(typeLibrary));
			}
			else{
				//Known so adding info
			//	SOAGlobalRegistryAdapter.getInstance().getGlobalRegistry().
			}
		}
		//If no new packages, then no need to call split package service and find the bundles.
	}
	private void updateRaptorProperties() {
		String userHomeDirectory = System.getProperty("user.home");
		String propertiesFileLocation= userHomeDirectory+File.separator+System.getProperty("ide.version").replace(".", "_");
		String updatedTipLibArtifacts=null;
		Properties properties = new Properties();
		File home = new File(propertiesFileLocation);
		if(!home.exists()){
				home.mkdir();
		}
		File raptorParentFile = new File(home,"raptorSoa.properties");
		if(raptorParentFile.exists()){
			FileInputStream in=null;
			try {
				in = new FileInputStream(raptorParentFile);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			try {if(in!=null)
				properties.load(in);
			} catch (IOException e) {
				e.printStackTrace();
			}finally{
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			updatedTipLibArtifacts=properties.getProperty("TypeLibArtifacts");
		}
		if(updatedTipLibArtifacts==null)updatedTipLibArtifacts="";
		else updatedTipLibArtifacts+=",";
		updatedTipLibArtifacts+="com.ebay.libraries.search.sharedsearchtypelibrary:SharedSearchServiceTypeLibrary:1.0.0:SharedSearchServiceTypeLibrary";
		properties.setProperty("TypeLibArtifacts", updatedTipLibArtifacts);
		OutputStream o =null;
		try {
			o=new FileOutputStream(raptorParentFile);
			properties.store(o, "Properties for soa on RIDE");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			if (o!=null){
				try {
					o.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void createControl(final Composite parent) {

		Set<? extends AssetInfo> projects = new TreeSet<ProjectInfo>();
		try {
			projects = RepositoryUtils.getProjectInfoProjectsFromWorkSpace();
		} catch (Exception e) {
			SOALogger.getLogger().error(e);
			throw new RuntimeException(e);
		}

		final Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(new GridLayout(1, true));
//
//		final Composite composite = new Composite(container, SWT.NULL);
//		composite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
//
////		projectsEditor = new DependencyListEditor("Project Dependencies:",
////				composite, projects, "Project");
//		setControl(container);
		
		IDependencyLazyLoader dependencyLazyLoader = new IDependencyLazyLoader() {		
			@Override
			public Set<? extends AssetInfo> getAlreadyAddedLibraries() {
				return null;
			}

			@Override
			public Set<? extends AssetInfo> getLibs() {

				try {
					return GlobalRepositorySystem.instanceOf()
							.getActiveRepositorySystem().getAssetRegistry()
							.getAllLibraries();
				} catch (Exception e) {
					SOALogger.getLogger().error(e);
					UIUtil.showErrorDialog(null, e);
				}
				return null;
			}
		};
		final Composite libraryComposite = new Composite(container, SWT.NULL);
		libraryComposite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		libraryEditor = new DependencyListEditor("Library Dependencies:",
				libraryComposite,dependencyLazyLoader, null, DependencyListEditor.DEPENDENCY_TYPE_LIBRARY);
		
		
		UIUtil.getHelpSystem().setHelp(container,
				GlobalRepositorySystem.instanceOf().getActiveRepositorySystem()
				.getHelpProvider().getHelpContextID(ISOAHelpProvider.PAGE_SERVICE_DEPENDENCIES));
		//What to do here, provide suggestions. As they get added, refresh type library also.
		
		Label seperator = new Label(container, SWT.SEPARATOR | SWT.HORIZONTAL);
		seperator.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		Label GuideLinesHeader = new Label(container,SWT.LEFT|SWT.BOLD|SWT.WRAP);
		GuideLinesHeader.setText("Possible suggestions for type libraries used by your wsdl, along with the bundles in which they may be available :");
		Point p = GuideLinesHeader.getSize();
		guideLines = new Text(container, SWT.LEFT|SWT.MULTI|SWT.WRAP|SWT.BORDER);		
		guideLines.setEditable(false);
		guideLines.setLayoutData(new GridData(GridData.FILL_BOTH));	
		setControl(container);
		
	}

	/**
	 * Gets the libraries.
	 *
	 * @return the libraries
	 */
	public Set<String> getLibraries() {
		Set<String> result = new HashSet<String>();
		for (final AssetInfo info : libraryEditor.getItems()) {
			result.add(info.getUniqueID());
		}
		return result;
	}

	/**
	 * Gets the projects.
	 *
	 * @return the projects
	 */
	public Set<String> getProjects() {
		Set<String> result = new HashSet<String>();
		for (final AssetInfo info : projectsEditor.getItems()) {
			result.add(info.getUniqueID());
		}
		return result;
	}
}
