/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.typelibrary.registry;

import java.util.ArrayList;

import org.ebayopensource.turmeric.eclipse.typelibrary.resources.SOAMessages;
import org.ebayopensource.turmeric.eclipse.utils.ui.UIUtil;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.dialogs.TwoPaneElementSelector;

import org.ebayopensource.turmeric.common.config.LibraryType;

/**
 * @author smathew
 * 
 * The selection dialog for adding types
 */
public class TypeSelector extends TwoPaneElementSelector {

	private ArrayList<LibraryType> selectedTypes = new ArrayList<LibraryType>();

	@Override
	protected void computeResult() {
		// TODO Auto-generated method stub
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

	public TypeSelector(Shell parent, String title, LibraryType libraryTpes[],
			String projectName) {
		this(parent, title, libraryTpes, projectName, "");
	}

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

	public ArrayList<LibraryType> getSelectedTypes() {
		if (selectedTypes == null) {
			selectedTypes = new ArrayList<LibraryType>();
		}
		return selectedTypes;
	}

	public void setSelectedTypes(ArrayList<LibraryType> selectedTypes) {
		this.selectedTypes = selectedTypes;
	}
}
