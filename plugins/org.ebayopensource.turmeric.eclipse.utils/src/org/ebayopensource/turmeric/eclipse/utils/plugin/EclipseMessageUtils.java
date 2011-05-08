/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.utils.plugin;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang.StringUtils;
import org.ebayopensource.turmeric.eclipse.utils.UtilsActivator;
import org.ebayopensource.turmeric.eclipse.utils.lang.StringUtil;
import org.eclipse.core.internal.resources.ResourceStatus;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceStatus;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.core.runtime.Status;


/**
 * This is a helper util class for dealing with the Eclipse messages and
 * statuses.
 *
 * @author yayu
 */
public class EclipseMessageUtils {
	private static final String STATUS_LABEL_OK = "ok";
	private static final String STATUS_LABEL_CANCEL = "cancel";
	private static final String STATUS_LABEL_WARNING = "warning";
	private static final String STATUS_LABEL_INFO = "information";
	private static final String STATUS_LABEL_ERROR = "error";
	private static final Map<Integer, String> SEVERITY_LABELS;

	/** The Constant ERROR_MESSAGE_UNKNOWN. */
	public static final String ERROR_MESSAGE_UNKNOWN = "Unknown error";

	static {
		Map<Integer, String> map = new ConcurrentHashMap<Integer, String>();
		map.put(Status.OK, STATUS_LABEL_OK);
		map.put(Status.CANCEL, STATUS_LABEL_CANCEL);
		map.put(Status.WARNING, STATUS_LABEL_WARNING);
		map.put(Status.INFO, STATUS_LABEL_INFO);
		map.put(Status.ERROR, STATUS_LABEL_ERROR);
		SEVERITY_LABELS = Collections.unmodifiableMap(map);
	}

	/**
	 * Creates the assert safe status.
	 *
	 * @param severity the severity
	 * @param pluginId the plugin id
	 * @param code the code
	 * @param message the message
	 * @param exception the exception
	 * @return the status
	 */
	public static Status createAssertSafeStatus(int severity, String pluginId,
			int code, String message, Throwable exception) {
		if (!(severity == Status.OK || severity == Status.ERROR
				|| severity == Status.WARNING || severity == Status.INFO || severity == Status.CANCEL)) {
			severity = Status.INFO;

		}
		if (StringUtils.isEmpty(pluginId)) {
			pluginId = UtilsActivator.PLUGIN_ID;
		}
		if (StringUtils.isEmpty(message)) {
			if (exception != null
					&& !StringUtils.isEmpty(exception.getLocalizedMessage())) {
				message = exception.getLocalizedMessage();
			} else {
				message = ERROR_MESSAGE_UNKNOWN;
			}
		}
		/*if (exception == null) {
			exception = new Exception(ERROR_MESSAGE_UNKNOWN);
		}*/
		return new Status(severity, pluginId, code, message, exception);
	}

	/**
	 * Creates the soa resource error status.
	 *
	 * @param resource the resource
	 * @param message the message
	 * @param exception the exception
	 * @return the sOA resource status
	 */
	public static SOAResourceStatus createSOAResourceErrorStatus(
			IResource resource, String message, Throwable exception) {
		final SOAResourceStatus status = new SOAResourceStatus(IStatus.ERROR,
				IStatus.ERROR, resource.getLocation(), message, exception);
		status.setResource(resource);
		return status;
	}

	/**
	 * Creates the soa resource warn status.
	 *
	 * @param resource the resource
	 * @param message the message
	 * @param exception the exception
	 * @return the sOA resource status
	 */
	public static SOAResourceStatus createSOAResourceWarnStatus(
			IResource resource, String message, Throwable exception) {
		final SOAResourceStatus status = new SOAResourceStatus(IStatus.WARNING,
				IStatus.WARNING, resource.getLocation(), message, exception);
		status.setResource(resource);
		return status;
	}

