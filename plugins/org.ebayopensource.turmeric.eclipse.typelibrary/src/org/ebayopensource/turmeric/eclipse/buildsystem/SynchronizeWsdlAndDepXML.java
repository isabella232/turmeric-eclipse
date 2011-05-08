package org.ebayopensource.turmeric.eclipse.buildsystem;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.namespace.QName;

import org.ebayopensource.turmeric.common.config.LibraryType;
import org.ebayopensource.turmeric.common.config.ReferredType;
import org.ebayopensource.turmeric.common.config.TypeDependencyType;
import org.ebayopensource.turmeric.common.config.TypeLibraryDependencyType;
import org.ebayopensource.turmeric.eclipse.buildsystem.utils.BuildSystemUtil;
import org.ebayopensource.turmeric.eclipse.core.TurmericCoreActivator;
import org.ebayopensource.turmeric.eclipse.core.logging.SOALogger;
import org.ebayopensource.turmeric.eclipse.core.resources.constants.SOAProjectConstants;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.GlobalRepositorySystem;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.ISOAProjectConfigurer;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.SOAGlobalRegistryAdapter;
import org.ebayopensource.turmeric.eclipse.resources.model.AssetInfo;
import org.ebayopensource.turmeric.eclipse.resources.util.SOAServiceUtil;
import org.ebayopensource.turmeric.eclipse.typelibrary.TypeLibraryActivator;
import org.ebayopensource.turmeric.eclipse.typelibrary.utils.TypeLibraryUtil;
import org.ebayopensource.turmeric.eclipse.utils.collections.CollectionUtil;
import org.ebayopensource.turmeric.eclipse.utils.plugin.ProgressUtil;
import org.ebayopensource.turmeric.eclipse.utils.plugin.WorkspaceUtil;
import org.ebayopensource.turmeric.tools.library.SOATypeRegistry;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.wst.wsdl.Definition;
import org.eclipse.wst.wsdl.internal.util.WSDLResourceFactoryImpl;
import org.eclipse.wst.wsdl.util.WSDLResourceImpl;
import org.eclipse.xsd.XSDSchema;
import org.eclipse.xsd.XSDSchemaDirective;
import org.eclipse.xsd.XSDTypeDefinition;

/**
 * The Class SynchronizeWsdlAndDepXML.
 */
public class SynchronizeWsdlAndDepXML {

	private Definition definition = null;
	private IProject project = null;

	/**
	 * Instantiates a new synchronize wsdl and dep xml.
	 *
	 * @param project the project
	 */
	public SynchronizeWsdlAndDepXML(IProject project) {
		this.project = project;
	}

	/**
	 * Wrapper method, Internally uses.
	 *
	 * @throws CoreException the core exception
	 * @throws Exception the exception
	 * @see {@link TypeLibSynhcronizer#syncronizeWSDLandDepXml(Definition, IProject)}
	 */
	public void syncronizeWsdlandDepXml() throws CoreException, Exception {
		final IFile wsdlFile = SOAServiceUtil.getWsdlFile(project.getName());

		Job job = new Job("Synchronize WSDL and Dependent XML") {

			@Override
			protected IStatus run(IProgressMonitor monitor) {
				try {
					monitor.beginTask("Synchronize", IProgressMonitor.UNKNOWN);
					ResourceSet resourceSet = new ResourceSetImpl();
					Resource.Factory.Registry registry = resourceSet
							.getResourceFactoryRegistry();
					Map<String, Object> extensionToFactoryMap = registry
							.getExtensionToFactoryMap();
					extensionToFactoryMap.put(SOAProjectConstants.WSDL,
							new WSDLResourceFactoryImpl());
					URI uri = URI.createFileURI(wsdlFile.getLocation()
							.toString());

					WSDLResourceImpl wsdlResource = (WSDLResourceImpl) resourceSet
							.createResource(uri);
					wsdlResource.load(null);
					definition = wsdlResource.getDefinition();
					syncronizeWSDLandDepXml(definition);
				} catch (Exception e) {
					SOALogger.getLogger().error(e);
					monitor.done();
					return Status.CANCEL_STATUS;
				}
				monitor.done();
				return Status.OK_STATUS;
			}
		};
		
		job.setUser(false);
		job.schedule();
	}

