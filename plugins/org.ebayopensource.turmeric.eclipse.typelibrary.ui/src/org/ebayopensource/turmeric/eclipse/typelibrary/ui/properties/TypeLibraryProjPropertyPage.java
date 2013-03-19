/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.typelibrary.ui.properties;

import java.lang.reflect.InvocationTargetException;

import org.apache.commons.lang.StringUtils;
import org.ebayopensource.turmeric.eclipse.core.logging.SOALogger;
import org.ebayopensource.turmeric.eclipse.core.model.typelibrary.TypeLibraryParamModel;
import org.ebayopensource.turmeric.eclipse.core.resources.constants.SOAProjectConstants.SupportedProjectType;
import org.ebayopensource.turmeric.eclipse.exception.resources.SOAResourceModifyFailedException;
import org.ebayopensource.turmeric.eclipse.exception.validation.ValidationInterruptedException;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.GlobalRepositorySystem;
import org.ebayopensource.turmeric.eclipse.repositorysystem.utils.TurmericServiceUtils;
import org.ebayopensource.turmeric.eclipse.resources.model.ISOAProject;
import org.ebayopensource.turmeric.eclipse.resources.util.SOAServiceUtil;
import org.ebayopensource.turmeric.eclipse.typelibrary.resources.SOAMessages;
import org.ebayopensource.turmeric.eclipse.typelibrary.resources.model.SOATypeLibraryProject;
import org.ebayopensource.turmeric.eclipse.typelibrary.resources.model.SOATypeLibraryProjectResolver;
import org.ebayopensource.turmeric.eclipse.typelibrary.ui.TypeLibraryUtil;
import org.ebayopensource.turmeric.eclipse.utils.plugin.ProgressUtil;
import org.ebayopensource.turmeric.eclipse.utils.ui.UIUtil;
import org.ebayopensource.turmeric.eclipse.validator.utils.common.ServiceVersionValidator;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbenchPropertyPage;
import org.eclipse.ui.actions.WorkspaceModifyOperation;
import org.osgi.framework.Version;


/**
 * The Class TypeLibraryProjPropertyPage.
 *
 * @author mzang
 */

