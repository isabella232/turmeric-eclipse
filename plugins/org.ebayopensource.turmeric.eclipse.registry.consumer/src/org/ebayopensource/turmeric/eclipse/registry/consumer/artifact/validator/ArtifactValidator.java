/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.registry.consumer.artifact.validator;

import java.util.ArrayList;
import java.util.List;

import org.ebayopensource.turmeric.eclipse.registry.consumer.Activator;
import org.ebayopensource.turmeric.eclipse.registry.consumer.exception.InvalidInputException;
import org.ebayopensource.turmeric.eclipse.registry.consumer.models.ArtifactValidationResult;
import org.ebayopensource.turmeric.eclipse.registry.consumer.models.AssertionsSeverityModel;
import org.ebayopensource.turmeric.eclipse.registry.consumer.preferences.AssertionServicePreferenceConstants;
import org.ebayopensource.turmeric.eclipse.registry.consumer.resources.Messages;
import org.ebayopensource.turmeric.eclipse.registry.consumer.servicegateway.RegistryServiceGateway;
import org.ebayopensource.turmeric.eclipse.registry.consumer.utils.ArtifactValidatorUtil;
import org.ebayopensource.turmeric.eclipse.registry.consumer.utils.SOAMessageUtils;
import org.ebayopensource.turmeric.eclipse.registry.exception.ArtifactValidationException;
import org.ebayopensource.turmeric.eclipse.registry.intf.IArtifactValidator;
import org.ebayopensource.turmeric.eclipse.registry.intf.IArtifactValidator2;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.SubProgressMonitor;


/**
 * Artifact Validator.
 *
 * @author ramurthy
 */

public class ArtifactValidator implements IArtifactValidator, IArtifactValidator2 {
	private static final String VERSION = "2.0.0";

	/**
	 * Get All Supported Validators.
	 *
	 * @return the all supported validators
	 */
	@Override
	public List<String> getAllSupportedValidators() {	
		if (Activator.DEBUG)
			Activator.getDefault().logArgumentMessage(Messages.METHOD_START, "getAllSupportedValidators");
		List<String> supportArtifactValidatorList = ArtifactValidatorUtil.getAllSupportedArtifactValidators();
		if (Activator.DEBUG)
			Activator.getDefault().logArgumentMessage(Messages.METHOD_END, "getAllSupportedValidators");
		return supportArtifactValidatorList;
	}
	
	/**
	 * Validate Artifact.
	 *
	 * @param artifactContents the artifact contents
	 * @param artifactType the artifact type
	 * @param progressMonitor the progress monitor
	 * @return the i status
	 * @throws ArtifactValidationException the artifact validation exception
	 */
	@Override
	public IStatus validateArtifact(byte[] artifactContents, String artifactType, IProgressMonitor progressMonitor)
			throws ArtifactValidationException {
		if (Activator.DEBUG)
			Activator.getDefault().logArgumentMessage(Messages.METHOD_START, "validateArtifact");
		
		List<ArtifactValidationResult> artifactValidationResultList = null;
		SubProgressMonitor subProgressMonitor =  new SubProgressMonitor(progressMonitor, IProgressMonitor.UNKNOWN);
		subProgressMonitor.beginTask(Messages.VALIDATION_IN_PROGRESS, IProgressMonitor.UNKNOWN);
		subProgressMonitor.subTask(Messages.VALIDATION_IN_PROGRESS);
		if (!subProgressMonitor.isCanceled()) {					
			try {
				// Invoke artifact validator
				artifactValidationResultList = ArtifactValidatorUtil.getArtifactValidationResult(artifactContents, artifactType.toUpperCase());
			} catch (InvalidInputException e) {
				Activator.getDefault().log(Messages.ERROR_INVALID_ARTIFACT_CONTENTS_OR_TYPE);
				return SOAMessageUtils.createStatus(IStatus.ERROR, Activator.PLUGIN_ID, IStatus.ERROR, 
						Messages.ERROR_INVALID_ARTIFACT_CONTENTS_OR_TYPE, null, 1);
			} catch (Exception e) {
				Activator.getDefault().log(e);
				throw new ArtifactValidationException(Messages.ERROR_VALIDATION_COULDNT_RUN, e);
			} finally {
				RegistryServiceGateway.invalidateConsumers();
			}
		}
		subProgressMonitor.done();
		if (subProgressMonitor.isCanceled()) {
			subProgressMonitor.done();
			throw new ArtifactValidationException(new InterruptedException());
		}
		if (Activator.DEBUG)
			Activator.getDefault().logArgumentMessage(Messages.METHOD_END, "validateArtifact");
		
		return getArtifactValidationResultStatus(artifactValidationResultList);
	}
	
