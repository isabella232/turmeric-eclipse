/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.typelibrary.actions;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.ebayopensource.turmeric.eclipse.codegen.utils.CodegenInvoker;
import org.ebayopensource.turmeric.eclipse.core.logging.SOALogger;
import org.ebayopensource.turmeric.eclipse.typelibrary.builders.TypeLibraryBuilderUtils;
import org.ebayopensource.turmeric.eclipse.typelibrary.buildsystem.TypeLibSynhcronizer;
import org.ebayopensource.turmeric.eclipse.typelibrary.codegen.model.GenTypeDeleteType;
import org.ebayopensource.turmeric.eclipse.typelibrary.resources.SOAMessages;
import org.ebayopensource.turmeric.eclipse.typelibrary.utils.TypeLibraryUtil;
import org.ebayopensource.turmeric.eclipse.ui.monitor.typelib.SOAGlobalRegistryAdapter;
import org.ebayopensource.turmeric.eclipse.utils.collections.CollectionUtil;
import org.ebayopensource.turmeric.eclipse.utils.plugin.EclipseMessageUtils;
import org.ebayopensource.turmeric.eclipse.utils.plugin.ProgressUtil;
import org.ebayopensource.turmeric.eclipse.utils.plugin.WorkspaceUtil;
import org.ebayopensource.turmeric.eclipse.utils.ui.UIUtil;
import org.ebayopensource.turmeric.eclipse.validator.utils.ValidateUtil;
import org.ebayopensource.turmeric.tools.library.SOATypeRegistry;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.WorkspaceJob;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.progress.IProgressConstants;

import org.ebayopensource.turmeric.common.config.LibraryType;

/**
 * @author smathew
 * 
 * Delete Action in the context menu. This menu is an Object Delegate and would
 * be hanging down in any I File object which is an XSD.
 * 
 * Steps in delete. Validate XSD file for accessibility Validate the Type
 * Dep/Info for writability. Delete the type from registry Delete The file from
 * disk Call Codegen. Synchronize dependency.
 */
