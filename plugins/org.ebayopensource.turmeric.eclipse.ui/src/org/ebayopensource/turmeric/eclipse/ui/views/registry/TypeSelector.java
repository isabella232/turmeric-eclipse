/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.ui.views.registry;

import java.util.ArrayList;

import org.ebayopensource.turmeric.common.config.LibraryType;
import org.ebayopensource.turmeric.eclipse.ui.resources.SOAMessages;
import org.ebayopensource.turmeric.eclipse.utils.ui.UIUtil;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.dialogs.TwoPaneElementSelector;

// TODO: Auto-generated Javadoc
/**
 * The Class TypeSelector.
 *
 * @author smathew
 * 
 * The selection dialog for adding types
 */
public class TypeSelector extends TwoPaneElementSelector {

	/** The selected types. */
	private ArrayList<LibraryType> selectedTypes = new ArrayList<LibraryType>();

	/* (non-Javadoc)
	 * @see org.eclipse.ui.dialogs.TwoPaneElementSelector#computeResult()
	 */
	@Override
	protected void computeResult() {
		super.computeResult();
		selectedTypes = new ArrayList<LibraryType>();
		if (fFilteredList != null) {
			if (fFilteredList.getSelectionIndex() >= 0) {
				if (fFilteredList.getSelection() != null) {
					for (Object obj : fFilteredList.getSelection()) {
						if (obj instanceof LibraryType)
							selectedTypes.add((LibraryType) obj);
					}
				}
			}
		}

	}

	/**
	 * Instantiates a new type selector.
	 *
	 * @param parent the parent
	 * @param title the title
	 * @param libraryTpes the library tpes
	 * @param projectName the project name
	 * @param curTypeName the cur type name
	 */
	public TypeSelector(Shell parent, String title, LibraryType libraryTpes[],
			String projectName, String curTypeName) {
		super(parent, new TypeSelectorElementRenderer(),
				new TypeSelectorQualifiedRenderer());
		setTitle(title);
		try {
			setElements(libraryTpes);
		} catch (Exception e) {
			UIUtil.showErrorDialog(parent, e.getMessage(), SOAMessages.ERROR,
					SOAMessages.TYPE_LIB_ERR);
		}
	}

	/**
	 * Instantiates a new type selector.
	 *
	 * @param parent the parent
	 * @param title the title
	 * @param libraryTpes the library tpes
	 * @param projectName the project name
	 */
	public TypeSelector(Shell parent, String title, LibraryType libraryTpes[],
			String projectName) {
		this(parent, title, libraryTpes, projectName, "");
	}

	/**
	 * Instantiates a new type selector.
	 *
	 * @param parent the parent
	 * @param title the title
	 * @param libraryTpes the library tpes
	 * @param projectName the project name
	 * @param renderer the renderer
	 */
	public TypeSelector(Shell parent, String title, LibraryType libraryTpes[],
			String projectName, TypeSelectorElementRenderer renderer) {
		super(parent, renderer, new TypeSelectorQualifiedRenderer());
		setTitle(title);
		try {
			setElements(libraryTpes);
		} catch (Exception e) {
			UIUtil.showErrorDialog(parent, e.getMessage(), SOAMessages.ERROR,
					SOAMessages.TYPE_LIB_ERR);
		}
	}

	/**
	 * Gets the selected types.
	 *
	 * @return the selected types
	 */
	public ArrayList<LibraryType> getSelectedTypes() {
		if (selectedTypes == null) {
			selectedTypes = new ArrayList<LibraryType>();
		}
		return selectedTypes;
	}

	/**
	 * Sets the selected types.
	 *
	 * @param selectedTypes the new selected types
	 */
	public void setSelectedTypes(ArrayList<LibraryType> selectedTypes) {
		this.selectedTypes = selectedTypes;
	}
}
