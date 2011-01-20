/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.typelibrary.builders;

import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.ebayopensource.turmeric.eclipse.codegen.utils.CodegenInvoker;
import org.ebayopensource.turmeric.eclipse.logging.SOALogger;
import org.ebayopensource.turmeric.eclipse.repositorysystem.RepositorySystemActivator;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.GlobalRepositorySystem;
import org.ebayopensource.turmeric.eclipse.resources.constants.SOAProjectConstants.SupportedProjectType;
import org.ebayopensource.turmeric.eclipse.resources.util.SOAServiceUtil;
import org.ebayopensource.turmeric.eclipse.typelibrary.core.SOATypeLibraryConstants;
import org.ebayopensource.turmeric.eclipse.typelibrary.core.wst.SOAXSDValidator;
import org.ebayopensource.turmeric.eclipse.typelibrary.utils.TypeLibraryUtil;
import org.ebayopensource.turmeric.eclipse.utils.classloader.SOAPluginClassLoader;
import org.ebayopensource.turmeric.eclipse.utils.plugin.EclipseMessageUtils;
import org.ebayopensource.turmeric.eclipse.utils.plugin.WorkspaceUtil;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jdt.core.JavaCore;


/**
 * 
 * Standard Type Library Builder Utils.
 * 
 * @author smathew
 * 
 */
public class TypeLibraryBuilderUtils {
	private static final SOALogger logger = SOALogger.getLogger();

	/**
	 * Wrapper API for TypeLibraryDeltaVisitor pattern. Returns all the XSDs
	 * which has been modified according to the delta
	 * 
	 * @param delta -
	 *            resource delta
	 * @param project
	 * @return list of modified xsds.
	 * @throws CoreException
	 */
	public static List<IFile> getModifiedXsds(IResourceDelta delta,
			IProject project) throws CoreException {
		TypeLibraryDeltaVisitor typeLibraryDeltaVisitor = new TypeLibraryDeltaVisitor(
				project);
		delta.accept(typeLibraryDeltaVisitor);
		return typeLibraryDeltaVisitor.getModifiedXsds();
	}

	/**
	 * Examine if the project is not null, accessible, has type lib nature. It
	 * also examines the readability and writability aspect.
	 * 
	 * @param project -
	 *            the project to be examined.
	 * @return
	 * @throws Exception
	 */
	public static IStatus checkProjectHealth(final IProject project)
			throws Exception {
		if (project == null)
			return EclipseMessageUtils.createErrorStatus(
					RepositorySystemActivator.PLUGIN_ID, "Project is null",
					null);

		if (project.isAccessible() == false)
			return EclipseMessageUtils.createResourceErrorStatus(project
					.getLocation(), "Project is not accessible->" + project,
					null);

		if (!project.hasNature(TypeLibraryProjectNature.getTypeLibraryNatureId())) {
			return EclipseMessageUtils.createResourceErrorStatus(project
					.getLocation(),
					"Project is not a valid Type library project->" + project,
					null);
		}

		for (final IResource resource : getTypeLibProjectReadableResources(project)) {
			if (WorkspaceUtil.isResourceReadable(resource) == false) {
				return EclipseMessageUtils.createResourceWarnStatus(resource
						.getLocation(),
						"Resource does not exist or is not readable->"
								+ resource.getName(), null);
			}
		}
		for (final IResource resource : getTypeLibProjectWritableResources(project)) {
			if (WorkspaceUtil.isResourceModifiable(resource) == false) {
				return EclipseMessageUtils.createSOAResourceWarnStatus(
						resource,
						"Resource does not exist or is not modifiable->"
								+ resource.getName(), null);
			}
		}

		return Status.OK_STATUS;
	}

	/**
	 * Validate the xsd for any wtp validation issues.
	 * 
	 * @param project -
	 *            the parent project
	 * @param complainAboutMissingXSDs -
	 *            if this is true if there is a missing xsd file( opposed to the
	 *            type info x),
	 * @return
	 * @throws Exception
	 */
	public static MultiStatus validateXSDS(IProject project,
			boolean complainAboutMissingXSDs) throws Exception {
		SOAXSDValidator sOAXSdValidator = new SOAXSDValidator();
		ArrayList<IStatus> statusList = new ArrayList<IStatus>();
		MultiStatus multiStatus = (MultiStatus) EclipseMessageUtils
				.createEmptyOKMultiStatus("XSD Validation");
		for (IFile file : TypeLibraryUtil.getAllXsdFiles(project, true)) {
			if (file != null && file.isAccessible()) {
				statusList.add(sOAXSdValidator.validate(file));
			} else {
				if (complainAboutMissingXSDs)
					statusList
							.add(EclipseMessageUtils
									.createResourceErrorStatus(
											project.getFullPath(),
											"This XSD type has an entry in typeinformation.xml and is not accessible now. Please fix this.->"
													+ file.getName(), null));
			}
		}
		if (!statusList.isEmpty()) {
			multiStatus = (MultiStatus) EclipseMessageUtils
					.createErrorMultiStatus(statusList, "XSD Validation");
		}
		return multiStatus;
	}

