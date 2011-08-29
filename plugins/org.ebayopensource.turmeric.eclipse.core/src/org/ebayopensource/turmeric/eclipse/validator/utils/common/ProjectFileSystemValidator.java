/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.validator.utils.common;

import org.ebayopensource.turmeric.eclipse.exception.validation.ValidationInterruptedException;
import org.ebayopensource.turmeric.eclipse.utils.plugin.WorkspaceUtil;
import org.ebayopensource.turmeric.eclipse.validator.core.AbstractSOAValidator;
import org.eclipse.core.runtime.IStatus;


/**
 * The Class ProjectFileSystemValidator.
 *
 * @author smathew
 * Validates the project path
 */
public class ProjectFileSystemValidator extends AbstractSOAValidator{
	
	
	/* (non-Javadoc)
	 * @see org.ebayopensource.turmeric.eclipse.validator.core.AbstractSOAValidator#validate(java.lang.Object)
	 * This validator accepts the project full path
	 * and validates the path. 
	 * If the path exists the it returns false.
	 * else true
	 * Null Safe method.
	 */
	@Override
	public IStatus validate(Object stringFileName) throws ValidationInterruptedException {
		IStatus status = super.validate(stringFileName);
		if (status.isOK() == true) {
			if (stringFileName instanceof String) {
				String filePath = (String)stringFileName;
				if (WorkspaceUtil.directoryExistsInFileSystem(filePath)) {
					status = getBasicStatusModel("Project already exists in the designated location: "
									+ stringFileName + " select another name.");
				}
				if (status.isOK() == true && filePath.length() > 150) {
					status = getBasicStatusModel("Project path is too long: "
							+ stringFileName);
				}
			}
		}
		return status;
	}

}
