/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.mylyn.wikitext.confluence.core.tasks;

import java.io.File;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Stylesheet {
	private File file;

	private String url;

	private final Map<String, String> attributes = new ConcurrentHashMap<String, String>();

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public void addConfiguredAttribute(Attribute attribute) {
		attributes.put(attribute.getName(), attribute.getValue());
	}
}
