/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.maven.core.ui.actions;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.maven.model.Dependency;
import org.apache.maven.model.Model;
import org.apache.maven.repository.metadata.ArtifactMetadata;
import org.ebayopensource.turmeric.eclipse.buildsystem.utils.ActionUtil;
import org.ebayopensource.turmeric.eclipse.core.logging.SOALogger;
import org.ebayopensource.turmeric.eclipse.maven.core.repositorysystem.IMavenOrganizationProvider;
import org.ebayopensource.turmeric.eclipse.maven.core.utils.MavenCoreUtils;
import org.ebayopensource.turmeric.eclipse.maven.core.utils.SOAMavenConstants;
import org.ebayopensource.turmeric.eclipse.maven.core.utils.WebXMLParser;
import org.ebayopensource.turmeric.eclipse.mavenapi.exception.MavenEclipseApiException;
import org.ebayopensource.turmeric.eclipse.mavenapi.impl.MavenEclipseUtil;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.GlobalRepositorySystem;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.ISOARepositorySystem;
import org.ebayopensource.turmeric.eclipse.resources.model.SOAImplProject;
import org.ebayopensource.turmeric.eclipse.resources.model.SOAIntfMetadata;
import org.ebayopensource.turmeric.eclipse.utils.collections.ListUtil;
import org.ebayopensource.turmeric.eclipse.utils.plugin.ProgressUtil;
import org.ebayopensource.turmeric.eclipse.utils.plugin.WorkspaceUtil;
import org.ebayopensource.turmeric.eclipse.utils.ui.UIUtil;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.ui.JavaElementLabelProvider;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.actions.WorkspaceModifyOperation;
import org.eclipse.ui.dialogs.ElementListSelectionDialog;
import org.jdom.Document;
import org.eclipse.m2e.core.internal.*;
import org.osgi.framework.Version;

/**
 * The Class AddImplProjectToWebProject.
 *
 * @author mzang
 */
public class AddImplProjectToWebProject implements IObjectActionDelegate {
	private IStructuredSelection selection;
	private static final SOALogger logger = SOALogger.getLogger();
	private Shell shell;
	private volatile boolean addParentArtifact = true;

