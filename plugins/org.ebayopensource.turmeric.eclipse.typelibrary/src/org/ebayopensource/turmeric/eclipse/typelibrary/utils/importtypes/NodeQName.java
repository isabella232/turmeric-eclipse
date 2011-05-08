package org.ebayopensource.turmeric.eclipse.typelibrary.utils.importtypes;

/**
 * The Class NodeQName.
 */
public class NodeQName {
	
	/** The namespace. */
	public String namespace;
	
	/** The ns. */
	public NS ns;
	
	/** The local name. */
	public String localName;

	/**
	 * Instantiates a new node q name.
	 *
	 * @param namespace the namespace
	 * @param ns the ns
	 * @param localName the local name
	 */
	public NodeQName(String namespace, NS ns, String localName) {
		this.namespace = namespace;
		this.ns = ns;
		this.localName = localName;
	}

}

enum NS {
	WSDL, SCHEMA, OTHER
}