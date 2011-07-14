package org.ebayopensource.turmeric.eclipse.typelibrary.utils.importtypes.xsdpiece;


public class TypeDocumentationPiece extends TypeRelatedContent {

	public TypeDocumentationPiece(String qName) {
		this.qName = qName;
	}

	public TypeDocumentationPiece() {
	}

	@Override
	public String getContent() {
		String documentation = model.getDocumentation();
		documentation = documentation.replace("&", "&amp;");
		documentation = documentation.replace("<", "&lt;");
		documentation = documentation.replace(">", "&gt;");
		documentation = documentation.replace("\"", "&quot;");
		documentation = documentation.replace("'", "&apos;");
		return documentation;
	}

}
