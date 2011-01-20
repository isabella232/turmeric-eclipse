/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.utils.plugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.ebayopensource.turmeric.eclipse.exception.core.SOANullParameterException;
import org.ebayopensource.turmeric.eclipse.exception.resources.SOAResourceNotAccessibleException;
import org.ebayopensource.turmeric.eclipse.utils.collections.CollectionUtil;
import org.ebayopensource.turmeric.eclipse.utils.collections.SetUtil;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceDescription;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;



/**
 * @author smathew This util class contains the util methods for workspace,
 *         workbench and resource related calls.
 */
public class WorkspaceUtil {

	public static final String PATH_SEPERATOR = "/";

	public static IWorkspace getWorkspace() {
		return ResourcesPlugin.getWorkspace();
	}

	public static IWorkspaceRoot getWorkspaceRoot() {
		return getWorkspace().getRoot();
	}

	public static boolean projectExistsInWorkSpace(String projectName) {
		IProject project = getWorkspaceRoot().getProject(projectName);
		return project != null && project.exists();
	}

	/**
	 * @param dir
	 * @return True if the directory exists
	 */
	public static boolean directoryExistsInFileSystem(String dir) {
		return new File(dir).exists();
	}

	/**
	 * @param parentFolder
	 * @param file
	 * @return Concatenates the folder and project name and return the complete
	 *         path as a string
	 * 
	 */
	public static String getProjectDirPath(String parentFolder, String file) {
		return parentFolder + File.separator + file;
	}

	public static IProject[] getAllProjectsInWorkSpace() {
		return getWorkspaceRoot().getProjects();
	}

	/**
	 * This function resolves the project location if the project location
	 * logically ie if the project location already has the project name as its
	 * last segment then it will remove it and will return the new location with
	 * project name only once at the end
	 * 
	 * 
	 * @param projectName
	 * @param projectLocation
	 * @return
	 */
	public static IPath resolveProjectPath(final String projectName,
			final IPath projectLocation) {
		if (projectLocation == null)
			return null;
		if (StringUtils.isBlank(projectName))
			return projectLocation;
		final IPath projectRoot = StringUtils.equals(projectLocation
				.lastSegment(), projectName) ? projectLocation
				.removeLastSegments(1) : projectLocation;
		if (!projectRoot.toFile().exists())
			return null;
		return !StringUtils.equals(projectRoot.toString(), "/") ? new Path(
				projectRoot + "/" + projectName) : new Path("/" + projectName);
	}

	/**
	 * Iterated wrapper to resolveProjectPath function
	 * 
	 * @param projectLocation
	 * @param projectNames
	 * @return
	 */
	public static IPath resolveProjectRoot(final IPath projectLocation,
			final String... projectNames) {
		for (final String projectName : projectNames) {
			final IPath location = resolveProjectPath(projectName,
					projectLocation);
			if (location == null)
				return location;
			if (StringUtils.equals(projectName, location.lastSegment()))
				return location.removeLastSegments(1);
		}
		return projectLocation;
	}

	public static IProject createProject(final String projectName,
			final IPath projectLocation, final IProgressMonitor progressMonitor)
			throws CoreException {
		final IProgressMonitor monitor = ProgressUtil
				.getDefaultMonitor(progressMonitor);
		final IProject project = getProject(projectName);
		if (project.exists())
			return openProject(project, monitor);
		final IPath resolvedLocation = resolveProjectPath(projectName,
				projectLocation);
		final IProjectDescription description = getWorkspace()
				.newProjectDescription(projectName);
		if (resolvedLocation != null
				&& projectLocation.equals(getWorkspace().getRoot()
						.getLocation()) == false)
			description.setLocation(resolvedLocation);

		project.create(description, monitor);
		return openProject(project, monitor);
	}

	public static IProject getProject(final String projectName) {
		return getWorkspaceRoot().getProject(projectName);
	}

	public static IProject getProject(final IPath path) {
		final IProject project = getWorkspaceRoot().getProject(
				path.lastSegment());
		return project;
	}

