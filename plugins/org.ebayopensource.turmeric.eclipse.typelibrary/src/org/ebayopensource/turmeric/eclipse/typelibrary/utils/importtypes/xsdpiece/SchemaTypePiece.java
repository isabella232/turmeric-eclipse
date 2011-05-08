package org.ebayopensource.turmeric.eclipse.typelibrary.utils.importtypes.xsdpiece;

/**
 * The Class SchemaTypePiece.
 */
public class SchemaTypePiece implements IXSDPiece {

	private String content;
	
	private String namespace;

	/**
	 * Instantiates a new schema type piece.
	 *
	 * @param content the content
	 * @param namespace the namespace
	 */
	public SchemaTypePiece(String content, String namespace) {
		this.content = content;
		this.namespace = namespace;
	}

	/* (non-Javadoc)
	 * @see org.ebayopensource.turmeric.eclipse.typelibrary.utils.importtypes.xsdpiece.IXSDPiece#getContent()
	 */
	@Override
	public String getContent() {
		return content;
	}
	
	/**
	 * Gets the namespace.
	 *
	 * @return the namespace
	 */
	public String getNamespace(){
		return namespace;
	}

}
