/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.buildsystem.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.wsdl.Definition;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.ebayopensource.turmeric.eclipse.buildsystem.resources.SOAMessages;
import org.ebayopensource.turmeric.eclipse.core.logging.SOALogger;
import org.ebayopensource.turmeric.eclipse.core.resources.constants.SOAProjectConstants;
import org.ebayopensource.turmeric.eclipse.exception.core.SOANullParameterException;
import org.ebayopensource.turmeric.eclipse.exception.resources.SOAResourceNotAccessibleException;
import org.ebayopensource.turmeric.eclipse.repositorysystem.model.BaseCodeGenModel;
import org.ebayopensource.turmeric.eclipse.repositorysystem.utils.TurmericServiceUtils;
import org.ebayopensource.turmeric.eclipse.resources.model.ISOAConsumerProject.SOAClientConfig;
import org.ebayopensource.turmeric.eclipse.resources.model.SOAImplMetadata;
import org.ebayopensource.turmeric.eclipse.resources.util.SOAConsumerUtil;
import org.ebayopensource.turmeric.eclipse.resources.util.SOAImplUtil;
import org.ebayopensource.turmeric.eclipse.resources.util.SOAServiceUtil;
import org.ebayopensource.turmeric.eclipse.utils.lang.StringUtil;
import org.ebayopensource.turmeric.eclipse.utils.plugin.JDTUtil;
import org.ebayopensource.turmeric.eclipse.utils.plugin.ProgressUtil;
import org.ebayopensource.turmeric.eclipse.utils.plugin.WorkspaceUtil;
import org.ebayopensource.turmeric.eclipse.utils.wsdl.WSDLUtil;
import org.eclipse.core.resources.ICommand;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;


/**
 * The standard Utility class for the builder. Most builders will need the code
 * generation models and engine in their functions. This utility mainly caters
 * these two objects so that the builder has a commonality in the way in which
 * they invoke the code generation engine.
 * 
 * @author smathew
 * 
 */
public class BuilderUtil {

	/**
	 * Foundation Code generation Model for builders. This is the base code
	 * generation model which will be specialized before used in most cases.
	 * Fills the metadata file(properties file) information into this model. All
	 * the common data that any model required will be filled in at this phase.
	 * But The gen type that is the most crucial factor is not decided here. It
	 * is decided in the specialization phase. Its also called heavily from
	 * context menu actions which includes codegen.
	 *
	 * @param project the project
	 * @param monitor the monitor
	 * @return the base code gen model
	 * @throws Exception the exception
	 */
	public static BaseCodeGenModel buildBaseCodeGenModel(final IProject project, 
			final IProgressMonitor monitor)
			throws Exception {
		return ModelTransformer.generateCodeGenModel(project, monitor);
	}

	/**
	 * Returns trues if the WSDL file is changed. This is not a straight forward
	 * API. It does not care about the changes in the WSDL file if it is not an
	 * interface projects. So please don't use it as a generic API.
	 *
	 * @param delta the delta
	 * @param project the project
	 * @return true, if is wSDL file changed
	 * @throws CoreException the core exception
	 */
	public static boolean isWSDLFileChanged(IResourceDelta delta,
			IProject project) throws CoreException {
		if (TurmericServiceUtils.isSOAInterfaceProject(project) == false)
			return false;
		final IFile wsdlFile = SOAServiceUtil.getWsdlFile(project, project
				.getName());
		return isWSDLFileChanged(delta, wsdlFile.getFullPath());
	}

