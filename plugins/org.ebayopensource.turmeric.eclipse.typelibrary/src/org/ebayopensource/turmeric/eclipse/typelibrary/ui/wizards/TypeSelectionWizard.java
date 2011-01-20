/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.typelibrary.ui.wizards;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.ebayopensource.turmeric.eclipse.buildsystem.core.SOAGlobalRegistryAdapter;
import org.ebayopensource.turmeric.eclipse.buildsystem.utils.BuildSystemUtil;
import org.ebayopensource.turmeric.eclipse.config.repo.SOAConfigExtensionFactory.SOAXSDTemplateSubType;
import org.ebayopensource.turmeric.eclipse.exception.resources.SOATypeCreationFailedException;
import org.ebayopensource.turmeric.eclipse.logging.SOALogger;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.GlobalRepositorySystem;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.TrackingEvent;
import org.ebayopensource.turmeric.eclipse.typelibrary.builders.TypeLibraryProjectNature;
import org.ebayopensource.turmeric.eclipse.typelibrary.buildsystem.TypeCreator;
import org.ebayopensource.turmeric.eclipse.typelibrary.buildsystem.TypeLibSynhcronizer;
import org.ebayopensource.turmeric.eclipse.typelibrary.ui.model.ComplexTypeCCParamModel;
import org.ebayopensource.turmeric.eclipse.typelibrary.ui.model.ComplexTypeParamModel;
import org.ebayopensource.turmeric.eclipse.typelibrary.ui.model.ComplexTypeSCParamModel;
import org.ebayopensource.turmeric.eclipse.typelibrary.ui.model.EnumTypeParamModel;
import org.ebayopensource.turmeric.eclipse.typelibrary.ui.model.SimpleTypeParamModel;
import org.ebayopensource.turmeric.eclipse.typelibrary.ui.model.TypeParamModel;
import org.ebayopensource.turmeric.eclipse.typelibrary.ui.wizards.pages.ComplexTypeCCWizardGeneralPage;
import org.ebayopensource.turmeric.eclipse.typelibrary.ui.wizards.pages.ComplexTypeSCWizardGeneralPage;
import org.ebayopensource.turmeric.eclipse.typelibrary.ui.wizards.pages.ComplexTypeWizardAttribPage;
import org.ebayopensource.turmeric.eclipse.typelibrary.ui.wizards.pages.ComplexTypeWizardElementPage;
import org.ebayopensource.turmeric.eclipse.typelibrary.ui.wizards.pages.ComplexTypeWizardGeneralPage;
import org.ebayopensource.turmeric.eclipse.typelibrary.ui.wizards.pages.EnumTypeWizardDetailsPage;
import org.ebayopensource.turmeric.eclipse.typelibrary.ui.wizards.pages.EnumTypeWizardGeneralPage;
import org.ebayopensource.turmeric.eclipse.typelibrary.ui.wizards.pages.SimpleTypeWizardGeneralPage;
import org.ebayopensource.turmeric.eclipse.typelibrary.ui.wizards.pages.TypeSelectionWizardPage;
import org.ebayopensource.turmeric.eclipse.typelibrary.ui.wizards.pages.ComplexTypeWizardElementPage.ElementTableModel;
import org.ebayopensource.turmeric.eclipse.typelibrary.utils.TypeLibraryUtil;
import org.ebayopensource.turmeric.eclipse.ui.SOABaseWizard;
import org.ebayopensource.turmeric.eclipse.utils.plugin.ProgressUtil;
import org.ebayopensource.turmeric.eclipse.utils.plugin.WorkspaceUtil;
import org.ebayopensource.turmeric.eclipse.utils.ui.UIUtil;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.ui.actions.WorkspaceModifyOperation;

/**
 * Wizard for selection of types
 * 
 * @author ramurthy
 * 
 */

public class TypeSelectionWizard extends SOABaseWizard {

