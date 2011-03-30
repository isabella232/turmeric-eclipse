/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.test.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Iterator;

import junit.framework.Assert;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.LineIterator;
import org.apache.commons.io.filefilter.NameFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.apache.commons.lang.StringUtils;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.repository.metadata.ArtifactMetadata;
import org.ebayopensource.turmeric.eclipse.buildsystem.utils.BuildSystemUtil;
import org.ebayopensource.turmeric.eclipse.core.resources.constants.SOAProjectConstants;
import org.ebayopensource.turmeric.eclipse.functional.test.SoaTestConstants;
import org.ebayopensource.turmeric.eclipse.maven.core.utils.MavenCoreUtils;
import org.ebayopensource.turmeric.eclipse.mavenapi.MavenApiPlugin;
import org.ebayopensource.turmeric.eclipse.mavenapi.exception.MavenEclipseApiException;
import org.ebayopensource.turmeric.eclipse.mavenapi.impl.MavenEclipseUtil;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.GlobalRepositorySystem;
import org.ebayopensource.turmeric.eclipse.resources.model.IAssetInfo;
import org.ebayopensource.turmeric.eclipse.resources.model.ISOAConsumerProject.SOAClientConfig;
import org.ebayopensource.turmeric.eclipse.resources.util.SOAClientConfigUtil;
import org.ebayopensource.turmeric.eclipse.resources.util.SOAConsumerUtil;
import org.ebayopensource.turmeric.eclipse.test.utils.WsdlUtilTest;
import org.ebayopensource.turmeric.eclipse.utils.plugin.ProgressUtil;
import org.ebayopensource.turmeric.eclipse.utils.plugin.WorkspaceUtil;
import org.ebayopensource.turmeric.repositorysystem.imp.utils.TurmericConstants;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.debug.core.IStreamListener;
import org.eclipse.debug.core.Launch;
import org.eclipse.debug.core.model.IProcess;
import org.eclipse.debug.core.model.IStreamMonitor;
import org.eclipse.debug.core.model.IStreamsProxy;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.launching.IVMInstall;
import org.eclipse.jdt.launching.IVMRunner;
import org.eclipse.jdt.launching.JavaRuntime;
import org.eclipse.jdt.launching.VMRunnerConfiguration;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;


public class FunctionalTestHelper {

	public static void consumeJunitModifiedTestSvcBeforeMod(
			String serviceName1, IProject consProject1) throws Exception {

		// change the Impl class
		changeServiceImplFile(serviceName1, "consume" + serviceName1);

		// copy the Consumer class file
		copyServiceConsumerFile(consProject1.getName(), serviceName1, "consume"
				+ serviceName1);

		// modify the consumer transport settings to local
		modifyClientPrjTransport(consProject1, serviceName1,
				SOAProjectConstants.Binding.LOCAL);

		invokeConsumer(consProject1);

	}

	public static void ensureM2EcipseBeingInited()
			throws Exception {
		
		BundleContext context = MavenApiPlugin.getDefault().getBundle().getBundleContext();
		MavenApiPlugin.getDefault().start(context);
		int state = MavenApiPlugin.getDefault().getBundle().getState();
		
		while(state != Bundle.ACTIVE) {
			System.out.println("M2 Eclipse still not started.  Sleeping and trying again.");
			Thread.sleep(5000L);
			state = MavenApiPlugin.getDefault().getBundle().getState();
		}
						
//		while (metadata != null && metadata.getVersion() == null) {
//			System.out
//					.println("Waiting for M2Eclipse, unable to retrieve library: " + libName + ". Sleeping.");
//			Thread.sleep(5000L);
//			metadata = MavenCoreUtils.getLibraryIdentifier(libName);
//		}
	}

