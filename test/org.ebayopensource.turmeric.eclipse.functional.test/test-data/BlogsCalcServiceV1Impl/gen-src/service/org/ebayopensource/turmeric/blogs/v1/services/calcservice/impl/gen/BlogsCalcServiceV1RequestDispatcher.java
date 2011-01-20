
package org.ebayopensource.turmeric.blogs.v1.services.calcservice.impl.gen;

import org.ebayopensource.turmeric.blogs.v1.services.Inparams;
import org.ebayopensource.turmeric.blogs.v1.services.Response;
import org.ebayopensource.turmeric.blogs.v1.services.calcservice.BlogsCalcServiceV1;
import org.ebayopensource.turmeric.runtime.common.exceptions.ServiceException;
import org.ebayopensource.turmeric.runtime.common.pipeline.Message;
import org.ebayopensource.turmeric.runtime.common.pipeline.MessageContext;
import org.ebayopensource.turmeric.runtime.spf.impl.internal.pipeline.BaseServiceRequestDispatcher;


/**
 * Note : Generated file, any changes will be lost upon regeneration.
 * 
 */
public class BlogsCalcServiceV1RequestDispatcher
    extends BaseServiceRequestDispatcher<BlogsCalcServiceV1>
{


    public BlogsCalcServiceV1RequestDispatcher() {
        super(BlogsCalcServiceV1 .class);
        addSupportedOperation("add", new Class[] {Inparams.class }, new Class[] {Response.class });
    }

    public boolean dispatch(MessageContext param0, BlogsCalcServiceV1 param1)
        throws ServiceException
    {
        MessageContext msgCtx = param0;
        BlogsCalcServiceV1 service = param1;
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
