package org.ebayopensource.turmeric.eclipse.resources.model;


import java.util.Properties;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;

public class ProjectModel {
	private IPath location;
	private String projectName;
	private String groupId;
	private String artifactId;
	private String version;
	private String javaPackage;
	private Properties properties;
	private boolean isUseDefaultLocation=false;
	private RaptorArchetype archetype;

	public RaptorArchetype getArchetype() {
		return archetype;
	}

	public void setArchetype(RaptorArchetype archetype) {
		this.archetype = archetype;
	}

	public boolean isUseDefaultLocation() {
		return isUseDefaultLocation;
	}

	public void setUseDefaultLocation(boolean isUseDefaultLocation) {
		this.isUseDefaultLocation = isUseDefaultLocation;
	}

	public IPath getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = Path.fromOSString(location);
	}

	public String getProjectName() {
		return projectName;
	}

	public IProject getProject(){
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		IWorkspaceRoot root = workspace.getRoot();
		return root.getProject(this.projectName);
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	public String getGroupId() {
		return groupId;
	}
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	public String getArtifactId() {
		return artifactId;
	}
	public void setArtifactId(String artifactId) {
		this.artifactId = artifactId;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getJavaPackage() {
		return javaPackage;
	}
	public void setJavaPackage(String javaPackage) {
		this.javaPackage = javaPackage;
	}
	public Properties getProperties() {
		return properties;
	}
	public void setProperties(Properties properties) {
		//it's only UI inputs
		this.properties = properties;
	}
}