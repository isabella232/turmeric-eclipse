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
package org.ebayopensource.turmeric.eclipse.test.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.regex.Pattern;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.filefilter.NameFileFilter;
import org.apache.commons.io.filefilter.NotFileFilter;
import org.apache.commons.lang.StringUtils;
import org.custommonkey.xmlunit.Diff;
import org.custommonkey.xmlunit.ElementNameAndAttributeQualifier;
import org.custommonkey.xmlunit.XMLUnit;
import org.ebayopensource.turmeric.eclipse.functional.test.SoaTestConstants;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.GlobalRepositorySystem;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.ISOARepositorySystem;
import org.ebayopensource.turmeric.eclipse.test.utils.WsdlUtilTest;
import org.ebayopensource.turmeric.eclipse.utils.io.PropertiesFileUtil;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;

/**
 * @author rpallikonda
 * 
 */
public class ProjectArtifactValidator implements IResourceVisitor {

	private String goldCopyDir = null;
	private Collection<File> files = null;
	private boolean matches = true;
	private boolean isV3 = false;
	private List<String> filterDirList = new ArrayList<String>();
	private List<String> filterFileList = new ArrayList<String>();
	private static StringBuffer message = new StringBuffer();

	public ProjectArtifactValidator(String... filterDirList) {
		super();
		if (filterDirList != null) {
			this.filterDirList.addAll(Arrays.asList(filterDirList));
		}
	}

