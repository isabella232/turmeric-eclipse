package org.ebayopensource.turmeric.eclipse.utils.ui;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.ebayopensource.turmeric.eclipse.core.logging.SOALogger;
import org.ebayopensource.turmeric.eclipse.core.resources.constants.SOAProjectConstants;
import org.ebayopensource.turmeric.eclipse.utils.plugin.ProgressUtil;
import org.ebayopensource.turmeric.eclipse.utils.plugin.WorkspaceUtil;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;

/**
 * The Class ProjectUtils.
 */
public class ProjectUtils {

	private static final SOALogger logger = SOALogger.getLogger();
	
	/**
	 * Checks if is project good for consumption.
	 *
	 * @param projectNames the project names
	 * @return true, if is project good for consumption
	 */
	public static boolean isProjectGoodForConsumption(String... projectNames) {

		if (!ResourcesPlugin.getWorkspace().isAutoBuilding()) {
			final List<String> serviceNames = new ArrayList<String>();
			ArrayList<IProject> projectsToBeBuilt = new ArrayList<IProject>();
			for (String projectName : projectNames) {
				if (WorkspaceUtil.getProject(projectName).exists()) {
					serviceNames.add(projectName);
					projectsToBeBuilt.add(WorkspaceUtil.getProject(projectName));
				}
			}
			final String serviceNameList = StringUtils.join(serviceNames, 
					SOAProjectConstants.DELIMITER_COMMA);
			if (!projectsToBeBuilt.isEmpty()) {
				if (UIUtil
						.openChoiceDialog(
								"Cannot consume the selected service",
								"You have set build automatically off. These projects should be built before it could be consumed ["
										+ serviceNameList 
										+ "].\n\n Click Ok to build it or otherwise Cancel.",
								IStatus.ERROR)) {
					for (IProject project : projectsToBeBuilt) {
						try {
							project.build(IncrementalProjectBuilder.FULL_BUILD,
									ProgressUtil.getDefaultMonitor(null));
						} catch (CoreException e) {
							logger.warning(e);
						}
					}
					return true;
				} else {
					return false;
				}
			}

		}
		return true;
	}	
}
