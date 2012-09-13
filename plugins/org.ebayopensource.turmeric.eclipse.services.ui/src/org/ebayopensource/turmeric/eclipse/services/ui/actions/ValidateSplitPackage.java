package org.ebayopensource.turmeric.eclipse.services.ui.actions;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.jar.Attributes;
import java.util.jar.Attributes.Name;
import java.util.jar.Manifest;
import java.util.logging.Level;

import org.apache.commons.httpclient.HttpException;
import org.ebayopensource.turmeric.eclipse.buildsystem.resources.SOAMessages;
import org.ebayopensource.turmeric.eclipse.buildsystem.utils.ActionUtil;
import org.ebayopensource.turmeric.eclipse.core.logging.SOALogger;
import org.ebayopensource.turmeric.eclipse.core.resources.constants.SOAProjectConstants;
import org.ebayopensource.turmeric.eclipse.exception.resources.SOAActionExecutionFailedException;
import org.ebayopensource.turmeric.eclipse.exception.resources.SOAResourceNotAccessibleException;
import org.ebayopensource.turmeric.eclipse.repositorysystem.RepositorySystemActivator;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.GlobalRepositorySystem;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.TrackingEvent;
import org.ebayopensource.turmeric.eclipse.repositorysystem.preferences.core.PreferenceConstants;
import org.ebayopensource.turmeric.eclipse.repositorysystem.ui.RepositorySystemUIActivator;
import org.ebayopensource.turmeric.eclipse.repositorysystem.utils.GlobalProjectHealthChecker;
import org.ebayopensource.turmeric.eclipse.resources.model.SOAImplProject;
import org.ebayopensource.turmeric.eclipse.resources.util.MarkerUtil;
import org.ebayopensource.turmeric.eclipse.services.ui.wizards.ConsumerFromWSDLWizard;
import org.ebayopensource.turmeric.eclipse.utils.lang.StringUtil;
import org.ebayopensource.turmeric.eclipse.utils.plugin.EclipseMessageUtils;
import org.ebayopensource.turmeric.eclipse.utils.plugin.ProgressUtil;
import org.ebayopensource.turmeric.eclipse.utils.plugin.WorkspaceUtil;
import org.ebayopensource.turmeric.eclipse.utils.ui.ProjectUtils;
import org.ebayopensource.turmeric.eclipse.utils.ui.UIUtil;
import org.ebayopensource.turmeric.eclipse.validator.utils.ValidateUtil;
import org.ebayopensource.turmeric.eclipse.validator.utils.common.AbstractBaseAccessValidator;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.resources.WorkspaceJob;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.internal.core.JavaProject;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.preference.PreferenceContentProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ValidateSplitPackage implements IObjectActionDelegate {
	private IStructuredSelection selection;
	private static final SOALogger logger = SOALogger.getLogger();

	/**
	 * {@inheritDoc}
	 *  @see org.eclipse.ui.IObjectActionDelegate#setActivePart(org.eclipse.jface.action.IAction, org.eclipse.ui.IWorkbenchPart)
	 */
	@Override
	public void setActivePart(final IAction action,
			final IWorkbenchPart targetPart) {
	}

	/**
	 * {@inheritDoc}
	 * @return 
	 * @see org.eclipse.ui.IActionDelegate#run(org.eclipse.jface.action.IAction)
	 */
	
	public static boolean isSplitPackageLevelError(){
		String errorlevel = RepositorySystemActivator.getDefault().getPreferences()
				.get(PreferenceConstants.PREF_ERROR_LEVEL_NAME,PreferenceConstants.PREF__WARNING);
		if(errorlevel.equals(PreferenceConstants.PREF__WARNING)){
			return false;
		}
		return true;
	}
	
	@Override
	public void run(final IAction action) {
		
			if (SOALogger.DEBUG)
				logger.entering(action, selection);
			
			if (selection == null)
				return;
			
			final IProject project = 
				ActionUtil.preValidateAction(selection.getFirstElement(), logger);
			if (project == null)
				return;
			try {
				ProjectUtils.callSplitPackageServiceFromManifest(project,isSplitPackageLevelError());
			} catch (SOAResourceNotAccessibleException e) {
				logger.error(e.getMessage(),e);
				UIUtil.showErrorDialog("Manifest File Missing! - Please build the project", "Manifest File Missing! - Please build the project using "
				+"Project--> Build or keep Build Automatically turned on",e);
			}
	}

	/**
	 * {@inheritDoc}
	 *  @see org.eclipse.ui.IActionDelegate#selectionChanged(org.eclipse.jface.action.IAction, org.eclipse.jface.viewers.ISelection)
	 */
	@Override
	public void selectionChanged(final IAction action,
			final ISelection selection) {
		this.selection = (IStructuredSelection) selection;
	}
}