	/**
	 * 
	 * Checks an interface project's health against soa standards. We might have
	 * to move it to interface package. but a small problem is that this checks
	 * mainly for the type library dependency feature prerequisites. WSDL should
	 * be there, Type Dependency file should be there. Nature should be correct.
	 * 
	 * @param project
	 * @return
	 * @throws Exception
	 */
	public static IStatus checkProjectHealthForIntfProject(
			final IProject project) throws Exception {
		
		if (project == null)
			return EclipseMessageUtils.createErrorStatus(
					RepositorySystemActivator.PLUGIN_ID, "Project is null",
					null);

		if (project.isAccessible() == false)
			return EclipseMessageUtils.createResourceErrorStatus(project
					.getLocation(), "Project is not accessible->" + project,
					null);

		if (project
				.hasNature(GlobalRepositorySystem.instanceOf().getActiveRepositorySystem()
						.getProjectNatureId(SupportedProjectType.INTERFACE)) == false) {
			return EclipseMessageUtils.createResourceErrorStatus(project
					.getLocation(), "Project is not a valid Interface project->"
					+ project, null);
		}
		
		

		for (final IResource resource : getIntfProjectReadableResources(project)) {
			if (WorkspaceUtil.isResourceReadable(resource) == false) {
				return EclipseMessageUtils.createResourceWarnStatus(resource
						.getLocation(),
						"Resource does not exist or is not readable->"
								+ resource.getName(), null);
			}
		}
		
		for (final IResource resource : getIntfProjectWritableResources(project)) {
			if (WorkspaceUtil.isResourceModifiable(resource) == false) {
				return EclipseMessageUtils.createResourceWarnStatus(resource
						.getLocation(),
						"Resource does not exist or is not modifiable->"
								+ resource.getName(), null);
			}
		}

		return Status.OK_STATUS;
	}

	private static List<IResource> getIntfProjectWritableResources(
			IProject project) {
		final List<IResource> resources = new ArrayList<IResource>();
		TypeLibraryUtil.getDependencyFile(project);
		return resources;
	}

	private static List<IResource> getIntfProjectReadableResources(
			IProject project) {
		final List<IResource> resources = new ArrayList<IResource>();
		resources.add(SOAServiceUtil.getWsdlFile(project.getName()));
		return resources;
	}

	/**
	 * Mainly used to validate a type library project. These are the minimum
	 * files that should be readable for the SOA plugin and codegen to work. For
	 * now its just the type dependency file.
	 * 
	 * @param project
	 * @return list of resources that are supposed to exist in a valid type
	 *         library project.
	 * @throws Exception
	 */
	public static List<IResource> getTypeLibProjectReadableResources(
			final IProject project) throws Exception {
		final List<IResource> resources = new ArrayList<IResource>();
		resources.add(TypeLibraryUtil.getDependencyFile(project));
		return resources;
	}

	/**
	 * Mainly used to validate a type library project. These are the minimum
	 * files that should be writable for the SOA plugin and codegen to modify.
	 * The returned list of files could be modified either by codegen or soa
	 * plugin. For now its just the type dependency file.
	 * 
	 * @param project
	 * @return list of resources that are supposed tobe writable in a valid type
	 *         library project.
	 * @throws Exception
	 */
	public static List<IResource> getTypeLibProjectWritableResources(
			final IProject project) throws Exception {
		final List<IResource> resources = new ArrayList<IResource>();
		resources.add(TypeLibraryUtil.getDependencyFile(project));
		return resources;
	}

	/**
	 * Creates a Codegen Invoker and populates its class loader with type
	 * libraries. In addition there is a cryptic process that this function
	 * performs. 1) Find out the output location typically project\bin. 2)
	 * Removes it from the classpath. 3) Add meta src and meta inf folders to
	 * the class path. This is to make sure that the latest XSDs and xml
	 * modified by the user are present in the class path and not the stale old
	 * output xsds and xmls.
	 * 
	 * @param project
	 * @return
	 * @throws Exception
	 */
	public static CodegenInvoker initForTypeLib(IProject project)
			throws Exception {
		CodegenInvoker codegenInvoker = CodegenInvoker.init(project);
		SOAPluginClassLoader soaPluginClassLoader = codegenInvoker
				.getSoaPluginClassLoader();
		ArrayList<IProject> typeLibProjects = WorkspaceUtil
				.getProjectsByNature(TypeLibraryProjectNature.getTypeLibraryNatureId());
		// Output location of each project
		ArrayList<URL> outPutUrls = new ArrayList<URL>();
		ArrayList<URL> metasrcmetainfUrls = new ArrayList<URL>();
		for (IProject typeLibProject : typeLibProjects) {
			outPutUrls.add(typeLibProject.getFolder(
					JavaCore.create(typeLibProject).getOutputLocation()
							.removeFirstSegments(1)).getLocation().toFile()
					.toURI().toURL());
			// we always love to have recent xsds and recent xmls :).
			metasrcmetainfUrls.add(typeLibProject.getFolder(
					SOATypeLibraryConstants.FOLDER_GEN_META_SRC).getLocation()
					.toFile().toURI().toURL());
			metasrcmetainfUrls.add(typeLibProject.getFolder(
					SOATypeLibraryConstants.FOLDER_META_SRC).getLocation()
					.toFile().toURI().toURL());
		}
		Set<URL> classPathList = soaPluginClassLoader.getM_classPathURLs();
		Iterator<URL> iterator = classPathList.iterator();
		while (iterator.hasNext()) {
			URL nextUrl = iterator.next();
			for (URL url : outPutUrls) {
				if (StringUtils.equalsIgnoreCase(nextUrl.toString(), url
						.toString())) {
					// we dont want the ouput location. It contains stale xmls
					// and xsds.
					// bad bad remove them
					iterator.remove();
					break;
				}
			}
		}
		classPathList.addAll(metasrcmetainfUrls);
		soaPluginClassLoader.setM_classPathURLs(classPathList);

		if (SOALogger.DEBUG) {
			logger.debug("Init for Type Lib, Final Urls are: "
					+ soaPluginClassLoader.getM_classPathURLs());
		}
		return codegenInvoker;
	}
}
