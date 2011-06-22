/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.utils.io;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.ebayopensource.turmeric.eclipse.utils.classloader.SOAToolFileUrlHandler;
import org.ebayopensource.turmeric.eclipse.utils.plugin.ProgressUtil;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;


/**
 * Standard SOA IO Utility class. Basic IO operations are being used from here.
 * Most of the APIs deals with files and directories even if the name says it is
 * generic IO :).
 * 
 * @author smathew
 * 
 */
public class IOUtil {

	/**
	 * Creates a new file under the root adding segments one by one
	 * incrementally. Basically, here we first create the parent directory and
	 * then create the child incrementally. It checks for the readability of the
	 * parent and the validity of the segment before creating every child.
	 * 
	 * @param root
	 *            the File base directory (ignored if null or not readable
	 * @param segment
	 *            the String[] of path elements to add to root (ignored if null)
	 * @return null if root not readable or File otherwise
	 */
	public static File newFile(File root, String... segments) {
		if (readableDir(root)) {
			if (null != segments) {
				for (String segment : segments) {
					if (validSegment(segment)) {
						root = new File(root, segment);
					}
				}
			}
			return root;
		}
		return null;
	}

	private static boolean validSegment(String segment) {
		return null != segment && 0 < segment.length();
	}

	/**
	 * Checks if the directory is readable. Null Safe. Returns true only if the
	 * file is not null, is a directory , if it exists and if it is readable.
	 *
	 * @param dir the dir
	 * @return true, if successful
	 */
	public static boolean readableDir(File dir) {
		return null != dir && dir.isDirectory() && dir.canRead();
	}
	
	/**
	 * Join all.
	 *
	 * @param parts the parts
	 * @return the string
	 */
	public static String joinAll(final String... parts) {		
		final String dirStr = StringUtils.join(Arrays.asList(parts), File.separator);
		return dirStr;
	}

	private IOUtil() {
	}

	/**
	 * This function checks for null, empty String, URLMalformed exception, URI
	 * exception and finally checks file existence In any of the case where it
	 * fails, it will return false. Clients doesnt have to handle null
	 * explicitly.
	 *
	 * @param url the url
	 * @return true, if successful
	 */
	public static boolean validateURL(String url) {
		if (!StringUtils.isEmpty(url)) {

			try {
				URL objUrl = new URL(url);
				objUrl.openStream().close();

			} catch (Exception exception) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Writes the contents to this file. This is just a convenience wrapper. If
	 * the file does not exist it will create one and sets the contents.
	 * Additionally it will close the stream whatever is the case.
	 *
	 * @param contents the contents
	 * @param file the file
	 * @param progressMonitor the progress monitor
	 * @throws CoreException the core exception
	 */
	public static void writeTo(String contents, final IFile file,
			IProgressMonitor progressMonitor) throws CoreException {
		progressMonitor = ProgressUtil.getDefaultMonitor(progressMonitor);
		InputStream input = IOUtils.toInputStream(contents);
		try {
			if (file.exists()) {
				file.setContents(input, true, true, progressMonitor);
			} else {
				file.create(input, true, progressMonitor);

			}
		} finally {
			IOUtils.closeQuietly(input);
		}
	}

	/**
	 * Loads the properties file residing inside a jar. The enclosed jar file is
	 * queried for the given jar entry path and is parsed into a properties
	 * file. Most of the cases this is being used to load some SOA properties
	 * file from a jar project which is not imported to the workspace but is
	 * stored somewhere in the file system. But any client who wants to load a
	 * properties file can use this. There is nothing SOA specific here in this
	 * API.
	 *
	 * @param enclosedJarFile the enclosed jar file
	 * @param jarEntryPath the jar entry path
	 * @return the properties
	 * @throws IOException This is a special case where we want the consumers to
	 * continue without failures, because most of the cases consumes
	 * this in a loop.
	 */
	public static Properties loadProperties(JarFile enclosedJarFile,
			String jarEntryPath) throws IOException {
		JarEntry jarEntry = enclosedJarFile.getJarEntry(jarEntryPath);
		InputStream inputStream = enclosedJarFile.getInputStream(jarEntry);
		Properties properties = new Properties();
		properties.load(inputStream);
		return properties;

	}

	/**
	 * Returns the java.io.tmpdir property. Clients are advised to use this
	 * wrapper because at a later stage we can easily change this location.
	 * 
	 * @return the default temp dir
	 */
	public static File getTempDirectory() {
		return new File(System.getProperty("java.io.tmpdir"));
	}

	/**
	 * In the normal jar URLs usage in Windows Java puts a lock on it. And the
	 * SOA Tool Handler will create a non locking URL by setting the caching
	 * off. There is obviously a performance compromise made here by disabling
	 * the cache.
	 *
	 * @param jarFileUrl the jar file url
	 * @param jarEntryPath the jar entry path
	 * @return the non locking url
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static URL getNonLockingURL(URL jarFileUrl, String jarEntryPath)
			throws IOException {
		File file = FileUtils.toFile(jarFileUrl);
		JarFile jarFile;
		jarFile = new JarFile(file);
		JarEntry jarEntry = jarFile.getJarEntry(jarEntryPath);
		if (jarEntry != null) {
			SOAToolFileUrlHandler handler = new SOAToolFileUrlHandler(jarFile,
					jarEntry);
			URL retUrl = new URL("jar", "", -1, new File(jarFile.getName())
					.toURI().toURL()
					+ "!/" + jarEntry.getName(), handler);
			handler.setExpectedUrl(retUrl);
			return retUrl;

		}
		return null;
	}

	/**
	 * Return the URL representation of the String. One important thing to note
	 * here is that this API tries to address Malformed URL issue. The new URL
	 * is created as per RFC2396 and there is a high chance that clients run
	 * into the MalformedURLException even if the String can be converted into
	 * an URL in a different way. This is the standard way of addressing the
	 * issue.
	 *
	 * @param url the url
	 * @return the uRL
	 * @throws MalformedURLException the malformed url exception
	 * @throws UnsupportedEncodingException the unsupported encoding exception
	 */
	public static URL toURL(String url) throws MalformedURLException,
			UnsupportedEncodingException {
		URL retURL = null;
		try {
			retURL = new URL(url);
		} catch (Exception exception) {
			File file = new File(url);
			retURL = file.toURI().toURL();
		}
		String vmFileEncoding = System.getProperty("file.encoding");
		retURL = new URL(URLDecoder.decode(retURL.toString(), vmFileEncoding));
		return retURL;
	}

	/**
	 * Returns the list of files contained in the given directory. Null safe.
	 * Returns an empty list if the directory is null or is not a directory or
	 * if it is empty. If the dirOrFile is true it will return only the child
	 * directories else it will return only the file children.
	 *
	 * @param dir the dir
	 * @param dirOrFile the dir or file
	 * @return returns the list of file in the directory,
	 */
	public static List<File> listFile(File dir, boolean dirOrFile) {
		List<File> fileList = new ArrayList<File>();
		if (dir != null && dir.isDirectory()) {
			for (File file : dir.listFiles())
				if (dirOrFile) {
					if (file.isDirectory()) {
						fileList.add(file);
					}
				} else {
					if (file.isFile()) {
						fileList.add(file);
					}
				}
		}
		return fileList;
	}
}
