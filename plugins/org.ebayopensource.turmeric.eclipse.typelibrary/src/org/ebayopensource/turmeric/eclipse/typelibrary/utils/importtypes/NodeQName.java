package org.ebayopensource.turmeric.eclipse.typelibrary.utils.importtypes;

/**
 * Node QName
 * @author mzang
 *
 */
public class NodeQName {
	public String namespace;
	public NS ns;
	public String localName;

	public NodeQName(String namespace, NS ns, String localName) {
		this.namespace = namespace;
		this.ns = ns;
		this.localName = localName;
	}

}

enum NS {
	WSDL, SCHEMA, OTHER
}