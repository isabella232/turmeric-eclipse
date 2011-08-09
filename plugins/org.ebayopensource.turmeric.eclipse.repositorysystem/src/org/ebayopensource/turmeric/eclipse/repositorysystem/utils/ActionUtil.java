package org.ebayopensource.turmeric.eclipse.repositorysystem.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.ebayopensource.turmeric.eclipse.core.logging.SOALogger;
import org.ebayopensource.turmeric.eclipse.exception.resources.SOAResourceModifyFailedException;
import org.ebayopensource.turmeric.eclipse.registry.ExtensionPointFactory;
import org.ebayopensource.turmeric.eclipse.registry.exception.ProviderException;
import org.ebayopensource.turmeric.eclipse.registry.intf.IRegistryProvider;
import org.ebayopensource.turmeric.eclipse.registry.models.SubmitAssetModel;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.GlobalRepositorySystem;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.ISOAProjectConfigurer;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.TrackingEvent;
import org.ebayopensource.turmeric.eclipse.repositorysystem.resources.SOAMessages;
import org.ebayopensource.turmeric.eclipse.resources.model.ISOAProject;
import org.ebayopensource.turmeric.eclipse.resources.model.SOAIntfMetadata;
import org.ebayopensource.turmeric.eclipse.resources.model.SOAIntfProject;
import org.ebayopensource.turmeric.eclipse.resources.util.SOAIntfUtil;
import org.ebayopensource.turmeric.eclipse.resources.util.SOAServiceUtil;
import org.ebayopensource.turmeric.eclipse.utils.core.VersionUtil;
import org.ebayopensource.turmeric.eclipse.utils.plugin.EclipseMessageUtils;
import org.ebayopensource.turmeric.eclipse.utils.plugin.ProgressUtil;
import org.ebayopensource.turmeric.eclipse.utils.plugin.WorkspaceUtil;
import org.ebayopensource.turmeric.eclipse.utils.ui.UIUtil;
import org.ebayopensource.turmeric.eclipse.validator.utils.ValidateUtil;
import org.ebayopensource.turmeric.eclipse.validator.utils.common.AbstractBaseAccessValidator;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.progress.UIJob;
import org.osgi.framework.Version;

/**
 * action util class
 * 
 */
public class ActionUtil {
	private static final SOALogger logger = SOALogger.getLogger();

	/**
	 * get assert model of the project.
	 * 
	 * @param project
	 * @return
	 * @throws Exception
	 */
	public static SubmitAssetModel getAssetModel(IProject project)
			throws Exception {
		if (project == null
				|| TurmericServiceUtils.isSOAInterfaceProject(project) == false) {
			throw new IllegalArgumentException(
					"Not a valid SOA interface project ->" + project);
		}
		// we trust the service name should be same as the project name
		final String serviceName = project.getName();
		// construct the model
		final SubmitAssetModel model = new SubmitAssetModel();
		String natureId = GlobalRepositorySystem.instanceOf()
				.getActiveRepositorySystem()
				.getTurmericProjectNatureId(project);
		ISOAProject soaProject = SOAServiceUtil.loadSOAProject(project,
				natureId);
		SOAIntfMetadata metadata = (SOAIntfMetadata) soaProject.getMetadata();
		if (StringUtils.isNotBlank(metadata.getPublicServiceName())) {
			// post 2.4 services
			model.setServiceName(metadata.getPublicServiceName());
			model.setAdminName(metadata.getServiceName());
		} else {
			// pre 2.4 services, adminName=serviceName
			model.setServiceName(metadata.getServiceName());
			model.setAdminName(metadata.getServiceName());
		}
		model.setNamespacePart(metadata.getServiceNamespacePart());
		model.setInterfaceProjectPath(project.getLocation().toString());
		model.setServiceLayer(metadata.getServiceLayer());
		model.setServiceNamespace(metadata.getTargetNamespace());
		model.setServiceVersion(metadata.getServiceVersion());
		model.setServiceWsdlLocation(SOAServiceUtil
				.getWsdlFile(project, serviceName).getLocation().toString());
		if (StringUtils.isBlank(metadata.getServiceDomainName())) {
			logger.warning(
					"Service domain name is missing. Please check service_intf_project.properties of the project and make sure 'domainName' propery is set.",
					"\n\nIf the service is created before installing AR plugin, then please either re-create the service or manually add 'domainName={DomainName}' to service_intf_project.properties and substitute the {DomainName}.");
		}
		model.setServiceDomain(metadata.getServiceDomainName());
		return model;
	}

