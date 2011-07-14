package org.ebayopensource.turmeric.eclipse.typelibrary.utils.importtypes.xsdpiece;

import org.ebayopensource.turmeric.eclipse.typelibrary.utils.importtypes.TypeModel;

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

	public TypeRelatedContent() {
	}

	public TypeModel getModel() {
		return model;
	}

	public void setModel(TypeModel model) {
		this.model = model;
	}

	public String getQName() {
		return qName;
	}

	public void setqName(String qName) {
		this.qName = qName;
	}

}
