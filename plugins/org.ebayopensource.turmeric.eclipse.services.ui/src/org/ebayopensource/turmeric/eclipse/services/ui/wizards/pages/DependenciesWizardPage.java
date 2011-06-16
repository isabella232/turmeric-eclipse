/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.services.ui.wizards.pages;

import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import org.ebayopensource.turmeric.eclipse.logging.SOALogger;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.GlobalRepositorySystem;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.ISOAHelpProvider;
import org.ebayopensource.turmeric.eclipse.repositorysystem.utils.RepositoryUtils;
import org.ebayopensource.turmeric.eclipse.resources.model.AssetInfo;
import org.ebayopensource.turmeric.eclipse.resources.model.ProjectInfo;
import org.ebayopensource.turmeric.eclipse.ui.components.DependencyListEditor;
import org.ebayopensource.turmeric.eclipse.ui.components.DependencyListEditor.IDependencyLazyLoader;
import org.ebayopensource.turmeric.eclipse.utils.ui.UIUtil;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;


/**
 * @author smathew
 * 
 * The project and library dependency page This page is used for both project
 * and library dependency
 * 
 */
public class DependenciesWizardPage extends WizardPage implements IWizardPage {
	private DependencyListEditor projectsEditor;

	private DependencyListEditor libraryEditor;

	public DependenciesWizardPage(final String projectType) {
		super("newSOAServiceProjectDependenciesWizardPage");
		setTitle(projectType + " Dependencies");
		setDescription("This wizard page adds project and library dependencies to the new "
				+ projectType + " Project.");
	}

	public void createControl(final Composite parent) {

		//Set<AssetInfo> libraries = new TreeSet<AssetInfo>();
		Set<? extends AssetInfo> projects = new TreeSet<ProjectInfo>();
		try {
			projects = RepositoryUtils.getProjectInfoProjectsFromWorkSpace();
		} catch (Exception e) {
			SOALogger.getLogger().error(e);
			throw new RuntimeException(e);
		}

		final Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(new GridLayout(1, true));

		final Composite composite = new Composite(container, SWT.NULL);
		composite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		projectsEditor = new DependencyListEditor("Project Dependencies:",
				composite, projects, "Project");
		setControl(container);
		
		IDependencyLazyLoader dependencyLazyLoader = new IDependencyLazyLoader() {		
			public Set<? extends AssetInfo> getAlreadyAddedLibraries() {
				return null;
			}

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
		
		setControl(container);
		UIUtil.getHelpSystem().setHelp(container,
				GlobalRepositorySystem.instanceOf().getActiveRepositorySystem()
				.getHelpProvider().getHelpContextID(ISOAHelpProvider.PAGE_SERVICE_DEPENDENCIES));
	}

	public Set<String> getLibraries() {
		Set<String> result = new HashSet<String>();
		for (final AssetInfo info : libraryEditor.getItems()) {
			result.add(info.getUniqueID());
		}
		return result;
	}

	public Set<String> getProjects() {
		Set<String> result = new HashSet<String>();
		for (final AssetInfo info : projectsEditor.getItems()) {
			result.add(info.getUniqueID());
		}
		return result;
	}
}
