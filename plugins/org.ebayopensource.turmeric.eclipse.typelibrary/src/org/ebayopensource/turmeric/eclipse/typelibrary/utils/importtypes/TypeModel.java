package org.ebayopensource.turmeric.eclipse.typelibrary.utils.importtypes;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.ebayopensource.turmeric.eclipse.typelibrary.utils.importtypes.xsdpiece.XSDContentList;

// TODO: Auto-generated Javadoc
/**
 * represent an XSD type.
 *
 * @author mzang
 */
public class TypeModel implements Comparable<TypeModel> {

	/** The type name. */
	private String typeName;

	/** The namespace. */
	private String namespace;

	/** The original namespace. */
	private String originalNamespace;

	/** The type lib name. */
	private String typeLibName;

	/** The type content. */
	private XSDContentList typeContent;

	/** The documentation. */
	private String documentation;

	/** The ns mapping schema. */
	private Map<String, String> nsMappingSchema = null;

	/** The errors. */
	private List<String> errors = null;

	/** The warnings. */
	private List<String> warnings = null;

	/*
	 * properties that only available for type library type, which is the type
	 * in wsdl but has the typeLibrarySource node. They must both have validated
	 * value or both are null.
	 */
	/** The typelib ref name. */
	private String typelibRefName = null;

	/** The typelib ref namespace. */
	private String typelibRefNamespace = null;

	// import related variables
	/** The selected. */
	private boolean selected = true;

	/** The has import error. */
	private boolean hasImportError = false;

	/** The import error. */
	private String importError = null;

	/** The internal dependencies. */
	private List<TypeModel> internalDependencies = null;

	/** The schema q name. */
	private String schemaQName = "";

	/** The schema xmlns. */
	private String schemaXMLNS = "";

	/**
	 * Instantiates a new type model.
	 */
	public TypeModel() {

	}

	/**
	 * create an instance to represent a type.
	 *
	 * @param typeName the type name
	 * @param namespace the namespace
	 * @param nsMappingSchema the ns mapping schema
	 * @param typeContent the type content
	 * @param documentation the documentation
	 * @param typelibName the typelib name
	 * @param typelibNamespace the typelib namespace
	 */
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

	/**
	 * Gets the original namespace.
	 *
	 * @return the original namespace
	 */
	public String getOriginalNamespace() {
		return originalNamespace;
	}

	/**
	 * Checks for internal dependencies.
	 *
	 * @return true, if successful
	 */
	public boolean hasInternalDependencies() {
		return internalDependencies != null && internalDependencies.size() > 0;
	}

	/**
	 * Checks if is need to import.
	 *
	 * @return true, if is need to import
	 */
	public boolean isNeedToImport() {
		return typelibRefName == null && typelibRefNamespace == null;
	}

	/**
	 * Checks if is type library type.
	 *
	 * @return true, if is type library type
	 */
	public boolean isTypeLibraryType() {
		return typelibRefName != null && typelibRefNamespace != null;
	}

	/**
	 * Checks if is type library source invalidated.
	 *
	 * @return true, if is type library source invalidated
	 */
	public boolean isTypeLibrarySourceInvalidated() {
		return (typelibRefName == null) == (typelibRefNamespace == null);
	}

	/**
	 * Gets the type name.
	 *
	 * @return the type name
	 */
	public String getTypeName() {
		return typeName;
	}

	/**
	 * Sets the type name.
	 *
	 * @param typeName the new type name
	 */
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	/**
	 * Gets the namespace.
	 *
	 * @return the namespace
	 */
	public String getNamespace() {
		return namespace;
	}

