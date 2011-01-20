/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.mavenapi.internal.util;

/**
 * 
 * @author James Ervin</a>
 * 
 */

public class ThrowableUtil {
	private static boolean isAdaptedToRuntimeException(final Throwable throwable) {
		if (!(throwable instanceof RuntimeException))
			return false;
		if (throwable.getCause() == null)
			return true;
		return isAdaptedToRuntimeException(throwable.getCause());
	}

	public static RuntimeException adaptToRuntimeException(
			final Throwable throwable) {
		if (throwable == null)
			return new RuntimeException();
		if (isAdaptedToRuntimeException(throwable))
			return (RuntimeException) throwable;
		final RuntimeException cause = throwable.getCause() != null ? adaptToRuntimeException(throwable
				.getCause()) : null;
		final RuntimeException exception = cause != null ? new RuntimeException(
				throwable.getClass().getName() + ": " + throwable.getMessage(),
				cause) : new RuntimeException(throwable.getClass().getName()
				+ ": " + throwable.getMessage());
		final StackTraceElement[] elements = throwable.getStackTrace();
		exception.setStackTrace(elements);
		return exception;
	}
}
