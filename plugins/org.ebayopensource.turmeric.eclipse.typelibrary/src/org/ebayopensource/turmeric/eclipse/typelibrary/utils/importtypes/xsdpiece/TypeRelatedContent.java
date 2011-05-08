package org.ebayopensource.turmeric.eclipse.typelibrary.utils.importtypes.xsdpiece;

import org.ebayopensource.turmeric.eclipse.typelibrary.utils.importtypes.TypeModel;

/**
 * The Class TypeRelatedContent.
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
	 * Instantiates a new type related content.
	 */
	public TypeRelatedContent() {
	}

	/**
	 * Gets the model.
	 *
	 * @return the model
	 */
	public TypeModel getModel() {
		return model;
	}

	/**
	 * Sets the model.
	 *
	 * @param model the new model
	 */
	public void setModel(TypeModel model) {
		this.model = model;
	}

	/**
	 * Gets the q name.
	 *
	 * @return the q name
	 */
	public String getqName() {
		return qName;
	}

	/**
	 * Sets the q name.
	 *
	 * @param qName the new q name
	 */
	public void setqName(String qName) {
		this.qName = qName;
	}

}
