/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.errorlibrary.properties.ui;

import org.apache.commons.lang.StringUtils;
import org.ebayopensource.turmeric.eclipse.core.logging.SOALogger;
import org.ebayopensource.turmeric.eclipse.resources.model.ISOAProject;
import org.ebayopensource.turmeric.eclipse.utils.ui.UIUtil;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
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

 
/**
 * The Class ErrorLibraryProjPropertyPage.
 *
 * @author yayu
 */
public class ErrorLibraryProjPropertyPage extends PreferencePage implements
IWorkbenchPropertyPage {

	 private static final SOALogger logger = SOALogger.getLogger();
	 private ISOAProject errorLibProject;
	 
	 private Text errorLibVersion;
	
	/**
	 * Instantiates a new error library proj property page.
	 */
	public ErrorLibraryProjPropertyPage() {
		super();
	}

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

		/*if (typelibProject == null || model == null) {
			return;
		}*/
		createErrorLibraryGroups(createTypeLibraryGroups);

	}

	private void createErrorLibraryGroups(Composite parent) {
		Group group = createPropertyGroup(parent);
		addErrorLibraryName(group);
		addErrorLibraryVersion(group, true);
		addErrorLiraryLocale(group);
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
        typeLibraryPropertyGroup.setText("Error Library Properties");

        return typeLibraryPropertyGroup;
    }

    /**
     * {@inheritDoc}
     * 
     * Always returns null.
     */
    public IAdaptable getElement() {
        return null;
    }
    
    /* (non-Javadoc)
     * @see org.eclipse.jface.preference.PreferencePage#performOk()
     */
    @Override
    public boolean performOk() {
        return super.performOk();
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

    private void addErrorLibraryVersion(Composite group, final boolean editable) {

        this.errorLibVersion = createLabeledText(group,
                "Error Library Version:", editable, "1.0.0");
        this.errorLibVersion.addModifyListener(new ModifyListener() {

            public void modifyText(ModifyEvent e) {
               /* try {
                    validateTypeLibraryProperties();
                } catch (ValidationInterruptedException e1) {
                    processException(e1);
                }*/
            }
        });

    }

   /* private boolean validateErrorLibraryProperties()
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
    }*/

    private void addErrorLibraryName(Composite group) {
        createLabeledText(group, "Error Library Name:", false, "NewErrorLibrary");
    }

    private void addErrorLiraryLocale(Composite group) {
        createLabeledText(group, "Locale:", false, "en");
    }

    /**
     * {@inheritDoc}
     */
    public void setElement(IAdaptable element) {
        try {
            if (element.getAdapter(IProject.class) instanceof IProject) {
                IProject project = (IProject) element
                        .getAdapter(IProject.class);

               /* model = SOATypeLibraryProjectResolver
                        .loadTypeLibraryModel(project);

                if (SOAServiceUtil.isSOATypeLibraryProject(project)) {
                    typelibProject = (SOATypeLibraryProject) GlobalRepositorySystem
                            .instanceOf().getActiveRepositorySystem()
                            .getAssetRegistry().getSOAProject(project);
                }*/
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
        return errorLibProject;
    }


}
