package org.ebayopensource.turmeric.eclipse.typelibrary.utils.importtypes.xsdpiece;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * represent a whole XSD file.
 * @author mzang
 *
 */
public class XSDContentList {

	private List<IXSDPiece> content = new LinkedList<IXSDPiece>();

	/**
	 * create instance
	 */
	public XSDContentList() {
		super();
	}

	/**
	 * add a String piece to XSD
	 * @param str
	 */
	public void add(String str) {
		content.add(new StringPiece(str));
	}

	/**
	 * add a piece to XSD
	 * @param str
	 */
	public void add(IXSDPiece piece) {
		content.add(piece);
	}

	/**
	 * generate XSD content
	 */
	public String toString() {
		StringBuilder xsd = new StringBuilder();
		for (IXSDPiece piece : content) {
			xsd.append(piece.getContent());
		}
		return xsd.toString();
	}

	/**
	 * get piece count
	 * @return
	 */
	public int size() {
		return content.size();
	}

	/**
	 * insert pieces to a location
	 * @param location
	 * @param pieces
	 */
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

	/**
	 * insert pieces to a location
	 * @param location
	 * @param listPieces
	 */
	public void insert(int location, List<IXSDPiece> listPieces) {
		content.addAll(location, listPieces);
	}

	/**
	 * get all XSD pieces
	 * @return
	 */
	public List<IXSDPiece> getContentList() {
		return content;
	}

}
