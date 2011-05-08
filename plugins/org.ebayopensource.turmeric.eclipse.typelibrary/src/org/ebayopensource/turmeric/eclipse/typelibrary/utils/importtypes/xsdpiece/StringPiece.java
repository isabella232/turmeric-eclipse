package org.ebayopensource.turmeric.eclipse.typelibrary.utils.importtypes.xsdpiece;

/**
 * The Class StringPiece.
 */
public class StringPiece implements IXSDPiece {

	private String schemaTypeQName;

	/**
	 * Instantiates a new string piece.
	 *
	 * @param schemaTypeQName the schema type q name
	 */
	public StringPiece(String schemaTypeQName) {
		this.schemaTypeQName = schemaTypeQName;
	}

	/* (non-Javadoc)
	 * @see org.ebayopensource.turmeric.eclipse.typelibrary.utils.importtypes.xsdpiece.IXSDPiece#getContent()
	 */
	@Override
	public String getContent() {
		return schemaTypeQName;
	}

	/**
	 * Gets the schema type q name.
	 *
	 * @return the schema type q name
	 */
	public String getSchemaTypeQName() {
		return schemaTypeQName;
	}

	/**
	 * Sets the schema type q name.
	 *
	 * @param schemaTypeQName the new schema type q name
	 */
	public void setSchemaTypeQName(String schemaTypeQName) {
		this.schemaTypeQName = schemaTypeQName;
	}

}
