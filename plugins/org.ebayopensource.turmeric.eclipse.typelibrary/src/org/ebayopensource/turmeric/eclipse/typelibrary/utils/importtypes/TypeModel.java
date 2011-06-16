package org.ebayopensource.turmeric.eclipse.typelibrary.utils.importtypes;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.ebayopensource.turmeric.eclipse.typelibrary.utils.importtypes.xsdpiece.XSDContentList;

public class TypeModel implements Comparable<TypeModel> {

	private String typeName;

	private String namespace;

	private String originalNamespace;

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
		this.originalNamespace = namespace;
		this.nsMappingSchema = nsMappingSchema;
		this.typeContent = typeContent;
		this.documentation = documentation;
		// type from type library
		this.typelibRefName = typelibName;
		this.typelibRefNamespace = typelibNamespace;
	}

	public String getOriginalNamespace() {
		return originalNamespace;
	}

	public boolean hasInternalDependencies() {
		return internalDependencies != null && internalDependencies.size() > 0;
	}

	public boolean isNeedToImport() {
		return typelibRefName == null && typelibRefNamespace == null;
	}

	public boolean isTypeLibraryType() {
		return typelibRefName != null && typelibRefNamespace != null;
	}

	public boolean isTypeLibrarySourceInvalidated() {
		return (typelibRefName == null) == (typelibRefNamespace == null);
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

	public boolean hasImportError() {
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

	public void addErrors(List<String> error) {
		if (errors == null) {
			errors = new ArrayList<String>();
		}
		mergeSameItems(errors, error);
	}

	public void addWarnings(List<String> warning) {
		if (warnings == null) {
			warnings = new ArrayList<String>();
		}
		mergeSameItems(warnings, warning);
	}

	private void mergeSameItems(Collection<String> target, List<String> source) {
		if (source.size() == 0) {
			return;
		}
		Collections.sort(source);
		String preItem = source.get(0);
		int counter = 1;
		int size = source.size();
		for (int i = 1; i < size; i++) {
			String curItem = source.get(i);
			if (curItem == null) {
				continue;
			}
			if (preItem.equals(curItem)) {
				counter++;
			} else {
				if (counter == 1) {
					target.add(preItem);
				} else if (counter == 2) {
					target.add(preItem + " (occurs twice)");
				} else {
					target.add(preItem + " (occurs " + counter + " times)");
				}
				preItem = curItem;
				counter = 1;
			}
		}
		if (counter == 1) {
			target.add(preItem);
		} else if (counter == 2) {
			target.add(preItem + " (occurs twice)");
		} else {
			target.add(preItem + " (occurs " + counter + " times)");
		}
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

	@Override
	public int compareTo(TypeModel o) {
		return this.hashCode() - o.hashCode();
	}

}
