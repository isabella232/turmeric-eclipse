package org.ebayopensource.turmeric.eclipse.soatools.configtool;


public class XMLOperationException extends Exception {

	private static final long serialVersionUID = -3169510624893248388L;

	public XMLOperationException(Exception e) {
		super(e);
	}

	public XMLOperationException(String message) {
		super(message);
	}

}
