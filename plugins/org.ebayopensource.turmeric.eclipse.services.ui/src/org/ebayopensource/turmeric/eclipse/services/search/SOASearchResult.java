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
package org.ebayopensource.turmeric.eclipse.services.search;

import org.ebayopensource.turmeric.eclipse.services.ui.Activator;
import org.eclipse.core.resources.IFile;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.search.ui.ISearchQuery;
import org.eclipse.search.ui.text.AbstractTextSearchResult;
import org.eclipse.search.ui.text.IEditorMatchAdapter;
import org.eclipse.search.ui.text.IFileMatchAdapter;
import org.eclipse.search.ui.text.Match;
import org.eclipse.ui.plugin.AbstractUIPlugin;


/**
 * The Class SOASearchResult.
 *
 * @author yayu
 */
public class SOASearchResult extends AbstractTextSearchResult implements IFileMatchAdapter {
	private SOASearchQuery searchQuery;
	private static final Match[] NO_MATCHES= new Match[0];

	/**
	 * Instantiates a new sOA search result.
	 *
	 * @param searchQuery the search query
	 */
	public SOASearchResult(SOASearchQuery searchQuery) {
		super();
		this.searchQuery = searchQuery;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ImageDescriptor getImageDescriptor() {
		return AbstractUIPlugin.imageDescriptorFromPlugin(Activator.PLUGIN_ID, 
				"icons/script_obj.gif");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getLabel() {
		return toString();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ISearchQuery getQuery() {
		return searchQuery;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getTooltip() {
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		final StringBuffer result = new StringBuffer();
		result.append("'");
		result.append(searchQuery.toString());
		result.append("' - ");
		result.append(getMatchCount());
		result.append(" matches in ");
		result.append(searchQuery.isSearchBuildSystem() 
				? "build system." : "workspace.");
		return result.toString();
	}

	@Override
	public IEditorMatchAdapter getEditorMatchAdapter() {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.search.ui.text.AbstractTextSearchResult#getFileMatchAdapter()
	 */
	@Override
	public IFileMatchAdapter getFileMatchAdapter() {
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Match[] computeContainedMatches(AbstractTextSearchResult result,
			IFile file) {
		return NO_MATCHES;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IFile getFile(Object element) {
		return null;
	}
	
	/**
	 * The Class SOASearchResultService.
	 */
	public static class SOASearchResultService {
		private String serviceName;
		private String serviceLayer;
		private String serviceVersion;
		
		/**
		 * Instantiates a new sOA search result service.
		 *
		 * @param serviceName the service name
		 * @param serviceLayer the service layer
		 * @param serviceVersion the service version
		 */
		public SOASearchResultService(String serviceName, String serviceLayer,
				String serviceVersion) {
			super();
			this.serviceName = serviceName;
			this.serviceLayer = serviceLayer;
			this.serviceVersion = serviceVersion;
		}

		/**
		 * Gets the service name.
		 *
		 * @return the service name
		 */
		public String getServiceName() {
			return serviceName;
		}

		/**
		 * Sets the service name.
		 *
		 * @param serviceName the new service name
		 */
		public void setServiceName(String serviceName) {
			this.serviceName = serviceName;
		}

		/**
		 * Gets the service layer.
		 *
		 * @return the service layer
		 */
		public String getServiceLayer() {
			return serviceLayer;
		}

		/**
		 * Sets the service layer.
		 *
		 * @param serviceLayer the new service layer
		 */
		public void setServiceLayer(String serviceLayer) {
			this.serviceLayer = serviceLayer;
		}

		/**
		 * Gets the service version.
		 *
		 * @return the service version
		 */
		public String getServiceVersion() {
			return serviceVersion;
		}

		/**
		 * Sets the service version.
		 *
		 * @param serviceVersion the new service version
		 */
		public void setServiceVersion(String serviceVersion) {
			this.serviceVersion = serviceVersion;
		}
		
		/* (non-Javadoc)
		 * @see java.lang.Object#hashCode()
		 */
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result
					+ ((serviceLayer == null) ? 0 : serviceLayer.hashCode());
			result = prime * result
					+ ((serviceName == null) ? 0 : serviceName.hashCode());
			result = prime
					* result
					+ ((serviceVersion == null) ? 0 : serviceVersion.hashCode());
			return result;
		}

		/* (non-Javadoc)
		 * @see java.lang.Object#equals(java.lang.Object)
		 */
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			final SOASearchResultService other = (SOASearchResultService) obj;
			if (serviceLayer == null) {
				if (other.serviceLayer != null)
					return false;
			} else if (!serviceLayer.equals(other.serviceLayer))
				return false;
			if (serviceName == null) {
				if (other.serviceName != null)
					return false;
			} else if (!serviceName.equals(other.serviceName))
				return false;
			if (serviceVersion == null) {
				if (other.serviceVersion != null)
					return false;
			} else if (!serviceVersion.equals(other.serviceVersion))
				return false;
			return true;
		}

		/* (non-Javadoc)
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			StringBuffer buf = new StringBuffer();
			buf.append("Service Name: ");
			buf.append(serviceName);
			buf.append(", Service Layer: ");
			buf.append(serviceLayer);
			buf.append(", Service Version: ");
			buf.append(serviceVersion);
			return buf.toString();
		}
		
	}



}
