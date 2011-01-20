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

import org.junit.Before;
import org.junit.Test;

import org.ebayopensource.turmeric.eclipse.exception.resources.SOAActionExecutionFailedException;
import org.eclipse.core.runtime.*;


public class TestSOAActionExecutionFailedException {
    
    private static final String OOPS_BUG = "Oops. Bug";
    SOAActionExecutionFailedException exception = null;
    
    @Before
    public void setUp() throws Exception {
        exception = new SOAActionExecutionFailedException(new Throwable());
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
    public void testStatus() throws Exception {
       assertNotNull(exception.getStatus());
    }
    
    @Test
    public void testStatusMessage() throws Exception {
        exception = new SOAActionExecutionFailedException(OOPS_BUG, new Throwable());
        IStatus status = exception.getStatus();
        assertEquals("Unexpected Status message", OOPS_BUG, status.getMessage());
    }

}
