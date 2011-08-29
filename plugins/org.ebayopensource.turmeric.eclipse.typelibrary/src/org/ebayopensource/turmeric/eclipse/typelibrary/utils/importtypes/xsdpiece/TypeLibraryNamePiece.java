package org.ebayopensource.turmeric.eclipse.typelibrary.utils.importtypes.xsdpiece;

import org.ebayopensource.turmeric.eclipse.typelibrary.utils.importtypes.TypeModel;

/**
 * represent the type lib name
 * 
 * @author mzang
 * 
 */
public class TypeLibraryNamePiece extends TypeRelatedContent {

	/**
	 * creat instance
	 * 
	 * @param qName
	 */
	public TypeLibraryNamePiece(String qName) {
		this.qName = qName;
	}

	/**
	 * create instance
	 * 
	 * @param qName
	 * @param model
	 */
	public TypeLibraryNamePiece(String qName, TypeModel model) {
		this.qName = qName;
		this.model = model;
	}

	/**
	 * default constructor
	 */
	public TypeLibraryNamePiece() {
	}

	/**
	 * get content
	 */
	@Override
	public String getContent() {
		return model.getTypeLibName();
	}

}
