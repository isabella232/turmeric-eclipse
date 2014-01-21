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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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
 * The Class WorkspaceUtil.
 *
 * @author smathew This util class contains the util methods for workspace,
 * workbench and resource related calls.
 */
public class WorkspaceUtil {

	/** The Constant PATH_SEPERATOR. */
	public static final String PATH_SEPERATOR = "/";

	/**
	 * Gets the workspace.
	 *
	 * @return the workspace
	 */
	public static IWorkspace getWorkspace() {
		return ResourcesPlugin.getWorkspace();
	}

	/**
	 * Gets the workspace root.
	 *
	 * @return the workspace root
	 */
	public static IWorkspaceRoot getWorkspaceRoot() {
		return getWorkspace().getRoot();
	}

	/**
	 * Project exists in work space.
	 *
	 * @param projectName the project name
	 * @return true, if successful
	 */
	public static boolean projectExistsInWorkSpace(String projectName) {
		IProject project = getWorkspaceRoot().getProject(projectName);
		return project != null && project.exists();
	}

	/**
	 * Directory exists in file system.
	 *
	 * @param dir the dir
	 * @return True if the directory exists
	 */
	public static boolean directoryExistsInFileSystem(String dir) {
		return new File(dir).exists();
	}

	/**
	 * Gets the project dir path.
	 *
	 * @param parentFolder the parent folder
	 * @param file the file
	 * @return Concatenates the folder and project name and return the complete
	 * path as a string
	 */
	public static String getProjectDirPath(String parentFolder, String file) {
		return parentFolder + File.separator + file;
	}

