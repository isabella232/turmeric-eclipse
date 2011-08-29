package org.ebayopensource.turmeric.eclipse.typelibrary.utils.importtypes.xsdpiece;

import org.ebayopensource.turmeric.eclipse.typelibrary.utils.importtypes.TypeModel;

/**
 * Type related content
 * 
 * @author mzang
 * 
 */
public abstract class TypeRelatedContent implements IXSDPiece {

	protected TypeModel model;

	protected String qName;

	/**
	 * xmlns:typename or null. null means the current type.
	 * 
	 * @param qName
	 */
	public TypeRelatedContent(String qName) {
		this.qName = qName;
	}

	/**
	 * default constructor
	 */
	public TypeRelatedContent() {
	}

	/**
	 * get the type model instance related with this type.
	 * 
	 * @return
	 */
	public TypeModel getModel() {
		return model;
	}

	/**
	 * set model instance
	 * @param model
	 */
	public void setModel(TypeModel model) {
		this.model = model;
	}

	/**
	 * get the QName of the type
	 * @return
	 */
	public String getQName() {
		return qName;
	}

	/**
	 * set the QName of the type
	 * @return
	 */
	public void setqName(String qName) {
		this.qName = qName;
	}

}