	/**
	 * Synchronize the xsd and the dependencies. Meaning, Scans the xsd, finds
	 * the types that this type refers to and adds it to the dependencies if not
	 * present. Also it removes the unwanted dependencies from the type
	 * dependencies. Unwanted includes unwanted parent type references and types
	 * referred from the parent type.
	 *
	 * @param schema the schema
	 * @param type the type
	 * @throws Exception the exception
	 */
	public void syncronizeXSDandDepXml(XSDSchema schema, QName type) throws Exception {
		IFile typeDepFile = TurmericCoreActivator.getDependencyFile(project);
		if (!typeDepFile.exists()) {
			TypeDepMarshaller.createDefaultDepXml(project,
					new NullProgressMonitor());
			WorkspaceUtil.refresh(typeDepFile);
		}
		Map<LibraryType, XSDSchemaDirective> importedTypes = TypeLibraryActivator.getAllTypeLibImports(schema);

		TypeLibraryDependencyType typeLibraryDependencyType = TypeDepMarshaller
				.unmarshallIt(typeDepFile);
		// No imports for this type
		// so remove the entry if there is one
		if (importedTypes == null || importedTypes.size() == 0) {
			if (TypeDepMarshaller.removeTypeEntryIfExists(
					typeLibraryDependencyType, type.getLocalPart())) {
				TypeDepMarshaller.marshallIt(typeLibraryDependencyType,
						typeDepFile);
				return;
			}
		} else {

			Set<QName> xsdImportedTypes = new HashSet<QName>();
			for (LibraryType libraryType : importedTypes.keySet()) {
				if (libraryType != null)
					xsdImportedTypes.add(TypeLibraryUtil.toQName(libraryType));
			}
			boolean marshallReqd = false;

			SOAGlobalRegistryAdapter registryAdapter = SOAGlobalRegistryAdapter
					.getInstance();
			SOATypeRegistry typeRegistry = registryAdapter.getGlobalRegistry();
			LibraryType libraryType = typeRegistry.getType(type);
			// add Top level Type Entry if required
			marshallReqd = TypeDepMarshaller.addTypeEntryIfNotExists(
					typeLibraryDependencyType, libraryType);
			// check for version change for top level Type Entry if required
			marshallReqd = marshallReqd
					| TypeDepMarshaller.modifyTypeEntry(
							typeLibraryDependencyType, libraryType);
			TypeDependencyType typeDependencyType = TypeDepMarshaller
					.getTypeEntry(typeLibraryDependencyType,
							type.getLocalPart());
			Set<QName> depXmlReferredTypes = TypeDepMarshaller
					.getAllReferredtypes(typeDependencyType);

			if (!CollectionUtil.twoWayDiff(xsdImportedTypes,
					depXmlReferredTypes).isEmpty()) {
				// this means there is a diff.
				// so there should be an IO.
				// To be safe lets remove everything from the dep xml and add
				// the reqd ones.
				// we believe CPUS are much fsater than hard disk heads :))
				marshallReqd = true;
				// remove evrything first
				Set<ReferredType> oldReferredTpes = TypeDepMarshaller
						.getALLReferredTypes(typeLibraryDependencyType);
				TypeDepMarshaller.removeAllReferredTypes(typeDependencyType);
				TypeDepMarshaller.addAllReferredTypes(typeDependencyType,
						importedTypes.keySet(), oldReferredTpes);
			}
			if (marshallReqd) {
				TypeDepMarshaller.marshallIt(typeLibraryDependencyType,
						typeDepFile);
			}
		}
	}

