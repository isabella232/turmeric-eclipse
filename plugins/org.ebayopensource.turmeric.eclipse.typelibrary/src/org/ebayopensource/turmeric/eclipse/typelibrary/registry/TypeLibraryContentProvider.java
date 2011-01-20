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
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.StringUtils;
import org.ebayopensource.turmeric.eclipse.logging.SOALogger;
import org.ebayopensource.turmeric.eclipse.typelibrary.utils.TypeLibraryUtil;
import org.ebayopensource.turmeric.tools.library.SOATypeRegistry;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

import org.ebayopensource.turmeric.common.config.TypeLibraryType;

/**
 * @author smathew
 * 
 */
public class TypeLibraryContentProvider implements ITreeContentProvider {

	public Object[] getChildren(Object parentElement) {
		// this is the root.
		final List<IRegistryTreeNode> children = new ArrayList<IRegistryTreeNode>();
		if (parentElement instanceof TypeTreeRoot) {
			// first level
			for (String typeLibraryCategory : TypeLibraryUtil.getCategories()) {
				children.add(new CategoryTreeNode(typeLibraryCategory,
						(TypeTreeRoot) parentElement));
			}
		} else if (parentElement instanceof CategoryTreeNode) {
			final CategoryTreeNode parentNode = (CategoryTreeNode) parentElement;
			// this is second level
			SOATypeRegistry typeRegistry = parentNode.getTypeTreeRoot()
					.getTypeRegistry();
			try {
				final List<TypeLibraryType> libs = typeRegistry
						.getAllTypeLibraries();
				final List<TypeLibraryType> clonedList = new ArrayList<TypeLibraryType>(
						libs);
				// filtering out the non category elements
				CollectionUtils.filter(clonedList,
						getPredicate(((CategoryTreeNode) parentElement)
								.getCategory()));
				for (TypeLibraryType type : clonedList) {
					children.add(new TypeLibraryTreeNode(parentNode, type));
				}
			} catch (Exception e) {
				SOALogger.getLogger().error(e);
			}
		}
		return children.toArray();
	}

	public Object getParent(Object element) {
		if (element instanceof IRegistryTreeNode) {
			return ((IRegistryTreeNode) element).getParent();
		}
		return null;
	}

	public boolean hasChildren(Object element) {
		return (element instanceof TypeTreeRoot || element instanceof CategoryTreeNode);
	}

	public Object[] getElements(Object inputElement) {
		if (inputElement instanceof SOATypeRegistry) {
			TypeTreeRoot[] typeTreeRoots = new TypeTreeRoot[1];
			typeTreeRoots[0] = new TypeTreeRoot((SOATypeRegistry) inputElement);
			return typeTreeRoots;
		}
		return null;
	}

	public void dispose() {

	}

	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {

	}

	private Predicate getPredicate(final String typeLibraryCategory) {
		return new Predicate() {
			public boolean evaluate(Object arg0) {
				if (arg0 instanceof TypeLibraryType) {
					return StringUtils.equalsIgnoreCase(
							((TypeLibraryType) arg0).getCategory(),
							typeLibraryCategory);
				}
				return false;
			}

		};
	}
}
