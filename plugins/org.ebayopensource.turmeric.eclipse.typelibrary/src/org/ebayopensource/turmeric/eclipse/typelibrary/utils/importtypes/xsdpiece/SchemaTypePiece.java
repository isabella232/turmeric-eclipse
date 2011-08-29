package org.ebayopensource.turmeric.eclipse.typelibrary.utils.importtypes.xsdpiece;

// TODO: Auto-generated Javadoc
/**
 * schema type in XSD file. Could be a basic type or external type
 * @author mzang
 *
 */
public class SchemaTypePiece implements IXSDPiece {

	/** The content. */
	private String content;
	
	/** The namespace. */
	private String namespace;

	/**
	 * create instance.
	 *
	 * @param content the content
	 * @param namespace the namespace
	 */
	public SchemaTypePiece(String content, String namespace) {
		this.content = content;
		this.namespace = namespace;
	}

	/**
	 * get the value.
	 *
	 * @return the content
	 */
	@Override
	public String getContent() {
		return content;
	}
	
	/**
	 * get space of the type.
	 *
	 * @return the namespace
	 */
	public String getNamespace(){
		return namespace;
	}

}
