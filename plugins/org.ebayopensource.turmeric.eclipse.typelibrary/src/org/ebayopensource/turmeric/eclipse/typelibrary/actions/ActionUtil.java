/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.typelibrary.actions;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.ebayopensource.turmeric.eclipse.exception.validation.ValidationInterruptedException;
import org.ebayopensource.turmeric.eclipse.logging.SOALogger;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.GlobalRepositorySystem;
import org.ebayopensource.turmeric.eclipse.resources.constants.SOAProjectConstants;
import org.ebayopensource.turmeric.eclipse.typelibrary.core.SOATypeLibraryConstants;
import org.ebayopensource.turmeric.eclipse.typelibrary.utils.TypeLibraryUtil;
import org.ebayopensource.turmeric.eclipse.utils.collections.ListUtil;
import org.ebayopensource.turmeric.eclipse.utils.plugin.EclipseMessageUtils;
import org.ebayopensource.turmeric.eclipse.utils.ui.UIUtil;
import org.ebayopensource.turmeric.eclipse.validator.utils.ValidateUtil;
import org.ebayopensource.turmeric.eclipse.validator.utils.common.AbstractBaseAccessValidator;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.wst.wsdl.Definition;
import org.eclipse.xsd.XSDSchema;


/**
 * @author smathew
 * 
 * 
 * Util Class to cater all type library actions. Most of the actions are in this
 * package. The cope of this class is very limited.
 */
public class ActionUtil {

	/**
	 * 
	 * Validates the given editor and the adapted object. In Type Lib context
	 * these are the checks performed Editor should have either a wsdl or xsd
	 * file. Adapted object should either be an XSD Model object or WSDL Model
	 * object
	 * 
	 * @param editorPart2
	 * @param adaptedObject
	 *            either XSD Schema or WSDL Definiton
	 * @return false if the file extension of the editor part's file is not XSD
	 *         or WSDL input or the adapted object is not XSD or WSDL Model
	 * @throws ValidationInterruptedException 
	 */
	public static boolean validateEditor(IEditorPart editorPart2,
			Object adaptedObject) throws ValidationInterruptedException {
		if (editorPart2.getEditorInput() instanceof FileEditorInput) {

			IFile editorFile = ((FileEditorInput) editorPart2.getEditorInput())
					.getFile();
			if (adaptedObject instanceof XSDSchema) {
				if (!StringUtils.equalsIgnoreCase(
						editorFile.getFileExtension(),
						SOATypeLibraryConstants.XSD)) {
					String errorMessage = "The XSD Editor does not have a XSD file in it, but a "
							+ editorFile.getFileExtension()
							+ " file. This might be because you tried to edit the WSDL and opened up the schema in an Inline Schema editor. Please go to the WSDL Editor's Source tab and try importing it again.";
					UIUtil.openChoiceDialog("The type cannot be imported.",
							errorMessage, MessageDialog.INFORMATION);
					return false;
				}
			}
			if (adaptedObject instanceof Definition) {
				if (!StringUtils
						.equalsIgnoreCase(editorFile.getFileExtension(),
								SOAProjectConstants.WSDL)) {
					String errorMessage = "The WSDL Editor does not have a WSDL file in it, but a "
							+ editorFile.getFileExtension()
							+ " file. This operation is not supported in a non WSDL File.";
					UIUtil.openChoiceDialog("The type cannot be imported.",
							errorMessage, MessageDialog.INFORMATION);
					return false;
				}
			}

			final IProject project = editorFile.getProject();
			final IStatus status = validateTypeDependencyAndProjectConfigFile(project);

			final String messages = ValidateUtil
					.getFormattedStatusMessagesForAction(status);
			if (messages != null) {
				UIUtil.showErrorDialog(UIUtil.getActiveShell(), "Error",
						messages, (Throwable) null);
				return false;
			}

		}

		return true;
	}
	
	private static List<IResource> getTypeLibraryResources(
			final IProject project) {
		return getTypeLibraryResources(project, true);
	}

	/**
	 * returns the list of resources for readbility/writability checks.
	 * 
	 * @param project
	 * 
	 * @return
	 */
	private static List<IResource> getTypeLibraryResources(
			final IProject project, boolean needProjectConfigFile) {
		final List<IResource> resources = new ArrayList<IResource>(2);
		try {
			final IResource typeDepFile = TypeLibraryUtil
					.getDependencyFile(project);
			resources.add(typeDepFile);
			if (needProjectConfigFile == true) {
				final IFile configFile = GlobalRepositorySystem.instanceOf()
				.getActiveRepositorySystem().getAssetRegistry()
				.getProjectConfigurationFile(project);
				if (configFile != null)
					resources.add(configFile);
			}

		} catch (Exception e) {
			SOALogger.getLogger().warning(e);
		}
		return resources;
	}
	
	public static IStatus validateTypeDependencyAndProjectConfigFile(
			final IProject project, final IFile... additionalWritableFiles) 
	throws ValidationInterruptedException {
		return validateTypeDependencyAndProjectConfigFile(project, true, additionalWritableFiles);
	}

	/**
	 * validates the type lib project for the readability/writability aspect of
	 * some of the config files. The list of the config files are computed by
	 * the above API
	 * 
	 * @see org.ebayopensource.turmeric.eclipse.typelibrary.actions.ActionUtil#getTypeLibraryResources
	 * 
	 * @param project
	 * @return
	 * @throws ValidationInterruptedException 
	 */
	public static IStatus validateTypeDependencyAndProjectConfigFile(
			final IProject project, final boolean validateProjectConfigFile, 
			final IFile... additionalWritableFiles) 
	throws ValidationInterruptedException {
		Assert.isTrue(Display.getCurrent() != null, "Invalid thread access");
		final IFile depFile = TypeLibraryUtil.getDependencyFile(project);
		if (depFile.exists() == false) {
			//the TypeDependencies.xml file might be missing for old services
			return EclipseMessageUtils.createErrorStatus("The TypeDependencies.xml file is missing at ["
					+ depFile.getLocation() + "].\n\nPlease do a Clean build on the project " + project.getName()
					+ " in order to generate a default one for you.");
		}
		final IStatus status = new AbstractBaseAccessValidator() {

			@Override
			public List<IResource> getReadableFiles() {
				return getTypeLibraryResources(project);
			}

			@Override
			public List<IResource> getWritableFiles() {
				final List<IResource> resources = getTypeLibraryResources(project, 
						validateProjectConfigFile);
				if (additionalWritableFiles != null) {
					resources.addAll(ListUtil.arrayList(additionalWritableFiles));
				}
				return resources;
			}

		}.validate(project.getName());

		return status;
	}
}
