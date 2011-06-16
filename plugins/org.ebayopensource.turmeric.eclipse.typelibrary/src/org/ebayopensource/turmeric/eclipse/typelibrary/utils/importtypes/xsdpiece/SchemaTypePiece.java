package org.ebayopensource.turmeric.eclipse.typelibrary.utils.importtypes.xsdpiece;

public class SchemaTypePiece implements IXSDPiece {

	private String content;
	
	private String namespace;

	public SchemaTypePiece(String content, String namespace) {
		this.content = content;
		this.namespace = namespace;
	}

	@Override
	public String getContent() {
		return content;
	}
	
	public String getNamespace(){
		return namespace;
	}

}
