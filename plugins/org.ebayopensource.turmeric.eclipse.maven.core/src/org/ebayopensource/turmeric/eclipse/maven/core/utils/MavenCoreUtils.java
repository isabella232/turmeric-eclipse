/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.maven.core.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.model.Dependency;
import org.apache.maven.model.Model;
import org.apache.maven.project.MavenProject;
import org.apache.maven.repository.metadata.ArtifactMetadata;
import org.ebayopensource.turmeric.eclipse.buildsystem.services.SOAResourceCreator;
import org.ebayopensource.turmeric.eclipse.logging.SOALogger;
import org.ebayopensource.turmeric.eclipse.maven.core.model.MavenAssetInfo;
import org.ebayopensource.turmeric.eclipse.maven.core.model.MavenProjectInfo;
import org.ebayopensource.turmeric.eclipse.maven.core.repositorysystem.IMavenOrganizationProvider;
import org.ebayopensource.turmeric.eclipse.mavenapi.MavenApiPlugin;
import org.ebayopensource.turmeric.eclipse.mavenapi.exception.MavenEclipseApiException;
import org.ebayopensource.turmeric.eclipse.mavenapi.impl.MavenApiHelper;
import org.ebayopensource.turmeric.eclipse.mavenapi.impl.MavenEclipseUtil;
import org.ebayopensource.turmeric.eclipse.mavenapi.intf.IMavenEclipseApi;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.GlobalRepositorySystem;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.ISOAOrganizationProvider;
import org.ebayopensource.turmeric.eclipse.repositorysystem.utils.TurmericServiceUtils;
import org.ebayopensource.turmeric.eclipse.resources.constants.SOAProjectConstants;
import org.ebayopensource.turmeric.eclipse.resources.constants.SOAProjectConstants.ServiceLayer;
import org.ebayopensource.turmeric.eclipse.resources.constants.SOAProjectConstants.SupportedProjectType;
import org.ebayopensource.turmeric.eclipse.resources.model.AssetInfo;
import org.ebayopensource.turmeric.eclipse.resources.model.ISOAProject;
import org.ebayopensource.turmeric.eclipse.resources.model.ISOAProject.SOAProjectSourceDirectory;
import org.ebayopensource.turmeric.eclipse.resources.model.SOABaseProject;
import org.ebayopensource.turmeric.eclipse.resources.model.SOAConsumerProject;
import org.ebayopensource.turmeric.eclipse.resources.model.SOAImplMetadata;
import org.ebayopensource.turmeric.eclipse.resources.model.SOAImplProject;
import org.ebayopensource.turmeric.eclipse.resources.model.SOAIntfMetadata;
import org.ebayopensource.turmeric.eclipse.resources.model.SOAIntfProject;
import org.ebayopensource.turmeric.eclipse.resources.util.SOAIntfUtil;
import org.ebayopensource.turmeric.eclipse.utils.collections.ListUtil;
import org.ebayopensource.turmeric.eclipse.utils.collections.SetUtil;
import org.ebayopensource.turmeric.eclipse.utils.core.VersionUtil;
import org.ebayopensource.turmeric.eclipse.utils.lang.StringUtil;
import org.ebayopensource.turmeric.eclipse.utils.plugin.WorkspaceUtil;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.ClasspathContainerInitializer;
import org.eclipse.jdt.core.IClasspathAttribute;
import org.eclipse.jdt.core.IClasspathContainer;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.maven.ide.eclipse.embedder.ArtifactKey;
import org.maven.ide.eclipse.embedder.ArtifactRef;
import org.maven.ide.eclipse.project.IMavenProjectFacade;

/**
 * @author yayu
 * @since 1.0.0
 */
public class MavenCoreUtils {
	private static final IMavenEclipseApi mavenEclipseAPI = MavenApiPlugin
			.getDefault().getMavenEclipseApi();
	private static final SOALogger logger = SOALogger.getLogger();

	/**
	 * 
	 * @return an instance of the MavenEclipseAPI.
	 */
	public static IMavenEclipseApi mavenEclipseAPI() {
		return mavenEclipseAPI;
	}

	/**
	 * 
	 * @param project a soa base project
	 * @param monitor an eclipse progress monitor
	 * @return a configured SOABaseProject
	 * @throws CoreException 
	 */
	public static SOABaseProject configureAsStandardMavenProject(
			final SOABaseProject project, IProgressMonitor monitor)
			throws CoreException {
		final String[] testOutputPath = { SOAMavenConstants.TESTCLASSES_EXCLUDE_PATTERNS };
		final List<SOAProjectSourceDirectory> srcDirs = new ArrayList<SOAProjectSourceDirectory>(
				4);
		srcDirs.add(new SOAProjectSourceDirectory(
				SOAMavenConstants.FOLDER_SRC_MAIN_JAVA,
				SOAMavenConstants.FOLDER_TARGET_CLASSES));
		srcDirs.add(new SOAProjectSourceDirectory(
				SOAMavenConstants.FOLDER_SRC_MAIN_RESOURCES,
				SOAMavenConstants.FOLDER_TARGET_CLASSES));
		srcDirs.add(new SOAProjectSourceDirectory(
				SOAMavenConstants.FOLDER_SRC_TEST_JAVA,
				SOAMavenConstants.FOLDER_TARGET_TESTCLASSES, testOutputPath,
				true));
		srcDirs.add(new SOAProjectSourceDirectory(
				SOAMavenConstants.FOLDER_SRC_TEST_RESOURCES,
				SOAMavenConstants.FOLDER_TARGET_TESTCLASSES, testOutputPath,
				true));
		project.setSourceDirectories(srcDirs);
		project.setOutputFolder(SOAMavenConstants.FOLDER_TARGET_CLASSES);
		SOAResourceCreator
				.createFolders(project.getProject(), project, monitor);
		return project;
	}

	/**
	 * 
	 * @param mavenProject a maven project
	 * @return a set of ArtifactKeys
	 */
	public static Set<ArtifactKey> getArtifactKeys(
			final MavenProject mavenProject) {
		if (mavenProject == null)
			return SetUtil.set();
		final IMavenProjectFacade projectFacade = MavenApiHelper
				.getMavenProjectManager()
				.getMavenProject(mavenProject.getGroupId(),
						mavenProject.getArtifactId(), mavenProject.getVersion());
		return ArtifactRef.toArtifactKey(projectFacade
				.getMavenProjectArtifacts());
	}

	/**
	 * Retrieve the MavenOrganizationProvider.
	 * 
	 * @return the Maven Organization provider instance
	 */
	public static IMavenOrganizationProvider getMavenOrgProviderInstance() {
		ISOAOrganizationProvider provider = GlobalRepositorySystem.instanceOf()
				.getActiveRepositorySystem().getActiveOrganizationProvider();
		if (provider instanceof IMavenOrganizationProvider) {
			return (IMavenOrganizationProvider) provider;
		}
		// this should never happen otherwise this method should not be called
		return null;
	}

