package org.ebayopensource.turmeric.eclipse.typelibrary.utils.importtypes.xsdpiece;

// TODO: Auto-generated Javadoc
/**
 * represent type QName.
 *
 * @author mzang
 */
public class TypeQNamePiece extends TypeRelatedContent {

	/** The xmlns. */
	private String xmlns;

	/** The literal type name. */
	private String literalTypeName = null;

	/**
	 * create instance.
	 *
	 * @param qName the q name
	 */
	public TypeQNamePiece(String qName) {
		this.qName = qName;
	}

	/**
	 * get XMLNS.
	 *
	 * @return the xmlns
	 */
	public String getXmlns() {
		return xmlns;
	}

	/**
	 * set XMLNS.
	 *
	 * @param xmlns the new xmlns
	 */
	public void setXmlns(String xmlns) {
		this.xmlns = xmlns;
	}

	/**
	 * get literal type name.
	 *
	 * @return the literal type name
	 */
	public String getLiteralTypeName() {
		return literalTypeName;
	}

	/**
	 * set literal type name.
	 *
	 * @param literalTypeName the new literal type name
	 */
	public void setLiteralTypeName(String literalTypeName) {
		this.literalTypeName = literalTypeName;
	}

	/**
	 * get literal content.
	 *
	 * @return the content
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
