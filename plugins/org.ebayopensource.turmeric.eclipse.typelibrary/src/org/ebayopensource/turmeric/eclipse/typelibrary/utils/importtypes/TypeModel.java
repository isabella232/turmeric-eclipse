package org.ebayopensource.turmeric.eclipse.typelibrary.utils.importtypes;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.ebayopensource.turmeric.eclipse.typelibrary.utils.importtypes.xsdpiece.XSDContentList;

public class TypeModel {

	private String typeName;

	private String namespace;

	private String typeLibName;

	private XSDContentList typeContent;

	private String documentation;

	private Map<String, String> nsMappingSchema = null;

	private List<String> errors = null;

	private List<String> warnings = null;

	/*
	 * properties that only available for type library type, which is the type
	 * in wsdl but has the typeLibrarySource node. They must both have validated
	 * value or both are null.
	 */
	private String typelibRefName = null;

	private String typelibRefNamespace = null;

	// import related variables
	private boolean selected = true;

	private boolean hasImportError = false;

	private String importError = null;

	private List<TypeModel> internalDependencies = null;

	private String schemaQName = "";

	private String schemaXMLNS = "";

	public TypeModel() {

	}

	public TypeModel(String typeName, String namespace,
			Map<String, String> nsMappingSchema, XSDContentList typeContent,
			String documentation, String typelibName, String typelibNamespace) {
		this.typeName = typeName;
		this.namespace = namespace;
		this.nsMappingSchema = nsMappingSchema;
		this.typeContent = typeContent;
		this.documentation = documentation;
		// type from type library
		this.typelibRefName = typelibName;
		this.typelibRefNamespace = typelibNamespace;
	}

	public boolean hasInternalDependencies() {
		return internalDependencies != null && internalDependencies.size() > 0;
	}

	// public String fillContentWithFreemarkers() throws IOException,
	// TemplateException {
	// updateTypePropertiesToMap();
	//
	// if (cfg == null) {
	// cfg = new Configuration();
	// StringTemplateLoader tloader = new StringTemplateLoader();
	// cfg.setTemplateLoader(tloader);
	// tloader.putTemplate(templateName, typeContent);
	// cfg.setObjectWrapper(new DefaultObjectWrapper());
	// }
	//
	// Template temp = cfg.getTemplate(templateName);
	// Writer writer = new StringWriter();
	// temp.process(freemarkerReplacement, writer);
	// return writer.toString();
	// }
	//
	// public String fillContentWithStringReplacement()
	// throws IllegalArgumentException {
	// updateTypePropertiesToMap();
	//
	// String tempContent = typeContent;
	// Set<String> keys = freemarkerReplacement.keySet();
	// for (String key : keys) {
	// String value = freemarkerReplacement.get(key);
	// if (value == null) {
	// throw new IllegalArgumentException("No value foud for key \""
	// + key + "\"");
	// }
	// tempContent = tempContent.replace("${" + key + "}", value);
	// }
	// return tempContent;
	// }
	//
	// private void updateTypePropertiesToMap() {
	// updateDocument();
	// if (depToQName != null) {
	// for (TypeModel2 model : depToQName.keySet()) {
	// String qName = depToQName.get(model);
	// String[] nsAndName = qName.split(":");
	// String xmlns = nsAndName[0];
	// String typeName = nsAndName[1];
	// freemarkerReplacement.put(qName, xmlns + ":"
	// + model.getTypeName());
	// freemarkerReplacement.put(typeName, model.getTypeName());
	// }
	// }
	// freemarkerReplacement.put(CutXSDFromWSDL.TYPE_NAME_KEY, typeName);
	//
	// }
	//
	// private void updateDocument() {
	// documentation = documentation.replace("&", "&amp;");
	// documentation = documentation.replace("<", "&lt;");
	// documentation = documentation.replace(">", "&gt;");
	// documentation = documentation.replace("\"", "&quot;");
	// documentation = documentation.replace("'", "&apos;");
	// freemarkerReplacement.put(CutXSDFromWSDL.TYPE_DOCUMENT_KEY,
	// documentation);
	// }