	/**
	 * we only check for the existence of interface projects.
	 * 
	 * @param serviceNames an Array of strings that contain service names
	 * @return an boolean arry of results for the service names
	 * @throws Exception 
	 */
	public static boolean[] serviceExists(final String... serviceNames)
			throws Exception {
		if (SOALogger.DEBUG)
			logger.entering(SetUtil.set(serviceNames));
		final boolean[] results = new boolean[serviceNames.length];
		final IMavenEclipseApi api = mavenEclipseAPI();
		final Set<String> existingServices = new HashSet<String>();
		String intfGroupId = getMavenOrgProviderInstance().getProjectGroupId(
				SupportedProjectType.INTERFACE);
		for (final Artifact artifact : api.findGroup(intfGroupId)) {
			existingServices.add(artifact.getArtifactId());
		}
		if (SOALogger.DEBUG)
			logger.debug("Existing Services->", existingServices);

		for (int i = 0; i < serviceNames.length; i++) {
			results[i] = existingServices.contains(serviceNames[i]);
		}
		if (SOALogger.DEBUG)
			logger.exiting(Arrays.asList(results));
		return results;
	}

	/**
	 * 
	 * @param typeLibName type library names
	 * @return true if the type library exists
	 * @throws Exception 
	 */
	public static boolean isTypeLibraryExist(final String typeLibName)
			throws Exception {
		if (SOALogger.DEBUG)
			logger.entering(typeLibName);
		boolean result = false;
		String groupId = getMavenOrgProviderInstance().getProjectGroupId(
				SupportedProjectType.TYPE_LIBRARY);
		for (final Artifact artifact : mavenEclipseAPI()
				.findArtifactByNameAndGroup(typeLibName, groupId)) {
			if (artifact.getArtifactId().equalsIgnoreCase(typeLibName)) {
				result = true;
				break;
			}
		}

		if (SOALogger.DEBUG)
			logger.exiting(Arrays.asList(result));
		return result;
	}

	/**
	 * 
	 * @param libs a list of libraries 
	 * @return a set of ArtifactMetadata for the libraries
	 * @throws MavenEclipseApiException 
	 */
	public static Set<ArtifactMetadata> artifactMetadata(
			final Collection<String> libs) throws MavenEclipseApiException {
		if (SOALogger.DEBUG)
			logger.entering(libs);

		final Set<ArtifactMetadata> metadata = SetUtil.set();
		final Set<String> processedMetadatas = new HashSet<String>();
		for (final String lib : libs) {
			ArtifactMetadata aMeta = getLibraryIdentifier(lib);
			if (aMeta != null && StringUtils.isNotBlank(aMeta.getArtifactId())) {
				final String fullLibName = libraryName(aMeta);
				if (processedMetadatas.contains(fullLibName) == false) {
					if (SOALogger.DEBUG)
						logger.debug("Non-processed lib->", fullLibName);
					metadata.add(aMeta);
					processedMetadatas.add(fullLibName);
				}
			}
		}
		if (SOALogger.DEBUG)
			logger.exiting(metadata);
		return metadata;
	}

	/**
	 * @param artifact artifact meta data
	 * @return The full library name in Maven format
	 */
	public static String libraryName(final ArtifactMetadata artifact) {
		return MavenCoreUtils.translateLibraryName(artifact.getGroupId(),
				artifact.getArtifactId(), artifact.getType(),
				artifact.getVersion());
	}

	/**
	 * 
	 * @param artifact the artifact name
	 * @return the libary name
	 */
	public static String libraryName(final Artifact artifact) {
		return MavenCoreUtils.translateLibraryName(artifact.getGroupId(),
				artifact.getArtifactId(), artifact.getType(),
				artifact.getVersion());
	}

	/**
	 * @param project the maven project
	 * @return The full library name for the given Maven project
	 */
	public static String libraryName(final MavenProject project) {
		if (SOALogger.DEBUG)
			logger.entering(project);
		final String packaging = StringUtils.isNotBlank(project.getPackaging()) ? project
				.getPackaging() : SOAMavenConstants.MAVEN_PACKAGING_JAR;
		final String result = MavenCoreUtils.translateLibraryName(
				project.getGroupId(), project.getArtifactId(), packaging,
				project.getVersion());
		if (SOALogger.DEBUG)
			logger.exiting(result);
		return result;
	}

	/**
	 * 
	 * @param metadata
	 * @return
	 * @throws MavenEclipseApiException
	 */
	public static MavenProject getLibrary(final ArtifactMetadata metadata)
			throws MavenEclipseApiException {
		if (SOALogger.DEBUG)
			logger.entering(metadata);
		final MavenProject result = mavenEclipseAPI
				.resolveArtifactAsProject(MavenEclipseUtil.artifact(metadata));
		if (SOALogger.DEBUG)
			logger.exiting(result);
		return result;
	}

	public static AssetInfo translateToAssetInfo(final Dependency dependency) {
		if (SOALogger.DEBUG)
			logger.entering(dependency);
		final String groupID = dependency.getGroupId();

		final IProject project = WorkspaceUtil.getProject(dependency
				.getArtifactId());
		AssetInfo assetInfo = null;
		if (project != null && project.isAccessible()) {
			// this is a project in the current workspace
			if (SOALogger.DEBUG)
				logger.debug("Project is accessible->", project);
			assetInfo = new MavenProjectInfo(dependency.getGroupId(),
					dependency.getArtifactId(), dependency.getVersion(), "",
					AssetInfo.TYPE_PROJECT);
		} else if (getMavenOrgProviderInstance().getProjectGroupId(
				SupportedProjectType.INTERFACE).equals(groupID)) {
			// service library
			assetInfo = new MavenAssetInfo(dependency.getGroupId(),
					dependency.getArtifactId(), dependency.getVersion(), "",
					ServiceLayer.COMMON.name(), AssetInfo.TYPE_SERVICE_LIBRARY);
		} else if (getMavenOrgProviderInstance().getProjectGroupId(
				SupportedProjectType.IMPL).equals(groupID)) {
			// project
			assetInfo = new MavenAssetInfo(dependency.getGroupId(),
					dependency.getArtifactId(), dependency.getVersion(), "",
					null, AssetInfo.TYPE_PROJECT);
		} else if (getMavenOrgProviderInstance().getProjectGroupId(
				SupportedProjectType.CONSUMER).equals(groupID)) {
			// project
			assetInfo = new MavenAssetInfo(dependency.getGroupId(),
					dependency.getArtifactId(), dependency.getVersion(), "",
					null, AssetInfo.TYPE_PROJECT);
		} else if (SOAMavenConstants.SOA_FRAMEWORK_GROUPID.equals(groupID)) {
			// library
			assetInfo = new MavenAssetInfo(dependency.getGroupId(),
					dependency.getArtifactId(), dependency.getVersion(), "",
					null, AssetInfo.TYPE_LIBRARY);
		} else {
			assetInfo = new MavenAssetInfo(dependency.getGroupId(),
					dependency.getArtifactId(), dependency.getVersion(), "",
					null, AssetInfo.TYPE_LIBRARY);
		}
		if (SOALogger.DEBUG)
			logger.exiting(assetInfo);
		return assetInfo;
	}

