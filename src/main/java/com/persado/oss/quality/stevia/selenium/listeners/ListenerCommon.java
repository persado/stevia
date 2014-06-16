package com.persado.oss.quality.stevia.selenium.listeners;

/*
 * #%L
 * Stevia QA Framework - Core
 * %%
 * Copyright (C) 2013 - 2014 Persado Intellectual Property Limited
 * %%
 * Copyright (c) Persado Intellectual Property Limited. All rights reserved.
 *  
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *  
 * * Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.
 *  
 * * Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 *  
 * * Neither the name of the Persado Intellectual Property Limited nor the names
 * of its contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 *  
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 * #L%
 */

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