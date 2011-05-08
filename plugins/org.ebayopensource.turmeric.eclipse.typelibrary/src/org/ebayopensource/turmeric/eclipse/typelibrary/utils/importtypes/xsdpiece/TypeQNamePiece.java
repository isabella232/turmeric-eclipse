package org.ebayopensource.turmeric.eclipse.typelibrary.utils.importtypes.xsdpiece;

/**
 * The Class TypeQNamePiece.
 */
public class TypeQNamePiece extends TypeRelatedContent {

	private String xmlns;

	private String literalTypeName = null;

	/**
	 * Instantiates a new type q name piece.
	 *
	 * @param qName the q name
	 */
	public TypeQNamePiece(String qName) {
		this.qName = qName;
	}

	/**
	 * Gets the xmlns.
	 *
	 * @return the xmlns
	 */
	public String getXmlns() {
		return xmlns;
	}

	/**
	 * Sets the xmlns.
	 *
	 * @param xmlns the new xmlns
	 */
	public void setXmlns(String xmlns) {
		this.xmlns = xmlns;
	}

	/**
	 * Gets the literal type name.
	 *
	 * @return the literal type name
	 */
	public String getLiteralTypeName() {
		return literalTypeName;
	}

	/**
	 * Sets the literal type name.
	 *
	 * @param literalTypeName the new literal type name
	 */
	public void setLiteralTypeName(String literalTypeName) {
		this.literalTypeName = literalTypeName;
	}

	/* (non-Javadoc)
	 * @see org.ebayopensource.turmeric.eclipse.typelibrary.utils.importtypes.xsdpiece.IXSDPiece#getContent()
	 */
	@Override
	public String getContent() {
		if (literalTypeName == null && model != null) {
			return xmlns + ":" + model.getTypeName();
		} else if (literalTypeName != null) {
			return xmlns + ":" + literalTypeName;
		} else {
			return xmlns + ":";
		}
	}

}
