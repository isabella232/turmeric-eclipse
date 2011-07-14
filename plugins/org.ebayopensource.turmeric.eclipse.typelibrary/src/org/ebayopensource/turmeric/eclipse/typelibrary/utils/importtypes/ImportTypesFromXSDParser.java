package org.ebayopensource.turmeric.eclipse.typelibrary.utils.importtypes;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.commons.lang.StringUtils;
import org.ebayopensource.turmeric.common.config.LibraryType;
import org.ebayopensource.turmeric.eclipse.repositorysystem.core.SOAGlobalRegistryAdapter;
import org.ebayopensource.turmeric.eclipse.typelibrary.utils.importtypes.xsdpiece.IXSDPiece;
import org.ebayopensource.turmeric.eclipse.typelibrary.utils.importtypes.xsdpiece.SchemaTypePiece;
import org.ebayopensource.turmeric.eclipse.typelibrary.utils.importtypes.xsdpiece.StringPiece;
import org.ebayopensource.turmeric.eclipse.typelibrary.utils.importtypes.xsdpiece.TypeDocumentationPiece;
import org.ebayopensource.turmeric.eclipse.typelibrary.utils.importtypes.xsdpiece.TypeLibraryNamePiece;
import org.ebayopensource.turmeric.eclipse.typelibrary.utils.importtypes.xsdpiece.TypeNamePiece;
import org.ebayopensource.turmeric.eclipse.typelibrary.utils.importtypes.xsdpiece.TypeNamespacePiece;
import org.ebayopensource.turmeric.eclipse.typelibrary.utils.importtypes.xsdpiece.TypeQNamePiece;
import org.ebayopensource.turmeric.eclipse.typelibrary.utils.importtypes.xsdpiece.TypeRelatedContent;
import org.ebayopensource.turmeric.eclipse.typelibrary.utils.importtypes.xsdpiece.XSDContentList;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * This class is used to parse XSD content and cut XSD content from it. Also it
 * will handle dependencies, add document node to type.
 * 
 * @author mzang
 * 
 */
public class ImportTypesFromXSDParser extends DefaultHandler {

	// FIXME: remove this later
	private String path = null;

	public void cutXSD(String wsdlPath) throws SAXException, IOException,
			ParserConfigurationException {
		path = wsdlPath;
		SAXParserFactory saxfac = SAXParserFactory.newInstance();
		saxfac.setFeature("http://xml.org/sax/features/namespace-prefixes",
				true);
		saxfac.setNamespaceAware(true);
		saxfac.setXIncludeAware(true);
		saxfac.setValidating(false);
		SAXParser saxParser = saxfac.newSAXParser();
		saxParser.parse(wsdlPath, this);
	}

	private static final String REPLACE = "${IMPORT_TYPE_REPLACEMENT}";

	private static final String REPLACE_SPLITER = "\\$\\{IMPORT_TYPE_REPLACEMENT\\}";

	// variables that need to be clear up once a type is processed.

	private XSDContentList content = new XSDContentList();

	private String typeName = null;

	private String tlName = null;

	private String tlNamespace = null;

	private String documentation = null;

	private int documentIndex = -1;

	private int documentAnnotationIndex = -1;

	private boolean acceptContent = false;

	private List<String> errors = new ArrayList<String>();

	private List<String> warnings = new ArrayList<String>();

	// variables for all types in the same schema node.
	private Map<String, String> nsMappingSchema = new HashMap<String, String>();

	private String targetNamespace = null;

	private String schemaXMLNS = null;

	private String schemaQName = null;

	private Map<String, String> tpyeAttrs = new HashMap<String, String>();

	// variables for all types in current wsdl.
	private Map<String, TypeModel> xsds = new HashMap<String, TypeModel>();

	private Map<String, TypeModel> refferedTLTypes = null;

	private Map<String, String> outerNSMapping = new HashMap<String, String>();

	private List<NodeQName> xmlPath = new ArrayList<NodeQName>();

	private static int noTypeNameCounter = 0;

	public static void clearNoTypeNameCounter() {
		noTypeNameCounter = 0;
	}

	public void setOuterNSMappint(Map<String, String> outerNSMapping) {
		this.outerNSMapping = outerNSMapping;
	}

	// node QNames

	private static final String TYPE_NS = "http://www.w3.org/[0-9]{4}/XMLSchema[/]{0,}";

	private static final Pattern SCHEMA_NS_PATTERN = Pattern.compile(TYPE_NS,
			Pattern.CASE_INSENSITIVE);

	private static String SCHEMA_DEF_NAME = "schema";

	private static String COMPLEX_TYPE_NODE = "complexType";

	private static String SIMPLE_TYPE_NODE = "simpleType";

	private static String ANNOTATION_NODE = "annotation";

	private static String TYPE_DOCUMENTATION_NODE = "documentation";

	private static String APPINFO_NODE = "appinfo";

	private static String TL_SOURCE_NODE = "typeLibrarySource";

	private static String ELEMENT_NODE = "element";

	private static String ATTRIBUTE_NODE = "attribute";

	private static String UNION_NODE = "union";

	private static String SEQUENCE_NODE = "sequence";

	private static String CHOICE_NODE = "choice";

	private static String XS_ANY_NODE = "any";

	// attribute names
	private static String ATTR_TARGET_NS_LB = "targetNamespace";

	private static String TYPE_LIBRARY_ATTR = "library";

	private static String TYPE_NS_ATTR = "namespace";

	private static String TYPE_NAME_ATTR = "name";