	/**
	 * when exception thrown from AR, it is not friendly. This method gets the
	 * root cause of the exception and create an error status for the exception,
	 * using a description
	 * SOAMessages.ERROR_FAIL_TO_SUBMIT_SERVICE_VERSION_TO_AR
	 * 
	 * @param e
	 * @return
	 */
	private static IStatus handleExceptionFromAR(Throwable e) {
		logger.error(e);
		Throwable rootCause = e.getCause();
		if (rootCause == null) {
			return EclipseMessageUtils.createErrorStatus(e);
		} else {
			while (rootCause.getCause() != null) {
				rootCause = rootCause.getCause();
			}
			if (rootCause instanceof LinkageError) {
				logger.error(rootCause);
				return EclipseMessageUtils.createErrorStatus(
						SOAMessages.ERROR_AR_OUT_OF_DATE, null);
			} else {
				return EclipseMessageUtils.createErrorStatus(
						SOAMessages.ERROR_FAIL_TO_SUBMIT_SERVICE_VERSION_TO_AR,
						rootCause);
			}
		}
	}

	/**
	 * Submite a service maintenance version. For Backward Compatibility, using
	 * reflect to call new added method. Show error message if method not found
	 * and notify users to update to latest version.
	 * 
	 * @param project
	 * @return
	 * @throws Exception
	 */
	public static IStatus submitServiceMaintenanceVersion(IProject project,
			String newVersion) throws Exception {
		final IRegistryProvider regProvider = ExtensionPointFactory
				.getSOARegistryProvider();
		if (regProvider == null) {
			return EclipseMessageUtils.createStatus(
					SOAMessages.WARNING_AR_NOT_AVAILABLE, IStatus.WARNING);
		}

		// use reflect for backward compatible.
		try {
			Method submitNewMaintenanceVersionMethod = regProvider.getClass()
					.getMethod("submitNewMaintenanceVersion",
							SubmitAssetModel.class);
			SubmitAssetModel model = getAssetModel(project);
			if (newVersion != null && newVersion.trim().length() > 0) {
				model.setServiceVersion(newVersion);
			}
			Object retStatus = submitNewMaintenanceVersionMethod.invoke(
					regProvider, model);
			return (IStatus) retStatus;
		} catch (NoSuchMethodException ex) {
			logger.error(ex);
			return EclipseMessageUtils.createErrorStatus(
					SOAMessages.ERROR_AR_OUT_OF_DATE, null);
		} catch (SecurityException ex) {
			return EclipseMessageUtils.createErrorStatus(ex);
		} catch (InvocationTargetException ex) {
			return handleExceptionFromAR(ex);
		}
	}

