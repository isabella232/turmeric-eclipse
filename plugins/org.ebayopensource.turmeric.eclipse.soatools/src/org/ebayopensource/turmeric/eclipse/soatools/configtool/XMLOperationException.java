package org.ebayopensource.turmeric.eclipse.soatools.configtool;


/**
 * The Class XMLOperationException.
 */
public class XMLOperationException extends Exception {

	private static final long serialVersionUID = -3169510624893248388L;

	/**
	 * Instantiates a new xML operation exception.
	 *
	 * @param e the e
	 */
	public XMLOperationException(Exception e) {
		super(e);
	}

	/**
	 * Instantiates a new xML operation exception.
	 *
	 * @param message the message
	 */
	public XMLOperationException(String message) {
		super(message);
	}

}
