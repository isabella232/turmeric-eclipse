/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.services.buildsystem;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import org.ebayopensource.turmeric.eclipse.buildsystem.core.BuildSystemConfigurer;
import org.ebayopensource.turmeric.eclipse.buildsystem.services.SOAResourceCreator;
import org.ebayopensource.turmeric.eclipse.buildsystem.utils.BuildSystemCodeGen;
import org.ebayopensource.turmeric.eclipse.buildsystem.utils.ProjectPropertiesFileUtil;
import org.ebayopensource.turmeric.eclipse.core.model.services.ServiceFromWsdlParamModel;
import org.ebayopensource.turmeric.eclipse.core.resources.constants.SOAProjectConstants.SupportedProjectType;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.GlobalRepositorySystem;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.ISOAOrganizationProvider;
import org.ebayopensource.turmeric.eclipse.resources.model.ProjectModel;
import org.ebayopensource.turmeric.eclipse.resources.model.SOAImplMetadata;
import org.ebayopensource.turmeric.eclipse.resources.model.SOAImplProject;
import org.ebayopensource.turmeric.eclipse.resources.model.SOAIntfProject;
import org.ebayopensource.turmeric.eclipse.resources.model.SOAProjectEclipseMetadata;
import org.ebayopensource.turmeric.eclipse.utils.plugin.ProgressUtil;
import org.ebayopensource.turmeric.eclipse.utils.plugin.WorkspaceUtil;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.jdom.Document;

/**
 * The Class ImplementationCreator.
 * 
 * @author smathew Creates the implementation project model
 */
public class WebProjectCreator {
	public static final String NATURE_ID_SOA_WEB_PROJECT = "com.ebay.soaframework.eclipse.SOADeployableNature";
	public static final String NATURE_ID_SOA_WEB_WITH_DOMAIN_PROJECT = "com.ebay.soaframework.eclipse.SOACoreDomainDeployableNature";
	public static final String webXmlFilePath="/src/main/webapp/WEB-INF/web.xml";
	/**
	 * Creates the impl model from blank wsdl.
	 * 
	 * @param paramModel
	 *            the param model
	 * @param interfaceProject
	 *            the interface project
	 * @param monitor
	 *            the monitor
	 * @return the sOA impl project
	 * @throws Exception
	 *             the exception
	 */
	public static SOAImplProject createImplModelFromBlankWsdl(
			ServiceFromWsdlParamModel paramModel,
			SOAIntfProject interfaceProject, IProgressMonitor monitor)
			throws Exception {
		// Creates the SOA related metadata
		SOAImplMetadata implMetadata = SOAImplMetadata.create(paramModel,
				interfaceProject.getMetadata());
		ProgressUtil.progressOneStep(monitor);

		SOAProjectEclipseMetadata eclipseMetadata = SOAProjectEclipseMetadata
				.create(implMetadata.getServiceImplProjectName(),
						paramModel.getWorkspaceRootDirectory());
		ProgressUtil.progressOneStep(monitor);

		SOAImplProject implProject = SOAImplProject.create(implMetadata,
				eclipseMetadata);
		ProgressUtil.progressOneStep(monitor);

		final ISOAOrganizationProvider orgProvider = GlobalRepositorySystem
				.instanceOf().getActiveRepositorySystem()
				.getActiveOrganizationProvider();
		Set<String> requiredLibraries = paramModel.getImplLibs();
		requiredLibraries.addAll(orgProvider
				.getDefaultDependencies(SupportedProjectType.IMPL));
		implProject.setRequiredLibraries(requiredLibraries);
		// adding the service project now
		Set<String> requiredProjects = paramModel.getImplProjects();
		requiredProjects.add(interfaceProject.getEclipseMetadata()
				.getProjectName());
		implProject.setRequiredProjects(requiredProjects);
		ProgressUtil.progressOneStep(monitor);
		return implProject;
	}

	
	public static Set<String> getAllWebProjects(boolean core) {
		ArrayList<IProject> soaWebProjects = new ArrayList<IProject>();
		Set<String> allWebProjectNames = new HashSet<String>();
		String nature = "";
		if(core){
			nature=NATURE_ID_SOA_WEB_WITH_DOMAIN_PROJECT;
		}
		else{
			nature = NATURE_ID_SOA_WEB_PROJECT;
		}
		try {
			soaWebProjects = WorkspaceUtil
					.getProjectsByNature(nature);
		} catch (CoreException e1) {
			e1.printStackTrace();
		}
		for(IProject project : soaWebProjects){
			allWebProjectNames.add(project.getName());
		}
		return allWebProjectNames;
	}

