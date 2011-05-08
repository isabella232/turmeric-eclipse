package org.ebayopensource.turmeric.eclipse.typelibrary.utils.importtypes.xsdpiece;

import org.ebayopensource.turmeric.eclipse.typelibrary.utils.importtypes.TypeModel;


/**
 * The Class TypeNamePiece.
 */
public class TypeNamePiece extends TypeRelatedContent {

	/**
	 * Instantiates a new type name piece.
	 *
	 * @param qName the q name
	 */
	public TypeNamePiece(String qName) {
		this.qName = qName;
	}
	
	/**
	 * Instantiates a new type name piece.
	 *
	 * @param qName the q name
	 * @param model the model
	 */
	public TypeNamePiece(String qName, TypeModel model) {
		this.qName = qName;
		this.model = model;
	}
	
	/**
	 * Instantiates a new type name piece.
	 */
	public TypeNamePiece() {
	}

	/* (non-Javadoc)
	 * @see org.ebayopensource.turmeric.eclipse.typelibrary.utils.importtypes.xsdpiece.IXSDPiece#getContent()
	 */
	@Override
	public String getContent() {
		return model.getTypeName();
	}

}
