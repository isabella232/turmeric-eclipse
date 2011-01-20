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
 * @author yayu
 * 
 */
public class SOAFileNotWritableException extends AbstractSOAException {
	private File file;

	/**
	 * 
	 */
	private static final long serialVersionUID = 22L;

	
	/**
	 * @param message
	 */
	public SOAFileNotWritableException(File file) {	
		this.file = file;
	}
	

	/**
	 * @param message
	 */
	public SOAFileNotWritableException(String message, File file) {
		super(message);
		this.file = file;
	}

	/**
	 * @param cause
	 */
	public SOAFileNotWritableException(File file, Throwable cause) {
		super(cause);
		this.file = file;
	}

	public SOAFileNotWritableException(Throwable cause) {
		this(null, cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public SOAFileNotWritableException(String message, File file,
			Throwable cause) {
		super(message, cause);
		this.file = file;
	}

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

	public File getFile() {
		return this.file;
	}

}
