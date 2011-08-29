package org.ebayopensource.turmeric.eclipse.typelibrary.utils.importtypes.xsdpiece;

/**
 * represent document node in XSD
 * 
 * @author mzang
 * 
 */
public class TypeDocumentationPiece extends TypeRelatedContent {

	/**
	 * constructor with QName
	 * 
	 * @param qName
	 */
	public TypeDocumentationPiece(String qName) {
		this.qName = qName;
	}

	/**
	 * default constructor
	 */
	public TypeDocumentationPiece() {
	}

	/**
	 * content of document.
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
