/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.utils.plugin;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.ebayopensource.turmeric.eclipse.utils.collections.ListUtil;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;


/**
 * @author smathew
 * 
 */
public class ProjectUtil {

	public static void addNature(IProject project, IProgressMonitor monitor, 
			final String... natureIDs)
	throws CoreException {

		final List<String> newIds = new ArrayList<String>();
		for (final String natureID : natureIDs) {
			// if the nature doesnt exist
			if (StringUtils.isNotBlank(natureID)
					&& project.hasNature(natureID) == false) {
				newIds.add(natureID);
			}
		}
		
		if (newIds.isEmpty() == false) {
			// Adding the SOA Nature to the getProject() description.
			final IProjectDescription description = project
			.getDescription();
			final List<String> list = ListUtil.array(description
					.getNatureIds());
			list.addAll(newIds);
			description.setNatureIds(list.toArray(new String[0]));
			project.setDescription(description, monitor);
		}
	}
	
	public static void removeNatures(IProject project, IProgressMonitor monitor, 
			final String... natureIDs) 
	throws CoreException {
		final IProjectDescription description = project.getDescription();
		
		final List<String> natures = ListUtil.array(description.getNatureIds());
		natures.removeAll(ListUtil.array(natureIDs));
		description.setNatureIds(natures.toArray(new String[0]));
		project.setDescription(description, monitor);
	}
}
