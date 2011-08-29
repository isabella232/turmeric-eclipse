package org.ebayopensource.turmeric.eclipse.typelibrary.utils.importtypes.xsdpiece;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

// TODO: Auto-generated Javadoc
/**
 * represent a whole XSD file.
 * @author mzang
 *
 */
public class XSDContentList {

	/** The content. */
	private List<IXSDPiece> content = new LinkedList<IXSDPiece>();

	/**
	 * create instance.
	 */
	public XSDContentList() {
		super();
	}

	/**
	 * add a String piece to XSD.
	 *
	 * @param str the str
	 */
	public void add(String str) {
		content.add(new StringPiece(str));
	}

	/**
	 * add a piece to XSD.
	 *
	 * @param piece the piece
	 */
	public void add(IXSDPiece piece) {
		content.add(piece);
	}

	/**
	 * generate XSD content.
	 *
	 * @return the string
	 */
	public String toString() {
		StringBuilder xsd = new StringBuilder();
		for (IXSDPiece piece : content) {
			xsd.append(piece.getContent());
		}
		return xsd.toString();
	}

	/**
	 * get piece count.
	 *
	 * @return the int
	 */
	public int size() {
		return content.size();
	}

	/**
	 * insert pieces to a location.
	 *
	 * @param location the location
	 * @param pieces the pieces
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
	 * insert pieces to a location.
	 *
	 * @param location the location
	 * @param listPieces the list pieces
	 */
	public void insert(int location, List<IXSDPiece> listPieces) {
		content.addAll(location, listPieces);
	}

	/**
	 * get all XSD pieces.
	 *
	 * @return the content list
	 */
	public List<IXSDPiece> getContentList() {
		return content;
	}

}
