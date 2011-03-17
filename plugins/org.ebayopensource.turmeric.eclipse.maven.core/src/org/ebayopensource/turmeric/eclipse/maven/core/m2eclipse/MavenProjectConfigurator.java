/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.maven.core.m2eclipse;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.maven.model.Build;
import org.apache.maven.model.Model;
import org.apache.maven.model.Plugin;
import org.apache.maven.model.PluginExecution;
import org.ebayopensource.turmeric.eclipse.maven.core.utils.MavenCoreUtils;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.GlobalRepositorySystem;
import org.ebayopensource.turmeric.eclipse.repositorysystem.utils.TurmericServiceUtils;
import org.ebayopensource.turmeric.eclipse.resources.constants.SOAProjectConstants;
import org.ebayopensource.turmeric.eclipse.resources.constants.SOAProjectConstants.SupportedProjectType;
import org.ebayopensource.turmeric.eclipse.utils.collections.ListUtil;
import org.ebayopensource.turmeric.eclipse.utils.plugin.EclipseMessageUtils;
import org.ebayopensource.turmeric.eclipse.utils.plugin.JDTUtil;
import org.ebayopensource.turmeric.eclipse.utils.plugin.WorkspaceUtil;
import org.ebayopensource.turmeric.eclipse.utils.xml.JDOMUtil;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.jdom.Document;
import org.jdom.Element;
import org.maven.ide.eclipse.project.configurator.AbstractProjectConfigurator;
import org.maven.ide.eclipse.project.configurator.ProjectConfigurationRequest;


/**
 * @author yayu
 *
 */
public class MavenProjectConfigurator extends AbstractProjectConfigurator {

	/**
	 * 
	 */
	public MavenProjectConfigurator() {
		super();
	}

	@Override
	public void configure(ProjectConfigurationRequest request,
			IProgressMonitor monitor) throws CoreException {
		if (request == null) {
			return;
		}
		
		if (request.isProjectImport() == true) {
			IProject project = request.getProject();
			SupportedProjectType projectType = null;
			if (isValidInterfaceProject(project)
					&& TurmericServiceUtils.isSOAInterfaceProject(project) == false) {
				projectType = SupportedProjectType.INTERFACE;
			} else if (isValidImplementationProject(project)
					&& TurmericServiceUtils.isSOAImplProject(project) == false) {
				projectType = SupportedProjectType.IMPL;
			} else if (isValidConsumerProject(project)
					&& TurmericServiceUtils.isSOAConsumerProject(project) == false) {
				projectType = SupportedProjectType.CONSUMER;
			} else if (isValidTypeLibraryProject(project)
					&& TurmericServiceUtils.isSOATypeLibraryProject(project) == false) {
				projectType = SupportedProjectType.TYPE_LIBRARY;
			} else if (isValidErrorLibraryProject(project)
					&& TurmericServiceUtils.isSOAErrorLibraryProject(project) == false) {
				projectType = SupportedProjectType.ERROR_LIBRARY;
			} else {
				//OK this is not a Turmeric project after all.
				return;
			}
			
			String natureId = GlobalRepositorySystem.instanceOf().getActiveRepositorySystem()
			.getProjectNatureId(projectType);
			
			if (StringUtils.isNotBlank(natureId)) {
				//it is a SOA project
				JDTUtil.addNatures(project, monitor, natureId);
				InputStream input = null;
				try {
					input = request.getPom().getContents();
					Model model = MavenCoreUtils.mavenEclipseAPI().parsePom(input);
					Build build = model.getBuild();
					final IJavaProject javaProject = JavaCore.create(project);
					List<IPath> srcDirs = JDTUtil.getSourceDirectories(project);
					List<IPath> additionalSrcDirs = new ArrayList<IPath>();
					Plugin: for (Plugin plugin : build.getPlugins()) {
						if (plugin.getArtifactId().equals("build-helper-maven-plugin")) {
							for (PluginExecution exec : plugin.getExecutions()) {
								if ("add-source".equals(exec.getId()) && exec.getConfiguration() != null) {
									String xml = exec.getConfiguration().toString();
									InputStream ins = null;
									try {
										ins = new ByteArrayInputStream(xml.getBytes());
										Document doc = JDOMUtil.readXML(ins);
										Element elem = doc.getRootElement().getChild("sources");
										if (elem != null) {
											for (Object obj : elem.getChildren("source")) {
												if (obj instanceof Element) {
													IPath src = new Path(((Element)obj).getTextTrim());
													if (srcDirs.contains(src) == false) {
														additionalSrcDirs.add(src);
													}
												}
											}
										}
										
									} finally {
										IOUtils.closeQuietly(ins);
									}
									break Plugin;
								}
							}
						}
					}
					
					if (additionalSrcDirs.isEmpty() == false) {
						final List<IClasspathEntry> entries = ListUtil.arrayList(javaProject.readRawClasspath());
						IPath outputDir = project.getFolder(build.getOutputDirectory()).getFullPath();
						List<String> missingDirs = new ArrayList<String>();
						for (IPath path : additionalSrcDirs) {
							IFolder folder = project.getFolder(path);
							if (folder.exists() == false) {
								missingDirs.add(path.toString());
							}
							IPath srcPath = project.getFolder(path).getFullPath();
							if (containsSourcePath(entries, srcPath) == false) {
								entries.add(JavaCore.newSourceEntry(srcPath, new IPath[0], 
										new IPath[0], outputDir.makeAbsolute()));
							}
						}
						if (missingDirs.isEmpty() == false) {
							WorkspaceUtil.createFolders(project, missingDirs, monitor);
						}
						
						javaProject.setRawClasspath(entries.toArray(new IClasspathEntry[0]), monitor);
					}
				} catch (Exception e) {
					throw new CoreException(EclipseMessageUtils.createErrorStatus(e));
				} finally {
					IOUtils.closeQuietly(input);
				}
			}
		}
	}
	
