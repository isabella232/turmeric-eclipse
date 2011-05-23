/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.ui.components;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang.StringUtils;
import org.ebayopensource.turmeric.eclipse.core.logging.SOALogger;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.GlobalRepositorySystem;
import org.ebayopensource.turmeric.eclipse.resources.model.AssetInfo;
import org.ebayopensource.turmeric.eclipse.resources.model.ISOAProject;
import org.ebayopensource.turmeric.eclipse.utils.collections.SetUtil;
import org.ebayopensource.turmeric.eclipse.utils.lang.StringUtil;
import org.ebayopensource.turmeric.eclipse.utils.ui.UIUtil;
import org.eclipse.jface.preference.ListEditor;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;


/**
 * The Class DependencyListEditor.
 *
 * @author smathew
 * 
 * List Editor for dependency management UI
 */
public class DependencyListEditor extends ListEditor {
	
	/** The Constant DEPENDENCY_TYPE_LIBRARY. */
	public static final String DEPENDENCY_TYPE_LIBRARY = "Library";

	private Composite parentComposite;

	private Set<? extends AssetInfo> libraries;

	private Map<String, AssetInfo> availableLibs = new ConcurrentHashMap<String, AssetInfo>();

	private String dependencyType = DEPENDENCY_TYPE_LIBRARY;

	private IDependencyLazyLoader dependencyLazyLoader;

	private ISOAProject soaProject;

	/**
	 * Instantiates a new dependency list editor.
	 *
	 * @param labelText the label text
	 * @param composite the composite
	 * @param libs the libs
	 * @param dependencyType the dependency type
	 */
	public DependencyListEditor(String labelText, Composite composite,
			Set<? extends AssetInfo> libs, String dependencyType) {
		this(labelText, composite, libs, null, dependencyType);
	}

	/**
	 * Instantiates a new dependency list editor.
	 *
	 * @param labelText the label text
	 * @param parentComposite the parent composite
	 * @param dependencyLazyLoader the dependency lazy loader
	 * @param soaProject the soa project
	 * @param dependencyType the dependency type
	 */
	public DependencyListEditor(String labelText, Composite parentComposite,
			IDependencyLazyLoader dependencyLazyLoader, ISOAProject soaProject,
			String dependencyType) {
		super("", labelText, parentComposite);
		this.parentComposite = parentComposite;
		this.dependencyType = dependencyType;
		this.dependencyLazyLoader = dependencyLazyLoader;
		this.soaProject = soaProject;
		initAlreadyAddedLibraries(this.dependencyLazyLoader
				.getAlreadyAddedLibraries());
	}

	/**
	 * Instantiates a new dependency list editor.
	 *
	 * @param labelText the label text
	 * @param composite the composite
	 * @param libs the libs
	 * @param alreadyAddedLibraries the already added libraries
	 * @param dependencyType the dependency type
	 */
	public DependencyListEditor(String labelText, Composite composite,
			Set<? extends AssetInfo> libs,
			Set<? extends AssetInfo> alreadyAddedLibraries,
			String dependencyType) {
		super("", labelText, composite);
		parentComposite = composite;
		libraries = libs;
		initAlreadyAddedLibraries(alreadyAddedLibraries);
		this.dependencyType = dependencyType;
	}

	/**
	 * Instantiates a new dependency list editor.
	 *
	 * @param labelText the label text
	 * @param composite the composite
	 * @param libs the libs
	 * @param alreadyAddedLibraries the already added libraries
	 */
	public DependencyListEditor(String labelText, Composite composite,
			Set<? extends AssetInfo> libs,
			Set<? extends AssetInfo> alreadyAddedLibraries) {
		this(labelText, composite, libs, alreadyAddedLibraries,
				DEPENDENCY_TYPE_LIBRARY);
	}

