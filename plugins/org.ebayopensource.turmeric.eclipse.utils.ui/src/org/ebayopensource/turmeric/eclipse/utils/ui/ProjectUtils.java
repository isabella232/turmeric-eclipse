package org.ebayopensource.turmeric.eclipse.utils.ui;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.jar.Attributes;
import java.util.jar.Manifest;
import java.util.jar.Attributes.Name;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.lang.StringUtils;
import org.ebayopensource.turmeric.eclipse.core.logging.SOALogger;
import org.ebayopensource.turmeric.eclipse.core.resources.constants.SOAProjectConstants;
import org.ebayopensource.turmeric.eclipse.exception.resources.SOAResourceNotAccessibleException;
import org.ebayopensource.turmeric.eclipse.resources.util.MarkerUtil;
import org.ebayopensource.turmeric.eclipse.utils.lang.StringUtil;
import org.ebayopensource.turmeric.eclipse.utils.plugin.ProgressUtil;
import org.ebayopensource.turmeric.eclipse.utils.plugin.WorkspaceUtil;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.internal.compiler.util.ManifestAnalyzer;
import org.eclipse.jdt.internal.core.JavaProject;
import org.eclipse.osgi.util.ManifestElement;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.osgi.framework.BundleException;

/**
 * The Class ProjectUtils.
 */
public class ProjectUtils {

	private static final SOALogger logger = SOALogger.getLogger();
	public static String SPLIT_PACKAGE_SERVICE_LATEST_PARAM = "allVersions";
	public static String HEADER_BUNDLE_NAME = "Bundle-SymbolicName";
	public static String HEADER_EXPORT_PACKAGE="Export-Package";
	public static String SPLIT_PACKAGE_SERVICE_ENDPOINT="http://crp-wsoaexps002.corp.ebay.com/OsgiPackageService/package/split?";
	public static String SPLIT_PACKAGE_SERVICE_PACKAGE_PARAM="package=";
	public static String SPLIT_PACKAGE_SERVICE_BUNDLE_PARAM="&bundleSymbolicName=";
	public static String ERROR_LEVEL_WARNING = "Warning";
	public static String ERROR_LEVEL_ERROR = "Error";
	
	public static String getBundleSymbolicNameFromManifest(IProject project) throws SOAResourceNotAccessibleException{
		//Preparing inputs to service
		Map<String,String> headers= new HashMap<String,String>();
		IFile file = project.getFile("\\src\\main\\resources\\META-INF\\MANIFEST.MF");
		if(!file.exists()){
			throw new SOAResourceNotAccessibleException("Manifest file is missing, please keep build automatically "
					+"turned on or perform a Build Project through the context menu", project);
		}
		InputStream inStream=null;
		try {
			inStream = file.getContents();
		} catch (CoreException e1) {
		}
		Manifest manifest=null;
		try {
		headers = ManifestElement.parseBundleManifest(inStream, headers);
		} catch (BundleException e) {
			
		} catch (IOException e1) {
		}
		try {
			if(inStream!=null)
			inStream.close();
		} catch (IOException e) {
		}
		
		String bundleName=headers.get(HEADER_BUNDLE_NAME);
		return bundleName;
	}
	
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
