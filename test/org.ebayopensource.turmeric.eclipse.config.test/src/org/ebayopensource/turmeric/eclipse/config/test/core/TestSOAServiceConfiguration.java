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
package org.ebayopensource.turmeric.eclipse.config.test.core;

import junit.framework.Assert;

import org.ebayopensource.turmeric.eclipse.config.core.SOAServiceConfiguration;
import org.junit.Test;


/**
 * @author yayu
 *
 */
public class TestSOAServiceConfiguration {
	
	

	@Test
	public void testSOAServiceConfiguration() {
		String baseRequestTypeName = "BaseRequest";
		String baseResponseTypeName = "BaseResponse";
		String baseRequestTypeNameSpace = "http://www.ebay.com/marketplace/services";
		String baseResponseTypeNameSpace = baseRequestTypeNameSpace;
		String typesInWSDL = "{http://www.ebay.com/marketplace/services#ErrorMessage,http://www.ebay.com/marketplace/services#ErrorData,http://www.ebay.com/marketplace/services#BaseRequest,http://www.ebay.com/marketplace/services#BaseResponse}";
		String clientConfigGroup = "TestClientGroup";
		String serviceConfigGroup = "TestServiceGroup";
		String envMapperImpl = "org.ebayopensource.turmeric.test.EnvMapperImpl";
		SOAServiceConfiguration config = new SOAServiceConfiguration(baseRequestTypeName,
				baseResponseTypeName, baseRequestTypeNameSpace,
				baseResponseTypeNameSpace, typesInWSDL, 
				clientConfigGroup, serviceConfigGroup,
				envMapperImpl);
		Assert.assertEquals(baseRequestTypeName, config.getBaseRequestTypeName());
		Assert.assertEquals(baseResponseTypeName, config.getBaseResponseTypeName());
		Assert.assertEquals(baseRequestTypeNameSpace, config.getBaseRequestTypeNameSpace());
		Assert.assertEquals(baseResponseTypeNameSpace, config.getBaseResponseTypeNameSpace());
		Assert.assertEquals(typesInWSDL, config.getTypesInWSDL());
		Assert.assertEquals(clientConfigGroup, config.getClientConfigGroup());
		Assert.assertEquals(serviceConfigGroup, config.getServiceConfigGroup());
		Assert.assertEquals(envMapperImpl, config.getEnvMapperImpl());
	}

}
