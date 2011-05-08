/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.registry.consumer.servicegateway;

import org.ebayopensource.turmeric.eclipse.registry.consumer.exception.AssertionsServiceException;
import org.ebayopensource.turmeric.eclipse.registry.consumer.models.URLHolder;


/**
 * Gateway to the methods of the registry service through a consumer.
 *
 * @author ramurthy
 */

public class RegistryServiceGateway {
	
	private static AssertionsServiceConsumer fAssertionsServiceConsumer = null;
	
	/**
	 * Returns the handler for Assertions Service Consumer.
	 *
	 * @return the assertions service consumer
	 * @throws AssertionsServiceException the assertions service exception
	 */
	public static AssertionsServiceConsumer getAssertionsServiceConsumer() throws AssertionsServiceException {
		try {	
			fAssertionsServiceConsumer = AssertionsServiceConsumer.getInstance(URLHolder.getURLHolderInstance().getAssertionsServiceURL());
		} catch (Exception e) {
			throw new AssertionsServiceException(e);
		}
		return fAssertionsServiceConsumer;
	}

	/**
	 * Sets the consumer handlers and the respective service handler to null. 
	 */
	public static void invalidateConsumers()
	{
		fAssertionsServiceConsumer = null;
		AssertionsServiceConsumer.invalidateService();
	}
}
