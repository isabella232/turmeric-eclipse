/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.template.wsdl.processors;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.ebayopensource.turmeric.eclipse.core.ICommand;
import org.ebayopensource.turmeric.eclipse.exception.core.CommandFailedException;
import org.ebayopensource.turmeric.eclipse.logging.SOALogger;
import org.ebayopensource.turmeric.eclipse.resources.ui.model.ServiceFromTemplateWsdlParamModel;
import org.ebayopensource.turmeric.eclipse.utils.plugin.ProgressUtil;
import org.ebayopensource.turmeric.eclipse.utils.xml.FreeMarkerUtil;
import org.eclipse.core.runtime.IProgressMonitor;
import org.osgi.framework.Version;


/**
 * @author smathew
 * 
 * This class performs the basic common steps required for any wsdl
 */
public class BasicProcessor implements ICommand {
private static final SOALogger logger = SOALogger.getLogger();
	
	private File targetFile;
	
	public BasicProcessor(){
		this(null);
	}
	
	public BasicProcessor(File tempTargetFile){
		this.targetFile = tempTargetFile;
	}

	public boolean execute(Object parameter, IProgressMonitor monitor) throws CommandFailedException {
		if (parameter != null && parameter instanceof CommonWSDLProcessorParam) {
			final CommonWSDLProcessorParam processorModel = (CommonWSDLProcessorParam) parameter;
			final URL wsdlFile = processorModel.getInputParamModel().getTemplateFile(); 
			try {
				final Map<String, String> data = new ConcurrentHashMap<String, String>();
				ServiceFromTemplateWsdlParamModel model = processorModel.getInputParamModel();
				data.put("serviceName", model.getPublicServiceName());
				data.put("namespacePart", model.getNamespacePart());
				final Version version = new Version(model.getServiceVersion());
				data.put("majorVersion", String.valueOf(version.getMajor()));
				data.put("minorVersion", String.valueOf(version.getMinor()));
				data.put("microVersion", String.valueOf(version.getMicro()));
				data.put("targetNamespace", model.getTargetNamespace());
				logger.info("Data for generating service WSDL->", data, 
						" using template->", wsdlFile);
				if(targetFile == null){
					targetFile = processorModel.getTargetFile().getLocation().toFile();
				}
				FreeMarkerUtil.generate(data, wsdlFile, wsdlFile.getFile(), 
						new FileOutputStream(targetFile));
				
				ProgressUtil.progressOneStep(monitor);
			} catch (Exception e1) {
				throw new CommandFailedException(
						"Template file could not be read.", e1);
			}
			return true;
		} else {
			throw new CommandFailedException(
					"Input is not compatible. Possible reason is a corrupted template area.");
		}

	}

}