	private TypeSelectionWizardPage typeSelectionWizardPage = null;
	private SimpleTypeWizardGeneralPage simpleTypeWizardGeneralPage = null;
	private ComplexTypeWizardGeneralPage complexTypeWizardGeneralPage = null;
	private ComplexTypeWizardElementPage complexTypeCCWizardElementPage = null;
	private ComplexTypeWizardElementPage complexTypeWizardElementPage = null;
	private ComplexTypeWizardAttribPage complexTypeWizardAttributePage = null;
	private EnumTypeWizardGeneralPage enumTypeWizardGeneralPage = null;
	private EnumTypeWizardDetailsPage enumTypeWizardDetailsPage = null;
	private ComplexTypeSCWizardGeneralPage complexTypeSCWizardGeneralPage = null;
	private ComplexTypeCCWizardGeneralPage complexTypeCCWizardGeneralPage = null;

	private Map<SOAXSDTemplateSubType, String> templateCategoryMap;

	private static final SOALogger logger = SOALogger.getLogger();
	private String typeLibName = "";

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ebayopensource.turmeric.eclipse.ui.SOABaseWizard#getContentPages()
	 */
	@Override
	public IWizardPage[] getContentPages() {
		try {
			setSelectedProjectName(getSelection());
		} catch (CoreException e) {
			logger.error(e);
		}
		List<IWizardPage> pages = new ArrayList<IWizardPage>();
		typeSelectionWizardPage = new TypeSelectionWizardPage();
		pages.add(typeSelectionWizardPage);
		templateCategoryMap = TypeLibraryUtil.getTemplateCategoryMap();
		// instantiate pages only if there are folders in the config plugin
		// templateCategoryMap keeps track of the folders that need to be shown
		// based on their availability in config plugin
		if (templateCategoryMap.containsKey(SOAXSDTemplateSubType.SIMPLE)) {
			simpleTypeWizardGeneralPage = new SimpleTypeWizardGeneralPage(
					typeLibName);
			pages.add(simpleTypeWizardGeneralPage);
		}
		if (templateCategoryMap.containsKey(SOAXSDTemplateSubType.ENUM)) {
			enumTypeWizardGeneralPage = new EnumTypeWizardGeneralPage(
					typeLibName);
			enumTypeWizardDetailsPage = new EnumTypeWizardDetailsPage();
			pages.add(enumTypeWizardGeneralPage);
			pages.add(enumTypeWizardDetailsPage);
		}
		if (templateCategoryMap.containsKey(SOAXSDTemplateSubType.COMPLEX)) {
			complexTypeWizardGeneralPage = new ComplexTypeWizardGeneralPage(
					typeLibName);
			complexTypeWizardElementPage = new ComplexTypeWizardElementPage(
					new ArrayList<ElementTableModel>());
			pages.add(complexTypeWizardGeneralPage);
			pages.add(complexTypeWizardElementPage);
		}
		if (templateCategoryMap
				.containsKey(SOAXSDTemplateSubType.COMPLEX_SIMPLECONTENT)) {
			complexTypeSCWizardGeneralPage = new ComplexTypeSCWizardGeneralPage(
					typeLibName);
			complexTypeWizardAttributePage = new ComplexTypeWizardAttribPage();
			pages.add(complexTypeSCWizardGeneralPage);
			pages.add(complexTypeWizardAttributePage);
		}
		if (templateCategoryMap
				.containsKey(SOAXSDTemplateSubType.COMPLEX_COMPLEXCONTENT)) {
			complexTypeCCWizardGeneralPage = new ComplexTypeCCWizardGeneralPage(
					typeLibName);
			complexTypeCCWizardElementPage = new ComplexTypeWizardElementPage(
					new ArrayList<ElementTableModel>());
			pages.add(complexTypeCCWizardGeneralPage);
			pages.add(complexTypeCCWizardElementPage);
		}
		return pages.toArray(new IWizardPage[pages.size()]);
	}

