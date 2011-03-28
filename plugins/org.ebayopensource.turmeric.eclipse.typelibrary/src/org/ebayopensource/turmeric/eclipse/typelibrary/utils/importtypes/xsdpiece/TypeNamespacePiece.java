package org.ebayopensource.turmeric.eclipse.typelibrary.utils.importtypes.xsdpiece;

import org.ebayopensource.turmeric.eclipse.typelibrary.utils.importtypes.TypeModel;


public class TypeNamespacePiece extends TypeRelatedContent {

	public TypeNamespacePiece(String qName) {
		this.qName = qName;
	}
	
	public TypeNamespacePiece(String qName, TypeModel model) {
		this.qName = qName;
		this.model = model;
	}
	
	public TypeNamespacePiece() {
	}

	@Override
	public String getContent() {
		return model.getNamespace();
	}

}