	/*
	 * finds the Impl class and switches with another file in the
	 * test-data/consume<serviceName> dir
	 */
	@SuppressWarnings("unchecked")
	public static void changeServiceImplFile(String serviceName,
			String srcDirName) throws CoreException {

		final String implFileName = serviceName + "Impl.java";
		String implFileDir = WsdlUtilTest.getPluginOSPath(SoaTestConstants.PLUGIN_ID,
				"test-data/" + srcDirName);
		IProject serviceImplPrj = ProjectUtil.getProject(serviceName + "Impl");

		IPath implFilePath = new Path(implFileDir + "/" + implFileName);

		Assert.assertNotNull(implFilePath);

		System.out.println("The implFilePath that is being copied is "
				+ implFilePath.toOSString());
		NameFileFilter fileFilter = new NameFileFilter(implFileName);

		Collection<File> files = FileUtils.listFiles(serviceImplPrj
				.getFullPath().toFile()
		/* .getLocation().toFile() */, fileFilter, TrueFileFilter.INSTANCE);

		File curImplFile = null;
		for (Iterator iterator = files.iterator(); iterator.hasNext();) {
			File file = (File) iterator.next();
			if (file.getAbsolutePath().indexOf("src") > 0) {
				curImplFile = file;
			}
			System.out.println("The file in collection is "
					+ file.getAbsolutePath());
		}
		Assert.assertNotNull(files);
		Assert.assertTrue(files.size() > 0);

		// File curImplFile = files.iterator().next();
		try {
			FileUtils.forceDelete(curImplFile);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			Assert.fail("deleteFile operation failed");
		}
		System.out.println("The implFilePath being overwritten is  "
				+ curImplFile.getAbsolutePath());
		try {
			FileUtils.copyFile(implFilePath.toFile(), curImplFile, false);
		} catch (IOException e) {
			e.printStackTrace();
			Assert.fail("copyFile operation failed");
		}

		WorkspaceUtil.refresh(serviceImplPrj);
		serviceImplPrj.build(IncrementalProjectBuilder.INCREMENTAL_BUILD,
				ProgressUtil.getDefaultMonitor(null));
	}

	/*
	 * It copies the Consumer.java file from the srcFolder sub-dir in the
	 * test-data area to the directory Base<cons>.java is located in
	 */
	@SuppressWarnings("unchecked")
	public static void copyServiceConsumerFile(String consumerPrjName,
			String serviceName, String srcFolder) throws Exception {

		// final String baseConsumerFilename = "Base"
		// + StringUtils.capitalize(serviceName) + "Consumer.java";

		final String copyConsDir = WsdlUtilTest.getPluginOSPath(
				SoaTestConstants.PLUGIN_ID, "test-data/" + srcFolder);
		Assert.assertNotNull(copyConsDir);

		final String srcConsumerFile = copyConsDir
				.concat("/" + "Consumer.java");

		System.out.println("The srcConsumerFile that is being copied is "
				+ srcConsumerFile);
		IProject consPrj = WorkspaceUtil.getProject(consumerPrjName);

		/*
		 * NameFileFilter fileFilter = new NameFileFilter(baseConsumerFilename);
		 * 
		 * Collection<File> files = FileUtils.listFiles(consPrj.getLocation()
		 * .toFile(), fileFilter, TrueFileFilter.INSTANCE);
		 * 
		 * Assert.assertNotNull(files); Assert.assertTrue(files.size() > 0);
		 * 
		 * File curBaseConsFile = null; for (Iterator iterator =
		 * files.iterator(); iterator.hasNext();) { File file = (File)
		 * iterator.next(); if (file.getAbsolutePath().indexOf("src") > 0 ) {
		 * curBaseConsFile = file; }
		 * System.out.println("The file in collection is " +
		 * file.getAbsolutePath()); }
		 * 
		 * // File curBaseConsFile = files.iterator().next();
		 * 
		 * IPath baseConsDir = new Path(curBaseConsFile.getAbsolutePath())
		 * .removeLastSegments(1);
		 * 
		 * System.out.println("The directory that consumer file is copied into is "
		 * + baseConsDir.toOSString()); try { FileUtils.copyFileToDirectory(new
		 * File(srcConsumerFile), baseConsDir.toFile(), false); } catch
		 * (IOException e) { e.printStackTrace();
		 * Assert.fail("Copying Consumer file to the Base Consumer Dir failed" +
		 * baseConsDir.toOSString()); }
		 * 
		 * WorkspaceUtil.refresh(consPrj);
		 * consPrj.build(IncrementalProjectBuilder.INCREMENTAL_BUILD,
		 * ProgressUtil .getDefaultMonitor(null));
		 */
		IPath srcDir = new Path(consPrj.getLocation().toString()
				.concat("/" + "src"));
		try {
			FileUtils.copyFileToDirectory(new File(srcConsumerFile),
					srcDir.toFile());
		} catch (IOException e) {
			e.printStackTrace();
			Assert.fail("Copying Consumer file to the Src Dir failed"
					+ srcDir.toOSString());
		}
		WorkspaceUtil.refresh(consPrj);
		consPrj.build(IncrementalProjectBuilder.INCREMENTAL_BUILD,
				ProgressUtil.getDefaultMonitor(null));
	}

