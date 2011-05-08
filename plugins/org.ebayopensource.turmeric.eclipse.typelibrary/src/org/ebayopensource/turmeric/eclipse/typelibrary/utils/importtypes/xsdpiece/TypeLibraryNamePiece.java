package org.ebayopensource.turmeric.eclipse.typelibrary.utils.importtypes.xsdpiece;

import org.ebayopensource.turmeric.eclipse.typelibrary.utils.importtypes.TypeModel;


/**
 * The Class TypeLibraryNamePiece.
 */
public class TypeLibraryNamePiece extends TypeRelatedContent {

	/**
	 * Instantiates a new type library name piece.
	 *
	 * @param qName the q name
	 */
	public TypeLibraryNamePiece(String qName) {
		this.qName = qName;
	}
	
	/**
	 * Instantiates a new type library name piece.
	 *
	 * @param qName the q name
	 * @param model the model
	 */
	public TypeLibraryNamePiece(String qName, TypeModel model) {
		this.qName = qName;
		this.model = model;
	}
	
	/**
	 * Instantiates a new type library name piece.
	 */
	public TypeLibraryNamePiece() {
	}

	/* (non-Javadoc)
	 * @see org.ebayopensource.turmeric.eclipse.typelibrary.utils.importtypes.xsdpiece.IXSDPiece#getContent()
	 */
	@Override
	public String getContent() {
		return model.getTypeLibName();
	}

}
