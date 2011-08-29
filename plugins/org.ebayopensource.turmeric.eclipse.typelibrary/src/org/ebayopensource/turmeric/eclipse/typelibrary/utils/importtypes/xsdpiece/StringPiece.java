package org.ebayopensource.turmeric.eclipse.typelibrary.utils.importtypes.xsdpiece;

/**
 * literal content of XSD file.
 * @author mzang
 *
 */
public class StringPiece implements IXSDPiece {

	private String content;

	/**
	 * create instance
	 * @param content
	 */
	public StringPiece(String content) {
		this.content = content;
	}

	/**
	 * get content
	 */
	@Override
	public String getContent() {
		return content;
	}

}
