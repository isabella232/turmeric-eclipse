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
package org.ebayopensource.turmeric.eclipse.resources.ui.model;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yayu
 * @since 1.0.0
 *
 */
public abstract class AbstractSOAProjetParamModel extends BaseServiceParamModel {
	private String projectName;
	
	private final List<String> eclipseNatures = new ArrayList<String>(5);
	
	private final List<UIModelProjectLinkedResource> linkedResources = new ArrayList<UIModelProjectLinkedResource>(3);
	
	/**
	 * 
	 */
	public AbstractSOAProjetParamModel() {
		super();
	}
	
	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public List<String> getEclipseNatures() {
		return eclipseNatures;
	}
	
	public boolean addEclipseNature(String eclipseNature) {
		return eclipseNatures.add(eclipseNature);
	}
	
	public boolean addProjectLinkedResource(UIModelProjectLinkedResource linkedResource) {
		return linkedResources.add(linkedResource);
	}

	public List<UIModelProjectLinkedResource> getLinkedResources() {
		return linkedResources;
	}

	/* (non-Javadoc)
	 * @see org.ebayopensource.turmeric.eclipse.resources.ui.model.ISOAServiceParamModel#validate()
	 */
	public boolean validate() {
		return true;
	}
	
	@Override
	public String toString() {
		final StringBuffer buf = new StringBuffer();
		buf.append("Project Name: ");
		buf.append(this.projectName);
		return buf.toString();
	}
	
	public static class UIModelProjectLinkedResource {
		public static final int LINK_TYPE_RESOURCE = 1;
		public static final int LINK_TYPE_FOLDER = 2;
		
		private String name;
		private int type = LINK_TYPE_RESOURCE;
		private String location;
		
		public UIModelProjectLinkedResource(String name, int type, String location) {
			super();
			this.name = name;
			this.type = type;
			this.location = location;
		}
		
		public UIModelProjectLinkedResource(String name, String location) {
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