	/**
	 * Syncronize all xs dsand dep xml.
	 *
	 * @throws CoreException the core exception
	 * @throws Exception the exception
	 */
	public void syncronizeAllXSDsandDepXml() throws CoreException, Exception {
		syncronizeAllXSDsandDepXml(null);
	}

/**
 * Wrapper method, Internally uses.
 *
 * @param xsdFiles the xsd files
 * @throws CoreException the core exception
 * @throws Exception the exception
 * @see {@link TypeLibSynhcronizer#syncronizeXSDandDepXml(XSDSchema, IProject, QName)
 */
	public void syncronizeAllXSDsandDepXml(List<IFile> xsdFiles)
			throws CoreException, Exception {
		// there could be some deleted XSDs also
		IFile typeDepFile = TurmericCoreActivator.getDependencyFile(project);
		List<IFile> allXSDFiles = xsdFiles != null ? xsdFiles : TypeLibraryUtil
				.getAllXsdFiles(project, true);
		List<String> allXSDStr = new ArrayList<String>();
		if (!typeDepFile.exists()) {
			TypeDepMarshaller.createDefaultDepXml(project,
					new NullProgressMonitor());
			WorkspaceUtil.refresh(typeDepFile);
		}

		for (IFile file : allXSDFiles) {
			if (file.exists()) {
				allXSDStr.add(TypeLibraryUtil.getXsdTypeNameFromFileName(file
						.getName()));
			}
		}
		boolean marshallIt = false;
		TypeLibraryDependencyType typeLibraryDependencyType = TypeDepMarshaller
				.unmarshallIt(typeDepFile);
		Iterator<TypeDependencyType> iterator = typeLibraryDependencyType
				.getType().iterator();
		while (iterator.hasNext()) {
			TypeDependencyType typeDependencyType = iterator.next();
			if (!allXSDStr.contains(typeDependencyType.getName())) {
				iterator.remove();
				marshallIt = true;
			}
		}
		if (marshallIt) {
			TypeDepMarshaller
					.marshallIt(typeLibraryDependencyType, typeDepFile);
		}

		for (IFile file : allXSDFiles) {
			if (file.isAccessible()) {
				XSDSchema xsdSchema = TypeLibraryUtil.parseSchema(file
						.getLocation().toFile().toURI().toURL());
				syncronizeXSDandDepXml(xsdSchema, TypeLibraryUtil.toQName(file));
			}
		}
	}

	/**
	 * Synchronize the wsdl and the dependencies. Meaning, Scans the wsdl, finds
	 * the types and adds it to the dependencies if not present. Also it removes
	 * the unwanted dependencies from the type dependencies.
	 *
	 * @param definition the definition
	 * @throws Exception the exception
	 */
	public void syncronizeWSDLandDepXml(Definition definition) throws Exception {
		Map<LibraryType, XSDTypeDefinition> wsdlImportedSchemas = TypeLibraryActivator.getTypeLibraryTypes(definition);

		IFile typeDepFile = TurmericCoreActivator.getDependencyFile(project);
		if (!typeDepFile.exists()) {
			TypeDepMarshaller.createDefaultDepXml(project,
					new NullProgressMonitor());
			WorkspaceUtil.refresh(typeDepFile);
		}
		TypeLibraryDependencyType typeLibraryDependencyType = TypeDepMarshaller
				.unmarshallIt(typeDepFile);
		String wsdlTypeName = GlobalRepositorySystem.instanceOf()
				.getActiveRepositorySystem().getTypeRegistryBridge()
				.getTypeDependencyWsdlTypeName();
		if (wsdlImportedSchemas == null || wsdlImportedSchemas.size() == 0) {
			if (TypeDepMarshaller.removeTypeEntryIfExists(
					typeLibraryDependencyType, wsdlTypeName)) {
				TypeDepMarshaller.marshallIt(typeLibraryDependencyType,
						typeDepFile);
				return;
			}
		} else {
			boolean marshallReqd = false;
			LibraryType libraryType = TypeLibraryUtil
					.createLibraryType(wsdlTypeName);
			marshallReqd = TypeDepMarshaller.addTypeEntryIfNotExists(
					typeLibraryDependencyType, libraryType);

			TypeDependencyType typeDependencyType = TypeDepMarshaller
					.getTypeEntry(typeLibraryDependencyType, wsdlTypeName);

			Set<QName> depXmlReferredTypes = TypeDepMarshaller
					.getAllReferredtypes(typeDependencyType);
			Set<QName> wsdlImportedTypes = new HashSet<QName>();
			for (LibraryType refLibraryType : wsdlImportedSchemas.keySet()) {
				wsdlImportedTypes.add(TypeLibraryUtil.toQName(refLibraryType));
			}

			if (!CollectionUtil.twoWayDiff(wsdlImportedTypes,
					depXmlReferredTypes).isEmpty()) {
				// this means there is a diff.
				// so there should be an IO.
				// To be safe lets remove everything from the dep xml and add
				// the reqd ones.
				// we believe CPUS are much fsater than hard disk heads :))
				marshallReqd = true;
				// remove evrything first

				Set<ReferredType> oldReferredTpes = TypeDepMarshaller
						.getALLReferredTypes(typeLibraryDependencyType);
				TypeDepMarshaller.removeAllReferredTypes(typeDependencyType);

				TypeDepMarshaller.addAllReferredTypes(typeDependencyType,
						wsdlImportedSchemas.keySet(), oldReferredTpes);

			}
			if (marshallReqd) {
				TypeDepMarshaller.marshallIt(typeLibraryDependencyType,
						typeDepFile);
				WorkspaceUtil.refresh(typeDepFile);
			}
		}

	}
	
