/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.mylyn.wikitext.confluence.core.tasks;

import java.io.Writer;
import java.util.regex.Pattern;

import org.eclipse.mylyn.wikitext.core.parser.builder.HtmlDocumentBuilder;
import org.eclipse.mylyn.wikitext.core.util.DefaultXmlStreamWriter;
import org.eclipse.mylyn.wikitext.core.util.XmlStreamWriter;

public class ConfluenceHtmlDocumentBuilder extends HtmlDocumentBuilder {

	public ConfluenceHtmlDocumentBuilder(Writer out) {
		super(out);
	}

	public ConfluenceHtmlDocumentBuilder(Writer out, boolean formatting) {
		super(formatting ? createFormattingXmlStreamWriter(out) : new DefaultXmlStreamWriter(out));
	}	
	
	public ConfluenceHtmlDocumentBuilder(XmlStreamWriter writer) {
		super(writer);
	}
	
	@Override
	protected void emitAnchorHref(String href) {
		writer.writeAttribute("href", makeUrlAbsolute(href)); //$NON-NLS-1$
	}
	
	private static final Pattern ABSOLUTE_URL_PATTERN = Pattern.compile("[a-zA-Z]{3,8}://?.*"); //$NON-NLS-1$
	
	protected String makeUrlAbsolute(String url) {
		
		if (ABSOLUTE_URL_PATTERN.matcher(url).matches()) {
			return url;
		}
		if (url.startsWith("#")) { //$NON-NLS-1$
			return url;
		}
		if (url.endsWith(".jpg") || url.endsWith(".png") || url.endsWith(".gif") || url.endsWith(".JPG") || url.endsWith(".PNG") || url.endsWith(".GIF")) {
			return url;
		}
		String absoluteUrl = url + ".html";
		return absoluteUrl;
	}
	

}
