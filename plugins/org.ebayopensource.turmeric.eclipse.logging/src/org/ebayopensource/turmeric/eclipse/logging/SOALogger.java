/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.logging;

import java.io.File;
import java.io.OutputStream;
import java.net.URL;
import java.util.Hashtable;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.ebayopensource.turmeric.eclipse.logging.system.ISOALoggingSystemProvider;
import org.ebayopensource.turmeric.eclipse.logging.system.SOALoggingSystemExtensionRegistry;
import org.ebayopensource.turmeric.eclipse.utils.lang.StringUtil;
import org.eclipse.core.runtime.Platform;



/**
 * A wrapper class for logging.
 * @author Yang Yu(yayu@ebay.com)
 *
 */
public final class SOALogger extends Logger{ 
	private static Hashtable<String,SOALogger> loggers = null;
	
	//public static final String USAGE_TRACKING = "!USAGE_TRACKING: ";
    
	public static final boolean DEBUG;
	/**
	 * Codegen and runtime global logger ID
	 */
	public static final String GLOBAL_LOGGER_ID = "org.ebayopensource.turmeric";
	/**
	 * Plugin global logger ID
	 */
	public static final String GLOBAL_LOGGER_ID_PLUGIN = "org.ebayopensource.turmeric.eclipse";
	
	private static final PluginLogDelegateHandler pluginLogHandler = new PluginLogDelegateHandler();
	public static final Logger GLOBAL_LOGGER = Logger.getLogger("");
	private static final String FILENAME_LOGGING_PROPERTIES = "turmeric-plugin-logging.properties";
	

	static
	{
		boolean trace = false;
		try {
			loggers = new Hashtable<String,SOALogger>();
			GLOBAL_LOGGER.addHandler( pluginLogHandler );
			String value = Platform.getDebugOption("org.ebayopensource.turmeric.eclipse.logging/debug");
			trace = "true".equalsIgnoreCase(value);

			value = Platform.getDebugOption("org.ebayopensource.turmeric.eclipse.logging/debug/level");
			Level tracingLevel = value != null ? Level.parse(value) : Level.FINER;
		
			if (trace) {
				//This is a way to share the logging level between Plugin and CodeGen.
				Logger.getLogger( GLOBAL_LOGGER_ID ).setLevel(tracingLevel);
				Logger.getLogger( GLOBAL_LOGGER_ID_PLUGIN ).setLevel(tracingLevel);
			}
			
		} catch (Exception e) {
			Logger.getLogger( GLOBAL_LOGGER_ID_PLUGIN ).throwing(SOALogger.class.getName(), "<static>", e);
		}
		try {
			ISOALoggingSystemProvider logSystemProvider = SOALoggingSystemExtensionRegistry.getInstance()
			.getLoggingSystemIDProvider(PluginLogDelegateHandler.getBuildSystemName());
			if (logSystemProvider != null) {
				logSystemProvider.initLoggingSystem();
			}
		} catch (Exception e) {
			//ignore the issue
			e.printStackTrace();
		}
		URL input = SOALogger.class.getResource(FILENAME_LOGGING_PROPERTIES);
		if (input != null) {
			String localConfigFile =  new StringBuilder().append(
                    System.getProperty("user.home")).append(File.separator) //$NON-NLS-1$
                    .append(".turmeric").append(File.separator).append( //$NON-NLS-1$
                    		FILENAME_LOGGING_PROPERTIES).toString(); //$NON-NLS-1$
			File configFile = new File(localConfigFile);
			if (configFile.exists() == false) {
				OutputStream output = null;
				try {
					FileUtils.copyURLToFile(input, configFile);
				} catch (Exception e) {
					//ignore the issue
					e.printStackTrace();
				} finally {
					IOUtils.closeQuietly(output);
				}
			}
			if (configFile.exists()) {
				System.setProperty("java.util.logging.config.file", configFile.getAbsolutePath());
			}
		}
		DEBUG = trace;
	}
	
