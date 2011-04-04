/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.ui.views.registry;

import org.ebayopensource.turmeric.common.config.LibraryType;
import org.ebayopensource.turmeric.eclipse.core.logging.SOALogger;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.SOAGlobalRegistryAdapter;
import org.ebayopensource.turmeric.eclipse.ui.resources.SOAMessages;
import org.ebayopensource.turmeric.tools.library.SOATypeRegistry;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.part.ViewPart;

/**
 * The Types Explorer view. The main class that represents the types
 * available for consumption in one place. Other actions like refresh, import
 * are also provided in this view. It also has the quick search functionality.
 * Technically this is just another view part. The default data source is either
 * the maven repository or the clearcase view based on the user selection. The
 * main functionality are all hosted in this viewer and that way this is the
 * heart of the type library system.
 * 
 * @author smathew
 * 
 */
public class RegistryView extends ViewPart {
	public static final String VIEW_ID = "org.ebayopensource.turmeric.eclipse.typelibrary.registryView"; 
	private static final SOALogger logger = SOALogger.getLogger();
	private TreeViewer typeLibraryViewer;
	private TypeViewer typeViewer;
	private Text typeNameText;
	private TypeLibrarySelectionChangedListener typeLibrarySelectionChangedListener;
	private TypeSelectionListener typeSelectionListener;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.part.WorkbenchPart#createPartControl(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public void createPartControl(Composite parent) {
		final FormToolkit toolkit = new FormToolkit(parent.getDisplay());
		final Composite composite = toolkit.createComposite(parent);
		composite.setLayout(new GridLayout(1, true));

		createSearchArea(composite, toolkit);

		try {
			createClientArea(composite, toolkit);
		} catch (Exception e) {
			logger.error(e);
			throw new RuntimeException(e);
		}
		//createToolbar();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.part.WorkbenchPart#setFocus()
	 */
	@Override
	public void setFocus() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.part.WorkbenchPart#dispose()
	 */
	@Override
	public void dispose() {
		if (typeLibraryViewer != null
				&& typeLibrarySelectionChangedListener != null) {
			typeLibraryViewer
					.removeSelectionChangedListener(typeLibrarySelectionChangedListener);
		}
		if (typeViewer != null && typeSelectionListener != null) {
			typeViewer.removeSelectionChangedListener(typeSelectionListener);
		}
		super.dispose();
	}

	private void createSearchArea(final Composite parent, FormToolkit toolkit) {
		final Composite composite = toolkit.createComposite(parent);
		composite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		composite.setLayout(new GridLayout(2, false));

		final Label label = toolkit.createLabel(composite, SOAMessages.SEARCH);
		label.setLayoutData(new GridData());

		typeNameText = toolkit.createText(composite, "", SWT.LEFT | SWT.BORDER);
		final GridData data = new GridData();
		data.widthHint = 200;
		typeNameText.setLayoutData(data);
		typeNameText.addModifyListener(new RegModifyListener());
		typeNameText.setToolTipText(SOAMessages.SEARCH_TIP);
	}