	private static String TYPE_BASE_ATTR = "base";

	private static String TYPE_TYPE_ATTR = "type";

	private static String ELEMENT_REF = "ref";

	// namespace in attribute value
	private static String XS_NS = "xs:";
	private static String XML_NS = "xmlns:";
	private static String XML_NS_ATTR = "xmlns";

	// import statement and included statement.
	private static final String TYPE_INCLUDE_NODE = "\r\n\t<xs:include schemaLocation=\"typelib://"
			+ REPLACE + "//" + REPLACE + ".xsd\" />";
	private static final String TYPE_IMPORT_NODE = "\r\n\t<xs:import namespace=\"{0}\" \r\n"
			+ "\t\tschemaLocation=\"typelib://{1}//{2}.xsd\" />";
	// document node
	private static final String ANNOTATION_DOC_NODE = "\r\n\t<xs:annotation>\r\n"
			+ "\t\t<xs:documentation>\r\n"
			+ "\t\t\t"
			+ REPLACE
			+ "\r\n\t\t</xs:documentation>\r\n" + "\t</xs:annotation>\r\n";

	private static final String DOC_NODE = "\r\n\t\t<xs:documentation>\r\n"
			+ "\t\t\t" + REPLACE + "\r\n\t\t</xs:documentation>\r\n";

	/*
	 * fill with all external type namespace. and real type library namespace
	 */
	private static final String TYPE_SCHEMA_NODE_1 = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n"
			+ "<xs:schema ";

	private static final String TYPE_SCHEMA_NODE_2 = "\r\n"
			+ "\t attributeFormDefault=\"unqualified\" elementFormDefault=\"qualified\" "
			+ "targetNamespace=\"";

	private static final String TYPE_SCHEMA_NODE_3 = "\" version=\"1.0.0\">\r\n";

	private static final String TYPE_SCHEMA_NODE_END = "\r\n</xs:schema>";

	private int schemaStartIndex = 0;

	public void useWSDLModel() {
		this.schemaStartIndex = 2;
	}

	public void setSchemaStartIndex(int schemaStartIndex) {
		this.schemaStartIndex = schemaStartIndex;
	}

	private String getNameForNoTypeWithoutName() {
		noTypeNameCounter++;
		return "NoNameType" + noTypeNameCounter;
	}

	private static Map<String, LibraryType> LIB_TYPE_MAPPING = new HashMap<String, LibraryType>();