	public static Handler getLogHandler() {
		return pluginLogHandler;
	}
	
	
	public static SOALogger getInstance(Class< ? > clazz) {
		return getInstance(clazz.getName());
	}
	
	public static SOALogger getInstance(String clazz) {
		// We want 1 logger per subsystem
		String subsystem = "";
		if (clazz != null) {
//			TODO: after plugin added SOABinding in. add this back
//			subsystem = BindingUtils.getPackageName(clazz);
			if (subsystem.length() == 0) {
				subsystem = clazz;
			}
		} else {
			subsystem = "";
		}

		return getLogger(subsystem);
	}
	
	private SOALogger(String loggerName) {
		super(loggerName, null);
	}
	
	/**
	 * If the tracing is enabled, then the logging level will be setup accordingly.
	 * @param clazz
	 * @return An instance of <code>LogManager</code>
	 */
	public static synchronized SOALogger getLogger(String clazz)
	{
		java.util.logging.LogManager manager = java.util.logging.LogManager.getLogManager();
		Logger result = manager.getLogger(clazz);
		if (result instanceof SOALogger) {
		    return (SOALogger)result;
		}
		else if (result != null)
		{//there is an existing logger instance
			if (loggers.keySet().contains(clazz))
			{
				return loggers.get(clazz);
			}
			SOALogger logger = new SOALogger(clazz);
			logger.setLevel(result.getLevel());
			logger.setFilter(result.getFilter());
			logger.setParent(result.getParent());
			logger.setUseParentHandlers(logger.getUseParentHandlers());
			loggers.put(clazz, logger);
			return logger;
		}
		else
		{//can not find a logger, so let's create one.
			result = new SOALogger(clazz);
		}
		
		manager.addLogger(result);
		SOALogger logger = (SOALogger)manager.getLogger(clazz);
		try {
			ISOALoggingSystemProvider logSystemProvider = SOALoggingSystemExtensionRegistry.getInstance()
			.getLoggingSystemIDProvider(PluginLogDelegateHandler.getBuildSystemName());
			if (logSystemProvider != null) {
				logSystemProvider.newLoggerCreated(logger);
			}
		} catch (Exception e) {
			//ignore the issue
			e.printStackTrace();
		}
		return logger;
	}
	
	public static void setBuildSystemName(String name)
	{
		PluginLogDelegateHandler.setBuildSystemName(name);
	}

	/**
	 * @param clazz The <code>Class</code> instance that is used for constructing <code>Logger</code> instance.
	 * @return
	 */
	public static SOALogger getLogger(Class<?> clazz)
	{
		SOALogger logger = getLogger(clazz.getName());
		
		return logger;
	}
	
	/**
	 * The logger name is automatically obtained.
	 * @return An instance of <code>LogManager</code>
	 */
	public static SOALogger getLogger()
	{
		StackTraceElement[] elements = new Throwable().getStackTrace();
		return getLogger(elements[1].getClassName());
	}
	
	
	//////////////////////////////////////////
	//Info Logging
	//////////////////////////////////////////
	/**
	 * @param msgs The messages
	 * @see java.util.logging.Level#INFO
	 */
	public void info(Object... msgs) {
		StackTraceElement[] elements = new Throwable().getStackTrace();
    	logp(Level.INFO, elements[1].getClassName(), elements[1].getMethodName(), StringUtil.toString(msgs));
	}
	/**
	 * @return <code>True</code> if the <code>INFO</code> logging level is enabled, or <code>False</code> otherwise.
	 * @see java.util.logging.Level#INFO
	 */
	public boolean isInfoEnabled()
	{
		return isLoggable(Level.INFO);
	}