public class TypeLibraryProjPropertyPage extends PreferencePage implements
        IWorkbenchPropertyPage {
	private SOATypeLibraryProject typelibProject;

    private TypeLibraryParamModel model;

    private static final SOALogger logger = SOALogger.getLogger();

    private Text typeLibraryVersion;

    /* (non-Javadoc)
     * @see org.eclipse.jface.preference.PreferencePage#createContents(org.eclipse.swt.widgets.Composite)
     */
    @Override
    protected Control createContents(Composite parent) {
        noDefaultAndApplyButton();
        // create top panel
        Composite result = new Composite(parent, SWT.NONE);
        try {
            GridLayout layout = new GridLayout();
            layout.marginHeight = convertVerticalDLUsToPixels(IDialogConstants.VERTICAL_MARGIN);
            layout.marginWidth = 0;
            layout.verticalSpacing = convertVerticalDLUsToPixels(10);
            layout.horizontalSpacing = convertHorizontalDLUsToPixels(IDialogConstants.HORIZONTAL_SPACING);
            result.setLayout(layout);
            result.setLayoutData(new GridData(GridData.FILL_BOTH));

            createGroups(result);

            Dialog.applyDialogFont(result);
        } catch (Exception e) {
            logger.error(e);
            UIUtil.showErrorDialog(e);
        }
        return result;
    }

    /**
     * @param parent
     * @throws Exception
     */
    private void createGroups(Composite createTypeLibraryGroups)
            throws Exception {

        if (typelibProject == null || model == null) {
            return;
        }
        createTypeLibraryGroups(createTypeLibraryGroups);

    }

    private void createTypeLibraryGroups(Composite parent) {
        Group group = createPropertyGroup(parent);
        addTypeLibraryName(group);
        addTypeLibraryVersion(group, true);
        addTypeLiraryCategory(group);
        addTypeLiraryNamespace(group);
    }

    /**
     * @param parent
     * @return
     */
    private Group createPropertyGroup(Composite parent) {
        Group typeLibraryPropertyGroup = new Group(parent, SWT.NONE);
        typeLibraryPropertyGroup.setLayout(new GridLayout(2, false));
        typeLibraryPropertyGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL,
                true, false));
        typeLibraryPropertyGroup.setText("Type Library Properties");

        return typeLibraryPropertyGroup;
    }

	/**
	 * {@inheritDoc}
	 */
    @Override
	public void createControl(Composite parent) {
        super.createControl(parent);
    }

	/**
	 * {@inheritDoc}
	 */
    @Override
	public IAdaptable getElement() {
        return null;
    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.preference.PreferencePage#performOk()
     */
    @Override
    public boolean performOk() {
		try {
			if (typeLibraryVersion == null) {
				return false;
			}
			final String newVersion = typeLibraryVersion.getText().trim();
			final WorkspaceModifyOperation operation = new WorkspaceModifyOperation() {

				@Override
				protected void execute(IProgressMonitor monitor)
						throws CoreException, InvocationTargetException,
						InterruptedException {
					monitor.beginTask("Modifying project properties->"
							+ typelibProject.getProjectName(),
							ProgressUtil.PROGRESS_STEP * 10);
					try {
						TypeLibraryUtil.updateTypeLibraryVersion(newVersion,
								typelibProject, monitor);
					} catch (Exception e) {
						logger.error(e);
						throw new SOAResourceModifyFailedException(
								"Failed to modify project dependencies for project->"
										+ typelibProject.getProjectName(), e);
					} finally {
						monitor.done();
					}
				}
			};

			try {
				new ProgressMonitorDialog(getControl().getShell()).run(true,
						true, operation);

			} catch (Exception e) {
				logger.error(e);
				UIUtil.showErrorDialog(e);
				return false;
			}

			return true;
		} catch (Exception e) {
			logger.error(e);
			UIUtil.showErrorDialog(
					"Error Occurred While Saving Project Properties", e);
			return false;
		}
    }

    /**
     * 
     * create a text and a label.
     * 
     * @param composite
     * @param label
     * @param isEditable
     * @param defaultValue
     */
    private Text createLabeledText(Composite composite, String label,
            boolean isEditable, String defaultValue) {
        new Label(composite, SWT.LEFT).setText(label);
        Text text = new Text(composite, SWT.BORDER);
        text.setEditable(isEditable);
        text.setText(StringUtils.defaultString(defaultValue));
        text.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL
                | GridData.FILL_HORIZONTAL));
        return text;
    }

    private void addTypeLibraryVersion(Composite group, final boolean editable) {

        this.typeLibraryVersion = createLabeledText(group,
                "Type Library Version:", editable, model.getVersion());
        this.typeLibraryVersion.addModifyListener(new ModifyListener() {

            @Override
			public void modifyText(ModifyEvent e) {
                try {
                    validateTypeLibraryProperties();
                } catch (ValidationInterruptedException e1) {
                    processException(e1);
                }
            }
        });

    }

    private boolean validateTypeLibraryProperties()
            throws ValidationInterruptedException {
        if (typeLibraryVersion != null) {
            String version = typeLibraryVersion.getText();
            final IStatus validationModel = ServiceVersionValidator
                    .getInstance().validate(version);

            if (validationModel.isOK() == false) {
                setErrorMessage(validationModel.getMessage());
                return false;
            }

            Version newVerion = new Version(version);
            Version oldVersion = new Version(model.getVersion());
            if (newVerion.getMajor() != oldVersion.getMajor()) {
                setErrorMessage(SOAMessages.ERR_CHANGE_MAJOR_VERSION_NOT_ALLOWED);
                return false;
            }

            int compareRet = newVerion.compareTo(oldVersion);
            if (compareRet < 0) {
                setErrorMessage(SOAMessages.ERR_SMALLER_VERSION_NOT_ALLOWED);
                return false;
            }

            IStatus status = ServiceVersionValidator.getInstance().validate(
                    newVerion.toString());
            if (status.isOK() == false) {
                setErrorMessage(status.getMessage());
                return false;
            }

        }

        setValid(true);
        setErrorMessage(null);
        return true;
    }

    private void addTypeLibraryName(Composite group) {
        createLabeledText(group, "Type Library Name:", false, model
                .getTypeLibraryName());
    }

    private void addTypeLiraryCategory(Composite group) {
        createLabeledText(group, "Category Name:", false, model.getCategory());
    }

    private void addTypeLiraryNamespace(Composite group) {
        createLabeledText(group, "Namespace:", false, model.getNamespace());
    }

	/**
	 * {@inheritDoc}
	 */
    @Override
	public void setElement(IAdaptable element) {
        try {
            if (element.getAdapter(IProject.class) instanceof IProject) {
                IProject project = (IProject) element
                        .getAdapter(IProject.class);

                model = SOATypeLibraryProjectResolver
                        .loadTypeLibraryModel(project);

                if (SOAServiceUtil.hasNatures(project,   
						GlobalRepositorySystem.instanceOf().getActiveRepositorySystem()
						.getProjectNatureId(SupportedProjectType.TYPE_LIBRARY))) {
                    typelibProject = (SOATypeLibraryProject) GlobalRepositorySystem
                            .instanceOf().getActiveRepositorySystem()
                            .getAssetRegistry().getSOAProject(project);
                }
            }
        } catch (Exception e) {
            logger.error(e);
        }
    }

    /**
     * Gets the soa project.
     *
     * @return the soa project
     */
    public ISOAProject getSoaProject() {
        return typelibProject;
    }

    private void processException(Exception exception) {
        if (exception != null) {
            UIUtil.showErrorDialog(exception);
            SOALogger.getLogger().error(exception);
        }
    }

}
