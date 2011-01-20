package com.ebayopensource.consumer;

import org.ebayopensource.turmeric.blogs.v1.services.GetNameResponse;
import org.ebayopensource.turmeric.blogs.v1.services.testsvc1.gen.SharedBlogsTestSvc1V1Consumer;
import org.ebayopensource.turmeric.runtime.common.exceptions.ServiceException;

public class TestConsumer extends SharedBlogsTestSvc1V1Consumer {

	public TestConsumer(String clientName) throws ServiceException {
		super(clientName);
		// TODO Auto-generated constructor stub
	}

	public TestConsumer(String clientName, String environment)
			throws ServiceException {
		super(clientName, environment);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param args
	 * @throws ServiceException 
	 */
	public static void main(String[] args) throws ServiceException {
		
		TestConsumer cns = new TestConsumer("BlogsTestSvc1V1Consumer");
		GetNameResponse res = cns.getName(null);
		System.out.println(res.getOut());

	}

}
