/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.registry.consumer.models;

/**
 * Artifact Validation Result
 * @author ramurthy
 */

public class ArtifactValidationResult {
	
	private String fArtifactValidationResultMessage;
	
	private AssertionsSeverityModel fAssertionSeverityModel;
	
	/**
	 * the line number for which the errors represent in the resource file,
	 * equals or less than zero means no line number applicable.
	 */
	private int lineNumber = -1;
	
	public ArtifactValidationResult() {
		
	}
	
	public String getArtifactValidationResultMessage() {
		return fArtifactValidationResultMessage;
	}

	public ArtifactValidationResult setArtifactValidationResultMessage(String artifactValidationResultMessage) {
		fArtifactValidationResultMessage = artifactValidationResultMessage;
		return this;
	}
	
	public AssertionsSeverityModel getAssertionsSeverityModel() {
		return fAssertionSeverityModel;
	}

	public ArtifactValidationResult setAssertionSeverityModel(AssertionsSeverityModel assertionsSeverityModel) {
		fAssertionSeverityModel = assertionsSeverityModel;
		return this;
	}

	public int getLineNumber() {
		return lineNumber;
	}

	public void setLineNumber(int lineNumber) {
		this.lineNumber = lineNumber;
	}

	public boolean equals(Object obj) {
		//we dont count line number
		if (this == obj)
			return true;
		if (obj == null || (obj.getClass() != this.getClass()))
			return false;
		ArtifactValidationResult artifactValidationResult = (ArtifactValidationResult) obj;
		if (fArtifactValidationResultMessage == null) {
			if (artifactValidationResult.getArtifactValidationResultMessage() != null)
				return false;
		} else if (!fArtifactValidationResultMessage.equals(artifactValidationResult.getArtifactValidationResultMessage()))
			return false;		
		return true;
	}
	
	public int hashCode() {
		//we dont count line number
		final int prime = 31;
		int hash = 7;
		hash = prime * hash + (fArtifactValidationResultMessage == null ? 0 : fArtifactValidationResultMessage.hashCode());		
		return hash;
	}

}
