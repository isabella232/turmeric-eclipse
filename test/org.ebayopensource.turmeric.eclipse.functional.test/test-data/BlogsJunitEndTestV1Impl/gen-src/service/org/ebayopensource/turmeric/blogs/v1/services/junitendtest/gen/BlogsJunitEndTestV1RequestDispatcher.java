/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/

package org.ebayopensource.turmeric.blogs.v1.services.blogsjunitendtestv1.gen;

import org.ebayopensource.turmeric.common.exceptions.ServiceException;
import org.ebayopensource.turmeric.common.pipeline.Message;
import org.ebayopensource.turmeric.common.pipeline.MessageContext;
import org.ebayopensource.turmeric.spf.impl.internal.pipeline.BaseServiceRequestDispatcher;
import org.ebayopensource.turmeric.blogs.v1.services.Inparams;
import org.ebayopensource.turmeric.blogs.v1.services.Response;


/**
 * Note : Generated file, any changes will be lost upon regeneration.
 * 
 */
public class BlogsJunitEndTestV1RequestDispatcher
    extends BaseServiceRequestDispatcher<org.ebayopensource.turmeric.blogs.v1.services.blogsjunitendtestv1.BlogsJunitEndTestV1>
{


    public BlogsJunitEndTestV1RequestDispatcher() {
        super(org.ebayopensource.turmeric.blogs.v1.services.blogsjunitendtestv1.BlogsJunitEndTestV1 .class);
        addSupportedOperation("add", new Class[] {Inparams.class }, new Class[] {Response.class });
    }

    public boolean dispatch(MessageContext param0, org.ebayopensource.turmeric.blogs.v1.services.blogsjunitendtestv1.BlogsJunitEndTestV1 param1)
        throws ServiceException
    {
        MessageContext msgCtx = param0;
        org.ebayopensource.turmeric.blogs.v1.services.blogsjunitendtestv1.BlogsJunitEndTestV1 service = param1;
        String operationName = msgCtx.getOperationName();
        Message requestMsg = msgCtx.getRequestMessage();
         
        if ("add".equals(operationName)) {
            Inparams param2 = ((Inparams) requestMsg.getParam(0));
            try {
                Message responseMsg = msgCtx.getResponseMessage();
                Response result = service.add(param2);
                responseMsg.setParam(0, result);
            } catch (Throwable th) {
                handleServiceException(msgCtx, th);
            }
            return true;
        }
        return false;
    }

}
