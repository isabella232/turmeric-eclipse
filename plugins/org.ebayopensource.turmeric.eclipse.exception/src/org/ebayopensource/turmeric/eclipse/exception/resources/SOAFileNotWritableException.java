/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
/**
 * 
 */
package org.ebayopensource.turmeric.eclipse.exception.resources;

import java.io.File;

import org.ebayopensource.turmeric.eclipse.exception.AbstractSOAException;


/**
 * The Class SOAFileNotWritableException.
 *
 * @author yayu
 */
public class SOAFileNotWritableException extends AbstractSOAException {
	private File file;

	/**
	 * 
	 */
	private static final long serialVersionUID = 22L;

	
	/**
	 * Instantiates a new sOA file not writable exception.
	 *
	 * @param file the file
	 */
	public SOAFileNotWritableException(File file) {	
		this.file = file;
	}
	

	/**
	 * Instantiates a new sOA file not writable exception.
	 *
	 * @param message the message
	 * @param file the file
	 */
	public SOAFileNotWritableException(String message, File file) {
		super(message);
		this.file = file;
	}

	/**
	 * Instantiates a new sOA file not writable exception.
	 *
	 * @param file the file
	 * @param cause the cause
	 */
	public SOAFileNotWritableException(File file, Throwable cause) {
		super(cause);
		this.file = file;
	}

	/**
	 * Instantiates a new sOA file not writable exception.
	 *
	 * @param cause the cause
	 */
	public SOAFileNotWritableException(Throwable cause) {
		this(null, cause);
	}

	/**
	 * Instantiates a new sOA file not writable exception.
	 *
	 * @param message the message
	 * @param file the file
	 * @param cause the cause
	 */
	public SOAFileNotWritableException(String message, File file,
			Throwable cause) {
		super(message, cause);
		this.file = file;
	}

	/* (non-Javadoc)
	 * @see org.ebayopensource.turmeric.eclipse.exception.AbstractSOAException#getMessage()
	 */
	@Override
	public String getMessage() {
		final StringBuffer buf = new StringBuffer();
		buf.append(super.getMessage()==null?"":super.getMessage());
		if (this.file != null) {
			buf.append("\n" + file.getAbsolutePath());
			buf
					.append("\n is read only or is not accessible, Please make it available and writable to perform this operation.");
		}

		return buf.toString();
	}

	/**
	 * Gets the file.
	 *
	 * @return the file
	 */
	public File getFile() {
		return this.file;
	}

}
