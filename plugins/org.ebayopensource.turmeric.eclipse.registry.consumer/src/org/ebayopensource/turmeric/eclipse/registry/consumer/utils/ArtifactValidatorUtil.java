/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.registry.consumer.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.ebayopensource.turmeric.common.v1.types.ErrorData;
import org.ebayopensource.turmeric.eclipse.registry.consumer.Activator;
import org.ebayopensource.turmeric.eclipse.registry.consumer.exception.AssertionsServiceException;
import org.ebayopensource.turmeric.eclipse.registry.consumer.exception.InvalidInputException;
import org.ebayopensource.turmeric.eclipse.registry.consumer.exception.NoDataReturnedException;
import org.ebayopensource.turmeric.eclipse.registry.consumer.models.ArtifactValidationResult;
import org.ebayopensource.turmeric.eclipse.registry.consumer.models.AssertionsSeverityModel;
import org.ebayopensource.turmeric.eclipse.registry.consumer.models.AssertionsSeverityModelComparator;
import org.ebayopensource.turmeric.eclipse.registry.consumer.servicegateway.AssertionsServiceConsumer;
import org.ebayopensource.turmeric.eclipse.registry.consumer.servicegateway.RegistryServiceGateway;
import org.ebayopensource.turmeric.repository.v1.services.ArtifactContentTypes;
import org.ebayopensource.turmeric.repository.v1.services.AssertionSeverity;
import org.ebayopensource.turmeric.repository.v1.services.ValidateArtifactResponse;
import org.ebayopensource.turmeric.repository.v1.services.ValidationResultItem;

/**
 * Artifact Validator Util
 * @author ramurthy
 */

public class ArtifactValidatorUtil {
	public static final String SUCCESS = "SUCCESS";
	
	/**
	 * Pre Validation
	 * @param artifactContents
	 * @param artifactType
	 * @param supportedArtifactValidatorsList
	 * @throws InvalidInputException
	 */
	private static void isPreValidationSuccess(byte[] artifactContents, String artifactType, List<String> supportedArtifactValidatorsList) 
			throws InvalidInputException {
		if (artifactContents == null || StringUtils.isBlank(artifactType)) {
			throw new InvalidInputException("Artifact Contents and/or Artifact Type is invalid.");
		}
		if (supportedArtifactValidatorsList != null && !supportedArtifactValidatorsList.isEmpty()) {
			boolean found = false;
			for (String supportedArtifactValidator : supportedArtifactValidatorsList) {
				if (supportedArtifactValidator.equalsIgnoreCase(artifactType)) {
					found = true;
					break;
				}
			}
			if (!found)
				throw new InvalidInputException("Artifact Validation for the specified Artifact Type " + artifactType + " is not supported");
		}
	}
	
	/**
	 * Get All Supported Artifact Validators
	 * @return
	 */
	public static List<String> getAllSupportedArtifactValidators() {
		List<String> supportedArtifactValidatorsList = new ArrayList<String>();
		for (ArtifactContentTypes artifactContentTypes : ArtifactContentTypes.values()) {
			supportedArtifactValidatorsList.add(artifactContentTypes.name());			
		}
		return supportedArtifactValidatorsList;
	}
	
	/**
	 * Get Artifact Validation Result
	 * @param artifactContents
	 * @param artifactType
	 * @return
	 * @throws AssertionsServiceException
	 * @throws NoDataReturnedException
	 * @throws InvalidInputException
	 */
	public static List<ArtifactValidationResult> getArtifactValidationResult(byte[] artifactContents, String artifactType)
			throws AssertionsServiceException, NoDataReturnedException,	InvalidInputException {
		
		isPreValidationSuccess(artifactContents, artifactType, getAllSupportedArtifactValidators());

		AssertionsServiceConsumer asConsumer = RegistryServiceGateway.getAssertionsServiceConsumer();
		
		Activator.getDefault().log("Validating artifact [type=" + artifactType + 
				"] against Assertion Service[endpoint=" + asConsumer.getServiceLocation() + 
				", user=" + asConsumer.getUserName() + 
				", password=" + asConsumer.getPassword() + "].");
		ValidateArtifactResponse validateArtifactResponse = validateArtifact(asConsumer, artifactContents, artifactType);
		List<ValidationResultItem> validationResultItemList = validateArtifactResponse.getValidationResultItem();
		List<ArtifactValidationResult> artifactValidationResultList = new ArrayList<ArtifactValidationResult>();
		if (validationResultItemList != null && !validationResultItemList.isEmpty()) {
			for (ValidationResultItem validationResultItem : validationResultItemList) {
				if (AssertionSeverity.MAY.equals(validationResultItem.getValidationSeverity()) ||
					AssertionSeverity.SHOULD.equals(validationResultItem.getValidationSeverity()) ||
					AssertionSeverity.MUST.equals(validationResultItem.getValidationSeverity())) { 
						ArtifactValidationResult artifactValidationResult = new ArtifactValidationResult();
						artifactValidationResult.setArtifactValidationResultMessage(validationResultItem.getMessage());
						artifactValidationResult.setAssertionSeverityModel(getAssertionsSeverityModel(validationResultItem.getValidationSeverity()));
						if (StringUtils.isNotBlank(validationResultItem.getLineNo())) {
							try {
								int lineNumber = Integer.parseInt(validationResultItem.getLineNo());
								artifactValidationResult.setLineNumber(lineNumber);
							} catch (NumberFormatException e) {
								Activator.getDefault().log(e);
							}
						}
						artifactValidationResultList.add(artifactValidationResult);
				}
			}			
		}
		Collections.sort(artifactValidationResultList, new AssertionsSeverityModelComparator());
		return artifactValidationResultList;
	}
	
	public static AssertionsSeverityModel getAssertionsSeverityModel(AssertionSeverity assertionSeverity) {
		if (assertionSeverity == AssertionSeverity.MUST)
			return AssertionsSeverityModel.MUST;
		if (assertionSeverity == AssertionSeverity.SHOULD)
			return AssertionsSeverityModel.SHOULD;
		if (assertionSeverity == AssertionSeverity.MAY)
			return AssertionsSeverityModel.MAY;
		return null;
	}
	
	/**
	 * Validate Artifact
	 * @return
	 * @throws AssertionsServiceException
	 */
	public static ValidateArtifactResponse validateArtifact(AssertionsServiceConsumer assertionsServiceConsumer, 
			byte[] artifactContent, String artifactType) throws AssertionsServiceException {
		ValidateArtifactResponse validateArtifactResponse = null;	
		try {
			validateArtifactResponse = assertionsServiceConsumer.validateArtifact(artifactContent, artifactType);
		} catch (Exception e) {
			throw new AssertionsServiceException(e);			
		}
		if(validateArtifactResponse == null || !SUCCESS.equalsIgnoreCase(validateArtifactResponse.getAck().value())) {
			final StringBuffer message = new StringBuffer();
			message.append("Assertions Service Exception: ");
			message.append(validateArtifactResponse.getAck());
			message.append(". ");
			if (validateArtifactResponse.getErrorMessage() != null 
					&& validateArtifactResponse.getErrorMessage().getError() != null) {
				for (ErrorData error : validateArtifactResponse.getErrorMessage().getError()) {
					message.append("Error: ");
					message.append(error.getMessage());
					message.append("; ");
				}
			}
			throw new AssertionsServiceException(message.toString());
		}
		return validateArtifactResponse;
	}
	
}
