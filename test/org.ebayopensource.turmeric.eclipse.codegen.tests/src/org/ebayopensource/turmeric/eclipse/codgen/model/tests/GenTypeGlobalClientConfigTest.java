/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.codgen.model.tests;


import static org.junit.Assert.*;

import org.ebayopensource.turmeric.eclipse.codegen.model.GenTypeGlobalClientConfig;
import org.junit.Test;


public class GenTypeGlobalClientConfigTest {

	@Test
	public void testGenTypeGlobalClientConfig() throws Exception {
		GenTypeGlobalClientConfig model = new GenTypeGlobalClientConfig("namespace", "serviceLayerFile", "serviceInterface", "serviceName", "1.0", "serviceImpl", "Service", "serviceLayer", "src", "destination", "outputdir","metaDataDir");
		assertNotNull(model.getNamespace());
		assertNotNull(model.getServiceImplClassName());
		assertNotNull(model.getOutputDirectory());
		assertNotNull(model.getMetadataDirectory());
		assertNotNull(model.getProjectRoot());
		assertNotNull(model.getSourceDirectory());
		assertNotNull(model.getServiceInterface());
		assertNotNull(model.getDestination());
		assertNotNull(model.getServiceLayerFile());
	}

}