	/**
	 * this is used to update interface project version. It combines the
	 * following operations: 1) Update local metadata 2) sync version with AR if
	 * AR is available. Notify users before doing it. It is cancelable. 3) do a
	 * v3 build if it is v3 mode. Notify users before doing it. Library Catalog
	 * version need to be synchronized before doing a v3 build. It is
	 * cancelable.
	 * 
	 * This method will be used in service property page when changing service
	 * version. And also be used in V3 when calling a Build Service in right
	 * click menu.
	 * 
	 * This method is meant to be invoked in the UI thread.
	 * 
	 * @param soaIntfProject
	 *            the interface project that need to be updated
	 * @param oldVersion
	 *            service old version
	 * @param newVersion
	 *            service new version.
	 * @param silence
	 *            just for the repos that need build after version change.
	 * @param monitor
	 *            the progress monitor
	 */
	public static void updateInterfaceProjectVersion(
			final SOAIntfProject soaIntfProject, String oldVersion,
			String newVersion, boolean silence, IProgressMonitor monitor)
			throws Exception {

		ISOAProjectConfigurer configurer = GlobalRepositorySystem.instanceOf()
				.getActiveRepositorySystem().getProjectConfigurer();
		ProgressUtil.progressOneStep(monitor);

		if (StringUtils.equals(oldVersion, newVersion) == true) {
			logger.info("Service version not change : " + newVersion);
			// still need try to build service if version not changed.
			if (silence == true) {
				configurer.postServiceVersionUpdated(soaIntfProject,
						oldVersion, newVersion, silence, monitor);
			}
			return;
		}

		final IProject intfProject = soaIntfProject.getProject();
		final String serviceName = intfProject.getName();
		final IStatus status = new AbstractBaseAccessValidator() {

			@Override
			public List<IResource> getReadableFiles() {
				// should check the following files
				try {
					return GlobalProjectHealthChecker
							.getSOAProjectReadableResources(intfProject);
				} catch (Exception e) {
					logger.warning(e);
				}
				return new ArrayList<IResource>(1);
			}

			@Override
			public List<IResource> getWritableFiles() {
				final List<IResource> files = new ArrayList<IResource>();
				files.add(SOAIntfUtil.getMetadataFile(intfProject, serviceName));
				files.add(SOAServiceUtil.getWsdlFile(intfProject, serviceName));
				return files;
			}

		}.validate(serviceName);

		final String messages = ValidateUtil
				.getFormattedStatusMessagesForAction(status);
		if (messages != null) {
			UIUtil.showErrorDialog(UIUtil.getActiveShell(), "Error", messages,
					(Throwable) null);
			return;
		}

		soaIntfProject.getMetadata().setServiceVersion(newVersion);
		// update local meta data.
		ProgressUtil.progressOneStep(monitor);

		configurer.updateProject(soaIntfProject, false, monitor);
		ProgressUtil.progressOneStep(monitor);

		// synchronize with AR if available.
		// although submitVersionToAssitionRepository will check
		// version change or not. here we add a check to avoid
		// creating a UIJob.
		if (GlobalRepositorySystem.instanceOf().getActiveRepositorySystem()
				.getActiveOrganizationProvider()
				.supportAssetRepositoryIntegration() == true
				&& ExtensionPointFactory.getSOARegistryProvider() != null) {
			final Version newVer = new Version(newVersion);
			final Version oldVer = new Version(oldVersion);
			ProgressUtil.progressOneStep(monitor);
			// no version check here because it is checked when
			// version changed.
			UIJob updateVersion = new UIJob(
					"Synchornizing service version with Asset Repository.") {

				@Override
				public IStatus runInUIThread(IProgressMonitor monitor) {
					try {
						ActionUtil.submitVersionToAssetRepository(newVer,
								oldVer, soaIntfProject.getProjectName(),
								monitor);
					} catch (Exception e) {
						logger.error(e);
					}
					return Status.OK_STATUS;
				}

			};
			updateVersion.schedule();
		}
		ProgressUtil.progressOneStep(monitor);

		// trigger build is needed.
		configurer.postServiceVersionUpdated(soaIntfProject, oldVersion,
				newVersion, silence, monitor);
	}

