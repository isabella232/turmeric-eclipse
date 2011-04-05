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
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.NameFileFilter;
import org.apache.commons.io.filefilter.NotFileFilter;
import org.apache.commons.lang.StringUtils;
import org.ebayopensource.turmeric.eclipse.functional.test.SoaTestConstants;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.ISOARepositorySystem;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.GlobalRepositorySystem;
import org.ebayopensource.turmeric.eclipse.test.utils.WsdlUtilTest;
import org.ebayopensource.turmeric.eclipse.utils.io.PropertiesFileUtil;
import org.ebayopensource.turmeric.repositorysystem.imp.utils.TurmericConstants;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;


public class TLArtifactValidator implements IResourceVisitor {

	private String goldCopyDir = null;
	private Collection<File> files = null;
	private boolean matches = true;

	@SuppressWarnings("unchecked")
	public void setGoldCopyRootDir(String subDir) {
		goldCopyDir = WsdlUtilTest.getPluginOSPath(SoaTestConstants.PLUGIN_ID,
				"test-data/" + subDir);
		System.out.println(" --- gold Copy Dir : " + goldCopyDir.toString());

		ISOARepositorySystem repositorySystem = GlobalRepositorySystem
				.instanceOf().getActiveRepositorySystem();

		ArrayList<String> dirList = new ArrayList<String>();
		dirList.add("build"); // '..\bin' is now '..\build'
		dirList.add(".settings");
		dirList.add("bin");

		ArrayList<String> fileList = new ArrayList<String>();
		fileList.add(".project");
		fileList.add("ObjectFactory.java");
		fileList.add("pom.xml");

		if (!repositorySystem.getId().equals(TurmericConstants.TURMERIC_ID)) {
			fileList.add("project.xml");
			fileList.add("dev.properties");
			fileList.add("ebox.classpath");
		} else {
			fileList.add("ebox.classpath");
			fileList.add(".classpath");
		}
		// else
		// {
		// to do: add content validation for pom.xml;
		// the tags in pom.xml are not populated in the same order everytime
		// fileList.add("pom.xml");
		// }

		NotFileFilter dirFilter = new NotFileFilter(new NameFileFilter(dirList));
		NotFileFilter fileFilter = new NotFileFilter(new NameFileFilter(
				fileList));
		files = FileUtils.listFiles(new File(goldCopyDir), fileFilter,
				dirFilter);
	}

	public boolean isMatches() {
		if (files.size() > 0) {
			for (Iterator<File> iterator = files.iterator(); iterator.hasNext();) {
				File file = iterator.next();
				System.out.println("Goldcopy extra file: "
						+ file.getAbsolutePath());
			}
		}
		return matches && !files.isEmpty();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.core.resources.IResourceVisitor#visit(org.eclipse.core.resources
	 * .IResource)
	 */
	public boolean visit(IResource resource) throws CoreException {

		if (!matches)
			return false;

		IPath path = resource.getProjectRelativePath();
		if (StringUtils.isEmpty(path.toString()))
			return true;

		if (path.toString().startsWith("bin"))
			return true;

		if (path.toString().startsWith("build"))
			return true;

		// Verify Builder and Nature in .project
		if (path.toString().equalsIgnoreCase(".project"))
			return true;

		// Skipping validaton of ObjectFactory.java as it is generated in a
		// different way each time.
		if (path.toString().endsWith("ObjectFactory.java"))
			return true;

		if (path.toString().startsWith(".settings"))
			return true;

		// Skipping validaton of TypeMapping.xml as it is generated in a
		// different way each time.
		if (path.toString().endsWith("TypeInformation.xml"))
			return true;

		if (path.toString().endsWith(".episode"))
			return true;

		if (path.toString().endsWith("pom.xml"))
			return true;

		if (path.toString().endsWith(".classpath"))
			return true;

		// now compare with the files in the gold copy
		IPath rsrcPath = resource.getProject().getLocation().append(path);
		System.out.println(rsrcPath.toOSString());
		if (rsrcPath.toFile().isFile()) {
			if (rsrcPath.toString().endsWith("properties")) {
				Properties srcProp = new Properties();
				FileInputStream fis = null;
				try {
					fis = new FileInputStream(rsrcPath.toFile());
					srcProp.load(fis);
				} catch (Exception e) {
					e.printStackTrace();
					matches = false;
				} finally {
					try {
						if (fis != null) {
							fis.close();
						}
					} catch (IOException ex) {
						
					}
				}
				Properties goldCopyProp = new Properties();
				FileInputStream fileStream = null;
				try {
					fileStream = new FileInputStream(new File(goldCopyDir + "/" + path.toString()));
					goldCopyProp.load(fileStream);
				} catch (Exception e) {
					e.printStackTrace();
					matches = false;
				} finally {
					if (fileStream != null) {
						try {
							fileStream.close();
						} catch (IOException e) {
							
						}
					}
				}
				if (matches) {
					matches = PropertiesFileUtil.isEqual(srcProp, goldCopyProp);
				}
			} else {
				try {
					if (path.toString().startsWith("gen-src")) {
						if (path.toString().endsWith(
								resource.getProject().getName() + ".java")) {
							// Assert the location
							if (FileUtils.contentEquals(
									rsrcPath.toFile(),
									new File(goldCopyDir + "/"
											+ path.toString())) == false) {
								System.out
										.println("the following did not match: "
												+ goldCopyDir + "/" + path);
								matches = false;
							}
						} else {
							matches = true;
						}
					} else if (path.toString().startsWith("gen-meta-src")) {
						if (FileUtils.contentEquals(rsrcPath.toFile(),
								new File(goldCopyDir + "/" + path.toString())) == false) {
							System.out.println("the following did not match: "
									+ goldCopyDir + "/" + path);
							matches = false;
						}

					} else {
						if (FileUtils.contentEquals(rsrcPath.toFile(),
								new File(goldCopyDir + "/" + path.toString())) == false) {
							System.out.println("the following did not match: "
									+ goldCopyDir + "/" + path);
							matches = false;
						}
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					System.out.println("the following did not match: "
							+ goldCopyDir + "/" + path);
					matches = false;
				}
			}
		} else {
			files.remove(new File(goldCopyDir + "/" + path.toString()));
		}
		return true;

	}
}
