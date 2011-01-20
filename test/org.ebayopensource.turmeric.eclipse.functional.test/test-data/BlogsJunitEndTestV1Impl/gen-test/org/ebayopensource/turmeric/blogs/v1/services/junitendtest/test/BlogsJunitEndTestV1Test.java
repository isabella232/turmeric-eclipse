
package org.ebayopensource.turmeric.blogs.v1.services.junitendtest.test;

import junit.framework.TestCase;
import org.ebayopensource.turmeric.blogs.v1.services.Response;
import org.ebayopensource.turmeric.blogs.v1.services.junitendtest.BlogsJunitEndTestV1;
import org.ebayopensource.turmeric.runtime.common.exceptions.ServiceException;
import org.ebayopensource.turmeric.runtime.sif.service.Service;
import org.ebayopensource.turmeric.runtime.sif.service.ServiceFactory;

public class BlogsJunitEndTestV1Test
    extends TestCase
{

    private BlogsJunitEndTestV1 m_proxy = null;

    public BlogsJunitEndTestV1Test(String testcaseName) {
        super(testcaseName);
    }

    private BlogsJunitEndTestV1 getProxy()
        throws ServiceException
    {
        if (m_proxy == null) {
            String svcAdminName = "BlogsJunitEndTestV1";
            String envName = "production";
            String clientName = "BlogsJunitEndTestV1_Test";
            Service service = ServiceFactory.create(svcAdminName, envName, clientName, null);
            m_proxy = service.getProxy();
        }
        return m_proxy;
    }

    public void testAdd()
        throws Exception
    {
        Response result = null;
        // TODO: REPLACE PARAMETER(S) WITH ACTUAL VALUE(S)
        result = getProxy().add(null);
        if (result == null) {
            throw new Exception("Response is Null");
        }
        // TODO: FIX FOLLOWING ASSERT STATEMENT
        assertTrue(false);
    }

}