	public static IProject openProject(final IProject project,
			final IProgressMonitor progressMonitor) throws CoreException {
		final IProgressMonitor monitor = progressMonitor != null ? progressMonitor
				: new NullProgressMonitor();
		if (project == null || !project.exists())
			return project;
		if (!project.isOpen())
			project.open(monitor);
		return project;
	}

	/**
	 * @param folder
	 *            names with "/", like src/meta-inf/ The delimiter used is
	 * @WorkSpaceUtil.PATH_SEPERATOR
	 * @param project
	 * @throws CoreException
	 *             This method creates sub folder. Good thing is that it will
	 *             check for existance of parent directories and eliminates the
	 *             chance of folder creation failures to a certain level
	 * 
	 */
	public static void createFolders(IProject project,
			final List<String> folders, final IProgressMonitor monitor)
			throws CoreException {
		if (project.isAccessible() && project.exists()) {
			for (final String folder : folders) {
				// Ignore . Dir. The project is created so Current dir
				// already exists. Meaning . should be ignored
				if (!isDotDirectory(folder)) {
					StringBuilder path = new StringBuilder();
					
					String subFolders[] = StringUtils.split(folder,
							PATH_SEPERATOR);
					for (String subFolder : subFolders) {
						path.append(PATH_SEPERATOR);
						path.append(subFolder);
						// target folder shouldnt exist but the parent folder
						// should. simple
						String spath = path.toString();
						if (!project.getFolder(spath).exists()
								&& project.getFolder(subFolder).getParent() != null
								&& project.getFolder(subFolder).getParent()
										.exists()) {
							project.getFolder(spath).create(true, true, monitor);
						}

						refresh(project.getFolder(spath));
					}
				}
			}
		}

	}

	public static void refresh(final IResource... resources)
			throws CoreException {
		for (final IResource resource : resources)
			refresh(resource, null);
	}

	public static void refresh(final IProgressMonitor monitor,
			final IResource... resources) throws CoreException {
		for (final IResource resource : resources)
			refresh(resource, monitor);
	}

	public static void refresh(final IResource resource,
			final IProgressMonitor progressMonitor) throws CoreException {
		if (resource == null)
			return;
		final IProgressMonitor monitor = ProgressUtil
				.getDefaultMonitor(progressMonitor);
		resource.refreshLocal(IResource.DEPTH_INFINITE, monitor);
	}

	/**
	 * This method assumes the path is relative to the workspace root or is
	 * indeed a true absolute location
	 */
	public static IPath getLocation(final IPath path) {
		if (path == null)
			return null;
		final IResource resource = getWorkspaceRoot().findMember(
				path.makeAbsolute());
		if (resource == null)
			return path.makeAbsolute();
		return resource.getLocation();
	}

	/**
	 * @param contents
	 * @param file
	 * @param progressMonitor
	 * @throws CoreException
	 *             This is a simple write method. If the file doesnt exist it
	 *             will create one,Any issues will result in Core Exception
	 *             being thrown, Clients should handle those
	 */
	public static void writeToFile(final String contents, final IFile file,
			final IProgressMonitor progressMonitor) throws CoreException {
		writeToFile(IOUtils.toInputStream(contents), file, ProgressUtil
				.getDefaultMonitor(progressMonitor));
	}

	/**
	 * @param input
	 * @param file
	 * @param progressMonitor
	 * @throws CoreException
	 *             This is a simple write method. If the file doesnt exist it
	 *             will create one,Any issues will result in Core Exception
	 *             being thrown, Clients should handle those
	 */
	public static void writeToFile(final InputStream input, final IFile file,
			final IProgressMonitor progressMonitor) throws CoreException {
		try {
			if (!file.exists()) {
				file.create(input, true, ProgressUtil
						.getDefaultMonitor(progressMonitor));
			} else {
				file.setContents(input, true, true, ProgressUtil
						.getDefaultMonitor(progressMonitor));
			}
		} finally {
			IOUtils.closeQuietly(input);
		}
	}

