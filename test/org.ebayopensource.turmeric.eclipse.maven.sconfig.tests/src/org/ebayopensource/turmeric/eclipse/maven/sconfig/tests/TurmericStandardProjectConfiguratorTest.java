package org.ebayopensource.turmeric.eclipse.maven.sconfig.tests;

import java.io.IOException;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.m2e.core.project.ResolverConfiguration;
import org.junit.Test;

import static org.junit.Assert.*;

public class TurmericStandardProjectConfiguratorTest extends
		AbstractMavenProjectTestCase {

	private IProject buildProject(String testProjectPOMPath)
			throws IOException, CoreException, InterruptedException {
		ResolverConfiguration configuration = new ResolverConfiguration();
		IProject project = importProject(testProjectPOMPath, configuration);
		waitForJobsToComplete();

		project.build(IncrementalProjectBuilder.FULL_BUILD, monitor);
		project.build(IncrementalProjectBuilder.INCREMENTAL_BUILD, monitor);
		waitForJobsToComplete();
		return project;
	}

	private void assertSourcePathFound(IClasspathEntry[] cp, Path sourcePath) {
		boolean fndsw = false;
		for (IClasspathEntry entry : cp) {
			if (entry.getPath().equals(sourcePath)) {
				fndsw = true;
				break;
			}
		}
		assertTrue("Missing " + sourcePath.toString() + "source entry", fndsw);
	}

	private IClasspathEntry[] getClasspath(IProject project)
			throws JavaModelException {
		IJavaProject javaProject1 = JavaCore.create(project);
		IClasspathEntry[] cp = javaProject1.getRawClasspath();
		return cp;
	}
	
	private void assertGeneratedFilesExist(IProject project, String file) {
		assertTrue("File is not synchronized: " + file, project.getFile(file).isSynchronized(IResource.DEPTH_ZERO));
		assertTrue("File is not accessible:" + file, project.getFile(file).isAccessible());
	}
	
	@Test
	public void testJAXBEpisodeCodegenToolsProject() throws Exception {
		String testProjectPOMPath = "projects/codegen-tools/pom.xml";
		IProject project = buildProject(testProjectPOMPath);

		IClasspathEntry[] cp = getClasspath(project);

		Path sourcePath = new Path(
				"/codegen-tools/target/generated-sources/jaxb-episode");
		assertSourcePathFound(cp, sourcePath);

		sourcePath = new Path(
				"/codegen-tools/target/generated-resources/jaxb-episode");
		assertSourcePathFound(cp, sourcePath);

		String file = "target/generated-sources/jaxb-episode/org/ebayopensource/turmeric/runtime/codegen/common/CodeGenOptionType.java";
		assertTrue(project.getFile(file).isSynchronized(IResource.DEPTH_ZERO));
		assertTrue(project.getFile(file).isAccessible());
	}

	@Test
	public void testTypeLibraryProject() throws Exception {
		String testProjectPOMPath = "projects/common-type-library/pom.xml";
		IProject project = buildProject(testProjectPOMPath);
		IClasspathEntry[] cp = getClasspath(project);

		Path sourcePath = new Path(
				"/common-type-library/target/generated-sources/codegen");
		assertSourcePathFound(cp, sourcePath);

		sourcePath = new Path(
				"/common-type-library/target/generated-resources/codegen");
		assertSourcePathFound(cp, sourcePath);

		String file = "target/generated-sources/codegen/org/ebayopensource/turmeric/common/v1/types/AckValue.java";
		assertTrue("File is not synchronized:  " + file, project.getFile(file)
				.isSynchronized(IResource.DEPTH_ZERO));
		assertTrue("File is not accessible: " + file, project.getFile(file).isAccessible());
	}
	
	@Test
	public void testErrorLibraryProject() throws Exception {
		String testProjectPOMPath = "projects/runtime-error-library/pom.xml";
		IProject project = buildProject(testProjectPOMPath);
		IClasspathEntry[] cp = getClasspath(project);
		
		Path sourcePath = new Path("/runtime-error-library/target/generated-sources/codegen");
		assertSourcePathFound(cp, sourcePath);
		
		sourcePath = new Path("/runtime-error-library/target/generated-resources/codegen");
		assertSourcePathFound(cp, sourcePath);
		
		String file = "target/generated-sources/codegen/org/ebayopensource/turmeric/runtime/errorlibrary/ErrorConstants.java";
		assertGeneratedFilesExist(project, file);
	}
	
	@Test
	public void testInterfaceProject() throws Exception {
		String testProjectPOMPath = "projects/AuthenticationService/pom.xml";
		IProject project = buildProject(testProjectPOMPath);
		IClasspathEntry[] cp = getClasspath(project);
		
		Path sourcePath = new Path("/AuthenticationService/target/generated-sources/codegen");
		assertSourcePathFound(cp, sourcePath);
		
		sourcePath = new Path("/AuthenticationService/target/generated-resources/codegen");
		assertSourcePathFound(cp, sourcePath);
		
		String file = "target/generated-sources/codegen/org/ebayopensource/turmeric/services/authenticationservice/intf/gen/AuthenticationServiceProxy.java";
		assertGeneratedFilesExist(project, file);
		
		file = "target/generated-resources/codegen/META-INF/soa/services/wsdl/AuthenticationService_mns.wsdl";
		assertGeneratedFilesExist(project, file);
	}
	
	@Test
	public void testImplementationProject() throws Exception {
		String testProjectPOMPath = "projects/AuthenticationServiceImpl/pom.xml";
		IProject project = buildProject(testProjectPOMPath);
		IClasspathEntry[] cp = getClasspath(project);
		
		Path sourcePath = new Path("/AuthenticationServiceImpl/target/generated-sources/codegen");
		assertSourcePathFound(cp, sourcePath);
		
		sourcePath = new Path("/AuthenticationServiceImpl/target/generated-resources/codegen");
		assertSourcePathFound(cp, sourcePath);
		
		String file = "target/generated-sources/codegen/org/ebayopensource/turmeric/services/authenticationservice/impl/gen/AuthenticationServiceRequestDispatcher.java";
		assertGeneratedFilesExist(project, file);
	}
}