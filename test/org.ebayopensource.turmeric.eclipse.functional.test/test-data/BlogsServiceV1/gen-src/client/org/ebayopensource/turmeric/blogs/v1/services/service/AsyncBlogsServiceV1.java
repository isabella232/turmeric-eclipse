
package org.ebayopensource.turmeric.blogs.v1.services.service;

import java.util.List;
import java.util.concurrent.Future;
import javax.xml.ws.AsyncHandler;
import javax.xml.ws.Response;
import org.ebayopensource.turmeric.blogs.v1.services.GetVersionRequest;
import org.ebayopensource.turmeric.blogs.v1.services.GetVersionResponse;

public interface AsyncBlogsServiceV1
    extends BlogsServiceV1
{


    public Future<?> getVersionAsync(GetVersionRequest param0, AsyncHandler<GetVersionResponse> handler);

    public Response<GetVersionResponse> getVersionAsync(GetVersionRequest param0);

    public List<Response<?>> poll(boolean block, boolean partial)
        throws InterruptedException
    ;

}