	/**
	 * Compare the type dependency and project dependency and synchronizes the
	 * project dependencies to match the type dependency. One possible
	 * optimization is to avoid removing and adding existing dependencies. Need
	 * some enhancement from Bnr API also
	 *
	 * @param monitor the monitor
	 * @throws Exception the exception
	 */
	public void synchronizeTypeDepandProjectDep(IProgressMonitor monitor)
			throws Exception {
		monitor = ProgressUtil.getDefaultMonitor(monitor);
		IFile typeDepFile = TurmericCoreActivator.getDependencyFile(project);
		Set<String> allXmlDepLibs = null;
		ProgressUtil.progressOneStep(monitor, 10);
		if (typeDepFile.exists()) {
			TypeLibraryDependencyType typeLibraryDependencyType = TypeDepMarshaller
					.unmarshallIt(typeDepFile);
			allXmlDepLibs = TypeDepMarshaller
					.getAllReferredTypeLibraries(typeLibraryDependencyType);
			allXmlDepLibs.addAll(GlobalRepositorySystem.instanceOf()
					.getActiveRepositorySystem().getTypeRegistryBridge()
					.getRequiredLibrariesForTypeLibraryProject());
			// Add all other dependent libraries this type is referring to
			ProgressUtil.progressOneStep(monitor);
			SOAGlobalRegistryAdapter registryAdapter = SOAGlobalRegistryAdapter.getInstance();
			SOATypeRegistry typeRegistry = registryAdapter.getGlobalRegistry();
			for (QName qName : TypeDepMarshaller
					.getAllReferredTypes(typeLibraryDependencyType)) {
				for (String lib : typeRegistry.getLibrariesReferredByType(qName)) {
					allXmlDepLibs.add(lib);
				}
			}
			ProgressUtil.progressOneStep(monitor);
			// there could be the case that an xsd form this project is being
			// imported.
			// in that case no dep should go to project.xml. because both xsds
			// are in the same project
			if (allXmlDepLibs.contains(project.getName())) {
				allXmlDepLibs.remove(project.getName());
			}

			List<AssetInfo> dependencies = GlobalRepositorySystem.instanceOf()
					.getActiveRepositorySystem().getAssetRegistry()
					.getDependencies(project.getName());
			ProgressUtil.progressOneStep(monitor);
			Set<String> projTypeLibDeps = new HashSet<String>();
			for (AssetInfo assetInfo : dependencies) {
				// We should add/remove only type libraries, don't play around
				// with any other dependency
				if (typeRegistry.getTypeLibrary(assetInfo.getName()) != null
						|| allXmlDepLibs.contains(assetInfo.getName())) {
					//we also accept the required dependencies for TL which are 
					//not necessarily a type library.
						projTypeLibDeps.add(assetInfo.getName());
					
				} else if (allXmlDepLibs.contains(assetInfo.getShortDescription())) {
					projTypeLibDeps.add(assetInfo.getShortDescription());
				} else if (allXmlDepLibs.contains(assetInfo.getDescription())) {
					projTypeLibDeps.add(assetInfo.getDescription());
				} 
			}
			

			if (!CollectionUtil.twoWayDiff(projTypeLibDeps, allXmlDepLibs)
					.isEmpty()) {
				// But here we don't think CPU is better than hard disk :))..
				// Here there is a lot of change hitting the framework. so just
				// doing the necessary stuff
				Set<String> addedLibraries = CollectionUtil.oneWayDiff(
						allXmlDepLibs, projTypeLibDeps);
				Set<String> removedLibraries = CollectionUtil.oneWayDiff(
						projTypeLibDeps, allXmlDepLibs);
				ISOAProjectConfigurer projectConfigurer = GlobalRepositorySystem
						.instanceOf().getActiveRepositorySystem()
						.getProjectConfigurer();
				String projectName = project.getName();
				for (String addedLibrary : addedLibraries) {
					projectConfigurer.addTypeLibraryDependency(projectName,
							addedLibrary, AssetInfo.TYPE_LIBRARY, true,
							monitor);
				}
				for (String removedLibrary : removedLibraries) {
					projectConfigurer.addTypeLibraryDependency(projectName,
							removedLibrary, AssetInfo.TYPE_LIBRARY, false,
							monitor);
				}
				BuildSystemUtil.updateSOAClasspathContainer(project);
			}
		}
	}
	

}