	@Override
	public IWizardPage getNextPage(IWizardPage page) {
		// logic for finding out the next page
		if (page instanceof TypeSelectionWizardPage) {
			if (((TypeSelectionWizardPage) page).isSimpleType()) {
				return simpleTypeWizardGeneralPage;
			} else if (((TypeSelectionWizardPage) page).isEnumType()) {
				return enumTypeWizardGeneralPage;
			} else if (((TypeSelectionWizardPage) page).isComplexType()) {
				return complexTypeWizardGeneralPage;
			} else if (((TypeSelectionWizardPage) page).isComplexSCType()) {
				return complexTypeSCWizardGeneralPage;
			} else if (((TypeSelectionWizardPage) page).isComplexCCType()) {
				return complexTypeCCWizardGeneralPage;
			}
		} else if (page == enumTypeWizardGeneralPage) {
			return enumTypeWizardDetailsPage;
		} else if (page == complexTypeSCWizardGeneralPage) {
			return complexTypeWizardAttributePage;
		} else if (page == complexTypeCCWizardGeneralPage) {
			complexTypeCCWizardElementPage.dialogChanged();
			return complexTypeCCWizardElementPage;
		} else if (page == complexTypeWizardGeneralPage) {
			return complexTypeWizardElementPage;
		}
		return null;
	}

	protected void setSelectedProjectName(IStructuredSelection selection)
			throws CoreException {
		if (selection != null) {
			if (selection.getFirstElement() != null) {
				if (selection.getFirstElement() instanceof IJavaElement) {
					IProject project = ((IJavaElement) selection
							.getFirstElement()).getJavaProject().getProject();
					if (project.hasNature(TypeLibraryProjectNature
							.getTypeLibraryNatureId())) {
						typeLibName = project.getName();
					}
				}
			}
		}
	}

	@Override
	public boolean canFinish() {
		// if pre-build validation fails, then typeSelectionWizardPage doesn't
		// get instantiated
		if (typeSelectionWizardPage == null)
			return true;
		if (getContainer().getCurrentPage() == typeSelectionWizardPage)
			return false;

		if (typeSelectionWizardPage.isSimpleType())
			return (simpleTypeWizardGeneralPage.isPageComplete() && getContainer()
					.getCurrentPage() == simpleTypeWizardGeneralPage);
		else if (typeSelectionWizardPage.isEnumType())
			return (enumTypeWizardGeneralPage.isPageComplete()
					&& getContainer().getCurrentPage() == enumTypeWizardDetailsPage && enumTypeWizardDetailsPage
					.isPageComplete());
		else if (typeSelectionWizardPage.isComplexType())
			return (complexTypeWizardGeneralPage.isPageComplete() && complexTypeWizardElementPage
					.isPageComplete());
		// getContainer().getCurrentPage() == complexTypeWizardElementPage &&
		else if (typeSelectionWizardPage.isComplexSCType())
			return (complexTypeSCWizardGeneralPage.isPageComplete() && complexTypeWizardAttributePage
					.isPageComplete());
		// getContainer().getCurrentPage() == complexTypeWizardAttributePage &&
		else if (typeSelectionWizardPage.isComplexCCType())
			return (complexTypeCCWizardGeneralPage.isPageComplete() && complexTypeCCWizardElementPage
					.isPageComplete());
		// getContainer().getCurrentPage() == complexTypeCCWizardElementPage &&
		return true;
	}

	private TypeParamModel paramModel = null;

