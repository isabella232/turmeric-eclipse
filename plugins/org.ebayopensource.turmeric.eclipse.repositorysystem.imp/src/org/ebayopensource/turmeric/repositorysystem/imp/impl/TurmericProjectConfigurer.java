/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
/**
 * 
 */
package org.ebayopensource.turmeric.repositorysystem.imp.impl;

import java.io.IOException;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.model.Build;
import org.apache.maven.model.Model;
import org.apache.maven.model.Plugin;
import org.apache.maven.repository.metadata.ArtifactMetadata;
import org.ebayopensource.turmeric.eclipse.buildsystem.utils.ProjectPropertiesFileUtil;
import org.ebayopensource.turmeric.eclipse.core.logging.SOALogger;
import org.ebayopensource.turmeric.eclipse.core.resources.constants.SOAProjectConstants;
import org.ebayopensource.turmeric.eclipse.maven.core.utils.MavenCoreUtils;
import org.ebayopensource.turmeric.eclipse.maven.core.utils.SOAMavenConstants;
import org.ebayopensource.turmeric.eclipse.maven.core.utils.SOAMavenConstants.ProjectType;
import org.ebayopensource.turmeric.eclipse.maven.ui.utils.MavenUIUtils;
import org.ebayopensource.turmeric.eclipse.mavenapi.exception.MavenEclipseApiException;
import org.ebayopensource.turmeric.eclipse.mavenapi.intf.IMavenEclipseApi;
import org.ebayopensource.turmeric.eclipse.mavenapi.request.ProjectMavenizationRequest;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.impl.AbstractSOAProjectConfigurer;
import org.ebayopensource.turmeric.eclipse.repositorysystem.utils.TurmericServiceUtils;
import org.ebayopensource.turmeric.eclipse.resources.model.AssetInfo;
import org.ebayopensource.turmeric.eclipse.resources.model.IAssetInfo;
import org.ebayopensource.turmeric.eclipse.resources.model.ISOAProject;
import org.ebayopensource.turmeric.eclipse.resources.model.SOABaseProject;
import org.ebayopensource.turmeric.eclipse.resources.model.SOAConsumerProject;
import org.ebayopensource.turmeric.eclipse.resources.model.SOAImplProject;
import org.ebayopensource.turmeric.eclipse.resources.model.SOAIntfMetadata;
import org.ebayopensource.turmeric.eclipse.resources.model.SOAIntfProject;
import org.ebayopensource.turmeric.eclipse.utils.collections.ListUtil;
import org.ebayopensource.turmeric.eclipse.utils.collections.SetUtil;
import org.ebayopensource.turmeric.eclipse.utils.plugin.EclipseMessageUtils;
import org.ebayopensource.turmeric.eclipse.utils.plugin.JDTUtil;
import org.ebayopensource.turmeric.eclipse.utils.plugin.ProgressUtil;
import org.ebayopensource.turmeric.eclipse.utils.plugin.WorkspaceUtil;
import org.ebayopensource.turmeric.repositorysystem.imp.utils.TurmericConstants;
import org.ebayopensource.turmeric.repositorysystem.imp.utils.TurmericPluginConfigLoader;
import org.ebayopensource.turmeric.repositorysystem.imp.utils.TurmericUtil;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectNature;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IClasspathContainer;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;

import freemarker.template.TemplateException;

/**
 * The Class TurmericProjectConfigurer.
 *
 * @author yayu
 */
public class TurmericProjectConfigurer extends AbstractSOAProjectConfigurer {
	private static final SOALogger logger = SOALogger.getLogger();
	/**
	 * Specifies that the endcoding should be UTF8.
	 */
	public static final String ENCODING_UTF8 = "UTF-8";
	
	/**
	 * The source folder exclusion pattern.  This is specified as a regular expression. **.java.
	 */
	public static final String SRC_FOLDER_EXCLUDE_PATTERN = "**.java";
	private static final List<String> EMPTY_STRING_LIST = ListUtil.list(new String[0]);