	/**
	 * Creates the resource error status.
	 *
	 * @param resourcePath the resource path
	 * @param message the message
	 * @param exception the exception
	 * @return the i resource status
	 */
	public static IResourceStatus createResourceErrorStatus(IPath resourcePath,
			String message, Throwable exception) {
		return new SOAResourceStatus(IStatus.ERROR, IStatus.ERROR,
				resourcePath, message, exception);
	}
	
	// Allow missing resources to be set at Warning Levels instead of Error.
	/**
	 * Creates the resource warn status.
	 *
	 * @param resourcePath the resource path
	 * @param message the message
	 * @param exception the exception
	 * @return the i resource status
	 */
	public static IResourceStatus createResourceWarnStatus(IPath resourcePath, String message, Throwable exception) {
		return new SOAResourceStatus(IStatus.WARNING, IStatus.WARNING, resourcePath, message, exception);
	}

	/**
	 * Creates the empty ok multi status.
	 *
	 * @param description the description
	 * @return the i status
	 */
	public static IStatus createEmptyOKMultiStatus(String description) {
		return createMultiStatus(null, IStatus.OK, null, description, null);
	}

	/**
	 * Creates the empty error multi status.
	 *
	 * @param description the description
	 * @return the i status
	 */
	public static IStatus createEmptyErrorMultiStatus(String description) {
		return createMultiStatus(null, IStatus.ERROR, null, description, null);
	}

	/**
	 * Creates the error multi status.
	 *
	 * @param children the children
	 * @param description the description
	 * @return the i status
	 */
	public static IStatus createErrorMultiStatus(Collection<IStatus> children,
			String description) {
		return createMultiStatus(null, IStatus.ERROR, children
				.toArray(new IStatus[0]), description, null);
	}
	
	/**
	 * Creates the error multi status based on children severity.
	 *
	 * @param children the children
	 * @param description the description
	 * @return the i status
	 */
	public static IStatus createErrorMultiStatusBasedOnChildrenSeverity(Collection<IStatus> children,
			String description) {
		int severity = IStatus.OK;
		for (IStatus status : children) {
			if (status.getSeverity() == IStatus.OK) {
				continue;
			} else if (status.getSeverity() == IStatus.WARNING) {
				severity = IStatus.WARNING;
				continue;
			} else if (status.getSeverity() == IStatus.ERROR) {
				severity = IStatus.ERROR;
				break;
			}
		}
		return createMultiStatus(null, severity, children
				.toArray(new IStatus[0]), description, null);
	}

	/**
	 * Creates the error multi status.
	 *
	 * @param children the children
	 * @param description the description
	 * @return the i status
	 */
	public static IStatus createErrorMultiStatus(IStatus[] children,
			String description) {
		return createMultiStatus(null, IStatus.ERROR, children, description,
				null);
	}

	/**
	 * Creates the multi status.
	 *
	 * @param children the children
	 * @param description the description
	 * @param thrown the thrown
	 * @return the i status
	 */
	public static IStatus createMultiStatus(IStatus[] children,
			String description, Throwable thrown) {
		int code = thrown != null ? IStatus.ERROR : IStatus.OK;
		return createMultiStatus(null, code, children, description, thrown);
	}

	/**
	 * Creates the multi status.
	 *
	 * @param pluginID the plugin id
	 * @param code the code
	 * @param children the children
	 * @param description the description
	 * @param thrown the thrown
	 * @return the i status
	 */
	public static IStatus createMultiStatus(String pluginID, int code,
			IStatus[] children, String description, Throwable thrown) {
		if (description == null) {
			description = (thrown == null)
					|| StringUtils.isNotBlank(thrown.getLocalizedMessage()) ? ERROR_MESSAGE_UNKNOWN
					: thrown.getLocalizedMessage();
		}
		if (pluginID == null)
			pluginID = UtilsActivator.SOA_PLUGIN_ID;

		if (children == null || children.length == 0) {
			return new MultiStatus(pluginID, code, description, thrown);
		} else {
			return new MultiStatus(pluginID, code, children, description,
					thrown);
		}
	}