	@Override
	public boolean performFinish() {
		try {
			final WorkspaceModifyOperation operation = new WorkspaceModifyOperation() {
				@Override
				protected void execute(IProgressMonitor monitor)
						throws CoreException, InvocationTargetException,
						InterruptedException {
					final long startTime = System.currentTimeMillis();
					final int totalWork = ProgressUtil.PROGRESS_STEP * 30;
					monitor.beginTask("Creating new schema type", totalWork);
					ProgressUtil.progressOneStep(monitor);

					try {
						paramModel = null;
						// creation of different types
						if (typeSelectionWizardPage.isSimpleType()) {
							paramModel = createSimpleType(startTime, monitor);
						} else if (typeSelectionWizardPage.isEnumType()) {
							paramModel = createEnumType(startTime, monitor);
						} else if (typeSelectionWizardPage.isComplexType()) {
							paramModel = createComplexType(startTime, monitor);
						} else if (typeSelectionWizardPage.isComplexSCType()) {
							paramModel = createComplexSCType(startTime, monitor);
						} else if (typeSelectionWizardPage.isComplexCCType()) {
							paramModel = createComplexCCType(startTime, monitor);
						}
						if (paramModel != null) {
							BuildSystemUtil
									.updateSOAClasspathContainer(WorkspaceUtil
											.getProject(paramModel
													.getTypeLibraryName()));
						}
					} catch (Exception e) {
						logger.error(e);
						throw new SOATypeCreationFailedException(
								"Failed to create new schema type", e);
					} finally {
						monitor.done();
					}
				}
			};
			getContainer().run(false, true, operation);

			// another operation for making sure the classpath container to be
			// refreshed
			if (paramModel != null) {
				getContainer().run(false, true, new WorkspaceModifyOperation() {

					@Override
					protected void execute(IProgressMonitor monitor)
							throws CoreException, InvocationTargetException,
							InterruptedException {
						final int totalWork = ProgressUtil.PROGRESS_STEP * 10;
						monitor.beginTask(
								"Calling code gen for type generation",
								totalWork);
						ProgressUtil.progressOneStep(monitor);

						final IProject project = WorkspaceUtil
								.getProject(paramModel.getTypeLibraryName());
						final String typeLibName = paramModel
								.getTypeLibraryName();
						final String typeName = paramModel.getTypeName();
						final String version = paramModel.getVersion();

						try {
							TypeCreator.callCodegen(project, typeLibName,
									typeName);
							ProgressUtil.progressOneStep(monitor);
							WorkspaceUtil.refresh(project);
							ProgressUtil.progressOneStep(monitor);
							String xsdFileName = TypeLibraryUtil
									.getXsdFileLocation(typeName, project);
							ProgressUtil.progressOneStep(monitor);
							TypeCreator.postProcessTypeCreation(typeName,
									version, typeLibName, project
											.getFile(xsdFileName));
						} catch (Exception e) {
							logger.error(e);
							throw new SOATypeCreationFailedException(
									"Failed to create new schema type", e);
						} finally {
							monitor.done();
						}
					}

				});
			}
			return true;
		} catch (Exception exception) {
			logger.error(exception);
			try {
				deleteXSDOnError();
			} catch (Exception exception1) {
				// swallow this exception
				logger.error(exception1);
			}
			UIUtil
					.showErrorDialog(
							UIUtil.getActiveShell(),
							"New Type Creation Failed",
							"Plugin encountered some problems while creating the type.",
							exception);
			return false;
		}
	}

	private void deleteXSDOnError() throws Exception {
		String typeName = "";
		if (typeSelectionWizardPage.isSimpleType()) {
			typeLibName = simpleTypeWizardGeneralPage.getTypeLibraryName();
			typeName = simpleTypeWizardGeneralPage.getResourceName();
		} else if (typeSelectionWizardPage.isEnumType()) {
			typeLibName = enumTypeWizardGeneralPage.getTypeLibraryName();
			typeName = enumTypeWizardGeneralPage.getResourceName();
		} else if (typeSelectionWizardPage.isComplexType()) {
			typeLibName = complexTypeWizardGeneralPage.getTypeLibraryName();
			typeName = complexTypeWizardGeneralPage.getResourceName();
		} else if (typeSelectionWizardPage.isComplexSCType()) {
			typeLibName = complexTypeSCWizardGeneralPage.getTypeLibraryName();
			typeName = complexTypeSCWizardGeneralPage.getResourceName();
		} else if (typeSelectionWizardPage.isComplexCCType()) {
			typeLibName = complexTypeCCWizardGeneralPage.getTypeLibraryName();
			typeName = complexTypeCCWizardGeneralPage.getResourceName();
		}
		IProject project = WorkspaceUtil.getProject(enumTypeWizardGeneralPage
				.getTypeLibraryName());
		if (project != null) {
			IFile xsdFile = project.getFile(TypeLibraryUtil.getXsdFileLocation(
					typeName, project));
			xsdFile.delete(true, ProgressUtil.getDefaultMonitor(null));
			project.refreshLocal(IResource.DEPTH_INFINITE, ProgressUtil
					.getDefaultMonitor(null));
			SOAGlobalRegistryAdapter.getInstance().invalidateRegistry();
			TypeLibSynhcronizer.syncronizeAllXSDsandDepXml(project);
			TypeLibSynhcronizer.synchronizeTypeDepandProjectDep(project,
					ProgressUtil.getDefaultMonitor(null));
		}
	}

