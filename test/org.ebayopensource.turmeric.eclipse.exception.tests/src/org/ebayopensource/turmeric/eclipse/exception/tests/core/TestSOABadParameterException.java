/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.exception.tests.core;

import static org.junit.Assert.*;

import org.ebayopensource.turmeric.eclipse.exception.core.SOABadParameterException;
import org.junit.Before;
import org.junit.Test;


public class TestSOABadParameterException {
    
    private static final String OOPS_BUG = "Oops. Bug";
    SOABadParameterException exception = null;
    
    @Before
    public void setUp() throws Exception {
        exception = new SOABadParameterException();    
    }

    @Test
    public void testAbstractSOAException() throws Exception {
        assertNotNull(exception);
    }

    @Test
    public void testGetMessage() throws Exception {
        assertNotNull(exception.getMessage());
    }
    
    @Test
    public void testSpecificMessage() throws Exception {
        SOABadParameterException mock = new SOABadParameterException(OOPS_BUG);
        assertEquals("Unexpected message:", OOPS_BUG, mock.getMessage());
    }
    
    @Test
    public void testThrowable() throws Exception {
        SOABadParameterException mock = new SOABadParameterException(OOPS_BUG, new Throwable());
        String throwable = mock.getLocalizedMessage();
        assertNotNull("Missing throwable locallized message", throwable);
    }

}
