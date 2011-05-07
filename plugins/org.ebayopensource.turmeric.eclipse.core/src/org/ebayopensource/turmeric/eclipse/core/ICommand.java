/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.core;

import org.ebayopensource.turmeric.eclipse.exception.core.CommandFailedException;
import org.eclipse.core.runtime.IProgressMonitor;


/**
 * Client can implement this interface whenever they need their commands to be
 * analyzed by the framework. Typically it is now used by the template
 * processing logic. Another use is when clients want to have their processors
 * lined up in some list and execute it one by one after collecting all of them
 * 
 * 
 * @author smathew
 * 
 */
public interface ICommand {

	/**
	 * Executes the operation with the given object. If there are some errors
	 * while executing the operation implementors should throw the command
	 * failed exception. Since most of the operations are usually long running
	 * ones it is always recommended to indicate the progress through the given
	 * progress monitor object. Also the classes which lines up this processors
	 * are supposed indicate the progress to the user using this monitor. In
	 * special cases in which the operations are very short, this can be null.
	 *
	 * @param object -
	 * Any input object the implementing class can understand and act
	 * on.
	 * @param monitor -
	 * to show the visual progress of the operation.
	 * @return true, if successful
	 * @throws CommandFailedException the command failed exception
	 */
	public boolean execute(Object object, IProgressMonitor monitor)
			throws CommandFailedException;
}