	/**
	 * Creates the error status.
	 *
	 * @param pluginID the plugin id
	 * @param description the description
	 * @param thrown the thrown
	 * @return an instance of <code>IStatus</code> with Error severity level
	 */
	public static IStatus createErrorStatus(String pluginID,
			String description, Throwable thrown) {
		if (description == null) {
			description = (thrown == null)
					|| StringUtils.isBlank(thrown.getLocalizedMessage()) ? ERROR_MESSAGE_UNKNOWN
					: thrown.getLocalizedMessage();
		}
		if (pluginID == null)
			pluginID = UtilsActivator.SOA_PLUGIN_ID;
		return createAssertSafeStatus(IStatus.ERROR, pluginID, 0, description,
				thrown);
	}

	/**
	 * Creates the error status.
	 *
	 * @param description the description
	 * @param thrown the thrown
	 * @return an instance of <code>IStatus</code> with Error severity level
	 */
	public static IStatus createErrorStatus(String description, Throwable thrown) {
		return createErrorStatus(UtilsActivator.SOA_PLUGIN_ID, description,
				thrown);
	}

	/**
	 * Creates the error status.
	 *
	 * @param description the description
	 * @return the i status
	 */
	public static IStatus createErrorStatus(String description) {
		return createErrorStatus(UtilsActivator.SOA_PLUGIN_ID, description,
				null);
	}

	/**
	 * Creates the error status.
	 *
	 * @param thrown the thrown
	 * @return an instance of <code>IStatus</code> with Error severity level
	 */
	public static IStatus createErrorStatus(Throwable thrown) {
		return createErrorStatus(UtilsActivator.SOA_PLUGIN_ID, thrown
				.getLocalizedMessage(), thrown);
	}

	/**
	 * Creates the status.
	 *
	 * @param description the description
	 * @param severity the severity
	 * @return the i status
	 */
	public static IStatus createStatus(String description, int severity) {
		return createAssertSafeStatus(severity, UtilsActivator.SOA_PLUGIN_ID,
				0, description, null);
	}

	/**
	 * Wrap messages as IStatus, making every effort to complete normally by
	 * correcting parameters. If there are more than 1 substatus, then returns a
	 * MultiStatus with an entry for each substatus. Otherwise , return one
	 * IStatus with description (and any single substatus) as message.
	 * 
	 * @param pluginId
	 *            the String id of the plugin (if null, replaced with
	 *            Activator.PLUGIN_ID)
	 * @param description
	 *            the String master description of the status. If null, replaced
	 *            with something like "3 error messages".
	 * @param severity
	 *            the int (one of IStatus.ERROR, etc - replaced with
	 *            IStatus.ERROR if invalid)
	 * @param substatus
	 *            the String[] of substatus messages (may be null; null elements
	 *            are ignored)
	 * @return non-null IStatus
	 */
	public static IStatus createStatus(String pluginId, String description,
			int severity, String... substatus) {
		if (null == pluginId) {
			pluginId = UtilsActivator.SOA_PLUGIN_ID;
		}

		if (SEVERITY_LABELS.containsKey(severity) == false) {
			// use Error as the default severity
			severity = IStatus.ERROR;
		}
		final int numSubstatus = substatus == null ? 0 : substatus.length;
		if (description == null) {
			if (numSubstatus > 0) {
				String severityLabel = SEVERITY_LABELS.get(severity);
				description = StringUtil.toString(numSubstatus, " ",
						severityLabel, " messages");
			} else {
				description = "(No description)";
			}
		}
		if (numSubstatus < 2) {
			// only one sub status
			if ((numSubstatus == 1) && (substatus[0] != null)) {
				return createAssertSafeStatus(severity, pluginId, 0, StringUtil
						.toString(description, ": ", substatus[0]), null);
			}
			return createAssertSafeStatus(severity, pluginId, 0, description,
					null);
		} else {// multi sub statuses
			MultiStatus result = new MultiStatus(pluginId, 0, description, null);
			for (String status : substatus) {
				if (status != null) {
					result.add(createAssertSafeStatus(severity, pluginId, 0,
							status, null));
				}
			}
			return result;
		}
	}