	public static Artifact getLatestArtifact(final String groupID,
			final String artifactID) throws MavenEclipseApiException {
		Artifact result = null;
		
		ISOAOrganizationProvider orgProvider = GlobalRepositorySystem.instanceOf()
		.getActiveRepositorySystem().getActiveOrganizationProvider();
		
		String preferredVersion = orgProvider.getPreferredArtifactVersion(groupID, artifactID);
		if (StringUtils.isNotBlank(preferredVersion)) {
			ArtifactMetadata metadata = MavenEclipseUtil.artifactMetadata(groupID, artifactID, preferredVersion, "jar");
			try {
				Artifact artifact = mavenEclipseAPI().resolveArtifact(metadata);
				if (artifact != null)
					return artifact;
			} catch (Exception e) {
				logger.warning("Can not find the preferred version ->" + metadata, e);
			}
		}
		
		for (final Artifact artifact : ((IMavenEclipseApi) mavenEclipseAPI())
				.findArtifactByNameAndGroup(artifactID, groupID)) {
			if (artifact != null
					&& StringUtils.equals(artifact.getGroupId(), groupID)
					&& StringUtils.equals(artifact.getArtifactId(), artifactID)) {
				if (result == null || VersionUtil.compare(result.getVersion(), 
						artifact.getVersion()) < 0) {
					result = artifact;
				}
			}
		}
		return result;
	}

	public static String getLibraryVersion(final String groupID,
			final String artifactID, final String defaultVersion)
			throws MavenEclipseApiException {
		if (SOALogger.DEBUG)
			logger.entering(new Object[] { groupID, artifactID, defaultVersion });
		Artifact artifact = getLatestArtifact(groupID, artifactID);
		String version = artifact != null ? artifact.getVersion() : null;

		if (version == null) {
			StringBuffer buf = new StringBuffer();
			buf.append("Can't find matching version for groupID=");
			buf.append(groupID);
			buf.append(", artifactID=");
			buf.append(artifactID);
			buf.append(". Using the default version->");
			buf.append(defaultVersion);
			logger.warning(buf.toString());
			version = defaultVersion;
		}
		if (SOALogger.DEBUG)
			logger.exiting(version);
		return version;
	}

	public static ArtifactMetadata getLibraryIdentifier(final String libName)
			throws MavenEclipseApiException {
		ArtifactMetadata result = null;
		if (SOALogger.DEBUG)
			logger.entering(libName);
		try {
			result = MavenEclipseUtil.artifactMetadata(libName);
			if (result == null || result.getGroupId() == null
					|| result.getArtifactId() == null)
				return null;
			if (StringUtils.isBlank(result.getVersion())) {
				Artifact artifact = getLatestArtifact(result.getGroupId(),
						result.getArtifactId());
				if (artifact != null) {
					result.setVersion(artifact.getVersion());
				}
			}
			return result;
		} finally {
			if (SOALogger.DEBUG)
				logger.exiting(result);
		}
	}

	/*
	 * public static String[] translateLibraryNames(final String... libNames ) {
	 * final Transformer< String > transformer = new Transformer< String >() {
	 * private static final long serialVersionUID = 3166012544618628824L; public
	 * String transform( final Object input ) { if( ArrayUtils.contains(
	 * SOAProjectConstants.SOA_FRAMEWORK_JARS, input.toString() ) ) try { return
	 * getLibraryIdentifier( input.toString()).toString(); } catch
	 * (MavenEclipseApiException e) { throw new RuntimeException(e); } return
	 * input.toString(); } }; return ListUtil.transformed( transformer, (
	 * Object[] )libNames ).toArray( new String[ 0 ] ); }
	 */

	public static String getAssetLocation(String projectName) throws Exception {
		if (StringUtils.isNotEmpty(projectName)) {
			final IProject project = WorkspaceUtil.getProject(projectName);
			if (project != null && project.isAccessible()) {
				// Case 1: exist in the workspace
				// return
				// FileLocator.resolve(project.getLocationURI().toURL()).getFile();
				// this approach would convert white spaces into %20
				return project.getLocation().makeAbsolute().toString();
			} else {
				// interface projects
				IPath jarPath = getServiceJarLocation(projectName);
				if (jarPath != null)
					return jarPath.toString();
				/*
				 * final String[] groupIDs =
				 * {SOAMavenConstants.SOA_TYPELIBRARY_GROUPID,
				 * SOAMavenConstants.SOA_ERRORLIBRARY_GROUPID,
				 * SOAMavenConstants.SOA_IMPL_GROUPID,
				 * SOAMavenConstants.SOA_CLIENT_GROUPID,
				 * SOAMavenConstants.SOA_FRAMEWORK_GROUPID};
				 */
				// FIXME we need SOA framework group ID here
				for (String groupID : getMavenOrgProviderInstance()
						.getAllProjectTypeGroupIds()) {
					jarPath = getArtifactJarLocation(groupID, projectName);
					if (jarPath != null)
						return jarPath.toString();
				}
			}

		}
		return null;
	}

	public static IPath getServiceJarLocation(final String serviceName)
			throws MavenEclipseApiException {
		return getArtifactJarLocation(getMavenOrgProviderInstance()
				.getProjectGroupId(SupportedProjectType.INTERFACE), serviceName);
	}

	/**
	 * Get the jar location for the latest versoin of the artifact
	 * 
	 * @param groupID
	 * @param artifactName
	 * @return
	 * @throws MavenEclipseApiException
	 */
	public static IPath getArtifactJarLocation(final String groupID,
			final String artifactName) throws MavenEclipseApiException {
		return getArtifactJarLocation(groupID, artifactName, null);
	}

	public static IPath getArtifactJarLocation(final String groupID,
			final String artifactName, final String version)
			throws MavenEclipseApiException {
		Artifact artifact = null;
		if (StringUtils.isNotBlank(version)) {
			artifact = MavenEclipseUtil.artifact(MavenEclipseUtil
					.artifactMetadata(groupID, artifactName, version,
							SOAProjectConstants.FILE_EXTENSION_JAR));
		} else {
			artifact = getLatestArtifact(groupID, artifactName);
		}

		if (artifact != null) {
			final MavenProject mProject = mavenEclipseAPI()
					.resolveArtifactAsProject(artifact);
			IPath jarPath = new Path(mProject.getFile().toString());
			// resolve the jar file path in the file system
			jarPath = jarPath.removeFileExtension().addFileExtension(
					SOAProjectConstants.FILE_EXTENSION_JAR);
			return jarPath;
		}
		return null;
	}

