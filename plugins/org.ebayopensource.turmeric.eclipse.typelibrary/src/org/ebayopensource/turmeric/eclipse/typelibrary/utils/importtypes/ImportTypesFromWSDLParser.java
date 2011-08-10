package org.ebayopensource.turmeric.eclipse.typelibrary.utils.importtypes;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * This class is used to cut XSD schema from WSDL. It uses
 * ImportTypesFromXSDParser to handle XSD content.
 * 
 * @author mzang
 * 
 */
public class ImportTypesFromWSDLParser extends DefaultHandler {

	String path = null;

	/**
	 * cut XSD files from WSDL
	 * 
	 * @param wsdlPath
	 * @throws SAXException
	 * @throws IOException
	 * @throws ParserConfigurationException
	 */
	public void cutWSDL(String wsdlPath) throws SAXException, IOException,
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

	// variables for all types in current wsdl.
	private Map<String, TypeModel> xsds = new HashMap<String, TypeModel>();

	private Map<String, TypeModel> referedTLTypes = new HashMap<String, TypeModel>();

	private Map<String, String> nsMappingWSDL = new HashMap<String, String>();

	private List<NodeQName> xmlPath = new ArrayList<NodeQName>();

	// node QNames

	private static final String WSDL_NS = "http://schemas.xmlsoap.org/wsdl[/]{0,}";

	private static final Pattern WSDL_NS_PATTERN = Pattern.compile(WSDL_NS,
			Pattern.CASE_INSENSITIVE);

	private static final String TYPE_NS = "http://www.w3.org/[0-9]{4}/XMLSchema[/]{0,}";

	private static final Pattern SCHEMA_NS_PATTERN = Pattern.compile(TYPE_NS,
			Pattern.CASE_INSENSITIVE);

	private static String XML_NS = "xmlns:";

	private static String WSDL_DEF_NAME = "definitions";

	private static String WSDL_TYPE_DEF_NAME = "types";

	private static String SCHEMA_DEF_NAME = "schema";

	/**
	 * get Type Models
	 * 
	 * @return
	 */
	public Collection<TypeModel> getTypeModels() {
		return this.xsds.values();
	}

	private static boolean isWSDLNS(String namespace) {
		Matcher matcher = WSDL_NS_PATTERN.matcher(namespace);
		return matcher.matches();
	}

	private boolean wsdlDef() {
		if (xmlPath.size() < 1) {
			return false;
		}
		NodeQName qName = xmlPath.get(0);
		return (qName.ns == NS.WSDL) && WSDL_DEF_NAME.equals(qName.localName);
	}

	private boolean wsdlTypesDef() {
		if (xmlPath.size() < 2) {
			return false;
		}
		NodeQName qName = xmlPath.get(1);
		return (qName.ns == NS.WSDL)
				&& WSDL_TYPE_DEF_NAME.equals(qName.localName);
	}

	private boolean schemaDef() {
		if (xmlPath.size() < 3) {
			return false;
		}
		NodeQName qName = xmlPath.get(2);
		return (qName.ns == NS.SCHEMA)
				&& SCHEMA_DEF_NAME.equals(qName.localName);
	}

	private boolean isWSDLNode() {
		if (xmlPath.size() != 1) {
			return false;
		}
		return wsdlDef();
	}

	private boolean isSchemaNodeStart() {
		if (xmlPath.size() != 3) {
			return false;
		}
		return wsdlDef() && wsdlTypesDef() && schemaDef();
	}

	private boolean isSchemaNodeEnd(String uri, String localName) {
		if (xmlPath.size() != 3) {
			return false;
		}

		return (wsdlDef() && wsdlTypesDef() && schemaDef())
				&& (isSchemaNS(uri) && SCHEMA_DEF_NAME.equals(localName));
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
	 */
	private void postProcessTypes() throws SAXException {
		referedTLTypes = ImportTypesFromXSDParser.postProcessTypes(xsds);
	}

	/**
	 * get referred types
	 * 
	 * @return
	 */
	public Map<String, TypeModel> getReferedTLTypes() {
		return referedTLTypes;
	}

	/**
	 * document end, post process
	 */
	@Override
	public void endDocument() throws SAXException {
		postProcessTypes();
		ImportTypesFromXSDParser.clearNoTypeNameCounter();
		// test();
	}

	/**
	 * this is for test purpose only.
	 * 
	 * @throws SAXException
	 */
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

	private static boolean isSchemaNS(String namespace) {
		Matcher matcher = SCHEMA_NS_PATTERN.matcher(namespace);
		return matcher.matches();
	}

	private ImportTypesFromXSDParser schmaHandler = null;

	/**
	 * start to handle an element
	 */
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {

		if (schmaHandler != null) {
			schmaHandler.startElement(uri, localName, qName, attributes);
			return;
		}

		NS ns = NS.OTHER;
		if (isSchemaNS(uri)) {
			ns = NS.SCHEMA;
		} else if (isWSDLNS(uri)) {
			ns = NS.WSDL;
		}
		NodeQName currNode = new NodeQName(uri, ns, localName);
		xmlPath.add(currNode);

		// current node is wsdl:definitions
		if (isWSDLNode() == true) {
			// store the namespace attributes in the nsMappingWSDL
			for (int index = 0; index < attributes.getLength(); index++) {
				String attrName = attributes.getQName(index);
				if (attrName.toLowerCase().startsWith((XML_NS))) {
					nsMappingWSDL.put(attrName, attributes.getValue(index));
				}
			}
			return;
		}

		// current node is xs:schema
		if (isSchemaNodeStart() == true) {
			schmaHandler = new ImportTypesFromXSDParser();
			schmaHandler.setOuterNSMappint(nsMappingWSDL);
			schmaHandler.startElement(uri, localName, qName, attributes);
		}
	}

	/**
	 * element end
	 */
	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		if (schmaHandler != null) {
			schmaHandler.endElement(uri, localName, qName);
			if (isSchemaNodeEnd(uri, localName) == true) {
				xsds.putAll(schmaHandler.getTypeModelMap());
				schmaHandler = null;
			}
		}

		if (schmaHandler == null) {
			xmlPath.remove(xmlPath.size() - 1);
		}

	}

	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		if (schmaHandler != null) {
			schmaHandler.characters(ch, start, length);
		}
	}

	/**
	 * for test only
	 * @param args
	 * @throws SAXException
	 * @throws IOException
	 * @throws ParserConfigurationException
	 */
	public static void main(String[] args) throws SAXException, IOException,
			ParserConfigurationException {
		// File f = new File(args[0]);
		File f = new File("C:\\Documents and Settings\\mzang\\Desktop\\aaaaa");

		for (File wsdl : f.listFiles()) {
			if (isWSDL(wsdl) == false) {
				continue;
			}
			try {
				ImportTypesFromWSDLParser instance = new ImportTypesFromWSDLParser();
				instance.cutWSDL(wsdl.toString());
			} catch (Throwable ex) {
				ex.printStackTrace();
			}
		}
	}

	private static boolean isWSDL(File f) {
		if (f.isFile() == false) {
			return false;
		}
		String uri = f.toString();
		int tail = uri.lastIndexOf('.');
		if (tail == -1) {
			return false;
		}
		String end = uri.substring(tail + 1);
		if (end.equalsIgnoreCase("wsdl") == false) {
			return false;
		}
		return true;
	}

}
