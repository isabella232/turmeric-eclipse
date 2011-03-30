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
package org.ebayopensource.turmeric.eclipse.ui.components;

import java.util.Collection;
import java.util.Collections;

import org.ebayopensource.turmeric.eclipse.core.resources.constants.SOAProjectConstants;
import org.ebayopensource.turmeric.eclipse.logging.SOALogger;
import org.ebayopensource.turmeric.eclipse.resources.model.AssetInfo;
import org.ebayopensource.turmeric.eclipse.resources.util.SOAConsumerUtil.EnvironmentItem;
import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;


/**
 * @author yayu
 * @since 1.0.0
 */
public class SOAConsumerServicesViewer extends TreeViewer {
	private static final SOALogger logger = SOALogger.getLogger();
	private boolean useAssetInfo = true;
	
	/**
	 * @param parent
	 */
	public SOAConsumerServicesViewer(Composite parent) {
		this(parent, true);
	}
	
	
	public SOAConsumerServicesViewer(Composite parent, boolean useAssetInfo) {
		super(parent, SWT.BORDER | SWT.SINGLE | SWT.V_SCROLL
                | SWT.H_SCROLL| SWT.FULL_SELECTION);
		this.useAssetInfo = useAssetInfo;
		init();
	}


	protected void addContentProvider() {
		setContentProvider(new ITreeContentProvider() {

			public Object[] getChildren(Object parentElement) {
				return getElements(parentElement);
			}

			public Object getParent(Object element) {
				return null;
			}

			public boolean hasChildren(Object element) {
				return element instanceof EnvironmentItem;
			}

			public Object[] getElements(Object inputElement) {
				if (inputElement instanceof Collection<?>) {
					return ((Collection<?>)inputElement).toArray();
				} else if (inputElement instanceof EnvironmentItem) {
					
					return useAssetInfo ? 
							((EnvironmentItem)inputElement).getServiceData().values().toArray()
							: ((EnvironmentItem)inputElement).getServices().toArray();
				}
				return Collections.EMPTY_LIST.toArray();
			}

			public void dispose() {
				
			}

			public void inputChanged(Viewer viewer, Object oldInput,
					Object newInput) {
				
			}
		});
	}
	
	protected void addLabelProvider() {
		setLabelProvider(new ITableLabelProvider() {

			public Image getColumnImage(Object element, int columnIndex) {
				
				return null;
			}

			public String getColumnText(Object element, int columnIndex) {
				if (element instanceof AssetInfo) {
					if (columnIndex == 1)
						return ((AssetInfo)element).getDescription();
				} else if (element instanceof String) {
					if (columnIndex == 1)
						return element.toString();
				}  else if (element instanceof EnvironmentItem) {
					final EnvironmentItem info = (EnvironmentItem)element;
					if (columnIndex == 0) {
						return info.getName();
					}
				}
				return SOAProjectConstants.EMPTY_STRING;
			}

			public void addListener(ILabelProviderListener listener) {
				
			}

			public void dispose() {
				
			}

			public boolean isLabelProperty(Object element, String property) {
				return false;
			}

			public void removeListener(ILabelProviderListener listener) {
				
			}
		});
	}

	protected void init() {
		setAutoExpandLevel(TreeViewer.ALL_LEVELS);
		addContentProvider();
		addLabelProvider();
		createColumns();
	}
	
	protected void createColumns() {
		final Tree tree = getTree();
		Assert.isTrue(tree.getParent().getLayout() instanceof GridLayout, 
				"The SOA Consumer Services Viewer must be put in a composite that is using GridLayout.");
		tree.setLayoutData(new GridData(GridData.FILL_BOTH));
		tree.setHeaderVisible(true);
		tree.setLinesVisible(true);
		
		final TableLayout layout = new TableLayout();
		layout.addColumnData(new ColumnWeightData(40, 100, true));
		layout.addColumnData(new ColumnWeightData(60, 150, true));
		tree.setLayout(layout);
		
		TreeColumn column = new TreeColumn(tree, SWT.LEFT);
		column.setText("Environment");
		
		column = new TreeColumn(tree, SWT.LEFT);
		column.setText("Admin Name");
	}
	
	

}