	/**
	 * Get Artifact Validation Result Status.
	 *
	 * @param artifactValidationResultList the artifact validation result list
	 * @return the artifact validation result status
	 */
	public IStatus getArtifactValidationResultStatus(List<ArtifactValidationResult> artifactValidationResultList) {
		if (Activator.DEBUG)
			Activator.getDefault().logArgumentMessage(Messages.METHOD_START, "getArtifactValidationResultStatus");
		
		if (artifactValidationResultList != null && !artifactValidationResultList.isEmpty()) {			
			List<IStatus> statusList = new ArrayList<IStatus>();
			for (ArtifactValidationResult artifactValidationResult : artifactValidationResultList) {
				if (artifactValidationResult.getAssertionsSeverityModel() == null)
					continue;
				if (AssertionsSeverityModel.MAY.equals(artifactValidationResult.getAssertionsSeverityModel())) { 
					statusList.add(SOAMessageUtils.createMayAssertionStatus(artifactValidationResult.getArtifactValidationResultMessage(), null));
					Activator.getDefault().logArgumentMessage(Messages.VALIDATION_RESULT_MAY, artifactValidationResult.getArtifactValidationResultMessage());
				}
				else if (AssertionsSeverityModel.SHOULD.equals(artifactValidationResult.getAssertionsSeverityModel())) {					
					statusList.add(SOAMessageUtils.createStatus(IStatus.WARNING, Activator.PLUGIN_ID, IStatus.WARNING, 
							artifactValidationResult.getArtifactValidationResultMessage(), null, artifactValidationResult.getLineNumber()));
					Activator.getDefault().logArgumentMessage(Messages.VALIDATION_RESULT_SHOULD, artifactValidationResult.getArtifactValidationResultMessage());
				}
				else if (AssertionsSeverityModel.MUST.equals(artifactValidationResult.getAssertionsSeverityModel())) { 
					statusList.add(SOAMessageUtils.createStatus(IStatus.ERROR, Activator.PLUGIN_ID, IStatus.ERROR, 
							artifactValidationResult.getArtifactValidationResultMessage(), null, artifactValidationResult.getLineNumber()));
					Activator.getDefault().logArgumentMessage(Messages.VALIDATION_RESULT_MUST, artifactValidationResult.getArtifactValidationResultMessage());
				}
			}
			IStatus multiStatus = SOAMessageUtils.createMultiStatus(Activator.PLUGIN_ID, 0, statusList.toArray(new IStatus[0]), 
					"", null);
			String validationResultMessage = "";
			if (multiStatus.getSeverity() == IStatus.ERROR) 
				validationResultMessage = Messages.VALIDATION_RESULT_ERROR;
			else if (multiStatus.getSeverity() == IStatus.WARNING)
				validationResultMessage = Messages.VALIDATION_RESULT_WARNING;
			
			if (Activator.DEBUG)
				Activator.getDefault().logArgumentMessage(Messages.VALIDATION_RESULT_METHOD_END, validationResultMessage, "getArtifactValidationResultStatus");
			return SOAMessageUtils.createMultiStatus(Activator.PLUGIN_ID, 0, statusList.toArray(new IStatus[0]), 
					validationResultMessage, null);
		}		
		if (Activator.DEBUG)
			Activator.getDefault().logArgumentMessage(Messages.METHOD_END, "getArtifactValidationResultStatus");
		return SOAMessageUtils.createStatus(IStatus.OK, Activator.PLUGIN_ID, IStatus.OK, "Artifact Validation Result - Success", null, -1);
	}

	/* (non-Javadoc)
	 * @see org.ebayopensource.turmeric.eclipse.registry.intf.IArtifactValidator2#getVersion()
	 */
	@Override
	public String getVersion() {
		return VERSION;
	}

	/* (non-Javadoc)
	 * @see org.ebayopensource.turmeric.eclipse.registry.intf.IArtifactValidator2#isAssertionServiceEnabled()
	 */
	@Override
	public boolean isAssertionServiceEnabled() {
		return Activator.getDefault().getPreferenceStore().getBoolean(
				AssertionServicePreferenceConstants.ENABLE_ASSERTION_SERVICE);
	}

}
