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

import org.eclipse.jdt.internal.ui.JavaPerspectiveFactory;
//import org.eclipse.jdt.internal.ui.JavaPlugin;
//import org.eclipse.jdt.ui.JavaUI;
//
//import org.eclipse.search.ui.NewSearchUI;
//import org.eclipse.ui.IFolderLayout;
//import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPageLayout;
//import org.eclipse.ui.progress.IProgressConstants;

//import org.eclipse.ui.internal.PageLayout;

/**
 * A factory for creating SOAPerspective objects.
 *
 * @author yayu
 */
@SuppressWarnings("restriction")
public final class SOAPerspectiveFactory extends JavaPerspectiveFactory{
	
	/** The Constant VIEWID_GLOBAL_TYPE_REGISTRY. */
	public static final String VIEWID_GLOBAL_TYPE_REGISTRY = "org.ebayopensource.turmeric.eclipse.typelibrary.registryView";
	
	/** The Constant VIEWID_PROPERTY. */
	
	private static final String UNTITLED_TEXT_FILE_WIZARD = "org.eclipse.ui.editors.wizards.UntitledTextFileWizard";	private static final String NEW_FILE = "org.eclipse.ui.wizards.new.file";	private static final String NEW_FOLDER = "org.eclipse.ui.wizards.new.folder";	private static final String NEW_JAVA_WORKING_SET_WIZARD = "org.eclipse.jdt.ui.wizards.NewJavaWorkingSetWizard";	private static final String NEW_SNIPPET_FILE_CREATION_WIZARD = "org.eclipse.jdt.ui.wizards.NewSnippetFileCreationWizard";	private static final String NEW_SOURCE_FOLDER_CREATION_WIZARD = "org.eclipse.jdt.ui.wizards.NewSourceFolderCreationWizard";	private static final String NEW_ANNOTATION_CREATION_WIZARD = "org.eclipse.jdt.ui.wizards.NewAnnotationCreationWizard";	private static final String NEW_ENUM_CREATION_WIZARD = "org.eclipse.jdt.ui.wizards.NewEnumCreationWizard";	private static final String NEWINTERFACECREATIONWIZARD = "org.eclipse.jdt.ui.wizards.NewInterfaceCreationWizard";	private static final String NEW_CLASS_CREATION_WIZARD = "org.eclipse.jdt.ui.wizards.NewClassCreationWizard";	private static final String NEW_PACKAGE_CREATION_WIZARD = "org.eclipse.jdt.ui.wizards.NewPackageCreationWizard";	private static final String JAVA_PROJECT_WIZARD = "org.eclipse.jdt.ui.wizards.JavaProjectWizard";	public static final String VIEWID_PROPERTY = "org.eclipse.ui.views.PropertySheet";
	
	/**
	 * Instantiates a new sOA perspective factory.
	 */
	public SOAPerspectiveFactory() {
		super();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void createInitialLayout(IPageLayout layout) {
		super.createInitialLayout(layout);
		
	}

}
