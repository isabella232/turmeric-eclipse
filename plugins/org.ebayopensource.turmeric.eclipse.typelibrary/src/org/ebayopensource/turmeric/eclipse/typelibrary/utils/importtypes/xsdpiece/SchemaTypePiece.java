package org.ebayopensource.turmeric.eclipse.typelibrary.utils.importtypes.xsdpiece;

/**
 * schema type in XSD file. Could be a basic type or external type
 * @author mzang
 *
 */
public class SchemaTypePiece implements IXSDPiece {

	private String content;
	
	private String namespace;

	/**
	 * create instance
	 * @param content
	 * @param namespace
	 */
	public SchemaTypePiece(String content, String namespace) {
		this.content = content;
		this.namespace = namespace;
	}

	/**
	 * get the value
	 */
	@Override
	public String getContent() {
		return content;
	}
	
	/**
	 * get space of the type
	 * @return
	 */
	public String getNamespace(){
		return namespace;
	}

}
