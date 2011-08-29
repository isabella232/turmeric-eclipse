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
package org.ebayopensource.turmeric.eclipse.resources.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.ebayopensource.turmeric.eclipse.resources.Activator;
import org.ebayopensource.turmeric.eclipse.utils.plugin.EclipseMessageUtils;
import org.ebayopensource.turmeric.eclipse.utils.plugin.EclipseMessageUtils.SOAResourceStatus;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceStatus;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;


// TODO: Auto-generated Javadoc
/**
 * The Class MarkerUtil.
 * @author yayu
 * 
 */
public final class MarkerUtil {
	/**
	 * SOA Problem Marker ID.
	 */
	public static final String SOA_PROBLEM_MARKER_ID = Activator.PLUGIN_ID
	+ ".soaproblem";

	/**
	 * WSDL Problem Marker ID.
	 */
	public static final String WSDL_PROBLEM_MARKER_ID = Activator.PLUGIN_ID
	+ ".wsdlproblem";

	/**
	 * Instantiates a new marker util.
	 */
	private MarkerUtil() {
		super();
	}

	/**
	 * Finds the SOA Error Markers given an IResource.
	 *
	 * @param resource the resource to check
	 * @return An array of IMarkers that were found.  May be null or empty if nothing is found.
	 * @throws CoreException the core exception
	 */
	public static IMarker[] findSOAErrorMarkers(IResource resource)
	throws CoreException {
		final List<IMarker> result = new ArrayList<IMarker>();
		for (final IMarker marker : findSOAProblemMarkers(resource)) {
			if (IMarker.SEVERITY_ERROR == marker.getAttribute(IMarker.SEVERITY,
					IMarker.SEVERITY_ERROR)) {
				result.add(marker);
			}
		}
		return result.toArray(new IMarker[0]);
	}

	/**
	 * Finds the SOA Problem Markers given a resource.
	 *
	 * @param resource the resource to search
	 * @return an array of IMarker entries. May be null or empty if noting is found.
	 * @throws CoreException the core exception
	 */
	public static IMarker[] findSOAProblemMarkers(IResource resource)
	throws CoreException {
		return resource.findMarkers(SOA_PROBLEM_MARKER_ID, true,
				IResource.DEPTH_INFINITE);
	}

	/**
	 * Finds markers given a resource and type.
	 *
	 * @param resource the resource
	 * @param type the type of markert to find
	 * @return An array of IMarkers that where found. May be empty or null if no markers found.
	 * @throws CoreException the core exception
	 */
	public static IMarker[] findMarkers(IResource resource, String type)
	throws CoreException {
		return resource.findMarkers(type, true, IResource.DEPTH_INFINITE);
	}

	/**
	 * Clean soa problem markers.
	 *
	 * @param resource the resource
	 * @throws CoreException the core exception
	 */
	public static void cleanSOAProblemMarkers(IResource resource)
	throws CoreException {
		cleanMarkers(resource, SOA_PROBLEM_MARKER_ID);
	}

	/**
	 * Clean markers.
	 *
	 * @param resource the resource
	 * @param type the type
	 * @throws CoreException the core exception
	 */
	public static void cleanMarkers(IResource resource, String type)
	throws CoreException {
		if (resource == null) {
			return;
		}
		final IMarker[] markers = resource.findMarkers(type, true,
				IResource.DEPTH_INFINITE);
		for (final IMarker marker : markers) {
			if (marker.exists()) {
				marker.delete();
			}
		}
	}

	/**
	 * Gets the marker severity.
	 *
	 * @param status the status
	 * @return the marker severity
	 */
	private static int getMarkerSeverity(IStatus status) {
		switch (status.getSeverity()) {
		case IStatus.ERROR:
			return IMarker.SEVERITY_ERROR;
		case IStatus.WARNING:
			return IMarker.SEVERITY_WARNING;
		default:
			return IMarker.SEVERITY_INFO;
		}
	}

	/**
	 * Creates the soa problem marker.
	 *
	 * @param thrown the thrown
	 * @param resource the resource
	 * @return the i marker[]
	 * @throws CoreException the core exception
	 */
	public static IMarker[] createSOAProblemMarker(Throwable thrown,
			IResource resource) throws CoreException {
		return createMarker(EclipseMessageUtils.createErrorStatus(thrown),
				resource, SOA_PROBLEM_MARKER_ID);
	}

	/**
	 * Creates the soa problem marker recursive.
	 *
	 * @param status the status
	 * @param project the project
	 * @return the i marker[]
	 * @throws CoreException the core exception
	 */
	public static IMarker[] createSOAProblemMarkerRecursive(IStatus status,
			IProject project) throws CoreException {
		ArrayList<IMarker> markersList = new ArrayList<IMarker>();
		if (status instanceof MultiStatus) {
			for (IStatus childStatus : ((MultiStatus) status).getChildren()) {
				createSOAProblemMarkerRecursive(childStatus, project);
			}
		} else if (status instanceof IResourceStatus) {
			IResource resource = project.getFile(((IResourceStatus) status)
					.getPath());
			markersList.addAll(Arrays.asList(createSOAProblemMarker(status,
					resource)));

		} else {
			markersList.addAll(Arrays.asList(createSOAProblemMarker(status,
					project)));

		}
		return markersList.toArray(new IMarker[0]);
	}

