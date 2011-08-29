package org.ebayopensource.turmeric.eclipse.typelibrary.utils.importtypes.xsdpiece;

import org.ebayopensource.turmeric.eclipse.typelibrary.utils.importtypes.TypeModel;

// TODO: Auto-generated Javadoc
/**
 * Type related content.
 *
 * @author mzang
 */
public abstract class TypeRelatedContent implements IXSDPiece {

	/** The model. */
	protected TypeModel model;

	/** The q name. */
	protected String qName;

	/**
	 * xmlns:typename or null. null means the current type.
	 *
	 * @param qName the q name
	 */
	public TypeRelatedContent(String qName) {
		this.qName = qName;
	}

	/**
	 * default constructor.
	 */
	public TypeRelatedContent() {
	}

	/**
	 * get the type model instance related with this type.
	 *
	 * @return the model
	 */
	public TypeModel getModel() {
		return model;
	}

	/**
	 * set model instance.
	 *
	 * @param model the new model
	 */
	public void setModel(TypeModel model) {
		this.model = model;
	}

	/**
	 * get the QName of the type.
	 *
	 * @return the q name
	 */
	public String getQName() {
		return qName;
	}

	/**
	 * set the QName of the type.
	 *
	 * @param qName the new q name
	 */
	public void setqName(String qName) {
		this.qName = qName;
	}

}
