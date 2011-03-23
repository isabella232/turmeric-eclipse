/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.utils.classloader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.apache.xbean.classloader.JarFileUrlConnection;

/**
 * @author smathew
 * 
 */
public class SOAToolFileUrlHandler extends URLStreamHandler {
	public static URL createUrl(JarFile jarFile, JarEntry jarEntry)
			throws MalformedURLException {
		return createUrl(jarFile, jarEntry, new File(jarFile.getName()).toURI().toURL());
	}

	public static URL createUrl(JarFile jarFile, JarEntry jarEntry,
			URL codeSource) throws MalformedURLException {
		SOAToolFileUrlHandler handler = new SOAToolFileUrlHandler(jarFile,
				jarEntry);
		URL url = new URL("jar", "", -1,
				codeSource + "!/" + jarEntry.getName(), handler);
		handler.setExpectedUrl(url);
		return url;
	}

	private URL expectedUrl;
	private final JarFile jarFile;
	private final JarEntry jarEntry;

	public SOAToolFileUrlHandler(JarFile jarFile, JarEntry jarEntry) {
		if (jarFile == null)
			throw new NullPointerException("jarFile is null");
		if (jarEntry == null)
			throw new NullPointerException("jarEntry is null");

		this.jarFile = jarFile;
		this.jarEntry = jarEntry;
	}

	public void setExpectedUrl(URL expectedUrl) {
		if (expectedUrl == null)
			throw new NullPointerException("expectedUrl is null");
		this.expectedUrl = expectedUrl;
	}

	public URLConnection openConnection(URL url) throws IOException {

		if (expectedUrl == null)
			throw new IllegalStateException("expectedUrl was not set");

		if (!expectedUrl.equals(url)) {
			if (!url.getProtocol().equals("jar")) {
				throw new IllegalArgumentException("Unsupported protocol "
						+ url.getProtocol());
			}
			String path = url.getPath();
			String[] chunks = path.split("!/", 2);
			if (chunks.length == 1) {
				throw new MalformedURLException(
						"Url does not contain a '!' character: " + url);
			}

			String file = chunks[0];
			String entryPath = chunks[1];

			if (!file.startsWith("file:")) {
				URLConnection retUrlConnection = new URL(url.toExternalForm())
						.openConnection();
				retUrlConnection.setDefaultUseCaches(false);
				return retUrlConnection;
			}
			file = file.substring("file:".length());

			if (!jarFile.getName().equals(file)) {
				URLConnection retUrlConnection = new URL(url.toExternalForm())
						.openConnection();
				retUrlConnection.setDefaultUseCaches(false);
				return retUrlConnection;
			}

			JarEntry newEntry = jarFile.getJarEntry(entryPath);
			if (newEntry == null) {
				throw new FileNotFoundException("Entry not found: " + url);
			}
			JarFileUrlConnection urlConnection = new JarFileUrlConnection(url,
					jarFile, newEntry);
			urlConnection.setDefaultUseCaches(false);
			return urlConnection;

		}
		JarFileUrlConnection urlConnection = new JarFileUrlConnection(url,
				jarFile, jarEntry);
		urlConnection.setDefaultUseCaches(false);
		return urlConnection;
	}
}
