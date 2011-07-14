package org.ebayopensource.turmeric.eclipse.typelibrary.utils.importtypes.xsdpiece;

public class StringPiece implements IXSDPiece {

	private String content;

	public StringPiece(String content) {
		this.content = content;
	}

	@Override
	public String getContent() {
		return content;
	}

}