	/**
	 * Convert the given instance of IProject into a MavenProject instance
	 * 
	 * @param project
	 * @return
	 * @throws CoreException
	 * @throws MavenEclipseApiException
	 */
	public static MavenProject getMavenProject(final IProject project)
			throws CoreException, MavenEclipseApiException {
		MavenProject result = null;
		if (SOALogger.DEBUG)
			logger.entering(project);
		try {
			if (project == null || !project.isAccessible())
				return result;
			final Model pom = MavenEclipseUtil.readPOM(project);
			if (pom == null)
				return result;
			final Artifact artifact = MavenEclipseUtil
					.artifact(MavenEclipseUtil.artifactMetadata(pom));
			return result = mavenEclipseAPI()
					.resolveArtifactAsProject(artifact);
		} finally {
			if (SOALogger.DEBUG)
				logger.exiting(result);
		}
	}

	public static Set<AssetInfo> getAllTypeLibraries() throws Exception {
		return getAllLibraries(getMavenOrgProviderInstance()
				.getProjectGroupId(SupportedProjectType.TYPE_LIBRARY));
	}

	private static Set<AssetInfo> getAllLibraries(String groupID)
			throws Exception {
		final Set<AssetInfo> result = new LinkedHashSet<AssetInfo>();
		for (final Artifact artifact : mavenEclipseAPI().findGroup(groupID)) {
			try {
				final MavenProject project = mavenEclipseAPI()
						.resolveArtifactAsProject(artifact);
				final AssetInfo info = getLibraryInfo(project);
				if (info != null)
					result.add(info);
			} catch (Exception e) {
				logger.warning("Error Occured when loading artifact ["
						+ artifact + "], ignoring this artifact", e);
			}
		}
		return result;
	}

	public static Set<AssetInfo> getAllErrorLibraries() throws Exception {
		return getAllLibraries(getMavenOrgProviderInstance().getProjectGroupId(
				SupportedProjectType.ERROR_LIBRARY));
	}

	public static Set<? extends AssetInfo> getAllServicesInLocalRepository()
			throws Exception {
		final Set<AssetInfo> services = SetUtil.linkedSet();
		final IMavenEclipseApi api = mavenEclipseAPI();
		for (final Artifact artifact : api
				.findGroup(getMavenOrgProviderInstance().getProjectGroupId(
						SupportedProjectType.INTERFACE))) {
			try {
				final MavenProject project = api
						.resolveArtifactAsProject(artifact);
				services.add(getIntfProjectInfoFromProperties(
						artifact.getArtifactId(), project));
			} catch (Exception e) {
				logger.warning(e);
			}
		}
		return services;
	}

	public static AssetInfo transformToAssetInfo(final MavenProject mProject)
			throws Exception {
		if (mProject != null) {
			if (getMavenOrgProviderInstance().getProjectGroupId(
					SupportedProjectType.INTERFACE).equals(
					mProject.getGroupId())) {
				return getIntfProjectInfoFromProperties(
						mProject.getArtifactId(), mProject);
			} else if (getMavenOrgProviderInstance().getProjectGroupId(
					SupportedProjectType.IMPL).equals(mProject.getGroupId())) {

			}
		}
		return null;
	}

	private static File getJarFileForService(final MavenProject mProject) {
		if (SOALogger.DEBUG)
			logger.entering(mProject);
		File jarFile = null;

		IPath jarPath = new Path(mProject.getFile().toString());
		// resolve the jar file path in the file system
		jarPath = jarPath.removeFileExtension().addFileExtension(
				SOAProjectConstants.FILE_EXTENSION_JAR);
		jarFile = jarPath.toFile();
		/*if (mProject.getVersion().endsWith("-SNAPSHOT")) {
			//snapshot version
			File newFile = new File(jarFile.getParentFile(), mProject.getArtifactId() + "-" + mProject.getVersion() 
					+ "." + SOAProjectConstants.FILE_EXTENSION_JAR);
			if (newFile.exists())
				jarFile = newFile;
		}*/
		if (SOALogger.DEBUG)
			logger.exiting(jarFile);
		return jarFile;
	}

	/**
	 * @param mProject
	 * @param jarEntryPath
	 * @return The input stream for the specified jar entry
	 * @throws IOException
	 */
	public static InputStream getInputStreamFromJar(
			final MavenProject mProject, final String jarEntryPath)
			throws IOException {
		if (SOALogger.DEBUG)
			logger.entering(mProject, jarEntryPath);
		final File file = getJarFileForService(mProject);

		InputStream io = null;
		if (file.exists() && file.canRead()) {
			final JarFile jarFile = new JarFile(file);
			final JarEntry jarEntry = jarFile.getJarEntry(jarEntryPath);
			if (jarEntry != null) {
				io = jarFile.getInputStream(jarEntry);
			} else {
				logger.warning("Can not find the jar entry->" + jarEntryPath);
			}
		} else {
			logger.warning("Jar file is either not exist or not readable ->"
					+ file);
		}
		if (SOALogger.DEBUG)
			logger.exiting(io);
		return io;
	}

	private static MavenProjectInfo getIntfProjectInfoFromProperties(
			final String projectName, final MavenProject mProject)
			throws Exception {
		if (SOALogger.DEBUG)
			logger.entering(projectName, mProject);

		MavenProjectInfo result = null;

		// try to load from the project if available
		final IProject project = WorkspaceUtil.getProject(projectName);
		if (project != null && project.isAccessible()) {
			if (SOALogger.DEBUG)
				logger.debug("Project is accessible->", project);
			result = getInterfaceProjectInfo(project);
		} else {
			// the service only exist in the repository
			if (SOALogger.DEBUG)
				logger.debug(
						"Project is NOT accessible, reading from the local repository->",
						mProject);
			final Properties props = mProject.getProperties();
			// the service name should be same as the interface project name
			final String serviceName = projectName;
			final String implProjectName = props
					.getProperty(SOAMavenConstants.POM_PROP_KEY_IMPL_PROJECT_NAME);
			Properties metadataProps = null;
			InputStream io = null;
			try {
				final String jarEntryPath = StringUtil.toString(
						SOAProjectConstants.FOLDER_META_INF,
						SOAIntfProject.FOLDER_SOA_COMMON_CONFIG,
						WorkspaceUtil.PATH_SEPERATOR, serviceName,
						WorkspaceUtil.PATH_SEPERATOR,
						SOAProjectConstants.PROPS_FILE_SERVICE_METADATA);
				io = getInputStreamFromJar(mProject, jarEntryPath);
				metadataProps = new Properties();
				metadataProps.load(io);
			} finally {
				IOUtils.closeQuietly(io);
			}

			result = createProjectInfoFromMetadataProps(serviceName,
					metadataProps);

			if (result != null) {
				result.setImplementationProjectName(implProjectName);
				/*
				 * if (StringUtils.isNotBlank(implProjectName))
				 * result.setImplementationProjectName(implProjectName); else {
				 * //TODO the impl project name might not be available for those
				 * project created in the old SOA plugin
				 * result.setImplementationProjectName(serviceName +
				 * SOAProjectConstants.IMPL_PROJECT_SUFFIX); logger.warning(
				 * "Could not load the impl project name from the pom.xml of service->"
				 * , projectName,
				 * ", using the default logic with \"Impl\" as suffix ->",
				 * result.getImplementationProjectName()); }
				 */
				processDependencies(mProject.getModel(), result);
			}
		}

		if (SOALogger.DEBUG)
			logger.exiting(result);
		return result;
	}

