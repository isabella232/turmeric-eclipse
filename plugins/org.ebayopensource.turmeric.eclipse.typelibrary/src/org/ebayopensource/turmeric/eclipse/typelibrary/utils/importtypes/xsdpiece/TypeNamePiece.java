package org.ebayopensource.turmeric.eclipse.typelibrary.utils.importtypes.xsdpiece;

import org.ebayopensource.turmeric.eclipse.typelibrary.utils.importtypes.TypeModel;


public class TypeNamePiece extends TypeRelatedContent {

	public TypeNamePiece(String qName) {
		this.qName = qName;
	}
	
	public TypeNamePiece(String qName, TypeModel model) {
		this.qName = qName;
		this.model = model;
	}
	
	public TypeNamePiece() {
	}

	@Override
	public String getContent() {
		return model.getTypeName();
	}

}
