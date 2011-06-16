/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.exception.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

public class TestAbstractSOAException {
    
    private static final String OOPS_BUG = "Oops. Bug";
    MockSOAException mock = null;
    
    @Before
    public void setUp() throws Exception {
        mock = new MockSOAException();    
    }

    @Test
    public void testAbstractSOAException() {
        assertNotNull(mock);
    }

    @Test
    public void testGetMessage() {
        assertNotNull(mock.getMessage());
    }
    
    @Test
    public void testSpecificMessage() {
        MockSOAException mock = new MockSOAException(OOPS_BUG);
        assertEquals("Unexpected message:", OOPS_BUG, mock.getMessage());
    }
    
    @Test
    public void testThrowable() {
        MockSOAException mock = new MockSOAException(OOPS_BUG, new Throwable());
        String throwable = mock.getLocalizedMessage();
        assertNotNull("Missing throwable locallized message", throwable);
    }

}
