package org.ebayopensource.turmeric.eclipse.typelibrary.utils.importtypes.xsdpiece;

import org.ebayopensource.turmeric.eclipse.typelibrary.utils.importtypes.TypeModel;

// TODO: Auto-generated Javadoc
/**
 * represent type name piece.
 *
 * @author mzang
 */
public class TypeNamespacePiece extends TypeRelatedContent {

	/**
	 * create instance.
	 *
	 * @param qName the q name
	 */
	public TypeNamespacePiece(String qName) {
		this.qName = qName;
	}
	
	/**
	 * create instance.
	 *
	 * @param qName the q name
	 * @param model the model
	 */
	public TypeNamespacePiece(String qName, TypeModel model) {
		this.qName = qName;
		this.model = model;
	}
	
	/**
	 * create default instance.
	 */
	public TypeNamespacePiece() {
	}

	/**
	 * return type namespace.
	 *
	 * @return the content
	 */
	@Override
	public String getContent() {
		return model.getNamespace();
	}

}
