package org.ebayopensource.turmeric.eclipse.typelibrary.utils.importtypes.xsdpiece;

// TODO: Auto-generated Javadoc
/**
 * represent document node in XSD.
 *
 * @author mzang
 */
public class TypeDocumentationPiece extends TypeRelatedContent {

	/**
	 * constructor with QName.
	 *
	 * @param qName the q name
	 */
	public TypeDocumentationPiece(String qName) {
		this.qName = qName;
	}

	/**
	 * default constructor.
	 */
	public TypeDocumentationPiece() {
	}

	/**
	 * content of document.
	 *
	 * @return the content
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