	/**
	 * Instantiates a new turmeric project configurer.
	 */
	public TurmericProjectConfigurer() {
		super();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.ebayopensource.turmeric.eclipse.repositorysystem.core.ISOAProjectConfigurer#getRepositoryPath(org.eclipse.core.runtime.IPath)
	 */

	public IPath getRepositoryPath(IPath projectLocation) {
		return null;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.ebayopensource.turmeric.eclipse.repositorysystem.core.ISOAProjectConfigurer#initializeProject(org.ebayopensource.turmeric.eclipse.resources.model.SOAIntfProject, org.ebayopensource.turmeric.eclipse.resources.model.SOAImplProject, org.eclipse.core.runtime.IProgressMonitor)
	 */
	public void initializeProject(SOAIntfProject intfProject,
			SOAImplProject implProject, IProgressMonitor monitor)
			throws Exception {
		final Set< String > dependencies = SetUtil.linkedSet();
        dependencies.addAll( intfProject.getRequiredLibraries() );
        dependencies.add(SOAMavenConstants.LIBRARY_JUNIT);
        dependencies.addAll( intfProject.getRequiredProjects() );
        
        final ProjectMavenizationRequest request = generateRequest( intfProject.getProject(),  
        		ProjectType.INTERFACE.name(), 
        		TurmericConstants.SOA_INTERFACE_GROUPID, intfProject.getMetadata().getServiceVersion(), 
        		SOAMavenConstants.MAVEN_PACKAGING_JAR, SOAProjectConstants.FOLDER_SRC, 
        		ListUtil.arrayList(SOAProjectConstants.FOLDER_SRC, SOAProjectConstants.FOLDER_GEN_SRC_CLIENT), 
        		dependencies);
        final Properties properties = request.getProperties();
        //lets try not keep any information in the pom file
        if (implProject != null)
        	//TODO this should be removed when the service registry is ready
        	properties.setProperty( SOAMavenConstants.POM_PROP_KEY_IMPL_PROJECT_NAME, implProject.getProjectName());
        
        mavenizeAndCleanUp( intfProject.getProject(), request, monitor);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.ebayopensource.turmeric.eclipse.repositorysystem.core.ISOAProjectConfigurer#initializeProject(org.ebayopensource.turmeric.eclipse.resources.model.SOAIntfProject, org.eclipse.core.runtime.IProgressMonitor)
	 */
	public void initializeProject(SOAIntfProject intfProject,
			IProgressMonitor monitor) throws Exception {
		initializeProject(intfProject, (SOAImplProject)null, monitor);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.ebayopensource.turmeric.eclipse.repositorysystem.core.ISOAProjectConfigurer#initializeProject(org.ebayopensource.turmeric.eclipse.resources.model.SOAImplProject, org.eclipse.core.runtime.IProgressMonitor)
	 */
	public void initializeProject(SOAImplProject implProject,
			IProgressMonitor monitor) throws Exception {
		final Set< String > dependencies = SetUtil.linkedSet();
		final SOAIntfMetadata intfMetadata = implProject.getMetadata().getIntfMetadata();
		dependencies.add( MavenCoreUtils.translateInterfaceLibraryName(intfMetadata.getServiceName(), intfMetadata.getServiceVersion()));
        dependencies.addAll( implProject.getRequiredLibraries() );
        dependencies.addAll( implProject.getRequiredProjects() );
        final String baseConsumerDir = implProject.getMetadata().getBaseConsumerSrcDir();
        
        final ProjectMavenizationRequest request = generateRequest( implProject.getProject(), 
        		ProjectType.IMPLEMENTATION.name(), 
        		TurmericConstants.SOA_IMPL_GROUPID, intfMetadata.getServiceVersion(), 
        		SOAMavenConstants.MAVEN_PACKAGING_JAR, baseConsumerDir, 
        		ListUtil.list(SOAProjectConstants.FOLDER_GEN_SRC_SERVICE, SOAProjectConstants.FOLDER_GEN_SRC_CLIENT),
        		dependencies);
        final Properties properties = request.getProperties();
        properties.setProperty( SOAMavenConstants.SOA_INTERFACE_GROUPID_PROPERTYNAME, 
        		TurmericConstants.SOA_INTERFACE_GROUPID );
        //this is also the interface project name
        properties.setProperty( SOAMavenConstants.POM_PROP_KEY_SERVICE_NAME, intfMetadata.getServiceName() );        
        properties.setProperty( SOAMavenConstants.POM_PROP_KEY_REQUIRED_SERVICES, "" );
        mavenizeAndCleanUp( implProject.getProject(), request, monitor );
	}

	/** 
	 * {@inheritDoc}
	 * 
	 * @see org.ebayopensource.turmeric.eclipse.repositorysystem.core.ISOAProjectConfigurer#initializeProject(org.ebayopensource.turmeric.eclipse.resources.model.SOAConsumerProject, java.lang.String, boolean, org.eclipse.core.runtime.IProgressMonitor)
	 */
	public void initializeProject(SOAConsumerProject consumerProject,
			String serviceVersion, boolean convertingJavaProject,
			IProgressMonitor monitor) throws Exception {
		final Set< String > dependencies = SetUtil.linkedSet();
        dependencies.addAll( consumerProject.getRequiredLibraries() );
        dependencies.addAll( consumerProject.getRequiredProjects() );
        final String baseConsumerDir = consumerProject.getMetadata().getBaseConsumerSrcDir();
        
        for (final SOAIntfMetadata intfMetadata : consumerProject.getRequiredServices().values()) {
        	dependencies.add( MavenCoreUtils.translateInterfaceLibraryName(intfMetadata.getServiceName(), intfMetadata.getServiceVersion()));
        }
        
        //TODO deal with the conversion of consumer when the new TED is ready.
        final ProjectMavenizationRequest request = generateRequest( consumerProject.getProject(), 
        		ProjectType.CONSUMER.name(), 
        		TurmericConstants.SOA_CLIENT_GROUPID, serviceVersion, 
        		SOAMavenConstants.MAVEN_PACKAGING_JAR, baseConsumerDir, 
        		EMPTY_STRING_LIST,
        		dependencies);
        final Properties properties = request.getProperties();
        properties.setProperty( SOAMavenConstants.POM_PROP_KEY_REQUIRED_SERVICES, 
        		StringUtils.join(consumerProject.getRequiredServices().keySet(), SOAProjectConstants.DELIMITER_COMMA) );
        
        mavenizeAndCleanUp( consumerProject.getProject(), request, monitor );
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.ebayopensource.turmeric.eclipse.repositorysystem.core.ISOAProjectConfigurer#initializeTypeLibProject(org.ebayopensource.turmeric.eclipse.resources.model.SOABaseProject, java.lang.String, org.eclipse.core.runtime.IProgressMonitor)
	 */
	public void initializeTypeLibProject(SOABaseProject typeLibProject,
			String version, IProgressMonitor monitor) throws Exception {
		final Set< String > dependencies = typeLibProject.getRequiredLibraries();
        dependencies.addAll( typeLibProject.getRequiredProjects() );
        
        final ProjectMavenizationRequest request = generateRequest( typeLibProject.getProject(), 
        		ProjectType.TYPELIBRARY.name(), 
        		TurmericConstants.SOA_TYPELIBRARY_GROUPID, version, 
        		SOAMavenConstants.MAVEN_PACKAGING_JAR, SOAProjectConstants.FOLDER_GEN_SRC, typeLibProject.getSourceDirectoryNames(), dependencies);
        
        mavenizeAndCleanUp( typeLibProject.getProject(), request, monitor );
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.ebayopensource.turmeric.eclipse.repositorysystem.core.ISOAProjectConfigurer#initializeErrorLibProject(org.ebayopensource.turmeric.eclipse.resources.model.SOABaseProject, java.lang.String, org.eclipse.core.runtime.IProgressMonitor)
	 */
	public void initializeErrorLibProject(SOABaseProject errorLibProject,
			String version, IProgressMonitor monitor) throws Exception {
		final Set< String > dependencies = SetUtil.linkedSet(
				errorLibProject.getRequiredLibraries());
        dependencies.addAll( errorLibProject.getRequiredProjects() );
        //we need an additional dependency for eBox mode
        //dependencies.addAll(ListUtil.arrayList(TurmericConstants.ERROR_LIB_DEPENDENCIES_TURMERIC));
        final IProject project = errorLibProject.getProject();
        final ProjectMavenizationRequest request = generateRequest( project, 
        		ProjectType.ERRORLIBRARY.name(), 
        		TurmericConstants.SOA_ERRORLIBRARY_GROUPID, version, 
        		SOAMavenConstants.MAVEN_PACKAGING_JAR, null, EMPTY_STRING_LIST, 
        		dependencies);
        mavenizeAndCleanUp( project, request, monitor );
        
        // Since the directory structure of error library project does not follow standard maven specification,
        // we need to customize it.
        TurmericUtil.configureErrorLibraryProject(errorLibProject, monitor);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.ebayopensource.turmeric.eclipse.repositorysystem.core.ISOAProjectConfigurer#updateProject(org.ebayopensource.turmeric.eclipse.resources.model.ISOAProject, org.eclipse.core.runtime.IProgressMonitor)
	 */
	public boolean updateProject(ISOAProject soaProject,
			IProgressMonitor monitor) throws Exception {
		return updateProject(soaProject, false, monitor);
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.ebayopensource.turmeric.eclipse.repositorysystem.core.ISOAProjectConfigurer#updateProject(org.ebayopensource.turmeric.eclipse.resources.model.ISOAProject, boolean)
	 */
	@Override
	public boolean updateProject(ISOAProject soaProject, boolean updateClasspath, IProgressMonitor monitor)
			throws Exception {
		boolean result = super.updateProject(soaProject, updateClasspath, monitor);
		if (result == true) {
			if (soaProject instanceof SOAImplProject) {
				ProjectPropertiesFileUtil.createPropsFile((SOAImplProject)soaProject);
			}
			return MavenCoreUtils.updateProjectInfo(soaProject);
		}
		return result;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.ebayopensource.turmeric.eclipse.repositorysystem.core.ISOAProjectConfigurer#addDependency(java.lang.String, java.lang.String, java.lang.String, boolean, org.eclipse.core.runtime.IProgressMonitor)
	 */
	public boolean addDependency(String projectName,
			String dependentProjectName, String type, boolean addRemove, IProgressMonitor monitor)
			throws Exception {
		final Map<String, String> libs = new ConcurrentHashMap<String, String>(1);
		libs.put(dependentProjectName, type);
		return modifyDependencyList(projectName, libs, addRemove, monitor);
	}
	
	private boolean modifyDependencyList(String projectName,
			Map<String, String> dependentLibraries, boolean addRemove, 
			IProgressMonitor monitor)
			throws Exception {
		boolean result = false;
		final IProject project = WorkspaceUtil.getProject(projectName);
		if (project != null && project.isAccessible()) {
			result = MavenUIUtils.addDependency(project, dependentLibraries, addRemove, monitor);
			if (result == true && addRemove) {
				for (String libName : dependentLibraries.keySet()) {
					MavenCoreUtils.updateMavenClasspathContainer(project, 
							libName, dependentLibraries.get(libName));
				}
			}
		}
		
		return result;
	}
	
	private Map<String, String> convertDependencies(List<AssetInfo> dependentLibraries) throws CoreException {
		final Map<String, String> libs = new LinkedHashMap<String, String>(
				dependentLibraries.size());
		for (AssetInfo info : dependentLibraries) {
			String type = info.getType();
			if (WorkspaceUtil.getProject(info.getName()).isAccessible()
					&& TurmericServiceUtils.isSOAInterfaceProject(
							WorkspaceUtil.getProject(info.getName()))) {
				type = IAssetInfo.TYPE_SERVICE_LIBRARY;
			}
			libs.put(info.getName(), type);
		}
		return libs;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.ebayopensource.turmeric.eclipse.repositorysystem.core.ISOAProjectConfigurer#addDependencies(java.lang.String, java.util.List, org.eclipse.core.runtime.IProgressMonitor)
	 */
	public boolean addDependencies(String projectName,
			List<AssetInfo> dependentLibraries, IProgressMonitor monitor)
			throws Exception {
		return modifyDependencyList(projectName, 
				convertDependencies(dependentLibraries), true, monitor);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.ebayopensource.turmeric.eclipse.repositorysystem.core.ISOAProjectConfigurer#removeDependencies(java.lang.String, java.util.List, org.eclipse.core.runtime.IProgressMonitor)
	 */
	public boolean removeDependencies(String projectName,
			List<AssetInfo> dependentLibraries, IProgressMonitor monitor)
			throws Exception {
		return modifyDependencyList(projectName, 
				convertDependencies(dependentLibraries), false, monitor);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.ebayopensource.turmeric.eclipse.repositorysystem.core.ISOAProjectConfigurer#addTypeLibraryDependency(java.lang.String, java.lang.String, java.lang.String, boolean, org.eclipse.core.runtime.IProgressMonitor)
	 */
	public boolean addTypeLibraryDependency(String projectName,
			String dependentProjectName, String type, boolean addRemove,
			IProgressMonitor monitor) throws Exception {
		final Artifact artifact = MavenCoreUtils.getLatestArtifact(
				TurmericConstants.SOA_TYPELIBRARY_GROUPID, dependentProjectName);
		String libName = dependentProjectName;
		if (artifact != null) {
			libName = MavenCoreUtils.libraryName(artifact);
		}

		boolean result = addDependency(projectName, libName, type, 
				addRemove, monitor);
		if (result == true && addRemove) {
			final IProject project = WorkspaceUtil.getProject(projectName);
			if (project != null && project.isAccessible()) {
				MavenCoreUtils.updateMavenClasspathContainer(project, 
						dependentProjectName, TurmericConstants.SOA_TYPELIBRARY_GROUPID);
			}
		}
		
		return result;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.ebayopensource.turmeric.eclipse.repositorysystem.core.ISOAProjectConfigurer#addProjectLinkedResources(org.ebayopensource.turmeric.eclipse.resources.model.SOABaseProject)
	 */
	public void addProjectLinkedResources(SOABaseProject project) {
		//do nothing here

	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.ebayopensource.turmeric.eclipse.repositorysystem.core.impl.AbstractSOAProjectConfigurer#serviceVersionChanged(java.lang.String, java.lang.String)
	 */
	@Override
	protected void serviceVersionChanged(String serviceName,
			String newServiceVersion) {
		//do nothing here

	}
	
	
	
	private ProjectMavenizationRequest generateRequest( final IProject project, 
			final String projectType,
			final String groupID,
			final String version,
			final String packaging,
			final String sourceDir, 
			final Collection<String> sourceDirectories,
			final Set< String > dependencies) throws Exception {
		final ProjectMavenizationRequest request = ProjectMavenizationRequest.createRequest( project );
		final ArtifactMetadata artifact = new ArtifactMetadata( groupID, project.getName(), version, packaging );
		request.setArtifact(artifact);
		request.setDependencies( MavenCoreUtils.artifactMetadata( dependencies ) );
		
		//setup SOA app dir structure
		Model model = TurmericPluginConfigLoader.getPluginConfigurations(projectType);
		request.setTestSourcePath(SOAProjectConstants.FOLDER_GEN_TEST);
		request.setTestOutputPath(SOAProjectConstants.FOLDER_OUTPUT_DIR);
		request.setOutputPath(SOAProjectConstants.FOLDER_OUTPUT_DIR);
		
		if (model != null) {
			final Build build = model.getBuild();
			if (build.getResources() != null) {
				request.getResourceDirectories().addAll(build.getResources());
			}
			request.setTestSourcePath(build.getTestSourceDirectory());
			if (sourceDir != null) {
				request.setSourcePath(sourceDir);
			} else {
				request.setSourcePath(build.getSourceDirectory());
			}
			if (build.getPlugins() != null) {
				for (Plugin plugin: build.getPlugins()) {
					final String pluginVersion = plugin.getVersion();
					//we might change the Plugin object, so we should do a clone first.
					Plugin newPlugin = plugin.clone();
					if (StringUtils.isNotBlank(pluginVersion) 
							&& pluginVersion.equals(TurmericConstants.TAG_AUTOUPDATE_VERSION)
							&& StringUtils.isNotBlank(plugin.getGroupId())
							&& StringUtils.isNotBlank(plugin.getArtifactId())) {
						//need to get the latest version
						Artifact pluginArtifact = MavenCoreUtils.getLatestArtifact(plugin.getGroupId(), 
								plugin.getArtifactId());
						if (pluginArtifact != null && !pluginArtifact.getVersion().contains("-T40")) {
							newPlugin.setVersion(pluginArtifact.getVersion());
						} else {
							newPlugin.setVersion(TurmericConstants.TURMERIC_DEVELOPMENT_VERSION);
						}
					}
					request.getBuildPlugins().add(newPlugin);
				}
			}
		}
		

		return request;
	}
	
	private void mavenizeAndCleanUp( final IProject project,
			final ProjectMavenizationRequest request,
			final IProgressMonitor monitor)
	throws CoreException, MavenEclipseApiException, IOException, InterruptedException, TemplateException
	{
		if (SOALogger.DEBUG)
			logger.entering(project, request);
		final IMavenEclipseApi api = MavenCoreUtils.mavenEclipseAPI();
		try
		{
			//			It seems that the pom.xml is not updated in the Eclipse Workspace by the mavenize project operation
			//			Util.refresh( project.getFile( "pom.xml" ) );
			//			Project Mavenization doesn't seem to care that the output directory is set here and set in the pom.xml
			//			it will use the maven default of target-eclipse no matter what.
			IJavaProject javaProject = null;
			try {
				api.mavenizeProject( request, monitor);
				logger.info("Mavenization finished->" + project);
				ProgressUtil.progressOneStep(monitor);
				javaProject = JavaCore.create( project );
				//javaProject.setOutputLocation( project.getFolder( SOAProjectConstants.FOLDER_OUTPUT_DIR).getFullPath(), null );
				FileUtils.deleteDirectory(project.getFolder( "target-eclipse" ).getLocation().toFile());
				//Util.delete( project.getFolder( "target-eclipse" ), null );
				final Map< ?, ? > options = javaProject.getOptions( false );
				options.clear();
				javaProject.setOptions( options );
			} catch (NullPointerException e) {
				throw new CoreException(EclipseMessageUtils
						.createErrorStatus("NPE occured during projects mavenization.\n\n" +
								"The possible cause is that the M2Eclipse Maven indexes in your current workspace might be corrupted. " +
								"Please remove folder {workspace}/.metadata/.plugins/org.maven.ide.eclipse/, and then restart your IDE.", e));
			}
//			The Mavenization also seemed to take the META-SRC src directory and add an exclusion pattern along with
//			setting its output location to itself.  Maybe they wanted to do a class library folder?  Either way its
//			no good for us.  Secondly, we are setting the Maven classpath container to have its entries exported by default.
//			Finally, we are adding the classpath entry attribute 'org.eclipse.jst.component.dependency' in case the project
//			is a WTP web project so that WTP will use the maven classpath container when deploying to its runtime servers.
			boolean changed = false;
			final List< IClasspathEntry > newEntries = ListUtil.list();
			final IPath containerPath =  new Path( SOAMavenConstants.MAVEN_CLASSPATH_CONTAINER_ID );
			for( final IClasspathEntry cpEntry : JDTUtil.rawClasspath( javaProject, true ) )
			{
				if( cpEntry.getEntryKind() == IClasspathEntry.CPE_CONTAINER ) {
					if( cpEntry.getPath().equals( containerPath ) ) {
						newEntries.add( JavaCore.newContainerEntry( containerPath, true ) );
						changed |= true;
					}
				}
				else if( cpEntry.getEntryKind() == IClasspathEntry.CPE_SOURCE ) {
					if( !ArrayUtils.isEmpty( cpEntry.getExclusionPatterns() ) || cpEntry.getOutputLocation() != null ) {
						newEntries.add( JavaCore.newSourceEntry( cpEntry.getPath() ) );
						changed |= true;
					} else {
						newEntries.add( cpEntry );
					}
				}
				else {
					newEntries.add( cpEntry );
				}
			}
			ProgressUtil.progressOneStep(monitor, 15);
			if( changed ) {
				javaProject.setRawClasspath( newEntries.toArray( new IClasspathEntry[ 0 ] ), null );
				ProgressUtil.progressOneStep(monitor);
				//This is a hot fix for the Maven classpath container issue,
				//should be removed as soon as the M2Eclipse guys fix it
				int count = 0; //we only go to sleep 5 times.
				IClasspathContainer container = MavenCoreUtils.getMaven2ClasspathContainer(javaProject);
				int entriesLength = 0;
				if (container != null) {
					entriesLength = container.getClasspathEntries().length;
				}
				while (entriesLength == 0
						&& count < 5) {
					logger.warning("Maven Classpath is empty->", SOAMavenConstants.MAVEN_CLASSPATH_CONTAINER_ID, ", going to sleep...");
					Thread.sleep(1000);
					ProgressUtil.progressOneStep(monitor, 10);
					container = MavenCoreUtils.getMaven2ClasspathContainer(javaProject);
					if (container != null) {
						entriesLength = container.getClasspathEntries().length;
					}
					count++;
				}
			}
		} finally {
			if (SOALogger.DEBUG)
				logger.exiting();
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 */
	public void addBuildSystemClasspathContainer(IJavaProject javaProject,
			IProgressMonitor monitor) throws CoreException {
		boolean found = false;
		final List< IClasspathEntry > newEntries = JDTUtil.rawClasspath( javaProject, true );
		final IPath containerPath =  new Path( SOAMavenConstants.MAVEN_CLASSPATH_CONTAINER_ID );
		for( final IClasspathEntry cpEntry : newEntries )
		{
			if( cpEntry.getEntryKind() == IClasspathEntry.CPE_CONTAINER ) {
				if( cpEntry.getPath().equals( containerPath ) ) {
					found = true;
					break;
				}
			}
		}
		ProgressUtil.progressOneStep(monitor, 15);
		if( found == false ) {
			newEntries.add( JavaCore.newContainerEntry( containerPath, true ) );
			javaProject.setRawClasspath( newEntries.toArray( new IClasspathEntry[ 0 ] ), null );
			ProgressUtil.progressOneStep(monitor);
		}

	}

	/**
	 * {@inheritDoc}
	 * 
	 * Remove the builder that was just added we don't need it.
	 * 
	 * @see org.ebayopensource.turmeric.eclipse.repositorysystem.core.ISOAProjectConfigurer#configureProjectNature(org.eclipse.core.resources.IProject, org.eclipse.core.resources.IProjectNature)
	 */
	public boolean configureProjectNature(IProject project,
			IProjectNature projectNature) throws CoreException {
		//impl project might not have the src/main/resources folder
		IPath standardPath = project.hasNature(TurmericConstants.NATURE_ID_SOA_IMPL_PROJECT)
		? new Path(SOAMavenConstants.FOLDER_SRC_MAIN_JAVA) : new Path(SOAMavenConstants.FOLDER_SRC_MAIN_RESOURCES);
		return project.exists(standardPath) == false;
	}

}
