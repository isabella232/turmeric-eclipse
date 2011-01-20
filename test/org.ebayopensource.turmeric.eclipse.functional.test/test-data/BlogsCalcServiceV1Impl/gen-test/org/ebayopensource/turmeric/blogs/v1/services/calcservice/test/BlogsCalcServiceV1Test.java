
package org.ebayopensource.turmeric.blogs.v1.services.calcservice.test;

import junit.framework.TestCase;
import org.ebayopensource.turmeric.blogs.v1.services.Response;
import org.ebayopensource.turmeric.blogs.v1.services.calcservice.BlogsCalcServiceV1;
import org.ebayopensource.turmeric.runtime.common.exceptions.ServiceException;
import org.ebayopensource.turmeric.runtime.sif.service.Service;
import org.ebayopensource.turmeric.runtime.sif.service.ServiceFactory;

public class BlogsCalcServiceV1Test
    extends TestCase
{

    private BlogsCalcServiceV1 m_proxy = null;

    public BlogsCalcServiceV1Test(String testcaseName) {
        super(testcaseName);
    }

    private BlogsCalcServiceV1 getProxy()
        throws ServiceException
    {
        if (m_proxy == null) {
            String svcAdminName = "BlogsCalcServiceV1";
            String envName = "production";
            String clientName = "BlogsCalcServiceV1_Test";
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