	public void setGoldCopyRootDir(String subDir) {
		goldCopyDir = WsdlUtilTest.getPluginOSPath(SoaTestConstants.PLUGIN_ID,
				"data/extractedData/" + subDir);
		System.out.println(" --- gold Copy Dir : " + goldCopyDir.toString());

		ISOARepositorySystem repositorySystem = GlobalRepositorySystem
				.instanceOf().getActiveRepositorySystem();

		List<String> dirList = this.filterDirList;
		dirList.add("build"); // '..\bin' is now '..\build'
		dirList.add(".settings");
		dirList.add("bin");
		dirList.add("target");

		List<String> fileList = filterFileList;
		fileList.add(".project");
		fileList.add("pom.xml");
		// filtering out this file as there is a harcoded path in this property
		// file which will make the comparison fail if executed from some other
		// system.
		// TODO need to find a way to compare property file
		fileList.add("service_intf_project.properties");
		fileList.add("project.xml");
		fileList.add(".classpath");
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
			for (Iterator iterator = files.iterator(); iterator.hasNext();) {
				File file = (File) iterator.next();
				System.out.println("Goldcopy file : " + file.getAbsolutePath());
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
	@Override
	public boolean visit(IResource resource) throws CoreException {

		// if (!matches)
		// return false;
		
		
		

		IPath path = resource.getProjectRelativePath();

		String filePath = path.toString();

		if (StringUtils.isEmpty(filePath))
			return true;

		for (String dir : filterDirList) {
			IPath dirPath = new Path(dir);
			if (path.matchingFirstSegments(dirPath) == dirPath.segmentCount()) {
				return true;
			}
		}

		String lastSegment = path.lastSegment();
		for (String file : this.filterFileList) {
			if (lastSegment.equalsIgnoreCase(file))
				return true;
		}

		// now compare with the files in the gold copy
		IPath rsrcPath = resource.getProject().getLocation().append(path);

		if (rsrcPath.toFile().isFile()) {

			if (!rsrcPath.toFile().exists()
					&& (new File(goldCopyDir + File.separator + path.toString())
							.exists())) {

				formatMessage(rsrcPath, "generated copy does not exist");

			}

			System.out.println(" --- Functional test generated file : "
					+ rsrcPath.toOSString());
			if ("properties".equals(rsrcPath.getFileExtension())) {

				Properties srcProp = new Properties();
				InputStream ins = null;
				try {
					ins = new FileInputStream(rsrcPath.toFile());
					srcProp.load(ins);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					matches = false;
				} finally {
					IOUtils.closeQuietly(ins);
				}
				Properties goldCopyProp = new Properties();
				try {
					ins = new FileInputStream(new File(goldCopyDir
							+ File.separator + path.toString()));
					goldCopyProp.load(ins);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					matches = false;
				} finally {
					IOUtils.closeQuietly(ins);
				}
				if (matches) {
					if (srcProp.containsKey("original_wsdl_uri")) {
						srcProp.remove("original_wsdl_uri");
						goldCopyProp.remove("original_wsdl_uri");
					}
					if (PropertiesFileUtil.isEqual(srcProp, goldCopyProp) == false) {

						formatMessage(rsrcPath, "gold copy did not match");

						System.out.println("the following did not match: "
								+ goldCopyDir + "/" + path);
						matches = false;
					}
				}

			} else {
				try {

					if ("java".equals(rsrcPath.getFileExtension())) {
						
//						if(rsrcPath.lastSegment().contains("TypeDefsBuilder")){
//						//failing in linux box.	
//						return true;
//							
//						}
						
						System.out.println("java file : "
								+ rsrcPath.toOSString());
						File goldCopyJava = new File(goldCopyDir
								+ File.separator + path.toString());

						if (!goldCopyJava.exists()) {
							formatMessage(rsrcPath, "gold copy do not exist");
							System.out.println("File not in gold copy"
									+ goldCopyDir + "/" + path);
							matches = false;
							return true;
						}
						if (compareTwoFiles(rsrcPath.toFile(), goldCopyJava) == false) {

							formatMessage(rsrcPath, "gold copy did not match");
							System.out.println("the following did not match: "
									+ goldCopyDir + "/" + path);

							matches = false;
						}

					} else {
						if ("xml".equals(rsrcPath.getFileExtension())) {
							System.out.println("xml file : "
									+ rsrcPath.toOSString());

							/*
							 * if (FileUtils.contentEquals( rsrcPath.toFile(),
							 * new File(goldCopyDir + "/" + path.toString())) ==
							 * false) { formatMessage(rsrcPath); System.out
							 * .println("the following did not match: " +
							 * goldCopyDir + "/" + path); matches = false; }
							 */

							try {
								String xmlfile = readFileAsString(rsrcPath
										.toOSString());

								// Only Validated if we have a schema with the
								// file
								// XMLAssert.assertXMLValid(xmlfile);
								// XMLAssert.assertXMLValid(goldCopyDir
								// + File.separator + path);
								XMLUnit.setIgnoreAttributeOrder(true);
								XMLUnit.setIgnoreWhitespace(true);
								
								String goldFile = readFileAsString(goldCopyDir
										+ File.separator + path);
//								XMLAssert.assertXMLEqual(goldFile,xmlfile);
								Diff diff = new Diff(goldFile, xmlfile);
								//Whichever element has the attribute "name" in the goldfile will be compared with 
								//all the elements that have attribute "name" in the xmlfile, takes care of order differences
								diff.overrideElementQualifier(new ElementNameAndAttributeQualifier("name"));
								if(!diff.similar()){
									formatMessage(rsrcPath,
									"gold copy did not match");
							System.out
									.println("the following did not match: "
											+ goldCopyDir + "/" + path);
							matches = false;
								}
//								XMLAssert.assertXMLEqual(goldFile,xmlfile);

							} catch (Exception e) {
								formatMessage(rsrcPath,
										"gold copy did not match");
								e.printStackTrace();
							}

						} 
						else if ("wsdl".equals(rsrcPath.getFileExtension())) {

							System.out.println("wsdlFile file : "
									+ rsrcPath.toOSString());

							/*
							 * if (FileUtils.contentEquals( rsrcPath.toFile(),
							 * new File(goldCopyDir + "/" + path.toString())) ==
							 * false) { formatMessage(rsrcPath); System.out
							 * .println("the following did not match: " +
							 * goldCopyDir + "/" + path); matches = false; }
							 */

//							try {
								// String wsdlFile = readFileAsString(rsrcPath
								// .toOSString());
								//
								//
								//
								// // Only Validated if we have a schema with
								// the file
								// //XMLAssert.assertXMLValid(xmlfile);
								// //XMLAssert.assertXMLValid(goldCopyDir
								// // + File.separator + path);
								// String goldFile =
								// readFileAsString(goldCopyDir
								// + File.separator + path);
								// XMLAssert.assertXMLEqual(goldFile,wsdlFile);

//								DocumentBuilderFactory dbf = DocumentBuilderFactory
//										.newInstance();
//								dbf.setNamespaceAware(true);
//								dbf.setCoalescing(true);
//								dbf.setIgnoringElementContentWhitespace(true);
//								dbf.setIgnoringComments(true);
//								DocumentBuilder db;
//								try {
//									db = dbf.newDocumentBuilder();
//									Document doc1 = db.parse(new File(rsrcPath
//											.toOSString()));
//									doc1.normalizeDocument();
//
//									Document doc2 = db
//											.parse(new File(goldCopyDir + "/"
//													+ path.toString()));
//									doc2.normalizeDocument();
//									if (!doc1.isEqualNode(doc2)) {
//
//										formatMessage(rsrcPath,
//												"gold copy did not match");
//										System.out
//												.println("the following did not match: "
//														+ goldCopyDir
//														+ "/"
//														+ path);
//										matches = false;
//
//									}
//								} catch (ParserConfigurationException e) {
//									// TODO Auto-generated catch block
//									e.printStackTrace();
//								} catch (SAXException e) {
//									// TODO Auto-generated catch block
//									e.printStackTrace();
//								}
//
//							} catch (Exception e) {
//								formatMessage(rsrcPath,
//										"gold copy did not match");
//								e.printStackTrace();
//							}

						} 
						else {

							System.out.println("other file : "
									+ rsrcPath.toOSString());

							if (FileUtils.contentEquals(
									rsrcPath.toFile(),
									new File(goldCopyDir + "/"
											+ path.toString())) == false) {
								formatMessage(rsrcPath,
										"gold copy did not match");
								System.out
										.println("the following did not match: "
												+ goldCopyDir + "/" + path);
								matches = false;
							}
						}

					}
				} catch (FileNotFoundException fnfe) {
					formatMessage(rsrcPath, "file not found exception");

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

	private static String readFileAsString(String filePath)
			throws java.io.IOException {
		StringBuffer fileData = new StringBuffer(1000);
		BufferedReader reader = new BufferedReader(new FileReader(filePath));
		char[] buf = new char[1024];
		int numRead = 0;
		while ((numRead = reader.read(buf)) != -1) {
			String readData = String.valueOf(buf, 0, numRead);
			fileData.append(readData);
			buf = new char[1024];
		}
		reader.close();
		return fileData.toString();
	}

	private boolean compareTwoFiles(File file1, File file2) throws IOException {
		@SuppressWarnings("unchecked")
		List<String> firstFile = FileUtils.readLines(file1);

		ArrayList<String> trimmedList1 = new ArrayList<String>();

		ArrayList<String> trimmedList2 = new ArrayList<String>();
		@SuppressWarnings("unchecked")
		List<String> secondFile = FileUtils.readLines(file2);

		while (firstFile.remove(""))
			;
		while (secondFile.remove(""))
			;

		for (String s : firstFile) {
			trimmedList1.add(s.trim());
		}

		for (String s : secondFile) {
			trimmedList2.add(s.trim());
		}

		ArrayList<String> commentRemoved1 = new ArrayList<String>();

		ArrayList<String> commentRemoved2 = new ArrayList<String>();

		commentRemoved1.addAll(trimmedList1);
		Iterator<String> it = trimmedList1.iterator();
		String s = null;
		while (it.hasNext()) {
			s = it.next();
			if (Pattern.matches(CommentDetector.COMMENT_DETECTOR_REGEX, s))
				commentRemoved1.remove(s);
		}

		commentRemoved2.addAll(trimmedList2);
		it = trimmedList2.iterator();
		while (it.hasNext()) {
			s = it.next();
			if (Pattern.matches(CommentDetector.COMMENT_DETECTOR_REGEX, s))
				commentRemoved2.remove(s);
		}

		if (commentRemoved1.size() == commentRemoved2.size()) {
			if (commentRemoved1.containsAll(commentRemoved2))
				return true;
			else{
				 
				Iterator<String> itr = commentRemoved1.iterator();
				Iterator<String> itr2 = commentRemoved2.iterator();
				
				while(itr.hasNext() && itr2.hasNext()){
					
					String line1 = itr.next();
					String line2 = itr2.next();
					if(!line1.equals(line2)){
						System.out.println(line1  +" is not matching " + line2);
					}
				}
				System.out.println(commentRemoved1);
				System.out.println(commentRemoved2);
				return false;
			}

		} else
		{
			System.out.println("Number of lines in first file "+commentRemoved1.size());
			System.out.println("Number of lines in second file "+commentRemoved2.size());
			return false;
		}
	}

	public static StringBuffer getErroredFileMessage() {

		return ProjectArtifactValidator.message;
	}

	public static void setMessage(StringBuffer message) {
		ProjectArtifactValidator.message = message;
	}
	
	private void compileSrc(String filePath){
		
		JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
	
		int result = compiler.run(null, null, null,
				filePath);
	}

	private void formatMessage(IPath resource, String msg) {

		message.append("----The file " + resource.toOSString() + msg);
	}

}
