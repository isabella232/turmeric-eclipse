
package org.ebayopensource.turmeric.blogs.v1.services.service.gen;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
import javax.xml.ws.AsyncHandler;
import javax.xml.ws.Dispatch;
import javax.xml.ws.Response;
import org.ebayopensource.turmeric.blogs.v1.services.GetVersionRequest;
import org.ebayopensource.turmeric.blogs.v1.services.GetVersionResponse;
import org.ebayopensource.turmeric.blogs.v1.services.service.AsyncBlogsServiceV1;
import org.ebayopensource.turmeric.runtime.common.exceptions.ServiceInvocationException;
import org.ebayopensource.turmeric.runtime.sif.impl.internal.service.BaseServiceProxy;
import org.ebayopensource.turmeric.runtime.sif.service.Service;


/**
 * Note : Generated file, any changes will be lost upon regeneration.
 * 
 */
public class BlogsServiceV1Proxy
    extends BaseServiceProxy<AsyncBlogsServiceV1>
    implements AsyncBlogsServiceV1
{


    public BlogsServiceV1Proxy(Service service) {
        super(service);
    }

    public Future<?> getVersionAsync(GetVersionRequest param0, AsyncHandler<GetVersionResponse> param1) {
        Dispatch dispatch = m_service.createDispatch("getVersion");
        Future<?> result = dispatch.invokeAsync(param0, param1);
        return result;
    }

    public Response<GetVersionResponse> getVersionAsync(GetVersionRequest param0) {
        Dispatch dispatch = m_service.createDispatch("getVersion");
        Response<GetVersionResponse> result = dispatch.invokeAsync(param0);
        return result;
    }

    public List<Response<?>> poll(boolean block, boolean partial)
        throws InterruptedException
    {
        return m_service.poll(block, partial);
    }

    public GetVersionResponse getVersion(GetVersionRequest param0) {
        Object[] params = new Object[ 1 ] ;
        params[ 0 ] = param0;
        List<Object> returnParamList = new ArrayList<Object>();
        try {
            m_service.invoke("getVersion", params, returnParamList);
        } catch (ServiceInvocationException svcInvocationEx) {
            throw wrapInvocationException(svcInvocationEx);
        }
        GetVersionResponse result = ((GetVersionResponse) returnParamList.get(0));
        return result;
    }

}
