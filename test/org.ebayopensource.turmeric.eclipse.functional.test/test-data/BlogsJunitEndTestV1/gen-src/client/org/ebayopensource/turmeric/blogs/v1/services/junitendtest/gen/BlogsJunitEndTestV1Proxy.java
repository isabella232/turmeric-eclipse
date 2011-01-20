
package org.ebayopensource.turmeric.blogs.v1.services.junitendtest.gen;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
import javax.xml.ws.AsyncHandler;
import javax.xml.ws.Dispatch;
import org.ebayopensource.turmeric.blogs.v1.services.Inparams;
import org.ebayopensource.turmeric.blogs.v1.services.junitendtest.AsyncBlogsJunitEndTestV1;
import org.ebayopensource.turmeric.runtime.common.exceptions.ServiceInvocationException;
import org.ebayopensource.turmeric.runtime.sif.impl.internal.service.BaseServiceProxy;
import org.ebayopensource.turmeric.runtime.sif.service.Service;


/**
 * Note : Generated file, any changes will be lost upon regeneration.
 * 
 */
public class BlogsJunitEndTestV1Proxy
    extends BaseServiceProxy<AsyncBlogsJunitEndTestV1>
    implements AsyncBlogsJunitEndTestV1
{


    public BlogsJunitEndTestV1Proxy(Service service) {
        super(service);
    }

    public Future<?> addAsync(Inparams param0, AsyncHandler<org.ebayopensource.turmeric.blogs.v1.services.Response> param1) {
        Dispatch dispatch = m_service.createDispatch("add");
        Future<?> result = dispatch.invokeAsync(param0, param1);
        return result;
    }

    public javax.xml.ws.Response<org.ebayopensource.turmeric.blogs.v1.services.Response> addAsync(Inparams param0) {
        Dispatch dispatch = m_service.createDispatch("add");
        javax.xml.ws.Response<org.ebayopensource.turmeric.blogs.v1.services.Response> result = dispatch.invokeAsync(param0);
        return result;
    }

    public List<javax.xml.ws.Response<?>> poll(boolean block, boolean partial)
        throws InterruptedException
    {
        return m_service.poll(block, partial);
    }

    public org.ebayopensource.turmeric.blogs.v1.services.Response add(Inparams param0) {
        Object[] params = new Object[ 1 ] ;
        params[ 0 ] = param0;
        List<Object> returnParamList = new ArrayList<Object>();
        try {
            m_service.invoke("add", params, returnParamList);
        } catch (ServiceInvocationException svcInvocationEx) {
            throw wrapInvocationException(svcInvocationEx);
        }
        org.ebayopensource.turmeric.blogs.v1.services.Response result = ((org.ebayopensource.turmeric.blogs.v1.services.Response) returnParamList.get(0));
        return result;
    }

}
