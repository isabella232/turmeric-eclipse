package org.ebayopensource.turmeric.eclipse.typelibrary.utils.importtypes.xsdpiece;

/**
 * represent type QName
 * @author mzang
 *
 */
public class TypeQNamePiece extends TypeRelatedContent {

	private String xmlns;

	private String literalTypeName = null;

	/**
	 * create instance
	 * @param qName
	 */
	public TypeQNamePiece(String qName) {
		this.qName = qName;
	}

	/**
	 * get XMLNS
	 * @return
	 */
	public String getXmlns() {
		return xmlns;
	}

	/**
	 * set XMLNS
	 * @param xmlns
	 */
	public void setXmlns(String xmlns) {
		this.xmlns = xmlns;
	}

	/**
	 * get literal type name
	 * @return
	 */
	public String getLiteralTypeName() {
		return literalTypeName;
	}

	/**
	 * set literal type name
	 * @param literalTypeName
	 */
	public void setLiteralTypeName(String literalTypeName) {
		this.literalTypeName = literalTypeName;
	}

	/**
	 * get literal content
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