	static {
		List<LibraryType> allTypesList;
		try {
			allTypesList = SOAGlobalRegistryAdapter.getInstance()
					.getGlobalRegistry().getAllTypes();
			for (LibraryType libType : allTypesList) {
				String namespace = libType.getNamespace();
				String typeName = libType.getName();
				LIB_TYPE_MAPPING.put(namespace + ":" + typeName, libType);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Collection<TypeModel> getTypeModels() {
		return this.xsds.values();
	}

	public Map<String, TypeModel> getTypeModelMap() {
		return this.xsds;
	}

	private static boolean isSchemaNS(String namespace) {
		Matcher matcher = SCHEMA_NS_PATTERN.matcher(namespace);
		return matcher.matches();
	}

	private boolean schemaDef() {
		if (xmlPath.size() < schemaStartIndex + 1) {
			return false;
		}
		NodeQName qName = xmlPath.get(schemaStartIndex);
		return (qName.ns == NS.SCHEMA)
				&& SCHEMA_DEF_NAME.equals(qName.localName);
	}

	private boolean typeDef() {
		if (xmlPath.size() < schemaStartIndex + 2) {
			return false;
		}
		NodeQName qName = xmlPath.get(schemaStartIndex + 1);
		return (qName.ns == NS.SCHEMA)
				&& (COMPLEX_TYPE_NODE.equals(qName.localName) || SIMPLE_TYPE_NODE
						.equals(qName.localName));
	}

	private boolean typeAnotationDef() {
		if (xmlPath.size() < schemaStartIndex + 3) {
			return false;
		}
		NodeQName qName = xmlPath.get(schemaStartIndex + 2);
		return (qName.ns == NS.SCHEMA)
				&& (ANNOTATION_NODE.equals(qName.localName));
	}

	private boolean typeAnotationDocDef() {
		if (xmlPath.size() < schemaStartIndex + 4) {
			return false;
		}
		NodeQName qName = xmlPath.get(schemaStartIndex + 3);
		return (qName.ns == NS.SCHEMA)
				&& (TYPE_DOCUMENTATION_NODE.equals(qName.localName));
	}

	private boolean typeAnotationAppInfoDef() {
		if (xmlPath.size() < schemaStartIndex + 4) {
			return false;
		}
		NodeQName qName = xmlPath.get(schemaStartIndex + 3);
		return (qName.ns == NS.SCHEMA)
				&& (APPINFO_NODE.equals(qName.localName));
	}

	private boolean typeAnotationTLSourceDef() {
		if (xmlPath.size() < schemaStartIndex + 5) {
			return false;
		}
		NodeQName qName = xmlPath.get(schemaStartIndex + 4);
		return (TL_SOURCE_NODE.equals(qName.localName));
	}

	// private boolean typeElementDef(NodeQName qName) {
	// if (qName == null) {
	// return false;
	// }
	// return (qName.ns == NS.SCHEMA)
	// && (ELEMENT_NODE.equals(qName.localName));
	// }

	private boolean typeAnyEleDef(NodeQName qName) {
		if (qName == null) {
			return false;
		}
		return (qName.ns == NS.SCHEMA) && (XS_ANY_NODE.equals(qName.localName));
	}

	private boolean isSchemaNode() {
		if (xmlPath.size() != schemaStartIndex + 1) {
			return false;
		}
		return schemaDef();
	}

	// private boolean isInSchemaNode() {
	// if (xmlPath.size() <= schemaStartIndex + 2) {
	// return false;
	// }
	// return wsdlDef() && schemaDef();
	// }

	private boolean isTypeNode() {
		if (xmlPath.size() != schemaStartIndex + 2) {
			return false;
		}
		return schemaDef() && typeDef();
	}

	private boolean isInTypeNode() {
		if (xmlPath.size() < schemaStartIndex + 3) {
			return false;
		}
		return schemaDef() && typeDef();
	}

	private boolean isTypeLibrarySourceNode() {
		if (xmlPath.size() != schemaStartIndex + 5) {
			return false;
		}
		return schemaDef() && typeDef() && typeAnotationDef()
				&& typeAnotationAppInfoDef() && typeAnotationTLSourceDef();
	}

	private boolean isTypeDocumentationNode() {
		if (xmlPath.size() != schemaStartIndex + 4) {
			return false;
		}
		return schemaDef() && typeDef() && typeAnotationDef()
				&& typeAnotationDocDef();
	}

	private boolean isTypeAnnotationNode() {
		if (xmlPath.size() != schemaStartIndex + 3) {
			return false;
		}
		return schemaDef() && typeDef() && typeAnotationDef();
	}

	private boolean isTypeDependencyRelated(String attrName, String attrValue) {
		if (TYPE_BASE_ATTR.equals(attrName) == false
				&& TYPE_TYPE_ATTR.equals(attrName) == false) {
			return false;
		}

		return true;
	}

	private static boolean isBasicTypeSchemaNamespace(String namespace) {
		if (namespace == null) {
			return false;
		}
		Matcher mather = SCHEMA_NS_PATTERN.matcher(namespace);
		return mather.matches();
	}

	private boolean isInW3CBasicSchemaTypeNamespace(String uri, String typeQName)
			throws SAXException {
		String[] qName = typeQName.split(":");
		if (qName.length == 1) {
			return isBasicTypeSchemaNamespace(uri);
		}
		if (qName.length != 2) {
			throw new SAXException(
					"Invalidated type. Type QName is \"namespacepart:typename\":"
							+ typeQName);
		}
		String namespace = nsMappingSchema.get(XML_NS + qName[0]);
		return isBasicTypeSchemaNamespace(namespace);
	}

	private static LibraryType findTypeInTypeLibrary(String namespace,
			String typeName) {
		return LIB_TYPE_MAPPING.get(namespace + ":" + typeName);
	}

	/**
	 * key part of this method is to handle dependency. there are four kinds of
	 * dependencies. 1) Using a standard type from W3C schema type. How to check
	 * - check the namespace value is W3C_TYPE_NS or not. How to handle - For
	 * this kind of dependency, just adding a xmlns definition attribute to the
	 * xs:schema node. 2) Using a type library type. How to check - using the
	 * namespace and type name to try to find such a type in type library. If
	 * found, then it is a dependency to a type library type. Otherwise, it is
	 * not a type library dependency type. How to handle - for a TL type
	 * dependency, need to add a xs:import statement. TYPE_IMPORT is provided to
	 * use. just provide the type library name and the type name. also need to
	 * adding a xmlns definition attribute to the xs:schema node. 3) An internal
	 * type dependency. How to check - if 2) not match and the namespace of that
	 * dependency type is the same as the namespace of current type, so it must
	 * be an internal dependency. How to handle - for internal dependency, there
	 * are two more thing to handle. First, the name of the dependency type may
	 * be changed. Second, the namespace of the dependency type may changed (in
	 * fact, must change). So for the xsd content, the attribute where used the
	 * dependency type, must use "${}" and waiting to be replaced (already use
	 * it when adding content to XSD). For the XSD header, also should use "${}"
	 * and waiting to be replaced. 4) An internal dependency which in fact is
	 * and external type library dependency. How to check - find the dependency
	 * from xsds map and method isTypeLibraryType returns true. How to handle -
	 * first, it is a type library dependency, so there is no need to using free
	 * marker because the type name will never change. Set the type name value
	 * to be it self. Then need to handle the namespace part because the
	 * namespace part is incorrect.
	 * 
	 * 
	 * Notice that we need to check 2) first because a type may have the same
	 * namespace with an existing type library. just like a class named MyClass
	 * in java.lang package.
	 * 
	 * @param xsds
	 *            TypeModels to be processed
	 * @return TypeModel that no need to be imported.
	 * 
	 */
	public static Map<String, TypeModel> postProcessTypes(
			Map<String, TypeModel> xsds) throws SAXException {
		Set<String> keys = xsds.keySet();

		// a map to store types that refers to type library type
		Map<String, TypeModel> referToTLType = new HashMap<String, TypeModel>();
		Set<String> typeLibTypeKey = new HashSet<String>();
		for (String key : keys) {
			TypeModel model = xsds.get(key);

			// type library name and namespace are both validated
			if (model.isNeedToImport() == false) {
				typeLibTypeKey.add(key);
				referToTLType.put(key, model);
				continue;
			}

			Map<String, String> typeNSMapping = model.getNsMappingSchema();

			/*
			 * the following Collection instance using set to avoid duplication
			 * all xmlns attributes for current type. The list is used to store
			 * contents to be added to XSD.
			 */
			// all xmlns mappings for current type.
			Set<String> xmlnsNames = new HashSet<String>();
			List<IXSDPiece> xmlns = new ArrayList<IXSDPiece>();

			// all include node for current type
			Set<String> includeTypeNames = new HashSet<String>();
			List<IXSDPiece> includes = new ArrayList<IXSDPiece>();

			// all importNode for current type
			Set<String> importTLTypeNames = new HashSet<String>();
			List<IXSDPiece> imports = new ArrayList<IXSDPiece>();

			// key-value for namespace and xmlns, just for case 4)
			Map<String, String> namespaceToXMLNS = new HashMap<String, String>();

			/*
			 * adding xmlns to XMLNS if needed. so that the current xsd will
			 * just use node name without prefix such as "xs:" or "xsd:". It
			 * doesn't have a name. So no need to add an element to
			 * "xmlnsNames".
			 */
			if (model.getSchemaXMLNS() != null) {
				xmlns.add(new StringPiece("\r\n\txmlns=\""
						+ model.getSchemaXMLNS() + "\""));
			}

			/*
			 * except schema basic type, the "xs:schema" is also kind of needing
			 * "xmlns:xs". Add it if needed.
			 */
			{
				String schemaQName = model.getSchemaQName();
				String[] qNames = schemaQName.split(":");
				if (qNames.length == 2) {

					xmlns.add(new StringPiece("\r\n\txmlns:" + qNames[0]
							+ "=\"http://www.w3.org/2001/XMLSchema\""));
					xmlnsNames.add(qNames[0]);
				}
			}

			/*
			 * go though all pieces, handle dependencies.
			 */
			for (IXSDPiece piece : model.getTypeContent().getContentList()) {
				if (piece instanceof StringPiece) {
					continue;
				}
				if (piece instanceof TypeRelatedContent) {
					TypeRelatedContent tp = (TypeRelatedContent) piece;
					if (tp.getQName() == null) {
						// null means current type. used for type name.
						tp.setModel(model);
						continue;
					}
				}
				// basic type
				if (piece instanceof SchemaTypePiece) {
					SchemaTypePiece sp = (SchemaTypePiece) piece;
					String dep = sp.getContent();

					String[] qName = dep.split(":");
					if (qName.length != 2) {
						throw new SAXException(
								"Invalidated type. Type QName should be format"
										+ " \"namespacepart:typename\":" + dep);
					}
					String nsShort = qName[0];
					String shortNSDefinition = XML_NS + nsShort;

					if (xmlnsNames.contains(nsShort)) {
						/* xmlns:xs is written to schema header by default. */
						continue;
					}
					xmlns.add(new StringPiece("\r\n\t" + shortNSDefinition
							+ "=\"" + sp.getNamespace() + "\""));
					xmlnsNames.add(nsShort);
					continue;
				}
				// if not dependency related here, jump to next.
				if ((piece instanceof TypeQNamePiece) == false) {
					continue;
				}

				TypeQNamePiece depPiece = (TypeQNamePiece) piece;
				String dep = depPiece.getQName();

				String[] qName = dep.split(":");
				// in fact, before an dependency is added ,this is already
				// checked.
				if (qName.length != 2) {
					throw new SAXException(
							"Invalidated type. Type QName should be in format "
									+ "\"namespacepart:typename\":" + dep);
				}
				// such as "tns"
				String nsShort = qName[0];
				// such as "xml:tns"
				String shortNSDefinition = XML_NS + nsShort;
				// full namespace, such as http://www.example.com/namespace
				String depNamespace = typeNSMapping.get(shortNSDefinition);
				// type name, such as ServiceRequest
				String depTypeName = qName[1];

				// try to find the type in type library using current namespace
				// and type name.
				LibraryType typeLibratyType = findTypeInTypeLibrary(
						depNamespace, depTypeName);

				TypeModel depInternalType = xsds.get(depNamespace + ":"
						+ depTypeName);

				depPiece.setXmlns(nsShort);

				depPiece.setModel(depInternalType);

				/*
				 * if this type refer to a type in type library, use the new
				 * namespace and get it.
				 */
				if (depInternalType != null
						&& depInternalType.isTypeLibraryType()) {
					typeLibratyType = findTypeInTypeLibrary(
							depInternalType.getTypelibRefNamespace(),
							depTypeName);
				}
				/*
				 * if the dependency is found in current schema file and it is
				 * also need to be imported (not refer to a TL type). then it is
				 * a common internal dependency.
				 */
				if ((depInternalType != null && depInternalType
						.isNeedToImport() == true)) {
					// use type in wsdl
					depPiece.setModel(depInternalType);
					if (xmlnsNames.contains(nsShort) == false) {
						xmlnsNames.add(nsShort);
						xmlns.add(new StringPiece("\r\n\t" + shortNSDefinition
								+ "=\""));
						xmlns.add(new TypeNamespacePiece(dep, depInternalType));
						xmlns.add(new StringPiece("\""));
					}
					if (includeTypeNames.contains(depTypeName) == false
							&& StringUtils.equals(depTypeName,
									model.getTypeName()) == false) {
						includeTypeNames.add(depTypeName);
						TypeLibraryNamePiece libName = new TypeLibraryNamePiece(
								dep, depInternalType);
						TypeNamePiece typeName = new TypeNamePiece(dep,
								depInternalType);
						String include = syncXSDPrefix(TYPE_INCLUDE_NODE,
								model.getSchemaQName());
						includes.addAll(addToXSDContentListBatch(include,
								libName, typeName));
					}
					model.addInternalDependency(depInternalType);

				}
				/*
				 * if the dependency is NOT found in current schema file but
				 * found in type library. Then it is a common Type Library Type
				 * dependency.
				 */
				else if ((typeLibratyType != null) && (depInternalType == null)) {
					// use type library type
					if (xmlnsNames.contains(nsShort) == false) {
						xmlnsNames.add(nsShort);
						xmlns.add(new StringPiece("\r\n\t" + shortNSDefinition
								+ "=\"" + depNamespace + "\""));
					}
					String importStr = syncXSDPrefix(TYPE_IMPORT_NODE,
							model.getSchemaQName());

					String importNode = MessageFormat.format(importStr,
							depNamespace, typeLibratyType.getLibraryInfo()
									.getLibraryName(), depTypeName);
					depPiece.setLiteralTypeName(depTypeName);
					if (importTLTypeNames.contains(importNode) == false) {
						importTLTypeNames.add(importNode);
						imports.add(new StringPiece(importNode));
					}

				}
				/*
				 * if the dependency is not only found in current schema file
				 * but also found in type library, AND it has validated TL name
				 * and Namespace attribute. Then it is a referred Type Library
				 * Type dependency.
				 */
				else if ((typeLibratyType != null)
						&& (depInternalType != null && depInternalType
								.isTypeLibraryType() == true)) {
					// this a a wsdl type refer to typle library. use type
					// library type

					// namespace of the TL type
					String typelibNamespace = depInternalType
							.getTypelibRefNamespace();
					// tl name of the found type
					String typeLibraryName = typeLibratyType.getLibraryInfo()
							.getLibraryName();
					// tl name defined in the typeLibrarySource node.
					String refTypeLibraryName = depInternalType
							.getTypelibRefName();
					// tl type name;
					String typeLibTypeName = depInternalType.getTypeName();
					/*
					 * the type library name found in typeLibrarySource is not
					 * the same as the actual type library name found in type
					 * registry, add a warning about this.
					 */
					if (typeLibraryName.equals(refTypeLibraryName) == false) {
						String tlNameWarning = "It is declared in typeLibrarySource node that dependency type \""
								+ typeLibTypeName
								+ "\" is in type library \""
								+ refTypeLibraryName
								+ "\". But this type is found in type library \""
								+ typeLibraryName + "\"";

						model.addWarning(tlNameWarning);
					}

					String xmlnsTL = namespaceToXMLNS.get(typelibNamespace);
					if (xmlnsTL == null) {
						xmlnsTL = createNewXMLNS(xmlnsNames);
						xmlnsNames.add(xmlnsTL);
						namespaceToXMLNS.put(typelibNamespace, xmlnsTL);
						xmlns.add(new StringPiece("\r\n\t" + XML_NS + xmlnsTL
								+ "=\"" + typelibNamespace + "\""));
					}

					depPiece.setXmlns(xmlnsTL);
					depPiece.setLiteralTypeName(typeLibTypeName);

					String importNode = MessageFormat.format(TYPE_IMPORT_NODE,
							typelibNamespace, typeLibraryName, typeLibTypeName);
					if (importTLTypeNames.contains(importNode) == false) {
						importTLTypeNames.add(importNode);
						imports.add(new StringPiece(importNode));
					}
				} else if ((typeLibratyType == null)
						&& (depInternalType == null)) {
					model.addWarning("Dependency \"" + dep
							+ "\" can't be resolved. It is not "
							+ "found in source file or type library.");
				} else if ((typeLibratyType == null)
						&& (depInternalType != null && depInternalType
								.isTypeLibraryType() == true)) {
					model.addWarning("Dependency \""
							+ dep
							+ "\" can't be resolved. Its definition is found in current "
							+ "schema file. It is declared"
							+ " as a type library type. "
							+ " But it is not found in Type Library.");
				} else if (depInternalType != null
						&& depInternalType.isTypeLibrarySourceInvalidated() == false) {
					model.addWarning("Dependency \""
							+ dep
							+ "\" is not validated. Its definition is found in current "
							+ "schema file. But its typeLibrarySource node, library "
							+ "or namespace attribute is missed.");
				}

			}

			List<IXSDPiece> extraContent = new ArrayList<IXSDPiece>();

			String typeSchemaNode1 = syncXSDPrefix(TYPE_SCHEMA_NODE_1,
					model.getSchemaQName());
			extraContent.add(new StringPiece(typeSchemaNode1));

			extraContent.addAll(xmlns);

			String typeSchemaNode2 = syncXSDPrefix(TYPE_SCHEMA_NODE_2,
					model.getSchemaQName());
			extraContent.add(new StringPiece(typeSchemaNode2));

			extraContent
					.add(new TypeNamespacePiece(model.getNamespace(), model));

			String typeSchemaNode3 = syncXSDPrefix(TYPE_SCHEMA_NODE_3,
					model.getSchemaQName());
			extraContent.add(new StringPiece(typeSchemaNode3));

			extraContent.addAll(imports);

			extraContent.addAll(includes);

			extraContent.add(new StringPiece("\r\n"));

			model.getTypeContent().insert(0, extraContent);

			String schemaNodeEnd = syncXSDPrefix(TYPE_SCHEMA_NODE_END,
					model.getSchemaQName());

			model.getTypeContent().add(schemaNodeEnd);

		}

		for (String removeKey : typeLibTypeKey) {
			xsds.remove(removeKey);
		}
		return referToTLType;
	}

	private static String createNewXMLNS(Collection<String> existingXMLNS) {
		int counter = 1;
		String startXMLNS = "tns";
		while (true) {
			String xmlns = startXMLNS + counter;
			if (existingXMLNS.contains(xmlns) == false) {
				return startXMLNS + counter;
			}
			counter++;
		}
	}

	@Override
	public void endDocument() throws SAXException {
		refferedTLTypes = postProcessTypes(xsds);
		// test();
	}

	public Map<String, TypeModel> getReferedTLTypes() {
		return refferedTLTypes;
	}

	public void test() throws SAXException {
		File file = new File(path);
		File parentFile = file.getParentFile();
		String wsdlFileName = file.getName();
		wsdlFileName = wsdlFileName.substring(0, wsdlFileName.lastIndexOf('.'));
		File xsdFolder = new File(parentFile, wsdlFileName);
		if (xsdFolder.exists() == false || xsdFolder.isDirectory() == false) {
			boolean createFolder = xsdFolder.mkdir();
			if (createFolder == false) {
				throw new SAXException("Unable to create folder:" + xsdFolder);
			}
		}

		Set<String> keys = xsds.keySet();
		for (String key : keys) {
			String fileName = key.substring(key.lastIndexOf(':') + 1);
			TypeModel model = xsds.get(key);
			if (model.isNeedToImport() == false) {
				continue;
			}
			model.setTypeLibName("TEMPLIBNAME");
			try {
				String xsdRealContent = null;
				File xsdFile = new File(xsdFolder, fileName + ".xsd");
				try {
					xsdRealContent = model.getTypeContent().toString();
				} catch (Exception ex) {
					ex.printStackTrace();
				}

				if (xsdFile.exists() == true && xsdFile.isFile() == true) {
					xsdFile.delete();
				}
				xsdFile.createNewFile();
				PrintWriter pw = new PrintWriter(xsdFile);
				pw.write(xsdRealContent);
				pw.flush();
				pw.close();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		if (xsdFolder.list() == null || xsdFolder.list().length == 0) {
			System.err.println("Parse failed:\t" + xsdFolder.toString());
		} else {
			System.out.println(xsdFolder.toString() + "\t"
					+ xsdFolder.list().length);
		}
	}

	private boolean hasTypeInElement() {
		if (xmlPath.size() < 2) {
			return false;
		}
		NodeQName eleAttrNode = xmlPath.get(xmlPath.size() - 2);
		NodeQName typeNode = xmlPath.get(xmlPath.size() - 1);
		return (ELEMENT_NODE.equals(eleAttrNode.localName) || ATTRIBUTE_NODE
				.equals(eleAttrNode.localName))
				&& (COMPLEX_TYPE_NODE.equals(typeNode.localName) || SIMPLE_TYPE_NODE
						.equals(typeNode.localName));
	}

	private boolean hasUnionInType() {
		if (xmlPath.size() < 2) {
			return false;
		}
		NodeQName typeNode = xmlPath.get(xmlPath.size() - 2);
		NodeQName unionNode = xmlPath.get(xmlPath.size() - 1);
		return UNION_NODE.endsWith(unionNode.localName)
				&& (COMPLEX_TYPE_NODE.equals(typeNode.localName) || SIMPLE_TYPE_NODE
						.equals(typeNode.localName));
	}

	private boolean hasChoiceInType() {
		if (xmlPath.size() < 3) {
			return false;
		}
		NodeQName typeNode = xmlPath.get(xmlPath.size() - 3);
		NodeQName seqNode = xmlPath.get(xmlPath.size() - 2);
		NodeQName choiceNode = xmlPath.get(xmlPath.size() - 1);
		return CHOICE_NODE.endsWith(choiceNode.localName)
				&& SEQUENCE_NODE.equals(seqNode.localName)
				&& (COMPLEX_TYPE_NODE.equals(typeNode.localName) || SIMPLE_TYPE_NODE
						.equals(typeNode.localName));
	}

	private boolean isElementNode() {
		if (xmlPath.size() < 0) {
			return false;
		}
		NodeQName eleNode = xmlPath.get(xmlPath.size() - 1);
		return ELEMENT_NODE.endsWith(eleNode.localName);
	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		NS ns = NS.OTHER;
		if (isSchemaNS(uri)) {
			ns = NS.SCHEMA;
		}
		NodeQName currNode = new NodeQName(uri, ns, localName);
		xmlPath.add(currNode);

		// current node is xs:schema
		if (isSchemaNode() == true) {
			// first, put all NS mappings of WSDL to schema NS mapping.
			nsMappingSchema.putAll(outerNSMapping);
			// set schemaQName
			schemaQName = qName;
			for (int index = 0; index < attributes.getLength(); index++) {
				String attrName = attributes.getQName(index);
				if (attrName.toLowerCase().startsWith((XML_NS))) {
					/*
					 * store the namespace attributes in the nsMappingSchema.
					 * this is after putting ns attrs into nsMappingWSDL. so if
					 * there are any duplicate ns definitions, the one in type
					 * node will be used.
					 */
					nsMappingSchema.put(attrName, attributes.getValue(index));
				} else {
					// put other attributes into tpyeAttrs map.
					tpyeAttrs.put(attrName, attributes.getValue(index));
				}
				// get xmlns attribute value
				if (XML_NS_ATTR.equals(attrName)) {
					schemaXMLNS = attributes.getValue(index);
				}
			}
			targetNamespace = tpyeAttrs.get(ATTR_TARGET_NS_LB);
			return;
		}

		// current node is typeLibrarySource node, get the library value and
		// namespace value
		if (isTypeLibrarySourceNode() == true) {
			tlName = attributes.getValue(TYPE_LIBRARY_ATTR);
			tlNamespace = attributes.getValue(TYPE_NS_ATTR);
		}

		if (isTypeDocumentationNode() == true) {
			documentation = "";
		}
		boolean inTypeNode = isInTypeNode();
		boolean isTypeNode = isTypeNode();
		boolean isElementNode = isElementNode();
		boolean hasTypeInElement = this.hasTypeInElement();
		boolean hasUnionInType = this.hasUnionInType();
		boolean hasChoiceInType = this.hasChoiceInType();

		if (hasTypeInElement == true) {
			warnings.add("Codegen doesn't support to "
					+ "define a type in element/attribute node.");
		}

		if (hasUnionInType == true) {
			warnings.add("Codegen doesn't support \"union\" in type.");
		}

		if (hasChoiceInType == true) {
			warnings.add("Codegen doesn't support \"choice\" in type.");
		}

		// current node is a type definition node
		if (isTypeNode == true) {
			// begin to add whatever content to xsdContent. this is used in
			// character method.
			acceptContent = true;
			// extract type name from attribute
			for (int index = 0; index < attributes.getLength(); index++) {
				String attrName = attributes.getQName(index);
				String attrValue = attributes.getValue(index);
				if (TYPE_NAME_ATTR.equals(attrName)) {
					// extract type name attribute value and store it to
					// typeName
					typeName = attrValue;
				}
			}
			// if there is no type name, just use a default type name;
		}

		if (inTypeNode == true) {
			if (typeAnyEleDef(currNode) == true) {
				warnings.add("This type uses \"xs:any\". "
						+ "Please consider to remove it.");
			}
		}

		// current node is in a type definition node or type definition node
		if (inTypeNode == true || isTypeNode == true) {
			// write whatever into xsdContent,
			content.add("<" + qName);

			for (int index = 0; index < attributes.getLength(); index++) {
				// trim name and value, just for sure.
				String attrName = attributes.getQName(index).trim();
				String attrValue = attributes.getValue(index).trim();

				if ((isTypeNode == true)
						&& (TYPE_NAME_ATTR.equals(attrName) == true)) {
					// extract type name attribute value and store it to
					// typeName
					typeName = attrValue;
					/*
					 * There might be no type name attribute.write type name to
					 * content outside the loop.
					 */
					continue;
				}

				if (isElementNode == true && ELEMENT_REF.equals(attrName)) {
					warnings.add("Codegen doesn't "
							+ "support using \"ref\" in element.");
				}

				content.add(" " + attrName + "=\"");
				/*
				 * if this attribute is dependency related, using a marker to
				 * replace it later.
				 */
				if (isTypeDependencyRelated(attrName, attrValue) == true) {
					if (isInW3CBasicSchemaTypeNamespace(uri, attrValue) == true) {
						// it is a standard type
						String[] basicQName = attrValue.split(":");
						if (basicQName.length == 2) {
							SchemaTypePiece piece = new SchemaTypePiece(
									attrValue, nsMappingSchema.get(XML_NS
											+ basicQName[0]));
							content.add(piece);
						} else {
							content.add(attrValue);
						}
					} else {
						/*
						 * then it is an typle lib dependency or internal
						 * dependency type, using free marker for replacement
						 */
						content.add(new TypeQNamePiece(attrValue));
					}
				} else {
					content.add(attrValue);
				}
				content.add("\"");
			}
			if (isTypeNode == true) {
				// no type name attribute, add this attribute
				if (typeName == null) {
					typeName = this.getNameForNoTypeWithoutName();
					warnings.add("Type name is empty. Using \"" + typeName
							+ "\" as type name.");
				}
				content.add(" " + TYPE_NAME_ATTR + "=\"");
				content.add(new TypeNamePiece(null));
				content.add("\"");
			}
			content.add(">");
			/*
			 * set documentAnnotationIndex, use it when there is no
			 * annotation>document node.
			 */
			if (isTypeNode() == true) {
				documentAnnotationIndex = content.size();
			}

			/*
			 * set documentIndex. now it is in annotation, use it when
			 * documentation node is not included.
			 */
			if (isTypeAnnotationNode() == true) {
				documentIndex = content.size();
			}
		}

	}

	/**
	 * due to the String.spliter method. if two replacement mark next to each
	 * other, it will fail. this is a private method and there is no such a
	 * usecase.
	 * 
	 * @param strContentBuilder
	 * @param pieces
	 */
	private static List<IXSDPiece> addToXSDContentListBatch(String strContent,
			IXSDPiece... pieces) {
		List<IXSDPiece> listPieces = new ArrayList<IXSDPiece>();

		String[] strPieces = strContent.split(REPLACE_SPLITER);

		if (strPieces.length != pieces.length + 1) {
			throw new IllegalArgumentException(
					"XSD piece not match. Piece size is " + pieces.length
							+ ". Conent piece size is " + strPieces.length
							+ "Content is : \r\n" + strContent);
		}
		listPieces.add(new StringPiece(strPieces[0]));
		for (int i = 1; i < strPieces.length; i++) {
			listPieces.add(pieces[i - 1]);
			listPieces.add(new StringPiece(strPieces[i]));
		}

		return listPieces;

	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		if (isInTypeNode() == true || isTypeNode() == true) {
			/*
			 * write whatever into XSD content if it is type node or in type
			 * node.
			 */
			content.add("</" + qName + ">");
		}
		if (isTypeNode() == true) {
			// fist, insert documentation if needed.
			if (this.documentation == null) {
				/*
				 * it means there is no documentation node. but there are to
				 * possibilities: no annotation document and no document
				 */
				if (documentIndex > 0) {
					// has annotation, no document, just insert document node

					String docNode = syncXSDPrefix(DOC_NODE, schemaQName);
					content.insert(
							documentAnnotationIndex,
							addToXSDContentListBatch(docNode,
									new TypeDocumentationPiece(null)));
				} else if (documentAnnotationIndex > 0) {
					/*
					 * has no annotation, no document, insert annotation
					 * document node
					 */
					String annotationDocNode = syncXSDPrefix(
							ANNOTATION_DOC_NODE, schemaQName);
					content.insert(
							documentAnnotationIndex,
							addToXSDContentListBatch(annotationDocNode,
									new TypeDocumentationPiece(null)));
				}
				documentation = "Documents goes here";
			}

			// end of a type node. create a TypeModel2 instance for current type
			TypeModel type = null;
			if ((tlName == null) == (tlNamespace == null)) {
				type = new TypeModel(typeName, targetNamespace,
						nsMappingSchema, content, documentation, tlName,
						tlNamespace);
			} else {
				type = new TypeModel(typeName, targetNamespace,
						nsMappingSchema, content, documentation, tlName,
						tlNamespace);
				type.addWarning("Type library source is invalidate!");
			}
			type.addErrors(this.errors);
			type.addWarnings(this.warnings);
			type.setSchemaQName(schemaQName);
			type.setSchemaXMLNS(schemaXMLNS);
			// put current type to map, key is the NS:TypeName
			String key = targetNamespace + ":" + typeName;
			if (xsds.get(key) != null) {
				throw new SAXException("Duplicated type found!" + key);
			}
			xsds.put(key, type);
			// clear type content, type name, documentation and two doc idx.
			typeName = null;
			content = new XSDContentList();
			documentation = null;
			documentIndex = -1;
			documentAnnotationIndex = -1;
			// clear tlName and tlNamespace
			tlName = null;
			tlNamespace = null;
			// clear warning and error
			errors.clear();
			warnings.clear();
			// no longer accept content until meet another type
			acceptContent = false;
		} else if (isSchemaNode() == true) {
			// end of a type schema block. clear schema node related variables -
			// schema NS and namespace.
			nsMappingSchema = new HashMap<String, String>();
			targetNamespace = null;
			schemaXMLNS = null;
		}
		xmlPath.remove(xmlPath.size() - 1);
	}

	/**
	 * Some schema use there own prefix rather than the common "xs:". some use
	 * xmlns="namespace" and there is no prefix. this method is used to replace
	 * the redefined "xs:" to expected value: empty or their own prefix. the
	 * target prefix comes from schemaQName prefix.
	 * 
	 * @param str
	 * @param schemaQName
	 * @return
	 */
	private static String syncXSDPrefix(String str, String schemaQName) {
		if (schemaQName == null) {
			return str;
		}
		String[] qName = schemaQName.split(":");
		String replacement = "";
		if (qName.length == 2) {
			replacement = qName[0] + ":";
			if (XS_NS.equals(replacement)) {
				return str;
			}
			str = str.replace("<" + XS_NS, "<" + replacement);
			str = str.replace("</" + XS_NS, "</" + replacement);

		} else {
			str = str.replace("<" + XS_NS, "<");
			str = str.replace("</" + XS_NS, "</");
		}
		return str;
	}

	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		if (acceptContent == false) {
			return;
		}
		String contentStr = new String(ch, start, length);
		contentStr = contentStr.replace("&", "&amp;");
		contentStr = contentStr.replace("<", "&lt;");
		contentStr = contentStr.replace(">", "&gt;");
		contentStr = contentStr.replace("\"", "&quot;");
		contentStr = contentStr.replace("'", "&apos;");
		if (isTypeDocumentationNode() == true) {
			documentation += contentStr;
			if (content.getContentList().get(content.size() - 1) instanceof TypeDocumentationPiece == false) {
				content.add(new TypeDocumentationPiece(null));
			}
		} else {
			content.add(contentStr);
		}
	}

	public static void main(String[] args) throws SAXException, IOException,
			ParserConfigurationException {
		// File f = new File(args[0]);
		File f = new File(
				"C:\\Documents and Settings\\mzang\\Desktop\\aaaaaxsd");

		for (File wsdl : f.listFiles()) {
			if (isXSD(wsdl) == false) {
				continue;
			}
			try {
				System.out.println("======file=======" + wsdl.toString());
				ImportTypesFromXSDParser instance = new ImportTypesFromXSDParser();
				instance.cutXSD(wsdl.toString());
			} catch (Throwable ex) {
				ex.printStackTrace();
			}
		}
	}

	private static boolean isXSD(File f) {
		if (f.isFile() == false) {
			return false;
		}
		String uri = f.toString();
		int tail = uri.lastIndexOf('.');
		if (tail == -1) {
			return false;
		}
		String end = uri.substring(tail + 1);
		if (end.equalsIgnoreCase("xsd") == false) {
			return false;
		}
		return true;
	}

}
