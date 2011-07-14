package org.ebayopensource.turmeric.eclipse.typelibrary.utils.importtypes.xsdpiece;

public class TypeQNamePiece extends TypeRelatedContent {

	private String xmlns;

	private String literalTypeName = null;

	public TypeQNamePiece(String qName) {
		this.qName = qName;
	}

	public String getXmlns() {
		return xmlns;
	}

	public void setXmlns(String xmlns) {
		this.xmlns = xmlns;
	}

	public String getLiteralTypeName() {
		return literalTypeName;
	}

	public void setLiteralTypeName(String literalTypeName) {
		this.literalTypeName = literalTypeName;
	}

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
