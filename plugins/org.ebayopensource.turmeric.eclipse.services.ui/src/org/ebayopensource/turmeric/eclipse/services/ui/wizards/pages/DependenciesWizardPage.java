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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.httpclient.HttpException;
import org.apache.commons.lang.StringUtils;
import org.ebayopensource.turmeric.eclipse.core.logging.SOALogger;
import org.ebayopensource.turmeric.eclipse.maven.core.model.MavenAssetInfo;
import org.ebayopensource.turmeric.eclipse.mavenapi.impl.MavenEclipseUtil;
import org.ebayopensource.turmeric.eclipse.registry.models.ClientAssetModel;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.GlobalRepositorySystem;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.ISOAHelpProvider;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.SOAGlobalRegistryAdapter;
import org.ebayopensource.turmeric.eclipse.repositorysystem.utils.RepositoryUtils;
import org.ebayopensource.turmeric.eclipse.resources.model.AssetInfo;
import org.ebayopensource.turmeric.eclipse.resources.model.ProjectInfo;
import org.ebayopensource.turmeric.eclipse.resources.util.MarkerUtil;
import org.ebayopensource.turmeric.eclipse.ui.components.DependencyListEditor;
import org.ebayopensource.turmeric.eclipse.ui.components.DependencyListEditor.IDependencyLazyLoader;
import org.ebayopensource.turmeric.eclipse.utils.ui.ProjectUtils;
import org.ebayopensource.turmeric.eclipse.utils.ui.UIUtil;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * The Class DependenciesWizardPage.
 *
 * @author smathew
 * 
 * The project and library dependency page This page was used for both project
 * and library dependency, on the raptor branch it was used on RIDE 2.0.0 to 
 * show type library dependencies and suggestions, from later versions of RIDE it
 * will not be  apart of the consumer creation process.
 * 
 * 
 */
public class DependenciesWizardPage extends WizardPage implements IWizardPage {
	private DependencyListEditor projectsEditor;
	
	private DependencyListEditor libraryEditor;
	Map<String,Set<String>> packageLibMap = new HashMap<String,Set<String>>();
	private TableViewer viewer;
	private Button copyToCLipboard;

	/**
	 * Instantiates a new dependencies wizard page.
	 *
	 * @param projectType the project type
	 */
	public DependenciesWizardPage(final String projectType) {
		super("newSOAServiceProjectDependenciesWizardPage");
		setTitle("Type Library Dependencies");
		setDescription(	"Your wsdl has been associated with some type libraries."+
				"Please read the recommended action and act accordingly.");
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
		Label GuideLinesHeader = new Label(container,SWT.LEFT|SWT.BOLD|SWT.WRAP);
		GuideLinesHeader.setText("Split Package Validation for packages contributed by type libraries:");
	
		setControl(container);
		
		Label seperator = new Label(container, SWT.SEPARATOR | SWT.HORIZONTAL);
		seperator.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		final Composite libraryComposite = new Composite(container, SWT.NULL);
		libraryComposite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		libraryEditor = new DependencyListEditor("Library Dependencies:",
				libraryComposite,dependencyLazyLoader, null, DependencyListEditor.DEPENDENCY_TYPE_LIBRARY);
		
		
		UIUtil.getHelpSystem().setHelp(container,
				GlobalRepositorySystem.instanceOf().getActiveRepositorySystem()
				.getHelpProvider().getHelpContextID(ISOAHelpProvider.PAGE_SERVICE_DEPENDENCIES));
		
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


