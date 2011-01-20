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
package org.ebayopensource.turmeric.eclipse.logging.system;

import java.util.logging.LogRecord;
import java.util.logging.Logger;

/**
 * @author yayu
 *
 */
public interface ISOALoggingSystemProvider {

	
	/**
	 * @return the build sytem ID
	 */
	public String getSystemID();
	
	/**
	 * 
	 */
	public void initLoggingSystem();
	
	/**
	 * @param logger the new logger instance just being created
	 */
	public void newLoggerCreated(Logger logger);
	
	/**
	 * @param record
	 * @return true if should log the raw message or false otherwise
	 */
	public boolean shouldLogRawMessage(LogRecord record);
}
