
package org.ebayopensource.turmeric.blogs.v1.services.testsvc1.impl;

import org.ebayopensource.turmeric.blogs.v1.services.GetName;
import org.ebayopensource.turmeric.blogs.v1.services.GetNameResponse;
import org.ebayopensource.turmeric.blogs.v1.services.GetVersionRequest;
import org.ebayopensource.turmeric.blogs.v1.services.GetVersionResponse;
import org.ebayopensource.turmeric.blogs.v1.services.testsvc1.BlogsTestSvc1V1;

public class BlogsTestSvc1V1Impl
    implements BlogsTestSvc1V1
{


    public GetVersionResponse getVersion(GetVersionRequest param0) {
        return null;
    }

	@Override
	public GetNameResponse getName(GetName getName) {
		// TODO Auto-generated method stub
		
		GetNameResponse res = new GetNameResponse();
		res.setOut("default");
		
		return res;
	}

}
