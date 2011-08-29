package org.ebayopensource.turmeric.eclipse.typelibrary.utils.importtypes.xsdpiece;

// TODO: Auto-generated Javadoc
/**
 * literal content of XSD file.
 * @author mzang
 *
 */
public class StringPiece implements IXSDPiece {

	/** The content. */
	private String content;

	/**
	 * create instance.
	 *
	 * @param content the content
	 */
	public StringPiece(String content) {
		this.content = content;
	}

	/**
	 * get content.
	 *
	 * @return the content
	 */
	@Override
	public String getContent() {
		return content;
	}

}
