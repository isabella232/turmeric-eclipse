
package ebayopensource.apis.eblbasecomponents.csapiinterfaceservice;

import java.util.List;
import java.util.concurrent.Future;
import javax.xml.ws.AsyncHandler;
import javax.xml.ws.Response;
import ebayopensource.apis.eblbasecomponents.CSUpdateMACActivityAddAttachmentsRequestType;
import ebayopensource.apis.eblbasecomponents.CSUpdateMACActivityAddAttachmentsResponseType;

public interface AsyncBlogsCSAPIInterfaceServiceV1
    extends BlogsCSAPIInterfaceServiceV1
{


    public Future<?> cSUpdateMACActivityAddAttachmentsAsync(CSUpdateMACActivityAddAttachmentsRequestType param0, AsyncHandler<CSUpdateMACActivityAddAttachmentsResponseType> handler);

    public Response<CSUpdateMACActivityAddAttachmentsResponseType> cSUpdateMACActivityAddAttachmentsAsync(CSUpdateMACActivityAddAttachmentsRequestType param0);

    public List<Response<?>> poll(boolean block, boolean partial)
        throws InterruptedException
    ;

}
