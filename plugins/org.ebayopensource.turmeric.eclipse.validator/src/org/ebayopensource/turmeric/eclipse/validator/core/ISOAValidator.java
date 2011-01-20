/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.validator.core;

import org.ebayopensource.turmeric.eclipse.exception.validation.ValidationInterruptedException;
import org.eclipse.core.runtime.IStatus;


/**
 * @author smathew This interface should be implemented by all all SOA
 *         validators, both UI and non UI validators. Might have to revisit this
 */
public interface ISOAValidator {

	/**
	 * core validation method
	 * 
	 * @return Status.OK model if the validation succeeds
	 * @see Status.OK_STATUS;
	 * 
	 * Implementing classes are advised to use proper names
	 * for the parameter for better clarity for clients.
	 * This is now Object for inheritance purpose.
	 * We could use generics here. But not sure if
	 * everybody will be comfortable in genrics usage now.
	 */
	public IStatus validate(Object obj) throws ValidationInterruptedException;

}