	public SimpleTypeParamModel createSimpleType(long startTime,
			IProgressMonitor monitor) throws Exception {
		SimpleTypeParamModel paramModel = new SimpleTypeParamModel();
		paramModel.setTypeName(simpleTypeWizardGeneralPage.getResourceName());
		paramModel.setTypeLibraryName(simpleTypeWizardGeneralPage
				.getTypeLibraryName());
		paramModel.setVersion(simpleTypeWizardGeneralPage.getVersionValue());
		paramModel
				.setNamespace(simpleTypeWizardGeneralPage.getNamespaceValue());
		if (templateCategoryMap.containsKey(SOAXSDTemplateSubType.SIMPLE))
			paramModel.setTemplateCategory(SOAXSDTemplateSubType.SIMPLE);
		paramModel.setTemplateName(simpleTypeWizardGeneralPage
				.getTemplateValue());
		paramModel.setBaseType(simpleTypeWizardGeneralPage.getRawBaseType());
		paramModel.setDescription(simpleTypeWizardGeneralPage.getDocText());
		TypeCreator.createType(paramModel, monitor);
		final TrackingEvent event = new TrackingEvent("SimpleType", new Date(
				startTime), System.currentTimeMillis() - startTime);
		GlobalRepositorySystem.instanceOf().getActiveRepositorySystem()
				.trackingUsage(event);
		return paramModel;
	}

	public EnumTypeParamModel createEnumType(long startTime,
			IProgressMonitor monitor) throws Exception {
		EnumTypeParamModel paramModel = new EnumTypeParamModel();
		paramModel.setTypeName(enumTypeWizardGeneralPage.getResourceName());
		paramModel.setTypeLibraryName(enumTypeWizardGeneralPage
				.getTypeLibraryName());
		paramModel.setVersion(enumTypeWizardGeneralPage.getVersionValue());
		paramModel.setNamespace(enumTypeWizardGeneralPage.getNamespaceValue());
		if (templateCategoryMap.containsKey(SOAXSDTemplateSubType.ENUM))
			paramModel.setTemplateCategory(SOAXSDTemplateSubType.ENUM);
		paramModel
				.setTemplateName(enumTypeWizardGeneralPage.getTemplateValue());
		paramModel.setBaseType(enumTypeWizardGeneralPage.getRawBaseType());
		paramModel.setDescription(enumTypeWizardGeneralPage.getDocText());
		paramModel.setEnumTableModel(enumTypeWizardDetailsPage
				.getEnumTableModel());
		TypeCreator.createType(paramModel, monitor);
		final TrackingEvent event = new TrackingEvent("EnumType", new Date(
				startTime), System.currentTimeMillis() - startTime);
		GlobalRepositorySystem.instanceOf().getActiveRepositorySystem()
				.trackingUsage(event);
		return paramModel;
	}

	public ComplexTypeParamModel createComplexType(long startTime,
			IProgressMonitor monitor) throws Exception {
		ComplexTypeParamModel paramModel = new ComplexTypeParamModel();
		paramModel.setTypeName(complexTypeWizardGeneralPage.getResourceName());
		paramModel.setTypeLibraryName(complexTypeWizardGeneralPage
				.getTypeLibraryName());
		paramModel.setVersion(complexTypeWizardGeneralPage.getVersionValue());
		paramModel.setNamespace(complexTypeWizardGeneralPage
				.getNamespaceValue());
		if (templateCategoryMap.containsKey(SOAXSDTemplateSubType.COMPLEX))
			paramModel.setTemplateCategory(SOAXSDTemplateSubType.COMPLEX);
		paramModel.setTemplateName(complexTypeWizardGeneralPage
				.getTemplateValue());
		// paramModel.setBaseType(complexTypeWizardGeneralPage.getBaseTypeValue());
		paramModel.setElementTableModel(complexTypeWizardElementPage
				.getElementTableModel());
		paramModel.setDescription(complexTypeWizardGeneralPage.getDocText());
		TypeCreator.createType(paramModel, monitor);
		final TrackingEvent event = new TrackingEvent("ComplexType", new Date(
				startTime), System.currentTimeMillis() - startTime);
		GlobalRepositorySystem.instanceOf().getActiveRepositorySystem()
				.trackingUsage(event);
		return paramModel;
	}

