
package org.ebayopensource.turmeric.blogs.v1.services.service.test;

import junit.framework.TestCase;
import org.ebayopensource.turmeric.blogs.v1.services.GetVersionResponse;
import org.ebayopensource.turmeric.blogs.v1.services.service.BlogsServiceV1;
import org.ebayopensource.turmeric.runtime.common.exceptions.ServiceException;
import org.ebayopensource.turmeric.runtime.sif.service.Service;
import org.ebayopensource.turmeric.runtime.sif.service.ServiceFactory;

public class BlogsServiceV1Test
    extends TestCase
{

    private BlogsServiceV1 m_proxy = null;

    public BlogsServiceV1Test(String testcaseName) {
        super(testcaseName);
    }

    private BlogsServiceV1 getProxy()
        throws ServiceException
    {
        if (m_proxy == null) {
            String svcAdminName = "BlogsServiceV1";
            String envName = "production";
            String clientName = "BlogsServiceV1_Test";
            Service service = ServiceFactory.create(svcAdminName, envName, clientName, null);
            m_proxy = service.getProxy();
        }
        return m_proxy;
    }

    public void testGetVersion()
        throws Exception
    {
        GetVersionResponse result = null;
        // TODO: REPLACE PARAMETER(S) WITH ACTUAL VALUE(S)
        result = getProxy().getVersion(null);
        if (result == null) {
            throw new Exception("Response is Null");
        }
        // TODO: FIX FOLLOWING ASSERT STATEMENT
        assertTrue(false);
    }

}
