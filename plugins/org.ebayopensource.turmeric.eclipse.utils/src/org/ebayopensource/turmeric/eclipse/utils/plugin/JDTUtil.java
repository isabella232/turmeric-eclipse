/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.utils.plugin;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.PropertyResourceBundle;
import java.util.Set;
import java.util.logging.Logger;

import org.apache.commons.lang.StringUtils;
import org.ebayopensource.turmeric.eclipse.utils.collections.ListUtil;
import org.ebayopensource.turmeric.eclipse.utils.collections.SetUtil;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaConventions;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.launching.JavaRuntime;
import org.eclipse.jst.common.project.facet.JavaFacetUtils;
import org.eclipse.wst.common.project.facet.core.IProjectFacetVersion;
import org.osgi.framework.Bundle;
import org.osgi.framework.Constants;


/**
 * The Class JDTUtil.
 *
 * @author smathew
 * 
 * Utility for JDT related functions
 */
public class JDTUtil {
	/**
	 * @see org.eclipse.jdt.internal.compiler.impl.CompilerOptions.VERSION_1_4
	 */
	@SuppressWarnings("restriction")
	private static final String COMPILER_OPTIONS_VERSION_1_4 = "1.4";
	
	/** The Constant DEFAULT_COMPILER_VERSION. */
	public static final String DEFAULT_COMPILER_VERSION = "1.6";
	
	/** The Constant DOT. */
	public static final String DOT = ".";

	/**
	 * Pure Wrapper Call.
	 *
	 * @param name the name
	 * @return the i status
	 */
	public static IStatus validateIdentifier(String name) {
		return JavaConventions.validateIdentifier(name, 
				COMPILER_OPTIONS_VERSION_1_4, COMPILER_OPTIONS_VERSION_1_4);

	}

	/**
	 * Pure Wrapper Call to
	 * This validates a type name ie Whatever compiler takes.
	 * eg: a. is not valid.
	 * a is valid
	 *
	 * @param typeName the type name
	 * @return the i status
	 */
	public static IStatus validateJavaTypeName(String typeName) {
		return JavaConventions.validateJavaTypeName(typeName, 
				COMPILER_OPTIONS_VERSION_1_4, COMPILER_OPTIONS_VERSION_1_4);
	}
	
	/**
	 * Validate method name.
	 *
	 * @param methodName the method name
	 * @return True if the method name is valid
	 */
	public static IStatus validateMethodName(String methodName) {
		return JavaConventions.validateMethodName(methodName, 
				COMPILER_OPTIONS_VERSION_1_4, COMPILER_OPTIONS_VERSION_1_4);
	}
	
	/**
	 * Validate pacakge name.
	 *
	 * @param packageName the package name
	 * @return the i status
	 */
	public static IStatus validatePacakgeName(String packageName) {
		return JavaConventions.validatePackageName(packageName, 
				COMPILER_OPTIONS_VERSION_1_4, COMPILER_OPTIONS_VERSION_1_4);
	}

