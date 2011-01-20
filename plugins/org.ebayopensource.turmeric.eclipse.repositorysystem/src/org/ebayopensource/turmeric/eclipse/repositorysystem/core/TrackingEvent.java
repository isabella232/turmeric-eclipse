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
 * @author yayu
 * @since 1.0.0
 */
public class TrackingEvent {
	private String featureName;
	private Date accessTime;
	private long duration = -1L;
	
	
	/**
	 * Tracking process involves wizards
	 */
	public static final int TRACKING_WIZARD = 0;
	/**
	 * Tracking process involves actions
	 */
	public static final int TRACKING_ACTION = 1;
	private int trackingType = TRACKING_WIZARD;
	
	
	public TrackingEvent(String featureName, int trackingType) {
		super();
		this.featureName = featureName;
		this.trackingType = trackingType;
	}

	public TrackingEvent(String featureName, Date accessTime, long duration) {
		super();
		this.featureName = featureName;
		this.accessTime = accessTime;
		this.duration = duration;
	}
	
	public TrackingEvent(String featureName, Date accessTime, long duration,
			int trackingType) {
		super();
		this.featureName = featureName;
		this.accessTime = accessTime;
		this.duration = duration;
		this.trackingType = trackingType;
	}

	public String getFeatureName() {
		return featureName;
	}
	public void setFeatureName(String featureName) {
		this.featureName = featureName;
	}
	public Date getAccessTime() {
		return accessTime;
	}
	public void setAccessTime(Date accessTime) {
		this.accessTime = accessTime;
	}
	public long getDuration() {
		return duration;
	}
	public void setDuration(long duration) {
		this.duration = duration;
	}
	public int getTrackingType() {
		return trackingType;
	}
	public void setTrackingType(int trackingType) {
		this.trackingType = trackingType;
	}
}