	public static Properties loadProperties(IFile file) throws CoreException,
			IOException {
		InputStream input = null;
		try {
			final Properties props = new Properties();

			if (file.exists()) {
				input = file.getContents(true);
				props.load(input);
			}
			return props;
		}

		finally {
			IOUtils.closeQuietly(input);
		}
	}

	/**
	 * for a,b,c output will be a/b/c/
	 * 
	 * @param dirs
	 * @return
	 */
	public static String addPathSeperators(String... dirs) {
		StringBuffer strBuffer = new StringBuffer("");
		for (String str : dirs) {
			strBuffer.append(str);
			strBuffer.append(PATH_SEPERATOR);
		}
		return strBuffer.toString();
	}

	public static void delete(IResource resource, IProgressMonitor monitor)
			throws CoreException {
		if (resource instanceof IFile)
			((IFile) resource).delete(true, true, monitor);
		else if (resource instanceof IFolder)
			((IFolder) resource).delete(true, true, monitor);
		else if (resource instanceof IProject)
			((IProject) resource).delete(false, true, monitor);
		else
			resource.delete(true, monitor);
	}

	/**
	 * Right now used for testing purpose
	 * 
	 * @param bool
	 * @throws CoreException
	 */
	public static void setBuildAutomatically(boolean bool) throws CoreException {
		IWorkspaceDescription description = getWorkspace().getDescription();
		description.setAutoBuilding(bool);
		getWorkspace().setDescription(description);
	}

	/**
	 * Right now used for testing purpose
	 * 
	 * @param string
	 *            projectName
	 * @throws CoreException
	 */
	public static void deleteProject(String projectName) throws CoreException {
		IProject project = getWorkspaceRoot().getProject(projectName);
		project.delete(true, new NullProgressMonitor());
	}

	/**
	 * @param resource
	 *            If the resource doesnt exist thats fin in this case.It will
	 *            return true is the resource doesnt exist
	 */
	public static boolean isResourceWritable(IResource resource) {
		if (resource.exists()) {// resource exists
			if (resource.isAccessible() == false
					|| (resource.getResourceAttributes() != null && resource
							.getResourceAttributes().isReadOnly())) {
				// If its not accessible or if its read only
				return false;
			}
		} else {
			if (resource.getParent() != null) {
				final IResource parent = resource.getParent();
				// Parent can decide on child's Writability
				if (parent.exists()) {
					// No Parent is fine
					if (!parent.isAccessible()
							|| (parent.getResourceAttributes() != null && parent
									.getResourceAttributes().isReadOnly())) {
						// If parent is not accessible or if the parent is just
						// read only
						return false;
					}
				}
			}
		}
		return true;
	}

	/**
	 * @param resource
	 *            In this case resource should exist
	 */
	public static boolean isResourceModifiable(IResource resource) {
		if (resource.exists()) {// resource there
			if (!resource.isAccessible()
					|| resource.getResourceAttributes().isReadOnly()) {
				// Not accessible or if just readable
				return false;
			}
		} else {// Without resource how will we modify :)
			return false;
		}
		return true;
	}

	/**
	 * @param resource
	 *            In this case resource should exist and should be readable
	 */
	public static boolean isResourceReadable(IResource resource) {
		if (resource.exists()) {// resource there
			if (!resource.isAccessible()) {// Not accessible, No good to read
				return false;
			}
		} else {// resource not there, Dont try to make us read a non existing
			// file.
			return false;
		}
		return true;
	}

	/**
	 * @return Only open projects are returned
	 */
	public static IProject[] getOpenProjectsInWorkSpace() {
		IProject projects[] = getWorkspaceRoot().getProjects();
		ArrayList<IProject> openedProjects = new ArrayList<IProject>();
		for (IProject project : projects) {
			if (project.isOpen())
				openedProjects.add(project);

		}
		return openedProjects.toArray(new IProject[0]);
	}

