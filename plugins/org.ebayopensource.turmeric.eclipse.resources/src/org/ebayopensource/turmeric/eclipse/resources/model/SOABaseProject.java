/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.resources.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.ebayopensource.turmeric.eclipse.core.resources.constants.SOAProjectConstants;
import org.ebayopensource.turmeric.eclipse.utils.lang.StringUtil;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IPath;


/**
 * @author smathew
 *
 */
public abstract class SOABaseProject implements ISOAProject{

	private IPath repositoryPath;

	private Set<String> requiredLibraries = new TreeSet<String>();

	private Set<String> requiredProjects = new TreeSet<String>();

	private List<SOAProjectSourceDirectory> sourceDirectories;
	
	private SOAProjectEclipseMetadata eclipseMetadata;
	
	private AbstractSOAMetadata metadata;
	
	private String outputFolder = SOAProjectConstants.FOLDER_OUTPUT_DIR;
	
	private final List<String> eclipseNatures = new ArrayList<String>(5);
	
	private final List<ProjectLinkedResource> linkedResources = new ArrayList<ProjectLinkedResource>(3);
	
	/**
	 * 
	 */
	protected SOABaseProject() {
		super();
	}

	/**
	 * {@inheritDoc}
	 */
	public IProject getProject() {
		return eclipseMetadata != null ? eclipseMetadata.getProject() : null;
	}

	/**
	 * {@inheritDoc}
	 */
	public AbstractSOAMetadata getMetadata() {
		return metadata;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setMetadata(AbstractSOAMetadata soaMetadata) {
		final Class<?> clazz = getSOAMetadataClass();
		if (clazz.isInstance(soaMetadata)) {
			this.metadata = soaMetadata;
		}
		else {
			throw new IllegalArgumentException(
					StringUtil.toString("SOAMetadata must be an instance of ", 
					clazz.getName(), "->", soaMetadata));
		}
	}
	
	/**
	 * 
	 * @return A List of Eclipse Natures as String
	 */
	public List<String> getEclipseNatures() {
		return eclipseNatures;
	}
	
	/**
	 * 
	 * @param eclipseNature 
	 * @return whether the addition was successful or not.
	 */
	public boolean addEclipseNature(String eclipseNature) {
		return eclipseNatures.add(eclipseNature);
	}
	
	/**
	 * 
	 * @return A list ofProjectLinkedResource objects
	 */
	public List<ProjectLinkedResource> getLinkedResources() {
		return linkedResources;
	}
	
	/**
	 * 
	 * @param linkedResource 
	 * @return whether the resources were added successfully or not
	 */
	public boolean addProjectLinkedResource(ProjectLinkedResource linkedResource) {
		return linkedResources.add(linkedResource);
	}

	/**
	 * 
	 * @return a SOAMetadata class
	 */
	protected Class<? extends AbstractSOAMetadata> getSOAMetadataClass() {
		return AbstractSOAMetadata.class;
	}

	/**
	 * {@inheritDoc}
	 */
	public IPath getRepositoryPath() {
		return repositoryPath;
	}

	/**
	 * 
	 * @param repositoryPath 
	 */
	public void setRepositoryPath(IPath repositoryPath) {
		this.repositoryPath = repositoryPath;
	}

	/**
	 * {@inheritDoc}
	 */
	public Set<String> getRequiredLibraries() {
		return requiredLibraries;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setRequiredLibraries(Set<String> requiredLibraries) {
		this.requiredLibraries = requiredLibraries;
	}

	/**
	 * {@inheritDoc}
	 */
	public Set<String> getRequiredProjects() {
		return requiredProjects;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setRequiredProjects(Set<String> requiredProjects) {
		this.requiredProjects = requiredProjects;
	}

	/**
	 * {@inheritDoc}
	 */
	public List<SOAProjectSourceDirectory> getSourceDirectories() {
		if (sourceDirectories == null) {
			sourceDirectories = new ArrayList<SOAProjectSourceDirectory>();
			for (final String srcDir : getSourceDirectoryNames()) {
				sourceDirectories.add(new SOAProjectSourceDirectory(srcDir));
			}
		}
		return sourceDirectories;
	}

	/**
	 * {@inheritDoc}
	 * @param sourceDirectories
	 */
	public void setSourceDirectories(List<SOAProjectSourceDirectory> sourceDirectories) {
		this.sourceDirectories = sourceDirectories;
	}


	/**
	 * 
	 * @return a list of output directories 
	 */
	public static List<String> getOutputDirectory() {
		final List<String> outPutArrayList = new ArrayList<String>();
		outPutArrayList.add(SOAProjectConstants.FOLDER_OUTPUT_DIR);
		return outPutArrayList;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public SOAProjectEclipseMetadata getEclipseMetadata() {
		return eclipseMetadata;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setEclipseMetadata(
			SOAProjectEclipseMetadata soaProjectEclipseMetadata) {
		this.eclipseMetadata = soaProjectEclipseMetadata;
	}

	/**
	 * 
	 * @return A List of source sub folders
	 */
	public abstract List<String> getSourceSubFolders() ;

	/**
	 * {@inheritDoc}
	 */
	public void setOutputFolder(String outputFolder) {
		this.outputFolder = outputFolder;
	}
	
	/**
	 * 
	 * @return the output folder name
	 */
	public String getRawOutputFolder() {
		return this.outputFolder;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public IPath getOutputFolder() {
		return getProject()
		.getFolder(outputFolder).getLocation();
	}

	/**
	 * {@inheritDoc}
	 */
	public String getProjectName() {
		return eclipseMetadata.getProjectName();
	}
	
	/**
	 * 
	 * 
	 *
	 */
	public static class ProjectLinkedResource {
		/**
		 * 
		 */
		public static final int LINK_TYPE_RESOURCE = 1;
		
		/**
		 * 
		 */
		public static final int LINK_TYPE_FOLDER = 2;
		
		private String name;
		private int type = LINK_TYPE_RESOURCE;
		private String location;
		
		/**
		 * 
		 * @param name 
		 * @param type 
		 * @param location 
		 */
		public ProjectLinkedResource(String name, int type, String location) {
			super();
			this.name = name;
			this.type = type;
			this.location = location;
		}
		
		/**
		 * 
		 * @param name 
		 * @param location 
		 */
		public ProjectLinkedResource(String name, String location) {
			this(name, LINK_TYPE_RESOURCE, location);
		}

		/**
		 * 
		 * @return the Linked Resource Name
		 */
		public String getName() {
			return name;
		}

		/**
		 * 
		 * @param name 
		 */
		public void setName(String name) {
			this.name = name;
		}

		/**
		 * 
		 * @return the type
		 */
		public int getType() {
			return type;
		}

		/**
		 * 
		 * @param type 
		 */
		public void setType(int type) {
			this.type = type;
		}

		/**
		 * 
		 * @return the location
		 */
		public String getLocation() {
			return location;
		}

		/**
		 * 
		 * @param location 
		 */
		public void setLocation(String location) {
			this.location = location;
		}
	}
}