	private static boolean containsSourcePath(List<IClasspathEntry> entries, IPath srcPath) {
		for (IClasspathEntry entry : entries) {
			if (entry.getPath().equals(srcPath))
				return true;
		}
		return false;
	}
	
	/**
	 * 
	 * @param project the eclipse project to validate
	 * @return true if it is a valid interface project
	 */
	public static boolean isValidInterfaceProject(IProject project) {
		return isFileAccessible(project, 
				SOAProjectConstants.PROPS_FILE_SERVICE_INTERFACE);
	}
	
	/**
	 * 
	 * @param project the implementation project to validate.
	 * @return true if it is a valid implementation project.
	 */
	public static boolean isValidImplementationProject(IProject project) {
		return isFileAccessible(project, 
				SOAProjectConstants.PROPS_FILE_SERVICE_IMPL);
	}
	
	/**
	 * 
	 * @param project the consumer project to validate
	 * @return true if it is a valid consumer project
	 */
	public static boolean isValidConsumerProject(IProject project) {
		return isFileAccessible(project, 
				SOAProjectConstants.PROPS_FILE_SERVICE_CONSUMER);
	}
	
	/**
	 * 
	 * @param project the project to verify
	 * @return true if it is a valid type library project
	 */
	public static boolean isValidTypeLibraryProject(IProject project) {
		return isFileAccessible(project, 
				SOAProjectConstants.PROPS_FILE_TYPE_LIBRARY);
	}
	
	private static final String ERROR_DOMAIN_LIST_PATH_PREFIX = 
		SOAProjectConstants.FOLDER_META_SRC + WorkspaceUtil.PATH_SEPERATOR + 
		SOAProjectConstants.FOLDER_META_INF + WorkspaceUtil.PATH_SEPERATOR + "errorlibrary/";
	
	/**
	 * 
	 * @param project the project to validate
	 * @return true if it is a valid error library project
	 */
	public static boolean isValidErrorLibraryProject(IProject project) {
		String path = ERROR_DOMAIN_LIST_PATH_PREFIX + project.getName() + "/domain_list.properties";
		return isFileAccessible(project, path);
	}
	
	private static boolean isFileAccessible(IProject project, String fileRelativePath) {
		if (project.isAccessible()) {
			return project.getFile(fileRelativePath)
			.isAccessible();
		}
		return false;
	}

}
