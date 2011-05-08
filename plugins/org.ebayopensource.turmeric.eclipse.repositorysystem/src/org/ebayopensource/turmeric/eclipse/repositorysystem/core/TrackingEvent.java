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
package org.ebayopensource.turmeric.eclipse.repositorysystem.core;

import java.util.Date;

/**
 * The Class TrackingEvent.
 *
 * @author yayu
 * @since 1.0.0
 */
public class TrackingEvent {
	private String featureName;
	private Date accessTime;
	private long duration = -1L;
	
	
	/** Tracking process involves wizards. */
	public static final int TRACKING_WIZARD = 0;
	
	/** Tracking process involves actions. */
	public static final int TRACKING_ACTION = 1;
	private int trackingType = TRACKING_WIZARD;
	
	
	/**
	 * Instantiates a new tracking event.
	 *
	 * @param featureName the feature name
	 * @param trackingType the tracking type
	 */
	public TrackingEvent(String featureName, int trackingType) {
		super();
		this.featureName = featureName;
		this.trackingType = trackingType;
	}

	/**
	 * Instantiates a new tracking event.
	 *
	 * @param featureName the feature name
	 * @param accessTime the access time
	 * @param duration the duration
	 */
	public TrackingEvent(String featureName, Date accessTime, long duration) {
		super();
		this.featureName = featureName;
		this.accessTime = accessTime;
		this.duration = duration;
	}
	
	/**
	 * Instantiates a new tracking event.
	 *
	 * @param featureName the feature name
	 * @param accessTime the access time
	 * @param duration the duration
	 * @param trackingType the tracking type
	 */
	public TrackingEvent(String featureName, Date accessTime, long duration,
			int trackingType) {
		super();
		this.featureName = featureName;
		this.accessTime = accessTime;
		this.duration = duration;
		this.trackingType = trackingType;
	}

	/**
	 * Gets the feature name.
	 *
	 * @return the feature name
	 */
	public String getFeatureName() {
		return featureName;
	}
	
	/**
	 * Sets the feature name.
	 *
	 * @param featureName the new feature name
	 */
	public void setFeatureName(String featureName) {
		this.featureName = featureName;
	}
	
	/**
	 * Gets the access time.
	 *
	 * @return the access time
	 */
	public Date getAccessTime() {
		return accessTime;
	}
	
	/**
	 * Sets the access time.
	 *
	 * @param accessTime the new access time
	 */
	public void setAccessTime(Date accessTime) {
		this.accessTime = accessTime;
	}
	
	/**
	 * Gets the duration.
	 *
	 * @return the duration
	 */
	public long getDuration() {
		return duration;
	}
	
	/**
	 * Sets the duration.
	 *
	 * @param duration the new duration
	 */
	public void setDuration(long duration) {
		this.duration = duration;
	}
	
	/**
	 * Gets the tracking type.
	 *
	 * @return the tracking type
	 */
	public int getTrackingType() {
		return trackingType;
	}
	
	/**
	 * Sets the tracking type.
	 *
	 * @param trackingType the new tracking type
	 */
	public void setTrackingType(int trackingType) {
		this.trackingType = trackingType;
	}
}
