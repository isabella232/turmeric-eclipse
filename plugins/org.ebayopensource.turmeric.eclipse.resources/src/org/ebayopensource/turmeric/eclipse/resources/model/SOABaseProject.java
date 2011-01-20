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

import org.ebayopensource.turmeric.eclipse.resources.constants.SOAProjectConstants;
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
	
	protected SOABaseProject() {
		super();
	}

	public IProject getProject() {
		return eclipseMetadata != null ? eclipseMetadata.getProject() : null;
	}

	public AbstractSOAMetadata getMetadata() {
		return metadata;
	}

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
	
	public List<String> getEclipseNatures() {
		return eclipseNatures;
	}
	
	public boolean addEclipseNature(String eclipseNature) {
		return eclipseNatures.add(eclipseNature);
	}
	
	public List<ProjectLinkedResource> getLinkedResources() {
		return linkedResources;
	}
	
	public boolean addProjectLinkedResource(ProjectLinkedResource linkedResource) {
		return linkedResources.add(linkedResource);
	}

	protected Class<? extends AbstractSOAMetadata> getSOAMetadataClass() {
		return AbstractSOAMetadata.class;
	}

	public IPath getRepositoryPath() {
		return repositoryPath;
	}

	public void setRepositoryPath(IPath repositoryPath) {
		this.repositoryPath = repositoryPath;
	}

	public Set<String> getRequiredLibraries() {
		return requiredLibraries;
	}

	public void setRequiredLibraries(Set<String> requiredLibraries) {
		this.requiredLibraries = requiredLibraries;
	}

	public Set<String> getRequiredProjects() {
		return requiredProjects;
	}

	public void setRequiredProjects(Set<String> requiredProjects) {
		this.requiredProjects = requiredProjects;
	}

	public List<SOAProjectSourceDirectory> getSourceDirectories() {
		if (sourceDirectories == null) {
			sourceDirectories = new ArrayList<SOAProjectSourceDirectory>();
			for (final String srcDir : getSourceDirectoryNames()) {
				sourceDirectories.add(new SOAProjectSourceDirectory(srcDir));
			}
		}
		return sourceDirectories;
	}

	public void setSourceDirectories(List<SOAProjectSourceDirectory> sourceDirectories) {
		this.sourceDirectories = sourceDirectories;
	}


	public static List<String> getOutputDirectory() {
		final List<String> outPutArrayList = new ArrayList<String>();
		outPutArrayList.add(SOAProjectConstants.FOLDER_OUTPUT_DIR);
		return outPutArrayList;
	}
	
	public SOAProjectEclipseMetadata getEclipseMetadata() {
		return eclipseMetadata;
	}

	public void setEclipseMetadata(
			SOAProjectEclipseMetadata soaProjectEclipseMetadata) {
		this.eclipseMetadata = soaProjectEclipseMetadata;
	}

	public abstract List<String> getSourceSubFolders() ;

	public void setOutputFolder(String outputFolder) {
		this.outputFolder = outputFolder;
	}
	public String getRawOutputFolder() {
		return this.outputFolder;
	}
	public IPath getOutputFolder() {
		return getProject()
		.getFolder(outputFolder).getLocation();
	}

	public String getProjectName() {
		return eclipseMetadata.getProjectName();
	}
	
	public static class ProjectLinkedResource {
		public static final int LINK_TYPE_RESOURCE = 1;
		public static final int LINK_TYPE_FOLDER = 2;
		
		private String name;
		private int type = LINK_TYPE_RESOURCE;
		private String location;
		
		public ProjectLinkedResource(String name, int type, String location) {
			super();
			this.name = name;
			this.type = type;
			this.location = location;
		}
		
		public ProjectLinkedResource(String name, String location) {
			this(name, LINK_TYPE_RESOURCE, location);
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public int getType() {
			return type;
		}

		public void setType(int type) {
			this.type = type;
		}

		public String getLocation() {
			return location;
		}

		public void setLocation(String location) {
			this.location = location;
		}
	}
}
