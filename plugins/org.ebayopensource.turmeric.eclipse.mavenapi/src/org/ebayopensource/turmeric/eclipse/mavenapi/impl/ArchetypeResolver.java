package org.ebayopensource.turmeric.eclipse.mavenapi.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Logger;

import org.apache.commons.lang.StringUtils;
import org.apache.maven.archetype.ArchetypeGenerationRequest;
import org.apache.maven.archetype.ArchetypeGenerationResult;
import org.apache.maven.archetype.catalog.Archetype;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.execution.MavenSession;
import org.eclipse.core.internal.resources.ResourceException;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.m2e.core.MavenPlugin;
import org.eclipse.m2e.core.embedder.IMaven;
import org.eclipse.m2e.core.embedder.IMavenConfiguration;
import org.eclipse.m2e.core.embedder.MavenModelManager;
import org.eclipse.m2e.core.internal.IMavenConstants;
import org.eclipse.m2e.core.internal.MavenPluginActivator;
import org.eclipse.m2e.core.internal.archetype.ArchetypeCatalogFactory;
import org.eclipse.m2e.core.internal.archetype.ArchetypeCatalogFactory.LocalCatalogFactory;
import org.eclipse.m2e.core.internal.archetype.ArchetypeCatalogFactory.RemoteCatalogFactory;
import org.eclipse.m2e.core.internal.archetype.ArchetypeManager;
import org.eclipse.m2e.core.project.IProjectConfigurationManager;
import org.eclipse.m2e.core.project.LocalProjectScanner;
import org.eclipse.m2e.core.project.MavenProjectInfo;
import org.eclipse.m2e.core.project.ProjectImportConfiguration;
import org.osgi.framework.Version;

public class ArchetypeResolver {
	final static String DEFAULT_REMOTE_REPO_URL = "http://ebaycentral/content/repositories/releases/";
	final static String ARCHETYPE_CATALOG = "archetype-catalog.xml";
	Collection<Archetype> currentArchetypes;
	
	public void createArchetypeProject(IProject project, IPath location,
			 String groupId, String artifactId,
			String version,
			String javaPackage, Properties properties,
			ProjectImportConfiguration configuration, IProgressMonitor monitor)
			throws CoreException {

		try{
			String url = System.getProperty("user.home") + File.separator +".m2" + File.separator + "archetype-catalog.xml";
			ArchetypeCatalogFactory factory = //new RemoteCatalogFactory(DEFAULT_REMOTE_REPO_URL, "Raptor Remote Catalog", false);
					new LocalCatalogFactory(url,"Default Local",false);
			//get the right artifact and send it
			List<Archetype> archtypeList= factory.getArchetypeCatalog().getArchetypes();
			List<Archetype> toCompare = new ArrayList<Archetype>();
			Archetype archetype = null;
			String minimumVersion = getMinimumVersion(properties.getProperty("RaptorPlatformVersion"));
			for(Archetype arctype :archtypeList){
				if((arctype.getArtifactId().equalsIgnoreCase(artifactId))
						&&(arctype.getGroupId().equalsIgnoreCase(groupId))
						&&(arctype.getVersion().startsWith((minimumVersion))))
						toCompare.add(arctype);
								//archetype = arctype;
			}
			archetype = bringLatest(toCompare);
			if(archetype==null) throw new RuntimeException("Archetype cannot be resolved : "+ artifactId +" : " + groupId + " : "+ version +" at "+ DEFAULT_REMOTE_REPO_URL);
	      Set<MavenProjectInfo> projectSet = createMavenProjectsByArchetype(project, location, archetype, properties.getProperty("groupId"), properties.getProperty("artifactId"), properties.getProperty("version"), javaPackage, properties, configuration, monitor);

	      //remove parent project
	      removeParentProject(projectSet);
	      
	      IProjectConfigurationManager configurationManager = MavenPlugin.getProjectConfigurationManager();
	      ClassLoader oldClassLoader = Thread.currentThread().getContextClassLoader();
	     
	    try{
	      configurationManager.importProjects(projectSet, configuration, new NullProgressMonitor());
	    }finally{
	     
	       Thread.currentThread().setContextClassLoader(oldClassLoader);
	    }

	      monitor.worked(1);
	     
	   
	    } catch (ResourceException e){
	    	//ignore
	    } catch (CoreException e) {
	      throw e;
	    } catch (InterruptedException e) {
	      throw new CoreException(Status.CANCEL_STATUS);
	    }
	    catch (Exception ex) {
	      throw new CoreException(new Status(IStatus.ERROR, "org.maven.ide.eclipse", "Failed to create project.", ex));
	    }
	}
	private String getMinimumVersion(String version) {
		
		return "2.0";
	}
	private void removeParentProject(Set<MavenProjectInfo> projectSet) {
		MavenProjectInfo parent = getParentProject(projectSet);
		if (parent != null) {
			projectSet.remove(parent);
		}
	}
	private MavenProjectInfo getParentProject(Set<MavenProjectInfo> projectSet) {
		for (MavenProjectInfo project : projectSet) {
			if (project.getParent() != null) {
				return project.getParent();
			}
		}

		return null;
	}

