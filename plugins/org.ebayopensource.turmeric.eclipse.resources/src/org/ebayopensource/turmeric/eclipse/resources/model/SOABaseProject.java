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


// TODO: Auto-generated Javadoc
/**
 * The Class SOABaseProject.
 *
 * @author smathew
 */
public abstract class SOABaseProject implements ISOAProject{

	/** The repository path. */
	private IPath repositoryPath;

	/** The required libraries. */
	private Set<String> requiredLibraries = new TreeSet<String>();

	/** The required projects. */
	private Set<String> requiredProjects = new TreeSet<String>();

	/** The source directories. */
	private List<SOAProjectSourceDirectory> sourceDirectories;
	
	/** The eclipse metadata. */
	private SOAProjectEclipseMetadata eclipseMetadata;
	
	/** The metadata. */
	private AbstractSOAMetadata metadata;
	
	/** The output folder. */
	private String outputFolder = SOAProjectConstants.FOLDER_OUTPUT_DIR;
	
	/** The eclipse natures. */
	private final List<String> eclipseNatures = new ArrayList<String>(5);
	
	/** The linked resources. */
	private final List<ProjectLinkedResource> linkedResources = new ArrayList<ProjectLinkedResource>(3);
	
	/**
	 * Instantiates a new sOA base project.
	 */
	protected SOABaseProject() {
		super();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IProject getProject() {
		return eclipseMetadata != null ? eclipseMetadata.getProject() : null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public AbstractSOAMetadata getMetadata() {
		return metadata;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
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
	 * Gets the eclipse natures.
	 *
	 * @return A List of Eclipse Natures as String
	 */
	public List<String> getEclipseNatures() {
		return eclipseNatures;
	}
	
	/**
	 * Adds the eclipse nature.
	 *
	 * @param eclipseNature the eclipse nature
	 * @return whether the addition was successful or not.
	 */
	public boolean addEclipseNature(String eclipseNature) {
		return eclipseNatures.add(eclipseNature);
	}
	
	/**
	 * Gets the linked resources.
	 *
	 * @return A list ofProjectLinkedResource objects
	 */
	public List<ProjectLinkedResource> getLinkedResources() {
		return linkedResources;
	}
	
	/**
	 * Adds the project linked resource.
	 *
	 * @param linkedResource the linked resource
	 * @return whether the resources were added successfully or not
	 */
	public boolean addProjectLinkedResource(ProjectLinkedResource linkedResource) {
		return linkedResources.add(linkedResource);
	}

	/**
	 * Gets the soa metadata class.
	 *
	 * @return a SOAMetadata class
	 */
	protected Class<? extends AbstractSOAMetadata> getSOAMetadataClass() {
		return AbstractSOAMetadata.class;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IPath getRepositoryPath() {
		return repositoryPath;
	}

	/**
	 * Sets the repository path.
	 *
	 * @param repositoryPath the new repository path
	 */
	public void setRepositoryPath(IPath repositoryPath) {
		this.repositoryPath = repositoryPath;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set<String> getRequiredLibraries() {
		return requiredLibraries;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setRequiredLibraries(Set<String> requiredLibraries) {
		this.requiredLibraries = requiredLibraries;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set<String> getRequiredProjects() {
		return requiredProjects;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setRequiredProjects(Set<String> requiredProjects) {
		this.requiredProjects = requiredProjects;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
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
	 * Gets the output directory.
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
	@Override
	public SOAProjectEclipseMetadata getEclipseMetadata() {
		return eclipseMetadata;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setEclipseMetadata(
			SOAProjectEclipseMetadata soaProjectEclipseMetadata) {
		this.eclipseMetadata = soaProjectEclipseMetadata;
	}

	/**
	 * Gets the source sub folders.
	 *
	 * @return A List of source sub folders
	 */
	public abstract List<String> getSourceSubFolders() ;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setOutputFolder(String outputFolder) {
		this.outputFolder = outputFolder;
	}
	
	/**
	 * Gets the raw output folder.
	 *
	 * @return the output folder name
	 */
	public String getRawOutputFolder() {
		return this.outputFolder;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public IPath getOutputFolder() {
		return getProject()
		.getFolder(outputFolder).getLocation();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getProjectName() {
		return eclipseMetadata.getProjectName();
	}
	
	/**
	 * The Class ProjectLinkedResource.
	 */
	public static class ProjectLinkedResource {
		
		/** The Constant LINK_TYPE_RESOURCE. */
		public static final int LINK_TYPE_RESOURCE = 1;
		
		/** The Constant LINK_TYPE_FOLDER. */
		public static final int LINK_TYPE_FOLDER = 2;
		
		/** The name. */
		private String name;
		
		/** The type. */
		private int type = LINK_TYPE_RESOURCE;
		
		/** The location. */
		private String location;
		
		/**
		 * Instantiates a new project linked resource.
		 *
		 * @param name the name
		 * @param type the type
		 * @param location the location
		 */
		public ProjectLinkedResource(String name, int type, String location) {
			super();
			this.name = name;
			this.type = type;
			this.location = location;
		}
		
		/**
		 * Instantiates a new project linked resource.
		 *
		 * @param name the name
		 * @param location the location
		 */
		public ProjectLinkedResource(String name, String location) {
			this(name, LINK_TYPE_RESOURCE, location);
		}

		/**
		 * Gets the name.
		 *
		 * @return the Linked Resource Name
		 */
		public String getName() {
			return name;
		}

		/**
		 * Sets the name.
		 *
		 * @param name the new name
		 */
		public void setName(String name) {
			this.name = name;
		}

		/**
		 * Gets the type.
		 *
		 * @return the type
		 */
		public int getType() {
			return type;
		}

		/**
		 * Sets the type.
		 *
		 * @param type the new type
		 */
		public void setType(int type) {
			this.type = type;
		}

		/**
		 * Gets the location.
		 *
		 * @return the location
		 */
		public String getLocation() {
			return location;
		}

		/**
		 * Sets the location.
		 *
		 * @param location the new location
		 */
		public void setLocation(String location) {
			this.location = location;
		}
	}
}
