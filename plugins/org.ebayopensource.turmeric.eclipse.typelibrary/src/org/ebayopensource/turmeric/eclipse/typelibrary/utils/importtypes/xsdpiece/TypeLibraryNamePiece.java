package org.ebayopensource.turmeric.eclipse.typelibrary.utils.importtypes.xsdpiece;

import org.ebayopensource.turmeric.eclipse.typelibrary.utils.importtypes.TypeModel;


public class TypeLibraryNamePiece extends TypeRelatedContent {

	public TypeLibraryNamePiece(String qName) {
		this.qName = qName;
	}
	
	public TypeLibraryNamePiece(String qName, TypeModel model) {
		this.qName = qName;
		this.model = model;
	}
	
	public TypeLibraryNamePiece() {
	}

	@Override
	public String getContent() {
		return model.getTypeLibName();
	}

}
