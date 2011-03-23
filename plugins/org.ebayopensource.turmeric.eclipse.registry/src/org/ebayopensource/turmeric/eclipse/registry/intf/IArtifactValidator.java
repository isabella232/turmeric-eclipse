/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
/**
 * 
 */
package org.ebayopensource.turmeric.eclipse.registry.intf;

import java.util.List;

import org.ebayopensource.turmeric.eclipse.registry.exception.ArtifactValidationException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;


/**
 * @author yayu
 * @since 1.0.0
 */
public interface IArtifactValidator {
	
	/** Validate the artifact
	 * @param artifacts
	 * @param artifactType the file extension of the artifacts, e.g. wsdl.
	 * @param monitor
	 * @return
	 * @throws ArtifactValidationException
	 */
	public IStatus validateArtifact(byte[] artifacts, String artifactType, 
			IProgressMonitor monitor) throws ArtifactValidationException;

	/**
	 * @return a list of all supported validators
	 */
	public List<String> getAllSupportedValidators();


}
