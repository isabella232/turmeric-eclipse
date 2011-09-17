/*******************************************************************************
 * Copyright (c) 2006-2011 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/

package org.ebayopensource.turmeric.eclipse.ui;

import org.eclipse.debug.ui.IDebugUIConstants;
import org.eclipse.jdt.internal.ui.JavaPlugin;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.search.ui.NewSearchUI;
import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;
import org.eclipse.ui.console.IConsoleConstants;
import org.eclipse.ui.progress.IProgressConstants;
import org.eclipse.ui.texteditor.templates.TemplatesView;

/**
 * A factory for creating SOAPerspective objects.
 * 
 * copied code from JavaPerstiveFactory so we don't depend on internals.
 *
 * @author yayu
 * @author dcarver
 */
@SuppressWarnings("restriction")
public final class SOAPerspectiveFactory implements IPerspectiveFactory{
	
		
	private static final String UNTITLED_TEXT_FILE_WIZARD = "org.eclipse.ui.editors.wizards.UntitledTextFileWizard";
	private static final String NEW_FILE = "org.eclipse.ui.wizards.new.file";
	private static final String NEW_FOLDER = "org.eclipse.ui.wizards.new.folder";
	private static final String NEW_JAVA_WORKING_SET_WIZARD = "org.eclipse.jdt.ui.wizards.NewJavaWorkingSetWizard";
	private static final String NEW_SNIPPET_FILE_CREATION_WIZARD = "org.eclipse.jdt.ui.wizards.NewSnippetFileCreationWizard";
	private static final String NEW_SOURCE_FOLDER_CREATION_WIZARD = "org.eclipse.jdt.ui.wizards.NewSourceFolderCreationWizard";
	private static final String NEW_ANNOTATION_CREATION_WIZARD = "org.eclipse.jdt.ui.wizards.NewAnnotationCreationWizard";
	private static final String NEW_ENUM_CREATION_WIZARD = "org.eclipse.jdt.ui.wizards.NewEnumCreationWizard";
	private static final String NEWINTERFACECREATIONWIZARD = "org.eclipse.jdt.ui.wizards.NewInterfaceCreationWizard";
	private static final String NEW_CLASS_CREATION_WIZARD = "org.eclipse.jdt.ui.wizards.NewClassCreationWizard";
	private static final String NEW_PACKAGE_CREATION_WIZARD = "org.eclipse.jdt.ui.wizards.NewPackageCreationWizard";
	private static final String JAVA_PROJECT_WIZARD = "org.eclipse.jdt.ui.wizards.JavaProjectWizard";
	
	public static final String VIEWID_PROPERTY = "org.eclipse.ui.views.PropertySheet";

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
 		String editorArea = layout.getEditorArea();

		IFolderLayout folder= layout.createFolder("left", IPageLayout.LEFT, (float)0.25, editorArea); //$NON-NLS-1$
		folder.addView(JavaUI.ID_PACKAGES);
		folder.addPlaceholder(JavaUI.ID_TYPE_HIERARCHY);
		folder.addPlaceholder(JavaPlugin.ID_RES_NAV);
		folder.addPlaceholder(IPageLayout.ID_PROJECT_EXPLORER);

		IFolderLayout outputfolder= layout.createFolder("bottom", IPageLayout.BOTTOM, (float)0.75, editorArea); //$NON-NLS-1$
		outputfolder.addView(IPageLayout.ID_PROBLEM_VIEW);
		outputfolder.addView(JavaUI.ID_JAVADOC_VIEW);
		outputfolder.addView(JavaUI.ID_SOURCE_VIEW);
		outputfolder.addPlaceholder(NewSearchUI.SEARCH_VIEW_ID);
		outputfolder.addPlaceholder(IConsoleConstants.ID_CONSOLE_VIEW);
		outputfolder.addPlaceholder(IPageLayout.ID_BOOKMARKS);
		outputfolder.addPlaceholder(IProgressConstants.PROGRESS_VIEW_ID);

		IFolderLayout outlineFolder = layout.createFolder("right", IPageLayout.RIGHT, (float)0.75, editorArea); //$NON-NLS-1$
		outlineFolder.addView(IPageLayout.ID_OUTLINE);

		outlineFolder.addPlaceholder(TemplatesView.ID);

		layout.addActionSet(IDebugUIConstants.LAUNCH_ACTION_SET);
		layout.addActionSet(JavaUI.ID_ACTION_SET);
		layout.addActionSet(JavaUI.ID_ELEMENT_CREATION_ACTION_SET);
		layout.addActionSet(IPageLayout.ID_NAVIGATE_ACTION_SET);

		// views - java
		layout.addShowViewShortcut(JavaUI.ID_PACKAGES);
		layout.addShowViewShortcut(JavaUI.ID_TYPE_HIERARCHY);
		layout.addShowViewShortcut(JavaUI.ID_SOURCE_VIEW);
		layout.addShowViewShortcut(JavaUI.ID_JAVADOC_VIEW);


		// views - search
		layout.addShowViewShortcut(NewSearchUI.SEARCH_VIEW_ID);

		// views - debugging
		layout.addShowViewShortcut(IConsoleConstants.ID_CONSOLE_VIEW);

		// views - standard workbench
		layout.addShowViewShortcut(IPageLayout.ID_OUTLINE);
		layout.addShowViewShortcut(IPageLayout.ID_PROBLEM_VIEW);
		layout.addShowViewShortcut(JavaPlugin.ID_RES_NAV);
		layout.addShowViewShortcut(IPageLayout.ID_TASK_LIST);
		layout.addShowViewShortcut(IProgressConstants.PROGRESS_VIEW_ID);
		layout.addShowViewShortcut(IPageLayout.ID_PROJECT_EXPLORER);
		layout.addShowViewShortcut(TemplatesView.ID);

		// new actions - Java project creation wizard
		layout.addNewWizardShortcut(JAVA_PROJECT_WIZARD); 
		layout.addNewWizardShortcut(NEW_PACKAGE_CREATION_WIZARD); //$NON-NLS-1$
		layout.addNewWizardShortcut(NEW_CLASS_CREATION_WIZARD); //$NON-NLS-1$
		layout.addNewWizardShortcut(NEWINTERFACECREATIONWIZARD); //$NON-NLS-1$
		layout.addNewWizardShortcut(NEW_ENUM_CREATION_WIZARD); //$NON-NLS-1$
		layout.addNewWizardShortcut(NEW_ANNOTATION_CREATION_WIZARD); //$NON-NLS-1$
		layout.addNewWizardShortcut(NEW_SOURCE_FOLDER_CREATION_WIZARD);	 //$NON-NLS-1$
		layout.addNewWizardShortcut(NEW_SNIPPET_FILE_CREATION_WIZARD); //$NON-NLS-1$
		layout.addNewWizardShortcut(NEW_JAVA_WORKING_SET_WIZARD); //$NON-NLS-1$
		layout.addNewWizardShortcut(NEW_FOLDER);//$NON-NLS-1$
		layout.addNewWizardShortcut(NEW_FILE);//$NON-NLS-1$
		layout.addNewWizardShortcut(UNTITLED_TEXT_FILE_WIZARD);//$NON-NLS-1$

		// 'Window' > 'Open Perspective' contributions
		layout.addPerspectiveShortcut(JavaUI.ID_BROWSING_PERSPECTIVE);
		layout.addPerspectiveShortcut(IDebugUIConstants.ID_DEBUG_PERSPECTIVE);
	}

}
