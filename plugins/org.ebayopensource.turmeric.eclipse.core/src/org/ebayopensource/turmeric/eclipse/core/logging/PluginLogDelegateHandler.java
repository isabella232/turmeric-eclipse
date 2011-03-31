/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.core.logging;

import java.text.MessageFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;

import org.apache.commons.lang.StringUtils;
import org.ebayopensource.turmeric.eclipse.core.TurmericCoreActivator;
import org.ebayopensource.turmeric.eclipse.core.logging.system.ISOALoggingSystemProvider;
import org.ebayopensource.turmeric.eclipse.core.logging.system.SOALoggingSystemExtensionRegistry;
import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;



/**
 * <p>This class is intended to be added to the JDK Logging framework. 
 * This would ensure any logging messages will go through it so that 
 * we could reformat the log message to follow the SOA logging format.</p>
 * @author Yang Yu(yayu@ebay.com)
 *
 */
public class PluginLogDelegateHandler 
extends Handler
{
	private static final String SYSTEM_LINE_SEPARATOR;
	private static ILog systemLogger;
	/**
	 * The name of the current build system.
	 */
	private static String BUILD_SYSTEM_NAME;
	static
	{
		String separator = System.getProperty("line.separator");
		if (separator == null)
			separator = "\n";
		SYSTEM_LINE_SEPARATOR = separator;
	}
    private final Formatter formatter = new MySimpleFormatter();
    
    public PluginLogDelegateHandler() {
		super();
	}
	@Override
    public void close() throws SecurityException
    {
    }
    @Override
    public void flush()
    {
    }
    private int level( final Level level )
    {
        if( level == null )
            return IStatus.INFO;
        if( level.intValue() >= Level.SEVERE.intValue() )
            return IStatus.ERROR;
        if( level.intValue() > Level.WARNING.intValue() )
            return IStatus.WARNING;
        return IStatus.INFO;
    }
    @Override
    public void publish( final LogRecord record )
    {
        if( record == null || StringUtils.isBlank(record.getMessage()))
            return;
        final int level = level( record.getLevel() );
        
        if (shouldLogRawMessage(record)) {
        	log( level, record.getLoggerName(), record.getMessage(), null );
        } else {
        	log( level, record.getLoggerName(), StringUtils.replace( formatter.format( record ), "\n", "\n " ), record.getThrown() );
        }
    }
    
    private boolean shouldLogRawMessage(LogRecord record) {
    	try {
			ISOALoggingSystemProvider logSystemProvider = SOALoggingSystemExtensionRegistry.getInstance()
			.getLoggingSystemIDProvider(PluginLogDelegateHandler.getBuildSystemName());
			if (logSystemProvider != null) {
				return logSystemProvider.shouldLogRawMessage(record);
			}
		} catch (Exception e) {
			//ignore the issue
			e.printStackTrace();
		}
    	return false;
    }
    /**
     * @param name The new Build System Name
     */
    public static void setBuildSystemName(String name) {
    	if (StringUtils.isNotBlank(name)) {
    		BUILD_SYSTEM_NAME = name;
    		log( IStatus.INFO, PluginLogDelegateHandler.class.getName(), 
    				"Changed system ID to->" + name, null );
    	}
    }
    
    public static String getBuildSystemName() {
    	return BUILD_SYSTEM_NAME;
    }
    /**
     * @param severity The log severity
     * @param loggerName The name of the logger
     * @param message The message body
     * @param throwable The exception instance or null if no exception.
     */
    private static void log( final int severity, 
    		final String loggerName,
            final String message, 
            final Throwable throwable )
    {
    	if( TurmericCoreActivator.getDefault() == null )
    		//the Activator has not been initialized yet
    		return;
    	if (systemLogger == null)
    		systemLogger = TurmericCoreActivator.getDefault().getLog();
    	systemLogger.log( new Status( severity, loggerName, 0, StringUtils.defaultString( message ), throwable ) );
    }
    
    private static class MySimpleFormatter extends Formatter {

	    Date dat = new Date();
	    //define the log time format
	    private final static String format = "{0,date} {0,time, long}";
	    private MessageFormat formatter;

	    private Object args[] = new Object[1];

	    /**
	     * Format the given LogRecord.
	     * @param record the log record to be formatted.
	     * @return a formatted log record
	     */
	    @Override
	    public synchronized String format(LogRecord record) 
	    {
	    	StringBuffer sb = new StringBuffer();
	    	// Minimize memory allocations here.
	    	dat.setTime(record.getMillis());
	    	args[0] = dat;
	    	StringBuffer text = new StringBuffer();
	    	if (formatter == null) {
	    		formatter = new MessageFormat(format);
	    	}
	    	formatter.format(args, text, null);
	    	sb.append(text);
	    	sb.append(" ");

	    	if (BUILD_SYSTEM_NAME != null)
	    	{
	    		sb.append("[");
	    		sb.append(BUILD_SYSTEM_NAME);
	    		sb.append("] ");
	    	}

	    	if (record.getSourceClassName() != null) {	
	    		sb.append(record.getSourceClassName());
	    	} else {
	    		sb.append(record.getLoggerName());
	    	}
	    	if (record.getSourceMethodName() != null) {	
	    		sb.append("  ");
	    		sb.append(record.getSourceMethodName());
	    	}
	    	sb.append(SYSTEM_LINE_SEPARATOR);
	    	String message = formatMessage(record);
	    	sb.append(record.getLevel().getLocalizedName());
	    	sb.append(": ");
	    	sb.append(message);
	    	return sb.toString();
	    }
    }
}
