package org.ebayopensource.turmeric.eclipse.maven.sconfig;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.maven.plugin.MojoExecution;
import org.ebayopensource.turmeric.eclipse.utils.plugin.JDTUtil;
import org.eclipse.core.internal.resources.Project;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.m2e.core.MavenPlugin;
import org.eclipse.m2e.core.embedder.IMaven;
import org.eclipse.m2e.core.internal.project.registry.MavenProjectFacade;
import org.eclipse.m2e.core.project.IMavenProjectFacade;
import org.eclipse.m2e.core.project.configurator.MojoExecutionBuildParticipant;
import org.sonatype.plexus.build.incremental.BuildContext;

/**
 * The Class TurmericStandardBuildParticipant.
 */
public class TurmericStandardBuildParticipant extends
		MojoExecutionBuildParticipant {

	/**
	 * Instantiates a new turmeric standard build participant.
	 * 
	 * @param execution
	 *            the execution
	 */
	public TurmericStandardBuildParticipant(MojoExecution execution) {
		super(execution, true);
	}

	@Override
	public Set<IProject> build(int kind, IProgressMonitor monitor)
			throws Exception {

		BuildContext buildContext = getBuildContext();
		IMavenProjectFacade mproj = getMavenProjectFacade();
		
		Set<IProject> sproj = super.build(kind, monitor);

		IProject proj = mproj.getProject();
		
		proj.refreshLocal(Project.DEPTH_INFINITE, monitor);

		IFile generatedSource = proj.getFile("target/generated-sources");
			File generatedSourceFolder = generatedSource.getFullPath().toFile();
			buildContext.refresh(generatedSourceFolder);

		IFile generatedResource = proj.getFile("target/generated-sources");
			File generatedResourceFolder = generatedResource.getFullPath()
					.toFile();
			buildContext.refresh(generatedResourceFolder);

		return sproj;

	}

}
