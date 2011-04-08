/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.errorlibrary.properties.registry;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Map;

import org.ebayopensource.turmeric.eclipse.core.logging.SOALogger;
import org.ebayopensource.turmeric.eclipse.errorlibrary.properties.ui.utils.TurmericErrorLibraryUtils;
import org.ebayopensource.turmeric.eclipse.errorlibrary.views.ISOAErrDomain;
import org.ebayopensource.turmeric.eclipse.errorlibrary.views.ISOAErrLibrary;
import org.ebayopensource.turmeric.eclipse.errorlibrary.views.ISOAError;
import org.ebayopensource.turmeric.eclipse.exception.resources.SOAInvocationException;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.GlobalRepositorySystem;
import org.ebayopensource.turmeric.eclipse.resources.model.AssetInfo;
import org.ebayopensource.turmeric.eclipse.utils.plugin.ProgressUtil;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.progress.IProgressService;


/**
 * The error library registry which maintains all errors.
 * @author yayu
 *
 */
public final class TurmericErrorRegistry {
	private static final SOALogger logger = SOALogger.getLogger();
	private static Map<String, ISOAErrLibrary> errorLibs = null;
	private static Map<String, ISOAErrDomain> errorDomains = null;
	private static Map<String, ISOAError> errors = null;

	private TurmericErrorRegistry() {
		super();
	}
	
	private static String getErrorTypeKey(String domainName, String errorName) {
		return domainName + "|" + errorName;
	}
	
	
	private static void init() throws Exception {
		long startTime = System.currentTimeMillis();
		try {
			synchronized(TurmericErrorRegistry.class) {
				if (errorLibs == null) {
					final IRunnableWithProgress runnable = new IRunnableWithProgress() {

						public void run(IProgressMonitor monitor)
								throws InvocationTargetException,
								InterruptedException {
							monitor.beginTask(
									"Initializing Turmeric Error Library Registry...", 
									IProgressMonitor.UNKNOWN);
							monitor.internalWorked(10);
							try {
								monitor.internalWorked(20);
								final Map<String, ISOAErrLibrary> result = new Hashtable<String, ISOAErrLibrary>();
								errorDomains = new Hashtable<String, ISOAErrDomain>();
								errors = new Hashtable<String, ISOAError>();
								for(AssetInfo lib: GlobalRepositorySystem.instanceOf().getActiveRepositorySystem()
								.getErorRegistryBridge().getErrorLibs()) {
									try {
										final ISOAErrLibrary errLib = 
											TurmericErrorLibraryUtils.loadErrorLibrary(lib);
										ProgressUtil.progressOneStep(monitor);
										if (errLib != null) {
											result.put(errLib.getName(), errLib);
											for (ISOAErrDomain domain : errLib.getDomains()) {
												errorDomains.put(domain.getName().toLowerCase(Locale.US), domain);
												for (ISOAError error : domain.getErrors()) {
													// add domain name as part of the key to ensure uniqueness of error type under particular domain
													errors.put(getErrorTypeKey(domain.getName(), error.getName()), error);
												}
											}
										}
									}
									catch (Exception e) {
										logger.warning("Error occured while loading error library [" 
												+ lib + "], ignoring this library.", e);
									}
								}
								
								errorLibs = result;
							} catch (Exception e) {
								throw new SOAInvocationException(e);
							} finally {
								monitor.done();
							}
						}
					};
					if (Display.getCurrent() == null) {
						// non-UI thread
						runnable.run(ProgressUtil.getDefaultMonitor(null));
					} else {
						final IProgressService service = PlatformUI
						.getWorkbench().getProgressService();
						service.run(false, false, runnable);
					}
					while (errorLibs == null) {
						logger.warning("Turmeirc error library registry not initialized yet, sleeping...");
						Thread.sleep(1000);
					}
				}
			}
		} finally {
			if (SOALogger.DEBUG) {
				long duration = System.currentTimeMillis() - startTime;
				logger.info("Time taken for initializing Turmeric error library registry is ", 
						duration, " ms.");
			}
		}
	}
	
	/**
	 * 
	 * @return a collecation or Error Libraries
	 * @throws Exception an exception if there is an error
	 */
	public static Collection<ISOAErrLibrary> getErrorLibraries() throws Exception {
		if (errorLibs == null) {
			init();
		}
		return errorLibs.values();
	}
	
	/**
	 * 
	 * @param errorLibName error library name
	 * @return the error library or null if the library is not found.
	 * @throws Exception an exception if there is an error
	 */
	public static ISOAErrLibrary getErrorLibraryByName(String errorLibName) throws Exception {
		getErrorLibraries();
		if (errorLibs != null)
			return errorLibs.get(errorLibName);
		return null;
	}
	
	/**
	 * 
	 * @param errorDomainName domain name
	 * @return the error domain
	 * @throws Exception an exception if there is an error
	 */
	public static ISOAErrDomain getErrorDomainByName(String errorDomainName) throws Exception {
		getErrorLibraries();
		if (errorDomains != null)
			return errorDomains.get(errorDomainName.toLowerCase(Locale.US));
		return null;
	}
	