	/**
	 * submit a new minor version to AR.
	 * 
	 * @param project
	 * @param newVersion
	 * @return
	 */
	public static IStatus submitNewVersionAssetToSOARegistry(IProject project,
			String newVersion) {
		IRegistryProvider regProvider;
		try {
			regProvider = ExtensionPointFactory.getSOARegistryProvider();
			if (regProvider == null) {
				return EclipseMessageUtils.createStatus(
						SOAMessages.WARNING_AR_NOT_AVAILABLE, IStatus.WARNING);
			}

			final SubmitAssetModel model = getAssetModel(project);
			if (newVersion != null && newVersion.trim().length() > 0) {
				model.setServiceVersion(newVersion);
			}
			// ProgressUtil.progressOneStep(monitor);
			// submit the model
			return regProvider.submitNewVersionForGovernance(model);
		} catch (CoreException e) {
			logger.error(e);
			return EclipseMessageUtils.createErrorStatus(e);
		} catch (ProviderException e) {
			return handleExceptionFromAR(e);
		} catch (Exception e) {
			logger.error(e);
			return EclipseMessageUtils.createErrorStatus(e);
		}

	}

	/**
	 * submit version to assert repo
	 * 
	 * @param newVersion
	 * @param oldVersion
	 * @param svcIntfName
	 * @param monitor
	 * @return
	 * @throws Exception
	 */
	public static IStatus submitVersionToAssetRepository(Object newVersion,
			Object oldVersion, String svcIntfName, IProgressMonitor monitor)
			throws Exception {
		Version newVer = VersionUtil.getVersion(newVersion);
		Version oldVer = VersionUtil.getVersion(oldVersion);

		monitor.setTaskName("Synchronize service version with "
				+ "Asset Repository for project->" + svcIntfName
				+ ", from version [" + oldVer + "], to version [" + newVer
				+ "].");

		// no version format check here because it is checked when version
		// changed.
		// here we use not equal but not bigger than to decide if a version is
		// changed.
		// Only show when service version is changed this time.
		boolean miniorChanged = newVer.getMinor() != oldVer.getMinor();
		boolean mantanChanged = newVer.getMicro() != oldVer.getMicro();
		final boolean syncVersion;

		if (miniorChanged || mantanChanged) {
			syncVersion = UIUtil
					.openChoiceDialog(
							"Synchronize Service Version",
							"Service version has been updated to ["
									+ newVer
									+ "] in local metadata. "
									+ "Would you like to synchronize service version change to the Asset Repository?",
							MessageDialog.QUESTION_WITH_CANCEL);

		} else {
			logger.info("Service Version not changed:" + newVer
					+ ". Exist submitVersionToAssetRepository");
			return Status.CANCEL_STATUS;
		}

		if (syncVersion == false) {
			return Status.CANCEL_STATUS;
		}

		try {
			IStatus result = null;
			IProject intfProj = WorkspaceUtil.getProject(svcIntfName);
			if (miniorChanged == true) {
				result = ActionUtil.submitNewVersionAssetToSOARegistry(
						intfProj, newVer.toString());
			} else if (mantanChanged == true) {
				result = ActionUtil.submitServiceMaintenanceVersion(intfProj,
						newVer.toString());

			} else {
				logger.info("Service Version not changed:" + newVer
						+ ". Exist submitVersionToAssitionRepository");
			}
			if (result != null) {
				if (result.isOK() == true) {
					GlobalRepositorySystem
							.instanceOf()
							.getActiveRepositorySystem()
							.trackingUsage(
									new TrackingEvent(ActionUtil.class
											.getName(),
											TrackingEvent.TRACKING_ACTION));
				} else {
					UIUtil.showErrorDialog(
							(Shell) null,
							"Service Version Synchronize Failed",
							"Failed to synchronize service version with Asset Repository.",
							result);
				}
			}
		} catch (Exception e) {
			logger.error(e);
			throw new SOAResourceModifyFailedException(
					"Failed to synchronize service version for project->"
							+ svcIntfName + ", from version [" + oldVer
							+ "] to version [" + newVer + "].", e);
		} finally {
			monitor.done();
		}

		return null;
	}

}