	/**
	 * Gets the all projects in work space.
	 *
	 * @return the all projects in work space
	 */
	public static IProject[] getAllProjectsInWorkSpace() {
		return getWorkspaceRoot().getProjects();
	}

	
	public static String getRaptorSoaPropertiesLocation() {
		String userHomeDirectory = System.getProperty("user.home");
		//Basic version override for vanilla eclipse
		
		if((System.getProperty("ide.version")==null)||(System.getProperty("ide.version").equals(""))){
			System.setProperty("ide.version","5.0.0");
		}
		String propertiesFileLocation= userHomeDirectory+File.separator+System.getProperty("ide.version").replace(".", "_");
		return propertiesFileLocation;
	}
	
	
	public static String getRPVersion(){
		String propertiesFileLocation= WorkspaceUtil.getRaptorSoaPropertiesLocation();
		Properties properties = new Properties();
		File RaptorPlatformFile = new File(propertiesFileLocation,"raptorSoa.properties");
		if(RaptorPlatformFile.exists()){
			FileInputStream in=null;
			try {
				in = new FileInputStream(RaptorPlatformFile);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			try {if(in!=null)
				properties.load(in);
			} catch (IOException e) {
				e.printStackTrace();
			}finally{
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return properties.getProperty("RaptorPlatform");
	}

	/**
	 * This function resolves the project location if the project location
	 * logically ie if the project location already has the project name as its
	 * last segment then it will remove it and will return the new location with
	 * project name only once at the end.
	 *
	 * @param projectName the project name
	 * @param projectLocation the project location
	 * @return the i path
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
	 * Iterated wrapper to resolveProjectPath function.
	 *
	 * @param projectLocation the project location
	 * @param projectNames the project names
	 * @return the i path
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

	/**
	 * Creates the project.
	 *
	 * @param projectName the project name
	 * @param projectLocation the project location
	 * @param progressMonitor the progress monitor
	 * @return the i project
	 * @throws CoreException the core exception
	 */
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

	/**
	 * Gets the project.
	 *
	 * @param projectName the project name
	 * @return the project
	 */
	public static IProject getProject(final String projectName) {
		return getWorkspaceRoot().getProject(projectName);
	}

	/**
	 * Gets the project.
	 *
	 * @param path the path
	 * @return the project
	 */
	public static IProject getProject(final IPath path) {
		final IProject project = getWorkspaceRoot().getProject(
				path.lastSegment());
		return project;
	}

	/**
	 * Open project.
	 *
	 * @param project the project
	 * @param progressMonitor the progress monitor
	 * @return the i project
	 * @throws CoreException the core exception
	 */
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
	 * Creates the folders.
	 *
	 * @param project the project
	 * @param folders the folders
	 * @param monitor the monitor
	 * @throws CoreException This method creates sub folder. Good thing is that it will
	 * check for existance of parent directories and eliminates the
	 * chance of folder creation failures to a certain level
	 * @WorkSpaceUtil.PATH_SEPERATOR
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

	/**
	 * Refresh.
	 *
	 * @param resources the resources
	 * @throws CoreException the core exception
	 */
	public static void refresh(final IResource... resources)
			throws CoreException {
		for (final IResource resource : resources)
			refresh(resource, null);
	}

	/**
	 * Refresh.
	 *
	 * @param monitor the monitor
	 * @param resources the resources
	 * @throws CoreException the core exception
	 */
	public static void refresh(final IProgressMonitor monitor,
			final IResource... resources) throws CoreException {
		for (final IResource resource : resources)
			refresh(resource, monitor);
	}

	/**
	 * Refresh.
	 *
	 * @param resource the resource
	 * @param progressMonitor the progress monitor
	 * @throws CoreException the core exception
	 */
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
	 * indeed a true absolute location.
	 *
	 * @param path the path
	 * @return the location
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
	 * Write to file.
	 *
	 * @param contents the contents
	 * @param file the file
	 * @param progressMonitor the progress monitor
	 * @throws CoreException This is a simple write method. If the file doesnt exist it
	 * will create one,Any issues will result in Core Exception
	 * being thrown, Clients should handle those
	 */
	public static void writeToFile(final String contents, final IFile file,
			final IProgressMonitor progressMonitor) throws CoreException {
		writeToFile(IOUtils.toInputStream(contents), file, ProgressUtil
				.getDefaultMonitor(progressMonitor));
	}

	/**
	 * Write to file.
	 *
	 * @param input the input
	 * @param file the file
	 * @param progressMonitor the progress monitor
	 * @throws CoreException This is a simple write method. If the file doesnt exist it
	 * will create one,Any issues will result in Core Exception
	 * being thrown, Clients should handle those
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

	/**
	 * Load properties.
	 *
	 * @param file the file
	 * @return the properties
	 * @throws CoreException the core exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
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
	 * for a,b,c output will be a/b/c/.
	 *
	 * @param dirs the dirs
	 * @return the string
	 */
	public static String addPathSeperators(String... dirs) {
		StringBuffer strBuffer = new StringBuffer("");
		for (String str : dirs) {
			strBuffer.append(str);
			strBuffer.append(PATH_SEPERATOR);
		}
		return strBuffer.toString();
	}

	/**
	 * Delete.
	 *
	 * @param resource the resource
	 * @param monitor the monitor
	 * @throws CoreException the core exception
	 */
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
	 * Right now used for testing purpose.
	 *
	 * @param bool the new builds the automatically
	 * @throws CoreException the core exception
	 */
	public static void setBuildAutomatically(boolean bool) throws CoreException {
		IWorkspaceDescription description = getWorkspace().getDescription();
		description.setAutoBuilding(bool);
		getWorkspace().setDescription(description);
	}
	
	public static boolean isBuildAutomatically() throws CoreException {
		IWorkspaceDescription description = getWorkspace().getDescription();
		return description.isAutoBuilding();
	}

	/**
	 * Right now used for testing purpose.
	 *
	 * @param projectName the project name
	 * @throws CoreException the core exception
	 */
	public static void deleteProject(String projectName) throws CoreException {
		IProject project = getWorkspaceRoot().getProject(projectName);
		project.delete(true, new NullProgressMonitor());
	}

	/**
	 * Checks if is resource writable.
	 *
	 * @param resource If the resource doesnt exist thats fin in this case.It will
	 * return true is the resource doesnt exist
	 * @return true, if is resource writable
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
	 * Checks if is resource modifiable.
	 *
	 * @param resource In this case resource should exist
	 * @return true, if is resource modifiable
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
	 * Checks if is resource readable.
	 *
	 * @param resource In this case resource should exist and should be readable
	 * @return true, if is resource readable
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
	 * Gets the open projects in work space.
	 *
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
	 * Gets the projects by nature.
	 *
	 * @param natureIds the nature ids
	 * @return the projects by nature
	 * @throws CoreException the core exception
	 * Returns the projects with this nature Only open projects are returned
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

	/**
	 * Creates the empty file.
	 *
	 * @param project the project
	 * @param fileName the file name
	 * @param monitor the monitor
	 * @return the i file
	 * @throws CoreException the core exception
	 */
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
	
	
	/**
	 * Gets the files with extensions.
	 *
	 * @param folder the folder
	 * @param checkExistence the check existence
	 * @param fileExt the file ext
	 * @return the files with extensions
	 * @throws CoreException the core exception
	 */
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
	 * @param folder the folder
	 * @param refresh the refresh
	 * @throws CoreException the core exception
	 * @throws SOANullParameterException the sOA null parameter exception
	 * @throws SOAResourceNotAccessibleException the sOA resource not accessible exception
	 */
	public static void deleteContents(IFolder folder, boolean refresh)
			throws CoreException, SOANullParameterException,
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

	/**
	 * Checks if is dot directory.
	 *
	 * @param folder the folder
	 * @return true, if is dot directory
	 */
	public static boolean isDotDirectory(String folder) {
		return StringUtils.equals(".", folder)
				|| StringUtils.equals("." + PATH_SEPERATOR, folder);
	}
	
	/**
	 * Adds the extension if required.
	 *
	 * @param path the path
	 * @param extension the extension
	 * @return the i path
	 */
	public static IPath addExtensionIfRequired(IPath path, String extension) {
		if (!StringUtils.equals(path.getFileExtension(), extension)) {
			path.addFileExtension(extension);
		}
		return path;
	}
}