	/**
	 * Formats the status and give a string out. If its multi status then each
	 * message would be in seperate line. Its suitable to show in a dialog
	 *
	 * @param status the status
	 * @return the string
	 */
	public static String formatStatus(IStatus status) {
		StringBuffer retBuffer = new StringBuffer();
		if (status.isMultiStatus()) {
			for (IStatus childStatus : status.getChildren()) {
				retBuffer.append(childStatus.getMessage() + "\r\n");
			}
		} else {
			retBuffer.append(status.getMessage() + "\r\n");
		}
		return retBuffer.toString();

	}

	/**
	 * Severity label.
	 *
	 * @param severity the severity
	 * @return null if not a valid severity or a user-readable name otherwise
	 */
	public static String severityLabel(int severity) {
		return SEVERITY_LABELS.get(severity);
	}

	/**
	 * The Class SOAResourceStatus.
	 *
	 * @author yayu
	 * @see ResourceStatus
	 */
	public static class SOAResourceStatus extends Status implements
			IResourceStatus {
		private IPath path;
		private IResource resource;

		/**
		 * Instantiates a new sOA resource status.
		 *
		 * @param type the type
		 * @param code the code
		 * @param path the path
		 * @param message the message
		 * @param exception the exception
		 */
		public SOAResourceStatus(int type, int code, IPath path,
				String message, Throwable exception) {
			super(type, ResourcesPlugin.PI_RESOURCES, code, message, exception);
			this.path = path;
		}

		/**
		 * Instantiates a new sOA resource status.
		 *
		 * @param code the code
		 * @param message the message
		 */
		public SOAResourceStatus(int code, String message) {
			this(getSeverity(code), code, null, message, null);
		}

		/**
		 * Instantiates a new sOA resource status.
		 *
		 * @param code the code
		 * @param path the path
		 * @param message the message
		 */
		public SOAResourceStatus(int code, IPath path, String message) {
			this(getSeverity(code), code, path, message, null);
		}

		/**
		 * Instantiates a new sOA resource status.
		 *
		 * @param code the code
		 * @param path the path
		 * @param message the message
		 * @param exception the exception
		 */
		public SOAResourceStatus(int code, IPath path, String message,
				Throwable exception) {
			this(getSeverity(code), code, path, message, exception);
		}

		/**
		 * Gets the path.
		 *
		 * @return the path
		 * @see IResourceStatus#getPath()
		 */
		public IPath getPath() {
			return path;
		}

		/**
		 * Gets the severity.
		 *
		 * @param code the code
		 * @return the severity
		 */
		protected static int getSeverity(int code) {
			return code == 0 ? 0 : 1 << (code % 100 / 33);
		}

		// for debug only
		private String getTypeName() {
			switch (getSeverity()) {
			case IStatus.OK:
				return "OK"; //$NON-NLS-1$
			case IStatus.ERROR:
				return "ERROR"; //$NON-NLS-1$
			case IStatus.INFO:
				return "INFO"; //$NON-NLS-1$
			case IStatus.WARNING:
				return "WARNING"; //$NON-NLS-1$
			default:
				return String.valueOf(getSeverity());
			}
		}

		/**
		 * Gets the resource.
		 *
		 * @return the resource
		 */
		public IResource getResource() {
			return resource;
		}

		/**
		 * Sets the resource.
		 *
		 * @param resource the new resource
		 */
		public void setResource(IResource resource) {
			this.resource = resource;
		}

		// for debug only
		/**
		 * {@inheritDoc}
		 */
		public String toString() {
			StringBuffer sb = new StringBuffer();
			sb.append("[type: "); //$NON-NLS-1$
			sb.append(getTypeName());
			sb.append("], [path: "); //$NON-NLS-1$
			sb.append(getPath());
			sb.append("], [message: "); //$NON-NLS-1$
			sb.append(getMessage());
			sb.append("], [plugin: "); //$NON-NLS-1$
			sb.append(getPlugin());
			sb.append("], [exception: "); //$NON-NLS-1$
			sb.append(getException());
			sb.append("]\n"); //$NON-NLS-1$
			return sb.toString();
		}
	}

}