	private void initAlreadyAddedLibraries(
			Set<? extends AssetInfo> alreadyAddedLibraries) {
		if (alreadyAddedLibraries != null) {
			String alreadyAdded[] = new String[alreadyAddedLibraries.size()];
			int index = 0;
			for (AssetInfo info : alreadyAddedLibraries) {
				alreadyAdded[index++] = info.getDescription();
				this.availableLibs.put(info.getDescription(), info);
			}
			Arrays.sort(alreadyAdded);
			getListControl(parentComposite).setItems(alreadyAdded);
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.preference.ListEditor#doFillIntoGrid(org.eclipse.swt.widgets.Composite, int)
	 */
	@Override
	protected void doFillIntoGrid(final Composite parent, final int numColumns) {

		super.doFillIntoGrid(parent, numColumns);
		// This code just browse through the buttons
		// and change the names from new to add
		for (final Control btn : getButtonBoxControl(parent).getChildren()) {
			if (btn instanceof Button) {
				final Button button = (Button) btn;
				if (JFaceResources.getString("ListEditor.add").equals(
						button.getText()))
					button.setText("&Add ");
				else if (JFaceResources.getString("ListEditor.remove").equals(
						button.getText()))
					button.setText("&Remove ");
				((GridData) button.getLayoutData()).widthHint += 15;
			}
		}
	}

	/**
	 * Gets the items.
	 *
	 * @return the items
	 */
	public Set<AssetInfo> getItems() {
		final Set<AssetInfo> result = new HashSet<AssetInfo>();
		for (final String item : getListControl(parentComposite).getItems()) {
			if (availableLibs.containsKey(item)) {
				result.add(availableLibs.get(item));
			}
		}
		return result;
	}

	/**
	 * Gets the libraries.
	 *
	 * @return the libraries
	 */
	public Set<? extends AssetInfo> getLibraries() {
		if (libraries == null && dependencyLazyLoader != null) {
			libraries = dependencyLazyLoader.getLibs();
		}
		return libraries;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.preference.ListEditor#getNewInputObject()
	 */
	@Override
	protected String getNewInputObject() {
		// filtering already added items from the Add dialog
		final Set<AssetInfo> availableLibs = new TreeSet<AssetInfo>();
		final Set<String> items = SetUtil.hashSet(getListControl(
				parentComposite).getItems());
		Set<? extends AssetInfo> libraries = getLibraries();
		for (final AssetInfo info : libraries) {
			if (items.contains(info.getDescription()) == false) {
				availableLibs.add(info);
			}
		}

		AssetInfo libInfo = null;
		if (DEPENDENCY_TYPE_LIBRARY.equals(dependencyType)) {
			try {
				final Collection<AssetInfo> result = GlobalRepositorySystem.instanceOf()
				.getActiveRepositorySystem().getLibraryDependencyDialog()
				.open(parentComposite.getShell(),
						this.soaProject, availableLibs, getLibraries());
				if (result.isEmpty() == false && result.iterator().hasNext()) {
					libInfo = result.iterator().next();
				}
				/*libInfo = GlobalRepositorySystem.instanceOf()
						.getActiveRepositorySystem().getProjectConfigurer()
						.addLibraryDependency();*/
			} catch (Exception e) {
				SOALogger.getLogger().error(e);
				UIUtil.showErrorDialog(parentComposite.getShell(),
						"Error Occured", e.getLocalizedMessage(), e);
			}
		} else {
			DependencySelector selector = new DependencySelector(
					parentComposite.getShell(), availableLibs, StringUtil
							.toString("Select ", dependencyType, " to add:"));

			if (selector.open() == Window.OK
					&& selector.getFirstResult() instanceof AssetInfo)
				libInfo = (AssetInfo) selector.getFirstResult();
		}

		if (StringUtils.isBlank(libInfo.getName()))
			return null;
		this.availableLibs.put(libInfo.getDescription(), libInfo);
		return libInfo.getDescription();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.preference.ListEditor#parseString(java.lang.String)
	 */
	@Override
	protected String[] parseString(final String stringList) {
		// we dont need preference related stuff
		return new String[0];
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.preference.ListEditor#createList(java.lang.String[])
	 */
	@Override
	protected String createList(final String[] items) {
		// we dont need preference related stuff
		return "";
	}

	/**
	 * used for lazy loading support.
	 * 
	 */
	public interface IDependencyLazyLoader {

		/**
		 * use this method to delegate the load process after the UI controls
		 * has set up.
		 *
		 * @return the libs
		 */
		Set<? extends AssetInfo> getLibs();

		/**
		 * this method is used to init UI controls,be sure to avoid adding
		 * time-consuming logic here.
		 *
		 * @return the already added libraries
		 */
		Set<? extends AssetInfo> getAlreadyAddedLibraries();

	}
}