	/**
	 * Instantiates a new adds the impl project to web project.
	 */
	public AddImplProjectToWebProject() {
		super();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setActivePart(final IAction action,
			final IWorkbenchPart targetPart) {
		shell = targetPart.getSite().getShell();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void run(final IAction action) {
		try {
			addParentArtifact = true;
			if (SOALogger.DEBUG)
				logger.entering(action, selection);

			if (selection == null)
				return;

			List<IProject> projects = new ArrayList<IProject>();
			for (Object projectObj : selection.toArray()) {
				IProject project = ActionUtil.preValidateAction(projectObj,
						logger);
				if (project != null) {
					projects.add(project);
				}
			}
			if (projects.size() == 0) {
				return;
			}
			// for now just support one selection
			final IProject implProject = projects.get(0);
			final ISOARepositorySystem repoSystem = GlobalRepositorySystem
					.instanceOf().getActiveRepositorySystem();

			final SOAImplProject implProj = (SOAImplProject) repoSystem
					.getAssetRegistry().getSOAProject(implProject);

			final SOAIntfMetadata intfMetadata = implProj.getMetadata()
					.getIntfMetadata();
			String adminName = intfMetadata.getServiceName();

			List<IProject> wsProjects = WorkspaceUtil
					.getProjectsByNature(IMavenConstants.NATURE_ID);

			if (wsProjects.size() == 0) {
				MessageDialog.openWarning(shell, "No Web Project",
						"There is no web project in current workspace.");
				return;
			}

			final Map<IProject, Model> targetProjects = new HashMap<IProject, Model>();
			for (IProject wsProject : wsProjects) {
				try {
					final Model pom = MavenEclipseUtil
					.readPOM(wsProject);
					if (isValidatedTargetProject(wsProject, pom, adminName) == true) {
						targetProjects.put(wsProject, pom);
					}
				} catch (Exception e) {
					logger.warning("Error occured while processing project ->" + wsProject.getName() + ". Ignoring it", e);
				}
			}

			if (targetProjects.size() == 0) {
				MessageDialog.openWarning(shell, "No Validated Web Project",
						"All web projects in current workspace have added servlet: "
								+ adminName);
				return;
			}
			
			final Model dependentPom = MavenEclipseUtil
			.readPOM(implProject);
			if (dependentPom == null) {
				return;
			}
			
			JavaElementLabelProvider labelProvider = new JavaElementLabelProvider() {

				@Override
				public String getText(Object element) {
					String text = super.getText(element);
					if (element instanceof IProject) {
						final Model pom = targetProjects.get((IProject)element);
						if (pom != null && pom.getParent() != null) {
							text = new StringBuilder(text).append(" - ").append(pom.getParent()).toString();
						}
					}
					return text;
				}
			};

			IProject selectedProject = null;
			ElementListSelectionDialog selectionDialog = new ElementListSelectionDialog(
					shell, labelProvider) {

				@Override
				protected Control createDialogArea(Composite parent) {
					Composite contents = (Composite) super
							.createDialogArea(parent);
					
					if (dependentPom.getParent() != null) {
						final Button button = new Button(contents, SWT.CHECK);
						button.setSelection(addParentArtifact);
						button.addSelectionListener(new SelectionAdapter() {

							@Override
							public void widgetSelected(SelectionEvent e) {
								addParentArtifact = button.getSelection();
							}
						});
						button
						.setLayoutData(new GridData(
								GridData.FILL_HORIZONTAL));
						button.setText("Use Project's Parent Artifact - " + dependentPom.getParent());
						button
						.setToolTipText("Automatically add the project's parent artifact to the seleted web project if it does not have a parent artifact yet.");
						this.fFilteredList.addSelectionListener(new SelectionAdapter() {

							@Override
							public void widgetSelected(SelectionEvent e) {
								Object[] data = fFilteredList.getSelection();
								if (data != null && data.length > 0 && data[0] instanceof IProject) {
									Model pom = targetProjects.get((IProject)data[0]);
									if (pom != null) {
										button.setEnabled(pom.getParent() == null);
									}
								}
							}
						});
					}
					return contents;
				}
			};
			
			selectionDialog.setMessage("Please select target web project");
			selectionDialog
					.setElements(targetProjects.keySet().toArray(new IProject[0]));
			selectionDialog.setTitle("Select the Target Web Project");

			selectionDialog.setBlockOnOpen(true);
			selectionDialog.setMultipleSelection(false);
			if (selectionDialog.open() == Window.OK) {
				if (selectionDialog.getResult() != null
						&& selectionDialog.getResult().length > 0) {
					selectedProject = ((IProject) selectionDialog.getResult()[0]);
				}
			}

			if (selectedProject == null) {
				return;
			}

			final IFile targetWebFile = getWebFileFromWebProject(selectedProject);

			if (targetWebFile == null || targetWebFile.isAccessible() == false) {
				return;
			}

			final IProject targetWebProject = selectedProject;
			final WorkspaceModifyOperation operation = new WorkspaceModifyOperation() {

				@Override
				protected void execute(IProgressMonitor monitor)
						throws CoreException, InvocationTargetException,
						InterruptedException {
					try {
						monitor.beginTask("Adding Services to Web Project...", 
								ProgressUtil.PROGRESS_STEP * 10);
						final IFile pomFile = MavenEclipseUtil.getPomFile(targetWebProject);
						pomFile.refreshLocal(IResource.DEPTH_ZERO, monitor);

						final Model pom = MavenEclipseUtil.readPOM(targetWebProject);
						ProgressUtil.progressOneStep(monitor);
						addServletNodeToWebProject(implProj, targetWebProject, pom,
								addParentArtifact, monitor);
						((IMavenOrganizationProvider)repoSystem.getActiveOrganizationProvider())
						.postAddingServiceToWebProjects(ListUtil.arrayList(implProject), targetWebProject, 
								pom, monitor);
					} catch (Exception e) {
						throw new InvocationTargetException(e);
					}  finally {
						WorkspaceUtil.refresh(targetWebProject, monitor);
						monitor.done();
					}
				}

			};

			try {
				new ProgressMonitorDialog(shell).run(true, true, operation);
			} catch (Exception e) {
				logger.error(e);
				UIUtil.showErrorDialog(e);
			}

		} catch (Exception e) {
			logger.error(e);
			UIUtil.showErrorDialog(e);
		} finally {
			if (SOALogger.DEBUG)
				logger.exiting();
		}
	}

	/**
	 * Checks if is validated target project.
	 *
	 * @param project the project
	 * @param pom the pom
	 * @param adminName the admin name
	 * @return true, if is validated target project
	 * @throws CoreException the core exception
	 * @throws MavenEclipseApiException the maven eclipse api exception
	 */
	public boolean isValidatedTargetProject(IProject project, Model pom, String adminName)
			throws CoreException, MavenEclipseApiException {
		if (pom == null) {
			return false;
		}

		String packagingType = pom.getPackaging();
		if (SOAMavenConstants.MAVEN_PACKAGING_WAR
				.equalsIgnoreCase(packagingType) == false) {
			return false;
		}

		IFile targetWebFile = getWebFileFromWebProject(project);

		if (targetWebFile == null || targetWebFile.isAccessible() == false) {
			return false;
		}

		List<String> allServlets = WebXMLParser.getServletNames(targetWebFile
				.getContents());

		return allServlets.contains(adminName) == false;
	}

	/**
	 * Gets the web file from web project.
	 *
	 * @param webProject the web project
	 * @return the web file from web project
	 */
	public static IFile getWebFileFromWebProject(IProject webProject) {
		IFile webXML = webProject.getFile("src/main/webapp/WEB-INF/web.xml");
		return webXML;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void selectionChanged(final IAction action,
			final ISelection selection) {
		this.selection = (IStructuredSelection) selection;
	}
	
	/**
	 * Adds the servlet node to web project.
	 *
	 * @param implProj the impl proj
	 * @param targetWebProject the target web project
	 * @param pom the pom
	 * @param addParentArtifact the add parent artifact
	 * @param monitor the monitor
	 */
	public static void addServletNodeToWebProject(SOAImplProject implProj,
			IProject targetWebProject, Model pom, boolean addParentArtifact,
			IProgressMonitor monitor) {

		SOAIntfMetadata intfMetadata = implProj.getMetadata().getIntfMetadata();
		try {
			String namespacePart = intfMetadata.getServiceNamespacePart();
			Version version = Version.parseVersion(intfMetadata
					.getServiceVersion());
			int majorVersion = version.getMajor();
			String adminName = intfMetadata.getServiceName();

			Document sourceDoc = WebXMLParser.getSourceDocument(adminName,
					namespacePart, majorVersion);
			ProgressUtil.progressOneStep(monitor);

			IFile targetWebFile = getWebFileFromWebProject(targetWebProject);
			// write to target web.xml
			WebXMLParser.addServletElementsToWebXML(sourceDoc, targetWebFile
					.getContents(), targetWebFile);
			ProgressUtil.progressOneStep(monitor, ProgressUtil.PROGRESS_STEP * 2);
			// add impl project to target project dependency

			
			final Model dependentPom = MavenEclipseUtil.readPOM(implProj
					.getProject());
			ProgressUtil.progressOneStep(monitor);
			
			final ArtifactMetadata metadata = MavenEclipseUtil
					.artifactMetadata(dependentPom);
			Dependency dependency = MavenEclipseUtil.dependency(metadata);
			if (dependency == null || dependency.getGroupId() == null) {
				return;
			}
			if (MavenCoreUtils.findDependency(dependency.getGroupId(),
					dependency.getArtifactId(), pom) != null) {
				logger.warning("Dependency has already "
						+ "been added skipping it->" + dependency);
				return;
			}
			pom.addDependency(dependency);
			ProgressUtil.progressOneStep(monitor);

			if (addParentArtifact == true && pom.getParent() == null
					&& dependentPom.getParent() != null) {
				pom.setParent(dependentPom.getParent());
				logger
						.info(
								"The project ",
								targetWebProject.getName(),
								" does not have a parent artifact, setting with the parent artifact from impl project->",
								implProj.getProject().getName());
				ProgressUtil.progressOneStep(monitor);
			}
			
			final IFile pomFile = MavenEclipseUtil.getPomFile(targetWebProject);

			MavenCoreUtils.mavenEclipseAPI().writePom(pomFile, pom);
			ProgressUtil.progressOneStep(monitor);
			pomFile.refreshLocal(IResource.DEPTH_ZERO, monitor);
		} catch (Exception e) {
			logger.error(e);
			UIUtil.showErrorDialog(e);
		}

	}

}
