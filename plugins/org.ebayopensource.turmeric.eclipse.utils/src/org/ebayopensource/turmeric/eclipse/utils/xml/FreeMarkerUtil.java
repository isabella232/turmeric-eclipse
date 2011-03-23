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
package org.ebayopensource.turmeric.eclipse.utils.xml;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.URL;
import java.util.Map;

import org.apache.commons.io.IOUtils;

import freemarker.cache.TemplateLoader;
import freemarker.cache.URLTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.TemplateException;

/**
 * @author yayu
 * 
 */
public final class FreeMarkerUtil {

	/**
	 * 
	 */
	private FreeMarkerUtil() {
		super();
	}

	public static void generate(final Map<String, ?> data,
			final Object templateParentFolder, final String templateName,
			final OutputStream out) throws IOException, TemplateException {
		try {
			Writer writer = new OutputStreamWriter(out);
			generate(data, templateParentFolder, templateName, writer);
			out.flush();
		} finally {
			IOUtils.closeQuietly(out);
		}
	}

	public static void generate(final Map<String, ?> data,
			final Object templateParentFolder, final String templateName,
			final Writer writer) throws IOException, TemplateException {

		final Configuration cfg = new Configuration();
		final TemplateLoader tempLoader = cfg.getTemplateLoader();
		try {

			// Specify the data source where the template files come from.
			// Here I set a file directory for it:
			if (templateParentFolder instanceof File) {
				cfg.setDirectoryForTemplateLoading((File) templateParentFolder);
			} else if (templateParentFolder instanceof Class<?>) {
				cfg.setClassForTemplateLoading((Class<?>) templateParentFolder,
						"");
			} else if (templateParentFolder instanceof URL) {
				final URL url = (URL) templateParentFolder;

				cfg.setTemplateLoader(new URLTemplateLoader() {

					@Override
					protected URL getURL(String arg0) {
						return url;
					}

				});
			} else {
				throw new IllegalArgumentException(
						"Illegal Argument for template loading->"
								+ templateParentFolder);
			}

			// Specify how templates will see the data-model. This is an
			// advanced topic...
			// but just use this:
			cfg.setObjectWrapper(new DefaultObjectWrapper());

			freemarker.template.Template temp = cfg.getTemplate(templateName);

			temp.process(data, writer);
		} finally {
			IOUtils.closeQuietly(writer);
			cfg.setTemplateLoader(tempLoader);
		}

	}

}
