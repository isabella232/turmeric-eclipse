package org.ebayopensource.turmeric.eclipse.typelibrary.utils.importtypes.xsdpiece;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * The Class XSDContentList.
 */
public class XSDContentList {

	private List<IXSDPiece> content = new LinkedList<IXSDPiece>();

	/**
	 * Instantiates a new xSD content list.
	 */
	public XSDContentList() {
		super();
	}

	/**
	 * Adds the.
	 *
	 * @param str the str
	 */
	public void add(String str) {
		content.add(new StringPiece(str));
	}

	/**
	 * Adds the.
	 *
	 * @param piece the piece
	 */
	public void add(IXSDPiece piece) {
		content.add(piece);
	}

	/**
	 * {@inheritDoc}
	 */
	public String toString() {
		StringBuilder xsd = new StringBuilder();
		for (IXSDPiece piece : content) {
			xsd.append(piece.getContent());
		}
		return xsd.toString();
	}

	/**
	 * Size.
	 *
	 * @return the int
	 */
	public int size() {
		return content.size();
	}

	/**
	 * Insert.
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
	 * Insert.
	 *
	 * @param location the location
	 * @param listPieces the list pieces
	 */
	public void insert(int location, List<IXSDPiece> listPieces) {
		content.addAll(location, listPieces);
	}

	/**
	 * Gets the content list.
	 *
	 * @return the content list
	 */
	public List<IXSDPiece> getContentList() {
		return content;
	}

}
