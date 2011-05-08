/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.test.utils;

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
import org.ebayopensource.turmeric.eclipse.utils.io.PropertiesFileUtil;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;


/**
 * The Class TLArtifactValidator.
 */
public class TLArtifactValidator implements IResourceVisitor {

	private String goldCopyDir = null;
	private Collection<File> files = null;
	private boolean matches = true;

	/**
	 * Sets the gold copy root dir.
	 *
	 * @param subDir the new gold copy root dir
	 */
	@SuppressWarnings("unchecked")
	public void setGoldCopyRootDir(String subDir) {
//		goldCopyDir = WsdlUtilTest.getPluginOSPath(Activator.PLUGIN_ID, "test-data/" + subDir);

		ArrayList<String> dirList = new ArrayList<String>();
		dirList.add("bin");
		// dirList.add("gen-src");

		ArrayList<String> fileList = new ArrayList<String>();
		fileList.add(".project");
		fileList.add("ObjectFactory.java");
		fileList.add("project.xml");
		NotFileFilter dirFilter = new NotFileFilter(new NameFileFilter(dirList));
		NotFileFilter fileFilter = new NotFileFilter(new NameFileFilter(fileList)); 
		files = FileUtils.listFiles(new File(goldCopyDir), fileFilter, dirFilter);

	}

	/**
	 * Checks if is matches.
	 *
	 * @return true, if is matches
	 */
	public boolean isMatches() {
		if (files.size() > 0) {
			for (Iterator<File> iterator = files.iterator(); iterator.hasNext();) {
				File file = (File) iterator.next();
				System.out.println("Goldcopy extra file: " + file.getAbsolutePath() );
			}
		}
		return matches && !files.isEmpty();
	}
	
	/**
	 * {@inheritDoc}
	 */
	
	public boolean visit(IResource resource) throws CoreException {

		if (!matches) return false;

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

		// Skipping validaton of ObjectFactory.java as it is generated in a different way each time.
		if(path.toString().endsWith("ObjectFactory.java"))
			return true;
		
		if (path.toString().startsWith(".settings")) 
			return true;
		
		// Skipping validaton of TypeMapping.xml as it is generated in a different way each time.
		if (path.toString().endsWith("TypeInformation.xml")) 
			return true;
		
		if (path.toString().endsWith(".episode")) 
			return true;

		// now compare with the files in the gold copy
		IPath rsrcPath = resource.getProject().getLocation().append(path);
		System.out.println(rsrcPath.toOSString());
		if (rsrcPath.toFile().isFile()) {
			if (rsrcPath.toString().endsWith("properties")) {
				Properties srcProp = new Properties();
				FileInputStream srcIs = null;
				try {
					srcIs = new FileInputStream(rsrcPath.toFile());
					srcProp.load(srcIs);
				} catch (Exception e) {
					e.printStackTrace();
					matches = false;
				} finally {
					closeInputStream(srcIs);
				}
				Properties goldCopyProp = new Properties();
				FileInputStream is = null;
				try {
					is = new FileInputStream(new File(goldCopyDir + "/" + path.toString()));
					goldCopyProp.load(is);
				} catch (Exception e) {
					e.printStackTrace();
					matches = false;
				} finally {
					closeInputStream(is);
				}
				if (matches) {
					matches = PropertiesFileUtil.isEqual(srcProp, goldCopyProp);
				}
			} else {
				try {
					if (path.toString().startsWith("gen-src")) {
						if (path.toString().endsWith(resource.getProject().getName() + ".java")) 
						{
	//						Assert the location
							if (FileUtils.contentEquals(rsrcPath.toFile(), new File(goldCopyDir + "/" + path.toString())) == false)
							{
								System.out.println("the following did not match: " + goldCopyDir + "/" +path);
								matches = false;
							}
						}
						else {
							matches = true;
						}
					} else if (path.toString().startsWith("gen-meta-src")) {
						if (FileUtils.contentEquals(rsrcPath.toFile(), new File(goldCopyDir + "/" + path.toString())) == false)
						{
							System.out.println("the following did not match: "+ goldCopyDir + "/"  + path);
							matches = false;
						}
	
					} else {
						if (FileUtils.contentEquals(rsrcPath.toFile(), new File(goldCopyDir + "/" + path.toString())) == false)
						{
							System.out.println("the following did not match: "+ goldCopyDir + "/"  + path);
							matches = false;
						}
					}	
				} catch (IOException e) {
					e.printStackTrace();
					System.out.println("the following did not match: " + goldCopyDir + "/" + path);
					matches = false;
				}
			}
		} else {
			files.remove(new File(goldCopyDir + "/" + path.toString()));
		}
		return true;

	}

	private void closeInputStream(FileInputStream srcIs) {
		if (srcIs != null) {
			try {
				srcIs.close();
			} catch (IOException e) {
			}
		}
	}

}
