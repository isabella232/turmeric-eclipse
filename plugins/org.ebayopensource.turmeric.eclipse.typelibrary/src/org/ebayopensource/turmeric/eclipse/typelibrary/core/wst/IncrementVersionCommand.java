/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.typelibrary.core.wst;

import javax.xml.namespace.QName;

import org.apache.commons.lang.StringUtils;
import org.ebayopensource.turmeric.eclipse.buildsystem.core.SOAGlobalRegistryAdapter;
import org.ebayopensource.turmeric.eclipse.logging.SOALogger;
import org.ebayopensource.turmeric.eclipse.typelibrary.actions.ActionUtil;
import org.ebayopensource.turmeric.eclipse.typelibrary.buildsystem.TypeLibSynhcronizer;
import org.ebayopensource.turmeric.eclipse.typelibrary.utils.TypeLibraryUtil;
import org.ebayopensource.turmeric.eclipse.utils.ui.UIUtil;
import org.ebayopensource.turmeric.tools.library.SOATypeRegistry;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.xsd.XSDSchema;
import org.osgi.framework.Version;


/**
 * @author smathew T
 * 
 * 
 * This is the increment version actionon the xsd menu
 * 
 * Increments the version based on SOA Governance rules
 */
public class IncrementVersionCommand extends AbastractTypeLibraryAtion {
	private static final SOALogger logger = SOALogger.getLogger();

	public void setActivePart(final IAction action,
			final IWorkbenchPart targetPart) {
	}

	public void run(final IAction action) {
		try {
			if (WTPTypeLibUtil.validateEditorForContextMenus(editorPart)) {
				if (super.doValidation(false) == false) {
					return;
				}
				Object adaptedObject = TypeLibraryUtil
						.getAdapterClassFromWTPEditors(editorPart);
				if (adaptedObject != null) {
					if (adaptedObject instanceof XSDSchema) {
						if (ActionUtil
								.validateEditor(editorPart, adaptedObject)) {
							performVersionChangeXSDEditor((XSDSchema) adaptedObject);
						}
					} else {
						showCommonErrorDialog(null);
					}
				} else {
					showCommonErrorDialog(null);
				}

			}
		} catch (Exception e) {
			SOALogger.getLogger().error(e);
			showCommonErrorDialog(e);
		} finally {
			if (SOALogger.DEBUG)
				logger.exiting();
		}
	}

	private void performVersionChangeXSDEditor(XSDSchema schema) {

		try {
			final Version oldVersion = schema.getVersion() == null ? Version.emptyVersion
					: new Version(StringUtils
							.defaultString(schema.getVersion()));

			IInputValidator inputValidator = new IInputValidator() {
				public String isValid(String newText) {
					try {
						if (new Version(newText).compareTo(oldVersion) <= 0) {
							return "Please enter a higher version than the original one.";
						}
						if (new Version(newText).getMajor() > oldVersion
								.getMajor()) {
							return "It is not allowed to bump up the major version of an existing type.";
						}
						if (!StringUtils.isEmpty(new Version(newText)
								.getQualifier())) {
							return "Version is allowed to have only three parts.";
						}

					} catch (Exception exception) {
						return "Enter a valid version.";
					}
					return null;
				}

			};

			InputDialog inputDialog = new InputDialog(UIUtil.getActiveShell(),
					"Version", "Enter the new version.", oldVersion.toString(),
					inputValidator);
			if (inputDialog.open() == Window.OK) {
				schema.setVersion(inputDialog.getValue());
				QName type = TypeLibraryUtil.toQName(getSelectedFile());
				SOATypeRegistry typeRegistry = SOAGlobalRegistryAdapter.getInstance().getGlobalRegistry();
				typeRegistry.getType(type)
						.setVersion(inputDialog.getValue());
				TypeLibSynhcronizer.syncronizeXSDandDepXml(schema,
						getSelectedFile().getProject(), type);
				MessageDialog.openInformation(UIUtil.getActiveShell(),
						"The operation was performed successfully.",
						"Successfully updated the version of this type.");
			}
		} catch (Exception exception) {
			throw new RuntimeException("Could not perform this operation:"
					+ exception.getMessage(), exception);
		}

	}

	public void showCommonErrorDialog(Exception exception) {
		if (exception == null) {
			UIUtil
					.showErrorDialog(
							null,
							"This action is not available for this selection.",
							"Action not available",
							"Please check if the project is a typeLibrary project and the seleted file is a valid xsd type.");
		} else {
			UIUtil
					.showErrorDialog(null, exception.getMessage(),
							"This action could not be performed",
							"Please check the below message and take appropriate action");
		}
	}

}
