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
import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.ebayopensource.turmeric.eclipse.buildsystem.SynchronizeWsdlAndDepXML;
import org.ebayopensource.turmeric.eclipse.core.ICommand;
import org.ebayopensource.turmeric.eclipse.core.logging.SOALogger;
import org.ebayopensource.turmeric.eclipse.core.model.services.ServiceFromTemplateWsdlParamModel;
import org.ebayopensource.turmeric.eclipse.exception.core.CommandFailedException;
import org.ebayopensource.turmeric.eclipse.typelibrary.ui.TypeLibraryUIActivator;
import org.ebayopensource.turmeric.eclipse.utils.plugin.ProgressUtil;
import org.ebayopensource.turmeric.eclipse.utils.plugin.WorkspaceUtil;
import org.ebayopensource.turmeric.eclipse.utils.xml.JDOMUtil;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.wst.wsdl.Definition;


/**
 * The Class WSDLTemplateProcessor.
 *
 * @author smathew
 * 
 * 
 * Takes the UI models and process it and create the templates.
 * Processing can be of two types emf based or template based
 */
public class WSDLTemplateProcessor {

	private static SOALogger logger = SOALogger.getLogger();

	/**
	 * Gets the temp wsdl file.
	 *
	 * @return the temp wsdl file
	 */
	public static File getTempWSDLFile() {
		return new File(System.getProperty("java.io.tmpdir"),
				"TurmericNewSvcFromTemplateTemp.wsdl");
	}

	private ServiceFromTemplateWsdlParamModel inputParamModel;
	private IFile destinationFile;
	private CommonWSDLProcessorParam commonWDLProcessorParam;

	/**
	 * Instantiates a new wSDL template processor.
	 *
	 * @param inputParamModel the input param model
	 */
	public WSDLTemplateProcessor(
			ServiceFromTemplateWsdlParamModel inputParamModel) {
		super();
		this.inputParamModel = inputParamModel;
	}

	/**
	 * Process.
	 *
	 * @param monitor the monitor
	 * @throws CommandFailedException the command failed exception
	 */
	public void process(final IProgressMonitor monitor)
			throws CommandFailedException {
		Definition outDefinition = generateWSDLDefinitionUsingTempTargetFile(
				monitor, null);
		FileProcessor.FileProcessorParam fileProcessorParam = new FileProcessor.FileProcessorParam(
				outDefinition, destinationFile);
		FileProcessor fileProcessor = new FileProcessor();
		fileProcessor.execute(fileProcessorParam, monitor);
		ProgressUtil.progressOneStep(monitor);
	}

	/**
	 * This is for WSDL validation. IFile must belongs to a project. So add this
	 * to use java.io.file.
	 * 
	 * @param monitor
	 * @param tempTargetFile
	 * @return
	 * @throws CommandFailedException
	 */
	private Definition generateWSDLDefinitionUsingTempTargetFile(
			final IProgressMonitor monitor, File tempTargetFile)
			throws CommandFailedException {
		commonWDLProcessorParam = new CommonWSDLProcessorParam(inputParamModel,
				destinationFile);
		List<ICommand> commands = new ArrayList<ICommand>();
		commands.add(new BasicProcessor(tempTargetFile));
		commands.add(new ModelProcessor(tempTargetFile));
		commands.add(new ImportProcessor());
		commands.add(new OperationsProcessor());
		commands.add(new BindingProcessor());

		ProgressUtil.progressOneStep(monitor);
		execute(commands, commonWDLProcessorParam, monitor);

		Definition outDefinition = commonWDLProcessorParam.getDefinition();
		return outDefinition;
	}

	/**
	 * Gets the wSDL file url.
	 *
	 * @param monitor the monitor
	 * @return the wSDL file url
	 * @throws CommandFailedException the command failed exception
	 * @throws MalformedURLException the malformed url exception
	 */
	public URL getWSDLFileURL(final IProgressMonitor monitor)
			throws CommandFailedException, MalformedURLException {
		File file = getTempWSDLFile();
		if (file.exists() == false) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				logger.error(e);
			}
		}
		Definition outDefinition = generateWSDLDefinitionUsingTempTargetFile(
				monitor, file);
		String contents = null;
		try {
			contents = TypeLibraryUIActivator.formatContents(JDOMUtil
					.convertXMLToString(JDOMUtil.convertToJDom(outDefinition
							.getDocument())));
			PrintWriter pw = new PrintWriter(new FileOutputStream(file));
			pw.write(contents);
			pw.close();
		} catch (IOException e) {
			logger.error(e);
		} catch (CoreException e) {
			logger.error(e);
		}
		ProgressUtil.progressOneStep(monitor);
		return file.toURI().toURL();
	}

	private void execute(List<ICommand> commandList,
			CommonWSDLProcessorParam commonWDLProcessorParam,
			IProgressMonitor monitor) throws CommandFailedException {
		for (ICommand command : commandList) {
			command.execute(commonWDLProcessorParam, monitor);
		}
	}

	/**
	 * Gets the destination file.
	 *
	 * @return the destination file
	 */
	public IFile getDestinationFile() {
		return destinationFile;
	}

	/**
	 * Sets the destination file.
	 *
	 * @param destinationFile the new destination file
	 */
	public void setDestinationFile(IFile destinationFile) {
		this.destinationFile = destinationFile;
	}

	/**
	 * {@inheritDoc}
	 */
	public void finalize() throws CommandFailedException {
		if (WorkspaceUtil.getWorkspace().isAutoBuilding() == false) {
			try {
				SynchronizeWsdlAndDepXML synch = new SynchronizeWsdlAndDepXML(commonWDLProcessorParam.getTargetFile()
						.getProject());
				synch.syncronizeWSDLandDepXml(commonWDLProcessorParam.getDefinition());
				synch.synchronizeTypeDepandProjectDep(ProgressUtil.getDefaultMonitor(null));
			} catch (Exception e) {
				throw new CommandFailedException("Failed to update Dependencies", e);
			}

			// WorkspaceUtil.refresh(processorModel.getTargetFile()
			// .getProject());
		}
	}

}