	/**
	 * Creates the impl model from existing wsdl.
	 * 
	 * @param paramModel
	 *            the param model
	 * @param interfaceProject
	 *            the interface project
	 * @param monitor
	 *            the monitor
	 * @return the sOA impl project
	 * @throws Exception
	 *             the exception
	 */
	public static SOAImplProject createImplModelFromExistingWsdl(
			ServiceFromWsdlParamModel paramModel,
			SOAIntfProject interfaceProject, IProgressMonitor monitor)
			throws Exception {
		// Creates the SOA related metadata
		SOAImplMetadata implMetadata = SOAImplMetadata.create(paramModel,
				interfaceProject.getMetadata());
		SOAProjectEclipseMetadata eclipseMetadata = SOAProjectEclipseMetadata
				.create(implMetadata.getServiceImplProjectName(),
						paramModel.getWorkspaceRootDirectory());
		SOAImplProject implProject = SOAImplProject.create(implMetadata,
				eclipseMetadata);

		final ISOAOrganizationProvider orgProvider = GlobalRepositorySystem
				.instanceOf().getActiveRepositorySystem()
				.getActiveOrganizationProvider();
		Set<String> requiredLibraries = paramModel.getImplLibs();
		requiredLibraries.addAll(orgProvider
				.getDefaultDependencies(SupportedProjectType.IMPL));

		implProject.setRequiredLibraries(requiredLibraries);
		// adding the service project now
		Set<String> requiredProjects = paramModel.getImplProjects();
		requiredProjects.add(interfaceProject.getEclipseMetadata()
				.getProjectName());
		implProject.setRequiredProjects(requiredProjects);
		return implProject;
	}

	
	public static void createWebProject(ServiceFromWsdlParamModel soaModel,
			IProgressMonitor monitor) {

		ProjectModel model = new ProjectModel();

		Properties properties = model.getProperties();
		if (properties == null) {
			properties = new Properties();
		}
		properties.put("webProjectName", soaModel.getWebProjectName());
		properties.put("webProjectDescription", soaModel.getWebProjectDesc());
		// todoforlaterproperties.put("RaptorPlatformVersion", );
		properties.put("appName", soaModel.getAppName());
		properties.put("groupId", soaModel.getWebProjectGroupID());

		properties.put("implProjectGroupId", "com.ebay.impl.soa");
		properties.put("implProjectArtifactId", soaModel.getImplName());
		properties.put("implVersion", soaModel.getServiceVersion());
		properties.put("admin_name", soaModel.getServiceName());
		properties.put("artifactId", soaModel.getWebProjectName());
		properties.put("version", "1.0.0-SNAPSHOT");
		String activatorName = soaModel.getServiceName() + "Activator";
		properties.put("activatorName", activatorName);
		String registererName= soaModel.getServiceName() + "Registerer";
		properties.put("serviceRegistry", registererName);
		
		if(soaModel.getRaptorPlatformVersion()!=null)
		properties.put("RaptorPlatformVersion",soaModel.getRaptorPlatformVersion());

		String activatorPackageName = null;
		String implPackage = null;
		String implClassName = soaModel.getServiceImpl();
		if (implClassName != null) {
			implPackage = implClassName.substring(0,
					implClassName.lastIndexOf("."));
		}
		if (implPackage != null) {
			activatorPackageName = implPackage + ".gen.*";
		}
		// Add the bundle activator
		properties.put("activatorPackageName", activatorPackageName);
		properties.put("serviceRegisterersFullClassName", implPackage+".gen."+registererName);
		properties.put("dummy", activatorPackageName+"."+registererName);
		IProject projectMine = WorkspaceUtil.getProject(soaModel
				.getWebProjectName());

		properties
				.put("javaPackage", implPackage + "." + soaModel.getServiceName()+"."+soaModel.getWebProjectName().toLowerCase());
		IProject project = projectMine;
		IPath location = new Path(soaModel.getWorkspaceRootDirectory());

		BuildSystemConfigurer.performRepositorySpecificTasks(project, location,soaModel.getWebProjectArchetypeGid(),soaModel.getWebProjectArchetypeArtId(),
				soaModel.getWebProjectArchetypeVsn(),
				properties.getProperty("javaPackage"), properties, monitor);

	}

	public static void updateWebProject(
			ServiceFromWsdlParamModel paramModel,
			IProgressMonitor monitor) {
		//Reuse
		IProject webProject = WorkspaceUtil.getProject(paramModel.getWebProjectName());
		
		generateAppTestFile(webProject,paramModel);
		
		BuildSystemConfigurer.performRepositorySpecificTasks(webProject,paramModel,monitor);
	}
	

	private static void generateAppTestFile(IProject webProject,ServiceFromWsdlParamModel paramModel)
	{
		String contents = "package ${package};${ln}${ln}import ${activatorPackageName};${ln}${ln}public class AppTest {${ln}${ln}public static void main(String[] args) {${ln}${activatorName}.init();${ln}}${ln}}";
		String implPackage = null;String activatorPackageName=null;
		String implClassName = paramModel.getServiceImpl();
		if (implClassName != null) {
			implPackage = implClassName.substring(0,
					implClassName.lastIndexOf("."));
		}
		if (implPackage != null) {
			activatorPackageName = implPackage + ".gen.*";
		}
		// Add the bundle activator
		contents = contents.replace("${activatorName}",paramModel.getServiceName() + "Activator");
		contents = contents.replace("${activatorPackageName}", activatorPackageName);	
		contents = contents.replace("${package}",  implPackage + "." + paramModel.getServiceName()+"."+paramModel.getWebProjectName().toLowerCase());
		contents=contents.replace("${ln}",System.getProperty("line.separator"));
		String filePath = ".src.main.java."+ implPackage + "." + paramModel.getServiceName()+"."+paramModel.getWebProjectName().toLowerCase()+".";
		filePath = filePath.replace(".",File.separator );
		filePath= filePath+"AppTest.java";
		IFile file = webProject.getFile(filePath);
		try{
		prepare((IFolder) file.getParent());
		InputStream source = new ByteArrayInputStream(contents.getBytes());
		file.create(source, true, null);
		webProject.refreshLocal(IResource.DEPTH_INFINITE, new 
					NullProgressMonitor());
		} catch (CoreException e) {
			e.printStackTrace();
		} 
		
	}
	
	private static void prepare(IFolder folder) throws CoreException {
		if (!folder.exists()) {
	        prepare((IFolder) folder.getParent());
	        folder.create(false, false, null);
	    }		
	}
	
	
		

}
