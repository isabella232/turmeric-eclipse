/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.repositorysystem.imp.utils;

import java.beans.PropertyChangeEvent;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import org.ebayopensource.turmeric.eclipse.core.logging.system.ISOALoggingSystemProvider;


/**
 * @author yayu
 *
 */
public class TurmericLoggingSystemProvider implements
		ISOALoggingSystemProvider {
	
	/**
	 * 
	 */
	public TurmericLoggingSystemProvider() {
		super();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.ebayopensource.turmeric.eclipse.core.logging.system.ISOALoggingSystemProvider#getSystemID()
	 */
	public String getSystemID() {
		return TurmericConstants.TURMERIC_ID;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.ebayopensource.turmeric.eclipse.core.logging.system.ISOALoggingSystemProvider#initLoggingSystem()
	 */
	public void initLoggingSystem() {
		
	}
	
	/**
	 * 
	 * @param logger
	 */
	public static void initLogHanlder(Logger logger) {
		
	}
	

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.ebayopensource.turmeric.eclipse.core.logging.system.ISOALoggingSystemProvider#newLoggerCreated(java.util.logging.Logger)
	 */
	public void newLoggerCreated(Logger logger) {

	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.ebayopensource.turmeric.eclipse.core.logging.system.ISOALoggingSystemProvider#shouldLogRawMessage(java.util.logging.LogRecord)
	 */
	public boolean shouldLogRawMessage(LogRecord record) {
		return false;
	}

	public void loggingPropertyChanged(PropertyChangeEvent event) {
		// TODO Auto-generated method stub
		
	}

}