public class DeleteTypeAction implements IObjectActionDelegate {
	private IStructuredSelection selection;
	private static final SOALogger logger = SOALogger.getLogger();

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IObjectActionDelegate#setActivePart(org.eclipse.jface.action.IAction,
	 *      org.eclipse.ui.IWorkbenchPart)
	 */
	public void setActivePart(final IAction action,
			final IWorkbenchPart targetPart) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IActionDelegate#run(org.eclipse.jface.action.IAction)
	 */
	public void run(final IAction action) {
		if (SOALogger.DEBUG)
			logger.entering(action, selection);
		try {
			if (selection == null) {
				showErrorDialog();
				return;
			}
			if (!UIUtil.openChoiceDialog("Confirm Type Delete",
					"Are you sure you want to delete this type?",
					MessageDialog.QUESTION))
				return;

			if (TypeLibraryUtil.isValidXSDFile(selection.getFirstElement())) {
				final IFile xsdFile = (IFile) selection.getFirstElement();
				final IProject project = xsdFile.getProject();
				final String fileName = StringUtils.substringBeforeLast(xsdFile.getName(), 
						"." + xsdFile.getFileExtension());
				final IFile episodeFile = project.getFile(
						"gen-meta-src/META-INF/"
						+ project.getName() + "/"
						+ fileName
						+ ".episode");
				final IStatus status = ActionUtil
						.validateTypeDependencyAndProjectConfigFile(project, episodeFile);
				
				final String messages = ValidateUtil
						.getFormattedStatusMessagesForAction(status);
				if (messages != null) {
					UIUtil.showErrorDialog(UIUtil.getActiveShell(), "Error",
							messages, (Throwable) null);
					return;
				}
				
				File eFile = episodeFile.getLocation().toFile();
				if (eFile.exists() == true) {
					File tmpFile = new File(eFile.getCanonicalPath() + ".bak");
					try {
						if (tmpFile.exists()) {
							tmpFile.delete();
						}
						if (eFile.renameTo(tmpFile) == false) {
							UIUtil.showErrorDialog(UIUtil.getActiveShell(), "Error", 
									SOAMessages.ERR_DELETE_TYPE
									+ eFile);
							return;
						}
					} catch (Exception e) {
						//the file could not be deleted, warn the user
						UIUtil.showErrorDialog(UIUtil.getActiveShell(), "Error", 
								SOAMessages.ERR_DELETE_TYPE
								+ eFile);
						return;
					} finally {
						if (tmpFile.exists()) {
							tmpFile.renameTo(eFile);
						}
					}
				}
				
				DeleteTypeAction.executeTypeDeletionTask((IFile) selection.getFirstElement());
			} else {
				showErrorDialog();
				return;
			}
		} catch (Exception e) {
			logger.error(e);
			UIUtil.showErrorDialog(e);
		} finally {
			if (SOALogger.DEBUG)
				logger.exiting();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IActionDelegate#selectionChanged(org.eclipse.jface.action.IAction,
	 *      org.eclipse.jface.viewers.ISelection)
	 */
	public void selectionChanged(final IAction action,
			final ISelection selection) {
		this.selection = (IStructuredSelection) selection;
	}

	/**
	 * @param file
	 * @throws CoreException
	 * @throws Exception
	 * This is being used from the test apis. So please dont make it private
	 */
	public static void executeTypeDeletionTask(final IFile file) throws CoreException, Exception {

		LibraryType libraryType;
		SOAGlobalRegistryAdapter registryAdaptor = SOAGlobalRegistryAdapter.getInstance();
		try {
			SOATypeRegistry typeRegistry = registryAdaptor.getGlobalRegistry();
			libraryType = typeRegistry.getType(TypeLibraryUtil.toQName(file));
			final List<LibraryType> childTypes = typeRegistry.getDependentChildTypeFiles(libraryType);
			if (!CollectionUtil.isEmpty(childTypes)) {
				StringBuilder msg = new StringBuilder("SOA governance found the following types depending on this type: ");
				for (LibraryType libType : childTypes) {
					msg.append(libType.getName());
					msg.append(", ");
				}
				String smsg = msg.toString();
				smsg = StringUtils.substringBeforeLast(smsg, ",");
				UIUtil.showErrorDialog(null, "File cannot be deleted",
						"Type cannot be deleted", smsg);

				return;
			} else {
				WorkspaceJob job = new WorkspaceJob("Deleting type->" + file.getLocation()) {
					
					@Override
					public IStatus runInWorkspace(IProgressMonitor monitor)
							throws CoreException {
						SOAGlobalRegistryAdapter registryAdaptor = SOAGlobalRegistryAdapter.getInstance();
						try {
							SOATypeRegistry typeRegistry = registryAdaptor.getGlobalRegistry();
							monitor.beginTask("Deleting type->" + file.getLocation(), 
									ProgressUtil.PROGRESS_STEP * 50);
							IProject project = file.getProject();
							LibraryType libraryType = typeRegistry.getType(
											TypeLibraryUtil.toQName(file));
							ProgressUtil.progressOneStep(monitor);
							typeRegistry.removeTypeFromRegistry(libraryType);
							ProgressUtil.progressOneStep(monitor);
							// file.delete(true, ProgressUtil
							// .getDefaultMonitor(null));
							DeleteTypeAction.callCodegen(project, file, monitor);
							ProgressUtil.progressOneStep(monitor);
							WorkspaceUtil.refresh(monitor, project);
							registryAdaptor.populateRegistry(project
									.getName());
							ProgressUtil.progressOneStep(monitor);
							TypeLibSynhcronizer
									.syncronizeAllXSDsandDepXml(project);
							ProgressUtil.progressOneStep(monitor);
							TypeLibSynhcronizer
									.synchronizeTypeDepandProjectDep(project, monitor);
							WorkspaceUtil.refresh(monitor, project);

						} catch (Exception e) {
							logger.error(e);
							throw new CoreException(EclipseMessageUtils.createErrorStatus(e));
						}
						setProperty(IProgressConstants.KEEP_PROPERTY, Boolean.TRUE);
						return Status.OK_STATUS;
					}
				};
				job.setUser(true);
				job.schedule();
			}
		} catch (Exception e) {
			WorkspaceUtil.refresh(file.getProject());
			UIUtil.showErrorDialog(e);
		}

	}

	private static void callCodegen(IProject project, IFile file, IProgressMonitor monitor) throws Exception {
		GenTypeDeleteType genTypeDeleteType = new GenTypeDeleteType();
		genTypeDeleteType.setProjectRoot(project.getLocation().toString());
		genTypeDeleteType.setLibraryName(project.getName());
		ArrayList<String> types = new ArrayList<String>();
		types.add(file.getName());
		genTypeDeleteType.setTypes(types);
		CodegenInvoker codegenInvoker = TypeLibraryBuilderUtils
				.initForTypeLib(project);
		ProgressUtil.progressOneStep(monitor);
		codegenInvoker.execute(genTypeDeleteType);
	}

	private void showErrorDialog() {
		UIUtil
				.showErrorDialog(
						null,
						"This action is not available for this selection.",
						"Action not available",
						"Please check if the project is a TypeLibrary project and the seleted file is a valid Type.");
	}

}
