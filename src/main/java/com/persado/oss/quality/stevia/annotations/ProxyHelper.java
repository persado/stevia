package com.persado.oss.quality.stevia.annotations;

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

import javassist.util.proxy.MethodHandler;
import javassist.util.proxy.ProxyFactory;
import javassist.util.proxy.ProxyObject;

public class ProxyHelper {

	@SuppressWarnings("rawtypes")
	public static <T> T create(final Object obj, final ProxyCallback onSuccess, final ProxyCallback onException) throws Exception {
		ProxyFactory factory = new ProxyFactory();
		Class<T> origClass = (Class<T>) obj.getClass();
		factory.setSuperclass(origClass);
		Class clazz = factory.createClass();
		MethodHandler handler = new MethodHandler() {

			@Override
			public Object invoke(Object self, Method overridden, Method forwarder,
					Object[] args) throws Throwable {
				try {
					Object returnVal = overridden.invoke(obj, args);
					if (null != onSuccess) onSuccess.execute();
					return returnVal;
				} catch (Throwable tr) {
					if (null != onException) onException.execute();
					throw tr;
				}
			}
		};
		Object instance = clazz.newInstance();
		((ProxyObject) instance).setHandler(handler);
		return (T) instance;
	}
	
	public static void main(String[] args) throws Exception {
		
		Foo f = new Foo();
		
		Foo fProxy = ProxyHelper.create(f, new ProxyCallback() {

			@Override
			public void execute() {
				System.out.println("proxyfied on success");
				
			}
			
		}, null);
		System.out.println("original object: ") ;
		f.testMethod();
		System.out.println("proxified object: ") ;
		fProxy.testMethod();
	}
	
	
}

class Foo {
	int counter = 0;
	public void testMethod() {
		System.out.println("Helloworld = "+ (++counter));
	}
}
