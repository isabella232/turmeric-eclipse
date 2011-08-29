package org.ebayopensource.turmeric.eclipse.typelibrary.ui;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.ebayopensource.turmeric.common.config.LibraryType;
import org.ebayopensource.turmeric.common.config.TypeLibraryType;
import org.ebayopensource.turmeric.eclipse.buildsystem.SynchronizeWsdlAndDepXML;
import org.ebayopensource.turmeric.eclipse.core.logging.SOALogger;
import org.ebayopensource.turmeric.eclipse.core.model.TemplateModel;
import org.ebayopensource.turmeric.eclipse.core.resources.constants.SOATypeLibraryConstants;
import org.ebayopensource.turmeric.eclipse.exception.core.CommandFailedException;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.SOAGlobalRegistryAdapter;
import org.ebayopensource.turmeric.eclipse.typelibrary.ui.resources.TypeLibMoveDeleteHook;
import org.ebayopensource.turmeric.eclipse.typelibrary.ui.wst.AddImportCommand;
import org.ebayopensource.turmeric.eclipse.typelibrary.utils.TypeLibraryUtil;
import org.ebayopensource.turmeric.eclipse.ui.model.typelib.ComplexTypeParamModel;
import org.ebayopensource.turmeric.eclipse.ui.model.typelib.ComplexTypeSCParamModel;
import org.ebayopensource.turmeric.eclipse.utils.plugin.JDTUtil;
import org.ebayopensource.turmeric.eclipse.utils.plugin.ProgressUtil;
import org.ebayopensource.turmeric.eclipse.utils.plugin.WorkspaceUtil;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.eclipse.wst.sse.core.internal.format.IStructuredFormatProcessor;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMElement;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMModel;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMNode;
import org.eclipse.wst.xml.core.internal.provisional.format.FormatProcessorXML;
import org.eclipse.wst.xsd.ui.internal.common.util.XSDCommonUIUtils;
import org.eclipse.xsd.XSDAnnotation;
import org.eclipse.xsd.XSDAttributeDeclaration;
import org.eclipse.xsd.XSDAttributeUse;
import org.eclipse.xsd.XSDComplexTypeContent;
import org.eclipse.xsd.XSDComplexTypeDefinition;
import org.eclipse.xsd.XSDConcreteComponent;
import org.eclipse.xsd.XSDElementDeclaration;
import org.eclipse.xsd.XSDFactory;
import org.eclipse.xsd.XSDModelGroup;
import org.eclipse.xsd.XSDModelGroupDefinition;
import org.eclipse.xsd.XSDParticle;
import org.eclipse.xsd.XSDSchema;
import org.eclipse.xsd.XSDSimpleTypeDefinition;
import org.eclipse.xsd.XSDTypeDefinition;
import org.osgi.framework.BundleContext;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * The activator class controls the plug-in life cycle.
 */
public class TypeLibraryUIActivator extends AbstractUIPlugin {

	// The plug-in ID
	/** The Constant PLUGIN_ID. */
	public static final String PLUGIN_ID = "org.ebayopensource.turmeric.eclipse.typelibrary.ui"; //$NON-NLS-1$

	/** The Constant ICON_PATH. */
	public static final String ICON_PATH = "icons/";

	// The shared instance
	private static TypeLibraryUIActivator plugin;

	private TypeLibMoveDeleteHook typeLibMoveDeleteHook;