	private static MavenAssetInfo getLibraryInfo(final MavenProject mProject) {
		File jarFile = null;
		if (WorkspaceUtil.getProject(mProject.getArtifactId()).isAccessible() == false) {
			jarFile = getJarFileForService(mProject);
		}
		final String dir = jarFile != null && jarFile.exists() ? jarFile
				.getParent() : "";
		final MavenAssetInfo assetInfo = new MavenAssetInfo(
				mProject.getGroupId(), mProject.getArtifactId(),
				mProject.getVersion(), dir, null, AssetInfo.TYPE_LIBRARY);
		if (jarFile != null && StringUtils.isNotBlank(dir))
			assetInfo.setJarNames(ListUtil.array(jarFile.getName()));
		return assetInfo;
	}

	private static MavenProjectInfo getImplProjectInfoFromProperties(
			final String implProjectName, final Properties props,
			final Model pom) throws MavenEclipseApiException {
		final String serviceName = props
				.getProperty(SOAMavenConstants.POM_PROP_KEY_SERVICE_NAME);
		String serviceVersion = null;
		final String requiredServices = props
				.getProperty(SOAMavenConstants.POM_PROP_KEY_REQUIRED_SERVICES);
		// the service groupId will be set to the service layer field
		final String serviceGroupID = props
				.getProperty(SOAMavenConstants.POM_PROP_KEY_SERVICE_GROUP_ID);
		if (StringUtils.isBlank(serviceVersion) && pom != null) {
			final Dependency dependency = findDependency(serviceGroupID,
					serviceName, pom);
			if (dependency != null)
				serviceVersion = dependency.getVersion();
		}
		// the impl project does not have interface, service layer and type
		final MavenProjectInfo projectInfo = new MavenProjectInfo(
				getMavenOrgProviderInstance().getProjectGroupId(
						SupportedProjectType.IMPL), implProjectName,
				serviceVersion, null);
		projectInfo.setServiceGroupID(serviceGroupID);
		// projectInfo.setServiceName(serviceName);
		projectInfo.setInterfaceProjectName(serviceName);
		if (StringUtils.isNotBlank(requiredServices)) {
			projectInfo.getRequiredServices().addAll(
					SetUtil.set(StringUtils.split(requiredServices,
							SOAProjectConstants.DELIMITER_COMMA)));
		}
		processDependencies(pom, projectInfo);
		return projectInfo;
	}

	private static MavenProjectInfo getConsumerProjectInfoFromProperties(
			final String consumerProjectName, final Properties props,
			final Model pom) throws MavenEclipseApiException {
		final String requiredServices = props
				.getProperty(SOAMavenConstants.POM_PROP_KEY_REQUIRED_SERVICES);
		// the consumer project does not have interface, service layer and type
		final MavenProjectInfo projectInfo = new MavenProjectInfo(
				getMavenOrgProviderInstance().getProjectGroupId(
						SupportedProjectType.CONSUMER), consumerProjectName,
				pom.getVersion(), null);
		if (StringUtils.isNotBlank(requiredServices)) {
			projectInfo.getRequiredServices().addAll(
					SetUtil.set(StringUtils.split(requiredServices,
							SOAProjectConstants.DELIMITER_COMMA)));
		}
		processDependencies(pom, projectInfo);
		return projectInfo;
	}

	public static Dependency findDependency(final String groupID,
			final String artifactID, final Model pom) {
		for (final Object obj : pom.getDependencies()) {
			if (obj instanceof Dependency) {
				final Dependency dependency = (Dependency) obj;
				if (dependency.getGroupId().equals(groupID)
						&& dependency.getArtifactId().equals(artifactID)) {
					return dependency;
				}
			}
		}
		return null;
	}

	/**
	 * use jar as the library type
	 * 
	 * @param groupID
	 * @param artifactID
	 * @param version
	 * @return
	 */
	public static String translateLibraryName(final String groupID,
			final String artifactID, final String version) {
		return translateLibraryName(groupID, artifactID,
				SOAMavenConstants.MAVEN_PACKAGING_JAR, version);
	}

	public static String translateLibraryName(final Dependency dependency) {
		return translateLibraryName(dependency.getGroupId(),
				dependency.getArtifactId(), dependency.getType(),
				dependency.getVersion());
	}

	public static String translateInterfaceLibraryName(
			final String serviceName, final String serviceVersion) {
		return translateLibraryName(getMavenOrgProviderInstance()
				.getProjectGroupId(SupportedProjectType.INTERFACE),
				serviceName, serviceVersion);
	}

	public static String translateLibraryName(final String groupID,
			final String artifactID, final String libraryType,
			final String version) {
		final StringBuffer buf = new StringBuffer();
		buf.append(groupID);
		buf.append(SOAProjectConstants.DELIMITER_SEMICOLON);
		buf.append(artifactID);
		buf.append(SOAProjectConstants.DELIMITER_SEMICOLON);
		buf.append(libraryType != null ? libraryType
				: SOAMavenConstants.MAVEN_PACKAGING_JAR);
		if (version != null) {
			buf.append(SOAProjectConstants.DELIMITER_SEMICOLON);
			buf.append(version);
		}
		return buf.toString();
	}

	public static MavenProjectInfo getInterfaceProjectInfo(
			final IProject intfProject) throws Exception {
		final Model pom = MavenEclipseUtil.readPOM(intfProject);
		if (pom == null)
			return null;

		final Properties props = pom.getProperties();
		// the service name should be same as the interface project name
		final String serviceName = intfProject.getName();
		final String implProjectName = props
				.getProperty(SOAMavenConstants.POM_PROP_KEY_IMPL_PROJECT_NAME);

		Properties metadataProps = SOAIntfUtil.loadMetadataProps(intfProject,
				serviceName);
		final MavenProjectInfo result = createProjectInfoFromMetadataProps(
				serviceName, metadataProps);
		if (result != null) {
			result.setImplementationProjectName(implProjectName);
			/*
			 * if (StringUtils.isNotBlank(implProjectName))
			 * result.setImplementationProjectName(implProjectName); else {
			 * //TODO the impl project name might not be available for those
			 * project created in the old SOA plugin
			 * result.setImplementationProjectName(serviceName +
			 * SOAProjectConstants.IMPL_PROJECT_SUFFIX); logger.warning(
			 * "Could not load the impl project name from the pom.xml of service->"
			 * , serviceName,
			 * ", using the default logic with \"Impl\" as suffix ->",
			 * result.getImplementationProjectName()); }
			 */

			processDependencies(pom, result);
		}
		return result;
	}

