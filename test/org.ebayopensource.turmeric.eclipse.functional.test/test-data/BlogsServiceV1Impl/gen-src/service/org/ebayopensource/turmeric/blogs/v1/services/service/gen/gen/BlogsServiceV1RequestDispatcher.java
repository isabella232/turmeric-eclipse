
package org.ebayopensource.turmeric.blogs.v1.services.service.gen.gen;

import org.ebayopensource.turmeric.blogs.v1.services.GetVersionRequest;
import org.ebayopensource.turmeric.blogs.v1.services.GetVersionResponse;
import org.ebayopensource.turmeric.blogs.v1.services.service.BlogsServiceV1;
import org.ebayopensource.turmeric.runtime.common.exceptions.ServiceException;
import org.ebayopensource.turmeric.runtime.common.pipeline.Message;
import org.ebayopensource.turmeric.runtime.common.pipeline.MessageContext;
import org.ebayopensource.turmeric.runtime.spf.impl.internal.pipeline.BaseServiceRequestDispatcher;


/**
 * Note : Generated file, any changes will be lost upon regeneration.
 * 
 */
public class BlogsServiceV1RequestDispatcher
    extends BaseServiceRequestDispatcher<BlogsServiceV1>
{


    public BlogsServiceV1RequestDispatcher() {
        super(BlogsServiceV1 .class);
        addSupportedOperation("getVersion", new Class[] {GetVersionRequest.class }, new Class[] {GetVersionResponse.class });
    }

    public boolean dispatch(MessageContext param0, BlogsServiceV1 param1)
        throws ServiceException
    {
        MessageContext msgCtx = param0;
        BlogsServiceV1 service = param1;
        String operationName = msgCtx.getOperationName();
        Message requestMsg = msgCtx.getRequestMessage();
         
        if ("getVersion".equals(operationName)) {
            GetVersionRequest param2 = ((GetVersionRequest) requestMsg.getParam(0));
            try {
                Message responseMsg = msgCtx.getResponseMessage();
                GetVersionResponse result = service.getVersion(param2);
                responseMsg.setParam(0, result);
            } catch (Throwable th) {
                handleServiceException(msgCtx, th);
            }
            return true;
        }
        return false;
    }

}
