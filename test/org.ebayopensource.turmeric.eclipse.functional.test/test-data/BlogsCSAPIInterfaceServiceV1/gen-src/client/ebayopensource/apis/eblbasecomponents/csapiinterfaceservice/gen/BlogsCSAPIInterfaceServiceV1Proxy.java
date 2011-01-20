
package ebayopensource.apis.eblbasecomponents.csapiinterfaceservice.gen;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
import javax.xml.ws.AsyncHandler;
import javax.xml.ws.Dispatch;
import javax.xml.ws.Response;
import ebayopensource.apis.eblbasecomponents.CSUpdateMACActivityAddAttachmentsRequestType;
import ebayopensource.apis.eblbasecomponents.CSUpdateMACActivityAddAttachmentsResponseType;
import ebayopensource.apis.eblbasecomponents.csapiinterfaceservice.AsyncBlogsCSAPIInterfaceServiceV1;
import org.ebayopensource.turmeric.runtime.common.exceptions.ServiceInvocationException;
import org.ebayopensource.turmeric.runtime.sif.impl.internal.service.BaseServiceProxy;
import org.ebayopensource.turmeric.runtime.sif.service.Service;


/**
 * Note : Generated file, any changes will be lost upon regeneration.
 * 
 */
public class BlogsCSAPIInterfaceServiceV1Proxy
    extends BaseServiceProxy<AsyncBlogsCSAPIInterfaceServiceV1>
    implements AsyncBlogsCSAPIInterfaceServiceV1
{


    public BlogsCSAPIInterfaceServiceV1Proxy(Service service) {
        super(service);
    }

    public Future<?> cSUpdateMACActivityAddAttachmentsAsync(CSUpdateMACActivityAddAttachmentsRequestType param0, AsyncHandler<CSUpdateMACActivityAddAttachmentsResponseType> param1) {
        Dispatch dispatch = m_service.createDispatch("CSUpdateMACActivityAddAttachments");
        Future<?> result = dispatch.invokeAsync(param0, param1);
        return result;
    }

    public Response<CSUpdateMACActivityAddAttachmentsResponseType> cSUpdateMACActivityAddAttachmentsAsync(CSUpdateMACActivityAddAttachmentsRequestType param0) {
        Dispatch dispatch = m_service.createDispatch("CSUpdateMACActivityAddAttachments");
        Response<CSUpdateMACActivityAddAttachmentsResponseType> result = dispatch.invokeAsync(param0);
        return result;
    }

    public List<Response<?>> poll(boolean block, boolean partial)
        throws InterruptedException
    {
        return m_service.poll(block, partial);
    }

    public CSUpdateMACActivityAddAttachmentsResponseType cSUpdateMACActivityAddAttachments(CSUpdateMACActivityAddAttachmentsRequestType param0) {
        Object[] params = new Object[ 1 ] ;
        params[ 0 ] = param0;
        List<Object> returnParamList = new ArrayList<Object>();
        try {
            m_service.invoke("CSUpdateMACActivityAddAttachments", params, returnParamList);
        } catch (ServiceInvocationException svcInvocationEx) {
            throw wrapInvocationException(svcInvocationEx);
        }
        CSUpdateMACActivityAddAttachmentsResponseType result = ((CSUpdateMACActivityAddAttachmentsResponseType) returnParamList.get(0));
        return result;
    }

}
