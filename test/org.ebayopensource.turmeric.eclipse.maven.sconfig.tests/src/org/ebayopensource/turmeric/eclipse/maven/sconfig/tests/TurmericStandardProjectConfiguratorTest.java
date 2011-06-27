package org.ebayopensource.turmeric.eclipse.maven.sconfig.tests;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.internal.core.ClasspathEntry;
import org.eclipse.m2e.core.project.ResolverConfiguration;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.*;

public class TurmericStandardProjectConfiguratorTest extends
		AbstractMavenProjectTestCase {
	@Test
	public void testJAXBEpisodeCodegenToolsProject() throws Exception {
		ResolverConfiguration configuration = new ResolverConfiguration();
		IProject project1 = importProject("projects/codegen-tools/pom.xml",
				configuration);
		waitForJobsToComplete();

		project1.build(IncrementalProjectBuilder.FULL_BUILD, monitor);
		project1.build(IncrementalProjectBuilder.INCREMENTAL_BUILD, monitor);
		waitForJobsToComplete();

//		assertNoErrors(project1);

		IJavaProject javaProject1 = JavaCore.create(project1);
		IClasspathEntry[] cp1 = javaProject1.getRawClasspath();
		
		boolean fndsw = false;
		Path sourcePath = new Path("/codegen-tools/target/generated-sources/jaxb-episode");
		for (IClasspathEntry entry : cp1) {
			
			if (entry.getPath().equals(sourcePath)) {
				fndsw = true;
				break;
			}
		}
		
		assertTrue("Missing generated-sources/jaxb-episode source entry", fndsw);

		fndsw = false;
		sourcePath = new Path("/codegen-tools/target/generated-resources/jaxb-episode");
		for (IClasspathEntry entry : cp1) {
			
			if (entry.getPath().equals(sourcePath)) {
				fndsw = true;
				break;
			}
		}

		assertTrue("Missing generated-resources/jaxb-episode source entry", fndsw);

		
//		assertEquals(new Path(
//				"/codegen-tools/target/generated-sources/jaxb-episode"),
//				cp1[5].getPath());

		assertTrue( project1.getFile(
		  "target/generated-sources/jaxb-episode/org/ebayopensource/turmeric/runtime/codegen/common/CodeGenOptionType.java"
		 ).isSynchronized( IResource.DEPTH_ZERO ) );
		assertTrue( project1.getFile(
		  "target/generated-sources/jaxb-episode/org/ebayopensource/turmeric/runtime/codegen/common/CodeGenOptionType.java"
		 ).isAccessible() );
	}

	@Test
	@Ignore
	public void test_p001_simple_v3() throws Exception {
		ResolverConfiguration configuration = new ResolverConfiguration();
		IProject project1 = importProject(
				"projects/antlr/antlr-v3-p001/pom.xml", configuration);
		waitForJobsToComplete();

		project1.build(IncrementalProjectBuilder.FULL_BUILD, monitor);
		project1.build(IncrementalProjectBuilder.INCREMENTAL_BUILD, monitor);
		waitForJobsToComplete();

		assertNoErrors(project1);

		IJavaProject javaProject1 = JavaCore.create(project1);
		IClasspathEntry[] cp1 = javaProject1.getRawClasspath();

		assertEquals(new Path("/antlr-v3-p001/target/generated-sources/antlr"),
				cp1[3].getPath());

		assertTrue(project1.getFile(
				"target/generated-sources/antlr/test/SampleParser.java")
				.isSynchronized(IResource.DEPTH_ZERO));
		assertTrue(project1.getFile(
				"target/generated-sources/antlr/test/SampleParser.java")
				.isAccessible());
	}

}