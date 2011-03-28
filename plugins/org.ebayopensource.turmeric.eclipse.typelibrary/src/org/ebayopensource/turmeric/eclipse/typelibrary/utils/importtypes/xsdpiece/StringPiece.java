package org.ebayopensource.turmeric.eclipse.typelibrary.utils.importtypes.xsdpiece;

public class StringPiece implements IXSDPiece {

	private String schemaTypeQName;

	public StringPiece(String schemaTypeQName) {
		this.schemaTypeQName = schemaTypeQName;
	}

	@Override
	public String getContent() {
		return schemaTypeQName;
	}

	public String getSchemaTypeQName() {
		return schemaTypeQName;
	}

	public void setSchemaTypeQName(String schemaTypeQName) {
		this.schemaTypeQName = schemaTypeQName;
	}

}
