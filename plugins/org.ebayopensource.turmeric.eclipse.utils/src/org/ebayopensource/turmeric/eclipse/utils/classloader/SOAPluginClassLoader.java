/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.utils.classloader;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.io.FileUtils;
import org.osgi.framework.Bundle;

/**
 * The Class SOAPluginClassLoader.
 *
 * @author smathew
 */
public class SOAPluginClassLoader extends URLClassLoader {
	private static final URL[] EMPTY_URLS = new URL[0];
	private static final Comparator<URL> URL_COMPARATOR = new Comparator<URL>() {

		@Override
		public int compare(URL o1, URL o2) {
			return (o1 != null && o2 != null) ? o1.toString().compareTo(
					o2.toString()) : 0;
		}
	};
	private Set<URL> m_jarURLs = new TreeSet<URL>(URL_COMPARATOR);
	private Set<URL> m_dirURLs = new TreeSet<URL>(URL_COMPARATOR);
	private Set<URL> m_classPathURLs = new TreeSet<URL>(URL_COMPARATOR);
	private List<Bundle> pluginBundles = new ArrayList<Bundle>();
	private static final Logger logger = Logger
			.getLogger(SOAPluginClassLoader.class.getName());

	/**
	 * Instantiates a new sOA plugin class loader.
	 *
	 * @param name the name
	 * @param urls the urls
	 */
	public SOAPluginClassLoader(String name, URL[] urls) {
		super(EMPTY_URLS);

		for (int i = 0; i < urls.length; i++) {
			File file = FileUtils.toFile(urls[i]);
			if (file.isDirectory()) {
				m_dirURLs.add(urls[i]);
			} else if (file.isFile()) {
				m_jarURLs.add(urls[i]);
			}
		}
		for (URL dirURL : m_dirURLs) {
			addURL(dirURL);
		}
		m_classPathURLs.addAll(m_jarURLs);
		m_classPathURLs.addAll(m_dirURLs);
		if (logger.isLoggable(Level.FINE)) {
			logger.fine("Inside soa plugin loader setM_classPathURLs: "
					+ m_classPathURLs);
		}
	}

	/* (non-Javadoc)
	 * @see java.lang.ClassLoader#loadClass(java.lang.String, boolean)
	 */
	@Override
	protected synchronized Class<?> loadClass(String name, boolean resolve)
			throws ClassNotFoundException {
		try {
			Class<?> _class = doLoadClass(name);
			if (_class != null)
				return _class;
		} catch (Exception e) {
			//oops, got some problems just ignore it.
		}
		return super.loadClass(name, resolve);
	}
	
	private Class<?> doLoadClass(String className) throws ClassNotFoundException {
		Class<?> clazz = null;
		try {
			for (Bundle pluginBundle : pluginBundles) {
				clazz = pluginBundle.loadClass(className);
				if (clazz != null) {
					//logger.info("loaded using bundle class loader->"
					//		+ className);
					return clazz;
				}
			}

		} catch (ClassNotFoundException classNotFoundException) {
		}
		return clazz;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Class<?> findClass(String name) throws ClassNotFoundException {

		Class<?> loadedClass = findLoadedClass(name);
		if (loadedClass != null) {
			return loadedClass;
		}

		StringBuilder sb = new StringBuilder(name.length() + 6);
		sb.append(name.replace('.', '/')).append(".class");

		InputStream is = getResourceAsStream(sb.toString());

		if (is == null)
			throw new ClassNotFoundException("Class not found " + sb);
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			byte[] buf = new byte[1024];
			int len;
			while ((len = is.read(buf)) >= 0)
				baos.write(buf, 0, len);

			buf = baos.toByteArray();

			// define package if not defined yet
			int i = name.lastIndexOf('.');
			if (i != -1) {
				String pkgname = name.substring(0, i);
				Package pkg = getPackage(pkgname);
				if (pkg == null)
					definePackage(pkgname, null, null, null, null, null, null,
							null);
			}
			baos.close();
			is.close();
			return defineClass(name, buf, 0, buf.length);
		} catch (IOException e) {
			throw new ClassNotFoundException(name, e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public URL findResource(String resourceName) {
		//logger.info("resource name in findresource is " + resourceName);
		try {
			URL retUrl = null;
			for (Bundle pluginBundle : pluginBundles) {
				retUrl = pluginBundle.getResource(resourceName);
				if (retUrl != null) {
					if (logger.isLoggable(Level.FINE)) {
						logger.fine("found resource using bundle " + resourceName);
					}
					return retUrl;
				}
			}

		} catch (Exception exception) {
		}

		for (URL url : m_jarURLs) {

			try {
				File file = FileUtils.toFile(url);
				JarFile jarFile;
				jarFile = new JarFile(file);
				JarEntry jarEntry = jarFile.getJarEntry(resourceName);
				if (jarEntry != null) {
					SOAToolFileUrlHandler handler = new SOAToolFileUrlHandler(
							jarFile, jarEntry);
					URL retUrl = new URL("jar", "", -1, new File(jarFile
							.getName()).toURI().toURL()
							+ "!/" + jarEntry.getName(), handler);
					handler.setExpectedUrl(retUrl);
					return retUrl;

				}
			} catch (IOException e) {
				e.printStackTrace(); // KEEPME
			}

		}

		return super.findResource(resourceName);
	}

	/**
	 * Gets the plugin bundles.
	 *
	 * @return the plugin bundles
	 */
	public List<Bundle> getPluginBundles() {
		return pluginBundles;
	}

	/**
	 * Sets the plugin bundles.
	 *
	 * @param pluginBundles the new plugin bundles
	 */
	public void setPluginBundles(List<Bundle> pluginBundles) {
		this.pluginBundles = pluginBundles;
	}

	/* (non-Javadoc)
	 * @see java.net.URLClassLoader#getURLs()
	 */
	@Override
	public URL[] getURLs() {
		return m_classPathURLs.toArray(new URL[0]);
	}

	/**
	 * Gets the m_class path ur ls.
	 *
	 * @return the m_class path ur ls
	 */
	public Set<URL> getM_classPathURLs() {
		return m_classPathURLs;
	}

	/**
	 * Sets the m_class path ur ls.
	 *
	 * @param pathURLs the new m_class path ur ls
	 */
	public void setM_classPathURLs(Set<URL> pathURLs) {
		if (logger.isLoggable(Level.FINE)) {
			logger.fine("Inside soa plugin loader setM_classPathURLs: " + pathURLs);
		}
		m_classPathURLs = pathURLs;
	}

}
