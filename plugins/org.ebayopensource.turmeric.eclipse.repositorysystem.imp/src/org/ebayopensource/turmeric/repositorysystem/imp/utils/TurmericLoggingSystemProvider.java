/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.repositorysystem.imp.utils;

import java.util.logging.LogRecord;
import java.util.logging.Logger;

import org.ebayopensource.turmeric.eclipse.logging.system.ISOALoggingSystemProvider;


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

	/* (non-Javadoc)
	 * @see org.ebayopensource.turmeric.eclipse.logging.system.ISOALoggingSystemProvider#getSystemID()
	 */
	public String getSystemID() {
		return TurmericConstants.TURMERIC_ID;
	}

	/* (non-Javadoc)
	 * @see org.ebayopensource.turmeric.eclipse.logging.system.ISOALoggingSystemProvider#initLoggingSystem()
	 */
	public void initLoggingSystem() {
		
	}
	
	public static void initLogHanlder(Logger logger) {
		
	}
	

	/* (non-Javadoc)
	 * @see org.ebayopensource.turmeric.eclipse.logging.system.ISOALoggingSystemProvider#newLoggerCreated(java.util.logging.Logger)
	 */
	public void newLoggerCreated(Logger logger) {

	}

	/* (non-Javadoc)
	 * @see org.ebayopensource.turmeric.eclipse.logging.system.ISOALoggingSystemProvider#shouldLogRawMessage(java.util.logging.LogRecord)
	 */
	public boolean shouldLogRawMessage(LogRecord record) {
		return false;
	}

}