	/*
	 * For now it sets Binding to LOCAL
	 */

	public static void modifyClientPrjTransport(IProject consProject,
			String serviceName, SOAProjectConstants.Binding binding) {
		String environmentName = "production";

		try {
			final SOAClientConfig config = SOAConsumerUtil.loadClientConfig(
					consProject, environmentName, serviceName);

			config.setServiceBinding("LOCAL");
			String protocalProcessorClassName = GlobalRepositorySystem
			.instanceOf()
			.getActiveRepositorySystem()
			.getActiveOrganizationProvider()
			.getSOAPProtocolProcessorClassName();
			SOAClientConfigUtil.save(config, protocalProcessorClassName);

			// When adding the local binding u might need to add the project as
			// dependency also..For that use the below code,

			GlobalRepositorySystem
					.instanceOf()
					.getActiveRepositorySystem()
					.getProjectConfigurer()
					.addDependency(consProject.getName(), serviceName + "Impl",
							IAssetInfo.TYPE_PROJECT, true,
							ProgressUtil.getDefaultMonitor(null));

			GlobalRepositorySystem
					.instanceOf()
					.getActiveRepositorySystem()
					.getProjectConfigurer()
					.addDependency(consProject.getName(), "SOAServer",
							IAssetInfo.TYPE_LIBRARY, true,
							ProgressUtil.getDefaultMonitor(null));

			WorkspaceUtil.refresh(consProject);

			BuildSystemUtil.updateSOAClasspathContainer(consProject);

			consProject.build(IncrementalProjectBuilder.FULL_BUILD,
					ProgressUtil.getDefaultMonitor(null));

		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail("changing transport to LOCAL failed");
		}

	}

