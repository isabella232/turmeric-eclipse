package org.ebayopensource.turmeric.eclipse.maven.sconfig;

import java.util.Set;

import org.apache.maven.plugin.MojoExecution;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.m2e.core.project.configurator.MojoExecutionBuildParticipant;

/**
 * The Class TurmericStandardBuildParticipant.
 */
public class TurmericStandardBuildParticipant extends
		MojoExecutionBuildParticipant {

	/**
	 * Instantiates a new turmeric standard build participant.
	 *
	 * @param execution the execution
	 */
	public TurmericStandardBuildParticipant(MojoExecution execution) {
		super(execution, true);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.m2e.core.project.configurator.MojoExecutionBuildParticipant#build(int, org.eclipse.core.runtime.IProgressMonitor)
	 */
	@Override
	public Set<IProject> build(int kind, IProgressMonitor monitor)
			throws Exception {

		// execute mojo
		return super.build(kind, monitor);
	}

}