	private static MavenProjectInfo createProjectInfoFromMetadataProps(
			final String serviceName, final Properties metadataProps)
			throws Exception {
		MavenProjectInfo result = null;

		if (metadataProps != null) {
			// TODO we still need the service layer and version info for
			// projects
			final String serviceLayer = metadataProps
					.getProperty(SOAProjectConstants.SERVICE_METADATA_PROPS_SERVICE_LAYER);
			final String serviceVersion = metadataProps
					.getProperty(SOAProjectConstants.SERVICE_METADATA_PROPS_SERVICE_VERSION);

			result = new MavenProjectInfo(getMavenOrgProviderInstance()
					.getProjectGroupId(SupportedProjectType.INTERFACE),
					serviceName, serviceVersion, serviceLayer);
			result.setType(AssetInfo.TYPE_SERVICE_LIBRARY);
			result.setInterfaceProjectName(serviceName);
		}
		return result;
	}

	public static MavenProjectInfo getImplementationProjectInfo(
			final IProject implProject) throws CoreException,
			MavenEclipseApiException {
		final Model pom = MavenEclipseUtil.readPOM(implProject);
		if (pom == null)
			return null;
		return getImplProjectInfoFromProperties(implProject.getName(),
				pom.getProperties(), pom);
	}

	public static MavenProjectInfo getConsumerProjectInfo(
			final IProject consumerProject) throws CoreException,
			MavenEclipseApiException {
		final Model pom = MavenEclipseUtil.readPOM(consumerProject);
		if (pom == null)
			return null;
		final String requiredServices = pom.getProperties().getProperty(
				SOAProjectConstants.PROP_REQUIRED_SERVICES);
		final MavenProjectInfo projectInfo = new MavenProjectInfo(
				getMavenOrgProviderInstance().getProjectGroupId(
						SupportedProjectType.CONSUMER),
				consumerProject.getName(), pom.getVersion(), null);

		if (StringUtils.isNotBlank(requiredServices)) {
			projectInfo.getRequiredServices().addAll(
					SetUtil.set(StringUtils.split(requiredServices,
							SOAProjectConstants.DELIMITER_COMMA)));
		}
		processDependencies(pom, projectInfo);
		return projectInfo;
	}

	/**
	 * for those lib projects, whose every dependencies are lib dependencies
	 * 
	 * @param libProject
	 * @param groupID
	 * @return
	 * @throws CoreException
	 * @throws MavenEclipseApiException
	 */
	private static MavenProjectInfo getLibraryProjectInfo(
			final IProject libProject, String groupID) throws CoreException,
			MavenEclipseApiException {
		// for type lib projects, every dependencies are lib dependencies
		final Model pom = MavenEclipseUtil.readPOM(libProject);
		if (pom == null)
			return null;

		final MavenProjectInfo projectInfo = new MavenProjectInfo(groupID,
				libProject.getName(), pom.getVersion(), null);

		processDependencies(pom, projectInfo);
		return projectInfo;
	}

	public static MavenProjectInfo getTypeLibraryProjectInfo(
			final IProject typelibProject) throws CoreException,
			MavenEclipseApiException {
		return getLibraryProjectInfo(
				typelibProject,
				getMavenOrgProviderInstance().getProjectGroupId(
						SupportedProjectType.TYPE_LIBRARY));
	}

	public static MavenProjectInfo getErrorLibraryProjectInfo(
			final IProject typelibProject) throws CoreException,
			MavenEclipseApiException {
		return getLibraryProjectInfo(
				typelibProject,
				getMavenOrgProviderInstance().getProjectGroupId(
						SupportedProjectType.ERROR_LIBRARY));
	}

	private static List<AssetInfo> processDependencies(final Model pom,
			final MavenProjectInfo projectInfo) throws MavenEclipseApiException {
		final List<AssetInfo> result = new ArrayList<AssetInfo>();
		final String requiredServices = pom.getProperties().getProperty(
				SOAProjectConstants.PROP_REQUIRED_SERVICES);
		final Set<String> requiredServicesList = SetUtil.set(StringUtils.split(
				requiredServices, SOAProjectConstants.DELIMITER_COMMA));
		for (final Object obj : pom.getDependencies()) {
			if (obj instanceof Dependency) {
				final Dependency dependency = (Dependency) obj;
				final String groupID = dependency.getGroupId();
				if (requiredServicesList.contains(dependency.getArtifactId())) {
					if (projectInfo != null)
						projectInfo.getRequiredServices().add(
								dependency.getArtifactId());
					result.add(new MavenAssetInfo(dependency.getGroupId(),
							dependency.getArtifactId(),
							dependency.getVersion(), "", null,
							AssetInfo.TYPE_SERVICE_LIBRARY));
				} else {
					final String fullLibName = MavenCoreUtils
							.translateLibraryName(dependency);
					final IProject project = WorkspaceUtil
							.getProject(dependency.getArtifactId());
					if (project != null && project.isAccessible()) {
						// this is a project in the current workspace
						if (projectInfo != null)
							projectInfo.getRequiredProjects().add(
									dependency.getArtifactId());
						result.add(new MavenProjectInfo(
								dependency.getGroupId(), dependency
										.getArtifactId(), dependency
										.getVersion(), ""));
					} else if (getMavenOrgProviderInstance().getProjectGroupId(
							SupportedProjectType.INTERFACE).equals(groupID)) {
						// service library
						if (projectInfo != null)
							projectInfo.getRequiredServices().add(
									dependency.getArtifactId());
						result.add(new MavenAssetInfo(dependency.getGroupId(),
								dependency.getArtifactId(), dependency
										.getVersion(), "", null,
								AssetInfo.TYPE_SERVICE_LIBRARY));
					} else if (getMavenOrgProviderInstance().getProjectGroupId(
							SupportedProjectType.IMPL).equals(groupID)) {
						// project
						if (projectInfo != null)
							projectInfo.getRequiredProjects().add(
									dependency.getArtifactId());
						result.add(new MavenProjectInfo(
								dependency.getGroupId(), dependency
										.getArtifactId(), dependency
										.getVersion(), ""));
					} else if (getMavenOrgProviderInstance().getProjectGroupId(
							SupportedProjectType.CONSUMER).equals(groupID)) {
						// project
						if (projectInfo != null)
							projectInfo.getRequiredLibraries().add(fullLibName);
						result.add(new MavenProjectInfo(
								dependency.getGroupId(), dependency
										.getArtifactId(), dependency
										.getVersion(), ""));
					} else if (SOAMavenConstants.SOA_FRAMEWORK_GROUPID
							.equals(groupID)) {
						// library
						if (projectInfo != null)
							projectInfo.getRequiredLibraries().add(fullLibName);
						result.add(new MavenAssetInfo(dependency.getGroupId(),
								dependency.getArtifactId(), dependency
										.getVersion(), "", null,
								AssetInfo.TYPE_LIBRARY));
					} else {
						if (projectInfo != null)
							projectInfo.getRequiredLibraries().add(fullLibName);
						result.add(new MavenAssetInfo(dependency.getGroupId(),
								dependency.getArtifactId(), dependency
										.getVersion(), "", null,
								AssetInfo.TYPE_LIBRARY));
					}
				}
			}
		}
		return result;
	}