	/**
	 * Parses the delta and returns true if the WSDL file is changed. Recursive
	 * function but does not use the visitor pattern.
	 * 
	 * @param delta
	 * @param wsdlPath
	 * @return
	 */
	private static boolean isWSDLFileChanged(IResourceDelta delta,
			IPath wsdlPath) {
		if (delta == null)
			return false;
		for (IResourceDelta childDelta : delta
				.getAffectedChildren(IResourceDelta.CHANGED)) {
			if (childDelta.getFullPath().equals(wsdlPath))
				return true;
			else if (isWSDLFileChanged(childDelta, wsdlPath)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Returns true if the WSDL target name space has been changed in an
	 * interface project. To be precise, it does NOT compare the WSDl's old name
	 * space and the new name space, but compares the WSDL's current namespace
	 * to the namespace in the implementation projects metadata file.
	 *
	 * @param delta the delta
	 * @param serviceName the service name
	 * @param intfProject the intf project
	 * @param implProject the impl project
	 * @return null means no change or the new targetNamespace otherwise.
	 * @throws Exception the exception
	 */
	public static String isWSDLTargetNamespaceChanged(IResourceDelta delta,
			String serviceName, IProject intfProject, IProject implProject)
			throws Exception {
		final IFile wsdlFile = SOAServiceUtil.getWsdlFile(intfProject,
				serviceName);
		if (isWSDLFileChanged(delta, wsdlFile.getFullPath()) == false)
			return null;
		final Definition wsdl = WSDLUtil.readWSDL(wsdlFile.getLocation()
				.toString());
		final SOAImplMetadata metadata = SOAImplUtil.loadServiceConfig(
				implProject, serviceName);
		if (!StringUtils.equals(wsdl.getTargetNamespace(), metadata
				.getTargetNamespace())) {
			return wsdl.getTargetNamespace();
		}
		return null;
	}

	/**
	 * Returns true if the WSDL target name space has been changed in an
	 * interface project. To be precise, it does NOT compare the WSDl's old name
	 * space and the new name space, but compares the WSDL's current namespace
	 * to the namespace in the client config file of the consumer.
	 *
	 * @param serviceName the service name
	 * @param intfProject the intf project
	 * @param consumerProject the consumer project
	 * @return null means no change or the new targetNamespace otherwise.
	 * @throws Exception the exception
	 */
	public static String isWSDLTargetNamespaceChangedInConsumerProject(
			String serviceName, IProject intfProject, IProject consumerProject)
			throws Exception {
		final IFile wsdlFile = SOAServiceUtil.getWsdlFile(intfProject,
				serviceName);
		if (wsdlFile.isAccessible() == false)
			return null;
		final Definition wsdl = WSDLUtil.readWSDL(wsdlFile.getLocation()
				.toString());
		String envName = null; 
		if (SOAConsumerUtil.isOldClientConfigDirStructure(consumerProject) == false) {
			List<String> envs = SOAConsumerUtil.getClientEnvironmentList(consumerProject, null);
			if (envs.isEmpty() == false)
				envName = envs.get(0);
		}
		
		final SOAClientConfig clientConfig = SOAConsumerUtil.loadClientConfig(
				consumerProject, envName, serviceName);
		if (clientConfig == null)
			return null;
		if (!StringUtils.equals(wsdl.getTargetNamespace(), clientConfig
				.getTargetNamespace())) {
			return wsdl.getTargetNamespace();
		}
		return null;
	}

	/**
	 * This algorithm is the decision maker for all SOA builders. If this is
	 * true builder gets kicked off and return silently otherwise. Remember
	 * there are two builders most of the time and its very imp to silently
	 * return at some point of time to avoid cyclic builds.
	 *
	 * @param delta the delta
	 * @param project the project
	 * @param criteriaString -
	 * this is mostly the extensions that we are interested in
	 * rebuilding. something like WSDT EXT, XSD EXT etc
	 * @return true, if successful
	 * @throws Exception the exception
	 */
	public static boolean shouldBuild(IResourceDelta delta, IProject project,
			String... criteriaString) throws Exception {
		// first time the delta is null
		if (delta == null)
			return true;
		BuildResourceDeltaVisitor buildDeltaVisitor = new BuildResourceDeltaVisitor(
				project, criteriaString);
		delta.accept(buildDeltaVisitor);
		return buildDeltaVisitor.isBuildRequired();
	}

	/**
	 * This is required because V3 appears to mess with the build order. This
	 * will stay here until V3 comes up with a bug fix.
	 *
	 * @param project the project
	 * @throws CoreException the core exception
	 */
	public static void reOrderBuildersIfRequired(IProject project)
			throws CoreException {
		ICommand commands[] = project.getDescription().getBuildSpec();
		List<ICommand> oldList = Arrays.asList(commands);
		List<ICommand> newList = new ArrayList<ICommand>();
		for (ICommand command : commands) {
			newList.add(command);
		}

		// Scanning if the order has changed
		for (int i = 0; i < newList.size(); i++) {
			// true means order changed
			if (!StringUtils.equals(oldList.get(i).getBuilderName(), newList
					.get(i).getBuilderName())) {
				IProjectDescription description = project.getDescription();
				description.setBuildSpec(newList.toArray(new ICommand[0]));
				project.setDescription(description, IResource.FORCE,
						ProgressUtil.getDefaultMonitor(null));
			}
		}
	}

	/**
	 * Cleans the bin/META-INF folder and do a refresh.
	 *
	 * @param project the project
	 * @param monitor the monitor
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws CoreException the core exception
	 */
	public static void cleanBinMetaInfDir(final IProject project,
			IProgressMonitor monitor) throws IOException, CoreException {
		final IFolder metaFolder = project
				.getFolder(SOAProjectConstants.FOLDER_OUTPUT_DIR
						+ WorkspaceUtil.PATH_SEPERATOR
						+ SOAProjectConstants.FOLDER_META_INF);
		metaFolder.getParent().refreshLocal(IResource.DEPTH_ONE, monitor);
		if (metaFolder.isAccessible()) {
			SOALogger.getLogger().warning(
					StringUtil.formatString(SOAMessages.CLEAN_FOLDER, metaFolder
							.getLocation()));
			FileUtils.cleanDirectory(metaFolder.getLocation().toFile());
		}
	}

	/**
	 * Returns the required project for any given project. Scans the build bath
	 * and finds all the project references and returns it back.
	 *
	 * @param project the project
	 * @param natureIds the nature ids
	 * @return the required projects
	 */
	public static IProject[] getRequiredProjects(IProject project,
			String... natureIds) {
		IJavaProject javaProject = JavaCore.create(project);
		if (javaProject == null)
			return new IProject[0];
		ArrayList<IProject> projects = new ArrayList<IProject>();
		try {
			for (final IClasspathEntry entry : javaProject
					.getResolvedClasspath(true)) {
				if (entry.getEntryKind() == IClasspathEntry.CPE_PROJECT) {
					IProject reqProject = WorkspaceUtil.getProject(entry
							.getPath());
					if (reqProject != null && !projects.contains(reqProject)) {
						if (natureIds != null) {
							for (String natureId : natureIds) {
								if (reqProject.hasNature(natureId)) {
									projects.add(reqProject);
									break;
								}
							}
						} else {
							projects.add(reqProject);
						}
					}
				}
			}
		} catch (Exception e) {
			return new IProject[0];
		}
		IProject[] result = new IProject[projects.size()];
		projects.toArray(result);
		return result;
	}

	/**
	 * Creates missing source folders as per the projects java model. If it
	 * exists, it is a no operation.
	 *
	 * @param project the project
	 * @param monitor the monitor
	 * @throws CoreException the core exception
	 */
	public static void generateSourceDirectories(final IProject project,
			final IProgressMonitor monitor) throws CoreException {
		if (project == null || project.isAccessible() == false)
			return;
		final IPath projectPath = project.getFullPath();
		final List<String> missingFolders = new ArrayList<String>();
		for (IPath srcPath : JDTUtil.getSourceDirectories(project)) {
			if (projectPath.isPrefixOf(srcPath)) {
				srcPath = srcPath.removeFirstSegments(1);
			}
			final IFolder srcFolder = project.getFolder(srcPath);
			if (!srcFolder.exists()) {
				srcFolder.refreshLocal(IResource.DEPTH_ZERO, monitor);
				if (srcFolder.exists() == false) {
					missingFolders.add(srcPath.toString());
				}
			}
		}
		if (missingFolders.isEmpty() == false) {
			WorkspaceUtil.createFolders(project, missingFolders, monitor);
		}
	}

	/**
	 * Deletes the gen folder content of interface projects(for now). The types
	 * are generated in the folder and we don't want stale java types to live
	 * there. Washing it out in this call.
	 *
	 * @param project the project
	 * @throws CoreException the core exception
	 * @throws SOANullParameterException the sOA null parameter exception
	 * @throws SOAResourceNotAccessibleException the sOA resource not accessible exception
	 */
	public static void cleanGenFolders(IProject project) throws CoreException,
			SOANullParameterException, SOAResourceNotAccessibleException {
		// Intf Project
		if (TurmericServiceUtils.isSOAInterfaceProject(project)) {
			IFolder clientFolder = project
					.getFolder(SOAProjectConstants.FOLDER_GEN_SRC_CLIENT);
			WorkspaceUtil.deleteContents(clientFolder, true);

		}
	}
}