	public boolean isNeedToImport() {
		return typelibRefName == null && typelibRefNamespace == null;
	}

	public boolean isTypeLibraryType() {
		return typelibRefName != null && typelibRefNamespace != null;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public String getNamespace() {
		return namespace;
	}

	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}

	public String getTypeLibName() {
		return typeLibName;
	}

	public void setTypeLibName(String typeLibName) {
		this.typeLibName = typeLibName;
	}

	public List<String> getErrors() {
		return errors;
	}

	public void setErrors(List<String> errors) {
		this.errors = errors;
	}

	public Map<String, String> getNsMappingSchema() {
		return nsMappingSchema;
	}

	public void setNsMappingSchema(Map<String, String> nsMappingSchema) {
		this.nsMappingSchema = nsMappingSchema;
	}

	public String getDocumentation() {
		return documentation;
	}

	public void setDocumentation(String documentation) {
		this.documentation = documentation;
	}

	public void setWarning(List<String> warnings) {
		this.warnings = warnings;
	}

	public boolean isHasImportError() {
		return hasImportError;
	}

	public void setHasImportError(boolean hasImportError) {
		this.hasImportError = hasImportError;
	}

	public String getImportError() {
		return importError;
	}

	public void setImportError(String importError) {
		this.importError = importError;
	}

	public void addWarning(String warning) {
		if (warnings == null) {
			warnings = new ArrayList<String>();
		}
		warnings.add(warning);
	}

	public void addError(String error) {
		if (errors == null) {
			errors = new ArrayList<String>();
		}
		errors.add(error);
	}

	public void addErrors(Collection<String> error) {
		if (errors == null) {
			errors = new ArrayList<String>();
		}
		errors.addAll(error);
	}

	public void addWarnings(Collection<String> warning) {
		if (warnings == null) {
			warnings = new ArrayList<String>();
		}
		warnings.addAll(warning);
	}

	public List<String> getWarnings() {
		return warnings;
	}

	public boolean hasError() {
		return errors != null && errors.size() > 0;
	}

	public boolean hasWarning() {
		return warnings != null && warnings.size() > 0;
	}

	public boolean isSelected() {
		return this.selected;
	}

	public void setSelected(boolean select) {
		this.selected = select;
	}

	public String getUnSupportedReason() {
		StringBuilder sb = new StringBuilder("Type \"" + typeName
				+ "\" has the following errors:\r\n");
		for (String error : errors) {
			sb.append("\t" + error + "\r\n");
		}
		return sb.toString();
	}

	public void addInternalDependency(TypeModel model) {
		if (internalDependencies == null) {
			internalDependencies = new ArrayList<TypeModel>();
		}
		internalDependencies.add(model);
	}

	public XSDContentList getTypeContent() {
		return typeContent;
	}

	public void setTypeContent(XSDContentList typeContent) {
		this.typeContent = typeContent;
	}

	public List<TypeModel> getInternalDependencies() {
		return internalDependencies;
	}

	public void setInternalDependencies(List<TypeModel> internalDependencies) {
		this.internalDependencies = internalDependencies;
	}

	public String getTypelibRefName() {
		return typelibRefName;
	}

	public void setTypelibRefName(String typelibRefName) {
		this.typelibRefName = typelibRefName;
	}

	public String getTypelibRefNamespace() {
		return typelibRefNamespace;
	}

	public void setTypelibRefNamespace(String typelibRefNamespace) {
		this.typelibRefNamespace = typelibRefNamespace;
	}

	public String getSchemaQName() {
		return schemaQName;
	}

	public void setSchemaQName(String schemaQName) {
		this.schemaQName = schemaQName;
	}

	public String getSchemaXMLNS() {
		return schemaXMLNS;
	}

	public void setSchemaXMLNS(String schemaXMLNS) {
		this.schemaXMLNS = schemaXMLNS;
	}

	public void setWarnings(List<String> warnings) {
		this.warnings = warnings;
	}

}