	public static MavenProjectInfo getProjectInfo(final String projectName)
			throws Exception {
		final IProject project = WorkspaceUtil.getProject(projectName);
		if (project != null && project.isAccessible()) {
			return getProjectInfo(project);
		} else {
			// the project does not exist, let's find it from the repository
			MavenProjectInfo projectInfo = getProjectInfo(
					getMavenOrgProviderInstance().getProjectGroupId(
							SupportedProjectType.INTERFACE), projectName, null);
			if (projectInfo != null)
				return projectInfo;

			projectInfo = getProjectInfo(getMavenOrgProviderInstance()
					.getProjectGroupId(SupportedProjectType.IMPL), projectName,
					null);
			if (projectInfo != null)
				return projectInfo;

			projectInfo = getProjectInfo(getMavenOrgProviderInstance()
					.getProjectGroupId(SupportedProjectType.CONSUMER),
					projectName, null);
			if (projectInfo != null)
				return projectInfo;

			projectInfo = getProjectInfo(getMavenOrgProviderInstance()
					.getProjectGroupId(SupportedProjectType.TYPE_LIBRARY),
					projectName, null);
			if (projectInfo != null)
				return projectInfo;

			projectInfo = getProjectInfo(
					SOAMavenConstants.SOA_FRAMEWORK_GROUPID, projectName, null);
			if (projectInfo != null)
				return projectInfo;
		}
		return null;
	}

	public static MavenProjectInfo getProjectInfo(final String groupID,
			final String projectName, final String version) throws Exception {
		final String libVersion = version != null ? version : MavenCoreUtils
				.getLibraryVersion(groupID, projectName,
						SOAProjectConstants.DEFAULT_SERVICE_VERSION);
		final String fullLibName = MavenCoreUtils.translateLibraryName(groupID,
				projectName, libVersion);
		final ArtifactMetadata artifact = getLibraryIdentifier(fullLibName); // TODO
		if (artifact != null) {
			final MavenProject mProject = MavenCoreUtils.getLibrary(artifact);
			if (getMavenOrgProviderInstance().getProjectGroupId(
					SupportedProjectType.INTERFACE).equals(groupID)) {
				return MavenCoreUtils.getIntfProjectInfoFromProperties(
						projectName, mProject);
			} else if (getMavenOrgProviderInstance().getProjectGroupId(
					SupportedProjectType.IMPL).equals(groupID)) {
				return MavenCoreUtils.getImplProjectInfoFromProperties(
						projectName, mProject.getProperties(),
						mProject.getModel());
			} else if (getMavenOrgProviderInstance().getProjectGroupId(
					SupportedProjectType.CONSUMER).equals(groupID)) {
				return MavenCoreUtils.getConsumerProjectInfoFromProperties(
						projectName, mProject.getProperties(),
						mProject.getModel());
			} else if (getMavenOrgProviderInstance().getProjectGroupId(
					SupportedProjectType.TYPE_LIBRARY).equals(groupID)) {
				// TODO return type library project info
				// return MavenUtil.getTypeLibraryInfo(mProject);
			} else if (SOAMavenConstants.SOA_FRAMEWORK_GROUPID.equals(groupID)) {
				// this could be either the soa framework jars or the client
				// projects
				// return MavenUtil.get(projectName, mProject.getProperties(),
				// mProject.getModel());
			}
		}

		return null;
	}

	public static MavenProjectInfo getProjectInfo(final IProject soaProject)
			throws Exception {
		if (TurmericServiceUtils.isSOAInterfaceProject(soaProject)) {
			return getInterfaceProjectInfo(soaProject);
		} else if (TurmericServiceUtils.isSOAImplProject(soaProject)) {
			return getImplementationProjectInfo(soaProject);
		} else if (TurmericServiceUtils.isSOAConsumerProject(soaProject)) {
			return getConsumerProjectInfo(soaProject);
		} else if (TurmericServiceUtils.isSOATypeLibraryProject(soaProject)) {
			return getTypeLibraryProjectInfo(soaProject);
		} else if (TurmericServiceUtils.isSOAErrorLibraryProject(soaProject)) {
			return getErrorLibraryProjectInfo(soaProject);
		}

		return null;
	}

	public static List<AssetInfo> getDependencies(final String projectName)
			throws CoreException, MavenEclipseApiException {
		final List<AssetInfo> result = new ArrayList<AssetInfo>();
		final IProject project = WorkspaceUtil.getProject(projectName);
		if (project != null && project.isAccessible()) {
			final Model pom = MavenEclipseUtil.readPOM(project);
			if (pom != null)
				result.addAll(processDependencies(pom, null));
		} else {
			// the project does not exist, let's find it from the repository
			final MavenProject mProject = MavenCoreUtils
					.getLibrary(getLibraryIdentifier(projectName));
			final Model pom = mProject.getModel();
			if (pom != null)
				result.addAll(processDependencies(pom, null));
		}
		return result;
	}

	private static boolean updateIntfProjectInfo(
			final SOAIntfProject intfProject) throws CoreException,
			MavenEclipseApiException {
		final SOAIntfMetadata intfMetadata = intfProject.getMetadata();
		final String serviceVersion = intfMetadata.getServiceVersion();
		final IFile pomFile = MavenEclipseUtil.getPomFile(intfProject
				.getProject());
		pomFile.refreshLocal(IResource.DEPTH_ZERO, null);
		final Model pom = MavenEclipseUtil.readPOM(intfProject.getProject());
		if (pom == null)
			return false;

		Model implModel = null;
		IFile implPomFile = null;
		if (serviceVersion.equals(pom.getVersion()) == false) {
			pom.setVersion(serviceVersion);
			if (intfMetadata.getImlementationProjectName() != null) {
				final IProject implProject = WorkspaceUtil
						.getProject(intfMetadata.getImlementationProjectName());
				if (implProject != null && implProject.isAccessible()) {
					implPomFile = MavenEclipseUtil.getPomFile(implProject);
					implModel = MavenEclipseUtil.readPOM(implProject);
					if (implModel != null) {
						implModel.setVersion(serviceVersion);
						final Dependency dependency = findDependency(
								pom.getGroupId(),
								intfMetadata.getServiceName(), implModel);
						if (dependency != null) {
							dependency.setVersion(serviceVersion);
						}
					}
				}
			}
		}

		// we dont update the dependencies in here, use addDependency() instead
		// processDependencies(intfProject, pom);
		mavenEclipseAPI().writePom(pomFile, pom);
		if (implModel != null && implPomFile != null) {
			mavenEclipseAPI().writePom(implPomFile, implModel);
		}
		return true;
	}

