package org.ebayopensource.turmeric.eclipse.typelibrary.utils.importtypes.xsdpiece;

import org.ebayopensource.turmeric.eclipse.typelibrary.utils.importtypes.TypeModel;

// TODO: Auto-generated Javadoc
/**
 * represent the type name.
 *
 * @author mzang
 */
public class TypeNamePiece extends TypeRelatedContent {

	/**
	 * create instance.
	 *
	 * @param qName the q name
	 */
	public TypeNamePiece(String qName) {
		this.qName = qName;
	}

	/**
	 * create instance.
	 *
	 * @param qName the q name
	 * @param model the model
	 */
	public TypeNamePiece(String qName, TypeModel model) {
		this.qName = qName;
		this.model = model;
	}

	/**
	 * create default instance.
	 *
	 */
	public TypeNamePiece() {
	}

	/**
	 * type name.
	 *
	 * @return the content
	 */
	@Override
	public String getContent() {
		return model.getTypeName();
	}

}
