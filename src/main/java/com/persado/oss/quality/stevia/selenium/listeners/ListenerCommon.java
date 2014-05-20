package com.persado.oss.quality.stevia.selenium.listeners;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.IInvokedMethod;
import org.testng.IResultMap;
import org.testng.ITestContext;

import com.persado.oss.quality.stevia.annotations.ProxyCallback;
import com.persado.oss.quality.stevia.annotations.ProxyHelper;
import com.persado.oss.quality.stevia.selenium.core.SteviaContext;

public class ListenerCommon {
	
	private static final Logger LOG = LoggerFactory.getLogger(ListenerCommon.class);

	public ListenerCommon() {
		super();
	}

	protected int findFailed(ITestContext context) {
		IResultMap failedConfigurations = context.getFailedConfigurations();
		IResultMap skippedConfigurations = context.getSkippedConfigurations();
		
		int countfailed = 0;
		if (failedConfigurations!= null) {
			countfailed += failedConfigurations.size();
		}
		if (skippedConfigurations != null) {
			countfailed += skippedConfigurations.size();
		}
		return countfailed;
	}

	/**
	 * from the method, try to proxify the object
	 * @param method
	 * @return
	 */
	protected Object proxifyObject(IInvokedMethod method) {
		Object instance = method.getTestMethod().getInstance();
		Object proxy = null;
	
		try {
			proxy = ProxyHelper.create(instance, null, new ProxyCallback() {
				@Override
				public void execute() {
					try {
						SteviaContext.getWebController().takeScreenShot();
					} catch (IOException e) {
						LOG.error("Screenshot failed! reason = "+e.getMessage());
					}
					
				}
			});	
		} catch (Exception e) {
			LOG.error("Proxy creation failed, using non-proxyed original object, reason = "+e.getMessage());
			proxy = instance;
		}
		return proxy;
	}

	protected Throwable findExceptionWithMessage(Throwable e) {
		if (null != e && e.getMessage() == null && e.getCause() != null) {
			return findExceptionWithMessage(e.getCause());
		}
		
		return e;
		
	}

}