	//////////////////////////////////////////
	//Warning Logging
	//////////////////////////////////////////
	/**
	 * @param message The warning message
	 * @see java.util.logging.Level#WARNING
	 */
	public void warning(Object... messages)
    {
		StackTraceElement[] elements = new Throwable().getStackTrace();
    	logp(Level.WARNING, elements[1].getClassName(), elements[1].getMethodName(), 
    			StringUtil.toString(messages));
    }
	/**
	 * @param messages The warning messages
	 * @see java.util.logging.Level#WARNING
	 */
	public void warning(Throwable cause, Object... messages)
    {
		StackTraceElement[] elements = new Throwable().getStackTrace();
    	logp(Level.WARNING, elements[1].getClassName(), elements[1].getMethodName(), 
    			StringUtil.toString(messages), cause);
    }
    /**
     * <p>This method will work if the <code>WARNING</code> logging level is enabled.</p>
     * @param message The message
     * @param cause The cause of the problem
     * @see java.util.logging.Level#WARNING
     */
    public void warning(Object message, Throwable cause)
    {
    	StackTraceElement[] elements = new Throwable().getStackTrace();
    	logp(Level.WARNING, elements[1].getClassName(), elements[1].getMethodName(), String.valueOf(message), cause);
    }
    
    //////////////////////////////////////////
	//Error Logging
	//////////////////////////////////////////
	/**
	 * @param msg The error message
	 * @see java.util.logging.Level#SEVERE
	 */
	public void error(String msg) {
		StackTraceElement[] elements = new Throwable().getStackTrace();
        logp(Level.SEVERE, elements[1].getClassName(), elements[1].getMethodName(), String.valueOf(msg));
	}
	/**
	 * @param message The error message
	 * @param cause The cause of the error
	 * @see java.util.logging.Level#SEVERE
	 */
	public void error(Object message, Throwable cause)
    {
		StackTraceElement[] elements = new Throwable().getStackTrace();
        logp(Level.SEVERE, elements[1].getClassName(), elements[1].getMethodName(), String.valueOf(message), cause);
    }
	/**
	 * @param cause The cause of the error
	 * @see java.util.logging.Level#SEVERE
	 */
	public void error(Throwable cause)
    {
		StackTraceElement[] elements = new Throwable().getStackTrace();
        logp(Level.SEVERE, elements[1].getClassName(), elements[1].getMethodName(),cause.getLocalizedMessage(), cause);
    }
	/**
	 * @param thrown The instance of <code>Throwable</code>
	 * @see java.util.logging.Level#SEVERE
	 */
	public void throwing(Throwable thrown) {
		StackTraceElement[] elements = new Throwable().getStackTrace();
		throwing(elements[1].getClassName(), elements[1].getMethodName(), thrown);
	}
    
    //////////////////////////////////////////
	//Debug Logging
	//////////////////////////////////////////
    /**
     * @param msg The debug message
     * @see java.util.logging.Level#FINE
     */
    public void debug(Object... msgs)
    {
    	StackTraceElement[] elements = new Throwable().getStackTrace();
        logp(Level.FINE, elements[1].getClassName(), elements[1].getMethodName(), StringUtil.toString(msgs));
    }
    
	//////////////////////////////////////////
	//Logging Entry/Exit
	//////////////////////////////////////////
	/**
	 * @param params The multiple parameters that passed in
	 * @see java.util.logging.Level#FINER
	 */
	public void entering(Object... params)
	{
		StackTraceElement[] elements = new Throwable().getStackTrace();
		if (params != null)
			entering(elements[1].getClassName(), elements[1].getMethodName(), params);
		else 
			entering(elements[1].getClassName(), elements[1].getMethodName());
	}
	
	/**
	 * <p>Suitable for logging methods with <code>void</code> return type.</p>
	 * @see java.util.logging.Level#FINER
	 */
	public void exiting()
	{
		StackTraceElement[] elements = new Throwable().getStackTrace();
		exiting(elements[1].getClassName(), elements[1].getMethodName());
	}

	/**
	 * @param result The result of the method call
	 * @see java.util.logging.Level#FINER
	 */
	public void exiting(Object result)
	{
		StackTraceElement[] elements = new Throwable().getStackTrace();
		exiting(elements[1].getClassName(), elements[1].getMethodName(), result);
	}
}