	/**
	 * Adds the java support to eclipse project.
	 * ie soa nature is added here.
	 * Class Path container related linking etc..
	 *
	 * @param project the project
	 * @param sourceDirectories the source directories
	 * @param defaultCompilerLevel the default compiler level
	 * @param outputLocation the output location
	 * @param monitor the monitor
	 * @throws CoreException the core exception
	 */
	public static void addJavaSupport(IProject project,
			List<String> sourceDirectories, String defaultCompilerLevel,
			String outputLocation, IProgressMonitor monitor) throws CoreException {

		boolean changedClasspath = false;
		if (addJavaNature(project, monitor)) {
			changedClasspath = true;
		}
		// Configuring the Java Project
		final IJavaProject javaProject = JavaCore.create(project);
		final List<IClasspathEntry> classpath = JDTUtil.rawClasspath(
				javaProject, true);
		if (outputLocation.equals(javaProject.getOutputLocation().toString()) == false) {
			final IFolder outputDirClasses = project.getFolder(outputLocation);
			javaProject.setOutputLocation(outputDirClasses.getFullPath(), monitor);
			changedClasspath = true;
		}

		// Dealing with the case where the root of the project is set to be the
		// src and bin destinations... bad... bad...
		for (final Iterator<IClasspathEntry> iterator = classpath.iterator(); iterator
				.hasNext();) {
			final IClasspathEntry entry = iterator.next();
			if (entry.getEntryKind() != IClasspathEntry.CPE_SOURCE
					|| !entry.getPath().equals(
							new Path("/" + project.getName())))
				continue;
			iterator.remove();
			changedClasspath |= true;
		}
		for (final String dir : sourceDirectories) {

			final IFolder source = project.getFolder(dir);
			// If the Java project existed previously, checking if directories
			// already exist in
			// its classpath.
			boolean found = false;
			for (final IClasspathEntry entry : classpath) {
				if (!entry.getPath().equals(source.getFullPath()))
					continue;
				found = true;
				break;
			}
			if (found)
				continue;
			changedClasspath |= true;
			classpath.add(JavaCore.newSourceEntry(source.getFullPath()));
		}
		ProgressUtil.progressOneStep(monitor, 10);
		// Adding the runtime library
		boolean found = false;
		for (final IClasspathEntry entry : classpath) {
			// All JRE Containers should have a prefix of
			// org.eclipse.jdt.launching.JRE_CONTAINER
			if (JavaRuntime.newDefaultJREContainerPath().isPrefixOf(
					entry.getPath())
					&& JavaRuntime.newDefaultJREContainerPath().equals(
							entry.getPath())) {
				found = true;
				break;
			}
		}
		if (!found) {
			changedClasspath = true;
			classpath.add(JavaRuntime.getDefaultJREContainerEntry());
		}
		ProgressUtil.progressOneStep(monitor);
		// Configuring the classpath of the Java Project
		if (changedClasspath) {
			javaProject.setRawClasspath(classpath
					.toArray(new IClasspathEntry[0]), null);
		}
		
		IProjectFacetVersion projectFacetVersion = JavaFacetUtils.JAVA_60;
		if (StringUtils.isNotBlank(defaultCompilerLevel)) {
			try {
				projectFacetVersion = JavaFacetUtils.compilerLevelToFacet(defaultCompilerLevel);
			} catch (Exception e) {
				Logger.getLogger(JDTUtil.class.getName()).throwing(JDTUtil.class.getName(), 
						"addJavaSupport", e);
			}
		}
		
		JavaFacetUtils.setCompilerLevel(project, projectFacetVersion);
	}
	
	/**
	 * Resolve classpath to ur ls.
	 *
	 * @param bundle The plugin bundle to load jars
	 * @param project The underlying SOA project
	 * @return All jars in the runtime classpath of the provided bundle.
	 * @throws Exception the exception
	 */
	public static Set<URL> resolveClasspathToURLs(final Bundle bundle, final IProject project) 
	throws Exception{
		final Map<String, URL> result = new LinkedHashMap<String, URL>();
//		Object obj = bundle.getHeaders().get(Constants.BUNDLE_CLASSPATH);
//		for (final String path : StringUtils.split(String.valueOf(obj), ",")) {
//			if (path.endsWith(".jar")) {
//				URL url = bundle.getEntry(path);
//				url = FileLocator.resolve(url);
//				result.put(new Path(url.getFile()).lastSegment(), url);
//			}
//		}
		
		//load missing jars from the project's classpath
		for (final URL url : resolveClasspathToURLs(project)) {
			final Path path = new Path(url.getFile());
			final String fileName = path.lastSegment();
			
			if (StringUtils.equalsIgnoreCase(path.getFileExtension(), "jar")) {
				if (result.containsKey(fileName) == false)
					result.put(fileName, url);
			} else if (result.containsKey(url.getFile()) == false) {
				result.put(url.getFile(), url);
			}
		}

		return SetUtil.linkedSet(result.values());
	}

	/**
	 * Resolves the projects class path container entries
	 * Explodes the container out and return a Set of URL Path.
	 *
	 * @param project the project
	 * @return the sets the
	 * @throws Exception the exception
	 */
	public static Set<URL> resolveClasspathToURLs(final IProject project)
			throws Exception {
		return resolveClasspathToURLs(JavaCore.create(project));
	}

	private static Set<URL> resolveClasspathToURLs(final IJavaProject javaProject)
			throws JavaModelException, IOException {
		final Set<URL> classpath = SetUtil.linkedSet();
		resolveClasspathToURLs(javaProject, classpath, SetUtil
				.set(new String[0]));
		return classpath;
	}

