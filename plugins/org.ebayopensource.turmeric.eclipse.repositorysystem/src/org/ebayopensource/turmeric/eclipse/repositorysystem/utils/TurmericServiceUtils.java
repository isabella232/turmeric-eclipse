/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.repositorysystem.utils;

import org.ebayopensource.turmeric.eclipse.core.resources.constants.SOAProjectConstants.SupportedProjectType;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.GlobalRepositorySystem;
import org.ebayopensource.turmeric.eclipse.resources.util.SOAServiceUtil;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;

/**
 * The Class TurmericServiceUtils.
 *
 * @author yayu
 */
public final class TurmericServiceUtils {

	/**
	 * 
	 */
	private TurmericServiceUtils() {
		super();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	@Override
	protected Object clone() throws CloneNotSupportedException {
		throw new CloneNotSupportedException();
	}
	
	/**
	 * Returns true if the eclipse project passed is a SOA interface project,
	 * The evaluation is fully based on the nature. If the project is null or
	 * not accessible, this will throw an exception and the clients are supposed
	 * to handle it.
	 *
	 * @param project the project
	 * @return true, if is sOA interface project
	 * @throws CoreException the core exception
	 */
	public static boolean isSOAInterfaceProject(final IProject project)
			throws CoreException {
		return SOAServiceUtil.hasNatures(project,
				GlobalRepositorySystem.instanceOf()
				.getActiveRepositorySystem().getProjectNatureId(SupportedProjectType.INTERFACE));
	}

	/**
	 * Returns true if the eclipse project passed is a SOA consumer project, THe
	 * evaluation is fully based on the nature. If the project is null or not
	 * accessible, this will throw an exception and the clients are supposed to
	 * handle it.
	 *
	 * @param project the project
	 * @return true, if is sOA consumer project
	 * @throws CoreException the core exception
	 */
	public static boolean isSOAConsumerProject(final IProject project)
			throws CoreException {
		return SOAServiceUtil.hasNatures(project,
				GlobalRepositorySystem.instanceOf()
				.getActiveRepositorySystem().getProjectNatureId(SupportedProjectType.CONSUMER));
	}

	/**
	 * Returns true if the eclipse project passed is a SOA implementation
	 * project, The evaluation is fully based on the nature. If the project is
	 * null or not accessible, this will throw an exception and the clients are
	 * supposed to handle it.
	 *
	 * @param project the project
	 * @return true, if is sOA impl project
	 * @throws CoreException the core exception
	 */
	public static boolean isSOAImplProject(final IProject project)
			throws CoreException {
		return SOAServiceUtil.hasNatures(project,
				GlobalRepositorySystem.instanceOf()
				.getActiveRepositorySystem().getProjectNatureId(SupportedProjectType.IMPL));
	}

	/**
	 * Returns true if the eclipse project passed is a SOA Type Library project,
	 * The evaluation is fully based on the nature. If the project is null or
	 * not accessible, this will throw an exception and the clients are supposed
	 * to handle it.
	 *
	 * @param project the project
	 * @return true, if is sOA type library project
	 * @throws CoreException the core exception
	 */
	public static boolean isSOATypeLibraryProject(final IProject project)
			throws CoreException {
		return SOAServiceUtil.hasNatures(project,
				GlobalRepositorySystem.instanceOf()
				.getActiveRepositorySystem().getProjectNatureId(SupportedProjectType.TYPE_LIBRARY));
	}

	/**
	 * Returns true if the eclipse project passed is a SOA Error library
	 * project, The evaluation is fully based on the nature. If the project is
	 * null or not accessible, this will throw an exception and the clients are
	 * supposed to handle it.
	 *
	 * @param project the project
	 * @return true, if is sOA error library project
	 * @throws CoreException the core exception
	 */
	public static boolean isSOAErrorLibraryProject(final IProject project)
			throws CoreException {
		return SOAServiceUtil.hasNatures(project,
				GlobalRepositorySystem.instanceOf()
				.getActiveRepositorySystem().getProjectNatureId(SupportedProjectType.ERROR_LIBRARY));
	}

	/**
	 * Returns true if the eclipse project passed is a SOA interface, consumer
	 * or implementation project The evaluation is fully based on the nature. If
	 * the project is null or not accessible, this will throw an exception and
	 * the clients are supposed to handle it.
	 *
	 * @param project the project
	 * @return true, if is sOA project
	 * @throws CoreException the core exception
	 */
	public static boolean isSOAProject(final IProject project)
			throws CoreException {
		return GlobalRepositorySystem.instanceOf()
		.getActiveRepositorySystem().isValidTurmericProject(project);
	}

}
