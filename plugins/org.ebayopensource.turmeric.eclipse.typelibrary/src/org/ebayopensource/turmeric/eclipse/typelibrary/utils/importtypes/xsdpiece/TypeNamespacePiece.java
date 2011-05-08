package org.ebayopensource.turmeric.eclipse.typelibrary.utils.importtypes.xsdpiece;

import org.ebayopensource.turmeric.eclipse.typelibrary.utils.importtypes.TypeModel;


/**
 * The Class TypeNamespacePiece.
 */
public class TypeNamespacePiece extends TypeRelatedContent {

	/**
	 * Instantiates a new type namespace piece.
	 *
	 * @param qName the q name
	 */
	public TypeNamespacePiece(String qName) {
		this.qName = qName;
	}
	
	/**
	 * Instantiates a new type namespace piece.
	 *
	 * @param qName the q name
	 * @param model the model
	 */
	public TypeNamespacePiece(String qName, TypeModel model) {
		this.qName = qName;
		this.model = model;
	}
	
	/**
	 * Instantiates a new type namespace piece.
	 */
	public TypeNamespacePiece() {
	}

	/* (non-Javadoc)
	 * @see org.ebayopensource.turmeric.eclipse.typelibrary.utils.importtypes.xsdpiece.IXSDPiece#getContent()
	 */
	@Override
	public String getContent() {
		return model.getNamespace();
	}

}