	private static void resolveClasspathToURLs(final IJavaProject javaProject,
			final Set<URL> resolvedEntries, final Set<String> visited)
			throws JavaModelException, IOException {
		if (javaProject == null || !javaProject.exists())
			return;
		final String projectName = javaProject.getProject().getName();
		if (visited.contains(projectName))
			return;
		visited.add(projectName);
		for (final IClasspathEntry entry : javaProject
				.getResolvedClasspath(true)) {
			if (entry.getEntryKind() == IClasspathEntry.CPE_PROJECT) {
				resolveClasspathToURLs(JavaCore.create(WorkspaceUtil
						.getProject(entry.getPath())), resolvedEntries, visited);
			}
			else if (entry.getEntryKind() == IClasspathEntry.CPE_LIBRARY) {
				resolvedEntries.add(WorkspaceUtil.getLocation(
						entry.getPath()).toFile().toURI().toURL());

			}
			else if (entry.getEntryKind() == IClasspathEntry.CPE_SOURCE) {
				IPath location = entry.getOutputLocation() != null ? entry.getOutputLocation()
						: javaProject.getOutputLocation();
				if (location.toString().startsWith(WorkspaceUtil.PATH_SEPERATOR + projectName)) {
					//it happens that the path is not absolute
					location = javaProject.getProject().getFolder(location.removeFirstSegments(1)).getLocation();
				}
				
				resolvedEntries.add(location.toFile().toURI().toURL());
			}
		}
	}
	
	/**
	 * Adds the natures.
	 *
	 * @param project the project
	 * @param monitor the monitor
	 * @param projectNatures the project natures
	 * @return true, if successful
	 * @throws CoreException the core exception
	 */
	public static boolean addNatures(final IProject project, 
			final IProgressMonitor monitor, String... projectNatures)
			throws CoreException {
		if (project == null || projectNatures == null || projectNatures.length == 0)
			return false;
		List<String> additionalNatureIds = new ArrayList<String>(projectNatures.length);
		for (String natureId : projectNatures) {
			if (project.hasNature(natureId) == false) {
				additionalNatureIds.add(natureId);
			}
		}
		
		if (additionalNatureIds.isEmpty() == false) {
			// Adding the natures to the project.
			final IProjectDescription description = project.getDescription();
			final List<String> natureIDs = ListUtil.array(description
					.getNatureIds());
			natureIDs.addAll(additionalNatureIds);
			description.setNatureIds(natureIDs.toArray(new String[0]));
			project.setDescription(description, monitor);
			return true;
		}
		return false;
	}

	/**
	 * Adds the java nature.
	 *
	 * @param project the project
	 * @param monitor the monitor
	 * @return true, if successful
	 * @throws CoreException the core exception
	 */
	public static boolean addJavaNature(final IProject project, 
			final IProgressMonitor monitor)
			throws CoreException {
		return addNatures(project, monitor, JavaCore.NATURE_ID);
	}

	/**
	 * Raw classpath.
	 *
	 * @param project the project
	 * @param readFromDisk the read from disk
	 * @return the list
	 * @throws JavaModelException the java model exception
	 */
	public static List<IClasspathEntry> rawClasspath(final IProject project,
			final boolean readFromDisk) throws JavaModelException {
		return rawClasspath(JavaCore.create(project), readFromDisk);
	}

	/**
	 * Raw classpath.
	 *
	 * @param project the project
	 * @param readFromDisk the read from disk
	 * @return the list
	 * @throws JavaModelException the java model exception
	 */
	public static List<IClasspathEntry> rawClasspath(
			final IJavaProject project, final boolean readFromDisk)
			throws JavaModelException {
		final List<IClasspathEntry> entries = ListUtil.list();
		if (project == null || !project.getProject().isAccessible())
			return entries;
		if (!readFromDisk
				&& project.getProject().getFile(".classpath").isAccessible()
				&& project.getProject().getFile(".classpath").isSynchronized(
						IResource.DEPTH_INFINITE))
			ListUtil.add(entries, project.getRawClasspath());
		else
			ListUtil.add(entries, project.readRawClasspath());
		return entries;
	}
	
	/**
	 * Checks if is classpath container.
	 *
	 * @param entry the entry
	 * @param path the path
	 * @return true, if is classpath container
	 */
	public static boolean isClasspathContainer(final IClasspathEntry entry, 
			final String path) {
		return entry.getEntryKind() == IClasspathEntry.CPE_CONTAINER
		&& (path.equals(entry.getPath().segment(0)));
	}
	
