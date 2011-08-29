/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.buildsystem.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.ebayopensource.turmeric.eclipse.core.logging.SOALogger;
import org.ebayopensource.turmeric.eclipse.resources.model.SOAIntfProject;
import org.ebayopensource.turmeric.eclipse.resources.util.SOAIntfUtil;
import org.ebayopensource.turmeric.eclipse.utils.io.IOUtil;
import org.ebayopensource.turmeric.eclipse.utils.plugin.WorkspaceUtil;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;

// TODO: Auto-generated Javadoc
/**
 * The Class ProjectProtoBufFileUtil.
 *
 * @author mzang
 */
public class ProjectProtoBufFileUtil {

	/** The Constant logger. */
	private static final SOALogger logger = SOALogger.getLogger();

	/** The Constant protoBufPostFixURL. */
	private static final String protoBufPostFixURL = "?proto";

	// private static final String PROTOBUF_HEADER_KEY =
	// "X-EBAY-SOA-CUSTOM-CONTENT-TYPE";

	/** match a message block:. */
	private static final String PROTO_FILE_SIGNATURE =
	/**
	 * 1)Start with "message"
	 */
	"message" +
	/**
	 * 2) followed by at least one empty character
	 */
	"[\\s]+" +
	/**
	 * 3) followed by message name, one character at least
	 */
	"[\\w]+" +
	/**
	 * 4) followed by zero or more empty characters
	 */
	"[\\s]*" +
	/**
	 * 5) followed by a pair of "{}" with any content in it.
	 */
	"\\{[^\\}]*\\}";

	/** The Constant PROTO_FILE_SIGNATURE_PATTERN. */
	private static final Pattern PROTO_FILE_SIGNATURE_PATTERN = Pattern
			.compile(PROTO_FILE_SIGNATURE, Pattern.CASE_INSENSITIVE);

	/**
	 * Creates the service proto buf file.
	 *
	 * @param soaIntfProject the soa intf project
	 * @param serviceLocation the service location
	 * @return true, if successful
	 */
	public static boolean createServiceProtoBufFile(
			SOAIntfProject soaIntfProject, String serviceLocation) {
		try {
			String protobufContent = getProtoBufFile(serviceLocation);
			if (protobufContent == null) {
				return false;
			}
			return createProtoBufFile(soaIntfProject, protobufContent);
		} catch (Throwable e) {
			logger.info("Failed to get protoBuf file: " + e.getMessage());
			return false;
		}
	}

	// private static boolean checkHeaders(Header[] headers) {
	// for (Header header : headers) {
	// String key = header.getName();
	// key = key.trim();
	// if (PROTOBUF_HEADER_KEY.equals(key)) {
	// return true;
	// }
	// }
	// return false;
	// }

	/**
	 * Check proto buf content.
	 *
	 * @param content the content
	 * @return true, if successful
	 */
	private static boolean checkProtoBufContent(String content) {
		Matcher matcher = PROTO_FILE_SIGNATURE_PATTERN.matcher(content);
		return matcher.find();
	}

	/**
	 * Gets the proto buf file.
	 *
	 * @param serviceLocation the service location
	 * @return the proto buf file
	 */
	public static String getProtoBufFile(String serviceLocation) {
		if (serviceLocation == null) {
			logger.info("Service location is null, return.");
			return null;
		}
		logger.info("Service location is: " + serviceLocation);

		StringBuilder protoBufContent = new StringBuilder();
		try {
			String protoBufURL = serviceLocation + protoBufPostFixURL;
			logger.info("Protobuf file URL location is: " + protoBufURL);

			GetMethod getProtoBuf = new GetMethod(protoBufURL);
			HttpClient connection = new HttpClient();
			connection.executeMethod(getProtoBuf);
			int status = getProtoBuf.getStatusCode();
			if (HttpStatus.SC_OK != status) {
				return null;
			}
			logger.info("Http status OK. Fetching file content...");

			BufferedReader reader = new BufferedReader(new InputStreamReader(
					getProtoBuf.getResponseBodyAsStream(), "UTF-8"));
			logger.info("Reading file content...");
			String line = null;
			while ((line = reader.readLine()) != null) {
				protoBufContent.append(line);
				protoBufContent.append("\r\n");
			}
			reader.close();
			logger.info("Protobuf file is fetched. Validating...");
			String content = protoBufContent.toString();
			if (checkProtoBufContent(content) == false) {
				logger.info("Protobuf file validation failed.");
				return null;
			}
			logger.info("Protobuf file validation pass.");
			return content;
		} catch (Throwable e) {
			logger.info("Failed to get protoBuf file: " + e.getMessage());
			return null;
		}
	}

	/**
	 * Creates the proto buf file.
	 *
	 * @param intfProject the intf project
	 * @param fileContent the file content
	 * @return true, if successful
	 */
	private static boolean createProtoBufFile(SOAIntfProject intfProject,
			String fileContent) {
		logger.info("Writing protoBuf file to " + intfProject.getProjectName());
		IProject intfProj = intfProject.getEclipseMetadata().getProject();
		String adminName = intfProject.getMetadata().getServiceName();
		try {
			IFile file = WorkspaceUtil.createEmptyFile(intfProj,
					SOAIntfUtil.getIntfProtoBufFilePath(adminName),
					new NullProgressMonitor());
			if (file.isAccessible() == false) {
				logger.info("Target protobuf file is not accessible.");
			}
			IOUtil.writeTo(fileContent, file, new NullProgressMonitor());
		} catch (CoreException e) {
			e.printStackTrace();
			return false;
		}
		logger.info("Writing protoBuf file finished.");
		return true;
	}

}
