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
package org.ebayopensource.turmeric.eclipse.core.logging.system;

import java.beans.PropertyChangeEvent;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

/**
 * The Interface ISOALoggingSystemProvider.
 *
 * @author yayu
 */
public interface ISOALoggingSystemProvider {

	
	/**
	 * Gets the system id.
	 *
	 * @return the build sytem ID
	 */
	public String getSystemID();
	
	/**
	 * Inits the logging system.
	 */
	public void initLoggingSystem();
	
	/**
	 * New logger created.
	 *
	 * @param logger the new logger instance just being created
	 */
	public void newLoggerCreated(Logger logger);
	
	/**
	 * Should log raw message.
	 *
	 * @param record the record
	 * @return true if should log the raw message or false otherwise
	 */
	public boolean shouldLogRawMessage(LogRecord record);
	
	/**
	 * Something has changed in the JDK logging system.
	 *
	 * @param event the event
	 */
	public void loggingPropertyChanged(PropertyChangeEvent event);
}
