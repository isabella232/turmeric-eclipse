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
import java.util.List;

import org.ebayopensource.turmeric.eclipse.core.logging.SOALogger;
import org.ebayopensource.turmeric.eclipse.typelibrary.ui.wizards.pages.ImportTypesWizardPage;
import org.ebayopensource.turmeric.eclipse.typelibrary.ui.wizards.pages.NewTypeLibraryWizardPage;
import org.ebayopensource.turmeric.eclipse.typelibrary.ui.TypeLibraryUtil;
import org.ebayopensource.turmeric.eclipse.typelibrary.utils.importtypes.TypeModel;
import org.ebayopensource.turmeric.eclipse.ui.wizards.AbstractTypeLibraryWizard;
import org.ebayopensource.turmeric.eclipse.utils.plugin.ProgressUtil;
import org.ebayopensource.turmeric.eclipse.utils.ui.UIUtil;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.ui.actions.WorkspaceModifyOperation;

/**
 * The Class ImportTypesWizard.
 */
public class ImportTypesWizard extends AbstractTypeLibraryWizard {

	/** The Constant IMPORT_MODE. */
	public static final int IMPORT_MODE = 0;
	
	/** The Constant EXPORT_MODE. */
	public static final int EXPORT_MODE = 1;

	private static String[][] DIALOG_LABELS = new String[2][];
	static {
		DIALOG_LABELS[0] = new String[] { "Import Types", "Import Types",
				"Import types to a Type Library project", "Types to Import" };
		DIALOG_LABELS[1] = new String[] { "Export Types", "Export Types",
				"Export types to a Type Library project", "Types to Export" };
	}
	private ImportTypesWizardPage typeSelectPage;
	private NewTypeLibraryWizardPage createTLProjectPage;

	private IProject typeLibProj;
	private String sourceFile;
	private int mode;

	/**
	 * Instantiates a new import types wizard.
	 *
	 * @param typeLibProj the type lib proj
	 * @param sourceFile the source file
	 * @param mode the mode
	 */
	public ImportTypesWizard(IProject typeLibProj, String sourceFile, int mode) {
		this.typeLibProj = typeLibProj;
		this.sourceFile = sourceFile;
		this.mode = mode;
	}

	@Override
	public IWizardPage[] getContentPages() {
		typeSelectPage = new ImportTypesWizardPage(DIALOG_LABELS[mode][0],
				DIALOG_LABELS[mode][1], DIALOG_LABELS[mode][2],
				DIALOG_LABELS[mode][3], typeLibProj, sourceFile);
		createTLProjectPage = new NewTypeLibraryWizardPage();
		return new IWizardPage[] { typeSelectPage, createTLProjectPage };
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean canFinish() {
		if (getContainer().getCurrentPage() == typeSelectPage) {
			return typeSelectPage.needNewTypeLibraryCreation() == false
					&& typeSelectPage.isPageComplete();
		} else if (getContainer().getCurrentPage() == createTLProjectPage) {
			return createTLProjectPage.isPageComplete();
		} else {
			return false;
		}
	}

	@Override
	public IWizardPage getNextPage(IWizardPage page) {
		if (page == typeSelectPage) {
			if (typeSelectPage.needNewTypeLibraryCreation() == true) {
				createTLProjectPage.setImportedTypes(typeSelectPage
						.getSelectedTypeModels());
				return createTLProjectPage;
			} else {
				return null;
			}
		} else if (page == createTLProjectPage) {
			return null;
		} else {
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see org.ebayopensource.turmeric.eclipse.ui.SOABaseWizard#performFinish()
	 */
	@Override
	public boolean performFinish() {

		String tlProjectNameTemp = null;
		String tlNamespaceTemp = null;
		if (typeSelectPage.needNewTypeLibraryCreation() == true) {
			NewTypeLibraryWizard createTLProj = new NewTypeLibraryWizard();
			createTLProj.setContainer(getContainer());
			createTLProj.setTypeLibWizardPage(createTLProjectPage);
			boolean projCreated = createTLProj.performFinish();
			if (projCreated == false) {
				return false;
			}
			tlProjectNameTemp = createTLProjectPage.getNameValue();
			tlNamespaceTemp = createTLProjectPage.getNamespaceValue();
		} else {
			tlProjectNameTemp = typeSelectPage.getSelectedTypeLibraryName();
			tlNamespaceTemp = typeSelectPage.getNamespace();
		}
		final String tlProjectName = tlProjectNameTemp;
		final String tlNamespace = tlNamespaceTemp;
		final List<TypeModel> types = typeSelectPage
				.getSelectedTypeImportModels();
		for (TypeModel model : types) {
			model.setNamespace(tlNamespace);
			model.setTypeLibName(tlProjectName);
		}
		final WorkspaceModifyOperation operation = new WorkspaceModifyOperation() {
			@Override
			protected void execute(IProgressMonitor monitor) {

				monitor.beginTask("Creating type ", ProgressUtil.PROGRESS_STEP
						* (types.size() * 3 + 10));

				TypeLibraryUtil.importTypesToTypeLibrarySAXP(types,
						tlProjectName, monitor);

				monitor.done();
			}
		};
		try {
			this.getContainer().run(false, false, operation);
			changePerspective();
		} catch (InvocationTargetException e) {
			logger.error(e);
			UIUtil.showErrorDialog(getShell(),
					"Error Occured During Type Creation ", null, e);
			if (SOALogger.DEBUG) {
				logger.exiting(false);
			}
		} catch (InterruptedException e) {
			logger.error(e);
			UIUtil.showErrorDialog(getShell(),
					"Error Occured During Type Creation", null, e);
			if (SOALogger.DEBUG)
				logger.exiting(false);
		}

		return true;

	}
}