	/**
	 * @param natures
	 * @return
	 * 
	 * Returns the projects with this nature Only open projects are returned
	 * @throws CoreException
	 */
	public static ArrayList<IProject> getProjectsByNature(String... natureIds)
			throws CoreException {
		Set<String> searchNatureSet = SetUtil.hashSet(natureIds);
		IProject projects[] = getWorkspaceRoot().getProjects();
		ArrayList<IProject> resultProjects = new ArrayList<IProject>();
		for (IProject project : projects) {
			if (project.isOpen() && project.isAccessible()) {
				String[] projectNatureIds = project.getDescription()
						.getNatureIds();
				Set<String> projectNatureSet = SetUtil
						.hashSet(projectNatureIds);
				if (!CollectionUtil.intersection(projectNatureSet,
						searchNatureSet).isEmpty()) {
					resultProjects.add(project);
				}
			}

		}
		return resultProjects;
	}

	public static IFile createEmptyFile(IProject project, String fileName,
			IProgressMonitor monitor) throws CoreException {
		IFile file = project.getFile(fileName);
		StringBuilder folder = new StringBuilder();
		for (int i = 0; i < file.getProjectRelativePath().segmentCount() - 1; i++) {
			folder.append(file.getProjectRelativePath().segment(i));
			folder.append(PATH_SEPERATOR);
			String sfolder = folder.toString();
			if (!project.getFolder(sfolder).exists()) {
				project.getFolder(sfolder).create(true, true,
						new NullProgressMonitor());
				project.getFolder(sfolder).refreshLocal(
						IResource.DEPTH_INFINITE, new NullProgressMonitor());
			}
		}
		file.create(IOUtils.toInputStream(" "), true, monitor);
		return file;
	}

	public static List<IFile> getFilesWithExtensions(final IFolder folder,
			final boolean checkExistence, final String fileExt)
			throws CoreException {
		final List<IFile> result = new ArrayList<IFile>();
		if (folder == null || folder.isAccessible() == false
				|| StringUtils.isBlank(fileExt))
			return result;
		for (final IResource resource : folder.members()) {
			if (resource instanceof IFile
					&& fileExt.equalsIgnoreCase(resource.getFileExtension())) {
				if (checkExistence == false
						|| (checkExistence && resource.isAccessible())) {
					result.add((IFile) resource);
				}
			} else if (resource instanceof IFolder && resource.isAccessible()) {
				result.addAll(getFilesWithExtensions((IFolder) resource,
						checkExistence, fileExt));
			}
		}

		return result;
	}

	/**
	 * Deletes the contents of the folder. Checks for existence. Does not
	 * perform a null check. Silently returning for null is not a good idea.
	 * 
	 * 
	 * @param folder
	 * @throws CoreException,
	 *             SOANullParameterException
	 * @throws SOAResourceNotAccessibleException
	 */
	public static void deleteContents(IFolder folder, boolean refresh)
			throws CoreException, CoreException, SOANullParameterException,
			SOAResourceNotAccessibleException {
		if (folder == null)
			throw new SOANullParameterException(0);
		if (refresh)
			WorkspaceUtil.refresh(folder);
		if (!folder.exists())
			throw new SOAResourceNotAccessibleException(folder.getName()
					+ " does not exist", folder);
		for (IResource resource : folder.members(IContainer.INCLUDE_PHANTOMS
				| IContainer.INCLUDE_TEAM_PRIVATE_MEMBERS)) {
			WorkspaceUtil
					.delete(resource, ProgressUtil.getDefaultMonitor(null));
		}
		if (refresh)
			WorkspaceUtil.refresh(folder);
	}

	public static boolean isDotDirectory(String folder) {
		return StringUtils.equals(".", folder)
				|| StringUtils.equals("." + PATH_SEPERATOR, folder);
	}
	
	public static IPath addExtensionIfRequired(IPath path, String extension) {
		if (!StringUtils.equals(path.getFileExtension(), extension)) {
			path.addFileExtension(extension);
		}
		return path;
	}
}
