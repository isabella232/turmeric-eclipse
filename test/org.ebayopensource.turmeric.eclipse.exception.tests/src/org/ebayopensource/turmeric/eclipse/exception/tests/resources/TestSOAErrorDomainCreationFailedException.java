/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.exception.tests.resources;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.ebayopensource.turmeric.eclipse.exception.resources.SOAErrorDomainCreationFailedException;
import org.junit.Before;
import org.junit.Test;



public class TestSOAErrorDomainCreationFailedException {
    
    private static final String OOPS_BUG = "Oops. Bug";
    SOAErrorDomainCreationFailedException exception = null;
    
    @Before
    public void setUp() throws Exception {
        exception = new SOAErrorDomainCreationFailedException(new Throwable());
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
        SOAErrorDomainCreationFailedException exception = new SOAErrorDomainCreationFailedException(OOPS_BUG, new Throwable());
        String message = exception.getMessage();
        assertTrue("Unexpected Message", message.contains(OOPS_BUG));
    }

}
