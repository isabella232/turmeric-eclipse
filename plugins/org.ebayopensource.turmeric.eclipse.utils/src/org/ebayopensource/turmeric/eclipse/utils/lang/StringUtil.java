/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.utils.lang;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import org.apache.commons.lang.StringUtils;

/**
 * A utility class for working with <code>String</code>.
 * 
 * @author yayu
 * 
 */
public class StringUtil {
	/**
	 * The end of line
	 */
	public static final String EOL;
	static {
		String eol = "\n";
		EOL = StringUtils.isEmpty(System.getProperty("line.separator")) ? eol
				: System.getProperty("line.separator");
	}

	/**
	 * Remove the first occurrence of the giving string.
	 * 
	 * @param string
	 *            The string to be removed from
	 * @param remove
	 *            The string to be removed
	 * @return
	 */
	public static String removeFirst(final String string, final String remove) {
		return StringUtils.replace(string, remove, "", 1);
	}

	/**
	 * Join the objects using the given delimiter.
	 * 
	 * @param objects
	 *            the objects
	 * @param delimiter
	 *            the delimiter
	 * @return
	 */
	public static String join(final Collection<? extends Object> objects,
			final String delimiter) {
		return StringUtils.join(objects != null ? objects.toArray() : null,
				delimiter);
	}

	/**
	 * 
	 * @param strings
	 *            the String... elements to copy if not null
	 * @return non-null String[] of non-null elements from input strings
	 */
	public static String[] copyNonNulls(final String... strings) {
		ArrayList<String> arrayList = new ArrayList<String>(Arrays
				.asList(strings));
		arrayList.removeAll(Collections.singleton(null));
		return arrayList.toArray(new String[0]);
	}

	/**
	 * Chop
	 * 
	 * @param i
	 * @param input
	 * @param length
	 * @param result
	 */
	public static void lineate(String input, int length, StringBuilder result) {
		int start = 0;
		while (length < (input.length() - start)) {
			int nextWhitespace = nextWhitespaceAfter(input, start, length);
			if (-1 == nextWhitespace) {
				result.append(input.substring(start));
				result.append(EOL);
				start = input.length();
			} else {
				result.append(input.substring(start, nextWhitespace));
				result.append(EOL);
				start = nextWhitespace + 1;
			}
		}
		if (start < (input.length() - 1)) {
			result.append(input.substring(start));
			result.append(EOL);
		}
	}

	static int nextWhitespaceAfter(String error, int start, int lineLength) {
		final int LENGTH = error.length();
		if ((null == error) || (start >= LENGTH)) {
			return -1;
		}
		int index = start + lineLength;
		while (index < LENGTH) {
			if (Character.isWhitespace(error.charAt(index))) {
				return index;
			}
			index++;
		}
		return -1;
	}

	/**
	 * This method will prepend the prefix to the string arg iff prefix is not
	 * already there at the beginning.
	 */
	public static String prefix(final String prefix, final String string) {
		if (StringUtils.isEmpty(prefix))
			return string;
		if (StringUtils.defaultString(string).startsWith(prefix))
			return StringUtils.defaultString(string);
		return prefix + StringUtils.defaultString(string);
	}

	/**
	 * This method will postfix the postfix to the string arg iff prefix is not
	 * already there at the end.
	 */
	public static String postfix(final String string, final String postfix) {
		if (StringUtils.isEmpty(postfix))
			return string;
		if (StringUtils.defaultString(string).endsWith(postfix))
			return StringUtils.defaultString(string);
		return StringUtils.defaultString(string) + postfix;
	}

	public static String bracket(final String prefix, final String string,
			final String postfix) {
		return postfix(prefix(prefix, string), postfix);
	}

	/**
	 * <p>
	 * </p>
	 * 
	 * @param messages
	 * @return A String to represent the message
	 */
	public static String toString(Object... messages)
			throws IllegalArgumentException {
		final StringBuffer buf = new StringBuffer();
		if (messages == null || messages.length == 0) {
			return buf.toString();
		}
		for (Object msg : messages) {
			buf.append(String.valueOf(msg));
		}
		return buf.toString();
	}
	
	/**
	 * Format the given message with the provided arguments.
	 * @see java.text.MessageFormat
	 * @param message
	 * @param args
	 * @return
	 * @throws IllegalArgumentException
	 */
	public static String formatString(String message, Object... args)
	throws IllegalArgumentException {
		if (args == null)
			return message;
		
		return MessageFormat.format(message, args);
	}

	/**
	 * @param str1
	 * @param str2
	 * @return
	 * trims, ignores the case and null safe
	 */
	public static boolean broadEquals(String str1, String str2) {
		String defStr1 = StringUtils.defaultString(str1);
		String defStr2 = StringUtils.defaultString(str2);
		defStr1 = defStr1.trim();
		defStr2 = defStr2.trim();
		return defStr1.equalsIgnoreCase(defStr2);

	}
}