	 private static Artifact resolveArchetype(org.apache.maven.archetype.catalog.Archetype archetype, IProgressMonitor monitor)
	            throws CoreException
	    {
	        IMaven maven = MavenPlugin.getMaven();

	        ArrayList<ArtifactRepository> repos = new ArrayList<ArtifactRepository>();
	        repos.addAll(maven.getArtifactRepositories());

	        String artifactRemoteRepository = archetype.getRepository();
	        try
	        {
	            if ( StringUtils.isBlank(artifactRemoteRepository) )
	            {
	                IMavenConfiguration mavenConfiguration = MavenPlugin.getMavenConfiguration();
	                if ( !mavenConfiguration.isOffline() )
	                {
	                    ArchetypeManager archetypeManager = MavenPluginActivator.getDefault().getArchetypeManager();
	                    RemoteCatalogFactory factory = archetypeManager.findParentCatalogFactory(archetype,
	                            RemoteCatalogFactory.class);
	                    if ( factory != null )
	                    {
	                        artifactRemoteRepository = factory.getRepositoryUrl();
	                        archetype.setRepository(artifactRemoteRepository);
	                    }
	                }
	            }

	            if ( StringUtils.isNotBlank(artifactRemoteRepository) )
	            {
	                ArtifactRepository archetypeRepository = maven.createArtifactRepository("archetype", archetype.getRepository()
	                        .trim());
	                repos.add(0, archetypeRepository);
	            }

	            maven.resolve(archetype.getGroupId(), archetype.getArtifactId(),archetype.getVersion(), "pom", null, repos, monitor);
	            
	            return maven.resolve(archetype.getGroupId(), archetype.getArtifactId(), archetype.getVersion(), "jar", null, repos, monitor);
	        }
	        catch (CoreException e)
	        {
	            StringBuilder sb = new StringBuilder();
	            sb.append("Can't resolve archetype ").append(archetype.getGroupId()).append(':').append(archetype.getArtifactId())
	                    .append(':').append(archetype.getVersion());
	            sb.append(" from any of the configured repositories.");
	            throw new CoreException(new Status(IStatus.ERROR, IMavenConstants.PLUGIN_ID, -1, sb.toString(), e));
	        }
	    }
	 public static Set<MavenProjectInfo> createMavenProjectsByArchetype(IProject project, IPath location,
	            org.apache.maven.archetype.catalog.Archetype archetype, String groupId, String artifactId, String version,
	            String javaPackage, Properties properties, ProjectImportConfiguration configuration,
	            IProgressMonitor monitor)
	            throws Exception
	    {
	        IProjectConfigurationManager configurationManager = MavenPlugin.getProjectConfigurationManager();

	        monitor.beginTask("Creating project " + project.getName(), 2);
	        IWorkspaceRoot workspaceRoot = ResourcesPlugin.getWorkspace().getRoot();

	        monitor.subTask("Executing archetype " + archetype.getGroupId() + ":" + archetype.getArtifactId());
	        if ( location == null )
	        {
	            location = workspaceRoot.getLocation();
	        }

	        IMaven maven =  MavenPlugin.getMaven();
	        Artifact artifact = resolveArchetype(archetype, monitor);

	        ArchetypeGenerationRequest request = new ArchetypeGenerationRequest()
	                .setTransferListener(maven.createTransferListener(monitor)).setArchetypeGroupId(artifact.getGroupId())
	                .setArchetypeArtifactId(artifact.getArtifactId()).setArchetypeVersion(artifact.getVersion())
	                .setArchetypeRepository(archetype.getRepository()).setGroupId(groupId).setArtifactId(artifactId)
	                .setVersion(version).setPackage(javaPackage).setLocalRepository(maven.getLocalRepository())
	                .setRemoteArtifactRepositories(maven.getArtifactRepositories(true)).setProperties(properties)
	                .setOutputDirectory(location.toPortableString());

	        MavenSession session = maven.createSession(maven.createExecutionRequest(monitor), null);
	        MavenSession oldSession = MavenPluginActivator.getDefault().setSession(session);
	        ArchetypeGenerationResult result;
	        
	        ClassLoader originclzLoader = Thread.currentThread().getContextClassLoader();
	    	Thread.currentThread().setContextClassLoader(IProjectConfigurationManager.class.getClassLoader());
	    	
	        try
	        {
	            result =  MavenPluginActivator.getDefault().getArchetype().generateProjectFromArchetype(request);
	        }
	        finally
	        {
	        	MavenPluginActivator.getDefault().setSession(oldSession);
	            Thread.currentThread().setContextClassLoader(originclzLoader);
	        }

	        Exception cause = result.getCause();
	        if ( cause != null )
	        {
	            String msg = "Failed to create project from archetype " + archetype.toString();
	            throw new CoreException(new Status(IStatus.ERROR, IMavenConstants.PLUGIN_ID, -1, msg, cause));
	        }
	        monitor.worked(1);

	        String projectFolder = location.append(artifactId).toFile().getAbsolutePath();
	        MavenModelManager modelManager = MavenPlugin.getMavenModelManager();
	        LocalProjectScanner scanner = new LocalProjectScanner(workspaceRoot.getLocation().toFile(), projectFolder,
	                false, modelManager);
	        scanner.run(monitor);

	        return configurationManager.collectProjects(scanner.getProjects());
	    }
	
public static void main(String[] args) throws CoreException {
	String url = System.getProperty("user.home") + File.separator +".m2" + File.separator + "archetype-catalog.xml";
	ArchetypeCatalogFactory factory = new RemoteCatalogFactory(DEFAULT_REMOTE_REPO_URL, "Raptor Remote Catalog", false);
			//new LocalCatalogFactory(url,"Default Local",false);
			//new RemoteCatalogFactory("http://nxraptor.qa.ebay.com/content/repositories/snapshots/", "Raptor Remote Catalog", false);
	//get the right artifact and send it
	List<Archetype> archtypeList= factory.getArchetypeCatalog().getArchetypes();
	Archetype archetype = null;
	List<Archetype> toCompare = new ArrayList<Archetype>();
	for(Archetype arctype :archtypeList){
		if((arctype.getArtifactId().equalsIgnoreCase("raptor-web-with-samples"))
				&&(arctype.getGroupId().equalsIgnoreCase("com.ebay.raptor.tools.mvn.archetypes"))
				&&(arctype.getVersion().startsWith(("1.2."))))
						toCompare.add(arctype);
	}
	//System.out.println(bringLatest(toCompare).getVersion());
}
private  Archetype bringLatest(List<Archetype> toCompare) {
	Archetype greatest = null;
	for (Archetype current:toCompare){
		if (greatest==null){
			greatest=current;
		}
		
		String greatestnumeric = greatest.getVersion();
		if(greatestnumeric.contains("-")){
			greatestnumeric =greatest.getVersion().substring(0,greatest.getVersion().indexOf("-"));
		}
		String currentNumeric = current.getVersion();
		if(currentNumeric.contains("-")){
			currentNumeric=current.getVersion().substring(0,current.getVersion().indexOf("-"));
		}
		if(compare(greatestnumeric,currentNumeric)<0){
					greatest=current;
				}
	}
	return greatest;
}
public  int compare(String strVersion1, String strVersion2)
		throws NumberFormatException {
	Version version1 = null;
	Version version2 = null;
	try {
		version1 = new Version(strVersion1);
		version2 = new Version(strVersion2);
		if (strVersion1.contains("-T40") == true)
			return -1;
		if (strVersion2.contains("-T40") == true)
			return 1;
	} catch (NumberFormatException exception) {
		
		return strVersion1.compareTo(strVersion2);
	} catch (IllegalArgumentException exception){
		
		return strVersion1.compareTo(strVersion2);
	}
	return version1.compareTo(version2);
}

}
