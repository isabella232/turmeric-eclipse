/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.eclipse.exception.tests;

import org.ebayopensource.turmeric.eclipse.exception.AbstractSOAException;

public class MockSOAException extends AbstractSOAException {

    public MockSOAException() {
        super();
    }

    public MockSOAException(String message) {
        super(message);
    }

    public MockSOAException(Throwable cause) {
        super(cause);
    }

    public MockSOAException(String message, Throwable cause) {
        super(message, cause);
    }

}