	private void createClientArea(Composite parent, FormToolkit toolkit)
			throws Exception {
		SOATypeRegistry typeRegistry = null;
		try {
			typeRegistry = SOAGlobalRegistryAdapter.getInstance().getGlobalRegistry();
		} catch (Exception e) {
			logger.warning(e);
		}

		final SashForm sashComposite = new SashForm(parent, SWT.NONE);
		sashComposite.setOrientation(SWT.HORIZONTAL);
		sashComposite.SASH_WIDTH = 5;
		sashComposite.setBackground(toolkit.getColors().getBackground());
		sashComposite.setLayoutData(new GridData(GridData.FILL_BOTH));

		// TypeLibrary viewer
		final Group typeLibGroup = new Group(sashComposite,
				SWT.SHADOW_ETCHED_OUT);
		typeLibGroup.setBackground(toolkit.getColors().getBackground());
		typeLibGroup.setText(SOAMessages.TYPE_LIB);
		typeLibGroup.setToolTipText(SOAMessages.TYPE_LIB_WKS);
		typeLibGroup.setLayoutData(new GridData(GridData.FILL_VERTICAL));
		final FillLayout layout = new FillLayout(SWT.VERTICAL);
		layout.marginHeight = 5;
		typeLibGroup.setLayout(layout);
		final Tree typeLibTree = toolkit.createTree(typeLibGroup, SWT.H_SCROLL
				| SWT.V_SCROLL | SWT.SINGLE);
		// typeLibTree.setLayoutData(new GridData(GridData.FILL_VERTICAL));
		typeLibraryViewer = new TreeViewer(typeLibTree);
		typeLibraryViewer.setContentProvider(new TypeLibraryContentProvider());
		typeLibraryViewer.setLabelProvider(new TypeLibraryLabelProvider());
		typeLibraryViewer.setInput(typeRegistry);
		typeLibraryViewer.getControl().setToolTipText(SOAMessages.TREE_TIP);
		// Type Viewer
		final Group typeGroup = new Group(sashComposite, SWT.SHADOW_ETCHED_OUT);
		typeGroup.setBackground(toolkit.getColors().getBackground());
		typeGroup.setText(SOAMessages.TYPES);
		typeGroup.setToolTipText(SOAMessages.TYPES_WKS);
		typeGroup.setLayoutData(new GridData(GridData.FILL_BOTH));
		typeGroup.setLayout(layout);
		final Table typeTable = toolkit.createTable(typeGroup, SWT.H_SCROLL
				| SWT.V_SCROLL | SWT.MULTI | SWT.FULL_SELECTION);
		// typeTable.setLayoutData(new GridData(GridData.FILL_BOTH));
		RefreshRegistryAction refreshAction = new RefreshRegistryAction(
				typeLibraryViewer);
		typeViewer = new TypeViewer(typeTable, refreshAction);
		refreshAction.setTypeViewer(typeViewer);
		typeViewer.setInput(typeRegistry);

		typeLibrarySelectionChangedListener = new TypeLibrarySelectionChangedListener(
				typeViewer);
		typeLibraryViewer
				.addSelectionChangedListener(typeLibrarySelectionChangedListener);

		typeSelectionListener = new TypeSelectionListener(typeLibraryViewer);
		typeViewer.addSelectionChangedListener(typeSelectionListener);

		int weightsParent[] = { 30, 70 };
		sashComposite.setWeights(weightsParent);
		toolkit.adapt(sashComposite);
		toolkit.paintBordersFor(sashComposite);
	}

	/**
	 * This is a smart implementation quick search. Basically we remove and add
	 * the filters based on the search text entered by the user rather than the
	 * usual text.equals(text) and remove the ones not satisfying. The heavy
	 * lifting is done by the eclipse framework.
	 * 
	 * @author smathew
	 * 
	 */
	class RegModifyListener implements ModifyListener {
		public void modifyText(ModifyEvent e) {
			if (e.getSource() instanceof Text) {
				String entered = ((Text) e.getSource()).getText();
				if (typeViewer != null) {
					for (ViewerFilter filter : typeViewer.getFilters()) {
						if (filter instanceof TypeFilterHonSearch) {
							// there is one already. remove it first
							typeViewer.removeFilter(filter);
						}
					}
					typeViewer.addFilter(new TypeFilterHonSearch(entered));
				}
			}
		}
	}
	
	public ViewerComparator getLibraryComparator() {
		final ViewerComparator comparator = new ViewerComparator() {
			@Override
			public int compare(Viewer viewer, Object e1, Object e2) {
				if (e1 instanceof LibraryType && e2 instanceof LibraryType) {
					return ((LibraryType) e1).getName().compareTo(
							((LibraryType) e2).getName());
				}
				return String.CASE_INSENSITIVE_ORDER.compare(e1.toString(), e2
						.toString());
			}

		};
		return comparator;
	}
	
	public TreeViewer getTypeLibraryViewer() {
		return typeLibraryViewer;
	}
	
	public TypeViewer getTypeViewer() {
		return typeViewer;
	}
}
