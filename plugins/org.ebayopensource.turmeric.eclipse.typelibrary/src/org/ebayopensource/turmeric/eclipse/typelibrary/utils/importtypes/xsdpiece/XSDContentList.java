package org.ebayopensource.turmeric.eclipse.typelibrary.utils.importtypes.xsdpiece;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class XSDContentList {

	private List<IXSDPiece> content = new LinkedList<IXSDPiece>();

	public XSDContentList() {
		super();
	}

	public void add(String str) {
		content.add(new StringPiece(str));
	}

	public void add(IXSDPiece piece) {
		content.add(piece);
	}

	public String toString() {
		StringBuilder xsd = new StringBuilder();
		for (IXSDPiece piece : content) {
			xsd.append(piece.getContent());
		}
		return xsd.toString();
	}

	public int size() {
		return content.size();
	}

	public void insert(int location, Object... pieces) {
		List<IXSDPiece> listPieces = new ArrayList<IXSDPiece>();
		for (Object objPiece : pieces) {
			IXSDPiece piece = null;
			if (objPiece instanceof String) {
				piece = new StringPiece((String) objPiece);
			} else {
				piece = (IXSDPiece) objPiece;
			}
			listPieces.add(piece);
		}
		insert(location, listPieces);
	}

	public void insert(int location, List<IXSDPiece> listPieces) {
		content.addAll(location, listPieces);
	}

	public List<IXSDPiece> getContentList() {
		return content;
	}

}
