/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.codegen.utils;

import java.net.URL;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.NullArgumentException;
import org.apache.commons.lang.StringUtils;
import org.ebayopensource.turmeric.eclipse.codegen.model.IMultiCodeGenModel;
import org.ebayopensource.turmeric.eclipse.codegen.model.IMultiCodeGenModel.IMultiCodeGenModelIterator;
import org.ebayopensource.turmeric.eclipse.core.logging.SOALogger;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.GlobalRepositorySystem;
import org.ebayopensource.turmeric.eclipse.repositorysystem.model.BaseCodeGenModel;
import org.ebayopensource.turmeric.eclipse.utils.classloader.SOAPluginClassLoader;
import org.ebayopensource.turmeric.eclipse.utils.collections.MapUtil;
import org.ebayopensource.turmeric.eclipse.utils.plugin.JDTUtil;
import org.eclipse.core.resources.IProject;

/**
 * This is the main class involved in code generation and can be thought of as
 * the heart of code generation. Interfaces plugin with SOA tools code
 * generation. This touch point resolves the class path, sets the class loader
 * and finally calls the code generation engine. Also expose the class loader
 * for clients to manipulate the properties if required.
 * 
 * @author smathew
 * 
 */
public class CodegenInvoker {

	private SOAPluginClassLoader soaPluginClassLoader;
	private static final SOALogger logger = SOALogger.getLogger();

	/**
	 * No one should ideally need an interface
	 */
	private CodegenInvoker() {

	}

	/**
	 * Initializes this invoker, Sets the class path and make the stage ready
	 * for code generation. The key here is the class loader which is loaded
	 * with all the class path required. Also we don't use the the standard IBM
	 * or SUN java class loader, reason being those class loaders seem to be
	 * using a locked stream for reading the classes from a jar file and the
	 * custom class loader address this issue.
	 * 
	 * @param project
	 * @return
	 * @throws Exception
	 */
	public static CodegenInvoker init(IProject project) throws Exception {
		if (project == null) {
			throw new NullArgumentException("Project can not be null");
		}
		Set<URL> urls = JDTUtil.resolveClasspathToURLs(project);

		SOAPluginClassLoader classLoader = new SOAPluginClassLoader("Codegen",
				urls.toArray(new URL[0]));

		CodegenInvoker codegenInvoker = new CodegenInvoker();
		codegenInvoker.setSoaPluginClassLoader(classLoader);
		return codegenInvoker;
	}

	/**
	 * 
	 * Executes the given code generation model in the context created by the
	 * 
	 * @see CodegenInvoker#init(IProject).
	 * 
	 *      This is again a tricky method. We want to have our old class loader
	 *      stored temporarily and use the fresh soa class loader for code
	 *      generation for obvious reasons( the relevant class path is there in
	 *      this class loader). After codegen we will set the old classpath
	 *      back.
	 * 
	 * 
	 * 
	 * @param model
	 *            - This model has all the parameters required for codegen
	 *            invocation.
	 * @return
	 */
	public boolean execute(BaseCodeGenModel model) throws Exception {
		Map<String, String> paramMap = model.getCodeGenOptions();
		ClassLoader oldClassLoader = Thread.currentThread()
				.getContextClassLoader();
		try {
			Thread.currentThread().setContextClassLoader(
					getSoaPluginClassLoader());
			if (model instanceof IMultiCodeGenModel) {
				for (IMultiCodeGenModelIterator iterator = ((IMultiCodeGenModel) model)
						.iterator(); iterator.hasNext();) {
					paramMap = iterator.nextInputOptions();
					addJdkHomeOptions(paramMap);
					logger.info(BaseCodeGenModel.toString(model.getGenType(),
							paramMap));
					callCodegen(MapUtil.toArray(paramMap, new String[0], true));
				}
			} else {
				addJdkHomeOptions(paramMap);
				logger.info(BaseCodeGenModel.toString(model.getGenType(),
						paramMap));
				callCodegen(MapUtil.toArray(paramMap, new String[0], true));
			}
		} finally {
			Thread.currentThread().setContextClassLoader(oldClassLoader);
		}
		return true;
	}


	private static void callCodegen(String[] parameters) throws Exception {
		if(SOALogger.DEBUG){
			logger.debug((Object[])parameters);
		}
		GlobalRepositorySystem.instanceOf().getActiveRepositorySystem()
				.getSOACodegenProvider().generateCode(parameters);

	}

	private void addJdkHomeOptions(Map<String, String> paramMap) {
		final String javaHome = CodeGenUtil.getJavaHome();
		if (StringUtils.isNotBlank(javaHome))
			paramMap.put(BaseCodeGenModel.PARAM_JAVA_HOME, javaHome);
		final String jdkHome = CodeGenUtil.getJdkHome();
		if (StringUtils.isNotBlank(jdkHome))
			paramMap.put(BaseCodeGenModel.PARAM_JDK_HOME, jdkHome);
	}

	/**
	 * Usual getter
	 * 
	 * @return
	 */
	public SOAPluginClassLoader getSoaPluginClassLoader() {
		return soaPluginClassLoader;
	}

	private void setSoaPluginClassLoader(
			SOAPluginClassLoader soaPluginClassLoader) {
		this.soaPluginClassLoader = soaPluginClassLoader;
	}

}
