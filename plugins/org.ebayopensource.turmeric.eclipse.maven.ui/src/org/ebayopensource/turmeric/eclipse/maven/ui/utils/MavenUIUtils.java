package org.ebayopensource.turmeric.eclipse.maven.ui.utils;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.maven.model.Dependency;
import org.apache.maven.model.Model;
import org.apache.maven.project.MavenProject;
import org.apache.maven.repository.metadata.ArtifactMetadata;
import org.ebayopensource.turmeric.eclipse.core.logging.SOALogger;
import org.ebayopensource.turmeric.eclipse.core.resources.constants.SOAProjectConstants;
import org.ebayopensource.turmeric.eclipse.core.resources.constants.SOAProjectConstants.SupportedProjectType;
import org.ebayopensource.turmeric.eclipse.maven.core.utils.MavenCoreUtils;
import org.ebayopensource.turmeric.eclipse.mavenapi.exception.MavenEclipseApiException;
import org.ebayopensource.turmeric.eclipse.mavenapi.impl.MavenEclipseUtil;
import org.ebayopensource.turmeric.eclipse.resources.model.AssetInfo;
import org.ebayopensource.turmeric.eclipse.utils.lang.StringUtil;
import org.ebayopensource.turmeric.eclipse.utils.plugin.WorkspaceUtil;
import org.ebayopensource.turmeric.eclipse.utils.ui.UIUtil;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.widgets.Shell;

/**
 * The Class MavenUIUtils.
 *
 * @since 1.0
 */
public class MavenUIUtils {
	
	private static final SOALogger logger = SOALogger.getLogger();	

	/**
	 * Adds a dependency to a project.
	 *
	 * @param project the eclipse project
	 * @param dependentLibraries dependency libraries to add
	 * @param addRemove  add = true, remove = false
	 * @param monitor an eclipse progress monitor
	 * @return returns true or false if the operation succeeded.
	 * @throws CoreException the core exception
	 * @throws MavenEclipseApiException the maven eclipse api exception
	 */
	public static boolean addDependency(final IProject project,
			Map<String, String> dependentLibraries, final boolean addRemove,
			IProgressMonitor monitor) throws CoreException,
			MavenEclipseApiException {
		ArtifactMetadata metadata = null;
		final IFile pomFile = MavenEclipseUtil.getPomFile(project);
		pomFile.refreshLocal(IResource.DEPTH_ZERO, monitor);
		final Model pom = MavenEclipseUtil.readPOM(project);
		if (pom == null)
			return false;

		for (String dependentName : dependentLibraries.keySet()) {
			final String type = dependentLibraries.get(dependentName);
			if (StringUtils.equals(type, AssetInfo.TYPE_SERVICE_LIBRARY)) {
				// service dependency
				String groupId = MavenCoreUtils.getMavenOrgProviderInstance()
						.getProjectGroupId(SupportedProjectType.INTERFACE);
				final String libVersion = MavenCoreUtils.getLibraryVersion(
						groupId, dependentName,
						SOAProjectConstants.DEFAULT_SERVICE_VERSION);
				final String fullLibName = MavenCoreUtils.translateLibraryName(
						groupId, dependentName, libVersion);
				metadata = MavenCoreUtils.getLibraryIdentifier(fullLibName);
			} else if (StringUtils.equals(type, AssetInfo.TYPE_PROJECT)) {
				final IProject dependentProject = WorkspaceUtil
						.getProject(dependentName);
				if (dependentProject != null && dependentProject.isAccessible()) {
					final Model dependentPom = MavenEclipseUtil
							.readPOM(dependentProject);
					if (dependentPom == null)
						return false;
					metadata = MavenEclipseUtil.artifactMetadata(dependentPom);
				} else if (dependentName
						.contains(SOAProjectConstants.DELIMITER_SEMICOLON)) {
					// it is Maven fully qualified identifier
					logger.warning(
							"library name to add is a fully qualified identifier->",
							dependentName);
					metadata = MavenEclipseUtil.artifactMetadata(dependentName);
				}
			} else if (StringUtils.equals(type, AssetInfo.TYPE_LIBRARY)) {
				metadata = MavenCoreUtils.getLibraryIdentifier(dependentName);
				if (metadata == null
						&& WorkspaceUtil.getProject(dependentName)
								.isAccessible()) {
					// could not find the lib, but exist as a project in the
					// workspace
					MavenProject mProj = MavenCoreUtils.getMavenProject(WorkspaceUtil
							.getProject(dependentName));
					if (mProj != null) {
						metadata = MavenEclipseUtil
								.convertToArtifactMetadata(mProj.getModel());
					}
				}
			} else {
				metadata = MavenCoreUtils.getLibraryIdentifier(dependentName);
			}

			if (metadata == null) {
				if (addRemove == true) {
					// we only report failed adding operation
					final String errMsg = StringUtil.toString(
							"Failed to add dependency->", dependentName,
							" to project->", project);
					logger.warning(errMsg);
					UIUtil.showErrorDialogInNewThread((Shell) null,
							"Error Occured", errMsg);
				}

				return false;
			}
			Dependency dependency = null;
			if (addRemove) {
				// adding a new dependency
				dependency = MavenEclipseUtil.dependency(metadata);
				if (dependency == null || dependency.getGroupId() == null)
					return false;
				if (MavenCoreUtils.findDependency(dependency.getGroupId(),
						dependency.getArtifactId(), pom) != null) {
					logger.warning("Dependency has already been added skipping it->"
							+ dependency);
					return false;
				}
				pom.addDependency(dependency);
			} else {
				// removing an existing dependency
				dependency = MavenCoreUtils.findDependency(metadata.getGroupId(),
						metadata.getArtifactId(), pom);
				if (dependency == null)
					return false;
				pom.removeDependency(dependency);
			}

			if (StringUtils.equals(type, AssetInfo.TYPE_SERVICE_LIBRARY)) {
				MavenCoreUtils.modifyRequiredServices(pom, dependentName, addRemove);
			}
		}

		MavenCoreUtils.mavenEclipseAPI().writePom(pomFile, pom);
		pomFile.refreshLocal(IResource.DEPTH_ZERO, monitor);
		return true;
	}

}
