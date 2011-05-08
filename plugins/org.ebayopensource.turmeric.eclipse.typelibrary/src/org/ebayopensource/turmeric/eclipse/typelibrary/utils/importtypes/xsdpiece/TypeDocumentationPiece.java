package org.ebayopensource.turmeric.eclipse.typelibrary.utils.importtypes.xsdpiece;


/**
 * The Class TypeDocumentationPiece.
 */
public class TypeDocumentationPiece extends TypeRelatedContent {

	/**
	 * Instantiates a new type documentation piece.
	 *
	 * @param qName the q name
	 */
	public TypeDocumentationPiece(String qName) {
		this.qName = qName;
	}

	/**
	 * Instantiates a new type documentation piece.
	 */
	public TypeDocumentationPiece() {
	}

	/* (non-Javadoc)
	 * @see org.ebayopensource.turmeric.eclipse.typelibrary.utils.importtypes.xsdpiece.IXSDPiece#getContent()
	 */
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
