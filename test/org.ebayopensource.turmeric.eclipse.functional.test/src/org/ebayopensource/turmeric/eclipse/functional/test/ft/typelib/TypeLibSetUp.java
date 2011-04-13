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
package org.ebayopensource.turmeric.eclipse.functional.test.ft.typelib;

import java.io.File;
import java.io.IOException;

import org.ebayopensource.turmeric.eclipse.functional.test.ft.wsdlsvc.ServiceSetupCleanupValidate;
import org.ebayopensource.turmeric.eclipse.test.util.ProjectUtil;


/**
 * @author vyaramala
 * 
 */
public class TypeLibSetUp {
	//public static final String TEMPDIR = System.getProperty("java.io.tmpdir");
	public static String TYPELIB_LOCATION = ServiceSetupCleanupValidate.getParentDir();
	// public static String TYPELIB_LOCATION =
	// EBoxServiceSetupCleanupValidate.getParentDir();
	public static final String TYPELIBRARY_NAME1 = "SOA21TestTL1";
	public static final String TYPELIBRARY_NAME2 = "SOA21TestTL2";
	public static final String TYPELIBRARY_COMMON = "SOACommonTypeLibrary1";
	public static final String TYPELIBRARY_TEST = "TypeLibraryTest";
	public static final String TYPELIBRARY_DOMAIN = "DomainTypeLibrary1";
	public static final String TYPELIBRARY_SERVICE = "ServiceTypeLibrary1";
	public static String SVC_LOCATION = ServiceSetupCleanupValidate.getParentDir();
	public static final String SVC_NAME1 = "TestSvc1";
	public static final String SVC_NAME2 = "TestSvc2";
	public static final String SVC_NAME3 = "TestSvc3";

	public static void setup() throws IOException, InterruptedException {
		Thread.sleep(10000);
		File f = new File(TYPELIB_LOCATION);
		if (!f.exists()) {
			f.mkdir();
		} else {
			TYPELIB_LOCATION = f.getAbsolutePath();
			rmdir(TYPELIB_LOCATION + File.separatorChar + TYPELIBRARY_NAME1);
			rmdir(TYPELIB_LOCATION + File.separatorChar + TYPELIBRARY_NAME2);
			rmdir(TYPELIB_LOCATION + File.separatorChar + TYPELIBRARY_COMMON);
			// CleanUp WS
			try {
				ProjectUtil.cleanUpWS();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static void rmdir(String dir) throws IOException {
		File f = new File(dir);
		if (!f.exists())
			return;
		if (!deleteDirectory(f))
			// throw new IOException("Unable to delete - " + dir);
			System.out.println("could not delete all files");
	}

	public static boolean deleteDirectory(File path) {
		if (path.exists()) {
			File[] files = path.listFiles();
			if (files == null)
				return (path.delete());
			for (int i = 0; i < files.length; i++) {
				if (files[i].isDirectory()) {
					deleteDirectory(files[i]);
				} else {
					files[i].delete();
				}
			}
		}
		return (path.delete());
	}

	public static void setupSvc(String svcName) throws IOException {
		File f = new File(TYPELIB_LOCATION);
		if (!f.exists()) {
			f.mkdir();
		} else {
			SVC_LOCATION = f.getAbsolutePath();
			rmdir(SVC_LOCATION + File.separatorChar + svcName);
		}
	}

}
