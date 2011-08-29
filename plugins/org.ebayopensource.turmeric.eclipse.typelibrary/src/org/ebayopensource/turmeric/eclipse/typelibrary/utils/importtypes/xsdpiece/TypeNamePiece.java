package org.ebayopensource.turmeric.eclipse.typelibrary.utils.importtypes.xsdpiece;

import org.ebayopensource.turmeric.eclipse.typelibrary.utils.importtypes.TypeModel;

/**
 * represent the type name
 * 
 * @author mzang
 * 
 */
public class TypeNamePiece extends TypeRelatedContent {

	/**
	 * create instance
	 * 
	 * @param qName
	 */
	public TypeNamePiece(String qName) {
		this.qName = qName;
	}

	/**
	 * create instance
	 * 
	 * @param qName
	 */
	public TypeNamePiece(String qName, TypeModel model) {
		this.qName = qName;
		this.model = model;
	}

	/**
	 * create default instance
	 * 
	 * @param qName
	 */
	public TypeNamePiece() {
	}

	/**
	 * type name
	 */
	@Override
	public String getContent() {
		return model.getTypeName();
	}

}