	public ComplexTypeSCParamModel createComplexSCType(long startTime,
			IProgressMonitor monitor) throws Exception {
		ComplexTypeSCParamModel paramModel = new ComplexTypeSCParamModel();
		paramModel
				.setTypeName(complexTypeSCWizardGeneralPage.getResourceName());
		paramModel.setTypeLibraryName(complexTypeSCWizardGeneralPage
				.getTypeLibraryName());
		paramModel.setVersion(complexTypeSCWizardGeneralPage.getVersionValue());
		paramModel.setNamespace(complexTypeSCWizardGeneralPage
				.getNamespaceValue());
		if (templateCategoryMap
				.containsKey(SOAXSDTemplateSubType.COMPLEX_SIMPLECONTENT))
			paramModel
					.setTemplateCategory(SOAXSDTemplateSubType.COMPLEX_SIMPLECONTENT);
		paramModel.setTemplateName(complexTypeSCWizardGeneralPage
				.getTemplateValue());
		paramModel.setBaseType(complexTypeSCWizardGeneralPage.getRawBaseType());
		paramModel.setDescription(complexTypeSCWizardGeneralPage.getDocText());
		paramModel.setAttribTableModel(complexTypeWizardAttributePage
				.getAttribTableModel());
		TypeCreator.createType(paramModel, monitor);
		final TrackingEvent event = new TrackingEvent(
				"ComplexType(Simple Content)", new Date(startTime), System
						.currentTimeMillis()
						- startTime);
		GlobalRepositorySystem.instanceOf().getActiveRepositorySystem()
				.trackingUsage(event);
		return paramModel;
	}

	public ComplexTypeCCParamModel createComplexCCType(long startTime,
			IProgressMonitor monitor) throws Exception {
		ComplexTypeCCParamModel paramModel = new ComplexTypeCCParamModel();
		paramModel
				.setTypeName(complexTypeCCWizardGeneralPage.getResourceName());
		paramModel.setTypeLibraryName(complexTypeCCWizardGeneralPage
				.getTypeLibraryName());
		paramModel.setVersion(complexTypeCCWizardGeneralPage.getVersionValue());
		paramModel.setNamespace(complexTypeCCWizardGeneralPage
				.getNamespaceValue());
		if (templateCategoryMap
				.containsKey(SOAXSDTemplateSubType.COMPLEX_COMPLEXCONTENT))
			paramModel
					.setTemplateCategory(SOAXSDTemplateSubType.COMPLEX_COMPLEXCONTENT);
		paramModel.setTemplateName(complexTypeCCWizardGeneralPage
				.getTemplateValue());
		paramModel.setBaseType(complexTypeCCWizardGeneralPage.getRawBaseType());
		paramModel.setDescription(complexTypeCCWizardGeneralPage.getDocText());
		paramModel.setElementTableModel(complexTypeCCWizardElementPage
				.getElementTableModel());
		TypeCreator.createType(paramModel, monitor);
		final TrackingEvent event = new TrackingEvent(
				"ComplexType(Complex Content)", new Date(startTime), System
						.currentTimeMillis()
						- startTime);
		GlobalRepositorySystem.instanceOf().getActiveRepositorySystem()
				.trackingUsage(event);
		changePerspective();
		return paramModel;
	}

	public IStatus preValidate() {
		return Status.OK_STATUS;
	}
}