	/**
	 * 
	 * @param domainName the domain name
	 * @param errorName the error name
	 * @return an error object
	 * @throws Exception .
	 */
	public static ISOAError getErrorByName(String domainName, String errorName) throws Exception {
		getErrorLibraries();
		if (errors != null)
			return errors.get(getErrorTypeKey(domainName, errorName));
		return null;
	}
	
	/**
	 * 
	 * @param errorLib an error library
	 * @return true if the library was added
	 * @throws Exception an error
	 */
	public static boolean addErrorLibrary(ISOAErrLibrary errorLib) throws Exception {
		getErrorLibraries();
		if (errorLibs != null && errorLibs.containsKey(errorLib.getName()) == false) {
			errorLibs.put(errorLib.getName(), errorLib);
			return true;
		}
		return false;
	}
	
	/**
	 * 
	 * @param errorLibName error library name
	 * @param errorDomain error domain
	 * @return true if the library was added successfully
	 * @throws Exception an error 
	 */
	public static boolean addErrorDomain(String errorLibName, ISOAErrDomain errorDomain) throws Exception {
		getErrorLibraries();
		if (containsErrorLibrary(errorLibName) == false) {
			return false;
		}
		String errorDomainName = errorDomain.getName().toLowerCase(Locale.US);
		if (containsErrorDomain(errorDomainName) == false) {
			errorDomains.put(errorDomainName, errorDomain);
			getErrorLibraryByName(errorLibName).getDomains().add(errorDomain);
			return true;
		}
		return false;
	}
	
	/**
	 * 
	 * @param errorDomainName error domain
	 * @param error an error object
	 * @return true if the error was added successfully
	 * @throws Exception if a critical error occurs and exception is thrown.
	 */
	public static boolean addError(String errorDomainName, ISOAError error) throws Exception {
		getErrorLibraries();
		String lowerErrorDomainName = errorDomainName.toLowerCase(Locale.US);
		if (containsErrorDomain(lowerErrorDomainName) == false) {
			return false;
		}
		String errorTypeKey = getErrorTypeKey(errorDomainName, error.getName());
		if (errors != null && errors.containsKey(errorTypeKey) == false) {
			errors.put(errorTypeKey, error);
			getErrorDomainByName(lowerErrorDomainName).getErrors().add(error);
			return true;
		}
		return false;
	}
	
	/**
	 * 
	 * @param errorLibName error library name
	 * @return the error library removed.
	 * @throws Exception if a critical error occurs an exception is thrown
	 */
	public static ISOAErrLibrary removeErrorLibrary(String errorLibName) throws Exception {
		getErrorLibraries();
		if (errorLibs != null) {
			return errorLibs.remove(errorLibName);
		}
		return null;
	}
	
	/**
	 * 
	 * @param domain the domain
	 * @return an error domain object that was removed
	 * @throws Exception an exception
	 */
	public static ISOAErrDomain removeErrorDomain(ISOAErrDomain domain) throws Exception {
		getErrorLibraries();
		if (errorDomains != null) {
			errorDomains.remove(domain.getName().toLowerCase(Locale.US));
			if (domain.getLibrary() != null) {
				domain.getLibrary().getDomains().remove(domain);
			}
		}
		return domain;
	}
	
	/**
	 * 
	 * @param domainName domain name
	 * @param error an error object
	 * @return the error object removed
	 * @throws Exception an error occurred
	 */
	public static ISOAError removeError(String domainName, ISOAError error) throws Exception {
		getErrorLibraries();
		if (errors != null) {
			errors.remove(getErrorTypeKey(domainName, error.getName()));
			if (error.getDomain() != null) {
				error.getDomain().getErrors().remove(error);
			}
		}
		return error;
	}
	
	/**
	 * 
	 * @param errorLibName error library name
	 * @return true if the error library exists
	 * @throws Exception an error has occurred
	 */
	public static boolean containsErrorLibrary(String errorLibName) throws Exception {
		getErrorLibraries();
		if (errorLibs != null) {
			return errorLibs.containsKey(errorLibName);
		}
		return false;
	}
	
	/**
	 * 
	 * @param errorDomainName error domain
	 * @return true if it contains the error domain
	 * @throws Exception an error has occurred
	 */
	public static boolean containsErrorDomain(String errorDomainName) throws Exception {
		getErrorLibraries();
		if (errorDomains != null) {
			return errorDomains.containsKey(errorDomainName.toLowerCase(Locale.US));
		}
		return false;
	}

	/**
	 * 
	 * @param domainName the domain name
	 * @param errorName error name
	 * @return true if it contains the dombination of domain name and error name
	 * @throws Exception an exception
	 */
	public static boolean containsError(String domainName, String errorName) throws Exception {
		getErrorLibraries();
		if (errors != null) {
			return errors.containsKey(getErrorTypeKey(domainName, errorName));
		}
		return false;
	}
	
	/**
	 * 
	 * @throws Exception an exception
	 */
	
	public static void refresh() throws Exception {
		synchronized(TurmericErrorRegistry.class) {
	        errorLibs = null;
	        errorDomains = null;
	        errors = null;
	    }
		init();
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		throw new CloneNotSupportedException();
	}
	
	

}