	/**
	 * Creates the soa problem marker.
	 *
	 * @param status the status
	 * @param resource the resource
	 * @return the i marker[]
	 * @throws CoreException the core exception
	 */
	public static IMarker[] createSOAProblemMarker(IStatus status,
			IResource resource) throws CoreException {
		return createMarker(status, resource, SOA_PROBLEM_MARKER_ID);
	}

	/**
	 * Creates the marker.
	 *
	 * @param status the status
	 * @param resource the resource
	 * @param type the type
	 * @return the i marker[]
	 * @throws CoreException the core exception
	 */
	public static IMarker[] createMarker(IStatus status, IResource resource,
			String type) throws CoreException {
		if (status == null || resource == null || status.isOK()) {
			return new IMarker[0];
		}
		final List<IMarker> result = new ArrayList<IMarker>();
		if (status.isMultiStatus()) {
			for (final IStatus stat : status.getChildren()) {
				IMarker marker = createSingleMarker(stat, resource, type);
				if (marker != null) {
					result.add(marker);
				}
				marker = createRootCauseMarker(stat, resource, type);
				if (marker != null) {
					result.add(marker);
				}
			}
		} else {
			IMarker marker = createSingleMarker(status, resource, type);
			if (marker != null) {
				result.add(marker);
			}
			marker = createRootCauseMarker(status, resource, type);
			if (marker != null) {
				result.add(marker);
			}
		}

		return result.toArray(new IMarker[0]);
	}

	/**
	 * Creates the root cause marker.
	 *
	 * @param status the status
	 * @param resource the resource
	 * @param type the type
	 * @return the i marker
	 * @throws CoreException the core exception
	 */
	private static IMarker createRootCauseMarker(IStatus status,
			IResource resource, String type) throws CoreException {
		Throwable cause = status.getException();
		if (cause == null || cause.getCause() == null) {
			return null;
		}

		while (cause.getCause() != null) {
			cause = cause.getCause();
		}
		IStatus rootStatus = EclipseMessageUtils
		.createStatus(status.getPlugin(), cause.getLocalizedMessage(),
				status.getSeverity());
		return createSingleMarker(rootStatus, resource, type);
	}

	/**
	 * Creates the single marker.
	 *
	 * @param status the status
	 * @param resource the resource
	 * @param type the type
	 * @return the i marker
	 * @throws CoreException the core exception
	 */
	private static IMarker createSingleMarker(IStatus status,
			IResource resource, String type) throws CoreException {
		IMarker result = null;
		if (status.isOK() == false && status.isMultiStatus() == false) {
			if (status instanceof SOAResourceStatus
					&& ((SOAResourceStatus) status).getResource() != null
					&& ((SOAResourceStatus) status).getResource()
					.isAccessible()) {
				result = ((SOAResourceStatus) status).getResource()
				.createMarker(type);
			} else {
				result = resource.createMarker(type);
			}

			result.setAttribute(IMarker.MESSAGE, status.getMessage());
			if (status instanceof IResourceStatus
					&& ((IResourceStatus) status).getPath() != null) {
				result.setAttribute(IMarker.LOCATION,
						((IResourceStatus) status).getPath().toString());
				result.setAttribute("path", resource.getLocation().toString());
			} else {
				result.setAttribute(IMarker.LOCATION, resource.getLocation()
						.toString());
				result.setAttribute("path", resource.getLocation().toString());
			}

			result.setAttribute(IMarker.SEVERITY, getMarkerSeverity(status));
		}
		return result;
	}

	/**
	 * Creates the wsdl marker.
	 *
	 * @param wsdlFile the wsdl file
	 * @param prefix the prefix
	 * @param problem the problem
	 * @param lineNumber the line number
	 * @throws CoreException the core exception
	 */
	public static void createWSDLMarker(IResource wsdlFile, String prefix,
			IStatus problem, int lineNumber) throws CoreException {
		if(wsdlFile == null){
			return;
		}
		IMarker marker = createSingleMarker(problem, wsdlFile,
				WSDL_PROBLEM_MARKER_ID);
		marker.setAttribute(IMarker.MESSAGE, prefix + ":"
				+ marker.getAttribute(IMarker.MESSAGE));
		if (lineNumber > -1) {
			marker.setAttribute(IMarker.LINE_NUMBER, lineNumber);
		}
	}

	/**
	 * Clean wsdl markers.
	 *
	 * @param wsdlFile the wsdl file
	 * @throws CoreException the core exception
	 */
	public static void cleanWSDLMarkers(IResource wsdlFile)
	throws CoreException {
		cleanMarkers(wsdlFile, WSDL_PROBLEM_MARKER_ID);
	}

}
