/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.exception.tests.resources;

import static org.junit.Assert.*;

import java.io.File;
import java.net.URL;

import org.ebayopensource.turmeric.eclipse.exception.resources.SOAFileNotWritableException;
import org.eclipse.core.runtime.Platform;
import org.eclipse.osgi.framework.internal.protocol.NullURLStreamHandlerService;
import org.junit.Before;
import org.junit.Test;
import org.osgi.framework.Bundle;




public class TestSOAFileNotWritableException {
    
    private static final String OOPS_BUG = "Oops. Bug";
    private SOAFileNotWritableException exception = null;
    
    @Before
    public void setUp() throws Exception {
        exception = new SOAFileNotWritableException(new Throwable());
    }

    @Test
    public void testAbstractSOAException() throws Exception {
        assertNotNull(exception);
    }

    @Test
    public void testGetMessage() throws Exception {
        assertNotNull(exception);
    }
    
    @Test
    public void testSpecificMessage() throws Exception {
        Bundle bundle =  Platform.getBundle("org.ebayopensource.turmeric.eclipse.exception.tests");
        URL url = bundle.getEntry("/META-INF/MANIFEST.MF");
        
        exception = new SOAFileNotWritableException(OOPS_BUG, new File(url.toExternalForm()));
        String message = exception.getMessage();
        assertTrue("Could not find expected message.", message.contains(OOPS_BUG));
    }

}