	private static boolean updateImplProjectInfo(
			final SOAImplProject implProject) throws CoreException,
			MavenEclipseApiException {
		final SOAImplMetadata implMetadata = implProject.getMetadata();
		final String implVersion = implMetadata.getImplVersion();
		final IFile pomFile = MavenEclipseUtil.getPomFile(implProject
				.getProject());
		pomFile.refreshLocal(IResource.DEPTH_ZERO, null);
		final Model pom = MavenEclipseUtil.readPOM(implProject.getProject());
		if (pom == null)
			return false;

		pom.setVersion(implVersion);
		// we dont update the dependencies in here, use addDependency() instead
		// processDependencies(implProject, pom);
		mavenEclipseAPI().writePom(pomFile, pom);
		return true;
	}

	private static boolean updateConsumerProjectInfo(
			final SOAConsumerProject consumerProject) throws CoreException,
			MavenEclipseApiException {
		final IFile pomFile = MavenEclipseUtil.getPomFile(consumerProject
				.getProject());
		pomFile.refreshLocal(IResource.DEPTH_ZERO, null);
		final Model pom = MavenEclipseUtil
				.readPOM(consumerProject.getProject());
		if (pom == null)
			return false;
		// we dont update the dependencies in here, use addDependency() instead
		// processDependencies(consumerProject, pom);

		mavenEclipseAPI().writePom(pomFile, pom);
		return true;
	}

	public static boolean updateProjectInfo(final ISOAProject soaProject)
			throws CoreException, MavenEclipseApiException {
		final Model pom = MavenEclipseUtil.readPOM(soaProject.getProject());
		if (pom == null)
			return false;
		if (soaProject instanceof SOAIntfProject) {
			return updateIntfProjectInfo((SOAIntfProject) soaProject);
		} else if (soaProject instanceof SOAImplProject) {
			return updateImplProjectInfo((SOAImplProject) soaProject);
		} else if (soaProject instanceof SOAConsumerProject) {
			return updateConsumerProjectInfo((SOAConsumerProject) soaProject);
		}
		return false;
	}


	/**
	 * @param pom
	 * @param serviceName
	 * @param addRemove
	 *            Add=true, Remove=false
	 */
	public static void modifyRequiredServices(final Model pom,
			final String serviceName, final boolean addRemove) {
		final Properties props = pom.getProperties();
		final String requiredServices = props
				.getProperty(SOAMavenConstants.POM_PROP_KEY_REQUIRED_SERVICES);
		final Set<String> requiredServicesList = SetUtil.treeSet(StringUtils
				.split(requiredServices, SOAProjectConstants.DELIMITER_COMMA));
		if (addRemove) {
			// adding a new required service
			requiredServicesList.add(serviceName);
		} else {
			// removing
			requiredServicesList.remove(serviceName);
		}
		props.setProperty(SOAMavenConstants.POM_PROP_KEY_REQUIRED_SERVICES,
				StringUtils.join(requiredServicesList,
						SOAProjectConstants.DELIMITER_COMMA));
	}

	public static void updateMavenClasspathContainer(final IProject project,
			final String dependentName, final String type)
			throws CoreException, InterruptedException {
		final IJavaProject javaProject = (IJavaProject) project
				.getNature(JavaCore.NATURE_ID);
		final IPath containerPath = new Path(
				SOAMavenConstants.MAVEN_CLASSPATH_CONTAINER_ID);
		final ClasspathContainerInitializer containerInitializer = JavaCore
				.getClasspathContainerInitializer(SOAMavenConstants.MAVEN_CLASSPATH_CONTAINER_ID);

		if (containerInitializer.canUpdateClasspathContainer(containerPath,
				javaProject)) {
			final IClasspathContainer mavenContainer = getMaven2ClasspathContainer(javaProject);
			containerInitializer.requestClasspathContainerUpdate(containerPath,
					javaProject, mavenContainer);
			containerInitializer.initialize(containerPath, javaProject);
			/*
			 * project.build(IncrementalProjectBuilder.FULL_BUILD,
			 * ProgressUtil.getDefaultMonitor(null));
			 */
			// TODO this is a hot fix for the Maven class path issue
			// it will continuously wait until the newly added service is
			// available in the classpath
			String tlGroupId = getMavenOrgProviderInstance().getProjectGroupId(
					SupportedProjectType.TYPE_LIBRARY);
			if (AssetInfo.TYPE_SERVICE_LIBRARY.equals(type)) {
				waitForClasspathContainerToUpdate(getMavenOrgProviderInstance()
						.getProjectGroupId(SupportedProjectType.INTERFACE),
						javaProject, dependentName);
			} else if (tlGroupId.equals(type)) {
				waitForClasspathContainerToUpdate(tlGroupId, javaProject,
						dependentName);
			}
		}
	}

	private static void waitForClasspathContainerToUpdate(String groupID,
			IJavaProject javaProject, String dependentName)
			throws JavaModelException, InterruptedException {
		IClasspathContainer container = getMaven2ClasspathContainer(javaProject);
		if (container == null)
			return;
		int retry = 0;
		int maxRetry = 4;
		Outer: while (retry < maxRetry) {
			for (IClasspathEntry entry : container.getClasspathEntries()) {
				if (matchEntry(entry, groupID, dependentName)) {
					break Outer;
				}
			}
			logger.warning("can not find the classpath entry->", dependentName,
					" from the Maven classpath container->",
					SOAMavenConstants.MAVEN_CLASSPATH_CONTAINER_ID,
					", going to sleep...");
			Thread.sleep(2000);
			retry++;
		}
		if (retry < maxRetry) {
			logger.info("Found the classpath entry->", dependentName);
		}
	}

	public static IClasspathContainer getMaven2ClasspathContainer(
			IJavaProject project) throws JavaModelException {
		IClasspathEntry[] entries = project.getRawClasspath();
		for (int i = 0; i < entries.length; i++) {
			IClasspathEntry entry = entries[i];
			if (entry.getEntryKind() == IClasspathEntry.CPE_CONTAINER
					&& isMaven2ClasspathContainer(entry.getPath())) {
				return JavaCore.getClasspathContainer(entry.getPath(), project);
			}
		}
		return null;
	}

	public static boolean isMaven2ClasspathContainer(IPath containerPath) {
		return containerPath != null
				&& containerPath.segmentCount() > 0
				&& SOAMavenConstants.MAVEN_CLASSPATH_CONTAINER_ID
						.equals(containerPath.segment(0));
	}

	private static boolean matchEntry(IClasspathEntry entry,
			final String groupID, final String artifactID) {
		boolean matchGroup = false;
		boolean matchArtifact = false;
		for (IClasspathAttribute attr : entry.getExtraAttributes()) {
			if (attr.getName().equals("maven.groupId")) {
				if (groupID.equals(attr.getValue()) == false)
					return false;
				else
					matchGroup = true;
			} else if (attr.getName().equals("maven.artifactId")) {
				if (artifactID.equals(attr.getValue()) == false)
					return false;
				else
					matchArtifact = true;
			}
			if (matchGroup && matchArtifact) {
				logger.info("Found classpath entry: ", entry);
				return true;
			}
		}
		return false;
	}

}
