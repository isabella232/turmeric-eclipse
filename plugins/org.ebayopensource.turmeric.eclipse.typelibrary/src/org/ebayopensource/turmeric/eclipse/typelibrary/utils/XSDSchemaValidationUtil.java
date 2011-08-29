package org.ebayopensource.turmeric.eclipse.typelibrary.utils;

import javax.wsdl.WSDLException;

import org.apache.commons.lang.StringUtils;
import org.ebayopensource.turmeric.common.config.LibraryType;
import org.ebayopensource.turmeric.eclipse.buildsystem.utils.BuilderUtil;
import org.ebayopensource.turmeric.eclipse.codegen.utils.CodegenInvoker;
import org.ebayopensource.turmeric.eclipse.core.logging.SOALogger;
import org.ebayopensource.turmeric.eclipse.core.resources.constants.SOAProjectConstants;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.GlobalRepositorySystem;
import org.ebayopensource.turmeric.eclipse.repositorysystem.model.BaseCodeGenModel;
import org.ebayopensource.turmeric.eclipse.resources.model.ISOAProject;
import org.ebayopensource.turmeric.eclipse.resources.model.SOAIntfProject;
import org.ebayopensource.turmeric.eclipse.typelibrary.codegen.model.GenTypeValidateXSDsForNonXSDFormats;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.NullProgressMonitor;

/**
 * 
 * @author mzang
 * 
 */
public class XSDSchemaValidationUtil {

	private static SOALogger logger = SOALogger.getLogger();

	/**
	 * check if given types are validated.
	 * @param project
	 * @param types
	 * @throws Exception
	 */
	public static void validateType(IProject project, LibraryType... types)
			throws Exception {
		logger.debug("Validating non schema support...");
		ISOAProject soaProj = GlobalRepositorySystem.instanceOf()
				.getActiveRepositorySystem().getAssetRegistry()
				.getSOAProject(project);
		if (soaProj instanceof SOAIntfProject == false) {
			return;
		}
		SOAIntfProject intfProject = (SOAIntfProject) soaProj;
		String nonXSDProtocols = intfProject.getMetadata()
				.getServiceNonXSDProtocols();
		if (StringUtils.isBlank(nonXSDProtocols)) {
			return;
		}
		if (nonXSDProtocols.contains(SOAProjectConstants.SVC_PROTOCOL_BUF) == false) {
			return;
		}

		if (types == null) {
			return;
		}

		StringBuilder paths = new StringBuilder();

		logger.debug("Checking the following XSD files for non schema support:");
		for (LibraryType type : types) {
			String path = TypeLibraryUtil.getXSD(type).toString();
			logger.debug(path);
			paths.append(path);
			paths.append(",");
		}

		if (paths.length() > 0) {
			paths.deleteCharAt(paths.length() - 1);
		} else {
			return;
		}
		logger.debug("Calling codegen for validation...");
		CodegenInvoker codegenInvoker = CodegenInvoker.init(project);
		BaseCodeGenModel baseCodeGenModel = BuilderUtil.buildBaseCodeGenModel(
				project, new NullProgressMonitor());
		GenTypeValidateXSDsForNonXSDFormats codeGenModel = transformToGenTypeValidateXSDsForNonXSDFormats(
				baseCodeGenModel, project);
		codeGenModel.setXsdsPath(paths.toString());
		codegenInvoker.execute(codeGenModel);
		logger.debug("Non schema support validation success");
	}

	/**
	 * Transforms the base model and creates a specialized model to invoke the
	 * codegen gen type - "ServiceFromWSDLIntf". Uses the available values from
	 * the base model and the rest of the values are populated after parsing the
	 * project data and the base model data. Meaning the rest of the data is
	 * inferred from the both of them.
	 * 
	 * @param model
	 *            - The base model has the general information from a project
	 *            already parsed and fed into
	 * @param project
	 *            - the project will be again used to get the rest of the
	 *            codegen parameter values.
	 * @param model
	 * @return
	 * @throws WSDLException
	 */
	
	public static GenTypeValidateXSDsForNonXSDFormats transformToGenTypeValidateXSDsForNonXSDFormats(
			BaseCodeGenModel model, IProject project) throws WSDLException {
		GenTypeValidateXSDsForNonXSDFormats genTypeValidateXSDsForNonXSDFormats = new GenTypeValidateXSDsForNonXSDFormats();
		genTypeValidateXSDsForNonXSDFormats.setAdminName(model.getAdminName());
		genTypeValidateXSDsForNonXSDFormats.setNamespace(model.getNamespace());
		genTypeValidateXSDsForNonXSDFormats.setServiceInterface(model
				.getServiceInterface());
		String genFolder = GlobalRepositorySystem.instanceOf()
				.getActiveRepositorySystem().getSOACodegenProvider()
				.getGenFolderForIntf();
		genTypeValidateXSDsForNonXSDFormats.setGenFolder(project
				.getFolder(genFolder).getLocation().toString());
		genTypeValidateXSDsForNonXSDFormats.setProjectRoot(model
				.getProjectRoot());
		genTypeValidateXSDsForNonXSDFormats.setServiceName(model
				.getServiceName());
		genTypeValidateXSDsForNonXSDFormats.setServiceVersion(model
				.getServiceVersion());
		genTypeValidateXSDsForNonXSDFormats.setServiceLayer(model
				.getServiceLayer());
		genTypeValidateXSDsForNonXSDFormats.setSourceDirectory(model
				.getSourceDirectory());
		genTypeValidateXSDsForNonXSDFormats.setDestination(model
				.getDestination());
		genTypeValidateXSDsForNonXSDFormats.setOutputDirectory(model
				.getOutputDirectory());
		genTypeValidateXSDsForNonXSDFormats.setServiceImplClassName(model
				.getServiceImplClassName());
		genTypeValidateXSDsForNonXSDFormats.setNonXSDFormats(model
				.getNonXSDFormats());

		return genTypeValidateXSDsForNonXSDFormats;
	}

}
