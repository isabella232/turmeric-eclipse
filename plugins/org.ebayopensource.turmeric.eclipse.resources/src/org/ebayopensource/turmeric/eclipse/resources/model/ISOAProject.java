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

import org.ebayopensource.turmeric.eclipse.core.resources.constants.SOAProjectConstants;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IPath;


/**
 * Represents a SOA project instance.
 * 
 * @author yayu
 *
 */
public interface ISOAProject {
	/**
	 * @return the name of the project
	 */
	public String getProjectName();

	/**
	 * {@inheritDoc}
	 * 
	 * @return the Eclipse IProject instance of the project
	 */
	public IProject getProject();

	/**
	 * 
	 * @return the repository path
	 */
	public IPath getRepositoryPath();
	
	
	/**
	 * 
	 * @param requiredLibs 
	 */
	public void setRequiredLibraries(Set<String> requiredLibs);
	
	/**
	 * 
	 * @return A set of required libraries as strings
	 */
	public Set<String> getRequiredLibraries();

	/**
	 * 
	 * @param requiredProjects 
	 */
	public void setRequiredProjects(Set<String> requiredProjects);
	
	/**
	 * 
	 * @return A set of Required projects as String
	 */
	public Set<String> getRequiredProjects();

	/**
	 * 
	 * @return A list of SOAProjectSource directories
	 */
	public List<SOAProjectSourceDirectory> getSourceDirectories();
	
	/**
	 * 
	 * @return A List of source directory names as strings
	 */
	public List<String> getSourceDirectoryNames();
	
	/**
	 * 
	 * @param outputFolder 
	 */
	public void setOutputFolder(String outputFolder);
	
	/**
	 * 
	 * @return the path for the Output Folder
	 */
	public IPath getOutputFolder();

	/**
	 * 
	 * @return the Eclipse Meta Data
	 */
	public SOAProjectEclipseMetadata getEclipseMetadata();

	/**
	 * 
	 * @param soaProjectEclipseMetadata 
	 */
	public void setEclipseMetadata(SOAProjectEclipseMetadata soaProjectEclipseMetadata);
	
	/**
	 * 
	 * @return the MetaData
	 */
	public AbstractSOAMetadata getMetadata();

	/**
	 * 
	 * @param soaMatadata 
	 */
	public void setMetadata(AbstractSOAMetadata soaMatadata);
	
	/**
	 * 
	 * 
	 *
	 */
	public static class SOAProjectSourceDirectory implements Comparable<SOAProjectSourceDirectory>{
		private String location;
		private boolean isTest;
		private String outputLocation;
		private String[] excludePatterns;
		
		/**
		 * 
		 */
		public SOAProjectSourceDirectory() {
			super();
		}
		
		/**
		 * 
		 * @param location 
		 */
		public SOAProjectSourceDirectory(String location) {
			this(location, SOAProjectConstants.FOLDERS_TEST.contains(location));
		}
		
		
		/**
		 * 
		 * @param location 
		 * @param isTest 
		 */
		public SOAProjectSourceDirectory(String location, boolean isTest) {
			super();
			this.location = location;
			this.isTest = isTest;
		}
		
		/**
		 * 
		 * @param location 
		 * @param outputLocation 
		 */
		public SOAProjectSourceDirectory(String location, String outputLocation) {
			super();
			this.location = location;
			this.outputLocation = outputLocation;
		}
		
		/**
		 * 
		 * @param location 
		 * @param outputLocation 
		 * @param excludePatterns 
		 * @param isTest 
		 */
		public SOAProjectSourceDirectory(String location, 
				String outputLocation, String[] excludePatterns, boolean isTest) {
			super();
			this.location = location;
			this.isTest = isTest;
			this.excludePatterns = excludePatterns;
			this.outputLocation = outputLocation;
		}
		
		/**
		 * 
		 * @return the location as a string 
		 */
		public String getLocation() {
			return location;
		}
		
		
		/**
		 * 
		 * @param location the location
		 */
		public void setLocation(String location) {
			this.location = location;
		}
		
		/**
		 * 
		 * @return is this a test project
		 */
		public boolean isTest() {
			return isTest;
		}
		
		/**
		 * 
		 * @param isTest 
		 */
		public void setTest(boolean isTest) {
			this.isTest = isTest;
		}
		
		/**
		 * 
		 * @return the output location for class files
		 */
		public String getOutputLocation() {
			return outputLocation;
		}
		
		/**
		 * 
		 * @param outputLocation 
		 */
		public void setOutputLocation(String outputLocation) {
			this.outputLocation = outputLocation;
		}
		
		/**
		 * 
		 * @return An array of regular expression exclusion patterns
		 */
		public String[] getExcludePatterns() {
			return excludePatterns;
		}
		
		/**
		 * 
		 * @param excludePatterns 
		 */
		public void setExcludePatterns(String[] excludePatterns) {
			this.excludePatterns = excludePatterns;
		}
		
		/**
		 * {@inheritDoc}
		 */
		public int compareTo(SOAProjectSourceDirectory o) {
			if (o != null && o.location != null) {
				o.location.compareToIgnoreCase(this.location);
			}
			return 0;
		}
	}
		
}