	/**
	 * Checks if is jRE classpath container.
	 *
	 * @param entry the entry
	 * @return true, if is jRE classpath container
	 */
	public static boolean isJREClasspathContainer(final IClasspathEntry entry) {
		return isClasspathContainer(entry, JavaRuntime.JRE_CONTAINER);
	}
	
	/**
	 * Gets the bundle info.
	 *
	 * @param bundle the bundle
	 * @param needDetail the need detail
	 * @return the bundle info
	 */
	public static String getBundleInfo(final Bundle bundle, final boolean needDetail) {
		final StringBuffer buf = new StringBuffer();
        if (bundle != null)
        {
        	buf.append(bundle.getSymbolicName());
        	Object versionID = bundle.getHeaders().get(Constants.BUNDLE_VERSION);
        	if (versionID != null)
        	{
        		buf.append(" ");
        		buf.append(versionID.toString());
        	}
        	if (needDetail)
        	{
        		buf.append("\nManifest Headers:");
        		for (Enumeration<?> keys = bundle.getHeaders().keys(); keys.hasMoreElements() ;)
        		{
        			Object key = keys.nextElement();
        			buf.append(key);
        			buf.append(" = ");
        			buf.append(bundle.getHeaders().get(key));
        			buf.append("\n");
        		}
        	}
        }
        return buf.toString();
	}

	/**
	 * Generate qualified class name using path seperator.
	 *
	 * @param classNameForPkg the class name for pkg
	 * @param pkgPrefix the pkg prefix
	 * @param genClassName the gen class name
	 * @return the string
	 */
	public static String generateQualifiedClassNameUsingPathSeperator(
			String classNameForPkg, 
			String pkgPrefix, 
			String genClassName) {

		String genPkgName = null;

		int lastDotPos = classNameForPkg.lastIndexOf(DOT);		
		if (lastDotPos > -1) {
			genPkgName = classNameForPkg.substring(0, lastDotPos) + WorkspaceUtil.PATH_SEPERATOR + pkgPrefix;
		} else {
			genPkgName = pkgPrefix;
		}
		genPkgName = StringUtils.replace(genPkgName, DOT, WorkspaceUtil.PATH_SEPERATOR);

		return genPkgName + WorkspaceUtil.PATH_SEPERATOR + genClassName;
	}

	/**
	 * Gets the plugin properties.
	 *
	 * @param bundle the bundle
	 * @param fileName the file name
	 * @return the plugin properties
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static PropertyResourceBundle getPluginProperties(
			final Bundle bundle, String fileName)
	throws IOException{
		PropertyResourceBundle pluginProperties;

		if (StringUtils.isBlank(fileName)) {
			final Object object = bundle.getHeaders().get(Constants.BUNDLE_LOCALIZATION);
			fileName = object != null ? object.toString() + ".properties" : "plugin.properties";
		}

		pluginProperties = new PropertyResourceBundle(
				FileLocator.openStream(bundle,
						new Path(fileName),false));

		return pluginProperties;
	}


    /**
     * Gets the plugin properties.
     *
     * @param bundle the bundle
     * @return the plugin properties
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public static PropertyResourceBundle getPluginProperties(
    		final Bundle bundle)
    throws IOException{
    	return getPluginProperties(bundle, null);
    }
    
    /**
     * Gets the source directories.
     *
     * @param project the project
     * @return the source directories
     */
    public static List<IPath> getSourceDirectories(final IProject project) {
    	final IJavaProject jProject = JavaCore.create(project);
		final List<IPath> srcEntries = new ArrayList<IPath>();
		for (final IClasspathEntry entry : jProject.readRawClasspath()) {
			if (entry.getEntryKind() == IClasspathEntry.CPE_SOURCE) {
				srcEntries.add(entry.getPath());
			}
		}
		return srcEntries;
    }
    
    /**
     * Convert class name to file path.
     *
     * @param className the class name
     * @return the i path
     */
    public static IPath convertClassNameToFilePath(String className) {
    	if (className == null)
    		return null;
    	final IPath path = new Path(StringUtils.replaceChars(className, ".", WorkspaceUtil.PATH_SEPERATOR));
    	return  path.addFileExtension("java");
    }

}
