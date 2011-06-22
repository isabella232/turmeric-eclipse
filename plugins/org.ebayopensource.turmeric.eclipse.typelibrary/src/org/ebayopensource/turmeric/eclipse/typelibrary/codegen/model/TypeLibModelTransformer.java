/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.typelibrary.codegen.model;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.ebayopensource.turmeric.eclipse.typelibrary.builders.TypeLibraryBuilderUtils;
import org.ebayopensource.turmeric.eclipse.typelibrary.builders.TypeLibraryProjectNature;
import org.ebayopensource.turmeric.eclipse.utils.plugin.WorkspaceUtil;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.runtime.CoreException;


/**
 * This is the Model transformer Engine. Transforms a project or the base model
 * to the specific model used for calling codegen. This class is a no validation
 * class. Exception happening here are bubbled up to the problems view
 * 
 * 
 * @author smathew
 * 
 */
public class TypeLibModelTransformer {

	/**
	 * Extracts the information from this project and builds the model out from
	 * it. Sets the project root library name and xjc class path
	 *
	 * @param project the project
	 * @return the base type lib codegen model
	 * @throws Exception the exception
	 */
	public static BaseTypeLibCodegenModel buildBaseTypeLibModel(IProject project)
			throws Exception {
		BaseTypeLibCodegenModel genTypeBaseTypeLibCodegenModel = new BaseTypeLibCodegenModel();
		genTypeBaseTypeLibCodegenModel.setProjectRoot(project.getLocation()
				.toString());
		genTypeBaseTypeLibCodegenModel.setLibraryName(project.getName());
		genTypeBaseTypeLibCodegenModel.setXjcClassPath(getXJCClassPath());
		TypeLibraryParamModel model = SOATypeLibraryProjectResolver.loadTypeLibraryModel(project);
		if (model != null) {
			genTypeBaseTypeLibCodegenModel.setLibNamespace(model.getNamespace());
			genTypeBaseTypeLibCodegenModel.setLibraryCategory(model.getCategory());
			genTypeBaseTypeLibCodegenModel.setLibraryName(model.getTypeLibraryName());
			genTypeBaseTypeLibCodegenModel.setLibraryVersion(model.getVersion());
		}
		
		return genTypeBaseTypeLibCodegenModel;
	}

	/**
	 * Builds a specialized model from the base model and the given project. The
	 * delta is used to decide if the returned model is a clean build model or
	 * an incremental model. Also it is used to find the modified xsd.
	 *
	 * @param baseTypeLibCodegenModel the base type lib codegen model
	 * @param project the project
	 * @param delta the delta
	 * @return the base type lib codegen model
	 * @throws Exception the exception
	 */
	public static BaseTypeLibCodegenModel transformToGenTypeIncrOrCleanBuildTypeLibrary(
			BaseTypeLibCodegenModel baseTypeLibCodegenModel, IProject project,
			IResourceDelta delta) throws Exception {
		if (delta == null) {// this is a clean
			return transformToGenTypeCleanBuildTypeLibrary(
					baseTypeLibCodegenModel, project);
		} else {
			return transformToGenTypeIncrBuildTypeLibrary(
					baseTypeLibCodegenModel, project, TypeLibraryBuilderUtils
							.getModifiedXsds(delta, project));
		}
	}

	private static BaseTypeLibCodegenModel transformToGenTypeCleanBuildTypeLibrary(
			BaseTypeLibCodegenModel baseTypeLibCodegenModel, IProject project)
			throws Exception {
		GenTypeCleanBuildTypeLibrary genTypeCleanBuildTypeLibrary = new GenTypeCleanBuildTypeLibrary();
		genTypeCleanBuildTypeLibrary.setLibraryName(baseTypeLibCodegenModel
				.getLibraryName());
		genTypeCleanBuildTypeLibrary.setProjectRoot(baseTypeLibCodegenModel
				.getProjectRoot());
		genTypeCleanBuildTypeLibrary.setXjcClassPath(baseTypeLibCodegenModel
				.getXjcClassPath());
		genTypeCleanBuildTypeLibrary.setLibraryName(baseTypeLibCodegenModel.getLibraryName());
		genTypeCleanBuildTypeLibrary.setLibraryCategory(baseTypeLibCodegenModel.getLibraryCategory());
		genTypeCleanBuildTypeLibrary.setLibraryVersion(baseTypeLibCodegenModel.getLibraryVersion());
		genTypeCleanBuildTypeLibrary.setLibNamespace(baseTypeLibCodegenModel.getLibNamespace());
		return genTypeCleanBuildTypeLibrary;
	}

	private static BaseTypeLibCodegenModel transformToGenTypeIncrBuildTypeLibrary(
			BaseTypeLibCodegenModel baseTypeLibCodegenModel, IProject project,
			List<IFile> types) throws Exception {
		GenTypeIncrBuildTypeLibrary genTypeIncrBuildTypeLibrary = new GenTypeIncrBuildTypeLibrary();
		genTypeIncrBuildTypeLibrary.setLibraryName(baseTypeLibCodegenModel
				.getLibraryName());
		genTypeIncrBuildTypeLibrary.setProjectRoot(baseTypeLibCodegenModel
				.getProjectRoot());
		genTypeIncrBuildTypeLibrary.setXjcClassPath(baseTypeLibCodegenModel
				.getXjcClassPath());
		ArrayList<String> strTypesList = new ArrayList<String>();
		for (IFile file : types) {
			strTypesList.add(file.getName());
		}
		genTypeIncrBuildTypeLibrary.setTypes(strTypesList);
		return genTypeIncrBuildTypeLibrary;
	}

	private static String getXJCClassPath() throws CoreException, IOException {
		StringBuffer xjcClassPath = new StringBuffer("");
		for (IProject typeLibProject : WorkspaceUtil
				.getProjectsByNature(TypeLibraryProjectNature.getTypeLibraryNatureId())) {
			xjcClassPath.append(typeLibProject.getLocation().toFile()
					.getCanonicalPath());
			xjcClassPath.append(File.pathSeparatorChar);
		}
		return StringUtils.substringBeforeLast(xjcClassPath.toString(),
				File.pathSeparator);

	}
}
