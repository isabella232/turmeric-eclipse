/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.resources.model;

import java.util.List;
import java.util.Set;

import org.ebayopensource.turmeric.eclipse.resources.constants.SOAProjectConstants;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IPath;


/**
 * Represents a SOA project instance
 * @author yayu
 *
 */
public interface ISOAProject {
	/**
	 * @return the name of the project
	 */
	public String getProjectName();

	/**
	 * @return the Eclipse IProject instance of the project
	 */
	public IProject getProject();

	public IPath getRepositoryPath();

	public void setRequiredLibraries(Set<String> requiredLibs);
	
	public Set<String> getRequiredLibraries();

	public void setRequiredProjects(Set<String> requiredProjects);
	
	public Set<String> getRequiredProjects();

	public List<SOAProjectSourceDirectory> getSourceDirectories();
	
	public List<String> getSourceDirectoryNames();
	
	public void setOutputFolder(String outputFolder);
	
	public IPath getOutputFolder();

	public SOAProjectEclipseMetadata getEclipseMetadata();

	public void setEclipseMetadata(SOAProjectEclipseMetadata soaProjectEclipseMetadata);
	
	public AbstractSOAMetadata getMetadata();

	public void setMetadata(AbstractSOAMetadata soaMatadata);

//	public IPackageFragmentRoot getSource();
	
	public static class SOAProjectSourceDirectory implements Comparable<SOAProjectSourceDirectory>{
		private String location;
		private boolean isTest;
		private String outputLocation;
		private String[] excludePatterns;
		
		public SOAProjectSourceDirectory() {
			super();
		}
		public SOAProjectSourceDirectory(String location) {
			this(location, SOAProjectConstants.FOLDERS_TEST.contains(location));
		}
		
		public SOAProjectSourceDirectory(String location, boolean isTest) {
			super();
			this.location = location;
			this.isTest = isTest;
		}
		public SOAProjectSourceDirectory(String location, String outputLocation) {
			super();
			this.location = location;
			this.outputLocation = outputLocation;
		}
		public SOAProjectSourceDirectory(String location, 
				String outputLocation, String[] excludePatterns, boolean isTest) {
			super();
			this.location = location;
			this.isTest = isTest;
			this.excludePatterns = excludePatterns;
			this.outputLocation = outputLocation;
		}
		public String getLocation() {
			return location;
		}
		public void setLocation(String location) {
			this.location = location;
		}
		public boolean isTest() {
			return isTest;
		}
		public void setTest(boolean isTest) {
			this.isTest = isTest;
		}
		public String getOutputLocation() {
			return outputLocation;
		}
		public void setOutputLocation(String outputLocation) {
			this.outputLocation = outputLocation;
		}
		public String[] getExcludePatterns() {
			return excludePatterns;
		}
		public void setExcludePatterns(String[] excludePatterns) {
			this.excludePatterns = excludePatterns;
		}
		public int compareTo(SOAProjectSourceDirectory o) {
			if (o != null && o.location != null) {
				o.location.compareToIgnoreCase(this.location);
			}
			return 0;
		}
	}
		
}