	/**
	 * Sets the namespace.
	 *
	 * @param namespace the new namespace
	 */
	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}

	/**
	 * Gets the type lib name.
	 *
	 * @return the type lib name
	 */
	public String getTypeLibName() {
		return typeLibName;
	}

	/**
	 * Sets the type lib name.
	 *
	 * @param typeLibName the new type lib name
	 */
	public void setTypeLibName(String typeLibName) {
		this.typeLibName = typeLibName;
	}

	/**
	 * Gets the errors.
	 *
	 * @return the errors
	 */
	public List<String> getErrors() {
		return errors;
	}

	/**
	 * Sets the errors.
	 *
	 * @param errors the new errors
	 */
	public void setErrors(List<String> errors) {
		this.errors = errors;
	}

	/**
	 * Gets the ns mapping schema.
	 *
	 * @return the ns mapping schema
	 */
	public Map<String, String> getNsMappingSchema() {
		return nsMappingSchema;
	}

	/**
	 * Sets the ns mapping schema.
	 *
	 * @param nsMappingSchema the ns mapping schema
	 */
	public void setNsMappingSchema(Map<String, String> nsMappingSchema) {
		this.nsMappingSchema = nsMappingSchema;
	}

	/**
	 * Gets the documentation.
	 *
	 * @return the documentation
	 */
	public String getDocumentation() {
		return documentation;
	}

	/**
	 * Sets the documentation.
	 *
	 * @param documentation the new documentation
	 */
	public void setDocumentation(String documentation) {
		this.documentation = documentation;
	}

	/**
	 * Sets the warning.
	 *
	 * @param warnings the new warning
	 */
	public void setWarning(List<String> warnings) {
		this.warnings = warnings;
	}

	/**
	 * Checks for import error.
	 *
	 * @return true, if successful
	 */
	public boolean hasImportError() {
		return hasImportError;
	}

	/**
	 * Sets the checks for import error.
	 *
	 * @param hasImportError the new checks for import error
	 */
	public void setHasImportError(boolean hasImportError) {
		this.hasImportError = hasImportError;
	}

	/**
	 * Gets the import error.
	 *
	 * @return the import error
	 */
	public String getImportError() {
		return importError;
	}

	/**
	 * Sets the import error.
	 *
	 * @param importError the new import error
	 */
	public void setImportError(String importError) {
		this.importError = importError;
	}

	/**
	 * Adds the warning.
	 *
	 * @param warning the warning
	 */
	public void addWarning(String warning) {
		if (warnings == null) {
			warnings = new ArrayList<String>();
		}
		warnings.add(warning);
	}

	/**
	 * Adds the error.
	 *
	 * @param error the error
	 */
	public void addError(String error) {
		if (errors == null) {
			errors = new ArrayList<String>();
		}
		errors.add(error);
	}

	/**
	 * Adds the errors.
	 *
	 * @param error the error
	 */
	public void addErrors(List<String> error) {
		if (errors == null) {
			errors = new ArrayList<String>();
		}
		mergeSameItems(errors, error);
	}

	/**
	 * Adds the warnings.
	 *
	 * @param warning the warning
	 */
	public void addWarnings(List<String> warning) {
		if (warnings == null) {
			warnings = new ArrayList<String>();
		}
		mergeSameItems(warnings, warning);
	}

	/**
	 * Merge same items.
	 *
	 * @param target the target
	 * @param source the source
	 */
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

	/**
	 * Gets the warnings.
	 *
	 * @return the warnings
	 */
	public List<String> getWarnings() {
		return warnings;
	}

	/**
	 * Checks for error.
	 *
	 * @return true, if successful
	 */
	public boolean hasError() {
		return errors != null && errors.size() > 0;
	}

	/**
	 * Checks for warning.
	 *
	 * @return true, if successful
	 */
	public boolean hasWarning() {
		return warnings != null && warnings.size() > 0;
	}

	/**
	 * Checks if is selected.
	 *
	 * @return true, if is selected
	 */
	public boolean isSelected() {
		return this.selected;
	}

	/**
	 * Sets the selected.
	 *
	 * @param select the new selected
	 */
	public void setSelected(boolean select) {
		this.selected = select;
	}

	/**
	 * Gets the un supported reason.
	 *
	 * @return the un supported reason
	 */
	public String getUnSupportedReason() {
		StringBuilder sb = new StringBuilder("Type \"" + typeName
				+ "\" has the following errors:\r\n");
		for (String error : errors) {
			sb.append("\t" + error + "\r\n");
		}
		return sb.toString();
	}

	/**
	 * Adds the internal dependency.
	 *
	 * @param model the model
	 */
	public void addInternalDependency(TypeModel model) {
		if (internalDependencies == null) {
			internalDependencies = new ArrayList<TypeModel>();
		}
		internalDependencies.add(model);
	}

	/**
	 * Gets the type content.
	 *
	 * @return the type content
	 */
	public XSDContentList getTypeContent() {
		return typeContent;
	}

	/**
	 * Sets the type content.
	 *
	 * @param typeContent the new type content
	 */
	public void setTypeContent(XSDContentList typeContent) {
		this.typeContent = typeContent;
	}

	/**
	 * Gets the internal dependencies.
	 *
	 * @return the internal dependencies
	 */
	public List<TypeModel> getInternalDependencies() {
		return internalDependencies;
	}

	/**
	 * Sets the internal dependencies.
	 *
	 * @param internalDependencies the new internal dependencies
	 */
	public void setInternalDependencies(List<TypeModel> internalDependencies) {
		this.internalDependencies = internalDependencies;
	}

	/**
	 * Gets the typelib ref name.
	 *
	 * @return the typelib ref name
	 */
	public String getTypelibRefName() {
		return typelibRefName;
	}

	/**
	 * Sets the typelib ref name.
	 *
	 * @param typelibRefName the new typelib ref name
	 */
	public void setTypelibRefName(String typelibRefName) {
		this.typelibRefName = typelibRefName;
	}

	/**
	 * Gets the typelib ref namespace.
	 *
	 * @return the typelib ref namespace
	 */
	public String getTypelibRefNamespace() {
		return typelibRefNamespace;
	}

	/**
	 * Sets the typelib ref namespace.
	 *
	 * @param typelibRefNamespace the new typelib ref namespace
	 */
	public void setTypelibRefNamespace(String typelibRefNamespace) {
		this.typelibRefNamespace = typelibRefNamespace;
	}

	/**
	 * Gets the schema q name.
	 *
	 * @return the schema q name
	 */
	public String getSchemaQName() {
		return schemaQName;
	}

	/**
	 * Sets the schema q name.
	 *
	 * @param schemaQName the new schema q name
	 */
	public void setSchemaQName(String schemaQName) {
		this.schemaQName = schemaQName;
	}

	/**
	 * Gets the schema xmlns.
	 *
	 * @return the schema xmlns
	 */
	public String getSchemaXMLNS() {
		return schemaXMLNS;
	}

	/**
	 * Sets the schema xmlns.
	 *
	 * @param schemaXMLNS the new schema xmlns
	 */
	public void setSchemaXMLNS(String schemaXMLNS) {
		this.schemaXMLNS = schemaXMLNS;
	}

	/**
	 * Sets the warnings.
	 *
	 * @param warnings the new warnings
	 */
	public void setWarnings(List<String> warnings) {
		this.warnings = warnings;
	}

	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(TypeModel o) {
		return this.hashCode() - o.hashCode();
	}

}
