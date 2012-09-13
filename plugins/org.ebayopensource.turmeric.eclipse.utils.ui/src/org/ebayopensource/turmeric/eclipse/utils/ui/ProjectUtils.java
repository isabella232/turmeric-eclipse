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
	
	public static void callSplitPackageServiceFromManifest(IProject project,boolean error) throws SOAResourceNotAccessibleException{
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
		String exportPackages=headers.get(HEADER_EXPORT_PACKAGE);
		ManifestElement[] elements=null;
		try {
			elements=ManifestElement.parseHeader(HEADER_EXPORT_PACKAGE, exportPackages);
		} catch (BundleException e) {
			e.printStackTrace();
		}
		if(bundleName==null||exportPackages==null||elements==null){
			logger.error("Error Parsing Manifest file. Skipping Split Package Validation.");
			return;
		}
		
		Set<String> packageList = new HashSet<String>();
		for (ManifestElement element:elements){
			packageList.add(element.getValue());
		}
		callSplitPackageServiceAndProcessOutPut(bundleName,packageList,project,error,true);
		}
	
	
	public static void callSplitPackageServiceAndProcessOutPut(String bundleName,Set<String> packageList, IProject project,boolean error,boolean showCompletionDialog){
		//Calling Service
		JSONObject response=null;
		try { 
			if (packageList.isEmpty())
			return;
			response = ProjectUtils.callSplitPackageService(bundleName, packageList);
		} catch (HttpException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//Storing source packages in the java project, to be compared.
		Set<IPackageFragmentRoot> sourceRoots = new HashSet<IPackageFragmentRoot>();
		try {
			if(JavaProject.hasJavaNature(project)){
			IJavaProject javaProject = JavaCore.create(project);
			IPackageFragmentRoot[] allRoots=javaProject.getAllPackageFragmentRoots();
			for(IPackageFragmentRoot root:allRoots){
					if(root.getKind()==IPackageFragmentRoot.K_SOURCE){
						sourceRoots.add(root);
					}
			}				
			}
			} catch (JavaModelException e) {
			e.printStackTrace();
		}
		
//		
		//Processing output from the service
		boolean conflict = false;
		String errorLevel = null;
		if(response!=null)
		{Iterator k =response.keys();
		 final String newLine = System.getProperty("line.separator");
		
		
		while (k.hasNext()){
			 StringBuilder errorMessage=new StringBuilder();
			Map<String,String> bundleVersion = new HashMap<String, String>();
			String ErrorPackageName = (String) k.next();
			JSONArray bundlesList = null;
			//Removing same bundle conflicts
			try {
				bundlesList = response.getJSONArray(ErrorPackageName);
				for (int i = 0; i < bundlesList.length(); i++) {
					JSONObject object = bundlesList.getJSONObject(i);
					String conflictBundleName = object.getString("bundleName");
					String conflictingVersion = object.getString("version");
					if(conflictBundleName.matches(bundleName+"\\-(\\d+\\.)?(\\d+\\.)?(\\*|\\d+)$")){							
						continue;
						//Ignoring Same Bundles
					}
					bundleVersion.put(conflictBundleName, conflictingVersion);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			
			//If valid conflicts were found for this package
			if(bundleVersion.size()>0){
				//display errors if any, for each erroneous bundle
				conflict = true;
				errorMessage.append("Split package errors detected for package "+ErrorPackageName
						+ " with the following bundles:");
				for(String bundle:bundleVersion.keySet()){
					errorMessage.append("\t"+ bundle+":"+bundleVersion.get(bundle));
					errorMessage.append(newLine);
				}
			
				//get the corresponding Resource for the package from sourceRoots, in order to mark it
				Set <IPackageFragment> packages  = new HashSet <IPackageFragment>();
				for(IPackageFragmentRoot eachSourceRoot:sourceRoots){
				 if(eachSourceRoot.getPackageFragment(ErrorPackageName).exists()){
					 IPackageFragment matchingPackage = eachSourceRoot.getPackageFragment(ErrorPackageName); 
					 if(matchingPackage!=null){
						 packages.add(matchingPackage);
					 }
				 }
				}
				//Create a marker for each such package from all the source roots
				for (IPackageFragment packageForMarking:packages){
					errorMessage.append(newLine);
					errorMessage.append("For more details about resolving split package errors follow this wiki: http://short/splitPackage");
		      		IMarker marker;
					try {
							marker = packageForMarking.getResource().createMarker(MarkerUtil.SOA_PROBLEM_MARKER_ID);
			  			   marker.setAttribute(IMarker.MESSAGE, errorMessage.toString());
			  			   if(error){
								marker.setAttribute(IMarker.SEVERITY,2); 
							}
							else{
								 marker.setAttribute(IMarker.SEVERITY,1);
							}
					} catch (CoreException e) {
						e.printStackTrace();
					}
				}
			//End of valid conflicts processing
			}
		//End of Each PAckage Processing
		}
		//End of  Not null response processing
		}
		if(error){
			errorLevel = ERROR_LEVEL_ERROR;
		}
		else{
			errorLevel = ERROR_LEVEL_WARNING;
		}
		if(showCompletionDialog){
			if(conflict)
			{
				UIUtil.openChoiceDialog(
				"Split Package Validation Completed",
				StringUtil.toString("Conflict(s) found! \n\r",
						"Results are displayed as "+errorLevel+"s in the Problems View.\n\r",
						"Check http://short/splitPackage for guidance on fixing them."),
				IStatus.ERROR);
			}
			else{
			//No conflicts found dialogbox
			
				UIUtil.openChoiceDialog(
					"Split Package Validation Completed",
					StringUtil.toString("No conflicts found! "),
					IStatus.OK);
			}
		}
	}
	public static JSONObject  callSplitPackageService(String serviceName, Set<String> packageList) throws HttpException, IOException, JSONException{

		StringBuilder sbuilder = new StringBuilder();
		// repo url
		sbuilder.append(SPLIT_PACKAGE_SERVICE_ENDPOINT);
		Boolean first = true;
		for(String namespace: packageList){
			if(first) sbuilder.append(SPLIT_PACKAGE_SERVICE_PACKAGE_PARAM);
			else sbuilder.append("&"+SPLIT_PACKAGE_SERVICE_PACKAGE_PARAM);
			sbuilder.append(namespace.toLowerCase());
			first = false;
		}
		sbuilder.append(SPLIT_PACKAGE_SERVICE_BUNDLE_PARAM);
		sbuilder.append(serviceName);
		HttpClient client = new HttpClient();
		GetMethod method = new GetMethod(sbuilder.toString());
		client.setConnectionTimeout(3000);
		client.executeMethod(method);
		JSONObject response =null;
		if(method.getStatusCode()!=204){
			String responseAsString = method.getResponseBodyAsString();
			response= new JSONObject(responseAsString);
		}
		return response;
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
