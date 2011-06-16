/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.exception.tests.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.ebayopensource.turmeric.eclipse.exception.core.SOANullParameterException;
import org.junit.Before;
import org.junit.Test;


public class TestSOANullParameterException {
    
    private static final String OOPS_BUG = "Oops. Bug";
    SOANullParameterException exception = null;
    
    @Before
    public void setUp() throws Exception {
        exception = new SOANullParameterException(0);    
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
    public void testParameterOrder() throws Exception {
        assertTrue("Parameter Order was not 0.", exception.getParamOrder() == 0);
    }
    
    @Test
    public void testChangeParamerterOrder() throws Exception {
        exception.setParamOrder(1);
        assertFalse("Paramater equals 0", exception.getParamOrder() == 0);
        assertEquals("Paramater does not equal 1", 1, exception.getParamOrder());
    }
    

}
