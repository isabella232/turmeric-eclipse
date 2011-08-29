package org.ebayopensource.turmeric.eclipse.typelibrary.utils.importtypes.xsdpiece;

import org.ebayopensource.turmeric.eclipse.typelibrary.utils.importtypes.TypeModel;

/**
 * represent type name piece
 * @author mzang
 *
 */
public class TypeNamespacePiece extends TypeRelatedContent {

	/**
	 * create instance
	 * @param qName
	 */
	public TypeNamespacePiece(String qName) {
		this.qName = qName;
	}
	
	/**
	 * create instance
	 * @param qName
	 * @param model
	 */
	public TypeNamespacePiece(String qName, TypeModel model) {
		this.qName = qName;
		this.model = model;
	}
	
	/**
	 * create default instance
	 */
	public TypeNamespacePiece() {
	}

	/**
	 * return type namespace
	 */
	@Override
	public String getContent() {
		return model.getNamespace();
	}

}
