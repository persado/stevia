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

import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.IInvokedMethod;
import org.testng.IInvokedMethodListener2;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.Test;

import com.persado.oss.quality.stevia.annotations.Postconditions;
import com.persado.oss.quality.stevia.annotations.Preconditions;
import com.persado.oss.quality.stevia.annotations.AnnotationsHelper;
import com.persado.oss.quality.stevia.selenium.core.SteviaContext;

public class ConditionsListener extends ListenerCommon implements IInvokedMethodListener2 {

	static final Logger LOG = LoggerFactory.getLogger(ConditionsListener.class);
	
	public ConditionsListener() {
		
	}

	@Override
	public void beforeInvocation(IInvokedMethod method, ITestResult testResult) {
		
	}

	@Override
	public void afterInvocation(IInvokedMethod method, ITestResult testResult) {
		
	}
	@Override
	public void beforeInvocation(IInvokedMethod method, ITestResult testResult, ITestContext context) {
		int failed = findFailed(context);
		if (failed > 0) {
			LOG.error("Preconditions execution will not proceed. {} Configurations have failed",failed);
			return;
		}
		
		
		Method rmethod = method.getTestMethod().getConstructorOrMethod().getMethod();
		Object proxy = proxifyObject(method);
		
		
		if (rmethod.getAnnotation(Test.class) != null) {
			if (rmethod.getAnnotation(Preconditions.class) != null) {
				LOG.warn("Method or Class of {} wants preconditions to be checked", rmethod.getName());
				AnnotationsHelper p = SteviaContext.getSpringContext().getBean(AnnotationsHelper.class);
				try {
					LOG.debug("Mask and Execute Preconditions of method {} ",rmethod.getName());
					p.maskAndExecPreconditions(rmethod, proxy);
					LOG.debug("Mask and Execute Preconditions of method {} DONE",rmethod.getName());
				} catch (Throwable e) {
					Throwable realException = findExceptionWithMessage(e);
					LOG.error("Detected exception in preconditions execution, message = "+realException.getMessage(),e);
					if (realException instanceof RuntimeException ) {
						throw ((RuntimeException)realException);
					} else {
						throw new IllegalStateException(realException.getMessage(),realException);
					}
				} 
			}
		}
	}

	@Override
	public void afterInvocation(IInvokedMethod method, ITestResult testResult, ITestContext context) {
		int failed = findFailed(context);
		if (failed > 0) {
			LOG.error("Preconditions execution will not proceed. {} Configurations have failed",failed);
			return;
		}
		
		
		Method rmethod = method.getTestMethod().getConstructorOrMethod().getMethod();
		Object proxy = proxifyObject(method);

		if (rmethod.getAnnotation(Test.class) != null) {
			if (rmethod.getAnnotation(Postconditions.class) != null) {
				LOG.warn("Method or Class of {} wants postconditions to be checked", rmethod.getName());
				AnnotationsHelper p = SteviaContext.getSpringContext().getBean(AnnotationsHelper.class);
				try {
					LOG.debug("Mask and Execute Postconditions of method {} ",rmethod.getName());
					p.maskAndExecPostconditions(rmethod, proxy);
					LOG.debug("Mask and Execute Postconditions of method {} DONE",rmethod.getName());
				} catch (Throwable e) {
					Throwable realException = findExceptionWithMessage(e);
					LOG.error("Detected exception in postconditions execution, message = "+realException.getMessage(),e);
					if (realException instanceof RuntimeException ) {
						throw ((RuntimeException)realException);
					} else {
						throw new IllegalStateException(realException.getMessage(),realException);
					}
				} 
			}
		}
	}

}