	/*
	 * It executes the Consumer.java main() in the consProjet. Before executing,
	 * the transport settings are changed to LOCAL It assumes that the Impl
	 * project is in the workspace.
	 */
	public static void invokeConsumer(IProject consProject)
			throws CoreException {
		IJavaProject clientJProj = JavaCore.create(consProject);

		String consumerClass = getConsumerFQN(consProject);

		Assert.assertNotNull(consumerClass);

		IVMInstall vm = JavaRuntime.getVMInstall(clientJProj);
		if (vm == null)
			vm = JavaRuntime.getDefaultVMInstall();
		IVMRunner vmr = vm.getVMRunner(ILaunchManager.RUN_MODE);
		String[] cp = JavaRuntime.computeDefaultRuntimeClassPath(clientJProj);
		VMRunnerConfiguration config = new VMRunnerConfiguration(consumerClass,
				cp);
		ILaunch launch = new Launch(null, ILaunchManager.RUN_MODE, null);
		vmr.run(config, launch, null);

		// This is the only one process we just fired off..so there will be only
		// process
		IProcess launchedProcess = launch.getProcesses()[0];
		IStreamsProxy streamProxy = launchedProcess.getStreamsProxy();

		final StringBuffer outSb = new StringBuffer(1024);
		final StringBuffer errSb = new StringBuffer(1024);
		streamProxy.getOutputStreamMonitor().addListener(new IStreamListener() {

			public void streamAppended(String text, IStreamMonitor monitor) {
				outSb.append(text);

			}
		});
		streamProxy.getErrorStreamMonitor().addListener(new IStreamListener() {

			public void streamAppended(String text, IStreamMonitor monitor) {
				errSb.append(text);

			}
		});

		int i = 0;
		while (i < 60 && !launchedProcess.isTerminated()) {
			i++;
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		if (launchedProcess.isTerminated()) {
			System.out.println("The Stdout is -- " + outSb.toString());
			System.out.println("The Stderr is -- " + errSb.toString());
		} else {
			System.out.println("The launched process did not terminate");
		}

		Assert.assertTrue("The consumer invocation failed with errors", (outSb
				.toString().indexOf("EXCEPTION") < 0));
		Assert.assertTrue("The consumer invocation failed with errors", (outSb
				.toString().indexOf("Exception") < 0));
		Assert.assertTrue("The consumer invocation failed with errors", (outSb
				.toString().indexOf("ERROR") < 0));
		Assert.assertTrue("The consumer invocation failed with errors", (errSb
				.toString().indexOf("EXCEPTION") < 0));
		Assert.assertTrue("The consumer invocation failed with errors", (errSb
				.toString().indexOf("ERROR") < 0));
		Assert.assertTrue("The consumer invocation failed with errors", (errSb
				.toString().indexOf("Exception") < 0));
		Assert.assertTrue("The consumer invocation failed with errors", (errSb
				.toString().indexOf("The java class is not found") < 0));
	}

	@SuppressWarnings("unchecked")
	public static String getConsumerFQN(IProject prj) {

		// String className = null;
		String className = "Consumer";
		NameFileFilter fileFilter = new NameFileFilter("Consumer.java");

		Collection<File> files = FileUtils
				.listFiles(prj.getLocation().toFile(), fileFilter,
						TrueFileFilter.INSTANCE);

		Assert.assertNotNull(files);
		Assert.assertTrue(files.size() > 0);

		File consFile = files.iterator().next();

		InputStream input = null;
		try {
			input = new FileInputStream(consFile);

			LineIterator iter = IOUtils.lineIterator(input, null);
			while (iter.hasNext()) {
				String line = iter.nextLine();
				if (line.startsWith("package")) {
					className = StringUtils.substringBetween(line, "package",
							";").trim();
					className = className + ".Consumer";
					break;
				}
			}
			iter.close();
		} catch (Exception e) {
			e.printStackTrace();
			IOUtils.closeQuietly(input);
		}

		return className;

	}

	public static void modifyClientPrjServicePP(IProject consProject2,
			String serviceName2, String messageProtocol) {
		SOAClientConfig config = null;
		String environmentName = "production";
		try {
			config = SOAConsumerUtil.loadClientConfig(consProject2,
					environmentName, serviceName2);
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail("failed to load Client config");
		}

		config.setMessageProtocol(messageProtocol);
		try {
			String protocalProcessorClassName = GlobalRepositorySystem
			.instanceOf()
			.getActiveRepositorySystem()
			.getActiveOrganizationProvider()
			.getSOAPProtocolProcessorClassName();
			SOAClientConfigUtil.save(config, protocalProcessorClassName);
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail("failed to save Client config");
		}
	}

	public static void modifyClientPrjRespDataBinding(IProject consProject2,
			String serviceName2, String responseDataBinding) throws Exception {
		SOAClientConfig config = null;
		String environmentName = "production";
		try {
			config = SOAConsumerUtil.loadClientConfig(consProject2,
					environmentName, serviceName2);
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail("failed to load Client config");
		}

		config.setResponseDataBinding(responseDataBinding);
		try {
			String protocalProcessorClassName = GlobalRepositorySystem
			.instanceOf()
			.getActiveRepositorySystem()
			.getActiveOrganizationProvider()
			.getSOAPProtocolProcessorClassName();
			SOAClientConfigUtil.save(config, protocalProcessorClassName);
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail("failed to save Client config");
		}

		WorkspaceUtil.refresh(consProject2);
		consProject2.build(IncrementalProjectBuilder.INCREMENTAL_BUILD,
				ProgressUtil.getDefaultMonitor(null));

	}
}
