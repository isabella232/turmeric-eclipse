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
	 * Gets the project name.
	 *
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
	 * Gets the repository path.
	 *
	 * @return the repository path
	 */
	public IPath getRepositoryPath();
	
	
	/**
	 * Sets the required libraries.
	 *
	 * @param requiredLibs the new required libraries
	 */
	public void setRequiredLibraries(Set<String> requiredLibs);
	
	/**
	 * Gets the required libraries.
	 *
	 * @return A set of required libraries as strings
	 */
	public Set<String> getRequiredLibraries();

	/**
	 * Sets the required projects.
	 *
	 * @param requiredProjects the new required projects
	 */
	public void setRequiredProjects(Set<String> requiredProjects);
	
	/**
	 * Gets the required projects.
	 *
	 * @return A set of Required projects as String
	 */
	public Set<String> getRequiredProjects();

	/**
	 * Gets the source directories.
	 *
	 * @return A list of SOAProjectSource directories
	 */
	public List<SOAProjectSourceDirectory> getSourceDirectories();
	
	/**
	 * Gets the source directory names.
	 *
	 * @return A List of source directory names as strings
	 */
	public List<String> getSourceDirectoryNames();
	
	/**
	 * Sets the output folder.
	 *
	 * @param outputFolder the new output folder
	 */
	public void setOutputFolder(String outputFolder);
	
	/**
	 * Gets the output folder.
	 *
	 * @return the path for the Output Folder
	 */
	public IPath getOutputFolder();

	/**
	 * Gets the eclipse metadata.
	 *
	 * @return the Eclipse Meta Data
	 */
	public SOAProjectEclipseMetadata getEclipseMetadata();

	/**
	 * Sets the eclipse metadata.
	 *
	 * @param soaProjectEclipseMetadata the new eclipse metadata
	 */
	public void setEclipseMetadata(SOAProjectEclipseMetadata soaProjectEclipseMetadata);
	
	/**
	 * Gets the metadata.
	 *
	 * @return the MetaData
	 */
	public AbstractSOAMetadata getMetadata();

	/**
	 * Sets the metadata.
	 *
	 * @param soaMatadata the new metadata
	 */
	public void setMetadata(AbstractSOAMetadata soaMatadata);
	
	/**
	 * The Class SOAProjectSourceDirectory.
	 */
	public static class SOAProjectSourceDirectory implements Comparable<SOAProjectSourceDirectory>{
		private String location;
		private boolean isTest;
		private String outputLocation;
		private String[] excludePatterns;
		
		/**
		 * Instantiates a new sOA project source directory.
		 */
		public SOAProjectSourceDirectory() {
			super();
		}
		
		/**
		 * Instantiates a new sOA project source directory.
		 *
		 * @param location the location
		 */
		public SOAProjectSourceDirectory(String location) {
			this(location, SOAProjectConstants.FOLDERS_TEST.contains(location));
		}
		
		
		/**
		 * Instantiates a new sOA project source directory.
		 *
		 * @param location the location
		 * @param isTest the is test
		 */
		public SOAProjectSourceDirectory(String location, boolean isTest) {
			super();
			this.location = location;
			this.isTest = isTest;
		}
		
		/**
		 * Instantiates a new sOA project source directory.
		 *
		 * @param location the location
		 * @param outputLocation the output location
		 */
		public SOAProjectSourceDirectory(String location, String outputLocation) {
			super();
			this.location = location;
			this.outputLocation = outputLocation;
		}
		
		/**
		 * Instantiates a new sOA project source directory.
		 *
		 * @param location the location
		 * @param outputLocation the output location
		 * @param excludePatterns the exclude patterns
		 * @param isTest the is test
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
		 * Gets the location.
		 *
		 * @return the location as a string
		 */
		public String getLocation() {
			return location;
		}
		
		
		/**
		 * Sets the location.
		 *
		 * @param location the location
		 */
		public void setLocation(String location) {
			this.location = location;
		}
		
		/**
		 * Checks if is test.
		 *
		 * @return is this a test project
		 */
		public boolean isTest() {
			return isTest;
		}
		
		/**
		 * Sets the test.
		 *
		 * @param isTest the new test
		 */
		public void setTest(boolean isTest) {
			this.isTest = isTest;
		}
		
		/**
		 * Gets the output location.
		 *
		 * @return the output location for class files
		 */
		public String getOutputLocation() {
			return outputLocation;
		}
		
		/**
		 * Sets the output location.
		 *
		 * @param outputLocation the new output location
		 */
		public void setOutputLocation(String outputLocation) {
			this.outputLocation = outputLocation;
		}
		
		/**
		 * Gets the exclude patterns.
		 *
		 * @return An array of regular expression exclusion patterns
		 */
		public String[] getExcludePatterns() {
			return excludePatterns;
		}
		
		/**
		 * Sets the exclude patterns.
		 *
		 * @param excludePatterns the new exclude patterns
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