	/**
	 * The constructor.
	 */
	public TypeLibraryUIActivator() {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
		StringBuffer buf = new StringBuffer();
		buf.append("SOAPlugin.start - ");
		buf.append(JDTUtil.getBundleInfo(context.getBundle(), SOALogger.DEBUG));
		SOALogger.getLogger().info(buf);
		typeLibMoveDeleteHook = new TypeLibMoveDeleteHook();
		ResourcesPlugin.getWorkspace().addResourceChangeListener(
				typeLibMoveDeleteHook, IResourceChangeEvent.POST_CHANGE);

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void stop(BundleContext context) throws Exception {
		if (typeLibMoveDeleteHook != null)
			ResourcesPlugin.getWorkspace().removeResourceChangeListener(
					typeLibMoveDeleteHook);
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance.
	 * 
	 * @return the shared instance
	 */
	public static TypeLibraryUIActivator getDefault() {
		return plugin;
	}

	/**
	 * Adds the element declaration.
	 * 
	 * @param complexTypeDefinition
	 *            the complex type definition
	 * @param model
	 *            the model
	 * @param elementName
	 *            the element name
	 * @param elementType
	 *            the element type
	 * @param minOccurs
	 *            the min occurs
	 * @param maxOccurs
	 *            the max occurs
	 * @throws CommandFailedException
	 *             the command failed exception
	 */
	public static void addElementDeclaration(
			XSDComplexTypeDefinition complexTypeDefinition,
			ComplexTypeParamModel model, String elementName,
			Object elementType, int minOccurs, int maxOccurs)
			throws CommandFailedException {
		String prefixedElementType = "";
		boolean isPrimitive = true;
		try {
			if (elementType instanceof String) {
				prefixedElementType = getPrefix(
						complexTypeDefinition.getSchema(),
						SOATypeLibraryConstants.W3C_NAMEPSACE) + elementType;
			} else {
				isPrimitive = false;
				LibraryType libElementType = (LibraryType) elementType;
				prefixedElementType = getPrefix(
						complexTypeDefinition.getSchema(),
						TypeLibraryUtil.getNameSpace(libElementType))
						+ libElementType.getName();
			}
		} catch (Exception e) {
			throw new CommandFailedException(
					"Could not get the name space for the type "
							+ elementType
							+ " from the registry. Please fix this issue and try again.");
		}
		XSDParticle xsdParticle = createXSDElementDeclaration(elementName,
				prefixedElementType, minOccurs, maxOccurs);
		XSDModelGroup xsdModelGroup = getModelGroup(complexTypeDefinition);
		xsdModelGroup.getContents().add(0, xsdParticle);
		if (!isPrimitive)
			addImport((LibraryType) elementType, complexTypeDefinition,
					model.getTypeName(), model.getVersion(),
					model.getTypeLibraryName());
	}

	/** The Constant NO_OCCURS. */
	public static final int NO_OCCURS = -2;

	/** The Constant UNBOUND. */
	public static final int UNBOUND = -1;

	/**
	 * Adds the attribute declarations.
	 * 
	 * @param complexTypeDefinition
	 *            the complex type definition
	 * @param model
	 *            the model
	 * @param attrName
	 *            the attr name
	 * @param attrType
	 *            the attr type
	 * @param attrDoc
	 *            the attr doc
	 * @throws CommandFailedException
	 *             the command failed exception
	 */
	public static void addAttributeDeclarations(
			XSDComplexTypeDefinition complexTypeDefinition,
			ComplexTypeSCParamModel model, String attrName, Object attrType,
			String attrDoc) throws CommandFailedException {
		String prefixedAttrType = "";
		boolean isPrimitive = true;
		try {
			if (attrType instanceof String) {// this is a primitive type
				prefixedAttrType = getPrefix(complexTypeDefinition.getSchema(),
						SOATypeLibraryConstants.W3C_NAMEPSACE) + attrType;
			} else {// this is a library Type
				isPrimitive = false;
				LibraryType libAttrType = (LibraryType) attrType;

				prefixedAttrType = getPrefix(complexTypeDefinition.getSchema(),
						TypeLibraryUtil.getNameSpace(libAttrType))
						+ libAttrType.getName();

			}
		} catch (Exception e) {
			throw new CommandFailedException(
					"Could not get the name space for the type "
							+ attrType
							+ " from the registry. Please fix this issue and try again.");
		}
		XSDAttributeUse attr = createXSDAttrDeclaration(attrName,
				prefixedAttrType);
		complexTypeDefinition.getAttributeContents().add(attr);
		if (!isPrimitive)
			addImport((LibraryType) attrType, complexTypeDefinition,
					model.getTypeName(), model.getVersion(),
					model.getTypeLibraryName());
		addDocumentation(attr.getAttributeDeclaration(), attrDoc);

	}

	/**
	 * Format contents.
	 * 
	 * @param contents
	 *            the contents
	 * @return the string
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 * @throws CoreException
	 *             the core exception
	 */
	public static String formatContents(String contents) throws IOException,
			CoreException {
		FormatProcessorXML formatProcessor = new FormatProcessorXML();
		return formatProcessor.formatContent(contents);
	}

	private static void addImport(LibraryType importType,
			XSDTypeDefinition typeDefinition, String typeName, String version,
			String typeLibraryName) throws CommandFailedException {
		try {
			XSDSchema importSchema = TypeLibraryUtil
					.parseSchema(TypeLibraryUtil
							.getXSD(SOAGlobalRegistryAdapter
									.getInstance()
									.getGlobalRegistry()
									.getType(
											TypeLibraryUtil.toQName(importType))));
			AddImportCommand addImportCommand = new AddImportCommand(
					typeDefinition.getSchema(),
					TypeLibraryUtil.getProtocolString(importType), importSchema);
			addImportCommand.run();

			// for add type to wsdl, Type Library is null
			if (typeLibraryName == null) {
				return;
			}

			TypeLibraryType typeLibInfo = SOAGlobalRegistryAdapter
					.getInstance().getGlobalRegistry()
					.getTypeLibrary(typeLibraryName);
			LibraryType libraryType = TypeLibraryUtil.getLibraryType(typeName,
					version, typeLibInfo);

			SOAGlobalRegistryAdapter.getInstance().addTypeToRegistry(
					libraryType);
			IProject project = WorkspaceUtil.getProject(typeLibraryName);
			SynchronizeWsdlAndDepXML synch = new SynchronizeWsdlAndDepXML(
					project);
			synch.syncronizeXSDandDepXml(typeDefinition.getSchema(),
					TypeLibraryUtil.toQName(libraryType));
			synch.synchronizeTypeDepandProjectDep(ProgressUtil
					.getDefaultMonitor(null));
		} catch (Exception e) {
			throw new CommandFailedException(e);
		}
	}

	/**
	 * Gets the image from registry.
	 * 
	 * @param path
	 *            the path
	 * @return the image from registry
	 */
	public static ImageDescriptor getImageFromRegistry(String path) {
		if (path == null)
			return null;
		path = StringUtils.replaceChars(path, '\\', '/');
		final String iconPath = path.startsWith(ICON_PATH) ? path : ICON_PATH
				+ path;

		ImageDescriptor image = getDefault().getImageRegistry().getDescriptor(
				iconPath);
		if (image == null) {

			final ImageDescriptor descriptor = imageDescriptorFromPlugin(
					PLUGIN_ID, iconPath);
			if (descriptor != null) {
				getDefault().getImageRegistry().put(iconPath, descriptor);
				image = getDefault().getImageRegistry().getDescriptor(iconPath);
			}
		}
		return image;
	}

	/**
	 * Gets the image descriptor.
	 * 
	 * @param path
	 *            the path
	 * @return the image descriptor
	 */
	public static ImageDescriptor getImageDescriptor(final String path) {
		ImageDescriptor descriptor = imageDescriptorFromPlugin(PLUGIN_ID, path);
		return descriptor;
	}

	/**
	 * Format child.
	 * 
	 * @param child
	 *            the child
	 */
	public static void formatChild(Element child) {
		if (child instanceof IDOMNode) {
			IDOMModel model = ((IDOMNode) child).getModel();
			try {
				model.aboutToChangeModel();
				IStructuredFormatProcessor formatProcessor = new FormatProcessorXML();
				formatProcessor.formatNode(child);
			} finally {
				model.changedModel();
			}
		}
	}

	/**
	 * Adds the documentation.
	 * 
	 * @param component
	 *            the component
	 * @param docText
	 *            the doc text
	 */
	public static void addDocumentation(XSDConcreteComponent component,
			String docText) {
		XSDAnnotation xsdAnnotation = XSDCommonUIUtils.getInputXSDAnnotation(
				component, true);
		Element element = xsdAnnotation.getElement();

		List documentationList = xsdAnnotation.getUserInformation();
		Element documentationElement = null;
		boolean documentationExists = false;
		if (documentationList.size() > 0) {
			documentationExists = true;
			documentationElement = (Element) documentationList.get(0);
		}

		if (documentationElement == null) {
			documentationElement = xsdAnnotation.createUserInformation(null);
			element.appendChild(documentationElement);
			formatChild(documentationElement);
			xsdAnnotation.updateElement();
			xsdAnnotation.setElement(element);
		}

		if (documentationElement.hasChildNodes()) {
			if (documentationElement instanceof IDOMElement) {
				IDOMElement domElement = (IDOMElement) documentationElement;

				Node firstChild = documentationElement.getFirstChild();
				Node lastChild = documentationElement.getLastChild();
				int start = 0;
				int end = 0;

				IDOMNode first = null;
				if (firstChild instanceof IDOMNode) {
					first = (IDOMNode) firstChild;
					start = first.getStartOffset();
				}
				if (lastChild instanceof IDOMNode) {
					IDOMNode last = (IDOMNode) lastChild;
					end = last.getEndOffset();
				}

				if (domElement != null) {

					domElement
							.getModel()
							.getStructuredDocument()
							.replaceText(documentationElement, start,
									end - start, docText);
				}
			}
		} else {
			if (docText.length() > 0) {

				Node childNode = documentationElement.getOwnerDocument()
						.createTextNode(docText);
				documentationElement.appendChild(childNode);
			}
		}
		formatChild(xsdAnnotation.getElement());
	}

	/**
	 * Gets the model group.
	 * 
	 * @param cType
	 *            the c type
	 * @return the model group
	 */
	public static XSDModelGroup getModelGroup(XSDComplexTypeDefinition cType) {
		XSDParticle particle = null;
		XSDComplexTypeContent xsdComplexTypeContent = cType.getContent();
		if (xsdComplexTypeContent instanceof XSDParticle) {
			particle = (XSDParticle) xsdComplexTypeContent;
		}

		if (particle == null) {
			return null;
		}

		Object particleContent = particle.getContent();
		XSDModelGroup group = null;

		if (particleContent instanceof XSDModelGroupDefinition) {
			group = ((XSDModelGroupDefinition) particleContent)
					.getResolvedModelGroupDefinition().getModelGroup();
		} else if (particleContent instanceof XSDModelGroup) {
			group = (XSDModelGroup) particleContent;
		}

		if (group == null) {
			return null;
		}

		return group;
	}

	/**
	 * Gets the prefix.
	 * 
	 * @param schema
	 *            the schema
	 * @param nameSpace
	 *            the name space
	 * @return the prefix
	 */
	public static String getPrefix(XSDSchema schema, String nameSpace) {
		Map<String, String> qNamesMap = schema.getQNamePrefixToNamespaceMap();

		if (qNamesMap.containsValue(nameSpace)) {
			for (Entry<String, String> entry : qNamesMap.entrySet()) {
				if (StringUtils.equals(entry.getValue(), nameSpace)) {
					return entry.getKey() + SOATypeLibraryConstants.COLON;
				}
			}
		}

		int prefixInt = 0;
		while (true) {
			prefixInt++;
			if (!qNamesMap
					.containsKey(SOATypeLibraryConstants.DEFAULT_TNS_PREFIX
							+ prefixInt))
				break;
		}
		String prefix = SOATypeLibraryConstants.DEFAULT_TNS_PREFIX + prefixInt;
		qNamesMap.put(prefix, nameSpace);
		return prefix + SOATypeLibraryConstants.COLON;
	}

	/**
	 * Creates the xsd element declaration.
	 * 
	 * @param strName
	 *            the str name
	 * @param strType
	 *            the str type
	 * @param minOccurs
	 *            the min occurs
	 * @param maxOccurs
	 *            the max occurs
	 * @return the xSD particle
	 */
	public static XSDParticle createXSDElementDeclaration(String strName,
			String strType, int minOccurs, int maxOccurs) {

		XSDSimpleTypeDefinition type = XSDFactory.eINSTANCE
				.createXSDSimpleTypeDefinition();
		type.setName(strType);
		XSDElementDeclaration element = XSDFactory.eINSTANCE
				.createXSDElementDeclaration();

		element.setName(StringUtils.defaultString(strName)); //$NON-NLS-1$
		element.setTypeDefinition(type);

		XSDParticle particle = XSDFactory.eINSTANCE.createXSDParticle();
		particle.setContent(element);
		// -2 means no Occurs attribute
		if (minOccurs != NO_OCCURS) {
			particle.setMinOccurs(minOccurs);
		}
		if (maxOccurs != NO_OCCURS) {
			particle.setMaxOccurs(maxOccurs);
		}

		return particle;
	}

	private static XSDAttributeUse createXSDAttrDeclaration(String strName,
			String strType) {

		XSDSimpleTypeDefinition type = XSDFactory.eINSTANCE
				.createXSDSimpleTypeDefinition();
		type.setName(strType);
		XSDAttributeDeclaration attribute = XSDFactory.eINSTANCE
				.createXSDAttributeDeclaration();

		attribute.setName(StringUtils.defaultString(strName)); //$NON-NLS-1$
		attribute.setTypeDefinition(type);

		XSDAttributeUse attributeUse = XSDFactory.eINSTANCE
				.createXSDAttributeUse();
		attributeUse.setAttributeDeclaration(attribute);
		attributeUse.setContent(attribute);
		return attributeUse;
	}

	/**
	 * Sets the base type for complex types.
	 * 
	 * @param complexTypeDefinition
	 *            the complex type definition
	 * @param baseType
	 *            the base type
	 * @param typeName
	 *            the type name
	 * @param typeLibraryName
	 *            the type library name
	 * @param version
	 *            the version
	 * @throws CommandFailedException
	 *             the command failed exception
	 */
	public static void setBaseTypeForComplexTypes(
			XSDComplexTypeDefinition complexTypeDefinition, Object baseType,
			String typeName, String typeLibraryName, String version)
			throws CommandFailedException {
		XSDComplexTypeDefinition restriction = XSDFactory.eINSTANCE
				.createXSDComplexTypeDefinition();
		boolean isPrimitive = true;
		try {
			if (baseType instanceof String)
				restriction.setName(getPrefix(
						complexTypeDefinition.getSchema(),
						SOATypeLibraryConstants.W3C_NAMEPSACE)
						+ baseType);
			else {
				isPrimitive = false;
				LibraryType libBaseType = (LibraryType) baseType;
				restriction.setName(getPrefix(
						complexTypeDefinition.getSchema(),
						TypeLibraryUtil.getNameSpace(libBaseType))
						+ libBaseType.getName());

			}
		} catch (Exception e) {
			throw new CommandFailedException(
					"Could not get the name space for the type "
							+ baseType
							+ " from the registry. Please fix this issue and try again.");
		}
		complexTypeDefinition.setBaseTypeDefinition(restriction);

		if (!isPrimitive) {
			addImport((LibraryType) baseType, complexTypeDefinition, typeName,
					version, typeLibraryName);
		}

	}

	public static TemplateModel processTemplateModel(InputStream inputStream)
			throws CoreException, IOException {
		TemplateModel templateModel = new TemplateModel();
		XSDSchema schema = TypeLibraryUtil.parseSchema(inputStream);
		if (schema.getTypeDefinitions() != null
				&& schema.getTypeDefinitions().size() > 0
				&& schema.getTypeDefinitions().get(0) != null
				&& schema.getTypeDefinitions().get(0) instanceof XSDTypeDefinition) {
			XSDAnnotation xsdAnnotation = XSDCommonUIUtils
					.getInputXSDAnnotation((XSDTypeDefinition) schema
							.getTypeDefinitions().get(0), false);
			if (xsdAnnotation != null) {
				List documentationList = xsdAnnotation.getUserInformation();
				if (documentationList.size() > 0) {
					Element documentationElement = (Element) documentationList
							.get(0);
					templateModel.setDocumentation(documentationElement
							.getTextContent());
				}
			}
		}
		return templateModel;

	}

}
